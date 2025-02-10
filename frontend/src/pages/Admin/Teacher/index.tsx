import {ActionType, PageContainer, ProTable} from "@ant-design/pro-components";
import {useRef, useState} from "react";
import {PageRequest} from "@/typings";
import {listTeacherInfoByPageUsingPost1} from "@/services/backend/teacherInfoController";
import {Button, message} from "antd";
import {TeacherColumn} from "@/pages/Admin/Teacher/column";
import TeacherOperateModal from "@/pages/Admin/Teacher/components/TeacherOperateModal";

export default function Teacher() {
  const [loading, setLoading] = useState<boolean>(false);
  const [currentRow, setCurrentRow] = useState<API.TeacherInfoVO | null>(null);
  const actionRef = useRef<ActionType>();
  const [modalVisible, setModalVisible] = useState<boolean>(false);

  const fetchTeacherPageData = async (params: {
    pageSize?: number;
    current?: number;
    keyword?: string;
  }) => {
    setLoading(true);
    console.log("parmas: ", params)
    try {
      const {data, code} = await listTeacherInfoByPageUsingPost1({
        ...params,
      })
      return {
        success: code === 0,
        data: data?.records || [],
        total: Number(data?.total) || 0,
      }
    } catch (e : Error) {
      message.error(`请求教师数据失败: ${e.message}`)
      return {
        success: false,
        data: [],
        total: 0,
      }
    } finally {
      setLoading(false);
    }
  }


  return (
    <PageContainer
      title="教师管理"
    >
      <ProTable<API.TeacherInfoVO>
        rowKey="id"
        actionRef={actionRef}
        request={fetchTeacherPageData}
        loading={loading}
        toolBarRender={()=>[
          <Button
            key="add"
            type="primary"
            onClick={() => {
              setCurrentRow(null);
              setModalVisible(true);
            }}>
            新增教师
          </Button>
        ]}
        columns={TeacherColumn(actionRef, setCurrentRow, setModalVisible)}
      />
      <TeacherOperateModal
        visible={modalVisible}
        setVisible={setModalVisible}
        defaultValue={currentRow}
        actionRef={actionRef}
      />
    </PageContainer>
  )
}
