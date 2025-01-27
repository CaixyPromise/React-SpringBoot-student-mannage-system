import {message, Table} from "antd";
import React, {useEffect, useState} from "react";
import {getStudentGradesVoByTaskSubjectUsingGet1} from "@/services/backend/scoreController";
import {ExpandStudentGradeInfoColumn} from "@/pages/Admin/RegistrationScore/columns";

const ExpandStudentGradeInfo: React.FC<{
  courseTaskId: number;
  subjectId: number;
}> = ({courseTaskId, subjectId}) => {
  const [studentGrades, setStudentGrades] = useState<API.StudentsGradeForAdminVO[]>([]);

  const queryStudentGrade = async () => {
    try {
      const {code, data} = await getStudentGradesVoByTaskSubjectUsingGet1({
        subjectId,
        courseTaskId
      })
      if (code === 0) {
        setStudentGrades(data);
      }
    } catch (e: Error) {
      message.error(e.message)
    }
  }

  useEffect(() => {
    if (courseTaskId && subjectId) {
      queryStudentGrade()
    }
  }, [courseTaskId, subjectId]);
  return (
    <Table
      dataSource={studentGrades}
      columns={ExpandStudentGradeInfoColumn}
    />
  )
}
export default ExpandStudentGradeInfo;
