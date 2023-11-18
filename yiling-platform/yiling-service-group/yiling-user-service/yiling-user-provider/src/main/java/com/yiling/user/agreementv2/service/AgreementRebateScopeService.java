package com.yiling.user.agreementv2.service;

import java.util.List;

import com.yiling.user.agreementv2.dto.AgreementRebateScopeDTO;
import com.yiling.user.agreementv2.entity.AgreementRebateScopeDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 协议返利范围表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-03
 */
public interface AgreementRebateScopeService extends BaseService<AgreementRebateScopeDO> {

    /**
     * 根据商品组ID获取返利范围
     *
     * @param groupId
     * @return
     */
    List<AgreementRebateScopeDTO> getRebateScopeList(Long groupId);

    /**
     * 根据协议ID获取返利范围
     *
     * @param agreementId
     * @return
     */
    List<AgreementRebateScopeDTO> getRebateScopeByAgreementId(Long agreementId);

}
