package com.yiling.order.order.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.order.order.bo.OrderFlowBO;
import com.yiling.order.order.dto.request.QueryOrderFlowRequest;
import com.yiling.order.order.entity.OrderDeliveryDO;

/**
 * <p>
 * 订单发货信息 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-17
 */
public interface OrderDeliveryService extends BaseService<OrderDeliveryDO> {

    /**
     * 获取发货批次信息
     * @param orderId
     * @param orderDetailId
     * @param goodsId
     * @return
     */
    List<OrderDeliveryDO> getOrderDeliveryInfo(Long orderId,Long orderDetailId,Long goodsId);

    /**
     * 获取发货批次信息批量
     * @param orderId
     * @return
     */
    List<OrderDeliveryDO> getOrderDeliveryList(Long orderId);

    /**
     * 获取发货数据
     * @param orderIds
     * @return
     */
    List<OrderDeliveryDO> listByOrderIds(List<Long> orderIds);

    /**
     * 根据订单编号，订单明细编号和批次号查询发货批次信息
     *
     * @param orderId
     * @param detailId
     * @param batchNo
     * @return
     */
    OrderDeliveryDO queryByOrderIdAndDetailIdAndBatchNo(Long orderId,Long detailId,String batchNo);

    /**
     * 删除发货单
     * @param orderId
     * @param userId
     * @return
     */
    boolean deleteOrderDelivery(Long orderId,Long userId);

    /**
     * 通过主键删除发货单数据
     * @param keyList 主键结果集
     * @return
     */
    boolean deleteOrderDeliveryByPrimaryKey(List<Long> keyList,Long userId);

    /**
     * pop 以岭流向接口
     *
     * @param request
     * @return
     */
    Page<OrderFlowBO> getPageList(QueryOrderFlowRequest request);
}
