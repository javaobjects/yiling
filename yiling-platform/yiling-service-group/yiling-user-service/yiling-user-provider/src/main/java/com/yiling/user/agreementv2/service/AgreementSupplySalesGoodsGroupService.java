package com.yiling.user.agreementv2.service;

import java.util.List;

import com.yiling.user.agreementv2.entity.AgreementSupplySalesGoodsGroupDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 协议供销商品组表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-03
 */
public interface AgreementSupplySalesGoodsGroupService extends BaseService<AgreementSupplySalesGoodsGroupDO> {

    /**
     * 根据协议id查询供销商品组
     *
     * @param agreementId
     * @return
     */
    List<AgreementSupplySalesGoodsGroupDO> getByAgreementId(Long agreementId);


}
