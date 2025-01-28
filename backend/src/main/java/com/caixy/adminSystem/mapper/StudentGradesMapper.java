package com.caixy.adminSystem.mapper;

import com.caixy.adminSystem.model.entity.StudentGrades;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.caixy.adminSystem.model.vo.studentGrade.GradeForStudentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author CAIXYPROMISE
* @description 针对表【student_grades(学生成绩表)】的数据库操作Mapper
* @createDate 2024-04-28 20:44:20
* @Entity com.caixy.adminSystem.model.entity.StudentGrades
*/
public interface StudentGradesMapper extends BaseMapper<StudentGrades> {
    List<GradeForStudentVO> selectStudentGrades(@Param("userId") Long userId, @Param("semesterId") Long semesterId);
}

