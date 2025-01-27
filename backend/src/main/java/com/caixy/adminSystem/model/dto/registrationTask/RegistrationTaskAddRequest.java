package com.caixy.adminSystem.model.dto.registrationTask;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 创建登分任务请求
 *


 */
@Data
public class RegistrationTaskAddRequest implements Serializable {

    /**
     * 任务名称
     */
    @NotBlank(message = "任务名称不能为空")
    @Max(20)
    private String name;

    /**
     * 学期id
     */
    @NotNull(message = "学期不能为空")
    private Long semesterId;

    /**
    * 课程任务id
    */
    @NotNull(message = "课程任务不能为空")
    private Long courseTaskId;

    /**
     * 是否激活状态
     */
    @Max(1)
    @Min(0)
    private Integer isActive;

    /**
    * 科目ids
    */
    @Size(min = 1, message = "科目不能为空")
    private List<Long> subjectIds;

    /**
     * 选课开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startDate;


    /**
     * 选课结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Future(message = "结束时间不能早于当前时间")
    private LocalDateTime endDate;

    private static final long serialVersionUID = 1L;
}