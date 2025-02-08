package com.caixy.adminSystem.model.vo.studentCourseSelection;

import com.caixy.adminSystem.model.vo.Subjects.SubjectsVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 选课科目VO
 *
 * @Author CAIXYPROMISE
 * @since 2025/2/7 23:54
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SelectionSubjectVO extends SubjectsVO
{
    /**
    * 已选人数
    */
    private Integer enrolledCount;
}
