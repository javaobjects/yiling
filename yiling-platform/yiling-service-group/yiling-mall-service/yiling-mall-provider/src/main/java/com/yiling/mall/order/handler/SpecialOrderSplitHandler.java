package com.yiling.mall.order.handler;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.goods.medicine.dto.GoodsSkuInfoDTO;
import com.yiling.mall.cart.entity.CartDO;
import com.yiling.mall.order.bo.SplitOrderContextBO;
import com.yiling.mall.order.bo.SplitOrderEnum;
import com.yiling.mall.order.bo.SplitOrderResultBO;
import com.yiling.mall.order.dto.request.OrderSubmitRequest;
import com.yiling.marketing.promotion.dto.PromotionGoodsLimitDTO;
import com.yiling.marketing.promotion.enums.PromotionTypeEnum;
import com.yiling.order.order.dto.request.CreateOrderDetailRequest;
import com.yiling.order.order.dto.request.CreateOrderPromotionActivityRequest;
import com.yiling.order.order.dto.request.CreateOrderRequest;
import com.yiling.order.order.enums.NoEnum;
import com.yiling.order.order.enums.OrderErrorCode;
import com.yiling.order.order.enums.PromotionActivityTypeEnum;
import com.yiling.pricing.goods.dto.GoodsPriceDTO;
import com.yiling.pricing.goods.dto.request.QueryGoodsPriceRequest;
import com.yiling.user.enterprise.dto.DeliveryAddressDTO;
import com.yiling.user.enterprise.dto.EnterpriseCustomerDTO;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.EnterpriseEmployeeDTO;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 秒杀订单拆单
 *
 * @author zhigang.guo
 * @date: 2022/4/26
 */
@Slf4j
@Component(value = SplitOrderEnum.SPECIAL_ORDER)
public class SpecialOrderSplitHandler extends AbstractOrderSplitHandler {

    @Override
    protected List<CartDO> getSplitCartList(SplitOrderContextBO contextBO) {

        // 特价或者秒杀商品信息
        List<CartDO> specialPriceList = contextBO.getAllCartDOList().stream().filter(t -> SplitOrderEnum.SPECIAL == SplitOrderEnum.getByPromotionActivityCode(t.getPromotionActivityType())).collect(Collectors.toList());

        log.info("秒杀拆单购物车数据:{}", specialPriceList);

        return specialPriceList;
    }

    @Override
    protected SplitOrderResultBO split(SplitOrderContextBO contextBO) {
        Map<String, String> mdcContext = MDC.getCopyOfContextMap();
        Map<Long, EnterpriseDTO> allEnterpriseDTOMap = contextBO.getAllEnterpriseList().stream().collect(Collectors.toMap(EnterpriseDTO::getId, Function.identity()));
        Map<Long, EnterpriseCustomerDTO> customerDTOMap = contextBO.getCustomerDTOList().stream().collect(Collectors.toMap(EnterpriseCustomerDTO::getEid, Function.identity()));
        Map<Long, CartDO> allCartDOMap = contextBO.getAllCartDOList().stream().collect(Collectors.toMap(CartDO::getId, Function.identity()));
        Map<Long, GoodsSkuInfoDTO> allGoodsDTOMap = contextBO.getAllGoodsDTOList().stream().collect(Collectors.toMap(GoodsSkuInfoDTO::getId, Function.identity()));
        // 设置订单状态函数
        Consumer<CreateOrderRequest> initOrderFunction = contextBO.getInitOrderFunction();

        OrderSubmitRequest specialSubmitRequest = contextBO.getRequest();
        // 获取秒杀特价促销活动信息
        List<PromotionGoodsLimitDTO> promotionGoodsLimitDTOList = contextBO.getPromotionGoodsLimitDTOList().stream().filter(item -> PromotionTypeEnum.isSecKillOrSpecialPrice(item.getType())).collect(Collectors.toList());
        // 秒杀订单拆单
        Map<Long, PromotionGoodsLimitDTO> cartLimitDTOMap = this.splitByPromotionActivity(specialSubmitRequest, contextBO.getAllCartDOList(), promotionGoodsLimitDTOList, allGoodsDTOMap);
        MDC.setContextMap(mdcContext);
        if (log.isDebugEnabled()) {
            log.info("秒杀拆单返回数据:{}", specialSubmitRequest);
        }
        // 构建创建订单数据
        List<CreateOrderRequest> createOrderRequestList = this.buildOrderRequest(specialSubmitRequest, contextBO.getOrderBatchNo(), contextBO.getYilingSubEids(), contextBO.getProvinceManagerFunction(), contextBO.getContacterFunction(), contextBO.getOrderNoFunction(), contextBO.getDeliveryAddressDTO(), contextBO.getIndustryDirectEids(), allCartDOMap, allEnterpriseDTOMap, allGoodsDTOMap, customerDTOMap, cartLimitDTOMap,initOrderFunction);

        log.info("秒杀拆单返回订单请求数据:{}", createOrderRequestList);

        if (CollectionUtil.isEmpty(createOrderRequestList)) {

            throw new BusinessException(OrderErrorCode.SPLIT_ORDER_ERROR);
        }

        return SplitOrderResultBO.builder().createOrderRequestList(createOrderRequestList).build();
    }

    /**
     * 秒杀促销
     *
     * @param request
     * @param promotionPriceList
     * @param allGoodsDTOMap
     * @return
     */
    private Map<Long, PromotionGoodsLimitDTO> splitByPromotionActivity(OrderSubmitRequest request, List<CartDO> promotionPriceList, List<PromotionGoodsLimitDTO> promotionGoodsLimitDTOList, Map<Long, GoodsSkuInfoDTO> allGoodsDTOMap) {
        if (CollectionUtil.isEmpty(promotionPriceList)) {
            return MapUtil.empty();
        }
        Map<Long, CartDO> cartDOMap = promotionPriceList.stream().collect(Collectors.toMap(CartDO::getId, t -> t));
        List<Long> promotionCartList = promotionPriceList.stream().map(t -> t.getId()).collect(Collectors.toList());
        List<Long> limitCartList = promotionPriceList.stream().filter(z -> PromotionActivityTypeEnum.LIMIT == PromotionActivityTypeEnum.getByCode(z.getPromotionActivityType())).map(t -> t.getId()).collect(Collectors.toList());
        List<Long> specialCartList = promotionPriceList.stream().filter(z -> PromotionActivityTypeEnum.SPECIAL == PromotionActivityTypeEnum.getByCode(z.getPromotionActivityType())).map(t -> t.getId()).collect(Collectors.toList());
        Map<Long, List<PromotionGoodsLimitDTO>> promotionGoodsLimitDTOMap = promotionGoodsLimitDTOList.stream().collect(Collectors.groupingBy(PromotionGoodsLimitDTO::getGoodsId));
        List<OrderSubmitRequest.DistributorOrderDTO> specialOrderList = Lists.newArrayList();
        List<OrderSubmitRequest.DistributorOrderDTO> limitOrderList = Lists.newArrayList();
        Map<Long, PromotionGoodsLimitDTO> cartLimitDTOMap = MapUtil.newHashMap();
        for (OrderSubmitRequest.DistributorOrderDTO orderInfo : request.getDistributorOrderList()) {
            List<Long> limitCartIdList = orderInfo.getCartIds().stream().filter(t -> limitCartList.contains(t)).collect(Collectors.toList());
            List<Long> specialCartIdList = orderInfo.getCartIds().stream().filter(t -> specialCartList.contains(t)).collect(Collectors.toList());
            List<Long> normalCartIdList = orderInfo.getCartIds().stream().filter(t -> !promotionCartList.contains(t)).collect(Collectors.toList());
            // 秒杀订单
            Map<Long, Integer> goodQuantityMap = Maps.newHashMap();
            if (CollectionUtil.isNotEmpty(limitCartIdList)) {
                OrderSubmitRequest.DistributorOrderDTO limitDistributorOrder = new OrderSubmitRequest.DistributorOrderDTO();
                limitDistributorOrder.setDistributorEid(orderInfo.getDistributorEid());
                limitDistributorOrder.setPaymentMethod(orderInfo.getPaymentMethod());
                limitDistributorOrder.setPaymentType(orderInfo.getPaymentType());
                limitDistributorOrder.setContractFileKeyList(orderInfo.getContractFileKeyList());
                limitDistributorOrder.setContractNumber(orderInfo.getContractNumber());
                limitDistributorOrder.setBuyerMessage(orderInfo.getBuyerMessage());
                limitDistributorOrder.setShopCustomerCouponId(null);
                limitDistributorOrder.setCartIds(limitCartIdList);
                limitOrderList.add(limitDistributorOrder);
                for (Long cartId : limitCartIdList) {
                    CartDO cartDO = cartDOMap.get(cartId);
                    Integer goodQuantity = goodQuantityMap.getOrDefault(cartDO.getDistributorGoodsId(), 0);
                    GoodsSkuInfoDTO goodsDTO = allGoodsDTOMap.get(cartDO.getGoodsSkuId());
                    List<PromotionGoodsLimitDTO> promotionGoodsLimitDTOS = promotionGoodsLimitDTOMap.get(cartDO.getDistributorGoodsId());
                    if (CollectionUtil.isEmpty(promotionGoodsLimitDTOS)) {
                        String failMsg = MessageFormat.format(OrderErrorCode.ORDER_PROMOTION_EXPIRED.getMessage(), goodsDTO.getGoodsInfo().getName());
                        throw new BusinessException(OrderErrorCode.ORDER_PROMOTION_EXPIRED, failMsg);
                    }
                    List<PromotionGoodsLimitDTO> limitPromotionGoods = promotionGoodsLimitDTOS.stream().filter(t -> t.getPromotionActivityId().equals(cartDO.getPromotionActivityId())).collect(Collectors.toList());
                    if (CollectionUtil.isEmpty(limitPromotionGoods)) {
                        String failMsg = MessageFormat.format(OrderErrorCode.ORDER_PROMOTION_EXPIRED.getMessage(), goodsDTO.getGoodsInfo().getName());
                        throw new BusinessException(OrderErrorCode.ORDER_PROMOTION_EXPIRED, failMsg);
                    }
                    PromotionGoodsLimitDTO limitDTO = limitPromotionGoods.stream().findFirst().get();
                    if (CompareUtil.compare(limitDTO.getLeftBuyCount(), cartDO.getQuantity() + goodQuantity) < 0) {
                        String failMsg = MessageFormat.format(OrderErrorCode.ORDER_PROMOTION_LIMITD.getMessage(), goodsDTO.getGoodsInfo().getName());
                        throw new BusinessException(OrderErrorCode.ORDER_PROMOTION_LIMITD, failMsg);
                    }
                    goodQuantityMap.put(cartDO.getDistributorGoodsId(), goodQuantity + cartDO.getQuantity());
                    cartLimitDTOMap.put(cartDO.getId(), limitDTO);
                }
            }
            // 特价订单
            if (CollectionUtil.isNotEmpty(specialCartIdList)) {
                OrderSubmitRequest.DistributorOrderDTO specialDistributorOrder = new OrderSubmitRequest.DistributorOrderDTO();
                specialDistributorOrder.setDistributorEid(orderInfo.getDistributorEid());
                specialDistributorOrder.setPaymentMethod(orderInfo.getPaymentMethod());
                specialDistributorOrder.setPaymentType(orderInfo.getPaymentType());
                specialDistributorOrder.setContractFileKeyList(orderInfo.getContractFileKeyList());
                specialDistributorOrder.setContractNumber(orderInfo.getContractNumber());
                specialDistributorOrder.setBuyerMessage(orderInfo.getBuyerMessage());
                specialDistributorOrder.setShopCustomerCouponId(null);
                specialDistributorOrder.setCartIds(specialCartIdList);
                specialOrderList.add(specialDistributorOrder);
                for (Long cartId : specialCartIdList) {
                    CartDO cartDO = cartDOMap.get(cartId);
                    GoodsSkuInfoDTO goodsDTO = allGoodsDTOMap.get(cartDO.getGoodsSkuId());
                    List<PromotionGoodsLimitDTO> promotionGoodsLimitDTOS = promotionGoodsLimitDTOMap.get(cartDO.getDistributorGoodsId());
                    Integer goodQuantity = goodQuantityMap.getOrDefault(cartDO.getDistributorGoodsId(), 0);
                    if (CollectionUtil.isEmpty(promotionGoodsLimitDTOS)) {
                        String failMsg = MessageFormat.format(OrderErrorCode.ORDER_PROMOTION_EXPIRED.getMessage(), goodsDTO.getGoodsInfo().getName());
                        throw new BusinessException(OrderErrorCode.ORDER_PROMOTION_EXPIRED, failMsg);
                    }
                    List<PromotionGoodsLimitDTO> specialPromotionGoods = promotionGoodsLimitDTOS.stream().filter(t -> t.getPromotionActivityId().equals(cartDO.getPromotionActivityId())).collect(Collectors.toList());
                    if (CollectionUtil.isEmpty(specialPromotionGoods)) {
                        String failMsg = MessageFormat.format(OrderErrorCode.ORDER_PROMOTION_EXPIRED.getMessage(), goodsDTO.getGoodsInfo().getName());
                        throw new BusinessException(OrderErrorCode.ORDER_PROMOTION_EXPIRED, failMsg);
                    }
                    PromotionGoodsLimitDTO specialDTO = specialPromotionGoods.stream().findFirst().get();
                    if (CompareUtil.compare(specialDTO.getLeftBuyCount(), cartDO.getQuantity() + goodQuantity) < 0) {
                        String failMsg = MessageFormat.format(OrderErrorCode.ORDER_PROMOTION_LIMITD.getMessage(), goodsDTO.getGoodsInfo().getName());
                        throw new BusinessException(OrderErrorCode.ORDER_PROMOTION_LIMITD, failMsg);
                    }
                    cartLimitDTOMap.put(cartDO.getId(), specialDTO);
                    goodQuantityMap.put(cartDO.getDistributorGoodsId(), goodQuantity + cartDO.getQuantity());
                }
            }
            orderInfo.setCartIds(normalCartIdList);
        }

        if (CollectionUtil.isNotEmpty(limitOrderList)) {
            request.getDistributorOrderList().addAll(limitOrderList);
        }
        if (CollectionUtil.isNotEmpty(specialOrderList)) {
            request.getDistributorOrderList().addAll(specialOrderList);
        }
        List<OrderSubmitRequest.DistributorOrderDTO> distributorOrderList = request.getDistributorOrderList().stream().filter(t -> CollectionUtil.isNotEmpty(t.getCartIds())).collect(Collectors.toList());
        request.setDistributorOrderList(distributorOrderList);

        return cartLimitDTOMap;
    }


    /**
     * 构建订单数据
     *
     * @param request
     * @param orderBatchNo
     * @param yilingSubEids
     * @param managerInfoFunction
     * @param contacterFunction
     * @param deliveryAddressDTO
     * @param industryDirectEids
     * @param allCartDOMap
     * @param allEnterpriseDTOMap
     * @param allGoodsDTOMap
     * @param customerDTOMap
     * @param cartLimitDTOMap
     * @return
     */
    private List<CreateOrderRequest> buildOrderRequest(OrderSubmitRequest request, String orderBatchNo, List<Long> yilingSubEids, Function<Long, EnterpriseEmployeeDTO> managerInfoFunction, Function<Long, UserDTO> contacterFunction, Function<NoEnum, String> orderNoFunction, DeliveryAddressDTO deliveryAddressDTO, List<Long> industryDirectEids, Map<Long, CartDO> allCartDOMap, Map<Long, EnterpriseDTO> allEnterpriseDTOMap, Map<Long, GoodsSkuInfoDTO> allGoodsDTOMap, Map<Long, EnterpriseCustomerDTO> customerDTOMap, Map<Long, PromotionGoodsLimitDTO> cartLimitDTOMap,Consumer<CreateOrderRequest> initOrderFunction) {
        List<CreateOrderRequest> createOrderRequestList = CollUtil.newArrayList();
        // 是否包含以岭卖家
        boolean hasYilingSeller = request.getDistributorOrderList().stream().filter(e -> yilingSubEids.contains(e.getDistributorEid())).findFirst().isPresent();
        // 省区经理信息
        EnterpriseEmployeeDTO provinceManagerInfo = null;
        if (hasYilingSeller) {
            provinceManagerInfo = managerInfoFunction.apply(request.getContacterId());
        }
        for (OrderSubmitRequest.DistributorOrderDTO orderInfo : request.getDistributorOrderList()) {
            EnterpriseDTO enterpriseDTO =  allEnterpriseDTOMap.get(request.getBuyerEid());
            CreateOrderRequest createOrderRequest = new CreateOrderRequest();
            createOrderRequest.setOrderNo(orderNoFunction.apply(NoEnum.ORDER_NO));
            createOrderRequest.setSplitOrderType(SplitOrderEnum.SPECIAL.name());
            createOrderRequest.setPaymentMethod(orderInfo.getPaymentMethod());
            createOrderRequest.setBatchNo(orderBatchNo);
            createOrderRequest.setBuyerEid(request.getBuyerEid());
            createOrderRequest.setBuyerEname(enterpriseDTO.getName());
            createOrderRequest.setSellerEid(orderInfo.getDistributorEid());
            createOrderRequest.setSellerEname(allEnterpriseDTOMap.get(orderInfo.getDistributorEid()).getName());
            createOrderRequest.setSellerErpCode(allEnterpriseDTOMap.get(orderInfo.getDistributorEid()).getErpCode());
            createOrderRequest.setDistributorEid(createOrderRequest.getSellerEid());
            createOrderRequest.setDistributorEname(createOrderRequest.getSellerEname());
            createOrderRequest.setPaymentType(orderInfo.getPaymentType());
            createOrderRequest.setOrderType(request.getOrderTypeEnum().getCode());
            createOrderRequest.setOrderSource(request.getOrderSourceEnum().getCode());
            createOrderRequest.setOrderNote(orderInfo.getBuyerMessage());
            createOrderRequest.setOpUserId(request.getOpUserId());
            createOrderRequest.setBuyerProvinceCode(enterpriseDTO.getProvinceCode());
            createOrderRequest.setBuyerCityCode(enterpriseDTO.getCityCode());
            createOrderRequest.setBuyerRegionCode(enterpriseDTO.getRegionCode());
            // pop订单创建，商务联系人
            if (ObjectUtil.isNotNull(contacterFunction)) {

                UserDTO userDTO = contacterFunction.apply(request.getContacterId());
                createOrderRequest.setContacterId(userDTO != null ? userDTO.getId() : 0L);
                createOrderRequest.setContacterName(userDTO != null ? userDTO.getName() : "");
            }

            if (yilingSubEids.contains(orderInfo.getDistributorEid())) {
                createOrderRequest.setProvinceManagerId(provinceManagerInfo != null ? provinceManagerInfo.getUserId() : 0L);
                createOrderRequest.setProvinceManagerCode(provinceManagerInfo != null ? provinceManagerInfo.getCode() : "");
            }
            // 企业信息账号
            if (yilingSubEids.contains(orderInfo.getDistributorEid()) || industryDirectEids.contains(orderInfo.getDistributorEid())) {
                createOrderRequest.setCustomerErpCode(request.getEasAccount());

            } else {

                EnterpriseCustomerDTO customerDTO = customerDTOMap.get(orderInfo.getDistributorEid());

                if (customerDTO != null && StringUtils.isNotBlank(customerDTO.getCustomerErpCode())) {

                    createOrderRequest.setCustomerErpCode(customerDTO.getCustomerErpCode());
                }
            }
            // 初始化赠品信息,防止后续获取活动信息时报错
            createOrderRequest.setPromotionActivityRequestList(Lists.newArrayList());
            createOrderRequest.setOrderGiftRequestList(Collections.emptyList());
            createOrderRequest.setOrderCouponUseList(Collections.emptyList());

            // 商品明细数据组装
            {
                List<CreateOrderDetailRequest> createOrderDetailRequestList = CollUtil.newArrayList();
                List<CreateOrderPromotionActivityRequest> promotionActivityRequestList = CollUtil.newArrayList();
                for (Long cartId : orderInfo.getCartIds()) {
                    CartDO cartDO = allCartDOMap.get(cartId);
                    GoodsSkuInfoDTO goodsDTO = allGoodsDTOMap.get(cartDO.getGoodsSkuId());
                    CreateOrderDetailRequest createOrderDetailRequest = this.buildOrderDetailRequest(createOrderRequest.getOrderNo(), cartDO, goodsDTO);
                    // 保存促销类型以及活动ID
                    PromotionGoodsLimitDTO goodsLimitDTO = cartLimitDTOMap.get(cartId);
                    createOrderDetailRequest.setPromotionActivityType(goodsLimitDTO.getType());
                    createOrderDetailRequest.setPromotionActivityId(goodsLimitDTO.getPromotionActivityId());
                    createOrderDetailRequest.setPromotionActivityPrice(goodsLimitDTO.getPrice());
                    createOrderDetailRequest.setGoodsPrice(goodsLimitDTO.getPromotionPrice());
                    CreateOrderPromotionActivityRequest activityRequest = new CreateOrderPromotionActivityRequest();
                    activityRequest.setActivityId(goodsLimitDTO.getPromotionActivityId());
                    activityRequest.setActivityName(goodsLimitDTO.getPromotionName());
                    activityRequest.setActivityBear(goodsLimitDTO.getBear());
                    activityRequest.setActivityType(goodsLimitDTO.getType());
                    activityRequest.setSponsorType(goodsLimitDTO.getSponsorType());
                    activityRequest.setActivityPlatformPercent(goodsLimitDTO.getPlatformPercent());
                    promotionActivityRequestList.add(activityRequest);
                    createOrderDetailRequestList.add(createOrderDetailRequest);
                }
                //去除重复，同一个订单，同一个活动只需要记录一次
                promotionActivityRequestList = promotionActivityRequestList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(CreateOrderPromotionActivityRequest::getActivityId))), ArrayList::new));
                createOrderRequest.setOrderDetailList(createOrderDetailRequestList);
                createOrderRequest.setPromotionActivityRequestList(promotionActivityRequestList);
            }
            // 收货地址
            createOrderRequest.setOrderAddressInfo(this.buildOrderAddressRequest(deliveryAddressDTO));
            // 购销合同
            createOrderRequest.setContractFileKeyList(orderInfo.getContractFileKeyList());
            // 设置订单状态
            initOrderFunction.accept(createOrderRequest);

            createOrderRequestList.add(createOrderRequest);
        }

        return createOrderRequestList;
    }


    @Override
    protected void calculateOrderMoney(Function<QueryGoodsPriceRequest, Map<Long, GoodsPriceDTO>> priceFunction, List<CreateOrderRequest> createOrderRequestList, Long buyerEid) {
        Map<String, String> mdcContext = MDC.getCopyOfContextMap();
        List<CreateOrderDetailRequest> allCreateOrderDetailRequestList = createOrderRequestList.stream().map(CreateOrderRequest::getOrderDetailList).flatMap(Collection::stream).collect(Collectors.toList());
        List<Long> distributorGoodsIds = allCreateOrderDetailRequestList.stream().map(CreateOrderDetailRequest::getDistributorGoodsId).distinct().collect(Collectors.toList());
        // 获取价格
        Map<Long, GoodsPriceDTO> goodsPriceMap = this.getGoodsPriceDto(priceFunction, buyerEid, distributorGoodsIds);
        MDC.setContextMap(mdcContext);
        if (log.isDebugEnabled()) {
            log.debug("specialOrderSplitHandler..calculateOrderMoney..goodsPriceMap:{}", goodsPriceMap);
        }
        // 计算订单明细上的金额
        createOrderRequestList.forEach(orderInfo -> orderInfo.getOrderDetailList().forEach(orderDetail -> {
            GoodsPriceDTO goodsPriceDTO = goodsPriceMap.get(orderDetail.getDistributorGoodsId());
            // 设置限价
            orderDetail.setLimitPrice(goodsPriceDTO.getLimitPrice());
            // 商品购买价格
            orderDetail.setGoodsPrice(goodsPriceDTO.getBuyPrice());
            // 定价系统原始价格
            orderDetail.setOriginalPrice(goodsPriceDTO.getLinePrice());
            // 设置小计金额
            orderDetail.setGoodsAmount(NumberUtil.round(NumberUtil.mul(orderDetail.getGoodsPrice(), orderDetail.getGoodsQuantity()), 2));
        }));
        // 设置订单金额
        createOrderRequestList.forEach(orderInfo -> {
            List<CreateOrderDetailRequest> createOrderDetailRequestList = orderInfo.getOrderDetailList();
            orderInfo.setTotalAmount(createOrderDetailRequestList.stream().map(CreateOrderDetailRequest::getGoodsAmount).reduce(BigDecimal::add).get());
            orderInfo.setFreightAmount(BigDecimal.ZERO);
            orderInfo.setPaymentAmount(orderInfo.getTotalAmount());
        });
        log.info("specialOrderSplitHandler..calculateOrderMoney..result:{}", createOrderRequestList);
    }
}
