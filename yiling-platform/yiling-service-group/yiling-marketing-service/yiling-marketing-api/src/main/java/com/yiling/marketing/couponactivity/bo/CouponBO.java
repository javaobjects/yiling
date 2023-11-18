package com.yiling.marketing.couponactivity.bo;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2021/11/14
 */
@Data
public class CouponBO implements java.io.Serializable  {

    private static final long serialVersionUID = 498081405819291634L;

    /**
     * ID
     */
    private Long id;

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 企业名称
     */
    private String ename;

    /**
     * 状态：1-未使用；2-已使用；3-已过期
     */
    private Integer usedStatus;

    /**
     * 优惠券活动ID
     */
    private Long activityId;

    /**
     * 优惠券名称
     */
    private String activityName;

    /**
     * 优惠券活动企业ID
     */
    private Long activityEid;

    /**
     * 优惠券活动企业名称
     */
    private String activityEname;

    /**
     * 类型（1-满减券；2-满折券）
     */
    private Integer type;

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
     * 可用供应商（1-全部供应商；2-部分供应商）
     */
    private Integer enterpriseLimit;

    /**
     * 可用商品（1-全部商品；2-部分商品）
     */
    private Integer goodsLimit;

    /**
     * 用券时间（1-固定时间；2-发放领取后生效）
     */
    private Integer useDateType;

    /**
     * 发放/领取xx天后失效
     */
    private Integer expiryDays;

    /**
     * 优惠券类型：1-商品优惠券 2-会员优惠券
     */
    private Integer memberType;

    /**
     * 1全部会员方案 ，2部分会员方案
     */
    private Integer memberLimit;

    /**
     * 开始时间
     */
    private Date beginTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 用券人ID
     */
    private Long userId;

    /**
     * 用券人名称
     */
    private String userName;

    /**
     * 是否可领取，商家券（1-用户可领 2-用户不可领）
     */
    private Integer canGet;

}
