import {FormInstance, message} from "antd";
import {getStudentGradesVoByStuIdUsingGET} from "@/services/backend/scoreController";
import React from "react";

const fetchStudentInfo = async (
    currentRow: Student.CurrentRowProps,
    setScoreModalVisible: (visible: boolean) => void,
    setLoading: React.Dispatch<React.SetStateAction<boolean>>,
    setStudentData: React.Dispatch<React.SetStateAction<API.StudentGradesVO>>,
    setGradeItems: React.Dispatch<React.SetStateAction<API.GradeItem[]>>,
    ) =>
{
    if (currentRow === undefined)
    {
        // message.error("学生信息不能为空");
        setScoreModalVisible(false);
        return;
    }
    const { id } = currentRow
    if (id === null || id === undefined || id === "")
    {
        message.error("学生id不能为空");
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
    fetchStudentInfo
}
