package com.caixy.adminSystem.model.vo.ClassesInfo;

import lombok.Data;

/**
 * 学院->专业->班级树节点
 *
 * @Author CAIXYPROMISE
 * @since 2025/1/3 14:12
 */
@Data
public class ClassLeafVO {
    private Long classId;
    private String className;
}