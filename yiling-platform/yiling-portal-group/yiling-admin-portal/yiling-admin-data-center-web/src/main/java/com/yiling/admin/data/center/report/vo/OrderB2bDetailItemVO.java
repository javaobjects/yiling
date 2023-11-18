package com.yiling.admin.data.center.report.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2022/5/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderB2bDetailItemVO extends BaseVO {

    /**
     * 订单号
     */
    @ApiModelProperty("订单号")
    private String orderNo;

    /**
     * 卖家eid
     */
    @ApiModelProperty("卖家eid")
    private Long sellerEid;

    /**
     * 卖家企业名称
     */
    @ApiModelProperty("卖家企业名称")
    private String sellerEName;

    /**
     * 买家eid
     */
    @ApiModelProperty("买家eid")
    private Long buyerEid;

    /**
     * 买家企业名称
     */
    @ApiModelProperty("买家企业名称")
    private String buyerEName;

    /**
     * erp客户名称
     */
    @ApiModelProperty(value = "erp客户名称")
    private String erpCustomerName;

    /**
     * 所属省份名称
     */
    @ApiModelProperty("所属省份名称")
    private String provinceName;

    /**
     * 所属城市名称
     */
    @ApiModelProperty("所属城市名称")
    private String cityName;

    /**
     * 所属区域名称
     */
    @ApiModelProperty("所属区域名称")
    private String regionName;

    /**
     * 下单时间
     */
    @ApiModelProperty("下单时间")
    private Date orderCreateTime;

    /**
     * 支付方式：1-线下支付 2-账期 3-预付款 4-在线支付
     */
    @ApiModelProperty(value = "支付方式：1-线下支付 2-账期 3-预付款 4-在线支付")
    private Integer paymentMethod;

    /**
     * 支付状态：1-待支付 2-已支付
     */
    @ApiModelProperty(value = "支付状态：1-待支付 2-已支付")
    private Integer paymentStatus;

    /**
     * 订单状态：10-待审核 20-待发货 30-已发货 40-已收货 100-已完成 -10-已取消
     */
    @ApiModelProperty(value = "订单状态：10-待审核 20-待发货 30-已发货 40-已收货 100-已完成 -10-已取消")
    private Integer orderStatus;

    /**
     * 收货时间
     */
    @ApiModelProperty(value = "收货时间")
    private Date receiveTime;

    /**
     * 商品ID
     */
    @ApiModelProperty(value = "商品ID")
    private Long goodsId;

    /**
     * 商品类型
     */
    @ApiModelProperty(value = "商品类型")
    private String goodsType;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    /**
     * 规格
     */
    @ApiModelProperty(value = "规格")
    private String specifications;

    /**
     * 批准文号
     */
    @ApiModelProperty(value = "批准文号")
    private String license;

    /**
     * 商品ERP编码
     */
    @ApiModelProperty(value = "商品ERP编码")
    private String goodsErpCode;

    /**
     * 批次号
     */
    @ApiModelProperty(value = "批次号")
    private String batchNo;

    /**
     * 有效期
     */
    @ApiModelProperty(value = "有效期")
    private Date expiryDate;


    /**
     * 购买数量
     */
    @ApiModelProperty(value = "购买数量")
    private Integer goodsQuantity;

    /**
     * 发货数量
     */
    @ApiModelProperty(value = "发货数量")
    private Integer deliveryQuantity;

    /**
     * 收货数量
     */
    @ApiModelProperty(value = "退货数量")
    private Integer refundQuantity;

    /**
     * 收货数量
     */
    @ApiModelProperty(value = "收货数量")
    private Integer receiveQuantity;

    /**
     * 供货价
     */
    @ApiModelProperty(value = "供货价")
    private BigDecimal supplyPrice;

    /**
     * 出货价
     */
    @ApiModelProperty(value = "出货价")
    private BigDecimal outPrice;

    /**
     * 商销价
     */
    @ApiModelProperty(value = "商销价")
    private BigDecimal merchantSalePrice;

    /**
     * 商品原价
     */
    @ApiModelProperty(value = "商品原价")
    private BigDecimal originalPrice;

    /**
     * 商品单价
     */
    @ApiModelProperty(value = "商品单价")
    private BigDecimal goodsPrice;

    /**
     * 商品小计
     */
    @ApiModelProperty(value = "商品小计")
    private BigDecimal goodsAmount;

    /**
     * 总折扣金额
     */
    @ApiModelProperty(value = "总折扣金额")
    private BigDecimal discountAmount;

    /**
     * 总折扣比例
     */
    @ApiModelProperty(value = "总折扣比例")
    private BigDecimal discountPercentage;


    /**
     * 支付金额（金额小计-优惠券）
     */
    @ApiModelProperty(value = "支付金额（金额小计-优惠券）")
    private BigDecimal paymentAmount;

    /**
     * 活动类型：1-优惠券 2-秒杀 3-特价 4-组合包
     */
    @ApiModelProperty(value = "活动类型：1-优惠券 2-秒杀 3-特价 4-组合包")
    private String activityType;

    /**
     * 活动内容
     */
    @ApiModelProperty(value = "活动内容")
    private String activityDescribe;

    /**
     * 平台承担折扣比例
     */
    @ApiModelProperty(value = "平台承担折扣比例")
    private BigDecimal platformPercentage;

    /**
     * 平台承担折扣金额
     */
    @ApiModelProperty(value = "平台承担折扣金额")
    private BigDecimal platformAmount;

    /**
     * 商家承担折扣比例
     */
    @ApiModelProperty(value = "商家承担折扣比例")
    private BigDecimal shopPercentage;

    /**
     * 商家承担折扣金额
     */
    @ApiModelProperty(value = "商家承担折扣金额")
    private BigDecimal shopAmount;

    /**
     * 购进渠道：1-大运河采购 2-京东采购 3-库存不足
     */
    @ApiModelProperty(value = "购进渠道：1-大运河采购 2-京东采购 3-库存不足")
    private Integer purchaseChannel;

    /**
     * 动销渠道：1-大运河平台销售
     */
    @ApiModelProperty(value = "动销渠道：1-大运河平台销售")
    private Integer syncPurChannel=1;

    /**
     * 销售额金额
     */
    @ApiModelProperty(value = "销售额金额")
    private BigDecimal salesAmount;

    /**
     * 门槛数量（仅参数类型为阶梯时有意义）
     */
    @ApiModelProperty(value = "门槛数量（仅参数类型为阶梯时有意义）")
    private Integer thresholdCount;

    /**
     * 阶梯活动名称
     */
    @ApiModelProperty(value = "阶梯活动名称")
    private String ladderName;

    /**
     * 阶梯返利金额
     */
    @ApiModelProperty(value = "阶梯返利金额")
    private BigDecimal ladderAmount;

    /**
     * 阶梯开始时间
     */
    @ApiModelProperty(value = "阶梯开始时间")
    private Date ladderStartTime;

    /**
     * 阶梯结束时间
     */
    @ApiModelProperty(value = "阶梯结束时间")
    private Date ladderEndTime;

    /**
     * 小三员活动名称
     */
    @ApiModelProperty(value = "小三员活动名称")
    private String xsyName;

    /**
     * 小三元基础奖励单价
     */
    @ApiModelProperty(value = "小三元基础奖励单价")
    private BigDecimal xsyPrice;

    /**
     * 小三员奖励金额
     */
    @ApiModelProperty(value = "小三员奖励金额")
    private BigDecimal xsyAmount;

    /**
     * 小三员奖励类型：1-金额 2-百分比
     */
    @ApiModelProperty(value = "小三员奖励类型：1-金额 2-百分比")
    private Integer xsyRewardType;

    /**
     * 小三员开始时间
     */
    @ApiModelProperty(value = "小三员开始时间")
    private Date xsyStartTime;

    /**
     * 小三员结束时间
     */
    @ApiModelProperty(value = "小三员结束时间")
    private Date xsyEndTime;

    /**
     * 特殊活动1名称
     */
    @ApiModelProperty(value = "特殊活动1名称")
    private String actFirstName;

    /**
     * 特殊活动1金额
     */
    @ApiModelProperty(value = "特殊活动1金额")
    private BigDecimal actFirstAmount;

    /**
     * 特殊活动1始时间
     */
    @ApiModelProperty(value = "特殊活动1始时间")
    private Date actFirstStartTime;

    /**
     * 特殊活动1结束时间
     */
    @ApiModelProperty(value = "特殊活动1结束时间")
    private Date actFirstEndTime;

    /**
     * 特殊活动2名称
     */
    @ApiModelProperty(value = "特殊活动2名称")
    private String actSecondName;

    /**
     * 特殊活动2金额
     */
    @ApiModelProperty(value = "特殊活动2金额")
    private BigDecimal actSecondAmount;

    /**
     * 特殊活动2始时间
     */
    @ApiModelProperty(value = "特殊活动2始时间")
    private Date actSecondStartTime;

    /**
     * 特殊活动2结束时间
     */
    @ApiModelProperty(value = "特殊活动2结束时间")
    private Date actSecondEndTime;

    /**
     * 特殊活动3名称
     */
    @ApiModelProperty(value = "特殊活动3名称")
    private String actThirdName;

    /**
     * 特殊活动3金额
     */
    @ApiModelProperty(value = "特殊活动3金额")
    private BigDecimal actThirdAmount;

    /**
     * 特殊活动3始时间
     */
    @ApiModelProperty(value = "特殊活动3始时间")
    private Date actThirdStartTime;

    /**
     * 特殊活动3结束时间
     */
    @ApiModelProperty(value = "特殊活动3结束时间")
    private Date actThirdEndTime;

    /**
     * 特殊活动4名称
     */
    @ApiModelProperty(value = "特殊活动4名称")
    private String actFourthName;

    /**
     * 特殊活动4金额
     */
    @ApiModelProperty(value = "特殊活动4金额")
    private BigDecimal actFourthAmount;

    /**
     * 特殊活动4始时间
     */
    @ApiModelProperty(value = "特殊活动4始时间")
    private Date actFourthStartTime;

    /**
     * 特殊活动4结束时间
     */
    @ApiModelProperty(value = "特殊活动4结束时间")
    private Date actFourthEndTime;

    /**
     * 特殊活动5名称
     */
    @ApiModelProperty(value = "特殊活动5名称")
    private String actFifthName;

    /**
     * 特殊活动5金额
     */
    @ApiModelProperty(value = "特殊活动5金额")
    private BigDecimal actFifthAmount;

    /**
     * 特殊活动5始时间
     */
    @ApiModelProperty(value = "特殊活动5始时间")
    private Date actFifthStartTime;

    /**
     * 特殊活动5结束时间
     */
    @ApiModelProperty(value = "特殊活动5结束时间")
    private Date actFifthEndTime;

    /**
     * 标识状态：1-正常订单,2-无效订单,3-异常订单
     */
    @ApiModelProperty(value = "标识状态：1-正常订单,2-无效订单,3-异常订单")
    private Integer identificationStatus;

    /**
     * 异常原因：1-打单商业,2-锁定终端,3-疑似商业,4-库存不足,5-其他
     */
    @ApiModelProperty(value = "异常原因：1-打单商业,2-锁定终端,3-疑似商业,4-库存不足,5-其他")
    private Integer abnormalReason;

    /**
     * 异常描述
     */
    @ApiModelProperty(value = "异常描述")
    private String abnormalDescribed;

    /**
     * 返利状态：1-待返利 2-已返利 3-部分返利
     */
    @ApiModelProperty(value = "返利状态：1-待返利 2-已返利 3-部分返利")
    private Integer rebateStatus;

}
