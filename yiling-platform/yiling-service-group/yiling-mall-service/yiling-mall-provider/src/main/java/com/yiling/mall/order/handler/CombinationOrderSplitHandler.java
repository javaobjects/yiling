package com.yiling.mall.order.handler;

import java.math.BigDecimal;
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
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.goods.medicine.dto.GoodsSkuInfoDTO;
import com.yiling.mall.cart.entity.CartDO;
import com.yiling.mall.order.bo.SplitOrderContextBO;
import com.yiling.mall.order.bo.SplitOrderEnum;
import com.yiling.mall.order.bo.SplitOrderResultBO;
import com.yiling.mall.order.dto.request.OrderSubmitRequest;
import com.yiling.marketing.promotion.dto.PromotionGoodsLimitDTO;
import com.yiling.order.order.dto.request.CreateOrderDetailRequest;
import com.yiling.order.order.dto.request.CreateOrderPromotionActivityRequest;
import com.yiling.order.order.dto.request.CreateOrderRequest;
import com.yiling.order.order.enums.NoEnum;
import com.yiling.order.order.enums.OrderErrorCode;
import com.yiling.order.order.enums.OrderGoodsTypeEnum;
import com.yiling.order.order.enums.OrderTypeEnum;
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
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 组合订单拆单
 *
 * @author zhigang.guo
 * @date: 2022/4/26
 */
@Slf4j
@Component(value = SplitOrderEnum.COMBINATION_ORDER)
public class CombinationOrderSplitHandler extends AbstractOrderSplitHandler {

    @Override
    protected List<CartDO> getSplitCartList(SplitOrderContextBO contextBo) {
        List<CartDO> combinationPriceList = contextBo.getAllCartDOList().stream().filter(t -> SplitOrderEnum.COMBINATION == SplitOrderEnum.getByPromotionActivityCode(t.getPromotionActivityType())).collect(Collectors.toList());

        log.info("组合商品拆单购物车数据:{}", combinationPriceList);

        return combinationPriceList;
    }

    @Override
    protected SplitOrderResultBO split(SplitOrderContextBO contextBO) {
        Map<String, String> mdcContext = MDC.getCopyOfContextMap();
        List<CartDO> combinationPriceList = contextBO.getAllCartDOList();
        Map<Long, EnterpriseDTO> allEnterpriseDTOMap = contextBO.getAllEnterpriseList().stream().collect(Collectors.toMap(EnterpriseDTO::getId, Function.identity()));
        Map<Long, EnterpriseCustomerDTO> customerDTOMap = contextBO.getCustomerDTOList().stream().collect(Collectors.toMap(EnterpriseCustomerDTO::getEid, Function.identity()));
        Map<Long, GoodsSkuInfoDTO> allGoodsDTOMap = contextBO.getAllGoodsDTOList().stream().collect(Collectors.toMap(GoodsSkuInfoDTO::getId, Function.identity()));
        // 获取组合优惠
        List<PromotionGoodsLimitDTO> promotionGoodsLimitDTOList = contextBO.getPromotionGoodsLimitDTOList().stream().collect(Collectors.toList());
        // 组合优惠信息
        Map<Long, List<PromotionGoodsLimitDTO>> promotionGoodsLimitMap = promotionGoodsLimitDTOList.stream().collect(Collectors.groupingBy(PromotionGoodsLimitDTO::getPromotionActivityId));
        // 设置订单函数
        Consumer<CreateOrderRequest> initOrderFunction = contextBO.getInitOrderFunction();

        // 按照活动
        Map<Long, List<CartDO>> cartDOMap = combinationPriceList.stream().collect(Collectors.groupingBy(CartDO::getPromotionActivityId));
        List<CreateOrderRequest> createOrderRequestList = CollUtil.newArrayList();
        OrderSubmitRequest request = contextBO.getRequest();
        String orderBatchNo = contextBO.getOrderBatchNo();
        Function<NoEnum, String> orderNoFunction = contextBO.getOrderNoFunction();
        Function<Long, UserDTO> contacterFunction = contextBO.getContacterFunction();
        DeliveryAddressDTO deliveryAddressDTO = contextBO.getDeliveryAddressDTO();
        List<Long> yilingSubEids = contextBO.getYilingSubEids();
        List<Long> industryDirectEids = contextBO.getIndustryDirectEids();
        // 是否包含以岭卖家
        boolean hasYilingSeller = request.getDistributorOrderList().stream().filter(e -> yilingSubEids.contains(e.getDistributorEid())).findFirst().isPresent();
        Map<Long, OrderSubmitRequest.DistributorOrderDTO> distributorOrderDTOMap = request.getDistributorOrderList().stream().collect(Collectors.toMap(OrderSubmitRequest.DistributorOrderDTO::getDistributorEid, Function.identity()));
        // 省区经理信息
        EnterpriseEmployeeDTO provinceManagerInfo = null;
        if (hasYilingSeller) {
            provinceManagerInfo = contextBO.getProvinceManagerFunction().apply(request.getContacterId());
        }
        for (Long promotionActivityId : cartDOMap.keySet()) {
            EnterpriseDTO enterpriseDTO =  allEnterpriseDTOMap.get(request.getBuyerEid());
            List<CartDO> cartDOS = cartDOMap.get(promotionActivityId);
            Long distributorEid = cartDOS.stream().findFirst().get().getDistributorEid();
            OrderSubmitRequest.DistributorOrderDTO orderInfo = distributorOrderDTOMap.get(distributorEid);
            CreateOrderRequest createOrderRequest = new CreateOrderRequest();
            createOrderRequest.setOrderNo(orderNoFunction.apply(NoEnum.ORDER_NO));
            createOrderRequest.setSplitOrderType(SplitOrderEnum.COMBINATION.name());
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

            // 订单组合优惠信息
            Map<Long, PromotionGoodsLimitDTO> orderPromotionGoodsMap = promotionGoodsLimitMap.get(promotionActivityId).stream().collect(Collectors.toMap(PromotionGoodsLimitDTO::getGoodsSkuId, Function.identity()));
            // 商品明细数据组装
            {
                List<CreateOrderDetailRequest> createOrderDetailRequestList = CollUtil.newArrayList();
                List<CreateOrderPromotionActivityRequest> promotionActivityRequestList = CollUtil.newArrayList();
                for (CartDO cartDO : cartDOS) {
                    GoodsSkuInfoDTO goodsDTO = allGoodsDTOMap.get(cartDO.getGoodsSkuId());
                    // 校验组合商品是否满足最新的拆分比率
                    if (cartDO.getQuantity() % goodsDTO.getPackageNumber() != 0) {

                        throw new BusinessException(OrderErrorCode.SKU_PACKAGE_NUMBER_EXIST);
                    }
                    CreateOrderDetailRequest createOrderDetailRequest = this.buildOrderDetailRequest(createOrderRequest.getOrderNo(), cartDO, goodsDTO);
                    // 保存促销类型以及活动ID
                    PromotionGoodsLimitDTO goodsLimitDTO = orderPromotionGoodsMap.get(cartDO.getGoodsSkuId());
                    createOrderDetailRequest.setPromotionActivityType(goodsLimitDTO.getType());
                    createOrderDetailRequest.setPromotionActivityId(goodsLimitDTO.getPromotionActivityId());
                    // 建立促销活动时的基价
                    createOrderDetailRequest.setPromotionActivityPrice(goodsLimitDTO.getPrice());
                    // 设置组合促销的销售价格
                    createOrderDetailRequest.setGoodsPrice(goodsLimitDTO.getPromotionPrice());
                    CreateOrderPromotionActivityRequest activityRequest = new CreateOrderPromotionActivityRequest();
                    activityRequest.setActivityId(goodsLimitDTO.getPromotionActivityId());
                    activityRequest.setActivityName(goodsLimitDTO.getPromotionName());
                    activityRequest.setActivityBear(goodsLimitDTO.getBear());
                    activityRequest.setSponsorType(goodsLimitDTO.getSponsorType());
                    activityRequest.setActivityType(PromotionActivityTypeEnum.COMBINATION.getCode());
                    activityRequest.setActivityPlatformPercent(goodsLimitDTO.getPlatformPercent());
                    // 获取促销套数
                    activityRequest.setActivityNum(cartDO.getQuantity() / goodsLimitDTO.getAllowBuyCount());
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

        MDC.setContextMap(mdcContext);
        log.info("组合促销拆单返回订单请求数据:{}", createOrderRequestList);

        if (CollectionUtil.isEmpty(createOrderRequestList)) {

            throw new BusinessException(OrderErrorCode.SPLIT_ORDER_ERROR);
        }

        return SplitOrderResultBO.builder().createOrderRequestList(createOrderRequestList).build();
    }


    /**
     * 不用设置销售价格，销售价格已在{@link CombinationOrderSplitHandler#split(com.yiling.mall.order.bo.SplitOrderContextBO)}设置
     *
     * @param priceFunction 价格函数
     * @param createOrderRequestList 订单信息
     * @param buyerEid 买家企业Eid
     */
    @Override
    protected void calculateOrderMoney(Function<QueryGoodsPriceRequest, Map<Long, GoodsPriceDTO>> priceFunction, List<CreateOrderRequest> createOrderRequestList, Long buyerEid) {
        List<CreateOrderDetailRequest> allCreateOrderDetailRequestList = createOrderRequestList.stream().map(CreateOrderRequest::getOrderDetailList).flatMap(Collection::stream).collect(Collectors.toList());
        List<Long> distributorGoodsIds = allCreateOrderDetailRequestList.stream().map(CreateOrderDetailRequest::getDistributorGoodsId).distinct().collect(Collectors.toList());
        Map<String, String> mdcContext = MDC.getCopyOfContextMap();
        // 获取价格
        Map<Long, GoodsPriceDTO> goodsPriceMap = this.getGoodsPriceDto(priceFunction, buyerEid, distributorGoodsIds);
        MDC.setContextMap(mdcContext);
        if (log.isDebugEnabled()) {
            log.debug("combinationOrderSplitHandler..calculateOrderMoney..goodsPriceMap:{}", goodsPriceMap);
        }
        // 计算订单明细上的金额
        createOrderRequestList.forEach(t -> t.getOrderDetailList().forEach(orderDetail -> {
            GoodsPriceDTO goodsPriceDTO = goodsPriceMap.get(orderDetail.getDistributorGoodsId());
            // 设置限价
            orderDetail.setLimitPrice(goodsPriceDTO.getLimitPrice());
            // 设置定价系统价格
            orderDetail.setOriginalPrice(goodsPriceDTO.getLinePrice());
            // 设置订单明细小计金额
            orderDetail.setGoodsAmount(NumberUtil.round(NumberUtil.mul(orderDetail.getGoodsPrice(), orderDetail.getGoodsQuantity()), 2));
        }));
        // 设置订单订单金额以及运费
        createOrderRequestList.forEach(orderInfo -> {
            orderInfo.setTotalAmount(orderInfo.getOrderDetailList().stream().map(CreateOrderDetailRequest::getGoodsAmount).reduce(BigDecimal::add).get());
            orderInfo.setFreightAmount(BigDecimal.ZERO);
            orderInfo.setPaymentAmount(orderInfo.getTotalAmount());
        });
        log.info("combinationOrderSplitHandler..calculateOrderMoney..result:{}", createOrderRequestList);
    }


    @Override
    protected void checkOrderMoney(OrderTypeEnum orderTypeEnum, List<CreateOrderRequest> createOrderRequestList) {

        super.checkOrderMoney(orderTypeEnum,createOrderRequestList);

        // 校验以岭品是否设置限价，未设置限价给出提示
        createOrderRequestList.forEach(t -> t.getOrderDetailList().forEach(orderDetail -> {
            if (OrderGoodsTypeEnum.YLGOODS == OrderGoodsTypeEnum.getByCode(orderDetail.getGoodsType()) && CompareUtil.compare(orderDetail.getLimitPrice(), BigDecimal.ZERO) <= 0) {
                log.warn("买家:{},组合包活动:{}以岭品:{}未设置限价",t.getBuyerEid(),orderDetail.getPromotionActivityId(),orderDetail.getDistributorGoodsId());
                throw new BusinessException(OrderErrorCode.ORDER_PROMOTION_LIMIT_GOODS_ERROR);
            }
        }));
    }
}
