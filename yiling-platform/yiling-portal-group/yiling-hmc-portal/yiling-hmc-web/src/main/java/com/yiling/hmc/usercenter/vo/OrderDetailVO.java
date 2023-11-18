package com.yiling.hmc.usercenter.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author fan.shen
 * @date 2022/4/22
 */
@Data
public class OrderDetailVO extends BaseVO {

    /**
     * 订单id
     */
    @ApiModelProperty("订单id")
    private Long orderId;

    /**
     * 保险id
     */
    @ApiModelProperty("保险id")
    private Long insuranceRecordId;

    /**
     * 药品id
     */
    @ApiModelProperty("药品id")
    private Long hmcGoodsId;

    /**
     * yl商品id
     */
    @ApiModelProperty("yl商品id")
    private Long goodsId;

    /**
     * 售卖规格id
     */
    @ApiModelProperty("售卖规格id")
    private Long sellSpecificationsId;

    /**
     * 药品名称
     */
    @ApiModelProperty("药品名称")
    private String goodsName;

    /**
     * 参保价
     */
    @ApiModelProperty("参保价")
    private BigDecimal insurancePrice;

    /**
     * 药品规格
     */
    @ApiModelProperty("药品规格")
    private String goodsSpecifications;

    /**
     * 购买数量
     */
    @ApiModelProperty("购买数量")
    private Long goodsQuantity;

    /**
     * 商品小计 = 参保价 * 数量
     */
    @ApiModelProperty("商品小计 = 参保价 * 数量")
    private BigDecimal goodsAmount;

    /**
     * 图片
     */
    @ApiModelProperty("图片")
    private String pic;

}
