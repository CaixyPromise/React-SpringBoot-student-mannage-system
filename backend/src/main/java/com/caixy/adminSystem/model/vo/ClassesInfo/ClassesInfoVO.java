package com.caixy.adminSystem.model.vo.ClassesInfo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 班级信息VO
 *
 * @name: com.caixy.adminSystem.model.vo.ClassesInfo.ClassesInfoVO
 * @author: CAIXYPROMISE
 * @since: 2024-04-28 22:58
 **/
@Data
public class ClassesInfoVO implements Serializable
{
    /**
     * 主键Id
     */
    private Long id;

    /**
     * 班级名称
     */
    private String name;

    /**
     * 学院名称
     */
    private String departName;

    /**
     * 专业名称
     */
    private String majorName;

    /**
     * 创建人Id
     */
    private Long creatorId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;

}
