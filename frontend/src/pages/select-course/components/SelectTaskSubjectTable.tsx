import React, { useEffect, useState } from "react";
import {Badge, Button, message, Table, Tag, Tooltip} from "antd";
import { ProColumns } from "@ant-design/pro-components";
import {
  cancelCourseUsingPost1,
  listAvailableSubjectsUsingGet1,
  selectCourseUsingPost1,
} from "@/services/backend/studentCourseSelectionController";
import dayjs from "dayjs";
import useDebounce from "@/hooks/useDebounce";
import ClassTimesDisplay from "@/components/ClassTimesDisplay";

const handleSelect = async (subjectIds: number[], courseSelectionId: number) => {
  // 处理选课逻辑
  try {
    const { data, code } = await selectCourseUsingPost1({
      subjectIds: subjectIds,
      courseSelectionId,
    });
    if (code === 0) {
      message.success("选课成功");
      return Promise.resolve();
    }
    return Promise.reject(new Error("选课接口返回异常"));
  } catch (e: any) {
    message.error(`选课失败: ${e.message}`);
    return Promise.reject(e);
  }
};

const handleUnselect = async (subjectIds: number[], courseSelectionId: number) => {
  // 处理退选逻辑
  try {
    const { data, code } = await cancelCourseUsingPost1({
      subjectIds: subjectIds,
      courseSelectionId,
    });
    if (code === 0) {
      message.success("退选成功");
      return Promise.resolve();
    }
    return Promise.reject(new Error("退选接口返回异常"));
  } catch (e: any) {
    message.error(`退选失败: ${e.message}`);
    return Promise.reject(e);
  }
};

const SelectTaskSubjectTable: React.FC<{
  taskItem: API.CourseSelectionInfoVO;
}> = ({ taskItem }) => {
  const [selectTaskSubject, setSelectTaskSubject] = useState<API.StudentCourseSubjectVO[]>([]);
  const [selectedRowKeys, setSelectedRowKeys] = useState<React.Key[]>([]);
  const [loading, setLoading] = useState<boolean>(false);

  // 新增: 记录当前选择模式
  const [selectMode, setSelectMode] = useState<"selected" | "unselected" | null>(null);

  const [doSelect] = useDebounce(handleSelect, 500);
  const [doCancel] = useDebounce(handleUnselect, 500);

  // 查询表格数据
  const queryAvailableSubject = async () => {
    setLoading(true);
    try {
      const { code, data } = await listAvailableSubjectsUsingGet1({
        courseSelectionId: taskItem.id,
      });
      if (code === 0 && data) {
        setSelectTaskSubject(data);
      }
    } catch (e: any) {
      message.error(e.message);
    } finally {
      setLoading(false);
    }
  };

  const [handleReload] = useDebounce(queryAvailableSubject, 500);

  // 单个操作列
  const subjectColumns: ProColumns<API.StudentCourseSubjectVO>[] = [
    {
      title: "科目名称",
      dataIndex: "subjectName",
      key: "subjectName",
    },
    {
      title: "学分",
      dataIndex: "gradeCredit",
      key: "gradeCredit",
    },
    {
      title: "最大选课人数",
      dataIndex: "maxStudents",
      key: "maxStudents",
    },
    {
      title: "已选人数",
      dataIndex: "enrolledCount",
      key: "enrolledCount",
    },
    {
      title: "上课地点",
      render: (text: string, record: API.CourseSelectionInfoVO) => {
        return <>
          <span>{record.classRoom}</span>
        </>
      }
    },
    {
      title: "上课时间",
      dataIndex: "classTimes",
      render: (classTimes: API.ClassTime[]) => <ClassTimesDisplay classTimes={classTimes} />,
    },
    {
      title: "授课教师",
      render: (text: string, record: API.CourseSelectionInfoVO) => {
        const {teacherName, teacherId, teacherMajor, teacherDepart} = record?.teacherInfo;

        return (
          <Tooltip
            title={
              <div style={{
                marginBottom: "4px"
              }}>
                <div>教师编号: {teacherId}</div>
                <div>教师专业: {teacherMajor}</div>
                <div>教师部门: {teacherDepart}</div>
              </div>
            }
          >
            <span>{teacherName}</span>
          </Tooltip>
        )
      }
    },
    {
      title: "操作",
      key: "operation",
      render: (_: any, record: API.StudentCourseSubjectVO) => {
        if (!record.selected && !record.full) {
          // 可选状态
          return (
            <Button
              type="link"
              onClick={() => {
                doSelect([record.subjectId], taskItem.id).then(() => {
                  queryAvailableSubject();
                });
              }}
            >
              选课
            </Button>
          );
        } else if (record.selected) {
          // 已选状态
          return (
            <Button
              type="link"
              danger
              onClick={() => {
                doCancel([record.subjectId], taskItem.id).then(() => {
                  queryAvailableSubject();
                });
              }}
            >
              退选
            </Button>
          );
        } else {
          // 名额已满
          return <Tag color="red">名额已满</Tag>;
        }
      },
    },
  ];

  // 多选行配置
  const rowSelection = {
    selectedRowKeys,
    onChange: (newSelectedRowKeys: React.Key[], selectedRows: API.StudentCourseSubjectVO[]) => {
      // 1. 若还没有确立模式，则根据第一个选中的行是 selected 还是 unselected
      //    来确定模式
      if (newSelectedRowKeys.length === 0) {
        // 全部取消选中
        setSelectedRowKeys([]);
        setSelectMode(null);
        return;
      }
      // 获取本次选中的最后一行
      const lastSelected = selectedRows[selectedRows.length - 1];
      if (!lastSelected) {
        return; // 容错
      }

      // 判断这行是“已选”还是“未选”
      const isSelected = !!lastSelected.selected;

      // 如果没有模式，则设定模式
      let targetMode: "selected" | "unselected" = isSelected ? "selected" : "unselected";

      // 若已有模式，则需要校验是否冲突
      if (selectMode && selectMode !== targetMode) {
        // 模式冲突，说明用户试图混选
        message.warning("不能同时选择已选科目和未选科目，请先取消当前选择后再操作。");
        return;
      }

      // 2. 检查所有选中的行是否都符合这个模式
      const conflictRow = selectedRows.find((row) => {
        if (targetMode === "selected") {
          return row.selected === false; // 发现一个未选行冲突
        } else {
          return row.selected === true; // 发现一个已选行冲突
        }
      });
      if (conflictRow) {
        // 存在冲突，拒绝
        message.warning("不能混选“已选”与“未选”科目，请重新选择。");
        return;
      }

      // 3. 一切正常，更新状态
      setSelectMode(targetMode);
      setSelectedRowKeys(newSelectedRowKeys);
    },
    getCheckboxProps: (record: API.StudentCourseSubjectVO) => ({
      // 如果模式存在，禁用不符合模式的行
      disabled: selectMode === "selected" ? !record.selected : selectMode === "unselected" ? record.selected : false,
    }),
  };

  // 组件初始化加载
  useEffect(() => {
    if (!taskItem.id) return;
    queryAvailableSubject();
  }, [taskItem]);

  // 批量选课
  const handleBatchSelect = () => {
    if (selectedRowKeys.length === 0) return;
    doSelect(selectedRowKeys as number[], taskItem.id).then(() => {
      queryAvailableSubject();
      setSelectedRowKeys([]);
      setSelectMode(null);
    });
  };

  // 批量退选
  const handleBatchUnselect = () => {
    if (selectedRowKeys.length === 0) return;
    doCancel(selectedRowKeys as number[], taskItem.id).then(() => {
      queryAvailableSubject();
      setSelectedRowKeys([]);
      setSelectMode(null);
    });
  };

  return (
    <Table
      title={() => (
        <div
          style={{
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
            padding: "8px 16px",
            backgroundColor: "#f5f5f5",
            borderRadius: "8px",
            marginBottom: "16px",
          }}
        >
          {/* 左侧：标题与任务信息 */}
          <div style={{ display: "flex", flexDirection: "column" }}>
            <h3 style={{ margin: 0, fontSize: "18px" }}>选课任务名称: {taskItem.taskName}</h3>
            <span style={{ color: "#888", fontSize: "14px" }}>
              当前学期: {taskItem.semesterName} | 开始时间:{" "}
              {dayjs(taskItem.startDate).format("YYYY-MM-DD")} | 结束时间:{" "}
              {dayjs(taskItem.endDate).format("YYYY-MM-DD")}
            </span>
          </div>
          {/* 右侧：操作按钮 */}
          <div style={{ display: "flex", gap: "8px", alignItems: "center" }}>
            {selectedRowKeys.length > 0 && selectMode === "unselected" && (
              <Button type="primary" onClick={handleBatchSelect}>
                批量选课 ({selectedRowKeys.length})
              </Button>
            )}
            {selectedRowKeys.length > 0 && selectMode === "selected" && (
              <Button danger onClick={handleBatchUnselect}>
                批量退选 ({selectedRowKeys.length})
              </Button>
            )}
            <Button onClick={() => handleReload()} loading={loading}>
              刷新数据
            </Button>
          </div>
        </div>
      )}
      loading={loading}
      dataSource={selectTaskSubject}
      columns={subjectColumns}
      rowKey="subjectId"
      rowSelection={rowSelection}
      pagination={false}
    />
  );
};

export default SelectTaskSubjectTable;
