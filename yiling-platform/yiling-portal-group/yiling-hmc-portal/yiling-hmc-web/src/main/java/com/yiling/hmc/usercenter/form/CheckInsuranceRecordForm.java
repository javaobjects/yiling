package com.yiling.hmc.usercenter.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 校验身份证后六位 Form
 *
 * @author: fan.shen
 * @date: 2022/4/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CheckInsuranceRecordForm extends BaseForm {

    /**
     * 保单id
     */
    @ApiModelProperty("保单id")
    private Long id;

    /**
     * 身份证后六位
     */
    @ApiModelProperty("身份证后六位")
    private String number;

}
