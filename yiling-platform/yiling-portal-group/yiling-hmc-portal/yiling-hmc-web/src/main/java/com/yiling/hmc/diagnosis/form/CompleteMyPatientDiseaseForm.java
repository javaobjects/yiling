package com.yiling.hmc.diagnosis.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 完善既往史 form
 * @author: fan.shen
 * @date: 2022/9/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CompleteMyPatientDiseaseForm extends BaseForm {

    @NotNull
    @ApiModelProperty("患者id")
    private Integer patientId;

    @ApiModelProperty("过往病史-集合")
    private List<String> historyDisease;

    @ApiModelProperty("家族病史-集合")
    private List<String> familyDisease;

    @ApiModelProperty("过敏史-集合")
    private List<String> allergyHistory;

}