package com.yiling.settlement.report.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 返利明细表-b2b订单
 * </p>
 *
 * @author dexi.yao
 * @date 2022-05-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("report_detail_b2b")
public class ReportDetailB2bDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 报表eid
     */
    private Long reportId;

    /**
     * 卖家eid
     */
    private Long sellerEid;

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
     * 订单明细ID
     */
    private Long detailId;

    /**
     * 订单来源：1-POP-PC平台,2-POP-APP平台,3-B2B-APP平台,4-销售助手-APP平台
     */
    private Integer orderSource;

    /**
     * 订单状态：10-待审核 20-待发货 30-已发货 40-已收货 100-已完成 -10-已取消
     */
    private Integer orderStatus;

    /**
     * 支付方式：1-线下支付 2-账期 3-预付款 4-在线支付
     */
    private Integer paymentMethod;

    /**
     * 支付状态：1-待支付 2-已支付
     */
    private Integer paymentStatus;

    /**
     * 商品id
     */
    private Long goodsId;

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
     * 商品规格
     */
    private String specifications;

    /**
     * 批准文号
     */
    private String license;

    /**
     * 批号
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
     * 下单数量
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
     * 商品单价
     */
    private BigDecimal goodsPrice;

    /**
     * 商品原价
     */
    private BigDecimal originalPrice;

    /**
     * 金额小计
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
     * 阶梯开始时间
     */
    private Date ladderStartTime;

    /**
     * 阶梯结束时间
     */
    private Date ladderEndTime;

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
     * 小三员奖励类型：1-金额 2-百分比
     */
    private Integer xsyRewardType;

    /**
     * 小三员开始时间
     */
    private Date xsyStartTime;

    /**
     * 小三员结束时间
     */
    private Date xsyEndTime;

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
     * 下单时间
     */
    private Date orderCreateTime;

    /**
     * 收货时间
     */
    private Date receiveTime;

    /**
     * 标识状态：1-正常订单,2-无效订单,3-异常订单
     */
    private Integer identificationStatus;

    /**
     * 异常原因：1-打单商业,2-锁定终端,3-疑似商业,4-库存不足,5-其他
     */
    private Integer abnormalReason;

    /**
     * 异常描述
     */
    private String abnormalDescribed;

    /**
     * 返利金额小计
     */
    private BigDecimal subRebate;

    /**
     * 返利状态：1-待返利 2-已返利 3-部分返利
     */
    private Integer rebateStatus;

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;


}
