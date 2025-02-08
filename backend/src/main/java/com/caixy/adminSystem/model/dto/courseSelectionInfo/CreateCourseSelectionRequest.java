package com.caixy.adminSystem.model.dto.courseSelectionInfo;

import com.caixy.adminSystem.model.dto.subject.SubjectClassTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author CAIXYPROMISE
 * @since 2025/1/3 1:41
 */
@Data
public class CreateCourseSelectionRequest
{
    /**
     * 科目ID
     */
    private List<SelectCourseData> courseSettings;

    /**
     * 学期ID
     */
    private Long semesterId;

    /**
     * 选课开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startDate;


    /**
     * 选课结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endDate;

    /**
     * 适用的班级ID列表
     */
    private List<Long> classIds;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 最小学分要求
     */
    private Double minCredit;


    @Data
    public static class SelectCourseData
    {
        /**
         * 课程id
         */
        private Long courseId;
        /**
         * 最大选课人数，0 表示不限制
         */
        private Integer maxStudents;

        /**
         * 上课时间
         */
        private List<SubjectClassTime> classTimes;

        /**
         * 上课地点
         */
        private String classRoom;

        /**
         * 上课老师
         */
        private Long classTeacher;
    }

}
