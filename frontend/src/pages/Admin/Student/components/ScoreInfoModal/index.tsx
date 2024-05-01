import {Button, Card, Descriptions, Form, Input, Modal, Select, Space} from "antd";
import React from "react";
import {CloseOutlined} from "@ant-design/icons";

interface Props {
    visible: boolean;
    setVisible: (visible: boolean) => void;
    id: string;
    stuName: string
    stuSex: string
    stuDept: string
    stuMajor: string
    stuClass: string
    subjectItem: OptionProps[]
    scoreItems : {
        id : string;
        name: string;
        total: string;
    }
}


const Index:React.FC<Props> = (props: Props) =>
{
    const {visible, setVisible, id,subjectItem, stuName, stuSex, stuDept, stuMajor, stuClass, scoreItems} = props;
    const [form] = Form.useForm();

    return <>
        <Modal
            open={visible}
            onCancel={() => setVisible(false)}
        >
        <Card
            bordered={false}
            style={{
                marginBottom: "8px",
            }}
        >
            <Descriptions title="学生信息" column={2}>
                <Descriptions.Item label={"学生ID"} span={2}>{id}</Descriptions.Item>
                <Descriptions.Item label={"姓名"}>{stuName}</Descriptions.Item>
                <Descriptions.Item label={"性别"}>{stuSex}</Descriptions.Item>
                <Descriptions.Item label={"学院"}>{stuDept}</Descriptions.Item>
                <Descriptions.Item label={"专业"}>{stuMajor}</Descriptions.Item>
                <Descriptions.Item label={"班级"}>{stuClass}</Descriptions.Item>
            </Descriptions>
        </Card>
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
                                title={`Item ${field.name + 1}`}
                                key={field.key}
                                extra={
                                    <CloseOutlined
                                        onClick={() => {
                                            remove(field.name);
                                        }}
                                    />
                                }
                            >
                                <Form.Item label="Name" name={[field.name, 'name']}>
                                    <Input />
                                </Form.Item>

                                <Form.Item label="List">
                                    <Form.List name={[field.name, 'list']}>
                                        {(subFields, subOpt) => (
                                            <div style={{ display: 'flex', flexDirection: 'column', rowGap: 16 }}>
                                                {subFields.map((subField) => (
                                                    <Space key={subField.key}>
                                                        <Form.Item noStyle name={[subField.name, 'first']}>
                                                            <Select options={subjectItem} />
                                                        </Form.Item>
                                                        <Form.Item noStyle name={[subField.name, 'second']}>
                                                            <Input placeholder="second" />
                                                        </Form.Item>
                                                        <CloseOutlined
                                                            onClick={() => {
                                                                subOpt.remove(subField.name);
                                                            }}
                                                        />
                                                    </Space>
                                                ))}
                                                {/*<Button type="dashed" onClick={() => subOpt.add()} block>*/}
                                                {/*    + Add Sub Item*/}
                                                {/*</Button>*/}
                                            </div>
                                        )}
                                    </Form.List>
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
    </Modal>
    </>
}

export default Index;
