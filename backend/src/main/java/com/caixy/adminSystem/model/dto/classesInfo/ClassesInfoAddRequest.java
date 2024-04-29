package com.caixy.adminSystem.model.dto.classesInfo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 创建请求
 *
 
 */
@Data
public class ClassesInfoAddRequest implements Serializable {

    /**
     * 名字
     */
    String name;


    /**
     * 学院id
     */
    Long departId;

    /**
     * 专业id
     */
    Long majorId;

    private static final long serialVersionUID = 1L;
}