import {Button, Card, Descriptions, Form, FormListFieldData, InputNumber, message, Modal, Select, Spin} from "antd";
import React, {useEffect, useState} from "react";
import {CloseOutlined, PlusOutlined} from "@ant-design/icons";
import {
    batchAddStudentGradesUsingPOST,
    deleteStudentGradesUsingPOST,
    updateStudentGradesUsingPOST
} from "@/services/backend/scoreController";
import {fetchStudentInfo} from "@/pages/Admin/Student/server";
import StudentSex from "@/pages/Admin/Student/components/StudentSex";
import {ProTable} from "@ant-design/pro-components";
import {ScoreColumn} from "@/pages/Admin/Student/Columns/columns";
import {validateScore} from "@/pages/Admin/Student/components/AddScoreInfoModal/utils";


const Index: React.FC<Student.AddScoreModalProps> = (props: Student.AddScoreModalProps) =>
{
    const { scoreModalVisible, setScoreModalVisible, currentRow, subjectItem } = props;
    const [ form ] = Form.useForm();
    const [ loading, setLoading ] = useState<boolean>(false);
    const [ studentData, setStudentData ] = useState<API.StudentGradesVO>({});
    const [ currentSubject, setCurrentSubject ] = useState<Student.SelectSubjectItem>({});
    const [ selectedSubjects, setSelectedSubjects ] = useState(new Set());

    // 科目id -> 实体映射
    const optionMapById: {
        [key: string]: OptionProps
    } = subjectItem ? subjectItem.reduce((acc, cur) =>
    {   // @ts-ignore
        acc[cur.value] = cur;
        return acc;
    }, {}) : {};

    const handleSubjectChange = (value: string, option: OptionProps, field: FormListFieldData) =>
    {
        // @ts-ignore
        const oldSubject = currentSubject[field.name as number]?.value
        // 创建新的选中科目集合以避免直接修改状态
        const newSelectedSubjects = new Set(selectedSubjects);
        if (oldSubject)
        {
            newSelectedSubjects.delete(oldSubject);
        }
        newSelectedSubjects.add(value);

        setSelectedSubjects(newSelectedSubjects);

        // 更新表单项
        setCurrentSubject({
            ...currentSubject,
            [field.name]: option
        });
    };

    const handleRemoveField = (fieldIndex: number) =>
    {
        const removedSubject = currentSubject[fieldIndex]
        console.log(removedSubject)
        if (removedSubject)
        {
            const newSelectedSubjects = new Set(selectedSubjects);
            newSelectedSubjects.delete(removedSubject);
            setSelectedSubjects(newSelectedSubjects);
        }
    };


    useEffect(() =>
    {
        if (scoreModalVisible && currentRow && currentRow.id)
        {
            fetchStudentInfo(currentRow, setScoreModalVisible, setLoading, setStudentData, () => {});
        }
    }, [ scoreModalVisible, currentRow ]);

    const handleSubmit = async () =>
    {
        const values = await form.validateFields();
        let isValid = true;
        if (values.items.length === 1 && !values.items[0].subjectId)
        {
            setScoreModalVisible(false);
            return;
        }
        for (const item of values.items)
        {
            const subjectId = item.subjectId;
            if (subjectId)
            {
                isValid = await validateScore(item.score, subjectId, optionMapById);
                if (!isValid)
                {
                    return;
                }
            }
        }
        try
        {
            const formData = values.items?.map((item: { subjectId: any; score: any; }) =>
            {
                const { subjectId, score } = item;
                return {
                    subjectId,
                    score,
                    studentId: studentData.studentInfo?.id
                }
            })
            console.log("formData is: ", formData)
            const { data, code } = await batchAddStudentGradesUsingPOST(formData);
            if (code === 0 && data)
            {
                message.success("成绩添加成功");
                setScoreModalVisible(false);
            }
        }
        catch (error: any)
        {
            message.error(error.message);
        }
    }


    return <>
        <Modal
            title="学生成绩信息表"
            open={scoreModalVisible}
            onCancel={() => setScoreModalVisible(false)}
            onOk={handleSubmit}
            width={1300}

        >
            <Spin spinning={loading} tip={"正在加载学生信息"} delay={300}>
                <Descriptions title="学生信息" column={2} bordered={true}>
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
                    bordered={false}
                    style={{
                        marginBottom: "8px",
                    }}
                    title="添加成绩"
                >
                    <Form
                        labelCol={{ span: 6 }}
                        wrapperCol={{ span: 18 }}
                        form={form}
                        name="dynamic_form_complex"
                        autoComplete="off"
                        initialValues={{ items: [ {} ] }}
                    >
                        <Form.List name="items">
                            {(fields, { add, remove }) =>
                            {
                                return (
                                    <div style={{ display: 'flex', rowGap: 16, flexDirection: 'column' }}>
                                        {fields.map((field) => (
                                            <Card
                                                size="small"
                                                title={`成绩 ${field.name + 1}`}
                                                key={field.key}
                                                extra={
                                                    <CloseOutlined onClick={() =>
                                                    {
                                                        remove(field.name);
                                                        handleRemoveField(field.name);
                                                    }}/>

                                                }
                                            >
                                                <Form.Item label="科目" name={[ field.name, 'subjectId' ]}>
                                                    <Select
                                                        options={subjectItem.map(item => ({
                                                            ...item,
                                                            label: item.label,
                                                            value: item.value,
                                                            disabled: selectedSubjects.has(item.value)
                                                        }))}
                                                        onChange={(value, option) => handleSubjectChange(value,
                                                            option as OptionProps,
                                                            field)}
                                                    />
                                                </Form.Item>

                                                <Form.Item
                                                    label={"成绩"} name={[ field.name, 'score' ]}
                                                >
                                                    <div style={{
                                                        display: "flex",
                                                        alignItems: "center"
                                                    }}>
                                                        <InputNumber
                                                            max={currentSubject[field.name]?.gradeMax ?? Number.MAX_VALUE}
                                                            min={currentSubject[field.name]?.gradeMin ?? 0}
                                                        />
                                                        <div style={{
                                                            marginLeft: "8px"
                                                        }}>
                                                            <div>
                                                                <span>最大：{currentSubject[field.name]?.gradeMax ?? 1000}</span>
                                                            </div>
                                                            <span>最小：{currentSubject[field.name]?.gradeMin ?? 0}</span>
                                                        </div>
                                                    </div>
                                                </Form.Item>
                                            </Card>
                                        ))}

                                        <Button type="dashed" onClick={() => add()} block icon={<PlusOutlined/>}>
                                            添加成绩
                                        </Button>
                                    </div>
                                )
                            }}
                        </Form.List>
                    </Form>
                </Card>
            </Spin>
        </Modal>
    </>
}

export default Index;
