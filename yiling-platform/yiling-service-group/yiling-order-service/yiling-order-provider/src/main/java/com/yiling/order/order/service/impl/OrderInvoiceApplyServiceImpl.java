package com.yiling.order.order.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.dao.OrderInvoiceApplyMapper;
import com.yiling.order.order.dto.OrderInvoiceApplyDTO;
import com.yiling.order.order.dto.OrderInvoicePullErpDTO;
import com.yiling.order.order.dto.request.OrderPullErpPageRequest;
import com.yiling.order.order.entity.OrderInvoiceApplyDO;
import com.yiling.order.order.service.OrderInvoiceApplyService;

/**
 * <p>
 * 订单开票申请 服务实现类
 * </p>
 *
 * @author wei.wang
 * @date 2021-07-02
 */
@Service
public class OrderInvoiceApplyServiceImpl extends BaseServiceImpl<OrderInvoiceApplyMapper, OrderInvoiceApplyDO> implements OrderInvoiceApplyService {


    /**
     * 根据orderIds获取订单开票信息
     *
     * @param orderIds
     * @return
     */
    @Override
    public List<OrderInvoiceApplyDTO> getOrderInvoiceApplyByList(List<Long> orderIds) {
        QueryWrapper<OrderInvoiceApplyDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(OrderInvoiceApplyDO::getOrderId, orderIds);
        return PojoUtils.map(list(wrapper), OrderInvoiceApplyDTO.class);
    }

    /**
     * 根据orderId获取所有开票申请
     *
     * @param orderId
     * @return
     */
    @Override
    public List<OrderInvoiceApplyDTO> listByOrderId(Long orderId) {
        QueryWrapper<OrderInvoiceApplyDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderInvoiceApplyDO::getOrderId, orderId);
        return PojoUtils.map(list(wrapper), OrderInvoiceApplyDTO.class);
    }

    /**
     * 获取未同步推送EAS申请发票订单id
     *
     * @param request
     * @return
     */
    @Override
    public Page<OrderInvoicePullErpDTO> getErpPullOrderInvoice(OrderPullErpPageRequest request) {
        Page<OrderInvoicePullErpDTO> list = this.getBaseMapper().getErpPullInvoiceOrderId(new Page<>(request.getCurrent(), request.getSize()), request);
        return list;
    }



}
