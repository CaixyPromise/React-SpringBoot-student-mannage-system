package com.caixy.adminSystem.model.dto.department;

import lombok.Data;

import java.io.Serializable;

/**
 * @name: com.caixy.model.dto.department.DepartmentWithMajorsDTO
 * @author: CAIXYPROMISE
 * @since: 2024-02-11 13:11
 **/
@Data
public class DepartmentWithMajorsDTO implements Serializable
{
    /**
     * 部门id
     */
    private Long departmentId;
    /**
     * 部门名称
     */
    private String departmentName;
    /**
     * 专业id
     */
    private Long majorId;
    /**
     * 专业名称
     */
    private String majorName;
    private static final long serialVersionUID = 1L;
}
