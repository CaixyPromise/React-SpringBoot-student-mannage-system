package com.caixy.adminSystem.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 选项式视图返回体
 *
 * @Author CAIXYPROMISE
 * @since 2025/1/16 1:04
 */
@Data
public class OptionVO<T> implements Serializable
{
    private T value;

    private String label;

    /**
    * 子选项
    */
    private OptionVO<T> children;

    private static final long serialVersionUID = 1L;
}
