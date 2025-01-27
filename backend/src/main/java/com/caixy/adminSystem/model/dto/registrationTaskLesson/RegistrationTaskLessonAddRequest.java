package com.caixy.adminSystem.model.dto.registrationTaskLesson;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 创建登分任务课程信息请求
 *


 */
@Data
public class RegistrationTaskLessonAddRequest implements Serializable {

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表
     */
    private List<String> tags;

    private static final long serialVersionUID = 1L;
}