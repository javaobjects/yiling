package com.yiling.marketing.couponactivity.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CouponActivityMyCouponDTO extends BaseDTO {

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 状态：1-未使用；2-已使用；3-已过期
     */
    private Integer usedStatusType;

    /**
     * 优惠券活动ID
     */
    private Long activityId;

    /**
     * 优惠券名称
     */
    private String activityName;

    /**
     * 优惠券活动类型（1-满减券；2-满折券）
     */
    private Integer type;

    /**
     * 活动分类（1-平台活动；2-商家活动）
     */
    private Integer sponsorType;

    /**
     * 门槛金额/件数（满xx元/件数）
     */
    private BigDecimal thresholdValue;

    /**
     * 优惠内容（减xx元/打xx折）
     */
    private BigDecimal discountValue;

    /**
     * 最高优惠额度（折扣券）
     */
    private BigDecimal discountMax;

    /**
     * 优惠规则-满xx元可用
     */
    private String thresholdValueRules;

    /**
     * 优惠规则-xx元/xx折
     */
    private String discountValueRules;

    /**
     * 优惠规则-最高减xx元
     */
    private String discountMaxRules;

    /**
     * 指定企业(1:全部 2:部分)
     */
    private Integer enterpriseLimit;

    /**
     * 指定商品(1:全部 2:部分)
     */
    private Integer goodsLimit;

    /**
     * 可用供应商名称
     */
    private String enterpriseLimitNames;

    /**
     * 商名可用描述
     */
    private String goodsLimitDescribe;

    /**
     * 开始时间
     */
    private Date beginTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 有效期
     */
    private String effectiveTime;

    /**
     * 用券人ID
     */
    private Long userId;

    /**
     * 用券人名称
     */
    private String userName;

    /**
     * 支付方式描述
     */
    private String payMethodDescribe;

    /**
     * 到期时间描述
     */
    private String dueDateDescribe;

    /**
     * 优惠券类型：1-商品优惠券 2-会员优惠券
     */
    private Integer memberType;

    /**
     * 1全部会员方案 ，2部分会员方案
     */
    private Integer memberLimit;

    /**
     * 会员优惠券关联的会员规格id
     */
    private List<Long> memberIds;

    /**
     * 会员优惠券关联的会员规格id
     */
    private List<MemberStageDTO>memberStageList;
}
