package com.caixy.adminSystem.model.vo.ClassesInfo;

import lombok.Data;

import java.util.List;

/**
 * 学院->专业->班级树节点
 * @Author CAIXYPROMISE
 * @since 2025/1/3 14:11
 */
@Data
public class DepartMajorClassTreeVO {
    private Long departId;
    private String departName;
    private List<MajorClassTreeVO> children;
}
