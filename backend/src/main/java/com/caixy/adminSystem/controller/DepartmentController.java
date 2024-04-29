package com.caixy.adminSystem.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.caixy.adminSystem.annotation.AuthCheck;
import com.caixy.adminSystem.common.BaseResponse;
import com.caixy.adminSystem.common.DeleteRequest;
import com.caixy.adminSystem.common.ErrorCode;
import com.caixy.adminSystem.common.ResultUtils;
import com.caixy.adminSystem.constant.RedisConstant;
import com.caixy.adminSystem.constant.UserConstant;
import com.caixy.adminSystem.exception.BusinessException;
import com.caixy.adminSystem.exception.ThrowUtils;
import com.caixy.adminSystem.model.dto.department.DepartmentInfoAddRequest;
import com.caixy.adminSystem.model.dto.department.DepartmentInfoQueryRequest;
import com.caixy.adminSystem.model.dto.department.DepartmentInfoUpdateRequest;
import com.caixy.adminSystem.model.entity.DepartmentInfo;
import com.caixy.adminSystem.model.entity.User;
import com.caixy.adminSystem.model.vo.department.DepartmentInfoVO;
import com.caixy.adminSystem.model.vo.department.DepartmentWithMajorsVO;
import com.caixy.adminSystem.service.DepartmentInfoService;
import com.caixy.adminSystem.service.UserService;
import com.caixy.adminSystem.utils.JsonUtils;
import com.caixy.adminSystem.utils.RedisOperatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 学院信息接口控制器
 *
 * @name: com.caixy.userservice.controller.DepartmentController
 * @author: CAIXYPROMISE
 * @since: 2024-02-10 01:46
 **/
@RestController
@RequestMapping("/department")
@Slf4j
public class DepartmentController
{
    @Resource
    private DepartmentInfoService departmentInfoService;

    @Resource
    private RedisOperatorService redisOperatorService;

    @Resource
    private UserService userService;

    /**
     * 获取学院下的专业信息
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/2/11 01:08
     */
    @GetMapping("/get/vo/department-major")
    public BaseResponse<DepartmentWithMajorsVO> getMajorUnderDepartment(
            @RequestParam("departmentId") long departmentId)
    {
        if (departmentId <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数不合法");
        }
        Map<Object, Object>
                departmentInfo =
                redisOperatorService.getHash(RedisConstant.ACADEMY_MAJOR.getKey(), String.valueOf(departmentId));
        if (departmentInfo.isEmpty())
        {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "未找到该学院下的专业");
        }
        else
        {
            DepartmentWithMajorsVO departmentWithMajorsVO =
                    new DepartmentWithMajorsVO();
            departmentWithMajorsVO.setDepartmentName(JsonUtils.jsonToObject((String) departmentInfo.get("_name"), String.class));
            departmentWithMajorsVO.setDepartmentId(departmentId);
            List<DepartmentWithMajorsVO.MajorInnerInfo> majors = departmentInfo.entrySet()
                    .stream()
                    .filter(entry -> !entry.getKey().equals("_name"))
                    .map(entry ->
                    {
                        DepartmentWithMajorsVO.MajorInnerInfo majorInfo = new DepartmentWithMajorsVO.MajorInnerInfo();
                        majorInfo.setMajorId(Long.valueOf((String) entry.getKey()));
                        majorInfo.setMajorName(JsonUtils.jsonToObject((String) entry.getValue(), String.class));
                        return majorInfo;
                    })
                    .collect(Collectors.toList());
            departmentWithMajorsVO.setMajors(majors);
            return ResultUtils.success(departmentWithMajorsVO);
        }
//        List<DepartmentWithMajorsDTO> departments =
//                departmentInfoService.getMajorUnderDepartment(departmentId);
//        if (departments.isEmpty())
//        {
//            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "未找到该学院下的专业");
//        }
//        DepartmentWithMajorsVO departmentWithMajorsVO =
//                new DepartmentWithMajorsVO();
//        DepartmentWithMajorsDTO departmentWithMajorsDTO = departments.get(0);
//        departmentWithMajorsVO.setDepartmentId(departmentWithMajorsDTO.getDepartmentId());
//        departmentWithMajorsVO.setDepartmentName(departmentWithMajorsDTO.getDepartmentName());
//        List<DepartmentWithMajorsVO.MajorInnerInfo> majors = departments.stream().map(dept ->
//        {
//            DepartmentWithMajorsVO.MajorInnerInfo majorInfoVO = new DepartmentWithMajorsVO.MajorInnerInfo();
//            majorInfoVO.setMajorId(dept.getMajorId());
//            majorInfoVO.setMajorName(dept.getMajorName());
//            return majorInfoVO;
//        }).collect(Collectors.toList());
//        departmentWithMajorsVO.setMajors(majors);
//        return ResultUtils.success(departmentWithMajorsVO);
    }


    // region 增删改查

    /**
     * 创建
     *
     * @param postAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addDepartmentInfo(@RequestBody DepartmentInfoAddRequest postAddRequest, HttpServletRequest request)
    {
        if (postAddRequest == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        DepartmentInfo post = new DepartmentInfo();
        BeanUtils.copyProperties(postAddRequest, post);

        User loginUser = userService.getLoginUser(request);
        post.setCreateUserId(loginUser.getId());
        boolean departmentIsExist = departmentInfoService.departmentExistByName(post.getName());
        if (departmentIsExist)
        {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "学院已存在");
        }
        // 更新到数据库
        boolean result = departmentInfoService.save(post);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newDepartmentInfoId = post.getId();
        // 更新所有学院和专业信息的缓存
        redisOperatorService.delete(RedisConstant.ALL_ACADEMY_MAJOR, "ALL");

        // 更新到redis
        Map<Object, Object> infoMap = new HashMap<>();
        infoMap.put("_name", post.getName());
        setDepartmentInfoToMap(newDepartmentInfoId, infoMap);
        return ResultUtils.success(newDepartmentInfoId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteDepartmentInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request)
    {
        if (deleteRequest == null || deleteRequest.getId() <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        DepartmentInfo oldDepartmentInfo = departmentInfoService.getById(id);
        ThrowUtils.throwIf(oldDepartmentInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldDepartmentInfo.getCreateUserId().equals(user.getId()) && !userService.isAdmin(request))
        {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = departmentInfoService.removeById(id);
        // 删除Redis中的学院信息
        redisOperatorService.delete(RedisConstant.ACADEMY_MAJOR, id);
        // 更新所有学院和专业信息的缓存
        redisOperatorService.delete(RedisConstant.ALL_ACADEMY_MAJOR, "ALL");
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param postUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateDepartmentInfo(@RequestBody DepartmentInfoUpdateRequest postUpdateRequest)
    {
        if (postUpdateRequest == null || postUpdateRequest.getId() <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        DepartmentInfo post = new DepartmentInfo();
        BeanUtils.copyProperties(postUpdateRequest, post);
        long id = postUpdateRequest.getId();
        // 判断是否存在
        DepartmentInfo oldDepartmentInfo = departmentInfoService.getById(id);
        ThrowUtils.throwIf(oldDepartmentInfo == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = departmentInfoService.updateById(post);
        if (!postUpdateRequest.getName().equals(oldDepartmentInfo.getName()))
        {
            Map<Object, Object> innerData = redisOperatorService.getHash(RedisConstant.ACADEMY_MAJOR.getKey(),
                    post.getId().toString());
            if (!innerData.isEmpty())
            {
                // 把innerData里的_name替换成新的name
                innerData.put("_name", postUpdateRequest.getName());
            }
            setDepartmentInfoToMap(post.getId(), innerData);
        }
        // 更新所有学院和专业信息的缓存
        redisOperatorService.delete(RedisConstant.ALL_ACADEMY_MAJOR, "ALL");

        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取 学院信息
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<DepartmentInfoVO> getDepartmentInfoVOById(@RequestParam long id, HttpServletRequest request)
    {
        if (id <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        DepartmentInfo post = departmentInfoService.getById(id);
        if (post == null)
        {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        DepartmentInfoVO departmentInfoVO = new DepartmentInfoVO();
        BeanUtils.copyProperties(post, departmentInfoVO);
        return ResultUtils.success(departmentInfoVO);
    }

    /**
     * 分页获取列表（仅管理员）
     *
     * @param postQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<DepartmentInfo>> listDepartmentInfoByPage(@RequestBody DepartmentInfoQueryRequest postQueryRequest)
    {
        long current = postQueryRequest.getCurrent();
        long size = postQueryRequest.getPageSize();
        Page<DepartmentInfo> postPage = departmentInfoService.page(new Page<>(current, size));
        return ResultUtils.success(postPage);
    }

    // endregion


    //region redis操作获取学院信息
    private Map<Object, Object> getDepartmentInfoMap(long id)
    {
        return redisOperatorService.getHash(RedisConstant.ACADEMY_MAJOR.getKey(), String.valueOf(id));
    }

    private void setDepartmentInfoToMap(long id, Object map)
    {
        redisOperatorService.setHashMap(RedisConstant.ACADEMY_MAJOR.getKey() + id, (HashMap<String, Object>) map,
                RedisConstant.ACADEMY_MAJOR.getExpire());
    }
    // endregion
}
