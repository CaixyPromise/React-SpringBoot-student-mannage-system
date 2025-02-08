package com.caixy.adminSystem.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caixy.adminSystem.common.ErrorCode;
import com.caixy.adminSystem.constant.CommonConstant;
import com.caixy.adminSystem.exception.BusinessException;
import com.caixy.adminSystem.exception.ThrowUtils;
import com.caixy.adminSystem.mapper.RegistrationTaskLessonMapper;
import com.caixy.adminSystem.mapper.RegistrationTaskMapper;
import com.caixy.adminSystem.model.dto.registrationTaskLesson.RegistrationTaskLessonPublicationRequest;
import com.caixy.adminSystem.model.dto.registrationTaskLesson.RegistrationTaskLessonQueryRequest;
import com.caixy.adminSystem.model.entity.RegistrationTaskLesson;

import com.caixy.adminSystem.model.enums.PulicationGradeEnum;
import com.caixy.adminSystem.model.vo.Subjects.SubjectsVO;
import com.caixy.adminSystem.model.vo.courseSelectionInfo.CourseSelectionInfoVO;
import com.caixy.adminSystem.model.vo.registrationTaskLesson.HasRegistrationTaskVO;
import com.caixy.adminSystem.model.vo.registrationTaskLesson.RegistrationTaskLessonVO;

import com.caixy.adminSystem.service.*;
import com.caixy.adminSystem.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 登分任务课程信息服务实现
 */
@Service
@Slf4j
public class RegistrationTaskLessonServiceImpl extends ServiceImpl<RegistrationTaskLessonMapper, RegistrationTaskLesson> implements RegistrationTaskLessonService
{

    @Resource
    private UserService userService;
    @Resource
    private CourseSelectionSubjectService courseSelectionSubjectService;
    @Resource
    private RegistrationTaskMapper registrationTaskMapper;
    @Resource
    private CourseSelectionInfoService courseSelectionInfoService;
    @Resource
    private SubjectsService subjectsService;

    /**
     * 校验数据
     *
     * @param registrationTaskLesson
     * @param add                    对创建的数据进行校验
     */
    @Override
    public void validRegistrationTaskLesson(RegistrationTaskLesson registrationTaskLesson, boolean add)
    {
        ThrowUtils.throwIf(registrationTaskLesson == null, ErrorCode.PARAMS_ERROR);

        // 修改数据时，有参数则校验
        // todo 补充校验规则
    }

    /**
     * 获取查询条件
     *
     * @param registrationTaskLessonQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<RegistrationTaskLesson> getQueryWrapper(
            RegistrationTaskLessonQueryRequest registrationTaskLessonQueryRequest)
    {
        QueryWrapper<RegistrationTaskLesson> queryWrapper = new QueryWrapper<>();
        if (registrationTaskLessonQueryRequest == null)
        {
            return queryWrapper;
        }
        // todo 从对象中取值
        Long id = registrationTaskLessonQueryRequest.getId();
        Long notId = registrationTaskLessonQueryRequest.getNotId();
        String title = registrationTaskLessonQueryRequest.getTitle();
        String content = registrationTaskLessonQueryRequest.getContent();
        String searchText = registrationTaskLessonQueryRequest.getSearchText();
        String sortField = registrationTaskLessonQueryRequest.getSortField();
        String sortOrder = registrationTaskLessonQueryRequest.getSortOrder();
        List<String> tagList = registrationTaskLessonQueryRequest.getTags();
        Long userId = registrationTaskLessonQueryRequest.getUserId();
        // todo 补充需要的查询条件
        // 从多字段中搜索
        if (StringUtils.isNotBlank(searchText))
        {
            // 需要拼接查询条件
            queryWrapper.and(qw -> qw.like("title", searchText).or().like("content", searchText));
        }
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        // JSON 数组查询
        if (CollUtil.isNotEmpty(tagList))
        {
            for (String tag : tagList)
            {
                queryWrapper.like("tags", "\"" + tag + "\"");
            }
        }
        // 精确查询
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取登分任务课程信息封装
     *
     * @param registrationTaskLesson
     * @param request
     * @return
     */
    @Override
    public List<RegistrationTaskLessonVO> getRegistrationLessonByTaskId(Long taskId)
    {
        LambdaQueryWrapper<RegistrationTaskLesson> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RegistrationTaskLesson::getTaskId, taskId);
        List<RegistrationTaskLesson> registrationTaskLessons = baseMapper.selectList(queryWrapper);
        if (CollUtil.isEmpty(registrationTaskLessons)) {
            return Collections.emptyList();
        }
        Set<Long> lessonId = new HashSet<>();
        Set<Long> courseTaskId = new HashSet<>();
        registrationTaskLessons.forEach(item -> {
            lessonId.add(item.getLessonId());
            courseTaskId.add(item.getCourseTaskId());
        });
        Map<Long, SubjectsVO> subjectsVOMap = subjectsService.getSubjectsVOMapByIds(lessonId);
        List<CourseSelectionInfoVO> courseTaskByIds = courseSelectionInfoService.getCourseTaskByIds(courseTaskId);
        Map<Long, CourseSelectionInfoVO> courseSelectionInfoVOMap = courseTaskByIds.stream().collect(Collectors.toMap(CourseSelectionInfoVO::getId, Function.identity()));
        return registrationTaskLessons.stream().map(item -> {
            RegistrationTaskLessonVO registrationTaskLessonVO = new RegistrationTaskLessonVO();
            BeanUtils.copyProperties(item, registrationTaskLessonVO);
            registrationTaskLessonVO.setSubjectsInfo(subjectsVOMap.get(item.getLessonId()));
            registrationTaskLessonVO.setCourseSelectionInfo(courseSelectionInfoVOMap.get(item.getCourseTaskId()));
            return registrationTaskLessonVO;
        }).collect(Collectors.toList());
    }

    /**
     * 分页获取登分任务课程信息封装
     *
     * @param registrationTaskLessonPage
     * @param request
     * @return
     */
    @Override
    public Page<RegistrationTaskLessonVO> getRegistrationTaskLessonVOPage(
            Page<RegistrationTaskLesson> registrationTaskLessonPage, HttpServletRequest request)
    {
        // todo: 补充分页获取登分任务课程信息封装逻辑
        return null;
    }

    /**
     * 获取是否有登分人物
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @version 2025/1/25 4:13
     */
    @Override
    public List<HasRegistrationTaskVO> getHasTasksBySubjectIdAndCourseTaskIds(Long subjectId, List<Long> courseTaskIds)
    {
        // 查询任务信息及其状态
        List<HasRegistrationTaskVO> taskList = baseMapper.selectHasRegistrationTaskBySubjectAndCourseTaskIds(subjectId, courseTaskIds);

        if (CollUtil.isEmpty(taskList)) {
            return Collections.emptyList();
        }

        // 当前时间
        Date currentDate = new Date();

        // 处理任务状态
        taskList.forEach(task -> {
            // 判断任务是否在有效日期范围内, 如果完成也是算作没有任务
            if (task.getStartDate() != null && task.getEndDate() != null || task.getIsFinished().equals(1)) {
                boolean isInRange = isDateInRange(task.getStartDate(), task.getEndDate(), currentDate);
                task.setHasTask(isInRange);
            } else {
                task.setHasTask(false); // 如果没有开始或结束时间，默认无任务
            }
        });

        return taskList;
    }

    /**
     * 发布成绩
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @version 2025/1/27 0:45
     */
    @Override
    public Boolean publicationGradeInfo(RegistrationTaskLessonPublicationRequest request) {
        List<Long> requestIds = request.getIds();
        List<RegistrationTaskLesson> registrationTaskLessons = baseMapper.selectBatchIds(requestIds);
        if (requestIds.size() != registrationTaskLessons.size()) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "登分任务课程信息不存在");
        }
        PulicationGradeEnum gradeEnum = PulicationGradeEnum.getEnumByCode(request.getState());
        if (gradeEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "发布状态错误");
        }
        LambdaUpdateWrapper<RegistrationTaskLesson> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(RegistrationTaskLesson::getIsPublish, gradeEnum.getCode());
        updateWrapper.in(RegistrationTaskLesson::getId, requestIds);
        return baseMapper.update(null, updateWrapper) > 0;
    }

    /**
     * 判断当前日期是否在范围内
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @version 2025/1/25 4:20
     */
    private boolean isDateInRange(Date startDate, Date endDate, Date fromDate) {
        return fromDate.after(startDate) && fromDate.before(endDate);
    }

}
