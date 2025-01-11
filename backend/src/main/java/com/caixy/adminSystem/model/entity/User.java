package com.caixy.adminSystem.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户
 * @TableName user
 */
@TableName(value ="user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 账号(用户学号/工号)
     */
    private String userAccount;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 用户
     */
    private Integer userSex;

    /**
     * 用户邮箱
     */
    private String userEmail;

    /**
     * 手机号
     */
    private String userPhone;

    /**
     * 用户部门/院系id(学院)
     */
    private Long userDepartment;

    /**
     * 用户专业id
     */
    private Long userMajor;
    
    /**
    * 用户班级
    */
    private Long userClass;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户标签
     */
    private String userTags;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 用户角色：user/admin/ban
     */
    private String userRole;

    /**
     * 角色权限等级(细分角色权限)
     */
    private Integer userRoleLevel;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}