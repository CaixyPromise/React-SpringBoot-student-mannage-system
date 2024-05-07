import {EChartsOption} from "echarts";

const StudentAnalysisOption = (data: API.StudentAnalysisVO): EChartsOption =>
{
    // 提取数据源
    const subjects = data.subjectAnalysis || [];
    const gradeItems = data.studentGrade?.gradeItem || [];

    // 构建ECharts数据表格
    const datasetSource: any[][] = [
        [ 'Subject', 'Student Score', 'Average Score', 'Highest Score' ]
    ];

    subjects.forEach(subject =>
    {
        const studentScore = gradeItems.find(item => item.subjectId === subject.subjectId)?.grade || 0;
        datasetSource.push([
            subject.subjectName,
            studentScore,
            subject.averageScore,
            subject.highestScore
        ]);
    });

    return {
        legend: {},
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow'
            },
            formatter: (params: any) =>
            {
                const subjectInfo = subjects.find(sub => sub.subjectName === params[0].name);
                if (subjectInfo)
                {
                    return `
                        ${subjectInfo.subjectName}<br/>
                        学生分数: ${params[0].value}<br/>
                        平均分数: ${params[1].value}<br/>
                        最高分数: ${params[2].value}<br/>
                        最高分学生: ${subjectInfo.highestScoreStudentName?.stuName}
                    `;
                }
                return '';
            }
        },
        dataset: {
            source: datasetSource
        },
        xAxis: { type: 'category' },
        yAxis: {},
        series: [
            { type: 'bar', name: '学生成绩', barGap: '5%', barCategoryGap: '20%' },
            { type: 'bar', name: '全校平均分', barGap: '5%', barCategoryGap: '20%' },
            { type: 'bar', name: '全校最高分', barGap: '5%', barCategoryGap: '20%' }
        ]
    };
}

export {
    StudentAnalysisOption
}
