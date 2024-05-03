declare namespace Student {
    /**
     * 学生信息请求体-> 表单输入
     *
     * @author CAIXYPROMISE
     * @since 2024/5/1 下午2:02
     * @version 1.0
     */
    interface PayloadBody
    {
        stuName: string;
        stuSex: number;
        stuDeptId: string;
        stuMajorId: string;
        stuClassId: string;
    }

    /**
     * 当前选中行数据
     *
     * @author CAIXYPROMISE
     * @since 2024/5/1 下午2:36
     * @version 1.0
     */
    interface CurrentRowProps
    {
        id: string,
        subjectName: string[],
        stuName: string,
        stuSex: number
    }

    /**
     * 成绩表单模块框配置
     *
     * @author CAIXYPROMISE
     * @since 2024/5/3 下午9:56
     * @version 1.0
     */
    interface ScoreModalProps {
        scoreModalVisible: boolean;
        setScoreModalVisible: (visible: boolean) => void;
        currentRow: CurrentRowProps;
        subjectItem: OptionProps[]
    }

    interface StudentSexProps {
        useSex: string
    }

}
