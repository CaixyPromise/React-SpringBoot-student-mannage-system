package com.caixy.adminSystem.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName subjects
 */
@TableName(value = "subjects")
@Data
public class Subjects implements Serializable
{
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 科目名称
     */
    private String name;

    /**
     * 最高分
     */
    private Long gradeMax;

    /**
     * 最低分
     */
    private Long gradeMin;

    /**
     * 不及格分数线
     */
    private Long gradeFail;

    /**
     * 优秀分数线
     */
    private Long gradeExcellent;

    /**
     * 创建人id
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
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}