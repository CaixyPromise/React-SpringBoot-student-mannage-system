import CreateModal from "@/components/CreateAndUpdateModal/components/CreateModal";
import UpdateModal from '@/components/CreateAndUpdateModal/components/UpdateModal';
import {PlusOutlined} from '@ant-design/icons';
import type {ActionType} from '@ant-design/pro-components';
import {PageContainer, ProTable} from '@ant-design/pro-components';
import '@umijs/max';
import {Button, Empty, MenuProps, message} from 'antd';
import React, {useRef, useState} from 'react';
import {
    createDepartmentColumns,
    DepartmentColumns,
    subMajorColumns,
    updateMajorColumns
} from "@/pages/Admin/DepartmentAndMajorList/columns/DepartmentAndMajorColumns";
import {
    addMajorInfoUsingPOST,
    deleteMajorInfoUsingPOST, downloadTemplateUsingGET,
    listMajorInfoByPageUsingPOST,
    updateMajorInfoUsingPOST
} from "@/services/backend/majorInfoController";
import {
    addDepartmentInfoUsingPOST,
    deleteDepartmentInfoUsingPOST,
    updateDepartmentInfoUsingPOST
} from "@/services/backend/departmentController";
import Dropdown from "antd/es/dropdown/dropdown";

interface DataSourceMajor
{
    majorId: string;
    majorName: string;
    operations: string; // 根据实际操作调整类型
}

interface DataSourceDepartment
{
    departmentId: string;
    departmentName: string;
    majors: DataSourceMajor[];
    key: string; // key通常用于React渲染列表的唯一标识
}

/**
 * 用户管理页面
 *
 * @constructor
 */
const DepartmentAndMajorListPage: React.FC = () =>
{
    // 学院模态框状态
    const [ createDeptModalVisible, setCreateDeptModalVisible ] = useState<boolean>(false);
    const [ updateDeptModalVisible, setUpdateDeptModalVisible ] = useState<boolean>(false);

    // 专业模态框状态
    const [ createMajorModalVisible, setCreateMajorModalVisible ] = useState<boolean>(false);
    const [ updateMajorModalVisible, setUpdateMajorModalVisible ] = useState<boolean>(false);

    // 当前选中的专业
    const [ majorCurrentRow, setMajorCurrentRow ] = useState<{
        majorId: string;
        majorName: string;
        departmentId: string;
    }>({ majorId: "", majorName: "", departmentId: "" });

    const actionRef = useRef<ActionType>();
    // 当前用户点击的数据
    const [ currentRow, setCurrentRow ] = useState<API.User>();
    const majorRef = useRef<ActionType>();
    const [ dataSource, setDataSource ] = useState<DataSourceDepartment[]>([])
    const [ currentUpdateDepartment, setCurrentUpdateDepartment ] = useState()
    const processData = (data) =>
    {
        const groupedByDepartment = {};
        data.records.forEach(record =>
        {
            if (!groupedByDepartment[record.departmentId])
            {
                groupedByDepartment[record.departmentId] = {
                    departmentId: record.departmentId,
                    departmentName: record.departmentName,
                    majors: [],
                };
            }
            // 只有当专业ID和专业名称不为null时，才添加专业信息
            if (record.majorId && record.majorName)
            {
                groupedByDepartment[record.departmentId].majors.push({
                    majorId: record.majorId,
                    majorName: record.majorName,
                    operations: '操作', // 根据需要定义操作
                });
            }
        });
        const result: DataSourceDepartment[] = Object.values(groupedByDepartment).map(dept => ({
            ...dept,
            key: dept.departmentId
        }));
        console.log("result is: ", result)
        setDataSource(result);
        return result;
    };
    const majorExpandedRowRender = (record: any) =>
    {
        const handleDeleteMajor = async (record: { majorId: API.DeleteRequest; }) =>
        {
            const { data, code } = await deleteMajorInfoUsingPOST({ id: record.majorId })
            if (code === 0 && data)
            {
                message.success('删除成功，即将刷新')
                actionRef.current?.reloadAndRest?.()
            }
        }
        const handleUpdateClick = (record: DataSourceMajor) =>
        {
            // 找到当前专业所属的学院ID
            const departmentId = dataSource.find(
                department => department.majors.some(major => major.majorId === record.majorId))?.departmentId || '';

            // 设置当前操作的专业行及其学院ID
            setMajorCurrentRow({
                majorId: record.majorId,
                majorName: record.majorName,
                departmentId: departmentId
            });

            // 显示更新专业的模态框
            setUpdateMajorModalVisible(true);
        };

        const isEmpty = record.majors.length === 0;

        return <ProTable
            columns={subMajorColumns({
                setCurrentRow: setMajorCurrentRow,
                handleDeleteFunction: handleDeleteMajor,
                setUpdateModalVisible: handleUpdateClick,
            })}
            headerTitle={false}
            search={false}
            actionRef={majorRef}
            options={false}
            dataSource={record.majors}
            pagination={false}
            rowKey={"majorId"}
            tableRender={(_, dom) => (
                isEmpty ? <Empty
                    description={<p>此学院尚未设立专业 <a
                        onClick={() => setCreateMajorModalVisible(true)}>现在去创建</a></p>}

                /> : dom
            )}
        />
    }


    const classeseExpandedRowRender = (record: any) =>
    {
        const handleDeleteMajor = async (record: { majorId: API.DeleteRequest; }) =>
        {
            const { data, code } = await deleteMajorInfoUsingPOST({ id: record.majorId })
            if (code === 0 && data)
            {
                message.success('删除成功，即将刷新')
                actionRef.current?.reloadAndRest?.()
            }
        }
        const handleUpdateClick = (record: DataSourceMajor) =>
        {
            // 找到当前专业所属的学院ID
            const departmentId = dataSource.find(
                department => department.majors.some(major => major.majorId === record.majorId))?.departmentId || '';

            // 设置当前操作的专业行及其学院ID
            setMajorCurrentRow({
                majorId: record.majorId,
                majorName: record.majorName,
                departmentId: departmentId
            });

            // 显示更新专业的模态框
            setUpdateMajorModalVisible(true);
        };

        const isEmpty = record.majors.length === 0;

        return <ProTable
            columns={subMajorColumns({
                setCurrentRow: setMajorCurrentRow,
                handleDeleteFunction: handleDeleteMajor,
                setUpdateModalVisible: handleUpdateClick,
            })}
            headerTitle={false}
            search={false}
            actionRef={majorRef}
            options={false}
            dataSource={record.majors}
            pagination={false}
            rowKey={"majorId"}
            tableRender={(_, dom) => (
                isEmpty ? <Empty
                    description={<p>此学院尚未设立专业 <a
                        onClick={() => setCreateMajorModalVisible(true)}>现在去创建</a></p>}

                /> : dom
            )}
        />
    }
    /**
     * 删除节点
     *
     * @param row
     */
    const handleDeleteDepartment = async (record) =>
    {
        const { data, code } = await deleteDepartmentInfoUsingPOST({
            id: record.departmentId
        })
        if (code === 0 && data)
        {
            message.success('删除成功，即将刷新')
            actionRef.current?.reloadAndRest?.()
        }
    }

    const handleAddDepartment = async (record) =>
    {
        try
        {
            const { data, code } = await addDepartmentInfoUsingPOST({
                name: record.name
            })
            if (code === 0)
            {
                message.success('添加成功，即将刷新')
            }
            else
            {
                message.error('添加失败')
            }
            return { data, code }
        }
        catch (e: any)
        {
            return {
                code: -1,
                data: ""
            }
        }
    }

    const handleUpdateDepartment = async (record) =>
    {
        try
        {
            console.log("update record is: ", record)
            const { data, code } = await updateDepartmentInfoUsingPOST({
                name: record.name,
                id: record.departmentId
            })
            if (code === 0)
            {
                message.success('修改成功，即将刷新')
            }
            else
            {
                message.error('修改失败')
            }
            return { data, code }
        }
        catch (e: any)
        {
            return {
                code: -1,
                data: ""
            }
        }
    }

    const handleAddMajor = async (record) =>
    {
        try
        {
            console.log("handleAddMajor record is: ", record)
            const { data, code } = await addMajorInfoUsingPOST({
                departmentId: record.departmentId,
                name: record.majorName
            })
            if (code === 0)
            {
                message.success('添加成功，即将刷新')
            }
            else
            {
                message.error('添加失败')
            }
            return { data, code }
        }
        catch (e: any)
        {
            return {
                code: -1,
                data: ""
            }
        }
    }

    const handleUpdateMajor = async (record) =>
    {
        try
        {
            const { data, code } = await updateMajorInfoUsingPOST({
                name: record.majorName,
                id: record.majorId,
                departId: record.departmentId
            })
            if (code === 0)
            {
                message.success('修改成功，即将刷新')
            }
            else
            {
                message.error('修改失败')
            }
            return { data, code }
        }
        catch (e: any)
        {
            return {
                code: -1,
                data: ""
            }
        }
    }

    const items: MenuProps = [
        {
            key: 'download-template',
            label: '下载导入模板',
            onClick: async () =>
            {
                const response = await fetch('/api/major/download/template/major');
                if (!response.ok) {
                    message.error('下载失败')
                }
                const blob = await response.blob();
                const downloadUrl = window.URL.createObjectURL(blob);
                const a = document.createElement('a');
                a.style.display = 'none';
                a.href = downloadUrl;
                // 从响应头部获取文件名
                a.download = 'template.xlsx';
                document.body.appendChild(a);
                a.click();
                window.URL.revokeObjectURL(downloadUrl);
                a.remove();
            }
        },
        {
            key: 'import-data',
            label: '批量导入专业',
            onClick: () =>
            {
                console.log("2nd item clicked")
            }
        },
    ];

    return (
        <PageContainer>
            <ProTable<API.DepartmentWithMajorsVO>
                headerTitle={'学院与专业信息管理'}
                actionRef={actionRef}
                rowKey="key"
                search={{
                    labelWidth: 120,
                }}
                toolBarRender={() => [
                    <Button
                        type="primary"
                        key="department"
                        onClick={() =>
                        {
                            setCreateDeptModalVisible(true);
                        }}
                    >
                        <PlusOutlined/> 新建学院
                    </Button>,
                    <Dropdown.Button
                        icon={<PlusOutlined/>}
                        type="primary"
                        menu={{
                            items: items
                        }}
                        onClick={() =>
                        {
                            setCreateMajorModalVisible(true)
                        }}
                        placement="bottomLeft"
                    >
                        新建专业
                    </Dropdown.Button>,
                    <Button
                        新建班级
                    ></Button>
                ]}
                request={async (params, sort, filter) =>
                {
                    const sortField = Object.keys(sort)?.[0];
                    const sortOrder = sort?.[sortField] ?? undefined;

                    const { data, code } = await listMajorInfoByPageUsingPOST({
                        ...params,
                        sortField,
                        sortOrder,
                        ...filter,
                    } as API.UserQueryRequest);
                    return {
                        success: code === 0,
                        data: processData(data) || [],
                        total: Number(data?.total) || 0,
                    };
                }}
                columns={DepartmentColumns(
                    {
                        setCurrentRow,
                        setUpdateModalVisible: () =>
                        {
                            setUpdateDeptModalVisible(prevState => !prevState)
                        },
                        handleDeleteFunction: handleDeleteDepartment
                    })}
                expandable={{ expandedRowRender: majorExpandedRowRender }}
            />
            {/*学院更新/创建操作*/}
            <CreateModal
                visible={createDeptModalVisible}
                columns={createDepartmentColumns}
                onSubmit={async (value) =>
                {
                    setCreateDeptModalVisible(false);
                    const response = await handleAddDepartment(value)
                    actionRef.current?.reload();
                    return response;
                }}
                onCancel={() =>
                {
                    setCreateDeptModalVisible(false);
                }}
            />
            <UpdateModal
                visible={updateDeptModalVisible}
                columns={createDepartmentColumns}
                defaultValue={currentRow}
                onSubmit={async (value, id) =>
                {
                    setUpdateDeptModalVisible(false);
                    const response = await handleUpdateDepartment(value)
                    setCurrentRow(undefined);
                    actionRef.current?.reload();
                    return response;
                }}
                onCancel={() =>
                {
                    setUpdateDeptModalVisible(false);
                }}
            />
            {/*专业更新/创建操作*/}
            <CreateModal
                visible={createMajorModalVisible}
                columns={updateMajorColumns({
                    departmentInfo: dataSource,
                    setCurrentSelect: setCurrentUpdateDepartment
                })}
                onSubmit={async (value, id) =>
                {
                    setCreateMajorModalVisible(false);
                    const response = await handleAddMajor(value)
                    actionRef.current?.reload();
                    return response;
                }}
                onCancel={() =>
                {
                    setCreateMajorModalVisible(false);
                }}
            />
            <UpdateModal
                visible={updateMajorModalVisible}
                columns={updateMajorColumns({
                    departmentInfo: dataSource,
                    setCurrentSelect: setCurrentUpdateDepartment
                })}
                defaultValue={{
                    majorId: majorCurrentRow?.majorId,
                    majorName: majorCurrentRow?.majorName,
                    departmentId: majorCurrentRow?.departmentId
                }}
                onSubmit={async (value, id) =>
                {
                    setUpdateMajorModalVisible(false);
                    await handleUpdateMajor(value);
                    setCurrentRow(undefined);
                    setMajorCurrentRow({
                        majorId: "",
                        majorName: "",
                        departmentId: ""
                    });
                    actionRef.current?.reload();
                    return {
                        code: -1,
                        data: ""
                    }
                }}
                onCancel={() =>
                {
                    setUpdateMajorModalVisible(false);
                }}
            />
        </PageContainer>
    );
};
export default DepartmentAndMajorListPage;
