import {type ActionType, PageContainer, ProTable} from "@ant-design/pro-components";
import {SubjectColumns} from "@/pages/Admin/Subject/Columns/columns";
import React, {useRef, useState} from "react";
import {deleteDepartmentInfoUsingPOST} from "@/services/backend/departmentController";
import {Button, Form, Input, message, Modal} from "antd";
import {listUserByPageUsingPOST} from "@/services/backend/userController";
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
    const [ currentRow, setCurrentRow ] = useState<API.Subjects>();

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

    return <>
        <PageContainer title={"科目管理"}>
            <ProTable
                columns={SubjectColumns({ setCurrentRow, setUpdateModalVisible, handleDeleteFunction: handleDelete })}
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
                            try {
                                const {data, code} = await addSubjectsUsingPOST({name})
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
                <Form form={form}>
                    <Form.Item name={"name"} label={"科目名称"}>
                        <Input />
                    </Form.Item>
                </Form>
            </Modal>

            <Modal title={"更新"}
                   open={updateModalVisible}
                   onOk={async () =>
                   {
                       const {name} = form.getFieldsValue();
                       if (name)
                       {
                           try {
                               const {data, code} = await updateSubjectsUsingPOST({id: currentRow.id, name})
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
                <Form form={form} initialValues={{
                    name: currentRow?.name
                }}>
                    <Form.Item name={"name"} label={"科目名称"}>
                        <Input />
                    </Form.Item>
                </Form>
            </Modal>

        </PageContainer>
    </>
}

export default Index;
