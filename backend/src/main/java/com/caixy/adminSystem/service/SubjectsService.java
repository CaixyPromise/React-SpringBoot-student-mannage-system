package com.caixy.adminSystem.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.caixy.adminSystem.model.dto.subject.SubjectsQueryRequest;
import com.caixy.adminSystem.model.entity.Subjects;
import com.baomidou.mybatisplus.extension.service.IService;
import com.caixy.adminSystem.model.vo.Subjects.SubjectsVO;


import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
* @author CAIXYPROMISE
* @description 针对表【subjects】的数据库操作Service
* @createDate 2024-04-02 22:30:06
*/
public interface SubjectsService extends IService<Subjects> {

    /**
     * 校验
     *
     * @param subjects
     * @param add
     */
    void validSubjects(Subjects subjects, boolean add);

    /**
     * 获取查询条件
     *
     * @param SubjectsQueryRequest
     * @return
     */
    LambdaQueryWrapper<Subjects> getQueryWrapper(SubjectsQueryRequest SubjectsQueryRequest);

    /**
     * 从 ES 查询
     *
     * @param SubjectsQueryRequest
     * @return
     */
    Page<Subjects> searchFromEs(SubjectsQueryRequest SubjectsQueryRequest);

    /**
     * 获取帖子封装
     *
     * @param Subjects
     * @param request
     * @return
     */
    SubjectsVO getSubjectsVO(Subjects Subjects, HttpServletRequest request);

    /**
     * 分页获取帖子封装
     *
     * @param SubjectsPage
     * @param request
     * @return
     */
    Page<SubjectsVO> getSubjectsVOPage(Page<Subjects> SubjectsPage, HttpServletRequest request);

    List<SubjectsVO> getAllSubjectsVO();

    List<SubjectsVO> getSubjectVOByIds(Collection<Long> ids);

    Map<Long, SubjectsVO> getSubjectsVOMapByIds(Collection<Long> ids);

    boolean checkGradeIsValid(Long grade, Subjects subjects);
}
