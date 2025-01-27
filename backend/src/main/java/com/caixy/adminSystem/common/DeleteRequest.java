package com.caixy.adminSystem.common;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

/**
 * 删除请求
 *
 
 */
@Data
@Builder
public class DeleteRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}