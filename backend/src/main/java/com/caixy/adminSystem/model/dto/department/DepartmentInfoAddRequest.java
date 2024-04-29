package com.caixy.adminSystem.model.dto.department;

import lombok.Data;

import java.io.Serializable;

/**
 * 增加学院请求体
 *
 * @name: com.caixy.model.dto.DepartmentInfoVO.DepartmentInfoAddRequest
 * @author: CAIXYPROMISE
 * @since: 2024-02-10 01:50
 **/
@Data
public class DepartmentInfoAddRequest implements Serializable
{
    private String name;
    private static final long serialVersionUID = 1L;
}
