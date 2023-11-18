package com.yiling.user.agreementv2.service;

import java.util.List;

import com.yiling.user.agreementv2.dto.AgreementRebateBasicServiceRewardDTO;
import com.yiling.user.agreementv2.dto.AgreementRebateProjectServiceRewardDTO;
import com.yiling.user.agreementv2.entity.AgreementRebateProjectServiceRewardDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 协议返利-项目服务奖励阶梯表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-05-17
 */
public interface AgreementRebateProjectServiceRewardService extends BaseService<AgreementRebateProjectServiceRewardDO> {

    /**
     * 根据协议Id获取项目服务奖励集合
     *
     * @param agreementId
     * @return
     */
    List<AgreementRebateProjectServiceRewardDTO> getProjectServiceRewardListByAgreementId(Long agreementId);

}
