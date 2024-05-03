import {addStudentInfoUsingPOST} from "@/services/backend/studentController";
import {Cascader, Form, Input, message, Modal, Select} from "antd";
import {userSexOption} from "@/pages/Admin/Student/Columns/option";
import React from "react";
import {ActionType} from "@ant-design/pro-components";
import {PayloadBody} from "@/pages/Admin/Student/typing";

interface CreateProps
{
    createModalVisible: boolean
    setCreateModalVisible: (value: boolean) => void
    cascadeOption: API.AllClassesOptionDataVO[]
    actionRef: React.MutableRefObject<ActionType | undefined>,
    packageRequestBody: () => PayloadBody | null
}


const Index: React.FC<CreateProps> = (payload : CreateProps ) =>
{
    const [form] = Form.useForm();
    const {createModalVisible, setCreateModalVisible, cascadeOption, packageRequestBody, actionRef} = payload

    return <>
        <Modal title={"新建"}
               open={createModalVisible}
               onOk={async () =>
               {
                   // console.log(form.getFieldsValue())
                   const requestBody = packageRequestBody();
                   if (requestBody !== null)
                   {
                       try
                       {
                           const { data, code } = await addStudentInfoUsingPOST(requestBody)
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
                       message.error("学生信息名称不能为空或未选择所属学院")
                   }
               }}
               onCancel={() =>
               {
                   setCreateModalVisible(false)
                   form.resetFields()
               }}
        >
            <Form form={form}>
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
