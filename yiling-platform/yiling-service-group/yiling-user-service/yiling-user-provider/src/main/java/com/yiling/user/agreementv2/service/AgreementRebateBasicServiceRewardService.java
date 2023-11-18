package com.yiling.user.agreementv2.service;

import java.util.List;

import com.yiling.user.agreementv2.dto.AgreementRebateBasicServiceRewardDTO;
import com.yiling.user.agreementv2.dto.AgreementRebateScaleRebateDTO;
import com.yiling.user.agreementv2.entity.AgreementRebateBasicServiceRewardDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 协议返利-基础服务奖励阶梯表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-05-17
 */
public interface AgreementRebateBasicServiceRewardService extends BaseService<AgreementRebateBasicServiceRewardDO> {

    /**
     * 根据协议Id获取基础服务奖励集合
     *
     * @param agreementId
     * @return
     */
    List<AgreementRebateBasicServiceRewardDTO> getBasicServiceRewardListByAgreementId(Long agreementId);

}
