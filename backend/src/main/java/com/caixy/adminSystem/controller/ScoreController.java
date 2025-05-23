package com.caixy.adminSystem.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.caixy.adminSystem.annotation.AuthCheck;
import com.caixy.adminSystem.common.BaseResponse;
import com.caixy.adminSystem.common.DeleteRequest;
import com.caixy.adminSystem.common.ErrorCode;
import com.caixy.adminSystem.common.ResultUtils;
import com.caixy.adminSystem.constant.UserConstant;
import com.caixy.adminSystem.exception.BusinessException;
import com.caixy.adminSystem.exception.ThrowUtils;

import com.caixy.adminSystem.model.dto.StudentGrades.StudentGradesAddRequest;
import com.caixy.adminSystem.model.dto.StudentGrades.StudentGradesQueryRequest;
import com.caixy.adminSystem.model.dto.StudentGrades.StudentGradesUpdateRequest;
import com.caixy.adminSystem.model.entity.StudentGrades;
import com.caixy.adminSystem.model.entity.User;
import com.caixy.adminSystem.model.vo.StudentGrades.StudentGradesVO;
import com.caixy.adminSystem.model.vo.studentGrade.GradeForStudentVO;
import com.caixy.adminSystem.model.vo.studentGrade.StudentsGradeForAdminVO;
import com.caixy.adminSystem.service.StudentGradesService;
import com.caixy.adminSystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 成绩接口
 */
@RestController
@RequestMapping("/score")
@Slf4j
public class ScoreController
{
    @Resource
    private StudentGradesService studentGradesService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建
     *
     * @param postAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add/elective")
    @AuthCheck(mustRole = UserConstant.TEACHER_ROLE)
    public BaseResponse<Boolean> addStudentElectiveGrades(@RequestBody @Valid StudentGradesAddRequest postAddRequest, HttpServletRequest request)
    {
        return ResultUtils.success(studentGradesService.addElectiveCourseGrade(postAddRequest, userService.getLoginUser(request).getId()));
    }

    @GetMapping("/get/grades/me")
    @AuthCheck(mustRole = UserConstant.STUDENT_ROLE)
    public BaseResponse<List<GradeForStudentVO>> getStudentGrades(HttpServletRequest request,
                                                                  @RequestParam(name = "semesterId", required = false) Long semesterId) {
        User loginUser = userService.getLoginUser(request);
        return ResultUtils.success(studentGradesService.getStudentGrades(loginUser.getId(), semesterId));
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteStudentGrades(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request)
    {
        if (deleteRequest == null || deleteRequest.getId() <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        StudentGrades oldStudentGrades = studentGradesService.getById(id);
        ThrowUtils.throwIf(oldStudentGrades == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldStudentGrades.getCreatorId().equals(user.getId()) && !userService.isAdmin(request))
        {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = studentGradesService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param postUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateStudentGrades(@RequestBody StudentGradesUpdateRequest postUpdateRequest)
    {
        if (postUpdateRequest == null || postUpdateRequest.getId() <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        StudentGrades post = new StudentGrades();
        BeanUtils.copyProperties(postUpdateRequest, post);

        long id = postUpdateRequest.getId();
        // 判断是否存在
        StudentGrades oldStudentGrades = studentGradesService.getById(id);
        ThrowUtils.throwIf(oldStudentGrades == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = studentGradesService.updateById(post);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<StudentGradesVO> getStudentGradesVOById(long id, HttpServletRequest request)
    {
        if (id <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        StudentGrades post = studentGradesService.getById(id);
        if (post == null)
        {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(studentGradesService.getStudentGradesVO(post, request));
    }

    /**
     * 获取学生成绩
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/5/3 下午4:39
     */
    @GetMapping("/get/stu/grade/byId")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<StudentGradesVO> getStudentGradesVOByStuId(@RequestParam("stuId") long stuId, HttpServletRequest request)
    {
        if (stuId <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        StudentGradesVO studentGradesVOByStuId = studentGradesService.getStudentGradesVOByStuId(stuId);
        return ResultUtils.success(studentGradesVOByStuId);
    }

    @GetMapping("/get/stu/grade/{taskId}/{courseTaskId}/{subjectId}")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<StudentsGradeForAdminVO>> getStudentGradesVOByTaskSubject(
            @PathVariable("taskId") Long taskId,
            @PathVariable("courseTaskId") Long courseTaskId,
            @PathVariable("subjectId") Long subjectId)
    {
        return ResultUtils.success(studentGradesService.getStudentGradesByCourseTaskIdAndSubjectId(taskId, courseTaskId, subjectId));
    }


    /**
     * 分页获取列表（仅管理员）
     *
     * @param postQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<StudentGrades>> listStudentGradesByPage(@RequestBody StudentGradesQueryRequest postQueryRequest)
    {
        long current = postQueryRequest.getCurrent();
        long size = postQueryRequest.getPageSize();
        Page<StudentGrades> postPage = studentGradesService.page(new Page<>(current, size), studentGradesService.getQueryWrapper(postQueryRequest));
        return ResultUtils.success(postPage);
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param postQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<StudentGradesVO>> listStudentGradesVOByPage(@RequestBody StudentGradesQueryRequest postQueryRequest, HttpServletRequest request)
    {
        long current = postQueryRequest.getCurrent();
        long size = postQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<StudentGrades> postPage = studentGradesService.page(new Page<>(current, size), studentGradesService.getQueryWrapper(postQueryRequest));
        return ResultUtils.success(studentGradesService.getStudentGradesVOPage(postPage, request));
    }
}
