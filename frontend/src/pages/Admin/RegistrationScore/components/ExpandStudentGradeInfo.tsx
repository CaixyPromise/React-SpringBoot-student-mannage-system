import {Button, Col, Empty, message, Row, Table} from "antd";
import React, { useEffect, useState } from "react";
import { getStudentGradesVoByTaskSubjectUsingGet1 } from "@/services/backend/scoreController";
import { ExpandStudentGradeInfoColumn } from "@/pages/Admin/RegistrationScore/columns";
import Visualization from "@/pages/Admin/RegistrationScore/components/Visualization";
import * as XLSX from "xlsx";
import {DownloadOutlined} from "@ant-design/icons"; // 导出 Excel
const ExpandStudentGradeInfo: React.FC<{
  courseTaskId: number;
  subjectInfo: API.SubjectsVO;
  taskId: number;
}> = ({ courseTaskId, subjectInfo, taskId }) => {
  const [studentGrades, setStudentGrades] = useState<API.StudentsGradeForAdminVO[]>([]);

  const queryStudentGrade = async () => {
    try {
      const { code, data } = await getStudentGradesVoByTaskSubjectUsingGet1({
        subjectId: subjectInfo?.id as number,
        courseTaskId,
        taskId,
      });
      if (code === 0) {
        setStudentGrades(data);
      }
    } catch (e: Error) {
      message.error(e.message);
    }
  };

  useEffect(() => {
    if (courseTaskId && subjectInfo?.id) {
      queryStudentGrade();
      console.log("subjectInfo: ", subjectInfo)
    }
  }, [courseTaskId, subjectInfo?.id]);

  // **📤 导出 Excel**
  const exportToExcel = () => {
    if (studentGrades.length === 0) {
      message.warning("没有成绩数据可导出");
      return;
    }

    // 1. **格式化 Excel 数据**
    const excelData = studentGrades.map((record) => {
      const { studentInfo, gradeItem } = record;
      if (!studentInfo || !gradeItem) return {}; // 处理空数据

      // 计算 平时 & 期末 成绩
      const usualScore = (gradeItem.usualGrade || 0) * (gradeItem.usualPercentage / 100);
      const finalScore = (gradeItem.finalGrade || 0) * (gradeItem.finalPercentage / 100);

      return {
        "学号": studentInfo.stuId,
        "姓名": studentInfo.stuName,
        "学院": studentInfo.stuDepart,
        "专业": studentInfo.stuMajor,
        "班级": studentInfo.stuClass,
        "科目": gradeItem.subjectName,
        "科目满分": subjectInfo?.gradeMax,
        "科目优秀分": subjectInfo?.gradeExcellent,
        "科目及格线": subjectInfo?.gradeFail,

        // ✅ **平时成绩**
        "平时成绩占比": `${gradeItem.usualPercentage}%`,
        "平时成绩得分": gradeItem.usualGrade,
        "平时成绩总评": usualScore.toFixed(2),

        // ✅ **期末成绩**
        "期末成绩占比": `${gradeItem.finalPercentage}%`,
        "期末成绩得分": gradeItem.finalGrade,
        "期末成绩总评": finalScore.toFixed(2),

        // ✅ **最终总成绩 & 学分绩点**
        "最终总成绩": gradeItem.totalGrade,
        "科目学分绩点": ((gradeItem.totalGrade / (subjectInfo?.gradeMax ?? 100)) * 5).toFixed(2),
      };
    });

    // 2. **创建 Excel 表格**
    const worksheet = XLSX.utils.json_to_sheet(excelData);
    const workbook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(workbook, worksheet, "学生成绩表");

    // 3. **导出文件**
    XLSX.writeFile(workbook, `学生成绩_${subjectInfo?.name}.xlsx`);
    message.success("学生成绩已导出");
  };


  return (
    <div>
      <Row gutter={16} justify="center" style={{ marginTop: 20 }}>
        <Col span={24}>
          <Table
            title={() => (
              <div style={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}}>
                <h3>学生成绩-{subjectInfo?.name}</h3>
                {
                  studentGrades?.length !== 0 && (
                  <Button
                    type="default"
                    icon={<DownloadOutlined />}
                    onClick={exportToExcel}
                  >
                    导出成绩到 Excel
                  </Button>)
                }
              </div>
            )}
            dataSource={studentGrades}
            columns={ExpandStudentGradeInfoColumn(subjectInfo)}
            rowKey="stuId"
          />
        </Col>
      </Row>
      {
        studentGrades?.length !== 0 && (<Row gutter={16}>
          <Col span={24}>
            <Visualization gradeItems={studentGrades} subjectInfo={subjectInfo} />
          </Col>
        </Row>)
      }
    </div>
  );
};

export default ExpandStudentGradeInfo;
