package com.caixy.adminSystem.model.enums;

import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户角色枚举
 */
@Getter
public enum UserSexEnum
{
    /**
     * 男
     */
    MALE("男", "1", 1),

    /**
     * 女
     */
    FEMALE("女", "2", 2);

    private final String text;

    private final String value;

    private final Integer code;

    UserSexEnum(String text, String value, Integer code)
    {
        this.text = text;
        this.value = value;
        this.code = code;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<String> getValues()
    {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static UserSexEnum getEnumByValue(String value)
    {
        if (ObjectUtils.isEmpty(value))
        {
            return null;
        }
        for (UserSexEnum anEnum : UserSexEnum.values())
        {
            if (anEnum.value.equals(value))
            {
                return anEnum;
            }
        }
        return null;
    }

    public static UserSexEnum getEnumByCode(Integer code)
    {
        if (ObjectUtils.isEmpty(code))
        {
            return null;
        }
        for (UserSexEnum anEnum : UserSexEnum.values())
        {
            if (anEnum.code.equals(code))
            {
                return anEnum;
            }
        }
        return null;
    }

}
