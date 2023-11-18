package com.yiling.user.agreementv2.service;

import com.yiling.user.agreementv2.bo.AgreementRebateTermsBO;
import com.yiling.user.agreementv2.dto.request.AddAgreementRebateTermsRequest;
import com.yiling.user.agreementv2.dto.request.CreateAgreementRequest;
import com.yiling.user.agreementv2.entity.AgreementRebateTermsDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 协议返利条款表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-24
 */
public interface AgreementRebateTermsService extends BaseService<AgreementRebateTermsDO> {

    /**
     * 基础校验数据
     *
     * @param request
     */
    void checkRebateTerms(CreateAgreementRequest request);

    /**
     * 添加协议返利条款
     *
     * @param request
     * @return
     */
    Boolean addAgreementRebateTerms(AddAgreementRebateTermsRequest request);

    /**
     * 根据协议Id获取返利条款
     *
     * @param agreementId
     * @return
     */
    AgreementRebateTermsDO getRebateTermsByAgreementId(Long agreementId);

    /**
     * 获取协议返利条款页
     *
     * @param agreementId
     * @return
     */
    AgreementRebateTermsBO getAgreementRebateTerms(Long agreementId);

}
