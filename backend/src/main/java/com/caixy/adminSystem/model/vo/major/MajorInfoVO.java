package com.caixy.adminSystem.model.vo.major;

import lombok.Data;

import java.io.Serializable;

/**
 * @name: com.caixy.model.vo.major.MajorInfoVO
 * @decription: MajorInfo
 * @author: CAIXYPROMISE
 * @since: 2024-02-10 23:56
 **/
@Data
public class MajorInfoVO implements Serializable
{
    /**
     * 专业名称
     */
    private String name;
    /**
     * 学院名称
     */
    private String departmentName;
    /**
     * 专业id
     */
    private Long majorId;
    /**
     * 学院id
     */
    private Long departmentId;
    private static final long serialVersionUID = 1L;
}
