package com.caixy.adminSystem.constant;

import lombok.Getter;

/**
 * @Name: com.caixy.project.constant.RedisConstant
 * @Description: Redis缓存的常量：Key和过期时间
 * @Author: CAIXYPROMISE
 * @Date: 2023-12-20 20:20
 **/
@Getter
public enum RedisConstant
{
    // 单个学院对应专业的信息
    ACADEMY_MAJOR("ACADEMY_MAJOR", 60L * 60L * 24L),

    // 全部学院对应专业的信息：信息列表
    ALL_ACADEMY_MAJOR("ALL_ACADEMY_MAJOR",60L*60L*24L),

    // 加入队伍锁配置
    JOIN_TEAM_LOCK("JOIN_TEAM_LOCK", 5L),

    /**
     * 同意入队请求
     */
    RESOLVE_JOIN_TEAM("RESOLVE_JOIN_TEAM", 5L),
    /**
     * 拒绝入队请求
     */
    REJECT_JOIN_TEAM("REJECT_JOIN_TEAM", 5L),

    /**
     * 申请需求请求
     */
    DEMAND_LOCK("DEMAND_LOCK", 5L),

    /**
     * 比赛页数缓存
     */
    RACE_PAGE_CACHE("RACE_PAGE_CACHE", 60L);

    ;

    private final String key;
    private final Long expire;

    RedisConstant(String key, Long expire)
    {
        this.key = key;
        this.expire = expire;
    }


}
