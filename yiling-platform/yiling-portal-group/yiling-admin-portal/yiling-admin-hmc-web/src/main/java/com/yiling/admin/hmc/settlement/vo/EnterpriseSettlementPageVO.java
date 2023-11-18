package com.yiling.admin.hmc.settlement.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2022/4/6
 */
@Data
public class EnterpriseSettlementPageVO extends BaseVO {

    @ApiModelProperty("商品id")
    private Long goodsId;

    @ApiModelProperty("售卖规格id")
    private Long sellSpecificationsId;
    
    @ApiModelProperty("药品规格")
    private String goodsSpecifications;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("售卖数量")
    private Integer goodsQuantity;

    @ApiModelProperty("结算单价")
    private BigDecimal price;

    @ApiModelProperty("订单id")
    private Long orderId;

    @ApiModelProperty("订单编号")
    private String orderNo;

    @ApiModelProperty("合计")
    private BigDecimal goodsAmount;

    @ApiModelProperty("创建日期")
    private Date createTime;

    @ApiModelProperty("订单完成日期")
    private Date finishTime;

    @ApiModelProperty("保司结算状态:1-待结算/2-已结算/3-无需结算失效单/4-预付款抵扣已结")
    private Integer insuranceSettlementStatus;

    @ApiModelProperty("管控渠道")
    private List<String> channelNameList;

    @ApiModelProperty("结账金额")
    private BigDecimal settlementAmount;

    @ApiModelProperty("对账执行时间")
    private Date executionTime;

    @ApiModelProperty("结算完成时间")
    private Date settlementTime;
}
