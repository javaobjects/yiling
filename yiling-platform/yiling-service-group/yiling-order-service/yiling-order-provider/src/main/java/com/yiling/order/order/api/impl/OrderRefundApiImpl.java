package com.yiling.order.order.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.api.OrderRefundApi;
import com.yiling.order.order.dto.OrderRefundDTO;
import com.yiling.order.order.dto.PageOrderRefundDTO;
import com.yiling.order.order.dto.request.OrderRefundStatusRequest;
import com.yiling.order.order.dto.request.RefundFinishRequest;
import com.yiling.order.order.dto.request.RefundPageRequest;
import com.yiling.order.order.dto.request.RefundQueryRequest;
import com.yiling.order.order.entity.OrderRefundDO;
import com.yiling.order.order.service.OrderRefundService;

import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 订单退款api
 * @author: yong.zhang
 * @date: 2021/10/27
 */
@DubboService
@Slf4j
public class OrderRefundApiImpl implements OrderRefundApi {

    @Autowired
    OrderRefundService orderRefundService;
    @DubboReference
    MqMessageSendApi   mqMessageSendApi;

    @Override
    public OrderRefundDTO queryById(Long id) {
        OrderRefundDO orderRefundDO = orderRefundService.getById(id);
        return PojoUtils.map(orderRefundDO, OrderRefundDTO.class);
    }

    @Override
    public OrderRefundDTO saveOrderRefund(OrderRefundDTO orderRefundDTO) {
        OrderRefundDO orderRefundDO = PojoUtils.map(orderRefundDTO, OrderRefundDO.class);
        orderRefundService.save(orderRefundDO);
        return PojoUtils.map(orderRefundDO, OrderRefundDTO.class);
    }

    @Override
    public List<OrderRefundDTO> batchSaveOrderRefund(List<OrderRefundDTO> orderRefundDTOs) {

        List<OrderRefundDO> orderRefundDOs = PojoUtils.map(orderRefundDTOs, OrderRefundDO.class);

        orderRefundService.saveBatch(orderRefundDOs);

        return PojoUtils.map(orderRefundDOs, OrderRefundDTO.class);
    }

    @Override
    public boolean editStatus(OrderRefundStatusRequest request) {
        return orderRefundService.editStatus(request);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean editStatus(List<OrderRefundStatusRequest> requestList) {
        requestList.forEach(request -> {
            orderRefundService.editStatus(request);
        });

        return true;
    }

    @Override
    public boolean finishRefund(RefundFinishRequest request) {
        return orderRefundService.finishRefund(request);
    }

    @Override
    public Page<PageOrderRefundDTO> pageList(RefundPageRequest request) {
        return orderRefundService.pageList(request);
    }

    @Override
    public List<OrderRefundDTO> listByCondition(RefundQueryRequest request) {
        List<OrderRefundDO> doList = orderRefundService.listByCondition(request);
        return PojoUtils.map(doList, OrderRefundDTO.class);
    }

    @Override
    public List<OrderRefundDTO> listByRefundNos(List<String> refundNos) {

        List<OrderRefundDO> orderRefundDOS = orderRefundService.listByRefundNos(refundNos);

        return PojoUtils.map(orderRefundDOS, OrderRefundDTO.class);
    }


    @Override
    public boolean compensationAutoNotAuditData(Integer count) {
        List<String> refundNoList =  orderRefundService.selectNotAuditOrderRefunds(count);
        if (CollectionUtil.isNotEmpty(refundNoList)) {
            log.info("查询到超时未自动审核退款单据:{}",refundNoList);
            for (String refundNo : refundNoList) {
                MqMessageBO mqMessageBO  = new MqMessageBO(Constants.TOPIC_REFUND_ORDER_AUDIT_NOTIFY, "", refundNo);
                mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
                mqMessageSendApi.send(mqMessageBO);
            }

        }
        return true;
    }
}
