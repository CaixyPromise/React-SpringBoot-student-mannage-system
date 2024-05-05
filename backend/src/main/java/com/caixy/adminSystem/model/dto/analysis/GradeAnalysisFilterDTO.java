package com.caixy.adminSystem.model.dto.analysis;

import lombok.Data;

import java.io.Serializable;

/**
 * 成绩分析个体分析类
 *
 * @name: com.caixy.adminSystem.model.dto.analysis.GradeAnalysisFilterDTO
 * @author: CAIXYPROMISE
 * @since: 2024-05-05 22:49
 **/
@Data
public class GradeAnalysisFilterDTO implements Serializable
{
    Long departmentId;
    Long majorId;
    Long classId;
    private static final long serialVersionUID = -1L;
}
