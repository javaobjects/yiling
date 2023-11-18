package com.yiling.order.order.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.order.order.dto.request.UpdatePresaleOrderRequest;
import com.yiling.order.order.entity.PresaleOrderDO;

/**
 * <p>
 * 预售订单表 服务类
 * </p>
 *
 * @author zhigang.guo
 * @date 2022-10-09
 */
public interface PresaleOrderService extends BaseService<PresaleOrderDO> {

    /**
     * 获取预售订单扩展信息
     * @param orderId 订单Id
     * @return 预售订单扩展信息
     */
    PresaleOrderDO getOrderInfo(Long orderId);

    /**
     * 批量查询预售订单扩展信息
     * @param orderIdList
     * @return
     */
    List<PresaleOrderDO> listByOrderIds(List<Long> orderIdList);

    /**
     * 通过订单号查询订单扩展信息
     * @param orderNoList
     * @return
     */
    List<PresaleOrderDO> listByOrderNos(List<String> orderNoList);


    /**
     * 查询需要支付尾款，并且需要发送短信提示的订单
     * @return
     */
    List<String> selectNeedSendBalanceSmsOrders(Integer hours);


    /**
     * 查询超时未支付尾款订单
     * @return
     */
    List<Long> selectTimeOutNotPayBalanceOrder();

    /**
     * 修改订单是否已支付定金和尾款,是否发送支付短信，取消短信
     * @param request
     * @return
     */
    boolean updatePresalOrderByOrderId(UpdatePresaleOrderRequest request);

    /**
     * 需要支付尾款订单
     * @return
     */
    List<String> selectNeedPayBalanceSmsOrders();

}
