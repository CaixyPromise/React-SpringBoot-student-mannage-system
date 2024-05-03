package com.caixy.adminSystem.model.vo.Subjects;

import lombok.Data;

import java.io.Serializable;

/**
 * 科目VO返回类
 *
 * @name: com.caixy.adminSystem.model.vo.Subjects.SubjectsVO
 * @author: CAIXYPROMISE
 * @since: 2024-04-29 21:57
 **/
@Data
public class SubjectsVO implements Serializable
{
    /**
     * 科目id
     */
    private Long id;

    /**
     * 科目名称
     */
    private String name;

    /**
     * 科目最大值
     */
    private Long gradeMax;

    /**
     * 科目最小值
     */
    private Long gradeMin;

    private static final long serialVersionUID = 1L;
}
