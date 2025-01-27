import {ProTable} from "@ant-design/pro-components";
import {AssignSelectionColumns} from "@/pages/Teacher/AssignedSelection/column";
import React from "react";
import {getAssignedTeacherSelectionInfoUsingGet1} from "@/services/backend/teacherInfoController";
import CourseTaskTabs from "@/pages/Teacher/AssignedSelection/components/CourseTaskTabs";

const AssignedSelectionPage: React.FC = () => {

  return (
    <ProTable
      columns={AssignSelectionColumns}
      request={async (params, sorter, filter) => {
        const {code, data} = await getAssignedTeacherSelectionInfoUsingGet1();
        if (code === 0) {
          const group = {};
          data?.forEach(record => {
            const subjectId = record.id;
            if (!group[subjectId]) {
              // 初始化分组对象，保留第一条记录作为表格行数据，并创建 tasks 数组保存所有任务信息
              group[subjectId] = { ...record, tasks: [] };
            }
            group[subjectId].tasks.push(record);
          });
          const result = Object.values(group);
          return {
            data: result ? result : [],
            success: true
          }
        }
        return {
          data:  [],
          success: false
        };
      }}
      expandable={{
        expandedRowRender: record => <CourseTaskTabs tasks={record.tasks} />,
      }}
      rowKey="id"
      search={false}
      pagination={false}
      dateFormatter="string"
      headerTitle="教师分配的选修课程列表"
    />
  );
}

export default AssignedSelectionPage;
