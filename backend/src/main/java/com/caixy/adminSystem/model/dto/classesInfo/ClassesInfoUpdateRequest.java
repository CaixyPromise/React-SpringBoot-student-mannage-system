package com.caixy.adminSystem.model.dto.classesInfo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 更新请求
 *
 
 */
@Data
public class ClassesInfoUpdateRequest implements Serializable {

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
    
    /**
     * 班级id
     */
    Long id;

    /**
     * 年级
     */
    private Integer grade;
    
    
    private static final long serialVersionUID = 1L;
}