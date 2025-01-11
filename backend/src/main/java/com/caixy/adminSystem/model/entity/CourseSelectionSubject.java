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
 * 
 * @TableName course_selection_subject
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="course_selection_subject")
@Data
public class CourseSelectionSubject extends BaseEntity
{
    /**
     * 选课信息ID，关联course_selection_info表
     */
    private Long courseSelectionId;

    /**
     * 科目ID，关联subject表
     */
    private Long subjectId;

    /**
     * 最大选课人数, 为0时不限人数
     */
    private Integer maxStudents;

    /**
     * 当前选课人数
     */
    private Integer enrolledCount;
}