package com.caixy.adminSystem.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 学生成绩表
 *
 * @TableName student_grades
 */
@TableName(value = "student_grades")
@Data
public  class StudentGrades implements Serializable
{
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 学生ID，引用自student_score表
     */
    private Long stuId;

    /**
     * 科目id
     */
    private Long subjectId;

    /**
     * 成绩
     */
    private Long grade;

    /**
     * 创建人id
     */
    private Long creatorId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除逻辑
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}