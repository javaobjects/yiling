package com.yiling.admin.b2b.integral.form;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 保存订单送积分配置 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrderGiveIntegralForm extends BaseForm {

    /**
     * 发放规则ID
     */
    @NotNull
    @ApiModelProperty(value = "发放规则ID", required = true)
    private Long giveRuleId;

    /**
     * 商家范围：1-全部商家 2-指定商家
     */
    @NotNull
    @ApiModelProperty(value = "商家范围：1-全部商家 2-指定商家", required = true)
    private Integer merchantScope;

    /**
     * 商品范围：1-全部商品 2-指定平台SKU 3-指定店铺SKU
     */
    @NotNull
    @ApiModelProperty(value = "商品范围：1-全部商品 2-指定平台SKU 3-指定店铺SKU", required = true)
    private Integer goodsScope;

    /**
     * 客户范围：1-全部客户 2-指定客户 3-指定客户范围
     */
    @NotNull
    @ApiModelProperty(value = "客户范围：1-全部客户 2-指定客户 3-指定客户范围", required = true)
    private Integer customerScope;

    /**
     * 指定客户范围的企业类型：1-全部类型 2-指定类型
     */
    @ApiModelProperty(value = "指定客户范围的企业类型：1-全部类型 2-指定类型")
    private Integer enterpriseType;

    /**
     * 指定范围企业类型集合
     */
    @ApiModelProperty("指定范围企业类型集合（企业类型：3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所，参考企业类型的字典即可）")
    private List<Integer> enterpriseTypeList;

    /**
     * 指定客户范围的用户类型：1-全部用户 2-普通用户 3-全部会员 4-部分会员
     */
    @ApiModelProperty(value = "指定客户范围的用户类型：1-全部用户 2-普通用户 3-全部会员 4-部分会员")
    private Integer userType;

    /**
     * 是否区分支付方式：1-全部支付方式 2-指定支付方式
     */
    @NotNull
    @ApiModelProperty(value = "是否区分支付方式：1-全部支付方式 2-指定支付方式", required = true)
    private Integer paymentMethodFlag;

    /**
     * 支付方式：1-线上支付 2-线下支付 3-账期支付
     */
    @ApiModelProperty(value = "支付方式：1-线上支付 2-线下支付 3-账期支付")
    private List<Integer> paymentMethodList;

}
