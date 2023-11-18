package com.yiling.hmc.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.order.dto.OrderPrescriptionGoodsDTO;
import com.yiling.hmc.wechat.dto.request.OrderPrescriptionGoodsRequest;
import org.springframework.stereotype.Service;

import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.hmc.order.dao.OrderPrescriptionGoodsMapper;
import com.yiling.hmc.order.entity.OrderPrescriptionGoodsDO;
import com.yiling.hmc.order.service.OrderPrescriptionGoodsService;

import java.util.List;

/**
 * <p>
 * C端兑付订单关联处方商品表 服务实现类
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-25
 */
@Service
public class OrderPrescriptionGoodsServiceImpl extends BaseServiceImpl<OrderPrescriptionGoodsMapper, OrderPrescriptionGoodsDO> implements OrderPrescriptionGoodsService {

    @Override
    public List<OrderPrescriptionGoodsDTO> getByOrderId(Long orderId) {
        QueryWrapper<OrderPrescriptionGoodsDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderPrescriptionGoodsDO::getOrderId, orderId);
        List<OrderPrescriptionGoodsDO> list = this.list(wrapper);
        return PojoUtils.map(list, OrderPrescriptionGoodsDTO.class);
    }
}
