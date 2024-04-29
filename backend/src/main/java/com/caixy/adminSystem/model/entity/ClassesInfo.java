package com.caixy.adminSystem.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 班级信息
 * @TableName classes_info
 */
@TableName(value ="classes_info")
@Data
public class ClassesInfo implements Serializable {
    /**
     * 主键Id
     */
    @TableId
    private Long id;

    /**
     * 班级名称
     */
    private String name;

    /**
     * 学院id
     */
    private Long departId;

    /**
     * 专业id
     */
    private Long majorId;

    /**
     * 创建人Id
     */
    private Long creatorId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 逻辑删除键
     */
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}