package com.yiling.order.order.service;

import com.yiling.framework.common.base.BaseService;
import com.yiling.order.order.dto.OrderB2BCountAmountDTO;
import com.yiling.order.order.dto.request.QueryB2BOrderCountRequest;
import com.yiling.order.order.entity.OrderExtendDO;

/**
 * <p>
 * 订单扩展 服务类
 * </p>
 *
 * @author wei.wang
 * @date 2022-11-02
 */
public interface OrderExtendService extends BaseService<OrderExtendDO> {

    /**
     * 获取订单属性
     * @param orderId
     * @return
     */
    OrderExtendDO getOrderExtendInfo(Long orderId);

    /**
     * 获取B2B订单报表金额信息
     * @param request
     * @return
     */
    OrderB2BCountAmountDTO getB2BCountAmount( QueryB2BOrderCountRequest request);

    /**
     * 获取B2B订单报表数量信息
     * @param request
     * @return
     */
    Integer getB2BCountQuantity( QueryB2BOrderCountRequest request);
}
