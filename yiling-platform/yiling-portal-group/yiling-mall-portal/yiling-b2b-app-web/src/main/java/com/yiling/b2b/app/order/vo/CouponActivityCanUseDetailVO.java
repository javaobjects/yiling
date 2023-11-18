package com.yiling.b2b.app.order.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
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
public class CouponActivityCanUseDetailVO extends BaseVO {

    /**
     * 企业ID
     */
    @ApiModelProperty(value = "企业ID")
    private Long eid;

    @ApiModelProperty(value = "是否选中")
    private Boolean isSelected;

    /**
     * 用券企业名称
     */
    @ApiModelProperty(value = "用券企业名称")
    private String ename;

    /**
     * 优惠券活动类型（1-满减券；2-满折券）
     */
    @ApiModelProperty(value = "优惠券活动类型（1-满减券；2-满折券）")
    private Integer type;

    /**
     * 优惠券活动名称
     */
    @ApiModelProperty(value = "优惠券活动名称")
    private String name;

    /**
     * 活动分类（1-平台活动；2-商家活动）
     */
    @ApiModelProperty(value = "活动分类（1-平台活动；2-商家活动）")
    private Integer sponsorType;

    /**
     * 门槛金额/件数（满xx元/件数）
     */
    @ApiModelProperty(value = "门槛金额/件数（满xx元/件数）")
    private BigDecimal thresholdValue;

    /**
     * 优惠规则-满xx元可用
     */
    @ApiModelProperty(value = "优惠规则-满xx元可用")
    private String thresholdValueRules;

    /**
     * 优惠内容（减xx元/打xx折）
     */
    @ApiModelProperty(value = "优惠内容（减xx元/打xx折）")
    private BigDecimal discountValue;

    /**
     * 优惠规则-xx元/xx折
     */
    @ApiModelProperty(value = "优惠规则-xx元/xx折")
    private String discountValueRules;

    /**
     * 最高优惠额度（折扣券）
     */
    @ApiModelProperty(value = "最高优惠额度（折扣券）")
    private BigDecimal discountMax;

    /**
     * 优惠规则-最高减xx元
     */
    @ApiModelProperty(value = "优惠规则-最高减xx元")
    private String discountMaxRules;

    /**
     * 指定商品(1:全部 2:部分)
     */
    @ApiModelProperty(value = "指定商品(1:全部 2:部分)")
    private Integer conditionGoods;

    /**
     * 用券开始时间
     */
    @ApiModelProperty(value = "用券开始时间")
    private Date beginTime;

    /**
     * 用券结束时间
     */
    @ApiModelProperty(value = "用券结束时间")
    private Date endTime;


    @ApiModelProperty(value = "到期提示")
    private String expirationDesc;

    /**
     * 此优惠券是否可叠加赠品（true-是 false-否）
     */
    private Boolean isSuportPromotionGoods;

    /**
     * 是否为最优优惠劵
     */
    @ApiModelProperty(value = "是否为最优优惠劵")
    private Boolean isBestCoupon;

}
