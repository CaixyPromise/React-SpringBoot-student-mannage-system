import {ProColumns} from "@ant-design/pro-components";
import {Badge, Space, Typography} from "antd";
import React from "react";
import {UserOutline} from "antd-mobile-icons";
import StudentSex from "@/pages/Admin/Student/components/StudentSex";
import {ProCoreActionType} from "@ant-design/pro-utils/es/typing";

export const StudentColumns = ({
    setCurrentRow,
    setUpdateModalVisible,
    handleDeleteFunction,
    setScoreModalVisible,
    setAddScoreModalVisible
}: {
    setCurrentRow: (currentRow: any) => void
    setUpdateModalVisible:  React.Dispatch<React.SetStateAction<any>>
    handleDeleteFunction: (record: any) => void,
    setScoreModalVisible: (visible: boolean) => void,
    setAddScoreModalVisible: (visible: boolean) => void
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
            <Space size="middle" direction="horizontal" wrap={true}>

                <Typography.Link onClick={() => {
                    setCurrentRow(record);
                    setScoreModalVisible(true)
                }}>
                    查看成绩
                </Typography.Link>


                <Typography.Link
                    onClick={() =>
                    {
                        setCurrentRow(record);
                        setAddScoreModalVisible(true);
                    }}
                >
                    添加成绩
                </Typography.Link>
                <Typography.Link
                    onClick={() =>
                    {
                        setCurrentRow(record);
                        setUpdateModalVisible(true);
                    }}
                >
                    修改信息
                </Typography.Link>
                <Typography.Link type="danger" onClick={() => handleDeleteFunction(record)}>
                    删除学生
                </Typography.Link>
            </Space>
        ),
    }
];


export const ScoreColumn = (handleSave, handleDeleteScore):ProColumns[] => ([
        {
            title: "成绩id",
            dataIndex: "gradeId",
            editable: () => false,
            valueType: "text",
        },
        {
            title: "科目名称",
            dataIndex: "subjectName",
            valueType: "text",
            editable: () => false
        },
        {
            title: "成绩",
            dataIndex: "grade",
            valueType: "digit",
            hideInSearch: true,
            editable: () => true,  // 设置成绩列为可编辑
            onCell: (record, rowIndex) => ({
                record,
                editable: true,
                dataIndex: 'grade',
                title: "成绩",
                handleSave: handleSave,
            }),
        },
        {
            title: "操作",
            dataIndex: 'option',
            valueType: 'option',
            render: (_, record,index: number, action: ProCoreActionType | undefined) => (
                <Space size="middle" direction="horizontal" wrap={true}>
                    <Typography.Link onClick={() => action?.startEditable(record.gradeId)}>
                        修改
                    </Typography.Link>
                    <Typography.Link type={"danger"} onClick={() => handleDeleteScore(record.gradeId, record)}>
                        删除
                    </Typography.Link>
                </Space>
            ),
        },
    ]
)
