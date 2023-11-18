package com.yiling.b2b.admin.coupon.from;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/10/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveCouponActivityBasicFrom extends BaseForm {

    /**
     * 优惠券活动id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 复制的优惠券活动id
     */
    @ApiModelProperty(value = "复制的优惠券活动id")
    private Long oldId;

    /**
     * 类型（1-满减券；2-满折券）
     */
    @ApiModelProperty(value = "类型（1-满减券；2-满折券）")
    @NotNull
    private Integer type;

    /**
     * 优惠券名称
     */
    @ApiModelProperty(value = "优惠券名称")
    @NotEmpty
    private String name;

    /**
     * 优惠券描述
     */
    @ApiModelProperty(value = "优惠券描述")
    private String description;

    /**
     * 预算编号
     */
    @ApiModelProperty(value = "预算编号")
    @NotEmpty
    private String budgetCode;

    /**
     * 使用门槛（1-支付金额满额）
     */
    @ApiModelProperty(value = "活动类型（1-支付金额满额）")
    @NotNull
    private Integer threshold;

    /**
     * 门槛金额/件数
     */
    @ApiModelProperty(value = "门槛金额/件数")
    @NotNull
    private BigDecimal thresholdValue;

    /**
     * 最高优惠额度（折扣券）
     */
    @ApiModelProperty(value = "最高优惠额度（折扣券）")
    private BigDecimal discountMax;

    /**
     * 优惠内容（金额）
     */
    @ApiModelProperty(value = "优惠内容（金额）")
    @NotNull
    private BigDecimal discountValue;

    /**
     * 有效期（1-固定时间；2-发放领取后生效）
     */
    @ApiModelProperty(value = "有效期（1-固定时间；2-发放领取后生效）")
    @NotNull
    private Integer useDateType;

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

    /**
     * 发放/领取xx天后失效
     */
    @ApiModelProperty(value = "发放/领取xx天后失效")
    private Integer expiryDays;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
}
