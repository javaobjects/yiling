package com.yiling.admin.b2b.paypromotion.from;

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
public class AddAllPayPromotionActivityForm extends BaseForm {

    @ApiModelProperty("活动id-修改时需要")
    private Long id;

    @ApiModelProperty("活动名称")
    private String name;

    /**
     * 预算金额
     */
    @ApiModelProperty("预算金额")
    private BigDecimal budgetAmount;

    @ApiModelProperty("活动分类（1-平台活动；2-商家活动；）")
    private Integer sponsorType;

    @ApiModelProperty("生效开始时间")
    private Date beginTime;

    @ApiModelProperty("生效开始时间")
    private Integer bear;

    @ApiModelProperty("生效结束时间")
    private Date endTime;

    @ApiModelProperty("运营备注")
    private String operatingRemark;

    @ApiModelProperty("商家范围类型（1-全部商家；2-指定商家；）")
    private Integer conditionSellerType;

    @ApiModelProperty("商品范围类型（1-全部商品；2-指定平台SKU；3-指定店铺SKU；）")
    private Integer conditionGoodsType;

    @ApiModelProperty("商户范围类型（1-全部客户；2-指定客户；3-指定范围客户）")
    private Integer conditionBuyerType;

    @ApiModelProperty("指定企业类型(1:全部类型 2:指定类型)")
    private Integer conditionEnterpriseType;

    /**
     * 指定部分会员用户类型（1-指定方案会员；2-指定推广方会员）逗号隔开
     */
    @ApiModelProperty("指定部分会员用户类型（1-指定方案会员；2-指定推广方会员）逗号隔开")
    private List<Integer> conditionUserMemberType;

    @ApiModelProperty("指定企业类型值，多个值用逗号隔开，字典enterprise_type（3-连锁总店；4-连锁直营；5-连锁加盟；6-单体药店；7-医疗机构；8-诊所）")
    private List<Integer> conditionEnterpriseTypeValue;

    @ApiModelProperty("指定用户类型（1-全部用户；2-仅普通用户；3-全部会员用户；4-部分会员）")
    private Integer conditionUserType;

    @ApiModelProperty("其他(1-新客适用,多个值用逗号隔开)")
    private List<Integer> conditionOther;

    @ApiModelProperty("订单累计金额-限制支付方式值(支付方式：1-在线支付 2-线下支付 3-账期 ),逗号隔开")
    private List<Integer> payType;

    /**
     * 生效条件(1-按金额)
     */
    @ApiModelProperty("生效条件(1-按金额)")
    private Integer conditionEffective;

    /**
     * 每个客户参数次数
     */
    @ApiModelProperty("每个客户参数次数")
    private Integer maxGiveNum;

    /**
     * 生效条件
     */
    @ApiModelProperty("生效条件")
    private List<PayPromotionCaculateRuleFrom> calculateRules;
}
