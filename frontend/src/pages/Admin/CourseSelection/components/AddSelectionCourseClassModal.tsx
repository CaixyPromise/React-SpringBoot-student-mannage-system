import React, {useEffect, useState} from "react";
import {Descriptions, Divider, Form, Modal, Spin} from "antd";
import {getCourseSelectionInfoVoByIdUsingGet1} from "@/services/backend/courseSelectionInfoController";
import dayjs from "dayjs";
import {useCourseSelection} from "@/pages/Admin/CourseSelection/contexts/CourseSelectionContext";
import SelectCourseTaskForm, {SelectCourseFormProps} from "@/pages/Admin/CourseSelection/components/SelectCourseTaskForm";

interface AddSelectionCourseClassModalProps {
  visible: boolean;
  onCancel: () => void;
  courseId: number;
  selectedClassIds: Array<number>;
}

const AddSelectionCourseClassModal: React.FC = () => {
  const [courseInfo, setCourseInfo] = useState<API.CourseSelectionInfoVO>({});
  const [loading, setLoading] = useState<boolean>(false);
  const [form] = Form.useForm<SelectCourseFormProps>();
  const {tableDataDict, courseModalVisible, courseModalOnCancel, courseId, selectedClassIds} = useCourseSelection();

  const queryTaskInfo = async () => {
    const {code, data} = await getCourseSelectionInfoVoByIdUsingGet1({
      id: courseId,
    })
    if (code === 0) {
      const dataInfo = {
        ...data,
        ...tableDataDict[courseId],
        // selectedGrade:
      }
      setCourseInfo(dataInfo)
      form.setFieldsValue(dataInfo)
    }
  }

  useEffect(()=> {
    if (courseId) {
      setLoading(true)
      Promise.all([
        queryTaskInfo()
      ]).then(()=>{
        setLoading(false)
      })
    }
  }, [courseId])

  const handleSubmit = (values: SelectCourseFormProps) => {
    console.log(values)
  }

  return (
    <Modal
      title="添加选课班级"
      onCancel={courseModalOnCancel}
      open={courseModalVisible}
      width={700}
    >
      <Spin spinning={loading}>
        <div>
          <Descriptions bordered>
            <Descriptions.Item label="课程名称" span={3}>{courseInfo.subjectName}</Descriptions.Item>
            <Descriptions.Item label="课程编号" span={3}>{courseInfo.subjectId}</Descriptions.Item>
            <Descriptions.Item label="选课学期" span={3}>{courseInfo.semesterName}</Descriptions.Item>
            <Descriptions.Item label="课程最大选课人数" span={2}>{courseInfo.maxStudents}</Descriptions.Item>
            <Descriptions.Item label="已选人数" span={2}>{courseInfo.enrolledCount}</Descriptions.Item>
            <Descriptions.Item label="选课开始时间">{dayjs(courseInfo.startDate).format("YYYY-MM-DD HH:mm:ss")}</Descriptions.Item>
            <Descriptions.Item label="选课结束时间">{dayjs(courseInfo.endDate).format("YYYY-MM-DD HH:mm:ss")}</Descriptions.Item>
          </Descriptions>
        </div>
        <Divider />
        <SelectCourseTaskForm
          form={form}
          submitCallback={handleSubmit}
          excludeClassIds={selectedClassIds}
        />
      </Spin>
    </Modal>
  )
}


export default AddSelectionCourseClassModal;
