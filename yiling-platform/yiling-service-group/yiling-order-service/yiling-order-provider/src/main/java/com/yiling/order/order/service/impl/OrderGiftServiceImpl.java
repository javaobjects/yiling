package com.yiling.order.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.order.order.dao.OrderGiftMapper;
import com.yiling.order.order.entity.OrderGiftDO;
import com.yiling.order.order.service.OrderGiftService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 订单赠品表 服务实现类
 * </p>
 *
 * @author zhigang.guo
 * @date 2021-10-23
 */
@Service
public class OrderGiftServiceImpl extends BaseServiceImpl<OrderGiftMapper, OrderGiftDO> implements OrderGiftService {

    /**
     * 根据订单id查询
     *
     * @param orderId
     * @return
     */
    @Override
    public List<OrderGiftDO> listByOrderId(Long orderId) {
        QueryWrapper<OrderGiftDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderGiftDO :: getOrderId,orderId);
        return list(wrapper);
    }

    @Override
    public List<OrderGiftDO> listByOrderIds(List<Long> orderIds) {
        QueryWrapper<OrderGiftDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(OrderGiftDO :: getOrderId,orderIds);
        return list(wrapper);
    }

    @Override
    public List<OrderGiftDO> listByOrderId(Long orderId, Long activityId) {
        QueryWrapper<OrderGiftDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderGiftDO :: getOrderId,orderId);
        wrapper.lambda().eq(OrderGiftDO :: getPromotionActivityId,activityId);
        return list(wrapper);
    }
}
