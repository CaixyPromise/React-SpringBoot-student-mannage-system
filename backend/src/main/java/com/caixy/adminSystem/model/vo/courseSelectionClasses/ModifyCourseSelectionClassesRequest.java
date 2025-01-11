package com.caixy.adminSystem.model.vo.courseSelectionClasses;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 修改选课任务请求体
 *
 * @Author CAIXYPROMISE
 * @since 2025/1/3 15:26
 */
@Data
public class ModifyCourseSelectionClassesRequest {
    @NotNull(message = "选课任务ID不能为空")
    private Long courseSelectionId; // 选课任务ID

    @NotEmpty(message = "班级ID列表不能为空")
    private List<Long> classIds; // 班级ID列表
}
