import React, {useEffect, useState} from "react";
import {PageContainer} from "@ant-design/pro-components";
import {Card, Spin} from "antd";

interface IndexProps
{

}

const Index: React.FC<IndexProps> = () =>
{
    const [ loading, setLoading ] = useState<boolean>();

    useEffect(() =>
    {
    }, [])

    return <>
        <PageContainer
            title={"学生分析"}
        >
            <Card title={"综合成绩分析"}>
                <Spin spinning={loading}>

                </Spin>
            </Card>

            <Card
                title={"学院成绩分析"}
                style={{marginTop: "8px"}}
            >
                <div style={{
                    display: "flex",
                    justifyContent: "space-between",
                    alignItems: "center",
                    marginLeft: "8px"
                }}>
                    <span>分析维度选择：</span>
                </div>


            </Card>

        </PageContainer>
    </>
}

export default Index;
