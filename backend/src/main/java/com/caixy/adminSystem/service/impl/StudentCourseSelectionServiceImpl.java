package com.caixy.adminSystem.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caixy.adminSystem.common.ErrorCode;
import com.caixy.adminSystem.constant.CommonConstant;
import com.caixy.adminSystem.exception.BusinessException;
import com.caixy.adminSystem.exception.ThrowUtils;
import com.caixy.adminSystem.mapper.CourseSelectionInfoMapper;
import com.caixy.adminSystem.mapper.CourseSelectionSubjectMapper;
import com.caixy.adminSystem.mapper.StudentCourseSelectionMapper;
import com.caixy.adminSystem.mapper.SubjectsMapper;
import com.caixy.adminSystem.model.dto.studentCourseSelection.StudentCourseSelectionQueryRequest;
import com.caixy.adminSystem.model.dto.studentCourseSelection.StudentSelectCourseRequest;
import com.caixy.adminSystem.model.entity.CourseSelectionInfo;
import com.caixy.adminSystem.model.entity.CourseSelectionSubject;
import com.caixy.adminSystem.model.entity.StudentCourseSelection;

import com.caixy.adminSystem.model.entity.Subjects;
import com.caixy.adminSystem.model.vo.studentCourseSelection.StudentCourseSubjectVO;

import com.caixy.adminSystem.service.StudentCourseSelectionService;
import com.caixy.adminSystem.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 学生选课信息表服务实现
 */
@Service
@Slf4j
public class StudentCourseSelectionServiceImpl extends ServiceImpl<StudentCourseSelectionMapper, StudentCourseSelection> implements StudentCourseSelectionService
{
    @Resource
    private CourseSelectionInfoMapper courseSelectionInfoMapper;

    @Resource
    private CourseSelectionSubjectMapper courseSelectionSubjectMapper;

    @Resource
    private StudentCourseSelectionMapper studentCourseSelectionMapper;

    @Resource
    private SubjectsMapper subjectsMapper;

    /**
     * 学生退选
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @version 2025/1/11 4:24
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelCoursesForStudent(StudentSelectCourseRequest request, Long studentId) {
        Long courseSelectionId = request.getCourseSelectionId();
        List<Long> subjectIds = request.getSubjectIds();

        // 1. 参数校验
        if (studentId == null || courseSelectionId == null
            || subjectIds == null || subjectIds.isEmpty()) {
            throw new RuntimeException("参数错误：请选择需退选的科目");
        }

        // 2. 验证选课任务是否存在、可用
        CourseSelectionInfo courseInfo = courseSelectionInfoMapper.selectById(courseSelectionId);
        if (courseInfo == null || courseInfo.getIsDelete() == 1) {
            throw new RuntimeException("选课任务不存在或已删除");
        }
        if (courseInfo.getIsActive() == 0) {
            throw new RuntimeException("选课任务已搁置或未激活，无法退选");
        }
        // 可加时间范围校验，如退选截至时间

        // 3. 查询学生已选科目记录
        List<StudentCourseSelection> selectedRecords = studentCourseSelectionMapper
                .listSelectedSubjects(studentId, courseSelectionId, subjectIds);
        if (selectedRecords.isEmpty()) {
            throw new RuntimeException("学生未选过这些科目，无需退选");
        }

        // 4. 查询对应的 course_selection_subject 记录
        //    确保 subjectIds 全都属于该选课任务
        List<CourseSelectionSubject> cssList = courseSelectionSubjectMapper
                .listBySelectionAndSubjects(courseSelectionId, subjectIds);
        if (cssList.size() != subjectIds.size()) {
            throw new RuntimeException("部分科目不在该选课任务中，无法退选");
        }
        // 生成 id 列表，用于批量递减
        List<Long> cssIdList = cssList.stream().map(CourseSelectionSubject::getId).collect(Collectors.toList());

        // 5. 更新学生选课记录：逻辑删除
        int deletedCount = studentCourseSelectionMapper
                .deleteStudentSelections(studentId, courseSelectionId, subjectIds);
        if (deletedCount < subjectIds.size()) {
            throw new RuntimeException("退选失败：部分记录未能删除，请重试");
        }

        // 6. 批量递减 enrolledCount
        int updateCount = courseSelectionSubjectMapper.decrementEnrolledCountBatch(cssIdList);
        if (updateCount < cssIdList.size()) {
            throw new RuntimeException("退选失败：更新已选人数异常，请重试");
        }
    }


    /**
     * 学生选课任务
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @version 2025/1/11 3:38
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void selectCoursesForStudent(StudentSelectCourseRequest request, Long studentId) {
        Long courseSelectionId = request.getCourseSelectionId();
        List<Long> subjectIds = request.getSubjectIds();

        // 1. 基本校验
        if (studentId == null || courseSelectionId == null
            || subjectIds == null || subjectIds.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数错误：请选择科目");
        }

        // 2. 查询选课任务是否有效
        CourseSelectionInfo courseInfo = courseSelectionInfoMapper.selectById(courseSelectionId);
        if (courseInfo == null || courseInfo.getIsDelete() == 1) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "选课任务不存在或已删除");
        }
        if (courseInfo.getIsActive() == 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "选课任务已搁置或未激活");
        }
        // 检查时间
        Date startTime = courseInfo.getStartDate();
        Date endTime = courseInfo.getEndDate();
        Date now = new Date();
        if (now.before(startTime) || now.after(endTime)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "不在选课时间范围内");
        }

        // 3. 查询可选科目记录
        List<CourseSelectionSubject> cssList = courseSelectionSubjectMapper
                .listBySelectionAndSubjects(courseSelectionId, subjectIds);
        if (cssList.size() != subjectIds.size()) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "部分科目不在该选课任务中或已被删除");
        }

        // 4. 检查学生是否已选了其中一些科目
        List<StudentCourseSelection> alreadySelected = studentCourseSelectionMapper
                .listSelectedSubjects(studentId, courseSelectionId, subjectIds);
        if (!alreadySelected.isEmpty()) {
            Set<Long> alreadySubIds = alreadySelected.stream()
                                                     .map(StudentCourseSelection::getSubjectId)
                                                     .collect(Collectors.toSet());
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "以下科目已经选过了：" + alreadySubIds);
        }

        // 5. 检查名额并更新已选人数
        List<Long> cssIdsToUpdate = new ArrayList<>();
        for (CourseSelectionSubject css : cssList) {
            int max = css.getMaxStudents();
            int enrolled = css.getEnrolledCount();
            if (enrolled >= max) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "科目 " + css.getSubjectId() + " 名额已满，无法继续选择");
            }
            cssIdsToUpdate.add(css.getId());
        }
        // 批量更新： enrolledCount += 1
        // 如果需要更精细的处理，可改为逐条更新 + recheck
        int updatedRows = courseSelectionSubjectMapper.incrementEnrolledCountBatch(cssIdsToUpdate);
        if (updatedRows < cssIdsToUpdate.size()) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新已选人数失败，部分科目可能已满，请重试");
        }

        // 6. 插入学生选课记录
        List<StudentCourseSelection> insertList = subjectIds.stream().map(sid -> {
            StudentCourseSelection sc = new StudentCourseSelection();
            sc.setStudentId(studentId);
            sc.setCourseSelectionId(courseSelectionId);
            sc.setSubjectId(sid);
            return sc;
        }).collect(Collectors.toList());

        int insertCount = studentCourseSelectionMapper.insertStudentSelections(insertList);
        if (insertCount < insertList.size()) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "插入学生选课记录失败");
        }
    }

    /**
     * 校验数据
     *
     * @param studentCourseSelection
     * @param add                    对创建的数据进行校验
     */
    @Override
    public void validStudentCourseSelection(StudentCourseSelection studentCourseSelection, boolean add)
    {
        ThrowUtils.throwIf(studentCourseSelection == null, ErrorCode.PARAMS_ERROR);

        // 修改数据时，有参数则校验
        // todo 补充校验规则
    }

    /**
     * 获取查询条件
     *
     * @param studentCourseSelectionQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<StudentCourseSelection> getQueryWrapper(
            StudentCourseSelectionQueryRequest studentCourseSelectionQueryRequest)
    {
        QueryWrapper<StudentCourseSelection> queryWrapper = new QueryWrapper<>();
        if (studentCourseSelectionQueryRequest == null)
        {
            return queryWrapper;
        }
        // todo 从对象中取值
        Long id = studentCourseSelectionQueryRequest.getId();
        Long notId = studentCourseSelectionQueryRequest.getNotId();
        String title = studentCourseSelectionQueryRequest.getTitle();
        String content = studentCourseSelectionQueryRequest.getContent();
        String searchText = studentCourseSelectionQueryRequest.getSearchText();
        String sortField = studentCourseSelectionQueryRequest.getSortField();
        String sortOrder = studentCourseSelectionQueryRequest.getSortOrder();
        List<String> tagList = studentCourseSelectionQueryRequest.getTags();
        Long userId = studentCourseSelectionQueryRequest.getUserId();
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


    @Override
    public List<StudentCourseSubjectVO> listSubjectsForStudent(Long studentId, Long courseSelectionId)
    {
        // 1. 查询选课任务下所有可选科目
        List<CourseSelectionSubject> cssList = courseSelectionSubjectMapper.getSubjectMappingBySelectionId(
                courseSelectionId);
        if (cssList == null || cssList.isEmpty())
        {
            return Collections.emptyList();
        }

        // 2. 提取科目ID列表
        List<Long> subjectIds = cssList.stream()
                                       .map(CourseSelectionSubject::getSubjectId)
                                       .distinct()
                                       .collect(Collectors.toList());

        // 3. 查询科目信息
        List<Subjects> subjects = subjectsMapper.selectBatchIds(subjectIds);
        Map<Long, Subjects> subjectMap = subjects.stream()
                                                 .collect(Collectors.toMap(Subjects::getId, s -> s));

        // 4. 查询学生已选科目ID列表
        List<Long> selectedSubjectIds = studentCourseSelectionMapper
                .getSelectedSubjectIdsByStudent(studentId, courseSelectionId);
        Set<Long> selectedSet = new HashSet<>(selectedSubjectIds);

        // 5. 组装返回结果
        return cssList.stream().map(css -> {
            Subjects sub = subjectMap.get(css.getSubjectId());
            if (sub == null)
            {
                // 返回空流
                return null;
            }
            StudentCourseSubjectVO vo = new StudentCourseSubjectVO();
            vo.setSubjectId(sub.getId());
            vo.setSubjectName(sub.getName());
            vo.setGradeCredit(sub.getGradeCredit());
            vo.setMaxStudents(css.getMaxStudents());
            vo.setEnrolledCount(css.getEnrolledCount());
            vo.setSelected(selectedSet.contains(sub.getId()));
            vo.setFull(css.getEnrolledCount() >= css.getMaxStudents());
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 获取学生选课信息表封装
     *
     * @param studentCourseSelection
     * @param request
     * @return
     */
    @Override
    public StudentCourseSubjectVO getStudentCourseSelectionVO(StudentCourseSelection studentCourseSelection,
                                                              HttpServletRequest request)
    {
        // todo: 补充获取学生选课信息表封装逻辑
        return null;
    }

    /**
     * 分页获取学生选课信息表封装
     *
     * @param studentCourseSelectionPage
     * @param request
     * @return
     */
    @Override
    public Page<StudentCourseSubjectVO> getStudentCourseSelectionVOPage(
            Page<StudentCourseSelection> studentCourseSelectionPage, HttpServletRequest request)
    {
        // todo: 补充分页获取学生选课信息表封装逻辑
        return null;
    }

}
