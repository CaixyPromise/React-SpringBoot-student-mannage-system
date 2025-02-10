import React, { useRef, useState } from 'react';
import { ActionType, ProColumns, ProTable } from '@ant-design/pro-components';
import { Button, message } from 'antd';
import { EditOutlined, PlusOutlined, SettingOutlined } from '@ant-design/icons';


import CreateOrEditModal from "@/pages/Admin/Semester/components/CreateOrEditModal";
import SetActiveModal from "@/pages/Admin/Semester/components/SetActiveModal";
import {deleteSemestersUsingPost1, listSemestersByPageUsingPost1} from "@/services/backend/semestersController";
const SemestersList: React.FC = () => {
  const actionRef = useRef<ActionType>();
  const [createOrEditModalVisible, setCreateOrEditModalVisible] = useState<boolean>(false);
  const [setActiveModalVisible, setSetActiveModalVisible] = useState<boolean>(false);
  const [currentRecord, setCurrentRecord] = useState<API.SemestersVO | undefined>();

  // 列配置
  const columns: ProColumns<API.SemestersVO>[] = [
    {
      title: '学期ID',
      dataIndex: 'id',
      valueType: 'text',
      search: true,
      width: 100
    },
    {
      title: '学期名称',
      dataIndex: 'name',
      valueType: 'text',
      align: "center",
      search: true,
    },
    {
      title: '激活状态',
      dataIndex: 'isActive',
      valueEnum: {
        1: { text: '激活', status: 'Processing' },
        0: { text: '未激活', status: 'Default' },
      },
      align: "center",
    },
    {
      title: '开始日期',
      dataIndex: 'startDate',
      valueType: 'date',
    },
    {
      title: '结束日期',
      dataIndex: 'endDate',
      valueType: 'date',
    },
    {
      title: '操作',
      valueType: 'option',
      render: (text, record) => [
        <Button
          key="edit"
          type="link"
          icon={<EditOutlined />}
          onClick={() => {
            setCurrentRecord(record);
            setCreateOrEditModalVisible(true);
          }}
        >
          修改
        </Button>,
      ],
    },
  ];

  /**
   * 查询学期数据
   * ProTable 的 request 函数
   */
  const handleQuery = async (
    params: any & {
      pageSize?: number;
      current?: number;
    },
  ) => {
    try {
      // 调用你通过OpenAPI生成的接口
      const res = await listSemestersByPageUsingPost1({
        current: params.current,
        pageSize: params.pageSize,
        // 如果还想根据学期名称搜索:
        name: params.name,
        // 这里可以自行扩展，如 isActive, startDate 范围等等
      });
      if (res && res.data) {
        // res.data 为 BaseResponse<Page<SemestersVO>> 中的 data
        const pageData = res.data;
        return {
          data: pageData.records || [],
          success: true,
          total: pageData.total || 0,
        };
      }
      return {
        data: [],
        success: false,
        total: 0,
      };
    } catch (err) {
      return {
        data: [],
        success: false,
        total: 0,
      };
    }
  };


  return (
    <>
      <ProTable<API.SemestersVO>
        headerTitle="学期管理"
        actionRef={actionRef}
        columns={columns}
        rowKey="id"
        request={handleQuery}
        search={{
          labelWidth: 'auto',
        }}
        toolBarRender={() => [
          <Button
            key="add"
            type="primary"
            icon={<PlusOutlined />}
            onClick={() => {
              setCurrentRecord(undefined);
              setCreateOrEditModalVisible(true);
            }}
          >
            新增学期
          </Button>,
          <Button
            key="setActive"
            icon={<SettingOutlined />}
            onClick={() => {
              setSetActiveModalVisible(true);
            }}
          >
            设置激活学期
          </Button>,
        ]}
      />

      {/* 新增 / 编辑 Modal */}
      {createOrEditModalVisible && (
        <CreateOrEditModal
          visible={createOrEditModalVisible}
          record={currentRecord}
          onCancel={() => setCreateOrEditModalVisible(false)}
          onSuccess={() => {
            setCreateOrEditModalVisible(false);
            actionRef.current?.reload();
          }}
        />
      )}

      {/* 设置激活学期 Modal */}
      {setActiveModalVisible && (
        <SetActiveModal
          visible={setActiveModalVisible}
          onCancel={() => setSetActiveModalVisible(false)}
          onSuccess={() => {
            setSetActiveModalVisible(false);
            actionRef.current?.reload();
          }}
        />
      )}
    </>
  );
};

export default SemestersList;
