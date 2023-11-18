package com.yiling.hmc.admin.order.vo;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 订单明细表
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-25
 */
@Data
public class OrderDetailVO extends BaseVO {

    @ApiModelProperty("订单id")
    private Long orderId;

    @ApiModelProperty("保险记录id")
    private Long insuranceRecordId;

    @ApiModelProperty("保险药品id")
    private Long hmcGoodsId;

    @ApiModelProperty("药品id")
    private Long goodsId;

    @ApiModelProperty("售卖规格id")
    private Long sellSpecificationsId;

    @ApiModelProperty("药品名称")
    private String goodsName;

    @ApiModelProperty("参保价")
    private BigDecimal insurancePrice;

    @ApiModelProperty("保司跟以岭结算单价")
    private BigDecimal settlePrice;

    @ApiModelProperty("保司跟以岭结算额")
    private BigDecimal settleAmount;

    @ApiModelProperty("以岭跟终端结算单价")
    private BigDecimal terminalSettlePrice;

    @ApiModelProperty("以岭跟终端结算额")
    private BigDecimal terminalSettleAmount;

    @ApiModelProperty("药品规格")
    private String goodsSpecifications;

    @ApiModelProperty("购买数量")
    private Long goodsQuantity;

    @ApiModelProperty("商品小计")
    private BigDecimal goodsAmount;

    // =========================================================================

    @ApiModelProperty("1管控 0不管控")
    private Integer controlStatus;

    @ApiModelProperty("管控渠道")
    private List<String> channelNameList;
}
