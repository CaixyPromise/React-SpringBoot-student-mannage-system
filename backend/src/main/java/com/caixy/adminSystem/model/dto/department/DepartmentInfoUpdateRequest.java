package com.caixy.adminSystem.model.dto.department;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户更新请求
 */
@Data
public class DepartmentInfoUpdateRequest implements Serializable
{
    /**
     * id
     */
    private Long id;

    /**
     * 学院名称
     */
    private String name;

    private static final long serialVersionUID = 1L;
}