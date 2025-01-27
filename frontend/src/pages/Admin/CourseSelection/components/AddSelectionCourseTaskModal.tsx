// PostCourseSelection.tsx
import React, { useRef } from "react";
import {  message, Modal, Button } from "antd";
import {addCourseSelectionInfoUsingPost1} from "@/services/backend/courseSelectionInfoController";
import SelectCourseTaskForm, {
  SelectCourseFormProps,
  SelectCourseTaskFormRef
} from "@/pages/Admin/CourseSelection/components/SelectCourseTaskForm";

interface PostCourseSelectionProps {
  visible: boolean;
  setVisible: (visible: boolean) => void;
}

const PostCourseSelection: React.FC<PostCourseSelectionProps> = ({ visible, setVisible }) => {
  const formRef = useRef<SelectCourseTaskFormRef>(null);

  /** 提交表单 */
  const handleSubmit = async (values: SelectCourseFormProps) => {
    console.log("values: ", values)
    try {
      console.log("提交的表单数据: ", values);
      if (!values) {
        return
      }
      const result = await addCourseSelectionInfoUsingPost1(values);
      if (result.code === 0) {
        message.success("提交成功");
        setVisible(false);
      } else {
        message.error(`提交失败: ${result.message}`);
      }
      message.success("提交成功！");
      setVisible(false);
    } catch (error: Error) {
      console.error("表单提交失败", error);
      message.error(`提交失败，${error.message}！请检查表单内容`);
    }
  };

  /** 点击提交按钮时触发子组件的 submit 方法 */
  const onSubmit = async () => {
    if (formRef.current) {
      try {
        await formRef.current.submit();
      } catch (error) {
        console.error("调用子组件提交方法失败:", error);
      }
    }
  };

  return (
    <Modal
      open={visible}
      title="发布选课任务"
      width={700}
      onCancel={() => setVisible(false)}
      footer={[
        <Button key="cancel" onClick={() => setVisible(false)}>
          取消
        </Button>,
        <Button key="submit" type="primary" onClick={onSubmit}>
          提交
        </Button>,
      ]}
    >
      <SelectCourseTaskForm
        ref={formRef}
        submitCallback={handleSubmit}
        excludeClassIds={[]}
      />
    </Modal>
  );
};

export default PostCourseSelection;
