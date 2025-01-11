import {getClassesOptionDataVoByPageUsingPost1} from "@/services/backend/classesController";
import {message} from "antd";
import React from "react";
import {getAllSubjectAnalysesUsingGet1, getGradesAnalysisByFilterUsingPost1} from "@/services/backend/analysisController";
import {EChartsOption} from "echarts";
import {convertToPolarOption, getAllSubjectAnalysis} from "@/pages/Admin/Analysis/Echarts-option";


/**
 * 获取所有学科分析数据
 *
 * @author CAIXYPROMISE
 * @since 2024/5/7 下午4:02
 * @version 1.0
 */
const fetchAllSubjectAnalysis = async (setData: React.Dispatch<React.SetStateAction<EChartsOption>>) =>
{
    try
    {
        const { data, code } = await getAllSubjectAnalysesUsingGet1();
        if (data && code === 0)
        {
            setData(getAllSubjectAnalysis(data))
        }
    }
    catch (e: any)
    {
        message.error(e.message)
    }
}

const fetchAnalysisByFilter = async (
    payload: API.GradeAnalysisFilterDTO,
    setFilterCharts: React.Dispatch<React.SetStateAction<EChartsOption>>,
    setFilterBarCharts: React.Dispatch<React.SetStateAction<EChartsOption>>
) =>
{
    try
    {
        const { data, code } = await getGradesAnalysisByFilterUsingPost1(payload);
        if (code === 0 && data )
        {
            setFilterCharts(convertToPolarOption(data));
            setFilterBarCharts(getAllSubjectAnalysis(data));
            return data;
        }
    }
    catch (error: any)
    {
        message.error(error.message)
    }
}


export {
    fetchAllSubjectAnalysis,
    fetchAnalysisByFilter
}
