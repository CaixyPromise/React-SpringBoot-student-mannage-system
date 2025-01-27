package com.caixy.adminSystem.model.dto.registrationTask;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 编辑登分任务请求
 *


 */
@Data
public class RegistrationTaskEditRequest implements Serializable {

    /**
     * id
     */
    @NotNull
    private Long id;

    /**
    * 激活状态code
    */
    private Integer isActive;

    private static final long serialVersionUID = 1L;
}