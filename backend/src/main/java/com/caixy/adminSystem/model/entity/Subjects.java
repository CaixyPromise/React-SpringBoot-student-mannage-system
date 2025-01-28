package com.caixy.adminSystem.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.caixy.adminSystem.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName subjects
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "subjects")
@Data
public class Subjects extends BaseEntity
{
    /**
     * 科目名称
     */
    private String name;

    /**
     * 课程类型
     */
    private Integer courseType;

    /**
     * 最高分
     */
    private Integer gradeMax;

    /**
     * 最低分
     */
    private Integer gradeMin;

    /**
     * 不及格分数线
     */
    private Integer gradeFail;

    /**
     * 优秀分数线
     */
    private Integer gradeExcellent;

    /**
    * 课程学分
    */
    private Double gradeCredit;

    /**
     * 创建人id
     */
    private Long creatorId;

    /**
    * 课程学时
    */
    private Integer creditHours;
}