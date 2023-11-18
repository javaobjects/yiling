package com.yiling.user.agreementv2.service;

import java.util.List;
import java.util.Map;

import com.yiling.user.agreementv2.dto.AgreementRebateControlCustomerTypeDTO;
import com.yiling.user.agreementv2.entity.AgreementRebateControlCustomerTypeDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 协议返利范围控销客户类型表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-03
 */
public interface AgreementRebateControlCustomerTypeService extends BaseService<AgreementRebateControlCustomerTypeDO> {

    /**
     * 根据返利范围Id批量获取返利客户类型
     *
     * @param rebateScopeIdList
     * @return
     */
    Map<Long, List<AgreementRebateControlCustomerTypeDTO>> getControlCustomerTypeMap(List<Long> rebateScopeIdList);

}
