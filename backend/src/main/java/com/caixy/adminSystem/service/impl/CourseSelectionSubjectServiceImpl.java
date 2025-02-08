package com.caixy.adminSystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caixy.adminSystem.common.ErrorCode;
import com.caixy.adminSystem.exception.BusinessException;
import com.caixy.adminSystem.mapper.SubjectsMapper;
import com.caixy.adminSystem.model.entity.CourseSelectionSubject;
import com.caixy.adminSystem.model.entity.Subjects;
import com.caixy.adminSystem.model.vo.Subjects.SubjectsVO;
import com.caixy.adminSystem.model.vo.studentCourseSelection.SelectionSubjectVO;
import com.caixy.adminSystem.service.CourseSelectionSubjectService;
import com.caixy.adminSystem.mapper.CourseSelectionSubjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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

    @Override
    @Transactional
    public void batchUpdateEnrolledCount(Long courseSelectionId, Map<Long, Integer> additionalCounts) {
        if (additionalCounts.isEmpty()) {
            return;
        }

        List<CourseSelectionSubject> updates = additionalCounts.entrySet().stream()
                .map(entry -> {
                    CourseSelectionSubject update = new CourseSelectionSubject();
                    update.setCourseSelectionId(courseSelectionId);
                    update.setSubjectId(entry.getKey());
                    update.setEnrolledCount(entry.getValue()); // SQL 直接累加
                    return update;
                }).collect(Collectors.toList());

        int updatedRows = baseMapper.batchUpdateEnrolledCount(updates);
        if (updatedRows != updates.size()) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "批量更新选课人数失败");
        }

        log.info("批量更新课程已选人数：{} 门课程已更新", updates.size());
    }


    /**
     * 根据选课任务Id查找选课科目
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @version 2025/1/21 1:53
     */
    @Override
    public List<SubjectsVO> getSubjectVOByTaskId(Long taskId)
    {
        if (taskId == null || taskId < 0) {
            return Collections.emptyList();
        }
        List<CourseSelectionSubject> selectionSubjectList = baseMapper.findCourseSelectionSubjectByCourseSelectionId(taskId);
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


    /**
     * 根据选课任务Id查找选课科目
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @version 2025/1/21 1:53
     */
    @Override
    public List<SelectionSubjectVO> getSelectSubjectVOByTaskId(Long taskId)
    {
        if (taskId == null || taskId < 0) {
            return Collections.emptyList();
        }
        List<CourseSelectionSubject> selectionSubjectList = baseMapper.findCourseSelectionSubjectByCourseSelectionId(taskId);
        if (selectionSubjectList.isEmpty()) {
            return Collections.emptyList();
        }
        Map<Long, CourseSelectionSubject> selectionSubjectMap = selectionSubjectList.stream().collect(Collectors.toMap(CourseSelectionSubject::getSubjectId, Function.identity()));
        LambdaQueryWrapper<Subjects> subjectWrapper = new LambdaQueryWrapper<>();
        subjectWrapper.in(Subjects::getId, selectionSubjectList.stream().map(CourseSelectionSubject::getSubjectId).collect(
                Collectors.toList()));
        List<Subjects> subjectsList = subjectsMapper.selectList(subjectWrapper);
        return subjectsList.stream().map(item -> {
            SelectionSubjectVO subjectsVO = new SelectionSubjectVO();
            BeanUtils.copyProperties(item, subjectsVO);
            subjectsVO.setEnrolledCount(selectionSubjectMap.get(item.getId()).getEnrolledCount());
            return subjectsVO;
        }).collect(Collectors.toList());
    }
}




