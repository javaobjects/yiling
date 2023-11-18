package com.yiling.hmc.diagnosis.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 编辑我的就诊人 form
 * @author: fan.shen
 * @date: 2022/9/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateMyPatientForm extends BaseForm {

    @NotNull
    @ApiModelProperty(value = "患者Id", required = true)
    private Integer patientId;

    @NotBlank
    @ApiModelProperty(value = "患者名称", required = true)
    private String patientName;

    @ApiModelProperty(value = "身份证", required = true)
    private String idCard;

    @ApiModelProperty(value = "地址类型代码")
    private String addressTypeCode;

    @ApiModelProperty(value = "省code", required = true)
    private String provinceCode;

    @ApiModelProperty(value = "市code", required = true)
    private String cityCode;

    @ApiModelProperty(value = "区code", required = true)
    private String regionCode;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @NotBlank
    @ApiModelProperty(value = "手机号", required = true)
    private String mobile;

    @ApiModelProperty(value = "家庭关系,传code码（例：本人传01）")
    private String relation;

    @ApiModelProperty(value = "民族")
    private String nation;

    @ApiModelProperty(value = "民族代码")
    private String nationCode;

    @ApiModelProperty(value = "实名认证标志 1-是，0-否")
    private Integer realNameFlag;

    @NotBlank
    @ApiModelProperty(value = "省Name", required = true)
    private String provinceName;

    @NotBlank
    @ApiModelProperty(value = "市Name", required = true)
    private String cityName;

    @NotBlank
    @ApiModelProperty(value = "区Name", required = true)
    private String regionName;

}