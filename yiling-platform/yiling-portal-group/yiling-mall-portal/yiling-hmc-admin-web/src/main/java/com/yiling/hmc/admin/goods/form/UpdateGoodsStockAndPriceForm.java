package com.yiling.hmc.admin.goods.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author: fan.shen
 * @date: 2022/4/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateGoodsStockAndPriceForm extends BaseForm {

    private Integer id;

    @ApiModelProperty(value = "销售价")
    private String salePrice;

    @ApiModelProperty(value = "成本价")
    private String marketPrice;

    @ApiModelProperty(value = "库存")
    private BigDecimal stock;

    @ApiModelProperty(value = "安全库存")
    private Integer safeStock;
}