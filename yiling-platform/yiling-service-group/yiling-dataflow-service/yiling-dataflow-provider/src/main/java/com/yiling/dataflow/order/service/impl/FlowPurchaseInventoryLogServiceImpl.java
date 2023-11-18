package com.yiling.dataflow.order.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.order.bo.FlowPurchaseInventorySumAdjustmentQuantityBO;
import com.yiling.dataflow.order.dao.FlowPurchaseInventoryLogMapper;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseInventoryLogListPageRequest;
import com.yiling.dataflow.order.dto.request.SaveFlowPurchaseInventoryLogRequest;
import com.yiling.dataflow.order.entity.FlowPurchaseInventoryLogDO;
import com.yiling.dataflow.order.service.FlowPurchaseInventoryLogService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.util.ObjectUtil;

/**
 * <p>
 * 流向商品库存日志 服务实现类
 * </p>
 *
 * @author houjie.sun
 * @date 2022-05-27
 */
@Service
public class FlowPurchaseInventoryLogServiceImpl extends BaseServiceImpl<FlowPurchaseInventoryLogMapper, FlowPurchaseInventoryLogDO> implements FlowPurchaseInventoryLogService {

    @Override
    public Boolean saveGoodInventoryLog(SaveFlowPurchaseInventoryLogRequest request) {
        if(ObjectUtil.isNull(request) || ObjectUtil.isNull(request.getFlowPurchaseInventoryId()) || ObjectUtil.isNull(request.getBeforeQuantity())
                || ObjectUtil.isNull(request.getAfterQuantity()) || ObjectUtil.isNull(request.getChangeQuantity())){
            return true;
        }
        FlowPurchaseInventoryLogDO entity = PojoUtils.map(request, FlowPurchaseInventoryLogDO.class);
        return this.save(entity);
    }

    @Override
    public Page<FlowPurchaseInventoryLogDO> pageByInventoryId(QueryFlowPurchaseInventoryLogListPageRequest request) {
        if(ObjectUtil.isNull(request) || ObjectUtil.isNull(request.getInventoryId())){
            return request.getPage();
        }
        return this.baseMapper.pageByInventoryId(request.getPage(), request);
    }

    @Override
    public List<FlowPurchaseInventorySumAdjustmentQuantityBO> getSumAdjustmentQuantityByInventoryIdList(List<Long> inventoryIdList) {
        return this.baseMapper.getSumAdjustmentQuantityByInventoryIdList(inventoryIdList);
    }

    @Override
    public List<FlowPurchaseInventorySumAdjustmentQuantityBO> getSumDeductionsQuantityByInventoryIdList(List<Long> inventoryIdList) {
        return this.baseMapper.getSumDeductionsQuantityByInventoryIdList(inventoryIdList);
    }
}
