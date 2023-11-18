package com.yiling.sales.assistant.app.agreement.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yap
 * @date: 2021/09/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementGoodsVO extends BaseVO {

    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    private Long   goodsId;
    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    /**
     * 售卖规格
     */
    @ApiModelProperty(value = "售卖规格")
    private String sellSpecifications;

    /**
     * 基价
     */
    @ApiModelProperty(value = "基价")
    private BigDecimal price;

    /**
     * 专利类型 1-非专利 2-专利
     */
    @ApiModelProperty(value = "专利类型 1-非专利 2-专利")
    private Integer isPatent;

}
