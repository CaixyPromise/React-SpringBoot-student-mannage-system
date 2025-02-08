package com.caixy.adminSystem.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.caixy.adminSystem.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 学生成绩表
 *
 * @TableName student_grades
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "student_grades")
@Data
public class StudentGrades extends BaseEntity
{
    /**
     * 学生ID，引用自student_score表
     */
    private Long stuId;

    /**
     * 科目id
     */
    private Long subjectId;

    /**
     * 学期id
     */
    private Long semesterId;

    /**
     * 成绩
     */
    private Integer totalGrade;

    /**
     * 平时成绩
     */
    private Integer usualGrade;

    /**
     * 期末成绩
     */
    private Integer finalGrade;

    /**
     * 平时分比例
     */
    private Integer usualPercentage;

    /**
     * 期末分比例
     */
    private Integer finalPercentage;

    /**
     * 课程组id
     */
    private Long courseGroupId;

    /**
     * 是否是选修课
     */
    private Integer isElectives;

    /**
     * 创建人id
     */
    private Long creatorId;
}