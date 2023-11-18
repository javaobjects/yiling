package com.yiling.user.agreementv2.service;

import com.yiling.user.agreementv2.bo.AgreementSupplySalesTermsBO;
import com.yiling.user.agreementv2.dto.AgreementSupplySalesTermsDTO;
import com.yiling.user.agreementv2.dto.request.AddAgreementSupplySalesTermsRequest;
import com.yiling.user.agreementv2.entity.AgreementSupplySalesTermsDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 协议供销条款表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-24
 */
public interface AgreementSupplySalesTermsService extends BaseService<AgreementSupplySalesTermsDO> {

    /**
     * 校验协议供销条款数据
     *
     * @param request
     */
    void validAgreementSupplySalesTerms(AddAgreementSupplySalesTermsRequest request);

    /**
     * 添加协议供销条款
     *
     * @param request
     * @return
     */
    Boolean addAgreementSupplySalesTerms(AddAgreementSupplySalesTermsRequest request);

    /**
     * 根据协议ID获取协议供销条款
     *
     * @param agreementId
     * @return
     */
    AgreementSupplySalesTermsDTO getSalesTermsByAgreementId(Long agreementId);

    /**
     * 获取供销条款页
     *
     * @param agreementId
     * @return
     */
    AgreementSupplySalesTermsBO getSupplySalesTerms(Long agreementId);




}
