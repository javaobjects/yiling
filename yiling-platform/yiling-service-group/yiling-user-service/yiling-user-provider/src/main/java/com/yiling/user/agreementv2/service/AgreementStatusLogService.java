package com.yiling.user.agreementv2.service;

import java.util.List;

import com.yiling.user.agreementv2.dto.AgreementStatusLogDTO;
import com.yiling.user.agreementv2.entity.AgreementStatusLogDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 协议状态日志表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-11
 */
public interface AgreementStatusLogService extends BaseService<AgreementStatusLogDO> {

    /**
     * 根据协议Id获取审核记录
     *
     * @param agreementId
     * @return
     */
    List<AgreementStatusLogDTO> getByAgreementId(Long agreementId);
}
