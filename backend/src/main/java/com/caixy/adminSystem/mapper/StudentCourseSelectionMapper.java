package com.caixy.adminSystem.mapper;

import com.caixy.adminSystem.model.entity.StudentCourseSelection;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author CAIXYPROMISE
* @description 针对表【student_course_selection(学生选课记录表)】的数据库操作Mapper
* @createDate 2024-12-26 14:46:52
* @Entity com.caixy.adminSystem.model.entity.StudentCourseSelection
*/
public interface StudentCourseSelectionMapper extends BaseMapper<StudentCourseSelection> {

    List<StudentCourseSelection> listAllByStudentAndSubjects(
            @Param("studentId") Long studentId,
            @Param("courseSelectionId") Long courseSelectionId,
            @Param("subjectIds") List<Long> subjectIds);

    int recoverStudentSelections(@Param("ids") List<Long> ids);

    /**
     * 查询学生已选科目的 subjectId 列表
     */
    List<Long> getSelectedSubjectIdsByStudent(
            @Param("studentId") Long studentId,
            @Param("courseSelectionId") Long courseSelectionId
    );

    /**
     * 批量插入学生选课记录
     */
    int insertStudentSelections(@Param("records") List<StudentCourseSelection> records);

    /**
     * 查询学生是否已经选过某些科目
     */
    List<StudentCourseSelection> listSelectedSubjects(
            @Param("studentId") Long studentId,
            @Param("courseSelectionId") Long courseSelectionId,
            @Param("subjectIds") List<Long> subjectIds
    );

    int deleteStudentSelections(Long studentId, Long courseSelectionId, List<Long> subjectIds);
}




