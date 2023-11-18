package com.yiling.mall.order.handler;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
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
import com.yiling.framework.common.util.Constants;
import com.yiling.goods.medicine.dto.GoodsSkuInfoDTO;
import com.yiling.mall.cart.entity.CartDO;
import com.yiling.mall.order.bo.SplitOrderContextBO;
import com.yiling.mall.order.bo.SplitOrderEnum;
import com.yiling.mall.order.bo.SplitOrderResultBO;
import com.yiling.mall.order.dto.request.OrderSubmitRequest;
import com.yiling.marketing.presale.dto.PresaleActivityGoodsDTO;
import com.yiling.order.order.dto.request.CreateOrderDetailRequest;
import com.yiling.order.order.dto.request.CreateOrderPromotionActivityRequest;
import com.yiling.order.order.dto.request.CreateOrderRequest;
import com.yiling.order.order.dto.request.CreatePresaleOrderRequest;
import com.yiling.order.order.enums.NoEnum;
import com.yiling.order.order.enums.OrderCategoryEnum;
import com.yiling.order.order.enums.OrderErrorCode;
import com.yiling.order.order.enums.OrderGoodsTypeEnum;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.order.order.enums.PaymentStatusEnum;
import com.yiling.order.order.enums.PreSaleActivityTypeEnum;
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
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/** 预售活动订单
 * @author zhigang.guo
 * @date: 2022/10/14
 */
@Component(value = SplitOrderEnum.PRE_SALE_ORDER)
@Slf4j
public class PresaleOrderSplitHandler extends AbstractOrderSplitHandler {
    @Override
    protected List<CartDO> getSplitCartList(SplitOrderContextBO contextBo) {

        List<CartDO> normalCartList = contextBo.getAllCartDOList().stream().filter(t -> SplitOrderEnum.PRESALE == SplitOrderEnum.getByPromotionActivityCode(t.getPromotionActivityType())).collect(Collectors.toList());

        log.info("预售订单购物车数据:{}", normalCartList);

        return normalCartList;
    }


    /**
     * 校验预售活动信息
     * @param cartDO 快速购买商品信息
     * @param presaleActivityGoodsDTO 预售活动信息
     * @return
     */
    private void checkPreSaleActivity(CartDO cartDO,PresaleActivityGoodsDTO presaleActivityGoodsDTO) {

        if (presaleActivityGoodsDTO == null) {

            throw new BusinessException(OrderErrorCode.SUBMIT_PRESALE_ORDER_ERROR);
        }

        if (CompareUtil.compare(presaleActivityGoodsDTO.getMinNum(),0) != 0 &&  CompareUtil.compare(cartDO.getQuantity(),presaleActivityGoodsDTO.getMinNum()) < 0 ) {

            String failMsg = MessageFormat.format(OrderErrorCode.SUBMIT_PRESALE_GOODS_MIN_ERROR.getMessage(), presaleActivityGoodsDTO.getMinNum());

            throw new BusinessException(OrderErrorCode.SUBMIT_PRESALE_GOODS_MIN_ERROR, failMsg);
        }

        // 商品总计可购买最大数量
        Integer availableAllNum =  NumberUtil.sub(presaleActivityGoodsDTO.getAllNum(),presaleActivityGoodsDTO.getAllHasBuyNum()).intValue();
        // 每个人最大可够买数量
        Integer availableMaxNum =  NumberUtil.sub(presaleActivityGoodsDTO.getMaxNum(),presaleActivityGoodsDTO.getCurrentHasBuyNum()).intValue();

        if (CompareUtil.compare(presaleActivityGoodsDTO.getMaxNum(),0) != 0 && CompareUtil.compare(cartDO.getQuantity(),availableMaxNum) > 0) {

            String failMsg = MessageFormat.format(OrderErrorCode.SUBMIT_PRESALE_GOODS_MAX_ERROR.getMessage(), (availableMaxNum < 0) ? 0 : availableMaxNum);

            throw new BusinessException(OrderErrorCode.SUBMIT_PRESALE_GOODS_MAX_ERROR, failMsg);
        }

        if (CompareUtil.compare(presaleActivityGoodsDTO.getAllNum(),0) != 0 && CompareUtil.compare(cartDO.getQuantity(),availableAllNum) > 0) {

            String failMsg = MessageFormat.format(OrderErrorCode.SUBMIT_PRESALE_GOODS_ALL_ERROR.getMessage(),  (availableAllNum < 0) ? 0 : availableAllNum);

            throw new BusinessException(OrderErrorCode.SUBMIT_PRESALE_GOODS_ALL_ERROR, failMsg);
        }

    }

    /**
     * 构建预售订单扩展信息
     * @param createOrderRequest
     * @param presaleActivityGoodsDTO
     * @return
     */
    private void buildPreSalOrderRequest(CreateOrderRequest createOrderRequest,PresaleActivityGoodsDTO presaleActivityGoodsDTO) {

        CreatePresaleOrderRequest createPresaleOrderRequest = new CreatePresaleOrderRequest();
        createPresaleOrderRequest.setOrderNo(createOrderRequest.getOrderNo());
        createPresaleOrderRequest.setActivityType(presaleActivityGoodsDTO.getPresaleType());
        createPresaleOrderRequest.setBalanceEndTime(presaleActivityGoodsDTO.getFinalPayEndTime());
        createPresaleOrderRequest.setBalanceStartTime(presaleActivityGoodsDTO.getFinalPayBeginTime());
        createPresaleOrderRequest.setIsPayDeposit(0);
        createPresaleOrderRequest.setIsPayBalance(0);
        createPresaleOrderRequest.setOpUserId(createOrderRequest.getOpUserId());
        createPresaleOrderRequest.setOpTime(createOrderRequest.getOpTime());
        // 是否发送取消短信
        createPresaleOrderRequest.setHasSendCancelSms(1);
        if (DateUtil.between(presaleActivityGoodsDTO.getFinalPayEndTime(),presaleActivityGoodsDTO.getFinalPayBeginTime(), DateUnit.HOUR) >= 24 ) {
            createPresaleOrderRequest.setHasSendCancelSms(0);
        }
        if (PreSaleActivityTypeEnum.FULL == PreSaleActivityTypeEnum.getByCode(presaleActivityGoodsDTO.getPresaleType())) {
            // 如果是全款无需支付定金,直接付尾款
            createPresaleOrderRequest.setIsPayDeposit(1);
            // 设置订单的支付尾款时间为15分钟之内
            createPresaleOrderRequest.setBalanceStartTime(new Date());
            createPresaleOrderRequest.setBalanceEndTime(DateUtil.offsetMinute(new Date(),15));
            // 默认设置为部分支付
            createOrderRequest.setPaymentStatus(PaymentStatusEnum.PARTPAID.getCode());
            createPresaleOrderRequest.setHasSendPaySms(1);
            createPresaleOrderRequest.setHasSendCancelSms(1);
        }


        createOrderRequest.setCreatePresaleOrderRequest(createPresaleOrderRequest);
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
    private List<CreateOrderRequest> buildOrderRequest(OrderSubmitRequest request, List<Long> yilingSubEids, List<Long> industryDirectEids, String orderBatchNo, DeliveryAddressDTO deliveryAddressDTO, Function<Long, UserDTO> contacterFunction, Function<Long, EnterpriseEmployeeDTO> provinceManagerFunction, Function<NoEnum, String> orderNoFunction, Map<Long, EnterpriseDTO> allEnterpriseDTOMap, Map<Long, CartDO> allCartDOMap, Map<Long, GoodsSkuInfoDTO> allGoodsDTOMap, Map<Long, EnterpriseCustomerDTO> customerDTOMap,Map<String, PresaleActivityGoodsDTO> presaleActivityGoodsDTOMap,Consumer<CreateOrderRequest> initOrderFunction) {

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
            createOrderRequest.setSplitOrderType(SplitOrderEnum.PRESALE.name());
            createOrderRequest.setOrderCategory(OrderCategoryEnum.PRESALE.getCode());
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
            // pop订单创建，商务联系人
            if (ObjectUtil.isNotNull(contacterFunction)) {
                UserDTO userDTO = contacterFunction.apply(request.getContacterId());
                createOrderRequest.setContacterId(userDTO != null ? userDTO.getId() : 0L);
                createOrderRequest.setContacterName(userDTO != null ? userDTO.getName() : "");
                createOrderRequest.setDepartmentId(request.getDepartmentId());
            }
            // 保存合同编号
            createOrderRequest.setContractNumber(orderInfo.getContractNumber());
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
            // 商品明细数据组装
            {
                List<CreateOrderDetailRequest> createOrderDetailRequestList = CollUtil.newArrayList();
                List<CreateOrderPromotionActivityRequest> promotionActivityRequestList = CollUtil.newArrayList();

                for (Long cartId : orderInfo.getCartIds()) {
                    CartDO cartDO = allCartDOMap.get(cartId);
                    GoodsSkuInfoDTO goodsDTO = allGoodsDTOMap.get(cartDO.getGoodsSkuId());
                    PresaleActivityGoodsDTO presaleActivityGoodsDTO = presaleActivityGoodsDTOMap.get(cartDO.getPromotionActivityId() + Constants.SEPARATOR_MIDDLELINE + cartDO.getDistributorGoodsId());

                    if (presaleActivityGoodsDTO == null) {

                        throw new BusinessException(OrderErrorCode.SUBMIT_PRESALE_ORDER_ERROR);
                    }

                    // 校验预售订单数据量限制
                    this.checkPreSaleActivity(cartDO,presaleActivityGoodsDTO);

                    CreateOrderDetailRequest createOrderDetailRequest = this.buildOrderDetailRequest(createOrderRequest.getOrderNo(), cartDO, goodsDTO);
                    if (OrderTypeEnum.POP == request.getOrderTypeEnum()) {
                        createOrderDetailRequest.setGoodsType(OrderGoodsTypeEnum.YLGOODS.getCode());
                    }
                    createOrderDetailRequest.setPromotionActivityType(PromotionActivityTypeEnum.PRESALE.getCode());
                    createOrderDetailRequest.setPromotionActivityId(cartDO.getPromotionActivityId());
                    // 设置商品价格
                    createOrderDetailRequest.setGoodsPrice(presaleActivityGoodsDTO.getPresaleAmount());
                    createOrderDetailRequestList.add(createOrderDetailRequest);

                    CreateOrderPromotionActivityRequest activityRequest = new CreateOrderPromotionActivityRequest();
                    activityRequest.setActivityId(cartDO.getPromotionActivityId());
                    activityRequest.setActivityName(presaleActivityGoodsDTO.getName());
                    activityRequest.setActivityBear(presaleActivityGoodsDTO.getBear());
                    activityRequest.setActivityType(PromotionActivityTypeEnum.PRESALE.getCode());
                    activityRequest.setActivityPlatformPercent(presaleActivityGoodsDTO.getPlatformRatio());
                    activityRequest.setOpUserId(request.getOpUserId());
                    promotionActivityRequestList.add(activityRequest);

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
            // 设置订单基本状态
            initOrderFunction.accept(createOrderRequest);
            // 目前只有一个商品先暂时取第一个
            this.buildPreSalOrderRequest(createOrderRequest,presaleActivityGoodsDTOMap.values().stream().findFirst().get());

            createOrderRequestList.add(createOrderRequest);
        }



        return createOrderRequestList;
    }

    @Override
    protected SplitOrderResultBO split(SplitOrderContextBO contextBo) {
        Map<String, String> mdcContext = MDC.getCopyOfContextMap();
        OrderSubmitRequest request = contextBo.getRequest();
        Map<Long, EnterpriseDTO> allEnterpriseDTOMap = contextBo.getAllEnterpriseList().stream().collect(Collectors.toMap(EnterpriseDTO::getId, Function.identity()));
        Map<Long, EnterpriseCustomerDTO> customerDTOMap = contextBo.getCustomerDTOList().stream().collect(Collectors.toMap(EnterpriseCustomerDTO::getEid, Function.identity()));
        Map<Long, CartDO> allCartDOMap = contextBo.getAllCartDOList().stream().collect(Collectors.toMap(CartDO::getId, Function.identity()));
        Map<Long, GoodsSkuInfoDTO> allGoodsDTOMap = contextBo.getAllGoodsDTOList().stream().collect(Collectors.toMap(GoodsSkuInfoDTO::getId, Function.identity()));
        Map<String, PresaleActivityGoodsDTO> activityGoodsDTOMap =  contextBo.getPresaleActivityGoodsDTOList().stream().collect(Collectors.toMap(t ->  t.getId() + Constants.SEPARATOR_MIDDLELINE  + t.getGoodsId(), Function.identity()));

        Consumer<CreateOrderRequest> initOrderFunction = contextBo.getInitOrderFunction();

        // 生成创建订单基本数据
        List<CreateOrderRequest> createOrderRequestList = this.buildOrderRequest(request, contextBo.getYilingSubEids(), contextBo.getIndustryDirectEids(), contextBo.getOrderBatchNo(), contextBo.getDeliveryAddressDTO(), contextBo.getContacterFunction(), contextBo.getProvinceManagerFunction(), contextBo.getOrderNoFunction(), allEnterpriseDTOMap, allCartDOMap, allGoodsDTOMap, customerDTOMap,activityGoodsDTOMap,initOrderFunction);

        MDC.setContextMap(mdcContext);
        log.info("预售拆单返回订单请求数据:{}", createOrderRequestList);

        if (CollectionUtil.isEmpty(createOrderRequestList)) {
            throw new BusinessException(OrderErrorCode.SPLIT_ORDER_ERROR);
        }

        return SplitOrderResultBO.builder().createOrderRequestList(createOrderRequestList).build();
    }

    /**
     *  明细的商品销售单价无需处理,上一步已设置
     * @param priceFunction 获取价格函数
     * @param createOrderRequestList 订单信息
     * @param buyerEid 买家企业Eid
     */
    @Override
    protected void calculateOrderMoney(Function<QueryGoodsPriceRequest, Map<Long, GoodsPriceDTO>> priceFunction, List<CreateOrderRequest> createOrderRequestList, Long buyerEid) {
        Map<String, String> mdcContext = MDC.getCopyOfContextMap();
        List<CreateOrderDetailRequest> allCreateOrderDetailRequestList = createOrderRequestList.stream().map(CreateOrderRequest::getOrderDetailList).flatMap(Collection::stream).collect(Collectors.toList());
        List<Long> distributorGoodsIds = allCreateOrderDetailRequestList.stream().map(CreateOrderDetailRequest::getDistributorGoodsId).distinct().collect(Collectors.toList());
        // 获取价格
        Map<Long, GoodsPriceDTO> goodsPriceMap = this.getGoodsPriceDto(priceFunction, buyerEid, distributorGoodsIds);
        MDC.setContextMap(mdcContext);
        if (log.isDebugEnabled()) {
            log.debug("presaleOrderSplitHandler..calculateOrderMoney..goodsPriceMap:{}", goodsPriceMap);
        }
        // 计算订单明细上的金额
        createOrderRequestList.stream().forEach(orderInfo -> {
            orderInfo.getOrderDetailList().forEach(orderDetail -> {
                GoodsPriceDTO goodsPriceDTO = goodsPriceMap.get(orderDetail.getDistributorGoodsId());
                // 设置限价
                orderDetail.setLimitPrice(goodsPriceDTO.getLimitPrice());
                // 定价系统原始价格
                orderDetail.setOriginalPrice(goodsPriceDTO.getLinePrice());
                // 设置小计金额
                orderDetail.setGoodsAmount(NumberUtil.round(NumberUtil.mul(orderDetail.getGoodsPrice(), orderDetail.getGoodsQuantity()), 2));
            });
            // 设置订单金额以及运费
            List<CreateOrderDetailRequest> createOrderDetailRequestList = orderInfo.getOrderDetailList();
            orderInfo.setTotalAmount(createOrderDetailRequestList.stream().map(CreateOrderDetailRequest::getGoodsAmount).reduce(BigDecimal::add).get());
            orderInfo.setFreightAmount(BigDecimal.ZERO);
            orderInfo.setPaymentAmount(orderInfo.getTotalAmount());
        });
        log.info("presaleOrderSplitHandler..calculateOrderMoney..result:{}", createOrderRequestList);
    }
}
