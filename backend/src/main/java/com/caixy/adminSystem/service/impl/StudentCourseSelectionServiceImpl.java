package com.caixy.adminSystem.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caixy.adminSystem.common.ErrorCode;
import com.caixy.adminSystem.constant.CommonConstant;
import com.caixy.adminSystem.exception.BusinessException;
import com.caixy.adminSystem.exception.ThrowUtils;
import com.caixy.adminSystem.mapper.*;
import com.caixy.adminSystem.model.dto.studentCourseSelection.StudentCourseSelectionQueryRequest;
import com.caixy.adminSystem.model.dto.studentCourseSelection.StudentSelectCourseRequest;
import com.caixy.adminSystem.model.dto.subject.SubjectClassTime;
import com.caixy.adminSystem.model.entity.*;

import com.caixy.adminSystem.model.vo.StudentInfo.StudentInfoVO;
import com.caixy.adminSystem.model.vo.Subjects.SubjectsVO;
import com.caixy.adminSystem.model.vo.studentCourseSelection.SelectionSubjectVO;
import com.caixy.adminSystem.model.vo.studentCourseSelection.StudentCourseSubjectVO;

import com.caixy.adminSystem.model.vo.studentCourseSelection.StudentWithCourseSelectionVO;
import com.caixy.adminSystem.model.vo.teacherInfo.TeacherInfoVO;
import com.caixy.adminSystem.service.*;
import com.caixy.adminSystem.utils.JsonUtils;
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
    private CourseSelectionClassesService courseSelectionClassesService;

    @Resource
    private CourseSelectionSubjectMapper courseSelectionSubjectMapper;

    @Resource
    private CourseSelectionSubjectService courseSelectionSubjectService;

    @Resource
    private StudentCourseSelectionMapper studentCourseSelectionMapper;

    @Resource
    private SubjectsMapper subjectsMapper;

    @Resource
    private TeacherInfoMapper teacherInfoMapper;

    @Resource
    private SubjectsService subjectsService;

    @Resource
    private StudentInfoService studentInfoService;

    /**
     * 学生退选
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @version 2025/1/11 4:24
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelCoursesForStudent(StudentSelectCourseRequest request, Long studentId)
    {
        Long courseSelectionId = request.getCourseSelectionId();
        List<Long> subjectIds = request.getSubjectIds();

        // 1. 参数校验
        if (studentId == null || courseSelectionId == null || subjectIds == null || subjectIds.isEmpty())
        {
            throw new RuntimeException("参数错误：请选择需退选的科目");
        }

        // 2. 验证选课任务是否存在、可用
        CourseSelectionInfo courseInfo = courseSelectionInfoMapper.selectById(courseSelectionId);
        if (courseInfo == null || courseInfo.getIsDelete() == 1)
        {
            throw new RuntimeException("选课任务不存在或已删除");
        }
        if (courseInfo.getIsActive() == 0)
        {
            throw new RuntimeException("选课任务已搁置或未激活，无法退选");
        }
        // 可加时间范围校验，如退选截至时间

        // 3. 查询学生已选科目记录
        List<StudentCourseSelection> selectedRecords = studentCourseSelectionMapper.listSelectedSubjects(studentId, courseSelectionId, subjectIds);
        if (selectedRecords.isEmpty())
        {
            throw new RuntimeException("学生未选过这些科目，无需退选");
        }

        // 4. 查询对应的 course_selection_subject 记录
        //    确保 subjectIds 全都属于该选课任务
        List<CourseSelectionSubject> cssList = courseSelectionSubjectMapper.listBySelectionAndSubjects(courseSelectionId, subjectIds);
        if (cssList.size() != subjectIds.size())
        {
            throw new RuntimeException("部分科目不在该选课任务中，无法退选");
        }
        // 生成 id 列表，用于批量递减
        List<Long> cssIdList = cssList.stream().map(CourseSelectionSubject::getId).collect(Collectors.toList());

        // 5. 更新学生选课记录：逻辑删除
        int deletedCount = studentCourseSelectionMapper.deleteStudentSelections(studentId, courseSelectionId, subjectIds);
        if (deletedCount < subjectIds.size())
        {
            throw new RuntimeException("退选失败：部分记录未能删除，请重试");
        }

        // 6. 批量递减 enrolledCount
        int updateCount = courseSelectionSubjectMapper.decrementEnrolledCountBatch(cssIdList);
        if (updateCount < cssIdList.size())
        {
            throw new RuntimeException("退选失败：更新已选人数异常，请重试");
        }
    }


    /**
     * 学生选课任务
     *
     * @author CAIXYPROMISE
     * @version 1.0
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void selectCoursesForStudent(StudentSelectCourseRequest request, Long studentId)
    {
        Long courseSelectionId = request.getCourseSelectionId();
        List<Long> subjectIds = request.getSubjectIds();

        // 1. 基本校验
        if (studentId == null || courseSelectionId == null || subjectIds == null || subjectIds.isEmpty())
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数错误：请选择科目");
        }

        // 2. 查询选课任务是否有效
        CourseSelectionInfo courseInfo = courseSelectionInfoMapper.selectById(courseSelectionId);
        if (courseInfo == null || courseInfo.getIsDelete() == 1)
        {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "选课任务不存在或已删除");
        }
        if (courseInfo.getIsActive() == 0)
        {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "选课任务已搁置或未激活");
        }
        // 检查时间
        Date startTime = courseInfo.getStartDate();
        Date endTime = courseInfo.getEndDate();
        Date now = new Date();
        if (now.before(startTime) || now.after(endTime))
        {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "不在选课时间范围内");
        }

        // 3. 查询可选科目记录
        List<CourseSelectionSubject> cssList = courseSelectionSubjectMapper.listBySelectionAndSubjects(courseSelectionId, subjectIds);
        if (cssList.size() != subjectIds.size())
        {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "部分科目不在该选课任务中或已被删除");
        }

        // 4. 检查学生是否已选或存在可恢复的记录
        List<StudentCourseSelection> existingRecords = studentCourseSelectionMapper.listAllByStudentAndSubjects(studentId, courseSelectionId, subjectIds);

        Set<Long> existingNotDeleted = new HashSet<>();
        Map<Long, StudentCourseSelection> toRecoverMap = new HashMap<>();

        for (StudentCourseSelection record : existingRecords)
        {
            Long subjectId = record.getSubjectId();
            if (record.getIsDelete() == 0)
            {
                existingNotDeleted.add(subjectId);
            }
            else
            {
                toRecoverMap.put(subjectId, record);
            }
        }

        if (!existingNotDeleted.isEmpty())
        {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "以下科目已经选过了：" + existingNotDeleted);
        }

        List<Long> toRecoverSubjectIds = new ArrayList<>(toRecoverMap.keySet());
        List<Long> toInsertSubjectIds = subjectIds.stream()
                .filter(sid -> !toRecoverMap.containsKey(sid)).collect(Collectors.toList());

        // 5. 处理名额并更新已选人数
        Map<Long, CourseSelectionSubject> cssMap = cssList.stream()
                .collect(Collectors.toMap(CourseSelectionSubject::getSubjectId, css -> css));

        List<Long> cssIdsToUpdate = new ArrayList<>();
        for (Long subjectId : toRecoverSubjectIds)
        {
            CourseSelectionSubject css = cssMap.get(subjectId);
            cssIdsToUpdate.add(css.getId());
        }
        for (Long subjectId : toInsertSubjectIds)
        {
            CourseSelectionSubject css = cssMap.get(subjectId);
            cssIdsToUpdate.add(css.getId());
        }

        int updatedRows = courseSelectionSubjectMapper.incrementEnrolledCountBatch(cssIdsToUpdate);
        if (updatedRows < cssIdsToUpdate.size())
        {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新已选人数失败，部分科目可能已满，请重试");
        }

        // 6. 恢复或插入选课记录
        if (!toRecoverSubjectIds.isEmpty())
        {
            List<Long> recoverIds = toRecoverMap.values().stream().map(StudentCourseSelection::getId).collect(Collectors.toList());
            int recoveredCount = studentCourseSelectionMapper.recoverStudentSelections(recoverIds);
            if (recoveredCount != toRecoverSubjectIds.size())
            {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "恢复选课记录失败");
            }
        }
        // 7. 插入新的选课记录
        if (!toInsertSubjectIds.isEmpty())
        {
            List<StudentCourseSelection> insertList = toInsertSubjectIds.stream().map(sid -> {
                StudentCourseSelection sc = new StudentCourseSelection();
                sc.setStudentId(studentId);
                sc.setCourseSelectionId(courseSelectionId);
                sc.setSubjectId(sid);
                sc.setSelectTime(new Date());
                sc.setIsDelete(0);
                return sc;
            }).collect(Collectors.toList());
            int insertCount = studentCourseSelectionMapper.insertStudentSelections(insertList);
            if (insertCount < insertList.size())
            {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "插入学生选课记录失败");
            }
        }
    }

    /**
     * 获取指定选课任务下的所有学生选课情况（包括学生基本信息、所选科目、总学分）。
     * 如果学生未选课，selectedSubjects 为空，totalCredit = 0.0。
     *
     * @param courseSelectionId 选课任务ID
     * @return 学生选课详情列表
     */
    @Override
    public List<StudentWithCourseSelectionVO> getStudentCourseSelections(Long courseSelectionId) {
        // 0. 检查选课任务是否存在或已删除
        CourseSelectionInfo courseSelectionInfo = courseSelectionInfoMapper.selectById(courseSelectionId);
        if (courseSelectionInfo == null || courseSelectionInfo.getIsDelete() == 1) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "选课任务不存在或已删除");
        }

        // 1. 查询该选课任务下所关联的班级ID
        List<Long> classIds = courseSelectionClassesService.getClassesByCourseSelectionId(courseSelectionId);
        if (classIds.isEmpty()) {
            return Collections.emptyList();
        }

        // 2. 查询这些班级下所有学生
        List<StudentInfoVO> allStudents = studentInfoService.getStudentByClassesIds(classIds);
        if (allStudents.isEmpty()) {
            return Collections.emptyList();
        }

        // 3. 查询学生的选课记录（student_course_selection），只查询当前任务 + 这些学生
        List<Long> studentIds = allStudents.stream().map(StudentInfoVO::getStudentId).filter(Objects::nonNull).collect(Collectors.toList());
        List<StudentCourseSelection> selections = baseMapper.selectList(
                new LambdaQueryWrapper<StudentCourseSelection>()
                        .eq(StudentCourseSelection::getCourseSelectionId, courseSelectionId)
                        .in(StudentCourseSelection::getStudentId, studentIds)
                        .eq(StudentCourseSelection::getIsDelete, 0)
        );

        // 4. 预加载科目信息（科目学分、科目名称等）
        Set<Long> subjectIds = selections.stream()
                .map(StudentCourseSelection::getSubjectId)
                .collect(Collectors.toSet());
        // 一次性查询这些科目的信息
        List<SubjectsVO> subjectVOByIds = subjectsService.getSubjectVOByIds(subjectIds);

        // 建立映射：subjectId -> (SubjectsVO)
        Map<Long, SubjectsVO> subjectsVOMapByIds = subjectVOByIds.stream()
                .collect(Collectors.toMap(SubjectsVO::getId, vo -> vo));

        // 5. 按 studentId 分组选课记录，后面按学生来封装
        Map<Long, List<StudentCourseSelection>> studentSelectionsMap = selections.stream()
                .collect(Collectors.groupingBy(StudentCourseSelection::getStudentId));

        // 6. 构建返回结果列表，每个学生对应一个 UnqualifiedStudentVO（此处名字可以换成更通用，如 StudentCourseSelectionVO）
        List<StudentWithCourseSelectionVO> result = new ArrayList<>();

        for (StudentInfoVO student : allStudents) {
            // 获取该学生在此任务下所有选课记录
            List<StudentCourseSelection> studentSelections = studentSelectionsMap
                    .getOrDefault(student.getStudentId(), Collections.emptyList());

            // 计算总学分
            double totalCredit = 0.0;
            if (!studentSelections.isEmpty()) {
                totalCredit = studentSelections.stream()
                        .mapToDouble(scs -> {
                            SubjectsVO subjectVO = subjectsVOMapByIds.get(scs.getSubjectId());
                            return (subjectVO != null) ? subjectVO.getGradeCredit() : 0.0;
                        })
                        .sum();
            }

            // 构建一个 VO（继承自 StudentInfoVO，这里叫 UnqualifiedStudentVO ）
            StudentWithCourseSelectionVO vo = new StudentWithCourseSelectionVO();
            // 把学生基本信息复制到 VO
            vo.setStudentId(student.getStudentId());
            vo.setStuId(student.getStuId());
            vo.setStuName(student.getStuName());
            vo.setStuSex(student.getStuSex());
            vo.setStuDepart(student.getStuDepart());
            vo.setStuMajor(student.getStuMajor());
            vo.setStuClass(student.getStuClass());
            vo.setStuDeptId(student.getStuDeptId());
            vo.setStuMajorId(student.getStuMajorId());
            vo.setStuClassId(student.getStuClassId());

            // 学分信息（这里无需设置 requiredCredit）
            vo.setTotalCredit(totalCredit);

            // 已选科目信息
            if (studentSelections.isEmpty()) {
                // 该学生在此任务下未选课
                vo.setSelectedSubjects(Collections.emptyList());
            } else {
                // 该学生已选科目
                List<SubjectsVO> selectedSubjects = studentSelections.stream()
                        .map(scs -> {
                            SubjectsVO subjectVO = subjectsVOMapByIds.get(scs.getSubjectId());
                            // 保险起见，判空处理
                            if (subjectVO == null) {
                                // 说明这个subjectId可能已经被逻辑删除或者查询不到
                                return new SubjectsVO();
                            }
                            return subjectVO;
                        })
                        .collect(Collectors.toList());
                vo.setSelectedSubjects(selectedSubjects);
            }

            // 加入返回列表
            result.add(vo);
        }

        return result;
    }

    /**
     * 查询在指定选课任务下，未达到最小学分要求的学生。
     * 复用了 getStudentCourseSelections返回的数据，减少重复查询。
     *
     * @param courseSelectionId 选课任务ID
     * @return 未达标学生列表
     */
    @Override
    public List<StudentWithCourseSelectionVO> getUnqualifiedStudents(Long courseSelectionId) {
        // 1. 查出选课任务信息，拿到最小学分
        CourseSelectionInfo courseSelectionInfo = courseSelectionInfoMapper.selectById(courseSelectionId);
        if (courseSelectionInfo == null || courseSelectionInfo.getIsDelete() == 1) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "选课任务不存在或已删除");
        }
        double minCredit = courseSelectionInfo.getMinCredit();

        // 2. 调用“数据准备方法”，一次性拿到所有学生选课情况
        List<StudentWithCourseSelectionVO> allStudentsSelections = getStudentCourseSelections(courseSelectionId);

        // 3. 在应用层做筛选： totalCredit < minCredit 的学生就是“未达标”
        List<StudentWithCourseSelectionVO> result = new ArrayList<>();
        for (StudentWithCourseSelectionVO vo : allStudentsSelections) {
            if (vo.getTotalCredit() < minCredit) {
                // 在这个阶段，把 requiredCredit 填上
                vo.setRequiredCredit(minCredit);
                result.add(vo);
            }
        }

        return result;
    }
    /**
     * 为未满足选课要求的学生自动分配课程，确保他们满足最低学分要求。
     * 同时在分配后批量插入选课记录，并更新每个课程的 enrolledCount。
     *
     * @param courseSelectionId 选课任务ID
     * @return 分配的学生人数
     */
    @Override
    @Transactional
    public Integer assignRandomCoursesToUnqualifiedStudents(Long courseSelectionId) {
        // 0. 检查选课任务是否存在，且只有结束了才能随机
        CourseSelectionInfo courseSelectionInfo = courseSelectionInfoMapper.selectById(courseSelectionId);
        if (courseSelectionInfo == null || courseSelectionInfo.getIsDelete() == 1) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "选课任务不存在或已删除");
        }
        // 检查选课任务是否已经结束
        Date now = new Date();
        if (courseSelectionInfo.getEndDate() != null && now.before(courseSelectionInfo.getEndDate())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "选课任务未结束，无法进行随机分配");
        }

        // 1. 获取未达标学生信息（复用之前实现的 getUnqualifiedStudents 方法）
        List<StudentWithCourseSelectionVO> unqualifiedStudents = getUnqualifiedStudents(courseSelectionId);
        if (unqualifiedStudents.isEmpty()) {
            log.info("所有学生已满足选课要求，无需分配课程");
            return 0;
        }

        // 2. 获取该任务下可选课程（注意：此处返回的课程必须包含学分、最大人数、当前已选人数等信息）
        List<SelectionSubjectVO> availableSubjects = courseSelectionSubjectService.getSelectSubjectVOByTaskId(courseSelectionId);
        if (availableSubjects.isEmpty()) {
            log.warn("没有可选的科目，无法自动分配课程");
            return 0;
        }

        // 4. 遍历未达标学生，计算最佳课程组合，并生成待插入的选课记录
        List<StudentCourseSelection> newSelections = new ArrayList<>();
        // 用于统计每个科目新增了多少选课记录（用于后续更新 enrolledCount）
        Map<Long, Integer> additionalCountMap = new HashMap<>();

        for (StudentWithCourseSelectionVO student : unqualifiedStudents) {
            double requiredCredit = student.getRequiredCredit();
            double currentCredit = student.getTotalCredit();
            double neededCredit = requiredCredit - currentCredit;
            if (neededCredit <= 0) {
                continue;  // 已满足要求（正常情况下不会进入这里）
            }

            // 计算最佳课程组合（动态规划 + 降级策略）
            List<SelectionSubjectVO> chosenSubjects = selectBestSubjects(availableSubjects,  neededCredit, student.getSelectedSubjects());
            if (chosenSubjects.isEmpty()) {
                log.warn("为学生 {} 找不到合适的课程组合，跳过分配", student.getStudentId());
                continue;
            }

            // 对 chosenSubjects 中的每门课程，还需要检查当前是否仍有容量（因为可能在之前学生分配时已更新）
            List<SelectionSubjectVO> finalSubjects = new ArrayList<>();
            for (SelectionSubjectVO subject : chosenSubjects) {
                // 判断是否达到最大人数限制：maxStudent==0 表示不限；否则，必须小于 maxStudent
                if (subject.getMaxStudent() != null && subject.getMaxStudent() > 0 &&
                        subject.getEnrolledCount() + additionalCountMap.getOrDefault(subject.getId(), 0) >= subject.getMaxStudent()) {
                    continue;
                }


                // 避免重复选择
                if (!finalSubjects.contains(subject)) {
                    finalSubjects.add(subject);
                }
            }

            // 如果经过容量检查后组合为空，则尝试降级策略：选择学分最高的单门课程（同样判断容量）
            if (finalSubjects.isEmpty()) {
                Optional<SelectionSubjectVO> fallback = availableSubjects.stream()
                        .filter(sub -> !student.getSelectedSubjects().stream().map(SubjectsVO::getId).collect(Collectors.toSet()).contains(sub.getId()))
                        .filter(sub -> sub.getMaxStudent() == null || sub.getMaxStudent() == 0 || sub.getEnrolledCount() + additionalCountMap.getOrDefault(sub.getId(), 0) < sub.getMaxStudent())
                        .max(Comparator.comparingDouble(SubjectsVO::getGradeCredit));
                fallback.ifPresent(finalSubjects::add);
            }

            // 生成该学生的选课记录（最终保证不重复）
            for (SubjectsVO subject : finalSubjects) {
                StudentCourseSelection selection = new StudentCourseSelection();
                selection.setStudentId(student.getStudentId());
                selection.setCourseSelectionId(courseSelectionId);
                selection.setSubjectId(subject.getId());
                selection.setSelectTime(new Date());
                selection.setByRandom(1); // 标记为系统随机分配
                selection.setIsDelete(0);
                newSelections.add(selection);

                // 统计新增数量，用于后续更新 enrolledCount
                additionalCountMap.put(subject.getId(), additionalCountMap.getOrDefault(subject.getId(), 0) + 1);
            }
        }

        // 5. 批量插入选课记录（避免 N+1 操作）
        if (!newSelections.isEmpty()) {
            boolean saved = this.saveBatch(newSelections);
            log.info("成功为 {} 名未达标学生分配了课程", newSelections.size());
            ThrowUtils.throwIf(!saved, ErrorCode.SYSTEM_ERROR, "批量插入选课记录失败");

            // 6. 更新每个课程的 enrolledCount（批量更新，可在 service 层封装一个更新方法）
            List<CourseSelectionSubject> updates = new ArrayList<>();
            for (Map.Entry<Long, Integer> entry : additionalCountMap.entrySet()) {
                CourseSelectionSubject update = new CourseSelectionSubject();
                update.setCourseSelectionId(courseSelectionId);
                update.setSubjectId(entry.getKey());
                update.setEnrolledCount(entry.getValue());
                updates.add(update);
            }

            // 6. 更新每个课程的 enrolledCount（批量更新，可在 service 层封装一个更新方法）
            courseSelectionSubjectService.batchUpdateEnrolledCount(courseSelectionId, additionalCountMap);

            return newSelections.size();
        } else {
            log.warn("没有课程可供分配，未执行插入");
            return 0;
        }
    }

    /**
     * 选择最优的课程组合，使总学分尽快满足要求
     * 使用动态规划算法（DP）计算最少课程组合，类似「最少硬币找零问题」。
     * 如果找不到合适组合，则采用降级策略：使用贪心算法选择学分最高的组合。
     *
     * @param availableSubjects 可选科目列表（必须包含 maxStudent 与 enrolledCount 信息）
     * @param targetCredit      需要填补的学分
     * @param selectedSubjects  学生已选课程列表（避免重复选课）
     * @return 最佳课程组合列表
     */
    private List<SelectionSubjectVO> selectBestSubjects(List<SelectionSubjectVO> availableSubjects,
                                                        double targetCredit,
                                                        List<SubjectsVO> selectedSubjects) {
        // 过滤掉已选课程 + 满员课程
        Set<Long> selectedIds = selectedSubjects.stream()
                .map(SubjectsVO::getId)
                .collect(Collectors.toSet());

        List<SelectionSubjectVO> filteredSubjects = availableSubjects.stream()
                .filter(sub -> !selectedIds.contains(sub.getId()))
                .filter(sub -> sub.getMaxStudent() == null || sub.getMaxStudent() == 0 || sub.getEnrolledCount() < sub.getMaxStudent())
                .sorted(Comparator.comparingDouble(SelectionSubjectVO::getGradeCredit))
                .collect(Collectors.toList());

        int maxCredit = (int) Math.ceil(targetCredit);
        int[] dp = new int[maxCredit + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;

        Map<Integer, List<SelectionSubjectVO>> dpPath = new HashMap<>();
        dpPath.put(0, new ArrayList<>());

        for (SelectionSubjectVO subject : filteredSubjects) {
            int credit = (int) Math.ceil(subject.getGradeCredit());
            for (int j = maxCredit; j >= credit; j--) {
                if (dp[j - credit] != Integer.MAX_VALUE && dp[j - credit] + 1 < dp[j]) {
                    dp[j] = dp[j - credit] + 1;
                    List<SelectionSubjectVO> newPath = new ArrayList<>(dpPath.getOrDefault(j - credit, new ArrayList<>()));
                    newPath.add(subject);
                    dpPath.put(j, newPath);
                }
            }
        }

        for (int i = maxCredit; i >= Math.ceil(targetCredit); i--) {
            if (dp[i] != Integer.MAX_VALUE) {
                return dpPath.get(i);
            }
        }

        // 贪心策略（降级）
        double sum = 0.0;
        List<SelectionSubjectVO> greedyCombo = new ArrayList<>();
        for (SelectionSubjectVO subject : filteredSubjects) {
            if (sum < targetCredit) {
                greedyCombo.add(subject);
                sum += subject.getGradeCredit();
            } else {
                break;
            }
        }

        return sum >= targetCredit ? greedyCombo : Collections.singletonList(filteredSubjects.get(0));
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
    public QueryWrapper<StudentCourseSelection> getQueryWrapper(StudentCourseSelectionQueryRequest studentCourseSelectionQueryRequest)
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
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return queryWrapper;
    }


    @Override
    public List<StudentCourseSubjectVO> listSubjectsForStudent(Long studentId, Long courseSelectionId)
    {
        // 1. 查询选课任务下所有可选科目映射信息
        List<CourseSelectionSubject> cssList = courseSelectionSubjectMapper.getSubjectListBySelectionId(courseSelectionId);
        if (cssList == null || cssList.isEmpty())
        {
            return Collections.emptyList();
        }

        // 2. 提取科目ID列表
        List<Long> subjectIds = cssList.stream().map(CourseSelectionSubject::getSubjectId).distinct().collect(Collectors.toList());

        // 3. 查询科目信息
        List<Subjects> subjects = subjectsMapper.selectBatchIds(subjectIds);
        Map<Long, Subjects> subjectMap = subjects.stream().collect(Collectors.toMap(Subjects::getId, s -> s));

        // 4. 查询学生已选科目ID列表
        List<Long> selectedSubjectIds = studentCourseSelectionMapper.getSelectedSubjectIdsByStudent(studentId, courseSelectionId);
        Set<Long> selectedSet = new HashSet<>(selectedSubjectIds);

        // 5. 提取所有教师 ID 并查询教师信息
        Set<Long> teacherIds = cssList.stream().map(CourseSelectionSubject::getTeacherId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, TeacherInfoVO> teacherMap;
        if (!teacherIds.isEmpty())
        {
            List<TeacherInfoVO> teacherInfos = teacherInfoMapper.selectTeacherInfoByIds(new ArrayList<>(teacherIds));
            teacherMap = teacherInfos.stream().collect(Collectors.toMap(TeacherInfoVO::getId, t -> t));
        }
        else
        {
            teacherMap = new HashMap<>();
        }

        // 6. 组装返回结果
        return cssList.stream().map(css -> {
            Subjects sub = subjectMap.get(css.getSubjectId());
            if (sub == null)
            {
                return null;
            }
            StudentCourseSubjectVO vo = new StudentCourseSubjectVO();
            vo.setSubjectId(sub.getId());
            vo.setSubjectName(sub.getName());
            vo.setGradeCredit(sub.getGradeCredit());
            vo.setMaxStudents(css.getMaxStudents());
            vo.setEnrolledCount(css.getEnrolledCount());
            vo.setSelected(selectedSet.contains(sub.getId()));
            // 当最大人数为0时，表示不限制人数，不认为已满
            vo.setFull(css.getMaxStudents() != null && css.getMaxStudents() > 0 && css.getEnrolledCount() >= css.getMaxStudents());

            // 设置教师信息
            if (css.getTeacherId() != null)
            {
                vo.setTeacherInfo(teacherMap.get(css.getTeacherId()));
            }

            // 设置上课地点
            vo.setClassRoom(css.getClassRoom());

            // 解析并设置上课时间
            if (StringUtils.isNotBlank(css.getClassTimes()))
            {
                List<SubjectClassTime> classTimes = JsonUtils.jsonToList(css.getClassTimes());
                vo.setClassTimes(classTimes);
            }
            else
            {
                vo.setClassTimes(Collections.emptyList());
            }

            return vo;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * 获取学生选课信息表封装
     *
     * @param studentCourseSelection
     * @param request
     * @return
     */
    @Override
    public StudentCourseSubjectVO getStudentCourseSelectionVO(StudentCourseSelection studentCourseSelection, HttpServletRequest request)
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
    public Page<StudentCourseSubjectVO> getStudentCourseSelectionVOPage(Page<StudentCourseSelection> studentCourseSelectionPage, HttpServletRequest request)
    {
        // todo: 补充分页获取学生选课信息表封装逻辑
        return null;
    }

}
