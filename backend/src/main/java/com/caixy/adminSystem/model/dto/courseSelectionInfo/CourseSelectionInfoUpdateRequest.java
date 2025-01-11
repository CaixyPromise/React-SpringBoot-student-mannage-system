package com.caixy.adminSystem.model.dto.courseSelectionInfo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 更新选课信息表请求
 *


 */
@Data
public class CourseSelectionInfoUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

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