package com.yiling.dataflow.wash.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.dto.UnlockFlowWashSaleDTO;
import com.yiling.dataflow.wash.dto.UnlockSaleRuleDTO;
import com.yiling.dataflow.wash.dto.request.QueryUnlockFlowWashSalePageRequest;
import com.yiling.dataflow.wash.dto.request.SaveUnlockFlowWashSaleRequest;

/**
 * @author: shuang.zhang
 * @date: 2023/5/4
 */
public interface UnlockFlowWashSaleApi {

    /**
     * 批量插入
     *
     * @param unlockFlowWashSaleList
     */
    void batchInsert(List<SaveUnlockFlowWashSaleRequest> unlockFlowWashSaleList);

    /**
     * 通过id批量更新
     * @param unlockFlowWashSaleList
     */
    void updateBatchById(List<SaveUnlockFlowWashSaleRequest> unlockFlowWashSaleList);

    /**
     * 通过非锁任务获取非锁流向数据
     * @param ufwtId
     * @return
     */
    List<UnlockFlowWashSaleDTO> getListByUfwtId(Long ufwtId);

    Page<UnlockFlowWashSaleDTO> pageList(QueryUnlockFlowWashSalePageRequest request);

    Integer countDistributionStatus(QueryUnlockFlowWashSalePageRequest request);

    List<UnlockFlowWashSaleDTO> getByIds(List<Long> ids);
}
