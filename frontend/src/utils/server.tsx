import {getClassesOptionDataVoByPageUsingPost1} from "@/services/backend/classesController";
import {message} from "antd";
import {getAllSubjectsVoUsingGet1} from "@/services/backend/subjectController";
import React from "react";

const fetchCollegeCascadeOption = async (setCascadeOption?: React.Dispatch<React.SetStateAction<API.AllClassesOptionDataVO[]>>) =>
{
    const returnValue: API.AllClassesOptionDataVO[] = []
    try {
        const { data, code } = await getClassesOptionDataVoByPageUsingPost1({});
        if (data && code === 0)
        {
            setCascadeOption?.(data)
            // 直接修改 returnValue 的内容
            returnValue.push(...data);
        }
    }
    catch (e: any)
    {
        message.error("获取选项配置失败: ", e.message)
        setCascadeOption?.([])
    }
    return returnValue
}

const fetchSubjectOption = async (setSubjectOption: React.Dispatch<React.SetStateAction<OptionProps[]>>) =>
{
    let returnValue: OptionProps[] = []
    try
    {
        const { data, code } = await getAllSubjectsVoUsingGet1();
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
