package com.yiling.admin.hmc.order.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: benben.jia
 * @data: 2023/03/02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddPlatformRemarkForm extends BaseForm {

    @NotNull
    @ApiModelProperty("订单id")
    private Long id;

    @ApiModelProperty("平台运营备注")
    private String platformRemark;

}
