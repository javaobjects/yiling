package com.yiling.user.agreementv2.service;

import com.yiling.user.agreementv2.bo.AgreementSettlementTermsBO;
import com.yiling.user.agreementv2.dto.request.AddAgreementSettlementTermsRequest;

/**
 * <p>
 * 协议结算条款 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-28
 */
public interface AgreementSettlementTermsService {

    /**
     * 基础数据校验
     *
     * @param request
     */
    void checkSettlementTerms(AddAgreementSettlementTermsRequest request);

    /**
     * 添加协议结算条款
     *
     * @param request 请求参数
     * @return
     */
    Boolean addAgreementSettlementTerms(AddAgreementSettlementTermsRequest request);

    /**
     * 获取协议结算条款页
     *
     * @param agreementId
     * @return
     */
    AgreementSettlementTermsBO getAgreementSettlementTerms(Long agreementId);

}
