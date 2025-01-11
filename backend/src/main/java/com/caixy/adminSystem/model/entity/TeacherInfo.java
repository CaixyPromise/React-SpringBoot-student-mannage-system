package com.caixy.adminSystem.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.caixy.adminSystem.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 教师信息表
 * @TableName teacher_info
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="teacher_info")
@Data
public class TeacherInfo extends BaseEntity
{
    /**
     * 学号
     */
    private String teacherId;

    /**
     * 教师姓名
     */
    private String teacherName;

    /**
     * 教师性别
     */
    private Integer teacherSex;

    /**
     * 教师学院id
     */
    private Long teacherDeptId;

    /**
     * 教师专业id
     */
    private Long teacherMajorId;

    /**
     * 创建人Id
     */
    private Long creatorId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}