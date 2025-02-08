package com.caixy.adminSystem.model.vo.courseSelectionInfo;

import com.caixy.adminSystem.model.dto.courseSelectionInfo.CreateCourseSelectionRequest;
import com.caixy.adminSystem.model.entity.CourseSelectionInfo;
import com.caixy.adminSystem.model.entity.TeacherInfo;
import com.caixy.adminSystem.model.vo.Subjects.SubjectsVO;
import com.caixy.adminSystem.model.vo.UserVO;
import com.caixy.adminSystem.model.vo.teacherInfo.TeacherInfoVO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 选课信息表视图
 *


 */
@Data
public class CourseSelectionInfoVO implements Serializable {
    /**
    * 选课id
    */
    private Long id;
    /**
    * 学期id
    */
    private Long semesterId;
    /**
    * 学期名称
    */
    private String semesterName;
    /**
    * 开始时间
    */
    private Date startDate;
    /**
    * 结束日期
    */
    private Date endDate;
    /**
    * 创建时间
    */
    private Date createTime;
    /**
    * 更新时间
    */
    private Date updateTime;
    /**
    * 是否删除
    */
    private Integer isDelete;

    /**
    * 是否激活
    */
    private Integer isActive;

    /**
    * 创建用户
    */
    private Long creatorId;

    /**
    * 选课任务名称
    */
    private String taskName;

    /**
     * 选课学分最小值
     */
    private Double minCredit;
}
