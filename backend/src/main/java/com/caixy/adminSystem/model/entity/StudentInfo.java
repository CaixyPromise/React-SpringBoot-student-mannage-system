package com.caixy.adminSystem.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName student_info
 */
@TableName(value ="student_info")
@Data
public class StudentInfo implements Serializable {
    /**
     * 学生ID
     */
    @TableId
    private Long id;

    /**
     * 学生姓名
     */
    private String stuName;

    /**
     * 学生性别
     */
    private Integer stuSex;

    /**
     * 学生学院id
     */
    private Long stuDeptId;

    /**
     * 学生专业id
     */
    private Long stuMajorId;

    /**
     * 学生班级Id
     */
    private Long stuClassId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}