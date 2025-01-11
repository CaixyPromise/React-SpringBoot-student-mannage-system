package com.caixy.adminSystem.model.dto.studentCourseSelection;

import lombok.Data;

import java.util.List;

/**
 * 学生发起选课
 *
 * @Author CAIXYPROMISE
 * @since 2025/1/11 2:57
 */
@Data
public class StudentSelectCourseRequest {
    private Long courseSelectionId;      // 选课任务ID
    private List<Long> subjectIds;       // 学生选的科目ID列表（多选）
}