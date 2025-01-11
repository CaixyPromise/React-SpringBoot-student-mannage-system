package com.caixy.adminSystem.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.caixy.adminSystem.model.dto.teacherInfo.TeacherInfoAddRequest;
import com.caixy.adminSystem.model.dto.teacherInfo.TeacherInfoQueryRequest;
import com.caixy.adminSystem.model.entity.TeacherInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.caixy.adminSystem.model.entity.User;
import com.caixy.adminSystem.model.vo.teacherInfo.TeacherInfoVO;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

/**
* @author CAIXYPROMISE
* @description 针对表【teacher_info(教师信息表)】的数据库操作Service
* @createDate 2025-01-11 19:10:20
*/
public interface TeacherInfoService extends IService<TeacherInfo> {

    void validTeacherInfo(TeacherInfo teacherInfo, boolean add);

    @Transactional(rollbackFor = Exception.class)
    Long addTeacher(TeacherInfoAddRequest teacherInfoAddRequest, HttpServletRequest request, User creator);

    QueryWrapper<TeacherInfo> getQueryWrapper(TeacherInfoQueryRequest teacherInfoQueryRequest);

    TeacherInfoVO getTeacherInfoVO(TeacherInfo teacherInfo, HttpServletRequest request);

    Page<TeacherInfoVO> getTeacherInfoVOPage(Page<TeacherInfo> teacherInfoPage);
}
