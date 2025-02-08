package com.caixy.adminSystem.model.dto.subject;

import com.caixy.adminSystem.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 查询请求
 *
 
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SubjectsQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private String name;

    private static final long serialVersionUID = 1L;
}