package com.caixy.adminSystem.model.properties;

import lombok.Data;

import java.io.Serializable;

/**
 * 专业信息对-用在导入时
 *
 * @name: com.caixy.adminSystem.model.properties.MajorInfoPair
 * @author: CAIXYPROMISE
 * @since: 2024-04-01 19:39
 **/
@Data
public class MajorInfoPair implements Serializable
{
    private String majorName;
    private String collegeName;
    private static final long serialVersionUID = 1L;
}
