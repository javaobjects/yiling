package com.yiling.hmc.order.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.pojo.Result;
import com.yiling.hmc.order.dto.OrderReturnDTO;
import com.yiling.hmc.order.dto.request.OrderReturnApplyRequest;
import com.yiling.hmc.order.dto.request.OrderReturnPageRequest;

/**
 * @author: yong.zhang
 * @date: 2022/3/25
 */
public interface OrderReturnApi {

    /**
     * 根据退货单id查询退货单信息
     *
     * @param returnId 退货单id
     * @return 退货单信息
     */
    OrderReturnDTO queryById(Long returnId);

    /**
     * 通过订单id查询退货单信息
     *
     * @param orderId 订单id
     * @return 退货单信息
     */
    OrderReturnDTO queryByOrderId(Long orderId);

    /**
     * 退单申请
     *
     * @param request 退单请求参数
     * @return 成功/失败
     */
    Result apply(OrderReturnApplyRequest request);

    /**
     * 退货单分页查询
     *
     * @param request 请求查询条件
     * @return 退货单信息
     */
    Page<OrderReturnDTO> pageList(OrderReturnPageRequest request);
}
