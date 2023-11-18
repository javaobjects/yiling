package com.yiling.user.agreementv2.service;

import java.util.List;

import com.yiling.user.agreementv2.entity.AgreementRebateControlAreaDetailDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 协议返利范围控销区域详情表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-03
 */
public interface AgreementRebateControlAreaDetailService extends BaseService<AgreementRebateControlAreaDetailDO> {

    /**
     * 查询返利范围控销区域详情
     * @param rebateScopeId
     * @return
     */
    List<AgreementRebateControlAreaDetailDO> getControlAreaDetailList(Long rebateScopeId);
}
