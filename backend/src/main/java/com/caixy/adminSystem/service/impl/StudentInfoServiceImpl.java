package com.caixy.adminSystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caixy.adminSystem.common.ErrorCode;
import com.caixy.adminSystem.exception.BusinessException;
import com.caixy.adminSystem.mapper.StudentInfoMapper;
import com.caixy.adminSystem.model.dto.studentInfo.StudentInfoQueryRequest;
import com.caixy.adminSystem.model.entity.ClassesInfo;
import com.caixy.adminSystem.model.entity.DepartmentInfo;
import com.caixy.adminSystem.model.entity.MajorInfo;
import com.caixy.adminSystem.model.entity.StudentInfo;
import com.caixy.adminSystem.model.enums.UserSexEnum;
import com.caixy.adminSystem.model.vo.StudentInfo.StudentInfoVO;
import com.caixy.adminSystem.service.ClassesInfoService;
import com.caixy.adminSystem.service.DepartmentInfoService;
import com.caixy.adminSystem.service.MajorInfoService;
import com.caixy.adminSystem.service.StudentInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
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
    DepartmentInfoService departmentInfoService;


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
    }

    @Override
    public QueryWrapper<StudentInfo> getQueryWrapper(StudentInfoQueryRequest postQueryRequest)
    {
        return null;
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


    @Override
    public Page<StudentInfoVO> getStudentInfoVOPage(Page<StudentInfo> postPage)
    {
        Page<StudentInfoVO> postVOPage = new Page<>(postPage.getCurrent(), postPage.getSize());
        List<StudentInfoVO> studentInfoVOS = this.getStudentInfoVOList(postPage.getRecords());
        postVOPage.setRecords(studentInfoVOS);
        postVOPage.setTotal(studentInfoVOS.size());
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
        Set<Long> departIds = new HashSet<>();
        Set<Long> majorIds = new HashSet<>();
        Set<Long> classIds = new HashSet<>();
        studentInfoList.forEach(item -> {
            departIds.add(item.getStuDeptId());
            majorIds.add(item.getStuMajorId());
            classIds.add(item.getStuClassId());
        });

        Map<Long, String> departMap = departmentInfoService.listByIds(departIds).stream()
                .collect(Collectors.toMap(DepartmentInfo::getId, DepartmentInfo::getName));
        Map<Long, String> majorMap = majorInfoService.listByIds(majorIds).stream()
                .collect(Collectors.toMap(MajorInfo::getId, MajorInfo::getName));
        Map<Long, String> classMap = classesInfoService.listByIds(classIds).stream()
                .collect(Collectors.toMap(ClassesInfo::getId, ClassesInfo::getName));

        return studentInfoList.stream().map(item ->
        {
            StudentInfoVO studentInfoVO = new StudentInfoVO();
            BeanUtils.copyProperties(item, studentInfoVO);
            studentInfoVO.setStuDepart(departMap.get(item.getStuDeptId()));
            studentInfoVO.setStuMajor(majorMap.get(item.getStuMajorId()));
            studentInfoVO.setStuClass(classMap.get(item.getStuClassId()));
            return studentInfoVO;
        }).collect(Collectors.toList());
    }
}




