package com.yiling.dataflow.wash.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.api.UnlockSaleBusinessApi;
import com.yiling.dataflow.wash.bo.CrmEnterpriseBusinessRuleBO;
import com.yiling.dataflow.wash.dto.request.DeleteUnlockSaleBusinessRequest;
import com.yiling.dataflow.wash.dto.request.QueryCrmEnterpriseUnlockSalePageListRequest;
import com.yiling.dataflow.wash.dto.request.SaveUnlockSaleBusinessRequest;
import com.yiling.dataflow.wash.service.UnlockSaleBusinessService;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-04-25
 */
@DubboService
public class UnlockSaleBusinessApiImpl implements UnlockSaleBusinessApi {

    @Autowired
    private UnlockSaleBusinessService unlockSaleBusinessService;

    @Override
    public List<Long> getCrmEnterpriseIdByRuleId(Long ruleId) {
        return unlockSaleBusinessService.getCrmEnterpriseIdByRuleId(ruleId);
    }

    @Override
    public boolean save(SaveUnlockSaleBusinessRequest request) {
        return unlockSaleBusinessService.save(request);
    }

    @Override
    public boolean delete(DeleteUnlockSaleBusinessRequest request) {
        return unlockSaleBusinessService.delete(request);
    }

    @Override
    public Page<CrmEnterpriseBusinessRuleBO> page(QueryCrmEnterpriseUnlockSalePageListRequest request) {
        return unlockSaleBusinessService.page(request);
    }
}
