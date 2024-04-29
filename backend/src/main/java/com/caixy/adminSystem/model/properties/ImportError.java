package com.caixy.adminSystem.model.properties;

import lombok.Data;

import java.io.Serializable;

/**
 * 导入错误信息
 *
 * @name: com.caixy.adminSystem.model.properties.ImportError
 * @author: CAIXYPROMISE
 * @since: 2024-04-01 19:37
 **/
@Data
public class ImportError implements Serializable
{
    /**
     * 学院名称
     */
    private String collegeName;

    /**
     * 专业名称
     */
    private String majorName;

    /**
     * 错误类系
     */
    private String errorType;

    private static final long serialVersionUID = 1L;
}
