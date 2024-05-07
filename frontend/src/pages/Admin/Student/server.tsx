import {message} from "antd";
import React from "react";
import {getAllSubjectAnalysesByStudentIdUsingGET} from "@/services/backend/analysisController";
import {getStudentGradesVoByStuIdUsingGET} from "@/services/backend/scoreController";
import {StudentAnalysisOption} from "@/pages/Admin/Student/components/ScoreInfoModal/option";
import {EChartsOption} from "echarts";

const extraId = (currentRow: Student.CurrentRowProps): boolean | string =>
{
    if (currentRow === undefined)
    {
        return false;
    }
    const { id } = currentRow
    if (id === null || id === undefined || id === "")
    {
        return false;
    }
    return id;
}


const fetchStudentGradesAnalyses = async (
    currentRow: Student.CurrentRowProps,
    setLoading: React.Dispatch<React.SetStateAction<boolean>>,
    setData: React.Dispatch<React.SetStateAction<EChartsOption>>,
): Promise<EChartsOption | undefined | null>=>
{
    const id = extraId(currentRow);
    if (id === false)
    {
        message.error("学生id不能为空");
        return null;
    }
    setLoading(true);
    try {
        // @ts-ignore
        const {data, code} = await getAllSubjectAnalysesByStudentIdUsingGET({studentId: id});
        if (code === 0 && data)
        {
            const prepareData = StudentAnalysisOption(data);
            setData(prepareData);
            return prepareData;
        }
    }
    catch (e: any)
    {
        message.error(e.message)
        return null
    }
    finally
    {
        setLoading(false);
    }
}


const fetchStudentInfo = async (
    currentRow: Student.CurrentRowProps,
    setScoreModalVisible: (visible: boolean) => void,
    setLoading: React.Dispatch<React.SetStateAction<boolean>>,
    setStudentData: React.Dispatch<React.SetStateAction<API.StudentGradesVO>>,
    setGradeItems: React.Dispatch<React.SetStateAction<API.GradeItem[]>>,
) =>
{
    const id = extraId(currentRow);
    if (id === false)
    {
        message.error("学生信息不能为空");
        setScoreModalVisible(false);
        return;
    }
    setLoading(true);
    try
    {
        // @ts-ignore
        const { data, code } = await getStudentGradesVoByStuIdUsingGET({ stuId: id })
        if (code === 0 && data)
        {
            setStudentData(data);
            setGradeItems(data.gradeItem ?? []);
        }
    }
    catch (e: any)
    {
        message.error(e.message);
        setScoreModalVisible(false);
    }
    finally
    {
        setLoading(false);
    }
}


export {
    fetchStudentInfo,
    fetchStudentGradesAnalyses
}
