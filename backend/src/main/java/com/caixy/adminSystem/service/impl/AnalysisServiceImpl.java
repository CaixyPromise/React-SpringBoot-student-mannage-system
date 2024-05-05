package com.caixy.adminSystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.caixy.adminSystem.model.dto.analysis.GradeAnalysisFilterDTO;
import com.caixy.adminSystem.model.entity.StudentGrades;
import com.caixy.adminSystem.model.entity.StudentInfo;
import com.caixy.adminSystem.model.entity.Subjects;
import com.caixy.adminSystem.model.vo.StudentInfo.StudentInfoVO;
import com.caixy.adminSystem.model.vo.analysis.SubjectAnalysis;
import com.caixy.adminSystem.service.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 数据分析服务类-实现
 *
 * @name: com.caixy.adminSystem.service.impl.AnalysisServiceImpl
 * @author: CAIXYPROMISE
 * @since: 2024-05-05 19:14
 **/
@Service
@AllArgsConstructor
public class AnalysisServiceImpl implements AnalysisService
{
    /**
     * 学生信息服务类
     */
    private final StudentInfoService studentInfoService;
    /**
     * 成绩信息服务类
     */
    private final StudentGradesService studentGradesService;
    /**
     * 班级信息服务类
     */
    private final ClassesInfoService classesInfoService;
    /**
     * 学院信息服务类
     */
    private final DepartmentInfoService departmentInfoService;
    /**
     * 专业信息服务类
     */
    private final MajorInfoService majorInfoService;
    /**
     * 科目服务类
     */
    private final SubjectsService subjectsService;

    /**
     * 获取所有科目成绩分析信息
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/5/5 下午10:16
     */
    public List<SubjectAnalysis> getAllSubjectAnalyses()
    {
        List<Subjects> subjectsList = subjectsService.list();
        List<StudentGrades> studentGradesList = studentGradesService.list();
        return analyzeGrades(subjectsList, studentGradesList);
    }

    /**
     * 获取指定科目成绩分析信息
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/5/5 下午10:16
     */
    public SubjectAnalysis getSubjectAnalysisById(Long subjectId)
    {
        Subjects subject = subjectsService.getById(subjectId);
        List<StudentGrades> studentGradesList = studentGradesService.listBySubjectId(subjectId);
        return analyzeGrades(Collections.singletonList(subject), studentGradesList).stream().findFirst().orElse(null);
    }

    /**
     * 获取指定科目成绩分析信息
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/5/5 下午10:16
     */
    private List<SubjectAnalysis> analyzeGrades(List<Subjects> subjectsList, List<StudentGrades> studentGradesList)
    {
        Map<Long, Subjects> subjectsMap = subjectsList.stream()
                .collect(Collectors.toMap(Subjects::getId, Function.identity()));

        Map<Long, List<StudentGrades>> studentGradesMap = studentGradesList.stream()
                .collect(Collectors.groupingBy(StudentGrades::getSubjectId));

        List<SubjectAnalysis> subjectAnalysisList = new ArrayList<>();
        studentGradesMap.forEach((subjectId, grades) ->
        {
            SubjectAnalysis analysis = createSubjectAnalysis(subjectId, grades, subjectsMap);
            subjectAnalysisList.add(analysis);
        });

        return subjectAnalysisList;
    }

    /**
     * 创建科目成绩分析信息
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/5/5 下午10:16
     */
    private SubjectAnalysis createSubjectAnalysis(Long subjectId, List<StudentGrades> grades, Map<Long, Subjects> subjectsMap)
    {
        double averageScore = grades.stream().mapToDouble(StudentGrades::getGrade).average().orElse(0.0);
        Optional<StudentGrades> maxGradeEntry = grades.stream().max(Comparator.comparingLong(StudentGrades::getGrade));

        SubjectAnalysis analysis = new SubjectAnalysis();
        analysis.setSubjectId(subjectId);
        analysis.setSubjectName(subjectsMap.get(subjectId).getName());
        analysis.setAverageScore(averageScore);

        maxGradeEntry.ifPresent(maxGrade ->
        {
            analysis.setHighestScore(maxGrade.getGrade());
            // 获取最高分学生信息
            StudentInfoVO highestScoreStudent = studentInfoService.getStudentInfoVOById(maxGrade.getStuId());
            analysis.setHighestScoreStudentName(highestScoreStudent);
        });

        return analysis;
    }


    /**
     * 获取指定学院、专业或班级的成绩分析信息
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/5/5 下午10:41
     */
    public List<SubjectAnalysis> getGradesAnalysisByFilter(GradeAnalysisFilterDTO gradeAnalysisFilterDTO) {
        // 根据过滤条件查询学生信息
        List<StudentInfo> filteredStudents = studentInfoService.list(new QueryWrapper<StudentInfo>()
                .eq(gradeAnalysisFilterDTO.getDepartmentId() != null, "stuDeptId", gradeAnalysisFilterDTO.getDepartmentId())
                .eq(gradeAnalysisFilterDTO.getMajorId() != null, "stuMajorId", gradeAnalysisFilterDTO.getMajorId())
                .eq(gradeAnalysisFilterDTO.getClassId() != null, "stuClassId", gradeAnalysisFilterDTO.getClassId()));

        // 获取过滤后学生的ID列表
        List<Long> studentIds = filteredStudents.stream().map(StudentInfo::getId).collect(Collectors.toList());

        // 查询这些学生的成绩
        List<StudentGrades> studentGradesList = studentGradesService.list(new QueryWrapper<StudentGrades>()
                .in("stuId", studentIds));

        // 查询涉及到的科目信息
        List<Long> subjectIds = studentGradesList.stream().map(StudentGrades::getSubjectId).collect(Collectors.toList());
        List<Subjects> subjectsList = subjectsService.listByIds(subjectIds);

        // 分析成绩数据
        return analyzeGrades(subjectsList, studentGradesList);
    }

}
