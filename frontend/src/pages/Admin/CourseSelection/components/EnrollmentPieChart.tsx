import React from 'react';
import { Pie } from '@ant-design/charts';

interface EnrollmentPieChartProps {
  enrolledCount: number;
  maxStudents: number;
}

const EnrollmentPieChart: React.FC<EnrollmentPieChartProps> = ({ enrolledCount, maxStudents }) => {
  const data = [
    { type: '已选人数', value: enrolledCount },
    { type: '剩余名额', value: maxStudents - enrolledCount },
  ];

  const config = {
    appendPadding: 10,
    data,
    angleField: 'value',
    colorField: 'type',
    radius: 0.8,
    label: {
      type: 'outer',
      content: '{name} {percentage}',
    },
    interactions: [
      {
        type: 'element-active',
      },
    ],
  };

  return (
    <div className="bg-white p-6 rounded-lg shadow-sm">
      <h3 className="text-lg font-medium mb-4">选课统计</h3>
      <Pie {...config} />
    </div>
  );
};

export default EnrollmentPieChart;
