package com.yiling.user.enterprise.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.user.enterprise.dto.EnterpriseCertificateAuthInfoDTO;
import com.yiling.user.enterprise.dto.request.EnterpriseCertificateAuthInfoRequest;
import com.yiling.user.enterprise.entity.EnterpriseCertificateAuthInfoDO;

/**
 * <p>
 * 企业资质副本 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2021/11/2
 */
public interface EnterpriseCertificateAuthInfoService extends BaseService<EnterpriseCertificateAuthInfoDO> {

    /**
     * 添加企业资质审核副本
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
