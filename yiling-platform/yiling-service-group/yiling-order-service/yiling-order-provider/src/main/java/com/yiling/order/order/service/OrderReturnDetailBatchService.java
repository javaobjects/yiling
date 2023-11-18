package com.yiling.order.order.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.order.order.dto.ReturnDetailBathDTO;
import com.yiling.order.order.dto.ReturnOrderExportDTO;
import com.yiling.order.order.dto.request.QueryOrderReturnInfoRequest;
import com.yiling.order.order.entity.OrderReturnDetailBatchDO;

/**
 * <p>
 * 退货单明细批次信息 服务类
 * </p>
 *
 * @author zhangy
 * @date 2021-08-06
 */
public interface OrderReturnDetailBatchService extends BaseService<OrderReturnDetailBatchDO> {

    /**
     * 退货单导出信息查询
     *
     * @param request
     * @return
     */
    Page<ReturnOrderExportDTO> queryExportByCondition(QueryOrderReturnInfoRequest request);

    /**
     * 查询某种类型的退货单明细批次信息
     *
     * @param detailId
     * @param batchNo
     * @param returnType
     * @return
     */
    List<OrderReturnDetailBatchDO> queryByDetailIdAndBatchNoAndType(Long detailId, String batchNo, Integer returnType);

    /**
     * 查询某一批次某商品的所有退货批次明细
     *
     * @param detailId
     * @param batchNo
     * @return
     */
    List<OrderReturnDetailBatchDO> getByDetailIdAndBatchNo(Long detailId, String batchNo);

    /**
     * @param returnId
     * @param detailId
     * @param batchNo
     * @return
     */
    List<OrderReturnDetailBatchDO> getOrderReturnDetailBatch(Long returnId, Long detailId, String batchNo);

    /**
     * @param returnIds
     * @return
     */
    List<OrderReturnDetailBatchDO> getOrderReturnDetailBatchByReturnIds(List<Long> returnIds);

    /**
     * 查询退货单明细批次信息
     *
     * @param returnId
     * @param detailId
     * @param batchNo
     * @return
     */
    OrderReturnDetailBatchDO queryByReturnIdAndDetailIdAndBathNo(Long returnId, Long detailId, String batchNo);

    /**
     * 根据订单明细编号集合查询出所有的退货批次信息
     *
     * @param detailIdList 订单明细id集合
     * @return
     */
    List<ReturnDetailBathDTO> queryByDetailId(List<Long> detailIdList);
}
