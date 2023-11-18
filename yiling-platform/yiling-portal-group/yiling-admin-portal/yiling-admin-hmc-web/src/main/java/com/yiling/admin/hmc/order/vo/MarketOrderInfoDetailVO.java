package com.yiling.admin.hmc.order.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: benben.jia
 * @data: 2023/03/01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MarketOrderInfoDetailVO extends BaseVO {

    /**
     * 商品图片
     */
    @ApiModelProperty("商品图片")
    private String pic;

    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String goodsName;

    /**
     * 商品规格
     */
    @ApiModelProperty("规格")
    private String goodsSpecification;

    @ApiModelProperty(value = "商品id_企业商品ID", hidden = true)
    private Long goodsId;

    /**
     * 商品标准库ID
     */
    @ApiModelProperty(value = "商品id_标准库SPU-ID", hidden = true)
    private Long standardId;

    /**
     * 售卖规格ID
     */
    @ApiModelProperty(value = "商品id_标准库SKU-ID", hidden = true)
    private Long sellSpecificationsId;

    /**
     * 商品单价
     */
    @ApiModelProperty("销售价格")
    private BigDecimal goodsPrice;

    /**
     * 购买数量
     */
    @ApiModelProperty("数量")
    private Integer goodsQuantity;

}
