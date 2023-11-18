package com.yiling.order.order.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.order.order.dao.OrderReturnDetailErpMapper;
import com.yiling.order.order.entity.OrderReturnDetailErpDO;
import com.yiling.order.order.service.OrderReturnDetailErpService;

/**
 * <p>
 * 退货单明细（erp出库单维度） 服务实现类
 * </p>
 *
 * @author yong.zhang
 * @date 2021-09-23
 */
@Service
public class OrderReturnDetailErpServiceImpl extends BaseServiceImpl<OrderReturnDetailErpMapper, OrderReturnDetailErpDO> implements OrderReturnDetailErpService {

    @Override
    public Integer sumReturnQualityByErpDeliveryNo(Long detailId, String batchNo, String erpDeliveryNo,String erpSendOrderId) {
        return this.getBaseMapper().sumReturnQualityByErpDeliveryNo(detailId, batchNo, erpDeliveryNo,erpSendOrderId);
    }

    @Override
    public List<OrderReturnDetailErpDO> listByReturnIds(List<Long> returnIds) {
        QueryWrapper<OrderReturnDetailErpDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(OrderReturnDetailErpDO::getReturnId, returnIds);
        return this.list(wrapper);
    }
}
