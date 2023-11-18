package com.yiling.mall.order.service.impl;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.framework.common.thread.SpringAsyncConfig;
import com.yiling.framework.common.util.Constants;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsSkuInfoDTO;
import com.yiling.mall.cart.entity.CartDO;
import com.yiling.mall.cart.service.CartService;
import com.yiling.mall.order.bo.CalOrderDiscountContextBO;
import com.yiling.mall.order.bo.OrderSubmitBO;
import com.yiling.mall.order.bo.SplitOrderContextBO;
import com.yiling.mall.order.bo.SplitOrderEnum;
import com.yiling.mall.order.bo.SplitOrderResultBO;
import com.yiling.mall.order.dto.request.OrderSubmitRequest;
import com.yiling.mall.order.event.CreateOrderEvent;
import com.yiling.mall.order.handler.DiscountHandlerChain;
import com.yiling.mall.order.handler.DiscountProcessHandler;
import com.yiling.mall.order.service.OrderSubmitService;
import com.yiling.marketing.paypromotion.dto.request.SavePayPromotionRecordRequest;
import com.yiling.marketing.promotion.api.PromotionActivityApi;
import com.yiling.marketing.promotion.dto.PromotionActivityDTO;
import com.yiling.marketing.promotion.dto.PromotionCombinationPackDTO;
import com.yiling.marketing.promotion.dto.PromotionDTO;
import com.yiling.marketing.promotion.dto.PromotionGoodsLimitDTO;
import com.yiling.marketing.promotion.dto.PromotionReduceStockDTO;
import com.yiling.marketing.promotion.dto.request.PromotionActivityRequest;
import com.yiling.marketing.promotion.dto.request.PromotionAppRequest;
import com.yiling.marketing.promotion.enums.PromotionSponsorTypeEnum;
import com.yiling.marketing.promotion.enums.PromotionTypeEnum;
import com.yiling.order.order.api.NoApi;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderPromotionActivityApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.request.CreateOrderPromotionActivityRequest;
import com.yiling.order.order.dto.request.CreateOrderRequest;
import com.yiling.order.order.enums.CustomerConfirmEnum;
import com.yiling.order.order.enums.NoEnum;
import com.yiling.order.order.enums.OrderAuditStatusEnum;
import com.yiling.order.order.enums.OrderErrorCode;
import com.yiling.order.order.enums.OrderSourceEnum;
import com.yiling.order.order.enums.OrderStatusEnum;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.order.order.enums.PaymentMethodEnum;
import com.yiling.order.order.enums.PaymentStatusEnum;
import com.yiling.order.order.enums.PromotionActivityTypeEnum;
import com.yiling.pricing.goods.api.GoodsPriceApi;
import com.yiling.user.enterprise.api.CustomerApi;
import com.yiling.user.enterprise.api.DeliveryAddressApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseCustomerDTO;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.enums.EnterpriseChannelEnum;
import com.yiling.user.payment.api.PaymentDaysAccountApi;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * B2B订单服务实现类
 * </p>
 *
 * @author zhigang.guo
 * @date 2021-10-11
 */
@Service("b2bOrderSubmitService")
@Slf4j
public class B2bOrderSubmitServiceImpl extends OrderSubmitServiceImpl implements OrderSubmitService {
    @Autowired
    CartService                         cartService;
    @DubboReference
    NoApi                               noApi;
    @DubboReference
    EnterpriseApi                       enterpriseApi;
    @DubboReference
    GoodsApi                            goodsApi;
    @DubboReference
    OrderApi                            orderApi;
    @DubboReference
    DeliveryAddressApi                  deliveryAddressApi;
    @DubboReference
    CustomerApi                         customerApi;
    @DubboReference
    PaymentDaysAccountApi               paymentDaysAccountApi;
    @DubboReference
    PromotionActivityApi                promotionActivityApi;
    @DubboReference
    GoodsPriceApi                       goodsPriceApi;
    @DubboReference
    OrderPromotionActivityApi           orderPromotionActivityApi;
    @Autowired
    private RedisService                redisService;
    @Autowired
    private SpringAsyncConfig           springAsyncConfig;
    @Autowired
    private List<DiscountProcessHandler> discountProcessHandlerList;
    @Lazy
    @Autowired
    private B2bOrderSubmitServiceImpl   _this;

    /**
     * 非销售助手b2b订单
     * @param orderTypeEnum 订单类型
     * @param orderSourceEnum 订单来源
     * @return
     */
    @Override
    public boolean matchOrder(OrderTypeEnum orderTypeEnum, OrderSourceEnum orderSourceEnum) {
        if (OrderTypeEnum.B2B == orderTypeEnum && OrderSourceEnum.SA != orderSourceEnum) {

            return true;
        }
        return false;
    }


    /**
     * 订单拆单
     * @param contextBO
     * @return
     */
    private List<CreateOrderRequest> splitOrder(SplitOrderContextBO contextBO ) {
        List<CreateOrderRequest> resultList = Lists.newArrayList();
        try {
            CompletableFuture<SplitOrderResultBO> combinationFuture = CompletableFuture
                    .supplyAsync(() -> SplitOrderEnum.COMBINATION.handlerInstance().splitOrder(contextBO), springAsyncConfig.getAsyncExecutor())
                    .whenComplete((splitOrderResultBO, throwable) -> resultList.addAll(splitOrderResultBO.getCreateOrderRequestList()));
            CompletableFuture<SplitOrderResultBO> specialFuture = CompletableFuture
                    .supplyAsync(() -> SplitOrderEnum.SPECIAL.handlerInstance().splitOrder(contextBO), springAsyncConfig.getAsyncExecutor())
                    .whenComplete((splitOrderResultBO, throwable) -> resultList.addAll(splitOrderResultBO.getCreateOrderRequestList()));
            CompletableFuture<SplitOrderResultBO> normalFuture = CompletableFuture
                    .supplyAsync(() -> SplitOrderEnum.NORMAL.handlerInstance().splitOrder(contextBO), springAsyncConfig.getAsyncExecutor())
                    .whenComplete((splitOrderResultBO, throwable) -> resultList.addAll(splitOrderResultBO.getCreateOrderRequestList()));
            CompletableFuture.allOf(combinationFuture, specialFuture,normalFuture).join();
        } catch (Exception e) {
            if (e.getCause() instanceof  BusinessException) {
                throw  (BusinessException) e.getCause();
            }
            throw new BusinessException(OrderErrorCode.SPLIT_ORDER_ERROR,e);
        }

        return resultList;
    }



    /**
     * 校验组合包商品限购逻辑
     * @param cartDTOs
     * @param promotionDTOList
     */
    private void validateCombinationOrderLimit(List<CartDO> cartDTOs, List<PromotionDTO> promotionDTOList){

        if (promotionDTOList.stream().anyMatch(t -> CompareUtil.compare(t.getAvailable(),1) != 0) ) {
            log.warn("组合活动已经失效..,result:{}",promotionDTOList);
            throw new BusinessException(OrderErrorCode.ORDER_PROMOTION_ERROR);
        }

        Map<Long,PromotionDTO> promotionDTOMap = promotionDTOList.stream().collect(Collectors.toMap(t -> t.getPromotionCombinationPackDTO().getPromotionActivityId(),Function.identity()));

        List<CartDO> limitBuyCartDTOList = cartDTOs.stream().filter(cartDTO -> {
        PromotionDTO  promotionDTO = promotionDTOMap.get(cartDTO.getPromotionActivityId());
        if (ObjectUtil.isNull(promotionDTO)) {
            return false;
        }
        return  promotionDTO.getPromotionCombinationPackDTO().getTotalNum() != 0 ;
        }).collect(Collectors.toList());

        // 是否校验限购商品信息
        if (CollectionUtil.isEmpty(limitBuyCartDTOList)) {

            return;
        }

        List<Long> activityIds = limitBuyCartDTOList.stream().map(t -> t.getPromotionActivityId()).collect(Collectors.toList());
        Long buyerEid = limitBuyCartDTOList.stream().findFirst().get().getBuyerEid();

        // 组合包活动已购买数量
        Map<Long,Long> combinationBuyNumberBOMap = orderPromotionActivityApi.sumBatchCombinationActivityNumber(activityIds);

        if (log.isDebugEnabled()) {

            log.debug("查询用户已购买组合包数量入参：{}，{},返回:[{}]",activityIds,buyerEid,JSON.toJSONString(combinationBuyNumberBOMap));
        }

        limitBuyCartDTOList.forEach(cartDTO -> {
            PromotionDTO promotionDTO = promotionDTOMap.get(cartDTO.getPromotionActivityId());
            PromotionCombinationPackDTO promotionCombinationPackDTO = promotionDTO.getPromotionCombinationPackDTO();
            // 校验总购买数量
            Long sumQty = combinationBuyNumberBOMap.getOrDefault(cartDTO.getPromotionActivityId(),0l);
            if (CompareUtil.compare(cartDTO.getQuantity() + sumQty.intValue(),promotionCombinationPackDTO.getTotalNum()) > 0) {
                String failMsg = MessageFormat.format(OrderErrorCode.SUBMIT_COMBINATION_ORDER_LIMIT_ERROR.getMessage(), promotionCombinationPackDTO.getPackageName());
                throw new BusinessException(OrderErrorCode.SUBMIT_COMBINATION_ORDER_LIMIT_ERROR,failMsg);
            }
        });
    }

    /**
     * 组合商品转换，查询组合活动信息
     * @param allCartDOList
     * @return
     */
    private List<CartDO> combinationCartTransfer(Long buyerId,List<CartDO> allCartDOList,List<PromotionGoodsLimitDTO> promotionGoodsLimitDTOS) {
        List<CartDO> combinationCartDOList = allCartDOList.stream().filter(t -> SplitOrderEnum.COMBINATION == SplitOrderEnum.getByPromotionActivityCode(t.getPromotionActivityType())).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(combinationCartDOList)) {
            return allCartDOList;
        }
        // 组合商品
        Map<Long,CartDO> combinationCartMap = combinationCartDOList.stream().collect(Collectors.toMap(CartDO::getPromotionActivityId,Function.identity()));
        // 非组合商品信息
        allCartDOList = allCartDOList.stream().filter(t -> SplitOrderEnum.COMBINATION != SplitOrderEnum.getByPromotionActivityCode(t.getPromotionActivityType())).collect(Collectors.toList());
        List<Long> activityIds = combinationCartDOList.stream().map(t -> t.getPromotionActivityId()).distinct().collect(Collectors.toList());
        PromotionActivityRequest request = new PromotionActivityRequest();
        request.setBuyerEid(buyerId);
        request.setGoodsPromotionActivityIdList(activityIds);
        List<PromotionDTO> promotionDTOList = promotionActivityApi.queryPromotionInfoByActivityAndBuyerId(request);
        if (log.isDebugEnabled()) {
            log.debug("...queryPromotionInfoByActivityAndBuyerId.. result:{}",JSON.toJSONString(promotionDTOList));
        }

        if (CollectionUtil.isEmpty(promotionDTOList) || promotionDTOList.size() != activityIds.size()) {
            log.warn("组合活动已经失效..,result:{}",promotionDTOList);
            throw new BusinessException(OrderErrorCode.ORDER_PROMOTION_ERROR);
        }

        Map<Long,PromotionDTO> promotionDTOMap = promotionDTOList.stream().collect(Collectors.toMap(t -> t.getPromotionCombinationPackDTO().getPromotionActivityId(),Function.identity()));
        // 组合商品限购
        this.validateCombinationOrderLimit(combinationCartDOList,promotionDTOList);

        List<CartDO> newCombinationCartDOList = Lists.newArrayList();
        // 生成组合商品的购物车信息数据
        for (Map.Entry<Long,PromotionDTO> entry : promotionDTOMap.entrySet()) {
            CartDO cartDO = combinationCartMap.get(entry.getKey());
            PromotionDTO promotionDTO = entry.getValue();
            PromotionActivityDTO promotionActivityDTO =  promotionDTO.getPromotionActivityDTO();
            // 将活动信息，设置到对应的商品信息中
            promotionDTO.getGoodsLimitDTOList().forEach(promotionGoodsLimitDTO -> {
                promotionGoodsLimitDTO.setBear(promotionActivityDTO.getBear());
                promotionGoodsLimitDTO.setType(promotionActivityDTO.getType());
                promotionGoodsLimitDTO.setSponsorType(promotionActivityDTO.getPromotionType());
                promotionGoodsLimitDTO.setPromotionName(promotionActivityDTO.getName());
                promotionGoodsLimitDTO.setPlatformPercent(promotionActivityDTO.getPlatformPercent());
            });
            promotionGoodsLimitDTOS.addAll(promotionDTO.getGoodsLimitDTOList());
            promotionDTO.getGoodsLimitDTOList().forEach(t -> {
                CartDO newCartDo = new CartDO();
                newCartDo.setId(cartDO.getId());
                newCartDo.setBuyerEid(cartDO.getBuyerEid());
                newCartDo.setSellerEid(cartDO.getSellerEid());
                newCartDo.setDistributorEid(cartDO.getDistributorEid());
                newCartDo.setDistributorGoodsId(t.getGoodsId());
                newCartDo.setGoodsSkuId(t.getGoodsSkuId());
                newCartDo.setPlatformType(cartDO.getPlatformType());
                newCartDo.setGoodSource(cartDO.getGoodSource());
                newCartDo.setPromotionActivityType(cartDO.getPromotionActivityType());
                newCartDo.setPromotionActivityId(cartDO.getPromotionActivityId());
                newCartDo.setQuantity(t.getAllowBuyCount() * cartDO.getQuantity());
                newCombinationCartDOList.add(newCartDo);
            });
        }
        return Stream.of(allCartDOList, newCombinationCartDOList).flatMap(Collection::stream).distinct().collect(Collectors.toList());
    }


    /**
     * 查询特价商品信息
     * @param buyerEid 购买人企业
     * @param allCartDOList 购物车商品信息
     * @param promotionGoodsLimitDTOS 参入特价促销商品信息
     */
    private void specialPromotionActivityList(Long buyerEid,List<CartDO> allCartDOList,List<PromotionGoodsLimitDTO> promotionGoodsLimitDTOS) {

        List<CartDO> specialPriceCartList = allCartDOList.stream().filter(t -> SplitOrderEnum.SPECIAL == SplitOrderEnum.getByPromotionActivityCode(t.getPromotionActivityType())).collect(Collectors.toList());

        if (CollectionUtil.isEmpty(specialPriceCartList)) {

            return;
        }

        List<Long> distributorGoodsId = specialPriceCartList.stream().map(t -> t.getDistributorGoodsId()).distinct().collect(Collectors.toList());
        PromotionAppRequest promotionAppRequest = new PromotionAppRequest();
        promotionAppRequest.setPlatform(PlatformEnum.B2B.getCode());
        promotionAppRequest.setTypeList(ListUtil.toList(PromotionTypeEnum.SPECIAL_PRICE.getType(), PromotionTypeEnum.SECOND_KILL.getType()));
        promotionAppRequest.setGoodsIdList(distributorGoodsId);
        promotionAppRequest.setBuyerEid(buyerEid);
        List<PromotionGoodsLimitDTO> promotionGoodsLimitDTOList = promotionActivityApi.queryGoodsPromotionInfo(promotionAppRequest);

        if (log.isDebugEnabled()) {

            log.debug("..调用营销接口查询参与秒杀&特价商品信息..参数:[{}],返回结果:[{}]",promotionAppRequest,promotionGoodsLimitDTOList);
        }

        // 促销活动失效
        if (CollectionUtil.isEmpty(promotionGoodsLimitDTOList)) {

            throw new BusinessException(OrderErrorCode.ORDER_PROMOTION_ERROR);
        }

        promotionGoodsLimitDTOS.addAll(promotionGoodsLimitDTOList);

    }

    /**
     * 创建订单
     * @param request 下单参数
     * @param createOrderRequestList 拆单结果
     * @return
     */
    @GlobalTransactional
    public OrderSubmitBO createOrder(OrderSubmitRequest request, List<CreateOrderRequest> createOrderRequestList) {

        // 购物车记录
        List<Long> allCartIds = request.getDistributorOrderList().stream().map(OrderSubmitRequest.DistributorOrderDTO::getCartIds).flatMap(Collection::stream).collect(Collectors.toList());
        // 平台优惠劵Id
        Long platformCustomerCouponId = CompareUtil.compare(0l, request.getPlatformCustomerCouponId(), true) >= 0 ? null : request.getPlatformCustomerCouponId();
        // 平台支付促销活动Id
        Long platformPaymentActivityId = CompareUtil.compare(0l, request.getPlatformPaymentActivityId(), true) >= 0 ? null : request.getPlatformPaymentActivityId();
        // 平台支付促销活动规则Id
        Long platformActivityRuleIdId = CompareUtil.compare(0l, request.getPlatformActivityRuleIdId(), true) >= 0 ? null : request.getPlatformActivityRuleIdId();

        // 订单是否有赠品信息
        Map<Long,Boolean> orderHasGift = request.getDistributorOrderList()
                .stream()
                .collect(Collectors.toMap(OrderSubmitRequest.DistributorOrderDTO::getDistributorEid,OrderSubmitRequest.DistributorOrderDTO::getIsHasGift));
        // 商家优惠劵ID
        Map<Long, Long> shopCustomerCouponIdMap = request.getDistributorOrderList()
                .stream()
                .filter(t -> t.getShopCustomerCouponId() != null && CompareUtil.compare(t.getShopCustomerCouponId(),0l) > 0 )
                .collect(Collectors.toMap(OrderSubmitRequest.DistributorOrderDTO::getDistributorEid,OrderSubmitRequest.DistributorOrderDTO::getShopCustomerCouponId));
        // 商家支付促销活动ID
        Map<Long, Long> shopPaymentActivityIdMap = request.getDistributorOrderList()
                .stream()
                .filter(t -> t.getShopPaymentActivityId() != null && CompareUtil.compare(t.getShopPaymentActivityId(),0l) > 0)
                .collect(Collectors.toMap(OrderSubmitRequest.DistributorOrderDTO::getDistributorEid,OrderSubmitRequest.DistributorOrderDTO::getShopPaymentActivityId));
        // 商家支付促销活动规则Id
        Map<Long, Long> shopActivityRuleIdIdMap = request.getDistributorOrderList()
                .stream()
                .filter(t -> t.getShopActivityRuleIdId() != null && CompareUtil.compare(t.getShopActivityRuleIdId(),0l) > 0)
                .collect(Collectors.toMap(OrderSubmitRequest.DistributorOrderDTO::getDistributorEid,OrderSubmitRequest.DistributorOrderDTO::getShopActivityRuleIdId));

        // 2,计算营销优惠
        CalOrderDiscountContextBO<CreateOrderRequest> discountContextBO = new CalOrderDiscountContextBO();
        discountContextBO.setPlatformEnum(PlatformEnum.B2B);
        discountContextBO.setOrderTypeEnum(OrderTypeEnum.B2B);
        discountContextBO.setBuyerEid(request.getBuyerEid());
        discountContextBO.setOpUserId(request.getOpUserId());
        discountContextBO.setPlatformCustomerCouponId(platformCustomerCouponId);
        discountContextBO.setPlatformPaymentActivityId(platformPaymentActivityId);
        discountContextBO.setPlatformActivityRuleIdId(platformActivityRuleIdId);
        discountContextBO.setShopCustomerCouponIdMap(shopCustomerCouponIdMap);
        discountContextBO.setShopPaymentActivityIdMap(shopPaymentActivityIdMap);
        discountContextBO.setShopActivityRuleIdIdMap(shopActivityRuleIdIdMap);
        discountContextBO.setCreateOrderRequestList(createOrderRequestList);
        discountContextBO.setOrderHasGiftMap(orderHasGift);

        DiscountHandlerChain handlerChain = new DiscountHandlerChain(discountContextBO);
        handlerChain.addHandler(discountProcessHandlerList).processDiscount();

        // 3、校验账期余额是否充足
        this.checkPaymentAccount(createOrderRequestList);

        // 4、保存订单数据
        orderApi.create(createOrderRequestList);

        // 5、获取订单信息
        List<String> orderNos = createOrderRequestList.stream().map(CreateOrderRequest::getOrderNo).collect(Collectors.toList());
        List<OrderDTO> orderDTOList = orderApi.listByOrderNos(orderNos);

        // 6,如果存在在线支付订单，创建在线支付交易
        String payId = this.initPaymentOrder(orderDTOList);

        // 7、清理购物车
        cartService.removeByIds(allCartIds);

        //8、发送订单消息Mq
        List<MqMessageBO> mqMessageBOList = this.sendOrderCreatedMessage(orderDTOList);

        // 9,清除用户会话信息
        String userSessionId = StringUtils.join("order:submit:session:",request.getBuyerEid());
        String settlementSessionId = StringUtils.join("order:settlement:session:",request.getBuyerEid());
        if (redisService.hasKey(userSessionId)) {
            redisService.del(userSessionId);
        }
        if (redisService.hasKey(settlementSessionId)) {
            redisService.del(settlementSessionId);
        }

        // 是否有特价商品
        Boolean haveSpecialProduct = createOrderRequestList.stream().anyMatch(t -> SplitOrderEnum.SPECIAL == SplitOrderEnum.getByCode(t.getSplitOrderType())); // 是否有特价商品
        OrderSubmitBO submitBo = new OrderSubmitBO();
        submitBo.setPayId(payId);
        submitBo.setOrderDTOList(orderDTOList);
        submitBo.setMqMessageBOList(mqMessageBOList);

        /**
         * 创建成功，扣减优惠逻辑
         */
        CreateOrderEvent createOrderEvent = new CreateOrderEvent(this,orderDTOList,true,haveSpecialProduct);
        createOrderEvent.setPlatformCustomerCouponId(discountContextBO.getPlatformCustomerCouponId());
        createOrderEvent.setShopCustomerCouponIds(MapUtil.isEmpty(discountContextBO.getShopCustomerCouponIdMap()) ? null : ListUtil.toList(discountContextBO.getShopCustomerCouponIdMap().values()));
        createOrderEvent.setPromotionReduceStockList(this.setPromotionReduceStockList(createOrderRequestList));
        createOrderEvent.setPaymentReduceStockList(this.setPaymentReduceStockList(request.getOpUserId(),createOrderRequestList,orderDTOList));
        createOrderEvent.setOrderSourceEnum(request.getOrderSourceEnum());
        createOrderEvent.setOrderTypeEnum(request.getOrderTypeEnum());
        createOrderEvent.setUserId(request.getOpUserId());
        createOrderEvent.setUserAgent(request.getUserAgent());
        createOrderEvent.setIp(request.getIpAddress());
        createOrderEvent.setSaveOrderDevice(true);


        this.applicationEventPublisher.publishEvent(createOrderEvent);


        return submitBo;
    }

    @Override
    public OrderSubmitBO submit(OrderSubmitRequest request) {
        // 所有购物车信息
        List<Long> allCartIds = request.getDistributorOrderList().stream().map(OrderSubmitRequest.DistributorOrderDTO::getCartIds).flatMap(Collection::stream).collect(Collectors.toList());
        List<CartDO> allCartDOList = cartService.listByIds(allCartIds);

        // 以岭子企业EId
        List<Long> yilingSubEids = enterpriseApi.listSubEids(Constants.YILING_EID);
        // 校验购物车
        this.validateCart(allCartDOList, allCartIds);
        // 秒杀特价组合包活动信息
        List<PromotionGoodsLimitDTO> promotionGoodsLimitDTOS = Lists.newArrayList();
        // 组合优惠购物车商品转换
        allCartDOList = this.combinationCartTransfer(request.getBuyerEid(),allCartDOList,promotionGoodsLimitDTOS);
        // 获取特价促销活动商品信息
        this.specialPromotionActivityList(request.getBuyerEid(), allCartDOList,promotionGoodsLimitDTOS);
        // 所有SKU商品ID集合
        List<Long> allGoodsSkuIds = allCartDOList.stream().map(CartDO::getGoodsSkuId).distinct().collect(Collectors.toList());
        // 所有商品信息集合
        List<GoodsSkuInfoDTO> allGoodsDTOList = goodsApi.batchQueryInfoBySkuIds(allGoodsSkuIds);
        Map<Long, GoodsSkuInfoDTO> allGoodsDTOMap = allGoodsDTOList.stream().collect(Collectors.toMap(GoodsSkuInfoDTO::getId, Function.identity()));
        // 获取最新的商品Id
        this.setLastMergeGoodId(allCartDOList,allGoodsDTOMap,yilingSubEids);
        // 所有配送商商品ID集合
        List<Long> allDistributorGoodsIds = allCartDOList.stream().map(CartDO::getDistributorGoodsId).distinct().collect(Collectors.toList());
        // 校验商品状态
        this.validateGoodsStatus(allCartDOList, allGoodsDTOMap);
        // 校验商品库存
        this.validateGoodsInventory(allCartDOList);
        // 校验B2B商品信息
        this.checkB2BGoodsByGids(request.getBuyerEid(),allDistributorGoodsIds);
        // 校验商品限购
        this.validateGoodsLimit(request.getBuyerEid(),allCartDOList,allGoodsDTOList);

        // 所有企业信息字典
        List<Long> buyerEids = ListUtil.toList(request.getBuyerEid());
        List<Long> allDistributorEids = request.getDistributorOrderList().stream().map(OrderSubmitRequest.DistributorOrderDTO::getDistributorEid).distinct().collect(Collectors.toList());
        List<Long> allEids = Stream.of(buyerEids, allDistributorEids).flatMap(Collection::stream).distinct().collect(Collectors.toList());
        List<EnterpriseDTO> allEnterpriseDTOList = enterpriseApi.listByIds(allEids);
        // 客户信息字典
        List<EnterpriseCustomerDTO> customerDTOList = customerApi.listByEidsAndCustomerEid(allDistributorEids, request.getBuyerEid());

        // 1、拆单
        SplitOrderContextBO contextBO = SplitOrderContextBO.builder().request(request).allCartDOList(allCartDOList).build();
        contextBO.setAllEnterpriseList(allEnterpriseDTOList);
        contextBO.setCustomerDTOList(customerDTOList);
        contextBO.setAllGoodsDTOList(allGoodsDTOList);
        contextBO.setYilingSubEids(yilingSubEids);
        contextBO.setIndustryDirectEids(Optional.ofNullable(enterpriseApi.listEidsByChannel(EnterpriseChannelEnum.INDUSTRY_DIRECT)).orElse(Collections.emptyList()));
        contextBO.setOrderBatchNo(noApi.gen(NoEnum.ORDER_BATCH_NO));
        contextBO.setDeliveryAddressDTO(deliveryAddressApi.getById(request.getAddressId()));
        contextBO.setProvinceManagerFunction((c) -> selectProvinceManager(c));
        contextBO.setOrderNoFunction((c) -> noApi.gen(c));
        contextBO.setPromotionGoodsLimitDTOList(promotionGoodsLimitDTOS);
        contextBO.setPriceFunction((c) -> goodsPriceApi.queryB2bGoodsPriceInfoMap(c));
        contextBO.setInitOrderFunction((c) -> initOrderFunction(c));
        List<CreateOrderRequest> createOrderRequestList = splitOrder(contextBO);
        //冻结库存
        this.lockInventory(createOrderRequestList);

        try {
             return  _this.createOrder(request, createOrderRequestList);
        } catch (Exception e){
            log.warn("创建订单失败，开始补偿库存");
            //退还冻结库存
            this.backInventory(createOrderRequestList);
            throw e;
        }
    }


    /**
     * 校验账期余额是否充足
     * @param createOrderRequestList
     */
    private void checkPaymentAccount(List<CreateOrderRequest> createOrderRequestList) {
        List<CreateOrderRequest> paymentDayList = createOrderRequestList.stream().filter(e -> PaymentMethodEnum.PAYMENT_DAYS == PaymentMethodEnum.getByCode(e.getPaymentMethod().longValue())).collect(Collectors.toList());

        if (CollectionUtil.isEmpty(paymentDayList)) {
            return;
        }

        /**将相同卖家的支付金额进行合并，防止出现拆单导致，相同的卖家出现多条的问题**/
        Long buyerEid = paymentDayList.stream().findFirst().get().getBuyerEid();
        Map<Long,BigDecimal> paymentAmountMap =  paymentDayList.stream().collect(Collectors.groupingBy(CreateOrderRequest::getSellerEid,Collectors.mapping(CreateOrderRequest::getPaymentAmount,Collectors.reducing(BigDecimal.ZERO,BigDecimal::add))));

        for (Map.Entry<Long,BigDecimal> entry : paymentAmountMap.entrySet()) {

            BigDecimal paymentDaysAvailableAmount = paymentDaysAccountApi.getB2bAvailableAmountByCustomerEid(entry.getKey(), buyerEid);

            // 订单应付金额
            if (paymentDaysAvailableAmount.compareTo(entry.getValue()) == -1) {

                throw new BusinessException(OrderErrorCode.ORDER_PAYMENT_DAY_ERROR);
            }
        }

    }


    /**
     * 设置赠品信息集合信息
     * @param createOrderRequestList
     * @return 扣减赠品信息
     */
    private List<PromotionReduceStockDTO> setPromotionReduceStockList(List<CreateOrderRequest> createOrderRequestList) {

        List<PromotionReduceStockDTO> promotionReduceList = Lists.newArrayList();
        createOrderRequestList.forEach(createOrderRequest -> createOrderRequest.getOrderGiftRequestList().forEach(giftRequest -> {
            PromotionReduceStockDTO promotionReduceStockDTO = new PromotionReduceStockDTO();
            promotionReduceStockDTO.setPromotionActivityId(giftRequest.getPromotionActivityId());
            promotionReduceStockDTO.setGoodsGiftId(giftRequest.getGoodsGiftId());

            promotionReduceList.add(promotionReduceStockDTO);
        }));

        return promotionReduceList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(t -> StringUtils.join(t.getPromotionActivityId(),Constants.SEPARATOR_MIDDLELINE,t.getGoodsGiftId())))), ArrayList::new));
    }


    /**
     *  生成支付促销活动回调请求参数
     * @param createOrderRequestList
     * @param orderDTOList
     * @return
     */
    private List<SavePayPromotionRecordRequest> setPaymentReduceStockList(Long opUserId,List<CreateOrderRequest> createOrderRequestList,List<OrderDTO> orderDTOList) {

        Map<String, OrderDTO> orderDtoMap = orderDTOList.stream().collect(Collectors.toMap(OrderDTO::getOrderNo, Function.identity()));

        return createOrderRequestList.stream().map(t -> {
            List<CreateOrderPromotionActivityRequest> promotionActivityRequestList = t.getPromotionActivityRequestList().stream().filter(z -> PromotionActivityTypeEnum.PAYMENT == PromotionActivityTypeEnum.getByCode(z.getActivityType())).collect(Collectors.toList());
            if (CollectionUtil.isEmpty(promotionActivityRequestList)) {

                return null;
            }

            CreateOrderPromotionActivityRequest shopPaymentActivity = promotionActivityRequestList.stream().filter(h -> PromotionSponsorTypeEnum.MERCHANT == PromotionSponsorTypeEnum.getByType(h.getSponsorType())).findFirst().orElse(new CreateOrderPromotionActivityRequest());
            CreateOrderPromotionActivityRequest platformPaymentActivity = promotionActivityRequestList.stream().filter(h -> PromotionSponsorTypeEnum.PLATFORM == PromotionSponsorTypeEnum.getByType(h.getSponsorType())).findFirst().orElse(new CreateOrderPromotionActivityRequest());
            SavePayPromotionRecordRequest request = new SavePayPromotionRecordRequest();
            request.setEid(t.getBuyerEid());
            request.setEname(t.getBuyerEname());
            request.setOrderId(orderDtoMap.getOrDefault(t.getOrderNo(),new OrderDTO()).getId());
            request.setPlatformActivityId(platformPaymentActivity.getActivityId());
            request.setPlatformRuleId(platformPaymentActivity.getActivityRuleId());
            request.setShopActivityId(shopPaymentActivity.getActivityId());
            request.setShopRuleId(shopPaymentActivity.getActivityRuleId());
            request.setBatchNo(t.getBatchNo());
            request.setOpTime(new Date());
            request.setOpUserId(Optional.ofNullable(opUserId).orElse(0l));

            return request;

        }).filter(Objects::nonNull).collect(Collectors.toList());

    }



    /**
     * 订单初始化函数
     * @param createOrderRequest
     */
    private void initOrderFunction(CreateOrderRequest createOrderRequest) {

        createOrderRequest.setPaymentStatus(PaymentStatusEnum.UNPAID.getCode());
        createOrderRequest.setOrderStatus(OrderStatusEnum.UNAUDITED.getCode());
        createOrderRequest.setAuditStatus(OrderAuditStatusEnum.AUDIT_PASS.getCode());
        createOrderRequest.setCustomerConfirmStatus(CustomerConfirmEnum.CONFIRMED.getCode());
        createOrderRequest.setCustomerConfirmTime(new Date());
        createOrderRequest.setCouponDiscountAmount(BigDecimal.ZERO);
        createOrderRequest.setPlatformCouponDiscountAmount(BigDecimal.ZERO);
        createOrderRequest.setCashDiscountAmount(BigDecimal.ZERO);
        createOrderRequest.setPresaleDiscountAmount(BigDecimal.ZERO);
        createOrderRequest.setPlatformPaymentDiscountAmount(BigDecimal.ZERO);
        createOrderRequest.setShopPaymentDiscountAmount(BigDecimal.ZERO);
    }

}
