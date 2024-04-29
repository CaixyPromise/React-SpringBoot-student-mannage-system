package com.caixy.adminSystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caixy.adminSystem.mapper.StudentInfoMapper;
import com.caixy.adminSystem.model.dto.post.PostQueryRequest;
import com.caixy.adminSystem.model.dto.studentInfo.StudentInfoQueryRequest;
import com.caixy.adminSystem.model.entity.Post;
import com.caixy.adminSystem.model.entity.StudentInfo;
import com.caixy.adminSystem.model.vo.PostVO;
import com.caixy.adminSystem.model.vo.StudentInfo.StudentInfoVO;
import com.caixy.adminSystem.service.StudentInfoService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
* @author CAIXYPROMISE
* @description 针对表【student_score】的数据库操作Service实现
* @createDate 2024-04-02 22:30:06
*/
@Service
public class StudentInfoServiceImpl extends ServiceImpl<StudentInfoMapper, StudentInfo>
    implements StudentInfoService
{

    @Override
    public void validStudentInfo(StudentInfo post, boolean add)
    {

    }

    @Override
    public QueryWrapper<StudentInfo> getQueryWrapper(StudentInfoQueryRequest postQueryRequest)
    {
        return null;
    }

    @Override
    public Page<StudentInfo> searchFromEs(StudentInfoQueryRequest postQueryRequest)
    {
        return null;
    }

    @Override
    public StudentInfoVO getStudentInfoVO(StudentInfo post, HttpServletRequest request)
    {
        return null;
    }

    @Override
    public Page<StudentInfoVO> getStudentInfoVOPage(Page<StudentInfo> postPage, HttpServletRequest request)
    {
        return null;
    }
}




