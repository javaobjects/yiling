package com.yiling.user.agreementv2.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加协议结算条款 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddAgreementSettlementTermsRequest extends BaseRequest {

    /**
     * 协议ID
     */
    private Long agreementId;

    /**
     * 协议付款方式集合
     */
    private List<AddAgreementPaymentMethodRequest> agreementPaymentMethodList;

    /**
     * 协议结算方式
     */
    private AddAgreementSettlementMethodRequest agreementSettlementMethod;

}
