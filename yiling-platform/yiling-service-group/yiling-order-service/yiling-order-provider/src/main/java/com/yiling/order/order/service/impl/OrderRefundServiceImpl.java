package com.yiling.order.order.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.order.order.dao.OrderRefundMapper;
import com.yiling.order.order.dto.PageOrderRefundDTO;
import com.yiling.order.order.dto.request.OrderRefundStatusRequest;
import com.yiling.order.order.dto.request.RefundFinishRequest;
import com.yiling.order.order.dto.request.RefundPageRequest;
import com.yiling.order.order.dto.request.RefundQueryRequest;
import com.yiling.order.order.entity.OrderRefundDO;
import com.yiling.order.order.enums.RefundStatusEnum;
import com.yiling.order.order.service.OrderRefundService;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;

/**
 * <p>
 * 退款表 服务实现类
 * </p>
 *
 * @author yong.zhang
 * @date 2021-10-19
 */
@Service
public class OrderRefundServiceImpl extends BaseServiceImpl<OrderRefundMapper, OrderRefundDO> implements OrderRefundService {

    @Override
    public boolean editStatus(OrderRefundStatusRequest request) {
        OrderRefundDO orderRefundDO = new OrderRefundDO();
        orderRefundDO.setId(request.getRefundId());
        orderRefundDO.setRefundStatus(request.getRefundStatus());
        orderRefundDO.setUpdateUser(request.getOpUserId());
        orderRefundDO.setUpdateTime(request.getOpTime());
        if (null != request.getRealRefundAmount()) {
            orderRefundDO.setRealRefundAmount(request.getRealRefundAmount());
        }
        if (null != request.getOperateUser() && 0L != request.getOperateUser()) {
            orderRefundDO.setOperateUser(request.getOperateUser());
            orderRefundDO.setOperateTime(request.getOperateTime());
        }
        return this.updateById(orderRefundDO);
    }

    @Override
    public boolean finishRefund(RefundFinishRequest request) {
        OrderRefundDO orderRefundDO = this.getById(request.getRefundId());
        if (orderRefundDO == null) {
            return false;
        }
        // 已退款成功，防止消息重复
        if (RefundStatusEnum.REFUNDED  == RefundStatusEnum.getByCode(orderRefundDO.getRefundStatus())) {
            return true;
        }
        OrderRefundDO upOrderRefundDO = new OrderRefundDO();
        upOrderRefundDO.setId(request.getRefundId());
        upOrderRefundDO.setRefundStatus(request.getRefundStatus());
        upOrderRefundDO.setRealRefundAmount(request.getRealRefundAmount());
        upOrderRefundDO.setThirdTradeNo(request.getThirdTradeNo());
        upOrderRefundDO.setThirdFundNo(request.getThirdFundNo());

        if (null != request.getFailReason()) {
            upOrderRefundDO.setFailReason(request.getFailReason());
        }
        upOrderRefundDO.setRefundTime(request.getOpTime());
        upOrderRefundDO.setUpdateUser(request.getOpUserId());
        upOrderRefundDO.setUpdateTime(request.getOpTime());
        return this.updateById(upOrderRefundDO);
    }

    @Override
    public Page<PageOrderRefundDTO> pageList(RefundPageRequest request) {
        if (request.getCreateStartTime() != null) {
            request.setCreateStartTime(DateUtil.beginOfDay(request.getCreateStartTime()));
        }
        if (request.getCreateStopTime() != null) {
            request.setCreateStopTime(DateUtil.endOfDay(request.getCreateStopTime()));
        }
        Page<OrderRefundDO> objectPage = new Page<>(request.getCurrent(), request.getSize());
        return this.getBaseMapper().pageList(objectPage, request);
    }

    @Override
    public List<OrderRefundDO> listByCondition(RefundQueryRequest request) {
        QueryWrapper<OrderRefundDO> wrapper = new QueryWrapper<>();
        if (null != request.getRefundIdList() && request.getRefundIdList().size() > 0) {
            wrapper.lambda().in(OrderRefundDO::getId, request.getRefundIdList());
        }
        if (StringUtils.isNotEmpty(request.getRefundNo())) {
            wrapper.lambda().eq(OrderRefundDO::getRefundNo, request.getRefundNo());
        }
        if (null != request.getOrderId()) {
            wrapper.lambda().eq(OrderRefundDO::getOrderId, request.getOrderId());
        }
        if (StringUtils.isNotEmpty(request.getOrderNo())) {
            wrapper.lambda().eq(OrderRefundDO::getOrderNo, request.getOrderNo());
        }
        if (null != request.getReturnId()) {
            wrapper.lambda().eq(OrderRefundDO::getReturnId, request.getReturnId());
        }
        if (StringUtils.isNotEmpty(request.getReturnNo())) {
            wrapper.lambda().eq(OrderRefundDO::getReturnNo, request.getReturnNo());
        }
        if (null != request.getRefundType()) {
            wrapper.lambda().eq(OrderRefundDO::getRefundType, request.getRefundType());
        }
        if (null != request.getRefundStatus()) {
            wrapper.lambda().eq(OrderRefundDO::getRefundStatus, request.getRefundStatus());
        }
        if (null != request.getRefundSource()) {
            wrapper.lambda().eq(OrderRefundDO::getRefundSource, request.getRefundSource());
        }

        wrapper.lambda().last("limit 100");
        return this.list(wrapper);
    }


    @Override
    public List<OrderRefundDO> listByRefundNos(List<String> refundNos) {

        QueryWrapper<OrderRefundDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(OrderRefundDO::getRefundNo, refundNos);

        return this.list(wrapper);
    }

    @Override
    public List<String> selectNotAuditOrderRefunds(Integer limit) {
        QueryWrapper<OrderRefundDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderRefundDO::getRefundStatus,RefundStatusEnum.UN_REFUND.getCode());
        wrapper.lambda().lt(OrderRefundDO::getCreateTime,DateUtil.offsetMinute(new Date(),-1));

        wrapper.last("limit " + limit);

        List<OrderRefundDO> orderRefundDOS = this.list(wrapper);

        if (CollectionUtil.isEmpty(orderRefundDOS)) {

            return Collections.emptyList();
        }

        return orderRefundDOS.stream().map(t -> t.getRefundNo()).collect(Collectors.toList());
    }
}
