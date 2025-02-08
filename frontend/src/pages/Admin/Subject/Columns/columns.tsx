import {ProColumns} from "@ant-design/pro-components";
import {Badge, InputNumber, Space, Typography} from "antd";
import React from "react";
import {userSexOption} from "@/pages/Admin/Student/Columns/option";
import {record} from "@umijs/utils/compiled/zod";

export const SubjectColumns = ({
 setCurrentRow,
 setUpdateModalVisible,
 handleDeleteFunction,
 setPostSelectModal
}: ColumnsFunctionProps & {
  setPostSelectModal: React.Dispatch<React.SetStateAction<any>>
}): ProColumns[] => [
  {
    title: 'id',
    width: 120,
    dataIndex: 'id',
    render: (item) => <a>{item}</a>,
    align: "center",
  },
  {
    title: "科目名称",
    width: 120,
    dataIndex: "name",
    valueType: "text",
    align: "center",
  },
  {
    title: "课程学分",
    width: 120,
    dataIndex: "gradeCredit",
    valueType: "digit",
    align: "center",
    render: (item) => <span>{item} 学分</span>,
    hideInSearch: true,
  },
  {
    title: "课程类型",
    dataIndex: "courseType",
    valueType: "select",
    align: "center",
    width: 100,
    valueEnum: {
      0: {
        text: "必修",
      },
      1: {
        text: "选修",
      }
    },
    hideInSearch: true,
  },
  {
    title: "科目最高分阈值",
    width: 120,
    dataIndex: "gradeMax",
    valueType: "digit",
    hideInSearch: true,
    render: (_, record) => (
      <><Badge status="processing" text={`${record.gradeMax} 分`}/> </>
    )
  },

  {
    title: "科目最低分阈值",
    width: 120,
    hideInSearch: true,
    dataIndex: "gradeMin",
    valueType: "digit",
    render: (_, record) => (
      <><Badge status="warning" text={`${record.gradeMin} 分`}/> </>
    )
  },

  {
    title: "优秀分数线",
    width: 120,
    hideInSearch: true,
    dataIndex: "gradeExcellent",
    valueType: "digit",
    render: (_, record) => (
      <><Badge status="success" text={`${record.gradeExcellent} 分`}/> </>
    )
  },
  {
    title: "不及格分数线",
    width: 120,
    hideInSearch: true,
    dataIndex: "gradeFail",
    valueType: "digit",
    render: (_, record) => (
      <><Badge status="error" text={`${record.gradeFail} 分`}/> </>
    )
  },
  {
    title: "课程学时",
    width: 120,
    hideInSearch: true,
    dataIndex: "creditHours",
    valueType: "digit",
  },
  {
    title: '创建人id',
    width: 120,
    hideInSearch: true,
    dataIndex: 'creatorId',
  },
  {
    title: '操作',
    dataIndex: 'option',
    valueType: 'option',
    width: 120,
    align: "center",
    render: (_, record) => (
      <Space size="middle">
        <Typography.Link
          onClick={() => {
            setCurrentRow(record);
            setUpdateModalVisible(true);
          }}
        >
          修改
        </Typography.Link>
      </Space>
    ),
  }
];
