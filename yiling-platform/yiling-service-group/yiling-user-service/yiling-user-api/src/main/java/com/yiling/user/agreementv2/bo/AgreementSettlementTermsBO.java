package com.yiling.user.agreementv2.bo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议结算条款 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-10
 */
@Data
@Accessors(chain = true)
public class AgreementSettlementTermsBO implements Serializable {

    /**
     * 协议付款方式集合
     */
    private List<AgreementPaymentMethodBO> agreementPaymentMethodList;

    /**
     * 协议结算方式
     */
    private AgreementSettlementMethodBO agreementSettlementMethod;

}
