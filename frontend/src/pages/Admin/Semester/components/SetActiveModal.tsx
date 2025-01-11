import React, { useEffect, useState } from 'react';
import {Modal, message, Select, Descriptions} from 'antd';
import {
  getCurrentSemesterUsingGet1,
  listSemestersByPageUsingPost1,
  setActiveSemesterUsingPost1
} from "@/services/backend/semestersController";
import dayjs from "dayjs";


// import { querySemesters, setActiveSemester } from '@/services/semesters';

type Props = {
  visible: boolean;
  onCancel: () => void;
  onSuccess: () => void;
};
const SetActiveModal: React.FC<Props> = ({ visible, onCancel, onSuccess }) => {
  /** 当前激活学期 */
  const [currentActiveSemester, setCurrentActiveSemester] = useState<API.SemestersVO | null>(null);
  /** 下拉列表里的可选学期（不包括已激活的学期） */
  const [semesterOptions, setSemesterOptions] = useState<API.SemestersVO[]>([]);
  /** 用户选中的学期id */
  const [selectedSemesterId, setSelectedSemesterId] = useState<number | undefined>(undefined);

  /**
   * 加载当前激活学期
   */
  const loadCurrentActiveSemester = async () => {
    try {
      const res = await getCurrentSemesterUsingGet1();
      if (res && res.data) {
        setCurrentActiveSemester(res.data);
      } else {
        setCurrentActiveSemester(null);
      }
    } catch (error) {
      message.error('获取当前激活学期失败');
    }
  };

  /**
   * 加载所有学期列表（分页查询），并过滤掉当前激活的学期
   */
  const loadAllSemesters = async () => {
    try {
      const res = await listSemestersByPageUsingPost1({
        current: 1,
        pageSize: 100, // 这里设置一个足够大的值
      });
      const records = res?.data?.records || [];
      // 过滤掉 isActive=1 的学期
      const nonActiveList = records.filter((item) => item.isActive !== 1);
      setSemesterOptions(nonActiveList);
    } catch (error) {
      message.error('获取学期列表失败');
    }
  };

  /**
   * Modal 显示/隐藏时的副作用
   */
  useEffect(() => {
    if (visible) {
      // 同步加载当前激活学期 & 所有非激活学期
      loadCurrentActiveSemester();
      loadAllSemesters();
      // 重置选中项
      setSelectedSemesterId(undefined);
    }
  }, [visible]);

  /**
   * 点击「确定」按钮
   */
  const handleOk = async () => {
    if (!selectedSemesterId) {
      message.warning('请选择要激活的学期');
      return;
    }
    const hide = message.loading('正在设置激活学期...');
    try {
      await setActiveSemesterUsingPost1({ id: selectedSemesterId });
      hide();
      message.success('设置激活学期成功');
      onSuccess();
    } catch (error) {
      hide();
      message.error('设置激活学期失败');
    }
  };

  return (
    <Modal
      title="设置激活学期"
      open={visible}
      onOk={handleOk}
      onCancel={onCancel}
    >
      {currentActiveSemester?.id ? (
        <Descriptions title="当前激活学期" column={1} bordered size="small">
          <Descriptions.Item label="ID">{currentActiveSemester.id}</Descriptions.Item>
          <Descriptions.Item label="名称">{currentActiveSemester.name}</Descriptions.Item>
          <Descriptions.Item label="开始日期">
            {dayjs(currentActiveSemester.startDate).format('YYYY-MM-DD')}
          </Descriptions.Item>
          <Descriptions.Item label="结束日期">
            {dayjs(currentActiveSemester.endDate).format('YYYY-MM-DD')}
          </Descriptions.Item>
        </Descriptions>
      ) : (
        <p>暂无激活学期</p>
      )}

      <div style={{ marginTop: 16 }}>
        <p>请选择要设置为激活的学期：</p>
        <Select
          style={{ width: '100%' }}
          placeholder="请选择学期"
          onChange={(value: number) => setSelectedSemesterId(value)}
          value={selectedSemesterId}
          allowClear
          options={semesterOptions.map((item) => ({
            label: item.name, // 下拉显示
            value: item.id,   // 选中值
          }))}
        />
      </div>
    </Modal>
  );
};

export default SetActiveModal;
