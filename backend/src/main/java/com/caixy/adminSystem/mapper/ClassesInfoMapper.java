package com.caixy.adminSystem.mapper;

import com.caixy.adminSystem.model.dto.classesInfo.DepartmentMajorClassDTO;
import com.caixy.adminSystem.model.entity.ClassesInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.caixy.adminSystem.model.vo.ClassesInfo.ClassesInfoVO;
import com.caixy.adminSystem.model.vo.ClassesInfo.DepartMajorClassDTO;

import java.util.List;
import java.util.Set;

/**
* @author CAIXYPROMISE
* @description 针对表【classes_info(班级信息)】的数据库操作Mapper
* @createDate 2024-04-28 22:48:38
* @Entity com.caixy.adminSystem.model.entity.ClassesInfo
*/
public interface ClassesInfoMapper extends BaseMapper<ClassesInfo> {
    List<ClassesInfoVO> selectClassByMajorAndDepartId(Long departId, Long majorId);
    List<DepartmentMajorClassDTO> fetchAllClassesData();
    List<DepartMajorClassDTO> selectDepartMajorClassList(List<Long> classIds);
}




