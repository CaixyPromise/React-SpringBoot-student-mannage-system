package com.caixy.adminSystem.model.vo.department;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 学院下所有学院的信息VO
 *
 * @name: com.caixy.model.vo.department.DepartmentWithMajorsVO
 * @author: CAIXYPROMISE
 * @since: 2024-02-11 13:13
 **/
@Data
public class DepartmentWithMajorsVO implements Serializable
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
     * 学院下的所有专业信息
     */
    List<MajorInnerInfo> majors;

    @Data
    public static class MajorInnerInfo
    {
        private Long majorId;
        private String majorName;
    }

    private static final long serialVersionUID = 1L;
}
