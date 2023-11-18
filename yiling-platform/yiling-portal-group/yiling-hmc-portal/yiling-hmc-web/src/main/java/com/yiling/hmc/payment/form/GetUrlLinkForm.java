package com.yiling.hmc.payment.form;

import javax.validation.constraints.NotBlank;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 批量支付订单参数
 * @author:wei.wang
 * @date:2021/11/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GetUrlLinkForm extends BaseForm {

    /**
     * payId
     */
    @NotBlank()
    @ApiModelProperty(value = "payId", required = true)
    private String payId;

}
