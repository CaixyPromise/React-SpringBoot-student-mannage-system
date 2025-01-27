package com.caixy.adminSystem.model.dto.subject;

import lombok.Data;

/**
 * 课程上课时间
 * @Author CAIXYPROMISE
 * @since 2025/1/17 23:48
 */
@Data
public class SubjectClassTime
{
    /**
     * 星期几
     */
    private Integer dayOfWeek;
    /**
     * 节次
     */
    private String period;
}
