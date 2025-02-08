package com.caixy.adminSystem.model.dto.registrationTask;

import com.caixy.adminSystem.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 查询登分任务请求
 *


 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RegistrationTaskQueryRequest extends PageRequest implements Serializable {


    /**
     * 任务ID
     */
    private Long id;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 是否激活 0-未激活 1-激活
     */
    private Integer isActive;

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