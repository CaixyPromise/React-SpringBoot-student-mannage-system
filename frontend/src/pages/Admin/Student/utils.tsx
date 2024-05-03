import {UserOutline} from "antd-mobile-icons";
import React from "react";


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
            <UserOutline color={color}/> <a>{label}</a>
        </span>
    );
};

export default StudentSex;
