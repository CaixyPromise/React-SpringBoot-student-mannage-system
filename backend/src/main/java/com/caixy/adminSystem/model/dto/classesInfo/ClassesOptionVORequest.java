package com.caixy.adminSystem.model.dto.classesInfo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 班级选项VO请求体
 *
 * @Author CAIXYPROMISE
 * @since 2025/1/6 23:44
 */
@Data
public class ClassesOptionVORequest implements Serializable
{
    private List<Long> excludeClassIds;
    private static final long serialVersionUID = 1L;
}
