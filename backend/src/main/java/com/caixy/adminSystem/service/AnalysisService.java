package com.caixy.adminSystem.service;

import com.caixy.adminSystem.model.dto.analysis.GradeAnalysisFilterDTO;
import com.caixy.adminSystem.model.vo.analysis.StudentAnalysisVO;
import com.caixy.adminSystem.model.vo.analysis.SubjectAnalysis;

import java.util.List;

/**
 * 数据分析服务类
 *
 * @name: com.caixy.adminSystem.service.AnalysisService
 * @author: CAIXYPROMISE
 * @since: 2024-05-05 19:14
 **/
public interface AnalysisService
{
    /**
     * 
     */
    List<SubjectAnalysis> getAllSubjectAnalyses();

//    SubjectAnalysis getSubjectAnalysisById(Long subjectId);

    StudentAnalysisVO getAllSubjectAnalysesByStudentId(Long studentId);

    List<SubjectAnalysis> getGradesAnalysisByFilter(GradeAnalysisFilterDTO gradeAnalysisFilterDTO);
}
