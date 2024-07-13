import {message} from "antd";

const validateScore = async (
    score: number,
    subjectId: string,
    subjectMap: {
    [key: string]: OptionProps
}) => {
    const subject = subjectMap[subjectId];
    if (subject && subject.value === subjectId)
    {
        // @ts-ignore
        if (Number(subject.gradeMax) < Number(score) || Number(score) < Number(subject.gradeMin))
        {
            message.error(`提交失败：${subject.label}-成绩不符合要求}`);
            // @ts-ignore
            message.error(`科目要求: 最大${subject.gradeMax}，最小${subject.gradeMin}; 当前: ${score}`)
            return false;
        }
    }
    return true;
}


export {
    validateScore
}
