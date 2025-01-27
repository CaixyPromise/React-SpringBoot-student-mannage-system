package com.caixy.adminSystem.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.caixy.adminSystem.model.dto.teacherInfo.TeacherInfoQueryRequest;
import com.caixy.adminSystem.model.entity.TeacherInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.caixy.adminSystem.model.vo.teacherInfo.TeacherInfoVO;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
* @author CAIXYPROMISE
* @description 针对表【teacher_info(教师信息表)】的数据库操作Mapper
* @createDate 2025-01-11 19:10:20
* @Entity com.caixy.adminSystem.model.entity.TeacherInfo
*/
public interface TeacherInfoMapper extends BaseMapper<TeacherInfo> {
    /**
     * 分页查询教师信息，同时查询学院和专业名称
     * @param page 分页对象
     * @return 分页查询结果
     */
    IPage<TeacherInfoVO> selectTeacherInfoPage(@Param("page") Page<TeacherInfo> page,
                                               @Param("query") TeacherInfoQueryRequest query);


    /**
     * 根据条件查询单条教师信息
     *
     * @param query 查询条件
     * @return 教师信息
     */
    TeacherInfoVO selectTeacherInfoByConditions(@Param("query") TeacherInfoVO query);
    /**
     * 批量根据教师ID查询教师信息
     *
     * @param ids 教师ID列表
     * @return 教师信息列表
     */
    List<TeacherInfoVO> selectTeacherInfoByIds(@Param("ids") Collection<Long> ids);
}




