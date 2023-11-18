package com.yiling.order.order.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.api.OrderReturnDetailApi;
import com.yiling.order.order.dto.OrderReturnDetailDTO;
import com.yiling.order.order.entity.OrderReturnDetailDO;
import com.yiling.order.order.service.OrderReturnDetailService;

/**
 * 退货单明细 API 实现
 *
 * @author: yong.zhang
 * @date: 2021/8/5
 */
@DubboService
public class OrderReturnDetailApiImpl implements OrderReturnDetailApi {
    @Autowired
    private OrderReturnDetailService orderReturnDetailService;

    /**
     * 根据退货单id批量获取明细
     *
     * @param returnIds
     * @return
     */
    @Override
    public List<OrderReturnDetailDTO> getOrderReturnDetailByReturnIds(List<Long> returnIds) {
        List<OrderReturnDetailDO> orderReturnDetailDOList = orderReturnDetailService.getOrderReturnDetailByReturnIds(returnIds);
        return PojoUtils.map(orderReturnDetailDOList, OrderReturnDetailDTO.class);
    }

    @Override
    public OrderReturnDetailDTO queryByReturnIdAndDetailId(Long returnId, Long detailId) {
        OrderReturnDetailDO orderReturnDetailDO = orderReturnDetailService.queryByReturnIdAndDetailId(returnId, detailId);
        return PojoUtils.map(orderReturnDetailDO, OrderReturnDetailDTO.class);
    }

    @Override
    public List<OrderReturnDetailDTO> getOrderReturnDetailByReturnId(Long returnId) {
        List<OrderReturnDetailDO> orderReturnDetailDOList = orderReturnDetailService.getOrderReturnDetailByReturnId(returnId);
        return PojoUtils.map(orderReturnDetailDOList, OrderReturnDetailDTO.class);
    }
}
