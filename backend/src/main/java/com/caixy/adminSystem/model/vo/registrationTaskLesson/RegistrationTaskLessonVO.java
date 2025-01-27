package com.caixy.adminSystem.model.vo.registrationTaskLesson;

import com.caixy.adminSystem.model.vo.Subjects.SubjectsVO;
import com.caixy.adminSystem.model.vo.courseSelectionInfo.CourseSelectionInfoVO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 登分任务课程信息视图
 *


 */
@Data
public class RegistrationTaskLessonVO implements Serializable {
    /**
    * id
    */
    private Long id;

    /**
     * 登分任务id
     */
    private Long taskId;

    /**
     * 课程id
     */
    private SubjectsVO subjectsInfo;

    /**
     * 课程任务id
     */
    private CourseSelectionInfoVO courseSelectionInfo;
    /**
     * 是否登分完成
     */
    private Integer isFinished;

    /**
     * 是否发布成绩
     */
    private Integer isPublish;

    /**
     * 完成日期
     */
    private Date finishedTime;
}
