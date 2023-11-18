package com.yiling.dataflow.wash.service;

import java.util.List;

import com.yiling.dataflow.wash.dto.UnlockSaleCustomerRangeDTO;
import com.yiling.dataflow.wash.entity.UnlockSaleCustomerRangeDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-04-25
 */
public interface UnlockSaleCustomerRangeService extends BaseService<UnlockSaleCustomerRangeDO> {

    UnlockSaleCustomerRangeDO getUnlockSaleCustomerRangeByRuleId(Long ruleId);

    List<UnlockSaleCustomerRangeDO> getUnlockSaleCustomerRangeByRuleIds(List<Long> ruleIds);
}
