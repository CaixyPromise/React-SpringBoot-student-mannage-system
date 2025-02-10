import CreateModal from "@/components/CreateAndUpdateModal/components/CreateModal";
import UpdateModal from '@/components/CreateAndUpdateModal/components/UpdateModal';
import {PlusOutlined} from '@ant-design/icons';
import {ActionType, PageContainer, ProTable} from '@ant-design/pro-components';
import '@umijs/max';
import {Button, Cascader, Empty, Form, Input, InputNumber, MenuProps, message, Modal} from 'antd';
import React, {useMemo, useRef, useState} from 'react';
import {
  createDepartmentColumns,
  DepartmentColumns,
  subClassesColumns,
  subMajorColumns,
  updateMajorColumns
} from "@/pages/Admin/DepartmentAndMajorList/columns/DepartmentAndMajorColumns";
import {
  addMajorInfoUsingPost1,
  deleteMajorInfoUsingPost1,
  listMajorInfoByPageUsingPost1,
  updateMajorInfoUsingPost1
} from "@/services/backend/majorInfoController";
import {
  addDepartmentInfoUsingPost1,
  deleteDepartmentInfoUsingPost1,
  updateDepartmentInfoUsingPost1
} from "@/services/backend/departmentController";
import Dropdown from "antd/es/dropdown/dropdown";
import {
  addClassesInfoUsingPost1,
  listClassesInfoVoByPageUnderMajorUsingPost1,
  updateClassesInfoUsingPost1
} from "@/services/backend/classesController";
import {ProTableProps} from "@ant-design/pro-table/lib";

interface DataSourceMajor {
  majorId: string;
  majorName: string;
  operations: string; // 根据实际操作调整类型
}

interface DataSourceDepartment {
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
const DepartmentAndMajorListPage: React.FC = () => {
  // 学院模态框状态
  const [createDeptModalVisible, setCreateDeptModalVisible] = useState<boolean>(false);
  const [updateDeptModalVisible, setUpdateDeptModalVisible] = useState<boolean>(false);

  // 专业模态框状态
  const [createMajorModalVisible, setCreateMajorModalVisible] = useState<boolean>(false);
  const [updateMajorModalVisible, setUpdateMajorModalVisible] = useState<boolean>(false);

  // 班级模态框状态
  const [createClassesModalVisible, setCreateClassesModalVisible] = useState<boolean>(false);
  const [updateClassesModalVisible, setUpdateClassesModalVisible] = useState<boolean>(false);
  // 当前选中的专业
  const [majorCurrentRow, setMajorCurrentRow] = useState<{
    majorId: string;
    majorName: string;
    departmentId: string;
  }>({majorId: "", majorName: "", departmentId: ""});

  const [classesCurrentRow, setClassesCurrentRow] = useState<{
    classesId: string;
    classesName: string;
    departmentId: string;
    majorId: string;
  }>({classesId: "", classesName: "", departmentId: "", majorId: ""});

  const [classForm] = Form.useForm<{
    departmentMajor: string[];
    classesName: string;
  }>();
  const actionRef = useRef<ActionType>();
  // 当前用户点击的数据
  const [currentRow, setCurrentRow] = useState<API.User>();
  const majorRef = useRef<ActionType>();
  const classesRef = useRef<ActionType>();

  const [dataSource, setDataSource] = useState<DataSourceDepartment[]>([])
  const [currentUpdateDepartment, setCurrentUpdateDepartment] = useState()

  const departmentsMajorsMap = useMemo(() => {
    const map = new Map();
    dataSource.forEach(department => {
      department.majors.forEach(major => {
        const key = `${department.departmentName}-${major.majorName}`;
        map.set(key, {
          departmentId: department.departmentId,
          majorId: major.majorId
        });
      });
    });
    return map;
  }, [dataSource]);

  const processData = (data) => {
    const groupedByDepartment = {};
    data.records.forEach(record => {
      if (!groupedByDepartment[record.departmentId]) {
        groupedByDepartment[record.departmentId] = {
          departmentId: record.departmentId,
          departmentName: record.departmentName,
          majors: [],
        };
      }
      // 只有当专业ID和专业名称不为null时，才添加专业信息
      if (record.majorId && record.majorName) {
        groupedByDepartment[record.departmentId].majors.push({
          majorId: record.majorId,
          majorName: record.majorName,
          departmentId: record.departmentId,
          operations: '操作', // 根据需要定义操作
        });
      }
    });
    const result: DataSourceDepartment[] = Object.values(groupedByDepartment).map(dept => ({
      ...dept,
      key: dept.departmentId
    }));
    setDataSource(result);
    console.log("dataSource: ", dataSource)
    return result;
  };
  const majorExpandedRowRender = (record: any) => {
    const handleDeleteMajor = async (record: { majorId: API.DeleteRequest; }) => {
      const {data, code} = await deleteMajorInfoUsingPost1({id: record.majorId})
      if (code === 0 && data) {
        message.success('删除成功，即将刷新')
        actionRef.current?.reloadAndRest?.()
      }
    }
    const handleUpdateClick = (record: DataSourceMajor) => {
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
      expandable={{
        expandedRowRender: classesExpandedRowRender,
      }}
      tableRender={(_, dom) => (
        isEmpty ? <Empty
          description={<p>此学院尚未设立专业 <a
            onClick={() => setCreateMajorModalVisible(true)}>现在去创建</a></p>}

        /> : dom
      )}
    />
  }


  const classesExpandedRowRender = (record: any) => {
    const handleDeleteMajor = async (record: { majorId: API.DeleteRequest; }) => {
      const {data, code} = await deleteMajorInfoUsingPost1({id: record.majorId})
      if (code === 0 && data) {
        message.success('删除成功，即将刷新')
        actionRef.current?.reloadAndRest?.()
      }
    }
    const handleUpdateClick = (record: { majorName: string; departName: string; id: any; name: any; }) => {
      const key = `${record.departName}-${record.majorName}`;
      const {departmentId, majorId} = departmentsMajorsMap.get(key) || {departmentId: '', majorId: ''};
      setClassesCurrentRow({
        classesId: record.id,
        classesName: record.name,
        departmentId: departmentId,
        majorId: majorId,
      });
      // 显示更新专业的模态框
      setUpdateClassesModalVisible(true);
    };

    const fetchDataSource: {
      data: API.ClassesInfoVO[] | undefined,
      total: number,
      success: boolean
    } = async () => {
      const {code, data} = await listClassesInfoVoByPageUnderMajorUsingPost1({
        majorId: record.majorId,
        departmentId: record.departmentId
      })
      if (code === 0 && data) {
        return {
          data: data,
          size: data.length,
          success: true
        };
      } else {
        return {
          data: [],
          size: 0,
          success: false
        };
      }

    }


    return <ProTable
      columns={subClassesColumns({
        setCurrentRow: setClassesCurrentRow,
        handleDeleteFunction: handleDeleteMajor,
        setUpdateModalVisible: handleUpdateClick,
      })}
      headerTitle={false}
      search={false}
      actionRef={classesRef}
      options={false}
      // dataSource={[]}
      pagination={false}
      rowKey={"id"}
      request={fetchDataSource}
      tableRender={(props: ProTableProps<any, any>, defaultDom) => {
        // @ts-ignore
        const {dataSource} = props?.action;
        if (dataSource && dataSource?.length === 0) {  // 检查dataSource是否为空
          return <Empty
            description={<p>此学院尚未设立班级 <a
              onClick={() => setCreateClassesModalVisible(true)}>现在去创建</a></p>}
          />;
        }
        return defaultDom;
      }}
    />
  }
  /**
   * 删除节点
   *
   * @param row
   */
  const handleDeleteDepartment = async (record) => {
    const {data, code} = await deleteDepartmentInfoUsingPost1({
      id: record.departmentId
    })
    if (code === 0 && data) {
      message.success('删除成功，即将刷新')
      actionRef.current?.reloadAndRest?.()
    }
  }

  const handleAddDepartment = async (record) => {
    try {
      const {data, code} = await addDepartmentInfoUsingPost1({
        name: record.name
      })
      if (code === 0) {
        message.success('添加成功，即将刷新')
      } else {
        message.error('添加失败')
      }
      return {data, code}
    } catch (e: any) {
      return {
        code: -1,
        data: ""
      }
    }
  }

  const handleUpdateDepartment = async (record) => {
    try {
      const {data, code} = await updateDepartmentInfoUsingPost1({
        name: record.name,
        id: record.departmentId
      })
      if (code === 0) {
        message.success('修改成功，即将刷新')
      } else {
        message.error('修改失败')
      }
      return {data, code}
    } catch (e: any) {
      return {
        code: -1,
        data: ""
      }
    }
  }

  const handleAddMajor = async (record) => {
    try {
      console.log("handleAddMajor record is: ", record)
      const {data, code} = await addMajorInfoUsingPost1({
        departmentId: record.departmentId,
        name: record.majorName
      })
      if (code === 0) {
        message.success('添加成功，即将刷新')
      } else {
        message.error('添加失败')
      }
      return {data, code}
    } catch (e: any) {
      return {
        code: -1,
        data: ""
      }
    }
  }

  const handleUpdateMajor = async (record: { majorName: any; majorId: any; departmentId: any; }) => {
    try {
      const {data, code} = await updateMajorInfoUsingPost1({
        name: record.majorName,
        id: record.majorId,
        departId: record.departmentId
      })
      if (code === 0) {
        message.success('修改成功，即将刷新')
      } else {
        message.error('修改失败')
      }
      return {data, code}
    } catch (e: any) {
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
      onClick: async () => {
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
      onClick: () => {
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

        search={false}
        toolBarRender={() => [
          <Button
            type="primary"
            key="department"
            onClick={() => {
              setCreateDeptModalVisible(true);
            }}
          >
            <PlusOutlined/> 新建学院
          </Button>,
          <Button
            type={"primary"}
            key={"class"}
            onClick={() => setCreateClassesModalVisible(true)}
          >
            <PlusOutlined/>新建班级
          </Button>,
          <Dropdown.Button
            key={"major"}
            icon={<PlusOutlined/>}
            type="primary"
            menu={{
              items: items
            }}
            onClick={() => {
              setCreateMajorModalVisible(true)
            }}
            placement="bottomLeft"
          >
            新建专业
          </Dropdown.Button>,

        ]}
        request={async (params, sort, filter) => {
          const sortField = Object.keys(sort)?.[0];
          const sortOrder = sort?.[sortField] ?? undefined;

          const {data, code} = await listMajorInfoByPageUsingPost1({
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
            setUpdateModalVisible: () => {
              setUpdateDeptModalVisible(prevState => !prevState)
            },
            handleDeleteFunction: handleDeleteDepartment
          })}
        expandable={{expandedRowRender: majorExpandedRowRender}}
      />
      {/*学院更新/创建操作*/}
      <CreateModal
        visible={createDeptModalVisible}
        columns={createDepartmentColumns}
        onSubmit={async (value) => {
          setCreateDeptModalVisible(false);
          const response = await handleAddDepartment(value)
          actionRef.current?.reload();
          return response;
        }}
        onCancel={() => {
          setCreateDeptModalVisible(false);
        }}
      />
      <UpdateModal
        visible={updateDeptModalVisible}
        columns={createDepartmentColumns}
        defaultValue={currentRow}
        onSubmit={async (value, id) => {
          setUpdateDeptModalVisible(false);
          const response = await handleUpdateDepartment(value)
          setCurrentRow(undefined);
          actionRef.current?.reload();
          return response;
        }}
        onCancel={() => {
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
        onSubmit={async (value, id) => {
          setCreateMajorModalVisible(false);
          const response = await handleAddMajor(value)
          actionRef.current?.reload();
          return response;
        }}
        onCancel={() => {
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
        onSubmit={async (value, id) => {
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
        onCancel={() => {
          setUpdateMajorModalVisible(false);
        }}
      />

      {/* 班级更新/创建操作*/}
      <Modal
        title={"新建班级"}
        open={createClassesModalVisible}
        onCancel={() => {
          setCreateClassesModalVisible(false)
          classForm.resetFields()
        }}
        onOk={async () => {
          try {
            const {departmentMajor, classesName} = classForm.getFieldsValue();
            if (departmentMajor && classesName) {
              const majorId = departmentMajor[1];
              const departId = departmentMajor[0];
              const response = await addClassesInfoUsingPost1({
                majorId,
                departId,
                name: classesName,
              })
              message.success("班级创建成功！")

              setCreateClassesModalVisible(false)
              classForm.resetFields()
              actionRef.current?.reload();
              return response;
            } else {
              message.error("请输入完整信息！")
            }
          } catch (error: any) {
            message.error(`班级创建失败: ${error.message}`)
          }
        }}
      >
        <Form form={classForm}>
          <Form.Item
            name="departmentMajor"
            label="所属学院和专业"
            rules={[{required: true, message: '请选择学院和专业!'}]}
          >
            <Cascader
              options={dataSource.map(dept => ({
                value: dept.departmentId,
                label: dept.departmentName,
                children: dept.majors.map(major => ({
                  value: major.majorId,
                  label: major.majorName,
                }))
              }))}
              placeholder="请先选择学院，然后选择专业"
            />
          </Form.Item>
          <Form.Item name={"className"} label={"班级名称"}
                     rules={[{required: true, message: '请输入班级名称!'}]}>
            <Input placeholder={"班级名称"}></Input>
          </Form.Item>
        </Form>
      </Modal>


      <Modal
        title="更新班级信息"
        open={updateClassesModalVisible}
        onCancel={() => {
          setUpdateClassesModalVisible(false);
          classForm.resetFields();
        }}
        onOk={async () => {
          const {departmentMajor, classesName} = classForm.getFieldsValue();
          if (departmentMajor && classesName) {
            const majorId = departmentMajor[1];
            const departId = departmentMajor[0];
            const response = await updateClassesInfoUsingPost1({
              id: classesCurrentRow.classesId,
              majorId,
              departId,
              name: classesName,
            });
            if (response.code === 0) {
              message.success("班级更新成功！");
              setUpdateClassesModalVisible(false);
              classForm.resetFields();
              actionRef.current?.reload();
            } else {
              message.error(`班级更新失败！失败原因：${response.message}`);
            }
            return response;
          } else {
            message.error("请输入完整信息！");
          }
        }}
      >
        <Form
          form={classForm}
          initialValues={{
            departmentMajor: [classesCurrentRow.departmentId, classesCurrentRow.majorId],
            className: classesCurrentRow.classesName
          }}
        >
          <Form.Item
            name="departmentMajor"
            label="所属学院和专业"
            rules={[{required: true, message: '请选择学院和专业!'}]}
          >
            <Cascader
              options={dataSource.map(dept => ({
                value: dept.departmentId,
                label: dept.departmentName,
                children: dept.majors.map(major => ({
                  value: major.majorId,
                  label: major.majorName,
                }))
              }))}
              placeholder="请先选择学院，然后选择专业"
            />
          </Form.Item>
          <Form.Item
            name="className"
            label="班级名称"
            rules={[{required: true, message: '请输入班级名称!'}]}
          >
            <Input placeholder="班级名称"/>
          </Form.Item>
          <Form.Item
            name="grade"
            label={"年级"}
            rules={[{required: true, message: '请输入年级!'}]}>
            <InputNumber placeholder={"年级"}></InputNumber>
          </Form.Item>
        </Form>
      </Modal>
    </PageContainer>
  );
};
export default DepartmentAndMajorListPage;
