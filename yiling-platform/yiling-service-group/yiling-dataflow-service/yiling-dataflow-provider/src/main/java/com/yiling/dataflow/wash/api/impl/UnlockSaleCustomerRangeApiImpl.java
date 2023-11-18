package com.yiling.dataflow.wash.api.impl;

import java.util.List;

import com.yiling.dataflow.wash.api.UnlockSaleCustomerRangeApi;
import com.yiling.dataflow.wash.dao.UnlockSaleCustomerRangeMapper;
import com.yiling.dataflow.wash.dto.UnlockSaleCustomerRangeDTO;
import com.yiling.dataflow.wash.entity.UnlockSaleCustomerRangeDO;
import com.yiling.dataflow.wash.service.UnlockSaleCustomerRangeService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-04-25
 */
@DubboService
public class UnlockSaleCustomerRangeApiImpl  implements UnlockSaleCustomerRangeApi {

    @Autowired
    private UnlockSaleCustomerRangeService unlockSaleCustomerRangeService;

    @Override
    public UnlockSaleCustomerRangeDTO getUnlockSaleCustomerRangeByRuleId(Long ruleId) {
        return PojoUtils.map(unlockSaleCustomerRangeService.getUnlockSaleCustomerRangeByRuleId(ruleId),UnlockSaleCustomerRangeDTO.class);
    }

    @Override
    public List<UnlockSaleCustomerRangeDTO> getUnlockSaleCustomerRangeByRuleIds(List<Long> ruleIds) {
        return  PojoUtils.map(unlockSaleCustomerRangeService.getUnlockSaleCustomerRangeByRuleIds(ruleIds),UnlockSaleCustomerRangeDTO.class);
    }
}
