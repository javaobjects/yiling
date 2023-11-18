package com.yiling.user.agreementv2.service;

import java.util.List;
import java.util.Map;

import com.yiling.user.agreementv2.dto.AgreementRebateControlDTO;
import com.yiling.user.agreementv2.entity.AgreementRebateControlDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 协议返利范围控销条件表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-03
 */
public interface AgreementRebateControlService extends BaseService<AgreementRebateControlDO> {

    /**
     * 根据返利范围ID批量获取返利控销条件
     *
     * @param rebateScopeId
     * @return
     */
    Map<Long, List<AgreementRebateControlDTO>> getRebateControlMap(List<Long> rebateScopeId);

}
