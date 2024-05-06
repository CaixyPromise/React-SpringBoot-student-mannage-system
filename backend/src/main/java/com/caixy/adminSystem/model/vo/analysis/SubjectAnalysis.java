package com.caixy.adminSystem.model.vo.analysis;

import com.caixy.adminSystem.model.entity.StudentInfo;
import com.caixy.adminSystem.model.vo.StudentInfo.StudentInfoVO;
import lombok.Data;

import java.io.Serializable;

/**
 * 科目分析实体类
 *
 * @name com.caixy.adminSystem.model.vo.analysis.SubjectAnalysis
 * @author CAIXYPROMISE
 * @since 2024-05-05 22:06
 **/
@Data
public class SubjectAnalysis implements Serializable
{
    /**
     * 科目id
     */
    private Long subjectId;
    /**
     * 科目名称
     */
    private String subjectName;
    /**
     * 平均分
     */
    private Double averageScore;
    /**
     * 最高分
     */
    private Long highestScore;
    /**
     * 最高分学生信息
     */
    private StudentInfoVO highestScoreStudentName;
    private static final long serialVersionUID = 1L;
}
