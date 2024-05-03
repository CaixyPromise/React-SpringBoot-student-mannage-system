import {type ActionType, PageContainer, ProTable} from "@ant-design/pro-components";
import React, {useEffect, useRef, useState} from "react";
import {Button, Card, Descriptions, Form, message, Modal} from "antd";
import {deleteSubjectsUsingPOST, getAllSubjectsVoUsingGET} from "@/services/backend/subjectController";
import {PlusOutlined} from "@ant-design/icons";
import {getClassesOptionDataVoByPageUsingPOST} from "@/services/backend/classesController";
import UpdateInfoModal from "@/pages/Admin/Student/components/UpdateInfoModal";
import CreateInfoModal from "@/pages/Admin/Student/components/CreateInfoModal";
import {listStudentInfoByPageUsingPOST,} from "@/services/backend/studentController";
import {StudentColumns} from "@/pages/Admin/Student/Columns/columns";
import ScoreInfoModal from "@/pages/Admin/Student/components/ScoreInfoModal";

const Index = () =>
{
    const [ form ] = Form.useForm();
    // 是否显示新建窗口
    const [ createModalVisible, setCreateModalVisible ] = useState<boolean>(false);
    // 是否显示更新窗口
    const [ updateModalVisible, setUpdateModalVisible ] = useState<boolean>(false);
    const [ scoreInfoModalVisible, setScoreInfoModalVisible ] = useState<boolean>(false);

    const actionRef = useRef<ActionType>();
    // 当前用户点击的数据
    const [ currentRow, setCurrentRow ] = useState<Student.CurrentRowProps>();
    const [ cascadeOption, setCascadeOption ] = useState<API.AllClassesOptionDataVO[]>([]);
    const [ subjectOption, setSubjectOption] = useState<OptionProps>();

    /**
     * 删除
     *
     * @author CAIXYPROMISE
     * @since 2024/5/1 下午6:33
     * @version 1.0
     */
    const handleDelete = async (record: { id: any; }) =>
    {
        console.log(record)
        const { data, code } = await deleteSubjectsUsingPOST({
            id: record.id,
        })
        if (code === 0 && data)
        {
            message.success('删除成功，即将刷新')
            actionRef.current?.reloadAndRest?.()
        }
    }

    const fetchCascadeOption = async () =>
    {
        try
        {
            const { data, code } = await getClassesOptionDataVoByPageUsingPOST({});
            setCascadeOption(data || [])
        }
        catch (e: any)
        {
            message.error("获取选项配置失败: ", e.message)
            setCascadeOption([])
        }
    }

    const fetchSubjectOption = async () =>
    {
        try {
            const { data, code } = await getAllSubjectsVoUsingGET();
            if (data && code === 0)
            {
                const optionList: OptionProps[] = []
                data.map(item =>
                {
                    optionList.push({
                        // @ts-ignore
                        label: item.name,
                        // @ts-ignore
                        value: item.id,
                        max: item.gradeMax,
                        min: item.gradeMin
                    })
                })
                // @ts-ignore
                setSubjectOption(optionList)
            }
        }
        catch (e: any)
        {
            message.error("获取选项配置失败: ", e.message)
        }
    }

    useEffect(() =>
    {
        fetchCascadeOption();
        fetchSubjectOption()
    }, [])

    const packageRequestBody = (): Student.PayloadBody | null =>
    {
        const { stuName, stuSex, subjectName } = form.getFieldsValue();
        if (stuName && stuSex && subjectName && subjectName.length === 3)
        {
            return {
                stuName,
                stuSex,
                stuDeptId: subjectName[0],
                stuMajorId: subjectName[1],
                stuClassId: subjectName[2],
            }
        }
        return null
    }

    const setScoreModalVisible = (record: API.StudentInfoVO) =>
    {
        setCurrentRow(record)
        setScoreInfoModalVisible(true)
    }

    return <>
        <PageContainer title={"学生管理"}>
            <ProTable
                columns={StudentColumns({
                    setCurrentRow,
                    setUpdateModalVisible,
                    handleDeleteFunction: handleDelete,
                    setScoreModalVisible
                })}
                actionRef={actionRef}
                rowKey="id"
                toolBarRender={() => [
                    <Button
                        type="primary"
                        key="primary"
                        onClick={() =>
                        {
                            setCreateModalVisible(true);
                        }}
                    >
                        <PlusOutlined/> 新建
                    </Button>,
                ]}
                request={async (params, sort, filter) =>
                {
                    const sortField = Object.keys(sort)?.[0];
                    const sortOrder = sort?.[sortField] ?? undefined;

                    const { data, code } = await listStudentInfoByPageUsingPOST({
                        ...params,
                        sortField,
                        sortOrder,
                        ...filter,
                    } as API.StudentInfoQueryRequest);

                    return {
                        success: code === 0,
                        data: data?.records || [],
                        total: Number(data?.total) || 0,
                    };
                }}
            />



            <CreateInfoModal
                createModalVisible={createModalVisible}
                setCreateModalVisible={setCreateModalVisible}
                cascadeOption={cascadeOption}
                actionRef={actionRef}
                packageRequestBody={packageRequestBody}
            />

            <ScoreInfoModal
                scoreModalVisible={scoreInfoModalVisible}
                setScoreModalVisible={setScoreInfoModalVisible}
                currentRow={currentRow}
                subjectItem={subjectOption}
            />

            <UpdateInfoModal
                updateModalVisible={updateModalVisible}
                setUpdateModalVisible={setUpdateModalVisible}
                cascadeOption={cascadeOption}
                actionRef={actionRef}
                packageRequestBody={packageRequestBody}
                currentRow={{
                    ...currentRow,
                    packageRequestBody
                }}
            />
        </PageContainer>
    </>
}

export default Index;
