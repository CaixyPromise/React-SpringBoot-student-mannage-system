package com.caixy.adminSystem.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.caixy.adminSystem.model.dto.semesters.SemestersQueryRequest;
import com.caixy.adminSystem.model.entity.Semesters;
import com.caixy.adminSystem.model.vo.semesters.SemestersVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 学期信息表服务
 *


 */
public interface SemestersService extends IService<Semesters> {

    /**
     * 校验数据
     *
     * @param semesters
     * @param add 对创建的数据进行校验
     */
    void validSemesters(Semesters semesters, boolean add);

    /**
     * 获取查询条件
     *
     * @param semestersQueryRequest
     * @return
     */
    QueryWrapper<Semesters> getQueryWrapper(SemestersQueryRequest semestersQueryRequest);
    
    /**
     * 获取学期信息表封装
     *
     * @param semesters
     * @param request
     * @return
     */
    SemestersVO getSemestersVO(Semesters semesters, HttpServletRequest request);

    /**
     * 分页获取学期信息表封装
     *
     * @param semestersPage
     * @param request
     * @return
     */
    Page<SemestersVO> getSemestersVOPage(Page<Semesters> semestersPage, HttpServletRequest request);

    Semesters getCurrentSemester();

    boolean setActiveSemester(Long semesterId);
}
