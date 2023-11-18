package com.yiling.admin.hmc.goods.form;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 添加保险药品
 * @author: gxl
 * @date: 2022/3/31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateGoodsControlForm extends BaseForm {
    @NotNull
    private Long id;

    /**
     * 商品市场价
     */
    @ApiModelProperty(value = "商品市场价")
    @NotNull
    private BigDecimal marketPrice;
    /**
     * 参保价
     */
    @ApiModelProperty(value = "参保价")
    @NotNull
    private BigDecimal insurancePrice;
}