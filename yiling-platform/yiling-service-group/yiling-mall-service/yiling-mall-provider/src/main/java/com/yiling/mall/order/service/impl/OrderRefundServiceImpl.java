package com.yiling.mall.order.service.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.rocketmq.enums.MqDelayLevel;
import com.yiling.mall.order.dto.request.RefundOrderRequest;
import com.yiling.mall.order.dto.request.RefundReTryRequest;
import com.yiling.mall.order.service.OrderRefundService;
import com.yiling.order.order.api.NoApi;
import com.yiling.order.order.api.OrderRefundApi;
import com.yiling.order.order.dto.OrderRefundDTO;
import com.yiling.order.order.dto.request.OrderRefundStatusRequest;
import com.yiling.order.order.dto.request.RefundQueryRequest;
import com.yiling.order.order.enums.OrderErrorCode;
import com.yiling.order.order.enums.RefundStatusEnum;
import com.yiling.payment.pay.api.RefundApi;
import com.yiling.payment.pay.dto.PayOrderDTO;
import com.yiling.payment.pay.dto.request.RefundParamRequest;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;

import static com.yiling.order.order.enums.NoEnum.ORDER_REFUND_NO;

/**
 * 退款操作
 *
 * @author: yong.zhang
 * @date: 2021/11/5
 */
@Slf4j
@Service
public class OrderRefundServiceImpl implements OrderRefundService {

    @DubboReference
    OrderRefundApi orderRefundApi;

    @DubboReference
    NoApi noApi;

    @DubboReference
    RefundApi refundApi;

    @DubboReference
    EnterpriseApi enterpriseApi;

    @DubboReference
    MqMessageSendApi mqMessageSendApi;

    @Value("${spring.profiles.active}")
    private String                     env;

    /**
     * 构建测试环境退款金额(测试环境默认退款都是0.01)
     * @param remainRefundMoney 本次剩余退款金额
     * @param request  请求退款参数
     * @param payOrderDTO 支付交易单据
     * @param enterpriseDTOMap 企业信息
     * @param orderHasRefundMap 订单已申请过的退款信息
     * @return
     */
    private OrderRefundDTO  buildRefundOrderToTest(BigDecimal remainRefundMoney, RefundOrderRequest request,PayOrderDTO payOrderDTO,Map<Long,EnterpriseDTO> enterpriseDTOMap,Map<String,List<OrderRefundDTO>> orderHasRefundMap ) {

        List<OrderRefundDTO> orderRefundDTOS = orderHasRefundMap.get(payOrderDTO.getPayNo());
        // 交易实际支付金额,测试环境以订单发起支付时的货款金额为准
        BigDecimal canRefundMoney =  payOrderDTO.getGoodsAmount();

        if (CollectionUtil.isNotEmpty(orderRefundDTOS)) {

            // 测试环境可以退款金额
            canRefundMoney =  NumberUtil.sub(canRefundMoney,orderRefundDTOS.stream().map(t -> t.getRefundAmount()).reduce(BigDecimal::add).get());

            // 测试环境目前退款都是按照1分钱来退款
            BigDecimal diffRefundAmount = NumberUtil.sub(payOrderDTO.getPayAmount(),NumberUtil.mul(new BigDecimal("0.01"),orderRefundDTOS.size()));

            // 校验是否超出交易的退款金额
            if (CompareUtil.compare(diffRefundAmount,BigDecimal.ZERO) <= 0) {

                return null;
            }
            // 校验是否已经退款完成
            if (CompareUtil.compare(canRefundMoney,BigDecimal.ZERO) <= 0) {

                return null;
            }
        }

        // 是否完成退款总金额
        if (CompareUtil.compare(remainRefundMoney,BigDecimal.ZERO) <= 0 ) {

            return null;
        }

        String refundNo = noApi.gen(ORDER_REFUND_NO);
        OrderRefundDTO orderRefundDTO = PojoUtils.map(request, OrderRefundDTO.class);
        orderRefundDTO.setRefundNo(refundNo);
        orderRefundDTO.setRefundStatus(RefundStatusEnum.UN_REFUND.getCode());
        orderRefundDTO.setRefundTime(new Date());
        orderRefundDTO.setBuyerEid(request.getBuyerEid());
        orderRefundDTO.setSellerEid(request.getSellerEid());
        orderRefundDTO.setPaymentAmount(request.getPaymentAmount());
        orderRefundDTO.setTotalAmount(request.getTotalAmount());
        orderRefundDTO.setPayWay(payOrderDTO.getPayWay());
        orderRefundDTO.setBuyerEname(enterpriseDTOMap.get(request.getBuyerEid()).getName());
        orderRefundDTO.setSellerEname(enterpriseDTOMap.get(request.getSellerEid()).getName());
        orderRefundDTO.setThirdTradeNo(payOrderDTO.getThirdTradeNo());
        orderRefundDTO.setCreateUser(request.getOpUserId());
        orderRefundDTO.setCreateTime(request.getOpTime());
        orderRefundDTO.setUpdateUser(request.getOpUserId());
        orderRefundDTO.setUpdateTime(request.getOpTime());
        orderRefundDTO.setRemark(request.getReason());
        orderRefundDTO.setPayNo(payOrderDTO.getPayNo());
        orderRefundDTO.setTradeType(payOrderDTO.getTradeType());

        if (CompareUtil.compare(canRefundMoney,remainRefundMoney) > 0) {
            orderRefundDTO.setRefundAmount(remainRefundMoney);
        } else {
            orderRefundDTO.setRefundAmount(canRefundMoney);
        }

        return orderRefundDTO;
    }


    /**
     * 构建生产环境退款申请单数据
     * @param remainRefundMoney 本次剩余退款金额
     * @param request  请求退款参数
     * @param payOrderDTO 支付交易单据
     * @param enterpriseDTOMap 企业信息
     * @param orderHasRefundMap 订单已申请过的退款信息
     * @return
     */
    private OrderRefundDTO  buildRefundOrderToPrd(BigDecimal remainRefundMoney, RefundOrderRequest request,PayOrderDTO payOrderDTO,Map<Long,EnterpriseDTO> enterpriseDTOMap,Map<String,List<OrderRefundDTO>> orderHasRefundMap ) {

        List<OrderRefundDTO> orderRefundDTOS = orderHasRefundMap.get(payOrderDTO.getPayNo());
        BigDecimal canRefundMoney = payOrderDTO.getPayAmount();

        if (CollectionUtil.isNotEmpty(orderRefundDTOS)) {
            canRefundMoney = NumberUtil.sub(canRefundMoney,orderRefundDTOS.stream().map(t -> t.getRefundAmount()).reduce(BigDecimal::add).get());
        }

        // 校验是否已经退款完成
        if (CompareUtil.compare(canRefundMoney,BigDecimal.ZERO) <= 0) {

            return null;
        }

        if (CompareUtil.compare(remainRefundMoney,BigDecimal.ZERO) <= 0 || CompareUtil.compare(canRefundMoney,BigDecimal.ZERO) <= 0 ) {

            return null;
        }

        String refundNo = noApi.gen(ORDER_REFUND_NO);
        OrderRefundDTO orderRefundDTO = PojoUtils.map(request, OrderRefundDTO.class);
        orderRefundDTO.setRefundNo(refundNo);
        orderRefundDTO.setRefundStatus(RefundStatusEnum.UN_REFUND.getCode());
        orderRefundDTO.setRefundTime(new Date());
        orderRefundDTO.setBuyerEid(request.getBuyerEid());
        orderRefundDTO.setSellerEid(request.getSellerEid());
        orderRefundDTO.setPaymentAmount(request.getPaymentAmount());
        orderRefundDTO.setTotalAmount(request.getTotalAmount());
        orderRefundDTO.setPayWay(payOrderDTO.getPayWay());
        orderRefundDTO.setBuyerEname(enterpriseDTOMap.get(request.getBuyerEid()).getName());
        orderRefundDTO.setSellerEname(enterpriseDTOMap.get(request.getSellerEid()).getName());
        orderRefundDTO.setThirdTradeNo(payOrderDTO.getThirdTradeNo());
        orderRefundDTO.setCreateUser(request.getOpUserId());
        orderRefundDTO.setCreateTime(request.getOpTime());
        orderRefundDTO.setUpdateUser(request.getOpUserId());
        orderRefundDTO.setUpdateTime(request.getOpTime());
        orderRefundDTO.setRemark(request.getReason());
        orderRefundDTO.setPayNo(payOrderDTO.getPayNo());
        orderRefundDTO.setTradeType(payOrderDTO.getTradeType());

        if (CompareUtil.compare(canRefundMoney,remainRefundMoney) > 0) {
            orderRefundDTO.setRefundAmount(remainRefundMoney);
        } else {
            orderRefundDTO.setRefundAmount(canRefundMoney);
        }

        return orderRefundDTO;
    }



    @Override
    public boolean refundOrder(RefundOrderRequest request) {
        log.info("refundOrder start, request :[{}]", request);
        Assert.notNull(request.getBuyerEid(),"采购商Eid为空!");
        Assert.notNull(request.getSellerEid(),"供应商Eid为空!");

        RefundQueryRequest queryRequest = new RefundQueryRequest();
        queryRequest.setOrderNo(request.getOrderNo());
        queryRequest.setRefundSource(request.getRefundSource());

        List<OrderRefundDTO> orderRefundDTOResultList = orderRefundApi.listByCondition(queryRequest);
        BigDecimal hasRefundMoney = CollectionUtil.isEmpty(orderRefundDTOResultList) ? BigDecimal.ZERO :orderRefundDTOResultList.stream().map(t -> t.getRefundAmount()).reduce(BigDecimal::add).get();

        if (CompareUtil.compare(hasRefundMoney,request.getPaymentAmount()) >= 0 ) {

            log.warn("申请退款失败,退款总额已大于订单金额!");
            throw new BusinessException(OrderErrorCode.REFUND_FAIL);
        }

        Map<String,List<OrderRefundDTO>> orderHasRefundMap = CollectionUtil.isEmpty(orderRefundDTOResultList) ? Collections.emptyMap() : orderRefundDTOResultList.stream().collect(Collectors.groupingBy(OrderRefundDTO::getPayNo));

        // 查询订单企业信息
        List<EnterpriseDTO> allEnterpriseDTOList = enterpriseApi.listByIds(ListUtil.toList(request.getBuyerEid(),request.getSellerEid()));

        if (CollectionUtil.isEmpty(allEnterpriseDTOList)) {
            log.error("采购商以及供应商未查询到,param:{}",request);
            return false;
        }

        RefundParamRequest refundParamRequest = RefundParamRequest.builder()
                .appOrderId(request.getOrderId())
                .refundAmount(request.getRefundAmount())
                .appOrderNo(request.getOrderNo())
                .build();

        // 验证可以退款的交易流水以及退款交易流水单号
        Result<List<PayOrderDTO>> selectCanRefundPayOrderResult = refundApi.selectCanRefundPayOrder(refundParamRequest);

        if (HttpStatus.HTTP_OK != selectCanRefundPayOrderResult.getCode() || CollectionUtil.isEmpty(selectCanRefundPayOrderResult.getData())) {

            log.warn("refundOrder end, result :[{}]", selectCanRefundPayOrderResult);
            throw new BusinessException(OrderErrorCode.REFUND_FAIL);
        }

        Map<Long,EnterpriseDTO> enterpriseDTOMap = allEnterpriseDTOList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, Function.identity()));
        BigDecimal remainRefundMoney = request.getRefundAmount();
        List<OrderRefundDTO> orderRefundDTOList = Lists.newArrayList();

        for (PayOrderDTO payOrderDTO : selectCanRefundPayOrderResult.getData()) {

            OrderRefundDTO orderRefundDTO ;

            if (Constants.DEBUG_ENV_LIST.contains(env)) {

                orderRefundDTO = this.buildRefundOrderToTest(remainRefundMoney, request, payOrderDTO, enterpriseDTOMap, orderHasRefundMap);
            } else {

                orderRefundDTO = this.buildRefundOrderToPrd(remainRefundMoney, request, payOrderDTO, enterpriseDTOMap, orderHasRefundMap);
            }

            if (orderRefundDTO != null) {
                remainRefundMoney = NumberUtil.sub(remainRefundMoney,orderRefundDTO.getRefundAmount());
                orderRefundDTOList.add(orderRefundDTO);
            }
        }

        if (CollectionUtil.isEmpty(orderRefundDTOList)) {

            log.warn("refundOrder end, result :[{}],orderRefundDTOResultList:[{}]", selectCanRefundPayOrderResult,orderRefundDTOResultList);

            throw new BusinessException(OrderErrorCode.REFUND_FAIL);
        }

        orderRefundApi.batchSaveOrderRefund(orderRefundDTOList);

        // 退款申请单据
        String refundNos = StringUtils.join(orderRefundDTOList.stream().map(t -> t.getRefundNo()).collect(Collectors.toList()), ",");

        // 退款申请自动审核，生成支付单据
        MqMessageBO mqMessageBO  = new MqMessageBO(Constants.TOPIC_REFUND_ORDER_AUDIT_NOTIFY, "", refundNos, MqDelayLevel.TEN_SECONDS);
        mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);

        mqMessageSendApi.send(mqMessageBO);

        return true;
    }


    /**
     * 重推发送失败退款记录
     * @param orderNo
     * @param orderId
     * @param refundId
     * @param reason
     * @param refundAmount
     * @param refundType
     * @param refundSource
     * @param opUserId
     * @param opTime
     * @param operateUser
     * @return
     */
    private boolean reSend(String orderNo,Long orderId, Long refundId, String reason, BigDecimal refundAmount, Integer refundType,Integer refundSource, Long opUserId, Date opTime, Long operateUser) {

        RefundParamRequest refundParamRequest = RefundParamRequest.builder()
                .appOrderId(orderId)
                .refundId(refundId)
                .reason(reason)
                .refundType(refundType)
                .appOrderNo(orderNo)
                .refundAmount(refundAmount).build();
        refundParamRequest.setOpUserId(opUserId);
        refundParamRequest.setOpTime(opTime);

        Result<Void> result = refundApi.retryFailurePayOrder(refundParamRequest);
        if (HttpStatus.HTTP_OK != result.getCode()) {
            log.info("refundOrder end, result :[{}]", result);
            throw new BusinessException(OrderErrorCode.REFUND_FAIL);
        }
        OrderRefundStatusRequest orderRefundStatusRequest = new OrderRefundStatusRequest();
        orderRefundStatusRequest.setRefundId(refundId);
        orderRefundStatusRequest.setRefundStatus(RefundStatusEnum.REFUNDING.getCode());
        orderRefundStatusRequest.setOpUserId(opUserId);
        orderRefundStatusRequest.setOpTime(new Date());
        if (null != operateUser && 0L != operateUser) {
            orderRefundStatusRequest.setOperateUser(operateUser);
            orderRefundStatusRequest.setOperateTime(opTime);
        }
        return orderRefundApi.editStatus(orderRefundStatusRequest);
}

    @Override
    public boolean reTryRefund(RefundReTryRequest request) {
        OrderRefundDTO orderRefundDTO = orderRefundApi.queryById(request.getRefundId());
        if (RefundStatusEnum.REFUNDED.getCode().equals(orderRefundDTO.getRefundStatus())) {
            throw new BusinessException(OrderErrorCode.REFUND_HAVE_FINISH);
        }
        log.info("开始重新退款，请求数据为:[{}],退款单信息为:[{}]", request, orderRefundDTO);
        // 1-已退款，仅标记已处理  2-未退款，通过接口退款
        if (1 == request.getOperate()) {
            OrderRefundStatusRequest orderRefundStatusRequest = new OrderRefundStatusRequest();
            orderRefundStatusRequest.setRefundId(request.getRefundId());
            orderRefundStatusRequest.setOpUserId(request.getOpUserId());
            orderRefundStatusRequest.setOpTime(request.getOpTime());
            orderRefundStatusRequest.setOperateUser(request.getOpUserId());
            orderRefundStatusRequest.setOperateTime(request.getOpTime());
            orderRefundStatusRequest.setRefundStatus(RefundStatusEnum.REFUNDED.getCode());
            orderRefundStatusRequest.setRealRefundAmount(orderRefundDTO.getRefundAmount());
            return orderRefundApi.editStatus(orderRefundStatusRequest);
        } else {
            // 重试
            return reSend(orderRefundDTO.getOrderNo(),orderRefundDTO.getOrderId(), orderRefundDTO.getId(), orderRefundDTO.getRemark(), orderRefundDTO.getRefundAmount(), 1,orderRefundDTO.getRefundSource(),request.getOpUserId(), request.getOpTime(), request.getOpUserId());
        }
    }
}
