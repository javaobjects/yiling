package com.yiling.dataflow.wash.api;

import java.util.List;

import com.yiling.dataflow.wash.dto.UnlockSaleCustomerRangeDTO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-04-25
 */
public interface UnlockSaleCustomerRangeApi {

    UnlockSaleCustomerRangeDTO getUnlockSaleCustomerRangeByRuleId(Long ruleId);

    List<UnlockSaleCustomerRangeDTO> getUnlockSaleCustomerRangeByRuleIds(List<Long> ruleIds);
}
