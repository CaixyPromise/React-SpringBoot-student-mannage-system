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
    private String name;


    /**
     * 学院id
     */
    private Long departId;

    /**
     * 专业id
     */
    private Long majorId;
    
    /**
    * 年级
    */
    private Integer grade;

    private static final long serialVersionUID = 1L;
}