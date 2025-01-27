package com.caixy.adminSystem.model.vo.registrationTask;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.caixy.adminSystem.model.entity.RegistrationTask;
import com.caixy.adminSystem.model.vo.UserVO;
import com.caixy.adminSystem.model.vo.semesters.SemestersVO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 登分任务视图
 *
 */
@Data
public class RegistrationTaskVO implements Serializable {
    private Long id;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 任务名称
     */
    private String name;

    /**
     * 学期id
     */
    private SemestersVO semestersInfo;
    /**
     * 是否激活状态
     */
    private Integer isActive;

    /**
     * 登记开始日期
     */
    private Date startDate;

    /**
     * 登记结束日期
     */
    private Date endDate;
}
