package com.caixy.adminSystem.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.caixy.adminSystem.model.dto.studentInfo.StudentInfoAddRequest;
import com.caixy.adminSystem.model.dto.studentInfo.StudentInfoQueryRequest;
import com.caixy.adminSystem.model.entity.StudentInfo;
import com.caixy.adminSystem.model.entity.User;
import com.caixy.adminSystem.model.vo.StudentInfo.StudentInfoVO;
import com.caixy.adminSystem.model.vo.studentCourseSelection.CourseStudentInfoVO;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

/**
 * @author CAIXYPROMISE
 * @description 针对表【student_score】的数据库操作Service
 * @createDate 2024-04-02 22:30:06
 */
public interface StudentInfoService extends IService<StudentInfo>
{
    Long addStudentInfo(StudentInfoAddRequest postAddRequest, HttpServletRequest request, User loginUser);

    Boolean deleteStudent(Long stuId);

    /**
     * 校验
     *
     * @param post
     * @param add
     */
    void validStudentInfo(StudentInfo post, boolean add);

    /**
     * 获取查询条件
     *
     * @param postQueryRequest
     * @return
     */
    LambdaQueryWrapper<StudentInfo> getQueryWrapper(StudentInfoQueryRequest postQueryRequest);

    /**
     * 从 ES 查询
     *
     * @param postQueryRequest
     * @return
     */
    Page<StudentInfo> searchFromEs(StudentInfoQueryRequest postQueryRequest);

    /**
     * 获取帖子封装
     *
     * @param post
     * @return
     */
    StudentInfoVO getStudentInfoVO(StudentInfo post);

    List<StudentInfoVO> getStudentInfoVoByIds(Collection<Long> studentIds);

    StudentInfoVO getStudentInfoVOById(Long id);

    List<StudentInfoVO> getStudentByClassesIds(List<Long> classesIds);

    List<CourseStudentInfoVO> getStudentsByCourseSelectionAndSubject(Long courseSelectionId, Long subjectId);

    /**
     * 分页获取帖子封装
     *
     * @param postPage
     * @return
     */
    Page<StudentInfoVO> getStudentInfoVOPage(Page<StudentInfo> postPage);

    List<StudentInfo> batchListStudentInfoByIds(List<Long> stuIds);
}
