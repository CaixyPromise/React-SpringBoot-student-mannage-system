import { Cascader, message } from "antd";
import React, {useEffect, useMemo, useState} from "react";
import { getClassesOptionDataVoByPageUsingPost1 } from "@/services/backend/classesController";

interface ValueProps {
  departId?: string;
  majorId?: string;
  classId?: string;
}

const DepartmentSelect: React.FC<{
  onChange?: (value: ValueProps) => void;
  value?: ValueProps;
  ignoreLevels?: ("classId")[]; // 允许忽略 "classId"
}> = ({ onChange, value, ignoreLevels = [] }) => {
  const [cascadeOption, setCascadeOption] = useState([]);

  /** 🎯 1. 获取学院-专业-班级数据 **/
  const fetchCollegeCascadeOption = async () => {
    try {
      const { data, code } = await getClassesOptionDataVoByPageUsingPost1({});
      if (data && code === 0) {
        setCascadeOption(data);
      }
    } catch (e: any) {
      message.error("获取选项配置失败: " + e.message);
      setCascadeOption([]);
    }
  };

  useEffect(() => {
    fetchCollegeCascadeOption();
  }, []);

  /** 🎯 2. 处理选项值，支持忽略班级 **/
  const handleChange = (selectedValues: (string)[]) => {
    if (onChange) {
      let [departId, majorId, classId] = selectedValues.map((v) => (v));

      // **如果忽略班级，则 classId 设置为 `undefined`**
      if (ignoreLevels.includes("classId")) {
        classId = undefined;
      }

      onChange({ departId, majorId, classId });
    }
  };

  /** 🎯 3. 处理 `value` 转换为数组格式 **/
  const cascaderValue: (string )[] | undefined = value
    ? [value.departId, value.majorId, value.classId].filter(
      (v, index) =>
        v !== undefined && (index !== 2 || !ignoreLevels.includes("classId")) // 过滤掉被忽略的级联层级
    )
    : undefined;

  /** 🎯 4. 过滤级联选项，**移除忽略的层级** **/
  const filteredOptions = useMemo(() => {
    if (ignoreLevels.includes("classId")) {
      return cascadeOption.map((depart) => ({
        ...depart,
        children: depart?.children?.map((major) => ({
          ...major,
          children: undefined, // 移除班级
        })),
      }));
    }
    return cascadeOption;
  }, [cascadeOption, ignoreLevels]);

  return (
    <Cascader
      options={filteredOptions}
      onChange={handleChange}
      value={cascaderValue}
      placeholder="请选择学院 / 专业"
    />
  );
};

export default DepartmentSelect;
