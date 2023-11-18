package com.yiling.mall.order.service.impl;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.goods.inventory.api.InventoryApi;
import com.yiling.goods.inventory.dto.InventoryDTO;
import com.yiling.goods.inventory.dto.request.AddOrSubtractQtyRequest;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsSkuInfoDTO;
import com.yiling.goods.medicine.enums.GoodsSkuStatusEnum;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.goods.restriction.api.GoodsPurchaseRestrictionApi;
import com.yiling.goods.restriction.dto.GoodsPurchaseRestrictionDTO;
import com.yiling.goods.restriction.dto.request.QueryGoodsPurchaseRestrictionRequest;
import com.yiling.mall.agreement.api.AgreementBusinessApi;
import com.yiling.mall.agreement.enums.GoodsLimitStatusEnum;
import com.yiling.mall.cart.entity.CartDO;
import com.yiling.order.order.api.NoApi;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.request.CreateOrderRequest;
import com.yiling.order.order.dto.request.QueryUserBuyNumberRequest;
import com.yiling.order.order.enums.OrderErrorCode;
import com.yiling.order.order.enums.PaymentMethodEnum;
import com.yiling.order.order.enums.PaymentStatusEnum;
import com.yiling.order.order.enums.PromotionActivityTypeEnum;
import com.yiling.payment.enums.PaymentErrorCode;
import com.yiling.payment.enums.TradeTypeEnum;
import com.yiling.payment.pay.api.PayApi;
import com.yiling.payment.pay.dto.request.CreatePayOrderRequest;
import com.yiling.pricing.goods.api.GoodsPriceApi;
import com.yiling.pricing.goods.dto.GoodsPriceDTO;
import com.yiling.pricing.goods.dto.request.QueryGoodsPriceRequest;
import com.yiling.user.enterprise.api.DeliveryAddressApi;
import com.yiling.user.enterprise.api.EmployeeApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.api.EnterprisePurchaseRelationApi;
import com.yiling.user.enterprise.dto.EnterpriseEmployeeDTO;
import com.yiling.user.procrelation.api.ProcurementRelationGoodsApi;
import com.yiling.user.procrelation.dto.DistributorGoodsBO;
import com.yiling.user.system.api.UserApi;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.mall.order.service.impl
 * @date: 2022/1/10
 */
@Slf4j
public abstract class OrderSubmitServiceImpl implements ApplicationEventPublisherAware {
    protected ApplicationEventPublisher     applicationEventPublisher;
    @DubboReference
    protected GoodsApi                      goodsApi;
    @DubboReference
    protected EnterprisePurchaseRelationApi enterprisePurchaseRelationApi;
    @DubboReference
    protected AgreementBusinessApi          agreementBusinessApi;
    @DubboReference
    protected InventoryApi                  inventoryApi;
    @DubboReference
    protected EmployeeApi                   employeeApi;
    @DubboReference
    protected PayApi                        payApi;
    @DubboReference
    protected EnterpriseApi                 enterpriseApi;
    @DubboReference
    protected UserApi                       userApi;
    @DubboReference
    protected NoApi                         noApi;
    @DubboReference
    protected DeliveryAddressApi            deliveryAddressApi;
    @DubboReference
    protected GoodsPriceApi                 goodsPriceApi;
    @DubboReference
    MqMessageSendApi                        mqMessageSendApi;
    @DubboReference
    OrderApi                                orderApi;
    @DubboReference                         // 商品限购
    GoodsPurchaseRestrictionApi             purchaseRestrictionApi;
    @DubboReference                         // 商品建采
    ProcurementRelationGoodsApi             procurementRelationGoodsApi;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {

        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * 校验商品信息是否满足包装规格
     *
     * @param allCartDOList
     * @param allGoodsDTOMap
     */
    protected void checkPackageNumber(List<CartDO> allCartDOList, Map<Long, GoodsSkuInfoDTO> allGoodsDTOMap) {
        // sku信息
        Map<Long, Integer> skuGoodQuantityMap = allCartDOList.stream().filter(
                t -> PromotionActivityTypeEnum.LIMIT !=  PromotionActivityTypeEnum.getByCode(t.getPromotionActivityType())
                        && PromotionActivityTypeEnum.SPECIAL !=  PromotionActivityTypeEnum.getByCode(t.getPromotionActivityType())
                        && PromotionActivityTypeEnum.COMBINATION != PromotionActivityTypeEnum.getByCode(t.getPromotionActivityType())
        ).collect(Collectors.toMap(CartDO::getGoodsSkuId,CartDO::getQuantity));

        if (MapUtil.isEmpty(skuGoodQuantityMap)) {
            return;
        }
        CollUtil.forEach(skuGoodQuantityMap, (skuId, number, i) -> {
            GoodsSkuInfoDTO skuDTO = allGoodsDTOMap.get(skuId);
            if (skuDTO == null) {
                throw new BusinessException(OrderErrorCode.SKU_NOT_EXIST);
            }
            if (number % skuDTO.getPackageNumber() != 0) {
                throw new BusinessException(OrderErrorCode.SKU_PACKAGE_NUMBER_EXIST);
            }
        });
    }


    /**
     * 校验购物车信息
     */
    protected void validateCart(List<CartDO> allCartDOList, List<Long> allCartIds) {
        if (CollUtil.isEmpty(allCartDOList) || allCartDOList.size() != allCartIds.size()) {
            throw new BusinessException(OrderErrorCode.SUBMIT_CART_CHANGED);
        }
    }

    /**
     * 校验采购关系
     */
    protected void validatePurchaseRelation(List<CartDO> allCartDOList, Long buyerEid) {
        List<Long> distributorEids = allCartDOList.stream().map(CartDO::getDistributorEid).distinct().collect(Collectors.toList());
        boolean checkPurchaseRelationResult = enterprisePurchaseRelationApi.checkPurchaseRelation(buyerEid, distributorEids);
        if (!checkPurchaseRelationResult) {
            throw new BusinessException(OrderErrorCode.SUBMIT_NO_PURCHASE_RELATION);
        }
    }

    /**
     * 校验采购权限
     */
    protected void validatePurchaseAuthority(List<CartDO> allCartDOList, Long buyerEid) {

        Map<Long, List<CartDO>> goodsAndDistributorMap = allCartDOList.stream().collect(Collectors.groupingBy(CartDO::getGoodsId));

        List<Long> goodIdList = allCartDOList.stream().map(t -> t.getGoodsId()).distinct().collect(Collectors.toList());

        Map<Long, List<DistributorGoodsBO>> resultMap = procurementRelationGoodsApi.getDistributorByYlGoodsIdAndBuyer(goodIdList, buyerEid);

        if (MapUtil.isEmpty(resultMap)) {

            throw new BusinessException(OrderErrorCode.SUBMIT_NO_PURCHASE_AUTHORITY);
        }

        goodsAndDistributorMap.entrySet().forEach(t -> {

            List<DistributorGoodsBO> distributorGoodsBOS = resultMap.get(t.getKey());

            if (CollectionUtil.isEmpty(distributorGoodsBOS)) {

                throw new BusinessException(OrderErrorCode.SUBMIT_NO_PURCHASE_AUTHORITY);
            }

            List<Long> distributorEidList = distributorGoodsBOS.stream().map(z -> z.getDistributorEid()).collect(Collectors.toList());

            List<Long> purchaseDistributorList = t.getValue().stream().map(z -> z.getDistributorEid()).collect(Collectors.toList());

            if (!distributorEidList.containsAll(purchaseDistributorList)) {

                throw new BusinessException(OrderErrorCode.SUBMIT_NO_PURCHASE_AUTHORITY);
            }
        });
    }

    /**
     * 校验商品状态
     */
    protected void validateGoodsStatus(List<CartDO> allCartDOList, Map<Long, GoodsSkuInfoDTO> allGoodsDTOMap) {
        for (CartDO cartDO : allCartDOList) {
            GoodsSkuInfoDTO goodsDTO = allGoodsDTOMap.get(cartDO.getGoodsSkuId());
            if (GoodsStatusEnum.getByCode(goodsDTO.getGoodsInfo().getGoodsStatus()) != GoodsStatusEnum.UP_SHELF) {
                throw new BusinessException(OrderErrorCode.SUBMIT_GOODS_OFF_SHELF);
            }
            if (!GoodsSkuStatusEnum.NORMAL.getCode().equals(goodsDTO.getStatus())) {
                throw new BusinessException(OrderErrorCode.SUBMIT_GOODS_DISABLE);
            }
            // 校验是否审核通过
            if (!GoodsStatusEnum.AUDIT_PASS.getCode().equals(goodsDTO.getGoodsInfo().getAuditStatus())) {
                throw new BusinessException(OrderErrorCode.SUBMIT_GOODS_DISABLE);
            }
        }
        // 校验包装规格
        this.checkPackageNumber(allCartDOList, allGoodsDTOMap);
    }

    /**
     * 校验商品库存
     */
    protected void validateGoodsInventory(List<CartDO> allCartDOList) {
        List<Long> goodSkuIds = allCartDOList.stream().map(CartDO::getGoodsSkuId).distinct().collect(Collectors.toList());
        /* 针对同个sku出现多条时，库存校验需要合并校验 **/
        Map<Long, Integer> skuQuantityMap = allCartDOList.stream().collect(Collectors.groupingBy(CartDO::getGoodsSkuId, Collectors.summingInt(CartDO::getQuantity)));
        Map<Long, InventoryDTO> distributorGoodsInventoryDTOMap = inventoryApi.getMapBySkuIds(goodSkuIds);
        for (Map.Entry<Long, Integer> entry : skuQuantityMap.entrySet()) {
            InventoryDTO inventoryDTO = distributorGoodsInventoryDTOMap.get(entry.getKey());
            // 超卖商品,无需校验库存
            if (inventoryDTO != null && ObjectUtil.equal(1, inventoryDTO.getOverSoldType())) {
                continue;
            }
            if (CompareUtil.compare(entry.getValue(), 0) <= 0) {
                throw new BusinessException(OrderErrorCode.SUBMIT_GOODS_INVENTORY_NOT_ENOUGH);
            }
            if (inventoryDTO == null || inventoryDTO.getQty() - inventoryDTO.getFrozenQty() < entry.getValue()) {
                throw new BusinessException(OrderErrorCode.SUBMIT_GOODS_INVENTORY_NOT_ENOUGH);
            }
        }
    }


    /**
     * 判断用户限制规则
     * @param buyerEid
     * @param allCartDOList
     */
    protected void validateGoodsLimit(Long buyerEid,List<CartDO> allCartDOList,List<GoodsSkuInfoDTO> allGoodsDTOList) {

        List<CartDO> normalCartDoList = allCartDOList.stream().filter(t -> PromotionActivityTypeEnum.NORMAL == PromotionActivityTypeEnum.getByCode(t.getPromotionActivityType())).collect(Collectors.toList());

        if (CollectionUtil.isEmpty(normalCartDoList)) {
            return;
        }

        long start1 = System.currentTimeMillis();
        QueryGoodsPurchaseRestrictionRequest restrictionRequest = new QueryGoodsPurchaseRestrictionRequest();
        restrictionRequest.setCustomerEid(buyerEid);
        restrictionRequest.setGoodsIdList(normalCartDoList.stream().map(t -> t.getDistributorGoodsId()).collect(Collectors.toList()));
        List<GoodsPurchaseRestrictionDTO> goodsPurchaseRestrictionDTOS = purchaseRestrictionApi.queryValidPurchaseRestriction(restrictionRequest);
        if (log.isDebugEnabled()) {
            log.debug("调用商品接口查询商品限购规则耗时：{}，入参：{},返回:[{}]",(System.currentTimeMillis() - start1),restrictionRequest, JSON.toJSONString(goodsPurchaseRestrictionDTOS));
        }

        if (CollectionUtil.isEmpty(goodsPurchaseRestrictionDTOS)) {

            return;
        }

        Set<Long> limitGoodSet = goodsPurchaseRestrictionDTOS.stream().map(t -> t.getGoodsId()).collect(Collectors.toSet());
        Map<Long, GoodsPurchaseRestrictionDTO> purchaseRestrictionDTOMap = goodsPurchaseRestrictionDTOS.stream().collect(Collectors.toMap(t -> t.getGoodsId(), Function.identity()));
        Map<Long, List<GoodsSkuInfoDTO>> goodsInfoMap = allGoodsDTOList.stream().collect(Collectors.groupingBy(t -> t.getGoodsId()));
        Map<Long, Integer> distributorGoodsQtyMap = normalCartDoList.stream().filter(t -> limitGoodSet.contains(t.getDistributorGoodsId())).collect(Collectors.groupingBy(t -> t.getDistributorGoodsId(), Collectors.summingInt(t -> t.getQuantity())));

        for (Map.Entry<Long, Integer>  entry : distributorGoodsQtyMap.entrySet()) {
            GoodsPurchaseRestrictionDTO goodsPurchaseRestrictionDTO = purchaseRestrictionDTOMap.get(entry.getKey());
            if (goodsPurchaseRestrictionDTO.getOrderRestrictionQuantity() != 0 && CompareUtil.compare(goodsPurchaseRestrictionDTO.getOrderRestrictionQuantity(),entry.getValue().longValue()) < 0) {
                throw limitException(goodsInfoMap,entry.getKey());
            }
        }

        // 过滤出有限制条件的,如果全部都没有限制,无需校验限制规则
        List<QueryUserBuyNumberRequest> queryUserBuyNumberRequests = goodsPurchaseRestrictionDTOS.stream().filter(t -> t.getTimeRestrictionQuantity() != 0 ).map(t -> {
            QueryUserBuyNumberRequest queryUserBuyNumberRequest = new QueryUserBuyNumberRequest();
            queryUserBuyNumberRequest.setGoodId(t.getGoodsId());
            queryUserBuyNumberRequest.setBuyerEid(buyerEid);
            queryUserBuyNumberRequest.setStartTime(t.getStartTime());
            queryUserBuyNumberRequest.setEndTime(t.getEndTime());
            queryUserBuyNumberRequest.setSelectRuleEnum(QueryUserBuyNumberRequest.SelectRuleEnum.getByCode(t.getTimeType()));
            return queryUserBuyNumberRequest;

        }).collect(Collectors.toList());

        // 没有需要校验,时间段限购的规则
        if (CollectionUtil.isEmpty(queryUserBuyNumberRequests)) {
            return;
        }

        long start = System.currentTimeMillis();
        Map<Long, Long> userBuyNumberMap =  orderApi.getUserBuyNumber(queryUserBuyNumberRequests);

        if (log.isDebugEnabled()) {

            log.debug("判断用户限购规则查询用户已购数量耗时:{},返回参数:{}",(System.currentTimeMillis() - start),userBuyNumberMap);
        }

        // 校验用户自定义时间段限制,是否满足匹配的限制
        for (Map.Entry<Long, Integer>  entry : distributorGoodsQtyMap.entrySet()) {
            GoodsPurchaseRestrictionDTO goodsPurchaseRestrictionDTO = purchaseRestrictionDTOMap.get(entry.getKey());
            Long allBuyNumber = userBuyNumberMap.getOrDefault(entry.getKey(),0l) + entry.getValue().longValue();
            if (goodsPurchaseRestrictionDTO.getTimeRestrictionQuantity() != 0 && CompareUtil.compare(allBuyNumber,goodsPurchaseRestrictionDTO.getTimeRestrictionQuantity()) > 0) {
                throw limitException(goodsInfoMap,entry.getKey());
            }
        }

        log.info("判断用户限购规则耗时：" + (System.currentTimeMillis() - start1) / 1000);
    }


    /**
     * 商品限价异常信息
     * @param goodsInfoMap
     * @param goodsId
     * @return
     */
    private BusinessException limitException(Map<Long, List<GoodsSkuInfoDTO>> goodsInfoMap,Long goodsId) {

        List<GoodsSkuInfoDTO> goodsSkuInfoDTOS = goodsInfoMap.get(goodsId);
        String goodsName = "**商品";
        if (CollectionUtil.isNotEmpty(goodsSkuInfoDTOS)) {
            goodsName = goodsSkuInfoDTOS.stream().findFirst().get().getGoodsInfo().getName();
        }

        String failMsg = MessageFormat.format(OrderErrorCode.SUBMIT_GOODS_LIMIT_ERROR.getMessage(), goodsName);

        return new BusinessException(OrderErrorCode.SUBMIT_GOODS_LIMIT_ERROR,failMsg);
    }


    /**
     * 查询省区经理信息
     *
     * @param contacterId 联系人ID
     * @return
     */
    protected EnterpriseEmployeeDTO selectProvinceManager(Long contacterId) {
        if (contacterId == null || contacterId == 0L) {
            return null;
        }

        EnterpriseEmployeeDTO enterpriseEmployeeDTO = employeeApi.getByEidUserId(Constants.YILING_EID, contacterId);

        if (enterpriseEmployeeDTO == null) {
            return null;
        }

        Long parentId = enterpriseEmployeeDTO.getParentId();
        if (parentId != null && parentId != 0L) {
            return employeeApi.getByEidUserId(Constants.YILING_EID, parentId);
        }

        return enterpriseEmployeeDTO;
    }


    /**
     * 锁定商品库存
     *
     * @param createOrderRequestList
     * @return
     */
    protected void lockInventory(List<CreateOrderRequest> createOrderRequestList) {
        List<AddOrSubtractQtyRequest> requestList = CollUtil.newArrayList();
        createOrderRequestList.forEach(orderInfo -> {
            orderInfo.getOrderDetailList().forEach(orderDetail -> {
                AddOrSubtractQtyRequest request = new AddOrSubtractQtyRequest();
                request.setSkuId(orderDetail.getGoodsSkuId());
                request.setFrozenQty(orderDetail.getGoodsQuantity().longValue());
                request.setOpUserId(orderInfo.getOpUserId());
                request.setOrderNo(orderInfo.getOrderNo());
                requestList.add(request);
            });
        });
        if (!(inventoryApi.batchAddFrozenQty(requestList) > 0)) {
            throw new BusinessException(OrderErrorCode.SUBMIT_GOODS_INVENTORY_NOT_ENOUGH);
        }
    }

    /**
     * 锁定商品库存
     *
     * @param createOrderRequestList
     * @return
     */
    protected void backInventory(List<CreateOrderRequest> createOrderRequestList) {
        List<AddOrSubtractQtyRequest> requestList = CollUtil.newArrayList();
        createOrderRequestList.forEach(orderInfo -> {
            orderInfo.getOrderDetailList().forEach(orderDetail -> {
                AddOrSubtractQtyRequest request = new AddOrSubtractQtyRequest();
                request.setSkuId(orderDetail.getGoodsSkuId());
                request.setFrozenQty(orderDetail.getGoodsQuantity().longValue());
                request.setOpUserId(orderInfo.getOpUserId());
                request.setOrderNo(orderInfo.getOrderNo());
                requestList.add(request);
            });
        });
        if (!(inventoryApi.batchSubtractFrozenQty(requestList) > 0)) {
            log.error("创建订单失败后退还库存失败，requestList:{}", JSONUtil.toJsonStr(createOrderRequestList));
        }
    }


    /**
     * 校验可控商品
     *
     * @param buyerEid 登录人企业Id
     * @param gidList 商品gid
     */
    protected void checkB2BGoodsByGids(Long buyerEid, List<Long> gidList) {

        Map<Long, Integer> goodsListResult = agreementBusinessApi.getB2bGoodsLimitByGids(gidList, buyerEid);

        if (log.isDebugEnabled()) {
            log.debug("agreementBusinessApi.getB2bGoodsLimitByGids 入参:[{}],返回结果:[{}]", gidList, goodsListResult);
        }

        // 控销商品无法结算
        if (MapUtil.isEmpty(goodsListResult)) {
            throw new BusinessException(OrderErrorCode.LIMIT_GOODS_SALE_ERROR);
        }
        // 是否管控商品
        if (goodsListResult.values().stream().anyMatch(value -> GoodsLimitStatusEnum.CONTROL_GOODS == GoodsLimitStatusEnum.getByCode(value))) {
            throw new BusinessException(OrderErrorCode.LIMIT_GOODS_SALE_ERROR);
        }
        // 是否建立采购关系
        if (goodsListResult.values().stream().anyMatch(value -> GoodsLimitStatusEnum.NOT_RELATION_SHIP == GoodsLimitStatusEnum.getByCode(value))) {
            throw new BusinessException(OrderErrorCode.NOT_RELATION_SHIP_ERROR);
        }
    }


    /**
     * 在线支付，创建支付交易流水
     *
     * @param orderDTOList
     * @return
     */
    protected String initPaymentOrder(List<OrderDTO> orderDTOList) {

        // 在线支付订单
        List<OrderDTO> onlineOrderList = orderDTOList
                .stream()
                .filter(e -> PaymentMethodEnum.ONLINE == PaymentMethodEnum.getByCode(e.getPaymentMethod().longValue())) // 在线订单
                .filter(e -> PaymentStatusEnum.getByCode(e.getPaymentStatus()) == PaymentStatusEnum.UNPAID ) // 未支付
                .collect(Collectors.toList());

        // 账期支付订单
        List<OrderDTO> paymentDayList = orderDTOList
                .stream()
                .filter(e -> PaymentMethodEnum.PAYMENT_DAYS == PaymentMethodEnum.getByCode(e.getPaymentMethod().longValue()))
                .collect(Collectors.toList());

        // 线下支付订单
        List<OrderDTO> offlineOrderList = orderDTOList
                .stream()
                .filter(e -> PaymentMethodEnum.OFFLINE == PaymentMethodEnum.getByCode(e.getPaymentMethod().longValue()))
                .collect(Collectors.toList());

        if (CollectionUtil.isEmpty(onlineOrderList)) {

            return "";
        }

        StringBuilder builder = new StringBuilder();
        builder.append("订单包含");
        Integer paymentDayNum = Optional.ofNullable(paymentDayList).map(e -> e.size()).orElse(0);
        Integer offlineOrderNum = Optional.ofNullable(offlineOrderList).map(e -> e.size()).orElse(0);

        if (onlineOrderList.size() > 0) {
            builder.append(onlineOrderList.size() + "笔在线支付 ");
        }

        if (offlineOrderNum > 0) {
            builder.append(offlineOrderNum + "笔线下支付 ");
        }

        if (paymentDayNum > 0) {
            builder.append(paymentDayNum + "笔账期支付 ");
        }

        CreatePayOrderRequest payOrderRequest = new CreatePayOrderRequest();
        List<CreatePayOrderRequest.appOrderRequest> appOrderList = onlineOrderList.stream().map(orderDto -> {
            CreatePayOrderRequest.appOrderRequest request = new CreatePayOrderRequest.appOrderRequest();
            request.setUserId(orderDto.getCreateUser());
            request.setAppOrderId(orderDto.getId());
            request.setAppOrderNo(orderDto.getOrderNo());
            request.setAmount(orderDto.getPaymentAmount());
            request.setBuyerEid(orderDto.getBuyerEid());
            request.setSellerEid(orderDto.getSellerEid());

            return request;
        }).collect(Collectors.toList());

        payOrderRequest.setTradeType(TradeTypeEnum.PAY);
        payOrderRequest.setContent(builder.toString());
        payOrderRequest.setAppOrderList(appOrderList);
        payOrderRequest.setOpUserId(onlineOrderList.stream().findFirst().get().getCreateUser());

        Result<String> createResult = payApi.createPayOrder(payOrderRequest);

        if (HttpStatus.HTTP_OK != createResult.getCode()) {

            throw new BusinessException(PaymentErrorCode.ORDER_PAID_ERROR);
        }
        return createResult.getData();
    }

    /**
     * 获取pop商品价格
     *
     * @param request
     * @return
     */
    protected Map<Long, GoodsPriceDTO> getPopGoodsPrice(QueryGoodsPriceRequest request) {

        Map<Long, GoodsPriceDTO> resultMap = Maps.newHashMap();
        // 商品价格字典
        Map<Long, BigDecimal> goodsPriceMap = goodsPriceApi.queryGoodsPriceMap(request);
        goodsPriceMap.forEach((k, v) -> resultMap.putIfAbsent(k, new GoodsPriceDTO().setBuyPrice(v).setLinePrice(v)));

        return resultMap;
    }



    /**
     * 发送订单创建消息
     * @param orderDTOList 订单集合
     * @return
     */
    protected List<MqMessageBO> sendOrderCreatedMessage(List<OrderDTO> orderDTOList) {

        return orderDTOList.stream().map(t -> {

            MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_ORDER_CREATED, "", t.getOrderNo());
            mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);

            return mqMessageBO;
        }).collect(Collectors.toList());

    }

    /**
     * 获取商品合并之后，由于商品Id发生变化后的最新Id
     * @param allCartDOList 购物车商品信息
     * @param allGoodsDTOMap 查询出来的商品集合信息
     * @param yilingSubEids  以岭子企业信息
     */
    protected void setLastMergeGoodId( List<CartDO> allCartDOList, Map<Long, GoodsSkuInfoDTO> allGoodsDTOMap,List<Long> yilingSubEids) {
        // 配送商商品Ids
        List<Long> allDistributorGoodsIds = allCartDOList.stream().map(CartDO::getDistributorGoodsId).distinct().collect(Collectors.toList());
        // 获取以岭goodId
        Map<Long,Long> ylGoodIdMap =  goodsApi.getYilingGoodsIdByGoodsIdAndYilingEids(allDistributorGoodsIds,yilingSubEids);

        for (CartDO cartDTO : allCartDOList) {
            // 配送商商品Id
            Long distributorGoodsId = Optional.ofNullable(allGoodsDTOMap.get(cartDTO.getGoodsSkuId())).map(t -> t.getGoodsId()).orElse(cartDTO.getDistributorGoodsId());
            // 获取以岭最新商品Id
            Long ylGoodId = ylGoodIdMap.getOrDefault(cartDTO.getDistributorGoodsId(),cartDTO.getDistributorGoodsId());
            ylGoodId = Long.valueOf(0).equals(ylGoodId) ? cartDTO.getDistributorGoodsId() : ylGoodId;
            // 配送商商品Id
            cartDTO.setDistributorGoodsId(distributorGoodsId);
            // 以岭商品Id
            cartDTO.setGoodsId(ylGoodId);
        }
    }


}
