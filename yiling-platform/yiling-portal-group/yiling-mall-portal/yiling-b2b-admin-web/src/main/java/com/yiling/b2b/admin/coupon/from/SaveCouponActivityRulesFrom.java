package com.yiling.b2b.admin.coupon.from;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Max;
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
public class SaveCouponActivityRulesFrom extends BaseForm {

    /**
     * 优惠券活动id
     */
    @ApiModelProperty(value = "id")
    private Long id;

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
    private List<String> coexistPromotionList;

    /**
     * 可用商品（1-全部商品；2-部分商品）
     */
    @NotNull
    private Integer goodsLimit;

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
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 用户领取设置，商家券（1-用户可领 2-用户不可领）
     */
    @ApiModelProperty("用户领取设置，商家券（1-用户可领 2-用户不可领）")
    private Integer canGet;

    /**
     * 活动开始时间
     */
    @ApiModelProperty("活动开始时间")
    private Date activityBeginTime;

    /**
     * 活动结束时间
     */
    @ApiModelProperty("活动结束时间")
    private Date activityEndTime;

    /**
     * 可领取数量
     */
    @ApiModelProperty("可领取数量")
    @Max(value = 99)
    private Integer giveNum;

    /**
     * 指定企业类型(1-全部；2-指定)
     */
    @ApiModelProperty("指定企业类型(1-全部；2-指定)")
    private Integer conditionEnterpriseType;

    /**
     * 已选企业类型，多个值用逗号隔开，字典enterprise_type（1-工业；2-商业；3-连锁总店；4-连锁直营；5-连锁加盟；6-单体药房；8-诊所；9-民营医院；10-三级医院；11-二级医院；12-社区中心;13-县镇卫生院;14-社区站/村卫生所;15-县人民/中医院）
     */
    @ApiModelProperty("已选企业类型，多个值用逗号隔开，字典enterprise_type")
    private List<String> conditionEnterpriseTypeValueList;

    /**
     * 指定用户类型（1-全部用户；2-仅普通用户；3-全部会员用户；4-部分指定会员；5-部分用户）
     */
    @ApiModelProperty("指定用户类型（1-全部用户；2-仅普通用户；3-全部会员用户；4-部分指定会员；5-部分用户）")
    private Integer conditionUserType;

}
