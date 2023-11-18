package com.yiling.order.order.api.impl;

import java.util.Date;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.api.OrderInvoiceDeliveryGroupApi;
import com.yiling.order.order.dto.OrderInvoiceDeliveryGroupDTO;
import com.yiling.order.order.dto.request.SaveOrderInvoiceDeliveryGroupRequest;
import com.yiling.order.order.entity.OrderInvoiceDeliveryGroupDO;
import com.yiling.order.order.service.OrderInvoiceDeliveryGroupService;

/**
 * 出库单关联开票分组信息
 * @author:wei.wang
 * @date:2021/10/8
 */
@DubboService
public class OrderInvoiceDeliveryGroupApiImpl implements OrderInvoiceDeliveryGroupApi {

    @Autowired
    private OrderInvoiceDeliveryGroupService orderInvoiceDeliveryGroupService;



    @Override
    public Boolean saveOne(SaveOrderInvoiceDeliveryGroupRequest request) {
        OrderInvoiceDeliveryGroupDO entityList = PojoUtils.map(request, OrderInvoiceDeliveryGroupDO.class);
        return orderInvoiceDeliveryGroupService.save(entityList);
    }

    /**
     * 根据申请信息获取
     *
     * @param applyId
     * @return
     */
    @Override
    public List<OrderInvoiceDeliveryGroupDTO> listByApplyId(Long applyId) {
        QueryWrapper<OrderInvoiceDeliveryGroupDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderInvoiceDeliveryGroupDO :: getApplyId,applyId);
        List<OrderInvoiceDeliveryGroupDO> list = orderInvoiceDeliveryGroupService.list(wrapper);
        return PojoUtils.map(list,OrderInvoiceDeliveryGroupDTO.class);
    }
}
