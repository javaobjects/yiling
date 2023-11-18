package com.yiling.mall.order.service.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.framework.common.util.Constants;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsSkuInfoDTO;
import com.yiling.mall.cart.entity.CartDO;
import com.yiling.mall.cart.service.CartService;
import com.yiling.mall.order.bo.OrderSubmitBO;
import com.yiling.mall.order.bo.SplitOrderContextBO;
import com.yiling.mall.order.bo.SplitOrderEnum;
import com.yiling.mall.order.dto.request.OrderSubmitRequest;
import com.yiling.mall.order.event.CreateOrderEvent;
import com.yiling.mall.order.service.OrderSubmitService;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.request.CreateOrderRequest;
import com.yiling.order.order.enums.CustomerConfirmEnum;
import com.yiling.order.order.enums.NoEnum;
import com.yiling.order.order.enums.OrderAuditStatusEnum;
import com.yiling.order.order.enums.OrderSourceEnum;
import com.yiling.order.order.enums.OrderStatusEnum;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.order.order.enums.PaymentStatusEnum;
import com.yiling.sales.assistant.message.api.MessageApi;
import com.yiling.sales.assistant.message.dto.request.OrderListMessageBuildRequest;
import com.yiling.sales.assistant.message.dto.request.OrderMessageRequest;
import com.yiling.sales.assistant.message.enums.MessageNodeEnum;
import com.yiling.sales.assistant.message.enums.MessageRoleEnum;
import com.yiling.sales.assistant.message.enums.SourceEnum;
import com.yiling.user.enterprise.api.CustomerApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseCustomerDTO;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.enums.EnterpriseChannelEnum;

import cn.hutool.core.collection.ListUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

/** 销售助手应用下单
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.mall.order.service.impl
 * @date: 2021/12/20
 */
@Service("SaOrderSubmitService")
@Slf4j
public class SaOrderSubmitServiceImpl extends OrderSubmitServiceImpl implements OrderSubmitService {
    /**
     * 校验订单提交参数函数
     */
    private final Map<OrderTypeEnum,validateParamFunction> validateOrderFunction = Maps.newHashMapWithExpectedSize(2);
    @Autowired
    CartService             cartService;
    @DubboReference
    EnterpriseApi           enterpriseApi;
    @DubboReference
    GoodsApi                goodsApi;
    @DubboReference
    OrderApi                orderApi;
    @DubboReference
    CustomerApi             customerApi;
    @DubboReference
    MessageApi              messageApi;
    @Lazy
    @Autowired
    private SaOrderSubmitServiceImpl _this;

    /**
     * 销售助手订单
     * @param orderTypeEnum 订单类型
     * @param orderSourceEnum 订单来源
     * @return
     */
    @Override
    public boolean matchOrder(OrderTypeEnum orderTypeEnum, OrderSourceEnum orderSourceEnum) {
        if (OrderSourceEnum.SA == orderSourceEnum) {
            return true;
        }
        return false;
    }

    @PostConstruct
    public void init() {

        validateOrderFunction.put(OrderTypeEnum.POP,this::validatePopOrder);
        validateOrderFunction.put(OrderTypeEnum.B2B,this::validateB2bOrder);
    }

    /**
     * 校验订单信息基本函数
     */
    @FunctionalInterface
    private interface validateParamFunction {

        void apply(OrderSubmitRequest request ,List<CartDO> cartDOList,List<GoodsSkuInfoDTO> goodsSkuInfoDTOList);
    }

    /**
     * 校验pop订单
     * @param request 订单提交参数
     * @param allCartDOList 购物车信息
     * @param allGoodsDTOList 商品sku基本信息
     */
    private void validatePopOrder(OrderSubmitRequest request,List<CartDO> allCartDOList,List<GoodsSkuInfoDTO> allGoodsDTOList) {

        // 校验商品状态
        this.validateGoodsStatus(allCartDOList, allGoodsDTOList.stream().collect(Collectors.toMap(GoodsSkuInfoDTO::getId, Function.identity())));
        // 校验商品库存
        this.validateGoodsInventory(allCartDOList);
        // 校验采购关系
        this.validatePurchaseRelation(allCartDOList, request.getBuyerEid());
        // 校验采购权限
        this.validatePurchaseAuthority(allCartDOList, request.getBuyerEid());
    }


    /**
     * 校验b2b订单信息
     * @param request 订单提交参数
     * @param allCartDOList 购物车信息
     * @param allGoodsDTOList 商品sku基本信息
     */
    private void validateB2bOrder(OrderSubmitRequest request,List<CartDO> allCartDOList,List<GoodsSkuInfoDTO> allGoodsDTOList) {

        // 校验商品状态
        this.validateGoodsStatus(allCartDOList, allGoodsDTOList.stream().collect(Collectors.toMap(GoodsSkuInfoDTO::getId, Function.identity())));
        // 校验商品库存
        this.validateGoodsInventory(allCartDOList);
        // 所有配送商商品ID集合
        List<Long> allDistributorGoodsIds = allCartDOList.stream().map(CartDO::getDistributorGoodsId).distinct().collect(Collectors.toList());
        // 校验B2B商品信息
        this.checkB2BGoodsByGids(request.getBuyerEid(),allDistributorGoodsIds);
    }

    /**
     * 构建下单所有需要参数
     * @param request  提交参数
     * @return 创建订单参数
     */
    private List<CreateOrderRequest> buildCreateOrderRequestList(OrderSubmitRequest request) {

        // 所有购物车信息
        List<Long> allCartIds = request.getDistributorOrderList().stream().map(OrderSubmitRequest.DistributorOrderDTO::getCartIds).flatMap(Collection::stream).collect(Collectors.toList());
        List<CartDO> allCartDOList = cartService.listByIds(allCartIds);
        // 校验购物车
        this.validateCart(allCartDOList, allCartIds);
        // 以岭子企业EId
        List<Long> yilingSubEids = enterpriseApi.listSubEids(Constants.YILING_EID);
        // 所有SKU商品ID集合
        List<Long> allGoodsSkuIds = allCartDOList.stream().map(CartDO::getGoodsSkuId).distinct().collect(Collectors.toList());
        // 所有商品信息集合
        List<GoodsSkuInfoDTO> allGoodsDTOList = goodsApi.batchQueryInfoBySkuIds(allGoodsSkuIds);
        Map<Long, GoodsSkuInfoDTO> allGoodsDTOMap = allGoodsDTOList.stream().collect(Collectors.toMap(GoodsSkuInfoDTO::getId, Function.identity()));
        // 获取最新的商品Id
        this.setLastMergeGoodId(allCartDOList,allGoodsDTOMap,yilingSubEids);
        // 验证订单提交商品信息
        validateOrderFunction.get(request.getOrderTypeEnum()).apply(request,allCartDOList,allGoodsDTOList);

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
        // POP订单有商务联系人信息
        contextBO.setContacterFunction((c) -> OrderTypeEnum.B2B == request.getOrderTypeEnum() ? null : userApi.getById(c));
        contextBO.setDeliveryAddressDTO(deliveryAddressApi.getById(request.getAddressId()));
        contextBO.setProvinceManagerFunction((c) -> this.selectProvinceManager(c));
        contextBO.setOrderNoFunction((c) -> noApi.gen(c));
        // 设置状态函数
        contextBO.setInitOrderFunction((c) -> initOrderFunction(c));
        // 设置价格函数
        contextBO.setPriceFunction((c) -> OrderTypeEnum.B2B == request.getOrderTypeEnum() ? goodsPriceApi.queryB2bGoodsPriceInfoMap(c) : super.getPopGoodsPrice(c));

        return SplitOrderEnum.NORMAL.handlerInstance().splitOrder(contextBO).getCreateOrderRequestList();
    }


    /**
     * 订单提交
     * @param request 创建订单基本参数
     * @return 返回创建订单基本信息
     */
    @Override
    public OrderSubmitBO submit(OrderSubmitRequest request) {

        // 校验订单提交信息
        List<CreateOrderRequest> createOrderRequestList = this.buildCreateOrderRequestList(request);
        //冻结库存
        this.lockInventory(createOrderRequestList);
        // 创建订单
        OrderSubmitBO orderSubmitBO = null;
        try {
            orderSubmitBO =  _this.createOrder(request,createOrderRequestList);
        }catch (Exception e){
            log.warn("创建订单失败，开始补偿库存");
            //退还冻结库存
            this.backInventory(createOrderRequestList);
            throw e;
        }

        return orderSubmitBO;
    }


    /**
     * 创建订单
     * @param request 下单参数
     * @param createOrderRequestList 拆单结果
     * @return
     */
    @GlobalTransactional
    public OrderSubmitBO createOrder(OrderSubmitRequest request, List<CreateOrderRequest> createOrderRequestList) {

        // 所有购物车信息
        List<Long> allCartIds = request.getDistributorOrderList().stream().map(OrderSubmitRequest.DistributorOrderDTO::getCartIds).flatMap(Collection::stream).collect(Collectors.toList());

        // 1、保存订单数据
        orderApi.create(createOrderRequestList);

        // 2、获取订单信息
        List<String> orderNos = createOrderRequestList.stream().map(CreateOrderRequest::getOrderNo).collect(Collectors.toList());
        List<OrderDTO> orderDTOList = orderApi.listByOrderNos(orderNos);

        // 3、清理购物车
        cartService.removeByIds(allCartIds);

        // 4，创建任务消息
        buildMessage(request.getOpUserId(), request.getBuyerEid(), orderDTOList);

        // 5,订单发送消息集合
        List<MqMessageBO> mqMessageBOList = this.sendOrderCreatedMessage(orderDTOList);

        /************冻结库存放到最后面,防止出现订单并发,全局事务锁冲突问题************/
        // 6、冻结库存
        //冻结库存改用补偿机制，放在分布式事务外
        //this.lockInventory(createOrderRequestList);


        OrderSubmitBO submitBo = new OrderSubmitBO();
        submitBo.setPayId("");
        submitBo.setOrderDTOList(orderDTOList);
        submitBo.setMqMessageBOList(mqMessageBOList);

        /**
         * 创建成功，扣减优惠逻辑
         */
        CreateOrderEvent createOrderEvent = new CreateOrderEvent(this,orderDTOList,false,false);
        createOrderEvent.setOrderSourceEnum(request.getOrderSourceEnum());
        createOrderEvent.setUserId(request.getOpUserId());
        createOrderEvent.setUserAgent(request.getUserAgent());
        createOrderEvent.setIp(request.getIpAddress());
        createOrderEvent.setSaveOrderDevice(true);
        createOrderEvent.setOrderSourceEnum(request.getOrderSourceEnum());
        createOrderEvent.setOrderTypeEnum(request.getOrderTypeEnum());

        this.applicationEventPublisher.publishEvent(createOrderEvent);

        return submitBo;
    }


    /**
     * 创建提交订单消息
     *
     * @param userId    用户id
     * @param eid   企业id
     * @param orderDTOList  订单信息
     */
    private void buildMessage(Long userId, Long eid, List<OrderDTO> orderDTOList) {

        List<OrderMessageRequest> orderMessageRequestList = orderDTOList.stream().map(orderDTO -> new OrderMessageRequest().setOrderNo(orderDTO.getOrderNo()).setCode(orderDTO.getOrderNo()).setCreateTime(orderDTO.getCreateTime())).collect(Collectors.toList());

        OrderListMessageBuildRequest request = new OrderListMessageBuildRequest();
        request.setUserId(userId);
        request.setEid(eid);
        request.setSourceEnum(SourceEnum.SA);
        request.setMessageRoleEnum(MessageRoleEnum.TO_USER);
        request.setMessageNodeEnum(MessageNodeEnum.WAITING_PAYMENT);
        request.setOrderMessageRequestList(orderMessageRequestList);

        messageApi.buildOrderListMessage(request);
    }


    /**
     * 订单初始化函数
     * @param createOrderRequest
     */
    private void initOrderFunction(CreateOrderRequest createOrderRequest) {

        createOrderRequest.setPaymentStatus(PaymentStatusEnum.UNPAID.getCode());
        createOrderRequest.setOrderStatus(OrderStatusEnum.UNAUDITED.getCode());
        createOrderRequest.setCustomerConfirmTime(new Date());
        createOrderRequest.setCouponDiscountAmount(BigDecimal.ZERO);
        createOrderRequest.setPlatformCouponDiscountAmount(BigDecimal.ZERO);
        createOrderRequest.setCashDiscountAmount(BigDecimal.ZERO);
        createOrderRequest.setPresaleDiscountAmount(BigDecimal.ZERO);
        createOrderRequest.setPlatformPaymentDiscountAmount(BigDecimal.ZERO);
        createOrderRequest.setShopPaymentDiscountAmount(BigDecimal.ZERO);

        switch (OrderTypeEnum.getByCode(createOrderRequest.getOrderType())) {
            case B2B:
                // 销售助手b2b订单默认审核通过,需要转发
                createOrderRequest.setAuditStatus(OrderAuditStatusEnum.AUDIT_PASS.getCode());
                createOrderRequest.setCustomerConfirmStatus(CustomerConfirmEnum.TRANSFER.getCode());
                break;
            case POP:
                // 销售助手Pop订单下单默认待提交,并且不需要转发,只需要确认下即可
                createOrderRequest.setAuditStatus(OrderAuditStatusEnum.UNSUBMIT.getCode());
                createOrderRequest.setCustomerConfirmStatus(CustomerConfirmEnum.CONFIRMED.getCode());
                break;
        }
    }

}
