package com.yiling.hmc.settlement.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.hmc.settlement.entity.InsuranceSettlementDetailDO;

/**
 * <p>
 * 保司结账明细表 服务类
 * </p>
 *
 * @author yong.zhang
 * @date 2022-05-13
 */
public interface InsuranceSettlementDetailService extends BaseService<InsuranceSettlementDetailDO> {

    /**
     * 根据保司结账主id查询保司结账明细
     *
     * @param insuranceSettlementId 保司结账主表id
     * @return 保司结账明细
     */
    List<InsuranceSettlementDetailDO> listByInsuranceSettlementId(Long insuranceSettlementId);

    /**
     * 根据订单号集合查询保司结账明细
     *
     * @param orderNoList 订单编号集合
     * @return 保司结账明细
     */
    List<InsuranceSettlementDetailDO> listByOrderNoList(List<String> orderNoList);
}
