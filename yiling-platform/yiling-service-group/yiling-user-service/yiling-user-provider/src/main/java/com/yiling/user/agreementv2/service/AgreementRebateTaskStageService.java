package com.yiling.user.agreementv2.service;

import java.util.List;

import com.yiling.user.agreementv2.dto.AgreementRebateTaskStageDTO;
import com.yiling.user.agreementv2.entity.AgreementRebateTaskStageDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 协议返利任务量阶梯表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-24
 */
public interface AgreementRebateTaskStageService extends BaseService<AgreementRebateTaskStageDO> {

    /**
     * 根绝协议ID获取所有任务量
     *
     * @param agreementId
     * @return
     */
    List<AgreementRebateTaskStageDTO> getRebateTaskStageList(Long agreementId);

}
