import {ProColumns} from "@ant-design/pro-components";
import {Badge, InputNumber, Space, Typography} from "antd";
import React from "react";
import {userSexOption} from "@/pages/Admin/Student/Columns/option";
import {record} from "@umijs/utils/compiled/zod";

export const SubjectColumns = ({
    setCurrentRow,
    setUpdateModalVisible,
    handleDeleteFunction
}: ColumnsFunctionProps): ProColumns[] => [
    {
        title: 'id',
        width: 120,
        dataIndex: 'id',
        render: (item) => <a>{item}</a>,
    },
    {
        title: "科目名称",
        width: 120,
        dataIndex: "name",
        valueType: "text"
    },
    {
        title: "科目最大值",
        width: 120,
        dataIndex: "gradeMax",
        valueType: "digit",
        render: (_, record) => (
            <><Badge status="processing" text={`${record.gradeMax} 分`}/> </>
        )
    },
    {
        title: "科目最小值",
        width: 120,
        dataIndex: "gradeMin",
        valueType: "digit",
        render: (_, record) => (
            <><Badge status="warning" text={`${record.gradeMin} 分`}/> </>
        )
    },

    {
        title: "优秀分数线",
        width: 120,
        dataIndex: "gradeExcellent",
        valueType: "digit",
        render: (_, record) => (
            <><Badge status="success" text={`${record.gradeExcellent} 分`}/> </>
        )
    },
    {
        title: "不及格分数线",
        width: 120,
        dataIndex: "gradeFail",
        valueType: "digit",
        render: (_, record) => (
            <><Badge status="error" text={`${record.gradeFail} 分`}/> </>
        )
    },
    {
        title: '创建人id',
        width: 120,
        dataIndex: 'creatorId',
    },
    {
        title: '操作',
        dataIndex: 'option',
        valueType: 'option',
        width: 100,
        render: (_, record) => (
            <Space size="middle">
                <Typography.Link
                    onClick={() =>
                    {
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
