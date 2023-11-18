package com.yiling.admin.b2b.presale.form;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/8/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SavePresaleActivityForm extends BaseForm {

    @ApiModelProperty("活动id-修改时需要")
    private Long id;

    /**
     * 选择平台（1-B2B；2-销售助手）逗号隔开
     */
    @ApiModelProperty(value = "选择平台（1-B2B；2-销售助手）逗号隔开")
    private List<String> platformSelected;

    /**
     * 选择端口（1-B端；2-c端）逗号隔开
     */
    private String portSelected;

    /**
     * 商户范围类型（1-全部客户；2-指定客户；3-指定范围客户）
     */
    @ApiModelProperty(value = "商户范围类型（1-全部客户；2-指定客户；3-指定范围客户）")
    private Integer conditionBuyerType;

    /**
     * 指定企业类型(1:全部类型 2:指定类型)
     */
    @ApiModelProperty(value = "指定企业类型(1:全部类型 2:指定类型)")
    private Integer conditionEnterpriseType;

    /**
     * 指定企业类型值，多个值用逗号隔开，字典enterprise_type（3-连锁总店；4-连锁直营；5-连锁加盟；6-单体药店；7-医疗机构；8-诊所）
     */
    @ApiModelProperty(value = "（3-连锁总店；4-连锁直营；5-连锁加盟；6-单体药店；7-医疗机构；8-诊所")
    private List<String> conditionEnterpriseTypeValue;

    /**
     * 指定用户类型（1-全部用户；2-仅普通用户；3-全部会员用户；4-指定方案会员；5-指定推广方会员）
     */
    @ApiModelProperty(value = "指定用户类型（1-全部用户；2-仅普通用户；3-全部会员用户；4-指定方案会员；5-指定推广方会员）\n" + "     */")
    private Integer conditionUserType;

    /**
     * 其他(1-新客适用,多个值用逗号隔开)
     */
    @ApiModelProperty(value = "其他(1-新客适用,多个值用逗号隔开)")
    private List<String> conditionOther;

    /**
     * 预售类型（1-定金预售；2-全款预售）
     */
    @ApiModelProperty(value = "预售类型（1-定金预售；2-全款预售）")
    private Integer presaleType;

    /**
     * 支付定金开始时间
     */
    @ApiModelProperty(value = "支付定金开始时间")
    private Date depositBeginTime;

    /**
     * 支付定金结束时间
     */
    @ApiModelProperty(value = "支付定金结束时间")
    private Date depositEndTime;

    /**
     * 支付尾款开始时间
     */
    @ApiModelProperty(value = "支付尾款开始时间")
    private Date finalPayBeginTime;

    /**
     * 支付尾款结束时间
     */
    @ApiModelProperty(value = "支付尾款结束时间")
    private Date finalPayEndTime;

    /**
     * 预算金额
     */
    @ApiModelProperty(value = "商品信息")
    private List<PresaleGoodsLimitForm> presaleGoodsLimitForms;

    /**
     * 预算金额
     */
    @ApiModelProperty(value = "预算金额")
    private BigDecimal budgetAmount;
}
