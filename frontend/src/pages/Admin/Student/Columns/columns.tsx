import {ProColumns} from "@ant-design/pro-components";
import {Space, Typography} from "antd";
import React from "react";

export const SubjectColumns = ({
    setCurrentRow,
    setUpdateModalVisible,
    handleDeleteFunction
}: ColumnsFunctionProps): ProColumns[] => [
    {
        title: '科目id',
        width: 120,
        dataIndex: 'id',
        render: (_) => <a>{_}</a>,
    },
    {
        title: '科目名称',
        width: 120,
        dataIndex: 'name',
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

                <Typography.Link
                    onClick={() =>
                    {
                        setCurrentRow(record);
                        setUpdateModalVisible(true);
                    }}
                >
                    编辑成绩
                </Typography.Link>

                <Typography.Link type="danger" onClick={() => handleDeleteFunction(record)}>
                    删除
                </Typography.Link>
            </Space>
        ),
    }
];
