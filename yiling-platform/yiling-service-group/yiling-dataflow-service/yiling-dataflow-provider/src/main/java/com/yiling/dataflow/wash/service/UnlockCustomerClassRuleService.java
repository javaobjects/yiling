package com.yiling.dataflow.wash.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.dto.request.QueryUnlockCustomerClassRulePageRequest;
import com.yiling.dataflow.wash.dto.request.SaveOrUpdateUnlockCustomerClassRuleRequest;
import com.yiling.dataflow.wash.entity.UnlockCustomerClassRuleDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 非锁客户分类规则表 服务类
 * </p>
 *
 * @author baifc
 * @since 2023-04-26
 */
public interface UnlockCustomerClassRuleService extends BaseService<UnlockCustomerClassRuleDO> {

    List<UnlockCustomerClassRuleDO> getAvailableList();

    Page<UnlockCustomerClassRuleDO> listPage(QueryUnlockCustomerClassRulePageRequest request);

    UnlockCustomerClassRuleDO getById(Long id);

    void save(SaveOrUpdateUnlockCustomerClassRuleRequest request);

    void update(SaveOrUpdateUnlockCustomerClassRuleRequest request);

    void delete(Long id);

    UnlockCustomerClassRuleDO ruleExecute(String customerName);
}
