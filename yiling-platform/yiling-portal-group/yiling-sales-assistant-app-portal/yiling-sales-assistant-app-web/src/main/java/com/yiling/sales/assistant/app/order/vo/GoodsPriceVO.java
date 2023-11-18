package com.yiling.sales.assistant.app.order.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 商品价格 VO
 *
 * @author: xuan.zhou
 * @date: 2021/6/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsPriceVO extends BaseVO {

    /**
     * 购买价
     */
    @ApiModelProperty("购买价")
    private BigDecimal buyPrice;

    /**
     * 划线价
     */
    @ApiModelProperty("划线价")
    private BigDecimal linePrice;
}
