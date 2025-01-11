package com.caixy.adminSystem.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 学生选课记录表
 * @TableName student_course_selection
 */
@TableName(value ="student_course_selection")
@Data
public class StudentCourseSelection implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 学生ID
     */
    private Long studentId;

    /**
     * 选课信息ID，关联course_selection_info表
     */
    private Long courseSelectionId;

    /**
    * 科目ID，关联subject表
    */
    private Long subjectId;

    /**
     * 选课时间
     */
    private Date selectTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}