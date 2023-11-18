package com.yiling.f2b.admin.agreementv2.form;

import java.util.List;

import com.yiling.framework.common.base.form.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加协议结算条款 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddAgreementSettlementTermsForm extends BaseForm {

    /**
     * 协议付款方式集合
     */
    private List<AddAgreementPaymentMethodForm> agreementPaymentMethodList;

    /**
     * 协议结算方式
     */
    private AddAgreementSettlementMethodForm agreementSettlementMethod;

}
