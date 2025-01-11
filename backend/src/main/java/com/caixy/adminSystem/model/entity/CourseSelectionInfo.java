package com.caixy.adminSystem.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.caixy.adminSystem.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 选课信息表
 * @TableName course_selection_info
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="course_selection_info")
@Data
public class CourseSelectionInfo extends BaseEntity {

    /**
     * 学期ID，关联semesters表
     */
    private Long semesterId;

    /**
     * 选课任务名称
     */
    private String taskName;

    /**
     * 选课学分最小值
     */
    private BigDecimal minCredit;



    /**
     * 选课开始时间
     */
    private Date startDate;

    /**
     * 选课结束时间
     */
    private Date endDate;

    /**
     * 创建人ID
     */
    private Long creatorId;

    /**
    * 任务状态 1-正常 0-搁置(暂停执行) 和 删除不同的是，删除是废弃，搁置是可能重新开启
    */
    private Integer isActive;
}