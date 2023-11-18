package com.yiling.mall.order.service.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.yiling.basic.location.api.IPLocationApi;
import com.yiling.basic.location.bo.IPLocationBO;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.basic.sms.api.SmsApi;
import com.yiling.basic.sms.enums.SmsTypeEnum;
import com.yiling.framework.common.enums.AppEnum;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.redis.RedisDistributedLock;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.thread.SpringAsyncConfig;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.rocketmq.enums.MqDelayLevel;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsSkuInfoDTO;
import com.yiling.goods.medicine.enums.GoodsSkuStatusEnum;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.mall.agreement.api.AgreementBusinessApi;
import com.yiling.mall.agreement.enums.GoodsLimitStatusEnum;
import com.yiling.mall.cart.dto.QuickBuyGoodsDTO;
import com.yiling.mall.cart.entity.CartDO;
import com.yiling.mall.cart.enums.CartGoodsSourceEnum;
import com.yiling.mall.order.bo.CalOrderDiscountContextBO;
import com.yiling.mall.order.bo.OrderSubmitBO;
import com.yiling.mall.order.bo.SplitOrderContextBO;
import com.yiling.mall.order.bo.SplitOrderEnum;
import com.yiling.mall.order.dto.request.OrderSubmitRequest;
import com.yiling.mall.order.dto.request.PresaleOrderSubmitRequest;
import com.yiling.mall.order.handler.DiscountHandlerChain;
import com.yiling.mall.order.handler.PreSaleDiscountProcessHandler;
import com.yiling.mall.order.service.PresaleOrderService;
import com.yiling.marketing.common.enums.CouponPlatformTypeEnum;
import com.yiling.marketing.presale.api.PresaleActivityApi;
import com.yiling.marketing.presale.dto.PresaleActivityGoodsDTO;
import com.yiling.marketing.presale.dto.request.QueryPresaleInfoRequest;
import com.yiling.marketing.promotion.dto.request.PromotionBuyRecord;
import com.yiling.marketing.promotion.dto.request.PromotionSaveBuyRecordRequest;
import com.yiling.order.order.api.NoApi;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderDetailApi;
import com.yiling.order.order.api.OrderDeviceInfoApi;
import com.yiling.order.order.api.PresaleOrderApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.dto.PresaleOrderDTO;
import com.yiling.order.order.dto.request.CreateOrderDeviceInfoRequest;
import com.yiling.order.order.dto.request.CreateOrderRequest;
import com.yiling.order.order.dto.request.UpdatePresaleOrderRequest;
import com.yiling.order.order.enums.CustomerConfirmEnum;
import com.yiling.order.order.enums.NoEnum;
import com.yiling.order.order.enums.OrderAuditStatusEnum;
import com.yiling.order.order.enums.OrderCategoryEnum;
import com.yiling.order.order.enums.OrderErrorCode;
import com.yiling.order.order.enums.OrderStatusEnum;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.order.order.enums.PaymentMethodEnum;
import com.yiling.order.order.enums.PaymentStatusEnum;
import com.yiling.order.order.enums.PreSalOrderReminderTypeEnum;
import com.yiling.order.order.enums.PreSaleActivityTypeEnum;
import com.yiling.order.order.enums.PromotionActivityTypeEnum;
import com.yiling.payment.enums.PaymentErrorCode;
import com.yiling.payment.enums.TradeTypeEnum;
import com.yiling.payment.pay.api.PayApi;
import com.yiling.payment.pay.dto.request.CreatePayOrderRequest;
import com.yiling.pricing.goods.api.GoodsPriceApi;
import com.yiling.user.enterprise.api.CustomerApi;
import com.yiling.user.enterprise.api.DeliveryAddressApi;
import com.yiling.user.enterprise.api.EmployeeApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseCustomerDTO;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.EnterpriseEmployeeDTO;
import com.yiling.user.enterprise.enums.EnterpriseChannelEnum;
import com.yiling.user.system.api.AppLoginInfoApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.dto.AppLoginInfoDTO;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/** 预售活动订单提交
 * @author zhigang.guo
 * @date: 2022/10/9
 */
@Component
@Slf4j
public class PresaleOrderServiceImpl implements PresaleOrderService {
    @DubboReference
    OrderApi orderApi;
    @DubboReference
    NoApi noApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    GoodsApi goodsApi;
    @DubboReference
    AgreementBusinessApi agreementBusinessApi;
    @DubboReference
    GoodsPriceApi goodsPriceApi;
    @DubboReference
    DeliveryAddressApi deliveryAddressApi;
    @DubboReference
    PayApi payApi;
    @DubboReference
    PresaleOrderApi presaleOrderApi;
    @DubboReference
    EmployeeApi employeeApi;
    @DubboReference
    UserApi userApi;
    @DubboReference
    CustomerApi customerApi;
    @DubboReference
    PresaleActivityApi presaleActivityApi;
    @DubboReference
    OrderDetailApi    orderDetailApi;
    @DubboReference
    SmsApi  smsApi;
    @DubboReference
    IPLocationApi ipLocationApi;
    @DubboReference
    AppLoginInfoApi appLoginInfoApi;
    @DubboReference
    OrderDeviceInfoApi orderDeviceInfoApi;
    @DubboReference
    MqMessageSendApi   mqMessageSendApi;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    RedisDistributedLock redisDistributedLock;
    @Autowired
    private SpringAsyncConfig springAsyncConfig;
    @Autowired
    private PreSaleDiscountProcessHandler preSaleDiscountProcessHandler;
    @Lazy
    @Autowired
    private PresaleOrderServiceImpl _this;

    /**
     * 校验商品状态
     */
    private void validateGoodsStatus(QuickBuyGoodsDTO quickBuyGoodsDTO, GoodsSkuInfoDTO goodsSkuFullDTO) {

        if (goodsSkuFullDTO == null) {
            throw new BusinessException(OrderErrorCode.SKU_NOT_EXIST);
        }
        if (GoodsStatusEnum.getByCode(goodsSkuFullDTO.getGoodsInfo().getGoodsStatus()) != GoodsStatusEnum.UP_SHELF) {
            throw new BusinessException(OrderErrorCode.SUBMIT_GOODS_OFF_SHELF);
        }
        if (!GoodsSkuStatusEnum.NORMAL.getCode().equals(goodsSkuFullDTO.getStatus())) {
            throw new BusinessException(OrderErrorCode.SUBMIT_GOODS_DISABLE);
        }
        // 校验是否审核通过
        if (!GoodsStatusEnum.AUDIT_PASS.getCode().equals(goodsSkuFullDTO.getGoodsInfo().getAuditStatus())){
            throw new BusinessException(OrderErrorCode.SUBMIT_GOODS_DISABLE);
        }
        // 校验包装规格
        if (quickBuyGoodsDTO.getQuantity() % goodsSkuFullDTO.getPackageNumber() != 0) {
            throw new BusinessException(OrderErrorCode.SKU_PACKAGE_NUMBER_EXIST);
        }
    }


    /**
     * 校验可控商品
     * @param buyerEid
     * @param gidList
     */
    private void checkB2BGoodsByGids(Long buyerEid,List<Long> gidList) {
        Map<Long, Integer> goodsListResult = agreementBusinessApi.getB2bGoodsLimitByGids(gidList,buyerEid);
        if (log.isDebugEnabled()) {
            log.debug("checkB2BGoodsByGids request->{},result->{}", JSON.toJSON(gidList), JSON.toJSON(goodsListResult));
        }
        // 控销商品无法结算
        if (MapUtil.isEmpty(goodsListResult)) {
            throw new BusinessException(OrderErrorCode.LIMIT_GOODS_SALE_ERROR);
        }
        // 是否管控商品
        if (goodsListResult.values().stream().anyMatch(value -> GoodsLimitStatusEnum.CONTROL_GOODS == GoodsLimitStatusEnum.getByCode(value))){
            throw new BusinessException(OrderErrorCode.LIMIT_GOODS_SALE_ERROR);
        }
        // 是否建立采购关系
        if (goodsListResult.values().stream().anyMatch(value -> GoodsLimitStatusEnum.NOT_RELATION_SHIP == GoodsLimitStatusEnum.getByCode(value))) {
            throw new BusinessException(OrderErrorCode.NOT_RELATION_SHIP_ERROR);
        }
    }


    /**
     * 查询省区经理信息
     * @param contacterId 联系人ID
     * @return
     */
    private EnterpriseEmployeeDTO selectProvinceManager(Long contacterId){
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
     * 拆单
     *
     * @param request
     * @return
     */
    private List<CreateOrderRequest> split(
            PresaleOrderSubmitRequest request,
            QuickBuyGoodsDTO quickBuyGoodsDTO,
            List<EnterpriseDTO> allEnterpriseDTOList,
            List<GoodsSkuInfoDTO> allGoodsDTOList,
            List<EnterpriseCustomerDTO> customerDTOList,
            List<PresaleActivityGoodsDTO> presaleActivityGoodsDTOList
    ) {

        OrderSubmitRequest orderSubmitRequest = PojoUtils.map(request,OrderSubmitRequest.class);

        OrderSubmitRequest.DistributorOrderDTO distributorOrderDTO = new OrderSubmitRequest.DistributorOrderDTO();
        distributorOrderDTO.setDistributorEid(quickBuyGoodsDTO.getDistributorEid());
        distributorOrderDTO.setPaymentType(request.getPaymentType());
        distributorOrderDTO.setPaymentMethod(request.getPaymentMethod());
        distributorOrderDTO.setBuyerMessage(request.getBuyerMessage());
        distributorOrderDTO.setCartIds(ListUtil.toList(quickBuyGoodsDTO.getGoodsSkuId()));
        distributorOrderDTO.setContractFileKeyList(ListUtil.empty());
        orderSubmitRequest.setDistributorOrderList(Collections.singletonList(distributorOrderDTO));


        // 模拟购物车,暂时将快速购买商品模拟购物车添加,方便后续多条商品同时参与预售时好扩展
        CartDO cartDO = new CartDO();
        cartDO.setId(quickBuyGoodsDTO.getGoodsSkuId());
        cartDO.setGoodsSkuId(quickBuyGoodsDTO.getGoodsSkuId());
        cartDO.setGoodsId(quickBuyGoodsDTO.getGoodsId());
        cartDO.setDistributorGoodsId(quickBuyGoodsDTO.getDistributorGoodsId());
        cartDO.setGoodsSkuId(quickBuyGoodsDTO.getGoodsSkuId());
        cartDO.setQuantity(quickBuyGoodsDTO.getQuantity());
        cartDO.setGoodSource(CartGoodsSourceEnum.B2B.getCode());
        cartDO.setSelectedFlag(1);
        cartDO.setPromotionActivityType(PromotionActivityTypeEnum.PRESALE.getCode());
        cartDO.setPromotionActivityId(quickBuyGoodsDTO.getPromotionActivityId());
        cartDO.setBuyerEid(request.getBuyerEid());
        cartDO.setSellerEid(request.getDistributorEid());
        cartDO.setDistributorEid(quickBuyGoodsDTO.getDistributorEid());


        // 1、拆单
        SplitOrderContextBO contextBO = SplitOrderContextBO.builder().request(orderSubmitRequest).allCartDOList(Collections.singletonList(cartDO)).build();
        contextBO.setAllEnterpriseList(allEnterpriseDTOList);
        contextBO.setCustomerDTOList(customerDTOList);
        contextBO.setAllGoodsDTOList(allGoodsDTOList);
        contextBO.setYilingSubEids(enterpriseApi.listSubEids(Constants.YILING_EID));
        contextBO.setIndustryDirectEids(Optional.ofNullable(enterpriseApi.listEidsByChannel(EnterpriseChannelEnum.INDUSTRY_DIRECT)).orElse(Collections.emptyList()));
        contextBO.setOrderBatchNo(noApi.gen(NoEnum.ORDER_BATCH_NO));
        contextBO.setContacterFunction((c) -> userApi.getById(c));
        contextBO.setDeliveryAddressDTO(deliveryAddressApi.getById(request.getAddressId()));
        contextBO.setProvinceManagerFunction((c) -> this.selectProvinceManager(c));
        contextBO.setOrderNoFunction((c) -> noApi.gen(c));
        // 设置价格函数
        contextBO.setPriceFunction((c) -> goodsPriceApi.queryB2bGoodsPriceInfoMap(c));
        contextBO.setPresaleActivityGoodsDTOList(presaleActivityGoodsDTOList);
        contextBO.setInitOrderFunction((c) -> this.initOrderFunction(c));

        List<CreateOrderRequest> createOrderRequestList = SplitOrderEnum.PRESALE.handlerInstance().splitOrder(contextBO).getCreateOrderRequestList();

        return createOrderRequestList;

    }


    /**
     * 在线支付，创建支付交易流水
     *
     * @param orderDTOList
     * @return
     */
    private String initPaymentOrder(List<PresaleOrderDTO> orderDTOList) {

        // 在线支付订单
        List<OrderDTO> onlineOrderList = orderDTOList
                .stream()
                .filter(e -> PaymentMethodEnum.ONLINE == PaymentMethodEnum.getByCode(e.getPaymentMethod().longValue())) // 在线订单
                .filter(e -> PaymentStatusEnum.getByCode(e.getPaymentStatus()) == PaymentStatusEnum.UNPAID // 未支付
                        || PaymentStatusEnum.getByCode(e.getPaymentStatus()) == PaymentStatusEnum.PARTPAID )  // 部分支付,标识为支付尾款订单
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


        PresaleOrderDTO presaleOrderDTO = orderDTOList.stream().findFirst().get();
        Map<Long, PresaleOrderDTO> presaleOrderDTOMap = orderDTOList.stream().collect(Collectors.toMap(PresaleOrderDTO::getOrderId, Function.identity()));

        // 预售活动类型,如果为定金预售，付定金即可，如果是全款预售，定金金额0,付尾款金额
        TradeTypeEnum tradeType = TradeTypeEnum.DEPOSIT;
        if (PreSaleActivityTypeEnum.FULL == PreSaleActivityTypeEnum.getByCode(presaleOrderDTO.getActivityType())) {
            tradeType = TradeTypeEnum.BALANCE;
        }

        CreatePayOrderRequest payOrderRequest = new CreatePayOrderRequest();
        List<CreatePayOrderRequest.appOrderRequest> appOrderList = onlineOrderList.stream().peek(orderDto -> {
            BigDecimal amount = orderDto.getPaymentAmount();
            // 如果是定金支活动,需要将定金金额设置为本次需要支付的金额
            if (PreSaleActivityTypeEnum.DEPOSIT == PreSaleActivityTypeEnum.getByCode(presaleOrderDTO.getActivityType())) {
                amount = Optional.ofNullable(presaleOrderDTOMap.get(orderDto.getId())).map(t -> t.getDepositAmount()).orElse(orderDto.getPaymentAmount());
            }
            orderDto.setPaymentAmount(amount);
        }).map(orderDto -> {
            CreatePayOrderRequest.appOrderRequest request = new CreatePayOrderRequest.appOrderRequest();
            request.setUserId(orderDto.getCreateUser());
            request.setAppOrderId(orderDto.getId());
            request.setAppOrderNo(orderDto.getOrderNo());
            request.setAmount(orderDto.getPaymentAmount());
            request.setBuyerEid(orderDto.getBuyerEid());
            request.setSellerEid(orderDto.getSellerEid());
            return request;
        }).collect(Collectors.toList());

        payOrderRequest.setTradeType(tradeType);
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
     * 计算订单预售优惠金额
     * @param createOrderRequestList
     * @param presaleActivityGoodsDTOList
     */
    private void calculateOrderPresaleDiscountAmount(Long buyerEid,Long opUserId,List<CreateOrderRequest> createOrderRequestList,List<PresaleActivityGoodsDTO> presaleActivityGoodsDTOList) {
        Map<String, PresaleActivityGoodsDTO> activityGoodsDTOMap = presaleActivityGoodsDTOList.stream().collect(Collectors.toMap(t ->  t.getId() + Constants.SEPARATOR_MIDDLELINE  + t.getGoodsId(), Function.identity()));

        // 预售优惠金额
        CalOrderDiscountContextBO<CreateOrderRequest> discountContextBO = new CalOrderDiscountContextBO();
        discountContextBO.setPlatformEnum(PlatformEnum.B2B);
        discountContextBO.setOrderTypeEnum(OrderTypeEnum.B2B);
        discountContextBO.setBuyerEid(buyerEid);
        discountContextBO.setOpUserId(opUserId);
        discountContextBO.setCreateOrderRequestList(createOrderRequestList);
        discountContextBO.setPreSaleActivityGoodsDTOMap(activityGoodsDTOMap);

        DiscountHandlerChain handlerChain = new DiscountHandlerChain(discountContextBO);
        handlerChain.addHandler(preSaleDiscountProcessHandler).processDiscount();

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
    }


    /**
     * 创建预售订单
     * @param request
     * @param quickBuyGoodsDTO
     * @return
     */
    @GlobalTransactional
    public OrderSubmitBO createOrder(
            PresaleOrderSubmitRequest request,
            QuickBuyGoodsDTO quickBuyGoodsDTO,
            GoodsSkuInfoDTO goodsSkuFullDTO,
            List<EnterpriseDTO> allEnterpriseDTOList,
            List<EnterpriseCustomerDTO> customerDTOList
    ) {


        /**预售订单活动信息*/
        List<PresaleActivityGoodsDTO> presaleActivityGoodsDTOList = this.selectGoodsPresaleActivityInfo(request.getBuyerEid(), Collections.singletonList(quickBuyGoodsDTO.getDistributorGoodsId()), quickBuyGoodsDTO.getPromotionActivityId());

        // 1、拆单
        List<CreateOrderRequest> createOrderRequestList = this.split(request,quickBuyGoodsDTO,allEnterpriseDTOList,Collections.singletonList(goodsSkuFullDTO),customerDTOList,presaleActivityGoodsDTOList);

        //2, 设置预售活动信息以及优惠金额
        this.calculateOrderPresaleDiscountAmount(request.getBuyerEid(),request.getOpUserId(),createOrderRequestList,presaleActivityGoodsDTOList);

        //3、保存订单数据
        orderApi.create(createOrderRequestList);

        // 4、获取订单信息
        List<String> orderNos = createOrderRequestList.stream().map(CreateOrderRequest::getOrderNo).collect(Collectors.toList());
        List<PresaleOrderDTO> orderDTOList = presaleOrderApi.listByOrderNos(orderNos);
        // 5,如果存在在线支付订单，创建在线支付交易
        String payId = this.initPaymentOrder(orderDTOList);

        String userSessionId = "order:presale:" + request.getBuyerEid();

        // 6、清理缓存会话数据
        stringRedisTemplate.delete(userSessionId);

        //7 扣减预售活动信息数据
        this.presaleOrderCallback(createOrderRequestList,orderDTOList);

        //8 设置订单消息
        List<MqMessageBO> mqMessageBOList = this.sendMessages(createOrderRequestList);

        OrderSubmitBO submitBo = new OrderSubmitBO();
        submitBo.setPayId(payId);
        submitBo.setOrderDTOList(PojoUtils.map(orderDTOList,OrderDTO.class));
        submitBo.setMqMessageBOList(mqMessageBOList);

        return submitBo;
    }

     /**
     *  预售活动回调接口
     * @param createOrderRequestList
     * @param orderDTOList
     * @return
     */
    private boolean presaleOrderCallback(List<CreateOrderRequest> createOrderRequestList, List<PresaleOrderDTO> orderDTOList) {

        Map<String, PresaleOrderDTO> orderDTOMap = orderDTOList.stream().collect(Collectors.toMap(PresaleOrderDTO::getOrderNo, Function.identity()));
        List<PromotionBuyRecord> allBuyRecordList = Lists.newArrayList();
        for (CreateOrderRequest createOrderRequest : createOrderRequestList) {

            PresaleOrderDTO orderDTO = orderDTOMap.get(createOrderRequest.getOrderNo());
            List<PromotionBuyRecord> buyRecordList =
            createOrderRequest.getOrderDetailList().stream()
                    .filter(t -> PromotionActivityTypeEnum.PRESALE == PromotionActivityTypeEnum.getByCode(t.getPromotionActivityType()))
                    .map(t -> {
                        PromotionBuyRecord buyRecord = new PromotionBuyRecord();
                        buyRecord.setOrderId(orderDTO.getId());
                        buyRecord.setEid(orderDTO.getBuyerEid());
                        buyRecord.setGoodsQuantity(t.getGoodsQuantity());
                        buyRecord.setGoodsId(t.getDistributorGoodsId());
                        buyRecord.setPromotionActivityId(t.getPromotionActivityId());
                        return        buyRecord;
                    }).collect(Collectors.toList());
            allBuyRecordList.addAll(buyRecordList);
        }

        PromotionSaveBuyRecordRequest promotionSaveBuyRecordRequest = new PromotionSaveBuyRecordRequest();
        promotionSaveBuyRecordRequest.setBuyRecordList(allBuyRecordList);
        promotionSaveBuyRecordRequest.setOpUserId(createOrderRequestList.stream().findFirst().get().getOpUserId());
        promotionSaveBuyRecordRequest.setOpTime(new Date());

        return  presaleActivityApi.presaleOrderCallback(promotionSaveBuyRecordRequest);
    }


    /**
     * 构建快速购买订单数据
     * @param userData
     * @return
     */
    private QuickBuyGoodsDTO buildQuickBuyGood(String userData) {

        userData = userData.substring(1, userData.length() - 1);
        //去除转义符
        userData = userData.replaceAll("\\\\", "");
        // 获取预售订单信息
        QuickBuyGoodsDTO quickBuyGoodsDTO = JSONUtil.toBean(userData, QuickBuyGoodsDTO.class);

        return quickBuyGoodsDTO;
    }

    /**
     * 查询商品预售活动信息
     * @param buyerEid 买家Id
     * @param distributorGoodsIds 商品Id
     * @param presaleActivityId 预售活动Id
     * @return
     */
    private List<PresaleActivityGoodsDTO> selectGoodsPresaleActivityInfo(Long buyerEid, List<Long> distributorGoodsIds, Long presaleActivityId) {

        QueryPresaleInfoRequest queryPresaleInfoRequest = new QueryPresaleInfoRequest();
        queryPresaleInfoRequest.setPlatformSelected(CouponPlatformTypeEnum.B2B.getCode());
        queryPresaleInfoRequest.setBuyEid(buyerEid);
        queryPresaleInfoRequest.setPresaleId(presaleActivityId);
        queryPresaleInfoRequest.setGoodsId(distributorGoodsIds);

        List<PresaleActivityGoodsDTO> presaleActivityGoodsDTOList = presaleActivityApi.getPresaleInfoByGoodsIdAndBuyEid(queryPresaleInfoRequest);

        if (log.isDebugEnabled()) {

            log.debug("..调用营销接口:getPresaleInfoByGoodsIdAndBuyEid...入参:{},返回参数:{}",queryPresaleInfoRequest,presaleActivityGoodsDTOList);
        }

        if (CollectionUtil.isEmpty(presaleActivityGoodsDTOList)) {

            throw new BusinessException(OrderErrorCode.SUBMIT_PRESALE_ORDER_ERROR);
        }

        return presaleActivityGoodsDTOList;

    }


    @Override
    public OrderSubmitBO preSaleOrderSubmit(PresaleOrderSubmitRequest request) {

        String userSessionId = "order:presale:" + request.getBuyerEid();

        String userData = stringRedisTemplate.opsForValue().get(userSessionId);
        if (StringUtils.isBlank(userData)) {
            throw new BusinessException(OrderErrorCode.SUBMIT_PRESALE_ORDER_ERROR);
        }

        /**预售订单目前只支持在线支付**/
        if (PaymentMethodEnum.ONLINE != PaymentMethodEnum.getByCode(request.getPaymentMethod().longValue())) {
            throw new BusinessException(OrderErrorCode.SUBMIT_PRESALE_ORDER_ERROR);
        }

        QuickBuyGoodsDTO quickBuyGoodsDTO = this.buildQuickBuyGood(userData);
        // 获取预售商品信息
        GoodsSkuInfoDTO goodsSkuFullDTO = goodsApi.getGoodsSkuInfoById(quickBuyGoodsDTO.getGoodsSkuId());
        if (goodsSkuFullDTO == null) {

            throw new RuntimeException("商品信息不全,未查询到相关商品信息!");
        }
        // 转换最新商品Id
        quickBuyGoodsDTO.setDistributorGoodsId(goodsSkuFullDTO.getGoodsId());
        /*校验商品状态信息*/
        this.validateGoodsStatus(quickBuyGoodsDTO,goodsSkuFullDTO);
        /*校验管控商品信息*/
        this.checkB2BGoodsByGids(request.getBuyerEid(), Collections.singletonList(quickBuyGoodsDTO.getDistributorGoodsId()));

        List<Long> allEids = Stream.of(request.getBuyerEid(), quickBuyGoodsDTO.getDistributorEid()).distinct().collect(Collectors.toList());
        List<EnterpriseDTO> allEnterpriseDTOList = enterpriseApi.listByIds(allEids);
        // 客户信息字典
        List<EnterpriseCustomerDTO> customerDTOList = customerApi.listByEidsAndCustomerEid(Collections.singletonList(quickBuyGoodsDTO.getDistributorEid()), request.getBuyerEid());
        String lockName = RedisKey.generate("order", quickBuyGoodsDTO.getPlatformType().toString(),"presale",request.getBuyerEid().toString(),quickBuyGoodsDTO.getPromotionActivityId().toString(),quickBuyGoodsDTO.getDistributorGoodsId().toString());
        String lockId = "";
        OrderSubmitBO submitBo;

        // 针对每个用户的每个品加锁防止出现,单个品的超卖
        try {
            lockId = redisDistributedLock.lock2(lockName, 60, 60, TimeUnit.SECONDS);
            submitBo = _this.createOrder(request,quickBuyGoodsDTO,goodsSkuFullDTO,allEnterpriseDTOList,customerDTOList);
        } finally {
            redisDistributedLock.releaseLock(lockName,lockId);
        }

        // 发送订单创建消息
        submitBo.getMqMessageBOList().forEach(mqMessageBO -> {
            mqMessageSendApi.send(mqMessageBO);
        });

        String batchNo = submitBo.getOrderDTOList().stream().findFirst().get().getBatchNo();

        // 异步创建用户下单设备信息信息
        CompletableFuture.runAsync(() -> createOrderTerminalInfo(request.getOpUserId(),request.getIpAddress(),request.getUserAgent(),batchNo), springAsyncConfig.getAsyncExecutor());

        return submitBo;
    }


    /**
     * 创建下单用户的ip地址信息
     * @param ip
     * @param batchNo
     */
    @SneakyThrows
    private boolean createOrderTerminalInfo(Long userId,String ip,String userAgent,String batchNo) {

        CreateOrderDeviceInfoRequest createOrderDeviceInfoRequest = new CreateOrderDeviceInfoRequest();
        createOrderDeviceInfoRequest.setIp(ip);
        createOrderDeviceInfoRequest.setBatchNo(batchNo);
        createOrderDeviceInfoRequest.setUserAgent(userAgent);

        // 查询ip归属地区
        IPLocationBO query = ipLocationApi.query(ip);

        if (log.isDebugEnabled()) {

            log.debug("调用ip查询归属地接口-入参:{},出参:{}",ip,query.toString());
        }

        createOrderDeviceInfoRequest.setIpProvinceName(query.getProvinceName());
        createOrderDeviceInfoRequest.setIpRegionName(query.getRegionName());
        createOrderDeviceInfoRequest.setIpCityName(query.getCityName());
        createOrderDeviceInfoRequest.setIpLocation(query.getLocation());
        createOrderDeviceInfoRequest.setIpOperatorName(query.getOperatorName());


        Long appId = AppEnum.B2B_APP.getAppId().longValue();
        createOrderDeviceInfoRequest.setMobileFlag(1);


        // 查询用户设备登录信息
        AppLoginInfoDTO appLoginInfoDTO =  appLoginInfoApi.getLatestLoginInfoByUserId(appId,userId);

        if (log.isDebugEnabled()) {

            log.debug("查询用户最后登录信息-入参:{},出参:{}",userId,appLoginInfoDTO);
        }

        if (appLoginInfoDTO != null) {
            createOrderDeviceInfoRequest.setTerminalType(appLoginInfoDTO.getTerminalType());
            createOrderDeviceInfoRequest.setManufacturer(appLoginInfoDTO.getManufacturer());
            createOrderDeviceInfoRequest.setBrand(appLoginInfoDTO.getBrand());
            createOrderDeviceInfoRequest.setModel(appLoginInfoDTO.getModel());
            createOrderDeviceInfoRequest.setOsVersion(appLoginInfoDTO.getOsVersion());
            createOrderDeviceInfoRequest.setSdkVersion(appLoginInfoDTO.getSdkVersion());
            createOrderDeviceInfoRequest.setScreenSize(appLoginInfoDTO.getScreenSize());
            createOrderDeviceInfoRequest.setResolution(appLoginInfoDTO.getResolution());
            createOrderDeviceInfoRequest.setUdid(appLoginInfoDTO.getUdid());
            createOrderDeviceInfoRequest.setAppVersion(appLoginInfoDTO.getAppVersion());
            createOrderDeviceInfoRequest.setChannelCode(appLoginInfoDTO.getChannelCode());
        }

        return orderDeviceInfoApi.save(createOrderDeviceInfoRequest);
    }


    /**
     * 发送订单消息
     * @param orderDTOList
     * @return
     */
    private List<MqMessageBO> sendMessages( List<CreateOrderRequest> orderDTOList) {

        // 订单创建消息
        List<MqMessageBO> mqMessageBOList = orderDTOList.stream().map(t -> {
            MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_ORDER_CREATED, "", t.getOrderNo());
            mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
            return mqMessageBO;
        }).collect(Collectors.toList());

        // 在线支付订单(支付定金的需要提醒,如果是支付尾款的无需提醒)
        List<CreateOrderRequest> onlineOrderList = orderDTOList
                .stream()
                .filter(e -> OrderCategoryEnum.PRESALE == OrderCategoryEnum.getByCode(e.getOrderCategory())) // 预售活动
                .filter(e -> PaymentMethodEnum.ONLINE == PaymentMethodEnum.getByCode(e.getPaymentMethod().longValue())) // 在线订单
                .collect(Collectors.toList());

        if (CollectionUtil.isEmpty(onlineOrderList)) {

            return mqMessageBOList;
        }

        // 设置在线支付订单,自动取消前5分钟给短信提醒
        // 自动取消短信提醒
        onlineOrderList.stream().forEach(t -> {
            MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_ORDER_CANCEL_SMS_NOTIFY, "", t.getOrderNo(),MqDelayLevel.TEN_MINUTES);
            mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
            mqMessageBOList.add(mqMessageBO);
        });

        return mqMessageBOList;
    }

    @Override
    public boolean sendPresaleOrderSmsNotice(String orderNo, PreSalOrderReminderTypeEnum preSalOrderReminderTypeEnum) {

        OrderDTO orderDTO = orderApi.selectByOrderNo(orderNo);
        if (orderDTO == null) {
            log.warn("订单不存在");
            return false;
        }

        if (OrderCategoryEnum.PRESALE != OrderCategoryEnum.getByCode(orderDTO.getOrderCategory())) {
            log.error("非预售订单");
            return false;
        }

        if (OrderStatusEnum.CANCELED == OrderStatusEnum.getByCode(orderDTO.getOrderStatus())) {

            UpdatePresaleOrderRequest request = new UpdatePresaleOrderRequest();
            request.setHasSendCancelSms(1);
            request.setHasSendPaySms(1);
            request.setOrderId(orderDTO.getId());
            presaleOrderApi.updatePresalOrderByOrderId(request);

            return true;
        }

        PresaleOrderDTO presaleOrderDTO =  presaleOrderApi.getOrderInfo(orderDTO.getId());
        if (presaleOrderDTO == null ||  StringUtils.isBlank(presaleOrderDTO.getOrderNo())) {
            log.error("预售订单扩展信息未查询到");
            return false;
        }

        if (preSalOrderReminderTypeEnum.CANCEL_REMINDER.equals(preSalOrderReminderTypeEnum)) {
            if (presaleOrderDTO.getIsPayDeposit() == 1) {

                log.info("订单已支付定金");
                return true;
            }

        } else  if (preSalOrderReminderTypeEnum.BALANCE_PAY_REMINDER.equals(preSalOrderReminderTypeEnum)) {
            if (presaleOrderDTO.getIsPayBalance() == 1) {
                UpdatePresaleOrderRequest request = new UpdatePresaleOrderRequest();
                request.setHasSendPaySms(1);
                request.setOrderId(orderDTO.getId());
                presaleOrderApi.updatePresalOrderByOrderId(request);
                log.info("订单已支付尾款");
                return true;
            }
        }
        UserDTO userDTO = userApi.getById(orderDTO.getCreateUser());
        List<OrderDetailDTO> orderDetailDTOList = orderDetailApi.getOrderDetailInfo(orderDTO.getId());

        if (userDTO == null || CollectionUtil.isEmpty(orderDetailDTOList)) {
            log.info("订单数据或者用户数据异常");
            return false;
        }

        String content = StrFormatter.format(preSalOrderReminderTypeEnum.getTemplateContent(),orderDetailDTOList.stream().findFirst().get().getGoodsName());
        // 发送订单将取消短信
        boolean result = smsApi.send(userDTO.getMobile(), content, SmsTypeEnum.EXPIRATION_REMINDER);
        log.info("预售订单{}提醒【订单取消】,发送短信完成，短信内容：{}，发送结果：{}" ,orderNo, content , result);

        if (PreSalOrderReminderTypeEnum.BALANCE_PAY_REMINDER == preSalOrderReminderTypeEnum) {
            UpdatePresaleOrderRequest request = new UpdatePresaleOrderRequest();
            request.setHasSendPaySms(1);
            request.setOrderId(orderDTO.getId());
            presaleOrderApi.updatePresalOrderByOrderId(request);
        }

        if (PreSalOrderReminderTypeEnum.BALANCE_CANCEL_REMINDER == preSalOrderReminderTypeEnum) {
            UpdatePresaleOrderRequest request = new UpdatePresaleOrderRequest();
            request.setHasSendCancelSms(1);
            request.setOrderId(orderDTO.getId());
            presaleOrderApi.updatePresalOrderByOrderId(request);
        }

        return result;
    }
}
