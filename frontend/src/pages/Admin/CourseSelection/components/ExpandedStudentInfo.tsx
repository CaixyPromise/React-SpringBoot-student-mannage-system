import React, {useCallback, useEffect, useMemo, useState} from "react";
import {getStudentsByCourseSelectionAndSubjectUsingGet1} from "@/services/backend/studentController";
import {Card, Empty, message, Table} from "antd";
import {Bar, Pie} from "@ant-design/charts";
import {useCourseSelection} from "@/pages/Admin/CourseSelection/contexts/CourseSelectionContext";
import EChartsReact from "echarts-for-react";
import {
  studentCountBarOption,
  courseSelectionPieOption,
  studentGenderOption, stuDepartInfoOption, enrollByDayOption, enrollByHourOption
} from "@/pages/Admin/CourseSelection/echars-option";
import dayjs from "dayjs";
import {expandStudentInfo} from "@/pages/Admin/CourseSelection/column";

interface ExpandedStudentInfoProps {
  subjectId: number;
  courseSelectionId: number;
}

const ExpandedStudentInfo: React.FC<ExpandedStudentInfoProps> = ({subjectId, courseSelectionId}) => {
  const {subjectInfoMap} = useCourseSelection();
  const [studentInfo, setStudentInfo] = useState<API.StudentInfoVO[]>([]);
  const subjectInfo: API.CourseSelectSubjectVO & {remainCount: number} = useMemo(()=>{
    const subject = subjectInfoMap[subjectId];
    const maxStudent = subject?.maxStudent ?? 0;
    const enrollCount = subject?.enrollCount ?? 0;
    return {
      ...subject,
      maxStudent,
      enrollCount,
      // 剩余名额
      remainCount: maxStudent - enrollCount
    };
  }, [subjectInfoMap]);
  const queryStudentInfo = useCallback(async () => {
    try {
      const {code, data} = await getStudentsByCourseSelectionAndSubjectUsingGet1({
      courseSelectionId,
      subjectId
      });
      if (code === 0 && data) {
        setStudentInfo(data);
      }
    } catch (e: Error) {
      message.error(e.message);
    }
  }, [subjectId, courseSelectionId])

  useEffect(()=>{
    queryStudentInfo();
  }, [subjectId, courseSelectionId])


  return (
    <>
      <Table
        title={() => <h2>已选学生信息</h2>}
        rowKey="stuId"
        dataSource={studentInfo}
        columns={expandStudentInfo}
        pagination={false}
        locale={{
          emptyText: <Empty description="暂无学生选课数据"/>
        }}
      />
      {
      studentInfo.length > 0 && (<>
          <div style={{display: 'flex', gap: '8px', marginTop: "30px"}}>
            {/* Chart Section */}
            <Card title="选课信息图表" style={{flex: 3}}>
              <div style={{display: 'flex', gap: '16px'}}>
                <div style={{flex: 1}}>
                  <EChartsReact option={courseSelectionPieOption(subjectInfo)}/>
                </div>
                <div style={{flex: 1}}>
                  <EChartsReact option={studentCountBarOption(subjectInfo)}/>
                </div>
                <div style={{flex: 1}}>
                  <EChartsReact option={studentGenderOption(studentInfo)}/>
                </div>
              </div>
            </Card>
          </div>
          <div style={{display: 'flex', gap: '8px', marginTop: "30px"}}>
            {/* Chart Section */}
            <Card title="选课学生院系信息图表" style={{flex: 3}}>
              <div style={{display: 'flex', gap: '16px'}}>
                {
                  stuDepartInfoOption(studentInfo)?.map((item, index) => (<div style={{flex: 1}}>
                    <EChartsReact option={item} key={index}/>
                  </div>))
                }
              </div>
            </Card>
          </div>

          <div style={{display: 'flex', gap: '8px', marginTop: "30px"}}>
            {/* 选课趋势 */}
            <Card title="选课趋势统计" style={{width: "100%"}}>
              <div style={{display: "flex", gap: "16px", justifyContent: "center"}}>
                <div style={{width: "50%"}}>
                  <EChartsReact option={enrollByDayOption(studentInfo)} style={{height: "300px"}}/>
                </div>
                <div style={{width: "50%"}}>
                  <EChartsReact option={enrollByHourOption(studentInfo)} style={{height: "300px"}}/>
                </div>
              </div>
            </Card>
          </div>
        </>)
      }
    </>
  )
}

export default ExpandedStudentInfo;
