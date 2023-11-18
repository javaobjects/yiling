package com.yiling.export.export.bo;

import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2022-05-26
 */
@Data
@Accessors(chain = true)
public class ExportReportDetailBO {

    /**
     * 报表明细id
     */
    private Long id;

    /**
     * 报表eid
     */
    private Long reportId;

    /**
     * eid
     */
    private Long eid;

    /**
     * 买家eid
     */
    private Long buyerEid;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 商业名称
     */
    private String eName;

    /**
     * erp客户名称
     */
    private String erpCustomerName;

    /**
     * 支付方式：1-线下支付 2-账期 3-预付款 4-在线支付
     */
    private Integer paymentMethod;

    /**
     * 支付方式：1-线下支付 2-账期 3-预付款 4-在线支付
     */
    private String paymentMethodStr;

    /**
     * 订单来源：1-POP-PC平台,2-POP-APP平台,3-B2B-APP平台,4-销售助手-APP平台
     */
    private Integer orderSource;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 报表类型：1-B2B返利 2-流向返利
     */
    private String typeStr;

    /**
     * 商品类型
     */
    private String goodsType;

    /**
     * 商品类型
     */
    private Integer orderGoodsType;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 规格
     */
    private String specifications;

    /**
     * 批准文号
     */
    private String license;

    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 商品ERP编码
     */
    private String goodsErpCode;

    /**
     * 商品生产厂家
     */
    private String goodsManufacturer;

    /**
     * 有效期
     */
    private Date expiryDate;


    /**
     * 购买数量
     */
    private Integer goodsQuantity;

    /**
     * 发货数量
     */
    private Integer deliveryQuantity;

    /**
     * 退货数量
     */
    private Integer refundQuantity;

    /**
     * 收货数量
     */
    private Integer receiveQuantity;

    /**
     * 供货价
     */
    private BigDecimal supplyPrice;

    /**
     * 出货价
     */
    private BigDecimal outPrice;

    /**
     * 商销价
     */
    private BigDecimal merchantSalePrice;

    /**
     * 商品原价
     */
    private BigDecimal originalPrice;

    /**
     * 商品单价
     */
    private BigDecimal goodsPrice;

    /**
     * 商品小计
     */
    private BigDecimal goodsAmount;

    /**
     * 总折扣金额
     */
    private BigDecimal discountAmount;

    /**
     * 总折扣比例
     */
    private BigDecimal discountPercentage;


    /**
     * 支付金额（金额小计-优惠券）
     */
    private BigDecimal paymentAmount;

    /**
     * 活动类型：1-优惠券 2-秒杀 3-特价 4-组合包
     */
    private String activityType;

    /**
     * 活动内容
     */
    private String activityDescribe;

    /**
     * 平台承担折扣比例
     */
    private BigDecimal platformPercentage;

    /**
     * 平台承担折扣金额
     */
    private BigDecimal platformAmount;

    /**
     * 商家承担折扣比例
     */
    private BigDecimal shopPercentage;

    /**
     * 商家承担折扣金额
     */
    private BigDecimal shopAmount;

    /**
     * 购进渠道：1-大运河采购 2-京东采购 3-库存不足
     */
    private Integer purchaseChannel;

    /**
     * 购进渠道：1-大运河采购 2-京东采购 3-库存不足
     */
    private String purchaseChannelStr;

    /**
     * 动销渠道：1-大运河平台销售
     */
    private String syncPurChannel="大运河平台销售";

    /**
     * 销售额金额
     */
    private BigDecimal salesAmount;

    /**
     * 门槛数量（仅参数类型为阶梯时有意义）
     */
    private Integer thresholdCount;

    /**
     * 阶梯活动名称
     */
    private String ladderName;

    /**
     * 阶梯返利金额
     */
    private BigDecimal ladderAmount;

    /**
     * 小三员活动名称
     */
    private String xsyName;

    /**
     * 小三元基础奖励单价
     */
    private BigDecimal xsyPrice;

    /**
     * 小三员奖励金额
     */
    private BigDecimal xsyAmount;

    /**
     * 小三员开始时间
     */
    private Date xsyStartTime;

    /**
     * 小三员结束时间
     */
    private Date xsyEndTime;

    /**
     * 小三员周期
     */
    private String actXsyCycle;

    /**
     * 特殊活动1名称
     */
    private String actFirstName;

    /**
     * 特殊活动1金额
     */
    private BigDecimal actFirstAmount;

    /**
     * 特殊活动1始时间
     */
    private Date actFirstStartTime;

    /**
     * 特殊活动1结束时间
     */
    private Date actFirstEndTime;

    /**
     * 特殊活动1周期
     */
    private String actFirstCycle;

    /**
     * 特殊活动2名称
     */
    private String actSecondName;

    /**
     * 特殊活动2金额
     */
    private BigDecimal actSecondAmount;

    /**
     * 特殊活动2始时间
     */
    private Date actSecondStartTime;

    /**
     * 特殊活动2结束时间
     */
    private Date actSecondEndTime;

    /**
     * 特殊活动2周期
     */
    private String actSecondCycle;

    /**
     * 特殊活动3名称
     */
    private String actThirdName;

    /**
     * 特殊活动3金额
     */
    private BigDecimal actThirdAmount;

    /**
     * 特殊活动3始时间
     */
    private Date actThirdStartTime;

    /**
     * 特殊活动3结束时间
     */
    private Date actThirdEndTime;

    /**
     * 特殊活动3周期
     */
    private String actThirdCycle;

    /**
     * 特殊活动4名称
     */
    private String actFourthName;

    /**
     * 特殊活动4金额
     */
    private BigDecimal actFourthAmount;

    /**
     * 特殊活动4始时间
     */
    private Date actFourthStartTime;

    /**
     * 特殊活动4结束时间
     */
    private Date actFourthEndTime;

    /**
     * 特殊活动4周期
     */
    private String actFourthCycle;

    /**
     * 特殊活动5名称
     */
    private String actFifthName;

    /**
     * 特殊活动5金额
     */
    private BigDecimal actFifthAmount;

    /**
     * 特殊活动5始时间
     */
    private Date actFifthStartTime;

    /**
     * 特殊活动5结束时间
     */
    private Date actFifthEndTime;

    /**
     * 特殊活动5周期
     */
    private String actFifthCycle;












//    /**
//     * 报表id
//     */
//    private Long reportId;

    /**
     * 卖家eid
     */
    private Long sellerEid;

    /**
     * 卖家企业名称
     */
    private String sellerEName;

    /**
     * Erp销售订单号
     */
    private String soNo;

    /**
     * 订单来源，字典：erp_sale_flow_source，1-大运河销售 2-自建平台销售 3-其它渠道销售
     */
    private String soSource;

    /**
     * 订单来源，字典：erp_sale_flow_source，1-大运河销售 2-自建平台销售 3-其它渠道销售
     */
    private String soSourceStr;

    /**
     * 销售日期
     */
    private Date soTime;

    /**
     * 客户编码（客户内码）
     */
    private String enterpriseInnerCode;

    /**
     * 客户名称
     */
    private String enterpriseName;

    /**
     * 商品单位
     */
    private String soUnit;

    /**
     * 商品生产厂家
     */
    private String soManufacturer;

    /**
     * 生产日期
     */
    private Date soProductTime;

    /**
     * 商品内码
     */
    private String goodsInSn;


    /**
     * 所属省份名称
     */
    private String provinceName;


    /**
     * 所属城市名称
     */
    private String cityName;


    /**
     * 所属区域名称
     */
    private String regionName;

    /**
     * 对应以岭的商品id
     */
    private Long ylGoodsId;

    /**
     * 以岭商品名称
     */
    private String ylGoodsName;

    /**
     * 以岭商品规格
     */
    private String ylGoodsSpecification;

    /**
     * 下单时间
     */
    private Date orderCreateTime;

    /**
     * 标识状态：1-正常订单,2-无效订单,3-异常订单
     */
    private Integer identificationStatus;

    /**
     * 标识状态：1-正常订单,2-无效订单,3-异常订单
     */
    private String identificationStatusStr;

    /**
     * 异常原因：1-打单商业,2-锁定终端,3-疑似商业,4-库存不足,5-其他
     */
    private Integer abnormalReason;

    /**
     * 异常原因：1-打单商业,2-锁定终端,3-疑似商业,4-库存不足,5-其他
     */
    private String abnormalReasonStr;

    /**
     * 异常描述
     */
    private String abnormalDescribed;

    /**
     * 返利状态：1-待返利 2-已返利 3-部分返利
     */
    private Integer rebateStatus;

    /**
     * 返利状态：1-待返利 2-已返利 3-部分返利
     */
    private String rebateStatusStr;

}
