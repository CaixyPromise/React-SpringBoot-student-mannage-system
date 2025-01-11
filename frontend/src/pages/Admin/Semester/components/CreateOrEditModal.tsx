import React from 'react';
import { ModalForm, ProFormDatePicker, ProFormText, ProFormSwitch } from '@ant-design/pro-components';
import {Form, message} from 'antd';
import {addSemestersUsingPost1, updateSemestersUsingPost1} from "@/services/backend/semestersController";

type Props = {
  visible: boolean;
  onCancel: () => void;
  onSuccess: () => void;
  record?: API.Semesters; // 如果传入则是编辑，否则新增
};

const CreateOrEditModal: React.FC<Props> = ({ visible, onCancel, onSuccess, record }) => {
  const [form] = Form.useForm<API.SemestersVO>();

  const isEdit = !!record?.id;

  return (
    <ModalForm<API.SemestersVO>
      title={isEdit ? '编辑学期' : '新增学期'}
      visible={visible}
      form={form}
      initialValues={record}
      modalProps={{
        onCancel,
      }}
      onFinish={async (values) => {
        const hide = message.loading('正在提交...');
        try {
          if (isEdit && record?.id) {
            // 编辑
            await updateSemestersUsingPost1({
              id: record.id,
              name: values.name,
              startDate: values.startDate,
              endDate: values.endDate,
            });
          } else {
            // 新增
            await addSemestersUsingPost1({
              name: values.name || '',
              startDate: values.startDate,
              endDate: values.endDate,
            });
          }
          hide();
          message.success(isEdit ? '更新成功' : '新增成功');
          onSuccess();
          return true;
        } catch (error) {
          hide();
          message.error(isEdit ? '更新失败' : '新增失败');
          return false;
        }
      }}
    >
      <ProFormText
        name="name"
        label="学期名称"
        placeholder="请输入学期名称"
        rules={[{ required: true, message: '学期名称不能为空' }]}
      />
      <ProFormDatePicker
        name="startDate"
        label="开始日期"
        rules={[{ required: true, message: '请选择开始日期' }]}
      />
      <ProFormDatePicker
        name="endDate"
        label="结束日期"
        rules={[{ required: true, message: '请选择结束日期' }]}
      />
    </ModalForm>
  );
};

export default CreateOrEditModal;
