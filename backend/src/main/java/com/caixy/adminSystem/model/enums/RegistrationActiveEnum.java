package com.caixy.adminSystem.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 登分任务状态枚举
 *
 * @Author CAIXYPROMISE
 * @since 2025/1/26 23:19
 */
@Getter
@AllArgsConstructor
public enum RegistrationActiveEnum
{
    ACTIVE(1, "激活"),
    INACTIVE(0, "未激活");

    private final Integer code;
    private final String desc;

    public static RegistrationActiveEnum getEnumByCode(Integer code)
    {
        if (code == null)
        {
            return null;
        }
        for (RegistrationActiveEnum value : RegistrationActiveEnum.values())
        {
            if (value.getCode().equals(code))
            {
                return value;
            }
        }
        return null;
    }
}
