import React from "react";
import {UserOutlined} from "@ant-design/icons";


const StudentSex: React.FC<Student.StudentSexProps> = ({ useSex }) =>
{
    if (!useSex)
    {
        return null;
    }

    const isMale = useSex === "1";
    const color = isMale ? "blue" : "pink";
    const label = isMale ? "男" : "女";

    return (
        <span aria-label={label} style={{ color }}>
            <UserOutlined color={color}/> <a>{label}</a>
        </span>
    );
};

export default StudentSex;
