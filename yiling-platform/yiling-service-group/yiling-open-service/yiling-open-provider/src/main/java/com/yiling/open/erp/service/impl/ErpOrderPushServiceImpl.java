package com.yiling.open.erp.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.rocketmq.enums.MqDelayLevel;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.api.PopGoodsApi;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.goods.medicine.dto.GoodsInfoDTO;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.goods.medicine.dto.PopGoodsDTO;
import com.yiling.goods.medicine.enums.GoodsLineEnum;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.open.erp.dao.ErpOrderPushMapper;
import com.yiling.open.erp.dto.ErpClientDTO;
import com.yiling.open.erp.dto.ErpOrderDTO;
import com.yiling.open.erp.dto.ErpOrderDetailDTO;
import com.yiling.open.erp.dto.ErpOrderPurchaseSendDTO;
import com.yiling.open.erp.dto.ErpOrderPurchaseSendDetailDTO;
import com.yiling.open.erp.dto.ErpOrderPushDTO;
import com.yiling.open.erp.dto.request.OrderPushErpBuyerEidPageRequest;
import com.yiling.open.erp.dto.request.OrderPushErpPageRequest;
import com.yiling.open.erp.dto.request.UpdateErpOrderPushRequest;
import com.yiling.open.erp.entity.ErpCustomerMappingDO;
import com.yiling.open.erp.entity.ErpOrderPushDO;
import com.yiling.open.erp.enums.PushTypeEnum;
import com.yiling.open.erp.service.ErpClientService;
import com.yiling.open.erp.service.ErpCustomerMappingService;
import com.yiling.open.erp.service.ErpOrderPushService;
import com.yiling.open.erp.validation.ValidationUtils;
import com.yiling.open.erp.validation.entity.ValidationResult;
import com.yiling.order.order.api.OrderAddressApi;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderDeliveryApi;
import com.yiling.order.order.api.OrderDeliveryErpApi;
import com.yiling.order.order.api.OrderDetailApi;
import com.yiling.order.order.api.OrderDetailChangeApi;
import com.yiling.order.order.api.OrderErpApi;
import com.yiling.order.order.dto.OrderAddressDTO;
import com.yiling.order.order.dto.OrderAttachmentDTO;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDeliveryDTO;
import com.yiling.order.order.dto.OrderDetailChangeDTO;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.enums.OrderAttachmentTypeEnum;
import com.yiling.order.order.enums.OrderAuditStatusEnum;
import com.yiling.order.order.enums.OrderErpPushStatusEnum;
import com.yiling.order.order.enums.OrderStatusEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.enums.EnterpriseChannelEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: shuang.zhang
 * @date: 2022/2/11
 */
@Slf4j
@Service
public class ErpOrderPushServiceImpl extends BaseServiceImpl<ErpOrderPushMapper, ErpOrderPushDO> implements ErpOrderPushService {

    @DubboReference
    OrderApi                  orderApi;
    @DubboReference
    EnterpriseApi             enterpriseApi;
    @DubboReference
    OrderDeliveryErpApi       orderDeliveryErpApi;
    @DubboReference
    OrderDeliveryApi          orderDeliveryApi;
    @DubboReference
    OrderDetailApi            orderDetailApi;
    @DubboReference
    OrderErpApi               orderErpApi;
    @DubboReference
    PopGoodsApi               popGoodsApi;
    @DubboReference
    GoodsApi                  goodsApi;
    @DubboReference
    OrderDetailChangeApi      orderDetailChangeApi;
    @DubboReference
    OrderAddressApi           orderAddressApi;
    @Autowired
    ErpCustomerMappingService erpCustomerMappingService;
    @Autowired
    ErpOrderPushMapper        erpOrderPushMapper;
    @Autowired
    ErpClientService          erpClientService;
    @Autowired
    @Lazy
    ErpOrderPushServiceImpl   _this;
    @DubboReference
    MqMessageSendApi          mqMessageSendApi;

    /**
     * @param orderIds
     */
    @Override
    public void erpOrderPush(List<Long> orderIds) {
        //判断订单是否可以推送的erp
        if (CollUtil.isNotEmpty(orderIds)) {
            List<OrderDTO> orderDTOList = orderApi.listByIds(orderIds);
            for (OrderDTO orderDTO : orderDTOList) {
                if (orderDTO.getAuditStatus().equals(OrderAuditStatusEnum.AUDIT_PASS.getCode()) && orderDTO.getPaymentMethod() != 0) {
                    if (!Arrays.asList(OrderStatusEnum.UNAUDITED.getCode(), OrderStatusEnum.CANCELED.getCode()).contains(orderDTO.getOrderStatus())) {
                        //判断订单是否已经插入到op库中
                        QueryWrapper<ErpOrderPushDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.lambda().eq(ErpOrderPushDO::getOrderId, orderDTO.getId());
                        List<ErpOrderPushDO> erpOrderPushDOList = erpOrderPushMapper.selectList(queryWrapper);
                        if (CollUtil.isEmpty(erpOrderPushDOList)) {
                            erpOrderPushDOList = new ArrayList<>();
                            ErpOrderPushDO erpOrderPushDO = new ErpOrderPushDO();
                            erpOrderPushDO.setBuyerEid(orderDTO.getBuyerEid());
                            erpOrderPushDO.setSellerEid(orderDTO.getSellerEid());
                            erpOrderPushDO.setOrderId(orderDTO.getId());
                            erpOrderPushDO.setOrderNo(orderDTO.getOrderNo());
                            erpOrderPushDO.setErpPushStatus(1);
                            erpOrderPushDO.setCreateTime(new Date());
                            erpOrderPushDO.setPushType(PushTypeEnum.ORDER_PUSH.getCode());
                            erpOrderPushDOList.add(erpOrderPushDO);

                            ErpOrderPushDO erpPurchaseOrderPushDO = new ErpOrderPushDO();
                            erpPurchaseOrderPushDO.setBuyerEid(orderDTO.getBuyerEid());
                            erpPurchaseOrderPushDO.setSellerEid(orderDTO.getSellerEid());
                            erpPurchaseOrderPushDO.setOrderId(orderDTO.getId());
                            erpPurchaseOrderPushDO.setOrderNo(orderDTO.getOrderNo());
                            erpPurchaseOrderPushDO.setErpPushStatus(1);
                            erpPurchaseOrderPushDO.setCreateTime(new Date());
                            erpPurchaseOrderPushDO.setPushType(PushTypeEnum.ORDER_PURCHASE_PUSH.getCode());
                            erpOrderPushDOList.add(erpPurchaseOrderPushDO);

                            ErpOrderPushDO erpPurchaseSendOrderPushDO = new ErpOrderPushDO();
                            erpPurchaseSendOrderPushDO.setBuyerEid(orderDTO.getBuyerEid());
                            erpPurchaseSendOrderPushDO.setSellerEid(orderDTO.getSellerEid());
                            erpPurchaseSendOrderPushDO.setOrderId(orderDTO.getId());
                            erpPurchaseSendOrderPushDO.setOrderNo(orderDTO.getOrderNo());
                            erpPurchaseSendOrderPushDO.setErpPushStatus(1);
                            erpPurchaseSendOrderPushDO.setCreateTime(new Date());
                            erpPurchaseSendOrderPushDO.setPushType(PushTypeEnum.ORDER_PURCHASE_SEND_PUSH.getCode());
                            erpOrderPushDOList.add(erpPurchaseSendOrderPushDO);
                            this.saveBatch(erpOrderPushDOList);

                            //发送监控邮件
                            List<Long> eidList = new ArrayList<>();
                            eidList.addAll(enterpriseApi.listEidsByChannel(EnterpriseChannelEnum.INDUSTRY));
                            eidList.addAll(enterpriseApi.listEidsByChannel(EnterpriseChannelEnum.INDUSTRY_DIRECT));
                            if (eidList.contains(orderDTO.getSellerEid())) {
                                _this.sendMq(Constants.TOPIC_ORDER_PUSH_FAIL_MAIL, Constants.TAG_ORDER_PUSH_FAIL_MAIL, JSONArray.toJSONString(Arrays.asList(orderDTO.getId())), MqDelayLevel.ONE_HOUR);
                            }
                        } else {
                            //存在就修改推送状态
                            for (ErpOrderPushDO erpOrderPushDO : erpOrderPushDOList) {
                                if (orderDTO.getOrderStatus().equals(OrderStatusEnum.UNDELIVERED.getCode()) || orderDTO.getOrderStatus().equals(OrderStatusEnum.PARTDELIVERED.getCode())) {
                                    // 1-订单推送
                                    QueryWrapper<ErpOrderPushDO> wrapper = new QueryWrapper<>();
                                    wrapper.lambda().eq(ErpOrderPushDO::getOrderId, erpOrderPushDO.getOrderId());
                                    wrapper.lambda().eq(ErpOrderPushDO::getId, erpOrderPushDO.getId());
                                    wrapper.lambda().eq(ErpOrderPushDO::getPushType, 1);
                                    wrapper.lambda().eq(ErpOrderPushDO::getErpPushStatus, 3);

                                    ErpOrderPushDO updateOne = new ErpOrderPushDO();
                                    updateOne.setErpPushRemark("");
                                    updateOne.setErpPushStatus(1);
                                    this.update(updateOne, wrapper);
                                    // 2-采购单推送
                                    QueryWrapper<ErpOrderPushDO> purchaseWrapper = new QueryWrapper<>();
                                    purchaseWrapper.lambda().eq(ErpOrderPushDO::getOrderId, erpOrderPushDO.getOrderId());
                                    purchaseWrapper.lambda().eq(ErpOrderPushDO::getId, erpOrderPushDO.getId());
                                    purchaseWrapper.lambda().eq(ErpOrderPushDO::getErpPushStatus, 3);
                                    purchaseWrapper.lambda().eq(ErpOrderPushDO::getPushType, 2);

                                    ErpOrderPushDO purchaseUpdateOne = new ErpOrderPushDO();
                                    purchaseUpdateOne.setErpPushRemark("");
                                    purchaseUpdateOne.setErpPushStatus(1);
                                    this.update(purchaseUpdateOne, purchaseWrapper);
                                }
                                if (orderDTO.getOrderStatus().equals(OrderStatusEnum.DELIVERED.getCode())) {
                                    // 3-采购发货单推送
                                    QueryWrapper<ErpOrderPushDO> wrapper = new QueryWrapper<>();
                                    wrapper.lambda().eq(ErpOrderPushDO::getOrderId, erpOrderPushDO.getOrderId());
                                    wrapper.lambda().eq(ErpOrderPushDO::getId, erpOrderPushDO.getId());
                                    wrapper.lambda().eq(ErpOrderPushDO::getErpPushStatus, 3);
                                    wrapper.lambda().eq(ErpOrderPushDO::getPushType, 3);

                                    ErpOrderPushDO updateOne = new ErpOrderPushDO();
                                    updateOne.setErpPushRemark("");
                                    updateOne.setErpPushStatus(1);
                                    this.update(updateOne, wrapper);
                                }
                            }
                        }
                    } else {
                        log.warn("erp订单推送失败, 此订单状态为“待审核”或“已取消”，不能推送，order_id:{}", orderDTO.getId());

                    }
                }
            }
        }
    }

    /**
     * 获取销售单方法
     *
     * @param request
     * @return
     */
    @Override
    public Page<ErpOrderPushDTO> getErpPushOrder(OrderPushErpPageRequest request) {
        QueryWrapper<ErpOrderPushDO> wrapper = new QueryWrapper();
        if (CollectionUtil.isNotEmpty(request.getSellerEids())) {
            wrapper.lambda().in(ErpOrderPushDO::getSellerEid, request.getSellerEids());
        }
        if (request.getStartCreateTime() != null) {
            wrapper.lambda().ge(ErpOrderPushDO::getCreateTime, request.getStartCreateTime());
        }
        if (request.getEndCreateTime() != null) {
            wrapper.lambda().le(ErpOrderPushDO::getCreateTime, request.getEndCreateTime());
        }
        wrapper.lambda().eq(ErpOrderPushDO::getErpPushStatus, 1)
                .eq(ErpOrderPushDO::getPushType, request.getPushType());
        Page<ErpOrderPushDO> page = this.page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
        return PojoUtils.map(page, ErpOrderPushDTO.class);
    }

    /**
     * ERP拉取买家订单
     *
     * @param request
     * @return
     */
    @Override
    public Page<ErpOrderPushDTO> getErpPushBuyerOrder(OrderPushErpBuyerEidPageRequest request) {
        QueryWrapper<ErpOrderPushDO> wrapper = new QueryWrapper();
        if (CollectionUtil.isNotEmpty(request.getBuyerEids())) {
            wrapper.lambda().in(ErpOrderPushDO::getBuyerEid, request.getBuyerEids());
        }

        if (request.getStartCreateTime() != null) {
            wrapper.lambda().ge(ErpOrderPushDO::getCreateTime, request.getStartCreateTime());
        }

        if (request.getEndCreateTime() != null) {
            wrapper.lambda().le(ErpOrderPushDO::getCreateTime, request.getEndCreateTime());
        }
        wrapper.lambda().eq(ErpOrderPushDO::getErpPushStatus, 1).eq(ErpOrderPushDO::getPushType, request.getPushType());
        Page<ErpOrderPushDO> page = this.page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
        return PojoUtils.map(page, ErpOrderPushDTO.class);
    }

    /**
     * ERP推送已同步订单，将为推送改为读取状态
     *
     * @param request
     * @return
     */
    @Override
    public Boolean updateErpStatusNotPushToReadSuccess(List<UpdateErpOrderPushRequest> request) {
        List<ErpOrderPushDO> list = PojoUtils.map(request, ErpOrderPushDO.class);
        for (ErpOrderPushDO one : list) {
            ErpOrderPushDO updateOne = new ErpOrderPushDO();
            QueryWrapper<ErpOrderPushDO> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(ErpOrderPushDO::getOrderId, one.getOrderId());
            wrapper.lambda().eq(ErpOrderPushDO::getPushType, one.getPushType());
            wrapper.lambda().eq(ErpOrderPushDO::getErpPushStatus, OrderErpPushStatusEnum.NOT_PUSH.getCode());

            updateOne.setErpPushRemark(one.getErpPushRemark());
            updateOne.setErpPushStatus(one.getErpPushStatus());
            updateOne.setErpPushTime(new Date());
            this.update(updateOne, wrapper);
        }
        return true;
    }

    @Override
    public Boolean updateExtractByOrderId(UpdateErpOrderPushRequest request) {
        ErpOrderPushDO updateOne = PojoUtils.map(request, ErpOrderPushDO.class);
        QueryWrapper<ErpOrderPushDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ErpOrderPushDO::getOrderId, updateOne.getOrderId());
        wrapper.lambda().eq(ErpOrderPushDO::getPushType, updateOne.getPushType());
        wrapper.lambda().eq(ErpOrderPushDO::getErpPushStatus, OrderErpPushStatusEnum.PUSH_SUCCESS.getCode());

        updateOne.setErpPushStatus(updateOne.getErpPushStatus());
        updateOne.setErpOrderNo(updateOne.getErpOrderNo());
        updateOne.setErpPushRemark(updateOne.getErpPushRemark());
        return this.update(updateOne, wrapper);
    }

    @Override
    public Boolean updateExtractMessageByOrderId(UpdateErpOrderPushRequest request) {
        ErpOrderPushDO updateOne = PojoUtils.map(request, ErpOrderPushDO.class);
        QueryWrapper<ErpOrderPushDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ErpOrderPushDO::getOrderId, updateOne.getOrderId());
        wrapper.lambda().eq(ErpOrderPushDO::getPushType, updateOne.getPushType());
        //wrapper.lambda().in(ErpOrderPushDO::getErpPushStatus, Arrays.asList(2, 4));
        return this.update(updateOne, wrapper);
    }

    @Override
    public List<ErpOrderPushDTO> getErpPushOrderListByOrderIds(List<Long> orderIds, Integer pushType) {
        QueryWrapper<ErpOrderPushDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(ErpOrderPushDO::getOrderId, orderIds);
        wrapper.lambda().eq(ErpOrderPushDO::getPushType, pushType);
        List<ErpOrderPushDO> list = this.list(wrapper);
        return PojoUtils.map(list, ErpOrderPushDTO.class);
    }

    @Override
    public List<ErpOrderDTO> getOrderSaleBySuId(Long suId, Integer pageSize) {
        List<ErpOrderDTO> erpOrderList = new ArrayList<>();
        OrderPushErpPageRequest erpRequest = new OrderPushErpPageRequest();
        erpRequest.setSize(pageSize);
        erpRequest.setPushType(1);
        erpRequest.setStartCreateTime(DateUtil.offsetMonth(DateUtil.parse(DateUtil.today()), -3));
        if (Constants.YILING_EID.equals(suId)) {
            List<Long> list = enterpriseApi.listSubEids(suId);
            list = list.stream().filter(e -> !e.equals(4L)).collect(Collectors.toList());
            erpRequest.setSellerEids(list);
        } else {
            List<ErpClientDTO> erpClientDTOList = erpClientService.selectBySuId(suId);
            erpRequest.setSellerEids(erpClientDTOList.stream().map(e -> e.getRkSuId()).collect(Collectors.toList()));
        }
        log.debug("orderErpApi.getErpPullOrder, request -> {}", JSON.toJSONString(erpRequest));
        Page<ErpOrderPushDTO> orderPage = this.getErpPushOrder(erpRequest);
        log.debug("orderErpApi.getErpPullOrder, reponse -> {}", JSON.toJSONString(orderPage));
        if (orderPage != null && CollectionUtil.isNotEmpty(orderPage.getRecords())) {

            List<Long> idList = orderPage.getRecords().stream().map(o -> o.getOrderId()).collect(Collectors.toList());

            Map<Long, List<OrderDetailChangeDTO>> detailMap = new HashMap<>();

            List<OrderDetailChangeDTO> orderDetailChangeList = orderDetailChangeApi.listByOrderIds(idList);

            for (OrderDetailChangeDTO one : orderDetailChangeList) {
                if (detailMap.containsKey(one.getOrderId())) {
                    List<OrderDetailChangeDTO> orderDetailList = detailMap.get(one.getOrderId());
                    orderDetailList.add(one);
                } else {
                    detailMap.put(one.getOrderId(), new ArrayList<OrderDetailChangeDTO>() {{
                        add(one);
                    }});
                }
            }

            Map<Long, String> map = getGoodsInfoYlGoodsIdAndEid(erpRequest.getSellerEids(), orderDetailChangeList.stream().map(e -> e.getGoodsId()).collect(Collectors.toList()));
            log.debug("/purchase/push, goodsInSn -> {}", JSON.toJSONString(map));

            List<OrderAddressDTO> orderAddressList = orderAddressApi.getOrderAddressList(idList);
            Map<Long, OrderAddressDTO> addressMap = orderAddressList.stream().collect(Collectors.toMap(OrderAddressDTO::getOrderId, o -> o, (k1, k2) -> k1));

            List<OrderDetailDTO> orderDetailDTOS = orderDetailApi.getOrderDetailByOrderIds(idList);
            Map<Long, OrderDetailDTO> orderDetailMap = orderDetailDTOS.stream().collect(Collectors.toMap(OrderDetailDTO::getId, o -> o, (k1, k2) -> k1));

            List<OrderDTO> orderDTOS = orderApi.listByIds(idList);
            Map<Long, OrderDTO> orderMap = orderDTOS.stream().collect(Collectors.toMap(OrderDTO::getId, Function.identity()));

            for (ErpOrderPushDTO one : orderPage.getRecords()) {
                OrderDTO orderDTO = orderMap.get(one.getOrderId());
                List<OrderDetailChangeDTO> orderDetailList = detailMap.get(one.getOrderId());

                List<ErpOrderDetailDTO> list = new ArrayList<>();
                for (OrderDetailChangeDTO detailOne : orderDetailList) {
                    OrderDetailDTO detailDTO = orderDetailMap.get(detailOne.getDetailId());
                    ErpOrderDetailDTO erpOrderDetail = new ErpOrderDetailDTO();
                    erpOrderDetail.setBuyNumber(Long.valueOf(detailOne.getGoodsQuantity()));
                    erpOrderDetail.setEnterpriseInnerCode(orderDTO.getCustomerErpCode());
                    erpOrderDetail.setGoodsAmount(detailOne.getGoodsAmount());
                    erpOrderDetail.setGoodsInSn(detailOne.getGoodsErpCode());
                    erpOrderDetail.setGoodsName(detailOne.getGoodsName());
                    erpOrderDetail.setGoodsPrice(detailDTO.getOriginalPrice());
                    erpOrderDetail.setOrderDetailId(detailOne.getDetailId());
                    erpOrderDetail.setOrderId(detailOne.getOrderId());
                    erpOrderDetail.setOrderNo(String.valueOf(detailOne.getOrderId()));
                    erpOrderDetail.setSellerInnerCode(orderDTO.getSellerErpCode());
                    erpOrderDetail.setCreateTime(new Date());
                    // 促销金额，预留字段，暂未使用 默认0
                    erpOrderDetail.setPromotionPrice(detailOne.getCouponDiscountAmount().add(detailOne.getPlatformCouponDiscountAmount()).add(detailOne.getCashDiscountAmount()));
                    erpOrderDetail.setTruthPrice((erpOrderDetail.getGoodsAmount().subtract(erpOrderDetail.getPromotionPrice())).divide(new BigDecimal(erpOrderDetail.getBuyNumber()), 4, BigDecimal.ROUND_HALF_UP));
                    list.add(erpOrderDetail);
                }

                // 订单销售合同
                List<OrderAttachmentDTO> orderAttachmentList = orderApi.listOrderAttachmentsByType(orderDTO.getId(), OrderAttachmentTypeEnum.SALES_CONTRACT_FILE);
                List<String> fileKeyList = orderAttachmentList.stream().filter(o -> StringUtils.isNotBlank(o.getFileKey())).map(OrderAttachmentDTO::getFileKey).collect(Collectors.toList());
                if (CollUtil.isEmpty(fileKeyList)) {
                    fileKeyList = ListUtil.empty();
                }
                String fileKeys = String.join(",", fileKeyList);

                ErpOrderDTO erpOrder = new ErpOrderDTO();
                OrderAddressDTO orderAddress = addressMap.get(orderDTO.getId());
                erpOrder.setCreateTime(new Date());
                erpOrder.setEnterpriseInnerCode(orderDTO.getCustomerErpCode());
                erpOrder.setEnterpriseName(orderDTO.getBuyerEname());
                erpOrder.setOrdeId(orderDTO.getId());
                erpOrder.setOrderDate(orderDTO.getCreateTime());
                erpOrder.setOrderNo(String.valueOf(orderDTO.getId()));
                erpOrder.setOrderSn(orderDTO.getOrderNo());
                erpOrder.setRemark(orderDTO.getOrderNote());
                erpOrder.setSaleInnerCode(orderDTO.getProvinceManagerCode());
                erpOrder.setSellerInnerCode(orderDTO.getSellerErpCode());
                if (orderAddress != null) {
                    erpOrder.setReceiveAddress(orderAddress.getAddress());
                    erpOrder.setReceiveMobile(orderAddress.getMobile());
                    erpOrder.setReceiveUser(orderAddress.getName());
                }
                erpOrder.setStatus(0);
                Date date = new Date();
                erpOrder.setCreateTime(date);
                erpOrder.setStatusTime(date);
                erpOrder.setPaymentMethod(orderDTO.getPaymentMethod());
                erpOrder.setPaymentStatus(orderDTO.getPaymentStatus());
                erpOrder.setSellerEid(one.getSellerEid());
                // 促销金额，预留字段，暂未使用 默认0
                erpOrder.setPromotionPrice(orderDTO.getPlatformCouponDiscountAmount().add(orderDTO.getCouponDiscountAmount()).add(orderDTO.getCashDiscountAmount()));
                erpOrder.setFileType(OrderAttachmentTypeEnum.SALES_CONTRACT_FILE.getCode());
                erpOrder.setFileKey(fileKeys);
                erpOrder.setOrderDetailsList(list);
                erpOrderList.add(erpOrder);
            }
        }
        return erpOrderList;
    }

    @Override
    public List<ErpOrderDTO> verifyERPOrderSaleResult(List<ErpOrderDTO> erpOrderList) {
        if (CollUtil.isEmpty(erpOrderList)) {
            return ListUtil.empty();
        }
        Iterator<ErpOrderDTO> it = erpOrderList.iterator();
        List<UpdateErpOrderPushRequest> list = new ArrayList<>();
        while (it.hasNext()) {
            ErpOrderDTO erpOrderDTO = it.next();
            ValidationResult validationResult = ValidationUtils.validate(erpOrderDTO);
            if (validationResult.hasError()) {
                it.remove();
                UpdateErpOrderPushRequest updateErpOrderDTO = new UpdateErpOrderPushRequest();
                updateErpOrderDTO.setOrderId(erpOrderDTO.getOrdeId());
                updateErpOrderDTO.setPushType(1);
                updateErpOrderDTO.setErpPushStatus(3);
                // 异常信息
                Map<String, String> errorMap = validationResult.getErrorMap();
                String errorKey = new ArrayList<>(errorMap.keySet()).get(0);
                String errorMsg = errorMap.get(errorKey);
                updateErpOrderDTO.setErpPushRemark("订单推送主单校验异常: ".concat(errorKey).concat(errorMsg));
                list.add(updateErpOrderDTO);
                continue;
            }
            List<ErpOrderDetailDTO> erpOrderDetailDTOList = erpOrderDTO.getOrderDetailsList();
            Boolean flag = true;
            for (ErpOrderDetailDTO erpOrderDetailDTO : erpOrderDetailDTOList) {
                validationResult = ValidationUtils.validate(erpOrderDetailDTO);
                if (validationResult.hasError()) {
                    flag = false;
                    UpdateErpOrderPushRequest updateErpOrderDTO = new UpdateErpOrderPushRequest();
                    updateErpOrderDTO.setOrderId(erpOrderDTO.getOrdeId());
                    updateErpOrderDTO.setPushType(1);
                    updateErpOrderDTO.setErpPushStatus(3);
                    // 异常信息
                    Map<String, String> errorMap = validationResult.getErrorMap();
                    String errorKey = new ArrayList<>(errorMap.keySet()).get(0);
                    String errorMsg = errorMap.get(errorKey);
                    updateErpOrderDTO.setErpPushRemark("订单推送明细校验异常: ".concat(errorKey).concat(errorMsg));
                    list.add(updateErpOrderDTO);
                    break;
                }
            }
            if (!flag) {
                it.remove();
                continue;
            }
            //是否满足订单推送条件
            flag = orderErpApi.checkErpPullOrder(erpOrderDTO.getOrdeId());
            if (!flag) {
                it.remove();
                UpdateErpOrderPushRequest updateErpOrderDTO = new UpdateErpOrderPushRequest();
                updateErpOrderDTO.setOrderId(erpOrderDTO.getOrdeId());
                updateErpOrderDTO.setPushType(1);
                updateErpOrderDTO.setErpPushStatus(3);
                updateErpOrderDTO.setErpPushRemark("不满足推送条件");
                list.add(updateErpOrderDTO);
                continue;
            }
        }
        if (!org.springframework.util.CollectionUtils.isEmpty(list)) {
            this.updateErpStatusNotPushToReadSuccess(list);
        }
        return erpOrderList;
    }

    @Override
    public List<ErpOrderPurchaseSendDTO> getOrderPurchaseSendBySuId(Long suId, Integer pageSize) {
        List<ErpOrderPurchaseSendDTO> erpOrderList = new ArrayList<>();
        OrderPushErpBuyerEidPageRequest erpRequest = new OrderPushErpBuyerEidPageRequest();
        erpRequest.setSize(pageSize);
        erpRequest.setPushType(3);
        erpRequest.setStartCreateTime(DateUtil.offsetMonth(DateUtil.parse(DateUtil.today()), -3));
        if (Constants.YILING_EID.equals(suId)) {
            List<Long> list = enterpriseApi.listSubEids(suId);
            erpRequest.setBuyerEids(list);
        } else {
            List<ErpClientDTO> erpClientDTOList = erpClientService.selectBySuId(suId);
            erpRequest.setBuyerEids(erpClientDTOList.stream().map(e -> e.getRkSuId()).collect(Collectors.toList()));
        }
        // 订单
        Page<ErpOrderPushDTO> orderPage = this.getErpPushBuyerOrder(erpRequest);
        if (orderPage != null && CollectionUtil.isNotEmpty(orderPage.getRecords())) {
            List<Long> idList = orderPage.getRecords().stream().map(o -> o.getOrderId()).collect(Collectors.toList());
            log.debug("/purchase/push, idList -> {}", idList);
            Map<Long, List<OrderDeliveryDTO>> detailMap = new HashMap<>();
            // 订单明细变更信息
            List<OrderDeliveryDTO> orderDeliveryDTOList = orderDeliveryApi.listByOrderIds(idList);

            for (OrderDeliveryDTO one : orderDeliveryDTOList) {
                if (detailMap.containsKey(one.getOrderId())) {
                    List<OrderDeliveryDTO> orderDetailList = detailMap.get(one.getOrderId());
                    orderDetailList.add(one);
                } else {
                    detailMap.put(one.getOrderId(), new ArrayList<OrderDeliveryDTO>() {
                        {
                            add(one);
                        }
                    });
                }
            }

            // 查询商品内码
            List<OrderDetailDTO> orderDetailDTOList = orderDetailApi.getOrderDetailByOrderIds(idList);
            Map<Long, OrderDetailDTO> orderDetailMap = orderDetailDTOList.stream()
                    .collect(Collectors.toMap(OrderDetailDTO::getId, Function.identity()));
            Map<Long, String> map = getGoodsInfoYlGoodsIdAndEid(erpRequest.getBuyerEids(),
                    orderDetailDTOList.stream().map(e -> e.getGoodsId()).collect(Collectors.toList()));

            List<OrderDTO> orderDTOList = orderApi.listByIds(idList);
            Map<Long, OrderDTO> orderDTOMap = orderDTOList.stream().collect(Collectors.toMap(OrderDTO::getId, Function.identity()));

            log.debug("/purchase/push, goodsInSn -> {}", JSON.toJSONString(map));

            for (ErpOrderPushDTO one : orderPage.getRecords()) {
                List<OrderDeliveryDTO> orderDetailList = detailMap.getOrDefault(one.getOrderId(), ListUtil.empty());
                OrderDTO orderDTO = orderDTOMap.get(one.getOrderId());
                List<ErpOrderPurchaseSendDetailDTO> list = new ArrayList<>();
                for (OrderDeliveryDTO detailOne : orderDetailList) {
                    OrderDetailDTO orderDetailDTO = orderDetailMap.get(detailOne.getDetailId());
                    ErpOrderPurchaseSendDetailDTO erpOrderDetail = new ErpOrderPurchaseSendDetailDTO();
                    erpOrderDetail.setOrderId(detailOne.getOrderId());
                    erpOrderDetail.setOrderNo(String.valueOf(detailOne.getOrderId()));
                    erpOrderDetail.setOrderDetailId(detailOne.getDetailId());
                    if (orderDTO.getOrderType() == 1) {
                        erpOrderDetail.setGoodsInSn(map.get(orderDetailDTO.getGoodsId()));
                    } else if (orderDTO.getOrderType() == 2) {
                        erpOrderDetail.setGoodsInSn(orderDetailDTO.getGoodsErpCode());
                    }
                    erpOrderDetail.setGoodsName(orderDetailDTO.getGoodsName());
                    erpOrderDetail.setSendNumber(Long.valueOf(detailOne.getDeliveryQuantity()));
                    erpOrderDetail.setGoodsPrice(orderDetailDTO.getGoodsPrice());
                    erpOrderDetail.setGoodsAmount(orderDetailDTO.getGoodsPrice().multiply(new BigDecimal(detailOne.getDeliveryQuantity())));
                    erpOrderDetail.setSendBatchNo(detailOne.getBatchNo());
                    erpOrderDetail.setEffectiveTime(detailOne.getExpiryDate());
                    erpOrderDetail.setCreateTime(new Date());
                    erpOrderDetail.setDeliveryNumber(String.valueOf(detailOne.getId()));
                    erpOrderDetail.setProductTime(detailOne.getProduceDate());
                    list.add(erpOrderDetail);
                }

                ErpOrderPurchaseSendDTO erpOrder = new ErpOrderPurchaseSendDTO();
                erpOrder.setOrdeId(orderDTO.getId());
                erpOrder.setOrderNo(String.valueOf(orderDTO.getId()));
                erpOrder.setOrderSn(orderDTO.getOrderNo());
                QueryWrapper<ErpCustomerMappingDO> wrapper = new QueryWrapper<>();
                wrapper.lambda().eq(ErpCustomerMappingDO::getSuId, suId);
                wrapper.lambda().eq(ErpCustomerMappingDO::getCustomerEid, orderDTO.getSellerEid());
                ErpCustomerMappingDO erpCustomerMappingDO = erpCustomerMappingService.getOne(wrapper);
                if (erpCustomerMappingDO == null) {
                    log.info("商业公司采购单没有匹配到上游公司编码suid={},sellerEid={}", suId, orderDTO.getSellerEid());
                    continue;
                }
                erpOrder.setSellerInnerCode(erpCustomerMappingDO.getCode());
                erpOrder.setPurchasePeopleCode(erpCustomerMappingDO.getPurchasePeopleCode());
                erpOrder.setPurchasePeopleName(erpCustomerMappingDO.getPurchasePeopleName());
                erpOrder.setBuyerEid(one.getBuyerEid());
                erpOrder.setSellerName(orderDTO.getSellerEname());
                Date date = new Date();
                erpOrder.setCreateTime(date);
                erpOrder.setStatus(0);
                erpOrder.setStatusTime(date);
                erpOrder.setSellerEid(one.getSellerEid());
                erpOrder.setOrderDetailsList(list);
                erpOrderList.add(erpOrder);
            }
        }
        return erpOrderList;
    }

    @Override
    public List<ErpOrderPurchaseSendDTO> verifyERPOrderPurchaseSendResult(List<ErpOrderPurchaseSendDTO> erpOrderList) {
        if (CollUtil.isEmpty(erpOrderList)) {
            return ListUtil.empty();
        }
        Iterator<ErpOrderPurchaseSendDTO> it = erpOrderList.iterator();
        List<UpdateErpOrderPushRequest> list = new ArrayList<>();
        while (it.hasNext()) {
            ErpOrderPurchaseSendDTO erpOrderDTO = it.next();
            ValidationResult validationResult = ValidationUtils.validate(erpOrderDTO);
            if (validationResult.hasError()) {
                it.remove();
                UpdateErpOrderPushRequest updateErpOrderDTO = new UpdateErpOrderPushRequest();
                updateErpOrderDTO.setOrderId(erpOrderDTO.getOrdeId());
                updateErpOrderDTO.setPushType(3);
                updateErpOrderDTO.setErpPushStatus(3);
                // 异常信息
                Map<String, String> errorMap = validationResult.getErrorMap();
                String errorKey = new ArrayList<>(errorMap.keySet()).get(0);
                String errorMsg = errorMap.get(errorKey);
                updateErpOrderDTO.setErpPushRemark("采购发货单推送主单校验异常: ".concat(errorKey).concat(errorMsg));
                list.add(updateErpOrderDTO);
                continue;
            }
            List<ErpOrderPurchaseSendDetailDTO> erpOrderDetailDTOList = erpOrderDTO.getOrderDetailsList();
            Boolean flag = true;
            for (ErpOrderPurchaseSendDetailDTO erpOrderDetailDTO : erpOrderDetailDTOList) {
                validationResult = ValidationUtils.validate(erpOrderDetailDTO);
                if (validationResult.hasError()) {
                    flag = false;
                    UpdateErpOrderPushRequest updateErpOrderDTO = new UpdateErpOrderPushRequest();
                    updateErpOrderDTO.setOrderId(erpOrderDTO.getOrdeId());
                    updateErpOrderDTO.setPushType(3);
                    updateErpOrderDTO.setErpPushStatus(3);
                    // 异常信息

                    Map<String, String> errorMap = validationResult.getErrorMap();
                    String errorKey = new ArrayList<>(errorMap.keySet()).get(0);
                    String errorMsg = errorMap.get(errorKey);
                    updateErpOrderDTO.setErpPushRemark("采购发货单推送明细校验异常: ".concat(errorKey).concat(errorMsg));
                    list.add(updateErpOrderDTO);
                    break;
                }
            }
            if (!flag) {
                it.remove();
            }

            //是否满足订单推送条件
            flag = orderErpApi.checkErpSendBuyerOrder(erpOrderDTO.getOrdeId());
            if (!flag) {
                it.remove();
                UpdateErpOrderPushRequest updateErpOrderDTO = new UpdateErpOrderPushRequest();
                updateErpOrderDTO.setOrderId(erpOrderDTO.getOrdeId());
                updateErpOrderDTO.setPushType(3);
                updateErpOrderDTO.setErpPushStatus(3);
                updateErpOrderDTO.setErpPushRemark("不满足推送条件");
                list.add(updateErpOrderDTO);
                continue;
            }
        }
        if (!CollectionUtils.isEmpty(list)) {
            this.updateErpStatusNotPushToReadSuccess(list);
        }
        return erpOrderList;
    }

    /**
     * 根据SellSpecificationsId和Eid获取商品内码
     *
     * @param suIds
     * @param goodsId
     * @return
     */
    public Map<Long, String> getGoodsInfoYlGoodsIdAndEid(List<Long> suIds, List<Long> goodsId) {
        Map<Long, String> map = new HashMap<>();

        List<GoodsInfoDTO> goodsInfoDTOS = popGoodsApi.batchQueryInfo(goodsId);
        Map<Long, Long> mapSellSpecificationsMap = goodsInfoDTOS.stream().collect(Collectors.toMap(GoodsInfoDTO::getId, GoodsInfoDTO::getSellSpecificationsId));
        List<GoodsDTO> goodsInfoList = goodsApi.findGoodsBySellSpecificationsIdAndEid(new ArrayList<>(mapSellSpecificationsMap.values()), suIds);
        Map<Long, List<GoodsDTO>> goodsMap = goodsInfoList.stream().collect(Collectors.groupingBy(GoodsDTO::getSellSpecificationsId));

        goodsId.forEach(e -> {
            map.put(e, "");
            List<GoodsDTO> list = goodsMap.get(mapSellSpecificationsMap.get(e));
            if (CollUtil.isNotEmpty(list)) {
                List<Long> popGoodsIds = list.stream().map(t -> t.getId()).collect(Collectors.toList());
                List<PopGoodsDTO> popGoodsDTOList = popGoodsApi.getPopGoodsListByGoodsIds(popGoodsIds);
                Map<Long, PopGoodsDTO> popGoodsDTOMap = popGoodsDTOList.stream().filter(t -> t.getGoodsStatus().equals(GoodsStatusEnum.UP_SHELF.getCode())).collect(Collectors.toMap(PopGoodsDTO::getGoodsId, Function.identity()));

                List<GoodsSkuDTO> goodsSkuDTOList = goodsApi.getGoodsSkuByGoodsIds(new ArrayList<>(popGoodsDTOMap.keySet()));
                goodsSkuDTOList = goodsSkuDTOList.stream().filter(t -> t.getGoodsLine().equals(GoodsLineEnum.POP.getCode())).collect(Collectors.toList());
                for (GoodsSkuDTO goodsSkuDTO : goodsSkuDTOList) {
                    if (StrUtil.isNotEmpty(goodsSkuDTO.getInSn())) {
                        map.put(e, goodsSkuDTO.getInSn());
                        break;
                    }
                }
            }
        });
        return map;
    }

    /**
     * 发送消息
     *
     * @param topic
     * @param topicTag
     * @param msg
     * @return
     */
    public boolean sendMq(String topic, String topicTag, String msg, MqDelayLevel delayLevel) {

        MqMessageBO mqMessageBO = _this.sendPrepare(topic, topicTag, msg, delayLevel);
        mqMessageSendApi.send(mqMessageBO);
        return true;
    }

    /**
     * 消息持久化
     *
     * @param topic
     * @param topicTag
     * @param msg
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public MqMessageBO sendPrepare(String topic, String topicTag, String msg, MqDelayLevel delayLevel) {
        MqMessageBO mqMessageBO = new MqMessageBO(topic, topicTag, msg, delayLevel);
        mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
        return mqMessageBO;
    }

}
