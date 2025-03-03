package com.caixy.adminSystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caixy.adminSystem.common.ErrorCode;
import com.caixy.adminSystem.exception.BusinessException;
import com.caixy.adminSystem.exception.ThrowUtils;
import com.caixy.adminSystem.mapper.StudentCourseSelectionMapper;
import com.caixy.adminSystem.mapper.StudentInfoMapper;
import com.caixy.adminSystem.model.dto.studentInfo.StudentInfoAddRequest;
import com.caixy.adminSystem.model.dto.studentInfo.StudentInfoQueryRequest;
import com.caixy.adminSystem.model.entity.*;
import com.caixy.adminSystem.model.enums.UserRoleEnum;
import com.caixy.adminSystem.model.enums.UserSexEnum;
import com.caixy.adminSystem.model.vo.StudentInfo.StudentInfoVO;
import com.caixy.adminSystem.model.vo.studentCourseSelection.CourseStudentInfoVO;
import com.caixy.adminSystem.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author CAIXYPROMISE
 * @description 针对表【student_score】的数据库操作Service实现
 * @createDate 2024-04-02 22:30:06
 */
@Service
public class StudentInfoServiceImpl extends ServiceImpl<StudentInfoMapper, StudentInfo>
        implements StudentInfoService
{
    @Resource
    private MajorInfoService majorInfoService;
    @Resource
    private ClassesInfoService classesInfoService;
    @Resource
    private DepartmentInfoService departmentInfoService;
    @Resource
    private UserService userService;
    @Resource
    private StudentCourseSelectionMapper studentCourseSelectionMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addStudentInfo(StudentInfoAddRequest postAddRequest, HttpServletRequest request, User loginUser)
    {
        StudentInfo post = new StudentInfo();
        BeanUtils.copyProperties(postAddRequest, post);
        // 参数校验
        validStudentInfo(post, true);
        post.setCreatorId(loginUser.getId());
        boolean result = this.save(post);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newStudentInfoId = post.getId();
        User newUser = User.builder()
                           .userAccount(post.getStuId())
                           .userPassword(post.getStuId())
                           .id(newStudentInfoId)
                           .userDepartment(post.getStuDeptId())
                           .userMajor(post.getStuMajorId())
                           .userClass(post.getStuClassId())
                           .userRoleLevel(0)
                           .userRole(UserRoleEnum.STUDENT.getValue())
                           .userName(post.getStuName())
                           .userSex(post.getStuSex())
                           .build();
        long register = userService.makeRegister(newUser);
        ThrowUtils.throwIf(register < 0, ErrorCode.OPERATION_ERROR);
        return newStudentInfoId;
    }

    /**
     * 删除学生
     * 
     * @author CAIXYPROMISE
     * @version 1.0
     * @version 2025/1/7 0:04
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteStudent(Long stuId)
    {
        StudentInfo studentInfo = getById(stuId);
        if (studentInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "学生不存在");
        }
        User userByAccount = userService.findUserByAccount(studentInfo.getStuId());
        if (userByAccount == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "学生不存在");
        }
        boolean removeStu = this.removeById(stuId);
        boolean removeAccount = userService.removeById(userByAccount.getId());
        return removeAccount && removeStu;
    }


    @Override
    public void validStudentInfo(StudentInfo post, boolean add)
    {
        UserSexEnum userSexEnum = UserSexEnum.getEnumByCode(post.getStuSex());
        if (userSexEnum == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "性别不合法");
        }
        boolean majorExistById = majorInfoService.majorExistById(post.getStuMajorId(), post.getStuDeptId());
        if (!majorExistById)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "专业不存在");
        }
        if (add)
        {
            String stuId = post.getStuId();
            if (StringUtils.isBlank(stuId) || stuId.length() < 8)
            {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "学号不合法");
            }
            LambdaQueryWrapper<StudentInfo> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(StudentInfo::getStuId, stuId);
            if (this.count(queryWrapper) > 0)
            {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "学号已存在");
            }
        }
    }

    @Override
    public LambdaQueryWrapper<StudentInfo> getQueryWrapper(StudentInfoQueryRequest request)
    {
        LambdaQueryWrapper<StudentInfo> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(request.getId() != null, StudentInfo::getId, request.getId());
        queryWrapper.eq(StringUtils.isNotBlank(request.getStudentId()), StudentInfo::getStuId, request.getStudentId());
        queryWrapper.like(StringUtils.isNotBlank(request.getStuName()), StudentInfo::getStuName, request.getStuName());
        queryWrapper.eq(request.getStuSex() != null, StudentInfo::getStuSex, request.getStuSex());

        // 处理  相关查询
        if (request.getStuDepart() != null) {
            queryWrapper.eq(request.getStuDepart().getDepartId() != null, StudentInfo::getStuDeptId, request.getStuDepart().getDepartId());
            queryWrapper.eq(request.getStuDepart().getMajorId() != null, StudentInfo::getStuMajorId, request.getStuDepart().getMajorId());
            queryWrapper.eq(request.getStuDepart().getClassId() != null, StudentInfo::getStuClassId, request.getStuDepart().getClassId());
        }

        return queryWrapper;
    }

    @Override
    public Page<StudentInfo> searchFromEs(StudentInfoQueryRequest postQueryRequest)
    {
        return null;
    }

    @Override
    public StudentInfoVO getStudentInfoVO(StudentInfo post)
    {
        StudentInfoVO studentInfoVO = new StudentInfoVO();
        BeanUtils.copyProperties(post, studentInfoVO);

        // 获取专业信息
        DepartmentInfo departmentInfoById = departmentInfoService.getById(post.getStuDeptId());
        MajorInfo majorInfoById = majorInfoService.getById(post.getStuMajorId());
        ClassesInfo classesInfoById = classesInfoService.getById(post.getStuClassId());
        studentInfoVO.setStuDepart(departmentInfoById.getName());
        studentInfoVO.setStuMajor(majorInfoById.getName());
        studentInfoVO.setStuClass(classesInfoById.getName());

        return studentInfoVO;
    }

    /**
     * 批量根据学生id获取vo
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/5/7 下午5:30
     */
    @Override
    public List<StudentInfoVO> getStudentInfoVoByIds(Collection<Long> studentIds)
    {
        if (studentIds == null || studentIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<StudentInfo> studentInfoList = this.listByIds(studentIds);
        return this.getStudentInfoVOList(studentInfoList);
    }

    /**
     * 根据学生id获取VO
     */
    @Override
    public StudentInfoVO getStudentInfoVOById(Long id)
    {
        StudentInfo studentInfo = this.getById(id);
        if (studentInfo == null)
        {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "学生信息不存在");
        }
        return getStudentInfoVO(studentInfo);
    }

    /**
     * 查询指定班级下的所有学生。
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @version 2025/2/6 20:54
     */
    @Override
    public List<StudentInfoVO> getStudentByClassesIds(List<Long> classesIds) {
        if (classesIds == null || classesIds.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<StudentInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(StudentInfo::getStuClassId, classesIds);
        List<StudentInfo> studentInfos = this.list(queryWrapper);
        return this.getStudentInfoVOList(studentInfos);
    }

    /**
     * 根据选课任务ID和科目ID查询选了该科目的所有学生信息VO
     *
     * @param courseSelectionId 选课任务ID
     * @param subjectId 科目ID
     * @return 学生信息VO列表
     */
    @Override
    public List<CourseStudentInfoVO> getStudentsByCourseSelectionAndSubject(Long courseSelectionId, Long subjectId) {
        // 构建查询条件，查找符合条件的学生选课记录
        LambdaQueryWrapper<StudentCourseSelection> query = new LambdaQueryWrapper<>();
        query.eq(StudentCourseSelection::getCourseSelectionId, courseSelectionId)
             .eq(StudentCourseSelection::getSubjectId, subjectId)
             .eq(StudentCourseSelection::getIsDelete, 0);

        // 查询学生选课记录列表
        List<StudentCourseSelection> selections = studentCourseSelectionMapper.selectList(query);
        if (selections == null || selections.isEmpty()) {
            return Collections.emptyList();
        }
        Map<Long, StudentCourseSelection> studentIdToSelectInfoMap = new HashMap<>();
        Set<Long> studentIds = new HashSet<>();
        // 提取所有学生ID
        selections.forEach(item -> {
            studentIdToSelectInfoMap.put(item.getStudentId(), item);
            studentIds.add(item.getStudentId());
        });
        // 使用 studentInfoService 批量获取学生信息VO
        List<StudentInfoVO> studentInfoVoByIds = getStudentInfoVoByIds(studentIds);
        Map<Long, StudentInfoVO> collect = studentInfoVoByIds.stream().collect(Collectors.toMap(StudentInfoVO::getStudentId, Function.identity()));
        return studentInfoVoByIds.stream().map(item -> {
            CourseStudentInfoVO vo = new CourseStudentInfoVO();
            // 拷贝学生信息
            BeanUtils.copyProperties(collect.get(item.getStudentId()), vo);
            // 拷贝选课信息
            BeanUtils.copyProperties(studentIdToSelectInfoMap.get(item.getStudentId()), vo);
            return vo;
        }).collect(Collectors.toList());
    }


    @Override
    public Page<StudentInfoVO> getStudentInfoVOPage(Page<StudentInfo> postPage)
    {
        Page<StudentInfoVO> postVOPage = new Page<>(postPage.getCurrent(), postPage.getSize());
        List<StudentInfoVO> studentInfoVOS = this.getStudentInfoVOList(postPage.getRecords());
        postVOPage.setRecords(studentInfoVOS);
        postVOPage.setTotal(postPage.getTotal());
        return postVOPage;
    }

    /**
     * 批量根据id获取学生信息
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/5/5 下午10:26
     */
    @Override
    public List<StudentInfo> batchListStudentInfoByIds(List<Long> stuIds)
    {
        return this.listByIds(stuIds);
    }


    private List<StudentInfoVO> getStudentInfoVOList(List<StudentInfo> studentInfoList)
    {
        if (studentInfoList == null || studentInfoList.isEmpty()) {
            return Collections.emptyList();
        }
        Set<Long> departIds = new HashSet<>();
        Set<Long> majorIds = new HashSet<>();
        Set<Long> classIds = new HashSet<>();
        studentInfoList.forEach(item ->
        {
            departIds.add(item.getStuDeptId());
            majorIds.add(item.getStuMajorId());
            classIds.add(item.getStuClassId());
        });

        Map<Long, String> departMap = departmentInfoService.listByIds(departIds).stream()
                                                           .collect(Collectors.toMap(DepartmentInfo::getId,
                                                                   DepartmentInfo::getName));
        Map<Long, String> majorMap = majorInfoService.listByIds(majorIds).stream()
                                                     .collect(Collectors.toMap(MajorInfo::getId, MajorInfo::getName));
        Map<Long, String> classMap = classesInfoService.listByIds(classIds).stream()
                                                       .collect(Collectors.toMap(ClassesInfo::getId,
                                                               ClassesInfo::getName));

        return studentInfoList.stream().map(item ->
        {
            StudentInfoVO studentInfoVO = new StudentInfoVO();
            BeanUtils.copyProperties(item, studentInfoVO);
            studentInfoVO.setStudentId(item.getId());
            studentInfoVO.setStuDepart(departMap.get(item.getStuDeptId()));
            studentInfoVO.setStuMajor(majorMap.get(item.getStuMajorId()));
            studentInfoVO.setStuClass(classMap.get(item.getStuClassId()));
            return studentInfoVO;
        }).collect(Collectors.toList());
    }
}