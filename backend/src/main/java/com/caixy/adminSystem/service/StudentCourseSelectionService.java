package com.caixy.adminSystem.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.caixy.adminSystem.model.dto.studentCourseSelection.StudentCourseSelectionQueryRequest;
import com.caixy.adminSystem.model.dto.studentCourseSelection.StudentSelectCourseRequest;
import com.caixy.adminSystem.model.entity.StudentCourseSelection;
import com.caixy.adminSystem.model.vo.studentCourseSelection.StudentCourseSubjectVO;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 学生选课信息表服务
 *


 */
public interface StudentCourseSelectionService extends IService<StudentCourseSelection> {

    @Transactional(rollbackFor = Exception.class)
    void cancelCoursesForStudent(StudentSelectCourseRequest request, Long studentId);

    @Transactional(rollbackFor = Exception.class)
    void selectCoursesForStudent(StudentSelectCourseRequest request, Long studentId);

    /**
     * 校验数据
     *
     * @param studentCourseSelection
     * @param add 对创建的数据进行校验
     */
    void validStudentCourseSelection(StudentCourseSelection studentCourseSelection, boolean add);

    /**
     * 获取查询条件
     *
     * @param studentCourseSelectionQueryRequest
     * @return
     */
    QueryWrapper<StudentCourseSelection> getQueryWrapper(StudentCourseSelectionQueryRequest studentCourseSelectionQueryRequest);

    List<StudentCourseSubjectVO> listSubjectsForStudent(
            Long studentId,
            Long courseSelectionId
    );

    /**
     * 获取学生选课信息表封装
     *
     * @param studentCourseSelection
     * @param request
     * @return
     */
    StudentCourseSubjectVO getStudentCourseSelectionVO(StudentCourseSelection studentCourseSelection, HttpServletRequest request);

    /**
     * 分页获取学生选课信息表封装
     *
     * @param studentCourseSelectionPage
     * @param request
     * @return
     */
    Page<StudentCourseSubjectVO> getStudentCourseSelectionVOPage(Page<StudentCourseSelection> studentCourseSelectionPage, HttpServletRequest request);
}
