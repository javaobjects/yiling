package com.yiling.mall.order.handler;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.yiling.framework.common.exception.BusinessException;
import com.yiling.goods.medicine.api.GoodsBiddingPriceApi;
import com.yiling.goods.medicine.api.GoodsDiscountRateApi;
import com.yiling.mall.order.bo.CalOrderDiscountContextBO;
import com.yiling.order.order.dto.CashDiscountAgreementInfoDTO;
import com.yiling.order.order.dto.request.CreateOrderDetailRequest;
import com.yiling.order.order.dto.request.CreateOrderRequest;
import com.yiling.order.order.dto.request.OrderDetailCashDiscountInfoDTO;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.order.payment.enums.PaymentErrorCode;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.procrelation.api.ProcurementRelationGoodsApi;
import com.yiling.user.procrelation.dto.ProcRelationRebateResultBO;
import com.yiling.user.procrelation.dto.request.CalculateProcRelationRebateRequest;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.util.NumberUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 计算订单现折优惠金额
 *
 * @author zhigang.guo
 * @date: 2022/11/18
 */
@Component
@Order(3)
@Slf4j
public class CashDiscountProcessHandler extends AbstractDiscountProcessHandler implements DiscountProcessHandler {
    @DubboReference
    ProcurementRelationGoodsApi procurementRelationGoodsApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    GoodsBiddingPriceApi goodsBiddingPriceApi;
    @DubboReference
    GoodsDiscountRateApi goodsDiscountRateApi;

    @Override
    protected void processDiscount(CalOrderDiscountContextBO discountContextBo) {

        // 目前现折优惠金额只有pop订单参与
        if (OrderTypeEnum.POP != discountContextBo.getOrderTypeEnum()) {

            return;
        }

        if (log.isDebugEnabled()) {

            log.debug("计算订单现折活动,订单来源:{},参数:{}", discountContextBo.getPlatformEnum(), discountContextBo);
        }

        List<CreateOrderRequest> createOrderRequestList = discountContextBo.getCreateOrderRequestList();
        /**计算订单现折优惠**/
        createOrderRequestList.forEach(createOrderRequest -> this.calculateOrderCashDiscountAmount(createOrderRequest));
    }


    /**
     * 计算现折金额
     *
     * @param createOrderRequest
     * @return
     */
    private void calculateOrderCashDiscountAmount(CreateOrderRequest createOrderRequest) {

        List<CreateOrderDetailRequest> orderDetailDTOList = createOrderRequest.getOrderDetailList();

        Map<Long, List<CreateOrderDetailRequest>> goodDetailMap = orderDetailDTOList.stream().collect(Collectors.groupingBy(CreateOrderDetailRequest::getGoodsId));

        CalculateProcRelationRebateRequest calRequest = buildCalculateRequest(goodDetailMap, createOrderRequest.getBuyerEid(), createOrderRequest.getDistributorEid());
        // 查询商业建采优惠信息
        List<ProcRelationRebateResultBO> relationRebateResultBOS = procurementRelationGoodsApi.calculateRebateForGoods(calRequest);

        if (log.isDebugEnabled()) {

            log.info("..计算订单现折优惠入参:[{}],订单编号:{},返回结果:[{}]", calRequest, createOrderRequest.getOrderNo(), relationRebateResultBOS);
        }

        if (CollectionUtil.isEmpty(relationRebateResultBOS)) {

            return;
        }

        // 同一个商品,同一个配送商理论在同时进行中的,只存在一个建采活动中
        Map<Long, ProcRelationRebateResultBO> procRelationRebateResultBOMap = relationRebateResultBOS.stream().collect(Collectors.toMap(e -> e.getGoodsId(), Function.identity(), (v1, v2) -> v1));
        List<Long> goodsIds = orderDetailDTOList.stream().map(CreateOrderDetailRequest::getGoodsId).distinct().collect(Collectors.toList());

        EnterpriseDTO buyerEnterpriseDTO = enterpriseApi.getById(createOrderRequest.getBuyerEid());
        // 招标挂网价
        Map<Long, BigDecimal> goodsBiddingPriceMap = goodsBiddingPriceApi.queryGoodsBidingPriceByLocation(goodsIds, buyerEnterpriseDTO.getProvinceCode());
        // 商品最低折扣比率字典
        Map<Long, BigDecimal> goodsDiscountRateMap = goodsDiscountRateApi.queryGoodsDiscountRateMap(createOrderRequest.getBuyerEid(), goodsIds);

        List<OrderDetailCashDiscountInfoDTO> orderDetailCashDiscountInfoList = CollUtil.newArrayList();
        // 金额正序排序
        orderDetailDTOList = orderDetailDTOList.stream().sorted(Comparator.comparing(CreateOrderDetailRequest::getGoodsAmount)).collect(Collectors.toList());

        orderDetailDTOList.stream().filter(t -> procRelationRebateResultBOMap.get(t.getGoodsId()) != null).forEach(orderDetail -> {
            ProcRelationRebateResultBO procRelationRebateResultBO = procRelationRebateResultBOMap.get(orderDetail.getGoodsId());
            // 相同的品出现多个规格，需要将现折金额分摊到明细上
            CashDiscountAgreementInfoDTO agreementInfoDTO = this.shareGoodsCalculateResult(orderDetail, goodDetailMap.get(orderDetail.getGoodsId()), procRelationRebateResultBO, orderDetailCashDiscountInfoList);
            // 商品现折金额
            BigDecimal goodsCashDiscountAmount = agreementInfoDTO.getDiscountAmount();
            // 商品应付金额
            BigDecimal goodsPayableAmount = NumberUtil.sub(orderDetail.getGoodsAmount(), goodsCashDiscountAmount);
            // 商品招标挂网价
            BigDecimal goodsBiddingPrice = goodsBiddingPriceMap.getOrDefault(orderDetail.getGoodsId(), BigDecimal.ZERO);

            // 如果商品折后金额小于商品招标挂网价，则错误提示
            if (goodsPayableAmount.compareTo(goodsBiddingPrice) == -1) {

                throw new BusinessException(PaymentErrorCode.GOODS_DISCOUNT_AMOUNT_ERROR);
            }

            // 如果商品折扣比率小于商品设置的最低折扣比率，则错误提示
            BigDecimal goodsMinDiscountRate = goodsDiscountRateMap.get(orderDetail.getGoodsId());

            if (goodsMinDiscountRate != null && goodsMinDiscountRate.compareTo(BigDecimal.ZERO) == 1) {

                BigDecimal goodsCashDiscountRate = NumberUtil.div(goodsPayableAmount, orderDetail.getGoodsAmount(), 2, RoundingMode.HALF_UP);

                if (goodsCashDiscountRate.compareTo(goodsMinDiscountRate) == -1) {
                    throw new BusinessException(PaymentErrorCode.GOODS_DISCOUNT_AMOUNT_ERROR);
                }
            }

            OrderDetailCashDiscountInfoDTO orderDetailCashDiscountInfo = new OrderDetailCashDiscountInfoDTO();
            orderDetailCashDiscountInfo.setGoodsId(orderDetail.getGoodsId());
            orderDetailCashDiscountInfo.setGoodsAmount(orderDetail.getGoodsAmount());
            orderDetailCashDiscountInfo.setCashDiscountAmount(goodsCashDiscountAmount);
            orderDetailCashDiscountInfo.setCashDiscountAgreementInfoList(Collections.singletonList(agreementInfoDTO));
            orderDetailCashDiscountInfoList.add(orderDetailCashDiscountInfo);

            // 现折金额大于商品金额
            if (CompareUtil.compare(orderDetailCashDiscountInfo.getCashDiscountAmount(), orderDetailCashDiscountInfo.getGoodsAmount()) > 0) {

                throw new BusinessException(PaymentErrorCode.GOODS_DISCOUNT_AMOUNT_ERROR);
            }

            orderDetail.setOrderDetailCashDiscountInfoDTO(orderDetailCashDiscountInfo);
            orderDetail.setCashDiscountAmount(orderDetailCashDiscountInfo.getCashDiscountAmount());

        });

        // 设置订单现折金额
        createOrderRequest.setCashDiscountAmount(orderDetailCashDiscountInfoList.stream().map(t -> t.getCashDiscountAmount()).reduce(BigDecimal::add).get());
        // 实付金额等
        createOrderRequest.setPaymentAmount(NumberUtil.sub(createOrderRequest.getPaymentAmount(), createOrderRequest.getCashDiscountAmount()));

    }


    /**
     * 现折金额分摊
     *
     * @param orderDetail
     * @param orderDetailDTOS
     * @param procRelationRebateResultBO
     * @return
     */
    private CashDiscountAgreementInfoDTO shareGoodsCalculateResult(CreateOrderDetailRequest orderDetail, List<CreateOrderDetailRequest> orderDetailDTOS, ProcRelationRebateResultBO procRelationRebateResultBO, List<OrderDetailCashDiscountInfoDTO> orderDetailCashDiscountInfoList) {

        // 表示只有一个商品，无需分摊现折金额
        if (orderDetailDTOS.size() <= 1) {
            CashDiscountAgreementInfoDTO agreementInfoDTO = new CashDiscountAgreementInfoDTO();
            agreementInfoDTO.setAgreementId(procRelationRebateResultBO.getRelationId());
            agreementInfoDTO.setVersion(procRelationRebateResultBO.getVersionId());
            agreementInfoDTO.setPolicyValue(procRelationRebateResultBO.getRebate());
            agreementInfoDTO.setDiscountAmount(procRelationRebateResultBO.getRebateAmount());

            return agreementInfoDTO;
        }

        // 表示相同的品出现多个规格，需要将现折金额分摊到明细上
        BigDecimal totalGoodsAmount = orderDetailDTOS.stream().map(CreateOrderDetailRequest::getGoodsAmount).reduce(BigDecimal::add).get();
        // 最后已一条分摊明细集合
        CreateOrderDetailRequest lastOrderDetail = orderDetailDTOS.stream().sorted(Comparator.comparing(CreateOrderDetailRequest::getGoodsAmount, Comparator.reverseOrder())).findFirst().get();

        CashDiscountAgreementInfoDTO agreementInfoDTO = new CashDiscountAgreementInfoDTO();
        agreementInfoDTO.setAgreementId(procRelationRebateResultBO.getRelationId());
        agreementInfoDTO.setVersion(procRelationRebateResultBO.getVersionId());
        agreementInfoDTO.setPolicyValue(procRelationRebateResultBO.getRebate());

        BigDecimal ratio = NumberUtil.div(orderDetail.getGoodsAmount(), totalGoodsAmount, 6);

        // 表示最后一条分摊
        if (orderDetail.getGoodsSkuId().equals(lastOrderDetail.getGoodsSkuId())) {
            agreementInfoDTO.setDiscountAmount(NumberUtil.sub(procRelationRebateResultBO.getRebateAmount(), getHashShareDiscount(orderDetailCashDiscountInfoList, procRelationRebateResultBO.getGoodsId(), procRelationRebateResultBO.getRelationId())));
        } else {
            agreementInfoDTO.setDiscountAmount(NumberUtil.mul(ratio, procRelationRebateResultBO.getRebateAmount()).setScale(2, BigDecimal.ROUND_HALF_UP));
        }

        // 最后做负数校验
        if (CompareUtil.compare(agreementInfoDTO.getDiscountAmount(), BigDecimal.ZERO) < 0) {

            agreementInfoDTO.setDiscountAmount(BigDecimal.ZERO);
        }

        return agreementInfoDTO;
    }


    /**
     * 获取商品已经分摊的优惠金额
     *
     * @param orderDetailCashDiscountInfoList
     * @param goodsId
     * @param agreementId
     * @return
     */
    private BigDecimal getHashShareDiscount(List<OrderDetailCashDiscountInfoDTO> orderDetailCashDiscountInfoList, Long goodsId, Long agreementId) {

        List<OrderDetailCashDiscountInfoDTO> goodsDetailCashDiscountList = orderDetailCashDiscountInfoList.stream().filter(t -> goodsId.equals(t.getGoodsId())).collect(Collectors.toList());

        if (CollectionUtil.isEmpty(goodsDetailCashDiscountList)) {

            return BigDecimal.ZERO;
        }

        BigDecimal shareDiscount = BigDecimal.ZERO;

        for (OrderDetailCashDiscountInfoDTO discountInfoDTO : goodsDetailCashDiscountList) {
            BigDecimal agreementDiscount = discountInfoDTO.getCashDiscountAgreementInfoList().stream().filter(t -> t.getAgreementId().equals(agreementId)).map(CashDiscountAgreementInfoDTO::getDiscountAmount).reduce(BigDecimal::add).get();
            shareDiscount = NumberUtil.add(shareDiscount, agreementDiscount);
        }
        return shareDiscount;
    }


    /**
     * 查询商业建采参数
     *
     * @param goodDetailMap
     * @param buyerEid
     * @param distributorEid
     * @return
     */
    private CalculateProcRelationRebateRequest buildCalculateRequest(Map<Long, List<CreateOrderDetailRequest>> goodDetailMap, Long buyerEid, Long distributorEid) {

        List<CalculateProcRelationRebateRequest.CalculateProcRelationGoodsRequest> goodsRequestList = goodDetailMap.entrySet().stream().map(t -> {

            CalculateProcRelationRebateRequest.CalculateProcRelationGoodsRequest relationGoodsRequest = new CalculateProcRelationRebateRequest.CalculateProcRelationGoodsRequest();
            relationGoodsRequest.setGoodsId(t.getKey());
            relationGoodsRequest.setGoodsQuantity(t.getValue().stream().collect(Collectors.summingLong(CreateOrderDetailRequest::getGoodsQuantity)));
            relationGoodsRequest.setGoodsAmount(t.getValue().stream().map(CreateOrderDetailRequest::getGoodsAmount).reduce(BigDecimal::add).get());

            return relationGoodsRequest;

        }).collect(Collectors.toList());


        CalculateProcRelationRebateRequest request = new CalculateProcRelationRebateRequest();
        request.setBuyerEid(buyerEid);
        request.setSellerEid(distributorEid);
        request.setGoodsList(goodsRequestList);

        return request;
    }
}
