package com.yiling.order.order.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.bo.OrderDetailChangeBO;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderInvoiceApplyDTO;
import com.yiling.order.order.dto.OrderInvoicePullErpDTO;
import com.yiling.order.order.dto.OrderReturnDetailDTO;
import com.yiling.order.order.dto.request.CompleteErpOrderInvoiceRequest;
import com.yiling.order.order.dto.request.OrderPullErpDeliveryPageRequest;
import com.yiling.order.order.dto.request.OrderPullErpPageRequest;
import com.yiling.order.order.dto.request.UpdateErpOrderDeliveryRequest;
import com.yiling.order.order.dto.request.UpdateErpPushStatusRequest;
import com.yiling.order.order.entity.OrderDO;
import com.yiling.order.order.entity.OrderDeliveryReceivableDO;
import com.yiling.order.order.entity.OrderDetailChangeDO;
import com.yiling.order.order.entity.OrderDetailTicketDiscountDO;
import com.yiling.order.order.entity.OrderInvoiceApplyDO;
import com.yiling.order.order.entity.OrderInvoiceDO;
import com.yiling.order.order.entity.OrderInvoiceDeliveryGroupDO;
import com.yiling.order.order.entity.OrderInvoiceDetailDO;
import com.yiling.order.order.entity.OrderReturnDO;
import com.yiling.order.order.entity.OrderReturnDetailDO;
import com.yiling.order.order.entity.OrderTicketDiscountDO;
import com.yiling.order.order.enums.CustomerConfirmEnum;
import com.yiling.order.order.enums.InvoiceApplyStatusEnum;
import com.yiling.order.order.enums.OrderAuditStatusEnum;
import com.yiling.order.order.enums.OrderErpPushStatusEnum;
import com.yiling.order.order.enums.OrderErrorCode;
import com.yiling.order.order.enums.OrderInvoiceApplyStatusEnum;
import com.yiling.order.order.enums.OrderReturnStatusEnum;
import com.yiling.order.order.enums.OrderStatusEnum;
import com.yiling.order.order.enums.OrderTransitionRuleCodeEnum;
import com.yiling.order.order.service.OrderDeliveryReceivableService;
import com.yiling.order.order.service.OrderDetailChangeService;
import com.yiling.order.order.service.OrderDetailTicketDiscountService;
import com.yiling.order.order.service.OrderErpService;
import com.yiling.order.order.service.OrderInvoiceApplyService;
import com.yiling.order.order.service.OrderInvoiceDeliveryGroupService;
import com.yiling.order.order.service.OrderInvoiceDetailService;
import com.yiling.order.order.service.OrderInvoiceService;
import com.yiling.order.order.service.OrderReturnDetailService;
import com.yiling.order.order.service.OrderReturnService;
import com.yiling.order.order.service.OrderService;
import com.yiling.order.order.service.OrderTicketDiscountService;
import com.yiling.order.order.service.TicketDiscountRecordService;

import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;


/**
 * 订单ERP对接实现
 *
 * @author:wei.wang
 * @date:2021/8/3
 */
@Service
@Slf4j
public class OrderErpServiceImpl implements OrderErpService {

    @Autowired
    private OrderService                     orderService;
    @Autowired
    private OrderInvoiceApplyService         orderInvoiceApplyService;
    @Autowired
    private OrderDetailTicketDiscountService orderDetailTicketDiscountService;
    @Autowired
    private TicketDiscountRecordService      ticketDiscountRecordService;
    @Autowired
    private OrderInvoiceService              orderInvoiceService;
    @Autowired
    private OrderReturnService               orderReturnService;
    @Autowired
    private OrderDetailChangeService         orderDetailChangeService;
    @Autowired
    private OrderTicketDiscountService       orderTicketDiscountService;
    @Autowired
    private OrderReturnDetailService         orderReturnDetailService;
    @Autowired
    private OrderInvoiceDetailService        orderInvoiceDetailService;
    @Autowired
    private OrderDeliveryReceivableService   orderDeliveryReceivableService;
    @Autowired
    private OrderInvoiceDeliveryGroupService orderInvoiceDeliveryGroupService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public  Map<String, Object>  updateErpReceivableNoByDeliveryNo(String erpDeliveryNo, String erpReceivableNo) {
        log.info("出库转应收erpDeliveryNo:{}", erpDeliveryNo);
        Map<String, Object> result = new HashMap<>();

        List<OrderDeliveryReceivableDO> erpDeliveryNoList = this.getByErpDeliveryNo(erpDeliveryNo);
        Set<Long> orderIdSet = erpDeliveryNoList.stream().map(OrderDeliveryReceivableDO::getOrderId).collect(Collectors.toSet());
        if (orderIdSet.size() != 1) {
            throw new BusinessException(OrderErrorCode.ERP_DELIVERY_NO_REPEAT_ERROR);
        }
        Long orderId = erpDeliveryNoList.get(0).getOrderId();

        result.put("orderId", orderId);
        result.put("ticketDiscountAmount", BigDecimal.ZERO);

        QueryWrapper<OrderDeliveryReceivableDO> deliveryReceivableWrapper = new QueryWrapper<>();
        deliveryReceivableWrapper.lambda().eq(OrderDeliveryReceivableDO::getErpDeliveryNo, erpDeliveryNo)
                .eq(OrderDeliveryReceivableDO :: getErpReceivableNo,erpReceivableNo)
                .last(" limit 1 ");
        OrderDeliveryReceivableDO one = orderDeliveryReceivableService.getOne(deliveryReceivableWrapper);
        if(one != null){
            log.error("出库转应收重复推送相同数据,出库单erpDeliveryNo：{},应收单erpReceivableNo：{}",erpDeliveryNo,erpReceivableNo);
            return result;
        }

        QueryWrapper<OrderInvoiceApplyDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderInvoiceApplyDO::getOrderId, orderId)
                .eq(OrderInvoiceApplyDO::getStatus,InvoiceApplyStatusEnum.APPLIED.getCode())
                .last(" limit 1 ");
        OrderInvoiceApplyDO applyOne = orderInvoiceApplyService.getOne(wrapper);
        log.info("获取申请信息OrderInvoiceApplyDO:{}", JSON.toJSONString(applyOne));

        Boolean flag = false;
        List<OrderDeliveryReceivableDO> saveList = new ArrayList<>();
        for (OrderDeliveryReceivableDO receivableDO : erpDeliveryNoList) {
            if (erpReceivableNo.equals(receivableDO.getErpReceivableNo())) {
                flag = true;
            }
            if (CollectionUtil.isEmpty(saveList) && StringUtils.isEmpty(receivableDO.getErpReceivableNo())) {
                receivableDO.setErpReceivableNo(erpReceivableNo);
                receivableDO.setApplyId(applyOne.getId());
                receivableDO.setErpReceivableFlag(0);
                receivableDO.setCreateUser(0L);
                receivableDO.setCreateTime(new Date());
                receivableDO.setUpdateUser(0L);
                receivableDO.setUpdateTime(new Date());
                saveList.add(receivableDO);
            }
        }

        if (!flag) {
            if (CollectionUtil.isNotEmpty(saveList)) {
                orderDeliveryReceivableService.updateBatchById(saveList);
            } else {
                OrderDeliveryReceivableDO deliveryReceivableDO = new OrderDeliveryReceivableDO();
                deliveryReceivableDO.setOrderId(orderId);
                deliveryReceivableDO.setApplyId(applyOne.getId());
                deliveryReceivableDO.setErpDeliveryNo(erpDeliveryNo);
                deliveryReceivableDO.setErpReceivableNo(erpReceivableNo);
                deliveryReceivableDO.setErpReceivableFlag(0);
                deliveryReceivableDO.setOpUserId(0L);
                deliveryReceivableDO.setOpTime(new Date());
                orderDeliveryReceivableService.save(deliveryReceivableDO);
            }
        }
        QueryWrapper<OrderInvoiceDeliveryGroupDO> groupDOQueryWrapper = new QueryWrapper<>();
        groupDOQueryWrapper.lambda().eq(OrderInvoiceDeliveryGroupDO :: getApplyId,applyOne.getId());
        List<OrderInvoiceDeliveryGroupDO> groupList = orderInvoiceDeliveryGroupService.list(groupDOQueryWrapper);
        List<String> erpDeliveryList = new ArrayList<>();
        for(OrderInvoiceDeliveryGroupDO groupOne : groupList ){
            List<String> list = Arrays.asList(groupOne.getGroupDeliveryNos().split(","));
            erpDeliveryList.addAll(list);
        }

        if(!erpDeliveryList.contains(erpDeliveryNo)){
            throw new BusinessException(OrderErrorCode.ERP_DELIVERY_NO_NOT_EXISTS);
        }

            //判断是否全部转应收成功
        QueryWrapper<OrderDeliveryReceivableDO> receivableWrapper = new QueryWrapper<>();
        receivableWrapper.lambda().eq(OrderDeliveryReceivableDO::getApplyId, applyOne.getId())
                .in(OrderDeliveryReceivableDO :: getErpDeliveryNo,erpDeliveryList)
                .eq(OrderDeliveryReceivableDO::getErpReceivableFlag, 0);
        List<OrderDeliveryReceivableDO> list = orderDeliveryReceivableService.list(receivableWrapper);

        if (CollectionUtil.isNotEmpty(list) && list.size() == erpDeliveryList.size() ) {
            OrderDO order = orderService.getById(orderId);
            Boolean afterReceiveFlag = true;
            if (OrderStatusEnum.DELIVERED.getCode() <= order.getOrderStatus()) {
                afterReceiveFlag = false;
            }
            //票折金额
            BigDecimal ticketDiscountAmount = BigDecimal.ZERO;
            //对应申请应收单票折
            List<OrderDetailTicketDiscountDO> orderTicketDiscountList = orderDetailTicketDiscountService.listByApplyId(applyOne.getId());
            if (CollectionUtil.isNotEmpty(orderTicketDiscountList)) {
                Map<Long, Double> changeMap = orderTicketDiscountList.stream().collect(Collectors.groupingBy(k -> k.getDetailId(), Collectors.summingDouble(v -> Double.valueOf(v.getUseAmount().toString()))));
                for (Map.Entry<Long, Double> changeOne : changeMap.entrySet()) {
                    //修改票折信息
                    log.info("申请applyd:{},应收单转换后处理票折金额参数：{}", applyOne.getId(), JSON.toJSONString(changeOne));
                    orderDetailChangeService.updateErpReceivableNoTicketAmount(changeOne.getKey(), BigDecimal.valueOf(changeOne.getValue()), afterReceiveFlag);
                    ticketDiscountAmount = ticketDiscountAmount.add(BigDecimal.valueOf(changeOne.getValue()));
                }
            }

            result.put("ticketDiscountAmount", ticketDiscountAmount);

            //不开票状态处理
            if(OrderTransitionRuleCodeEnum.SALE_OUT_ORDER_OPEN_NOT_INVOICE.getCode().equals(applyOne.getTransitionRuleCode())){
                //判断是否全部发货
                List<OrderDetailChangeDO> orderDetailChangeList = orderDetailChangeService.listByOrderId(orderId);
                //发货数量
                Integer deliveryQuantity = orderDetailChangeList.stream().mapToInt(OrderDetailChangeDO::getDeliveryQuantity).sum();
                List<OrderInvoiceDetailDO> orderInvoiceDetailList = orderInvoiceDetailService.listByOrderIds(new ArrayList<Long>() {{
                    add(orderId);
                }});
                //开票数量
                Integer invoiceQuantity = 0;
                //开票金额
                BigDecimal invoiceAmount = BigDecimal.ZERO;
                //开票使用金额
                BigDecimal useTicketDiscountAmount = BigDecimal.ZERO;
                if (CollectionUtil.isNotEmpty(orderInvoiceDetailList)) {
                    for (OrderInvoiceDetailDO invoiceDetailOne : orderInvoiceDetailList) {
                        if (StringUtils.isBlank(invoiceDetailOne.getBatchNo()) && invoiceDetailOne.getInvoiceQuantity().compareTo(0) == 0) {
                            invoiceQuantity = invoiceQuantity + invoiceDetailOne.getGoodsQuantity();
                        }else{
                            invoiceQuantity = invoiceQuantity + invoiceDetailOne.getInvoiceQuantity();
                        }
                        invoiceAmount = invoiceAmount.add(invoiceDetailOne.getInvoiceAmount());
                        useTicketDiscountAmount = useTicketDiscountAmount.add(invoiceDetailOne.getTicketDiscountAmount());
                    }
                }
                log.info("不开票状态下发货数量deliveryQuantity:{}，开票数量invoiceQuantity:{}",deliveryQuantity,invoiceQuantity);
                if(deliveryQuantity.compareTo(invoiceQuantity) == 0){
                    order.setInvoiceAmount(invoiceAmount);
                    order.setInvoiceStatus(OrderInvoiceApplyStatusEnum.NOT_NEED.getCode());
                    order.setTicketDiscountAmount(useTicketDiscountAmount);
                    orderService.updateById(order);
                    saveTicketReturnOrder(order.getId());
                }else{
                    order.setInvoiceStatus(OrderInvoiceApplyStatusEnum.PENDING_APPLY.getCode());
                    orderService.updateById(order);
                }
            }
            //不开票将申请状态改为不开票状态
            if(OrderTransitionRuleCodeEnum.SALE_OUT_ORDER_OPEN_NOT_INVOICE.getCode().equals(applyOne.getTransitionRuleCode())){
                applyOne.setStatus(OrderInvoiceApplyStatusEnum.NOT_NEED.getCode());
                orderInvoiceApplyService.updateById(applyOne);
            }
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> removeErpReceivableNo(String erpReceivableNo) {
        log.info("删除应收单erpReceivableNo:{}", erpReceivableNo);
        Map<String, Object> result = new HashMap<>();

        QueryWrapper<OrderDeliveryReceivableDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(OrderDeliveryReceivableDO::getErpReceivableNo, erpReceivableNo)
                .orderByDesc(OrderDeliveryReceivableDO::getUpdateTime);
        List<OrderDeliveryReceivableDO> receivableList = orderDeliveryReceivableService.list(queryWrapper);
        if (CollectionUtil.isNotEmpty(receivableList)) {
            Set<Long> applyIdSet = receivableList.stream().map(OrderDeliveryReceivableDO::getApplyId).collect(Collectors.toSet());
            if (applyIdSet.size() != 1) {
                throw new BusinessException(OrderErrorCode.RECEIVABLE_ORDER_REPEAT_ERROR);
            }

            Long applyId = receivableList.get(0).getApplyId();
            Long orderId = receivableList.get(0).getOrderId();
            OrderDO order = orderService.getById(orderId);

            result.put("orderId", orderId);
            result.put("ticketDiscountAmount", BigDecimal.ZERO);

            //判断这个申请里面的出库单是否全部回写应收单
            QueryWrapper<OrderInvoiceDeliveryGroupDO> groupDOQueryWrapper = new QueryWrapper<>();
            groupDOQueryWrapper.lambda().eq(OrderInvoiceDeliveryGroupDO :: getApplyId,applyId);
            List<OrderInvoiceDeliveryGroupDO> groupList = orderInvoiceDeliveryGroupService.list(groupDOQueryWrapper);

            List<String> erpDeliveryList = new ArrayList<>();
            if(CollectionUtil.isNotEmpty(groupList)){
                for(OrderInvoiceDeliveryGroupDO groupOne : groupList ){
                    List<String> list = Arrays.asList(groupOne.getGroupDeliveryNos().split(","));
                    erpDeliveryList.addAll(list);
                }
            }

            //删除应收单
            for (OrderDeliveryReceivableDO one : receivableList) {
                one.setErpReceivableFlag(1);
                one.setErpReceivableNo("");
            }
            orderDeliveryReceivableService.updateBatchById(receivableList);


            //判断当前申请是全部出库单
            QueryWrapper<OrderDeliveryReceivableDO> receivableWrapper = new QueryWrapper<>();
            receivableWrapper.lambda().in(OrderDeliveryReceivableDO::getErpDeliveryNo, erpDeliveryList)
                    .eq(OrderDeliveryReceivableDO::getApplyId,applyId)
                    .eq(OrderDeliveryReceivableDO::getErpReceivableFlag, 1);
            List<OrderDeliveryReceivableDO> applyIdList = orderDeliveryReceivableService.list(receivableWrapper);
            //这个申请应收单是否全部删除
            /*Boolean flag = true;
            for (OrderDeliveryReceivableDO applyOne : applyIdList) {
                if (!applyOne.getErpReceivableNo().equals(erpReceivableNo)) {
                    flag = false;
                }
            }*/
            if (CollectionUtil.isNotEmpty(applyIdList) && applyIdList.size() == erpDeliveryList.size() ) {
                BigDecimal ticketDiscountAmount = BigDecimal.ZERO;
                //删除order_invoice_apply
                OrderInvoiceApplyDO applyDO = new OrderInvoiceApplyDO();
                applyDO.setId(applyId);
                orderInvoiceApplyService.deleteByIdWithFill(applyDO);

                //删除组信息
                OrderInvoiceDeliveryGroupDO groupDO = new OrderInvoiceDeliveryGroupDO();
                groupDO.setOpUserId(0L);
                QueryWrapper<OrderInvoiceDeliveryGroupDO> groupWrapper = new QueryWrapper<>();
                groupWrapper.lambda().eq(OrderInvoiceDeliveryGroupDO::getApplyId, applyId);
                orderInvoiceDeliveryGroupService.batchDeleteWithFill(groupDO,groupWrapper);

                Boolean afterReceiveFlag = true;
                if (OrderStatusEnum.DELIVERED.getCode() <= order.getOrderStatus()) {
                    afterReceiveFlag = false;
                }
                //修改order_detail_change 中票折信息
                List<OrderDetailTicketDiscountDO> orderTicketDiscountList = orderDetailTicketDiscountService.listByApplyId(applyId);
                if (CollectionUtil.isNotEmpty(orderTicketDiscountList)) {
                    Map<Long, Double> changeMap = orderTicketDiscountList.stream().collect(Collectors.groupingBy(k -> k.getDetailId(), Collectors.summingDouble(v -> Double.valueOf(v.getUseAmount().toString()))));
                    for (Map.Entry<Long, Double> changeOne : changeMap.entrySet()) {
                        //修改票折信息
                        log.info("申请applyd:{},应收单删除后处理票折金额参数：{}", applyId, JSON.toJSONString(changeOne));
                        orderDetailChangeService.updateErpReceivableNoCancelTicketAmount(changeOne.getKey(), BigDecimal.valueOf(changeOne.getValue()), afterReceiveFlag);
                        ticketDiscountAmount = ticketDiscountAmount.add(BigDecimal.valueOf(changeOne.getValue()));
                    }
                    //返还票折金额
                    Map<String, BigDecimal> map = new HashMap<>();
                    for (OrderDetailTicketDiscountDO one : orderTicketDiscountList) {
                        if (map.containsKey(one.getTicketDiscountNo())) {
                            map.put(one.getTicketDiscountNo(), map.get(one.getTicketDiscountNo()).add(one.getUseAmount()));
                        } else {
                            map.put(one.getTicketDiscountNo(), one.getUseAmount());
                        }
                    }
                    for (Map.Entry<String, BigDecimal> entry : map.entrySet()) {
                        ticketDiscountRecordService.reduceUsedAmount(entry.getKey(), entry.getValue());
                    }

                }
                //设置返还的
                result.put("ticketDiscountAmount", ticketDiscountAmount);

                //删除order_detail_ticket_discount 票折记录
                QueryWrapper<OrderDetailTicketDiscountDO> detailTicketDiscountWrapper = new QueryWrapper<>();
                detailTicketDiscountWrapper.lambda().eq(OrderDetailTicketDiscountDO::getApplyId, applyId);
                OrderDetailTicketDiscountDO detailTicketDiscountDO = new OrderDetailTicketDiscountDO();
                detailTicketDiscountDO.setOpUserId(0L);
                orderDetailTicketDiscountService.batchDeleteWithFill(detailTicketDiscountDO, detailTicketDiscountWrapper);

                //删除order_ticket_discount 票折记录
                QueryWrapper<OrderTicketDiscountDO> ticketDiscountWrapper = new QueryWrapper<>();
                ticketDiscountWrapper.lambda().eq(OrderTicketDiscountDO::getApplyId, applyId);
                OrderTicketDiscountDO ticketDiscountDO = new OrderTicketDiscountDO();
                detailTicketDiscountDO.setOpUserId(0L);
                orderTicketDiscountService.batchDeleteWithFill(ticketDiscountDO, ticketDiscountWrapper);

                //删除order_invoice_detail 信息
                List<String> groupNoList = groupList.stream().map(o -> o.getGroupNo()).collect(Collectors.toList());
                QueryWrapper<OrderInvoiceDetailDO> orderInvoiceDetailWrapper = new QueryWrapper<>();
                orderInvoiceDetailWrapper.lambda().in(OrderInvoiceDetailDO::getGroupNo, groupNoList);
                OrderInvoiceDetailDO orderInvoiceDetailDO = new OrderInvoiceDetailDO();
                orderInvoiceDetailDO.setOpUserId(0L);
                orderInvoiceDetailService.batchDeleteWithFill(orderInvoiceDetailDO, orderInvoiceDetailWrapper);

                //修改order 改为待申请状态
                order.setTicketDiscountAmount(BigDecimal.ZERO);
                order.setInvoiceAmount(BigDecimal.ZERO);
                order.setTicketDiscountNo("");
                order.setInvoiceStatus(OrderInvoiceApplyStatusEnum.PENDING_APPLY.getCode());
                //判断是否有申请中状态
                List<OrderInvoiceApplyDTO> orderInvoiceApplyList = orderInvoiceApplyService.listByOrderId(orderId);
                if(CollectionUtil.isNotEmpty(orderInvoiceApplyList)){
                    List<OrderInvoiceApplyDTO> applyList = orderInvoiceApplyList.stream().filter(s -> s.getStatus().equals(InvoiceApplyStatusEnum.APPLIED.getCode())).collect(Collectors.toList());
                    if(CollectionUtil.isNotEmpty(applyList)){
                        order.setInvoiceStatus(OrderInvoiceApplyStatusEnum.PART_APPLIED.getCode());
                    }
                }


                orderService.updateById(order);
            }
        }

        return result;
    }


    private List<OrderDeliveryReceivableDO> getByErpDeliveryNo(String erpDeliveryNo) {
        QueryWrapper<OrderDeliveryReceivableDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderDeliveryReceivableDO::getErpDeliveryNo, erpDeliveryNo);
        List<OrderDeliveryReceivableDO> list = orderDeliveryReceivableService.list(wrapper);
        if (CollectionUtil.isEmpty(list)) {
            throw new BusinessException(OrderErrorCode.ERP_DELIVERY_NO_NOT_EXISTS);
        }
        return list;
    }


    @Override
    public Boolean checkErpPullOrder(Long orderId) {

        QueryWrapper<OrderDO> wrapper = new QueryWrapper();
        wrapper.lambda()
                .eq(OrderDO::getAuditStatus, OrderAuditStatusEnum.AUDIT_PASS.getCode())
                .eq(OrderDO::getOrderStatus,OrderStatusEnum.UNDELIVERED.getCode())
                .eq(OrderDO::getCustomerConfirmStatus,CustomerConfirmEnum.CONFIRMED.getCode())
                .ne(OrderDO::getPaymentMethod,0)
                .eq(OrderDO :: getId,orderId);
        OrderDO one = orderService.getOne(wrapper);
        log.info("查询订单推送结果：{}",JSON.toJSONString(one));
        if(one != null){
            OrderDO order = new OrderDO();
            order.setErpPushStatus(OrderErpPushStatusEnum.PUSH_SUCCESS.getCode());
            order.setErpPushTime(new Date());
            order.setId(orderId);
            orderService.updateById(order);
        }

        return one != null ;
    }

    /**
     * 获取未同步推送EAS申请发票订单id
     *
     * @param request
     * @return
     */
    @Override
    public Page<OrderInvoicePullErpDTO> getErpPullOrderInvoice(OrderPullErpPageRequest request) {
        Page<OrderInvoicePullErpDTO> list = orderInvoiceApplyService.getErpPullOrderInvoice(request);
        return list;
    }

    /**
     * 申请发票同步ERP推送状态
     *
     * @param request
     * @return
     */
    @Override
    public Boolean updateErpOrderInvoicePushStatus(List<UpdateErpPushStatusRequest> request) {
        List<String> groupNoList = request.stream().map(o -> o.getGroupNo()).collect(Collectors.toList());
        Map<String, UpdateErpPushStatusRequest> map = request.stream().collect(Collectors.toMap(UpdateErpPushStatusRequest::getGroupNo, o -> o, (k1, k2) -> k1));
        QueryWrapper<OrderInvoiceDeliveryGroupDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(OrderInvoiceDeliveryGroupDO::getGroupNo, groupNoList);
        List<OrderInvoiceDeliveryGroupDO> list = orderInvoiceDeliveryGroupService.list(wrapper);
        for (OrderInvoiceDeliveryGroupDO one : list) {
            UpdateErpPushStatusRequest result = map.get(one.getGroupNo());
            if (result != null) {
                one.setErpPushRemark(result.getErpPushRemark());
                one.setErpPushStatus(result.getErpPushStatus());
                one.setErpPushTime(new Date());
            }
        }
        return orderInvoiceDeliveryGroupService.updateBatchById(list);
    }

    /**
     * 作废发票
     *
     * @param invoiceList 发票号
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderDO cancelErpOrderInvoice(List<String> invoiceList) {
        log.info("作废发票invoiceNo:{}", JSON.toJSONString(invoiceList));

        OrderInvoiceDO invoice = getByInvoiceNo(invoiceList.get(0));
        if (invoice == null) {
            throw new BusinessException(OrderErrorCode.INVOICE_NO_ORDER_NOT_EXISTS);
        }

        OrderDO order = orderService.getById(invoice.getOrderId());
        if (order == null) {
            throw new BusinessException(OrderErrorCode.ORDER_NOT_EXISTS);
        }

        QueryWrapper<OrderInvoiceDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(OrderInvoiceDO::getInvoiceNo, invoiceList);
        OrderInvoiceDO entity = new OrderInvoiceDO();
        entity.setUpdateUser(0L);
        entity.setUpdateTime(new Date());

        orderInvoiceService.batchDeleteWithFill(entity, wrapper);

        QueryWrapper<OrderInvoiceDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(OrderInvoiceDO::getApplyId, invoice.getApplyId());
        List<OrderInvoiceDO> list = orderInvoiceService.list(queryWrapper);

        if (list.isEmpty()) {
            OrderInvoiceApplyDO applyOne = new OrderInvoiceApplyDO();
            applyOne.setId(invoice.getApplyId());
            applyOne.setStatus(InvoiceApplyStatusEnum.INVALID.getCode());
            orderInvoiceApplyService.updateById(applyOne);

            if(OrderInvoiceApplyStatusEnum.getByCode(order.getInvoiceStatus()) == OrderInvoiceApplyStatusEnum.INVOICED){
                //全部开票了
                QueryWrapper<OrderInvoiceDO> orderInvoiceWrapper = new QueryWrapper<>();
                orderInvoiceWrapper.lambda().in(OrderInvoiceDO :: getOrderId,order.getId());
                List<OrderInvoiceDO> orderInvoiceList = orderInvoiceService.list(orderInvoiceWrapper);
                if(orderInvoiceList.isEmpty()){
                    //发票全部删除完成
                    order.setInvoiceStatus(OrderInvoiceApplyStatusEnum.INVALID.getCode());
                    orderService.updateById(order);
                    return order;
                }
            }else{
                order.setInvoiceStatus(OrderInvoiceApplyStatusEnum.PART_APPLIED.getCode());
                orderService.updateById(order);
            }

        }
        return null;
    }

    /**
     * 开票成功
     *
     * @param request
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean completeErpOrderInvoice(List<CompleteErpOrderInvoiceRequest> request) {
        //保存发票信息
        /**
         * 1：这个申请是否完成，完成修改申请状态为已开票
         * 2：判断订单是否开票完成。完成修改order表中状态是否全部开票，没有改成部分开票
         * 3：加入订单里面票折金额，开票金额，票折单号
         */
        log.info("开票成功request:{}", JSON.toJSONString(request));
        String erpReceivableNo = request.get(0).getErpReceivableNo();
        QueryWrapper<OrderDeliveryReceivableDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderDeliveryReceivableDO::getErpReceivableNo, erpReceivableNo)
                .orderByDesc(OrderDeliveryReceivableDO::getUpdateTime);
        List<OrderDeliveryReceivableDO> receivableList = orderDeliveryReceivableService.list(wrapper);

        Set<Long> applyIdSet = receivableList.stream().map(OrderDeliveryReceivableDO::getApplyId).collect(Collectors.toSet());
        if (applyIdSet.size() != 1) {
            throw new BusinessException(OrderErrorCode.RECEIVABLE_ORDER_REPEAT_ERROR);
        }
        Long orderId = receivableList.get(0).getOrderId();
        Long applyId = receivableList.get(0).getApplyId();

        List<OrderDetailChangeDO> orderDetailChangeList = orderDetailChangeService.listByOrderId(orderId);
        //发货数量
        Integer deliveryQuantity = orderDetailChangeList.stream().mapToInt(OrderDetailChangeDO::getDeliveryQuantity).sum();

        QueryWrapper<OrderInvoiceDetailDO> detailDOQueryWrapper = new QueryWrapper<>();
        detailDOQueryWrapper.lambda().eq(OrderInvoiceDetailDO::getOrderId, orderId);
        List<OrderInvoiceDetailDO> invoiceDetailList = orderInvoiceDetailService.list(detailDOQueryWrapper);
        //开票数量
        Integer invoiceQuantity = 0;
        BigDecimal invoiceAmount = BigDecimal.ZERO;
        BigDecimal ticketDiscountAmount = BigDecimal.ZERO;
        if (CollectionUtil.isNotEmpty(invoiceDetailList)) {
            for (OrderInvoiceDetailDO invoiceDetailOne : invoiceDetailList) {
                if (StringUtils.isBlank(invoiceDetailOne.getBatchNo()) && invoiceDetailOne.getInvoiceQuantity().compareTo(0) == 0) {
                    invoiceQuantity = invoiceQuantity + invoiceDetailOne.getGoodsQuantity();
                }else{
                    invoiceQuantity = invoiceQuantity + invoiceDetailOne.getInvoiceQuantity();
                }
                invoiceAmount = invoiceAmount.add(invoiceDetailOne.getInvoiceAmount());
                ticketDiscountAmount = ticketDiscountAmount.add(invoiceDetailOne.getTicketDiscountAmount());
            }
        }
        //全部开票成功

        if(deliveryQuantity.compareTo(invoiceQuantity) == 0){
            //全部开票成功
            invoiceAllSuccess(orderId, invoiceAmount, ticketDiscountAmount);

        }else{
            OrderDO  orderDO = new OrderDO();
            orderDO.setId(orderId);
            orderDO.setInvoiceStatus(OrderInvoiceApplyStatusEnum.PENDING_APPLY.getCode());
            orderService.updateById(orderDO);
        }


        //修改申请状态
        OrderInvoiceApplyDO apply = new OrderInvoiceApplyDO();
        apply.setId(applyId);
        apply.setStatus(InvoiceApplyStatusEnum.INVOICED.getCode());
        orderInvoiceApplyService.updateById(apply);


        OrderInvoiceApplyDO applyOne = orderInvoiceApplyService.getById(applyId);
        List<OrderInvoiceDO> saveList = new ArrayList<>();
        for (CompleteErpOrderInvoiceRequest one : request) {
            OrderInvoiceDO invoiceOne = new OrderInvoiceDO();
            invoiceOne.setApplyId(applyId)
                    .setReceivableNo(one.getErpReceivableNo())
                    .setInvoiceNo(one.getInvoiceNo())
                    .setInvoiceAmount(one.getInvoiceAmount())
                    .setOrderId(orderId)
                    .setOrderNo(applyOne.getOrderNo())
                    .setCreateTime(new Date());
            saveList.add(invoiceOne);
        }

        orderInvoiceService.saveBatch(saveList);

        return Boolean.TRUE;
    }

    /**
     * 全部开票成功
     * @param orderId 订单id
     * @param invoiceAmount 开票金额
     * @param ticketDiscountAmount 票折金额
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean invoiceAllSuccess(Long orderId, BigDecimal invoiceAmount, BigDecimal ticketDiscountAmount) {
        OrderDO orderDO = new OrderDO();
        orderDO.setId(orderId);
        List<OrderTicketDiscountDO> orderTicketDiscountList = orderTicketDiscountService.listByOrderId(orderId);
        if(CollectionUtil.isNotEmpty(orderTicketDiscountList)){
            Set<String> ticketNoList = orderTicketDiscountList.stream().map(OrderTicketDiscountDO::getTicketDiscountNo).collect(Collectors.toSet());
            orderDO.setTicketDiscountNo(ticketNoList.stream().collect(Collectors.joining(",")));
        }
        orderDO.setInvoiceAmount(invoiceAmount);
        orderDO.setInvoiceStatus(OrderInvoiceApplyStatusEnum.INVOICED.getCode());
        orderDO.setTicketDiscountAmount(ticketDiscountAmount);
        orderService.updateById(orderDO);


        return saveTicketReturnOrder(orderId);
    }

    private Boolean saveTicketReturnOrder(Long orderId) {
        // 开发票的时候将票折信息添加到当前的待审核破损退货单票折信息中
        // 改破损退货单的原因：采购上主动退货是在的开票之后申请，所以没有这个问题
        List<OrderReturnDetailDTO> orderReturnDetail = orderReturnService.getOrderReturnDetailByOrderIdAndTypeAndStatus(orderId, null, OrderReturnStatusEnum.ORDER_RETURN_PENDING.getCode());
        List<OrderReturnDetailDTO> orderReturnDetailList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(orderReturnDetail)) {
            BigDecimal totalTicketDiscountAmount = BigDecimal.ZERO;
            Long returnId = orderReturnDetail.get(0).getReturnId();
            for (OrderReturnDetailDTO one : orderReturnDetail) {
                OrderReturnDetailDTO saveOne = new OrderReturnDetailDTO();
                OrderDetailChangeBO orderDetailChangeBO = orderDetailChangeService.updateReturnData(one.getDetailId(), one.getReturnQuantity(), false);
                saveOne.setReturnTicketDiscountAmount(orderDetailChangeBO.getTicketDiscountAmount());
                if (returnId.equals(one.getReturnId())) {
                    totalTicketDiscountAmount = totalTicketDiscountAmount.add(orderDetailChangeBO.getTicketDiscountAmount());
                }
                saveOne.setId(one.getId());
                orderReturnDetailList.add(saveOne);
            }
            OrderReturnDO orderReturnDO = new OrderReturnDO();
            orderReturnDO.setId(returnId);
            orderReturnDO.setTicketDiscountAmount(totalTicketDiscountAmount);
            orderReturnService.updateById(orderReturnDO);
        }
        List<OrderReturnDetailDO> map = PojoUtils.map(orderReturnDetailList, OrderReturnDetailDO.class);
        return orderReturnDetailService.updateBatchById(map);
    }

    @Override
    public Boolean checkErpPullBuyerOrder(Long orderId) {
        QueryWrapper<OrderDO> wrapper = new QueryWrapper();
        wrapper.lambda()
                .eq(OrderDO::getAuditStatus, OrderAuditStatusEnum.AUDIT_PASS.getCode())
                .ge(OrderDO::getOrderStatus,OrderStatusEnum.UNDELIVERED.getCode())
                .eq(OrderDO:: getCustomerConfirmStatus, CustomerConfirmEnum.CONFIRMED.getCode())
                .ne(OrderDO::getPaymentMethod,0)
                .eq(OrderDO::getId,orderId);
        OrderDO one = orderService.getOne(wrapper);
        if(one != null){
            OrderDO updateOne = new OrderDO();
            updateOne.setErpPurchaseStatus(2);
            updateOne.setErpPurchaseTime(new Date());
            orderService.update(updateOne, wrapper);
        }
        return one != null;
    }

    @Override
    public Boolean checkErpSendBuyerOrder(Long orderId) {
        QueryWrapper<OrderDO> wrapper = new QueryWrapper();
        wrapper.lambda()
                .eq(OrderDO::getAuditStatus, OrderAuditStatusEnum.AUDIT_PASS.getCode())
                .ge(OrderDO::getOrderStatus,OrderStatusEnum.DELIVERED.getCode())
                .eq(OrderDO:: getCustomerConfirmStatus, CustomerConfirmEnum.CONFIRMED.getCode())
                .ne(OrderDO::getPaymentMethod,0)
                .eq(OrderDO::getId,orderId);
        OrderDO one = orderService.getOne(wrapper);
        return one != null;
    }

    /**
     * ERP拉取未出库的订单
     *
     * @param request
     * @return
     */
    @Override
    public Page<OrderDTO> getErpPullDeliveryOrder(OrderPullErpDeliveryPageRequest request) {
        QueryWrapper<OrderDO> wrapper = new QueryWrapper();
        if (CollectionUtil.isNotEmpty(request.getBuyEids())) {
            wrapper.lambda().in(OrderDO::getBuyerEid, request.getBuyEids());
        }

        if (request.getStartCreateTime() != null) {
            wrapper.lambda().ge(OrderDO::getDeliveryTime, request.getStartCreateTime());
        }

        if (request.getEndCreateTime() != null) {
            wrapper.lambda().le(OrderDO::getDeliveryTime, request.getEndCreateTime());
        }
        wrapper.lambda().eq(OrderDO::getErpDeliveryStatus, OrderErpPushStatusEnum.NOT_PUSH.getCode())
                .eq(OrderDO :: getCustomerConfirmStatus,CustomerConfirmEnum.CONFIRMED.getCode())
                .eq(OrderDO::getAuditStatus, OrderAuditStatusEnum.AUDIT_PASS.getCode())
                .orderByAsc(OrderDO::getId);
        Page<OrderDO> page = orderService.page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
        return PojoUtils.map(page, OrderDTO.class);
    }

    /**
     * ERP推送出库的订单，并修改状态为以推送
     *
     * @param request
     * @return
     */
    @Override
    public Boolean updateErpDeliveryOrderById(List<UpdateErpOrderDeliveryRequest> request) {
        List<OrderDO> list = PojoUtils.map(request, OrderDO.class);
        for (OrderDO one : list) {
            OrderDO updateOne = new OrderDO();
            QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(OrderDO::getId, one.getId());
            wrapper.lambda().eq(OrderDO::getErpDeliveryStatus, OrderErpPushStatusEnum.NOT_PUSH.getCode());
            updateOne.setErpDeliveryRemark(one.getErpDeliveryRemark());
            updateOne.setErpDeliveryStatus(one.getErpDeliveryStatus());
            updateOne.setErpDeliveryTime(new Date());
            orderService.update(updateOne, wrapper);
        }
        return true;
    }


    private OrderInvoiceDO getByInvoiceNo(String invoiceNo) {
        QueryWrapper<OrderInvoiceDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(OrderInvoiceDO::getInvoiceNo, invoiceNo)
                .last("limit 1");
        return orderInvoiceService.getOne(queryWrapper);
    }
}
