package com.caixy.adminSystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caixy.adminSystem.common.ErrorCode;
import com.caixy.adminSystem.exception.BusinessException;
import com.caixy.adminSystem.mapper.*;
import com.caixy.adminSystem.model.dto.StudentGrades.StudentGradesAddRequest;
import com.caixy.adminSystem.model.dto.StudentGrades.StudentGradesQueryRequest;
import com.caixy.adminSystem.model.entity.*;
import com.caixy.adminSystem.model.vo.StudentGrades.StudentGradesVO;
import com.caixy.adminSystem.model.vo.StudentInfo.StudentInfoVO;
import com.caixy.adminSystem.model.vo.registrationTaskLesson.HasRegistrationTaskVO;
import com.caixy.adminSystem.model.vo.studentGrade.GradeForAdminVO;
import com.caixy.adminSystem.model.vo.studentGrade.GradeForStudentVO;
import com.caixy.adminSystem.model.vo.studentGrade.StudentsGradeForAdminVO;
import com.caixy.adminSystem.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author CAIXYPROMISE
 * @description 针对表【student_grades】的数据库操作Service实现
 * @createDate 2024-04-02 22:30:06
 */
@Service
public class StudentGradesServiceImpl extends ServiceImpl<StudentGradesMapper, StudentGrades> implements StudentGradesService
{
    @Resource
    private StudentInfoService studentInfoService;

    @Resource
    private SubjectsService subjectsService;

    @Resource
    private CourseSelectionInfoMapper courseSelectionInfoMapper;

    @Resource
    private SemestersService semestersService;

    @Resource
    private RegistrationTaskService registrationTaskService;
    @Resource
    private RegistrationTaskLessonMapper registrationTaskLessonMapper;
    @Resource
    private StudentCourseSelectionMapper studentCourseSelectionMapper;
    @Autowired
    private StudentInfoMapper studentInfoMapper;
    @Autowired
    private CourseSelectionSubjectMapper courseSelectionSubjectMapper;


    /**
     * 根据学生id，获取学生成绩信息
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/5/3 下午4:32
     */
    @Override
    public StudentGradesVO getStudentGradesVOByStuId(long stuId)
    {
        // 查询学生信息
        StudentInfo student = studentInfoService.getById(stuId);
        if (student == null)
        {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "学生不存在");
        }
        StudentInfoVO studentInfoVO = studentInfoService.getStudentInfoVO(student);
        StudentGradesVO studentGradesVO = new StudentGradesVO();
        studentGradesVO.setStuId(stuId);
        studentGradesVO.setStudentInfo(studentInfoVO);

        // 获取成绩信息
        QueryWrapper<StudentGrades> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("stuId", stuId);
        List<StudentGrades> studentGradesList = list(queryWrapper);
        // 如果成绩信息为空，则返回空成绩列表
        if (studentGradesList.isEmpty())
        {
            studentGradesVO.setGradeItem(Collections.emptyList());
            return studentGradesVO;
        }

        // 收集学生科目信息 -- 科目id
        Set<Long> stuGradeSubjectIds = studentGradesList.stream().map(StudentGrades::getSubjectId).collect(Collectors.toSet());
        // 查询科目信息
        List<Subjects> subjects = subjectsService.listByIds(stuGradeSubjectIds);
        // 转 id -> 实体 map映射 科目
        Map<Long, Subjects> subjectsMap = subjects.stream().collect(Collectors.toMap(Subjects::getId, item -> item));
        // 收集学生成绩信息
        List<StudentGradesVO.GradeItem> gradeItemList = studentGradesList.stream().map(item -> {
            StudentGradesVO.GradeItem gradeItem = new StudentGradesVO.GradeItem();
            gradeItem.setGradeId(item.getId());
            gradeItem.setSubjectId(item.getSubjectId());
            gradeItem.setSubjectName(subjectsMap.get(item.getSubjectId()).getName());
            gradeItem.setGrade(item.getTotalGrade());
            return gradeItem;
        }).collect(Collectors.toList());
        // 设置成绩信息
        studentGradesVO.setGradeItem(gradeItemList);
        return studentGradesVO;
    }


    @Override
    public void validStudentGrades(StudentGradesAddRequest request, boolean isElectives, Long userId, List<StudentInfo> studentInfoList)
    {
        Long semesterId = request.getSemesterId();
        Optional.ofNullable(semestersService.getById(semesterId)).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ERROR, "学期不存在"));

        Long subjectId = request.getSubjectId();
        Optional.ofNullable(subjectsService.getById(subjectId)).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ERROR, "科目不存在"));
        Long registrationTaskId = request.getTaskId();

        Long courseGroupId = request.getCourseGroupId();
        List<StudentGradesAddRequest.StudentGradeInfo> studentGradeInfos = request.getStudentGradeInfos();
        if (studentGradeInfos.isEmpty())
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "学生信息为空");
        }
        // 选课任务校验
        if (isElectives)
        {
            checkElectiveCourseInfo(studentInfoList, userId, subjectId, courseGroupId, registrationTaskId, studentGradeInfos);
        }
    }

    /**
     * 添加选修课程成绩
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @version 2025/1/26 1:31
     */
    @Override
    @Transactional
    public Boolean addElectiveCourseGrade(StudentGradesAddRequest request, Long userId)
    {
        List<StudentGradesAddRequest.StudentGradeInfo> studentGradeInfos = request.getStudentGradeInfos();
        Set<String> stuIds = studentGradeInfos.stream().map(StudentGradesAddRequest.StudentGradeInfo::getStuId).collect(Collectors.toSet());
        if (stuIds.isEmpty())
        {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "学生不存在");
        }
        List<StudentInfo> studentInfos = studentInfoMapper.selectList(new LambdaQueryWrapper<StudentInfo>().in(StudentInfo::getStuId, stuIds));
        if (studentInfos.isEmpty())
        {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "学生不存在");
        }
        validStudentGrades(request, true, userId, studentInfos);
        Map<String, Long> stuIdMap = studentInfos.stream().collect(Collectors.toMap(StudentInfo::getStuId, StudentInfo::getId));
        boolean saved = saveBatch(studentGradeInfos.stream().map(item -> {
            StudentGrades studentGrades = new StudentGrades();
            Long stuID = stuIdMap.get(item.getStuId());
            if (stuID == null)
            {
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "学生不存在");
            }
            BeanUtils.copyProperties(item, studentGrades);
            studentGrades.setSubjectId(request.getSubjectId());
            studentGrades.setSemesterId(request.getSemesterId());
            studentGrades.setCourseGroupId(request.getCourseGroupId());
            studentGrades.setStuId(stuID);
            studentGrades.setIsElectives(1);
            studentGrades.setCreatorId(userId);
            return studentGrades;
        }).collect(Collectors.toList()));
        if (saved)
        {
            LambdaUpdateWrapper<RegistrationTaskLesson> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(RegistrationTaskLesson::getIsFinished, 1);
            updateWrapper.set(RegistrationTaskLesson::getFinishedTime, new Date());
            updateWrapper.eq(RegistrationTaskLesson::getTaskId, request.getTaskId());
            registrationTaskLessonMapper.update(null, updateWrapper);
        }
        return saved;
    }

    /**
     * 根据课程任务和科目id获取学生成绩信息
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @version 2025/1/27 1:19
     */
    @Override
    public List<StudentsGradeForAdminVO> getStudentGradesByCourseTaskIdAndSubjectId(Long courseTaskId, Long subjectId)
    {
        LambdaQueryWrapper<StudentGrades> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StudentGrades::getCourseGroupId, courseTaskId);
        queryWrapper.eq(StudentGrades::getSubjectId, subjectId);
        List<StudentGrades> studentGradesList = list(queryWrapper);
        if (studentGradesList.isEmpty())
        {
            return Collections.emptyList();
        }
        Set<Long> studentIds = studentGradesList.stream().map(StudentGrades::getStuId).collect(Collectors.toSet());
        List<StudentInfoVO> studentInfoList = studentInfoService.getStudentInfoVoByIds(studentIds);
        Map<Long, StudentInfoVO> studentInfoMap = studentInfoList.stream().collect(Collectors.toMap(StudentInfoVO::getId, Function.identity()));
        return studentGradesList.stream().map(item -> {
            StudentsGradeForAdminVO studentsGradeForAdminVO = new StudentsGradeForAdminVO();
            studentsGradeForAdminVO.setStuId(item.getStuId());
            studentsGradeForAdminVO.setStudentInfo(studentInfoMap.get(item.getStuId()));
            GradeForAdminVO gradeForAdminVO = new GradeForAdminVO();
            BeanUtils.copyProperties(item, gradeForAdminVO);
            studentsGradeForAdminVO.setGradeItem(gradeForAdminVO);
            return studentsGradeForAdminVO;
        }).collect(Collectors.toList());
    }


    @Override
    public QueryWrapper<StudentGrades> getQueryWrapper(StudentGradesQueryRequest postQueryRequest)
    {
        return null;
    }

    @Override
    public Page<StudentGrades> searchFromEs(StudentGradesQueryRequest postQueryRequest)
    {
        return null;
    }

    @Override
    public StudentGradesVO getStudentGradesVO(StudentGrades post, HttpServletRequest request)
    {
        return null;
    }

    @Override
    public Page<StudentGradesVO> getStudentGradesVOPage(Page<StudentGrades> postPage, HttpServletRequest request)
    {
        return null;
    }

    /**
     * 根据科目id查询学生成绩
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/5/5 下午10:15
     */
    @Override
    public List<StudentGrades> listBySubjectId(Long subjectId)
    {
        QueryWrapper<StudentGrades> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", subjectId);
        return this.list(queryWrapper);
    }

    @Override
    public List<GradeForStudentVO> getStudentGrades(Long userId, Long semesterId)
    {
        return baseMapper.selectStudentGrades(userId, semesterId);
    }

    private void checkElectiveCourseInfo(List<StudentInfo> studentInfoList, Long userId, Long subjectId, Long courseGroupId, Long registrationTaskId, List<StudentGradesAddRequest.StudentGradeInfo> studentGradeInfos)
    {
        // 查询课程选课教师合法性
        CourseSelectionSubject isTeacher = courseSelectionSubjectMapper.selectOne(new LambdaQueryWrapper<CourseSelectionSubject>().eq(CourseSelectionSubject::getSubjectId, subjectId).eq(CourseSelectionSubject::getCourseSelectionId, courseGroupId).eq(CourseSelectionSubject::getTeacherId, userId));
        if (isTeacher == null)
        {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "没有权限");
        }
        // 查询登分任务课程信息,是否有选课任务
        List<HasRegistrationTaskVO> hasRegistrationTaskVOS = registrationTaskLessonMapper.selectHasRegistrationTaskBySubjectAndCourseTaskIds(subjectId, Collections.singletonList(courseGroupId));
        if (hasRegistrationTaskVOS.isEmpty() || !hasRegistrationTaskVOS.get(0).getTaskId().equals(registrationTaskId))
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "登分任务不存在");
        }
        HasRegistrationTaskVO hasRegistrationTaskVO = hasRegistrationTaskVOS.get(0);
        if (hasRegistrationTaskVO.getIsFinished() == 1)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "登分任务已完成");
        }
        Date currentDate = new Date();
        if (currentDate.before(hasRegistrationTaskVO.getStartDate()) || currentDate.after(hasRegistrationTaskVO.getEndDate()))
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "不在登分时间范围内");
        }
        // 检查学生信息是否重复
        Set<String> stuIds = studentGradeInfos.stream().map(StudentGradesAddRequest.StudentGradeInfo::getStuId).collect(Collectors.toSet());
        if (stuIds.size() != studentGradeInfos.size())
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "学生信息重复");
        }
        // 查询学生已选科目的 subjectId 列表
        LambdaQueryWrapper<StudentCourseSelection> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StudentCourseSelection::getCourseSelectionId, courseGroupId);
        queryWrapper.eq(StudentCourseSelection::getSubjectId, subjectId);
        queryWrapper.in(StudentCourseSelection::getStudentId, studentInfoList.stream().map(StudentInfo::getId).collect(Collectors.toList()));
        List<StudentCourseSelection> studentCourseSelections = studentCourseSelectionMapper.selectList(queryWrapper);
        if (studentCourseSelections.size() != stuIds.size())
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "学生信息错误");
        }
        validateStudentGrades(subjectId, studentGradeInfos);
    }

    private void validateStudentGrades(Long subjectId, List<StudentGradesAddRequest.StudentGradeInfo> studentGradeInfos)
    {
        // 防止 studentGradeInfos 为 null 或空
        if (studentGradeInfos == null || studentGradeInfos.isEmpty())
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "学生成绩信息为空");
        }

        // 获取科目信息
        Subjects subjectInfo = subjectsService.getById(subjectId);
        if (subjectInfo == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "科目不存在");
        }

        Integer gradeMax = subjectInfo.getGradeMax();
        Integer gradeMin = subjectInfo.getGradeMin();

        // 处理每个学生的成绩信息
        studentGradeInfos.forEach(item -> {
            // 确保 totalGrade 在合法范围内
            if (item.getTotalGrade() == null || item.getTotalGrade() > gradeMax || item.getTotalGrade() < gradeMin)
            {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "成绩范围不合法");
            }

            Integer usualGrade = item.getUsualGrade();
            if (usualGrade != null && (usualGrade < 0 || usualGrade > 100))
            {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "平时成绩范围不合法");
            }

            Integer finalGrade = item.getFinalGrade();
            if (finalGrade != null && (finalGrade < 0 || finalGrade > 100))
            {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "期末成绩范围不合法");
            }

            Integer usualPercentage = item.getUsualPercentage();
            if (usualPercentage != null && (usualPercentage < 0 || usualPercentage > 100))
            {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "平时成绩比例范围不合法");
            }

            Integer finalPercentage = item.getFinalPercentage();
            if (finalPercentage != null && (finalPercentage < 0 || finalPercentage > 100))
            {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "期末成绩比例范围不合法");
            }

            // 检查平时成绩和期末成绩比例之和是否为 100%
            if (usualPercentage != null && finalPercentage != null && (usualPercentage + finalPercentage) != 100)
            {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "平时成绩和期末成绩比例之和必须为100%");
            }

            // 计算总成绩，并确保其在合法范围内

            double calculatedTotalScore = (usualGrade != null ? usualGrade : 0) * (usualPercentage / 100.0) + (finalGrade != null ? finalGrade : 0) * ((100 - usualPercentage) / 100.0);
            // 对计算出的总成绩进行合法性检查
            if (calculatedTotalScore < gradeMin || calculatedTotalScore > gradeMax)
            {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "计算的总成绩不在合法范围内");
            }
        });
    }

}




