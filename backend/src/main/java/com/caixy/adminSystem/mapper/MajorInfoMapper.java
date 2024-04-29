package com.caixy.adminSystem.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.caixy.adminSystem.model.entity.DepartmentInfo;
import com.caixy.adminSystem.model.entity.MajorInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.caixy.adminSystem.model.vo.department.UserDepartmentMajorVO;
import com.caixy.adminSystem.model.vo.major.MajorInfoWithDepartmentQueryVO;
import com.caixy.adminSystem.model.vo.major.MajorWithDepartmentVO;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* @author CAIXYPROMISE
* @description 针对表【major_info(专业信息表)】的数据库操作Mapper
* @createDate 2024-03-24 21:19:21
* @Entity com.caixy.adminSystem.model.entity.MajorInfo
*/
public interface MajorInfoMapper extends BaseMapper<MajorInfo> {

    List<MajorInfoWithDepartmentQueryVO> getMajorWithDepartment();
    Page<MajorInfoWithDepartmentQueryVO> listMajorWithDepartmentByPage(Page<UserDepartmentMajorVO> page);
    /**
     * 根据专业ID获取专业及其所属部门的详细信息。
     * <p>
     * 获取一个专业的详细信息，包括专业ID、专业名称、创建时间、更新时间，
     * 以及所属部门的ID和名称。
     * 忽略已被标记为删除的专业。
     * </p>
     *
     * @param id 要查询的专业的ID。
     * @return {@link MajorWithDepartmentVO} 包含专业和其所属部门信息。如果没有找到对应的专业，返回 {@code null}。
     */
    MajorWithDepartmentVO getMajorWithDepartmentById(@Param("id") Long id);
    List<DepartmentInfo> findDepartmentIdsByNames(@Param("names") List<String> names);
    List<MajorInfo> checkMajorNamesExistUnderDepartments(@Param("majorNames") List<String> majorNames, @Param("departmentIds") List<Long> departmentIds);

}




