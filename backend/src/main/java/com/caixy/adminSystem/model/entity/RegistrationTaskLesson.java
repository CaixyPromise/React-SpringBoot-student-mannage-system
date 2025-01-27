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
 * 登分任务课程信息表
 * @TableName registration_task_lesson
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="registration_task_lesson")
@Data
public class RegistrationTaskLesson extends BaseEntity
{

    /**
     * 登分任务id
     */
    private Long taskId;

    /**
     * 课程id
     */
    private Long lessonId;

    /**
     * 课程任务id
     */
    private Long courseTaskId;
    /**
     * 是否登分完成
     */
    private Integer isFinished;

    /**
     * 是否发布成绩
     */
    private Integer isPublish;

    /**
     * 完成日期
     */
    private Date finishedTime;

}