import React, {useEffect, useState} from 'react';
import {Badge, Button, Card, Empty, Table} from 'antd';
import {useCourseSelection} from '../contexts/CourseSelectionContext';
import CourseSelectionTree from './CourseSelectionTree';
import {Bar, Pie} from '@ant-design/charts';
import {getAllSelectionClassesTreeUsingGet1} from "@/services/backend/classesController";
import {
  getSelectTaskCoursesByTaskIdUsingGet1,
  getSelectTaskCoursesUsingGet1
} from "@/services/backend/courseSelectionInfoController";

interface ExpandedClassInfoProps {
  taskItem: API.CourseSelectionInfoVO;
}

const ExpandedClassInfo: React.FC<ExpandedClassInfoProps> = ({taskItem}) => {
  const {setDepartments, setLoading} = useCourseSelection();
  const [subjectData, setSubjectData] = useState<Array<API.SubjectsVO>>([]);
  const taskId = taskItem.id;

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
  const fetchSubject = async () => {
    try {
      const {code, data} = await getSelectTaskCoursesByTaskIdUsingGet1({
        taskId: taskId
      });
      if (code === 0) {
        setSubjectData(data);
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
      fetchSubject()
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

  const pieConfig = {
    data: pieData,
    angleField: 'value',
    colorField: 'type',
    radius: 0.8,
    label: {
      type: 'outer',
      content: '{name} {percentage}',
    },
    legend: {
      position: 'bottom',
    },
    interactions: [
      {
        type: 'element-active',
      },
    ],
    animation: {
      appear: {
        animation: 'wave-in',
        duration: 1000,
      },
    },
  };

  const barConfig = {
    data: barData,
    xField: 'category',
    yField: 'value',
    seriesField: 'category',
    legend: false,
    animation: {
      appear: {
        animation: 'fade-in',
        duration: 800,
      },
    },
  };

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
    // tips: 为了避免出现数据冲突。保证数据一致性。此处不做选课任务的科目增加删除操作，因为选课学分要求是固定的。
  ];

  return (
    <> {
      taskId ? <>
          <Table
            title={() => <h2>参与选课科目列表</h2>}
            dataSource={subjectData}
            columns={subjectColumns}
            rowKey="id"
            pagination={false}
            bordered
            style={{marginBottom: '16px'}}
          />
          <div style={{display: 'flex', gap: '8px'}}>
            {/* Tree Section */}
            <Card title="班级列表" style={{flex: 2, marginRight: '16px'}}>
              <CourseSelectionTree taskId={taskItem.id}/>
            </Card>

            {/* Chart Section */}
            <Card title="统计图表" style={{flex: 3}}>
              <div style={{display: 'flex', gap: '16px'}}>
                <div style={{flex: 1}}>
                  <Pie {...pieConfig} />
                </div>
                <div style={{flex: 1}}>
                  <Bar {...barConfig} />
                </div>
              </div>
            </Card>
          </div>
        </> :
        <Empty>
          <span>请求选课任务信息出错 :(</span>
          <span style={{color: "red"}}>错误信息：选课任务信息Id为空</span>
        </Empty>
    }
    </>
  );
};

export default ExpandedClassInfo;
