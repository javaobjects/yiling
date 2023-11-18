package com.yiling.order.order.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.order.order.dao.OrderInvoiceDeliveryGroupMapper;
import com.yiling.order.order.entity.OrderInvoiceDeliveryGroupDO;
import com.yiling.order.order.service.OrderInvoiceDeliveryGroupService;

/**
 * <p>
 * 出库单关联开票分组信息 服务实现类
 * </p>
 *
 * @author wei.wang
 * @date 2021-10-08
 */
@Service
public class OrderInvoiceDeliveryGroupServiceImpl extends BaseServiceImpl<OrderInvoiceDeliveryGroupMapper, OrderInvoiceDeliveryGroupDO> implements OrderInvoiceDeliveryGroupService {

}
