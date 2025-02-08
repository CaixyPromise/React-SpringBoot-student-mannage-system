package com.caixy.adminSystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caixy.adminSystem.common.ErrorCode;
import com.caixy.adminSystem.exception.BusinessException;
import com.caixy.adminSystem.exception.ThrowUtils;
import com.caixy.adminSystem.mapper.CourseSelectionInfoMapper;
import com.caixy.adminSystem.mapper.RegistrationTaskMapper;
import com.caixy.adminSystem.model.dto.registrationTask.RegistrationTaskAddRequest;
import com.caixy.adminSystem.model.dto.registrationTask.RegistrationTaskQueryRequest;
import com.caixy.adminSystem.model.entity.*;

import com.caixy.adminSystem.model.vo.registrationTask.RegistrationTaskVO;

import com.caixy.adminSystem.model.vo.semesters.SemestersVO;
import com.caixy.adminSystem.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 登分任务服务实现
 */
@Service
@Slf4j
public class RegistrationTaskServiceImpl extends ServiceImpl<RegistrationTaskMapper, RegistrationTask> implements RegistrationTaskService
{

    @Resource
    private UserService userService;
    @Resource
    private CourseSelectionSubjectService courseSelectionSubjectService;
    @Resource
    private CourseSelectionInfoMapper courseSelectionInfoMapper;
    @Resource
    private RegistrationTaskLessonService registrationTaskLessonService;
    @Resource
    private SemestersService semestersService;

    private Date localDateTimeToDate(LocalDateTime localDateTime)
    {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean addRegistrationTask(RegistrationTaskAddRequest request, Long creatorId)
    {
        if (request.getStartDate() == null || request.getEndDate() == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "选课开始或结束时间不能为空");
        }
        if (request.getEndDate().isBefore(request.getStartDate()))
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "结束时间不能早于开始时间");
        }
        Long courseTaskId = request.getCourseTaskId();
        CourseSelectionInfo courseSelectionInfo = courseSelectionInfoMapper.selectOne(
                new LambdaQueryWrapper<CourseSelectionInfo>()
                        .eq(CourseSelectionInfo::getId, courseTaskId)
        );
        if (courseSelectionInfo == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "课程任务不存在");
        }
        List<Long> subjectIds = request.getSubjectIds();

        List<CourseSelectionSubject> courseSelectionSubjects = courseSelectionSubjectService.list(
                new LambdaQueryWrapper<CourseSelectionSubject>()
                        .in(CourseSelectionSubject::getSubjectId, subjectIds)
                        .eq(CourseSelectionSubject::getCourseSelectionId, courseTaskId));
        if (courseSelectionSubjects == null ||
            courseSelectionSubjects.isEmpty() ||
            courseSelectionSubjects.size() != subjectIds.size())
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "课程任务科目不存在");
        }

        RegistrationTask registrationTask = new RegistrationTask();
        BeanUtils.copyProperties(request, registrationTask);
        registrationTask.setCreatorId(creatorId);
        registrationTask.setStartDate(localDateTimeToDate(request.getStartDate()));
        registrationTask.setEndDate(localDateTimeToDate(request.getEndDate()));
        boolean saved = save(registrationTask);
        if (!saved) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "添加失败");
        }
        List<RegistrationTaskLesson> taskLessons = subjectIds.stream().map(item ->
        {
            RegistrationTaskLesson registrationTaskLesson = new RegistrationTaskLesson();
            registrationTaskLesson.setTaskId(registrationTask.getId());
            registrationTaskLesson.setLessonId(item);
            registrationTaskLesson.setCourseTaskId(request.getCourseTaskId());
            return registrationTaskLesson;
        }).collect(Collectors.toList());
        boolean saveLesson = registrationTaskLessonService.saveBatch(taskLessons);
        if (!saveLesson) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "添加失败");
        }
        return saveLesson && saved;
    }


    /**
     * 校验数据
     *
     * @param registrationTask
     * @param add              对创建的数据进行校验
     */
    @Override
    public void validRegistrationTask(RegistrationTask registrationTask, boolean add)
    {
        ThrowUtils.throwIf(registrationTask == null, ErrorCode.PARAMS_ERROR);

        // 修改数据时，有参数则校验
        // todo 补充校验规则
    }

    /**
     * 获取查询条件
     *
     * @param request
     * @return
     */
    @Override
    public LambdaQueryWrapper<RegistrationTask> getQueryWrapper(RegistrationTaskQueryRequest request) {
        LambdaQueryWrapper<RegistrationTask> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(request.getId() != null, RegistrationTask::getId, request.getId());
        queryWrapper.eq(request.getIsActive() != null, RegistrationTask::getIsActive, request.getIsActive());
        queryWrapper.like(StringUtils.isNotBlank(request.getName()), RegistrationTask::getName, request.getName());
        queryWrapper.eq(request.getSemesterName() != null, RegistrationTask::getSemesterId, request.getSemesterName());

        // 时间区间查询
        queryWrapper.ge(request.getStartDate() != null, RegistrationTask::getStartDate, request.getStartDate());
        queryWrapper.le(request.getEndDate() != null, RegistrationTask::getEndDate, request.getEndDate());

        return queryWrapper;
    }

    @Override
    public Page<RegistrationTaskVO> getPage(RegistrationTaskQueryRequest request) {
        long current = request.getCurrent();
        long size = request.getPageSize();
        Page<RegistrationTask> registrationTaskPage = page(new Page<>(current, size), getQueryWrapper(request));
        return getRegistrationTaskVOPage(registrationTaskPage);
    }


    /**
     * 获取登分任务封装
     *
     * @param registrationTask
     * @param request
     * @return
     */
    @Override
    public RegistrationTaskVO getRegistrationTaskVO(RegistrationTask registrationTask, HttpServletRequest request)
    {
        // todo: 补充获取登分任务封装逻辑
        return null;
    }

    /**
     * 分页获取登分任务封装
     *
     * @param registrationTaskPage
     * @return
     */
    @Override
    public Page<RegistrationTaskVO> getRegistrationTaskVOPage(Page<RegistrationTask> registrationTaskPage)
    {
        List<RegistrationTask> records = registrationTaskPage.getRecords();
        Set<Long> semesterIds = records.stream().map(RegistrationTask::getSemesterId).collect(Collectors.toSet());
        Map<Long, SemestersVO> semestersVOListByIds = semestersService.getSemestersVOListByIds(semesterIds);
        List<RegistrationTaskVO> registrationTaskVOS = records.stream().map(item -> {
            RegistrationTaskVO registrationTaskVO = new RegistrationTaskVO();
            BeanUtils.copyProperties(item, registrationTaskVO);
            registrationTaskVO.setSemestersInfo(semestersVOListByIds.get(item.getSemesterId()));
            return registrationTaskVO;
        }).collect(Collectors.toList());
        Page<RegistrationTaskVO> voPage = new Page<>(registrationTaskPage.getCurrent(), registrationTaskPage.getSize(), registrationTaskPage.getTotal());
        voPage.setRecords(registrationTaskVOS);
        return voPage;
    }

}
