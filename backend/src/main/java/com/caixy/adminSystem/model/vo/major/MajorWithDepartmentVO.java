package com.caixy.adminSystem.model.vo.major;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 专业关联学院信息
 *
 * @name: com.caixy.model.vo.major.MajorWithDepartmentVO
 * @author: CAIXYPROMISE
 * @since: 2024-02-11 01:43
 **/
@Data
public class MajorWithDepartmentVO implements Serializable
{
    private Long id;
    private Long departId;
    private String name;

    private Date createTime;
    private Date updateTime;
    private Long departmentId;
    private String departmentName;

    private static final long serialVersionUID = 1L;
}

