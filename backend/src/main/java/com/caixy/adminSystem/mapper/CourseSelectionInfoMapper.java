package com.caixy.adminSystem.mapper;

import com.caixy.adminSystem.model.entity.CourseSelectionInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.caixy.adminSystem.model.vo.courseSelectionInfo.CourseSelectionInfoVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author CAIXYPROMISE
* @description 针对表【course_selection_info(选课信息表)】的数据库操作Mapper
* @createDate 2024-12-26 14:46:52
* @Entity com.caixy.adminSystem.model.entity.CourseSelectionInfo
*/
public interface CourseSelectionInfoMapper extends BaseMapper<CourseSelectionInfo> {
    List<CourseSelectionInfoVO> getStudentTasks(
            @Param("currentSemesterId") Long currentSemesterId,
            @Param("currentTime") Date currentTime,
            @Param("studentClassId") Long studentClassId
    );
}




