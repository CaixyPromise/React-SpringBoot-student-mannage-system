package com.caixy.adminSystem.model.dto.semesters;

import lombok.Data;

import java.io.Serializable;

/**
 * 设置激活学期请求
 */
@Data
public class SemestersSetActiveRequest implements Serializable {

    /**
     * 要激活的学期 id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}
