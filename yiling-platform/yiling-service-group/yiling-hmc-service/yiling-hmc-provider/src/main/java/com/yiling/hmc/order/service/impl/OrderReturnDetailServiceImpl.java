package com.yiling.hmc.order.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.hmc.order.dao.OrderReturnDetailMapper;
import com.yiling.hmc.order.entity.OrderReturnDetailDO;
import com.yiling.hmc.order.service.OrderReturnDetailService;

/**
 * <p>
 * 退货单明细表 服务实现类
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-25
 */
@Service
public class OrderReturnDetailServiceImpl extends BaseServiceImpl<OrderReturnDetailMapper, OrderReturnDetailDO> implements OrderReturnDetailService {

    @Override
    public List<OrderReturnDetailDO> listByReturnId(Long returnId) {
        QueryWrapper<OrderReturnDetailDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderReturnDetailDO::getReturnId, returnId);
        return this.list(wrapper);
    }
}
