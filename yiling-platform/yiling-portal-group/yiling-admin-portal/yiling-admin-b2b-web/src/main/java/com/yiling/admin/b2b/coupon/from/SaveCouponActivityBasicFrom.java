package com.yiling.admin.b2b.coupon.from;

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
    private Integer type;

    /**
     * 优惠券名称
     */
    @ApiModelProperty(value = "优惠券名称")
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
    private String budgetCode;

    /**
     * 活动分类（1-平台活动；2-商家活动）
     */
    @ApiModelProperty(value = "活动分类（1-平台活动；2-商家活动）")
    private Integer sponsorType;

    /**
     * 使用门槛（1-支付金额满额）
     */
    @ApiModelProperty(value = "活动类型（1-支付金额满额）")
    private Integer threshold;

    /**
     * 门槛金额/件数
     */
    @ApiModelProperty(value = "门槛金额/件数")
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
    private BigDecimal discountValue;

    /**
     * 费用承担方（1-平台；2-商家；3-共同承担）
     */
    @ApiModelProperty(value = "费用承担方（1-平台；2-商家；3-共同承担）")
    private Integer bear;

    /**
     * 平台承担比例
     */
    @ApiModelProperty(value = "平台承担比例")
    private BigDecimal platformRatio;

    /**
     * 商家承担比例
     */
    @ApiModelProperty(value = "商家承担比例")
    private BigDecimal businessRatio;

    /**
     * 有效期（1-固定时间；2-发放领取后生效）
     */
    @ApiModelProperty(value = "有效期（1-固定时间；2-发放领取后生效）")
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

    /**
     * 生成优惠券总数量
     */
    @ApiModelProperty(value = "生成优惠券总数量")
    private Integer totalCount;

    /**
     * 1全部会员方案可用，2部分可用
     */
    @ApiModelProperty(value = "1全部会员方案可用，2部分可用")
    private Integer memberLimit;
}
