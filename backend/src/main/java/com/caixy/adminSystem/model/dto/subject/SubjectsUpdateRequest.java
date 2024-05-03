package com.caixy.adminSystem.model.dto.subject;

import lombok.Data;

import java.io.Serializable;

/**
 * 更新请求
 */
@Data
public class SubjectsUpdateRequest implements Serializable
{

    /**
     * id
     */
    private Long id;

    /**
     * 名称
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

    private static final long serialVersionUID = 1L;
}