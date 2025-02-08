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
import com.caixy.adminSystem.model.dto.studentCourseSelection.*;
import com.caixy.adminSystem.model.entity.StudentCourseSelection;
import com.caixy.adminSystem.model.entity.User;
import com.caixy.adminSystem.model.vo.studentCourseSelection.StudentCourseSubjectVO;
import com.caixy.adminSystem.model.vo.studentCourseSelection.StudentWithCourseSelectionVO;
import com.caixy.adminSystem.service.StudentCourseSelectionService;
import com.caixy.adminSystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 学生选课信息表接口
 *


 */
@RestController
@RequestMapping("/studentCourseSelection")
@Slf4j
public class StudentCourseSelectionController {

    @Resource
    private StudentCourseSelectionService studentCourseSelectionService;

    @Resource
    private UserService userService;

    @Resource
    private StudentCourseSelectionService studentCourseSubjectService;

    /**
     * 获取学生选课信息列表
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @version 2025/2/7 18:30
     */
    @GetMapping("/allCourseSelections/{courseSelectionId}")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<StudentWithCourseSelectionVO>> getAllSelections(@PathVariable Long courseSelectionId) {
        return ResultUtils.success(studentCourseSelectionService.getStudentCourseSelections(courseSelectionId));
    }

    /**
     * 获取选课未满的学生
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @version 2025/2/7 18:30
     */
    @GetMapping("/unqualified/{courseSelectionId}")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<StudentWithCourseSelectionVO>> getUnqualifiedStudents(@PathVariable Long courseSelectionId) {
        return ResultUtils.success(studentCourseSelectionService.getUnqualifiedStudents(courseSelectionId));
    }

    @PostMapping("/autoAssign")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Integer> assignRandomCoursesToUnqualifiedStudents(@RequestBody AutoAssignRequest autoAssignRequest) {
        return ResultUtils.success(studentCourseSelectionService.assignRandomCoursesToUnqualifiedStudents(autoAssignRequest.getCourseSelectionId()));
    }

    /**
     * 学生选课
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @version 2025/1/11 3:49
     */
    @PostMapping("/select")
    public BaseResponse<Boolean> selectCourse(@RequestBody StudentSelectCourseRequest studentSelectCourseRequest, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        studentCourseSelectionService.selectCoursesForStudent(studentSelectCourseRequest, loginUser.getId());
        return ResultUtils.success(true);
    }

    /**
     * 学生退选
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @version 2025/1/11 4:27
     */
    @PostMapping("/cancel")
    public BaseResponse<Boolean> cancelCourse(@RequestBody StudentSelectCourseRequest studentSelectCourseRequest, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        studentCourseSelectionService.cancelCoursesForStudent(studentSelectCourseRequest, loginUser.getId());
        return ResultUtils.success(true);
    }

    @GetMapping("/availableSubjects")
    @AuthCheck(mustRole = UserConstant.STUDENT_ROLE)
    public BaseResponse<List<StudentCourseSubjectVO>> listAvailableSubjects(
            @RequestParam("courseSelectionId") Long courseSelectionId,
            HttpServletRequest request
    ) {
        Long studentId = userService.getLoginUser(request).getId();
        List<StudentCourseSubjectVO> subjectList = studentCourseSubjectService
                .listSubjectsForStudent(studentId, courseSelectionId);
        return ResultUtils.success(subjectList);
    }

    // region 增删改查

    /**
     * 创建学生选课信息表
     *
     * @param studentCourseSelectionAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addStudentCourseSelection(@RequestBody StudentCourseSelectionAddRequest studentCourseSelectionAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(studentCourseSelectionAddRequest == null, ErrorCode.PARAMS_ERROR);
        // todo 在此处将实体类和 DTO 进行转换
        StudentCourseSelection studentCourseSelection = new StudentCourseSelection();
        BeanUtils.copyProperties(studentCourseSelectionAddRequest, studentCourseSelection);
        // 数据校验
        studentCourseSelectionService.validStudentCourseSelection(studentCourseSelection, true);
        // todo 填充默认值
        User loginUser = userService.getLoginUser(request);
//        studentCourseSelection.setUserId(loginUser.getId());
        // 写入数据库
        boolean result = studentCourseSelectionService.save(studentCourseSelection);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newStudentCourseSelectionId = studentCourseSelection.getId();
        return ResultUtils.success(newStudentCourseSelectionId);
    }

    /**
     * 删除学生选课信息表
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteStudentCourseSelection(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        StudentCourseSelection oldStudentCourseSelection = studentCourseSelectionService.getById(id);
        ThrowUtils.throwIf(oldStudentCourseSelection == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        checkIsSelfOrAdmin(oldStudentCourseSelection, user);
        // 操作数据库
        boolean result = studentCourseSelectionService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新学生选课信息表（仅管理员可用）
     *
     * @param studentCourseSelectionUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateStudentCourseSelection(@RequestBody StudentCourseSelectionUpdateRequest studentCourseSelectionUpdateRequest) {
        if (studentCourseSelectionUpdateRequest == null || studentCourseSelectionUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        StudentCourseSelection studentCourseSelection = new StudentCourseSelection();
        BeanUtils.copyProperties(studentCourseSelectionUpdateRequest, studentCourseSelection);
        // 数据校验
        studentCourseSelectionService.validStudentCourseSelection(studentCourseSelection, false);
        // 判断是否存在
        long id = studentCourseSelectionUpdateRequest.getId();
        StudentCourseSelection oldStudentCourseSelection = studentCourseSelectionService.getById(id);
        ThrowUtils.throwIf(oldStudentCourseSelection == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = studentCourseSelectionService.updateById(studentCourseSelection);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取学生选课信息表（封装类）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<StudentCourseSubjectVO> getStudentCourseSelectionVOById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        StudentCourseSelection studentCourseSelection = studentCourseSelectionService.getById(id);
        ThrowUtils.throwIf(studentCourseSelection == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(studentCourseSelectionService.getStudentCourseSelectionVO(studentCourseSelection, request));
    }

    /**
     * 分页获取学生选课信息表列表（仅管理员可用）
     *
     * @param studentCourseSelectionQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<StudentCourseSelection>> listStudentCourseSelectionByPage(@RequestBody StudentCourseSelectionQueryRequest studentCourseSelectionQueryRequest) {
        long current = studentCourseSelectionQueryRequest.getCurrent();
        long size = studentCourseSelectionQueryRequest.getPageSize();
        // 查询数据库
        Page<StudentCourseSelection> studentCourseSelectionPage = studentCourseSelectionService.page(new Page<>(current, size),
                studentCourseSelectionService.getQueryWrapper(studentCourseSelectionQueryRequest));
        return ResultUtils.success(studentCourseSelectionPage);
    }

    /**
     * 分页获取学生选课信息表列表（封装类）
     *
     * @param studentCourseSelectionQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<StudentCourseSubjectVO>> listStudentCourseSelectionVOByPage(@RequestBody StudentCourseSelectionQueryRequest studentCourseSelectionQueryRequest,
                                                                                         HttpServletRequest request) {
        long current = studentCourseSelectionQueryRequest.getCurrent();
        long size = studentCourseSelectionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<StudentCourseSelection> studentCourseSelectionPage = studentCourseSelectionService.page(new Page<>(current, size),
                studentCourseSelectionService.getQueryWrapper(studentCourseSelectionQueryRequest));
        // 获取封装类
        return ResultUtils.success(studentCourseSelectionService.getStudentCourseSelectionVOPage(studentCourseSelectionPage, request));
    }

    /**
     * 分页获取当前登录用户创建的学生选课信息表列表
     *
     * @param studentCourseSelectionQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<StudentCourseSubjectVO>> listMyStudentCourseSelectionVOByPage(@RequestBody StudentCourseSelectionQueryRequest studentCourseSelectionQueryRequest,
                                                                                           HttpServletRequest request) {
        ThrowUtils.throwIf(studentCourseSelectionQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser(request);
        studentCourseSelectionQueryRequest.setUserId(loginUser.getId());
        long current = studentCourseSelectionQueryRequest.getCurrent();
        long size = studentCourseSelectionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<StudentCourseSelection> studentCourseSelectionPage = studentCourseSelectionService.page(new Page<>(current, size),
                studentCourseSelectionService.getQueryWrapper(studentCourseSelectionQueryRequest));
        // 获取封装类
        return ResultUtils.success(studentCourseSelectionService.getStudentCourseSelectionVOPage(studentCourseSelectionPage, request));
    }

    /**
     * 编辑学生选课信息表（给用户使用）
     *
     * @param studentCourseSelectionEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editStudentCourseSelection(@RequestBody StudentCourseSelectionEditRequest studentCourseSelectionEditRequest, HttpServletRequest request) {
        if (studentCourseSelectionEditRequest == null || studentCourseSelectionEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        StudentCourseSelection studentCourseSelection = new StudentCourseSelection();
        BeanUtils.copyProperties(studentCourseSelectionEditRequest, studentCourseSelection);
        // 数据校验
        studentCourseSelectionService.validStudentCourseSelection(studentCourseSelection, false);
        User loginUser = userService.getLoginUser(request);
        // 判断是否存在
        long id = studentCourseSelectionEditRequest.getId();
        StudentCourseSelection oldStudentCourseSelection = studentCourseSelectionService.getById(id);
        ThrowUtils.throwIf(oldStudentCourseSelection == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        checkIsSelfOrAdmin(oldStudentCourseSelection, loginUser);
        // 操作数据库
        boolean result = studentCourseSelectionService.updateById(studentCourseSelection);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    // endregion

    private void checkIsSelfOrAdmin(StudentCourseSelection studentCourseSelection, User loginUser)
    {
//        if (!studentCourseSelection.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser))
//        {
//            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
//        }
    }
}
