import {PageContainer, ProTable} from "@ant-design/pro-components";
import {selectCourseTaskColumn} from "@/pages/Student/select-course/column";
import React, {useEffect, useState} from "react";
import {getUserCourseSelectionInfoUsingGet1} from "@/services/backend/courseSelectionInfoController";
import {Empty, message} from "antd";
import SelectTaskSubjectTable from "@/pages/Student/select-course/components/SelectTaskSubjectTable";
import useDebounce from "@/hooks/useDebounce";

const SelectCoursePage = () => {
  const [selectTaskList, setSelectTaskList] = useState<API.CourseSelectionInfoVO[]>([]);
  const [expandedRowKeys, setExpandedRowKeys] = useState<string[]>([]);
  const querySelectTask = async ()=> {
    try {
      const {code, data} = await getUserCourseSelectionInfoUsingGet1();
      if (code === 0) {
        setSelectTaskList(data);
      }
      else {
        message.error("获取选课任务失败");
      }
    }
    catch (e: Error) {
      message.error(`获取选课任务失败: ${e.message}`);
    }
  }


  useEffect(()=>{
    Promise.all([
      querySelectTask()
    ])
  }, [])

  return <>
    <PageContainer
      title="课程选课"
    >
      <ProTable<API.CourseSelectionInfoVO>
        // title="选课任务列表"
        pagination={false}
        search={false}
        dataSource={selectTaskList}
        columns={selectCourseTaskColumn}
        expandable={{
          expandedRowKeys,
          onExpandedRowsChange: (keys) => setExpandedRowKeys(keys as string[]),
          expandedRowRender: (record) => (
            <SelectTaskSubjectTable taskItem={record} />
            // <ExpandedSubjectInfo taskItem={record}/>
          ),
        }}
      />
    </PageContainer>
  </>
}

export default SelectCoursePage;
