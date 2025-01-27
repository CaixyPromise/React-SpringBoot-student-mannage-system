import React, {useEffect, useMemo, useState} from "react";
import {Button, DatePicker, Form, Input, message, Modal, Select, Spin, Switch} from "antd";
import {useForm} from "antd/es/form/Form";
import SemesterSelect from "@/components/SemesterSelect";
import {ClockCircleOutlined} from "@ant-design/icons";
import {
  getCourseSelectionInfoBySemesterIdUsingGet1, getCourseSelectSubjectByTaskIdUsingGet1,
} from "@/services/backend/courseSelectionInfoController";
import dayjs from "dayjs";
import {addRegistrationTaskUsingPost1} from "@/services/backend/registrationTaskController";

const {RangePicker} = DatePicker;

interface RegistrationScoreModalProps {
  visible: boolean;
  setVisible: (state: boolean) => void;
}
interface RegistrationScoreFormProps {
  name: string;
  taskDateRange: [dayjs.Dayjs, dayjs.Dayjs];
  semesterId: number;
  courseTaskId: number;
  subjectIds: number[];
  isActive: boolean;
}


const AddRegistrationScoreModal: React.FC<RegistrationScoreModalProps> = ({visible, setVisible}) => {
  const [form] = useForm<RegistrationScoreFormProps>()
  const [taskList, setTaskList] = useState<API.CourseSelectionInfoVO[]>([]);
  const [subjectList, setSubjectList] = useState<API.SubjectsVO[]>([]);

  const courseTaskId = Form.useWatch('courseTaskId', form);
  const subjectOption = useMemo(() => {
    return subjectList.map(subject => ({label: subject.name, value: subject.id}))
  }, [subjectList])
  const querySubjectInfo = async () => {
    try {
      const {data} = await getCourseSelectSubjectByTaskIdUsingGet1({
        taskId: courseTaskId
      })
      setSubjectList(data || []);
    } catch (e: Error) {
      message.error(`获取课程列表异常: ${e.message}`);
    }
  }
  useEffect(() => {
    if (courseTaskId) {
      querySubjectInfo();
    } else {
      // 当任务 ID 为空时，清空课程数据
      setSubjectList([]);
    }
    form.resetFields(['subjectIds']);
  }, [courseTaskId]);


  const selectedSemesterId = Form.useWatch("semesterId", form);

  const taskOption = useMemo(() => {
    return taskList.map(task => ({label: task.taskName, value: task.id}))
  }, [taskList])

  const queryTaskInfoList = async () => {
    try {
      const {data} = await getCourseSelectionInfoBySemesterIdUsingGet1({
        semesterId: selectedSemesterId
      });
      setTaskList(data || []);
    } catch (e: Error) {
      message.error(`获取课程任务列表异常: ${e.message}`);
    }
  }

  useEffect(() => {
    if (selectedSemesterId) {
      queryTaskInfoList()
    }
  }, [selectedSemesterId]);

  const onSubmit = async (value: RegistrationScoreFormProps) => {
    const { taskDateRange, isActive } = value;
    if (taskDateRange.length !== 2) {
      message.error("请选择登分时间范围");
      return;
    }
    // 把 dayjs 对象格式化成字符串
    const startStr = dayjs(taskDateRange[0]).format("YYYY-MM-DD HH:mm:ss");
    const endStr = dayjs(taskDateRange[1]).format("YYYY-MM-DD HH:mm:ss");

    const submissionData: API.RegistrationTaskAddRequest = {
      ...value,
      startDate: startStr,
      endDate: endStr,
      isActive: isActive ? 1 : 0,
    };
    console.log(submissionData)

    try {
      const {data} = await addRegistrationTaskUsingPost1({
        ...submissionData
      });
      if (data) {
        message.success("添加成功");
        setVisible(false);
        form.resetFields();
      }
    } catch (e: Error) {
      message.error(`添加失败: ${e.message}`);
    }
  }

  return (
    <Modal
      open={visible}
      onCancel={() => setVisible(false)}
      title="添加-登分任务"
      width={800}
      onOk={()=>{
        form.submit()
      }}
    >
      <Form form={form} onFinish={onSubmit}>
        <Form.Item
          label="任务名称"
          name='name'
          rules={[{required: true, message: '请输入任务名称',}, {max: 20, message: '任务名称长度不能超过20个字符'}]}
        >
          <Input placeholder='请输入任务名称'/>
        </Form.Item>
        <Form.Item
          label='选择学期'
          name='semesterId'
          rules={[{required: true, message: '请选择学期'}]}
        >
          <SemesterSelect/>
        </Form.Item>

        {
          selectedSemesterId && (
            <Spin spinning={taskList?.length === 0}>
              <Form.Item
                label='课程任务'
                name='courseTaskId'
                rules={[{required: true, message: '请选择课程任务'}]}
              >
                <Select
                  options={taskOption}
                />
              </Form.Item>
            </Spin>
          )
        }
        {
          courseTaskId && (
            <Form.Item
              label='选择课程'
              name='subjectIds'
              rules={[{required: true, message: '请选择课程'}]}
            >
              <Select
                options={subjectOption}
                mode='multiple'
              />
            </Form.Item>
          )
        }

        <Form.Item
          label='登分时间'
          name='taskDateRange'
          rules={[{required: true, message: '请选择登分时间'}]}
        >
          <RangePicker
            showTime
            format="YYYY-MM-DD HH:mm:ss"
            placeholder={["开始时间", "结束时间"]}
            suffixIcon={<ClockCircleOutlined/>}
          />
        </Form.Item>
        <Form.Item
          label="是否激活"
          name="isActive"
          valuePropName="checked" // 将 Switch 的状态与表单字段值绑定
          initialValue={false} // 默认值
        >
          <Switch />
        </Form.Item>
      </Form>
    </Modal>
  )
}


export default AddRegistrationScoreModal;
