import {ProColumns} from "@ant-design/pro-components";
import {Space, Typography} from "antd";
import React from "react";
import {userSexOption} from "@/pages/Admin/Student/Columns/option";

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
        title: '学生姓名',
        width: 120,
        dataIndex: 'stuName',
    },
    {
        title: '学生性别',
        width: 120,
        dataIndex: 'stuSex',
        valueType: "select",
        valueEnum: {
            1: {text: "男"},
            2: {text: "女"}
        }
    },
    {
        title: '所属学院',
        width: 120,
        dataIndex: 'stuDepart',
    },
    {
        title: '所属专业',
        width: 120,
        dataIndex: 'stuMajor',
    },
    {
        title: '所属班级',
        width: 120,
        dataIndex: 'stuClass',
    },
    // {
    //     title: '创建人id',
    //     width: 120,
    //     dataIndex: 'creatorId',
    // },
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
                        setCurrentRow({
                            id: record.id,
                            subjectName: [record.stuDeptId, record.stuMajorId, record.stuClassId ],
                            stuName: record.stuName,
                            stuSex: record.stuSex,
                        });
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
