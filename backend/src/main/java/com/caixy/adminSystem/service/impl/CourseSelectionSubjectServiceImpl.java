package com.caixy.adminSystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caixy.adminSystem.mapper.SubjectsMapper;
import com.caixy.adminSystem.model.entity.CourseSelectionSubject;
import com.caixy.adminSystem.model.entity.Subjects;
import com.caixy.adminSystem.model.vo.Subjects.SubjectsVO;
import com.caixy.adminSystem.model.vo.courseSelectionSubject.CourseSelectionSubjectVO;
import com.caixy.adminSystem.service.CourseSelectionSubjectService;
import com.caixy.adminSystem.mapper.CourseSelectionSubjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author CAIXYPROMISE
 * @description 针对表【course_selection_subject】的数据库操作Service实现
 * @createDate 2025-01-10 01:42:59
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CourseSelectionSubjectServiceImpl extends ServiceImpl<CourseSelectionSubjectMapper, CourseSelectionSubject>
        implements CourseSelectionSubjectService
{
    private final SubjectsMapper subjectsMapper;
    /**
     * 根据选课任务Id查找选课科目
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @version 2025/1/21 1:53
     */
    @Override
    public List<SubjectsVO> getSelectSubjectByTaskId(Long taskId)
    {
        LambdaQueryWrapper<CourseSelectionSubject> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CourseSelectionSubject::getCourseSelectionId, taskId);
        List<CourseSelectionSubject> selectionSubjectList = this.list(wrapper);
        if (selectionSubjectList.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<Subjects> subjectWrapper = new LambdaQueryWrapper<>();
        subjectWrapper.in(Subjects::getId, selectionSubjectList.stream().map(CourseSelectionSubject::getSubjectId).collect(
                Collectors.toList()));
        List<Subjects> subjectsList = subjectsMapper.selectList(subjectWrapper);
        return subjectsList.stream().map(item -> {
            SubjectsVO subjectsVO = new SubjectsVO();
            BeanUtils.copyProperties(item, subjectsVO);
            return subjectsVO;
        }).collect(Collectors.toList());
    }
}




