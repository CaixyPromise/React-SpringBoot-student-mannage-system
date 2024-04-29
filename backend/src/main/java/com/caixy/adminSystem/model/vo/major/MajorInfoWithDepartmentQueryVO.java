package com.caixy.adminSystem.model.vo.major;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 专业关联学院信息vo
 *
 * @name: com.caixy.model.vo.major.MajorInfoWithDepartmentQueryVO
 * @author: CAIXYPROMISE
 * @since: 2024-02-11 01:27
 **/
@Data
public class MajorInfoWithDepartmentQueryVO implements Serializable
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
    private Long majorId;
    /**
     * 专业名称
     */
    private String majorName;
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
