package com.caixy.adminSystem.controller;

import com.caixy.adminSystem.common.BaseResponse;
import com.caixy.adminSystem.common.ResultUtils;
import com.caixy.adminSystem.model.vo.ClassesInfo.ClassesInfoVO;
import com.caixy.adminSystem.model.vo.courseSelectionClasses.ModifyCourseSelectionClassesRequest;
import com.caixy.adminSystem.model.vo.courseSelectionInfo.CourseSelectionInfoVO;
import com.caixy.adminSystem.service.CourseSelectionClassesService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 课程选课班级信息控制器
 *
 * @Author CAIXYPROMISE
 * @since 2025/1/3 15:40
 */
@RestController
@RequestMapping("/courseSelection")
public class CourseSelectionController {

    @Resource
    private CourseSelectionClassesService courseSelectionClassesService;

    /**
     * 新增班级到选课任务
     */
    @PostMapping("/addClasses")
    public BaseResponse<Boolean> addClassesToCourseSelection(@RequestBody @Valid ModifyCourseSelectionClassesRequest request) {
        courseSelectionClassesService.addClassesToCourseSelection(request);
        return ResultUtils.success(true);
    }

}
