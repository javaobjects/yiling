package com.yiling.user.agreementv2.service;

import java.util.List;

import com.yiling.user.agreementv2.dto.AgreementRebateScaleRebateDTO;
import com.yiling.user.agreementv2.entity.AgreementRebateScaleRebateDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 协议返利-规模返利阶梯表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-05-16
 */
public interface AgreementRebateScaleRebateService extends BaseService<AgreementRebateScaleRebateDO> {

    /**
     * 根据协议Id获取规模返利集合
     *
     * @param agreementId
     * @return
     */
    List<AgreementRebateScaleRebateDTO> getScaleRebateListByAgreementId(Long agreementId);

}
