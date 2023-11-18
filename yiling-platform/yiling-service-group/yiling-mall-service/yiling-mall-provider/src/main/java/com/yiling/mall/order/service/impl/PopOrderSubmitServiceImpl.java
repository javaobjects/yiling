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

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsSkuInfoDTO;
import com.yiling.mall.cart.entity.CartDO;
import com.yiling.mall.cart.service.CartService;
import com.yiling.mall.order.bo.CalOrderDiscountContextBO;
import com.yiling.mall.order.bo.OrderSubmitBO;
import com.yiling.mall.order.bo.SplitOrderContextBO;
import com.yiling.mall.order.bo.SplitOrderEnum;
import com.yiling.mall.order.dto.request.OrderSubmitRequest;
import com.yiling.mall.order.event.CreateOrderEvent;
import com.yiling.mall.order.handler.CashDiscountProcessHandler;
import com.yiling.mall.order.handler.DiscountHandlerChain;
import com.yiling.mall.order.service.OrderSubmitService;
import com.yiling.order.order.api.NoApi;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.dto.OrderDTO;
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
import com.yiling.order.payment.enums.PaymentErrorCode;
import com.yiling.user.enterprise.api.CustomerApi;
import com.yiling.user.enterprise.api.DeliveryAddressApi;
import com.yiling.user.enterprise.api.DepartmentApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.api.PaymentMethodApi;
import com.yiling.user.enterprise.dto.EnterpriseCustomerDTO;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.EnterpriseDepartmentDTO;
import com.yiling.user.enterprise.enums.EnterpriseChannelEnum;
import com.yiling.user.payment.api.PaymentDaysAccountApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.dto.PaymentMethodDTO;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * POP订单服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-25
 */
@Service("popOrderSubmitService")
@Slf4j
public class PopOrderSubmitServiceImpl extends OrderSubmitServiceImpl implements OrderSubmitService {
    @Autowired
    CartService                   cartService;
    @DubboReference
    NoApi                         noApi;
    @DubboReference
    EnterpriseApi                 enterpriseApi;
    @DubboReference
    GoodsApi                      goodsApi;
    @DubboReference
    UserApi                       userApi;
    @DubboReference
    OrderApi                      orderApi;
    @DubboReference
    DeliveryAddressApi            deliveryAddressApi;
    @DubboReference
    CustomerApi                   customerApi;
    @DubboReference
    DepartmentApi                 departmentApi;
    @DubboReference
    PaymentDaysAccountApi         paymentDaysAccountApi;
    @DubboReference
    PaymentMethodApi              paymentMethodApi;
    @Autowired
    CashDiscountProcessHandler    cashDiscountProcessHandler;
    @Lazy
    @Autowired
    private PopOrderSubmitServiceImpl   _this;

    /**
     * 非销售助手pop订单
     * @param orderTypeEnum 订单类型
     * @param orderSourceEnum 订单来源
     * @return
     */
    @Override
    public boolean matchOrder(OrderTypeEnum orderTypeEnum, OrderSourceEnum orderSourceEnum) {
        if (OrderTypeEnum.POP == orderTypeEnum  && OrderSourceEnum.SA != orderSourceEnum) {

            return true;
        }
        return false;
    }


    /**
     * 校验商务联系人部门选择是否正确
     * @param contacterId 商务联系人ID
     * @param departmentId 企业部门ID
     */
    private void checkContacterDepartmentId(Long contacterId,Long departmentId) {
        // 没有选择部门无需校验
        if (contacterId == null || contacterId == 0) {

            return;
        }

        if (departmentId == null || departmentId == 0) {
            return;
        }

        Map<Long, List<EnterpriseDepartmentDTO>> departmentByEidUserMap = departmentApi.getDepartmentByEidUser(Constants.YILING_EID, ListUtil.toList(contacterId));
        List<EnterpriseDepartmentDTO> enterpriseDepartmentDTOS = departmentByEidUserMap.get(contacterId);

        if (CollectionUtil.isEmpty(enterpriseDepartmentDTOS)) {

            throw new BusinessException(OrderErrorCode.SUBMIT_DEPARTMENT_ERROR);
        }

        if (enterpriseDepartmentDTOS.stream().anyMatch(t -> t.getId().equals(departmentId))) {

            return;
        }

        throw new BusinessException(OrderErrorCode.SUBMIT_DEPARTMENT_ERROR);
    }


    /**
     * 校验订单是否有符合的支付方式
     * @param request
     */
    private void checkOrderPaymentMethod(OrderSubmitRequest request) {

        List<Long> sellerEids = request.getDistributorOrderList().stream().map(t -> t.getDistributorEid()).collect(Collectors.toList());

        Map<Long, List<PaymentMethodDTO>> paymentMethodDTOMap = paymentMethodApi.listByCustomerEidAndEids(request.getBuyerEid(), sellerEids, PlatformEnum.POP);

        if (MapUtil.isEmpty(paymentMethodDTOMap)) {

            throw new BusinessException(PaymentErrorCode.NO_PAYMENT_METHOD);
        }

        for (OrderSubmitRequest.DistributorOrderDTO distributorOrderDTO : request.getDistributorOrderList()) {

            List<PaymentMethodDTO> paymentMethodDTOList = paymentMethodDTOMap.get(distributorOrderDTO.getDistributorEid());

            if (CollectionUtil.isEmpty(paymentMethodDTOList)) {
                throw new BusinessException(PaymentErrorCode.NO_PAYMENT_METHOD);
            }

            List<Long> paymentMethodIds = paymentMethodDTOList.stream().map(PaymentMethodDTO::getCode).collect(Collectors.toList());

            if (!paymentMethodIds.contains(distributorOrderDTO.getPaymentMethod().longValue())) {

                throw new BusinessException(PaymentErrorCode.PAYMENT_METHOD_UNUSABLE);
            }

        }
    }


    @Override
    @GlobalTransactional
    public OrderSubmitBO submit(OrderSubmitRequest request) {

        // 所有购物车信息
        List<Long> allCartIds = request.getDistributorOrderList().stream().map(OrderSubmitRequest.DistributorOrderDTO::getCartIds).flatMap(Collection::stream).collect(Collectors.toList());
        List<CartDO> allCartDOList = cartService.listByIds(allCartIds);
        // 校验购物车
        this.validateCart(allCartDOList, allCartIds);
        // 校验采购关系
        this.validatePurchaseRelation(allCartDOList, request.getBuyerEid());
        // 所有配送商商品ID集合
        List<Long> allGoodsSkuIds = allCartDOList.stream().map(CartDO::getGoodsSkuId).distinct().collect(Collectors.toList());
        List<GoodsSkuInfoDTO> allGoodsDTOList = goodsApi.batchQueryInfoBySkuIds(allGoodsSkuIds);
        Map<Long, GoodsSkuInfoDTO> allGoodsDTOMap = allGoodsDTOList.stream().collect(Collectors.toMap(GoodsSkuInfoDTO::getId, Function.identity()));
        // 以岭子企业EId
        List<Long> yilingSubEids = enterpriseApi.listSubEids(Constants.YILING_EID);
        // 获取最新的商品Id
        this.setLastMergeGoodId(allCartDOList,allGoodsDTOMap,yilingSubEids);
        // 校验采购权限
        this.validatePurchaseAuthority(allCartDOList, request.getBuyerEid());
        // 校验商品状态
        this.validateGoodsStatus(allCartDOList, allGoodsDTOMap);
        // 校验商品库存
        this.validateGoodsInventory(allCartDOList);
        // 校验商务联系人部门
        this.checkContacterDepartmentId(request.getContacterId(),request.getDepartmentId());
        // 校验支付方式是否匹配
        this.checkOrderPaymentMethod(request);

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
        contextBO.setContacterFunction((c) -> userApi.getById(c));
        contextBO.setDeliveryAddressDTO(deliveryAddressApi.getById(request.getAddressId()));
        contextBO.setProvinceManagerFunction((c) -> this.selectProvinceManager(c));
        contextBO.setOrderNoFunction((c) -> noApi.gen(c));
        contextBO.setPriceFunction((c) -> this.getPopGoodsPrice(c));
        contextBO.setInitOrderFunction((c) -> this.initOrderFunction(c));

        List<CreateOrderRequest> createOrderRequestList = SplitOrderEnum.NORMAL.handlerInstance().splitOrder(contextBO).getCreateOrderRequestList();
        //冻结库存
        this.lockInventory(createOrderRequestList);
        OrderSubmitBO submitBO = null;
        try {
            submitBO = _this.createOrder(request, createOrderRequestList);
        }catch (Exception e){
            log.warn("创建订单失败，开始补偿库存");
            //补偿退还冻结库存
            this.backInventory(createOrderRequestList);
            throw e;
        }
        return submitBO;
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

        // 2,计算现折优惠金额
        CalOrderDiscountContextBO<CreateOrderRequest> discountContextBO = new CalOrderDiscountContextBO();
        discountContextBO.setPlatformEnum(PlatformEnum.POP);
        discountContextBO.setOrderTypeEnum(OrderTypeEnum.POP);
        discountContextBO.setBuyerEid(request.getBuyerEid());
        discountContextBO.setOpUserId(request.getOpUserId());
        discountContextBO.setCreateOrderRequestList(createOrderRequestList);

        DiscountHandlerChain handlerChain = new DiscountHandlerChain(discountContextBO);
        handlerChain.addHandler(cashDiscountProcessHandler).processDiscount();

        // 3、校验账期余额是否充足
        this.checkPaymentAccount(createOrderRequestList);

        // 4、保存订单数据
        orderApi.create(createOrderRequestList);

        // 5、获取订单信息
        List<String> orderNos = createOrderRequestList.stream().map(CreateOrderRequest::getOrderNo).collect(Collectors.toList());
        List<OrderDTO> orderDTOList = orderApi.listByOrderNos(orderNos);

        // 6、清理购物车
        cartService.removeByIds(allCartIds);

        // 7、发送订单创建消息
        List<MqMessageBO> mqMessageBOList = this.sendOrderCreatedMessage(orderDTOList);

        /************冻结库存放到最后面,防止出现订单并发,全局事务锁冲突问题************/
        // 8、冻结库存
        //冻结库存改用补偿机制
        //this.lockInventory(createOrderRequestList);


        OrderSubmitBO submitBo = new OrderSubmitBO();
        submitBo.setPayId("");
        submitBo.setOrderDTOList(orderDTOList);
        submitBo.setMqMessageBOList(mqMessageBOList);

        CreateOrderEvent createOrderEvent =   new CreateOrderEvent(this,orderDTOList,false,false);
        createOrderEvent.setOrderSourceEnum(request.getOrderSourceEnum());
        createOrderEvent.setOrderTypeEnum(request.getOrderTypeEnum());
        createOrderEvent.setUserId(request.getOpUserId());
        createOrderEvent.setUserAgent(request.getUserAgent());
        createOrderEvent.setIp(request.getIpAddress());
        createOrderEvent.setSaveOrderDevice(true);
        createOrderEvent.setReducePaymentDayAccount(true);

        /**
         * 创建成功，扣减优惠逻辑
         */
        this.applicationEventPublisher.publishEvent(createOrderEvent);

        return submitBo;

    }



    /**
     * 订单初始化
     * @param createOrderRequest
     */
    private void initOrderFunction(CreateOrderRequest createOrderRequest) {

        createOrderRequest.setPaymentStatus(PaymentStatusEnum.UNPAID.getCode());
        createOrderRequest.setOrderStatus(OrderStatusEnum.UNAUDITED.getCode());
        createOrderRequest.setCustomerConfirmStatus(CustomerConfirmEnum.CONFIRMED.getCode());
        createOrderRequest.setAuditStatus(OrderAuditStatusEnum.UNSUBMIT.getCode());
        createOrderRequest.setCustomerConfirmTime(new Date());
        createOrderRequest.setCouponDiscountAmount(BigDecimal.ZERO);
        createOrderRequest.setPlatformCouponDiscountAmount(BigDecimal.ZERO);
        createOrderRequest.setPresaleDiscountAmount(BigDecimal.ZERO);
        createOrderRequest.setPlatformPaymentDiscountAmount(BigDecimal.ZERO);
        createOrderRequest.setShopPaymentDiscountAmount(BigDecimal.ZERO);
        createOrderRequest.setCashDiscountAmount(BigDecimal.ZERO);

    }


    /**
     * 校验账期余额是否充足
     * 将相同卖家的支付金额进行合并，防止出现拆单导致，相同的卖家出现多条的问题
     * @param createOrderRequestList
     */
    private void checkPaymentAccount(List<CreateOrderRequest> createOrderRequestList) {

        List<CreateOrderRequest> paymentDayList = createOrderRequestList.stream().filter(e -> PaymentMethodEnum.PAYMENT_DAYS == PaymentMethodEnum.getByCode(e.getPaymentMethod().longValue())).collect(Collectors.toList());

        if (CollectionUtil.isEmpty(paymentDayList)) {

            return;
        }

        Long buyerEid = paymentDayList.stream().findFirst().get().getBuyerEid();

        Map<Long,BigDecimal> paymentAmountMap =  paymentDayList.stream().collect(Collectors.groupingBy(CreateOrderRequest::getSellerEid,Collectors.mapping(CreateOrderRequest::getPaymentAmount,Collectors.reducing(BigDecimal.ZERO,BigDecimal::add))));
        for (Map.Entry<Long,BigDecimal> entry : paymentAmountMap.entrySet()) {

            BigDecimal paymentDaysAvailableAmount = paymentDaysAccountApi.getAvailableAmountByCustomerEid(entry.getKey(), buyerEid);

            // 账期余额小于应付金额
            if (paymentDaysAvailableAmount.compareTo(entry.getValue()) == -1) {

                throw new BusinessException(PaymentErrorCode.PAYMENTDAYS_QUOTA_NOT_ENOUGH);
            }

        }

    }


}
