package com.caixy.adminSystem.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.caixy.adminSystem.common.ErrorCode;
import com.caixy.adminSystem.exception.BusinessException;
import com.caixy.adminSystem.model.dto.major.MajorImportData;
import com.caixy.adminSystem.model.entity.MajorInfo;
import com.caixy.adminSystem.model.properties.ImportError;
import com.caixy.adminSystem.model.properties.MajorInfoPair;
import com.caixy.adminSystem.service.MajorInfoService;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 批量导入专业数据监听器
 *
 * @name: com.caixy.adminSystem.listener.MajorDataListener
 * @author: CAIXYPROMISE
 * @since: 2024-03-25 17:14
 **/
@Slf4j
public class MajorDataListener extends AnalysisEventListener<MajorImportData>
{

    private final MajorInfoService majorInfoService;

    // 存储读取到的数据
    private final List<MajorInfoPair> majorInfos = new ArrayList<>();
    /**
     * 导入行hash集合，用于去重
     */
    private final Set<String> processedCombination = new HashSet<>();

    public MajorDataListener(MajorInfoService majorInfoService)
    {

        this.majorInfoService = majorInfoService;
    }

    @Override
    public void invoke(MajorImportData data, AnalysisContext context)
    {
        String collegeName = data.getCollegeName();
        String majorName = data.getMajorName();
        String combination = collegeName + ":" + majorName;

        // 只处理未出现过的组合
        if (!processedCombination.contains(combination))
        {
            processedCombination.add(combination);
            MajorInfoPair majorInfo = new MajorInfoPair();
            majorInfo.setMajorName(majorName);
            majorInfo.setCollegeName(collegeName);
            majorInfos.add(majorInfo);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context)
    {
        // 提取学院和专业名称
        List<String> collegeNames = majorInfos.stream()
                .map(MajorInfoPair::getCollegeName)
                .distinct()
                .collect(Collectors.toList());

        List<String> majorNames = majorInfos.stream()
                .map(MajorInfoPair::getMajorName)
                .distinct()
                .collect(Collectors.toList());

        Map<String, Object> validationResult = majorInfoService.validateMajorAndDepartment(collegeNames, majorNames);
        Map<String, Long> collegeNameToIdMap = (Map<String, Long>) validationResult.get("collegeNameToIdMap");
        List<ImportError> importErrors = (List<ImportError>) validationResult.get("importErrors");

        if (!importErrors.isEmpty())
        {
            // 如果有错误信息，记录日志并抛出异常
            log.error("校验失败: " + importErrors);
            throw new BusinessException(ErrorCode.OPERATION_ERROR, importErrors.toString());
        }

        // 校验通过，转换并准备插入数据
        List<MajorInfo> validMajors = majorInfos.stream()
                .filter(majorInfoPair -> collegeNameToIdMap.containsKey(majorInfoPair.getCollegeName()))
                .map(majorInfoPair ->
                {
                    MajorInfo majorInfo = new MajorInfo();
                    majorInfo.setName(majorInfoPair.getMajorName());
                    majorInfo.setDepartId(collegeNameToIdMap.get(majorInfoPair.getCollegeName()));
                    return majorInfo;
                }).collect(Collectors.toList());

        majorInfoService.batchInsertOrUpdateMajors(validMajors);
    }
}