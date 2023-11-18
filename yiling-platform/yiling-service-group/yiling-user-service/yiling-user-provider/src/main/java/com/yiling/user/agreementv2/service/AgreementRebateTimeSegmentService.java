package com.yiling.user.agreementv2.service;

import java.util.List;

import com.yiling.user.agreementv2.dto.AgreementRebateTimeSegmentDTO;
import com.yiling.user.agreementv2.entity.AgreementRebateTimeSegmentDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 协议时段表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-24
 */
public interface AgreementRebateTimeSegmentService extends BaseService<AgreementRebateTimeSegmentDO> {

    /**
     * 根据协议Id获取返利时段
     *
     * @param agreementId
     * @return
     */
    List<AgreementRebateTimeSegmentDTO> getRebateTimeByAgreementId(Long agreementId);

}
