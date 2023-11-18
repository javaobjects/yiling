package com.yiling.marketing.presale.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 营销活动主表
 * </p>
 *
 * @author zhangy
 * @date 2022-08-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PresaleActivityGoodsDTO extends BaseDTO {


    /**
     * 活动名称
     */
    private String name;

    /**
     * 活动分类（1-平台活动；2-商家活动；）
     */
    private Integer sponsorType;

    /**
     * 生效开始时间
     */
    private Date beginTime;

    /**
     * 生效结束时间
     */
    private Date endTime;


    /**
     * 费用承担方（1-平台承担；2-商家承担；）
     */
    private Integer bear;

    /**
     * 平台承担比例
     */
    private BigDecimal platformRatio;

    /**
     * 商家承担比例
     */
    private BigDecimal businessRatio;

    /**
     * 其他(1-新客适用,多个值用逗号隔开)
     */
    private String conditionOther;

    /**
     * 预售类型（1-定金预售；2-全款预售）
     */
    private Integer presaleType;

    /**
     * 支付尾款开始时间
     */
    private Date finalPayBeginTime;

    /**
     * 支付尾款结束时间
     */
    private Date finalPayEndTime;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 企业名称
     */
    private String ename;

    /**
     * 预售价
     */
    private BigDecimal presaleAmount;

    /**
     * 定金比例
     */
    private BigDecimal depositRatio;

    /**
     * 促销方式：0-无 1-定金膨胀 2-尾款立减
     */
    private Integer goodsPresaleType;

    /**
     * 定金膨胀倍数
     */
    private BigDecimal expansionMultiplier;

    /**
     * 尾款立减金额
     */
    private BigDecimal finalPayDiscountAmount;

    /**
     * 每人最小预定量
     */
    private Integer minNum;

    /**
     * 每人最大预定量
     */
    private Integer maxNum;

    /**
     * 合计最大预定量
     */
    private Integer allNum;

    /**
     * 当前用户已经购买的数量
     */
    private Integer currentHasBuyNum;

    /**
     * 所有用户已经购买的数量
     */
    private Integer allHasBuyNum;

    /**
     * 定金
     */
    private BigDecimal  deposit;

    /**
     * 尾款
     */
    private BigDecimal  finalPayment;
}
