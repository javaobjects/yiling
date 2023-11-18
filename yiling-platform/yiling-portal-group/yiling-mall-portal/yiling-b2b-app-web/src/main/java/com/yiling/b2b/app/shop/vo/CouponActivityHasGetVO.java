package com.yiling.b2b.app.shop.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 店铺-优惠券列表VO
 * @author: lun.yu
 * @date: 2021/11/12
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
     * 支付方式
     */
    private String payMethodSelected;

    /**
     * 用券时间（1-固定时间；2-发放领取后生效）
     */
    @ApiModelProperty("1-固定时间；2-发放领取后生效")
    private Integer useDateType;

    /**
     * 会员优惠券关联的会员规格id
     */
    @ApiModelProperty("会员优惠券关联的会员规格id")
    private List<Long> memberIds;

    /**
     * 1全部会员方案可用，2部分可用
     */
    @ApiModelProperty("1全部会员方案可用，2部分可用")
    private Integer memberLimit;
}
