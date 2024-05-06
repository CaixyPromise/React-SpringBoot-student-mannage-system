import React, {useEffect, useState} from "react";
import {Card, Descriptions, message, Modal, Spin} from "antd";
import {fetchStudentGradesAnalyses, fetchStudentInfo} from "@/pages/Admin/Student/server";
import StudentSex from "@/pages/Admin/Student/components/StudentSex";
import {ProTable} from "@ant-design/pro-components";
import {ScoreColumn} from "@/pages/Admin/Student/Columns/columns";
import {validateScore} from "@/pages/Admin/Student/components/AddScoreInfoModal/utils";
import {deleteStudentGradesUsingPOST, updateStudentGradesUsingPOST} from "@/services/backend/scoreController";
import EChartsReact from "echarts-for-react";
import {StudentAnalysisOption} from "@/pages/Admin/Student/components/ScoreInfoModal/option";

const Index: React.FC<Student.ScoreModalProps> = (props: Student.ScoreModalProps) =>
{
    const { scoreInfoModalVisible, setScoreInfoModalVisible, subjectItem, currentRow } = props;
    const [ loading, setLoading ] = useState<boolean>(false);
    const [ studentData, setStudentData ] = useState<API.StudentGradesVO>({});
    const [ gradeItem, setGradeItem ] = useState<API.GradeItem[]>([])
    const [ analysesLoading, setAnalysesLoading ] = useState<boolean>(false);
    const [ analysesData, setAnalysesData ] = useState<API.StudentAnalysisVO>({})
    const [ echartOption, setEchartOption] = useState<any>({});


    // 科目id -> 实体映射
    const optionMapById: {
        [key: string]: OptionProps
    } = subjectItem ? subjectItem.reduce((acc, cur) =>
    {   // @ts-ignore
        acc[cur.value] = cur;
        return acc;
    }, {}) : {};
    useEffect(() =>
    {
        const loadData = async () =>
        {
            if (scoreInfoModalVisible && currentRow && currentRow.id)
            {
                await fetchStudentGradesAnalyses(currentRow, setAnalysesLoading, setAnalysesData)
                await fetchStudentInfo(currentRow, setScoreInfoModalVisible, setLoading, setStudentData, setGradeItem);
            }
        }
        loadData().then(() => {
            if (analysesData && !analysesLoading)
            {
                setEchartOption(StudentAnalysisOption(analysesData))
            }
        });
    }, [ scoreInfoModalVisible, currentRow ]);

    const handleUpdateScore = async (rowKey: any, record: API.GradeItem) =>
    {
        // 校验成绩
        // @ts-ignore
        const isValid = await validateScore(record.grade, rowKey, optionMapById);
        if (!isValid)
        {
            return;
        }
        // 更新成绩
        try
        {
            const { data, code } = await updateStudentGradesUsingPOST({
                id: rowKey,
                grade: record.grade,
            })
            if (code === 0)
            {
                message.success("成绩更新成功");
                // 重新加载一次数据
                fetchStudentInfo(currentRow, setScoreInfoModalVisible, setLoading, setStudentData, setGradeItem);
            }
        }
        catch (error: any)
        {
            message.error(error.message);
        }
    };

    const handleDeleteScore = async (id: any, row?: API.GradeItem) =>
    {
        try
        {
            if (id == null)
            {
                return
            }
            const { code } = await deleteStudentGradesUsingPOST({ id })
            if (code === 0)
            {
                message.success("成绩删除成功");
                // 重新加载一次数据
                fetchStudentInfo(currentRow, setScoreInfoModalVisible, setLoading, setStudentData, setGradeItem);
            }
        }
        catch (error: any)
        {
            message.error(error.message);
        }
    }




    return <>
        <Modal
            open={scoreInfoModalVisible}
            title={"学生信息"}
            onCancel={() => setScoreInfoModalVisible(false)}
            footer={null}
            width={1300}
        >
            <Spin spinning={loading && analysesLoading} tip={"正在加载学生信息"} delay={300}>
                <Descriptions column={2} bordered={true}>
                    <Descriptions.Item label={"学生ID"}
                                       span={2}><a>{studentData.studentInfo?.id}</a></Descriptions.Item>
                    <Descriptions.Item label={"班级"} span={2}>{studentData.studentInfo?.stuClass}</Descriptions.Item>

                    <Descriptions.Item label={"姓名"}>{studentData.studentInfo?.stuName}</Descriptions.Item>
                    <Descriptions.Item label={"性别"}><StudentSex
                        useSex={studentData.studentInfo?.stuSex}/></Descriptions.Item>
                    <Descriptions.Item label={"学院"}>{studentData.studentInfo?.stuDepart}</Descriptions.Item>
                    <Descriptions.Item label={"专业"}>{studentData.studentInfo?.stuMajor}</Descriptions.Item>
                </Descriptions>

                <Card
                    title={"学生成绩分析"}
                >
                    <EChartsReact option={echartOption} />
                </Card>
                <Card
                    // bordered={false}
                    style={{
                        marginTop: "8px",
                        marginBottom: "8px",
                    }}
                    title="学生成绩"
                >
                    <ProTable
                        rowKey={"gradeId"}
                        columns={ScoreColumn(handleUpdateScore, handleDeleteScore)}
                        dataSource={gradeItem}
                        editable={{
                            type: 'single',  // 可选单行编辑模式
                            onSave: handleUpdateScore,
                            onDelete: handleDeleteScore,
                        }}
                        pagination={false}
                    />
                </Card>
            </Spin>
        </Modal>
    </>
}

export default Index;
