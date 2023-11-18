package com.yiling.mall.order.handler;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.mall.order.bo.CalOrderDiscountContextBO;
import com.yiling.mall.order.bo.SplitOrderEnum;
import com.yiling.marketing.presale.dto.PresaleActivityGoodsDTO;
import com.yiling.order.order.dto.request.CreateOrderDetailRequest;
import com.yiling.order.order.dto.request.CreateOrderRequest;
import com.yiling.order.order.enums.OrderErrorCode;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.order.order.enums.PreSaleActivityTypeEnum;
import com.yiling.order.order.enums.PromotionActivityTypeEnum;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import lombok.extern.slf4j.Slf4j;

/** 计算预售优惠金额
 * @author zhigang.guo
 * @date: 2022/12/2
 */
@Component
@Order(4)
@Slf4j
public class PreSaleDiscountProcessHandler extends AbstractDiscountProcessHandler implements DiscountProcessHandler {

    @Override
    protected void processDiscount(CalOrderDiscountContextBO discountContextBo) {

        if (OrderTypeEnum.B2B != discountContextBo.getOrderTypeEnum()) {

            return;
        }

        List<CreateOrderRequest> createOrderRequestList = discountContextBo.getCreateOrderRequestList();
        Map<String, PresaleActivityGoodsDTO> preSaleActivityGoodsDTOMap = discountContextBo.getPreSaleActivityGoodsDTOMap();
        // 获取预售订单集合
        List<CreateOrderRequest> preSaleOrderList = createOrderRequestList.stream().filter(createOrderRequest -> SplitOrderEnum.PRESALE.name() == createOrderRequest.getSplitOrderType()).collect(Collectors.toList());

        if (CollectionUtil.isEmpty(preSaleOrderList) || MapUtil.isEmpty(preSaleActivityGoodsDTOMap)) {

            return ;
        }

        // 计算预售优惠金额
        if (log.isDebugEnabled()) {

            log.debug("计算订单预售优惠金额,参数:{}",discountContextBo);
        }

        for (CreateOrderRequest createOrderRequest : preSaleOrderList) {
            // 订单预售定金金额
            BigDecimal orderDepositAmount = BigDecimal.ZERO;

            for (CreateOrderDetailRequest createOrderDetailRequest : createOrderRequest.getOrderDetailList()) {

                if (PromotionActivityTypeEnum.PRESALE != PromotionActivityTypeEnum.getByCode(createOrderDetailRequest.getPromotionActivityType())) {
                    continue;
                }

                PresaleActivityGoodsDTO presaleActivityGoodsDTO = preSaleActivityGoodsDTOMap.get(createOrderDetailRequest.getPromotionActivityId() + Constants.SEPARATOR_MIDDLELINE + createOrderDetailRequest.getDistributorGoodsId());
                if (presaleActivityGoodsDTO == null) {

                    throw new RuntimeException("预售商品未找到预售商品明细!");
                }
                // 商品预售优惠金额
                BigDecimal preSaleDiscountAmount = this.calculateGoodsPreSaleDiscountAmount(createOrderDetailRequest, presaleActivityGoodsDTO);
                // 定金金额
                BigDecimal depositAmount = this.calculateGoodsDepositAmount(createOrderDetailRequest, presaleActivityGoodsDTO);
                orderDepositAmount = NumberUtil.add(orderDepositAmount, depositAmount);
                // 订单明细预售优惠金额
                createOrderDetailRequest.setPresaleDiscountAmount(preSaleDiscountAmount);
                // 设置定金小计
                createOrderDetailRequest.setDepositAmount(depositAmount);
            }

            // 设置订单预售优惠金额
            createOrderRequest.setPresaleDiscountAmount(createOrderRequest.getOrderDetailList().stream().map(t -> t.getPresaleDiscountAmount()).reduce(BigDecimal::add).get());
            // 设置订单实际支付金额
            createOrderRequest.setPaymentAmount(NumberUtil.sub(createOrderRequest.getPaymentAmount(), createOrderRequest.getPresaleDiscountAmount()));
            // 设置订单本金金额
            createOrderRequest.getCreatePresaleOrderRequest().setDepositAmount(orderDepositAmount);
            // 设置订单尾款金额
            createOrderRequest.getCreatePresaleOrderRequest().setBalanceAmount(NumberUtil.sub(createOrderRequest.getPaymentAmount(), orderDepositAmount));

            // 校验订单金额是否发生异常
            if (CompareUtil.compare(orderDepositAmount, BigDecimal.ZERO) < 0 || CompareUtil.compare(createOrderRequest.getCreatePresaleOrderRequest().getBalanceAmount(), BigDecimal.ZERO) < 0) {

                log.error("拆单订单金额异常,出现零元订单");
                throw new BusinessException(OrderErrorCode.ORDER_MONEY_ERROR);
            }

        }

    }


    /**
     * 计算每个明细的商品优惠金额
     * @param createOrderDetailRequest
     * @param presaleActivityGoodsDTO
     * @return
     */
    private BigDecimal calculateGoodsPreSaleDiscountAmount(CreateOrderDetailRequest createOrderDetailRequest, PresaleActivityGoodsDTO presaleActivityGoodsDTO){

        // 非预售商品无需计算,预售优惠金额以及定金金额
        if (PromotionActivityTypeEnum.PRESALE != PromotionActivityTypeEnum.getByCode(createOrderDetailRequest.getPromotionActivityType())) {

            return BigDecimal.ZERO;
        }

        // 全款预售无优惠金额
        if (PreSaleActivityTypeEnum.FULL == PreSaleActivityTypeEnum.getByCode(presaleActivityGoodsDTO.getPresaleType())) {

            return BigDecimal.ZERO;
        }

        // 商品没有设置具体的优惠金额
        if (CompareUtil.compare(0,presaleActivityGoodsDTO.getGoodsPresaleType()) == 0) {

            return BigDecimal.ZERO;
        }

        // 尾款立减
        if (CompareUtil.compare(2,presaleActivityGoodsDTO.getGoodsPresaleType()) == 0) {

            return NumberUtil.round(NumberUtil.mul(presaleActivityGoodsDTO.getFinalPayDiscountAmount(),createOrderDetailRequest.getGoodsQuantity()),2);
        }

        // 定金膨胀
        if (CompareUtil.compare(1,presaleActivityGoodsDTO.getGoodsPresaleType()) == 0) {

            // 定金金额
            BigDecimal depositAmount = NumberUtil.round(NumberUtil.mul(createOrderDetailRequest.getGoodsAmount(),NumberUtil.div(presaleActivityGoodsDTO.getDepositRatio(),100)),2);
            // 定金膨胀金额
            BigDecimal expansionAmount = NumberUtil.round(NumberUtil.mul(depositAmount,presaleActivityGoodsDTO.getExpansionMultiplier()),2);

            // 实际优惠金额
            BigDecimal diffDiscountAmount = NumberUtil.sub(expansionAmount,depositAmount);

            if (log.isDebugEnabled()) {
                log.debug("商品{},定金膨胀计算值定金为:{},定金膨胀金额:{},实际优惠金额{}",createOrderDetailRequest.getDistributorGoodsId(),depositAmount,expansionAmount,diffDiscountAmount);
            }

            return diffDiscountAmount;
        }

        return BigDecimal.ZERO;
    }


    /**
     * 计算每个明细的商品定金金额
     * @param createOrderDetailRequest
     * @param presaleActivityGoodsDTO
     * @return
     */
    private BigDecimal calculateGoodsDepositAmount(CreateOrderDetailRequest createOrderDetailRequest, PresaleActivityGoodsDTO presaleActivityGoodsDTO){

        // 非预售商品无需计算,预售优惠金额以及定金金额
        if (PromotionActivityTypeEnum.PRESALE != PromotionActivityTypeEnum.getByCode(createOrderDetailRequest.getPromotionActivityType())) {

            return BigDecimal.ZERO;
        }

        // 全款预售
        if (PreSaleActivityTypeEnum.FULL == PreSaleActivityTypeEnum.getByCode(presaleActivityGoodsDTO.getPresaleType())) {

            return BigDecimal.ZERO;
        }

        // 定金金额
        BigDecimal depositAmount = NumberUtil.round(NumberUtil.mul(createOrderDetailRequest.getGoodsAmount(),NumberUtil.div(presaleActivityGoodsDTO.getDepositRatio(),100)),2);

        return depositAmount;
    }
}
