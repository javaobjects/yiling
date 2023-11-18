package com.yiling.dataflow.wash.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.dto.UnlockSaleRuleDTO;
import com.yiling.dataflow.wash.dto.request.DeleteUnlockSaleRuleRequest;
import com.yiling.dataflow.wash.dto.request.QueryuUnlockSaleRulePageRequest;
import com.yiling.dataflow.wash.dto.request.SaveOrUpdateUnlockSaleRuleRequest;

/**
 * @author: shuang.zhang
 * @date: 2023/5/4
 */
public interface UnlockSaleRuleApi {

    /**
     * 获取所有的非锁销售分配规则
     * @return
     */
    List<UnlockSaleRuleDTO> getUnlockSaleRuleList();

    UnlockSaleRuleDTO getById(Long id);

    Page<UnlockSaleRuleDTO> listPage(QueryuUnlockSaleRulePageRequest request);

    Long saveOrUpdate(SaveOrUpdateUnlockSaleRuleRequest ruleRequest);

    Boolean delete(DeleteUnlockSaleRuleRequest request);

    String generateCode();
}
