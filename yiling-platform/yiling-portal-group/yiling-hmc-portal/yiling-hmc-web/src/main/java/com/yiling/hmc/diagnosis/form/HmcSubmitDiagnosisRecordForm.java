package com.yiling.hmc.diagnosis.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 提交问诊单
 *
 * @author: fan.shen
 * @data: 2023/05/12
 */
@Data
@Accessors(chain = true)
public class HmcSubmitDiagnosisRecordForm {

    @NotNull(message = "不能为空")
    @ApiModelProperty(value = "医生id")
    private Integer doctorId;

    @NotNull(message = "不能为空")
    @ApiModelProperty(value = "问诊类型，0图文1音频2视频")
    private Integer type;

    @ApiModelProperty("精确日期")
    private Date date;

    @ApiModelProperty("所属时段")
    private String belongTime;

    @NotNull(message = "不能为空")
    @ApiModelProperty(value = "就诊人id")
    private Integer patientId;

    @NotBlank(message = "不能为空")
    @Length(message = "症状描述不符合要求", min = 10, max = 300)
    @ApiModelProperty(value = "病情资料-症状描述")
    private String diseaseDescribe;

    @ApiModelProperty(value = "病情资料-上传既往病历和查验报告(图片key)")
    private List<String> diseaseDescribePicture;

}
