package com.yiling.ih.patient.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 主诉症状
 *
 * @author: fan.shen
 * @data: 2023/05/15
 */
@Data
@Accessors(chain = true)
public class HmcDiagnosisRecordDetailDescribeDTO implements java.io.Serializable {

    /**
     * 主诉症状
     */
    private String diseaseDescribe;

    /**
     * 病情资料-上传既往病历和查验报告(图片key)
     */
    private List<String> diseaseDescribePicture;


}
