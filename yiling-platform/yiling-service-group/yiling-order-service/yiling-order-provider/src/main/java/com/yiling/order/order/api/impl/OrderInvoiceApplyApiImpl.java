package com.yiling.order.order.api.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.api.OrderInvoiceApplyApi;
import com.yiling.order.order.dto.OrderInvoiceApplyDTO;
import com.yiling.order.order.dto.request.UpdateOrderInvoiceApplyRequest;
import com.yiling.order.order.entity.OrderInvoiceApplyDO;
import com.yiling.order.order.service.OrderInvoiceApplyService;

/**
 * 订单发票申请Api
 * @author:wei.wang
 * @date:2021/7/2
 */
@DubboService
public class OrderInvoiceApplyApiImpl implements OrderInvoiceApplyApi {

    @Autowired
    private OrderInvoiceApplyService orderInvoiceApplyService;

    /**
     * 根据orderId获取订单开票信息
     *
     * @param orderIds
     * @return
     */
    @Override
    public List<OrderInvoiceApplyDTO> getOrderInvoiceApplyByList(List<Long> orderIds) {
        List<OrderInvoiceApplyDTO> result = orderInvoiceApplyService.getOrderInvoiceApplyByList(orderIds);
        return result;
    }



    /**
     * 根据orderId获取所有开票申请
     *
     * @param orderId
     * @return
     */
    @Override
    public List<OrderInvoiceApplyDTO> listByOrderId(Long orderId) {
        return orderInvoiceApplyService.listByOrderId(orderId);
    }


    /**
     * 保存或新增发票申请
     *
     * @param orderInvoiceApply
     * @return
     */
    @Override
    public OrderInvoiceApplyDTO saveOrUpdateById(UpdateOrderInvoiceApplyRequest orderInvoiceApply) {
        OrderInvoiceApplyDO result = PojoUtils.map(orderInvoiceApply, OrderInvoiceApplyDO.class);
        orderInvoiceApplyService.save(result);
        return PojoUtils.map(result,OrderInvoiceApplyDTO.class);
    }

    /**
     * 获取申请信息
     *
     * @param id
     * @return
     */
    @Override
    public OrderInvoiceApplyDTO getOneById(Long id) {
        OrderInvoiceApplyDO applyDTO = orderInvoiceApplyService.getById(id);
        return PojoUtils.map(applyDTO,OrderInvoiceApplyDTO.class);
    }


}
