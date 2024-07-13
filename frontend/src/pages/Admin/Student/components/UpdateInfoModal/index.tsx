import {updateStudentInfoUsingPOST} from "@/services/backend/studentController";
import {Cascader, Form, Input, message, Modal, Select} from "antd";
import {userSexOption} from "@/pages/Admin/Student/Columns/option";
import React from "react";
import {ActionType} from "@ant-design/pro-components";


interface UpdateProps
{
    updateModalVisible: boolean
    setUpdateModalVisible: (value: boolean) => void
    cascadeOption: API.AllClassesOptionDataVO[]
    actionRef: React.MutableRefObject<ActionType | undefined>,
    currentRow: {
        packageRequestBody: () => Student.PayloadBody | null;
        id?: string | undefined;
        subjectName?: string[] | undefined;
        stuName?: string | undefined;
        stuSex?: number | undefined;
    },
    packageRequestBody: Student.PayloadBody | null
}


const Index: React.FC<UpdateProps> = (payload: UpdateProps) =>
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
                   const formValue = form.getFieldsValue();
                   const { subjectName, stuName, stuSex } = formValue;
                   console.log(subjectName, stuName, stuSex)
                   if (!(subjectName && stuName && stuSex))
                   {
                       message.error("请填写完整信息")
                       return
                   }
                   const [ departId, majorId, classId ] = formValue.subjectName

                   if (formValue.stuName === "" || departId === undefined || majorId === undefined || classId === undefined)
                   {
                       return null
                   }

                   try
                   {
                       const { data, code } = await updateStudentInfoUsingPOST({
                           // @ts-ignore
                           id: currentRow.id,
                           stuClassId: classId,
                           stuDeptId: departId,
                           stuMajorId: majorId,
                           stuName: formValue.stuName,
                           stuSex: formValue.stuSex,
                       })
                       if (code === 0)
                       {
                           message.success("修改成功")
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

               }
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
