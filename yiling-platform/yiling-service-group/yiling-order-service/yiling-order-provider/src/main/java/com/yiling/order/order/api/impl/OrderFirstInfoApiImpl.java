package com.yiling.order.order.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.api.OrderFirstInfoApi;
import com.yiling.order.order.dto.OrderFirstInfoDTO;
import com.yiling.order.order.entity.OrderFirstInfoDO;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.order.order.service.OrderFirstInfoService;

import cn.hutool.core.util.ObjectUtil;

/** 用户首单信息api
 * @author zhigang.guo
 * @date: 2022/5/30
 */
@DubboService
public class OrderFirstInfoApiImpl implements OrderFirstInfoApi {

    @Autowired
    private OrderFirstInfoService orderFirstInfoService;


    @Override
    public Boolean checkNewVisitor(Long buyerEid, OrderTypeEnum orderTypeEnum) {
        OrderFirstInfoDO orderFirstInfoDO = orderFirstInfoService.queryOrderFirstInfo(buyerEid,orderTypeEnum.getCode());
        if (ObjectUtil.isNull(orderFirstInfoDO)) {
            return true;
        }
        return false;
    }

    @Override
    public OrderFirstInfoDTO queryOrderFirstInfo(Long buyerEid, OrderTypeEnum orderTypeEnum) {
        OrderFirstInfoDO orderFirstInfoDO = orderFirstInfoService.queryOrderFirstInfo(buyerEid, orderTypeEnum.getCode());
        return PojoUtils.map(orderFirstInfoDO, OrderFirstInfoDTO.class);
    }


    @Override
    public Boolean saveFirstInfo(Long orderId, Long opUserId) {

        return orderFirstInfoService.saveFirstInfo(orderId,opUserId);
    }
}
