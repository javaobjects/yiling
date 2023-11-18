package com.yiling.admin.b2b.coupon.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/22
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CouponActivityGoodsGiftPageVo extends BaseVO {

    /**
     * 优惠券活动名称
     */
    @ApiModelProperty("优惠券活动名称")
    private String name;

    /**
     * 预算编号
     */
    @ApiModelProperty("预算编号")
    private String budgetCode;

    /**
     * 活动分类（1-平台活动；2-商家活动）
     */
    @ApiModelProperty("活动分类（1-平台活动；2-商家活动）")
    private Integer sponsorType;

    /**
     * 优惠券类型（1-满减券；2-满折券）
     */
    @ApiModelProperty("优惠券类型（1-满减券；2-满折券）")
    private Integer type;

    /**
     * 门槛金额/件数
     */
    @ApiModelProperty("门槛金额/件数（满xx元/件）")
    private BigDecimal thresholdValue;

    /**
     * 优惠内容（金额）
     */
    @ApiModelProperty("优惠内容（减yy元/打yy折）")
    private BigDecimal discountValue;

    /**
     * 最高优惠金额（折扣券）
     */
    @ApiModelProperty("最高优惠金额（折扣券）")
    private BigDecimal discountMax;

    /**
     * 优惠规则
     */
    @ApiModelProperty("优惠规则")
    private String couponRules;

    /**
     * 优惠券总数量
     */
    @ApiModelProperty("优惠券总数量")
    private Integer totalCount;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private Long createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private Long updateUser;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;


}
