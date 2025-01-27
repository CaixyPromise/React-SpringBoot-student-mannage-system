package com.caixy.adminSystem.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.caixy.adminSystem.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 登分任务表
 * @TableName registration_task
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="registration_task")
@Data
public class RegistrationTask extends BaseEntity
{
    /**
     * 任务名称
     */
    private String name;

    /**
     * 学期id
     */
    private Long semesterId;


    /**
     * 添加人信息
     */
    private Long creatorId;

    /**
     * 是否激活状态
     */
    private Integer isActive;

    /**
     * 登记开始日期
     */
    private Date startDate;

    /**
     * 登记结束日期
     */
    private Date endDate;
}