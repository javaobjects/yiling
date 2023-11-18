package com.yiling.mall.coupon.bo;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2021/12/30
 */
@Data
public class AutoGiveCouponActivityBO implements java.io.Serializable{

    private static final long serialVersionUID = 8338460641485329127L;

    // ****************************** 自动发券活动信息 ******************************

    /**
     * 自动发券活动ID
     */
    private Long autoGiveId;

    /**
     * 自动发券活动名称
     */
    private String autoGiveName;

    /**
     * 活动开始时间
     */
    private Date autoGiveBeginTime;

    /**
     * 活动结束时间
     */
    private Date autoGiveEndTime;

    /**
     * 自动发券类型（1-订单累计金额；2-会员自动发券）
     */
    private Integer autoGiveType;

    /**
     * 累计金额/数量（如果是订单累计金额，则表示累计金额。如果是订单累计数量，则表示累计订单数量。）
     */
    private Integer cumulative;

    /**
     * 最多发放次数
     */
    private Integer maxGiveNum;

    /**
     * 状态：1-启用 2-停用
     */
    private Integer autoGiveStatus;


    // ****************************** 优惠券活动信息 ******************************

    /**
     * 自动发券活动ID
     */
    private Long couponActivityId;

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 企业名称
     */
    private String ename;

    /**
     * 优惠券活动类型（1-满减券；2-满折券）
     */
    private Integer type;

    /**
     * 优惠券活动名称
     */
    private String name;

    /**
     * 优惠券描述
     */
    private String description;

    /**
     * 预算编号
     */
    private String budgetCode;

    /**
     * 活动分类（1-平台活动；2-商家活动）
     */
    private Integer sponsorType;

    /**
     * 使用门槛（1-支付金额满额）
     */
    private Integer threshold;

    /**
     * 门槛金额/件数
     */
    private BigDecimal thresholdValue;

    /**
     * 优惠内容（金额）
     */
    private BigDecimal discountValue;

    /**
     * 最高优惠额度（折扣券）
     */
    private BigDecimal discountMax;

    /**
     * 费用承担方（1-平台；2-商家；3-共同承担）
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
     * 用券时间（1-固定时间；2-发放领取后生效）
     */
    private Integer useDateType;

    /**
     * 用券开始时间
     */
    private Date beginTime;

    /**
     * 用券结束时间
     */
    private Date endTime;

    /**
     * 发放/领取xx天后失效
     */
    private Integer expiryDays;

    /**
     * 平台限制（1-全部平台；2-部分平台）
     */
    private Integer platformLimit;

    /**
     * 选择平台（1-B2B；2-销售助手）
     */
    private String platformSelected;

    /**
     * 支付方式限制（1-全部支付方式；2-部分支付方式）
     */
    private Integer payMethodLimit;

    /**
     * 可用支付方式（1-在线支付；2-货到付款；3-账期。2,3现在不支持）
     */
    private String payMethodSelected;

    /**
     * 可叠加促销列表（1-满赠）
     */
    private String coexistPromotion;

    /**
     * 优惠券总数量
     */
    private Integer totalCount;


    /**
     * 已使用数量
     */
    private Integer useCount;

    /**
     * 状态：1-启用 2-停用 3-废弃
     */
    private Integer status;

    /**
     * 活动状态（1-未开始 2-进行中 3-已结束）
     */
    private Integer activityStatus;

    /**
     * 供应商限制（1-全部供应商；2-部分供应商）
     */
    private Integer enterpriseLimit;


}
