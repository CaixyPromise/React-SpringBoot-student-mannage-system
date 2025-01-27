package com.caixy.adminSystem.model.dto.registrationTask;

import com.caixy.adminSystem.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 查询登分任务请求
 *


 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RegistrationTaskQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
    * 学期id
    */
    private Long semesterId;

    private static final long serialVersionUID = 1L;
}