package com.yiling.order.order.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.api.OrderInvoiceDetailApi;
import com.yiling.order.order.dto.OrderInvoiceDetailDTO;
import com.yiling.order.order.dto.request.SaveOrderInvoiceDetailRequest;
import com.yiling.order.order.entity.OrderInvoiceDetailDO;
import com.yiling.order.order.service.OrderInvoiceDetailService;

/**
 * 订单开票明细Api
 * @author:wei.wang
 * @date:2021/8/4
 */
@DubboService
public class OrderInvoiceDetailApiImpl implements OrderInvoiceDetailApi {
    @Autowired
    private OrderInvoiceDetailService orderInvoiceDetailService;



    /**
     * 保存申请开票的时候提交申请明细信息
     *
     * @param list
     * @return
     */
    @Override
    public Boolean saveBatchDate(List<SaveOrderInvoiceDetailRequest> list) {
        List<OrderInvoiceDetailDO> result = PojoUtils.map(list, OrderInvoiceDetailDO.class);
        return orderInvoiceDetailService.saveBatch(result);
    }

    /**
     * 根据订单ids获取开票申请明细信息
     *
     * @param orders
     * @return
     */
    @Override
    public List<OrderInvoiceDetailDTO> listByOrderIds(List<Long> orders) {
        List<OrderInvoiceDetailDO> list = orderInvoiceDetailService.listByOrderIds(orders);
        return PojoUtils.map(list,OrderInvoiceDetailDTO.class);
    }

    /**
     * 根据组编号获取信息
     *
     * @param groupNoList 组编号
     * @return
     */
    @Override
    public List<OrderInvoiceDetailDTO> listByGroupNoList(List<String> groupNoList) {
        QueryWrapper<OrderInvoiceDetailDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(OrderInvoiceDetailDO :: getGroupNo,groupNoList);
        List<OrderInvoiceDetailDO> list = orderInvoiceDetailService.list(wrapper);
        return PojoUtils.map(list,OrderInvoiceDetailDTO.class);
    }
}
