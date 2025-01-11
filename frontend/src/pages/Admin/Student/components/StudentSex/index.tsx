import React from "react";
import {UserOutlined} from "@ant-design/icons";

const StudentSex: React.FC<{ useSex?: string | number }> = ({ useSex }) => {
  if (useSex === undefined || useSex === null) {
    return null;
  }

  // 确保 useSex 是字符串，方便比较
  const sexString = String(useSex).trim();

  const isMale = sexString === "1"; // 男性标识为 "1"
  const color = isMale ? "blue" : "pink";
  const label = isMale ? "男" : "女";

  return (
    <span aria-label={label} style={{ color }}>
      <UserOutlined color={color}/> <a>{label}</a>
    </span>
  );
};

export default StudentSex;
