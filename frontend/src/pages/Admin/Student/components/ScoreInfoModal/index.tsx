import {Button, Card, Descriptions, Form, Input, InputNumber, message, Modal, Select, Space, Spin} from "antd";
import React, {useEffect, useState} from "react";
import {CloseOutlined} from "@ant-design/icons";
import {getStudentGradesVoByStuIdUsingGET} from "@/services/backend/scoreController";
import StudentSex from "@/pages/Admin/Student/utils";



const Index:React.FC<Student.ScoreModalProps> = (props: Student.ScoreModalProps) =>
{
    const {scoreModalVisible, setScoreModalVisible, currentRow,subjectItem} = props;
    const [form] = Form.useForm();
    const [loading, setLoading] = useState<boolean>(false);
    const [studentData, setStudentData] = useState<API.StudentGradesVO>({});
    const [currentSubject, setCurrentSubject] = useState<API.SubjectsVO>({});
    const fetchStudentInfo = async () =>
    {
        console.log("fetchStudentInfo.currentRow: ", currentRow)
        if (currentRow === undefined)
        {
            // message.error("学生信息不能为空");
            setScoreModalVisible(false);
            return;
        }
        const {id} = currentRow
        if (id === null || id === undefined || id === "")
        {
            message.error("学生id不能为空");
            setScoreModalVisible(false);
            return;
        }
        setLoading(true);
        try {
            const {data, code} = await getStudentGradesVoByStuIdUsingGET({stuId: id})
            if (code === 0 && data)
            {
                setStudentData(data);
                setLoading(false);
                form.setFieldsValue({
                    items: data.gradeItem.map((item) => ({
                        name: item.subjectName,
                        list: [
                            {first: item.subjectName, second: item.grade},
                        ],
                    })),
                });
            }
        }
        catch (e: any)
        {
            message.error(e.message);
            setLoading(false);
            setScoreModalVisible(false);
        }
    }

    useEffect(() => {
        // Ensure the modal is visible and currentRow is defined with a valid ID before fetching data
        if (scoreModalVisible && currentRow && currentRow.id) {
            fetchStudentInfo();
        }
    }, [scoreModalVisible, currentRow]); // React to changes in visibility and the current row

    return <>
        <Modal
            title="学生成绩信息表"
            open={scoreModalVisible}
            onCancel={() => setScoreModalVisible(false)}
            width={800}
        >
        <Spin spinning={loading} tip={"正在加载学生信息"} delay={300}>
        <Descriptions title="学生信息" column={2} bordered={true}>
            <Descriptions.Item label={"学生ID"} span={2}><a>{studentData.studentInfo?.id}</a></Descriptions.Item>
            <Descriptions.Item label={"姓名"}>{studentData.studentInfo?.stuName}</Descriptions.Item>
            <Descriptions.Item label={"性别"}><StudentSex useSex={studentData.studentInfo?.stuSex} /></Descriptions.Item>
            <Descriptions.Item label={"学院"}>{studentData.studentInfo?.stuDepart}</Descriptions.Item>
            <Descriptions.Item label={"专业"}>{studentData.studentInfo?.stuMajor}</Descriptions.Item>
            <Descriptions.Item label={"班级"}>{studentData.studentInfo?.stuClass}</Descriptions.Item>
        </Descriptions>
        <Card
            bordered={false}
            style={{
                marginBottom: "8px",
            }}
        >

                <Form
                    labelCol={{ span: 6 }}
                    wrapperCol={{ span: 18 }}
                    form={form}
                    name="dynamic_form_complex"
                    style={{ maxWidth: 600 }}
                    autoComplete="off"
                    initialValues={{ items: [{}] }}
                >
                    <Form.List name="items">
                        {(fields, { add, remove }) => (
                            <div style={{ display: 'flex', rowGap: 16, flexDirection: 'column' }}>
                                {fields.map((field) => (
                                    <Card
                                        size="small"
                                        title={`成绩 ${field.name + 1}`}
                                        key={field.key}
                                        extra={
                                            <CloseOutlined
                                                onClick={() => {
                                                    remove(field.name);
                                                }}
                                            />
                                        }
                                    >
                                        <Form.Item label="科目" name={[field.name, 'name']}>
                                            <Select options={subjectItem} onChange={(value) => setCurrentSubject(value)} />
                                        </Form.Item>

                                        <Form.Item label={"成绩"} name={[field.name, 'score']}>
                                            <div>
                                                <InputNumber
                                                    max={currentSubject.gradeMax ?? Number.MAX_VALUE}
                                                    min={currentSubject.gradeMin ?? 0}
                                                />
                                                {/*<span>最大：{currentSubject.gradeMax ?? Number.MAX_VALUE}</span>*/}
                                                {/*<span>最小：{}</span>*/}

                                            </div>
                                        </Form.Item>
                                    </Card>
                                ))}

                                <Button type="dashed" onClick={() => add()} block>
                                    + 添加成绩
                                </Button>
                            </div>
                        )}
                    </Form.List>
                </Form>
        </Card>
        </Spin>
    </Modal>
    </>
}

export default Index;
