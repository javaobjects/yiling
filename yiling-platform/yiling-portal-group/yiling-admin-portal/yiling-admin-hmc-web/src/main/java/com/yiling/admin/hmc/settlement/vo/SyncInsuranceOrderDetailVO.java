package com.yiling.admin.hmc.settlement.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2022/7/4
 */
@Data
public class SyncInsuranceOrderDetailVO implements Serializable {

    @ApiModelProperty("售卖规格id")
    private Long sellSpecificationsId;

    @ApiModelProperty("药品名称")
    private String goodsName;

    @ApiModelProperty("购买数量")
    private Long goodsQuantity;

    @ApiModelProperty("参保价")
    private BigDecimal insurancePrice;

    @ApiModelProperty("以岭跟终端结算单价")
    private BigDecimal terminalSettlePrice;

    @ApiModelProperty("保司跟以岭结算单价")
    private BigDecimal settlePrice;

    @ApiModelProperty("1管控 0不管控")
    private Integer controlStatus;

    @ApiModelProperty("管控渠道")
    private List<String> channelNameList;
}
