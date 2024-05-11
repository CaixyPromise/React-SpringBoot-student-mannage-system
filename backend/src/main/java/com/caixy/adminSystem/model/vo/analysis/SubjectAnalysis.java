package com.caixy.adminSystem.model.vo.analysis;

import com.caixy.adminSystem.model.vo.StudentInfo.StudentInfoVO;
import lombok.Data;

import java.io.Serializable;

/**
 * 科目分析实体类
 *
 * @author CAIXYPROMISE
 * @name com.caixy.adminSystem.model.vo.analysis.SubjectAnalysis
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
     * 科目优秀分数线
     */
    private Long subjectExcellentLevel;

    /**
     * 科目不及格分数线
     */
    private Long subjectFailureLevel;


    /**
     * 平均分
     */
    private Double averageScore;

    /**
     * 最高分
     */
    private Long highestScore;

    /**
     * 优秀率
     */
    private Double excellentRate;

    /**
     * 不及格率
     */
    private Double failureRate;

    /**
     * 优秀人数
     */
    private Long excellentCount;

    /**
     * 不及格人数
     */
    private Long failureCount;

    /**
     * 最高分学生信息
     */
    private StudentInfoVO highestScoreStudentName;
    private static final long serialVersionUID = 1L;
}
