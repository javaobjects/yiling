package com.yiling.order.order.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.order.order.dao.OrderInvoiceDetailMapper;
import com.yiling.order.order.entity.OrderInvoiceDetailDO;
import com.yiling.order.order.service.OrderInvoiceDetailService;

/**
 * 订单开票明细实现
 * @author:wei.wang
 * @date:2021/8/4
 */
@Service
public class OrderInvoiceDetailServiceImpl extends BaseServiceImpl<OrderInvoiceDetailMapper, OrderInvoiceDetailDO> implements OrderInvoiceDetailService {



    /**
     * 根据订单ids获取开票申请明细信息
     *
     * @param orders
     * @return
     */
    @Override
    public List<OrderInvoiceDetailDO> listByOrderIds(List<Long> orders) {
        QueryWrapper<OrderInvoiceDetailDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(OrderInvoiceDetailDO :: getOrderId,orders);
        return list(wrapper) ;
    }
}
