import React, { useEffect, useState } from "react";
import { message, Select } from "antd";
import dayjs from "dayjs";
import { getSemestersUsingGet1 } from "@/services/backend/semestersController";

const { Option } = Select;

interface Semester {
  id: number;
  name: string;
  startDate: string;
  endDate: string;
  isActive: number; // 1 for active, 0 for inactive
}

const colorMapping: Record<number, string> = {
  1: "green", // Active semester
  0: "gray",  // Inactive semester
};

interface SemesterSelectProps {
  value?: number; // The selected semester ID
  onChange?: (value: number) => void; // Callback when the selection changes
  placeholder?: string; // Placeholder text
}

const SemesterSelect: React.FC<SemesterSelectProps> = React.forwardRef<HTMLDivElement, SemesterSelectProps>(
  ({ value, onChange, placeholder }, ref) => {
    const [semesterOptions, setSemesterOptions] = useState<Semester[]>([]);

    const querySemesters = async () => {
      try {
        const response = await getSemestersUsingGet1();
        if (response.code === 0 && response.data) {
          const sortedSemesters = response.data.sort((a: Semester, b: Semester) => {
            if (a.isActive === 1 && b.isActive !== 1) return -1;
            if (b.isActive === 1 && a.isActive !== 1) return 1;

            const dateA = new Date(a.startDate).getTime();
            const dateB = new Date(b.startDate).getTime();
            return dateB - dateA;
          });
          setSemesterOptions(sortedSemesters);
        } else {
          message.error(`获取学期信息失败: ${response.message}`);
        }
      } catch (error) {
        message.error(`获取学期信息错误: ${error}`);
      }
    };

    useEffect(() => {
      querySemesters();
    }, []);

    return (
      <Select
        ref={ref}
        value={value}
        onChange={onChange}
        placeholder={placeholder || "请选择学期"}
      >
        {semesterOptions.map((semester) => (
          <Option key={semester.id} value={semester.id}>
            <span style={{ color: colorMapping[semester.isActive] }}>
              {semester.name} [
              {`${dayjs(semester.startDate).format("YYYY-MM-DD")} ~ ${dayjs(
                semester.endDate
              ).format("YYYY-MM-DD")}`}
              ] {semester.isActive === 1 && "（当前学期）"}
            </span>
          </Option>
        ))}
      </Select>
    );
  }
);

export default SemesterSelect;
