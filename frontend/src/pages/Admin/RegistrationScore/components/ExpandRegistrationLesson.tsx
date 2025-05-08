import React, {useState} from "react";
import {message} from "antd";
import {listRegistrationTaskLessonVoByPageUsingGet1} from "@/services/backend/registrationTaskLessonController";
import {ProTable} from "@ant-design/pro-components";
import {ExpandRegistrationLessonColumn} from "@/pages/Admin/RegistrationScore/columns";
import ExpandStudentGradeInfo from "@/pages/Admin/RegistrationScore/components/ExpandStudentGradeInfo";


const ExpandRegistrationLesson: React.FC<{
  id?: number
}> = ({id}) => {
  const [expandedRowKeys, setExpandedRowKeys] = useState<string[]>([]);

  const queryRegistrationLesson = async () => {
    const defaultReturn = {
      success: false,
      data: [],
      total: 0
    }
    if (!id) {
      return defaultReturn;
    }
    try {
      const {code, data} = await listRegistrationTaskLessonVoByPageUsingGet1({
        id
      })
      if (code === 0) {
        defaultReturn.success = true;
        defaultReturn.data = data || [];
        defaultReturn.total = data?.length || 0;
      }
      return defaultReturn;
    } catch (e: Error) {
      message.error("查询失败")
      return defaultReturn;
    }
  }

  return (
    <>
      <ProTable
        rowKey='id'
        search={false}
        columns={ExpandRegistrationLessonColumn}
        request={queryRegistrationLesson}
        pagination={false}
        expandable={{
          expandedRowKeys,
          onExpandedRowsChange: (keys) => setExpandedRowKeys(keys),
          expandedRowRender: (record) => (
            <>
              <ExpandStudentGradeInfo taskId={id} courseTaskId={record?.courseSelectionInfo?.id} subjectInfo={record?.subjectsInfo} />
            </>
          ),
        }}
      />
    </>
  )
}


export default ExpandRegistrationLesson;
