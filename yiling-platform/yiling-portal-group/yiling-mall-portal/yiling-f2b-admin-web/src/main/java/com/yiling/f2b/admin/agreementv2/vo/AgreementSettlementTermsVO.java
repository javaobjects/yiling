package com.yiling.f2b.admin.agreementv2.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议结算条款导入 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-18
 */
@Data
@Accessors(chain = true)
public class AgreementSettlementTermsVO implements Serializable {

    /**
     * 协议付款方式集合
     */
    private List<AgreementPaymentMethodVO> agreementPaymentMethodList;

    /**
     * 协议结算方式
     */
    private AgreementSettlementMethodVO agreementSettlementMethod;

}
