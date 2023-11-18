package com.yiling.ih.patient.dto.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 补充症状描述 request
 *
 * @author fan.shen
 * @date 2023-05-15
 */
@Data
public class HmcSupplementDiseaseDescribeRequest implements java.io.Serializable {

    private static final long serialVersionUID = 2574332310936289764L;

    /**
     * 问诊单id
     */
    private Integer diagnosisRecordId;

    /**
     * 病情资料-症状描述
     */
    private String diseaseDescribe;

    /**
     * 病情资料-上传既往病历和查验报告(图片key)
     */
    private List<String> diseaseDescribePicture;
}
