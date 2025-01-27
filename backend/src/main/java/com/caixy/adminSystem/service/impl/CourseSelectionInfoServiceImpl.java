package com.caixy.adminSystem.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caixy.adminSystem.common.ErrorCode;
import com.caixy.adminSystem.constant.CommonConstant;
import com.caixy.adminSystem.exception.BusinessException;
import com.caixy.adminSystem.exception.ThrowUtils;
import com.caixy.adminSystem.mapper.*;
import com.caixy.adminSystem.model.dto.courseSelectionInfo.CourseSelectionInfoQueryRequest;
import com.caixy.adminSystem.model.dto.courseSelectionInfo.CreateCourseSelectionRequest;
import com.caixy.adminSystem.model.dto.subject.SubjectClassTime;
import com.caixy.adminSystem.model.entity.*;

import com.caixy.adminSystem.model.vo.Subjects.CourseSelectSubjectVO;
import com.caixy.adminSystem.model.vo.Subjects.SubjectsVO;
import com.caixy.adminSystem.model.vo.courseSelectionInfo.CourseSelectionInfoVO;

import com.caixy.adminSystem.model.vo.teacherInfo.TeacherInfoVO;
import com.caixy.adminSystem.service.*;
import com.caixy.adminSystem.utils.JsonUtils;
import com.caixy.adminSystem.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 选课信息表服务实现
 */
@Service
@Slf4j
public class CourseSelectionInfoServiceImpl extends ServiceImpl<CourseSelectionInfoMapper, CourseSelectionInfo> implements CourseSelectionInfoService
{
    @Resource
    private SubjectsService subjectService;  // 科目表 service
    @Resource
    private SemestersService semesterService; // 学期表 service
    @Resource
    private ClassesInfoService classesService;   // 班级表 service
    @Resource
    private CourseSelectionClassesService courseSelectionClassesService; // 新建的“选课-班级”关联表 service
    @Resource
    private CourseSelectionInfoMapper courseSelectionInfoMapper;
    @Resource
    private SubjectsMapper subjectMapper;           // 用于查科目名称
    @Resource
    private SemestersMapper semestersMapper;       // 用于查学期名称
    @Resource
    private CourseSelectionSubjectService courseSelectionSubjectService; // 选课-科目关联表 service
    @Resource
    private CourseSelectionSubjectMapper courseSelectionSubjectMapper;
    @Resource
    private StudentInfoMapper studentInfoMapper;
    @Resource
    private TeacherInfoMapper teacherInfoMapper;

    private Date localDateTimeToDate(LocalDateTime localDateTime)
    {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 校验数据
     *
     * @param courseSelectionInfo
     * @param add                 对创建的数据进行校验
     */
    @Override
    public void validCourseSelectionInfo(CourseSelectionInfo courseSelectionInfo, boolean add)
    {
        ThrowUtils.throwIf(courseSelectionInfo == null, ErrorCode.PARAMS_ERROR);

        // 修改数据时，有参数则校验
        // todo 补充校验规则
    }

    /**
     * 获取查询条件
     *
     * @param courseSelectionInfoQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<CourseSelectionInfo> getQueryWrapper(
            CourseSelectionInfoQueryRequest courseSelectionInfoQueryRequest)
    {
        QueryWrapper<CourseSelectionInfo> queryWrapper = new QueryWrapper<>();
        if (courseSelectionInfoQueryRequest == null)
        {
            return queryWrapper;
        }
        // todo 从对象中取值
        Long id = courseSelectionInfoQueryRequest.getId();
        Long notId = courseSelectionInfoQueryRequest.getNotId();
        String title = courseSelectionInfoQueryRequest.getTitle();
        String content = courseSelectionInfoQueryRequest.getContent();
        String searchText = courseSelectionInfoQueryRequest.getSearchText();
        String sortField = courseSelectionInfoQueryRequest.getSortField();
        String sortOrder = courseSelectionInfoQueryRequest.getSortOrder();
        List<String> tagList = courseSelectionInfoQueryRequest.getTags();
        Long userId = courseSelectionInfoQueryRequest.getUserId();
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
     * 获取选课信息表封装
     *
     * @param courseSelectionInfo
     * @param request
     * @return
     */
    @Override
    public CourseSelectionInfoVO getCourseSelectionInfoVO(CourseSelectionInfo courseSelectionInfo,
                                                          HttpServletRequest request)
    {
        CourseSelectionInfoVO courseSelectionInfoVO = new CourseSelectionInfoVO();
        BeanUtil.copyProperties(courseSelectionInfo, courseSelectionInfoVO);
        return courseSelectionInfoVO;
    }

    /**
     * 分页获取选课信息表封装
     *
     * @param courseSelectionInfoPage
     * @param request
     * @return
     */
    @Override
    public Page<CourseSelectionInfoVO> getCourseSelectionInfoVOPage(Page<CourseSelectionInfo> courseSelectionInfoPage,
                                                                    HttpServletRequest request)
    {
        // todo: 补充分页获取选课信息表封装逻辑
        return null;
    }

    /**
     * 创建选课信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createCourseSelection(CreateCourseSelectionRequest request, Long currentUserId)
    {
        // 1. 基本非空校验
        if (request == null || request.getCourseSettings() == null || request.getCourseSettings().isEmpty()
            || request.getSemesterId() == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数错误：subjectId或semesterId不能为空");
        }
        if (request.getClassIds() == null || request.getClassIds().isEmpty())
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请选择至少一个班级");
        }
        if (request.getStartDate() == null || request.getEndDate() == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "选课开始或结束时间不能为空");
        }
        if (request.getEndDate().isBefore(request.getStartDate()))
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "结束时间不能早于开始时间");
        }
        BigDecimal minCredit = request.getMinCredit();
        if (minCredit == null || minCredit.compareTo(BigDecimal.ZERO) < 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "最小学分必须 >= 0");
        }
        String taskName = request.getTaskName();
        if (StringUtils.isBlank(taskName) || taskName.length() > 20)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "任务名称不能为空且长度不能超过20");
        }

        // 2. 验证科目列表是否存在，并返回不存在的科目ID
        List<CreateCourseSelectionRequest.SelectCourseData> requestedSubject = request.getCourseSettings();
        Set<Long> requestedSubjectIds = requestedSubject.stream()
                                                        .map(CreateCourseSelectionRequest.SelectCourseData::getCourseId)
                                                        .collect(Collectors.toSet());
        List<Subjects> subjects = subjectService.listByIds(requestedSubjectIds);
        if (subjects == null || subjects.size() != requestedSubjectIds.size())
        {
            Set<Long> foundIds = subjects == null
                                 ? new HashSet<>()
                                 : subjects.stream().map(Subjects::getId).collect(Collectors.toSet());
            List<Long> missingIds = requestedSubjectIds.stream()
                                                       .filter(id -> !foundIds.contains(id))
                                                       .collect(Collectors.toList());
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "科目不存在, id=" + missingIds);
        }

        // 3. 验证学期是否存在
        Semesters semester = semesterService.getById(request.getSemesterId());
        if (semester == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "学期不存在, id=" + request.getSemesterId());
        }

        // 4. 验证班级ID列表是否有效
        List<Long> classIds = request.getClassIds();
        long validCount = classesService.count(
                Wrappers.<ClassesInfo>lambdaQuery()
                        .in(ClassesInfo::getId, classIds)
                        .eq(ClassesInfo::getIsDelete, 0)
        );
        if (validCount != classIds.size())
        {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "部分班级ID无效或已被删除");
        }

        // 5. 验证最小学分要求是否符合已选科目的学分范围
        BigDecimal sumCredits = subjects.stream()
                                        .map(s -> s.getGradeCredit() == null ? BigDecimal.ZERO : new BigDecimal(
                                                s.getGradeCredit().toString()))
                                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal minSubjectCredit = subjects.stream()
                                              .map(s -> s.getGradeCredit() == null ? BigDecimal.valueOf(
                                                      Double.MAX_VALUE) : new BigDecimal(s.getGradeCredit().toString()))
                                              .min(Comparator.naturalOrder())
                                              .orElse(BigDecimal.ZERO);

        if (minCredit.compareTo(minSubjectCredit) < 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,
                    "最小学分要求不能低于已选科目中的最小学分 " + minSubjectCredit);
        }
        if (minCredit.compareTo(sumCredits) > 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "最小学分要求不能高于已选科目总学分 " + sumCredits);
        }

        // 收集所有teacherId
        Set<Long> allTeacherIds = request.getCourseSettings().stream()
                                         .map(CreateCourseSelectionRequest.SelectCourseData::getClassTeacher)
                                         .filter(Objects::nonNull)
                                         .collect(Collectors.toSet());

        // 如果有老师ID，就一次性查表做对比
        if (!allTeacherIds.isEmpty())
        {
            Long count = teacherInfoMapper.selectCount(
                    Wrappers.lambdaQuery(TeacherInfo.class).in(TeacherInfo::getId, allTeacherIds)
            );
            if (!count.equals((long) allTeacherIds.size()))
            {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "存在无效的教师ID");
            }
        }
        // 校验课程时间
        request.getCourseSettings().stream()
               .map(CreateCourseSelectionRequest.SelectCourseData::getClassTimes)
               .forEach(this::validateClassTimes);
        // 检查教室都不能为空且长度符合要求
        request.getCourseSettings().stream()
               .map(CreateCourseSelectionRequest.SelectCourseData::getClassRoom)
               .forEach(classRoom ->
               {
                   if (classRoom == null || classRoom.isEmpty() || classRoom.length() > 20)
                   {
                       throw new BusinessException(ErrorCode.PARAMS_ERROR, "教室不能为空且长度不能大于20");
                   }
               });
        // 6. 插入选课信息表 (course_selection_info)，不设置subjectId字段
        CourseSelectionInfo entity = new CourseSelectionInfo();
        entity.setSemesterId(request.getSemesterId());
        entity.setTaskName(request.getTaskName());
        entity.setMinCredit(minCredit);
        entity.setStartDate(localDateTimeToDate(request.getStartDate()));
        entity.setEndDate(localDateTimeToDate(request.getEndDate()));
        entity.setCreatorId(currentUserId);

        this.save(entity);
        Long newCourseSelectionId = entity.getId();
        if (newCourseSelectionId == null)
        {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "插入选课信息表失败");
        }

        // 7. 批量插入到“选课-班级”关系表 (course_selection_classes)
        List<CourseSelectionClasses> mappingList = classIds.stream().map(cid ->
        {
            CourseSelectionClasses csc = new CourseSelectionClasses();
            csc.setCourseSelectionId(newCourseSelectionId);
            csc.setClassId(cid);
            return csc;
        }).collect(Collectors.toList());
        boolean batchSaved = courseSelectionClassesService.saveBatch(mappingList);
        if (!batchSaved)
        {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "批量插入选课-班级关联信息失败");
        }

        // 8. 批量插入到“选课-科目”关系表 (course_selection_subject)
        List<CourseSelectionSubject> subjectMappingList = requestedSubject.stream().map(selectSubject ->
        {
            CourseSelectionSubject css = new CourseSelectionSubject();
            css.setCourseSelectionId(newCourseSelectionId);
            css.setSubjectId(selectSubject.getCourseId());
            css.setMaxStudents(selectSubject.getMaxStudents());
            css.setEnrolledCount(0);
            css.setIsDelete(0);

            css.setTeacherId(selectSubject.getClassTeacher());
            css.setClassRoom(selectSubject.getClassRoom());
            css.setClassTimes(JsonUtils.toJsonString(selectSubject.getClassTimes()));

            Date now = new Date();
            css.setCreateTime(now);
            css.setUpdateTime(now);
            return css;
        }).collect(Collectors.toList());
        boolean subjectBatchSaved = courseSelectionSubjectService.saveBatch(subjectMappingList);
        if (!subjectBatchSaved)
        {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "批量插入选课-科目关联信息失败");
        }
        // 9. 返回新创建的选课任务ID
        return newCourseSelectionId;
    }

    @Override
    public Page<CourseSelectionInfoVO> pageCourseSelection(int pageNum, int pageSize, Long semesterId, String taskName)
    {
        // 构建分页对象
        Page<CourseSelectionInfo> page = new Page<>(pageNum, pageSize);

        // 构建查询条件
        LambdaQueryWrapper<CourseSelectionInfo> wrapper = Wrappers.lambdaQuery(CourseSelectionInfo.class)
                                                                  .eq(CourseSelectionInfo::getIsDelete, 0)
                                                                  .orderByDesc(CourseSelectionInfo::getCreateTime);
        if (semesterId != null)
        {
            wrapper.eq(CourseSelectionInfo::getSemesterId, semesterId);
        }
        if (StringUtils.isNotBlank(taskName))
        {
            wrapper.like(CourseSelectionInfo::getTaskName, taskName);
        }

        // 查询分页数据
        IPage<CourseSelectionInfo> resultPage = courseSelectionInfoMapper.selectPage(page, wrapper);

        // 调用公共方法组装 VO
        List<CourseSelectionInfoVO> voList = assembleCourseSelectionInfoVOs(resultPage.getRecords());

        // 返回分页结果
        Page<CourseSelectionInfoVO> voPage = new Page<>(pageNum, pageSize);
        voPage.setRecords(voList);
        voPage.setTotal(resultPage.getTotal());
        voPage.setCurrent(resultPage.getCurrent());
        voPage.setSize(resultPage.getSize());
        voPage.setPages(resultPage.getPages());

        return voPage;
    }


    /**
     * 搁置任务
     *
     * @author CAIXYPROMISE
     * @version 1.0
     */
    @Override
    public Boolean putTaskHoldById(Long taskId)
    {
        CourseSelectionInfo courseSelectionInfoById = getCourseSelectionInfoById(taskId);
        if (courseSelectionInfoById.getIsActive() == 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "选课任务已经处于搁置状态");
        }
        courseSelectionInfoById.setIsActive(0);
        return updateById(courseSelectionInfoById);
    }

    /**
     * 激活任务
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @version 2025/1/10 3:29
     */
    @Override
    public Boolean putTaskActiveById(Long taskId)
    {
        CourseSelectionInfo courseSelectionInfoById = getCourseSelectionInfoById(taskId);
        if (courseSelectionInfoById.getIsActive() == 1)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "选课任务已经处于激活状态");
        }
        courseSelectionInfoById.setIsActive(1);
        return updateById(courseSelectionInfoById);
    }

    /**
     * 根据id批量查询选课任务
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @version 2025/1/26 23:53
     */
    @Override
    public List<CourseSelectionInfoVO> getCourseTaskByIds(Collection<Long> courseSelectionIds) {
        LambdaQueryWrapper<CourseSelectionInfo> wrapper = Wrappers.lambdaQuery(CourseSelectionInfo.class);
        wrapper.in(CourseSelectionInfo::getId, courseSelectionIds);
        List<CourseSelectionInfo> courseSelectionInfos = courseSelectionInfoMapper.selectList(wrapper);
        return assembleCourseSelectionInfoVOs(courseSelectionInfos);
    }

    @Override
    public List<CourseSelectSubjectVO> getSelectTaskCourses(Long taskId)
    {
        // Step 1: 根据选课任务 ID 查询科目基本信息列表
        List<SubjectsVO> subjects = courseSelectionSubjectMapper.getSubjectsByCourseSelectionId(taskId);
        if (CollectionUtils.isEmpty(subjects))
        {
            return Collections.emptyList();
        }

        // Step 2: 查询 course_selection_subject 表的映射记录
        List<CourseSelectionSubject> mappings = courseSelectionSubjectMapper.selectList(
                Wrappers.<CourseSelectionSubject>lambdaQuery()
                        .eq(CourseSelectionSubject::getCourseSelectionId, taskId)
                        .eq(CourseSelectionSubject::getIsDelete, 0)
        );
        if (CollectionUtils.isEmpty(mappings))
        {
            return Collections.emptyList();
        }

        // Step 3: 将映射按 subjectId 索引，方便后续查找
        Map<Long, CourseSelectionSubject> mappingBySubject = mappings.stream()
                                                                     .collect(Collectors.toMap(
                                                                             CourseSelectionSubject::getSubjectId,
                                                                             Function.identity()));

        // Step 4: 收集所有教师 ID 并查询教师信息
        Set<Long> teacherIds = mappings.stream()
                                       .map(CourseSelectionSubject::getTeacherId)
                                       .filter(Objects::nonNull)
                                       .collect(Collectors.toSet());
        Map<Long, TeacherInfoVO> teacherMap = new HashMap<>();
        if (!teacherIds.isEmpty())
        {
            List<TeacherInfoVO> teacherInfos = teacherInfoMapper.selectTeacherInfoByIds(teacherIds);
            teacherMap = teacherInfos.stream()
                                     .collect(Collectors.toMap(TeacherInfoVO::getId, Function.identity()));
        }

        // Step 5: 遍历科目列表，组装 CourseSelectSubjectVO 对象
        List<CourseSelectSubjectVO> result = new ArrayList<>();
        for (SubjectsVO subject : subjects)
        {
            CourseSelectionSubject mapping = mappingBySubject.get(subject.getId());
            if (mapping == null)
            {
                continue; // 若未找到对应的映射，可跳过或根据业务逻辑处理
            }

            // 创建 VO 并拷贝科目基本信息
            CourseSelectSubjectVO vo = new CourseSelectSubjectVO();
            BeanUtils.copyProperties(subject, vo);

            // 设置上课地点
            vo.setClassRoom(mapping.getClassRoom());

            // 设置教师信息
            if (mapping.getTeacherId() != null)
            {
                vo.setTeacherInfo(teacherMap.get(mapping.getTeacherId()));
            }

            // 解析并设置上课时间列表（假设存储为 JSON 字符串）
            String classTimesJson = mapping.getClassTimes();
            if (StringUtils.isNotBlank(classTimesJson))
            {
                List<SubjectClassTime> classTimes =
                        JsonUtils.jsonToList(classTimesJson);
                vo.setClassTimes(classTimes);
            }
            else
            {
                vo.setClassTimes(Collections.emptyList());
            }

            result.add(vo);
        }

        return result;
    }

    /**
     * 获取学生选课任务
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @version 2025/1/11 0:22
     */
    @Override
    public List<CourseSelectionInfoVO> getStudentTasks(Long studentId)
    {
        Semesters currentSemester = semesterService.getCurrentSemester();
        if (currentSemester == null)
        {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "当前学期不存在");
        }
        Long currentSemesterId = currentSemester.getId();
        Date currentTime = new Date();
        // 获取学生信息以确定班级 ID
        StudentInfo student = studentInfoMapper.selectById(studentId);
        if (student == null || student.getStuClassId() == null)
        {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "学生不存在或未分配班级");
        }
        Long studentClassId = student.getStuClassId();
        // 查询符合条件的课程
        List<CourseSelectionInfo> courseSelectionInfos = courseSelectionInfoMapper.getStudentTasks(currentSemesterId,
                currentTime, studentClassId);

        // 调用公共方法组装 VO
        return assembleCourseSelectionInfoVOs(courseSelectionInfos);
    }

    private CourseSelectionInfo getCourseSelectionInfoById(Long taskId)
    {
        CourseSelectionInfo byId = getById(taskId);
        if (byId == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "选课任务不存在");
        }
        return byId;
    }

    /**
     * 校验上课时间是否存在冲突
     *
     * @param classTimes 上课时间列表
     */
    private void validateClassTimes(List<SubjectClassTime> classTimes)
    {
        if (classTimes == null || classTimes.isEmpty())
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "至少需要设置一个上课时间");
        }

        Set<String> timeSlots = new HashSet<>();
        for (SubjectClassTime classTime : classTimes)
        {
            Integer dayOfWeek = classTime.getDayOfWeek();
            String period = classTime.getPeriod();

            if (dayOfWeek == null || period == null || period.isEmpty())
            {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "上课时间的星期几和节次不能为空");
            }

            String timeKey = dayOfWeek + "-" + period;
            if (timeSlots.contains(timeKey))
            {
                throw new BusinessException(ErrorCode.PARAMS_ERROR,
                        "上课时间存在冲突：" + getDayOfWeekLabel(dayOfWeek) + " " + period);
            }
            timeSlots.add(timeKey);
        }
    }

    /**
     * 获取星期几的中文表示（辅助方法）
     *
     * @param dayOfWeek 星期几
     * @return 中文表示
     */
    private static String getDayOfWeekLabel(Integer dayOfWeek)
    {
        switch (dayOfWeek)
        {
            case 1:
                return "星期一";
            case 2:
                return "星期二";
            case 3:
                return "星期三";
            case 4:
                return "星期四";
            case 5:
                return "星期五";
            case 6:
                return "星期六";
            case 7:
                return "星期日";
            default:
                return "未知";
        }
    }

    private List<CourseSelectionInfoVO> assembleCourseSelectionInfoVOs(List<CourseSelectionInfo> courseSelectionInfos)
    {
        if (CollectionUtils.isEmpty(courseSelectionInfos))
        {
            return Collections.emptyList();
        }

        // 收集学期ID和教师ID
        Set<Long> semesterIds = new HashSet<>();
        courseSelectionInfos.forEach(item ->
        {
            semesterIds.add(item.getSemesterId());
        });

        // 查询学期信息
        Map<Long, Semesters> semesterMap;
        if (!semesterIds.isEmpty())
        {
            List<Semesters> semesterList = semestersMapper.selectList(
                    Wrappers.<Semesters>lambdaQuery()
                            .in(Semesters::getId, semesterIds)
                            .eq(Semesters::getIsDelete, 0)
            );
            semesterMap = semesterList.stream()
                                      .collect(Collectors.toMap(Semesters::getId, e -> e));
        }
        else {semesterMap = new HashMap<>();}

        // 组装 VO 列表
        return courseSelectionInfos.stream().map(csi ->
        {
            CourseSelectionInfoVO vo = new CourseSelectionInfoVO();
            BeanUtils.copyProperties(csi, vo);

            // 补充学期信息
            Semesters semester = semesterMap.get(csi.getSemesterId());
            vo.setSemesterName(semester != null ? semester.getName() : null);

            return vo;
        }).collect(Collectors.toList());
    }
    @Override
    public List<CourseSelectionInfoVO> getCourseSelectionInfoBySemesterId(Long semesterId) {
        List<CourseSelectionInfo> courseSelectionInfos = courseSelectionInfoMapper.selectList(
                Wrappers.<CourseSelectionInfo>lambdaQuery()
                        .eq(CourseSelectionInfo::getSemesterId, semesterId)
                        .eq(CourseSelectionInfo::getIsDelete, 0)
        );
        return assembleCourseSelectionInfoVOs(courseSelectionInfos);
    }

}
