package com.yiling.user.agreementv2.service;

import java.util.List;
import java.util.Map;

import com.yiling.user.agreementv2.dto.AgreementRebateStageDTO;
import com.yiling.user.agreementv2.entity.AgreementRebateStageDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 协议返利阶梯表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-24
 */
public interface AgreementRebateStageService extends BaseService<AgreementRebateStageDO> {

    /**
     * 根据任务量ID获取任务
     *
     * @param taskStageId
     * @return
     */
    List<AgreementRebateStageDTO> getRebateStageList(Long taskStageId);

    /**
     * 根据任务量ID批量获取任务
     *
     * @param taskStageIdList
     * @return
     */
    Map<Long, List<AgreementRebateStageDTO>> getStageMapByStageIdList(List<Long> taskStageIdList);

}
