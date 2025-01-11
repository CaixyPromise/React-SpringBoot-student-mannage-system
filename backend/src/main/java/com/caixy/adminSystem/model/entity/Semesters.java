package com.caixy.adminSystem.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 学期管理表
 * @TableName semesters
 */
@TableName(value ="semesters")
@Data
public class Semesters implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 学期名称，例如2023-2024学年第一学期
     */
    private String name;

    /**
    * 是否激活
    */
    private Integer isActive;

    /**
     * 学期开始日期
     */
    private Date startDate;

    /**
     * 学期结束日期
     */
    private Date endDate;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}