package com.caixy.adminSystem.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.caixy.adminSystem.annotation.AuthCheck;
import com.caixy.adminSystem.common.BaseResponse;
import com.caixy.adminSystem.common.DeleteRequest;
import com.caixy.adminSystem.common.ErrorCode;
import com.caixy.adminSystem.common.ResultUtils;
import com.caixy.adminSystem.constant.UserConstant;
import com.caixy.adminSystem.exception.BusinessException;
import com.caixy.adminSystem.exception.ThrowUtils;
import com.caixy.adminSystem.model.dto.semesters.*;
import com.caixy.adminSystem.model.entity.Semesters;
import com.caixy.adminSystem.model.entity.User;
import com.caixy.adminSystem.model.enums.SemesterActiveEnum;
import com.caixy.adminSystem.model.vo.semesters.SemestersVO;
import com.caixy.adminSystem.service.SemestersService;
import com.caixy.adminSystem.service.UserService;
import com.caixy.adminSystem.service.impl.SemestersServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 学期信息表接口
 */
@RestController
@RequestMapping("/semesters")
@Slf4j
public class SemestersController
{

    @Resource
    private SemestersService semestersService;


    // region 增删改查

    /**
     * 创建学期信息表
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addSemesters(@RequestBody SemestersAddRequest addRequest)
    {
        ThrowUtils.throwIf(addRequest == null, ErrorCode.PARAMS_ERROR);
        // DTO -> Entity
        Semesters semesters = new Semesters();
        BeanUtils.copyProperties(addRequest, semesters);
        // 数据校验
        semestersService.validSemesters(semesters, true);
        // 保存数据库
        boolean result = semestersService.save(semesters);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(semesters.getId());
    }

    /**
     * 删除学期信息表
     * 不允许删除激活状态的学期
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteSemesters(@RequestBody DeleteRequest deleteRequest)
    {
        if (deleteRequest == null || deleteRequest.getId() == null || deleteRequest.getId() <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = deleteRequest.getId();
        // 判断是否存在
        Semesters oldSemesters = semestersService.getById(id);
        ThrowUtils.throwIf(oldSemesters == null, ErrorCode.NOT_FOUND_ERROR);
        // 不允许删除激活学期
        ThrowUtils.throwIf(oldSemesters.getIsActive() != null && oldSemesters.getIsActive() == 1,
                ErrorCode.OPERATION_ERROR, "不允许删除激活状态的学期");
        // 删除数据库
        boolean result = semestersService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新学期信息表（仅管理员可用）
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateSemesters(@RequestBody SemestersUpdateRequest updateRequest)
    {
        if (updateRequest == null || updateRequest.getId() == null || updateRequest.getId() <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 判断是否存在
        Semesters oldSemesters = semestersService.getById(updateRequest.getId());
        ThrowUtils.throwIf(oldSemesters == null, ErrorCode.NOT_FOUND_ERROR);

        // DTO -> Entity
        Semesters semesters = new Semesters();
        BeanUtils.copyProperties(updateRequest, semesters);

        // 数据校验
        semestersService.validSemesters(semesters, false);

        // 更新数据库
        boolean result = semestersService.updateById(semesters);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取学期信息表（封装类）
     */
    @GetMapping("/get/vo")
    public BaseResponse<SemestersVO> getSemestersVOById(@RequestParam("id") long id,
                                                        HttpServletRequest request)
    {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询
        Semesters semesters = semestersService.getById(id);
        ThrowUtils.throwIf(semesters == null, ErrorCode.NOT_FOUND_ERROR);
        // 转 VO
        SemestersVO vo = semestersService.getSemestersVO(semesters, request);
        return ResultUtils.success(vo);
    }

    /**
     * 分页获取学期信息表列表（仅管理员可用）
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<SemestersVO>> listSemestersByPage(@RequestBody SemestersQueryRequest queryRequest,
                                                               HttpServletRequest request)
    {
        long current = queryRequest.getCurrent();
        long size = queryRequest.getPageSize();
        // 查询条件
        Page<Semesters> semestersPage = semestersService.page(new Page<>(current, size),
                semestersService.getQueryWrapper(queryRequest));
        // 转为 VO
        Page<SemestersVO> voPage = semestersService.getSemestersVOPage(semestersPage, request);
        return ResultUtils.success(voPage);
    }
    // endregion

    // region 激活学期

    /**
     * 获取当前激活学期
     */
    @GetMapping("/get/current")
    public BaseResponse<SemestersVO> getCurrentSemester()
    {
        // Service层方法需要你自行在 SemestersServiceImpl 中定义
        Semesters current = semestersService.getCurrentSemester();
        if (current == null)
        {
            // 如果无激活学期，可自行处理
            return ResultUtils.success(null);
        }
        SemestersVO vo = new SemestersVO();
        BeanUtils.copyProperties(current, vo);
        return ResultUtils.success(vo);
    }

    /**
     * 设置某个学期为激活学期（仅管理员可用）
     */
    @PostMapping("/set/active")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> setActiveSemester(@RequestBody SemestersSetActiveRequest request)
    {
        ThrowUtils.throwIf(request == null || request.getId() == null, ErrorCode.PARAMS_ERROR, "学期ID不能为空");
        boolean result = semestersService.setActiveSemester(request.getId());
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "设置激活学期失败");
        return ResultUtils.success(true);
    }

    @GetMapping("/get/semesters")
    public BaseResponse<List<SemestersVO>> getSemesters()
    {
        List<Semesters> semesters = semestersService.list();
        List<SemestersVO> semestersVOS = semesters.stream().map(item ->
        {
            SemestersVO vo = new SemestersVO();
            BeanUtils.copyProperties(item, vo);
            return vo;
        }).collect(Collectors.toList());
        return ResultUtils.success(semestersVOS);
    }
    // endregion

}
