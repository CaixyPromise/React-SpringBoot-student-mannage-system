package com.caixy.adminSystem.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caixy.adminSystem.common.ErrorCode;
import com.caixy.adminSystem.constant.CommonConstant;
import com.caixy.adminSystem.exception.BusinessException;
import com.caixy.adminSystem.exception.ThrowUtils;
import com.caixy.adminSystem.model.dto.teacherInfo.TeacherInfoAddRequest;
import com.caixy.adminSystem.model.dto.teacherInfo.TeacherInfoQueryRequest;
import com.caixy.adminSystem.model.entity.*;
import com.caixy.adminSystem.model.enums.UserRoleEnum;
import com.caixy.adminSystem.model.enums.UserSexEnum;
import com.caixy.adminSystem.model.vo.teacherInfo.TeacherInfoVO;
import com.caixy.adminSystem.service.*;
import com.caixy.adminSystem.mapper.TeacherInfoMapper;
import com.caixy.adminSystem.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author CAIXYPROMISE
 * @description 针对表【teacher_info(教师信息表)】的数据库操作Service实现
 * @createDate 2025-01-11 19:10:20
 */
@Service
public class TeacherInfoServiceImpl extends ServiceImpl<TeacherInfoMapper, TeacherInfo>
        implements TeacherInfoService
{
    @Resource
    private MajorInfoService majorInfoService;
    @Resource
    private ClassesInfoService classesInfoService;
    @Resource
    private DepartmentInfoService departmentInfoService;
    @Resource
    private UserService userService;

    /**
     * 校验数据
     *
     * @param teacherInfo
     * @param add      对创建的数据进行校验
     */
    @Override
    public void validTeacherInfo(TeacherInfo teacherInfo, boolean add) {
        ThrowUtils.throwIf(teacherInfo == null, ErrorCode.PARAMS_ERROR);
        UserSexEnum userSexEnum = UserSexEnum.getEnumByCode(teacherInfo.getTeacherSex());
        if (userSexEnum == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "性别不合法");
        }
        boolean majorExistById = majorInfoService.majorExistById(teacherInfo.getTeacherMajorId(), teacherInfo.getTeacherDeptId());
        if (!majorExistById)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "专业不存在");
        }
        if (add)
        {
            String teacherId = teacherInfo.getTeacherId();
            if (StringUtils.isBlank(teacherId) || teacherId.length() < 8)
            {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "学号不合法");
            }
            LambdaQueryWrapper<TeacherInfo> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(TeacherInfo::getTeacherId, teacherId);
            if (this.count(queryWrapper) > 0)
            {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "学号已存在");
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addTeacher(TeacherInfoAddRequest teacherInfoAddRequest, HttpServletRequest request, User creator) {
        TeacherInfo post = new TeacherInfo();
        BeanUtils.copyProperties(teacherInfoAddRequest, post);
        // 参数校验
        validTeacherInfo(post, true);
        post.setCreatorId(creator.getId());
        boolean result = this.save(post);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newTeacherInfoId = post.getId();
        User newUser = User.builder()
                           .userAccount(post.getTeacherId())
                           .userPassword(post.getTeacherId())
                           .id(newTeacherInfoId)
                           .userDepartment(post.getTeacherDeptId())
                           .userMajor(post.getTeacherMajorId())
                           .userRoleLevel(0)
                           .userRole(UserRoleEnum.TEACHER.getValue())
                           .userName(post.getTeacherName())
                           .userSex(post.getTeacherSex())
                           .build();
        long register = userService.makeRegister(newUser);
        ThrowUtils.throwIf(register < 0, ErrorCode.OPERATION_ERROR);
        return newTeacherInfoId;
    }

    /**
     * 获取查询条件
     *
     * @param teacherInfoQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<TeacherInfo> getQueryWrapper(TeacherInfoQueryRequest teacherInfoQueryRequest) {
        QueryWrapper<TeacherInfo> queryWrapper = new QueryWrapper<>();
        if (teacherInfoQueryRequest == null) {
            return queryWrapper;
        }
        // todo 从对象中取值
        Long id = teacherInfoQueryRequest.getId();
        Long notId = teacherInfoQueryRequest.getNotId();
        String title = teacherInfoQueryRequest.getTitle();
        String content = teacherInfoQueryRequest.getContent();
        String searchText = teacherInfoQueryRequest.getSearchText();
        String sortField = teacherInfoQueryRequest.getSortField();
        String sortOrder = teacherInfoQueryRequest.getSortOrder();
        List<String> tagList = teacherInfoQueryRequest.getTags();
        Long userId = teacherInfoQueryRequest.getCreatorId();
        // todo 补充需要的查询条件
        // 从多字段中搜索
        if (StringUtils.isNotBlank(searchText)) {
            // 需要拼接查询条件
            queryWrapper.and(qw -> qw.like("title", searchText).or().like("content", searchText));
        }
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        // JSON 数组查询
        if (CollUtil.isNotEmpty(tagList)) {
            for (String tag : tagList) {
                queryWrapper.like("tags", "\"" + tag + "\"");
            }
        }
        // 精确查询
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取教师信息封装
     *
     * @param teacherInfo
     * @param request
     * @return
     */
    @Override
    public TeacherInfoVO getTeacherInfoVO(TeacherInfo teacherInfo, HttpServletRequest request) {
        // todo: 补充获取教师信息封装逻辑
        return null;
    }

    /**
     * 分页获取教师信息封装
     *
     * @param teacherInfoPage
     * @return
     */
    @Override
    public Page<TeacherInfoVO> getTeacherInfoVOPage(Page<TeacherInfo> teacherInfoPage) {
        Page<TeacherInfoVO> postVOPage = new Page<>(teacherInfoPage.getCurrent(), teacherInfoPage.getSize());
        List<TeacherInfoVO> studentInfoVOS = this.getTeacherInfoVOList(teacherInfoPage.getRecords());
        postVOPage.setRecords(studentInfoVOS);
        postVOPage.setTotal(studentInfoVOS.size());
        return postVOPage;
    }

    private List<TeacherInfoVO> getTeacherInfoVOList(List<TeacherInfo> teacherInfoList)
    {
        if (teacherInfoList.isEmpty()) {
            return Collections.emptyList();
        }
        Set<Long> departIds = new HashSet<>();
        Set<Long> majorIds = new HashSet<>();
        teacherInfoList.forEach(item -> {
            departIds.add(item.getTeacherDeptId());
            majorIds.add(item.getTeacherMajorId());
        });

        Map<Long, String> departMap = departmentInfoService.listByIds(departIds).stream()
                                                           .collect(Collectors.toMap(DepartmentInfo::getId,
                                                                   DepartmentInfo::getName));
        Map<Long, String> majorMap = majorInfoService.listByIds(majorIds).stream()
                                                     .collect(Collectors.toMap(MajorInfo::getId, MajorInfo::getName));

        return teacherInfoList.stream().map(item -> {
            TeacherInfoVO teacherInfoVO = new TeacherInfoVO();
            BeanUtils.copyProperties(item, teacherInfoVO);
            teacherInfoVO.setTeacherDepart(departMap.get(item.getTeacherDeptId()));
            teacherInfoVO.setTeacherMajor(majorMap.get(item.getTeacherMajorId()));
            return teacherInfoVO;
        }).collect(Collectors.toList());
    }
}




