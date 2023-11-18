package com.yiling.dataflow.wash.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.dto.UnlockFlowWashSaleDTO;
import com.yiling.dataflow.wash.dto.request.QueryUnlockFlowWashSalePageRequest;
import com.yiling.dataflow.wash.dto.request.SaveUnlockFlowWashSaleRequest;
import com.yiling.dataflow.wash.dto.request.UpdateUnlockFlowWashSaleRequest;
import com.yiling.dataflow.wash.entity.UnlockFlowWashSaleDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 流向销售合并报 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-04-25
 */
public interface UnlockFlowWashSaleService extends BaseService<UnlockFlowWashSaleDO> {

    /**
     * 批量插入
     *
     * @param unlockFlowWashSaleDOList
     */
    void batchInsert(List<UnlockFlowWashSaleDO> unlockFlowWashSaleDOList);

    void updateBatchById(List<SaveUnlockFlowWashSaleRequest> unlockFlowWashSaleList);

    /**
     * 通过非锁任务获取非锁流向
     * @param ufwtId
     * @return
     */
    List<UnlockFlowWashSaleDO> getListByUfwtId(Long ufwtId);

    void updateClassificationCrmIdAndCustomerName(UpdateUnlockFlowWashSaleRequest request);

    Page<UnlockFlowWashSaleDO> pageList(QueryUnlockFlowWashSalePageRequest request);

    Integer countDistributionStatus(QueryUnlockFlowWashSalePageRequest request);

    Integer deleteByUfwtId(Long ufwtId);
}
