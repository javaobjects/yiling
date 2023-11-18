package com.yiling.hmc.order.vo;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import com.yiling.hmc.diagnosis.vo.HmcPrescriptionGoodsInfoVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2023/02/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryMarketOrderPageVO extends BaseVO {

    /**
     * 平台订单编号
     */
    @ApiModelProperty("平台订单编号")
    private String orderNo;

    /**
     * 订单状态:1-预订单待支付,2-已取消,3-待自提,4-待发货,5-待收货,6-已收货,7-已完成,8-取消已退
     */
    @ApiModelProperty("订单状态:1-预订单待支付,2-已取消,3-待自提,4-待发货,5-待收货,6-已收货,7-已完成,8-取消已退")
    private Integer orderStatus;

    /**
     * 订单类型 1-八子，2-处方订单
     */
    @ApiModelProperty("订单类型 1-八子，2-处方订单")
    private Integer marketOrderType;

    /**
     * 药品服务终端id
     */
    @ApiModelProperty("药品服务终端id")
    private Long eid;

    /**
     * 药品服务终端名称
     */
    @ApiModelProperty("药品服务终端名称")
    private String ename;

    /**
     * 支付状态:1-未支付，2-已支付
     */
    @ApiModelProperty("支付状态:1-未支付，2-已支付")
    private Integer paymentStatus;

    /**
     * 运费
     */
    @ApiModelProperty("运费")
    private BigDecimal freightAmount;

    /**
     * 商品总额
     */
    @ApiModelProperty("商品总额")
    private BigDecimal goodsTotalAmount;

    /**
     * 订单总额 = 运费 + 商品总额
     */
    @ApiModelProperty("订单总额 = 运费 + 商品总额")
    private BigDecimal orderTotalAmount;

    @ApiModelProperty("药品详情")
    private List<MarketOrderDetailVO> detailVOS;

    @ApiModelProperty("处方药品详情")
    private HmcPrescriptionGoodsInfoVO prescriptionGoodsInfoVO;

    @ApiModelProperty(value = "处方商品描述",example = "核桃仁 8g、川贝 9g、土鳖虫 12g")
    private String hmcPrescriptionGoodsGoodsDesc;

    /**
     * 快递单号
     */
    @ApiModelProperty("快递单号")
    private String deliverNo;

    /**
     * 快递公司名称
     */
    @ApiModelProperty("快递公司名称")
    private String deliverCompanyName;

    /**
     * 处方id
     */
    @ApiModelProperty("处方id")
    private Long prescriptionId;

    /**
     * 处方价格
     */
    @ApiModelProperty("处方价格")
    private Long prescriptionPrice;

    /**
     * IH 订单id
     */
    @ApiModelProperty("IH 订单id")
    private Long ihOrderId;

    /**
     * IH 处方编号
     */
    @ApiModelProperty("IH 处方编号")
    private String ihPrescriptionNo;

    /**
     * IH 处方订单编号
     */
    @ApiModelProperty("IH 处方订单编号")
    private String ihPrescriptionOrderNo;

    @ApiModelProperty("IH配送商来源 1：以岭互联网医院IH 2：健康中心HMC")
    private Integer ihPharmacySource;



}
