package com.yiling.hmc.order.vo;

import java.math.BigDecimal;
import java.util.Date;
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
public class QueryMarketOrderVO extends BaseVO {

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
     * 支付方式:1-微信支付
     */
    @ApiModelProperty("支付方式:1-微信支付")
    private Integer paymentMethod;

    /**
     * 支付状态:1-未支付，2-已支付
     */
    @ApiModelProperty("支付状态:1-未支付，2-已支付")
    private Integer paymentStatus;

    /**
     * 支付时间
     */
    @ApiModelProperty("支付时间")
    private Date payTime;

    /**
     * 发货时间
     */
    @ApiModelProperty("发货时间")
    private Date deliverTime;

    /**
     * 收货时间
     */
    @ApiModelProperty("收货时间")
    private Date receiveTime;

    /**
     * 取消时间
     */
    @ApiModelProperty("取消时间")
    private Date cancelTime;

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


    /**
     * 配送方式：1-快递 2-自提
     */
    @ApiModelProperty("配送方式：1-快递 2-自提")
    private Integer deliverType;

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
     * 用户留言
     */
    @ApiModelProperty("用户留言")
    private String remark;

    /**
     * 下单时间
     */
    @ApiModelProperty("下单时间")
    private Date createTime;

    /**
     * 收货人
     */
    @ApiModelProperty("收货人")
    private String name;

    /**
     * 收货人手机
     */
    @ApiModelProperty("收货人手机")
    private String mobile;

    /**
     * 收货人地址
     */
    @ApiModelProperty("收货人地址")
    private String address;

    @ApiModelProperty("药品详情")
    private List<MarketOrderDetailVO> detailVOS;

    @ApiModelProperty("服务器当前时间")
    private Date currentTime;

    /**
     * 收货截止时间
     */
    @ApiModelProperty("收货截止时间")
    private Date receiveCutoffTime;

    /**
     * 取消截止时间
     */
    @ApiModelProperty("取消截止时间")
    private Date cancelCutoffTime;

    @ApiModelProperty("处方药品详情")
    private HmcPrescriptionGoodsInfoVO prescriptionGoodsInfoVO;
}
