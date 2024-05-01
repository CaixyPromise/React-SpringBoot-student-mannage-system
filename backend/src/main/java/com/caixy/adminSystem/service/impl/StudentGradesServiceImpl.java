package com.caixy.adminSystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caixy.adminSystem.mapper.StudentGradesMapper;
import com.caixy.adminSystem.model.dto.StudentGrades.StudentGradesQueryRequest;
import com.caixy.adminSystem.model.entity.StudentGrades;
import com.caixy.adminSystem.model.entity.StudentInfo;
import com.caixy.adminSystem.model.vo.StudentGrades.StudentGradesVO;
import com.caixy.adminSystem.model.vo.StudentInfo.StudentInfoVO;
import com.caixy.adminSystem.service.StudentGradesService;
import com.caixy.adminSystem.service.StudentInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author CAIXYPROMISE
 * @description 针对表【student_grades】的数据库操作Service实现
 * @createDate 2024-04-02 22:30:06
 */
@Service
public class StudentGradesServiceImpl extends ServiceImpl<StudentGradesMapper, StudentGrades>
        implements StudentGradesService
{
    @Resource
    private StudentInfoService studentInfoService;


    public StudentGradesVO getStudentGradesVOByStuId(long stuId)
    {
        StudentInfo student = studentInfoService.getById(stuId);
        QueryWrapper<StudentGrades> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", stuId);
        List<StudentGrades> post = list(queryWrapper);


        StudentInfoVO studentInfoVO = studentInfoService.getStudentInfoVO(student);


        return null;
    }

    @Override
    public void validStudentGrades(StudentGrades post, boolean add)
    {

    }

    @Override
    public QueryWrapper<StudentGrades> getQueryWrapper(StudentGradesQueryRequest postQueryRequest)
    {
        return null;
    }

    @Override
    public Page<StudentGrades> searchFromEs(StudentGradesQueryRequest postQueryRequest)
    {
        return null;
    }

    @Override
    public StudentGradesVO getStudentGradesVO(StudentGrades post, HttpServletRequest request)
    {
        return null;
    }

    @Override
    public Page<StudentGradesVO> getStudentGradesVOPage(Page<StudentGrades> postPage, HttpServletRequest request)
    {
        return null;
    }
}




