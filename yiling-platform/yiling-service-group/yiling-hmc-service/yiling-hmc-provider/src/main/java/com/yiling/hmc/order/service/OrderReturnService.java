package com.yiling.hmc.order.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.framework.common.pojo.Result;
import com.yiling.hmc.order.dto.request.OrderReturnApplyRequest;
import com.yiling.hmc.order.dto.request.OrderReturnPageRequest;
import com.yiling.hmc.order.entity.OrderReturnDO;

/**
 * <p>
 * 退货表 服务类
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-25
 */
public interface OrderReturnService extends BaseService<OrderReturnDO> {

    /**
     * 退单申请
     *
     * @param request 退单请求参数
     * @return 成功/失败
     */
    Result apply(OrderReturnApplyRequest request);

    /**
     * 通过订单id查询退货单信息
     *
     * @param orderId 订单id
     * @return 退货单信息
     */
    OrderReturnDO queryByOrderId(Long orderId);

    /**
     * 退货单分页查询
     *
     * @param request 请求查询条件
     * @return 退货单信息
     */
    Page<OrderReturnDO> pageList(OrderReturnPageRequest request);

}
