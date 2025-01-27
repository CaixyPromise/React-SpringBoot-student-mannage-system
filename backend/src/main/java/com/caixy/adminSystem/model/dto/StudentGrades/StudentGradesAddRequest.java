package com.caixy.adminSystem.model.dto.StudentGrades;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 创建请求
 */
@Data
public class StudentGradesAddRequest implements Serializable
{
    /**
     * 科目id
     */
    @NotNull(message = "科目不能为空")
    private Long subjectId;

    /**
     * 登分任务id
     */
    @NotNull(message = "登分任务不能为空")
    private Long taskId;

    /**
     * 课程组id（选课任务id/必修课任务id）
     */
    @NotNull(message = "课程组不能为空")
    private Long courseGroupId;

    /**
     * 学期id
     */
    @NotNull(message = "学期不能为空")
    private Long semesterId;

    /**
     * 学生成绩信息
     */
    @Size(min = 1, message = "学生成绩信息不能为空")
    private List<StudentGradeInfo> studentGradeInfos;

    @Data
    public static class StudentGradeInfo
    {
        /**
         * 学号
         */
        @NotEmpty(message = "学号不能为空")
        private String stuId;

        /**
         * 成绩
         */
        @NotNull(message = "成绩不能为空")
        private Long totalGrade;

        /**
         * 平时成绩
         */
        @NotNull(message = "平时成绩不能为空")
        private Integer usualGrade;

        /**
         * 期末成绩
         */
        @NotNull(message = "期末成绩不能为空")
        private Integer finalGrade;

        /**
         * 平时分比例
         */
        @NotNull(message = "平时分比例不能为空")
        private Integer usualPercentage;

        /**
         * 期末分比例
         */
        @NotNull(message = "期末分比例不能为空")
        private Integer finalPercentage;
    }

    private static final long serialVersionUID = 1L;
}