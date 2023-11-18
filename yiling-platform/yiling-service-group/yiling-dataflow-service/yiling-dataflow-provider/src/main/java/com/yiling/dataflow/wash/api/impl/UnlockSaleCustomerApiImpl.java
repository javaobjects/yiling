package com.yiling.dataflow.wash.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.api.UnlockSaleCustomerApi;
import com.yiling.dataflow.wash.bo.CrmEnterpriseBusinessRuleBO;
import com.yiling.dataflow.wash.dto.request.DeleteUnlockSaleCustomerRequest;
import com.yiling.dataflow.wash.dto.request.QueryCrmEnterpriseUnlockSalePageListRequest;
import com.yiling.dataflow.wash.dto.request.SaveUnlockSaleCustomerRequest;
import com.yiling.dataflow.wash.service.UnlockSaleCustomerService;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-04-25
 */
@DubboService
public class UnlockSaleCustomerApiImpl implements UnlockSaleCustomerApi {

    @Autowired
    private UnlockSaleCustomerService unlockSaleCustomerService;

    @Override
    public List<Long> getCrmEnterpriseIdByRuleId(Long ruleId) {
        return unlockSaleCustomerService.getCrmEnterpriseIdByRuleId(ruleId);
    }

    @Override
    public boolean save(SaveUnlockSaleCustomerRequest request) {
        return unlockSaleCustomerService.save(request);
    }

    @Override
    public boolean delete(DeleteUnlockSaleCustomerRequest request) {
        return unlockSaleCustomerService.delete(request);
    }

    @Override
    public Page<CrmEnterpriseBusinessRuleBO> page(QueryCrmEnterpriseUnlockSalePageListRequest request) {
        return unlockSaleCustomerService.page(request);
    }
}
