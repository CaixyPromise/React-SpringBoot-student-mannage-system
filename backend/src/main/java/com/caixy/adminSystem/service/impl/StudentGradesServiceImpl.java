package com.caixy.adminSystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caixy.adminSystem.common.ErrorCode;
import com.caixy.adminSystem.exception.BusinessException;
import com.caixy.adminSystem.mapper.StudentGradesMapper;
import com.caixy.adminSystem.model.dto.StudentGrades.StudentGradesAddRequest;
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
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
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
        List<StudentGrades> studentGradesList = list(queryWrapper);
        // 如果成绩信息为空，则返回空成绩列表
        if (studentGradesList.isEmpty())
        {
            studentGradesVO.setGradeItem(Collections.emptyList());
            return studentGradesVO;
        }

        // 收集学生科目信息 -- 科目id
        Set<Long> stuGradeSubjectIds =
                studentGradesList.stream().map(StudentGrades::getSubjectId).collect(Collectors.toSet());
        // 查询科目信息
        List<Subjects> subjects = subjectsService.listByIds(stuGradeSubjectIds);
        // 转 id -> 实体 map映射 科目
        Map<Long, Subjects> subjectsMap = subjects.stream().collect(Collectors.toMap(Subjects::getId, item -> item));
        // 收集学生成绩信息
        List<StudentGradesVO.GradeItem> gradeItemList = studentGradesList.stream().map(item ->
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
        // 添加操作时的校验操作
        if (add)
        {
            StudentInfo studentInfo = studentInfoService.getById(post.getStuId());
            if (studentInfo == null)
            {
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "学生不存在");
            }
        }
        else
        {
            // 更新时的校验
            StudentGrades studentGrades = this.getById(post.getId());
            if (studentGrades == null)
            {
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "学生成绩不存在");
            }
            // 如果当前成绩和需要修改的成绩是一样的就不需要更新
            if (Objects.equals(studentGrades.getGrade(), post.getGrade()))
            {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "成绩信息无需更新");
            }
            // 只能更新当前成绩-> 传入时没有传入科目id，为了下面校验科目的成绩是否合法，需要设置科目id到实体类防止null错误
            post.setSubjectId(studentGrades.getSubjectId());
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
            throw new BusinessException(ErrorCode.PARAMS_ERROR, String.format("成绩信息不合法: 最大: %d, 最小: %d, 当前: %d",
                    subjectById.getGradeMax(), subjectById.getGradeMin(), grade));
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean batchAddStudentGrade(List<StudentGradesAddRequest> postAddRequest, Long creatorId)
    {
        // 校验科目是否存在
        Set<Long> subjectIds =
                postAddRequest.stream().map(StudentGradesAddRequest::getSubjectId).collect(Collectors.toSet());
        if (subjectIds.isEmpty())
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "科目不存在");
        }
        QueryWrapper<Subjects> subjectsQueryWrapper = new QueryWrapper<>();
        subjectsQueryWrapper.in("id", subjectIds);
        List<Subjects> subjects = subjectsService.list(subjectsQueryWrapper);
        if (subjectIds.size() != subjects.size())
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "科目不存在");
        }
        // 科目id -> 实体映射
        Map<Long, Subjects> subjectsMap = subjects.stream().collect(Collectors.toMap(Subjects::getId, item -> item));

        // 校验学生是否存在
        Set<Long> stuIds =
                postAddRequest.stream().map(StudentGradesAddRequest::getStudentId).collect(Collectors.toSet());
        if (stuIds.isEmpty())
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "学生不存在");
        }
        QueryWrapper<StudentInfo> studentInfoQueryWrapper = new QueryWrapper<>();
        studentInfoQueryWrapper.in("id", stuIds);
        List<StudentInfo> studentInfos = studentInfoService.list(studentInfoQueryWrapper);
        if (stuIds.size() != studentInfos.size())
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "学生不存在");
        }
        // 请求体转实体类
        List<StudentGrades> resultStudentGradeList = postAddRequest.stream().map(item -> {
            StudentGrades studentGrades = new StudentGrades();
            // 校验成绩是否合法
            Subjects stuSubject = subjectsMap.get(item.getSubjectId());
            if (subjectsService.checkGradeIsValid(item.getScore(), stuSubject))
            {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, String.format("%s-成绩信息不合法: 最大: %d, 最小: %d, 当前: %d",
                        stuSubject.getName(),
                        stuSubject.getGradeMax(),
                        stuSubject.getGradeMin(),
                        item.getScore()));
            }
            studentGrades.setStuId(item.getStudentId());
            studentGrades.setSubjectId(item.getSubjectId());
            studentGrades.setCreatorId(creatorId);
            studentGrades.setGrade(item.getScore());
            return studentGrades;
        }).collect(Collectors.toList());

        // 批量添加学生成绩并返回
        return this.saveBatch(resultStudentGradeList);
    }

    /**
     * 根据科目id查询学生成绩
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/5/5 下午10:15
     */
    @Override
    public List<StudentGrades> listBySubjectId(Long subjectId)
    {
        QueryWrapper<StudentGrades> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", subjectId);
        return this.list(queryWrapper);

    }
}




