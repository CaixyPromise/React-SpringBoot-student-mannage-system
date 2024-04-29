package com.caixy.adminSystem.model.dto.department;

import com.caixy.adminSystem.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 学院信息分页请求体
 *
 * @name: com.caixy.model.dto.department.DepartmentInfoQueryRequest
 * @author: CAIXYPROMISE
 * @since: 2024-02-10 02:00
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class DepartmentInfoQueryRequest extends PageRequest implements Serializable
{
    /**
     * 学院id
     */
    private Long id;
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
