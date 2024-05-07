package com.caixy.adminSystem.controller;

import com.caixy.adminSystem.annotation.AuthCheck;
import com.caixy.adminSystem.common.BaseResponse;
import com.caixy.adminSystem.common.ResultUtils;
import com.caixy.adminSystem.constant.UserConstant;
import com.caixy.adminSystem.model.dto.analysis.GradeAnalysisFilterDTO;
import com.caixy.adminSystem.model.vo.analysis.StudentAnalysisVO;
import com.caixy.adminSystem.model.vo.analysis.SubjectAnalysis;
import com.caixy.adminSystem.service.AnalysisService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据分析接口控制器
 *
 * @name com.caixy.adminSystem.controller.AnalysisController
 * @author CAIXYPROMISE
 * @since 2024-05-05 19:12
 **/
@RestController
@RequestMapping("/analysis")
@Slf4j
@AllArgsConstructor
public class AnalysisController
{
    private final AnalysisService analysisService;

    /**
     * 获取所有学科数据分析信息
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/5/6 下午2:47
     */
    @GetMapping("/all")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<SubjectAnalysis>> getAllSubjectAnalyses()
    {
        return ResultUtils.success(analysisService.getAllSubjectAnalyses());
    }

    /**
     * 获取指定学生成绩分析信息
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/5/5 下午10:16
     */
    @GetMapping("/student")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<StudentAnalysisVO> getAllSubjectAnalysesByStudentId(@RequestParam("studentId") Long studentId)
    {
        return ResultUtils.success(analysisService.getAllSubjectAnalysesByStudentId(studentId));
    }

    /**
     * 获取指定学院、专业或班级的成绩分析信息
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/5/5 下午10:41
     */
    @PostMapping("/depart")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<SubjectAnalysis>> getGradesAnalysisByFilter(@RequestBody GradeAnalysisFilterDTO gradeAnalysisFilterDTO)
    {
        return ResultUtils.success(analysisService.getGradesAnalysisByFilter(gradeAnalysisFilterDTO));
    }
}
