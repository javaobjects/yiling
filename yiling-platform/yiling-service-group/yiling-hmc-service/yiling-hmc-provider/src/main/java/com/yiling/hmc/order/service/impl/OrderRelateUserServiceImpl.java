package com.yiling.hmc.order.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.order.dao.OrderRelateUserMapper;
import com.yiling.hmc.order.dto.OrderRelateUserDTO;
import com.yiling.hmc.order.entity.OrderRelateUserDO;
import com.yiling.hmc.order.enums.HmcOrderRelateUserTypeEnum;
import com.yiling.hmc.order.service.OrderRelateUserService;
import com.yiling.hmc.wechat.dto.request.SaveOrderRelateUserRequest;

/**
 * <p>
 * 订单相关人表 服务实现类
 * </p>
 *
 * @author fan.shen
 * @date 2022-04-27
 */
@Service
public class OrderRelateUserServiceImpl extends BaseServiceImpl<OrderRelateUserMapper, OrderRelateUserDO> implements OrderRelateUserService {

    @Override
    public Long add(SaveOrderRelateUserRequest request) {
        OrderRelateUserDO relateUserDO = PojoUtils.map(request, OrderRelateUserDO.class);
        relateUserDO.setType(request.getUserTypeEnum().getCode());
        this.save(relateUserDO);
        return relateUserDO.getId();
    }

    @Override
    public OrderRelateUserDTO queryByOrderIdAndRelateType(Long orderId, HmcOrderRelateUserTypeEnum userTypeEnum) {
        QueryWrapper<OrderRelateUserDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderRelateUserDO::getOrderId, orderId);
        wrapper.lambda().eq(OrderRelateUserDO::getType, userTypeEnum.getCode());
        OrderRelateUserDO userDO = this.getOne(wrapper);
        return PojoUtils.map(userDO, OrderRelateUserDTO.class);
    }
}
