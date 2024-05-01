import {updateStudentInfoUsingPOST} from "@/services/backend/studentController";
import {Cascader, Form, Input, message, Modal, Select} from "antd";
import {userSexOption} from "@/pages/Admin/Student/Columns/option";
import React from "react";
import {ActionType} from "@ant-design/pro-components";
import {PayloadBody} from "@/pages/Admin/Student/typing";


interface UpdateProps
{
    updateModalVisible: boolean
    setUpdateModalVisible: (value: boolean) => void
    cascadeOption: API.AllClassesOptionDataVO[]
    actionRef: React.MutableRefObject<ActionType | undefined>,
    currentRow: {
        packageRequestBody: () => PayloadBody | null;
        id?: string | undefined;
        subjectName?: string[] | undefined;
        stuName?: string | undefined;
        stuSex?: number | undefined;
    },
}


const Index: React.FC<UpdateProps> = ({ payload }: UpdateProps) =>
{
    const [ form ] = Form.useForm();
    const {
        updateModalVisible, setUpdateModalVisible, cascadeOption, currentRow, packageRequestBody, actionRef
    } = payload
    return <>
        <Modal title={"更新"}
               open={updateModalVisible}
               onOk={async () =>
               {
                   console.log(form.getFieldsValue())
                   const requestBody = packageRequestBody();
                   if (requestBody)
                   {
                       try
                       {
                           const { data, code } = await updateStudentInfoUsingPOST(
                               { id: currentRow.id, ...requestBody })
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
                ...currentRow
            }}>
                <Form.Item name={"subjectName"} label={"所属学院"} required>
                    <Cascader options={cascadeOption}/>
                </Form.Item>
                <Form.Item name={"stuName"} label={"学生姓名"} required>
                    <Input placeholder="请输入学生姓名"/>
                </Form.Item>
                <Form.Item name={"stuSex"} label={"学生性别"} required>
                    <Select placeholder="请输入学生性别" options={userSexOption}/>
                </Form.Item>
            </Form>
        </Modal>
    </>
}

export default Index;
