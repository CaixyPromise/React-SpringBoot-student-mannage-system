package com.caixy.adminSystem.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caixy.adminSystem.common.ErrorCode;
import com.caixy.adminSystem.exception.ThrowUtils;
import com.caixy.adminSystem.mapper.SemestersMapper;
import com.caixy.adminSystem.model.dto.semesters.SemestersQueryRequest;
import com.caixy.adminSystem.model.entity.Semesters;
import com.caixy.adminSystem.model.vo.semesters.SemestersVO;
import com.caixy.adminSystem.service.SemestersService;
import com.caixy.adminSystem.service.UserService;
import com.caixy.adminSystem.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 学期信息表服务实现
 */
@Service
@Slf4j
public class SemestersServiceImpl extends ServiceImpl<SemestersMapper, Semesters>
        implements SemestersService {

    @Resource
    private UserService userService;

    /**
     * 校验数据
     *
     * @param semesters
     * @param add       是否为新增
     */
    @Override
    public void validSemesters(Semesters semesters, boolean add) {
        ThrowUtils.throwIf(semesters == null, ErrorCode.PARAMS_ERROR);

        // 新增时需要的必填项校验
        if (add) {
            ThrowUtils.throwIf(StringUtils.isBlank(semesters.getName()), ErrorCode.PARAMS_ERROR, "学期名称不能为空");
            ThrowUtils.throwIf(semesters.getStartDate() == null, ErrorCode.PARAMS_ERROR, "学期开始日期不能为空");
            ThrowUtils.throwIf(semesters.getEndDate() == null, ErrorCode.PARAMS_ERROR, "学期结束日期不能为空");
        }

        // 如果有日期，校验开始日期不能大于结束日期
        if (semesters.getStartDate() != null && semesters.getEndDate() != null) {
            ThrowUtils.throwIf(semesters.getStartDate().after(semesters.getEndDate()),
                    ErrorCode.PARAMS_ERROR, "开始日期不能晚于结束日期");
        }
    }

    /**
     * 获取查询条件
     *
     * @param request SemestersQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Semesters> getQueryWrapper(SemestersQueryRequest request) {
        QueryWrapper<Semesters> queryWrapper = new QueryWrapper<>();
        if (request == null) {
            return queryWrapper;
        }
        Long id = request.getId();
        String name = request.getName();
        Integer isActive = request.getIsActive();
        Date startDateBegin = request.getStartDateBegin();
        Date startDateEnd = request.getStartDateEnd();
        Date endDateBegin = request.getEndDateBegin();
        Date endDateEnd = request.getEndDateEnd();
        String sortField = request.getSortField();
        String sortOrder = request.getSortOrder();

        // 精确查询
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(isActive), "isActive", isActive);

        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);

        // 日期范围查询 - 开始日期
        if (startDateBegin != null) {
            queryWrapper.ge("startDate", startDateBegin);
        }
        if (startDateEnd != null) {
            queryWrapper.le("startDate", startDateEnd);
        }

        // 日期范围查询 - 结束日期
        if (endDateBegin != null) {
            queryWrapper.ge("endDate", endDateBegin);
        }
        if (endDateEnd != null) {
            queryWrapper.le("endDate", endDateEnd);
        }

        // 排序字段校验
        if (SqlUtils.validSortField(sortField)) {
            queryWrapper.orderBy(true, sortOrder.equalsIgnoreCase("asc"), sortField);
        } else {
            // 默认按创建时间倒序
            queryWrapper.orderByDesc("createTime");
        }

        return queryWrapper;
    }

    /**
     * 获取学期信息表封装
     *
     * @param semesters
     * @param request
     * @return
     */
    @Override
    public SemestersVO getSemestersVO(Semesters semesters, HttpServletRequest request) {
        if (semesters == null) {
            return null;
        }
        SemestersVO vo = new SemestersVO();
        BeanUtils.copyProperties(semesters, vo);
        return vo;
    }

    /**
     * 分页获取学期信息表封装
     *
     * @param semestersPage 分页对象
     * @param request       HttpServletRequest
     * @return
     */
    @Override
    public Page<SemestersVO> getSemestersVOPage(Page<Semesters> semestersPage, HttpServletRequest request) {
        if (semestersPage == null) {
            return null;
        }
        Page<SemestersVO> voPage = new Page<>(semestersPage.getCurrent(), semestersPage.getSize(), semestersPage.getTotal());
        List<Semesters> records = semestersPage.getRecords();
        if (CollUtil.isEmpty(records)) {
            return voPage;
        }
        // 转换为 VO
        List<SemestersVO> voList = records.stream()
                                          .map(semesters -> getSemestersVO(semesters, request))
                                          .collect(java.util.stream.Collectors.toList());
        voPage.setRecords(voList);
        return voPage;
    }

    /**
     * 获取当前激活学期 (isActive = 1)
     * 如果没有找到，返回 null 或自行抛异常
     */
    @Override
    public Semesters getCurrentSemester() {
        QueryWrapper<Semesters> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("isActive", 1);
        queryWrapper.eq("isDelete", 0);
        return this.getOne(queryWrapper);
    }

    /**
     * 批量获取学期信息vo
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @version 2025/1/26 22:54
     */
    @Override
    public Map<Long, SemestersVO> getSemestersVOListByIds(Collection<Long> semesterId) {
        if (CollUtil.isEmpty(semesterId)) {
            return new HashMap<>();
        }
        LambdaQueryWrapper<Semesters> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Semesters::getId, semesterId);
        queryWrapper.eq(Semesters::getIsDelete, 0);
        return this.list(queryWrapper).stream()
                .map(semesters -> getSemestersVO(semesters, null))
                .collect(Collectors.toMap(SemestersVO::getId, Function.identity()));
    }

    /**
     * 设置激活学期
     * @param semesterId 要激活的学期 ID
     */
    @Override
    public boolean setActiveSemester(Long semesterId) {
        // 先检查目标学期是否存在
        Semesters toActive = this.getById(semesterId);
        ThrowUtils.throwIf(toActive == null, ErrorCode.NOT_FOUND_ERROR, "要激活的学期不存在");

        // 全部设为非激活
        Semesters updateAll = new Semesters();
        updateAll.setIsActive(0);
        this.update(updateAll, new QueryWrapper<Semesters>().eq("isDelete", 0));

        // 再更新目标学期为激活
        Semesters updateTarget = new Semesters();
        updateTarget.setId(semesterId);
        updateTarget.setIsActive(1);
        return this.updateById(updateTarget);
    }

}
