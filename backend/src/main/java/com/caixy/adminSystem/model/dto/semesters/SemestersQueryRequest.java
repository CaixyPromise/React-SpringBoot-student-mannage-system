package com.caixy.adminSystem.model.dto.semesters;

import com.caixy.adminSystem.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 查询学期信息表请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SemestersQueryRequest extends PageRequest implements Serializable {

    /**
     * 学期ID
     */
    private Long id;

    /**
     * 学期名称（模糊查询）
     */
    private String name;

    /**
     * 是否激活
     */
    private Integer isActive;

    /**
     * 开始日期范围 - 起
     */
    private Date startDateBegin;

    /**
     * 开始日期范围 - 止
     */
    private Date startDateEnd;

    /**
     * 结束日期范围 - 起
     */
    private Date endDateBegin;

    /**
     * 结束日期范围 - 止
     */
    private Date endDateEnd;

    private static final long serialVersionUID = 1L;
}
