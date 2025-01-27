package com.caixy.adminSystem.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.caixy.adminSystem.model.dto.registrationTask.RegistrationTaskAddRequest;
import com.caixy.adminSystem.model.dto.registrationTask.RegistrationTaskQueryRequest;
import com.caixy.adminSystem.model.entity.RegistrationTask;
import com.caixy.adminSystem.model.vo.registrationTask.RegistrationTaskVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 登分任务服务
 *


 */
public interface RegistrationTaskService extends IService<RegistrationTask> {

    Boolean addRegistrationTask(RegistrationTaskAddRequest request, Long creatorId);

    /**
     * 校验数据
     *
     * @param registrationTask
     * @param add 对创建的数据进行校验
     */
    void validRegistrationTask(RegistrationTask registrationTask, boolean add);

    /**
     * 获取查询条件
     *
     * @param registrationTaskQueryRequest
     * @return
     */
    LambdaQueryWrapper<RegistrationTask> getQueryWrapper(RegistrationTaskQueryRequest registrationTaskQueryRequest);

    Page<RegistrationTaskVO> getPage(RegistrationTaskQueryRequest request);

    /**
     * 获取登分任务封装
     *
     * @param registrationTask
     * @param request
     * @return
     */
    RegistrationTaskVO getRegistrationTaskVO(RegistrationTask registrationTask, HttpServletRequest request);

    /**
     * 分页获取登分任务封装
     *
     * @param registrationTaskPage
     * @return
     */
    Page<RegistrationTaskVO> getRegistrationTaskVOPage(Page<RegistrationTask> registrationTaskPage);
}
