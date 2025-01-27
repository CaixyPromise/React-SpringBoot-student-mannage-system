package com.caixy.adminSystem.service;

import com.caixy.adminSystem.model.entity.CourseSelectionSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.caixy.adminSystem.model.vo.Subjects.SubjectsVO;
import com.caixy.adminSystem.model.vo.courseSelectionSubject.CourseSelectionSubjectVO;

import java.util.List;

/**
* @author CAIXYPROMISE
* @description 针对表【course_selection_subject】的数据库操作Service
* @createDate 2025-01-10 01:42:59
*/
public interface CourseSelectionSubjectService extends IService<CourseSelectionSubject> {

    List<SubjectsVO> getSelectSubjectByTaskId(Long taskId);
}
