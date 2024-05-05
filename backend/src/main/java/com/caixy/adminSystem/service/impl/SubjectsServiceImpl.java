package com.caixy.adminSystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caixy.adminSystem.common.ErrorCode;
import com.caixy.adminSystem.exception.BusinessException;
import com.caixy.adminSystem.model.dto.subject.SubjectsQueryRequest;
import com.caixy.adminSystem.model.entity.Subjects;
import com.caixy.adminSystem.model.vo.Subjects.SubjectsVO;
import com.caixy.adminSystem.service.SubjectsService;
import com.caixy.adminSystem.mapper.SubjectsMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author CAIXYPROMISE
* @description 针对表【subjects】的数据库操作Service实现
* @createDate 2024-04-02 22:30:06
*/
@Service
public class SubjectsServiceImpl extends ServiceImpl<SubjectsMapper, Subjects>
    implements SubjectsService{

    @Override
    public void validSubjects(Subjects subjects, boolean add)
    {
        QueryWrapper<Subjects> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", subjects.getName());
        if (this.count(queryWrapper) > 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "学科名称已存在");
        }
    }

    @Override
    public QueryWrapper<Subjects> getQueryWrapper(SubjectsQueryRequest SubjectsQueryRequest)
    {
        return null;
    }

    @Override
    public Page<Subjects> searchFromEs(SubjectsQueryRequest SubjectsQueryRequest)
    {
        return null;
    }

    @Override
    public SubjectsVO getSubjectsVO(Subjects Subjects, HttpServletRequest request)
    {
        return null;
    }

    @Override
    public Page<SubjectsVO> getSubjectsVOPage(Page<Subjects> SubjectsPage, HttpServletRequest request)
    {
        return null;
    }

    /**
     * 获取所有学科信息
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/4/29 下午10:18
     */
    @Override
    public List<SubjectsVO> getAllSubjectsVO()
    {
        List<Subjects> subjectsList = list();
        return subjectsList.stream().map(this::getSubjectsVO).collect(Collectors.toList());
    }

    private SubjectsVO getSubjectsVO(Subjects Subjects)
    {
        SubjectsVO subjectsVO = new SubjectsVO();
        BeanUtils.copyProperties(Subjects, subjectsVO);
        return subjectsVO;
    }

    /**
     * 检查成绩是否是符合科目要求
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/5/3 下午5:53
     */
    @Override
    public boolean checkGradeIsValid(Long grade, Subjects subjects)
    {
        return grade <= subjects.getGradeMin() || grade >= subjects.getGradeMax();
    }
}




