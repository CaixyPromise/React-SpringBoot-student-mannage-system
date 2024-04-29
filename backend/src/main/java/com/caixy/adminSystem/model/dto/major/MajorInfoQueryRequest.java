package com.caixy.adminSystem.model.dto.major;



import com.caixy.adminSystem.common.PageRequest;

import java.io.Serializable;
import java.util.Date;

/**
 * 学院信息分页请求体
 *
 * @name: com.caixy.model.dto.department.DepartmentInfoQueryRequest
 * @author: CAIXYPROMISE
 * @since: 2024-02-10 02:00
 **/
public class MajorInfoQueryRequest extends PageRequest implements Serializable
{
    /**
     * 学院id
     */
    private String departmentId;
    /**
     * 学院名称
     */
    private String departmentName;
    /**
     * 专业id
     */
    private String majorId;
    /**
     * 学院名称
     */
    private String name;
    /**
     * 添加人id
     */
    private Long createUserId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}
