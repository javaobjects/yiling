package com.yiling.mall.order.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.order.order.dto.request.SaveOrderTicketBatchRequest;
import com.yiling.order.order.enums.InvoiceApplyStatusEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsBiddingPriceApi;
import com.yiling.goods.medicine.api.GoodsDiscountRateApi;
import com.yiling.mall.order.service.OrderInvoiceApplyProcessService;
import com.yiling.order.order.api.NoApi;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderDeliveryErpApi;
import com.yiling.order.order.api.OrderDetailChangeApi;
import com.yiling.order.order.api.OrderDetailTicketDiscountApi;
import com.yiling.order.order.api.OrderErpApi;
import com.yiling.order.order.api.OrderInvoiceApplyApi;
import com.yiling.order.order.api.OrderInvoiceDeliveryGroupApi;
import com.yiling.order.order.api.OrderInvoiceDetailApi;
import com.yiling.order.order.api.OrderInvoiceLogApi;
import com.yiling.order.order.api.OrderReturnApi;
import com.yiling.order.order.api.OrderTicketDiscountApi;
import com.yiling.order.order.api.TicketDiscountRecordApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDeliveryErpDTO;
import com.yiling.order.order.dto.OrderDetailChangeDTO;
import com.yiling.order.order.dto.OrderInvoiceApplyDTO;
import com.yiling.order.order.dto.OrderInvoiceDetailDTO;
import com.yiling.order.order.dto.OrderReturnDTO;
import com.yiling.order.order.dto.TicketDiscountRecordDTO;
import com.yiling.order.order.dto.request.OrderDetailTicketDiscountRequest;
import com.yiling.order.order.dto.request.SaveOrderDetailTicketDiscountRequest;
import com.yiling.order.order.dto.request.SaveOrderInvoiceDeliveryGroupRequest;
import com.yiling.order.order.dto.request.SaveOrderInvoiceDetailRequest;
import com.yiling.order.order.dto.request.SaveOrderInvoiceLogRequest;
import com.yiling.order.order.dto.request.SaveOrderTicketApplyInfoRequest;
import com.yiling.order.order.dto.request.SaveOrderTicketDiscountRequest;
import com.yiling.order.order.dto.request.UpdateOrderInvoiceApplyRequest;
import com.yiling.order.order.dto.request.UpdateOrderInvoiceInfoRequest;
import com.yiling.order.order.enums.NoEnum;
import com.yiling.order.order.enums.OrderErrorCode;
import com.yiling.order.order.enums.OrderInvoiceApplyStatusEnum;
import com.yiling.order.order.enums.OrderInvoiceDiscountTypeEnum;
import com.yiling.order.order.enums.OrderReturnTypeEnum;
import com.yiling.order.order.enums.OrderStatusEnum;
import com.yiling.order.order.enums.OrderTicketDiscountTypeEnum;
import com.yiling.order.order.enums.OrderTransitionRuleCodeEnum;
import com.yiling.order.order.enums.PaymentMethodEnum;
import com.yiling.order.order.enums.PaymentStatusEnum;
import com.yiling.user.common.util.bean.In;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.payment.api.PaymentDaysAccountApi;
import com.yiling.user.payment.dto.request.RefundPaymentDaysAmountRequest;
import com.yiling.user.payment.dto.request.UpdateInvoiceTicketDiscountRequest;
import cn.hutool.core.collection.CollectionUtil;
import io.netty.resolver.dns.macos.MacOSDnsServerAddressStreamProvider;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 订单 开票服务
 * </p>
 *
 * @author:wei.wang
 * @date:2021/7/26
 */
@Service
@Slf4j
@RefreshScope
public class OrderInvoiceApplyProcessServiceImpl implements OrderInvoiceApplyProcessService {
    @DubboReference
    OrderApi                     orderApi;
    @DubboReference
    OrderInvoiceApplyApi         orderInvoiceApplyApi;
    @DubboReference
    OrderDetailTicketDiscountApi orderDetailTicketDiscountApi;
    @DubboReference
    TicketDiscountRecordApi      ticketDiscountRecordApi;
    @DubboReference
    OrderTicketDiscountApi       orderTicketDiscountApi;
    @DubboReference
    OrderInvoiceLogApi           orderInvoiceLogApi;
    @DubboReference
    GoodsDiscountRateApi         goodsDiscountRateApi;
    @DubboReference
    OrderInvoiceDetailApi        orderInvoiceDetailApi;
    @DubboReference
    EnterpriseApi                enterpriseApi;
    @DubboReference
    GoodsBiddingPriceApi         goodsBiddingPriceApi;
    @DubboReference
    OrderDetailChangeApi         orderDetailChangeApi;
    @DubboReference
    OrderErpApi                  orderErpApi;
    @DubboReference
    PaymentDaysAccountApi        paymentDaysAccountApi;
    @DubboReference
    OrderReturnApi               orderReturnApi;
    @DubboReference
    OrderDeliveryErpApi          orderDeliveryErpApi;
    @DubboReference
    OrderInvoiceDeliveryGroupApi orderInvoiceDeliveryGroupApi;
    @DubboReference
    NoApi                        noApi;

    @Value("#{${common.order-max-amount}}")
    private Map<String, String> ruleMap ;

    @Autowired
    RedisService redisService;


    /**
     * 申请开票
     *
     * @param request
     * @return
     */
    @Override
    @GlobalTransactional
    public Boolean apply(SaveOrderTicketApplyInfoRequest request) {
        OrderDTO order = orderApi.getOrderInfo(request.getId());
        //校验各种状态
        validateApplyInvoiceStatus(order);
        //插入表操作
        insertResultOperation(request, order);
        return true;
    }


    /**
     * 作废应收单号
     *
     * @param erpReceivableNo 应收单号
     * @return
     */
    @Override
    @GlobalTransactional
    public Boolean removeErpReceivableNo(String erpReceivableNo) {
        Map<String, Object> receivableMap = orderErpApi.removeErpReceivableNo(erpReceivableNo);
        Long orderId = (Long) receivableMap.get("orderId");
        OrderDTO orderDTO = orderApi.getOrderInfo(orderId);
        BigDecimal ticketDiscountAmount = (BigDecimal) receivableMap.get("ticketDiscountAmount");
        if (orderDTO != null
                && PaymentMethodEnum.PAYMENT_DAYS.getCode().equals(Long.valueOf(orderDTO.getPaymentMethod()))
                && ticketDiscountAmount.compareTo(BigDecimal.ZERO) > 0
                && PaymentStatusEnum.getByCode(orderDTO.getPaymentStatus()) == PaymentStatusEnum.UNPAID) {
            UpdateInvoiceTicketDiscountRequest request = new UpdateInvoiceTicketDiscountRequest();
            request.setOrderId(orderDTO.getId());
            request.setInvoiceType(2);
            request.setTicketDiscountAmount(ticketDiscountAmount);
            log.info("订单删除应收单,退还账期参数,UpdateInvoiceTicketDiscountRequest:{}", JSON.toJSONString(request));
            paymentDaysAccountApi.updateTicketDiscountAmount(request);
        }
        return true;
    }

    /**
     * 根据ERP出库单号更新ERP应收单号
     *
     * @param erpDeliveryNo   ERP出库单号
     * @param erpReceivableNo ERP应收单号
     * @return
     */
    @Override
    public Boolean updateErpReceivableNoByDeliveryNo(String erpDeliveryNo, String erpReceivableNo) {
        Map<String, Object> receivableMap = orderErpApi.updateErpReceivableNoByDeliveryNo(erpDeliveryNo, erpReceivableNo);
        Long orderId = (Long) receivableMap.get("orderId");
        OrderDTO orderDTO = orderApi.getOrderInfo(orderId);
        BigDecimal ticketDiscountAmount = (BigDecimal) receivableMap.get("ticketDiscountAmount");
        if (orderDTO != null && PaymentMethodEnum.PAYMENT_DAYS.getCode().equals(Long.valueOf(orderDTO.getPaymentMethod()))
                && ticketDiscountAmount.compareTo(BigDecimal.ZERO) > 0
                && PaymentStatusEnum.getByCode(orderDTO.getPaymentStatus()) == PaymentStatusEnum.UNPAID) {
            UpdateInvoiceTicketDiscountRequest invoiceRequest = new UpdateInvoiceTicketDiscountRequest();
            invoiceRequest.setOrderId(orderId);
            invoiceRequest.setInvoiceType(1);
            invoiceRequest.setTicketDiscountAmount(ticketDiscountAmount);
            paymentDaysAccountApi.updateTicketDiscountAmount(invoiceRequest);
            log.info("订单完成开票,退还账期参数,UpdateInvoiceTicketDiscountRequest:{}", JSON.toJSONString(invoiceRequest));
        }
        return true;
    }

    /**
     * erp作废发票
     *
     * @param invoiceList 发票号
     * @return
     */
    @Override
    @GlobalTransactional
    public Boolean cancelErpOrderInvoice(List<String> invoiceList) {
        OrderDTO orderDTO = orderErpApi.cancelErpOrderInvoice(invoiceList);
        if (orderDTO != null) {
            List<OrderReturnDTO> orderReturnDTOS = orderReturnApi.listByOrderId(orderDTO.getId());
            List<OrderReturnDTO> list = new ArrayList<>();
            Boolean returnFlag = false;
            if (CollectionUtil.isNotEmpty(orderReturnDTOS)) {
                for (OrderReturnDTO one : orderReturnDTOS) {
                    if (!OrderReturnTypeEnum.SELLER_RETURN_ORDER.getCode().equals(one.getReturnType())) {
                        list.add(one);
                    }
                }
                if (list.size() == 1) {
                    List<OrderDetailChangeDTO> orderDetailChangeDTOS = orderDetailChangeApi.listByOrderId(orderDTO.getId());
                    Integer returnQuantity = 0;
                    Integer deliveryQuantity = 0;
                    for (OrderDetailChangeDTO changeOne : orderDetailChangeDTOS) {
                        returnQuantity = returnQuantity + changeOne.getReturnQuantity();
                        deliveryQuantity = deliveryQuantity + changeOne.getDeliveryQuantity();
                    }
                    if (returnQuantity.equals(deliveryQuantity)) {
                        returnFlag = true;
                    }
                }
            }
            if (PaymentMethodEnum.PAYMENT_DAYS.getCode().equals(Long.valueOf(orderDTO.getPaymentMethod())) && returnFlag
                    && PaymentStatusEnum.getByCode(orderDTO.getPaymentStatus()) == PaymentStatusEnum.UNPAID) {
                RefundPaymentDaysAmountRequest paymentDaysAmountRequest = new RefundPaymentDaysAmountRequest();
                paymentDaysAmountRequest.setOrderId(orderDTO.getId())
                        .setRefundAmount(orderDTO.getInvoiceAmount());
                paymentDaysAmountRequest.setOpTime(new Date());
                paymentDaysAmountRequest.setOpUserId(0L);
                paymentDaysAmountRequest.setPlatformEnum(PlatformEnum.getByCode(orderDTO.getOrderType()));
                log.info("作废发票,退还账期参数,paymentDaysAmountRequest:{}", JSON.toJSONString(paymentDaysAmountRequest));
                paymentDaysAccountApi.refund(paymentDaysAmountRequest);

            }
        }

        return true;
    }


    private void insertResultOperation(SaveOrderTicketApplyInfoRequest request, OrderDTO order) {
        //申请信息
        OrderInvoiceApplyDTO orderApply = new OrderInvoiceApplyDTO();
        orderApply.setCreateTime(request.getOpTime());
        orderApply.setCreateUser(request.getOpUserId());
        orderApply.setTransitionRuleCode(request.getTransitionRuleCode());

        if (OrderTransitionRuleCodeEnum.SALE_OUT_ORDER_OPEN_ELECTRON_INVOICE.getCode().equals(request.getTransitionRuleCode())) {
            if (StringUtils.isEmpty(request.getInvoiceEmail())) {
                throw new BusinessException(OrderErrorCode.ORDER_INFO_PARAM_ERROR);
            }
            orderApply.setInvoiceEmail(request.getInvoiceEmail());
            redisService.set(RedisKey.generate("order", "invoice", "email", order.getBuyerEid().toString()), request.getInvoiceEmail(), 3 * 30 * 24 * 60 * 60L);
        }

        //查询票折信息
        List<TicketDiscountRecordDTO> ticketRecordList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(request.getTicketDiscountNoList())) {
            ticketRecordList = ticketDiscountRecordApi.getTicketDiscountRecordByTicketNoList(request.getTicketDiscountNoList());
        }
        //获取明细信息
        List<OrderDetailChangeDTO> orderDetailChangeList = orderDetailChangeApi.listByOrderId(request.getId());
        Map<Long, OrderDetailChangeDTO> changeMap = orderDetailChangeList.stream().collect(Collectors.toMap(s -> s.getDetailId(), o -> o, (k1, k2) -> k1));

        //获取出库单发货信息
        List<Long> ids = new ArrayList<Long>() {{
            add(request.getId());
        }};
        List<OrderDeliveryErpDTO> orderDeliveryErpList = orderDeliveryErpApi.listByOrderIds(ids);
        Map<String, List<OrderDeliveryErpDTO>> orderDeliveryMap = orderDeliveryErpList.stream().collect(Collectors.groupingBy(s -> s.getDetailId() + "_" + s.getErpDeliveryNo() + "_" + s.getBatchNo()));

        List<OrderInvoiceDetailDTO> detailInvoiceList = orderInvoiceDetailApi.listByOrderIds(ids);
        //将相同的明细出库单批次拆分组合
        Map<String, OrderDeliveryErpDTO> orderDeliverySumMap = new HashMap<>();
        for (Map.Entry<String, List<OrderDeliveryErpDTO>> one : orderDeliveryMap.entrySet()){
            String key = one.getKey();
            List<OrderDeliveryErpDTO> deliveryErpList = one.getValue();
            Integer deliverySumAllQuantity = 0;
            BigDecimal cashDeliveryDiscountAmount = BigDecimal.ZERO;

            for(OrderDeliveryErpDTO deliveryErpOne :deliveryErpList ){
                deliverySumAllQuantity = deliverySumAllQuantity + deliveryErpOne.getDeliveryQuantity();
                cashDeliveryDiscountAmount = cashDeliveryDiscountAmount.add(deliveryErpOne.getCashDiscountAmount());
            }
            OrderDeliveryErpDTO deliveryErpDTO  = new OrderDeliveryErpDTO();
            deliveryErpDTO.setOrderId(deliveryErpList.get(0).getOrderId());
            deliveryErpDTO.setDetailId(deliveryErpList.get(0).getDetailId());
            deliveryErpDTO.setBatchNo(deliveryErpList.get(0).getBatchNo());
            deliveryErpDTO.setExpiryDate(deliveryErpList.get(0).getExpiryDate());
            deliveryErpDTO.setProduceDate(deliveryErpList.get(0).getProduceDate());
            deliveryErpDTO.setErpDeliveryNo(deliveryErpList.get(0).getErpDeliveryNo());
            deliveryErpDTO.setErpSendOrderId(deliveryErpList.get(0).getErpSendOrderId());
            deliveryErpDTO
                    .setDeliveryQuantity(deliverySumAllQuantity)
                    .setCashDiscountAmount(cashDeliveryDiscountAmount);
            orderDeliverySumMap.put(key,deliveryErpDTO);
        }

        //校验参数和按照相同的明细出库单批次拆分现折和票折
        validateApplyParam(request, order, ticketRecordList, orderDeliverySumMap,detailInvoiceList);

        String groupNo = noApi.gen(NoEnum.ORDER_INVOICE_GROUP_DELIVERY_NO);

        //组装OrderInvoiceDetail数据
        List<OrderInvoiceDetailDTO> orderInvoiceDetailList = getOrderInvoiceDetailList(request, changeMap, orderDeliveryErpList, groupNo,detailInvoiceList);
        log.info("申请修改开票,组装数据,orderInvoiceDetailList:{}", JSON.toJSONString(orderInvoiceDetailList));
        //保存信息
        orderInvoiceDetailApi.saveBatchDate(PojoUtils.map(orderInvoiceDetailList, SaveOrderInvoiceDetailRequest.class));

        List<SaveOrderTicketDiscountRequest> orderTicketDiscountList = new ArrayList<>();
        List<SaveOrderDetailTicketDiscountRequest> discountLists = new ArrayList<>();
        if (request.getTicketDiscountFlag().equals(OrderTicketDiscountTypeEnum.USE_TICKET_DISCOUNT.getCode())) {

            //返回使用票折明细信息
            assembleResultByTicketNo(request, ticketRecordList, request.getOpUserId(), discountLists);

            //票折使用的金额
            Map<String, BigDecimal> amountMap = new HashMap<>();
            for (SaveOrderDetailTicketDiscountRequest one : discountLists) {
                if (amountMap.containsKey(one.getTicketDiscountNo())) {
                    BigDecimal decimal = amountMap.get(one.getTicketDiscountNo()).add(one.getUseAmount());
                    amountMap.put(one.getTicketDiscountNo(), decimal);
                } else {
                    amountMap.put(one.getTicketDiscountNo(), one.getUseAmount());
                }
            }
            for (Map.Entry<String, BigDecimal> entry : amountMap.entrySet()) {
                //修改票折表中使用金额
                ticketDiscountRecordApi.updateUsedAmount(entry.getKey(), entry.getValue());
            }

            Set<String> ticketNoSet = discountLists.stream().map(o -> o.getTicketDiscountNo()).collect(Collectors.toSet());
            String ticketNo = String.join(",", ticketNoSet);

            orderApply.setTicketDiscountNo(ticketNo);

            for (String ticketOne : ticketNoSet) {
                SaveOrderTicketDiscountRequest orderTicketDiscount = new SaveOrderTicketDiscountRequest();
                orderTicketDiscount.setCashDiscountAmount(order.getCashDiscountAmount())
                        .setCreateTime(request.getOpTime())
                        .setCreateUser(request.getOpUserId())
                        .setOrderId(order.getId())
                        .setOrderNo(order.getOrderNo())
                        .setUseAmount(amountMap.get(ticketOne))
                        .setTicketDiscountNo(ticketOne)
                        .setTotalAmount(order.getTotalAmount());
                orderTicketDiscountList.add(orderTicketDiscount);
            }
        }
        //开票金额
        BigDecimal invoiceAmount = BigDecimal.ZERO;
        //现折总金额
        BigDecimal cashDiscountAmount = BigDecimal.ZERO;
        //票折总金额
        BigDecimal ticketDiscountAmount = BigDecimal.ZERO;
        for (OrderInvoiceDetailDTO one : orderInvoiceDetailList) {
            invoiceAmount = invoiceAmount.add(one.getInvoiceAmount());
            cashDiscountAmount = cashDiscountAmount.add(one.getCashDiscountAmount());
            ticketDiscountAmount = ticketDiscountAmount.add(one.getTicketDiscountAmount());
        }
        order.setInvoiceTime(request.getOpTime());
        order.setInvoiceStatus(OrderInvoiceApplyStatusEnum.PART_APPLIED.getCode());

        orderApply.setOrderId(order.getId())
                .setOrderNo(order.getOrderNo())
                .setTotalAmount(order.getTotalAmount())
                .setCashDiscountAmount(cashDiscountAmount)
                .setTicketDiscountAmount(ticketDiscountAmount)
                .setTicketDiscountFlag(request.getTicketDiscountFlag())
                .setInvoiceAmount(invoiceAmount)
                .setStatus(InvoiceApplyStatusEnum.APPLIED.getCode())
                .setInvoiceForm(request.getInvoiceForm())
                .setInvoiceSummary(StringUtils.isNotEmpty(request.getInvoiceSummary()) ? request.getInvoiceSummary() : "");

       /* if (OrderTransitionRuleCodeEnum.SALE_OUT_ORDER_OPEN_NOT_INVOICE.getCode().equals(request.getTransitionRuleCode())) {
            orderApply.setStatus(InvoiceApplyStatusEnum.NOT_NEED.getCode());
        }*/

        UpdateOrderInvoiceApplyRequest applyRequest = PojoUtils.map(orderApply, UpdateOrderInvoiceApplyRequest.class);
        OrderInvoiceApplyDTO orderInvoiceApplyDTO = orderInvoiceApplyApi.saveOrUpdateById(applyRequest);

        if (CollectionUtil.isNotEmpty(discountLists)) {
            for (SaveOrderDetailTicketDiscountRequest one : discountLists) {
                one.setApplyId(orderInvoiceApplyDTO.getId());
            }
            orderDetailTicketDiscountApi.saveBatch(discountLists);
        }

        if (CollectionUtil.isNotEmpty(orderTicketDiscountList)) {
            for (SaveOrderTicketDiscountRequest one : orderTicketDiscountList) {
                one.setApplyId(orderInvoiceApplyDTO.getId());
            }
            orderTicketDiscountApi.saveBatch(orderTicketDiscountList);
        }

        //提交票折申请时候保存订单里面发票信息
        UpdateOrderInvoiceInfoRequest orderInvoiceInfo = PojoUtils.map(order, UpdateOrderInvoiceInfoRequest.class);
        orderApi.updateOrderInvoiceInfo(orderInvoiceInfo);

        SaveOrderInvoiceLogRequest one = new SaveOrderInvoiceLogRequest();
        one.setOrderId(order.getId())
                .setCreateTime(request.getOpTime())
                .setCreateUser(request.getOpUserId())
                .setStatus(OrderInvoiceApplyStatusEnum.PART_APPLIED.getCode());

        orderInvoiceLogApi.save(one);

        //保存组信息
        SaveOrderInvoiceDeliveryGroupRequest groupRequest = new SaveOrderInvoiceDeliveryGroupRequest();
        groupRequest.setOrderId(order.getId());
        groupRequest.setApplyId(orderInvoiceApplyDTO.getId());
        groupRequest.setGroupNo(groupNo);
        groupRequest.setGroupDeliveryNos(request.getErpDeliveryNoList().stream().collect(Collectors.joining(",")));
        groupRequest.setInvoiceSummary(request.getInvoiceSummary());
        groupRequest.setOpUserId(request.getOpUserId());
        groupRequest.setOpTime(request.getOpTime());
        orderInvoiceDeliveryGroupApi.saveOne(groupRequest);
    }

    /**
     * 组装OrderInvoiceDetail数据
     *
     * @param request
     * @param changeMap
     * @param orderDeliveryErpList
     * @param groupNo
     * @return
     */
    private List<OrderInvoiceDetailDTO> getOrderInvoiceDetailList(SaveOrderTicketApplyInfoRequest request,
                                                                  Map<Long, OrderDetailChangeDTO> changeMap,
                                                                  List<OrderDeliveryErpDTO> orderDeliveryErpList ,
                                                                  String groupNo,
                                                                  List<OrderInvoiceDetailDTO> detailInvoiceList) {
        List<OrderInvoiceDetailDTO> orderInvoiceDetailList = new ArrayList<>();
        //已开票数量集合
        Map<String, Integer> invoiceQuantityMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(detailInvoiceList)) {
            invoiceQuantityMap = detailInvoiceList.stream().collect(Collectors.groupingBy(s -> s.getDetailId() + "_" + s.getErpDeliveryNo() + "_" + s.getBatchNo() + "_" + s.getErpSendOrderId(), Collectors.summingInt(s -> s.getInvoiceQuantity())));
        }
        //剩余可以开票的数量
        List<OrderDeliveryErpDTO> remainOrderDeliveryErpList = new ArrayList<>();
        for(OrderDeliveryErpDTO erpDTO : orderDeliveryErpList){
            Integer remainQuantity = invoiceQuantityMap.get(erpDTO.getDetailId() + "_" + erpDTO.getErpDeliveryNo() + "_" + erpDTO.getBatchNo() + "_" + erpDTO.getErpSendOrderId());
            erpDTO.setDeliveryQuantity(erpDTO.getDeliveryQuantity() -(remainQuantity != null ? remainQuantity : 0) );
            if(erpDTO.getDeliveryQuantity() > 0){
                remainOrderDeliveryErpList.add(erpDTO);
            }
        }
        //剩余可以开票的数据
        Map<String, List<OrderDeliveryErpDTO>> remainInvoiceMap = remainOrderDeliveryErpList.stream().collect(Collectors.groupingBy(s -> s.getDetailId() + "_" + s.getErpDeliveryNo() + "_" + s.getBatchNo()));

        for (OrderDetailTicketDiscountRequest ticketDiscountRequest : request.getTicketDiscount()) {
            OrderDetailChangeDTO changeDTO = changeMap.get(ticketDiscountRequest.getDetailId());


            BigDecimal ticketDiscountAmount = ticketDiscountRequest.getTicketDiscountAmount();
            BigDecimal invoiceAmount = ticketDiscountRequest.getInvoiceAllAmount().subtract(ticketDiscountRequest.getCashDiscountAmount()).subtract(ticketDiscountAmount);
            BigDecimal ticketGoodsPrice = invoiceAmount.divide(BigDecimal.valueOf(ticketDiscountRequest.getInvoiceAllQuantity()), 8, BigDecimal.ROUND_HALF_UP);

            BigDecimal ticketAllBatchAmount = BigDecimal.ZERO;

            for (int i = 0; i < ticketDiscountRequest.getSaveOrderTicketBatchList().size(); i++) {
                SaveOrderTicketBatchRequest batchRequest = ticketDiscountRequest.getSaveOrderTicketBatchList().get(i);
                if (batchRequest.getInvoiceQuantity().compareTo(0) > 0) {
                    //批次下面开了多少票
                    Integer batchAllQuantity = 0;
                    //批次现折使用总金额
                    BigDecimal cashAllBatchAmount = BigDecimal.ZERO;

                    List<OrderDeliveryErpDTO> orderInvoiceErpList = remainInvoiceMap.get(ticketDiscountRequest.getDetailId() + "_" + ticketDiscountRequest.getErpDeliveryNo() + "_" + batchRequest.getBatchNo());
                    for(int n = 0; n < orderInvoiceErpList.size(); n++){
                        Integer batchRemainQuantity  = batchRequest.getInvoiceQuantity() - batchAllQuantity;
                        if((batchRequest.getInvoiceQuantity() - batchAllQuantity) == 0){
                            break ;
                        }
                        OrderDeliveryErpDTO OrderDeliveryErpOne = orderInvoiceErpList.get(n);
                        OrderInvoiceDetailDTO detailOne = new OrderInvoiceDetailDTO();
                        detailOne.setOrderId(OrderDeliveryErpOne.getOrderId());
                        detailOne.setDetailId(OrderDeliveryErpOne.getDetailId());
                        detailOne.setGroupNo(groupNo);
                        detailOne.setErpDeliveryNo(OrderDeliveryErpOne.getErpDeliveryNo());
                        detailOne.setStandardId(changeDTO.getStandardId());
                        detailOne.setBatchNo(batchRequest.getBatchNo());
                        detailOne.setGoodsId(changeDTO.getGoodsId());
                        detailOne.setErpSendOrderId(OrderDeliveryErpOne.getErpSendOrderId());
                        detailOne.setGoodsErpCode(changeDTO.getGoodsErpCode());
                        detailOne.setGoodsPrice(changeDTO.getGoodsPrice());
                        detailOne.setGoodsQuantity(OrderDeliveryErpOne.getDeliveryQuantity());
                        detailOne.setGoodsAmount(changeDTO.getGoodsPrice().multiply(BigDecimal.valueOf(detailOne.getGoodsQuantity())).setScale(2, BigDecimal.ROUND_HALF_UP));
                        detailOne.setTicketDiscountType(ticketDiscountRequest.getInvoiceDiscountType());
                        detailOne.setTicketDiscountRate(ticketDiscountRequest.getTicketDiscountRate());
                        detailOne.setTicketDiscountPrice(ticketGoodsPrice);
                        detailOne.setCreateUser(request.getOpUserId());
                        detailOne.setCreateTime(request.getOpTime());

                        if(batchRemainQuantity > OrderDeliveryErpOne.getDeliveryQuantity()){
                            //开票数量就是orderInvoiceErpList.get(n).getDeliveryQuantity()数量

                            detailOne.setInvoiceQuantity( OrderDeliveryErpOne.getDeliveryQuantity());
                            //现折金额
                            BigDecimal batchCashAmount = batchRequest.getCashDiscountAmount().multiply(BigDecimal.valueOf(detailOne.getInvoiceQuantity()))
                                    .divide(BigDecimal.valueOf(batchRequest.getInvoiceQuantity()), 2, BigDecimal.ROUND_HALF_UP);
                            detailOne.setCashDiscountAmount(batchCashAmount);

                            //票折金额
                            BigDecimal batchTicketAmount = ticketDiscountAmount.multiply(BigDecimal.valueOf(detailOne.getInvoiceQuantity()))
                                    .divide(BigDecimal.valueOf(ticketDiscountRequest.getInvoiceAllQuantity()), 2, BigDecimal.ROUND_HALF_UP);

                            detailOne.setTicketDiscountAmount(batchTicketAmount);

                            detailOne.setInvoiceAmount(changeDTO.getGoodsPrice().multiply(BigDecimal.valueOf(detailOne.getInvoiceQuantity())).setScale(2, BigDecimal.ROUND_HALF_UP)
                                    .subtract(detailOne.getTicketDiscountAmount())
                                    .subtract(detailOne.getCashDiscountAmount()));

                            //批次现折总和
                            cashAllBatchAmount = cashAllBatchAmount.add(batchCashAmount);
                            //批次现折总和
                            ticketAllBatchAmount = ticketAllBatchAmount.add(batchTicketAmount);
                            //批次下需要分摊多少数量
                            batchAllQuantity = batchAllQuantity + orderInvoiceErpList.get(n).getDeliveryQuantity();
                        }else{
                            //开票数量就是 batchRemainQuantity
                            detailOne.setInvoiceQuantity(batchRemainQuantity);
                            //现折金额
                            BigDecimal batchCashAmount = batchRequest.getCashDiscountAmount().subtract(cashAllBatchAmount);
                            detailOne.setCashDiscountAmount(batchCashAmount);
                            if(i == ticketDiscountRequest.getSaveOrderTicketBatchList().size() - 1){
                                detailOne.setTicketDiscountAmount(ticketDiscountAmount.subtract(ticketAllBatchAmount));
                            }else{
                                //票折金额
                                BigDecimal batchTicketAmount = ticketDiscountAmount.multiply(BigDecimal.valueOf(detailOne.getInvoiceQuantity()))
                                        .divide(BigDecimal.valueOf(ticketDiscountRequest.getInvoiceAllQuantity()), 2, BigDecimal.ROUND_HALF_UP);
                                detailOne.setTicketDiscountAmount(batchTicketAmount);
                            }
                            //批次现折总和
                            ticketAllBatchAmount = ticketAllBatchAmount.add(detailOne.getTicketDiscountAmount());

                            detailOne.setInvoiceAmount(changeDTO.getGoodsPrice().multiply(BigDecimal.valueOf(detailOne.getInvoiceQuantity())).setScale(2, BigDecimal.ROUND_HALF_UP)
                                    .subtract(detailOne.getTicketDiscountAmount())
                                    .subtract(detailOne.getCashDiscountAmount()));

                            batchAllQuantity = batchAllQuantity + batchRemainQuantity ;
                        }
                        orderInvoiceDetailList.add(detailOne);
                    }

                }
            }
        }
        return orderInvoiceDetailList;
    }

    /**
     * 校验参数是否正确
     *
     * @param request
     * @param order
     * @param ticketRecordList
     */
    private void validateApplyParam(SaveOrderTicketApplyInfoRequest request,
                                    OrderDTO order,
                                    List<TicketDiscountRecordDTO> ticketRecordList,
                                    Map<String, OrderDeliveryErpDTO> orderDeliverySumMap,
                                    List<OrderInvoiceDetailDTO> detailInvoiceList) {
        //整体开票金额
        BigDecimal invoiceAmount = BigDecimal.ZERO;
        //整体开票现折
        BigDecimal cashDiscountAmount = BigDecimal.ZERO;
        //票折总金额
        BigDecimal ticketAllAmount = BigDecimal.ZERO;

        List<OrderDetailChangeDTO> orderDetailChangeList = orderDetailChangeApi.listByOrderId(request.getId());
        Map<Long, OrderDetailChangeDTO> changeList = orderDetailChangeList.stream().collect(Collectors.toMap(s -> s.getDetailId(), o -> o, (k1, k2) -> k1));

        Map<String, List<OrderInvoiceDetailDTO>> orderInvoiceDetailMap = new HashMap<>();

        if (CollectionUtil.isNotEmpty(detailInvoiceList)) {
            orderInvoiceDetailMap = detailInvoiceList.stream().collect(Collectors.groupingBy(s -> s.getDetailId() + "_" + s.getErpDeliveryNo() + "_" + s.getBatchNo()));
        }
        //判断转换规则需要一样
        List<OrderInvoiceApplyDTO> orderInvoiceApplyList = orderInvoiceApplyApi.listByOrderId(order.getId());
        if(CollectionUtil.isNotEmpty(orderInvoiceApplyList)){
            if( OrderTransitionRuleCodeEnum.getByCode(orderInvoiceApplyList.get(orderInvoiceApplyList.size()-1).getTransitionRuleCode()) != OrderTransitionRuleCodeEnum.SALE_OUT_ORDER_OPEN_NOT_INVOICE &&
                    !request.getTransitionRuleCode().equals(orderInvoiceApplyList.get(orderInvoiceApplyList.size()-1).getTransitionRuleCode())){
                throw new BusinessException(OrderErrorCode.ERP_TRANSITION_RULE_CODE_ERROR);
            }
        }

        for (OrderDetailTicketDiscountRequest ticketDiscountOne : request.getTicketDiscount()) {
            OrderDetailChangeDTO changeDTO = changeList.get(ticketDiscountOne.getDetailId());
            if (changeDTO == null) {
                throw new BusinessException(OrderErrorCode.ORDER_INFO_PARAM_ERROR);
            }

            ticketDiscountOne.setGoodsId(changeDTO.getGoodsId());
            ticketDiscountOne.setGoodsPrice(changeDTO.getGoodsPrice());
            //当前开票金额
            BigDecimal invoiceAllAmount = BigDecimal.ZERO;
            //当前开票数量
            Integer invoiceAllQuantity = 0;
            //当前开票现折金额
            BigDecimal cashAllAmount = BigDecimal.ZERO;

            for (SaveOrderTicketBatchRequest batchOne : ticketDiscountOne.getSaveOrderTicketBatchList()) {
                OrderDeliveryErpDTO orderDeliveryErpOne = orderDeliverySumMap.get(ticketDiscountOne.getDetailId() + "_" + ticketDiscountOne.getErpDeliveryNo() + "_" + batchOne.getBatchNo());
                if (orderDeliveryErpOne == null) {
                    throw new BusinessException(OrderErrorCode.ORDER_INFO_PARAM_ERROR);
                }
                List<OrderInvoiceDetailDTO> invoiceDetailList = orderInvoiceDetailMap.get(ticketDiscountOne.getDetailId() + "_" + ticketDiscountOne.getErpDeliveryNo() + "_" + batchOne.getBatchNo());
                //已开票数量
                Integer pastInvoiceQuantity = 0;
                //已开现折金额
                BigDecimal pastCashDiscountAmount = BigDecimal.ZERO;
                if (CollectionUtil.isNotEmpty(invoiceDetailList)) {
                    for (OrderInvoiceDetailDTO detailOne : invoiceDetailList) {
                        pastInvoiceQuantity = pastInvoiceQuantity + detailOne.getInvoiceQuantity();
                        pastCashDiscountAmount = pastCashDiscountAmount.add(detailOne.getCashDiscountAmount());
                    }
                }

                if (orderDeliveryErpOne.getDeliveryQuantity().compareTo(pastInvoiceQuantity + batchOne.getInvoiceQuantity()) == 0) {

                    batchOne.setCashDiscountAmount(orderDeliveryErpOne.getCashDiscountAmount().subtract(pastCashDiscountAmount));

                } else if (orderDeliveryErpOne.getDeliveryQuantity().compareTo(pastInvoiceQuantity + batchOne.getInvoiceQuantity()) > 0) {

                    BigDecimal cashDiscountOne = BigDecimal.valueOf(batchOne.getInvoiceQuantity()).multiply(orderDeliveryErpOne.getCashDiscountAmount())
                            .divide(BigDecimal.valueOf(orderDeliveryErpOne.getDeliveryQuantity()), 2, BigDecimal.ROUND_HALF_UP);
                    batchOne.setCashDiscountAmount(cashDiscountOne);

                } else if (orderDeliveryErpOne.getDeliveryQuantity().compareTo(pastInvoiceQuantity + batchOne.getInvoiceQuantity()) < 0) {
                    throw new BusinessException(OrderErrorCode.ORDER_INVOICE_QUANTITY_ERROR);
                }
                //设置每个批次开票金额
                batchOne.setInvoiceAmount(changeDTO.getGoodsPrice().multiply(BigDecimal.valueOf(batchOne.getInvoiceQuantity())).setScale(2, BigDecimal.ROUND_HALF_UP));

                invoiceAllQuantity = invoiceAllQuantity + batchOne.getInvoiceQuantity();
                invoiceAllAmount = invoiceAllAmount.add(batchOne.getInvoiceAmount());
                invoiceAmount = invoiceAmount.add(batchOne.getInvoiceAmount());
                cashAllAmount = cashAllAmount.add(batchOne.getCashDiscountAmount());
                cashDiscountAmount = cashDiscountAmount.add(batchOne.getCashDiscountAmount());

            }
            ticketDiscountOne.setInvoiceAllAmount(invoiceAllAmount);
            ticketDiscountOne.setInvoiceAllQuantity(invoiceAllQuantity);
            ticketDiscountOne.setCashDiscountAmount(cashAllAmount);

            if (request.getTicketDiscountFlag().equals(OrderTicketDiscountTypeEnum.USE_TICKET_DISCOUNT.getCode())) {
                //票折信息判断
                if (CollectionUtil.isEmpty(ticketRecordList)) {
                    throw new BusinessException(OrderErrorCode.ORDER_THAN_TICKET_DISCOUNT_NOT_EXISTS);
                }

                if (OrderInvoiceDiscountTypeEnum.USE_AMOUNT_TYPE.getCode().equals(ticketDiscountOne.getInvoiceDiscountType())) {
                    BigDecimal ticketDiscountRate = ticketDiscountOne.getTicketDiscountAmount()
                            .multiply(BigDecimal.valueOf(100)).divide(invoiceAllAmount, 2, BigDecimal.ROUND_HALF_UP);
                    ticketDiscountOne.setTicketDiscountRate(ticketDiscountRate);
                } else {
                    //票折折扣比率
                    BigDecimal discountRate = ticketDiscountOne.getTicketDiscountRate().divide(BigDecimal.valueOf(100), 4, BigDecimal.ROUND_HALF_UP);
                    //票折折扣金额
                    BigDecimal ticketDiscountAmount = discountRate.multiply(invoiceAllAmount).setScale(2, BigDecimal.ROUND_HALF_UP);
                    ticketDiscountOne.setTicketDiscountAmount(ticketDiscountAmount);
                }
                ticketAllAmount = ticketAllAmount.add(ticketDiscountOne.getTicketDiscountAmount());
            }else{
                ticketDiscountOne.setTicketDiscountAmount(BigDecimal.ZERO);
                ticketDiscountOne.setTicketDiscountRate(BigDecimal.ZERO);
            }
            if (ticketDiscountOne.getCashDiscountAmount().add(ticketDiscountOne.getTicketDiscountAmount())
                    .compareTo(invoiceAllAmount) > 0) {
                throw new BusinessException(OrderErrorCode.ORDER_INFO_PARAM_ERROR);
            }
        }
        //判断是否超过金额限制

        if (StringUtils.isNotEmpty(ruleMap.get(request.getTransitionRuleCode()))) {
            BigDecimal amount = new BigDecimal(ruleMap.get(request.getTransitionRuleCode()));
            if (invoiceAmount.subtract(cashDiscountAmount).subtract(ticketAllAmount).compareTo(amount) > 0) {
                throw new BusinessException(OrderErrorCode.ORDER_INVOICE_QUANTITY_ERROR);
            }
        }

        //判断票折是否满足使用
        if (request.getTicketDiscountFlag().equals(OrderTicketDiscountTypeEnum.USE_TICKET_DISCOUNT.getCode())) {
            //校验选择票折金额是否够用
            BigDecimal choiceTicketDiscountAmount = BigDecimal.ZERO;
            for (TicketDiscountRecordDTO record : ticketRecordList) {
                choiceTicketDiscountAmount = choiceTicketDiscountAmount.add(record.getAvailableAmount());
            }
            if (choiceTicketDiscountAmount.compareTo(ticketAllAmount) < 0) {
                throw new BusinessException(OrderErrorCode.ORDER_DISCOUNT_MORE_THAN_TICKET_DISCOUNT_AMOUNT);
            }
        }

        Map<Long, List<OrderDetailTicketDiscountRequest>> mapPrice = request.getTicketDiscount().stream().collect(Collectors.groupingBy(s -> s.getGoodsId()));
        //最低折扣率
        List<Long> goodsList = orderDetailChangeList.stream().map(OrderDetailChangeDTO::getGoodsId).collect(Collectors.toList());
        Map<Long, BigDecimal> keepDiscountRate = goodsDiscountRateApi.queryGoodsDiscountRateMap(order.getBuyerEid(), goodsList);

        //招标挂网价
        EnterpriseDTO enterprise = enterpriseApi.getById(order.getBuyerEid());
        Map<Long, BigDecimal> bidingPrice = goodsBiddingPriceApi.queryGoodsBidingPriceByLocation(goodsList, enterprise.getProvinceCode());

        BigDecimal bigDecimalRate = new BigDecimal(0.5);
        for (Map.Entry<Long, List<OrderDetailTicketDiscountRequest>> entry : mapPrice.entrySet()) {
            List<OrderDetailTicketDiscountRequest> entryValueList = entry.getValue();
            BigDecimal deliveryAmount = BigDecimal.ZERO;
            Integer deliveryNumber = 0;
            BigDecimal goodsPrice = BigDecimal.ZERO;
            for (OrderDetailTicketDiscountRequest one : entryValueList) {
                deliveryAmount = deliveryAmount.add(one.getInvoiceAllAmount().subtract(one.getCashDiscountAmount()).subtract(one.getTicketDiscountAmount()));
                deliveryNumber = deliveryNumber + one.getInvoiceAllQuantity();
                goodsPrice = one.getGoodsPrice();
            }
            BigDecimal discountPrice = deliveryAmount.divide(
                    BigDecimal.valueOf(deliveryNumber), 4, BigDecimal.ROUND_HALF_UP);


            if (keepDiscountRate.get(entry.getKey()) != null && keepDiscountRate.get(entry.getKey()).compareTo(BigDecimal.ZERO) != 0) {
                bigDecimalRate = keepDiscountRate.get(entry.getKey());
            }
            getCompareToPrice(discountPrice, goodsPrice, bigDecimalRate, bidingPrice.get(entry.getKey()));
        }
    }


    /**
     * 按照单号平均分配
     *
     * @param
     * @param ticketRecordList
     * @param opUserId
     */
    private void assembleResultByTicketNo(SaveOrderTicketApplyInfoRequest request,
                                          List<TicketDiscountRecordDTO> ticketRecordList,
                                          Long opUserId,
                                          List<SaveOrderDetailTicketDiscountRequest> discountLists) {


        //标记下标,分配在那里了
        Integer index = 0;
        //小于的时候已经分配过的量
        BigDecimal ticketUsedAmount = BigDecimal.ZERO;

        for (OrderDetailTicketDiscountRequest one : request.getTicketDiscount()) {
            //大于的时候已经被分配的量
            BigDecimal usedAmount = BigDecimal.ZERO;
            for (int i = index; i < ticketRecordList.size(); i++) {
                TicketDiscountRecordDTO record = ticketRecordList.get(i);
                BigDecimal subtract = record.getTotalAmount().subtract(record.getUsedAmount()).subtract(ticketUsedAmount);
                BigDecimal remainAmount = one.getTicketDiscountAmount().subtract(usedAmount);
                if (remainAmount.compareTo(subtract) > 0) {
                    usedAmount = usedAmount.add(subtract);
                    //已经分配过的量以使用完
                    ticketUsedAmount = BigDecimal.ZERO;
                    index = index + 1;
                    SaveOrderDetailTicketDiscountRequest resultRecord = getDetailTicketDiscountResult(one, record, subtract, opUserId, request.getId());
                    discountLists.add(resultRecord);
                } else if (remainAmount.compareTo(subtract) < 0) {
                    ticketUsedAmount = ticketUsedAmount.add(remainAmount);
                    SaveOrderDetailTicketDiscountRequest resultRecord = getDetailTicketDiscountResult(one, record, remainAmount, opUserId, request.getId());
                    discountLists.add(resultRecord);
                    break;
                } else {
                    index = index + 1;
                    //已经分配过的量以使用完
                    ticketUsedAmount = BigDecimal.ZERO;
                    SaveOrderDetailTicketDiscountRequest resultRecord = getDetailTicketDiscountResult(one, record, subtract, opUserId, request.getId());
                    discountLists.add(resultRecord);
                    break;
                }
            }
        }
    }

    /**
     * 组装数据
     *
     * @param one
     * @param record
     * @param subtract
     * @return
     */
    private SaveOrderDetailTicketDiscountRequest getDetailTicketDiscountResult(OrderDetailTicketDiscountRequest one,
                                                                               TicketDiscountRecordDTO record,
                                                                               BigDecimal subtract,
                                                                               Long opUserId,
                                                                               Long orderId) {
        SaveOrderDetailTicketDiscountRequest lastOne = new SaveOrderDetailTicketDiscountRequest();
        lastOne.setOrderId(orderId)
                .setErpDeliveryNo(one.getErpDeliveryNo())
                .setDetailId(one.getDetailId())
                .setUseAmount(subtract)
                .setTicketDiscountNo(record.getTicketDiscountNo())
                .setCreateTime(new Date())
                .setCreateUser(opUserId);
        return lastOne;
    }


    private void getCompareToPrice(BigDecimal discountPrice, BigDecimal goodsPrice,
                                   BigDecimal keepDiscountRate, BigDecimal bigPrice) {

        //现金折扣比率
        /*BigDecimal discountPrice = (detail.getDeliveryAmount().subtract(detail.getDeliveryCashDiscountAmount().add(totalDiscountAmount))).divide(
                BigDecimal.valueOf(detail.getDeliveryQuantity()), 4, BigDecimal.ROUND_HALF_UP);*/


        if (keepDiscountRate != null) {
            if (discountPrice.compareTo(
                    keepDiscountRate.multiply(goodsPrice)) <= 0) {
                throw new BusinessException(OrderErrorCode.ORDER_PRICE_LESS_THAN_GOODS_LOWEST_PRICE);
            }

        }

        if (bigPrice != null && bigPrice.compareTo(BigDecimal.ZERO) != 0) {
            if (discountPrice.compareTo(bigPrice) < 0) {
                throw new BusinessException(OrderErrorCode.ORDER_PRICE_LESS_THAN_RED_PRICE);
            }
        }


    }

    private void validateApplyInvoiceStatus(OrderDTO order) {
        //只有收货和发货状态才能申请开票

        if (!(order.getOrderStatus().equals(OrderStatusEnum.DELIVERED.getCode())
                || order.getOrderStatus().equals(OrderStatusEnum.RECEIVED.getCode())
                || order.getOrderStatus().equals(OrderStatusEnum.PARTDELIVERED.getCode()))) {

            throw new BusinessException(OrderErrorCode.ORDER_INFO_STATUS_ERROR);
        }
        //只有待审核或者不存在可以申请提交
        if (!(OrderInvoiceApplyStatusEnum.getByCode(order.getInvoiceStatus()) == OrderInvoiceApplyStatusEnum.PENDING_APPLY
                || OrderInvoiceApplyStatusEnum.getByCode(order.getInvoiceStatus()) == OrderInvoiceApplyStatusEnum.DEFAULT_VALUE_APPLY)) {
            throw new BusinessException(OrderErrorCode.ORDER_INFO_STATUS_ERROR);
        }


    }

}
