package com.yiling.user.enterprise.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.user.enterprise.api.CertificateAuthApi;
import com.yiling.user.enterprise.dto.EnterpriseCertificateAuthInfoDTO;
import com.yiling.user.enterprise.dto.request.EnterpriseCertificateAuthInfoRequest;
import com.yiling.user.enterprise.service.EnterpriseCertificateAuthInfoService;

/**
 * 企业副本 API 实现
 *
 * @author: lun.yu
 * @date: 2021/11/2
 */
@DubboService
public class CertificateAuthApiImpl implements CertificateAuthApi {

    @Autowired
    EnterpriseCertificateAuthInfoService certificateAuthInfoService;

    @Override
    public Boolean addEnterpriseCertificateAuth(List<EnterpriseCertificateAuthInfoRequest> request) {
        return certificateAuthInfoService.addEnterpriseCertificateAuth(request);
    }

    @Override
    public List<EnterpriseCertificateAuthInfoDTO> getCertificateAuthInfoListByAuthId(Long enterpriseAuthId) {
        return certificateAuthInfoService.getCertificateAuthInfoListByAuthId(enterpriseAuthId);
    }
}
