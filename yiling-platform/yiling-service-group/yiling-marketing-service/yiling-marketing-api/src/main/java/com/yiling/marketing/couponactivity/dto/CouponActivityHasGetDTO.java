package com.yiling.marketing.couponactivity.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CouponActivityHasGetDTO extends BaseDTO {

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
     * 活动分类（1-平台活动；2-商家活动）
     */
    private Integer sponsorType;

    /**
     * 门槛金额/件数（满xx元/件数）
     */
    private BigDecimal thresholdValue;

    /**
     * 优惠规则-满xx元可用
     */
    private String thresholdValueRules;

    /**
     * 优惠内容（减xx元/打xx折）
     */
    private BigDecimal discountValue;

    /**
     * 优惠规则-xx元/xx折
     */
    private String discountValueRules;

    /**
     * 最高优惠额度（折扣券）
     */
    private BigDecimal discountMax;

    /**
     * 优惠规则-最高减xx元
     */
    private String discountMaxRules;

    /**
     * 指定商品(1:全部 2:部分)
     */
    private Integer conditionGoods;

    /**
     * 用券开始时间
     */
    private Date beginTime;

    /**
     * 用券结束时间
     */
    private Date endTime;

    /**
     * 优惠规则
     */
    private String couponRules;

    /**
     * 是否已领取（true-是；false-否）
     */
    private Boolean getFlag;

    /**
     * 还可以领取几张
     */
    private Integer canGetNum;

    /**
     * 有效期规则
     */
    private String giveOutEffectiveRules;

    /**
     * 支付方式
     */
    private String payMethodSelected;

    /**
     * 企业ID
     */
    private Long realEid;

    /**
     * 用券时间（1-固定时间；2-发放领取后生效）
     */
    private Integer useDateType;

    /**
     * 1全部会员方案可用，2部分可用
     */
    private Integer memberLimit;

    /**
     * 会员优惠券关联的会员规格id
     */
    private List<Long>memberIds;

    /**
     * 会员优惠券关联的会员规格id
     */
    private List<MemberStageDTO>memberStageList;

    /**
     * 可用商品（1-全部商品可用；2-部分商品可用）
     */
    private Integer goodsLimit;

    /**
     * 发放/领取xx天后失效
     */
    private Integer expiryDays;
}
