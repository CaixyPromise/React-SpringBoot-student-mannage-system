import { Cascader, Form, Input, message, Modal, Select } from "antd";
import React, { useEffect, useMemo, useState } from "react";
import { userSexOption } from "@/pages/Admin/Student/Columns/option";
import { fetchCollegeCascadeOption } from "@/utils/server";
import {
  addTeacherInfoUsingPost1,
  updateTeacherInfoUsingPost1,
} from "@/services/backend/teacherInfoController";
import {ActionType} from "@ant-design/pro-components";

interface OperateModalProps {
  visible: boolean;
  setVisible: (visible: boolean) => void;
  defaultValue: API.TeacherInfoVO;
  actionRef: React.RefObject<ActionType>
}

interface TeacherFormProp {
  teacherId?: string;
  teacherName?: string;
  teacherSex?: number;
  teacherCollegeInfo?: number[];
}

function filterToCollegeAndMajor(data) {
  return data.map((item) => ({
    value: item.value,
    label: item.label,
    children: item.children?.map((child) => ({
      value: child.value,
      label: child.label,
    })),
  }));
}

const packagePayload = (formValue: TeacherFormProp) => {
  if (!formValue || formValue?.teacherCollegeInfo?.length !== 2) {
    return null;
  }
  const [teacherDeptId, teacherMajorId] = formValue.teacherCollegeInfo;
  return {
    ...formValue,
    teacherDeptId,
    teacherMajorId,
  };
};

const handleAdd = async (formValue: TeacherFormProp) => {
  if (!formValue) {
    return false;
  }
  try {
    const { code } = await addTeacherInfoUsingPost1({
      ...formValue,
    });
    if (code === 0) {
      message.success("添加成功");
      return true;
    }
  } catch (e: any) {
    message.error(`添加失败: ${e.message}`);
  }
  return false;
};

const handleUpdate = async (formValue: TeacherFormProp & { id: number }) => {
  if (!formValue || !formValue?.id) {
    return false;
  }
  try {
    const { code } = await updateTeacherInfoUsingPost1({
      ...formValue,
    });
    if (code === 0) {
      message.success("更新成功");
      return true;
    }
  } catch (e: any) {
    message.error(`更新失败: ${e.message}`);
  }
  return false;
};

const TeacherOperateModal: React.FC<OperateModalProps> = ({
  visible,
  setVisible,
  defaultValue,
  actionRef
}) => {
  const [form] = Form.useForm<TeacherFormProp>();
  const [cascadeOptions, setCascadeOptions] = useState([]);

  useEffect(() => {
    const loadOptions = async () => {
      const options = await fetchCollegeCascadeOption();
      const filteredOptions = filterToCollegeAndMajor(options);
      setCascadeOptions(filteredOptions);
    };
    loadOptions();
  }, []);

  const isUpdateMode = useMemo(() => defaultValue?.id !== undefined, [defaultValue]);

  // 设置表单默认值
  useEffect(() => {
    if (visible && defaultValue) {
      form.setFieldsValue({
        teacherId: defaultValue?.teacherId,
        teacherName: defaultValue?.teacherName,
        teacherSex: defaultValue?.teacherSex,
        teacherCollegeInfo: defaultValue?.teacherDeptId && defaultValue?.teacherMajorId
          ? [defaultValue.teacherDeptId, defaultValue.teacherMajorId]
          : undefined,
      });
    }
  }, [defaultValue, visible, form]);

  const handleSubmit = async (values: TeacherFormProp) => {
    const payload = packagePayload(values);
    if (!payload) {
      message.error("表单数据不完整");
      return;
    }

    let success = false;
    if (isUpdateMode) {
      success = await handleUpdate({ ...payload, id: defaultValue?.id! });
    } else {
      success = await handleAdd(payload);
    }

    if (success) {
      setVisible(false);
      actionRef?.current?.reload()
      form.resetFields();
    }
  };

  return (
    <Modal
      title={`教师操作 - ${isUpdateMode ? "更新" : "添加"}`}
      open={visible}
      onCancel={() => setVisible(false)}
      afterClose={() => {
        form.resetFields();
      }}
      onOk={() => form.submit()}
    >
      <Form form={form} onFinish={handleSubmit}>
        <Form.Item name="teacherId" label="教师工号" rules={[{ required: true }]}>
          <Input
            disabled={isUpdateMode}
            placeholder="请输入教师工号"
          />
        </Form.Item>
        <Form.Item name="teacherName" label="教师姓名" rules={[{ required: true }]}>
          <Input placeholder="请输入教师姓名" />
        </Form.Item>
        <Form.Item
          name="teacherCollegeInfo"
          label="所属院系"
          rules={[{ required: true, message: "请选择所属院系" }]}
        >
          <Cascader
            placeholder="请选择所属院系"
            options={cascadeOptions}
            allowClear
          />
        </Form.Item>
        <Form.Item name="teacherSex" label="教师性别" rules={[{ required: true }]}>
          <Select placeholder="请选择教师性别" options={userSexOption} />
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default TeacherOperateModal;
