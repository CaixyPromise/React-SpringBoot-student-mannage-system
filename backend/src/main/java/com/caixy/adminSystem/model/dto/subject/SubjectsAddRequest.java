package com.caixy.adminSystem.model.dto.subject;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 创建请求
 *
 */
@Data
public class SubjectsAddRequest implements Serializable
{
    /**
     * 科目名称
     */
    private String name;

    /**
     * 科目最高分
     */
    private Integer gradeMax;
    /**
     * 课程类型
     */
    private Integer courseType;
    /**
     * 科目最低分
     */
    private Integer gradeMin;

    /**
     * 优秀分数线
     */
    private Integer gradeExcellent;

    /**
     * 不及格分数线
     */
    private Integer gradeFail;

    /**
     * 课程学时
     */
    private Integer creditHours;

    /**
    * 学分
    */
    private Double gradeCredit;

    private static final long serialVersionUID = 1L;
}