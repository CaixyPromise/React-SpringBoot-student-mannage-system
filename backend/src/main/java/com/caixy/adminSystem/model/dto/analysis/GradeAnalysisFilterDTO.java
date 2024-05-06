package com.caixy.adminSystem.model.dto.analysis;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

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
    /**
     * 需要查询的学院id
     */
    List<Long> departmentIds;

    /**
     * 需要查询的专业id
     */
    List<Long> majorIds;

    /**
     * 需要查询的班级id
     */
    List<Long> classIds;

    private static final long serialVersionUID = -1L;
}
