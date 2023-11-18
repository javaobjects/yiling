package com.yiling.admin.b2b.coupon.from;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveCouponActivityAutoGiveRulesFrom extends BaseForm {

    /**
     * id
     */
    @ApiModelProperty("id")
    @NotNull
    private Long id;

    /**
     * 自动发券类型（1-订单累计金额；2-会员自动发券）
     */
    @ApiModelProperty("自动发券类型（1-订单累计金额；2-会员自动发券）")
    @NotNull
    private Integer type;

    /**
     * 指定商品(1-全部；2-指定)
     */
    @ApiModelProperty("指定商品(1-全部；2-指定)")
    private Integer conditionGoods;

    /**
     * 指定支付方式(1-全部；2-指定)
     */
    @ApiModelProperty("指定支付方式(1-全部；2-指定)")
    private Integer conditionPaymethod;

    /**
     * 已选支付方式，多个值用逗号隔开（1-在线支付；2-货到付款；3-账期）
     */
    @ApiModelProperty("已选支付方式，多个值用逗号隔开（1-在线支付；2-货到付款；3-账期）")
    private List<String> conditionPaymethodValueList;

    /**
     * 指定订单状态(1-全部；2-指定)
     */
    @ApiModelProperty("指定订单状态(1-全部；2-指定)")
    private Integer conditionOrderStatus;

    /**
     * 已选订单状态，多个值用逗号隔开（1-已发货）
     */
    @ApiModelProperty("已选订单状态，多个值用逗号隔开（1-已发货）")
    private List<String> conditionOrderStatusValueList;

    /**
     * 指定下单平台(1-全部；2-指定)
     */
    @ApiModelProperty("指定下单平台(1-全部；2-指定)")
    private Integer conditionOrderPlatform;

    /**
     * 已选下单平台，多个值用逗号隔开（1-B2B；2-销售助手）
     */
    @ApiModelProperty("已选下单平台，多个值用逗号隔开（1-B2B；2-销售助手）")
    private List<String> conditionOrderPlatformValueList;

    /**
     * 指定企业类型(1-全部；2-指定)
     */
    @ApiModelProperty("指定企业类型(1-全部；2-指定)")
    @NotNull
    private Integer conditionEnterpriseType;

    /**
     * 已选企业类型，多个值用逗号隔开，字典enterprise_type（1-工业；2-商业；3-连锁总店；4-连锁直营；5-连锁加盟；6-单体药房；8-诊所；9-民营医院；10-三级医院；11-二级医院；12-社区中心;13-县镇卫生院;14-社区站/村卫生所;15-县人民/中医院）
     */
    @ApiModelProperty("已选企业类型，多个值用逗号隔开，字典enterprise_type")
    private List<String> conditionEnterpriseTypeValueList;

    /**
     * 是否有推广码 1-是，2-否
     */
    @ApiModelProperty("是否有推广码 1-是，2-否，3-不使用")
    private Integer conditionPromotionCode;

    /**
     * 指定用户类型（1-全部用户；2-仅普通用户；3-全部会员用户；4-部分指定会员）
     */
    @ApiModelProperty("指定用户类型（1-全部用户；2-仅普通用户；3-全部会员用户；4-部分指定会员）")
    @NotNull
    private Integer conditionUserType;

    /**
     * 累计金额/数量（如果是订单累计金额，则表示累计金额。如果是订单累计数量，则表示累计订单数量。）
     */
    @ApiModelProperty("累计金额/数量（如果是订单累计金额，则表示累计金额。如果是订单累计数量，则表示累计订单数量。）")
    private Integer cumulative;

    /**
     * 是否重复发放(1-仅发一次；2-重复发放多次)
     */
    @ApiModelProperty("是否重复发放(1-仅发一次；2-重复发放多次/月)")
    @NotNull
    private Integer repeatGive;

    /**
     * 最多发放次数/月数
     */
    @ApiModelProperty("最多发放次数/月数")
    private Integer maxGiveNum;

    /**
     * 备注
     */
    private String remark;
}
