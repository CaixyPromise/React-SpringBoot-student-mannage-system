import {EChartsOption} from "echarts";


const getAllSubjectAnalysis = (data: API.SubjectAnalysis[]): EChartsOption =>
{
    const xAxisData = data.map(item => item.subjectName);
    const averageScores = data.map(item => item.averageScore?.toFixed());
    const highestScores = data.map(item => item.highestScore);
    const excellentScores = data.map(item => item.excellentCount);
    const excellentRates = data.map(item => item.excellentRate?.toFixed());
    const failedScores = data.map(item => item.failureCount);
    const failureRates = data.map(item => item.failureRate?.toFixed());
    return {
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow'
            },
            formatter: function (params)
            {
                const dataIndex = params[0].dataIndex;
                return `${xAxisData[dataIndex]}<br/>
                        平均分: ${averageScores[dataIndex]}<br/>
                        最高分: ${highestScores[dataIndex]}<br/>
                        优秀上线人数: ${excellentScores[dataIndex]}<br/>
                        优秀率: ${excellentRates[dataIndex]}%<br/>
                        不及格人数: ${failedScores[dataIndex]}<br/>
                        不及格率: ${failureRates[dataIndex]}%<br/>
                        最高分学生: ${data[dataIndex].highestScoreStudentName.stuName}`;
            }
        },
        legend: {
            data: [ '平均分', '最高分' ]
        },
        xAxis: {
            type: 'category',
            data: xAxisData
        },
        yAxis: {
            type: 'value'
        },
        series: [
            {
                name: '平均分',
                type: 'bar',
                stack: 'total',
                data: averageScores,

                label: {
                    show: true,
                    position: 'inside',
                    formatter: '{c}'
                }
            },
            {
                name: '最高分',
                type: 'bar',
                stack: 'total',
                data: highestScores,
                label: {
                    show: true,
                    position: 'inside',
                    formatter: '{c}'
                }
            }
        ]
    };
};

const convertToPolarOption = (data: API.SubjectAnalysis[]): EChartsOption => {
    const subjects = data.map(item => item.subjectName);
    const averageScores = data.map(item => item.averageScore);
    const highestScores = data.map(item => item.highestScore);

    return {
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow'
            },
            formatter: (params) =>
            {
                const subject = data.find(item => item.subjectName === params[0].axisValue);
                return `
          ${params[0].axisValue}<br/>
          平均分: ${(params[0].data)?.toFixed()}<br/>
          最高分: ${params[1].data}<br/>
          最高分学生: ${subject?.highestScoreStudentName.stuName} (${subject?.highestScoreStudentName.stuMajor})
        `;
            }
        },
        angleAxis: {
            type: 'category',
            data: subjects,
        },
        radiusAxis: {},
        polar: {},
        series: [
            {
                type: 'bar',
                data: averageScores,
                coordinateSystem: 'polar',
                name: '平均分',
                stack: 'score'
            },
            {
                type: 'bar',
                data: highestScores,
                coordinateSystem: 'polar',
                name: '最高分',
                stack: 'score'
            }
        ],
        legend: {
            show: true,
            data: [ '平均分', '最高分' ]
        }
    };
};



export {
    getAllSubjectAnalysis,
    convertToPolarOption
}
