package com.caixy.adminSystem.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 专业信息表
 * @TableName major_info
 */
@TableName(value ="major_info")
@Data
public class MajorInfo implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 学院id
     */
    private Long departId;

    /**
     * 专业名称
     */
    private String name;

    /**
     * 创建该目录用户id
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

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}