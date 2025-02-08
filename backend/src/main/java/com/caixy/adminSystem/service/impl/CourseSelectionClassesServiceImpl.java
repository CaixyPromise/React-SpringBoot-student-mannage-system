package com.caixy.adminSystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caixy.adminSystem.common.ErrorCode;
import com.caixy.adminSystem.exception.BusinessException;
import com.caixy.adminSystem.mapper.ClassesInfoMapper;
import com.caixy.adminSystem.mapper.CourseSelectionInfoMapper;
import com.caixy.adminSystem.model.entity.*;
import com.caixy.adminSystem.model.vo.courseSelectionClasses.ModifyCourseSelectionClassesRequest;
import com.caixy.adminSystem.service.*;
import com.caixy.adminSystem.mapper.CourseSelectionClassesMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author CAIXYPROMISE
 * @description 针对表【course_selection_classes】的数据库操作Service实现
 * @createDate 2025-01-03 01:23:48
 */
@Service
@Slf4j
public class CourseSelectionClassesServiceImpl extends ServiceImpl<CourseSelectionClassesMapper, CourseSelectionClasses>
        implements CourseSelectionClassesService
{

    @Resource
    private CourseSelectionClassesMapper courseSelectionClassesMapper;
    @Resource
    private ClassesInfoMapper classesInfoMapper;
    @Resource
    private CourseSelectionInfoMapper courseSelectionInfoMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addClassesToCourseSelection(ModifyCourseSelectionClassesRequest request)
    {
        // 1. 参数校验
        if (request == null || CollectionUtils.isEmpty(request.getClassIds()))
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "班级ID列表不能为空");
        }

        Long courseSelectionId = request.getCourseSelectionId();
        if (courseSelectionId == null || courseSelectionId <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "选课任务ID不能为空或不合法");
        }

        // 2. 校验选课任务是否存在
        CourseSelectionInfo courseSelectionInfo = courseSelectionInfoMapper.selectById(courseSelectionId);
        if (courseSelectionInfo == null || courseSelectionInfo.getIsDelete() == 1)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "选课任务不存在或已被删除");
        }

        // 3. 校验班级是否存在
        List<Long> classIds = request.getClassIds();
        long validClassCount = classesInfoMapper.selectCount(
                Wrappers.<ClassesInfo>lambdaQuery()
                        .in(ClassesInfo::getId, classIds)
                        .eq(ClassesInfo::getIsDelete, 0)
        );
        if (validClassCount != classIds.size())
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "部分班级ID无效或已被删除");
        }

        // 4. 查询当前已存在的关联班级，避免重复插入
        List<CourseSelectionClasses> existingMappings = courseSelectionClassesMapper.selectList(
                Wrappers.<CourseSelectionClasses>lambdaQuery()
                        .eq(CourseSelectionClasses::getCourseSelectionId, courseSelectionId)
                        .in(CourseSelectionClasses::getClassId, classIds)
                        .eq(CourseSelectionClasses::getIsDelete, 0)
        );
        Set<Long> existingClassIds = existingMappings.stream()
                                                     .map(CourseSelectionClasses::getClassId)
                                                     .collect(Collectors.toSet());

        // 5. 过滤掉已存在的班级ID
        List<Long> newClassIds = classIds.stream()
                                         .filter(classId -> !existingClassIds.contains(classId))
                                         .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(newClassIds))
        {
            return; // 没有新班级需要插入
        }

        // 6. 批量插入新关联记录
        List<CourseSelectionClasses> newMappings = newClassIds.stream().map(classId ->
        {
            CourseSelectionClasses mapping = new CourseSelectionClasses();
            mapping.setCourseSelectionId(courseSelectionId);
            mapping.setClassId(classId);
            return mapping;
        }).collect(Collectors.toList());

        boolean saved = this.saveBatch(newMappings);
        if (!saved)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "新增班级关联记录失败");
        }
    }

    /**
     * 获取选课任务下的班级信息
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @version 2025/2/6 20:42
     */
    @Override
    public List<Long> getClassesByCourseSelectionId(Long courseSelectionId)
    {
        LambdaQueryWrapper<CourseSelectionClasses> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseSelectionClasses::getCourseSelectionId, courseSelectionId);
        return this.baseMapper.selectList(queryWrapper).stream()
                .map(CourseSelectionClasses::getClassId)
                .collect(Collectors.toList());
    }
}




