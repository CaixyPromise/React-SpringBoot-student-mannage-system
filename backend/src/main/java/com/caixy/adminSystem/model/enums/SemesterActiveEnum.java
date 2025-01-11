package com.caixy.adminSystem.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 学期激活状态
 *
 * @Author CAIXYPROMISE
 * @since 2024/12/26 15:58
 */
@Getter
@AllArgsConstructor
public enum SemesterActiveEnum
{
    ACTIVE(1, "激活"),
    INACTIVE(0, "未激活");

    private final Integer code;
    private final String desc;
    public static SemesterActiveEnum getEnumByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (SemesterActiveEnum value : SemesterActiveEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
