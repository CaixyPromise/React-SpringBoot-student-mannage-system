import {ProColumns} from "@ant-design/pro-components";
import React from "react";
import {Space, Typography} from "antd";
import {DepartmentAndMajor} from "@/constants/departmentAndMajor";
import {ColumnsFunctionProps} from "@/typings";

export const DepartmentColumns = ({
  setCurrentRow,
  setUpdateModalVisible,
  handleDeleteFunction
}: ColumnsFunctionProps): ProColumns[] => [
  {
    title: '学院id',
    width: 120,
    dataIndex: 'departmentId',
    render: (_) => <a>{_}</a>,
  },
  {
    title: '学院名称',
    width: 120,
    dataIndex: 'departmentName',
  },
  {
    title: '操作',
    dataIndex: 'option',
    valueType: 'option',
    width: 100,
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
        <Typography.Link type="danger" onClick={() => handleDeleteFunction(record)}>
          删除
        </Typography.Link>
      </Space>
    ),
  }
];

export const subMajorColumns = ({
                                  setCurrentRow,
                                  setUpdateModalVisible,
                                  handleDeleteFunction
                                }: ColumnsFunctionProps): ProColumns[] => [
  {
    title: "专业id",
    dataIndex: "majorId",
    width: 120,
  },
  {
    title: "专业名称",
    dataIndex: "majorName",
    width: 120,
  },

  {
    title: '操作',
    dataIndex: 'option',
    valueType: 'option',
    width: 200,
    render: (_, record) => (
      <Space size="middle">
        <Typography.Link
          onClick={() => {
            setCurrentRow(record);
            setUpdateModalVisible(record);
          }}
        >
          修改
        </Typography.Link>
        <Typography.Link type="danger" onClick={() => handleDeleteFunction(record)}>
          删除
        </Typography.Link>
      </Space>
    ),
  },
]

export const subClassesColumns = ({
                                    setCurrentRow,
                                    setUpdateModalVisible,
                                    handleDeleteFunction
                                  }: ColumnsFunctionProps): ProColumns[] => [
  {
    title: "班级id",
    dataIndex: "id",
    width: 120,
  },
  {
    title: "班级名称",
    dataIndex: "name",
    width: 120,
  },
  {
    title: "专业名称",
    dataIndex: "majorName",
    width: 120,
  },
  {
    title: "学院名称",
    dataIndex: "departName",
    width: 120,
  },
  {
    title: '操作',
    dataIndex: 'option',
    valueType: 'option',
    width: 200,
    render: (_, record) => (
      <Space size="middle">
        <Typography.Link
          onClick={() => {
            setCurrentRow(record);
            setUpdateModalVisible(record);
          }}
        >
          修改
        </Typography.Link>
        <Typography.Link type="danger" onClick={() => handleDeleteFunction(record)}>
          删除
        </Typography.Link>
      </Space>
    ),
  },
]


export const createDepartmentColumns: ProColumns[] = [
  {
    title: "学院名称",
    dataIndex: "name",
    valueType: "text"
  }
]

export const updateMajorColumns = ({
                                     departmentInfo,
                                     setCurrentSelect
                                   }: {
  departmentInfo: DepartmentAndMajor[];
  setCurrentSelect: (value: any) => void;
}): ProColumns[] => [
  {
    title: "学院名称",
    dataIndex: "departmentId",
    valueType: "select",
    valueEnum: departmentInfo.reduce((acc, current) => {
      acc[current.departmentId] = {
        text: current.departmentName,
      };
      return acc;
    }, {}),
    // 当下拉框的选项变化时，可以通过 form 实例设置其他字段的值
    fieldProps: {
      onChange: (value: any) => {
        setCurrentSelect(value);
      },
    },
  },
  {
    title: "专业名称",
    dataIndex: "majorName",
    valueType: "text"
  }
]
