package com.caixy.adminSystem.model.dto.registrationTaskLesson;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 查询登分任务请求
 *
 * @Author CAIXYPROMISE
 * @since 2025/1/25 3:32
 */
@Data
public class HasRegistrationTaskRequest implements Serializable
{
    /**
    * 科目id
    */
    @NotNull
    private Long subjectId;

    /**
    * 课程ids
    */
    @NotNull
    private List<Long> courseTaskIds;
    private static final long serialVersionUID = 1L;
}
