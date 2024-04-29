package com.caixy.adminSystem.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.caixy.adminSystem.model.dto.classesInfo.ClassesInfoQueryRequest;
import com.caixy.adminSystem.model.entity.ClassesInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.caixy.adminSystem.model.vo.ClassesInfo.ClassesInfoVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author CAIXYPROMISE
* @description 针对表【classes_info(班级信息)】的数据库操作Service
* @createDate 2024-04-28 22:48:38
*/
public interface ClassesInfoService extends IService<ClassesInfo> {
    /**
     * 校验
     *
     * @param post
     * @param add
     */
    void validClassesInfo(ClassesInfo post, boolean add);

    /**
     * 获取查询条件
     *
     * @param postQueryRequest
     * @return
     */
    QueryWrapper<ClassesInfo> getQueryWrapper(ClassesInfoQueryRequest postQueryRequest);

    /**
     * 从 ES 查询
     *
     * @param postQueryRequest
     * @return
     */
    Page<ClassesInfo> searchFromEs(ClassesInfoQueryRequest postQueryRequest);

    /**
     * 获取帖子封装
     *
     * @param post
     * @param request
     * @return
     */
    ClassesInfoVO getClassesInfoVO(ClassesInfo post, HttpServletRequest request);

    /**
     * 分页获取帖子封装
     *
     * @param postPage
     * @param request
     * @return
     */
    Page<ClassesInfoVO> getClassesInfoVOPage(Page<ClassesInfo> postPage, HttpServletRequest request);
}
