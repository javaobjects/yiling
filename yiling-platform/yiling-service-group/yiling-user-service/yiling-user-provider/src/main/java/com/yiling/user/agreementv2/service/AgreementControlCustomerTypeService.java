package com.yiling.user.agreementv2.service;

import java.util.List;

import com.yiling.user.agreementv2.dto.AgreementControlCustomerTypeDTO;
import com.yiling.user.agreementv2.entity.AgreementControlCustomerTypeDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 协议控销客户类型表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-24
 */
public interface AgreementControlCustomerTypeService extends BaseService<AgreementControlCustomerTypeDO> {

    /**
     * 获取控销客户类型
     *
     * @param controlGoodsGroupId
     * @return
     */
    List<AgreementControlCustomerTypeDTO> getControlCustomerTypeList(Long controlGoodsGroupId);

}
