import React, {useEffect, useState} from "react";
import {PageContainer} from "@ant-design/pro-components";
import {Button, Card, Form, Select, Spin} from "antd";
import {fetchAllSubjectAnalysis, fetchAnalysisByFilter} from "@/pages/Admin/Analysis/server";
import styles from "./styles.scss"
import EChartsReact from "echarts-for-react";
import {EChartsOption} from "echarts";
import {fetchCollegeCascadeOption, fetchSubjectOption} from "@/utils/server";
import {studentSexOption} from "@/pages/Admin/Analysis/constants";

interface IndexProps
{

}

const Index: React.FC<IndexProps> = () =>
{
    const [ form ] = Form.useForm();
    const [ loading, setLoading ] = useState<boolean>(false);
    const [ cascadeOption, setCascadeOption ] = useState<API.AllClassesOptionDataVO[]>([]);
    const [ allSubjectAnalysis, setAllSubjectAnalysis ] = useState<EChartsOption>({});
    const [ subjectOption, setSubjectOption ] = useState<OptionProps[]>([]);

    const [ filterBarCharts, setFilterBarCharts ] = useState<EChartsOption>({});
    const [ filterPolarCharts, setFilterPolarCharts ] = useState<EChartsOption>({});

    const [ colleges, setColleges ] = useState<OptionProps[]>([]);
    const [ majors, setMajors ] = useState<OptionProps[]>([]);
    const [ classes, setClasses ] = useState<OptionProps[]>([]);

    const preparedSelectedOption = (cascadeData: API.AllClassesOptionDataVO[]) =>
    {
        const collegesOptions: ((prevState: OptionProps[]) => OptionProps[]) | {
            label: string | undefined;
            value: string | undefined;
        }[] = [];
        const majorsOptions: ((prevState: OptionProps[]) => OptionProps[]) | {
            label: string;
            value: string | undefined;
        }[] = [];
        const classesOptions: ((prevState: OptionProps[]) => OptionProps[]) | {
            label: string;
            value: string | undefined;
        }[] = [];

        cascadeData.forEach(college =>
        {
            collegesOptions.push({
                label: college.label,
                value: college.value
            });

            college.children?.forEach(major =>
            {
                majorsOptions.push({
                    label: `${college.label} - ${major.label}`,
                    value: major.value
                });

                major.children?.forEach(klass =>
                {
                    classesOptions.push({
                        label: `${college.label} - ${major.label} - ${klass.label}`,
                        value: klass.value
                    });
                });
            });
        });

        setColleges(collegesOptions);
        setMajors(majorsOptions);
        setClasses(classesOptions);
        return {
            collegesOptions, majorsOptions, classesOptions
        }
    }

    useEffect(() =>
    {
        const loadData = async () =>
        {
            setLoading(true);
            const cascadeData = await fetchCollegeCascadeOption(setCascadeOption);
            const subjectData = await fetchSubjectOption(setSubjectOption)
            await fetchAllSubjectAnalysis(setAllSubjectAnalysis)

            if (cascadeData.length > 0 && subjectData.length > 0)
            {
                preparedSelectedOption(cascadeData);
                await fetchAnalysisByFilter({}, setFilterPolarCharts, setFilterBarCharts);
            }
            setLoading(false);
        }
        loadData();
    }, [])
    // 表单值变化时触发搜索
    const handleFormChange = (_, allValues) =>
    {
        triggerSearch(allValues);
    };

    // 触发搜索
    const triggerSearch = async (values = form.getFieldsValue()) =>
    {
        setLoading(true);
        try
        {
            const analysisResult = await fetchAnalysisByFilter(
                values as API.GradeAnalysisFilterDTO,
                setFilterPolarCharts,
                setFilterBarCharts
            );
            // setFilterCharts(analysisResult);
        }
        catch (error)
        {
            console.error('Fetching analysis failed:', error);
        }
        finally
        {
            setLoading(false);
        }
    };

    const resetForm = () =>
    {
        form.resetFields();
        // form.setFieldsValue({
        //     majorIds: majors.length > 0 ? [ majors[0].value ] : [],
        //     classIds: classes.length > 0 ? [ classes[0].value ] : [],
        //     departmentIds: colleges.length > 0 ? [ colleges[0].value ] : [],
        //     subjectIds: subjectOption.length > 0 ? [ subjectOption[0].value ] : [],
        //     stuSex: [ studentSexOption[0]?.value ]
        // })
    }

    return <>
        <PageContainer
            title={"学生分析"}
        >
            <Card title={"科目成绩分析"}>
                <Spin spinning={loading} tip={"成绩报告分析中，请稍后..."}>
                    <EChartsReact option={allSubjectAnalysis}/>
                </Spin>
            </Card>

            <Card
                title={"条件成绩分析"}
                style={{ marginTop: "8px" }}
            >
                <div className={styles.searchBox}>
                    <span>分析维度</span>
                    <Form form={form} layout={"inline"} onValuesChange={handleFormChange}>
                        <Form.Item name="departmentIds" label="学院选择">
                            <Select
                                mode="multiple"
                                options={colleges}
                                style={{ minWidth: '200px', maxWidth: '300px' }}
                            />
                        </Form.Item>
                        <Form.Item name="majorIds" label="专业选择">
                            <Select
                                mode="multiple"
                                options={majors}
                                style={{ minWidth: '200px', maxWidth: '300px' }}
                            />
                        </Form.Item>
                        <Form.Item name="classIds" label="班级选择">
                            <Select
                                mode="multiple"
                                options={classes}
                                style={{ minWidth: '200px', maxWidth: '400px' }}
                            />
                        </Form.Item>
                        <Form.Item label={"科目选择"} name={"subjectIds"} className={styles.formItem}>
                            <Select
                                options={subjectOption}
                                loading={loading}
                                style={{ minWidth: '150px' }}
                                mode={"multiple"}
                            />
                        </Form.Item>
                        <Form.Item label={"性别选择"} name={"stuSex"} className={styles.formItem}>
                            <Select
                                options={studentSexOption}
                                loading={loading}
                                style={{ minWidth: '150px' }}
                            />
                        </Form.Item>
                    </Form>
                    <Button type="primary" onClick={resetForm}>重置</Button>
                </div>

                <div>
                    <Card>
                        <Spin spinning={loading} tip={"成绩报告分析中，请稍后..."}>
                            <div className={styles.chartBox}>
                                <div className={styles.chartItem}>
                                    <EChartsReact option={filterPolarCharts} className={styles.chart}/>
                                </div>
                                <div className={styles.chartItem}>
                                    <EChartsReact option={filterBarCharts} className={styles.chart} />
                                </div>
                            </div>
                        </Spin>
                    </Card>
                </div>
            </Card>
        </PageContainer>
    </>
}

export default Index;
