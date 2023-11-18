package com.yiling.hmc.activity.vo;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.hmc.activity.form.PatientTagForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 保存医患关系VO
 * @author: fan.shen
 * @date: 2022-09-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveHmcPatientRelVO extends BaseForm {

    @ApiModelProperty("patientId")
    private Integer patientId;

    /**
     * 患者名称
     */
    @ApiModelProperty(value = "患者名称")
    private String patientName;


    /**
     * 身份证号码
     */
    @ApiModelProperty(value = "身份证")
    private String idCard;

    /**
     * 患者年龄
     */
    @ApiModelProperty(value = "患者年龄")
    private Integer patientAge;

    /**
     * 患者性别
     */
    @ApiModelProperty(value = "性别 1男 0女")
    private Integer gender;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String mobile;

    /**
     * 描述
     */
    @ApiModelProperty("描述")
    private String desc;

}