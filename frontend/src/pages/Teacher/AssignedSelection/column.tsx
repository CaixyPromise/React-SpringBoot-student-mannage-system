
export const AssignSelectionColumns = [
  {
    title: '科目ID',
    dataIndex: 'id',
  },
  {
    title: '科目名称',
    dataIndex: 'name',
  },
  {
    title: '课程类型',
    dataIndex: 'courseType',
  },
  {
    title: '学分',
    dataIndex: 'gradeCredit',
  },
  {
    title: '教室',
    dataIndex: 'classRoom',
  },
  {
    title: '上课时间',
    dataIndex: 'classTimes',
    render: (_, record) => {
      const times = record.classTimes || [];
      return times.map(t => `星期${t.dayOfWeek} 第${t.period}节`).join('; ');
    },
  },
  {
    title: '最大人数',
    dataIndex: 'maxStudents',
  },
  {
    title: '已选人数',
    dataIndex: 'enrolledCount',
  },
  {
    title: '选课任务名称',
    dataIndex: ['courseSelectionInfoVO', 'taskName'],
  },
  {
    title: '任务开始时间',
    dataIndex: ['courseSelectionInfoVO', 'startDate'],
    valueType: 'dateTime',
  },
  {
    title: '任务结束时间',
    dataIndex: ['courseSelectionInfoVO', 'endDate'],
    valueType: 'dateTime',
  },
];
