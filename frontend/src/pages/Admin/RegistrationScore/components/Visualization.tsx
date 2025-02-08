import React, {useEffect, useMemo, useState} from "react";
import {Row, Col, Card, Statistic, Tooltip} from "antd";
import {Column, Pie, Bar} from "@ant-design/charts";

const getStatistics = (grades: API.StudentsGradeForAdminVO[]) => {
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

    // Calculate max and min
    maxTotalScore = Math.max(maxTotalScore, total);
    minTotalScore = Math.min(minTotalScore, total);

    maxUsualScore = Math.max(maxUsualScore, usual);
    minUsualScore = Math.min(minUsualScore, usual);

    maxFinalScore = Math.max(maxFinalScore, final);
    minFinalScore = Math.min(minFinalScore, final);

    if (total >= grade?.gradeItem?.gradeExcellent) {
      excellentCount++;
    }
    if (total < grade?.gradeItem?.gradeFail) {
      failCount++;
    }
  });

  const totalCount = grades.length;
  const result = {
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
  console.log(result)
  return result;
};

const Visualization: React.FC<{
  gradeItems: API.StudentsGradeForAdminVO[];
}> = ({gradeItems}) => {
  const statistics = useMemo(() => getStatistics(gradeItems), [gradeItems]);

  // 设置柱状图数据
  const columnData = gradeItems.map((item) => ({
    name: item?.studentInfo?.stuName,
    usualGrade: item?.gradeItem?.usualGrade,
    finalGrade: item?.gradeItem?.finalGrade,
    totalGrade: item?.gradeItem?.totalGrade,
    maxTotalGrade: Math.max(item?.gradeItem?.totalGrade || 0, statistics.maxTotalScore),
    minTotalGrade: Math.min(item?.gradeItem?.totalGrade || 0, statistics.minTotalScore),
    maxUsualGrade: Math.max(item?.gradeItem?.usualGrade || 0, statistics.maxUsualScore),
    minUsualGrade: Math.min(item?.gradeItem?.usualGrade || 0, statistics.minUsualScore),
    maxFinalGrade: Math.max(item?.gradeItem?.finalGrade || 0, statistics.maxFinalScore),
    minFinalGrade: Math.min(item?.gradeItem?.finalGrade || 0, statistics.minFinalScore),
  }));

  // 设置饼图数据
  const pieData = [
    {type: "优秀", value: statistics.excellentCount},
    {type: "不及格", value: statistics.failCount},
    {type: "其他", value: statistics.totalCount - statistics.excellentCount - statistics.failCount},
  ];

  // 设置条形图数据（总分、平时分、期末分）
  const barData = [
    {type: "总分", value: statistics.avgTotalScore},
    {type: "平时分", value: statistics.avgUsualScore},
    {type: "期末分", value: statistics.avgFinalScore},
  ];

  return (
    <div>
      {/* 第一行：统计信息 */}
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
                <Statistic title="最大总分" value={statistics.maxTotalScore}/>
              </Col>
              <Col span={8}>
                <Statistic title="最小总分" value={statistics.minTotalScore}/>
              </Col>
              <Col span={8}>
                <Statistic title="最大平时分" value={statistics.maxUsualScore}/>
              </Col>
            </Row>
            <Row gutter={16} style={{marginTop: 20}}>
              <Col span={8}>
                <Statistic title="最小平时分" value={statistics.minUsualScore}/>
              </Col>
              <Col span={8}>
                <Statistic title="最大期末分" value={statistics.maxFinalScore}/>
              </Col>
              <Col span={8}>
                <Statistic title="最小期末分" value={statistics.minFinalScore}/>
              </Col>
            </Row>
          </Card>
        </Col>
      </Row>

      {/* 第二行：成绩柱状图、成绩分布饼图和各分数维度分析 */}
      <Row gutter={16} justify="center" style={{marginBottom: 20}}>
        <Col span={8}>
          <Card title="成绩柱状图" style={{minHeight: 300}}>
            <Column
              data={columnData}
              xField="name"
              yField="totalGrade"
              seriesField="name"
              label={{visible: true, position: "middle"}}
              meta={{
                totalGrade: {alias: "总分"},
                usualGrade: {alias: "平时成绩"},
                finalGrade: {alias: "期末成绩"},
              }}
              tooltip={{
                customContent: (title, data) => {
                  const studentName = data[0]?.data?.name;
                  const totalGrade = data[0]?.data?.totalGrade;
                  const usualGrade = data[0]?.data?.usualGrade;
                  const finalGrade = data[0]?.data?.finalGrade;
                  const tooltipContent = (
                    <div>
                      <div>学生: {studentName}</div>
                      <div>总分: {totalGrade}</div>
                      <div>平时成绩: {usualGrade}</div>
                      <div>期末成绩: {finalGrade}</div>
                    </div>
                  );
                  return tooltipContent;
                },
              }}
            />
          </Card>
        </Col>

        <Col span={8}>
          <Card title="成绩分布" style={{minHeight: 300}}>
            <Pie
              data={pieData}
              angleField="value"
              colorField="type"
              radius={0.8}
              label={{
                type: "outer",
                content: "{name}: {percentage}",
              }}
              interactions={[
                {
                  type: "element-active",
                },
              ]}
            />
          </Card>
        </Col>

        <Col span={8}>
          <Card title="各分数维度分析" style={{minHeight: 300}}>
            <Bar
              data={barData}
              xField="type"
              yField="value"
              seriesField="type"
              label={{
                visible: true,
                position: "top",
                content: (data) => `${data.value.toFixed(2)}`,
              }}
              meta={{
                value: {alias: "分数"},
              }}
            />
          </Card>
        </Col>
      </Row>
    </div>
  );
};

export default Visualization;
