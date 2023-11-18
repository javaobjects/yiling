package com.yiling.admin.hmc.goods.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2022/4/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateGoodsPurchaseForm extends BaseForm {

    @NotNull
    private Long id;
    /**
     * 1-线上 2-线下
     */
    @ApiModelProperty(value = "1-线上 2-线下")
    private Integer channelType;

    /**
     * 0-关闭 1-开启
     */
    @ApiModelProperty(value = "0-关闭 1-开启")
    private Integer controlStatus;

}