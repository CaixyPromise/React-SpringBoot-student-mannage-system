package com.caixy.adminSystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caixy.adminSystem.common.ErrorCode;
import com.caixy.adminSystem.exception.BusinessException;
import com.caixy.adminSystem.mapper.SubjectsMapper;
import com.caixy.adminSystem.model.dto.subject.SubjectsQueryRequest;
import com.caixy.adminSystem.model.entity.Subjects;
import com.caixy.adminSystem.model.vo.Subjects.SubjectsVO;
import com.caixy.adminSystem.service.SubjectsService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author CAIXYPROMISE
 * @description 针对表【subjects】的数据库操作Service实现
 * @createDate 2024-04-02 22:30:06
 */
@Service
public class SubjectsServiceImpl extends ServiceImpl<SubjectsMapper, Subjects>
        implements SubjectsService
{

    @Override
    public void validSubjects(Subjects subjects, boolean add)
    {
        if (add)
        {
            QueryWrapper<Subjects> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name", subjects.getName());
            if (this.count(queryWrapper) > 0)
            {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "学科名称已存在");
            }
        }
        // 校验分数和分数线合法性
        validScoreAndLine(subjects);
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

    /**
     * 根据学科id获取学科信息
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @version 2025/2/6 21:02
     */
    @Override
    public List<SubjectsVO> getSubjectVOByIds(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        List<Subjects> subjectsList = listByIds(ids);
        return subjectsList.stream().map(this::getSubjectsVO).collect(Collectors.toList());
    }


    /**
     * 批量获取学科信息
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @version 2025/1/26 23:49
     */
    @Override
    public Map<Long, SubjectsVO> getSubjectsVOMapByIds(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new HashMap<>();
        }
        List<Subjects> subjectsList = listByIds(ids);
        return subjectsList.stream().map(this::getSubjectsVO).collect(Collectors.toMap(SubjectsVO::getId, Function.identity()));
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
        return grade < subjects.getGradeMin() || grade > subjects.getGradeMax();
    }


    private static void validScoreAndLine(Subjects subjects)
    {
        // 判断数值是否在安全区间内
        Integer scoreMax = subjects.getGradeMax();
        Integer scoreMin = subjects.getGradeMin();

        // 检查分数是否为null
        if (scoreMin == null || scoreMax == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "分数不能为null");
        }

        // 检查分数是否超过极值
        if (scoreMin < 0 || scoreMax <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "分数必须大于0");
        }

        // 检查最大分数是否设置在合理的范围内
        if (scoreMax > 2000)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "最大分数不能超过2000");
        }

        // 检查最小分数是否大于最大分数
        if (scoreMin > scoreMax)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "最小分数不能大于最大分数");
        }

        if (scoreMin.equals(scoreMax))
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "最小分数和最大分数不能相等");
        }
        Integer creditHours = subjects.getCreditHours();
        if (creditHours == null || creditHours <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "学时不能为null且必须大于0");
        }
        if (creditHours > 240) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "学时不能超过240");
        }

        // 检查分数线是否在合理范围内
        Integer excellentLine = subjects.getGradeExcellent();
        Integer failLine = subjects.getGradeFail();

        if (excellentLine == null || failLine == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "分数线不能为null");
        }

        if (excellentLine < 0 || failLine < 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "分数线必须大于等于0");
        }

        if (excellentLine > scoreMax || failLine > scoreMax)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "分数线不能大于最大分数");
        }

        if (excellentLine <= failLine)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "优秀分数线必须小于不及格分数线");
        }
    }


}




