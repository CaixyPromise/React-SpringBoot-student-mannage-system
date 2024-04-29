package com.caixy.adminSystem.model.dto.major;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户更新请求
 */
@Data
public class MajorInfoUpdateRequest implements Serializable
{
    /**
     * 专业id
     */
    private Long id;
    /**
     * 学院id
     */
    private Long departId;
    /**
     * 专业名称
     */
    private String name;

    private static final long serialVersionUID = 1L;
}