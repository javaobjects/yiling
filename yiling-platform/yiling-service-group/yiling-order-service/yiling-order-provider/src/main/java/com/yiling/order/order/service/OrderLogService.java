package com.yiling.order.order.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.order.order.dto.OrderLogDTO;
import com.yiling.order.order.entity.OrderLogDO;

/**
 * <p>
 * 订单日志 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-17
 */
public interface OrderLogService extends BaseService<OrderLogDO> {
    /**
     * 获取日志信息
     * @param orderId
     * @return
     */
    List<OrderLogDTO> getOrderLogInfo(Long orderId);
    /**
     * 获取日志信息
     * @param orderId
     * @param logType
     * @return
     */
    List<OrderLogDTO> getOrderLogInfo(Long orderId, Integer logType);

    /**
     * 添加订单日志
     *
     * @param orderId 订单ID
     * @param logContent 日志内容
     * @return
     */
    boolean add(Long orderId, String logContent);

    /**
     *
     * @param orderId 订单ID
     * @param logContent 日志内容
     * @param opUserId  操作人ID
     * @return
     */
    boolean add(Long orderId, String logContent,Long opUserId);

    /**
     * 添加退货单日志
     *
     * @param orderId 订单ID
     * @param logType 日志类型
     * @param logContent 日志内容
     * @return
     */
    boolean add(Long orderId, Integer logType, String logContent);
}
