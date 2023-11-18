package com.yiling.hmc.admin.order.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: benben.jia
 * @data: 2023/03/03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddMerchantRemarkForm extends BaseForm {

    @NotNull
    @ApiModelProperty("订单id")
    private Long id;

    @ApiModelProperty("商家运营备注")
    private String merchantRemark;

}
