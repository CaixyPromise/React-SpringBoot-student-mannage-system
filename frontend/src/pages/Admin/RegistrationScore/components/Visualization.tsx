import React, {useMemo} from "react";
import {Row, Col, Card, Statistic} from "antd";
import EChartsReact from "echarts-for-react";
import { prepareBoxplotData } from 'echarts/dist/extension/dataTool';

const getStatistics = (grades: API.StudentsGradeForAdminVO[], subjectInfo: API.SubjectsVO) => {
  let excellentCount = 0;
  let failCount = 0;
  let totalScore = 0;
  let usualScore = 0;
  let finalScore = 0;
  let maxTotalScore = -Infinity;
  let minTotalScore = Infinity;
  let maxUsualScore = -Infinity;
  let minUsualScore = Infinity;
  let maxFinalScore = -Infinity;
  let minFinalScore = Infinity;

  grades.forEach((grade) => {
    const total = grade?.gradeItem?.totalGrade || 0;
    const usual = grade?.gradeItem?.usualGrade || 0;
    const final = grade?.gradeItem?.finalGrade || 0;

    totalScore += total;
    usualScore += usual;
    finalScore += final;

    // 计算最高/最低分
    maxTotalScore = Math.max(maxTotalScore, total);
    minTotalScore = Math.min(minTotalScore, total);
    maxUsualScore = Math.max(maxUsualScore, usual);
    minUsualScore = Math.min(minUsualScore, usual);
    maxFinalScore = Math.max(maxFinalScore, final);
    minFinalScore = Math.min(minFinalScore, final);

    if (total >= (subjectInfo?.gradeExcellent ?? 0)) {
      excellentCount++;
    }
    if (total < (subjectInfo?.gradeFail ?? 0)) {
      failCount++;
    }
  });

  const totalCount = grades.length;
  return {
    excellentCount,
    failCount,
    totalCount,
    avgTotalScore: totalScore / totalCount,
    avgUsualScore: usualScore / totalCount,
    avgFinalScore: finalScore / totalCount,
    maxTotalScore,
    minTotalScore,
    maxUsualScore,
    minUsualScore,
    maxFinalScore,
    minFinalScore,
  };
};

const Visualization: React.FC<{
  gradeItems: API.StudentsGradeForAdminVO[],
  subjectInfo: API.SubjectsVO
}> = ({gradeItems, subjectInfo}) => {
  const statistics = useMemo(() => getStatistics(gradeItems, subjectInfo), [gradeItems]);

  // **📈 成绩分布饼图配置**
  const pieOptions = {
    title: {text: "成绩分布", left: "center"},
    tooltip: {trigger: "item"},
    legend: {orient: "vertical", left: "left"},
    series: [
      {
        name: "成绩分布",
        type: "pie",
        radius: "50%",
        data: [
          {value: statistics.excellentCount, name: "优秀"},
          {value: statistics.failCount, name: "不及格"},
          {value: statistics.totalCount - statistics.excellentCount - statistics.failCount, name: "其他"},
        ],
        emphasis: {
          itemStyle: {shadowBlur: 10, shadowOffsetX: 0, shadowColor: "rgba(0, 0, 0, 0.5)"},
        },
      },
    ],
  };

  // **📊 各维度成绩条形图配置**
  const barOptions = useMemo(() => {
    return {
      title: {text: "各分数维度分析", left: "center"},
      tooltip: {trigger: "axis"},
      xAxis: {type: "category", data: ["总分", "平时分", "期末分"]},
      yAxis: {type: "value"},
      series: [
        {
          name: "平均分",
          type: "bar",
          data: [statistics.avgTotalScore, statistics.avgUsualScore, statistics.avgFinalScore],
          itemStyle: {color: "#ff7875"},
        },
      ],
    }
  }, [gradeItems])
  const histogramOptions = useMemo(() => {
    if (!subjectInfo) return {}; // 避免 subjectInfo 为空时报错
    console.log(gradeItems);
    // 1. 统计不同成绩区间内的学生人数
    const {gradeMax, gradeExcellent, gradeMin, gradeFail} = subjectInfo;

    // 2. 确保 key 使用正确的变量值
    const excellentLevelText = `${gradeExcellent}-${gradeMax}`;
    const mediumLevelText = `${gradeExcellent}-${gradeFail}`;
    const failLevelText = `${gradeFail}-${gradeMin}`;

    const scoreRanges = {
      [excellentLevelText]: 0,
      [mediumLevelText]: 0,
      [failLevelText]: 0,
    };

    // 3. 遍历成绩数据，分类统计
    gradeItems.forEach((item) => {
      const totalGrade = item?.gradeItem?.totalGrade || 0;
      if (totalGrade >= gradeExcellent) {
        scoreRanges[excellentLevelText]++;
      } else if (totalGrade >= gradeFail) {
        scoreRanges[mediumLevelText]++;
      } else {
        scoreRanges[failLevelText]++;
      }
    });

    // 4. 生成 ECharts 配置
    return {
      title: {text: "成绩区间分布", left: "center"},
      tooltip: {trigger: "axis"},
      xAxis: {
        type: "category",
        data: Object.keys(scoreRanges) // 正确使用区间文本
      },
      yAxis: {type: "value"},
      series: [
        {
          name: "学生人数",
          type: "bar",
          data: Object.values(scoreRanges), // 正确映射学生人数
          itemStyle: {color: "#5470c6"},
        }
      ],
    };
  }, [gradeItems, subjectInfo]);

  // **📈 条形图 + 折线图**
  const barAndLineOptions = useMemo(() => {
    return {
      title: {text: "总分趋势 + 最高/最低分", left: "center"},
      tooltip: {trigger: "axis"},
      legend: {data: ["总分", "最高分", "最低分"], bottom: 0},
      xAxis: {type: "category", data: gradeItems.map((item) => item.studentInfo.stuName)},
      yAxis: {type: "value"},
      series: [
        {
          name: "总分",
          type: "bar",
          data: gradeItems.map((item) => item?.gradeItem?.totalGrade || 0),
          itemStyle: {color: "#ff7875"},
        },
        {
          name: "最高分",
          type: "line",
          data: gradeItems.map(() => statistics.maxTotalScore),
          lineStyle: {color: "green", width: 2},
        },
        {
          name: "最低分",
          type: "line",
          data: gradeItems.map(() => statistics.minTotalScore),
          lineStyle: {color: "red", width: 2},
        }
      ],
    };
  }, [gradeItems, statistics]);

  /**
   * 成绩趋势雷达图
   * 用途：比较不同学生在 总分、平时分、期末分 之间的相对得分情况
   * 特点：雷达图可以清晰地显示各个维度的对比
   */
  const radarOptions = useMemo(() => {
    return {
      title: {text: "成绩趋势雷达图", left: "center"},
      tooltip: {},
      radar: {
        indicator: [
          {name: "总分", max: statistics.maxTotalScore},
          {name: "平时分", max: statistics.maxUsualScore},
          {name: "期末分", max: statistics.maxFinalScore},
        ],
      },
      series: [
        {
          name: "成绩对比",
          type: "radar",
          data: gradeItems.map((item) => ({
            value: [item?.gradeItem?.totalGrade || 0, item?.gradeItem?.usualGrade || 0, item?.gradeItem?.finalGrade || 0],
            name: item?.studentInfo?.stuName,
          })),
        },
      ],
    };
  }, [gradeItems, statistics]);



  /**
   * 成绩分布箱线图
   * 用途：分析成绩的分布范围、**异常值（离群点）**等
   * 特点：适用于分析成绩的偏态分布
   */
  const boxplotOptions = useMemo(() => {
    if (!gradeItems || gradeItems.length === 0) return {};

    // 1. 提取所有学生的总成绩
    const scores = gradeItems.map((item) => item?.gradeItem?.totalGrade || 0);

    // 2. 确保数据足够，否则箱线图无法渲染
    if (scores.length < 5) {
      return {
        title: { text: "成绩分布箱线图", left: "center" },
        tooltip: { trigger: "item" },
        xAxis: { type: "category", data: ["总分"] },
        yAxis: { type: "value" },
        series: [{ name: "总分", type: "scatter", data: scores }], // 如果数据不足，显示散点图
      };
    }

    // 3. 使用 ECharts 内置方法计算箱线图数据
    const boxData = prepareBoxplotData([scores]);

    return {
      title: { text: "成绩分布箱线图", left: "center" },
      tooltip: { trigger: "item" },
      xAxis: { type: "category", data: ["总分"] },
      yAxis: { type: "value" },
      series: [
        {
          name: "总分",
          type: "boxplot",
          data: boxData.boxData, // 处理后的箱线图数据
        },
        {
          name: "异常值",
          type: "scatter",
          data: boxData.outliers, // 异常值（离群点）
        },
      ],
    };
  }, [gradeItems]);

  return (
    <div>
      {/* 📊 第一行：统计信息 */}
      <Row gutter={16} justify="center" style={{marginBottom: 20}}>
        <Col span={24}>
          <Card title="统计信息" style={{minHeight: 450, textAlign: "center"}}>
            <Row gutter={16}>
              <Col span={8}>
                <Statistic title="优秀人数" value={statistics.excellentCount}/>
              </Col>
              <Col span={8}>
                <Statistic title="不及格人数" value={statistics.failCount}/>
              </Col>
              <Col span={8}>
                <Statistic title="总人数" value={statistics.totalCount}/>
              </Col>
            </Row>
            <Row gutter={16} style={{marginTop: 20}}>
              <Col span={8}>
                <Statistic title="总分平均分" value={statistics.avgTotalScore.toFixed(2)}/>
              </Col>
              <Col span={8}>
                <Statistic title="平时分平均分" value={statistics.avgUsualScore.toFixed(2)}/>
              </Col>
              <Col span={8}>
                <Statistic title="期末分平均分" value={statistics.avgFinalScore.toFixed(2)}/>
              </Col>
            </Row>
            <Row gutter={16} style={{marginTop: 20}}>
              <Col span={8}>
                <Statistic title="最高总分" value={statistics.maxTotalScore}/>
              </Col>
              <Col span={8}>
                <Statistic title="最低总分" value={statistics.minTotalScore}/>
              </Col>
              <Col span={8}>
                <Statistic title="最高平时分" value={statistics.maxUsualScore}/>
              </Col>
            </Row>
            <Row gutter={16} style={{marginTop: 20}}>
              <Col span={8}>
                <Statistic title="最低平时分" value={statistics.minUsualScore}/>
              </Col>
              <Col span={8}>
                <Statistic title="最高期末分" value={statistics.maxFinalScore}/>
              </Col>
              <Col span={8}>
                <Statistic title="最低期末分" value={statistics.minFinalScore}/>
              </Col>
            </Row>
          </Card>
        </Col>
      </Row>

      {/* 📊 第二行：可视化 */}
      <Row gutter={16} justify="center" style={{marginBottom: 20}}>
        <Col span={8}>
          <Card title="成绩柱状图" style={{minHeight: 300}}>
            <EChartsReact option={histogramOptions}/>
          </Card>
        </Col>

        <Col span={8}>
          <Card title="成绩分布" style={{minHeight: 300}}>
            <EChartsReact option={pieOptions}/>
          </Card>
        </Col>

        <Col span={8}>
          <Card title="各分数维度分析" style={{minHeight: 300}}>
            <EChartsReact option={barOptions}/>
          </Card>
        </Col>

        <Col span={8}>
          <Card title="总分趋势 + 最高/最低分" style={{minHeight: 300}}>
            <EChartsReact option={barAndLineOptions}/>
          </Card>
        </Col>
        <Col span={8}>
          <Card title="成绩趋势雷达图">
            <EChartsReact option={radarOptions}/>
          </Card>
        </Col>
        <Col span={8}>
          <Card title="成绩分布箱线图">
            <EChartsReact option={boxplotOptions}/>
          </Card>
        </Col>
      </Row>
    </div>
  );
};

export default Visualization;
