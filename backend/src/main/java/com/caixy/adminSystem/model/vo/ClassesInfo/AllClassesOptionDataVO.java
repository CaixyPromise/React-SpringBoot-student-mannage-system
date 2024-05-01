package com.caixy.adminSystem.model.vo.ClassesInfo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 学院+专业+班级选项VO
 *
 * @name: com.caixy.adminSystem.model.vo.ClassesInfo.AllClassesOptionDataVO
 * @author: CAIXYPROMISE
 * @since: 2024-04-29 23:16
 **/
@Data
@AllArgsConstructor
public class AllClassesOptionDataVO implements Serializable
{
    private String value;
    private String label;
    private List<AllClassesOptionDataVO> children;

    public AllClassesOptionDataVO(String value, String label)
    {
        this.value = value;
        this.label = label;
    }

    private static final long serialVersionUID = 1L;
}
