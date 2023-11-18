package com.yiling.user.agreementv2.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.user.agreementv2.dto.AgreementRebatePayEnterpriseDTO;
import com.yiling.user.agreementv2.entity.AgreementRebatePayEnterpriseDO;

/**
 * <p>
 * 协议返利支付方指定商业公司表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-28
 */
public interface AgreementRebatePayEnterpriseService extends BaseService<AgreementRebatePayEnterpriseDO> {

    /**
     * 获取协议返利保证金支付公司
     *
     * @param agreementId
     * @return
     */
    List<AgreementRebatePayEnterpriseDTO> getRebatePayEnterpriseList(Long agreementId);

}
