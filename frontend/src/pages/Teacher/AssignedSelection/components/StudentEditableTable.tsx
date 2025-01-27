import React, {useEffect, useRef, useState} from 'react';
import {EditableProTable, ProColumns} from '@ant-design/pro-components';
import {InputNumber, message, Select, Tooltip, Button, Empty, Descriptions} from "antd";
import {InfoOutlined} from "@ant-design/icons";
import {getStudentsByCourseSelectionAndSubjectUsingGet1} from "@/services/backend/studentController";
import dayjs from "dayjs";
import {addStudentElectiveGradesUsingPost1} from "@/services/backend/scoreController";

interface StudentTableInfo extends API.StudentInfoVO {
  finalScore?: number;
  usualScore?: number;
  usualPercentage?: number;
  finalPercentage?: number;
  totalScore?: number;
}

const StudentEditableTable: React.FC<{
  courseSelectionId: number;
  subjectId: number;
  registeredScore: boolean;
  maxScore: number;
  registrationTaskInfo: API.HasRegistrationTaskVO;
}> = ({registrationTaskInfo, courseSelectionId, subjectId, registeredScore, maxScore}) => {
  const [columns, setColumns] = useState<ProColumns[]>([]);
  const [studentInfoVOList, setStudentInfoVOList] = useState<StudentTableInfo[]>([]);
  const [usualPercentage, setUsualPercentage] = useState<number>(50);
  const [isConfirmed, setIsConfirmed] = useState<boolean>(false);
  const [editableKeys, setEditableKeys] = useState<API.StudentInfoVO[]>([]);
  const [errorRows, setErrorRows] = useState<Set<number>>(new Set());
  const tableRef = useRef(null);
  const fetchStudentInfoVO = async () => {
    try {
      const {code, data} = await getStudentsByCourseSelectionAndSubjectUsingGet1({
        courseSelectionId,
        subjectId
      });
      if (code === 0 && data) {
        setStudentInfoVOList(data);
      } else {
        message.error('获取学生信息失败');
      }
    } catch (e) {
      message.error(`获取学生信息失败: ${e.message}`);
    }
  };

  useEffect(() => {
    fetchStudentInfoVO();
  }, [subjectId, courseSelectionId]);

  // 初始化可编辑行keys
  useEffect(() => {
    setEditableKeys(studentInfoVOList?.map(item => item?.id));
  }, [studentInfoVOList]);

  useEffect(() => {
    const baseColumns = [
      // {title: 'ID', dataIndex: 'id', editable: false},
      {title: '学号', dataIndex: 'stuId', editable: false},
      {title: '姓名', dataIndex: 'stuName', editable: false},
      {title: '性别', dataIndex: 'stuSex', editable: false},
      {title: '学院', dataIndex: 'stuDepart', editable: false},
      {title: '专业', dataIndex: 'stuMajor', editable: false},
      {title: '班级', dataIndex: 'stuClass', editable: false},
    ];
    const now = dayjs();

    if (registrationTaskInfo?.hasTask &&
      registrationTaskInfo?.startDate &&
      registrationTaskInfo?.endDate &&
      dayjs(registrationTaskInfo.startDate).isBefore(now) &&
      dayjs(registrationTaskInfo.endDate).isAfter(now)) {
      baseColumns.push(
        {
          title: (
            <div>
              平时分&nbsp;
              <Select
                value={usualPercentage}
                onChange={setUsualPercentage}
                style={{width: 80}}
              >
                {Array.from({length: 99}, (_, i) => (
                  <Select.Option key={i + 1} value={i + 1}>{i + 1}%</Select.Option>
                ))}
              </Select>
            </div>
          ),
          dataIndex: 'usualScore',
          editable: true,
          valueType: 'digit',
          renderFormItem: (_, {record, ...rest}, form) => (
            <InputNumber {...rest} min={0} max={maxScore}/>
          ),
        },
        {
          title: (
            <div>
              期末分&nbsp;
              <span>{100 - usualPercentage}%</span>
            </div>
          ),
          dataIndex: 'finalScore',
          editable: true,
          valueType: 'digit',
          renderFormItem: (_, {record, ...rest}, form) => (
            <InputNumber {...rest} min={0} max={maxScore}/>
          ),
        },
        {
          title: (
            <Tooltip title={<div>
              <span>平时分: {usualPercentage}% + 期末分: {100 - usualPercentage}% = {maxScore}</span><br/>
              <span>输入平时分和期末分自动计算总分</span>
            </div>}>
              <InfoOutlined/>总分&nbsp;
            </Tooltip>
          ),
          dataIndex: 'totalScore',
          editable: false,
          valueType: 'digit',
          render: (_, record) => {
            const usual = record.usualScore || 0;
            const fin = record.finalScore || 0;
            return (usual * (usualPercentage / 100)) + (fin * ((100 - usualPercentage) / 100));
          },
        }
      );
    }

    setColumns(baseColumns);
  }, [registeredScore, usualPercentage, maxScore]);

  const handleScoreSubmitToServer = async (values: StudentTableInfo[]) => {
    const submitData = {
      courseGroupId: registrationTaskInfo?.courseTaskId,  // 可以根据需要更改
      semesterId: registrationTaskInfo?.semesterId,     // 可以根据需要更改
      studentGradeInfos: values?.map(score => ({
        finalGrade: score.finalScore,
        finalPercentage: score.finalPercentage,
        stuId: score.stuId,
        totalGrade: score.totalScore,
        usualGrade: score.usualScore,
        usualPercentage: score.usualPercentage
      })),
      subjectId: subjectId,
      taskId: registrationTaskInfo?.taskId
    };
    console.log("提交数据: ", submitData);
    try {
      const {code} = await addStudentElectiveGradesUsingPost1({
        ...submitData
      });
      if (code === 0) {
        message.success('提交成功');
        setIsConfirmed(true)
      }
    } catch (e: Error) {
      message.error(`提交失败: ${e.message}`);
    }
  };

  const handleScoreSubmit = () => {
    const errorSet = new Set<number>(); // 用于记录出错的行
    const updatedList = studentInfoVOList.map((item, index) => {
      const isUsualScoreValid = item.usualScore !== undefined && item.usualScore !== null;
      const isFinalScoreValid = item.finalScore !== undefined && item.finalScore !== null;
      // 如果有空白成绩，标记出错的行
      if (!isUsualScoreValid || !isFinalScoreValid) {
        errorSet.add(item?.stuId);
      }
      // 计算总分
      return {
        ...item,
        usualPercentage,
        finalPercentage: 100 - usualPercentage,
        totalScore: (item.usualScore || 0) * (usualPercentage / 100) + (item.finalScore || 0) * ((100 - usualPercentage) / 100),
      };
    });

    // 更新表格数据
    setStudentInfoVOList(updatedList);

    // 如果存在空白成绩，阻止提交，并滚动到出错的行
    if (errorSet.size > 0) {
      setErrorRows(errorSet);
      message.error(`请填写所有成绩, 未填学号: ${Array.from(errorSet).join(', ')}`);

      // 滚动到第一行错误的地方
      if (tableRef.current) {
        const firstErrorRow = tableRef.current.querySelector(`[data-row-key='${Array.from(errorSet)[0]}']`);
        if (firstErrorRow) {
          firstErrorRow.scrollIntoView({behavior: 'smooth', block: 'center'});
        }
      }
      return;
    }
    // 如果没有错误，继续提交
    handleScoreSubmitToServer(updatedList);
  };

  return (
    <>
      <div style={{margin: 0, padding: 0, width: '100%'}}>
        <div>
          <div style={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}}>
            {
              registrationTaskInfo?.hasTask && (
                <Descriptions bordered>
                  <Descriptions.Item
                    span={3}
                    label="登分任务名称"
                  >
                    {registrationTaskInfo?.name}
                  </Descriptions.Item>
                  <Descriptions.Item
                    span={2}
                    label="登分开始时间"
                  >
                    {dayjs(registrationTaskInfo?.startDate).format('YYYY-MM-DD HH:mm')}
                  </Descriptions.Item>
                  <Descriptions.Item
                    span={2}
                    label="登分截止时间"
                  >
                    {dayjs(registrationTaskInfo?.endDate).format('YYYY-MM-DD HH:mm')}
                  </Descriptions.Item>
                  <Descriptions.Item
                    label="登分状态"
                    style={{
                      color: registrationTaskInfo?.isFinished === 1 ? 'green' : 'red',
                    }}>
                    {registrationTaskInfo?.isFinished === 1 ? '已登分' : '未登分'}
                  </Descriptions.Item>
                  {
                    registrationTaskInfo?.finishedTime &&
                    <Descriptions.Item
                      span={3}
                      label="登分时间"
                    >
                      {dayjs(registrationTaskInfo?.finishedTime).format('YYYY-MM-DD HH:mm')}
                    </Descriptions.Item>}
                </Descriptions>
              )
            }
          </div>
        </div>
        <EditableProTable
          ref={tableRef}
          style={{margin: 0, padding: 0, width: '100%'}}
          bordered
          rowKey="id"
          headerTitle="学生信息"
          columns={columns}
          value={studentInfoVOList}
          recordCreatorProps={false}
          editable={{
            type: 'multiple',
            editableKeys: editableKeys,
            actionRender: () => [],
            onValuesChange: (changedRecord, allRecords) => {
              setStudentInfoVOList(allRecords);
            },
            onChange: setEditableKeys,
          }}
          search={false}
          toolBarRender={false}
          pagination={false}
          locale={{
            emptyText: (
              <div style={{textAlign: 'center', padding: '20px 0'}}>
                <Empty
                  imageStyle={{width: 100, height: 100}}
                  style={{display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center'}}
                  description="暂无学生信息"
                />
              </div>
            ),
          }}
          rowClassName={(record, index) => errorRows.has(index) ? 'ant-table-row-error' : ''}
        />
        <div style={{textAlign: 'center', marginTop: 32}}>
          {!isConfirmed ? (
            <Button type="primary" onClick={() => {
              // 第一次点击确认：禁用编辑
              setIsConfirmed(true);
              setEditableKeys([]); // 禁用所有行的编辑
            }}>
              确认
            </Button>
          ) : (
            <>
              <Button type="primary" onClick={() => {
                // 第二次点击提交：收集数据并调用提交函数
                const submissionData = studentInfoVOList.map(item => ({
                  ...item,
                  usualScore: item.usualScore || null,
                  finalScore: item.finalScore || null,
                  usualPercentage: usualPercentage,
                  finalPercentage: 100 - usualPercentage,
                  totalScore: (item.usualScore || 0) * (usualPercentage / 100)
                    + (item.finalScore || 0) * ((100 - usualPercentage) / 100),
                }));
                handleScoreSubmit(submissionData);
              }}>
                提交
              </Button>
              <Button type='dashed' danger style={{marginLeft: 8}} onClick={() => {
                // 取消按钮：恢复编辑状态
                setIsConfirmed(false);
                setEditableKeys(studentInfoVOList?.map(item => item?.id));
              }}>
                取消
              </Button>
            </>
          )}
        </div>
      </div>
    </>
  );
};

export default StudentEditableTable;
