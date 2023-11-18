package com.yiling.f2b.admin.agreementv2.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 创建协议 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-01
 */
@Data
@ApiModel("创建协议表单")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CreateAgreementForm extends BaseForm {

    /**
     * 协议主条款
     */
    @ApiModelProperty("协议主条款")
    private AddAgreementMainTermsForm agreementMainTerms;

    /**
     * 协议供销条款
     */
    @ApiModelProperty("协议供销条款")
    private AddAgreementSupplySalesTermsForm agreementSupplySalesTerms;

    /**
     * 协议结算条款
     */
    @ApiModelProperty("协议结算条款")
    private AddAgreementSettlementTermsForm agreementSettlementTerms;

    /**
     * 协议返利条款
     */
    @ApiModelProperty("协议返利条款")
    private AddAgreementRebateTermsForm agreementRebateTerms;

}
