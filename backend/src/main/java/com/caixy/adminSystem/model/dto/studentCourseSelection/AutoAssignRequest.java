package com.caixy.adminSystem.model.dto.studentCourseSelection;

import lombok.Data;

import java.io.Serializable;

/**
 * 自动分配请求
 *
 * @Author CAIXYPROMISE
 * @since 2025/2/8 0:48
 */
@Data
public class AutoAssignRequest implements Serializable
{
    private Long courseSelectionId;
}
