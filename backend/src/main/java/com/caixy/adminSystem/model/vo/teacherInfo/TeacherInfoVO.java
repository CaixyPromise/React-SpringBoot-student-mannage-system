package com.caixy.adminSystem.model.vo.teacherInfo;

import cn.hutool.json.JSONUtil;
import com.caixy.adminSystem.model.entity.TeacherInfo;
import com.caixy.adminSystem.model.vo.UserVO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 教师信息视图
 *


 */
@Data
public class TeacherInfoVO implements Serializable {
    /**
     * 教师id
     */
    private Long id;
    /**
    * 教师工号
    */
    private String teacherId;

    /**
     * 教师姓名
     */
    private String teacherName;

    /**
     * 教师性别
     */
    private Integer teacherSex;

    /**
     * 教师学院
     */
    private String teacherDepart;

    /**
     * 教师专业
     */
    private String teacherMajor;

    /**
     * 教师学院id
     */
    private Long teacherDeptId;

    /**
     * 教师专业id
     */
    private Long teacherMajorId;

    private static final long serialVersionUID = 1L;
}
