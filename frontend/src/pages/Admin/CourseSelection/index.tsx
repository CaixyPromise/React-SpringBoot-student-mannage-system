import React, {useRef, useState} from 'react';
import {ActionType, PageContainer} from '@ant-design/pro-components';
import {ProTable} from '@ant-design/pro-components';
import {Button, message} from 'antd';
import {PlusOutlined} from '@ant-design/icons';
import ExpandedSubjectInfo from './components/ExpandedSubjectInfo';
import {CourseSelectionProvider} from './contexts/CourseSelectionContext';
import {
   pageCourseSelectionUsingPost1,
} from "@/services/backend/courseSelectionInfoController";
import AddSelectionCourseClassModal from "@/pages/Admin/CourseSelection/components/AddSelectionCourseClassModal";
import {SelectionCourseColumnConfig} from "@/pages/Admin/CourseSelection/column";
import AddSelectionCourseTaskModal from "@/pages/Admin/CourseSelection/components/AddSelectionCourseTaskModal";
import ExpandStudentSelectionInfo from "@/pages/Admin/CourseSelection/components/ExpandStudentSelectionInfo";
import dayjs from "dayjs";

const CourseSelectionTaskList: React.FC = () => {
  const [expandedRowKeys, setExpandedRowKeys] = useState<string[]>([]);
  const [tableDataDict, setTableDataDict] = useState<Record<number, API.CourseSelectionInfoVO>>({});
  const [addTaskModalVisible, setAddTaskModalVisible] = useState<boolean>(false);
  const actionRef = useRef<ActionType>();
  const [detailsExpandInfo, setDetailsExpandInfo] = useState<{
    taskItem: API.CourseSelectionInfoVO;
    visible: boolean
  }>({visible: false, taskItem: {}});

  const openDetailsModal = (taskItem: API.CourseSelectionInfoVO | undefined) => {
    if (!taskItem) {
      return;
    }
    setDetailsExpandInfo({
      taskItem: taskItem,
      visible: true,
    })
  }

  const onCloseDetailsModal = () => {
    setDetailsExpandInfo({
      taskItem: {},
      visible: false,
    })
  }


  return (
    <PageContainer>
      <CourseSelectionProvider
        tableDataDict={tableDataDict}
      >
        <ProTable<API.CourseSelectionInfoVO>
          actionRef={actionRef}
          headerTitle="选课任务管理"
          rowKey="id"
          columns={SelectionCourseColumnConfig(actionRef, openDetailsModal)}
          search={{
            labelWidth: 120,
          }}
          toolBarRender={() => [
            <Button
              key="add"
              type="primary"
              icon={<PlusOutlined/>}
              onClick={() => {
                setAddTaskModalVisible(true)
                actionRef?.current?.reload()
              }}
            >
              新建选课任务
            </Button>,
          ]}
          request={async (params, sort, filter) => {
            const { pickTime, ...rest } = params;
            const response = await pageCourseSelectionUsingPost1({
              ...params,
              startDate: pickTime?.[0] ? dayjs(pickTime[0]).format("YYYY-MM-DDTHH:mm:ss") : undefined,
              endDate: pickTime?.[1] ? dayjs(pickTime[1]).format("YYYY-MM-DDTHH:mm:ss") : undefined,
            })
            console.log("params: ", params)

            const {code, data} = response;
            if (code === 0 && data) {
              // 将数据记录转为 id 映射对象
              const idMap = data.records.reduce((map: { [x: string]: any; }, record: API.CourseSelectionInfoVO) => {
                map[record.id] = record;
                return map;
              }, {} as Record<number, API.CourseSelectionInfoVO>);
              setTableDataDict(idMap);
              return {
                data: data.records,
                success: true,
                total: 1,
              }
            }
            message.error('获取选课任务列表失败');
            return {
              data: [],
              success: false,
              total: 0,
            }
          }}
          expandable={{
            expandedRowKeys,
            onExpandedRowsChange: (keys) => setExpandedRowKeys(keys as string[]),
            expandedRowRender: (record) => (
              <ExpandedSubjectInfo taskItem={record}/>
            ),
          }}
          className="mb-0"
        />
        <AddSelectionCourseClassModal />
        <AddSelectionCourseTaskModal
          visible={addTaskModalVisible}
          setVisible={setAddTaskModalVisible}
        />
        <ExpandStudentSelectionInfo
          courseSelectionItem={detailsExpandInfo?.taskItem}
          visible={detailsExpandInfo.visible}
          onClose={onCloseDetailsModal}
        />
      </CourseSelectionProvider>
    </PageContainer>
  );
};

export default CourseSelectionTaskList;

