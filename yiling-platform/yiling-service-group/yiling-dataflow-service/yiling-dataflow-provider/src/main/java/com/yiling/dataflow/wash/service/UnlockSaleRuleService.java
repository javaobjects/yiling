package com.yiling.dataflow.wash.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.dto.UnlockSaleRuleDTO;
import com.yiling.dataflow.wash.dto.request.QueryuUnlockSaleRulePageRequest;
import com.yiling.dataflow.wash.dto.request.SaveOrUpdateUnlockSaleRuleRequest;
import com.yiling.dataflow.wash.entity.UnlockFlowWashSaleDO;
import com.yiling.dataflow.wash.entity.UnlockSaleRuleDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-04-25
 */
public interface UnlockSaleRuleService extends BaseService<UnlockSaleRuleDO> {

    /**
     * 获取所有的非锁销售分配规则
     * @return
     */
    List<UnlockSaleRuleDO> getUnlockSaleRuleList();

    Page<UnlockSaleRuleDO> listPage(QueryuUnlockSaleRulePageRequest request);

    Long saveOrUpdate(SaveOrUpdateUnlockSaleRuleRequest ruleRequest);

    String generateCode();

}
