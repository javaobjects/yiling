package com.yiling.marketing.coupon.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CouponHasGetDTO extends BaseDTO {

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 企业名称
     */
    private String ename;

    /**
     * 发放/领取人ID
     */
    private Long getUserId;

    /**
     * 用券人ID
     */
    private Long userId;

    /**
     * 优惠券活动ID
     */
    private Long couponActivityId;

    /**
     * 优惠券活动名称
     */
    private String name;

    /**
     * 活动分类（1-平台活动；2-商家活动）
     */
    private Integer sponsorType;

    /**
     * 优惠券活动类型（1-满减券；2-满折券）
     */
    private Integer type;

    /**
     * 使用状态：1-未使用；2-已使用；3-已过期
     */
    private Integer usedStatus;

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
     * 供应商限制（1-全部供应商；2-部分供应商）
     */
    private Integer enterpriseLimit;

    /**
     * 可用供应商名称列表
     */
    private String enterpriseLimitRules;

    /**
     * 指定商品(1:全部 2:部分)
     */
    private Integer conditionGoods;

    /**
     * 可用供应商名称列表
     */
    private String goodsLimitRules;

    /**
     * 开始时间
     */
    private Date beginTime;

    /**
     * 结束时间
     */
    private Date endTime;

}
