package generator.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 学生信息表
 * @TableName student_info
 */
@TableName(value ="student_info")
@Data
public class StudentInfo implements Serializable {
    /**
     * 学生ID
     */
    @TableId
    private Long id;

    /**
     * 学号
     */
    private String stuId;

    /**
     * 学生姓名
     */
    private String stuName;

    /**
     * 学生性别
     */
    private Integer stuSex;

    /**
     * 学生学院id
     */
    private Long stuDeptId;

    /**
     * 学生专业id
     */
    private Long stuMajorId;

    /**
     * 学生班级Id
     */
    private Long stuClassId;

    /**
     * 创建人Id
     */
    private Long creatorId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除逻辑键
     */
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}