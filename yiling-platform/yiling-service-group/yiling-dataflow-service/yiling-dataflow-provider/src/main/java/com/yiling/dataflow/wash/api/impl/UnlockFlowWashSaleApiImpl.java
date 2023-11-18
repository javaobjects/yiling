package com.yiling.dataflow.wash.api.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.api.UnlockFlowWashSaleApi;
import com.yiling.dataflow.wash.dto.UnlockFlowWashSaleDTO;
import com.yiling.dataflow.wash.dto.request.QueryUnlockFlowWashSalePageRequest;
import com.yiling.dataflow.wash.dto.request.SaveUnlockFlowWashSaleRequest;
import com.yiling.dataflow.wash.entity.UnlockFlowWashSaleDO;
import com.yiling.dataflow.wash.service.UnlockFlowWashSaleService;
import com.yiling.framework.common.util.PojoUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: shuang.zhang
 * @date: 2023/5/4
 */
@DubboService
public class UnlockFlowWashSaleApiImpl implements UnlockFlowWashSaleApi {

    @Autowired
    private UnlockFlowWashSaleService unlockFlowWashSaleService;

    @Override
    public void batchInsert(List<SaveUnlockFlowWashSaleRequest> unlockFlowWashSaleList) {
        unlockFlowWashSaleService.batchInsert(PojoUtils.map(unlockFlowWashSaleList, UnlockFlowWashSaleDO.class));
    }

    @Override
    public void updateBatchById(List<SaveUnlockFlowWashSaleRequest> unlockFlowWashSaleList) {
        unlockFlowWashSaleService.updateBatchById(unlockFlowWashSaleList);
    }

    @Override
    public List<UnlockFlowWashSaleDTO> getListByUfwtId(Long ufwtId) {
        return PojoUtils.map(unlockFlowWashSaleService.getListByUfwtId(ufwtId), UnlockFlowWashSaleDTO.class);
    }

    @Override
    public Page<UnlockFlowWashSaleDTO> pageList(QueryUnlockFlowWashSalePageRequest request) {
        return PojoUtils.map(unlockFlowWashSaleService.pageList(request), UnlockFlowWashSaleDTO.class);
    }

    @Override
    public Integer countDistributionStatus(QueryUnlockFlowWashSalePageRequest request) {
        return unlockFlowWashSaleService.countDistributionStatus(request);
    }

    @Override
    public List<UnlockFlowWashSaleDTO> getByIds(List<Long> ids) {
        return PojoUtils.map(unlockFlowWashSaleService.listByIds(ids),UnlockFlowWashSaleDTO.class);
    }
}
