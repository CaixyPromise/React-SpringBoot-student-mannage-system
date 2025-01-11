package com.caixy.adminSystem.model.dto.semesters;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 创建学期信息表请求
 */
@Data
public class SemestersAddRequest implements Serializable {

    /**
     * 学期名称
     */
    private String name;

    /**
     * 学期开始日期
     */
    private Date startDate;

    /**
     * 学期结束日期
     */
    private Date endDate;

    private static final long serialVersionUID = 1L;
}
