package com.caixy.adminSystem.model.vo.studentCourseSelection;

import com.caixy.adminSystem.model.entity.StudentCourseSelection;
import com.caixy.adminSystem.model.vo.UserVO;
import lombok.Data;

import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 学生选课信息表视图
 *


 */
@Data
public class StudentCourseSubjectVO implements Serializable {
    private Long subjectId;
    private String subjectName;
    private Double gradeCredit;

    private boolean selected;        // 是否已选
    private boolean full;            // 是否名额已满
    private Integer maxStudents;
    private Integer enrolledCount;
}
