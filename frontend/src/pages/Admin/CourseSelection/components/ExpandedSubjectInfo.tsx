import React, {useEffect, useState} from 'react';
import {Badge, Button, Card, Empty, Table, Tooltip} from 'antd';
import {useCourseSelection} from '../contexts/CourseSelectionContext';
import CourseSelectionTree from './CourseSelectionTree';
import {Bar, Pie} from '@ant-design/charts';
import {getAllSelectionClassesTreeUsingGet1} from "@/services/backend/classesController";
import {
  getSelectTaskCoursesByTaskIdUsingGet1,
} from "@/services/backend/courseSelectionInfoController";
import ClassTimesDisplay from "@/components/ClassTimesDisplay";
import ExpandedStudentInfo from "@/pages/Admin/CourseSelection/components/ExpandedStudentInfo";
import {ProTable} from "@ant-design/pro-components";
import ExpandStudentSelectionInfo from "@/pages/Admin/CourseSelection/components/ExpandStudentSelectionInfo";

interface ExpandedClassInfoProps {
  taskItem: API.CourseSelectionInfoVO;
}

const ExpandedSubjectInfo: React.FC<ExpandedClassInfoProps> = ({taskItem}) => {
  const {setDepartments, setLoading, setSubjectInfoMap} = useCourseSelection();
  const [subjectData, setSubjectData] = useState<Array<API.SubjectsVO>>([]);
  const taskId = taskItem.id;
  const [expandedRowKeys, setExpandedRowKeys] = useState<string[]>([]);
  const [detailModalVisible, setDetailModalVisible] = useState<boolean>(false);

  const fetchData = async () => {
    try {
      const {code, data} = await getAllSelectionClassesTreeUsingGet1({
        courseSelectionId: taskId,
      })
      if (code === 0) {
        setDepartments(data);
      }
    } catch (error) {
      console.error('Failed to fetch class data:', error);
    }
  };
  const fetchSubjectList = async () => {
    try {
      const {code, data} = await getSelectTaskCoursesByTaskIdUsingGet1({
        taskId: taskId
      });
      if (code === 0) {
        setSubjectData(data);
        const result: Record<number, API.CourseSelectSubjectVO> = {};
        data?.forEach(item => {
          result[item?.id as number] = item;
        })
        setSubjectInfoMap(result);
      }
    } catch (e) {
      console.error('请求科目信息出错', e);
    }
  }

  useEffect(() => {
    if (!taskId) {
      return;
    }
    setLoading(true);
    Promise.all([
      fetchData(),
      fetchSubjectList()
    ]).finally(() => setLoading(false))
  }, [taskItem, setDepartments, setLoading]);

  const {maxStudents = 0, enrolledCount = 0} = taskItem;

  const pieData = [
    {type: '已选人数', value: taskItem.enrolledCount},
    {type: '剩余名额', value: maxStudents - enrolledCount},
  ];

  const barData = [
    {category: '已选人数', value: enrolledCount},
    {category: '剩余名额', value: maxStudents - enrolledCount},
  ];

  const subjectColumns = [
    {
      title: '科目id',
      width: 120,
      dataIndex: 'id',
      render: (item) => <a>{item}</a>,
      align: "center",
    },
    {
      title: '科目名称',
      dataIndex: 'name',
      key: 'name',
    },
    {
      title: '学分',
      dataIndex: 'gradeCredit',
      key: 'gradeCredit',
    },
    {
      title: '已选学生人数',
      dataIndex: 'enrollCount',
      key: 'enrollCount',
    },
    {
      title: '最大选课人数',
      dataIndex: 'maxStudent',
      key: 'maxStudent',
    },
    {
      title: "科目最高分阈值",
      dataIndex: "gradeMax",
      valueType: "digit",
      render: (_, record) => (
        <><Badge status="processing" text={`${record.gradeMax} 分`}/> </>
      )
    },

    {
      title: "科目最低分阈值",
      dataIndex: "gradeMin",
      valueType: "digit",
      render: (_, record) => (
        <><Badge status="warning" text={`${record.gradeMin} 分`}/> </>
      )
    },

    {
      title: "优秀分数线",
      dataIndex: "gradeExcellent",

      valueType: "digit",
      render: (_, record) => (
        <><Badge status="success" text={`${record.gradeExcellent} 分`}/> </>
      )
    },
    {
      title: "不及格分数线",
      dataIndex: "gradeFail",
      valueType: "digit",
      render: (_, record) => (
        <><Badge status="error" text={`${record.gradeFail} 分`}/> </>
      )
    },
    {
      title: '课程类型',
      dataIndex: 'courseType',
      key: 'courseType',
      render: (type: number) => (type === 1 ? '必修' : '选修'),
    },
    {
      title: "上课地点",
      render: (text: string, record: API.CourseSelectionInfoVO) => {
        return <>
          <span>{record.classRoom}</span>
        </>
      }
    },
    {
      title: "上课时间",
      dataIndex: "classTimes",
      render: (classTimes: API.ClassTime[]) => <ClassTimesDisplay classTimes={classTimes} />,
    },
    {
      title: "授课教师",
      render: (text: string, record: API.CourseSelectionInfoVO) => {
        const {teacherName, teacherId, teacherMajor, teacherDepart} = record?.teacherInfo;

        return (
          <Tooltip
            title={
              <div style={{
                marginBottom: "4px"
              }}>
                <div>教师编号: {teacherId}</div>
                <div>教师专业: {teacherMajor}</div>
                <div>教师部门: {teacherDepart}</div>
              </div>
            }
          >
            <span>{teacherName}</span>
          </Tooltip>
        )
      }
    },
    // tips: 为了避免出现数据冲突。保证数据一致性。此处不做选课任务的科目增加删除操作，因为选课学分要求是固定的。
  ];

  return (
    <> {
      taskId ? <>
          <Table
            search={false}
            toolBarRender={false}
            title={() => (
              <div style={{display: 'flex', justifyContent: 'space-between'}}>
                <h2>参与选课科目列表</h2>
              </div>
            )}
            rowKey="id"
            dataSource={subjectData}
            columns={subjectColumns}
            pagination={false}
            style={{marginBottom: '16px'}}
            expandable={{
              expandedRowKeys,
              onExpandedRowsChange: (keys) => setExpandedRowKeys(keys as string[]),
              expandedRowRender: (record) => (
                <>
                  <ExpandedStudentInfo  courseSelectionId={taskId} subjectId={record?.id}/>
                </>
              ),
            }}
          />
          {/*<div style={{display: 'flex', gap: '8px'}}>*/}
          {/*  /!* Tree Section *!/*/}
          {/*  <Card title="班级列表" style={{flex: 2, marginRight: '16px'}}>*/}
          {/*    <CourseSelectionTree taskId={taskItem.id}/>*/}
          {/*  </Card>*/}

          {/*  /!* Chart Section *!/*/}
          {/*  <Card title="统计图表" style={{flex: 3}}>*/}
          {/*    <div style={{display: 'flex', gap: '16px'}}>*/}
          {/*      <div style={{flex: 1}}>*/}
          {/*        <Pie {...pieConfig} />*/}
          {/*      </div>*/}
          {/*      <div style={{flex: 1}}>*/}
          {/*        <Bar {...barConfig} />*/}
          {/*      </div>*/}
          {/*    </div>*/}
          {/*  </Card>*/}
          {/*</div>*/}
        </> :
        <Empty>
          <span>请求选课任务信息出错 :(</span>
          <span style={{color: "red"}}>错误信息：选课任务信息Id为空</span>
        </Empty>
    }

    </>
  );
};

export default ExpandedSubjectInfo;
