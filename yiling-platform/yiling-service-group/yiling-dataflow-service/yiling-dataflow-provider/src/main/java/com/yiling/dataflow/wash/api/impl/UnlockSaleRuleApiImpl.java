package com.yiling.dataflow.wash.api.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.api.UnlockSaleRuleApi;
import com.yiling.dataflow.wash.dto.UnlockSaleRuleDTO;
import com.yiling.dataflow.wash.dto.request.DeleteUnlockSaleRuleRequest;
import com.yiling.dataflow.wash.dto.request.QueryuUnlockSaleRulePageRequest;
import com.yiling.dataflow.wash.dto.request.SaveOrUpdateUnlockSaleRuleRequest;
import com.yiling.dataflow.wash.entity.UnlockSaleRuleDO;
import com.yiling.dataflow.wash.service.UnlockSaleRuleService;
import com.yiling.framework.common.util.PojoUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: shuang.zhang
 * @date: 2023/5/4
 */
@DubboService
public class UnlockSaleRuleApiImpl implements UnlockSaleRuleApi {

    @Autowired
    private UnlockSaleRuleService unlockSaleRuleService;

    @Override
    public List<UnlockSaleRuleDTO> getUnlockSaleRuleList() {
        return PojoUtils.map(unlockSaleRuleService.getUnlockSaleRuleList(), UnlockSaleRuleDTO.class);
    }

    @Override
    public UnlockSaleRuleDTO getById(Long id) {
        return PojoUtils.map(unlockSaleRuleService.getById(id), UnlockSaleRuleDTO.class);
    }

    @Override
    public Page<UnlockSaleRuleDTO> listPage(QueryuUnlockSaleRulePageRequest request) {
        return PojoUtils.map(unlockSaleRuleService.listPage(request), UnlockSaleRuleDTO.class);
    }

    @Override
    public Long saveOrUpdate(SaveOrUpdateUnlockSaleRuleRequest ruleRequest) {
        return unlockSaleRuleService.saveOrUpdate(ruleRequest);
    }

    @Override
    public Boolean delete(DeleteUnlockSaleRuleRequest request) {
        UnlockSaleRuleDO unlockSaleRuleDO = new UnlockSaleRuleDO();
        unlockSaleRuleDO.setId(request.getId());
        unlockSaleRuleDO.setOpUserId(request.getOpUserId());
        return unlockSaleRuleService.deleteByIdWithFill(unlockSaleRuleDO) > 0 ? true : false;
    }

    @Override
    public String generateCode() {
        return unlockSaleRuleService.generateCode();
    }


}
