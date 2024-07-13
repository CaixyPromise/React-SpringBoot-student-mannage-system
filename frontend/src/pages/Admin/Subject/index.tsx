import {type ActionType, PageContainer, ProTable} from "@ant-design/pro-components";
import {SubjectColumns} from "@/pages/Admin/Subject/Columns/columns";
import React, {useRef, useState} from "react";
import {Button, Form, Input, InputNumber, message, Modal, Select, Space} from "antd";
import {
    addSubjectsUsingPOST,
    deleteSubjectsUsingPOST,
    listSubjectsByPageUsingPOST,
    updateSubjectsUsingPOST
} from "@/services/backend/subjectController";
import {PlusOutlined} from "@ant-design/icons";

const Index = () =>
{
    const { Option } = Select;
    const [ form ] = Form.useForm<Subject.SubjectFormItem>();
    // 是否显示新建窗口
    const [ createModalVisible, setCreateModalVisible ] = useState<boolean>(false);
    // 是否显示更新窗口
    const [ updateModalVisible, setUpdateModalVisible ] = useState<boolean>(false);
    const actionRef = useRef<ActionType>();
    // 当前用户点击的数据
    const [ currentRow, setCurrentRow ] = useState<Subject.CurrentRow>();
    const [ isPercentFail, setIsPercentFail ] = useState(true);
    const [ isPercentExcellent, setIsPercentExcellent ] = useState(true);

    const handleScoreTypeChange = (value: number, type: string) =>
    {
        let { gradeMax, gradeMin } = form.getFieldsValue();

        console.log(form.getFieldsValue())
        if (gradeMax === undefined || gradeMin === undefined)
        {
            gradeMax = 150;
            gradeMin = 0;
            message.warning('无法自动设置分数，请先设置对应成绩上下限；已默认设置为150和0');
        }
        const totalScore = gradeMax - gradeMin;
        const percentage = value / 100;

        const isPercentageSelected = value > 0;

        if (type === 'fail')
        {

            setIsPercentFail(isPercentageSelected);
            form.setFieldsValue({
                gradeFail: isPercentageSelected ? Number((percentage * totalScore).toFixed(2)) : undefined
            });
        }
        else
        {
            setIsPercentExcellent(isPercentageSelected)
            form.setFieldsValue({
                gradeExcellent: isPercentageSelected ? Number((percentage * totalScore).toFixed(2)) : undefined
            });
        }
    };


    const handleDelete = async (record: { id: any; }) =>
    {
        const { data, code } = await deleteSubjectsUsingPOST({
            id: record.id,
        })
        if (code === 0 && data)
        {
            message.success('删除成功，即将刷新')
            actionRef.current?.reloadAndRest?.()
        }
    }

    const validateScoresAndLines = async ({
        gradeMax,
        gradeMin,
        gradeExcellent,
        gradeFail
    }: {
        gradeMax?: number | string;
        gradeMin?: number | string;
        gradeExcellent?: number | string;
        gradeFail?: number | string;
    }): Promise<boolean> =>
    {
        // 尝试将所有输入转换为数字
        const max = Number(gradeMax);
        const min = Number(gradeMin);
        const excellent = Number(gradeExcellent);
        const fail = Number(gradeFail);

        // 日志输出转换后的值和它们的类型
        console.log(max, min, excellent, fail);
        console.log("Types:", typeof max, typeof min, typeof excellent, typeof fail);

        // 非空校验
        if (min === undefined || max === undefined || isNaN(min) || isNaN(max))
        {
            message.error("分数不能为null或非数字");
            return false;
        }

        // 分数必须大于0的校验
        if (min < 0 || max <= 0)
        {
            message.error("分数必须大于0");
            return false;
        }

        // 最大分数合理性校验
        if (max > 2000)
        {
            message.error("最大分数不能超过2000");
            return false;
        }

        // 最小分数与最大分数的比较
        if (min > max)
        {
            message.error("最小分数不能大于最大分数");
            return false;
        }

        // 最小分数与最大分数不能相等
        if (min === max)
        {
            message.error("最小分数和最大分数不能相等");
            return false;
        }

        // 分数线非空和数字校验
        if (excellent === undefined || fail === undefined || isNaN(excellent) || isNaN(fail))
        {
            message.error("分数线不能为null或非数字");
            return false;
        }

        // 分数线必须大于等于0的校验
        if (excellent < 0 || fail < 0)
        {
            message.error("分数线必须大于等于0");
            return false;
        }

        // 分数线与最大分数的比较
        if (excellent > max || fail > max)
        {
            message.error(`分数线不能大于最大分数(${max}): 优秀(${excellent}), 不及格(${fail})`);
            return false;
        }

        // 优秀分数线必须大于不及格分数线的校验
        if (excellent <= fail)
        {
            message.error(`优秀分数线(${excellent})必须大于不及格分数线(${fail})`);
            return false;
        }

        return true;
    };

    const setCurrentRowFunction = (record: API.SubjectsVO) =>
    {
        const payload: Subject.CurrentRow = {
            id: record.id as unknown as string,
            name: record.name,
            gradeMin: record.gradeMin,
            gradeMax: record.gradeMax,
            gradeExcellent: record?.gradeExcellent,
            gradeFail: record?.gradeFail,
        }
        setCurrentRow(payload)
        form.setFieldsValue({
            ...payload
        })
    }

    const ModalFormItemComponent = () =>
    {
        const onGradeInputChange = (value: number | null) =>
        {
            if (value !== null)
            {
                const { gradeExcellentType, gradeFailType } = form.getFieldsValue();
                if (gradeExcellentType && gradeExcellentType > 0)
                {
                    handleScoreTypeChange(gradeExcellentType, 'excellent');
                }
                if (gradeFailType && gradeFailType > 0)
                {
                    handleScoreTypeChange(gradeFailType, 'fail');
                }
            }
        }
        return (
            <Form form={form}>
                <Form.Item name={"name"} label={"科目名称"}>
                    <Input/>
                </Form.Item>
                <Form.Item name="gradeMin" initialValue={form.getFieldValue("gradeMin") ?? null} label="科目分数最小值">
                    <InputNumber
                        min={0}
                        max={2000}
                        onChange={onGradeInputChange}
                    />
                </Form.Item>
                <Form.Item name="gradeMax" initialValue={form.getFieldValue("gradeMax") ?? null} label="科目分数最大值">
                    <InputNumber
                        min={0}
                        max={2000}
                        onChange={onGradeInputChange}
                    />
                </Form.Item>

                <Form.Item label="不及格标准分数" style={{ marginBottom: "1px" }}>
                    <Space.Compact>
                        <Form.Item name="gradeFailType">
                            <Select style={{ width: "100px" }}
                                    onChange={(value) => handleScoreTypeChange(value, 'fail')}>
                                <Option key="fail-custom" value={0}>自定义</Option>
                                {[ ...Array(9).keys() ].map(i => (
                                    <Option key={`fail-${10 * (i + 1)}`}
                                            value={10 * (i + 1)}>总分{10 * (i + 1)}%</Option>
                                ))}
                            </Select>
                        </Form.Item>
                        <Form.Item name="gradeFail" initialValue={form.getFieldValue("gradeFail") ?? null}>
                            <InputNumber
                                disabled={isPercentFail}
                                min={0}
                                max={2000}
                            />
                        </Form.Item>
                    </Space.Compact>
                </Form.Item>
                <Form.Item label="优秀标准分数">
                    <Space.Compact>
                        <Form.Item name="gradeExcellentType">
                            <Select style={{ width: "100px" }}
                                    onChange={(value) => handleScoreTypeChange(value, 'excellent')}>
                                <Option key="excellent-custom" value={0}>自定义</Option>
                                {[ ...Array(9).keys() ].map(i => (
                                    <Option key={`excellent-${10 * (i + 1)}`}
                                            value={10 * (i + 1)}>总分{10 * (i + 1)}%</Option>
                                ))}
                            </Select>
                        </Form.Item>
                        <Form.Item name="gradeExcellent" initialValue={form.getFieldValue("gradeExcellent") ?? null}>
                            <InputNumber
                                style={{ width: "100px" }}
                                disabled={isPercentExcellent}
                                min={0}
                                max={2000}
                            />
                        </Form.Item>
                    </Space.Compact>
                </Form.Item>

            </Form>
        );
    }

    return <>
        <PageContainer title={"科目管理"}>
            <ProTable
                columns={SubjectColumns({
                    handleDeleteFunction: handleDelete,
                    setCurrentRow: setCurrentRowFunction,
                    setUpdateModalVisible
                })}
                actionRef={actionRef}
                rowKey="id"
                toolBarRender={() => [
                    <Button
                        type="primary"
                        key="primary"
                        onClick={() =>
                        {
                            setCreateModalVisible(true);
                        }}
                    >
                        <PlusOutlined/> 新建
                    </Button>,
                ]}
                request={async (params, sort, filter) =>
                {
                    const sortField = Object.keys(sort)?.[0];
                    const sortOrder = sort?.[sortField] ?? undefined;

                    const { data, code } = await listSubjectsByPageUsingPOST({
                        ...params,
                        sortField,
                        sortOrder,
                        ...filter,
                    } as API.SubjectsQueryRequest);

                    return {
                        success: code === 0,
                        data: data?.records || [],
                        total: Number(data?.total) || 0,
                    };
                }}
            />

            <Modal title={"新建"}
                   open={createModalVisible}
                   afterOpenChange={(visible) =>
                   {
                       if (visible)
                       {
                           form.resetFields()
                       }
                   }}
                   onOk={async () =>
                   {
                       const formData = form.getFieldsValue();
                       if (formData.name)
                       {
                           if (!await validateScoresAndLines({
                               ...formData
                           }))
                           {
                               return;
                           }
                           try
                           {
                               const { data, code } = await addSubjectsUsingPOST({
                                   ...formData
                               })
                               if (code === 0)
                               {
                                   message.success("添加成功")

                               }
                           }
                           catch (e: any)
                           {
                               message.error(e.message)
                           }
                           finally
                           {
                               setCreateModalVisible(false)
                               form.resetFields()
                               actionRef.current?.reloadAndRest?.()
                           }
                       }
                       else
                       {
                           message.error("科目名称不能为空")
                       }
                   }}
                   onCancel={() =>
                   {
                       setCreateModalVisible(false)
                       form.resetFields()
                   }}
            >
                <ModalFormItemComponent key={"add"}/>
            </Modal>

            <Modal title={"更新"}
                   open={updateModalVisible}
                   onOk={async () =>
                   {
                       const formData = form.getFieldsValue();
                       if (formData.name)
                       {
                           if (!await validateScoresAndLines({
                               ...formData
                           }))
                           {
                               return Promise.reject();
                           }
                           try
                           {
                               const { data, code } = await updateSubjectsUsingPOST({
                                   ...currentRow,
                                   ...formData,
                                   // @ts-ignore
                                   id: currentRow?.id,
                               })
                               if (code === 0)
                               {
                                   message.success("修改成功")
                               }
                           }
                           catch (e: any)
                           {
                               message.error(e.message)
                           }
                           finally
                           {
                               setUpdateModalVisible(false)
                               form.resetFields()
                               actionRef.current?.reloadAndRest?.()
                           }
                       }
                       else
                       {
                           message.error("科目名称不能为空")
                       }
                   }}
                   onCancel={() =>
                   {
                       setUpdateModalVisible(false);
                       form.resetFields();
                   }}
            >
                <ModalFormItemComponent key={"modify"}/>
            </Modal>

        </PageContainer>
    </>
}

export default Index;
