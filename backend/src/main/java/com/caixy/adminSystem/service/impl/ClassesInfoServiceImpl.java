package com.caixy.adminSystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caixy.adminSystem.common.ErrorCode;
import com.caixy.adminSystem.exception.BusinessException;
import com.caixy.adminSystem.mapper.ClassesInfoMapper;
import com.caixy.adminSystem.mapper.CourseSelectionClassesMapper;
import com.caixy.adminSystem.model.dto.classesInfo.ClassesInfoQueryRequest;
import com.caixy.adminSystem.model.dto.classesInfo.ClassesInfoQueryUnderMajorRequest;
import com.caixy.adminSystem.model.dto.classesInfo.DepartmentMajorClassDTO;
import com.caixy.adminSystem.model.entity.ClassesInfo;
import com.caixy.adminSystem.model.entity.CourseSelectionClasses;
import com.caixy.adminSystem.model.vo.ClassesInfo.*;
import com.caixy.adminSystem.service.ClassesInfoService;
import com.caixy.adminSystem.service.MajorInfoService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

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
    private MajorInfoService majorInfoService;
    @Resource
    private CourseSelectionClassesMapper courseSelectionClassesMapper;
    @Resource
    private ClassesInfoMapper classesInfoMapper;


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

    @Override
    public List<DepartMajorClassTreeVO> getClassTreeByCourseSelectionId(Long courseSelectionId) {
        if (courseSelectionId == null || courseSelectionId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "courseSelectionId不能为空或不合法");
        }
        // 1. 查出该选课任务关联了哪些班级ID
        List<CourseSelectionClasses> mappingList = courseSelectionClassesMapper.selectList(
                Wrappers.<CourseSelectionClasses>lambdaQuery()
                        .eq(CourseSelectionClasses::getCourseSelectionId, courseSelectionId)
                        .eq(CourseSelectionClasses::getIsDelete, 0)
        );
        if (CollectionUtils.isEmpty(mappingList)) {
            return Collections.emptyList();
        }
        List<Long> classIds = mappingList.stream()
                                         .map(CourseSelectionClasses::getClassId)
                                         .distinct()
                                         .collect(Collectors.toList());

        // 2. 一次性查询班级 & 关联学院、专业
        //    这里用自定义SQL或多表JOIN都可以，示意写法见下方注释
        //    假设写在 XML 里，比如: classesInfoMapper.selectDepartMajorClassList(classIds)
        List<DepartMajorClassDTO> rawList = classesInfoMapper.selectDepartMajorClassList(classIds);
        // DepartMajorClassDTO 里包含:
        // - classId, className
        // - majorId, majorName
        // - departId, departName

        if (CollectionUtils.isEmpty(rawList)) {
            return Collections.emptyList();
        }

        // 3. 按 departId -> majorId -> class 分组
        // 3.1 先按 departId 分组
        Map<Long, List<DepartMajorClassDTO>> departMap = rawList.stream()
                                                                .collect(Collectors.groupingBy(DepartMajorClassDTO::getDepartId));

        // 3.2 构建树形列表
        List<DepartMajorClassTreeVO> result = new ArrayList<>();
        for (Map.Entry<Long, List<DepartMajorClassDTO>> departEntry : departMap.entrySet()) {
            Long departId = departEntry.getKey();
            List<DepartMajorClassDTO> departItems = departEntry.getValue();
            if (CollectionUtils.isEmpty(departItems)) {
                continue;
            }

            // 学院节点
            DepartMajorClassTreeVO departNode = new DepartMajorClassTreeVO();
            departNode.setDepartId(departId);
            departNode.setDepartName(departItems.get(0).getDepartName());
            // 同一 departId 下 departName 一致，所以拿第一条即可

            // 再按 majorId 分组
            Map<Long, List<DepartMajorClassDTO>> majorMap = departItems.stream()
                                                                       .collect(Collectors.groupingBy(DepartMajorClassDTO::getMajorId));
            List<MajorClassTreeVO> majorChildren = new ArrayList<>();
            for (Map.Entry<Long, List<DepartMajorClassDTO>> majorEntry : majorMap.entrySet()) {
                Long majorId = majorEntry.getKey();
                List<DepartMajorClassDTO> majorItems = majorEntry.getValue();
                if (CollectionUtils.isEmpty(majorItems)) {
                    continue;
                }

                MajorClassTreeVO majorNode = new MajorClassTreeVO();
                majorNode.setMajorId(majorId);
                majorNode.setMajorName(majorItems.get(0).getMajorName());

                // 最后收集班级
                List<ClassLeafVO> classList = majorItems.stream().map(item -> {
                    ClassLeafVO classLeaf = new ClassLeafVO();
                    classLeaf.setClassId(item.getClassId());
                    classLeaf.setClassName(item.getClassName());
                    return classLeaf;
                }).collect(Collectors.toList());

                majorNode.setChildren(classList);
                majorChildren.add(majorNode);
            }

            departNode.setChildren(majorChildren);
            result.add(departNode);
        }

        return result;
    }
}




