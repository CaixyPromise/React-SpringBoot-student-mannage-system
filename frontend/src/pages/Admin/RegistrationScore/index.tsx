import {ActionType, PageContainer, ProTable} from "@ant-design/pro-components";
import React, {useRef, useState} from "react";
import AddRegistrationScoreModal from "@/pages/Admin/RegistrationScore/components/AddRegistrationScoreModal";
import {Button, message} from "antd";
import {RegistrationTableColumn} from "@/pages/Admin/RegistrationScore/columns";
import {listRegistrationTaskByPageUsingPost1} from "@/services/backend/registrationTaskController";
import ExpandRegistrationLesson from "@/pages/Admin/RegistrationScore/components/ExpandRegistrationLesson";

const RegistrationScore: React.FC = () => {
  const actionRef = useRef<ActionType>()
  const [addVisible, setAddVisible] = useState<boolean>(false);
  const [expandedRowKeys, setExpandedRowKeys] = useState<string[]>([]);

  const queryRegistrationPage = async (params: {
    pageSize?: number;
    current?: number;
    keyword?: string;
  }) => {
    const result = {
      data: [],
      success: false,
      total: 0,
    }
    try {
      const {code, data} = await listRegistrationTaskByPageUsingPost1({
        ...params,
      });
      if (code === 0) {
        result.data = data.records;
        result.success = true;
        result.total = data.total;
      }
    } catch (error) {
      message.error("查询失败")
    }
    return result;
  }

  return (
    <PageContainer title="登分任务管理">
      <ProTable
        rowKey='id'
        actionRef={actionRef}
        toolBarRender={() => [
          <Button key='add' type="primary" onClick={() => setAddVisible(true)}>添加登分任务</Button>
        ]}
        columns={RegistrationTableColumn}
        request={queryRegistrationPage}
        expandable={{
          expandedRowKeys,
          onExpandedRowsChange: (keys) => setExpandedRowKeys(keys),
          expandedRowRender: (record: API.RegistrationTaskVO) => (
            <>
              <ExpandRegistrationLesson id={record?.id} />
            </>
          ),
        }}
      />
      <AddRegistrationScoreModal visible={addVisible} setVisible={setAddVisible}/>
    </PageContainer>
  );
}


export default RegistrationScore;
