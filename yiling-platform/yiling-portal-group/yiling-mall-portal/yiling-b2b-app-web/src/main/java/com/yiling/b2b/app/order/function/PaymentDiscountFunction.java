package com.yiling.b2b.app.order.function;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.yiling.b2b.app.order.vo.OrderDistributorVO;
import com.yiling.b2b.app.order.vo.OrderGoodsVO;
import com.yiling.b2b.app.order.vo.OrderSettlementPageVO;
import com.yiling.b2b.app.order.vo.PaymentActivityDetailVO;
import com.yiling.b2b.app.order.vo.PaymentActivityShowTypeEnum;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.marketing.promotion.api.PromotionActivityApi;
import com.yiling.marketing.promotion.dto.PaymentActivityUseDTO;
import com.yiling.marketing.promotion.dto.PaymentActivityUseDetailDTO;
import com.yiling.marketing.promotion.dto.PromotionDTO;
import com.yiling.marketing.promotion.dto.request.QueryPaymentActivityDetailRequest;
import com.yiling.marketing.promotion.dto.request.QueryPaymentActivityRequest;
import com.yiling.order.order.enums.PromotionActivityTypeEnum;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 计算订单支付促销活动优惠信息
 *
 * @author zhigang.guo
 * @date: 2023/4/17
 */
@Slf4j
@Component
public class PaymentDiscountFunction {

    @DubboReference(timeout = 1000 * 10)
    PromotionActivityApi promotionActivityApi;


    /**
     * 下单计算订单支付促销活动优惠
     *
     * @param currentEid
     * @param pageVO
     * @param platformPaymentActivityId
     * @param shopPaymentActivityMap
     * @param comPromotionMap
     */
    public void calPaymentDiscount(Long currentEid, OrderSettlementPageVO pageVO, Long platformPaymentActivityId, Map<Long, Long> shopPaymentActivityMap, Map<Long, PromotionDTO> comPromotionMap) {

        List<OrderDistributorVO> orderDistributorVOList = pageVO.getOrderDistributorList();
        // 平台支付促销活动记录
        List<PaymentActivityDetailVO> paymentActivityDetailVOS = this.selectPaymentPromotionActivitys(currentEid, orderDistributorVOList, comPromotionMap, shopPaymentActivityMap, platformPaymentActivityId);
        // 设置支付促销按钮
        pageVO.setPaymentActivityShowType(this.getPaymentActivityShowTypeEnum(paymentActivityDetailVOS));
        // 设置平台支付促销金额,以及平台支付促销活动ID
        if (PaymentActivityShowTypeEnum.show == pageVO.getPaymentActivityShowType()) {
            pageVO.setPlatformPaymentDiscountAmount(paymentActivityDetailVOS.stream().filter(t -> t.getIsSelected()).map(PaymentActivityDetailVO::getPaymentDiscount).reduce(BigDecimal::add).orElse(BigDecimal.ZERO));
            // 平台支付促销活动信息
            pageVO.setPlatformPaymentActivity(paymentActivityDetailVOS);
        }
        // 设置提示信息
        if (PaymentActivityShowTypeEnum.tip ==  pageVO.getPaymentActivityShowType()) {
            pageVO.setPlatformPaymentActivity(Collections.singletonList(paymentActivityDetailVOS.stream().findFirst().get()));
        }
        // 店铺支付促销总的优惠金额
        pageVO.setShopPaymentDiscountAmount(orderDistributorVOList.stream().map(OrderDistributorVO::getPaymentDiscountMoney).reduce(BigDecimal::add).orElse(BigDecimal.ZERO));
    }


    /**
     * 支付促销活动参数
     *
     * @param orderDistributorVOS
     * @param comPromotionMap
     * @return
     */
    private List<QueryPaymentActivityDetailRequest> buildQueryPaymentActivityParam(List<OrderDistributorVO> orderDistributorVOS, Map<Long, PromotionDTO> comPromotionMap) {

        List<QueryPaymentActivityDetailRequest> queryPaymentUseListDetailRequests = Lists.newArrayList();

        // 组合包活动，需要拆散组合包活动数据
        orderDistributorVOS.forEach(orderDistributor -> orderDistributor.getOrderGoodsList().stream().filter(orderGoodsVO -> PromotionActivityTypeEnum.COMBINATION == PromotionActivityTypeEnum.getByCode(orderGoodsVO.getPromotionActivityType())).forEach(orderGoodsVO -> combinationGoodDetail(comPromotionMap, queryPaymentUseListDetailRequests, orderDistributor, orderGoodsVO)));
        // 非组合包数据
        orderDistributorVOS.forEach(orderDistributor -> orderDistributor.getOrderGoodsList().stream().filter(orderGoodsVO -> PromotionActivityTypeEnum.COMBINATION != PromotionActivityTypeEnum.getByCode(orderGoodsVO.getPromotionActivityType())).forEach(orderGoodsVO -> normalGoodDetail(queryPaymentUseListDetailRequests, orderDistributor, orderGoodsVO)));

        return queryPaymentUseListDetailRequests;
    }

    // 组合品
    private void combinationGoodDetail(Map<Long, PromotionDTO> comPromotionMap, List<QueryPaymentActivityDetailRequest> queryPaymentUseListDetailRequests, OrderDistributorVO orderDistributor, OrderGoodsVO orderGoodsVO) {
        PromotionDTO promotionDTO = comPromotionMap.get(orderGoodsVO.getPromotionActivityId());

        if (promotionDTO == null) {
            return;
        }

        promotionDTO.getGoodsLimitDTOList().forEach(t -> {
            QueryPaymentActivityDetailRequest request = new QueryPaymentActivityDetailRequest();
            request.setEid(orderDistributor.getDistributorEid());
            request.setGoodsId(t.getGoodsId());
            request.setGoodsAmount(NumberUtil.mul(NumberUtil.mul(t.getAllowBuyCount(), t.getPromotionPrice()),orderGoodsVO.getGoodsQuantity()).subtract(orderGoodsVO.getCouponDiscountMoney()));
            request.setGoodsNumber(NumberUtil.mul(t.getAllowBuyCount(), orderGoodsVO.getGoodsQuantity()));
            request.setPaymentMethod(orderDistributor.getPaymentMethod().intValue());
            queryPaymentUseListDetailRequests.add(request);
        });

        return;
    }

    // 正常品
    private void normalGoodDetail(List<QueryPaymentActivityDetailRequest> queryPaymentUseListDetailRequests, OrderDistributorVO orderDistributor, OrderGoodsVO orderGoodsVO) {

        QueryPaymentActivityDetailRequest request = new QueryPaymentActivityDetailRequest();
        request.setEid(orderDistributor.getDistributorEid());
        request.setGoodsId(orderGoodsVO.getGoodsId());
        request.setGoodsAmount(orderGoodsVO.getAmount().subtract(orderGoodsVO.getCouponDiscountMoney()));
        request.setGoodsNumber(BigDecimal.valueOf(orderGoodsVO.getGoodsQuantity()));
        request.setPaymentMethod(orderDistributor.getPaymentMethod().intValue());

        queryPaymentUseListDetailRequests.add(request);

    }

    /**
     * 查询平台支付促销活动
     *
     * @param currentEid 当前登录企业信息
     * @param orderDistributorVOS 配送商的商品信息
     * @return 返回平台支付促销活动信息
     */
    private List<PaymentActivityDetailVO> selectPaymentPromotionActivitys(Long currentEid, List<OrderDistributorVO> orderDistributorVOS, Map<Long, PromotionDTO> comPromotionMap, Map<Long, Long> shopPaymentActivityIdMap, Long platformPaymentActivityId) {
        // 查询支付促销活动参数
        List<QueryPaymentActivityDetailRequest> queryPaymentUseListDetailRequests = this.buildQueryPaymentActivityParam(orderDistributorVOS, comPromotionMap);
        // 按照商品Id合并
        Map<Long, List<QueryPaymentActivityDetailRequest>> goodIdMap = queryPaymentUseListDetailRequests.stream().collect(Collectors.groupingBy(QueryPaymentActivityDetailRequest::getGoodsId));
        // 生成分组后的请求支付促销商品记录信息
        List<QueryPaymentActivityDetailRequest> groupActivityDetailRequest = goodIdMap.entrySet().stream().map(entry -> {

            QueryPaymentActivityDetailRequest firstDetailRequest = entry.getValue().stream().findFirst().get();
            QueryPaymentActivityDetailRequest detailRequest = new QueryPaymentActivityDetailRequest();
            detailRequest.setEid(firstDetailRequest.getEid());
            detailRequest.setPaymentMethod(firstDetailRequest.getPaymentMethod());
            detailRequest.setGoodsId(entry.getKey());
            detailRequest.setGoodsAmount(entry.getValue().stream().map(QueryPaymentActivityDetailRequest::getGoodsAmount).reduce(BigDecimal::add).orElse(BigDecimal.ZERO));
            detailRequest.setGoodsNumber(entry.getValue().stream().map(QueryPaymentActivityDetailRequest::getGoodsNumber).reduce(BigDecimal::add).orElse(BigDecimal.ZERO));

            return detailRequest;

        }).collect(Collectors.toList());


        QueryPaymentActivityRequest request = new QueryPaymentActivityRequest();
        request.setPlatform(PlatformEnum.B2B.getCode());
        request.setCurrentEid(currentEid);
        request.setGoodsDetailList(groupActivityDetailRequest);

        PaymentActivityUseDTO paymentActivityUse = promotionActivityApi.getPaymentActivityUseList(request);

        if (log.isDebugEnabled()) {

            log.debug("结算页查询支付促销活动入参：[{}],返回结果:[{}]", request, paymentActivityUse);
        }

        if (paymentActivityUse == null) {

            return Collections.emptyList();
        }

        // 平台支付促销活动
        List<PaymentActivityDetailVO> platformPaymentActivityDetailVOList = this.buildPaymentActivityDetailVO(paymentActivityUse.getPlatformList(), platformPaymentActivityId);

        if (CollectionUtil.isEmpty(paymentActivityUse.getBusinessList())) {

            return platformPaymentActivityDetailVOList;
        }
        // 设置商家活动支付促销活动
        {
            Map<Long, List<PaymentActivityUseDetailDTO>> listMap = paymentActivityUse.getBusinessList().stream().collect(Collectors.groupingBy(PaymentActivityUseDetailDTO::getEid));
            for (OrderDistributorVO orderDistributorVo : orderDistributorVOS) {
                List<PaymentActivityUseDetailDTO> paymentActivityUseDetailDTOList = listMap.get(orderDistributorVo.getDistributorEid());

                if (CollectionUtil.isEmpty(paymentActivityUseDetailDTOList)) {
                    continue;
                }
                // 客户选择商家支付促销活动ID
                Long shopPaymentActivityId = shopPaymentActivityIdMap.get(orderDistributorVo.getDistributorEid());
                List<PaymentActivityDetailVO> paymentActivityDetailVOList = this.buildPaymentActivityDetailVO(paymentActivityUseDetailDTOList, shopPaymentActivityId);
                // 设置商家支付促销展示类型
                orderDistributorVo.setPaymentActivityShowType(this.getPaymentActivityShowTypeEnum(paymentActivityDetailVOList));
                // 如果匹配上了支付促销活动,给出促销活动提示信息
                if (PaymentActivityShowTypeEnum.show == orderDistributorVo.getPaymentActivityShowType()) {
                    orderDistributorVo.setShopPaymentActivity(paymentActivityDetailVOList);
                    orderDistributorVo.getPaymentMethodList().forEach(paymentMethodVO -> {
                        // 当前支付方式
                        Integer paymentMethod = paymentMethodVO.getId().intValue();
                        List<PaymentActivityDetailVO> paymentMethodList = paymentActivityDetailVOList.stream().filter(t -> t.getPaymentMethodList().contains(paymentMethod)).collect(Collectors.toList());
                        if (CollectionUtil.isNotEmpty(paymentMethodList)) {
                            // 按照创建时间排序,取出最近的一条
                            Collections.sort(paymentMethodList, Comparator.comparing(PaymentActivityDetailVO::getEnabled).thenComparing(PaymentActivityDetailVO::getCreateTime).reversed());
                            paymentMethodVO.setDiscountValueRules(paymentMethodList.stream().findFirst().get().getDiscountValueRules());
                        }
                    });
                }

                if (PaymentActivityShowTypeEnum.tip == orderDistributorVo.getPaymentActivityShowType()) {
                    orderDistributorVo.setShopPaymentActivity(Collections.singletonList(paymentActivityDetailVOList.stream().findFirst().get()));
                }

                // 设置配送商支付支付优惠金额
                if (CollectionUtil.isNotEmpty(orderDistributorVo.getShopPaymentActivity())) {
                    orderDistributorVo.setPaymentDiscountMoney(orderDistributorVo.getShopPaymentActivity().stream().filter(t -> t.getIsSelected()).map(PaymentActivityDetailVO::getPaymentDiscount).reduce(BigDecimal::add).orElse(BigDecimal.ZERO));
                }
                // 设置配送商实际支付金额
                orderDistributorVo.setPaymentAmount(NumberUtil.sub(orderDistributorVo.getPaymentAmount(), orderDistributorVo.getPaymentDiscountMoney()));
            }
        }


        return platformPaymentActivityDetailVOList;

    }


    /**
     * 调用营销分摊接口查询促销分摊优惠金额
     *
     * @param currentEid 当前企业EId
     * @param orderDistributorVOS
     * @param comPromotionMap
     * @param shopPaymentActivityIdMap
     * @param platformPaymentActivityId
     */
   /* private void calculatePaymentDiscount(Long currentEid, List<OrderDistributorVO> orderDistributorVOS, Map<Long, PromotionDTO> comPromotionMap, Map<Long, Long> shopPaymentActivityIdMap, Long platformPaymentActivityId) {

        OrderUsePaymentActivityRequest request = new OrderUsePaymentActivityRequest();
        request.setOpUserId(0l);
        request.setCurrentEid(currentEid);
        request.setPlatform(PlatformEnum.B2B.getCode());
        List<OrderUsePaymentActivityGoodsRequest> queryPaymentUseListDetailRequests = Lists.newArrayList();
        request.setGoodsSkuDetailList(queryPaymentUseListDetailRequests);

        for (OrderDistributorVO orderDistributor : orderDistributorVOS) {
            Long shopActivityId = shopPaymentActivityIdMap.get(orderDistributor.getDistributorEid());
            if (ObjectUtil.isNull(shopActivityId) && ObjectUtil.isNull(platformPaymentActivityId)) {

                continue;
            }
            orderDistributor.getOrderGoodsList().forEach(orderGoodsVO -> {
                // 组合包活动，需要拆散组合包活动数据
                if (PromotionActivityTypeEnum.COMBINATION == PromotionActivityTypeEnum.getByCode(orderGoodsVO.getPromotionActivityType())) {
                    PromotionDTO promotionDTO = comPromotionMap.get(orderGoodsVO.getPromotionActivityId());
                    promotionDTO.getGoodsLimitDTOList().forEach(t -> {
                        OrderUsePaymentActivityGoodsRequest goodsDetailRequest = new OrderUsePaymentActivityGoodsRequest();
                        goodsDetailRequest.setShopActivityId(shopActivityId);
                        goodsDetailRequest.setPaymentMethod(orderDistributor.getPaymentMethod().intValue());
                        goodsDetailRequest.setEid(orderDistributor.getDistributorEid());
                        goodsDetailRequest.setGoodsId(t.getGoodsId());
                        goodsDetailRequest.setGoodsSkuId(t.getGoodsSkuId());
                        goodsDetailRequest.setRelationId(StringUtils.join(orderGoodsVO.getCartId(), Constants.SEPARATOR_MIDDLELINE, t.getGoodsSkuId()));
                        // 减去优惠劵之后的优惠金额
                        goodsDetailRequest.setGoodsAmount(NumberUtil.mul(t.getAllowBuyCount(), t.getPromotionPrice(), orderGoodsVO.getGoodsQuantity()));
                        // 商品件数
                        goodsDetailRequest.setGoodsNumber(NumberUtil.mul(t.getAllowBuyCount(), orderGoodsVO.getGoodsQuantity()));
                        goodsDetailRequest.setPlatformActivityId(platformPaymentActivityId);
                        queryPaymentUseListDetailRequests.add(goodsDetailRequest);
                    });
                } else {
                    OrderUsePaymentActivityGoodsRequest goodsDetailRequest = new OrderUsePaymentActivityGoodsRequest();
                    goodsDetailRequest.setShopActivityId(shopActivityId);
                    goodsDetailRequest.setPaymentMethod(orderDistributor.getPaymentMethod().intValue());
                    goodsDetailRequest.setEid(orderDistributor.getDistributorEid());
                    goodsDetailRequest.setGoodsId(orderGoodsVO.getGoodsId());
                    goodsDetailRequest.setGoodsSkuId(orderGoodsVO.getGoodsSkuId());
                    goodsDetailRequest.setRelationId(StringUtils.join(orderGoodsVO.getCartId(), Constants.SEPARATOR_MIDDLELINE, orderGoodsVO.getGoodsSkuId()));
                    // 减去优惠劵之后的优惠金额
                    goodsDetailRequest.setGoodsAmount(NumberUtil.sub(orderGoodsVO.getAmount(), orderGoodsVO.getCouponDiscountMoney()));
                    // 商品件数
                    goodsDetailRequest.setGoodsNumber(BigDecimal.valueOf(orderGoodsVO.getGoodsQuantity()));
                    goodsDetailRequest.setPlatformActivityId(platformPaymentActivityId);
                    queryPaymentUseListDetailRequests.add(goodsDetailRequest);
                }
            });
        }

        OrderUsePaymentActivityDTO orderUsePaymentActivityDto = promotionActivityApi.orderUsePaymentActivityShareAmountBudget(request);

        if (log.isDebugEnabled()) {

            log.debug("调动营销支付促销活动，验证，分摊,参数:[{}],结果:[{}]", request, orderUsePaymentActivityDto);
        }

        Map<Long, OrderUsePaymentActivityEnterpriseDTO> orderUsePaymentActivityEnterpriseDTOMap = orderUsePaymentActivityDto.getEnterpriseGoodsList().stream().collect(Collectors.toMap(OrderUsePaymentActivityEnterpriseDTO::getEid, Function.identity()));
        orderDistributorVOS.forEach(orderDistributor -> {
            for (OrderGoodsVO orderGoodsVO : orderDistributor.getOrderGoodsList()) {

                OrderUsePaymentActivityEnterpriseDTO orderUsePaymentActivityEnterpriseDTO = orderUsePaymentActivityEnterpriseDTOMap.get(orderDistributor.getDistributorEid());

                if (orderUsePaymentActivityEnterpriseDTO == null) {
                    continue;
                }

                // 设置订单的支付优惠金额
                Map<String, OrderUsePaymentActivityGoodsDTO> orderUsePaymentActivityGoodsMap = orderUsePaymentActivityEnterpriseDTO.getGoodsDetailList().stream().collect(Collectors.toMap(OrderUsePaymentActivityGoodsDTO::getRelationId, Function.identity()));
                PromotionDTO promotionDTO = comPromotionMap.get(orderGoodsVO.getPromotionActivityId());
                // 支付优惠总金额
                BigDecimal paymentDiscountMoney = BigDecimal.ZERO;
                if (PromotionActivityTypeEnum.COMBINATION == PromotionActivityTypeEnum.getByCode(orderGoodsVO.getPromotionActivityType())) {
                    for (PromotionGoodsLimitDTO promotionGoodsLimitDTO : promotionDTO.getGoodsLimitDTOList()) {
                        OrderUsePaymentActivityGoodsDTO orderUsePaymentActivityGoodsDTO = orderUsePaymentActivityGoodsMap.get(StringUtils.join(orderGoodsVO.getCartId(), Constants.SEPARATOR_MIDDLELINE, promotionGoodsLimitDTO.getGoodsSkuId()));
                        if (orderUsePaymentActivityGoodsDTO == null) {
                            continue;
                        }
                        // 组合商品分摊支付优惠金额
                        paymentDiscountMoney = NumberUtil.add(paymentDiscountMoney, orderUsePaymentActivityGoodsDTO.getBusinessShareAmount(), orderUsePaymentActivityGoodsDTO.getPlatformShareAmount());
                    }
                    // 设置支付分摊优惠金额
                    orderGoodsVO.setPaymentDiscountMoney(paymentDiscountMoney);
                } else {
                    OrderUsePaymentActivityGoodsDTO orderUsePaymentActivityGoodsDTO = orderUsePaymentActivityGoodsMap.get(StringUtils.join(orderGoodsVO.getCartId(), Constants.SEPARATOR_MIDDLELINE, orderGoodsVO.getGoodsSkuId()));
                    if (orderUsePaymentActivityGoodsDTO == null) {
                        continue;
                    }
                    // 组合商品分摊支付优惠金额
                    paymentDiscountMoney = NumberUtil.add(paymentDiscountMoney, orderUsePaymentActivityGoodsDTO.getBusinessShareAmount(), orderUsePaymentActivityGoodsDTO.getPlatformShareAmount());
                    // 设置支付分摊优惠金额
                    orderGoodsVO.setPaymentDiscountMoney(paymentDiscountMoney);
                }
            }
            // 设置每个配送商的实际优惠支付金额
            orderDistributor.setPaymentDiscountMoney(orderDistributor.getOrderGoodsList().stream().map(OrderGoodsVO::getPaymentDiscountMoney).reduce(BigDecimal::add).get());
        });
    }*/


    /**
     * 转换优惠劵活动信息
     *
     * @param useDetailDTOList
     * @return
     */
    private List<PaymentActivityDetailVO> buildPaymentActivityDetailVO(List<PaymentActivityUseDetailDTO> useDetailDTOList, Long activityId) {
        if (CollectionUtil.isEmpty(useDetailDTOList)) {

            return Collections.emptyList();
        }

        List<PaymentActivityDetailVO> resultList = useDetailDTOList.stream().map(t -> {
            PaymentActivityDetailVO detailVO = new PaymentActivityDetailVO();
            detailVO.setActivityId(t.getActivityId());
            detailVO.setActivityRuleIdId(t.getRuleId());
            detailVO.setCreateTime(t.getCreateTime());
            detailVO.setBeginTime(t.getBeginTime());
            detailVO.setEndTime(t.getEndTime());
            detailVO.setEnabled(t.getEnabled());
            detailVO.setEid(t.getEid());
            detailVO.setDiscountValueRules(t.getDiscountValueRules());
            detailVO.setType(t.getType());
            detailVO.setName(t.getName());
            detailVO.setSponsorType(t.getSponsorType());
            detailVO.setPaymentDiscount(t.getPaymentDiscount());
            detailVO.setDiffAmount(t.getDiffAmount());
            detailVO.setIsSelected(false);
            detailVO.setId(t.getActivityId());
            detailVO.setPaymentMethodList(t.getPaymentMethodList());
            return detailVO;

        }).collect(Collectors.toList());


        // ,如果有多个支付促销活动，先按照是否达到促销门槛排序，然后按照创建时间倒序。
        Collections.sort(resultList, Comparator.comparing(PaymentActivityDetailVO::getEnabled).thenComparing(PaymentActivityDetailVO::getCreateTime).reversed());

        // 如果用户选择活动,以用户选择的支付促销活动为准
        if (activityId != null && activityId != 0) {

            resultList.stream().filter(t -> t.getEnabled() && activityId.equals(t.getActivityId())).findFirst().ifPresent(t -> t.setIsSelected(true));
        }

        // 如果没有选中,默认选中一个
        if (activityId == null && !resultList.stream().anyMatch(t -> t.getIsSelected()) && resultList.stream().anyMatch(t -> t.getEnabled())) {
            // 取出已经达到选择条件,并按照创建时间排序,去排序倒序第一条
            List<PaymentActivityDetailVO> enableActivityDetailVOList = resultList.stream().filter(t -> t.getEnabled()).collect(Collectors.toList());
            Collections.sort(enableActivityDetailVOList, Comparator.comparing(PaymentActivityDetailVO::getCreateTime).reversed());

            enableActivityDetailVOList.stream().findFirst().ifPresent(t -> t.setIsSelected(true));
        }

        return resultList;

    }


    /**
     * 设置促销活动展示按钮
     *
     * @param paymentActivityDetailVOS
     * @return
     */
    private PaymentActivityShowTypeEnum getPaymentActivityShowTypeEnum(List<PaymentActivityDetailVO> paymentActivityDetailVOS) {
        if (CollectionUtil.isEmpty(paymentActivityDetailVOS)) {
            return PaymentActivityShowTypeEnum.hidden;
        }

        if (paymentActivityDetailVOS.stream().anyMatch(t -> t.getEnabled())) {
            return PaymentActivityShowTypeEnum.show;
        }

        return PaymentActivityShowTypeEnum.tip;
    }
}