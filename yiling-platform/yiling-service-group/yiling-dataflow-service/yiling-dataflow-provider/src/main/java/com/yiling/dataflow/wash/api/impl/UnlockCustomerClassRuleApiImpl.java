package com.yiling.dataflow.wash.api.impl;


import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.api.UnlockCustomerClassRuleApi;
import com.yiling.dataflow.wash.dto.UnlockCustomerClassRuleDTO;
import com.yiling.dataflow.wash.dto.request.QueryUnlockCustomerClassRulePageRequest;
import com.yiling.dataflow.wash.dto.request.SaveOrUpdateUnlockCustomerClassRuleRequest;
import com.yiling.dataflow.wash.service.UnlockCustomerClassRuleService;
import com.yiling.framework.common.util.PojoUtils;

/**
 * @author fucheng.bai
 * @date 2023/5/4
 */
@DubboService
public class UnlockCustomerClassRuleApiImpl implements UnlockCustomerClassRuleApi {

    @Autowired
    private UnlockCustomerClassRuleService unlockCustomerClassRuleService;

    @Override
    public Page<UnlockCustomerClassRuleDTO> listPage(QueryUnlockCustomerClassRulePageRequest request) {
        return PojoUtils.map(unlockCustomerClassRuleService.listPage(request), UnlockCustomerClassRuleDTO.class);
    }

    @Override
    public UnlockCustomerClassRuleDTO getById(Long id) {
        return PojoUtils.map(unlockCustomerClassRuleService.getById(id), UnlockCustomerClassRuleDTO.class);
    }

    @Override
    public void add(SaveOrUpdateUnlockCustomerClassRuleRequest request) {
        unlockCustomerClassRuleService.save(request);
    }

    @Override
    public void update(SaveOrUpdateUnlockCustomerClassRuleRequest request) {
        unlockCustomerClassRuleService.update(request);
    }

    @Override
    public void delete(Long id) {
        unlockCustomerClassRuleService.delete(id);
    }
}
