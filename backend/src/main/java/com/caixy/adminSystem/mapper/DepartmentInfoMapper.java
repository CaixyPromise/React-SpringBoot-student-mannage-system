package com.caixy.adminSystem.mapper;

import com.caixy.adminSystem.model.dto.department.DepartmentWithMajorsDTO;
import com.caixy.adminSystem.model.entity.DepartmentInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* @author CAIXYPROMISE
* @description 针对表【department_info(学院信息表)】的数据库操作Mapper
* @createDate 2024-03-24 21:19:21
* @Entity com.caixy.adminSystem.model.entity.DepartmentInfo
*/
public interface DepartmentInfoMapper extends BaseMapper<DepartmentInfo> {
    List<DepartmentWithMajorsDTO> selectMajorByDepartmentId(@Param("departmentId") Long departmentId);

    /**
     * 校验指定的学院ID和专业ID是否存在且对应关系正确。
     *
     * @param departmentIds 学院ID列表
     * @param majorIds      专业ID列表
     * @return 查询结果列表，每个元素为一个包含departmentId和majorId的Map
     */
    List<Map<String, Object>> validateDepartmentsAndMajors(
            @Param("departmentIds") List<Long> departmentIds,
            @Param("majorIds") List<Long> majorIds
    );

}




