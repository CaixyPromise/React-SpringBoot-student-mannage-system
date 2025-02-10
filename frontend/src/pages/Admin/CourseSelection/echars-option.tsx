import {EChartsOption} from "echarts";
import dayjs from "dayjs";

type SubjectInfo = API.CourseSelectSubjectVO & { remainCount: number };
export const courseSelectionPieOption = (subjectInfo: SubjectInfo): EChartsOption => ({
  title: {
    text: "选课情况",
  },
  tooltip: {
    trigger: "item",
    formatter: "{b}: {c} ({d}%)"
  },
  legend: {
    bottom: 0
  },
  series: [
    {
      name: "选课情况",
      type: "pie",
      radius: "80%",
      data: [
        {value: subjectInfo.enrollCount, name: "已选人数"},
        {value: subjectInfo.remainCount, name: "剩余名额"}
      ],
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: "rgba(0, 0, 0, 0.5)"
        }
      },
      label: {
        show: true,
        formatter: "{b}: {c}"
      }
    }
  ]
});

export const studentCountBarOption = (subjectInfo: SubjectInfo): EChartsOption => ({
  title: {
    text: "选课人数情况",

  },
  tooltip: {
    trigger: "axis",
    axisPointer: {
      type: "shadow"
    }
  },
  xAxis: {
    type: "category",
    data: ["已选人数", "剩余名额"]
  },
  yAxis: {
    type: "value"
  },
  series: [
    {
      data: [subjectInfo.enrollCount, subjectInfo.remainCount],
      type: "bar",
      label: {
        show: true,
        position: "top"
      },
      itemStyle: {
        color: function (params: any) {
          return params.dataIndex === 0 ? "#5470c6" : "#91cc75"; // 颜色区分
        }
      }
    }
  ]
});
export const stuDepartInfoOption = (students: API.StudentInfoVO[]): EChartsOption[] => {
  /** 计算学院、专业、班级分布 */
  const departmentCount: Record<string, number> = {};
  const majorCount: Record<string, number> = {};
  const classCount: Record<string, number> = {};

  students.forEach((student) => {
    departmentCount[student?.stuDepart] = (departmentCount[student?.stuDepart] || 0) + 1;
    majorCount[student?.stuMajor] = (majorCount[student?.stuMajor] || 0) + 1;
    classCount[student?.stuClass] = (classCount[student?.stuClass] || 0) + 1;
  });

  /** 获取前 N 个最多的项目 */
  const getTopN = (data: Record<string, number>, N = 10) =>
    Object.entries(data)
      .map(([name, value]) => ({ name, value }))
      .sort((a, b) => b.value - a.value) // 按照数量降序排序
      .slice(0, N);

  // 取前 10 个数据
  const topDepartments = getTopN(departmentCount);
  const topMajors = getTopN(majorCount);
  const topClasses = getTopN(classCount);

  return [
    {
      title: { text: "前 10 学院分布" },
      tooltip: { trigger: "axis" },
      legend: { data: ["学院"] },
      xAxis: {
        type: "category",
        data: topDepartments.map((item) => item.name),
        axisLabel: { rotate: 30, interval: 0 } // 旋转防止文字重叠
      },
      yAxis: { type: "value" },
      series: [
        {
          name: "学院",
          type: "bar",
          data: topDepartments.map((item) => item.value),
          itemStyle: { color: "#5470c6" } // 颜色
        }
      ]
    },
    {
      title: { text: "前 10 专业分布" },
      tooltip: { trigger: "axis" },
      legend: { data: ["专业"] },
      xAxis: {
        type: "category",
        data: topMajors.map((item) => item.name),
        axisLabel: { rotate: 30, interval: 0 }
      },
      yAxis: { type: "value" },
      series: [
        {
          name: "专业",
          type: "bar",
          data: topMajors.map((item) => item.value),
          itemStyle: { color: "#91cc75" }
        }
      ]
    },
    {
      title: { text: "前 10 班级分布" },
      tooltip: { trigger: "axis" },
      legend: { data: ["班级"] },
      xAxis: {
        type: "category",
        data: topClasses.map((item) => item.name),
        axisLabel: { rotate: 30, interval: 0 }
      },
      yAxis: { type: "value" },
      series: [
        {
          name: "班级",
          type: "bar",
          data: topClasses.map((item) => item.value),
          itemStyle: { color: "#fac858" }
        }
      ]
    }
  ];
};


export const studentGenderOption = (students: API.StudentInfoVO[]) => {
  const genderCount = { '男': 0, '女': 0, '未知': 0 };

  students.forEach((student) => {
    if (student.stuSex === 1) genderCount["男"] += 1;
    else if (student.stuSex === 2) genderCount["女"] += 1;
    else genderCount["未知"] += 1;
  });
  const genderStats = Object.entries(genderCount).map(([name, value]) => ({ name, value }));
  return {
    tooltip: {
      trigger: "item",
      formatter: "{b}: {c} ({d}%)"
    },
    legend: {
      bottom: 0
    },
    series: [
      {
        name: "性别分布",
        type: "pie",
        radius: "80%",
        data: genderStats,
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: "rgba(0, 0, 0, 0.5)"
          }
        },
        label: {
          show: true,
          formatter: "{b}: {c}"
        }
      }
    ]
  }
}
export const enrollByDayOption = (students: API.StudentCourseSelection[]): EChartsOption => {
  /** 计算选课时间数据 */
  const today = dayjs().format("YYYY-MM-DD"); // 获取当前日期
  const enrollCountByDay: Record<string, number> = {};

  students.forEach((student) => {
    if (!student.selectTime) return;
    const enrollDate = dayjs(student.selectTime).format("YYYY-MM-DD");
    enrollCountByDay[enrollDate] = (enrollCountByDay[enrollDate] || 0) + 1;
  });

  const allDates = Object.keys(enrollCountByDay).sort(); // 获取所有日期并排序
  const enrollData = allDates.map((d) => enrollCountByDay[d]);
  const lastRecordedDate = allDates.length > 0 ? allDates[allDates.length - 1] : today; // 获取数据最后一天

  return {
    title: { text: `每日选课人数（截止至 ${lastRecordedDate}）`, left: "center" },
    tooltip: { trigger: "axis" },
    xAxis: { type: "category", data: allDates, axisLabel: { rotate: 30, interval: 0 } },
    yAxis: { type: "value" },
    series: [
      {
        name: "选课人数（柱状图）",
        type: "bar",
        data: enrollData,
        itemStyle: { color: "#73c0de" },
      },
      {
        name: "选课趋势（折线图）",
        type: "line",
        data: enrollData,
        smooth: true, // 平滑曲线
        lineStyle: { color: "#ff7f50" }, // 折线颜色
        itemStyle: { color: "#ff7f50" }, // 数据点颜色
        label: { show: true, position: "top" }, // 显示数据点值
        markPoint: {
          data: [
            {
              type: "max",
              name: "最大值",
              symbol: "pin",
              symbolSize: 50,
              itemStyle: { color: "red" }, // 高亮最大值
            },
          ],
        },
      },
    ],
  };
};


export const enrollByHourOption = (students: API.StudentCourseSelection[]): EChartsOption => {
  /** 计算选课时间数据 */
  const today = dayjs().format("YYYY-MM-DD"); // 获取当前日期
  const enrollCountByDay: Record<string, number> = {};
  const enrollCountByHour: Record<number, number> = Array(24).fill(0); // 24 小时数组

  students.forEach((student) => {
    if (!student.selectTime) return;
    const enrollDate = dayjs(student.selectTime).format("YYYY-MM-DD");
    enrollCountByDay[enrollDate] = (enrollCountByDay[enrollDate] || 0) + 1;

    const hour = dayjs(student.selectTime).hour();
    if (!enrollCountByHour[hour]) enrollCountByHour[hour] = 0;
    enrollCountByHour[hour]++;
  });

  const allDates = Object.keys(enrollCountByDay).sort();
  const lastRecordedDate = allDates.length > 0 ? allDates[allDates.length - 1] : today;

  // 如果今天 > 最后一天的记录，则使用最后一天数据
  const isUsingToday = today <= lastRecordedDate ? today : lastRecordedDate;

  return {
    title: { text: `24 小时选课趋势（${isUsingToday}）`, left: "center" },
    tooltip: { trigger: "axis" },
    xAxis: { type: "category", data: Array.from({ length: 24 }, (_, i) => `${i}:00`) },
    yAxis: { type: "value" },
    series: [{ name: "选课人数", type: "line", data: enrollCountByHour, itemStyle: { color: "#ff7875" } }]
  };
};
