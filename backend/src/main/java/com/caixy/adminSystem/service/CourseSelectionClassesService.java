package com.caixy.adminSystem.service;

import com.caixy.adminSystem.model.entity.CourseSelectionClasses;
import com.baomidou.mybatisplus.extension.service.IService;
import com.caixy.adminSystem.model.vo.courseSelectionClasses.ModifyCourseSelectionClassesRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author CAIXYPROMISE
* @description 针对表【course_selection_classes】的数据库操作Service
* @createDate 2025-01-03 01:23:48
*/
public interface CourseSelectionClassesService extends IService<CourseSelectionClasses> {

    @Transactional(rollbackFor = Exception.class)
    void addClassesToCourseSelection(ModifyCourseSelectionClassesRequest request);

    List<Long> getClassesByCourseSelectionId(Long courseSelectionId);
}
