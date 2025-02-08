package com.caixy.adminSystem.model.dto.studentInfo;

import com.caixy.adminSystem.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 *
 
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StudentInfoQueryRequest extends PageRequest implements Serializable {
    private Long id;
    private String studentId;
    private String stuName;
    private Integer stuSex;

    private StudentInfoQueryRequest.StuDepart stuDepart;

    @Data
    public static class StuDepart {
        private Long departId;
        private Long majorId;
        private Long classId;
    }
    private static final long serialVersionUID = 1L;
}