package com.yiling.mall.payment.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.goods.medicine.api.GoodsBiddingPriceApi;
import com.yiling.goods.medicine.api.GoodsDiscountRateApi;
import com.yiling.mall.payment.dto.request.PaymentRequest;
import com.yiling.mall.payment.service.PaymentService;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderDetailApi;
import com.yiling.order.order.dto.CashDiscountAgreementInfoDTO;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.dto.request.OrderDetailCashDiscountInfoDTO;
import com.yiling.order.order.dto.request.UpdateOrderCashDiscountAmountRequest;
import com.yiling.order.order.dto.request.UpdateOrderPaymentMethodRequest;
import com.yiling.order.order.enums.OrderErrorCode;
import com.yiling.order.order.enums.OrderStatusEnum;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.order.order.enums.PaymentMethodEnum;
import com.yiling.order.payment.enums.PaymentErrorCode;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.api.PaymentMethodApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.payment.api.PaymentDaysAccountApi;
import com.yiling.user.payment.dto.request.ReduceQuotaRequest;
import com.yiling.user.procrelation.api.ProcurementRelationGoodsApi;
import com.yiling.user.procrelation.dto.ProcRelationRebateResultBO;
import com.yiling.user.procrelation.dto.request.CalculateProcRelationRebateRequest;
import com.yiling.user.system.dto.PaymentMethodDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

/**
 * 支付 Service
 *
 * @author: xuan.zhou
 * @date: 2021/6/29
 */
@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    @DubboReference
    OrderApi                                orderApi;
    @DubboReference
    OrderDetailApi                          orderDetailApi;
    @DubboReference
    PaymentMethodApi                        paymentMethodApi;
    @DubboReference
    EnterpriseApi                           enterpriseApi;
    @DubboReference
    GoodsBiddingPriceApi                    goodsBiddingPriceApi;
    @DubboReference
    GoodsDiscountRateApi                    goodsDiscountRateApi;
    @DubboReference
    PaymentDaysAccountApi                   paymentDaysAccountApi;
    @DubboReference
    MqMessageSendApi                        mqMessageSendApi;
    @DubboReference
    ProcurementRelationGoodsApi             procurementRelationGoodsApi;
    @Lazy
    @Autowired
    private PaymentServiceImpl              _this;

    @Override
    public BigDecimal  calculateOrderCashDiscountAmount(PaymentRequest.OrderPaymentRequest paymentRequest) {

        OrderDTO orderDTO = orderApi.getOrderInfo(paymentRequest.getOrderId());
        // 支付方式
        Map<Long, OrderDTO> orderDTOMap = Collections.singletonList(orderDTO).stream().collect(Collectors.toMap(OrderDTO::getId, Function.identity()));
        PaymentRequest request = new PaymentRequest();
        request.setOrderPaymentList(Collections.singletonList(paymentRequest));

        // 复用方法,计算折扣金额
        Map<Long, UpdateOrderCashDiscountAmountRequest> requestMap =   this.calculateOrderCashDiscountAmount(request,orderDTOMap);

        if (MapUtil.isEmpty(requestMap)) {

            return BigDecimal.ZERO;
        }

        UpdateOrderCashDiscountAmountRequest updateOrderCashDiscountAmountRequest =  requestMap.get(paymentRequest.getOrderId());

        return updateOrderCashDiscountAmountRequest != null ? updateOrderCashDiscountAmountRequest.getCashDiscountAmount() : BigDecimal.ZERO;
    }


    /**
     *  处理订单支付事务数据
     * @param request
     * @param orderDTOList
     * @return
     */
    @GlobalTransactional
    public List<MqMessageBO> processPayTx(PaymentRequest request,List<OrderDTO> orderDTOList) {

        Long buyerEid = orderDTOList.stream().findFirst().get().getBuyerEid();
        List<Long> sellerEids = orderDTOList.stream().map(OrderDTO::getSellerEid).distinct().collect(Collectors.toList());
        Map<Long, List<PaymentMethodDTO> > paymentMethodDTOMap = MapUtil.newHashMap();
        if (OrderTypeEnum.POP == OrderTypeEnum.getByCode(request.getOrderType())) {
            paymentMethodDTOMap = paymentMethodApi.listByCustomerEidAndEids(buyerEid, sellerEids, PlatformEnum.POP);
        } else if (OrderTypeEnum.B2B == OrderTypeEnum.getByCode(request.getOrderType())) {
            paymentMethodDTOMap = paymentMethodApi.listByCustomerEidAndEids(buyerEid, sellerEids, PlatformEnum.B2B);
        }

        // 校验支付方式
        Map<Long, OrderDTO> orderDTOMap = orderDTOList.stream().collect(Collectors.toMap(OrderDTO::getId, Function.identity()));
        Map<Long, List<PaymentMethodDTO>> finalPaymentMethodDTOMap = paymentMethodDTOMap;
        request.getOrderPaymentList().forEach(orderPayment -> {
            OrderDTO orderDTO = orderDTOMap.get(orderPayment.getOrderId());
            List<PaymentMethodDTO> paymentMethodDTOList = finalPaymentMethodDTOMap.get(orderDTO.getSellerEid());
            if (CollUtil.isEmpty(paymentMethodDTOList)) {
                throw new BusinessException(PaymentErrorCode.NO_PAYMENT_METHOD);
            }

            List<Long> paymentMethodIds = paymentMethodDTOList.stream().map(PaymentMethodDTO::getCode).collect(Collectors.toList());
            if (!paymentMethodIds.contains(orderPayment.getPaymentMethodId())) {
                throw new BusinessException(PaymentErrorCode.PAYMENT_METHOD_UNUSABLE);
            }
        });

        // 计算订单现折金额
        Map<Long, UpdateOrderCashDiscountAmountRequest> calculateOrderCashDiscountAmount = Maps.newHashMap();

        // pop 订单才有协议优惠
        if (OrderTypeEnum.POP == OrderTypeEnum.getByCode(request.getOrderType())) {

            calculateOrderCashDiscountAmount = this.calculateOrderCashDiscountAmount(request, orderDTOMap);
        }

        for (PaymentRequest.OrderPaymentRequest orderPayment : request.getOrderPaymentList()) {

            OrderDTO orderDTO = orderDTOMap.get(orderPayment.getOrderId());

            UpdateOrderCashDiscountAmountRequest updateOrderCashDiscountAmountRequest = calculateOrderCashDiscountAmount.get(orderDTO.getId());
            // 订单现折金额
            BigDecimal orderCashDiscountAmount = updateOrderCashDiscountAmountRequest != null ? updateOrderCashDiscountAmountRequest.getCashDiscountAmount() : BigDecimal.ZERO;
            // 订单应付金额
            BigDecimal orderPaymentAmount = NumberUtil.sub(orderDTO.getTotalAmount(), orderDTO.getFreightAmount(), orderCashDiscountAmount);

            if (PaymentMethodEnum.getByCode(orderPayment.getPaymentMethodId()) == PaymentMethodEnum.PAYMENT_DAYS) {
                // 账期可用额度
                BigDecimal paymentDaysAvailableAmount = paymentDaysAccountApi.getAvailableAmountByCustomerEid(orderDTO.getSellerEid(), orderDTO.getBuyerEid());
                if (paymentDaysAvailableAmount.compareTo(orderPaymentAmount) == -1) {
                    throw new BusinessException(PaymentErrorCode.PAYMENTDAYS_QUOTA_NOT_ENOUGH);
                }

                // 扣减账期额度
                this.reducePaymentDaysQuota(orderDTO, orderPaymentAmount, request.getOpUserId());
            }

            // 更新订单现折金额
            if (updateOrderCashDiscountAmountRequest != null) {
                orderApi.updateOrderCashDiscountAmount(updateOrderCashDiscountAmountRequest);
            }

            // 更新订单支付状态
            this.updateOrderPaymentStatus(orderPayment.getOrderId(), orderPayment.getPaymentMethodId(), orderPaymentAmount, request.getOpUserId());
        }

        if (OrderTypeEnum.POP == OrderTypeEnum.getByCode(request.getOrderType())) {
            // 发送订单已支付消息
            return  orderDTOList.stream().map(orderDto -> {
                MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_ORDER_PAID, "", orderDto.getOrderNo());
                mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);

                return mqMessageBO;
            }).collect(Collectors.toList());
        }

        return Collections.emptyList();

    }

    @Override
    public boolean pay(PaymentRequest request) {
        List<Long> orderIds = request.getOrderPaymentList().stream().map(PaymentRequest.OrderPaymentRequest::getOrderId).distinct().collect(Collectors.toList());
        List<OrderDTO> orderDTOList = orderApi.listByIds(orderIds);
        if (CollUtil.isEmpty(orderDTOList) || orderIds.size() != orderDTOList.size()) {
            throw new BusinessException(PaymentErrorCode.ORDER_NOT_EXISTS);
        }

        // 校验订单支付状态：以是否有支付方式来判断
        boolean hasPaidOrder = orderDTOList.stream().filter(e -> e.getPaymentMethod() != 0).findAny().isPresent();
        if (hasPaidOrder) {
            throw new BusinessException(PaymentErrorCode.ORDER_HAD_PAID);
        }
        // 校验订单状态是否发生变更
        boolean orderStatusChange = orderDTOList.stream().filter(e -> OrderStatusEnum.UNAUDITED != OrderStatusEnum.getByCode(e.getOrderStatus()) && OrderStatusEnum.UNDELIVERED != OrderStatusEnum.getByCode(e.getOrderStatus())).findAny().isPresent();

        if (orderStatusChange) {
            throw new BusinessException(OrderErrorCode.ORDER_INFO_STATUS_CHANGE);
        }

        // 处理支付数据
        List<MqMessageBO> mqMessageBOList = _this.processPayTx(request, orderDTOList);
        mqMessageBOList.forEach(mqMessageBO -> {
            // 发送消息
            mqMessageSendApi.send(mqMessageBO);
        });

        return true;
    }
    /**
     * 现折金额分摊
     *
     * @param orderDetail
     * @param orderDetailDTOS
     * @param procRelationRebateResultBO
     * @return
     */
    private CashDiscountAgreementInfoDTO shareGoodsCalculateResult(OrderDetailDTO orderDetail, List<OrderDetailDTO> orderDetailDTOS, ProcRelationRebateResultBO procRelationRebateResultBO, List<OrderDetailCashDiscountInfoDTO> orderDetailCashDiscountInfoList) {

        // 表示只有一个商品，无需分摊现折金额
        if (orderDetailDTOS.size() <= 1) {

            CashDiscountAgreementInfoDTO agreementInfoDTO = new CashDiscountAgreementInfoDTO();
            agreementInfoDTO.setAgreementId(procRelationRebateResultBO.getRelationId());
            agreementInfoDTO.setVersion(procRelationRebateResultBO.getVersionId());
            agreementInfoDTO.setPolicyValue(procRelationRebateResultBO.getRebate());
            agreementInfoDTO.setDiscountAmount(procRelationRebateResultBO.getRebateAmount());

            return agreementInfoDTO;
        }

        // 表示相同的品出现多个规格，需要将现折金额分摊到明细上
        BigDecimal totalGoodsAmount = orderDetailDTOS.stream().map(OrderDetailDTO::getGoodsAmount).reduce(BigDecimal::add).get();
        // 最后已一条分摊明细集合
        OrderDetailDTO lastOrderDetail = orderDetailDTOS.stream().sorted(Comparator.comparing(OrderDetailDTO::getGoodsAmount, Comparator.reverseOrder())).findFirst().get();

        CashDiscountAgreementInfoDTO agreementInfoDTO = new CashDiscountAgreementInfoDTO();
        agreementInfoDTO.setAgreementId(procRelationRebateResultBO.getRelationId());
        agreementInfoDTO.setVersion(procRelationRebateResultBO.getVersionId());
        agreementInfoDTO.setPolicyValue(procRelationRebateResultBO.getRebate());

        BigDecimal ratio = NumberUtil.div(orderDetail.getGoodsAmount(), totalGoodsAmount, 6);

        // 表示最后一条分摊
        if (orderDetail.getId().equals(lastOrderDetail.getId())) {
            agreementInfoDTO.setDiscountAmount(NumberUtil.sub(procRelationRebateResultBO.getRebateAmount(), getHashShareDiscount(orderDetailCashDiscountInfoList, procRelationRebateResultBO.getGoodsId(), procRelationRebateResultBO.getRelationId())));
        } else {
            agreementInfoDTO.setDiscountAmount(NumberUtil.mul(ratio, procRelationRebateResultBO.getRebateAmount()).setScale(2, BigDecimal.ROUND_HALF_UP));
        }

        // 最后做负数校验
        if (CompareUtil.compare(agreementInfoDTO.getDiscountAmount(), BigDecimal.ZERO) < 0) {

            agreementInfoDTO.setDiscountAmount(BigDecimal.ZERO);
        }

        return agreementInfoDTO;
    }


    /**
     * 获取商品已经分摊的优惠金额
     *
     * @param orderDetailCashDiscountInfoList
     * @param goodsId
     * @param agreementId
     * @return
     */
    private BigDecimal getHashShareDiscount(List<OrderDetailCashDiscountInfoDTO> orderDetailCashDiscountInfoList, Long goodsId, Long agreementId) {

        List<OrderDetailCashDiscountInfoDTO> goodsDetailCashDiscountList = orderDetailCashDiscountInfoList.stream().filter(t -> goodsId.equals(t.getGoodsId())).collect(Collectors.toList());

        if (CollectionUtil.isEmpty(goodsDetailCashDiscountList)) {

            return BigDecimal.ZERO;
        }

        BigDecimal shareDiscount = BigDecimal.ZERO;

        for (OrderDetailCashDiscountInfoDTO discountInfoDTO : goodsDetailCashDiscountList) {
            BigDecimal agreementDiscount = discountInfoDTO.getCashDiscountAgreementInfoList().stream().filter(t -> t.getAgreementId().equals(agreementId)).map(CashDiscountAgreementInfoDTO::getDiscountAmount).reduce(BigDecimal::add).get();
            shareDiscount = NumberUtil.add(shareDiscount, agreementDiscount);
        }
        return shareDiscount;
    }


    /**
     * 查询商业建采参数
     * @param goodDetailMap
     * @param buyerEid
     * @param distributorEid
     * @return
     */
    private CalculateProcRelationRebateRequest buildCalculateRequest(Map<Long, List<OrderDetailDTO>> goodDetailMap, Long buyerEid, Long distributorEid) {

        List<CalculateProcRelationRebateRequest.CalculateProcRelationGoodsRequest> goodsRequestList = goodDetailMap.entrySet().stream().map(t -> {

            CalculateProcRelationRebateRequest.CalculateProcRelationGoodsRequest relationGoodsRequest = new CalculateProcRelationRebateRequest.CalculateProcRelationGoodsRequest();
            relationGoodsRequest.setGoodsId(t.getKey());
            relationGoodsRequest.setGoodsQuantity(t.getValue().stream().collect(Collectors.summingLong(OrderDetailDTO::getGoodsQuantity)));
            relationGoodsRequest.setGoodsAmount(t.getValue().stream().map(OrderDetailDTO::getGoodsAmount).reduce(BigDecimal::add).get());

            return relationGoodsRequest;

        }).collect(Collectors.toList());


        CalculateProcRelationRebateRequest request = new CalculateProcRelationRebateRequest();
        request.setBuyerEid(buyerEid);
        request.setSellerEid(distributorEid);
        request.setGoodsList(goodsRequestList);

        return request;
    }

    /**
     * 计算现折金额
     * @param request
     * @param orderDTOMap
     * @return
     */
    private Map<Long, UpdateOrderCashDiscountAmountRequest> calculateOrderCashDiscountAmount(PaymentRequest request, Map<Long, OrderDTO> orderDTOMap) {
        Map<Long, UpdateOrderCashDiscountAmountRequest> map = MapUtil.newHashMap();
        for (PaymentRequest.OrderPaymentRequest orderPayment : request.getOrderPaymentList()) {

            OrderDTO orderDTO = orderDTOMap.get(orderPayment.getOrderId());
            List<OrderDetailDTO> orderDetailDTOList = orderDetailApi.getOrderDetailInfo(orderDTO.getId());
            Map<Long, List<OrderDetailDTO>> goodDetailMap = orderDetailDTOList.stream().collect(Collectors.groupingBy(OrderDetailDTO::getGoodsId));

            CalculateProcRelationRebateRequest calRequest = buildCalculateRequest(goodDetailMap, orderDTO.getBuyerEid(), orderDTO.getDistributorEid());
            // 查询商业建采优惠信息
            List<ProcRelationRebateResultBO> relationRebateResultBOS = procurementRelationGoodsApi.calculateRebateForGoods(calRequest);

            if (log.isDebugEnabled()) {

                log.info("..计算订单现折优惠入参:[{}],订单编号:{},返回结果:[{}]", calRequest, orderDTO.getOrderNo(),relationRebateResultBOS);
            }

            if (CollectionUtil.isEmpty(relationRebateResultBOS)) {

                continue;
            }

            // 同一个商品,同一个配送商理论在同时进行中的,只存在一个建采活动中
            Map<Long, ProcRelationRebateResultBO> procRelationRebateResultBOMap = relationRebateResultBOS.stream().collect(Collectors.toMap(e -> e.getGoodsId(), Function.identity(), (v1, v2) -> v1));
            List<Long> goodsIds = orderDetailDTOList.stream().map(OrderDetailDTO::getGoodsId).distinct().collect(Collectors.toList());

            EnterpriseDTO buyerEnterpriseDTO = enterpriseApi.getById(orderDTO.getBuyerEid());
            // 招标挂网价
            Map<Long, BigDecimal> goodsBiddingPriceMap = goodsBiddingPriceApi.queryGoodsBidingPriceByLocation(goodsIds, buyerEnterpriseDTO.getProvinceCode());
            // 商品最低折扣比率字典
            Map<Long, BigDecimal> goodsDiscountRateMap = goodsDiscountRateApi.queryGoodsDiscountRateMap(orderDTO.getBuyerEid(), goodsIds);

            List<OrderDetailCashDiscountInfoDTO> orderDetailCashDiscountInfoList = CollUtil.newArrayList();
            // 金额正序排序
            orderDetailDTOList = orderDetailDTOList.stream().sorted(Comparator.comparing(OrderDetailDTO::getGoodsAmount)).collect(Collectors.toList());

            orderDetailDTOList.stream().filter(t -> procRelationRebateResultBOMap.get(t.getGoodsId()) != null).forEach(orderDetail -> {

                ProcRelationRebateResultBO procRelationRebateResultBO = procRelationRebateResultBOMap.get(orderDetail.getGoodsId());
                // 相同的品出现多个规格，需要将现折金额分摊到明细上
                CashDiscountAgreementInfoDTO agreementInfoDTO = this.shareGoodsCalculateResult(orderDetail, goodDetailMap.get(orderDetail.getGoodsId()), procRelationRebateResultBO, orderDetailCashDiscountInfoList);
                // 商品现折金额
                BigDecimal goodsCashDiscountAmount = agreementInfoDTO.getDiscountAmount();
                // 商品应付金额
                BigDecimal goodsPayableAmount = NumberUtil.sub(orderDetail.getGoodsAmount(), goodsCashDiscountAmount);
                // 商品招标挂网价
                BigDecimal goodsBiddingPrice = goodsBiddingPriceMap.getOrDefault(orderDetail.getGoodsId(), BigDecimal.ZERO);

                // 如果商品折后金额小于商品招标挂网价，则错误提示
                if (goodsPayableAmount.compareTo(goodsBiddingPrice) == -1) {
                    throw new BusinessException(PaymentErrorCode.GOODS_DISCOUNT_AMOUNT_ERROR);
                }
                // 如果商品折扣比率小于商品设置的最低折扣比率，则错误提示
                BigDecimal goodsMinDiscountRate = goodsDiscountRateMap.get(orderDetail.getGoodsId());

                if (goodsMinDiscountRate != null && goodsMinDiscountRate.compareTo(BigDecimal.ZERO) == 1) {
                    BigDecimal goodsCashDiscountRate = NumberUtil.div(goodsPayableAmount, orderDetail.getGoodsAmount(), 2, RoundingMode.HALF_UP);
                    if (goodsCashDiscountRate.compareTo(goodsMinDiscountRate) == -1) {
                        throw new BusinessException(PaymentErrorCode.GOODS_DISCOUNT_AMOUNT_ERROR);
                    }
                }

                OrderDetailCashDiscountInfoDTO orderDetailCashDiscountInfo = new OrderDetailCashDiscountInfoDTO();
                orderDetailCashDiscountInfo.setOrderDetailId(orderDetail.getId());
                orderDetailCashDiscountInfo.setGoodsId(orderDetail.getGoodsId());
                orderDetailCashDiscountInfo.setGoodsAmount(orderDetail.getGoodsAmount());
                orderDetailCashDiscountInfo.setCashDiscountAmount(goodsCashDiscountAmount);
                orderDetailCashDiscountInfo.setCashDiscountAgreementInfoList(Collections.singletonList(agreementInfoDTO));
                orderDetailCashDiscountInfoList.add(orderDetailCashDiscountInfo);

                // 现折金额大于商品金额
                if (CompareUtil.compare(orderDetailCashDiscountInfo.getCashDiscountAmount(),orderDetailCashDiscountInfo.getGoodsAmount()) > 0) {

                    throw new BusinessException(PaymentErrorCode.GOODS_DISCOUNT_AMOUNT_ERROR);
                }

            });

            if (CollUtil.isNotEmpty(orderDetailCashDiscountInfoList)) {
                UpdateOrderCashDiscountAmountRequest updateOrderCashDiscountAmountRequest = new UpdateOrderCashDiscountAmountRequest();
                updateOrderCashDiscountAmountRequest.setOrderId(orderDTO.getId());
                updateOrderCashDiscountAmountRequest.setOrderNo(orderDTO.getOrderNo());
                updateOrderCashDiscountAmountRequest.setCashDiscountAmount(orderDetailCashDiscountInfoList.stream().map(OrderDetailCashDiscountInfoDTO::getCashDiscountAmount).reduce(BigDecimal::add).get());
                updateOrderCashDiscountAmountRequest.setOrderDetailCashDiscountInfoList(orderDetailCashDiscountInfoList);
                updateOrderCashDiscountAmountRequest.setOpUserId(request.getOpUserId());
                map.put(orderDTO.getId(), updateOrderCashDiscountAmountRequest);
            }
        }

        return map;
    }

    /**
     * 扣减账期额度
     *
     * @param orderDTO 订单信息
     * @param orderPaymentAmount 订单应付金额
     * @return
     */
    private boolean reducePaymentDaysQuota(OrderDTO orderDTO, BigDecimal orderPaymentAmount, Long opUserId) {
        ReduceQuotaRequest reduceQuotaRequest = new ReduceQuotaRequest();
        reduceQuotaRequest.setEid(orderDTO.getSellerEid());
        reduceQuotaRequest.setCustomerEid(orderDTO.getBuyerEid());
        reduceQuotaRequest.setOrderId(orderDTO.getId());
        reduceQuotaRequest.setOrderNo(orderDTO.getOrderNo());
        reduceQuotaRequest.setUseAmount(orderPaymentAmount);
        reduceQuotaRequest.setOpUserId(opUserId);
        reduceQuotaRequest.setTime(new Date());
        reduceQuotaRequest.setPlatformEnum(PlatformEnum.POP);
        return paymentDaysAccountApi.reduceQuota(reduceQuotaRequest);
    }

    /**
     * 更新订单支付方式以及金额
     *
     * @param orderId 订单ID
     * @param paymentMethodId 支付方式ID
     * @param paymentAmount 实付金额
     * @param opUserId 操作人ID
     * @return
     */
    private boolean updateOrderPaymentStatus(Long orderId, Long paymentMethodId, BigDecimal paymentAmount, Long opUserId) {
        UpdateOrderPaymentMethodRequest request = new UpdateOrderPaymentMethodRequest();
        request.setOrderId(orderId);
        request.setPaymentMethodId(paymentMethodId.intValue());
        request.setPaymentAmount(paymentAmount);
        request.setOpUserId(opUserId);

        return orderApi.updatePaymentPayMethod(request);
    }

}
