package com.yiling.hmc.diagnosis.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 补充症状描述接口
 *
 * @author: fan.shen
 * @data: 2023/05/15
 */
@Data
@Accessors(chain = true)
public class HmcSupplementDiseaseDescribeForm {
    
    @NotNull(message = "不能为空")
    @ApiModelProperty(value = "问诊单id")
    private Integer diagnosisRecordId;

    @NotBlank(message = "不能为空")
    @Length(message = "症状描述不符合要求", min = 10, max = 300)
    @ApiModelProperty(value = "病情资料-症状描述")
    private String diseaseDescribe;

    @ApiModelProperty(value = "病情资料-上传既往病历和查验报告(图片key)")
    private List<String> diseaseDescribePicture;

}
