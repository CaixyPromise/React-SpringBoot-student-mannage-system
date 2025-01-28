import {useEffect, useState} from "react";
import {PageContainer, ProTable} from "@ant-design/pro-components";
import {MyGradeTableColumns} from "@/pages/Student/my-grades/column";
import {message, Table} from "antd";
import {getStudentGradesUsingGet1} from "@/services/backend/scoreController";
import SemesterSelect from "@/components/SemesterSelect";

const MyGradesPages = () => {
  const [semesterOption, setSemesterOption] = useState<number | undefined>();
  const [gradeItems, setGradeItems] = useState<API.GradeForStudentVO[]>([]);

  const queryGradeInfo = async (params) => {
    const dataReturn = {
      success: false,
      data: [],
      total: 0,
    }
    console.log("params: ", params)
    try {
      const {code, data} = await getStudentGradesUsingGet1({
        semesterId: semesterOption
      })
      if (code === 0 && data) {
        dataReturn.success = true;
        dataReturn.data = data;
        dataReturn.total = data.length;
        return dataReturn;
      }
      return dataReturn;
    } catch (e: Error) {
      message.error(e?.message)
      return dataReturn;
    }
  }


  return (
    <PageContainer
      title='我的成绩信息'
    >
      <ProTable
        rowKey="gradeId"
        columns={MyGradeTableColumns}
        request={queryGradeInfo}
        pagination={false}
      />
    </PageContainer>
  )
}

export default MyGradesPages;
