package com.caixy.adminSystem.model.dto.classesInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 从三个表（学院信息、专业信息、班级信息）查询所需的字段
 *
 * @name: com.caixy.adminSystem.model.dto.classesInfo.DepartmentMajorClassDTO
 * @author: CAIXYPROMISE
 * @since: 2024-04-29 23:09
 **/
@Data
public class DepartmentMajorClassDTO implements Serializable
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

    /**
     * 班级id
     */
    private Long classId;

    /**
     * 班级名称
     */
    private String className;

    private static final long serialVersionUID = 1L;
}
