import {ProColumns} from "@ant-design/pro-components";
import {Badge, Space, Typography} from "antd";
import React from "react";
import {UserOutline} from "antd-mobile-icons";
import StudentSex from "@/pages/Admin/Student/utils";

export const StudentColumns = ({
    setCurrentRow,
    setUpdateModalVisible,
    handleDeleteFunction,
    setScoreModalVisible,
}: {
    setCurrentRow: (currentRow: any) => void
    setUpdateModalVisible:  React.Dispatch<React.SetStateAction<any>>
    handleDeleteFunction: (record: any) => void,
    setScoreModalVisible: (record: any) => void
}): ProColumns[] => [
    {
        title: 'id',
        width: 120,
        dataIndex: 'id',
        render: (_) => <a>{_}</a>,
    },
    {
        title: '学生姓名',
        width: 120,
        dataIndex: 'stuName',
    },
    {
        title: '学生性别',
        width: 120,
        dataIndex: 'stuSex',
        render: (_, record) => (
            <StudentSex useSex={record.stuSex} />
        )
    },
    {
        title: '所属学院',
        width: 120,
        dataIndex: 'stuDepart',
    },
    {
        title: '学生专业',
        width: 120,
        dataIndex: 'stuMajor',
    },
    {
        title: '学生班级',
        width: 120,
        dataIndex: 'stuClass',
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
                        setScoreModalVisible(record);
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
