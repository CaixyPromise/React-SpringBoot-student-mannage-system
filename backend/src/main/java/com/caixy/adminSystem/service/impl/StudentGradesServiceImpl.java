package com.caixy.adminSystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caixy.adminSystem.common.ErrorCode;
import com.caixy.adminSystem.exception.BusinessException;
import com.caixy.adminSystem.mapper.StudentGradesMapper;
import com.caixy.adminSystem.model.dto.StudentGrades.StudentGradesQueryRequest;
import com.caixy.adminSystem.model.entity.StudentGrades;
import com.caixy.adminSystem.model.entity.StudentInfo;
import com.caixy.adminSystem.model.entity.Subjects;
import com.caixy.adminSystem.model.vo.StudentGrades.StudentGradesVO;
import com.caixy.adminSystem.model.vo.StudentInfo.StudentInfoVO;
import com.caixy.adminSystem.service.StudentGradesService;
import com.caixy.adminSystem.service.StudentInfoService;
import com.caixy.adminSystem.service.SubjectsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Resource
    private SubjectsService subjectsService;

    /**
     * 根据学生id，获取学生成绩信息
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/5/3 下午4:32
     */
    @Override
    public StudentGradesVO getStudentGradesVOByStuId(long stuId)
    {
        // 查询学生信息
        StudentInfo student = studentInfoService.getById(stuId);
        if (student == null)
        {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "学生不存在");
        }
        StudentInfoVO studentInfoVO = studentInfoService.getStudentInfoVO(student);
        StudentGradesVO studentGradesVO = new StudentGradesVO();
        studentGradesVO.setStuId(stuId);
        studentGradesVO.setStudentInfo(studentInfoVO);

        // 获取成绩信息
        QueryWrapper<StudentGrades> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("stuId", stuId);
        List<StudentGrades> post = list(queryWrapper);
        // 如果成绩信息为空，则返回空成绩列表
        if (post.isEmpty())
        {
            studentGradesVO.setGradeItem(Collections.emptyList());
            return studentGradesVO;
        }

        // 收集学生科目信息
        Set<Long> stuGradeSubjectIds = post.stream().map(StudentGrades::getId).collect(Collectors.toSet());
        // 查询科目信息
        List<Subjects> subjects = subjectsService.listByIds(stuGradeSubjectIds);
        // 转 id -> 实体 map映射 科目
        Map<Long, Subjects> subjectsMap = subjects.stream().collect(Collectors.toMap(Subjects::getId, item -> item));
        // 收集学生成绩信息
        List<StudentGradesVO.GradeItem> gradeItemList = post.stream().map(item ->
        {
            StudentGradesVO.GradeItem gradeItem = new StudentGradesVO.GradeItem();
            gradeItem.setGradeId(item.getId());
            gradeItem.setSubjectId(item.getSubjectId());
            gradeItem.setSubjectName(subjectsMap.get(item.getSubjectId()).getName());
            gradeItem.setGrade(item.getGrade());
            return gradeItem;
        }).collect(Collectors.toList());
        // 设置成绩信息
        studentGradesVO.setGradeItem(gradeItemList);
        return studentGradesVO;
    }


    @Override
    public void validStudentGrades(StudentGrades post, boolean add)
    {
        StudentInfo studentInfo = studentInfoService.getById(post.getStuId());

        if (studentInfo == null)
        {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "学生不存在");
        }
        Subjects subjectById = subjectsService.getById(post.getSubjectId());
        if (subjectById == null)
        {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "科目不存在");
        }
        Long grade = post.getGrade();
        // 检查成绩是否合法，是否符合科目最大最小值要求
        if (grade == null || subjectsService.checkGradeIsValid(grade, subjectById))
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "成绩信息不合法");
        }
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




