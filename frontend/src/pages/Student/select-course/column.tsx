import {Badge, Tag, Tooltip} from "antd";
import React from "react";
import {ProColumns} from "@ant-design/pro-components";
import dayjs from "dayjs";
import {RenderDateStatus} from "@/pages/Admin/CourseSelection/column";
import ClassTimesDisplay from "@/components/ClassTimesDisplay";

export const selectCourseTaskColumn: ProColumns[] = [
  {
    title: "选课任务名称",
    dataIndex: "taskName",
    key: "taskName",
    render: (text: string) => <b>{text}</b>,
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
  }
];
