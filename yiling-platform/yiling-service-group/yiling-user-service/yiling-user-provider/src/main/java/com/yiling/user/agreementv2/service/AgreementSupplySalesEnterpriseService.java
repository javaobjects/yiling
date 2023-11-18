package com.yiling.user.agreementv2.service;

import java.util.List;

import com.yiling.user.agreementv2.dto.AgreementSupplySalesEnterpriseDTO;
import com.yiling.user.agreementv2.entity.AgreementSupplySalesEnterpriseDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 协议供销指定商业公司表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-24
 */
public interface AgreementSupplySalesEnterpriseService extends BaseService<AgreementSupplySalesEnterpriseDO> {

    /**
     * 根据协议id获取供销商业公司
     *
     * @param agreementId
     * @return
     */
    List<AgreementSupplySalesEnterpriseDTO> getByAgreementId(Long agreementId);

}
