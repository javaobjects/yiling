package com.yiling.mall.order.handler;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.goods.medicine.dto.GoodsSkuInfoDTO;
import com.yiling.mall.cart.entity.CartDO;
import com.yiling.mall.order.bo.SplitOrderContextBO;
import com.yiling.mall.order.bo.SplitOrderEnum;
import com.yiling.mall.order.bo.SplitOrderResultBO;
import com.yiling.mall.order.dto.request.OrderSubmitRequest;
import com.yiling.order.order.dto.request.CreateOrderDetailRequest;
import com.yiling.order.order.dto.request.CreateOrderRequest;
import com.yiling.order.order.enums.NoEnum;
import com.yiling.order.order.enums.OrderErrorCode;
import com.yiling.order.order.enums.OrderGoodsTypeEnum;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.order.order.enums.PaymentMethodEnum;
import com.yiling.pricing.goods.dto.GoodsPriceDTO;
import com.yiling.pricing.goods.dto.request.QueryGoodsPriceRequest;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.enterprise.dto.DeliveryAddressDTO;
import com.yiling.user.enterprise.dto.EnterpriseCustomerDTO;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.EnterpriseEmployeeDTO;
import com.yiling.user.payment.api.PaymentDaysAccountApi;
import com.yiling.user.payment.dto.PaymentDaysAccountDTO;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 正常商品拆单
 *
 * @author zhigang.guo
 * @date: 2022/4/26
 */
@Slf4j
@Component(value = SplitOrderEnum.NORMAL_ORDER)
public class NormalOrderSplitHandler extends AbstractOrderSplitHandler {
    @DubboReference
    PaymentDaysAccountApi paymentDaysAccountApi;

    @Override
    protected List<CartDO> getSplitCartList(SplitOrderContextBO contextBo) {
        List<CartDO> normalCartList = contextBo.getAllCartDOList().stream().filter(t -> SplitOrderEnum.NORMAL == SplitOrderEnum.getByPromotionActivityCode(t.getPromotionActivityType())).collect(Collectors.toList());

        log.info("正常拆单购物车数据:{}", normalCartList);

        return normalCartList;
    }


    /**
     * 构建生成订单基本信息数据
     *
     * @param request
     * @param yilingSubEids
     * @param industryDirectEids
     * @param orderBatchNo
     * @param deliveryAddressDTO
     * @param contacterFunction
     * @param provinceManagerFunction
     * @param allEnterpriseDTOMap
     * @param allCartDOMap
     * @param allGoodsDTOMap
     * @param customerDTOMap
     * @return
     */
    private List<CreateOrderRequest> buildOrderRequest(OrderSubmitRequest request, List<Long> yilingSubEids, List<Long> industryDirectEids, String orderBatchNo, DeliveryAddressDTO deliveryAddressDTO, Function<Long, UserDTO> contacterFunction, Function<Long, EnterpriseEmployeeDTO> provinceManagerFunction, Function<NoEnum, String> orderNoFunction, Map<Long, EnterpriseDTO> allEnterpriseDTOMap, Map<Long, CartDO> allCartDOMap, Map<Long, GoodsSkuInfoDTO> allGoodsDTOMap, Map<Long, EnterpriseCustomerDTO> customerDTOMap,Consumer<CreateOrderRequest> initOrderFunction) {
        List<CreateOrderRequest> createOrderRequestList = CollUtil.newArrayList();
        // 是否包含以岭卖家
        boolean hasYilingSeller = request.getDistributorOrderList().stream().filter(e -> yilingSubEids.contains(e.getDistributorEid())).findFirst().isPresent();
        // 省区经理信息
        EnterpriseEmployeeDTO provinceManagerInfo = null;
        if (hasYilingSeller) {
            provinceManagerInfo = provinceManagerFunction.apply(request.getContacterId());
        }
        for (OrderSubmitRequest.DistributorOrderDTO orderInfo : request.getDistributorOrderList()) {
            EnterpriseDTO enterpriseDTO =  allEnterpriseDTOMap.get(request.getBuyerEid());
            CreateOrderRequest createOrderRequest = new CreateOrderRequest();
            createOrderRequest.setOrderNo(orderNoFunction.apply(NoEnum.ORDER_NO));
            createOrderRequest.setSplitOrderType(SplitOrderEnum.NORMAL.name());
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
            createOrderRequest.setBuyerProvinceCode(enterpriseDTO.getProvinceCode());
            createOrderRequest.setBuyerCityCode(enterpriseDTO.getCityCode());
            createOrderRequest.setBuyerRegionCode(enterpriseDTO.getRegionCode());
            createOrderRequest.setOrderType(request.getOrderTypeEnum().getCode());
            createOrderRequest.setOrderSource(request.getOrderSourceEnum().getCode());
            createOrderRequest.setOrderNote(orderInfo.getBuyerMessage());
            createOrderRequest.setOpUserId(request.getOpUserId());
            // 保存合同编号
            createOrderRequest.setContractNumber(orderInfo.getContractNumber());

            // pop订单创建，商务联系人
            if (ObjectUtil.isNotNull(contacterFunction)) {

                UserDTO userDTO = contacterFunction.apply(request.getContacterId());
                createOrderRequest.setContacterId(userDTO != null ? userDTO.getId() : 0L);
                createOrderRequest.setContacterName(userDTO != null ? userDTO.getName() : "");
                createOrderRequest.setDepartmentId(request.getDepartmentId());
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
                orderInfo.getCartIds().forEach(cartId -> {
                    CartDO cartDO = allCartDOMap.get(cartId);
                    GoodsSkuInfoDTO goodsDTO = allGoodsDTOMap.get(cartDO.getGoodsSkuId());
                    CreateOrderDetailRequest createOrderDetailRequest = this.buildOrderDetailRequest(createOrderRequest.getOrderNo(), cartDO, goodsDTO);
                    if (OrderTypeEnum.POP == request.getOrderTypeEnum()) {
                        createOrderDetailRequest.setGoodsType(OrderGoodsTypeEnum.YLGOODS.getCode());
                    }
                    createOrderDetailRequestList.add(this.buildOrderDetailRequest(createOrderRequest.getOrderNo(), cartDO, goodsDTO));
                });
                createOrderRequest.setOrderDetailList(createOrderDetailRequestList);
            }
            // 收货地址
            createOrderRequest.setOrderAddressInfo(this.buildOrderAddressRequest(deliveryAddressDTO));
            // 购销合同
            createOrderRequest.setContractFileKeyList(orderInfo.getContractFileKeyList());
            // 设置订单基本状态
            initOrderFunction.accept(createOrderRequest);

            createOrderRequestList.add(createOrderRequest);
        }

        return createOrderRequestList;
    }


    /**
     * 获取账期上浮点位
     * 如果是账期支付，并且是b2b订单，需要上浮价格
     *
     * @param orderInfo
     * @return
     */
    private BigDecimal getAccountUpPoint(CreateOrderRequest orderInfo) {
        BigDecimal upPoint = new BigDecimal(1);
        if (OrderTypeEnum.B2B != OrderTypeEnum.getByCode(orderInfo.getOrderType())) {
            return null;
        }

        if (orderInfo.getPaymentMethod() == null) {
            return null;
        }
        if (PaymentMethodEnum.PAYMENT_DAYS != PaymentMethodEnum.getByCode(orderInfo.getPaymentMethod().longValue())) {
            return null;
        }
        // 是否存在非以岭品,非以岭需要计算账期上浮,如果全部是以岭品无需获取上浮点位
        boolean existNotYlGoods = orderInfo.getOrderDetailList().stream().anyMatch(orderDetail -> OrderGoodsTypeEnum.YLGOODS != OrderGoodsTypeEnum.getByCode(orderDetail.getGoodsType()));
        if (!existNotYlGoods) {
            return null;
        }
        PaymentDaysAccountDTO accountDTO = paymentDaysAccountApi.getByCustomerEid(orderInfo.getSellerEid(), orderInfo.getBuyerEid());
        if (accountDTO == null) {
            throw new BusinessException(UserErrorCode.PAYMENT_DAYS_ACCOUNT_NOT_EXISTS);
        }
        if (accountDTO.getUpPoint() != null) {
            upPoint = upPoint.add(NumberUtil.div(accountDTO.getUpPoint(), 100));
        }
        return upPoint;
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
            log.debug("normalOrderSplitHandler..calculateOrderMoney..goodsPriceMap:{}", goodsPriceMap);
        }
        // 计算订单明细上的金额
        createOrderRequestList.stream().forEach(orderInfo -> {
            // 获取账期上浮点位
            BigDecimal upPoint = this.getAccountUpPoint(orderInfo);
            orderInfo.getOrderDetailList().forEach(orderDetail -> {
                GoodsPriceDTO goodsPriceDTO = goodsPriceMap.get(orderDetail.getDistributorGoodsId());
                // 设置限价
                orderDetail.setLimitPrice(goodsPriceDTO.getLimitPrice());
                // 商品当前销售价格
                orderDetail.setGoodsPrice(goodsPriceDTO.getLinePrice());
                // 定价系统原始价格
                orderDetail.setOriginalPrice(goodsPriceDTO.getLinePrice());
                // 如果是账期支付，并且是b2b订单，非以岭商品上浮价格
                if (upPoint != null  && OrderGoodsTypeEnum.YLGOODS != OrderGoodsTypeEnum.getByCode(orderDetail.getGoodsType())) {
                    orderDetail.setGoodsPrice(NumberUtil.round(NumberUtil.mul(goodsPriceDTO.getLinePrice(), upPoint), 2));
                }
                // 设置小计金额
                orderDetail.setGoodsAmount(NumberUtil.round(NumberUtil.mul(orderDetail.getGoodsPrice(), orderDetail.getGoodsQuantity()), 2));
            });
            // 设置订单金额以及运费
            List<CreateOrderDetailRequest> createOrderDetailRequestList = orderInfo.getOrderDetailList();
            orderInfo.setTotalAmount(createOrderDetailRequestList.stream().map(CreateOrderDetailRequest::getGoodsAmount).reduce(BigDecimal::add).get());
            orderInfo.setFreightAmount(BigDecimal.ZERO);
            orderInfo.setPaymentAmount(orderInfo.getTotalAmount());
        });
        log.info("normalOrderSplitHandler..calculateOrderMoney..result:{}", createOrderRequestList);
    }

    @Override
    protected SplitOrderResultBO split(SplitOrderContextBO contextBo) {
        Map<String, String> mdcContext = MDC.getCopyOfContextMap();
        OrderSubmitRequest request = contextBo.getRequest();
        Map<Long, EnterpriseDTO> allEnterpriseDTOMap = contextBo.getAllEnterpriseList().stream().collect(Collectors.toMap(EnterpriseDTO::getId, Function.identity()));
        Map<Long, EnterpriseCustomerDTO> customerDTOMap = contextBo.getCustomerDTOList().stream().collect(Collectors.toMap(EnterpriseCustomerDTO::getEid, Function.identity()));
        Map<Long, CartDO> allCartDOMap = contextBo.getAllCartDOList().stream().collect(Collectors.toMap(CartDO::getId, Function.identity()));
        Map<Long, GoodsSkuInfoDTO> allGoodsDTOMap = contextBo.getAllGoodsDTOList().stream().collect(Collectors.toMap(GoodsSkuInfoDTO::getId, Function.identity()));
        Consumer<CreateOrderRequest> initOrderFunction = contextBo.getInitOrderFunction();


        // 生成创建订单基本数据
        List<CreateOrderRequest> createOrderRequestList = this.buildOrderRequest(request, contextBo.getYilingSubEids(), contextBo.getIndustryDirectEids(), contextBo.getOrderBatchNo(), contextBo.getDeliveryAddressDTO(), contextBo.getContacterFunction(), contextBo.getProvinceManagerFunction(), contextBo.getOrderNoFunction(), allEnterpriseDTOMap, allCartDOMap, allGoodsDTOMap, customerDTOMap,initOrderFunction);

        MDC.setContextMap(mdcContext);
        log.info("正常拆单返回订单请求数据:{}", createOrderRequestList);

        if (CollectionUtil.isEmpty(createOrderRequestList)) {
            throw new BusinessException(OrderErrorCode.SPLIT_ORDER_ERROR);
        }

        return SplitOrderResultBO.builder().createOrderRequestList(createOrderRequestList).build();
    }
}
