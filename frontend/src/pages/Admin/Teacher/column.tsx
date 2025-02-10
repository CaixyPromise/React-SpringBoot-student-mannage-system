import {ActionType, ProColumns} from "@ant-design/pro-components";
import StudentSex from "@/pages/Admin/Student/components/StudentSex";
import React from "react";
import {Button, message, Space} from "antd";
import {deleteTeacherInfoUsingPost1} from "@/services/backend/teacherInfoController";
import DepartmentSelect from "@/components/DepartmentSelect";

const handleDelete = async (actionRef: React.RefObject<ActionType>, record: API.TeacherInfoVO) => {
  if (record.id === undefined) {
    return;
  }
  try {
    const {code} = await deleteTeacherInfoUsingPost1({
      id: record.id
    });
    if (code === 0) {
      message.success(`删除成功: ${record.teacherName}`);
    }
  } finally {
    actionRef.current?.reload();
  }
}


export const TeacherColumn = (
  actionRef: React.RefObject<ActionType>,
  setCurrentRow: React.SetStateAction<boolean>,
  setModalVisible: React.SetStateAction<boolean>
): ProColumns[] => ([
  {
    title: '教师id',
    dataIndex: 'id',
    render: (_) => <a>{_}</a>,
  },
  {
    title: '教师工号',
    dataIndex: 'teacherId',
  },
  {
    title: '教师姓名',
    dataIndex: 'teacherName',
  },
  {
    title: '教师性别',
    dataIndex: 'teacherSex',
    valueEnum: {
      0: {text: '未知'},
      1: {text: '男'},
      2: {text: '女'},
    },
    render: (_, record) => (
      <StudentSex useSex={record.teacherSex}/>
    )
  },
  {
    title: '所属学院',
    dataIndex: 'teacherDepart',
    formItemProps: {
      label: '院系信息',
      name: 'teacherDepart'
    },
    renderFormItem: (_, {...rest}) => {
      return (
        <DepartmentSelect
          ignoreLevels={["classId"]}
          {...rest}
        />
      );
    },
  },
  {
    title: '教师专业',
    dataIndex: 'teacherMajor',
    hideInSearch: true,
  },
  {
    title: '操作',
    width: 200,
    hideInSearch: true,
    render: (_, record) => {
      return (
        <Space size="middle" direction="horizontal" wrap={true}>
          <Button
            type="link"
            onClick={() => {
              setCurrentRow(record);
              setModalVisible(true);
            }}
          >
            编辑
          </Button>
          <Button
            type="link"
            danger
            onClick={() => {
              handleDelete(actionRef, record)
            }}
          >
            删除
          </Button>
        </Space>
      )
    }
  }
])
