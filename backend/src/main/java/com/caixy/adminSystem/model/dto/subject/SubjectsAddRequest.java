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
    private Long gradeMax;

    /**
     * 科目最低分
     */
    private Long gradeMin;

    /**
     * 优秀分数线
     */
    private Long gradeExcellent;

    /**
     * 不及格分数线
     */
    private Long gradeFail;

    private static final long serialVersionUID = 1L;
}