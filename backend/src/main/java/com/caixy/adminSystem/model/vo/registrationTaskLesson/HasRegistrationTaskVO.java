package com.caixy.adminSystem.model.vo.registrationTaskLesson;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 选课任务查询结果VO
 *
 * @Author CAIXYPROMISE
 * @since 2025/1/25 3:36
 */
@Data
public class HasRegistrationTaskVO implements Serializable
{
    /**
    * 任务id
    */
    private Long taskId;
    
    /**
    * 任务名称
    */
    private String name;
    
    /**
     * 科目ID
     */
    private Long courseTaskId;
    /**
    * 有任务
    */
    private Boolean hasTask;

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


    /**
     * 登记开始日期
     */
    private Date startDate;

    /**
     * 登记结束日期
     */
    private Date endDate;
    
    /**
    * 学期id
    */
    private Long semesterId;
    private static final long serialVersionUID = 1L;
}
