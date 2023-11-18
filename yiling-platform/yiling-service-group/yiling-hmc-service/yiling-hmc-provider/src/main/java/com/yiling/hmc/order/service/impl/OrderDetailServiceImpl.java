package com.yiling.hmc.order.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.hmc.order.dao.OrderDetailMapper;
import com.yiling.hmc.order.entity.OrderDetailDO;
import com.yiling.hmc.order.service.OrderDetailService;

/**
 * <p>
 * 订单明细表 服务实现类
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-25
 */
@Service
public class OrderDetailServiceImpl extends BaseServiceImpl<OrderDetailMapper, OrderDetailDO> implements OrderDetailService {

    @Override
    public List<OrderDetailDO> listByOrderId(Long orderId) {
        QueryWrapper<OrderDetailDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderDetailDO::getOrderId, orderId);
        return this.list(wrapper);
    }
}
