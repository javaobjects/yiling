package com.yiling.mall.order.mq.message.listener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.common.message.MessageExt;

import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.rocketmq.annotation.RocketMqOrderListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.mq.MessageOrderListener;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;
import com.yiling.mall.order.api.OrderProcessApi;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderDeliveryApi;
import com.yiling.order.order.api.OrderDetailApi;
import com.yiling.order.order.api.OrderDetailChangeApi;
import com.yiling.order.order.api.OrderReturnApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDeliveryDTO;
import com.yiling.order.order.dto.OrderDetailChangeDTO;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.dto.request.OrderDeliveryRequest;
import com.yiling.order.order.dto.request.OrderDetailRequest;
import com.yiling.order.order.dto.request.OrderReturnApplyRequest;
import com.yiling.order.order.enums.OrderReturnTypeEnum;
import com.yiling.order.order.enums.OrderTypeEnum;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: yong.zhang
 * @date: 2021/8/14
 */
@Slf4j
@RocketMqOrderListener(topic = Constants.TOPIC_ORDER_RETURN, consumerGroup = Constants.TOPIC_ORDER_RETURN)
public class OrderReturnMessageListener implements MessageOrderListener {

    @DubboReference
    OrderApi orderApi;
    @DubboReference
    OrderProcessApi orderProcessApi;
    @DubboReference
    OrderDetailChangeApi orderDetailChangeApi;
    @DubboReference
    OrderReturnApi orderReturnApi;
    @DubboReference
    OrderDetailApi orderDetailApi;
    @DubboReference
    OrderDeliveryApi orderDeliveryApi;

    @MdcLog
    @Override
    public MqAction consume(MessageExt message, ConsumeOrderlyContext context) {
        String orderNo = null;
        try {
            orderNo = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
            log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), orderNo);

            if (StrUtil.isEmpty(orderNo)) {
                log.error("订单号为空");
                return MqAction.CommitMessage;
            }
            String tags = message.getTags();
            boolean isSuccess = false;
            if (Constants.TAG_ORDER_DELIVERED.equals(tags)) {
                // 发货时，供应商退货单创建
                isSuccess = orderDelivered(orderNo);
            } else if (Constants.TAG_ORDER_RECEIVED.equals(tags)) {
                // 收货时，破损退货单创建
                isSuccess = orderReceived(orderNo);
            } else if (Constants.TAG_ORDER_MODIFY_AUDIT.equals(tags)) {
                // 反审核时，退货单的重新创建
                isSuccess = orderModifyAudit(orderNo);
            }
            if (!isSuccess) {
                log.error("订单信息不存在：{}", orderNo);
                return MqAction.CommitMessage;
            }
        } catch (BusinessException e) {

            log.error("[{}], BusinessException Msg:{}", orderNo, e.getMessage());
            return MqAction.CommitMessage;

        } catch (Exception e) {
            log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 异常信息:{}", message.getMsgId(), message.getTopic(), message.getTags(), orderNo, e);
            throw e;
        }
        return MqAction.CommitMessage;
    }

    /**
     * 发货创建供应商退货单
     *
     * @param orderNo
     * @return
     */
    private boolean orderDelivered(String orderNo) {
        // 供应商退货单流程
        // 通过订单id获取数据库数据
        // 采购商收获的时候判断是否需要生成采购商退货单
        int count = orderReturnApi.countByOrderNoAndType(orderNo, OrderReturnTypeEnum.SELLER_RETURN_ORDER.getCode());
        // 防止重复消费的问题
        if (count > 0) {
            log.error("此订单供应商已经存在，存在重复消费：{}", orderNo);
            return false;
        }
        OrderDTO orderDTO = orderApi.selectByOrderNo(orderNo);
        if (orderDTO == null) {
            log.error("订单信息不存在：{}", orderNo);
            return false;
        }
        // 如果发货数量和购买数量数量不一致，才需要生成供应商退货单
        List<OrderDetailChangeDTO> orderDetailChangeDTOList = orderDetailChangeApi.listByOrderId(orderDTO.getId());
        if (OrderTypeEnum.B2B == OrderTypeEnum.getByCode(orderDTO.getOrderType())) {
            return orderB2BDelivered(orderDTO, orderDetailChangeDTOList);
        } else {
            return orderPOPDelivered(orderDTO, orderDetailChangeDTOList);
        }
    }

    /**
     * POP发货退货单申请
     *
     * @param orderDTO
     * @param orderDetailChangeDTOList
     * @return
     */
    private boolean orderPOPDelivered(OrderDTO orderDTO, List<OrderDetailChangeDTO> orderDetailChangeDTOList) {
        List<OrderDetailRequest> orderDetailList = new ArrayList<>();
        for (OrderDetailChangeDTO orderDetailChangeDTO : orderDetailChangeDTOList) {
            //当购买数量大于发货数量，则需要生成退货单
            if (orderDetailChangeDTO.getSellerReturnQuantity() > 0) {
                OrderDetailRequest orderDetailRequest = new OrderDetailRequest();
                orderDetailRequest.setDetailId(orderDetailChangeDTO.getDetailId());
                orderDetailRequest.setGoodsId(orderDetailChangeDTO.getGoodsId());
                List<OrderDeliveryRequest> orderDeliveryList = new ArrayList<>();
                OrderDeliveryRequest orderDeliveryRequest = new OrderDeliveryRequest();
                // 供应商发货退货单没有批次号
                orderDeliveryRequest.setBatchNo("");
                orderDeliveryRequest.setReturnQuantity(orderDetailChangeDTO.getSellerReturnQuantity());
                orderDeliveryList.add(orderDeliveryRequest);
                orderDetailRequest.setOrderDeliveryList(orderDeliveryList);
                orderDetailList.add(orderDetailRequest);
            }
        }
        if (orderDetailList.size() > 0) {
            OrderReturnApplyRequest orderReturnApplyRequest = new OrderReturnApplyRequest();
            orderReturnApplyRequest.setOrderId(orderDTO.getId());
            orderReturnApplyRequest.setReturnType(OrderReturnTypeEnum.SELLER_RETURN_ORDER.getCode());
            orderReturnApplyRequest.setRemark("");
            orderReturnApplyRequest.setOpUserId(0L);
            orderReturnApplyRequest.setOrderDetailList(orderDetailList);
            return orderProcessApi.supplierApplyOrderReturn(orderReturnApplyRequest, orderDTO);
        } else {
            log.info("供应商发货数量已经充足，不需要生成退货单");
        }
        return true;
    }

    /**
     * B2B发货退货单申请
     *
     * @param orderDTO
     * @param orderDetailChangeDTOList
     * @return
     */
    private boolean orderB2BDelivered(OrderDTO orderDTO, List<OrderDetailChangeDTO> orderDetailChangeDTOList) {
        List<OrderDetailRequest> detailApplyRequestList = new ArrayList<>();
        for (OrderDetailChangeDTO orderDetailChangeDTO : orderDetailChangeDTOList) {
            //当购买数量大于发货数量，则需要生成退货单
            if (orderDetailChangeDTO.getSellerReturnQuantity() > 0) {
                OrderDetailRequest detailApplyRequest = new OrderDetailRequest();
                detailApplyRequest.setDetailId(orderDetailChangeDTO.getDetailId());
                detailApplyRequest.setGoodsId(orderDetailChangeDTO.getGoodsId());
                detailApplyRequest.setGoodsSkuId(orderDetailChangeDTO.getGoodsSkuId());
                List<OrderDeliveryRequest> orderReturnDetailBatchList = new ArrayList<>();
                OrderDeliveryRequest batchApplyRequest = new OrderDeliveryRequest();
                batchApplyRequest.setBatchNo("");
                batchApplyRequest.setReturnQuantity(orderDetailChangeDTO.getSellerReturnQuantity());
                orderReturnDetailBatchList.add(batchApplyRequest);
                detailApplyRequest.setOrderDeliveryList(orderReturnDetailBatchList);
                detailApplyRequestList.add(detailApplyRequest);
            }
        }

        if (detailApplyRequestList.size() > 0) {
            OrderReturnApplyRequest request = new OrderReturnApplyRequest();
            request.setOrderId(orderDTO.getId());
            request.setReturnType(OrderReturnTypeEnum.SELLER_RETURN_ORDER.getCode());
            request.setOrderDetailList(detailApplyRequestList);
            request.setRemark("");
            request.setOpUserId(orderDTO.getContacterId());
            request.setOpTime(new Date());
            log.info("B2B供应商发货时的退货，请求数据为:[{}]", request);
            return orderProcessApi.deliverOrderReturn(request);
        } else {
            log.info("供应商发货数量已经充足，不需要生成退货单");
        }
        return true;
    }

    /**
     * 收获创建破损退货单
     *
     * @param orderNo
     * @return
     */
    private boolean orderReceived(String orderNo) {
        // 破损退货单流程
        // 通过订单id获取数据库数据
        // 采购商收获的时候判断是否需要生成采购商退货单
        int count = orderReturnApi.countByOrderNoAndType(orderNo, OrderReturnTypeEnum.DAMAGE_RETURN_ORDER.getCode());
        // 防止重复消费的问题
        if (count > 0) {
            log.error("此订单退货单已经存在，存在重复消费：{}", orderNo);
            return false;
        }
        OrderDTO orderDTO = orderApi.selectByOrderNo(orderNo);
        if (orderDTO == null) {
            log.error("订单信息不存在：{}", orderNo);
            return false;
        }
        // 如果发货数量和收货数量不一致，才需要生成破损退货单
        List<OrderDetailRequest> orderDetailList = new ArrayList<>();
        List<OrderDetailDTO> orderDetailDTOList = orderDetailApi.getOrderDetailInfo(orderDTO.getId());
        boolean isReturn = false;
        for (OrderDetailDTO orderDetailDTO : orderDetailDTOList) {
            OrderDetailRequest orderDetailRequest = new OrderDetailRequest();
            orderDetailRequest.setDetailId(orderDetailDTO.getId());
            orderDetailRequest.setGoodsId(orderDetailDTO.getGoodsId());
            List<OrderDeliveryDTO> orderDeliveryDTOList = orderDeliveryApi.getOrderDeliveryList(orderDTO.getId(), orderDetailDTO.getId());
            List<OrderDeliveryRequest> orderDeliveryList = new ArrayList<>();
            for (OrderDeliveryDTO orderDeliveryDTO : orderDeliveryDTOList) {
                if (orderDeliveryDTO.getDeliveryQuantity() > orderDeliveryDTO.getReceiveQuantity()) {
                    OrderDeliveryRequest orderDeliveryRequest = new OrderDeliveryRequest();
                    //                    orderDeliveryRequest.setOrderReturnDetailId()
                    orderDeliveryRequest.setBatchNo(orderDeliveryDTO.getBatchNo());
                    orderDeliveryRequest.setReturnQuantity(orderDeliveryDTO.getDeliveryQuantity() - orderDeliveryDTO.getReceiveQuantity());
                    orderDeliveryList.add(orderDeliveryRequest);
                    isReturn = true;
                }
            }
            orderDetailRequest.setOrderDeliveryList(orderDeliveryList);
            orderDetailList.add(orderDetailRequest);
        }
        if (isReturn) {
            OrderReturnApplyRequest orderReturnApplyRequest = new OrderReturnApplyRequest();
            orderReturnApplyRequest.setOrderId(orderDTO.getId());
            orderReturnApplyRequest.setReturnType(OrderReturnTypeEnum.DAMAGE_RETURN_ORDER.getCode());
            //            orderReturnApplyRequest.setOrderReturnId();
            //            orderReturnApplyRequest.setCurrentEid()
            orderReturnApplyRequest.setRemark("");
            orderReturnApplyRequest.setOrderDetailList(orderDetailList);
            log.info("创建破损退货单,请求数据为:[{}]", orderReturnApplyRequest);
            orderProcessApi.damageOrderReturn(orderReturnApplyRequest, 0, orderDTO);
        } else {
            log.info("供应商发货数量已经充足，不需要生成退货单");
        }
        return true;
    }

    /**
     * 反审核，删除退货单后创建新的退货单
     *
     * @param orderNo
     * @return
     */
    private boolean orderModifyAudit(String orderNo) {
        return orderReturnApi.insertReturnOrderForModifyAudit(orderNo);
    }
}
