package com.caixy.adminSystem.model.dto.courseSelectionInfo;

import com.caixy.adminSystem.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 查询选课信息表请求
 *


 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CourseSelectionInfoQueryRequest extends PageRequest implements Serializable {
    /**
     * 任务ID
     */
    private Long id;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 学期名称
     */
    private Long semesterName;

    /**
     * 开始日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;

    /**
     * 结束日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;

    private static final long serialVersionUID = 1L;
}