package com.caixy.adminSystem.model.vo.semesters;

import cn.hutool.json.JSONUtil;
import com.caixy.adminSystem.model.entity.Semesters;
import com.caixy.adminSystem.model.vo.UserVO;
import lombok.Data;

import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 学期信息表视图
 *


 */
@Data
public class SemestersVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
    * 学期名称
    */
    private String name;
    
    /**
    * 是否激活（是否是当前活跃学期）
    */
    private Integer isActive;

    /**
     * 学期开始日期
     */
    private Date startDate;

    /**
     * 学期结束日期
     */
    private Date endDate;
    

    private static final long serialVersionUID = 1L;
}
