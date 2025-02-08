import {ProColumns} from "@ant-design/pro-table/es/typing";
import dayjs from "dayjs";
import {Badge, Button, message, Modal, Tag, Tooltip} from "antd";
import {
  activateTaskByIdUsingGet1,
  deleteCourseSelectionInfoUsingPost1,
  putTaskHoldByIdUsingGet1
} from "@/services/backend/courseSelectionInfoController";
import React, {MutableRefObject} from "react";
import {ActionType} from "@ant-design/pro-components";

export const RenderDateStatus = (startDate: string, endDate: string) => {
  const now = dayjs();
  const start = dayjs(startDate);
  const end = dayjs(endDate);

  let status = "processing"; // 默认状态
  let color = "blue"; // 默认颜色

  if (now.isBefore(start)) {
    status = "default";
    color = "gray"; // 未开始状态
  } else if (now.isAfter(end)) {
    status = "error";
    color = "red"; // 已结束状态
  } else {
    status = "success";
    color = "green"; // 正常状态
  }

  return (
    <div>
      <Badge
        status={status}
        text={
          <Tooltip title={`开始时间：${start.format("YYYY-MM-DD HH:mm")}, 结束时间：${end.format("YYYY-MM-DD HH:mm")}`}>
            <span
              style={{color}}>{`选课任务时间: ${start.format("YYYY-MM-DD HH:mm")} ~ ${end.format("YYYY-MM-DD HH:mm")}`}</span>
          </Tooltip>
        }
      />
    </div>
  );
};

const handleDeleteTask = async (taskId: number | undefined, actionRef: MutableRefObject<ActionType>) => {
  if (taskId === undefined) {
    return;
  }
  try {
    const {code} = await deleteCourseSelectionInfoUsingPost1({id: taskId})
    if (code === 0) {
      message.success(`删除成功`);
      actionRef?.current?.reload();
    }
  } catch (e) {
    message.error(`删除失败 ${e}`);
  }
}

const handleActivateTask = async (taskId: number | undefined, actionRef: MutableRefObject<ActionType>) => {
  if (taskId === undefined) {
    return;
  }
  try {
    const {code} = await activateTaskByIdUsingGet1({taskId})
    if (code === 0) {
      message.success(`激活成功`);
      actionRef?.current?.reload();
    }
  } catch (e) {
    message.error(`激活失败: ${e}`);
  }
}

const handleHoldTask = async (taskId: number | undefined, actionRef: MutableRefObject<ActionType>) => {
  if (taskId === undefined) {
    return;
  }
  try {
    const {code} = await putTaskHoldByIdUsingGet1({taskId})
    if (code === 0) {
      message.success(`搁置成功`);
      actionRef?.current?.reload();
    }
  } catch (e) {
    message.error(`搁置失败: ${e}`);
  }
}


export const SelectionCourseColumnConfig = (
  actionRef: React.RefObject<ActionType>,
  openDetailsModal: (taskId: API.CourseSelectionInfoVO | undefined) => void
): ProColumns<any>[] => [
  {
    title: "选课任务名称",
    dataIndex: "taskName",
    key: "taskName",
    render: (text: string) => <b>{text}</b>,
  },
  {
    title: "学期",
    dataIndex: "semesterName",
    key: "semesterName",
  },
  {
    title: "选课时间",
    key: "dateRange",
    width: 450,
    render: (_: any, record: API.CourseSelectionInfoVO) => RenderDateStatus(record.startDate, record.endDate),
  },
  {
    title: "最小选课学分",
    dataIndex: "minCredit",
    key: "minCredit",
    render: (credit: number) => <Tag color="blue">{credit.toFixed(2)}</Tag>,
  },
  {
    title: "创建时间",
    dataIndex: "createTime",
    key: "createTime",
    render: (date: string) => dayjs(date).format("YYYY-MM-DD HH:mm"),
  },
  {
    title: "更新时间",
    dataIndex: "updateTime",
    key: "updateTime",
    render: (date: string) => dayjs(date).format("YYYY-MM-DD HH:mm"),
  },
  {
    title: "任务状态",
    key: "taskStatus",
    render: (_: any, record: API.CourseSelectionInfoVO) => {
      if (dayjs(record?.endDate) < dayjs()) {
        return <Tag color="default">已结束</Tag>;
      }
      if (record.isActive === 0) {
        return <Tag color="warning">搁置</Tag>;
      }
      if (record.isDelete === 1) {
        return <Tag color="grey">已删除</Tag>;
      }
      return <Tag color="green">正常</Tag>;
    },
  },
  {
    title: "操作",
    key: "operation",
    render: (_: any, record: API.CourseSelectionInfoVO) => {
      return (
        <>
          {
            (dayjs(record?.endDate) < dayjs() && record?.isActive) && (
              <Button type="link" onClick={() => {
                openDetailsModal(record)
              }}>查看学生选课详情</Button>
            )
          }
          {
            record?.isActive ?
              <>
                <Button
                  type="link"
                  danger
                  onClick={() => handleDeleteTask(record.id, actionRef)}>
                  {dayjs(record.endDate) < dayjs() ? '归档' : '删除'}任务
                </Button>
                <Button
                  type="link"
                  onClick={() => handleHoldTask(record.id, actionRef)}
                >
                  搁置任务
                </Button>
              </>
              : <Button
                type="link"
                onClick={() => handleActivateTask(record.id, actionRef)}
              >
                恢复任务
              </Button>
          }
        </>
      )
    }
  }
]


export const expandStudentInfo: ProColumns<API.CourseStudentInfoVO>[] = [
  {
    title: '学号',
    dataIndex: 'stuId',
    key: 'stuId',
  },
  {
    title: '姓名',
    dataIndex: 'stuName',
    key: 'stuName',
  },
  {
    title: '学生学院',
    dataIndex: 'stuDepart',
    key: 'stuDepart',
  },
  {
    title: '学生专业',
    dataIndex: 'stuMajor',
    key: 'stuMajor',
  },
  {
    title: '学生班级',
    dataIndex: 'stuClass',
    key: 'stuClass',
  },
  {
    title: '选课时间',
    dataIndex: 'selectTime',
    key: 'selectTime',
    render: (text: string) => {
      return <span>{dayjs(text)?.format('YYYY-MM-DD HH:mm:ss')}</span>;
    }
  },
  {
    title: '选课方式',
    dataIndex: 'byRandom',
    key: 'byRandom',
    render: (text: number) => {
      return <span>{text ? '超时系统随机' : '学生自主选择'}</span>;
    }
  }
]
