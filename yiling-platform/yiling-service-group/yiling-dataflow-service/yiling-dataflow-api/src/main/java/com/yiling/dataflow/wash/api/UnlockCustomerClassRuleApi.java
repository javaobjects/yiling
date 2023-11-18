package com.yiling.dataflow.wash.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.dto.UnlockCustomerClassRuleDTO;
import com.yiling.dataflow.wash.dto.request.QueryUnlockCustomerClassRulePageRequest;
import com.yiling.dataflow.wash.dto.request.SaveOrUpdateUnlockCustomerClassRuleRequest;

/**
 * 非锁客户分类规则
 * @author fucheng.bai
 * @date 2023/5/4
 */
public interface UnlockCustomerClassRuleApi {

    /**
     * 分页查询非锁客户分类规则
     * @param request
     * @return
     */
    Page<UnlockCustomerClassRuleDTO> listPage(QueryUnlockCustomerClassRulePageRequest request);


    /**
     * 根据id查询
     * @param id
     * @return
     */
    UnlockCustomerClassRuleDTO getById(Long id);

    /**
     * 新增规则
     * @param request
     */
    void add(SaveOrUpdateUnlockCustomerClassRuleRequest request);

    /**
     * 修改规则
     * @param request
     */
    void update(SaveOrUpdateUnlockCustomerClassRuleRequest request);

    /**
     * 删除
     * @param id
     */
    void delete(Long id);
}
