import {ProColumns} from "@ant-design/pro-components";
import dayjs from "dayjs";
import {Button, message, Space} from "antd";
import {
  activeRegistrationTaskUsingPostUsingPost1,
  deleteRegistrationTaskUsingPost1
} from "@/services/backend/registrationTaskController";
import {ProCoreActionType} from "@ant-design/pro-utils/es/typing";
import {record} from "@umijs/utils/compiled/zod";
import {updatePublicationUsingPost1} from "@/services/backend/registrationTaskLessonController";

const toggleActiveStatus = async (id: number | undefined, isActive: 0 | 1, action: ProCoreActionType) => {
  if (id === undefined) {
    message.error("id不能为空")
  }
  try {
    const {code} = await activeRegistrationTaskUsingPostUsingPost1({
      id,
      isActive
    });
    if (code === 0) {
      message.success("任务状态变更成功")
      action?.reload();
    }
  } catch (error) {
    message.error("操作失败")
  }
}

export const setPublicationScoreState = async (id: number[] | undefined, state: number, action: ProCoreActionType) => {
  if (id === undefined) {
    message.error("id不能为空")
  }
  try {
    const {code} = await updatePublicationUsingPost1({
      ids: id,
      state
    })
    if (code === 0) {
      message.success("成绩状态变更成功")
      action?.reload();
    }
  } catch (e: Error) {
    message.error("操作失败")
  }
}


export const RegistrationTableColumn: ProColumns[] = [
  {
    title: '任务Id',
    dataIndex: 'id',
    key: 'id',
  },
  {
    title: '任务名称',
    dataIndex: 'name',
    key: 'name',
  },
  {
    title: '状态',
    dataIndex: 'isActive',
    key: 'isActive',
    valueEnum: {
      1: {text: '激活', status: 'Success'},
      0: {text: '未激活', status: 'Default'},
    }
  },
  {
    title: '学期名称',
    dataIndex: 'semesterName',
    key: 'semesterName',
    render: (_, record) => {
      return record?.semestersInfo?.name || "未设置"
    }
  },
  {
    title: '开始日期',
    dataIndex: 'startDate',
    valueType: 'date',
    render: (_, record) => dayjs(record.startDate).format("YYYY-MM-DD HH:mm"),
  },
  {
    title: '结束日期',
    dataIndex: 'endDate',
    valueType: 'date',
    render: (_, record) => dayjs(record.endDate).format("YYYY-MM-DD HH:mm"),

  },
  {
    title: "创建时间",
    dataIndex: "createTime",
    key: "createTime",
    render: (_, record) => dayjs(record.createTime).format("YYYY-MM-DD HH:mm"),
  },
  {
    title: "更新时间",
    dataIndex: "updateTime",
    key: "updateTime",
    render: (_, record) => dayjs(record.updateTime).format("YYYY-MM-DD HH:mm"),
  },
  {
    title: '操作',
    valueType: 'option',
    render: (_, record, _ix, action) => {
      return (
        <Space size="middle">
          {
            record?.isActive === 1 ? (
              <Button danger type='link' onClick={() => toggleActiveStatus(record?.id, 0, action)}>
                停止任务
              </Button>
            ) : (
              <Button type='link' onClick={() => toggleActiveStatus(record?.id, 1, action)}>
                激活任务
              </Button>
            )
          }
          <Button type='link' danger onClick={async () => {
            await deleteRegistrationTaskUsingPost1(record?.id);
            action?.reload();
          }}>
            删除
          </Button>
        </Space>
      )
    }
  }
]


export const ExpandRegistrationLessonColumn: ProColumns<API.RegistrationTaskLessonVO>[] = [
  {
    title: '课程名称',
    dataIndex: 'name',
    key: 'name',
    render: (_, record) => {
      if (!record?.subjectsInfo) {
        return "未设置"
      }
      return record?.subjectsInfo?.name
    }
  },
  {
    title: '课程任务名称',
    dataIndex: 'taskName',
    key: 'taskName',
    render: (_, record) => {
      if (!record?.courseSelectionInfo) {
        return "未设置"
      }
      return record?.courseSelectionInfo?.taskName
    }
  },
  {
    title: '是否发布成绩',
    dataIndex: 'isPublish',
    key: 'isPublish',
    valueEnum: {
      1: {text: '已发布', status: 'Success'},
      0: {text: '未发布', status: 'Default'},
    }
  },
  {
    title: '是否完成',
    dataIndex: 'isFinished',
    key: 'isFinished',
    valueEnum: {
      1: {text: '已完成', status: 'Success'},
      0: {text: '未完成', status: 'Default'},
    }
  },
  {
    title: '操作',
    valueType: 'option',
    render: (_, record, _ix, action) => {
      return (
        <Space size="middle">
          {
            record?.isPublish === 0 ? (
              record?.isFinished === 1 && (
                <Button type='link' onClick={() => setPublicationScoreState([record?.id], 1, action)}>
                  发布成绩
                </Button>
              )
            ) : (
              <Button danger type='link' onClick={() => setPublicationScoreState([record?.id], 0, action)}>
                取消发布
              </Button>
            )
          }
        </Space>
      )
    }
  }
]


export const ExpandStudentGradeInfoColumn: ProColumns<API.StudentsGradeForAdminVO>[] = [
  {
    title: '学号',
    dataIndex: 'studentId',
    key: 'studentId',
    render: (_, record) => {
      if (!record?.studentInfo) {
        return "未设置"
      }
      return record?.studentInfo?.stuId;
    }
  },
  {
    title: '姓名',
    dataIndex: 'name',
    key: 'name',
    render: (_, record) => {
      if (!record?.studentInfo) {
        return "未设置"
      }
      return record?.studentInfo?.stuName;
    }
  },
  {
    title: '学生院系信息',
    dataIndex: 'department',
    key: 'department',
    width: 400,
    render: (_, record) => {
      if (!record?.studentInfo) {
        return "未设置"
      }
      const {stuMajor, stuDepart, stuClass} = record?.studentInfo;
      return <span>{stuDepart ?? ""}-{stuMajor ?? ""}-{stuClass ?? ""}</span>;
    }
  },

  {
    title: '平时成绩',
    dataIndex: 'score',
    key: 'score',
    render: (_, record) => {
      if (!record?.gradeItem) {
        return "未设置";
      }
      const { usualGrade, usualPercentage } = record?.gradeItem;
      const usualScore = usualGrade * (usualPercentage / 100); // 计算平时成绩占总分的具体分数
      return (
        <span>
        {usualGrade ?? 0} ({usualPercentage ?? 0}%) = {usualScore.toFixed(2)} 分
      </span>
      );
    }
  },
  {
    title: '期末成绩',
    dataIndex: 'score',
    key: 'score',
    render: (_, record) => {
      if (!record?.gradeItem) {
        return "未设置";
      }
      const { finalGrade, finalPercentage } = record?.gradeItem;
      const finalScore = finalGrade * (finalPercentage / 100); // 计算期末成绩占总分的具体分数
      return (
        <span>
        {finalGrade ?? 0} ({finalPercentage ?? 0}%) = {finalScore.toFixed(2)} 分
      </span>
      );
    }
  },
  {
    title: '总成绩',
    dataIndex: 'score',
    key: 'score',
    render: (_, record) => {
      if (!record?.gradeItem) {
        return "未设置"
      }
      return <span>{record?.gradeItem?.totalGrade}</span>;
    }
  },
]
