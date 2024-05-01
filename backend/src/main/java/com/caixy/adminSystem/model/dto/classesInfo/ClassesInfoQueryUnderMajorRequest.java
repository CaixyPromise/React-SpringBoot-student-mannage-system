package com.caixy.adminSystem.model.dto.classesInfo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 查找专业之下的班级信息
 *
 * @name: com.caixy.adminSystem.model.dto.classesInfo.ClassesInfoQueryUnderMajorRequest
 * @author: CAIXYPROMISE
 * @since: 2024-04-29 18:41
 **/
@Data
public class ClassesInfoQueryUnderMajorRequest implements Serializable
{
    /**
     * 专业id
     */
    @NotNull
    private Long majorId;

    /**
     * 学院id
     */
    @NotNull
    private Long departmentId;

    private static final long serialVersionUID = 1L;
}
