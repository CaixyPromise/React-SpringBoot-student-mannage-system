package com.caixy.adminSystem.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.caixy.adminSystem.model.dto.StudentGrades.StudentGradesAddRequest;
import com.caixy.adminSystem.model.dto.StudentGrades.StudentGradesQueryRequest;
import com.caixy.adminSystem.model.entity.StudentGrades;
import com.caixy.adminSystem.model.entity.StudentInfo;
import com.caixy.adminSystem.model.vo.StudentGrades.StudentGradesVO;
import com.caixy.adminSystem.model.vo.studentGrade.StudentsGradeForAdminVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author CAIXYPROMISE
* @description 针对表【student_grades】的数据库操作Service
* @createDate 2024-04-02 22:30:06
*/
public interface StudentGradesService extends IService<StudentGrades> {

    StudentGradesVO getStudentGradesVOByStuId(long stuId);

    /**
     * 校验
     *
     * @param post
     * @param add
     * @param userId
     * @param studentInfoList
     */
    void validStudentGrades(StudentGradesAddRequest request, boolean isElectives, Long userId, List<StudentInfo> studentInfoList);

    Boolean addElectiveCourseGrade(StudentGradesAddRequest request, Long userId);

    List<StudentsGradeForAdminVO> getStudentGradesByCourseTaskIdAndSubjectId(Long courseTaskId, Long subjectId);

    /**
     * 获取查询条件
     *
     * @param postQueryRequest
     * @return
     */
    QueryWrapper<StudentGrades> getQueryWrapper(StudentGradesQueryRequest postQueryRequest);

    /**
     * 从 ES 查询
     *
     * @param postQueryRequest
     * @return
     */
    Page<StudentGrades> searchFromEs(StudentGradesQueryRequest postQueryRequest);

    /**
     * 获取帖子封装
     *
     * @param post
     * @param request
     * @return
     */
    StudentGradesVO getStudentGradesVO(StudentGrades post, HttpServletRequest request);

    /**
     * 分页获取帖子封装
     *
     * @param postPage
     * @param request
     * @return
     */
    Page<StudentGradesVO> getStudentGradesVOPage(Page<StudentGrades> postPage, HttpServletRequest request);

    List<StudentGrades> listBySubjectId(Long subjectId);
}
