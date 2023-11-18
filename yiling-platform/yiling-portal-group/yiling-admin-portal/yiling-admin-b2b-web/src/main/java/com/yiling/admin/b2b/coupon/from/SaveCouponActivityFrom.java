package com.yiling.admin.b2b.coupon.from;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
public class SaveCouponActivityFrom extends BaseForm {

    /**
     * 优惠券活动id
     */
    @ApiModelProperty(value = "id")
    private Long id;

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
     * 活动分类（1-平台活动；2-商家活动）
     */
    @ApiModelProperty(value = "活动分类（1-平台活动；2-商家活动）")
    @NotNull
    private Integer sponsorType;

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
     * 费用承担方（1-平台；2-商家；3-共同承担）
     */
    @ApiModelProperty(value = "费用承担方（1-平台；2-商家；3-共同承担）")
    @NotNull
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
     * 用券时间（1-固定时间；2-发放领取后生效）
     */
    @ApiModelProperty(value = "用券时间（1-固定时间；2-发放领取后生效）")
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
     * 平台限制（1-全部平台；2-部分平台）
     */
    @ApiModelProperty(value = "平台限制（1-全部平台；2-部分平台）")
    @NotNull
    private Integer platformLimit;

    /**
     * 选择平台（1-B2B；2-销售助手）
     */
    @ApiModelProperty(value = "选择平台（1-B2B；2-销售助手）")
    private List<String> platformSelectedList;

    /**
     * 支付方式限制（1-全部支付方式；2-部分支付方式）
     */
    @ApiModelProperty(value = "支付方式限制（1-全部支付方式；2-部分支付方式）")
    @NotNull
    private Integer payMethodLimit;

    /**
     * 选择支付方式（1-在线支付；2-货到付款；3-账期。2,3现在不支持）
     */
    @ApiModelProperty(value = "选择支付方式（1-在线支付；2-货到付款；3-账期。2,3现在不支持）")
    private List<String> payMethodSelectedList;

    /**
     * 可叠加促销列表（1-满赠）
     */
    @ApiModelProperty(value = "可叠加促销列表（1-满赠）")
    @NotEmpty
    private List<String> coexistPromotionList;

    /**
     * 供应商限制（1-全部供应商；2-部分供应商）
     */
    @ApiModelProperty(value = "供应商限制（1-全部供应商；2-部分供应商）")
    @NotNull
    private Integer enterpriseLimit;

    /**
     * 生成优惠券总数量
     */
    @ApiModelProperty(value = "生成优惠券总数量")
    private Integer totalCount;

    /**
     * 状态：1-启用 2-停用 3-废弃
     */
    @ApiModelProperty(value = "状态：1-启用 2-停用 3-废弃")
    private Integer status;

    /**
     * 是否删除：0-否 1-是
     */
    @ApiModelProperty(value = "是否删除：0-否 1-是")
    private Integer delFlag;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private Long createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private Long updateUser;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
}
