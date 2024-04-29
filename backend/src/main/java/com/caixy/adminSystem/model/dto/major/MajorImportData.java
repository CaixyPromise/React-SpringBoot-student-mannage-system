package com.caixy.adminSystem.model.dto.major;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 专业信息导入模板
 *
 * @name: com.caixy.adminSystem.model.dto.major.MajorImportData
 * @author: CAIXYPROMISE
 * @since: 2024-03-25 15:39
 **/

@Data
public class MajorImportData {
    @ExcelProperty("所属学院名称")
    private String collegeName;

    @ExcelProperty("新建专业名称")
    private String majorName;
}
