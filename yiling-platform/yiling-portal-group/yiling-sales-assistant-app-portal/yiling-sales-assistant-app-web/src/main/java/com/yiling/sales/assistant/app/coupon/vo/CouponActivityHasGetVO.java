package com.yiling.sales.assistant.app.coupon.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 店铺-优惠券列表VO
 *
 * @author houjie.sun
 * @date 2022/04/06
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CouponActivityHasGetVO extends BaseVO {

    /**
     * 企业ID
     */
    @ApiModelProperty("企业ID")
    private Long eid;

    /**
     * 企业名称
     */
    @ApiModelProperty("企业名称")
    private String ename;

    /**
     * 优惠券活动类型（1-满减券；2-满折券）
     */
    @ApiModelProperty("优惠券活动类型（1-满减券；2-满折券）")
    private Integer type;

    /**
     * 优惠券活动名称
     */
    @ApiModelProperty("优惠券活动名称")
    private String name;

    /**
     * 活动分类（1-平台活动；2-商家活动）
     */
    @ApiModelProperty("活动分类（1-平台活动；2-商家活动）")
    private Integer sponsorType;

    /**
     * 优惠规则-满xx元可用
     */
    @ApiModelProperty("优惠规则-满xx元可用")
    private String thresholdValueRules;

    /**
     * 优惠规则-xx元/xx折
     */
    @ApiModelProperty("优惠规则-xx元/xx折")
    private String discountValueRules;

    /**
     * 优惠规则-最高减xx元
     */
    @ApiModelProperty("优惠规则-最高减xx元")
    private String discountMaxRules;

    /**
     * 优惠内容（减xx元/打xx折）
     */
    @ApiModelProperty("优惠内容（减xx元/打xx折）")
    private BigDecimal discountValue;

    /**
     * 指定商品(1:全部 2:部分)
     */
    @ApiModelProperty("指定商品(1:全部 2:部分)")
    private Integer conditionGoods;

    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    private Date beginTime;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    private Date endTime;

    /**
     * 是否已领取（true-是；false-否）
     */
    @ApiModelProperty("是否已领取（true-是；false-否）")
    private Boolean getFlag;

    /**
     * 有效期规则
     */
    @ApiModelProperty("有效期规则")
    private String giveOutEffectiveRules;

    /**
     * 可用商品（1-全部商品可用；2-部分商品可用）
     */
    @ApiModelProperty("可用商品（1-全部商品可用；2-部分商品可用）")
    private Integer goodsLimit;
}
