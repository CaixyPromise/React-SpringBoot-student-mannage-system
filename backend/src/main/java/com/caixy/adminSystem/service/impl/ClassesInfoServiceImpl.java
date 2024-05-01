package com.caixy.adminSystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caixy.adminSystem.common.ErrorCode;
import com.caixy.adminSystem.exception.BusinessException;
import com.caixy.adminSystem.mapper.ClassesInfoMapper;
import com.caixy.adminSystem.model.dto.classesInfo.ClassesInfoQueryRequest;
import com.caixy.adminSystem.model.dto.classesInfo.ClassesInfoQueryUnderMajorRequest;
import com.caixy.adminSystem.model.dto.classesInfo.DepartmentMajorClassDTO;
import com.caixy.adminSystem.model.entity.ClassesInfo;
import com.caixy.adminSystem.model.entity.MajorInfo;
import com.caixy.adminSystem.model.vo.ClassesInfo.ClassesInfoVO;
import com.caixy.adminSystem.service.ClassesInfoService;
import com.caixy.adminSystem.service.DepartmentInfoService;
import com.caixy.adminSystem.service.MajorInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author CAIXYPROMISE
 * @description 针对表【classes_info(班级信息)】的数据库操作Service实现
 * @createDate 2024-04-28 22:48:38
 */
@Service
public class ClassesInfoServiceImpl extends ServiceImpl<ClassesInfoMapper, ClassesInfo>
        implements ClassesInfoService
{

    @Resource
    private DepartmentInfoService departmentInfoService;

    @Resource
    private MajorInfoService majorInfoService;


    @Override
    public List<DepartmentMajorClassDTO>  fetchAllClassesData()
    {
        return this.baseMapper.fetchAllClassesData();
    }

    @Override
    public void validClassesInfo(ClassesInfo post, boolean add)
    {
        boolean majorExistById = majorInfoService.majorExistById(post.getMajorId(), post.getDepartId());
        if (!majorExistById)
        {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "专业不存在");
        }
        if (add)
        {
            if (classesExistByName(post.getName(), post.getMajorId()))
            {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "班级名重复");
            }
        }
    }

    @Override
    public QueryWrapper<ClassesInfo> getQueryWrapper(ClassesInfoQueryRequest postQueryRequest)
    {
        return null;
    }

    @Override
    public Page<ClassesInfo> searchFromEs(ClassesInfoQueryRequest postQueryRequest)
    {
        return null;
    }

    @Override
    public ClassesInfoVO getClassesInfoVO(ClassesInfo post, HttpServletRequest request)
    {
        return null;
    }

    @Override
    public Page<ClassesInfoVO> getClassesInfoVOPage(Page<ClassesInfo> postPage, HttpServletRequest request)
    {
        return null;
    }

    @Override
    public List<ClassesInfoVO> getClassesInfoVOPageUnderMajor(ClassesInfoQueryUnderMajorRequest classesInfoQueryRequest)
    {
        List<ClassesInfoVO> classesInfoVOS =
                baseMapper.selectClassByMajorAndDepartId(classesInfoQueryRequest.getDepartmentId(),
                        classesInfoQueryRequest.getMajorId());
        if (classesInfoVOS.isEmpty())
        {
            return Collections.emptyList();
        }
        return classesInfoVOS;
    }


    private boolean classesExistByName(String name, Long majorId)
    {
        QueryWrapper<ClassesInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("majorId", majorId);
        queryWrapper.eq("name", name);
        return this.count(queryWrapper) > 0;
    }
}




