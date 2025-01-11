import {addStudentInfoUsingPost1} from "@/services/backend/studentController";
import {Cascader, Form, Input, message, Modal, Select} from "antd";
import {userSexOption} from "@/pages/Admin/Student/Columns/option";
import React from "react";
import {ActionType} from "@ant-design/pro-components";

interface CreateProps {
  createModalVisible: boolean
  setCreateModalVisible: (value: boolean) => void
  cascadeOption: API.AllClassesOptionDataVO[]
  actionRef: React.MutableRefObject<ActionType | undefined>,
  packageRequestBody: () => Student.PayloadBody | null
}


const Index: React.FC<CreateProps> = (payload: CreateProps) => {
  const [form] = Form.useForm();
  const {createModalVisible, setCreateModalVisible, cascadeOption, actionRef} = payload
  const packageRequestBody: () => (null | {
    stuSex: any;
    stuId: string;
    stuDeptId: any;
    stuMajorId: any;
    stuName: any;
    stuClassId: any
  }) = () => {
    const formValue = form.getFieldsValue()
    const [departId, majorId, classId] = formValue.subjectName
    const {stuName, stuId} = formValue;
    if (stuName === "" || stuId === "" || stuId.length !== 11) {
      return null
    }
    if (departId === undefined || majorId === undefined || classId === undefined) {
      return null
    }
    return {
      stuClassId: classId,
      stuDeptId: departId,
      stuMajorId: majorId,
      ...formValue
    }
  }
  return <>
    <Modal title={"添加学生信息"}
           open={createModalVisible}
           onOk={async () => {
             // console.log(form.getFieldsValue())
             const requestBody = packageRequestBody();

             if (requestBody !== null) {
               try {
                 const {data, code} = await addStudentInfoUsingPost1(requestBody)
                 if (code === 0) {
                   message.success("添加成功")

                 }
               } catch (e: any) {
                 message.error(e.message)
               } finally {
                 setCreateModalVisible(false)
                 form.resetFields()
                 actionRef.current?.reloadAndRest?.()
               }
             } else {
               console.log(form.getFieldsValue())
               message.error("学生信息名称不能为空或未选择所属学院")
             }
           }}
           onCancel={() => {
             setCreateModalVisible(false)
             form.resetFields()
           }}
    >
      <Form form={form}>
        <Form.Item name="stuId" label="学生学号" required>
          <Input placeholder="请输入学生学号"/>
        </Form.Item>
        <Form.Item name={"stuName"} label={"学生姓名"} required>
          <Input placeholder="请输入学生姓名"/>
        </Form.Item>
        <Form.Item name={"subjectName"} label={"所属学院"} required>
          <Cascader options={cascadeOption}/>
        </Form.Item>
        <Form.Item name={"stuSex"} label={"学生性别"} required>
          <Select placeholder="请输入学生性别" options={userSexOption}/>
        </Form.Item>
      </Form>
    </Modal>
  </>
}


export default Index;
