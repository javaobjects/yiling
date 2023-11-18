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
public class AddGoodsPurchaseForm extends BaseForm {
    /**
     * 管控表id
     */
    @ApiModelProperty(value = "管控药品id")
    @NotNull
    private Long goodsControlId;

    /**
     * 供应商eid
     */
    @NotNull
    @ApiModelProperty(value = "渠道商eid")
    private Long sellerEid;

    /**
     * 1-线上 2-线下
     */
    @NotNull
    @ApiModelProperty(value = "1-线上 2-线下")
    private Integer channelType;

}