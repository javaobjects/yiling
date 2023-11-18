package com.yiling.hmc.order.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.order.dao.OrderPrescriptionMapper;
import com.yiling.hmc.order.dto.OrderPrescriptionDTO;
import com.yiling.hmc.order.entity.OrderPrescriptionDO;
import com.yiling.hmc.order.service.OrderPrescriptionService;

/**
 * <p>
 * C端兑付订单关联处方表 服务实现类
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-25
 */
@Service
public class OrderPrescriptionServiceImpl extends BaseServiceImpl<OrderPrescriptionMapper, OrderPrescriptionDO> implements OrderPrescriptionService {

    @Override
    public OrderPrescriptionDTO getByOrderId(Long orderId) {
        QueryWrapper<OrderPrescriptionDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderPrescriptionDO::getOrderId, orderId);
        OrderPrescriptionDO prescriptionDO = this.getOne(wrapper);
        return PojoUtils.map(prescriptionDO, OrderPrescriptionDTO.class);
    }
}
