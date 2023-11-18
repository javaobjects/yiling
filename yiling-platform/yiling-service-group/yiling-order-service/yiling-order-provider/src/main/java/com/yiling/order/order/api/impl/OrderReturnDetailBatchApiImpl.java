package com.yiling.order.order.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.api.OrderReturnDetailBatchApi;
import com.yiling.order.order.dto.OrderReturnDetailBatchDTO;
import com.yiling.order.order.dto.ReturnDetailBathDTO;
import com.yiling.order.order.dto.ReturnOrderExportDTO;
import com.yiling.order.order.dto.request.QueryOrderReturnInfoRequest;
import com.yiling.order.order.entity.OrderReturnDetailBatchDO;
import com.yiling.order.order.service.OrderReturnDetailBatchService;

/**
 * @author: yong.zhang
 * @date: 2021/8/6
 */
@DubboService
public class OrderReturnDetailBatchApiImpl implements OrderReturnDetailBatchApi {
    @Autowired
    private OrderReturnDetailBatchService orderReturnDetailBatchService;


    @Override
    public Page<ReturnOrderExportDTO> queryExportByCondition(QueryOrderReturnInfoRequest request) {
        return orderReturnDetailBatchService.queryExportByCondition(request);
    }

    @Override
    public List<OrderReturnDetailBatchDTO> getOrderReturnDetailBatch(Long returnId, Long detailId, String batchNo) {
        List<OrderReturnDetailBatchDO> orderReturnDetailBatchDOList = orderReturnDetailBatchService.getOrderReturnDetailBatch(returnId, detailId, batchNo);
        return PojoUtils.map(orderReturnDetailBatchDOList, OrderReturnDetailBatchDTO.class);
    }

    @Override
    public List<OrderReturnDetailBatchDTO> getOrderReturnDetailBatchByReturnIds(List<Long> returnIds) {
        List<OrderReturnDetailBatchDO> orderReturnDetailBatchByReturnIds = orderReturnDetailBatchService.getOrderReturnDetailBatchByReturnIds(returnIds);
        return PojoUtils.map(orderReturnDetailBatchByReturnIds, OrderReturnDetailBatchDTO.class);
    }

    @Override
    public List<ReturnDetailBathDTO> queryByDetailId(List<Long> detailIdList) {
        return orderReturnDetailBatchService.queryByDetailId(detailIdList);
    }

    /**
     * 查询某种类型的退货单明细批次信息
     *
     * @param detailId
     * @param batchNo
     * @param returnType
     * @return
     */
    @Override
    public List<OrderReturnDetailBatchDTO> queryByDetailIdAndBatchNoAndType(Long detailId, String batchNo, Integer returnType) {
        List<OrderReturnDetailBatchDO> orderReturnDetailBatchDOS = orderReturnDetailBatchService.queryByDetailIdAndBatchNoAndType(detailId, batchNo, returnType);
        return PojoUtils.map(orderReturnDetailBatchDOS,OrderReturnDetailBatchDTO.class);
    }
}
