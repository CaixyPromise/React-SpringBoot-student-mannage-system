import {ProColumns, ProSchema} from "@ant-design/pro-components";
import SemesterSelect from "@/components/SemesterSelect";

export const MyGradeTableColumns: ProColumns<API.GradeForStudentVO>[] = [
  {
    title: '分数id',
    dataIndex: 'gradeId',
    key: 'gradeId',
  },
  {
    title: '学期',
    dataIndex: 'semester',
    key: 'semester',
    hideInTable: true,
    renderFormItem: (schema: ProSchema, config) => {
      return <SemesterSelect value={config.value} onChange={config.onChange}/>
    }
  },
  {
    title: '课程名称',
    dataIndex: 'subjectName',
    key: 'subjectName',
  },
  {
    title: '得分',
    dataIndex: 'totalGrade',
    key: 'totalGrade',
    render: (_, record) => {
      return <span style={{
        color: (record?.totalGrade || 0) >= (record?.gradeFail || 0) ? 'green' : 'red'
      }}>{record?.totalGrade ?? 0}</span>
    },
    hideInSearch: true,

  },
  {
    title: '不及格分数线',
    dataIndex: 'gradeFail',
    key: 'gradeFail',
    hideInSearch: true,
  },
]
