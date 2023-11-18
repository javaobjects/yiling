package com.yiling.order.order.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.order.order.dto.OrderReturnDetailBatchDTO;
import com.yiling.order.order.dto.ReturnDetailBathDTO;
import com.yiling.order.order.dto.ReturnOrderExportDTO;
import com.yiling.order.order.dto.request.QueryOrderReturnInfoRequest;

/**
 * @author: yong.zhang
 * @date: 2021/8/6
 */
public interface OrderReturnDetailBatchApi {

    /**
     * 退货单导出信息查询
     *
     * @param request
     * @return
     */
    Page<ReturnOrderExportDTO> queryExportByCondition(QueryOrderReturnInfoRequest request);

    /**
     * @param returnId
     * @param detailId
     * @param batchNo
     * @return
     */
    List<OrderReturnDetailBatchDTO> getOrderReturnDetailBatch(Long returnId, Long detailId, String batchNo);

    /**
     * @param returnIds
     * @return
     */
    List<OrderReturnDetailBatchDTO> getOrderReturnDetailBatchByReturnIds(List<Long> returnIds);

    /**
     * 根据订单明细编号集合查询出所有的退货批次信息
     *
     * @param detailIdList 订单明细id集合
     * @return
     */
    List<ReturnDetailBathDTO> queryByDetailId(List<Long> detailIdList);

    /**
     * 查询某种类型的退货单明细批次信息
     *
     * @param detailId
     * @param batchNo
     * @param returnType
     * @return
     */
    List<OrderReturnDetailBatchDTO> queryByDetailIdAndBatchNoAndType(Long detailId, String batchNo, Integer returnType);
}
