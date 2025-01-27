import React from "react";
import { Tag } from "antd";

const dayOfWeekMap = {
  1: "星期一",
  2: "星期二",
  3: "星期三",
  4: "星期四",
  5: "星期五",
  6: "星期六",
  7: "星期日",
};

interface ClassTime {
  dayOfWeek?: number;
  period?: string;
}

interface Props {
  classTimes?: ClassTime[];
}

const ClassTimesDisplay: React.FC<Props> = ({ classTimes }) => {
  if (!classTimes || classTimes?.length === 0) {
    return <span>暂无上课时间</span>;
  }

  return (
    <div>
      {classTimes?.map((time, index) => {
        const dayOfWeek = dayOfWeekMap[time?.dayOfWeek || 0] || "未知";
        const period = time.period || "未知节次";

        return (
          <Tag color="blue" key={index}>
            {`${dayOfWeek} - ${period}`} 节
          </Tag>
        );
      })}
    </div>
  );
};

export default ClassTimesDisplay;
