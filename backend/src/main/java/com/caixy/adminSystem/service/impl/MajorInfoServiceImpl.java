package com.caixy.adminSystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caixy.adminSystem.common.ErrorCode;
import com.caixy.adminSystem.exception.BusinessException;
import com.caixy.adminSystem.mapper.MajorInfoMapper;
import com.caixy.adminSystem.model.entity.DepartmentInfo;
import com.caixy.adminSystem.model.entity.MajorInfo;
import com.caixy.adminSystem.model.properties.ImportError;
import com.caixy.adminSystem.model.vo.major.MajorInfoWithDepartmentQueryVO;
import com.caixy.adminSystem.model.vo.major.MajorWithDepartmentVO;
import com.caixy.adminSystem.service.MajorInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author CAIXYPROMISE
 * @description 针对表【major_info(专业信息表)】的数据库操作Service实现
 * @createDate 2024-03-24 21:19:21
 */
@Service
public class MajorInfoServiceImpl extends ServiceImpl<MajorInfoMapper, MajorInfo>
        implements MajorInfoService
{

    /**
     * 根据学院名称判断该学院是否存在：用于学院创建
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/2/11 00:03
     */
    @Override
    public boolean majorExistByName(String majorName, Long departmentId)
    {
        return this.count(new LambdaQueryWrapper<MajorInfo>()
                .eq(MajorInfo::getName, majorName)
                .eq(MajorInfo::getDepartId, departmentId)) > 0;
    }

    /**
     * 根据学院ID判断该学院是否存在：用于班级创建
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/4/29 下午4:03
     */
    @Override
    public boolean majorExistById(Long majorId, Long departId)
    {
        return this.count(new LambdaQueryWrapper<MajorInfo>()
                .eq(MajorInfo::getId, majorId)
                .eq(MajorInfo::getDepartId, departId)) != 0;
    }


    @Override
    public Page<MajorInfoWithDepartmentQueryVO> listMajorInfoWithDepartment(long current, long size)
    {
        return this.baseMapper.listMajorWithDepartmentByPage(new Page<>(current, size));
    }

    @Override
    public List<MajorInfoWithDepartmentQueryVO> getMajorWithDepartment()
    {
        return this.baseMapper.getMajorWithDepartment();
    }

    @Override
    public MajorWithDepartmentVO getMajorWithDepartmentById(long id)
    {
        return this.baseMapper.getMajorWithDepartmentById(id);
    }

    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public void batchInsertOrUpdateMajors(List<MajorInfo> majorInfos)
    {
        this.saveBatch(majorInfos);
    }


    public List<String> checkDepartmentNamesNotExist(List<String> departmentNames, List<String> majorNames)
    {
        List<DepartmentInfo> departments = baseMapper.findDepartmentIdsByNames(departmentNames);
        Set<String> foundDepartmentNames = departments.stream()
                .map(DepartmentInfo::getName)
                .collect(Collectors.toSet());

        // 检查并收集不存在的学院名称
        List<String> notExistDepartmentNames = departmentNames.stream()
                .filter(name -> !foundDepartmentNames.contains(name))
                .collect(Collectors.toList());

        List<Long> departmentIds = departments.stream().map(DepartmentInfo::getId).collect(Collectors.toList());

        List<MajorInfo> existingMajors = baseMapper.checkMajorNamesExistUnderDepartments(majorNames, departmentIds);

        // 只收集已经存在的专业名称
        List<String> existMajorNames = existingMajors.stream()
                .map(MajorInfo::getName)
                .distinct()
                .collect(Collectors.toList());

        return notExistDepartmentNames;
    }


    public Boolean validateMajorIsExist(List<String> departmentNames, List<String> majorNames)
    {
        // 批量查询学院名称是否存在，同时获取对应的学院ID
        List<DepartmentInfo> departments = baseMapper.findDepartmentIdsByNames(departmentNames);
        // 检查是否所有学院名称都被找到了，如果没有，表示有不存在的学院，校验失败
        if (departments.size() < departmentNames.size())
        {
            return false; // 不是所有学院名称都存在
        }

        // 从departments中提取存在的学院ID
        List<Long> departmentIds = departments.stream()
                .map(DepartmentInfo::getId)
                .collect(Collectors.toList());

        // 根据学院ID检查对应的专业名称是否存在
        List<MajorInfo> existingMajors = baseMapper.checkMajorNamesExistUnderDepartments(majorNames, departmentIds);

        // 如果找到了任何一个已存在的专业，表示校验失败
        return existingMajors.isEmpty();
    }

    public Map<String, Object> validateMajorAndDepartment(List<String> collegeNames, List<String> majorNames)
    {
        Map<String, Object> validationResults = new HashMap<>();
        Map<String, Long> collegeNameToIdMap = new HashMap<>();
        List<ImportError> importErrors = new ArrayList<>();

        List<DepartmentInfo> departments = baseMapper.findDepartmentIdsByNames(collegeNames);
        Set<String> foundDepartmentNames = departments.stream()
                .map(DepartmentInfo::getName)
                .collect(Collectors.toSet());

        List<String> notExistDepartmentNames = collegeNames.stream()
                .filter(name -> !foundDepartmentNames.contains(name))
                .collect(Collectors.toList());

        if (!notExistDepartmentNames.isEmpty()) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "以下学院不存在：" + notExistDepartmentNames);
        }

        // 填充学院名称到ID的映射
        for (DepartmentInfo department : departments) {
            collegeNameToIdMap.put(department.getName(), department.getId());
        }

        // 检查专业是否已存在
//        List<MajorInfo> existingMajors = baseMapper.checkMajorNamesExistUnderDepartments(majorNames, departmentIds);
//        for (MajorInfo major : existingMajors) {
//            ImportError error = new ImportError();
//            error.setMajorName(major.getName());
//            error.setErrorType("MajorExists");
//            importErrors.add(error);
//        }

        validationResults.put("collegeNameToIdMap", collegeNameToIdMap);
        validationResults.put("importErrors", importErrors);
        return validationResults;
    }
}




