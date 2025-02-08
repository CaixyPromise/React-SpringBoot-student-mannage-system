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
    private Long id;
    private String teacherId;
    private String teacherName;
    private Integer teacherSex;

    private TeacherDepart teacherDepart;

    @Data
    public static class TeacherDepart {
        private Long departId;
        private Long majorId;
        private Long classId;
    }

    private static final long serialVersionUID = 1L;
}