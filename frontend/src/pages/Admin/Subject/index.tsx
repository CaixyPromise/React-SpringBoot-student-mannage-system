import {type ActionType, PageContainer, ProTable} from "@ant-design/pro-components";
import {SubjectColumns} from "@/pages/Admin/Subject/Columns/columns";
import React, {useEffect, useRef, useState} from "react";
import {Button, Form, Input, InputNumber, message, Modal} from "antd";
import {
    addSubjectsUsingPOST, deleteSubjectsUsingPOST,
    listSubjectsByPageUsingPOST,
    updateSubjectsUsingPOST
} from "@/services/backend/subjectController";
import {PlusOutlined} from "@ant-design/icons";

const Index = () =>
{
    const [ form ] = Form.useForm();
    // 是否显示新建窗口
    const [ createModalVisible, setCreateModalVisible ] = useState<boolean>(false);
    // 是否显示更新窗口
    const [ updateModalVisible, setUpdateModalVisible ] = useState<boolean>(false);
    const actionRef = useRef<ActionType>();
    // 当前用户点击的数据
    const [ currentRow, setCurrentRow ] = useState<Subject.CurrentRow>();
    const [gradeMax, setGradeMax] = useState<number>(100);
    const [gradeMin, setGradeMin] = useState<number>(0);


    const handleDelete = async (record) =>
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

    const checkScoreIsValid = async (): Promise<boolean> =>
    {
        if (gradeMax <= gradeMin)
        {
            message.error("分数最大值必须大于分数最小值");
            return false;
        }
        if (gradeMin < 0)
        {
            message.error("分数最小值必须大于等于0");
            return false;
        }
        if (gradeMax > Number.MAX_VALUE)
        {
            message.error(`分数最大值必须小于等于${Number.MAX_VALUE}`);
            return false;
        }
        return true
    }

    const setCurrentRowFunction = (record: API.SubjectsVO) =>
    {
        console.log("setCurrentRowFunction: ", record)
        setCurrentRow({
            id: record.id,
            name: record.name,
            gradeMin: record.gradeMin,
            gradeMax: record.gradeMax,
        })
        setGradeMin(record.gradeMin);
        setGradeMax(record.gradeMax);
    }

    const ModalFormItemComponent = ({ initialValues }: { initialValues?: Subject.CurrentRow }) => {
        // const [form] = Form.useForm();
        useEffect(() => {
            form.setFieldsValue(initialValues); // 在组件加载后设置初始值
        }, [initialValues, form]);

        return (
            <Form form={form}>
                <Form.Item name={"name"} label={"科目名称"}>
                    <Input />
                </Form.Item>
                <Form.Item name="gradeMin" label="科目分数最小值">
                    <InputNumber
                        min={0}
                        max={initialValues?.gradeMax}
                        onChange={(value) => setGradeMin(value)}
                    />
                </Form.Item>
                <Form.Item name="gradeMax" label="科目分数最大值">
                    <InputNumber
                        min={initialValues?.gradeMin}
                        max={Number.MAX_VALUE}
                        defaultValue={initialValues?.gradeMax}
                        onChange={(value) => setGradeMax(value)}
                    />
                </Form.Item>
            </Form>
        );
    }


    return <>
        <PageContainer title={"科目管理"}>
            <ProTable
                columns={SubjectColumns({
                    handleDeleteFunction: handleDelete,
                    setCurrentRow: setCurrentRowFunction,
                    setUpdateModalVisible
                })}
                actionRef={actionRef}
                rowKey="id"
                toolBarRender={() => [
                    <Button
                        type="primary"
                        key="primary"
                        onClick={() => {
                            setCreateModalVisible(true);
                        }}
                    >
                        <PlusOutlined /> 新建
                    </Button>,
                ]}
                request={async (params, sort, filter) => {
                    const sortField = Object.keys(sort)?.[0];
                    const sortOrder = sort?.[sortField] ?? undefined;

                    const { data, code } = await listSubjectsByPageUsingPOST({
                        ...params,
                        sortField,
                        sortOrder,
                        ...filter,
                    } as API.SubjectsQueryRequest);

                    return {
                        success: code === 0,
                        data: data?.records || [],
                        total: Number(data?.total) || 0,
                    };
                }}
            />

            <Modal title={"新建"}
                   open={createModalVisible}
                   onOk={async () =>
                   {
                        const {name} = form.getFieldsValue();
                        if (name)
                        {
                            if (!await checkScoreIsValid())
                            {
                                return;
                            }
                            try {
                                const {data, code} = await addSubjectsUsingPOST({
                                    name,
                                    gradeMin,
                                    gradeMax
                                })
                                if (code === 0)
                                {
                                    message.success("添加成功")

                                }
                            }
                            catch (e: any)
                            {
                                message.error(e.message)
                            }
                            finally
                            {
                                setCreateModalVisible(false)
                                form.resetFields()
                                actionRef.current?.reloadAndRest?.()
                            }
                        }
                        else
                        {
                            message.error("科目名称不能为空")
                        }
                   }}
                   onCancel={() =>
                   {
                       setCreateModalVisible(false)
                       form.resetFields()
                   }}
            >
                <ModalFormItemComponent key={"add"}/>
            </Modal>

            <Modal title={"更新"}
                   open={updateModalVisible}
                   onOk={async () =>
                   {
                       const {name} = form.getFieldsValue();
                       if (name)
                       {
                           try {
                               if (!await checkScoreIsValid())
                               {
                                   return;
                               }
                               const {data, code} = await updateSubjectsUsingPOST({
                                   id: currentRow.id,
                                   name,
                                   gradeMin,
                                   gradeMax})
                               if (code === 0)
                               {
                                   message.success("添加成功")
                               }
                           }
                           catch (e: any)
                           {
                               message.error(e.message)
                           }
                           finally
                           {
                               setUpdateModalVisible(false)
                               form.resetFields()
                               actionRef.current?.reloadAndRest?.()
                           }
                       }
                       else
                       {
                           message.error("科目名称不能为空")
                       }
                   }}
                   onCancel={() =>
                   {
                       setUpdateModalVisible(false);
                       form.resetFields();
                   }}
            >
                <ModalFormItemComponent key={"modify"} initialValues={currentRow}/>
            </Modal>

        </PageContainer>
    </>
}

export default Index;
