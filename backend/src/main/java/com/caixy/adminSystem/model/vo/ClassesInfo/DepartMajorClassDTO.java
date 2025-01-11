package com.caixy.adminSystem.model.vo.ClassesInfo;

import lombok.Data;

/**
 * 后端多表查询的结果封装类
 *
 * @Author CAIXYPROMISE
 * @since 2025/1/3 14:14
 */
@Data
public class DepartMajorClassDTO {
    private Long classId;     // 班级ID
    private String className; // 班级名称

    private Long majorId;     // 专业ID
    private String majorName; // 专业名称

    private Long departId;    // 学院ID
    private String departName;// 学院名称
}
