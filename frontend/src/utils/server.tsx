import {getClassesOptionDataVoByPageUsingPOST} from "@/services/backend/classesController";
import {message} from "antd";
import {getAllSubjectsVoUsingGET} from "@/services/backend/subjectController";
import React from "react";

const fetchCollegeCascadeOption = async (setCascadeOption: React.Dispatch<React.SetStateAction<API.AllClassesOptionDataVO[]>>) =>
{
    let returnValue: API.AllClassesOptionDataVO[] = []
    try
    {
        const { data, code } = await getClassesOptionDataVoByPageUsingPOST({});
        if (data && code === 0)
        {
            setCascadeOption(data)
        }
        // @ts-ignore
        returnValue = data

    }
    catch (e: any)
    {
        message.error("获取选项配置失败: ", e.message)
        setCascadeOption([])
    }
    return returnValue

}

const fetchSubjectOption = async (setSubjectOption: React.Dispatch<React.SetStateAction<OptionProps[]>>) =>
{
    let returnValue: OptionProps[] = []
    try
    {
        const { data, code } = await getAllSubjectsVoUsingGET();
        if (data && code === 0)
        {
            const optionList: OptionProps[] = []
            data.map(item =>
            {
                optionList.push({
                    // @ts-ignore
                    label: item?.name,
                    // @ts-ignore
                    value: item?.id,
                    gradeMax: item.gradeMax,
                    gradeMin: item.gradeMin
                })
            })
            setSubjectOption(optionList)
            returnValue = optionList
        }
    }
    catch (e: any)
    {
        message.error("获取选项配置失败: ", e.message)
    }
    return returnValue
}


export {
    fetchSubjectOption,
    fetchCollegeCascadeOption
}
