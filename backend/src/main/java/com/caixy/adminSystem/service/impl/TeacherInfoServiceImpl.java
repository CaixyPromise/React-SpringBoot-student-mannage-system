package com.caixy.adminSystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caixy.adminSystem.common.ErrorCode;
import com.caixy.adminSystem.exception.BusinessException;
import com.caixy.adminSystem.exception.ThrowUtils;
import com.caixy.adminSystem.mapper.CourseSelectionInfoMapper;
import com.caixy.adminSystem.mapper.CourseSelectionSubjectMapper;
import com.caixy.adminSystem.mapper.SubjectsMapper;
import com.caixy.adminSystem.model.dto.teacherInfo.TeacherInfoAddRequest;
import com.caixy.adminSystem.model.dto.teacherInfo.TeacherInfoQueryRequest;
import com.caixy.adminSystem.model.entity.*;
import com.caixy.adminSystem.model.enums.UserRoleEnum;
import com.caixy.adminSystem.model.enums.UserSexEnum;
import com.caixy.adminSystem.model.vo.courseSelectionInfo.CourseSelectionInfoVO;
import com.caixy.adminSystem.model.vo.teacherInfo.AssignedTeacherSelectionInfo;
import com.caixy.adminSystem.model.vo.teacherInfo.TeacherInfoVO;
import com.caixy.adminSystem.service.*;
import com.caixy.adminSystem.mapper.TeacherInfoMapper;
import com.caixy.adminSystem.utils.JsonUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author CAIXYPROMISE
 * @description 针对表【teacher_info(教师信息表)】的数据库操作Service实现
 * @createDate 2025-01-11 19:10:20
 */
@Service
public class TeacherInfoServiceImpl extends ServiceImpl<TeacherInfoMapper, TeacherInfo>
        implements TeacherInfoService
{
    @Resource
    private MajorInfoService majorInfoService;
    @Resource
    private ClassesInfoService classesInfoService;
    @Resource
    private DepartmentInfoService departmentInfoService;
    @Resource
    private UserService userService;
    @Autowired
    private TeacherInfoMapper teacherInfoMapper;

    @Autowired
    private CourseSelectionInfoMapper courseSelectionInfoMapper;
    @Autowired
    private SubjectsMapper subjectsMapper;
    @Resource
    private CourseSelectionSubjectMapper courseSelectionSubjectMapper;

    /**
     * 校验数据
     *
     * @param teacherInfo
     * @param add      对创建的数据进行校验
     */
    @Override
    public void validTeacherInfo(TeacherInfo teacherInfo, boolean add) {
        ThrowUtils.throwIf(teacherInfo == null, ErrorCode.PARAMS_ERROR);
        UserSexEnum userSexEnum = UserSexEnum.getEnumByCode(teacherInfo.getTeacherSex());
        if (userSexEnum == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "性别不合法");
        }
        boolean majorExistById = majorInfoService.majorExistById(teacherInfo.getTeacherMajorId(), teacherInfo.getTeacherDeptId());
        if (!majorExistById)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "专业不存在");
        }
        if (add)
        {
            String teacherId = teacherInfo.getTeacherId();
            if (StringUtils.isBlank(teacherId) || teacherId.length() < 8)
            {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "工号不合法");
            }
            LambdaQueryWrapper<TeacherInfo> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(TeacherInfo::getTeacherId, teacherId);
            if (this.count(queryWrapper) > 0)
            {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "工号已存在");
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addTeacher(TeacherInfoAddRequest teacherInfoAddRequest, HttpServletRequest request, User creator) {
        TeacherInfo post = new TeacherInfo();
        BeanUtils.copyProperties(teacherInfoAddRequest, post);
        // 参数校验
        validTeacherInfo(post, true);
        post.setCreatorId(creator.getId());
        boolean result = this.save(post);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newTeacherInfoId = post.getId();
        User newUser = User.builder()
                           .userAccount(post.getTeacherId())
                           .userPassword(post.getTeacherId())
                           .id(newTeacherInfoId)
                           .userDepartment(post.getTeacherDeptId())
                           .userMajor(post.getTeacherMajorId())
                           .userRoleLevel(0)
                           .userRole(UserRoleEnum.TEACHER.getValue())
                           .userName(post.getTeacherName())
                           .userSex(post.getTeacherSex())
                           .build();
        long register = userService.makeRegister(newUser);
        ThrowUtils.throwIf(register < 0, ErrorCode.OPERATION_ERROR);
        return newTeacherInfoId;
    }

    /**
     * 获取教师信息封装
     *
     * @param teacherInfo
     * @param request
     * @return
     */
    @Override
    public TeacherInfoVO getTeacherInfoVO(TeacherInfo teacherInfo, HttpServletRequest request) {
        // todo: 补充获取教师信息封装逻辑
        return null;
    }

    /**
     * 获取教师信息
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @version 2025/1/16 3:04
     */
    @Override
    public TeacherInfoVO getTeacherVOWithCondition(TeacherInfoQueryRequest queryRequest) {
        TeacherInfoVO teacherInfoVO = new TeacherInfoVO();
        teacherInfoVO.setTeacherId(queryRequest.getTeacherId());
        teacherInfoVO.setId(queryRequest.getId());
        teacherInfoVO.setTeacherName(queryRequest.getTeacherName());
        TeacherInfoVO resultInfo = teacherInfoMapper.selectTeacherInfoByConditions(teacherInfoVO);
        if (resultInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return resultInfo;
    }
    /**
     * 获取教师信息
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @version 2025/1/16 3:05
     */
    @Override
    public TeacherInfoVO getTeacherVOById(Long id) {
        TeacherInfoQueryRequest queryRequest = new TeacherInfoQueryRequest();
        queryRequest.setId(id);
        return getTeacherVOWithCondition(queryRequest);
    }

    @Override
    public LambdaQueryWrapper<TeacherInfo> getQueryWrapper(TeacherInfoQueryRequest request) {
        LambdaQueryWrapper<TeacherInfo> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(request.getId() != null, TeacherInfo::getId, request.getId());
        queryWrapper.eq(StringUtils.isNotBlank(request.getTeacherId()), TeacherInfo::getTeacherId, request.getTeacherId());
        queryWrapper.like(StringUtils.isNotBlank(request.getTeacherName()), TeacherInfo::getTeacherName, request.getTeacherName());
        queryWrapper.eq(request.getTeacherSex() != null, TeacherInfo::getTeacherSex, request.getTeacherSex());

        // 处理 teacherDepart 相关查询
        if (request.getTeacherDepart() != null) {
            queryWrapper.eq(request.getTeacherDepart().getDepartId() != null, TeacherInfo::getTeacherDeptId, request.getTeacherDepart().getDepartId());
            queryWrapper.eq(request.getTeacherDepart().getMajorId() != null, TeacherInfo::getTeacherMajorId, request.getTeacherDepart().getMajorId());
        }

        return queryWrapper;
    }

    /**
     * 分页获取教师信息封装
     *
     * @param teacherInfoPage
     * @return
     */
    @Override
    public Page<TeacherInfoVO> getTeacherInfoVOPage(Page<TeacherInfo> teacherInfoPage) {
        Page<TeacherInfoVO> postVOPage = new Page<>(teacherInfoPage.getCurrent(), teacherInfoPage.getSize());
        List<TeacherInfoVO> studentInfoVOS = this.getTeacherInfoVOList(teacherInfoPage.getRecords());
        postVOPage.setRecords(studentInfoVOS);
        postVOPage.setTotal(teacherInfoPage.getTotal());
        return postVOPage;
    }

    /**
     * 获取教师信息选项
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @version 2025/1/16 1:07
     */
    @Override
    public IPage<TeacherInfoVO> getTeacherInfoOptionVO(TeacherInfoQueryRequest teacherInfoQueryRequest)
    {
        Page<TeacherInfo> teacherInfoPage = new Page<>(teacherInfoQueryRequest.getCurrent(), teacherInfoQueryRequest.getPageSize());
        IPage<TeacherInfoVO> teacherInfoVOIPage = teacherInfoMapper.selectTeacherInfoPage(teacherInfoPage, teacherInfoQueryRequest);        List<TeacherInfoVO> records = teacherInfoVOIPage.getRecords();
        if (records.isEmpty()) {
            return new Page<>();
        }
        return teacherInfoVOIPage;
    }

    /**
     * 获取教师负责的选修课程信息
     *
     */
    @Override
    public List<AssignedTeacherSelectionInfo> getAssignedTeacherSelectionInfoByTeacherId(Long teacherId) {
        // 1. 查询该教师负责的所有选修课程记录
        List<CourseSelectionSubject> records = courseSelectionSubjectMapper.selectList(
                Wrappers.<CourseSelectionSubject>lambdaQuery()
                        .eq(CourseSelectionSubject::getTeacherId, teacherId)
                        .eq(CourseSelectionSubject::getIsDelete, 0)
        );
        if (CollectionUtils.isEmpty(records)) {
            return Collections.emptyList();
        }

        // 2. 收集所有科目ID，查询相关科目信息
        Set<Long> subjectIds = records.stream()
                                      .map(CourseSelectionSubject::getSubjectId)
                                      .collect(Collectors.toSet());
        List<Subjects> subjects = subjectsMapper.selectBatchIds(subjectIds);
        Map<Long, Subjects> subjectMap = subjects.stream()
                                                 .collect(Collectors.toMap(Subjects::getId, Function.identity()));

        // 3. 转换每条记录为 AssignedTeacherSelectionInfo 对象
        // 收集所有选课任务ID以查询选课任务信息
        Set<Long> selectionIds = records.stream()
                                        .map(CourseSelectionSubject::getCourseSelectionId)
                                        .collect(Collectors.toSet());
        List<CourseSelectionInfo> selectionInfos = courseSelectionInfoMapper.selectBatchIds(selectionIds);
        Map<Long, CourseSelectionInfo> selectionMap = selectionInfos.stream()
                                                                    .collect(Collectors.toMap(CourseSelectionInfo::getId, Function.identity()));

        List<AssignedTeacherSelectionInfo> result = new ArrayList<>();
        for (CourseSelectionSubject record : records) {
            Subjects subject = subjectMap.get(record.getSubjectId());
            if (subject == null) {
                continue;
            }

            AssignedTeacherSelectionInfo info = new AssignedTeacherSelectionInfo();
            // 将科目基本信息复制到 info 中
            BeanUtils.copyProperties(subject, info);

            // 设置教师分配课程的附加信息
            info.setClassRoom(record.getClassRoom());
            info.setClassTimes(JsonUtils.jsonToList(record.getClassTimes()));
            info.setMaxStudents(record.getMaxStudents());
            info.setEnrolledCount(record.getEnrolledCount());
            info.setCourseSelectionId(record.getCourseSelectionId());

            // 设置选课任务详细信息
            CourseSelectionInfo selection = selectionMap.get(record.getCourseSelectionId());
            if (selection != null) {
                CourseSelectionInfoVO selectionVO = new CourseSelectionInfoVO();
                BeanUtils.copyProperties(selection, selectionVO);
                info.setCourseSelectionInfoVO(selectionVO);
            }

            result.add(info);
        }

        return result;
    }


    private List<TeacherInfoVO> getTeacherInfoVOList(List<TeacherInfo> teacherInfoList)
    {
        if (teacherInfoList.isEmpty()) {
            return Collections.emptyList();
        }
        Set<Long> departIds = new HashSet<>();
        Set<Long> majorIds = new HashSet<>();
        teacherInfoList.forEach(item -> {
            departIds.add(item.getTeacherDeptId());
            majorIds.add(item.getTeacherMajorId());
        });

        Map<Long, String> departMap = departmentInfoService.listByIds(departIds).stream()
                                                           .collect(Collectors.toMap(DepartmentInfo::getId,
                                                                   DepartmentInfo::getName));
        Map<Long, String> majorMap = majorInfoService.listByIds(majorIds).stream()
                                                     .collect(Collectors.toMap(MajorInfo::getId, MajorInfo::getName));

        return teacherInfoList.stream().map(item -> {
            TeacherInfoVO teacherInfoVO = new TeacherInfoVO();
            BeanUtils.copyProperties(item, teacherInfoVO);
            teacherInfoVO.setTeacherDepart(departMap.get(item.getTeacherDeptId()));
            teacherInfoVO.setTeacherMajor(majorMap.get(item.getTeacherMajorId()));
            return teacherInfoVO;
        }).collect(Collectors.toList());
    }
}




