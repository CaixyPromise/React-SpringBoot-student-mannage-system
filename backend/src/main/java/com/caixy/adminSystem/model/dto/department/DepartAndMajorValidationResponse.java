package com.caixy.adminSystem.model.dto.department;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 验证学院与专业信息是否匹配合格对象封装
 *
 * @name: com.caixy.model.dto.department.DepartAndMajorValidationResponse
 * @author: CAIXYPROMISE
 * @since: 2024-02-12 01:15
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartAndMajorValidationResponse implements Serializable
{
    /**
     * 是否合格
     */
    private Boolean isValid;

    private static final  long serialVersionUID = 1L;
}
