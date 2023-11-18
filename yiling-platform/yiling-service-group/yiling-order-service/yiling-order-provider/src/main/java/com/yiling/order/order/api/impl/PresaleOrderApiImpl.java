package com.yiling.order.order.api.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.api.PresaleOrderApi;
import com.yiling.order.order.dto.PresaleOrderDTO;
import com.yiling.order.order.dto.request.UpdatePresaleOrderRequest;
import com.yiling.order.order.entity.OrderDO;
import com.yiling.order.order.entity.PresaleOrderDO;
import com.yiling.order.order.service.OrderService;
import com.yiling.order.order.service.PresaleOrderService;

import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 预售订单扩展信息
 *
 * @author zhigang.guo
 * @date: 2022/10/10
 */
@DubboService
@Slf4j
public class PresaleOrderApiImpl implements PresaleOrderApi {

    @Autowired
    private PresaleOrderService presaleOrderService;
    @Autowired
    private OrderService orderService;


    @Override
    public PresaleOrderDTO getOrderInfo(Long orderId) {

        // 查询预售活动扩展信息
        PresaleOrderDO presaleOrderDO = presaleOrderService.getOrderInfo(orderId);

        if (presaleOrderDO == null) {

            return null;
        }

        OrderDO orderInfo = orderService.getById(orderId);

        if (orderInfo == null) {

            return PojoUtils.map(presaleOrderDO, PresaleOrderDTO.class);
        }

        return buildPresaleOrderDto(orderInfo, presaleOrderDO);
    }


    /**
     * 构建预售订单数据
     *
     * @param orderInfo
     * @param presaleOrderDO
     * @return
     */
    public PresaleOrderDTO buildPresaleOrderDto(OrderDO orderInfo, PresaleOrderDO presaleOrderDO) {

        PresaleOrderDTO presaleOrderDTO = PojoUtils.map(orderInfo, PresaleOrderDTO.class);

        presaleOrderDTO.setActivityType(presaleOrderDO.getActivityType());
        presaleOrderDTO.setIsPayDeposit(presaleOrderDO.getIsPayDeposit());
        presaleOrderDTO.setIsPayBalance(presaleOrderDO.getIsPayBalance());
        presaleOrderDTO.setDepositAmount(presaleOrderDO.getDepositAmount());
        presaleOrderDTO.setBalanceAmount(presaleOrderDO.getBalanceAmount());
        presaleOrderDTO.setBalanceStartTime(presaleOrderDO.getBalanceStartTime());
        presaleOrderDTO.setBalanceEndTime(presaleOrderDO.getBalanceEndTime());
        presaleOrderDTO.setOrderId(presaleOrderDO.getOrderId());

        return presaleOrderDTO;
    }


    @Override
    public List<String> selectNeedSendBalanceSmsOrders(Integer hour) {


        return presaleOrderService.selectNeedSendBalanceSmsOrders(hour);
    }


    @Override
    public List<String> selectNeedPayBalanceSmsOrders() {


        return presaleOrderService.selectNeedPayBalanceSmsOrders();
    }

    @Override
    public List<Long> selectTimeOutNotPayBalanceOrder() {

        return presaleOrderService.selectTimeOutNotPayBalanceOrder();
    }


    @Override
    public boolean updatePresalOrderByOrderId(UpdatePresaleOrderRequest request) {

        return presaleOrderService.updatePresalOrderByOrderId(request);
    }


    @Override
    public List<PresaleOrderDTO> listByOrderIds(List<Long> orderIdList) {

        List<PresaleOrderDO> preSaleOrderDOS = presaleOrderService.listByOrderIds(orderIdList);

        if (CollectionUtil.isEmpty(preSaleOrderDOS)) {

            return Collections.emptyList();
        }


        List<OrderDO> orderDOList = orderService.listByIds(orderIdList);

        if (CollectionUtil.isEmpty(orderDOList)) {

            return Collections.emptyList();
        }

        Map<Long, OrderDO> orderResultMap = orderDOList.stream().collect(Collectors.toMap(OrderDO::getId, Function.identity()));

        return preSaleOrderDOS.stream().map(t -> buildPresaleOrderDto(orderResultMap.getOrDefault(t.getOrderId(), new OrderDO()), t)).collect(Collectors.toList());
    }


    @Override
    public List<PresaleOrderDTO> listByOrderNos(List<String> orderNoList) {

        List<OrderDO> orderDOList = orderService.listByOrderNos(orderNoList);

        if (CollectionUtil.isEmpty(orderDOList)) {

            return Collections.emptyList();
        }

        List<PresaleOrderDO> preSaleOrderDOS = presaleOrderService.listByOrderNos(orderNoList);

        if (CollectionUtil.isEmpty(preSaleOrderDOS)) {

            return PojoUtils.map(orderDOList, PresaleOrderDTO.class);
        }

        Map<Long, OrderDO> orderResultMap = orderDOList.stream().collect(Collectors.toMap(OrderDO::getId, Function.identity()));

        return preSaleOrderDOS.stream().map(t -> buildPresaleOrderDto(orderResultMap.getOrDefault(t.getOrderId(), new OrderDO()), t)).collect(Collectors.toList());
    }

}
