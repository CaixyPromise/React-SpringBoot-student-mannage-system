package com.caixy.adminSystem.model.dto.registrationTaskLesson;

import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 发布成绩请求
 *
 * @Author CAIXYPROMISE
 * @since 2025/1/27 0:28
 */
@Data
public class RegistrationTaskLessonPublicationRequest implements Serializable
{
    /**
    * id
    */
    @Size(min = 1, message = "id不能为空")
    private List<Long> ids;

    private Integer state;

    private static final long serialVersionUID = 1L;
}
