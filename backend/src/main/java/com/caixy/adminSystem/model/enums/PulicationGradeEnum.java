package com.caixy.adminSystem.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 发布成绩枚举
 *
 * @Author CAIXYPROMISE
 * @since 2025/1/27 0:50
 */
@Getter
@AllArgsConstructor
public enum PulicationGradeEnum
{
    ACTIVE(1, "激活"),
    INACTIVE(0, "未激活");

    private final Integer code;
    private final String desc;

    public static PulicationGradeEnum getEnumByCode(Integer code)
    {
        if (code == null)
        {
            return null;
        }
        for (PulicationGradeEnum value : PulicationGradeEnum.values())
        {
            if (value.getCode().equals(code))
            {
                return value;
            }
        }
        return null;
    }
}
