package com.caixy.adminSystem.model.dto.teacherInfo;

import com.caixy.adminSystem.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 查询教师信息请求
 *


 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TeacherInfoQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;
    /**
     * 教师工号
     */
    private String teacherId;
    /**
    * 教师名字
    */
    private String teacherName;

    /**
     * 教师学院id
     */
    private Long teacherDeptId;

    /**
     * 教师专业id
     */
    private Long teacherMajorId;

    /**
     * 教师性别
     */
    private Integer teacherSex;

    private static final long serialVersionUID = 1L;
}