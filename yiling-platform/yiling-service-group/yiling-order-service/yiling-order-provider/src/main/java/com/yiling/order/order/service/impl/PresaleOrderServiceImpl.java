package com.yiling.order.order.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.order.order.dao.OrderMapper;
import com.yiling.order.order.dao.PresaleOrderMapper;
import com.yiling.order.order.dto.request.UpdatePresaleOrderRequest;
import com.yiling.order.order.entity.OrderDO;
import com.yiling.order.order.entity.PresaleOrderDO;
import com.yiling.order.order.enums.OrderStatusEnum;
import com.yiling.order.order.enums.PaymentStatusEnum;
import com.yiling.order.order.service.PresaleOrderService;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;

/**
 * <p>
 * 预售订单表 服务实现类
 * </p>
 *
 * @author zhigang.guo
 * @date 2022-10-09
 */
@Service
public class PresaleOrderServiceImpl extends BaseServiceImpl<PresaleOrderMapper, PresaleOrderDO> implements PresaleOrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public PresaleOrderDO getOrderInfo(Long orderId) {

        QueryWrapper<PresaleOrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PresaleOrderDO :: getOrderId,orderId);
        wrapper.last("limit 1");

        return this.baseMapper.selectOne(wrapper);
    }

    @Override
    public List<String> selectNeedSendBalanceSmsOrders(Integer hours) {

        QueryWrapper<PresaleOrderDO> wrapper = new QueryWrapper<>();
        // 已付定金
        wrapper.lambda().eq(PresaleOrderDO :: getIsPayDeposit,1);
        // 未付余款
        wrapper.lambda().eq(PresaleOrderDO :: getIsPayBalance,0);
        // 未发送短信
        wrapper.lambda().eq(PresaleOrderDO :: getHasSendCancelSms,0);
        // 付余款的时间小于当前时间
        wrapper.lambda().le(PresaleOrderDO::getBalanceStartTime,new Date());
        wrapper.lambda().le(PresaleOrderDO::getBalanceEndTime, DateUtil.offsetHour(new Date(),hours));

        List<PresaleOrderDO> presaleOrderDoList = this.baseMapper.selectList(wrapper);

        if (CollectionUtil.isEmpty(presaleOrderDoList)) {

            return Collections.emptyList();
        }

        // 预售订单尾款支付时间大于24小时，短信提醒
        List<String> orderNoList = presaleOrderDoList.stream()
                .map(t -> t.getOrderNo()).collect(Collectors.toList());

        return orderNoList;
    }


    @Override
    public List<Long> selectTimeOutNotPayBalanceOrder() {

        QueryWrapper<PresaleOrderDO> wrapper = new QueryWrapper<>();
        // 已付定金
        wrapper.lambda().eq(PresaleOrderDO :: getIsPayDeposit,1);
        // 未付余款
        wrapper.lambda().eq(PresaleOrderDO :: getIsPayBalance,0);
        // 付余款的时间小于当前时间
        wrapper.lambda().lt(PresaleOrderDO::getBalanceEndTime,new Date());

        List<PresaleOrderDO> presaleOrderDoList = this.baseMapper.selectList(wrapper);

        if (CollectionUtil.isEmpty(presaleOrderDoList)) {

            return Collections.emptyList();
        }

        List<Long> orderIdList = presaleOrderDoList.stream().map(t -> t.getOrderId()).collect(Collectors.toList());
        QueryWrapper<OrderDO> orderWrapper = new QueryWrapper<>();
        orderWrapper.lambda().in(OrderDO::getId,orderIdList);
        // 待审核
        orderWrapper.lambda().eq(OrderDO::getOrderStatus, OrderStatusEnum.UNAUDITED.getCode());
        // 部分支付
        orderWrapper.lambda().eq(OrderDO::getPaymentStatus, PaymentStatusEnum.PARTPAID.getCode());

        List<OrderDO>  orderDOList = orderMapper.selectList(orderWrapper);

        if (CollectionUtil.isEmpty(orderDOList)) {

            return Collections.emptyList();
        }

        return orderDOList.stream().map(t -> t.getId()).collect(Collectors.toList());

    }


    @Override
    public boolean updatePresalOrderByOrderId(UpdatePresaleOrderRequest request) {

        QueryWrapper<PresaleOrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PresaleOrderDO :: getOrderId,request.getOrderId());

        PresaleOrderDO presaleOrderDO = new PresaleOrderDO();

        if (request.getIsPayBalance() != null) {
            presaleOrderDO.setIsPayBalance(request.getIsPayBalance());
        }

        if (request.getIsPayDeposit() != null) {
            presaleOrderDO.setIsPayDeposit(request.getIsPayDeposit());
        }

        if (request.getHasSendCancelSms() != null) {
            presaleOrderDO.setHasSendCancelSms(request.getHasSendCancelSms());
        }

        if (request.getHasSendPaySms() != null) {
            presaleOrderDO.setHasSendPaySms(request.getHasSendPaySms());
        }

        return this.update(presaleOrderDO,wrapper);
    }


    @Override
    public List<PresaleOrderDO> listByOrderIds(List<Long> orderIdList) {

        QueryWrapper<PresaleOrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(PresaleOrderDO :: getOrderId,orderIdList);

        return this.list(wrapper);
    }

    @Override
    public List<PresaleOrderDO> listByOrderNos(List<String> orderNoList) {
        QueryWrapper<PresaleOrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(PresaleOrderDO :: getOrderNo,orderNoList);

        return this.list(wrapper);
    }


    @Override
    public List<String> selectNeedPayBalanceSmsOrders() {
        QueryWrapper<PresaleOrderDO> wrapper = new QueryWrapper<>();
        // 定金预售活动
        wrapper.lambda().eq(PresaleOrderDO :: getActivityType,1);
        // 已付定金
        wrapper.lambda().eq(PresaleOrderDO :: getIsPayDeposit,1);
        // 未付余款
        wrapper.lambda().eq(PresaleOrderDO :: getIsPayBalance,0);
        wrapper.lambda().eq(PresaleOrderDO :: getHasSendPaySms,0);

        // 付余款的时间小于当前时间
        wrapper.lambda().lt(PresaleOrderDO::getBalanceStartTime,new Date());

        List<PresaleOrderDO> presaleOrderDoList = this.baseMapper.selectList(wrapper);

        if (CollectionUtil.isEmpty(presaleOrderDoList)) {

            return Collections.emptyList();
        }
        return presaleOrderDoList.stream().map(t -> t.getOrderNo()).collect(Collectors.toList());
    }
}
