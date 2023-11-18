package com.yiling.f2b.admin.agreementv2.form;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加协议付款方式 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-28
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddAgreementPaymentMethodForm extends BaseForm {

    /**
     * 付款方式：1-支票 2-电汇 3-易贷 4-3个月承兑 5-6个月承兑
     */
    @Range(min = 1, max = 5)
    @ApiModelProperty("付款方式：1-支票 2-电汇 3-易贷 4-3个月承兑 5-6个月承兑（见字典：agreement_pay_method）")
    private Integer payMethod;

    /**
     * 占比
     */
    @Range(min = 1, max = 100)
    @ApiModelProperty("占比（整数1-100）")
    private Integer ratio;

}
