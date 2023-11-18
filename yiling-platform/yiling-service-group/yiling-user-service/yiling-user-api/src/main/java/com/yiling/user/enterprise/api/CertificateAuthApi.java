package com.yiling.user.enterprise.api;

import java.util.List;

import com.yiling.user.enterprise.dto.EnterpriseCertificateAuthInfoDTO;
import com.yiling.user.enterprise.dto.request.EnterpriseCertificateAuthInfoRequest;

/**
 * 企业资质副本 API
 *
 * @author: lun.yu
 * @date: 2021/11/2
 */
public interface CertificateAuthApi {

    /**
     * 新增企业资质副本
     * @param request
     * @return
     */
    Boolean addEnterpriseCertificateAuth(List<EnterpriseCertificateAuthInfoRequest> request);

    /**
     * 根据企业副本ID获取资质信息
     * @param enterpriseAuthId
     * @return
     */
    List<EnterpriseCertificateAuthInfoDTO> getCertificateAuthInfoListByAuthId(Long enterpriseAuthId);

}
