import {type ActionType, PageContainer, ProTable} from "@ant-design/pro-components";
import React, {useEffect, useRef, useState} from "react";
import {Button, Form, message} from "antd";
import {deleteSubjectsUsingPost1, getAllSubjectsVoUsingGet1} from "@/services/backend/subjectController";
import {PlusOutlined} from "@ant-design/icons";
import {getClassesOptionDataVoByPageUsingPost1} from "@/services/backend/classesController";
import UpdateInfoModal from "@/pages/Admin/Student/components/UpdateInfoModal";
import CreateInfoModal from "@/pages/Admin/Student/components/CreateInfoModal";
import {deleteStudentInfoUsingPost1, listStudentInfoByPageUsingPost1,} from "@/services/backend/studentController";
import {StudentColumns} from "@/pages/Admin/Student/Columns/columns";
import AddScoreInfoModal from "@/pages/Admin/Student/components/AddScoreInfoModal";
import ScoreInfoModal from "@/pages/Admin/Student/components/ScoreInfoModal";
import {fetchCollegeCascadeOption, fetchSubjectOption} from "@/utils/server";

const Index = () =>
{
    const [ form ] = Form.useForm();
    // 是否显示新建窗口
    const [ createModalVisible, setCreateModalVisible ] = useState<boolean>(false);
    // 是否显示更新窗口
    const [ updateModalVisible, setUpdateModalVisible ] = useState<boolean>(false);
    // 添加学生成绩窗口
    const [ addScoreInfoModalVisible, setAddScoreInfoModalVisible ] = useState<boolean>(false);
    // 查看学生成绩窗口
    const [ scoreInfoModalVisible, setScoreInfoModalVisible ] = useState<boolean>(false);
    const actionRef = useRef<ActionType>();
    // 当前用户点击的数据
    const [ currentRow, setCurrentRow ] = useState<Student.CurrentRowProps>();
    const [ cascadeOption, setCascadeOption ] = useState<API.AllClassesOptionDataVO[]>([]);
    const [ subjectOption, setSubjectOption ] = useState<OptionProps[]>();

    /**
     * 删除
     *
     * @author CAIXYPROMISE
     * @since 2024/5/1 下午6:33
     * @version 1.0
     */
    const handleDelete = async (record: { id: any; }) =>
    {
        try {
            const { data, code } = await deleteStudentInfoUsingPost1({
                id: record.id,
            })
            if (code === 0 && data)
            {
                message.success('删除成功，即将刷新')
                actionRef.current?.reloadAndRest?.()
            }
        }
        catch (e)
        {
            message.error(`删除失败: ${e.message}`)
        }
    }


    useEffect(() =>
    {
        fetchCollegeCascadeOption(setCascadeOption);
        fetchSubjectOption(setSubjectOption)
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

    return <>
        <PageContainer title={"学生管理"}>
            <ProTable
                columns={StudentColumns({
                    setCurrentRow,
                    setUpdateModalVisible,
                    handleDeleteFunction: handleDelete,
                    setAddScoreModalVisible: setAddScoreInfoModalVisible,
                    setScoreModalVisible: setScoreInfoModalVisible
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

                    const { data, code } = await listStudentInfoByPageUsingPost1({
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

            <AddScoreInfoModal
                scoreModalVisible={addScoreInfoModalVisible}
                setScoreModalVisible={setAddScoreInfoModalVisible}
                currentRow={currentRow}
                subjectItem={subjectOption}
            />
            {/*在这里被使用*/}
            <ScoreInfoModal
                scoreInfoModalVisible={scoreInfoModalVisible}
                setScoreInfoModalVisible={setScoreInfoModalVisible}
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
