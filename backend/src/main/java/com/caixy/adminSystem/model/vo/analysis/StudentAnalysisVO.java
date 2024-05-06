package com.caixy.adminSystem.model.vo.analysis;

import com.caixy.adminSystem.model.vo.StudentGrades.StudentGradesVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 学生成绩分析VO
 *
 * @author CAIXYPROMISE
 * @name com.caixy.adminSystem.model.vo.analysis.StudentAnalysisVO
 * @since 2024-05-06 17:29
 **/
@Data
public class StudentAnalysisVO implements Serializable
{
    /**
     * 学生成绩情况
     */
    private StudentGradesVO studentGrade;

    /**
     * 全校成绩情况
     */
    private List<SubjectAnalysis> subjectAnalysis;

    private static final long serialVersionUID = 1L;
}
