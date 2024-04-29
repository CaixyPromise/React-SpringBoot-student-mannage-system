package com.caixy.adminSystem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.caixy.adminSystem.model.entity.MajorInfo;
import com.caixy.adminSystem.model.vo.major.MajorInfoWithDepartmentQueryVO;
import com.caixy.adminSystem.model.vo.major.MajorWithDepartmentVO;

import java.util.List;
import java.util.Map;

/**
 * @author CAIXYPROMISE
 * @description 针对表【major_info(专业信息表)】的数据库操作Service
 * @createDate 2024-03-24 21:19:21
 */
public interface MajorInfoService extends IService<MajorInfo>
{

    boolean majorExistByName(String departmentName, Long departmentId);

    boolean majorExistById(Long majorId, Long departId);

    Page<MajorInfoWithDepartmentQueryVO> listMajorInfoWithDepartment(long current, long size);

    List<MajorInfoWithDepartmentQueryVO> getMajorWithDepartment();

    MajorWithDepartmentVO getMajorWithDepartmentById(long id);

    void batchInsertOrUpdateMajors(List<MajorInfo> majorInfos);

//    Map<String, Boolean> checkMajorExistenceUnderDepartments(List<MajorInfo> majorInfos);

    Map<String, Object> validateMajorAndDepartment(List<String> collegeNames, List<String> majorNames);
}
