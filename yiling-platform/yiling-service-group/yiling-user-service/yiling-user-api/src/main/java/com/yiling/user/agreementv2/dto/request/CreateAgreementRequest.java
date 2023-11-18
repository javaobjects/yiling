package com.yiling.user.agreementv2.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 创建协议 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CreateAgreementRequest extends BaseRequest {

    /**
     * 协议主条款
     */
    private AddAgreementMainTermsRequest agreementMainTerms;

    /**
     * 协议供销条款
     */
    private AddAgreementSupplySalesTermsRequest agreementSupplySalesTerms;

    /**
     * 协议结算条款
     */
    private AddAgreementSettlementTermsRequest agreementSettlementTerms;

    /**
     * 协议返利条款
     */
    private AddAgreementRebateTermsRequest agreementRebateTerms;

}
