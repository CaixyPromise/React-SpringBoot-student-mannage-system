import React, {useEffect, useMemo, useState} from 'react';
import {Tabs, Descriptions, Card, Row, Col, message, Badge, Tooltip} from 'antd';
import StudentEditableTable from "@/pages/Teacher/AssignedSelection/components/StudentEditableTable";
import dayjs from "dayjs";
import {getHasTasksBySubjectIdAndCourseTaskIdsUsingPost1} from "@/services/backend/registrationTaskLessonController";

const { TabPane } = Tabs;

const CourseTaskTabs: React.FC<{
  tasks: API.AssignedTeacherSelectionInfo[]
}> = ({ tasks = [] }) => {
  const [hasGradeTask, setHasGradeTask] = useState<API.HasRegistrationTaskVO[]>([]);

  const hasTaskMap = useMemo(()=> {
    const resultMap: Record<number, API.HasRegistrationTaskVO> = {};
    hasGradeTask.forEach(task => {
      resultMap[task?.courseTaskId] = task;
    });
    return resultMap;
  }, [hasGradeTask])

  const queryRegistrationTask = async () => {
    // @ts-ignore
    const collectionCourseSelectionIds: number[] = tasks.map(task => {
      if (task?.courseSelectionId) {
        return task?.courseSelectionId;
      }
    });
    if (collectionCourseSelectionIds.length === 0) {
      return;
    }
    try {
      const {code, data} = await getHasTasksBySubjectIdAndCourseTaskIdsUsingPost1({
        subjectId: tasks[0]?.id,
        courseTaskIds: collectionCourseSelectionIds
      })
      if (code === 0) {
        setHasGradeTask(data);
      }
    } catch (e: Error) {
      message.error(`查询选课任务失败: ${e.message}`);
    }
  }
  useEffect(() => {
    if (tasks.length === 0 || tasks[0]?.id === undefined) {
      return
    }
    queryRegistrationTask();
  }, []);

  const RenderTaskLevel: React.FC<{level: API.HasRegistrationTaskVO}> = ({level}) => {
    if (!level.hasTask) {
      // 如果没有任务，返回空
      return null;
    }

    if (level.isFinished === 1 && level.isPublish === 1) {
      // 已完成并且已发布
      return <Badge status="success"/>
    }

    if (level.isFinished === 1 && level.isPublish === 0) {
      // 已完成但未发布
      return <Badge status="warning"  />;
    }

    if (level.isFinished === 0) {
      // 任务未完成
      return <Badge status="error"  />;
    }

    // 默认情况下返回无任务
    return null;
  };

  return (
    <Card title="课程选课信息" style={{ height: '100%' }}>
      <Tabs>
        {tasks.map(task => {
          const registrationTaskInfo: API.HasRegistrationTaskVO = hasTaskMap[task?.courseSelectionId];
          return (
            <TabPane
              tab={(
                <>
                  <span>{task?.courseSelectionInfoVO?.taskName}</span>
                  {
                    registrationTaskInfo && registrationTaskInfo?.hasTask ?
                      <span style={{marginLeft: "8px"}}><RenderTaskLevel level={registrationTaskInfo} /></span>
                      : null
                  }
                </>) || `选课 ${task?.courseSelectionId}`}
              key={task?.courseSelectionId}
            >
              <Row gutter={[16, 16]} style={{ height: '100%' }}>
                {/* Description部分 */}
                <Col span={24}>
                  <Card title="选课信息" style={{ width: '100%' }}>
                    <Descriptions bordered column={1}>
                      <Descriptions.Item label="选课ID">{task?.courseSelectionId}</Descriptions.Item>
                      <Descriptions.Item label="选课名称">{task?.courseSelectionInfoVO?.taskName}</Descriptions.Item>
                      <Descriptions.Item label="最小学分">
                        {task?.courseSelectionInfoVO?.minCredit}
                      </Descriptions.Item>
                      <Descriptions.Item label="开始时间">
                        {dayjs(task?.courseSelectionInfoVO?.startDate?.toString()).format('YYYY-MM-DD HH:mm')}
                      </Descriptions.Item>
                      <Descriptions.Item label="结束时间">
                        {dayjs(task?.courseSelectionInfoVO?.endDate?.toString()).format('YYYY-MM-DD HH:mm')}
                      </Descriptions.Item>
                    </Descriptions>
                  </Card>
                </Col>

                {/* 表格部分 */}
                <Col span={24} style={{ height: '100%' }}>
                  <Card title="学生课程信息"
                        styles={{
                          body: {
                            paddingLeft: 0,
                            width: 'auto'
                          }
                        }}
                  >
                    <div style={{ width: '100%' }}>
                      <StudentEditableTable
                        registrationTaskInfo={registrationTaskInfo}
                        courseSelectionId={task?.courseSelectionInfoVO?.id}
                        subjectId={task?.id}
                        maxScore={Number(task?.gradeMax)}
                      />
                    </div>
                  </Card>
                </Col>

              </Row>
            </TabPane>
          )
        })}
      </Tabs>
    </Card>
  );
};

export default CourseTaskTabs;
