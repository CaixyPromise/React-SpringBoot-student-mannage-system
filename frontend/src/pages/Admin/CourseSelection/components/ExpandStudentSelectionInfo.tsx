import {Button, Dropdown, Empty, Menu, MenuProps, message, Modal, Select, Space, Table, theme, Tooltip} from "antd";
import {DownloadOutlined, FunctionOutlined, MehOutlined, ReloadOutlined, SmileOutlined} from "@ant-design/icons";
import React, {useEffect, useState} from "react";
import {
  assignRandomCoursesToUnqualifiedStudentsUsingPost1,
  getAllSelectionsUsingGet1,
  getUnqualifiedStudentsUsingGet1
} from "@/services/backend/studentCourseSelectionController";
import * as XLSX from "xlsx";
import dayjs from "dayjs";
import styles from "../styles.scss";

interface ExpandedSubjectInfoProps {
  visible: boolean;
  onClose: () => void;
  courseSelectionItem: API.CourseSelectionInfoVO;
}

type FilterType = "all" | "unqualified";

const ExpandStudentSelectionInfo: React.FC<ExpandedSubjectInfoProps> = ({visible, onClose, courseSelectionItem}) => {
  const [studentsData, setStudentsData] = useState<API.StudentWithCourseSelectionVO[]>([]);
  const [unqualifiedStudentsData, setUnqualifiedStudentsData] = useState<API.StudentWithCourseSelectionVO[]>([]);
  const [filterType, setFilterType] = useState<FilterType>("all");
  const [loading, setLoading] = useState(false);
  // 1. 请求数据
  const fetchStudents = async () => {
    if (!courseSelectionItem?.id) return;
    setLoading(true);
    try {
      const [allStudentsRes, unqualifiedStudentsRes] = await Promise.all([
        getAllSelectionsUsingGet1({courseSelectionId: courseSelectionItem?.id}),
        getUnqualifiedStudentsUsingGet1({courseSelectionId: courseSelectionItem?.id})
      ]);
      if (allStudentsRes.code === 0) setStudentsData(allStudentsRes.data);
      if (unqualifiedStudentsRes.code === 0) setUnqualifiedStudentsData(unqualifiedStudentsRes.data);
    } catch (error) {
      message.error("网络错误，无法获取数据");
    }
    setLoading(false);
  };

  useEffect(() => {
    if (visible && courseSelectionItem?.id) fetchStudents();
  }, [visible, courseSelectionItem]);

  // 2. 导出 Excel
  const renderToExcel = (dataRows: API.StudentWithCourseSelectionVO[], fileName: string) => {
    if (dataRows.length === 0) {
      message.warning("没有数据可导出");
      return;
    }

    const dataForExcel = dataRows?.map(student => ({
      "学号": student.stuId,
      "姓名": student.stuName,
      "学院": student.stuDepart,
      "专业": student.stuMajor,
      "班级": student.stuClass,
      "已选学分": student.totalCredit,
      ...(student.requiredCredit !== null && student.requiredCredit !== undefined
        ? { "缺少学分": student.requiredCredit }
        : {}), // 仅当 requiredCredit 存在时添加
      "已选科目": student?.selectedSubjects?.map(sub => sub.name).join("，") || "未选课"
    }));


    const worksheet = XLSX.utils.json_to_sheet(dataForExcel);
    const workbook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(workbook, worksheet, "选课数据");
    XLSX.writeFile(workbook, `${fileName}-选课数据.xlsx`);
    message.success("数据已导出");
  };

  const exportToExcel = (exportType: FilterType) => {
    const exportData = exportType === "all" ? studentsData : unqualifiedStudentsData;
    if (exportData.length === 0) {
      message.warning("没有数据可导出");
      return;
    }
    renderToExcel(exportData, exportType === "all" ? "全部学生选课信息" : "未达学分要求学生选课信息");
  }

  // 3. 学生列表 Table 配置
  const studentColumns = [
    {title: "学号", dataIndex: "stuId", key: "stuId"},
    {title: "姓名", dataIndex: "stuName", key: "stuName"},
    {title: "学院", dataIndex: "stuDepart", key: "stuDepart"},
    {title: "专业", dataIndex: "stuMajor", key: "stuMajor"},
    {title: "班级", dataIndex: "stuClass", key: "stuClass"},
    {
      title: "当前已选学分", dataIndex: "totalCredit", key: "totalCredit",
      render: (text: string, record: API.StudentWithCourseSelectionVO) => {
        const {totalCredit} = record;
        if (courseSelectionItem?.minCredit) {
          if (totalCredit !== undefined && totalCredit < courseSelectionItem?.minCredit) {
            console.log("totalCredit: ", totalCredit)
            return <Tooltip title='学分不足'>
              <span style={{
                color: 'red',
                fontWeight: 'bold',
              }}>{text}</span>
            </Tooltip>;
          }
        }
        return text;
      }
    },
    {title: "最低学分", dataIndex: "requiredCredit", key: "requiredCredit", render: (value) => value ?? "无要求"},
  ];

  // 4. 课程列表 Table 配置（展开行）
  const expandedRowRender = (record: API.StudentWithCourseSelectionVO) => {
    if (!record.selectedSubjects || record.selectedSubjects.length === 0) {
      return <Empty description={<span style={{color: 'red'}}>未选课</span>}/>
    }

    return (
      <Table
        columns={[
          {title: "课程名称", dataIndex: "name", key: "name"},
          {
            title: "课程类型",
            dataIndex: "courseType",
            key: "courseType",
            render: (val) => (val === 0 ? "必修" : "选修")
          },
          {title: "学分", dataIndex: "gradeCredit", key: "gradeCredit"},
          {title: "学时", dataIndex: "creditHours", key: "creditHours"},
          {title: "最高分", dataIndex: "gradeMax", key: "gradeMax"},
          {title: "最低分", dataIndex: "gradeMin", key: "gradeMin"},
          {title: "及格分", dataIndex: "gradeFail", key: "gradeFail"},
          {title: "优秀分", dataIndex: "gradeExcellent", key: "gradeExcellent"},
        ]}
        dataSource={record.selectedSubjects}
        rowKey="id"
        pagination={false}
        size="small"
      />
    );
  };

// 定义菜单项
  const items: MenuProps["items"] = [
    {
      key: "all",
      icon: <SmileOutlined />,
      label: "导出全部",
    },
    {
      key: "unqualified",
      icon: <MehOutlined />,
      label: "导出未达标",
    },
  ];

// 处理菜单点击事件
  const handleMenuClick: MenuProps["onClick"] = (e) => {
    exportToExcel(e.key);
  };

  function handleRandomAssign() {
    let taskCount = 0;
    const {minCredit} = courseSelectionItem ?? 0;
    studentsData.forEach(item => {
      if (minCredit && item.totalCredit < minCredit) {
        taskCount++;
      }
    })
    if (taskCount <= 0) {
      message.info("没有需要随机分配的学生");
      return;
    }
    Modal.confirm({
      title: "随机分配课程",
      content: <>
        <div>
          <p>确定要随机分配课程吗？预计随机分配 {taskCount} 个学生</p>
          <p>分配对象：未选以及未选满最低学分要求的学生</p>
        </div>
      </>,
      onOk: async () => {
        if (courseSelectionItem?.id === undefined) {
          message.error("课程选择任务不存在");
          return;
        }
        // 检查时间，必须时间已经结束才能随机选课
        if (dayjs(courseSelectionItem?.endDate) > dayjs()) {
          message.error("选课时间未结束，无法随机分配");
          return;
        }
        try {
          const {code, data} = await assignRandomCoursesToUnqualifiedStudentsUsingPost1({
            courseSelectionId: courseSelectionItem?.id,
          })
          if (code === 0) {
            message.success(`随机分配成功, 分配 ${data} 名学生`);
          }
          await fetchStudents();
        } catch (e: Error) {
          message.error(e.message);
        }
      }
    })
  }

  return (
    <Modal title="学生选课详情" open={visible} onCancel={onClose} width={1300} footer={null}>
      {/* 工具栏 */}
      <Space style={{marginBottom: "16px", display: "flex", justifyContent: "space-between"}}>
        <div>
          <div style={{
            display: "flex",
            flexDirection: "column",
            backgroundColor: "#f0f0f0",
            padding: "10px",
            borderRadius: "5px",
          }}>
            <h3 style={{display: 'flex', margin: 0, fontSize: "18px", gap: "5px"}}>
              <span>选课任务名称: {courseSelectionItem.taskName} |</span>
              <span>学期: {courseSelectionItem.semesterName}  </span>
              <span style={{color: 'red'}}>
                | 最低学分要求: {courseSelectionItem?.minCredit?.toFixed(2)} 学分
              </span>

            </h3>
            <span style={{display: 'flex', color: "#888", fontSize: "14px", gap: "5px"}}>
              <span>开始时间:{" "}{dayjs(courseSelectionItem.startDate).format("YYYY-MM-DD HH:mm")} |</span>
              <span>结束时间:{" "}{dayjs(courseSelectionItem.endDate).format("YYYY-MM-DD HH:mm")} </span>
              {
                unqualifiedStudentsData?.length && <span style={{color: 'red'}}>
                 | 未完成学生人数: {unqualifiedStudentsData.length}人
                </span>
              }
            </span>
          </div>
        </div>
        <div style={{
          display: "flex",
          gap: "10px"
        }}>
          <Select
            style={{
              minWidth: "200px"
            }}
            value={filterType}
            onChange={setFilterType}
            options={[
              {value: "all", label: "全部学生信息"},
              {value: "unqualified", label: "未达标学生"}
            ]}
          />
          <Button type="primary" icon={<ReloadOutlined/>} onClick={fetchStudents} loading={loading}>
            刷新数据
          </Button>
          <Dropdown menu={{ items, onClick: handleMenuClick }}>
            <Button icon={<DownloadOutlined />}>导出 Excel</Button>
          </Dropdown>
          {
            courseSelectionItem?.endDate && dayjs(courseSelectionItem.endDate).isBefore(dayjs()) && (
              <Button
                type='dashed'
                icon={<FunctionOutlined/>}
                onClick={handleRandomAssign}
              >
                随机安排课程
              </Button>
            )
          }
        </div>
      </Space>

      {/* 学生信息 Table */}
      <Table
        columns={studentColumns}
        dataSource={filterType === "all" ? studentsData : unqualifiedStudentsData}
        rowKey="studentId"
        loading={loading}
        expandable={{expandedRowRender}}
        pagination={{pageSize: 20}}
        style={styles}
      />
    </Modal>
  );
}

export default ExpandStudentSelectionInfo;
