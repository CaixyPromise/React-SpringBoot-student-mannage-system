import React, {useRef, useState} from 'react';
import {ActionType, PageContainer} from '@ant-design/pro-components';
import {ProTable} from '@ant-design/pro-components';
import {Button, message} from 'antd';
import {PlusOutlined} from '@ant-design/icons';
import ExpandedClassInfo from './components/ExpandedClassInfo';
import {CourseSelectionProvider} from './contexts/CourseSelectionContext';
import {
  pageCourseSelectionUsingGet1,
} from "@/services/backend/courseSelectionInfoController";
import AddSelectionCourseClassModal from "@/pages/Admin/CourseSelection/components/AddSelectionCourseClassModal";
import {SelectionCourseColumnConfig} from "@/pages/Admin/CourseSelection/column";
import AddSelectionCourseTaskModal from "@/pages/Admin/CourseSelection/components/AddSelectionCourseTaskModal";

const CourseSelectionTaskList: React.FC = () => {
  const [expandedRowKeys, setExpandedRowKeys] = useState<string[]>([]);
  const [tableDataDict, setTableDataDict] = useState<Record<number, API.CourseSelectionInfoVO>>({});
  const [addTaskModalVisible, setAddTaskModalVisible] = useState<boolean>(false);
  const actionRef = useRef<ActionType>();

  return (
    <PageContainer>
      <CourseSelectionProvider
        tableDataDict={tableDataDict}
      >
        <ProTable<API.CourseSelectionInfoVO>
          actionRef={actionRef}
          headerTitle="选课任务管理"
          rowKey="id"
          columns={SelectionCourseColumnConfig(actionRef)}
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
            const response = await pageCourseSelectionUsingGet1({
              ...params,
            })
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
              <ExpandedClassInfo taskItem={record}/>
            ),
          }}
          className="mb-0"
        />
        <AddSelectionCourseClassModal />
        <AddSelectionCourseTaskModal
          visible={addTaskModalVisible}
          setVisible={setAddTaskModalVisible}
        />
      </CourseSelectionProvider>
    </PageContainer>
  );
};

export default CourseSelectionTaskList;

