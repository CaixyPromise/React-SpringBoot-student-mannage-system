package com.caixy.adminSystem.service.impl;

import com.caixy.adminSystem.model.vo.studentGrade.GradeForStudentVO;
import com.caixy.adminSystem.service.StudentGradesService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author CAIXYPROMISE
 * @since 2025/1/28 2:20
 */
@SpringBootTest
class StudentGradesServiceImplTest
{
    @Resource
    private StudentGradesService studentGradesService;

    @Test
    void getGradesByStudentId() {
        List<GradeForStudentVO> studentGrades = studentGradesService.getStudentGrades(1876289538437013506L, null);
        System.out.println(studentGrades);
        List<GradeForStudentVO> studentGrades_semester = studentGradesService.getStudentGrades(1876289538437013506L, 187218993329424797L);
        System.out.println(studentGrades_semester);


    }
}