import { Col, message, Row, Table } from "antd";
import React, { useEffect, useState } from "react";
import { getStudentGradesVoByTaskSubjectUsingGet1 } from "@/services/backend/scoreController";
import { ExpandStudentGradeInfoColumn } from "@/pages/Admin/RegistrationScore/columns";
import Visualization from "@/pages/Admin/RegistrationScore/components/Visualization";

const ExpandStudentGradeInfo: React.FC<{
  courseTaskId: number;
  subjectId: number;
}> = ({ courseTaskId, subjectId }) => {
  const [studentGrades, setStudentGrades] = useState<API.StudentsGradeForAdminVO[]>([]);

  const queryStudentGrade = async () => {
    try {
      const { code, data } = await getStudentGradesVoByTaskSubjectUsingGet1({
        subjectId,
        courseTaskId,
      });
      if (code === 0) {
        setStudentGrades(data);
      }
    } catch (e: Error) {
      message.error(e.message);
    }
  };

  useEffect(() => {
    if (courseTaskId && subjectId) {
      queryStudentGrade();
    }
  }, [courseTaskId, subjectId]);

  return (
    <div>
      {/* 第三行：表格 */}
      <Row gutter={16} justify="center" style={{ marginTop: 20 }}>
        <Col span={24}>
          <Table
            dataSource={studentGrades}
            columns={ExpandStudentGradeInfoColumn}
            rowKey="stuId" // 添加唯一的 key 字段
          />
        </Col>
      </Row>
      <Row gutter={16}>
        {/* 第一行：可视化组件 */}
        <Col span={24}>
          <Visualization gradeItems={studentGrades} />
        </Col>
      </Row>


    </div>
  );
};

export default ExpandStudentGradeInfo;
