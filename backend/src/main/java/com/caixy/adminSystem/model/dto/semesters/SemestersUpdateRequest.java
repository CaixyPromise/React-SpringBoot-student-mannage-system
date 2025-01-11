package com.caixy.adminSystem.model.dto.semesters;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 更新学期信息表请求
 */
@Data
public class SemestersUpdateRequest implements Serializable {

    /**
     * 主键 ID
     */
    private Long id;

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
