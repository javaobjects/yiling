package com.yiling.admin.hmc.order.vo;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * HMC处方药品信息VO
 *
 * @author: fan.shen
 * @data: 2023/05/10
 */
@Data
public class HmcPrescriptionCommodityVO {

    @ApiModelProperty("ih平台药品id")
    private Integer ihGoodsId;

    @ApiModelProperty("hmc药品id")
    private Integer hmcGoodsId;

    @ApiModelProperty("hmc规格id")
    private Integer hmcSellSpecificationsId;

    @ApiModelProperty("药品名称")
    private String goodsName;

    @ApiModelProperty("药品规格")
    private String specifications;

    @ApiModelProperty("药品数量")
    private BigDecimal num;

    @ApiModelProperty("药品单价")
    private String goodsPrice;

    @ApiModelProperty("药品图片")
    private String picture;

    @ApiModelProperty("煎法-中药")
    private String decoction;

    @ApiModelProperty("企业商品ID")
    private Long goodsId;

}
