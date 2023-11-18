package com.yiling.settlement.b2b.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.common.enums.CouponBearTypeEnum;
import com.yiling.marketing.presale.api.PresaleActivityApi;
import com.yiling.marketing.presale.dto.PresaleActivityDTO;
import com.yiling.marketing.promotion.api.PromotionActivityApi;
import com.yiling.marketing.promotion.dto.PromotionActivityDTO;
import com.yiling.marketing.promotion.dto.request.PromotionJudgeGoodsRequest;
import com.yiling.marketing.promotion.dto.request.PromotionSettleJudgeRequest;
import com.yiling.marketing.promotion.dto.request.PromotionSettleJudgeRequestItem;
import com.yiling.order.order.api.OrderB2BApi;
import com.yiling.order.order.api.OrderGiftApi;
import com.yiling.order.order.api.OrderPromotionActivityApi;
import com.yiling.order.order.api.PresaleOrderApi;
import com.yiling.order.order.dto.B2BSettlementDTO;
import com.yiling.order.order.dto.B2BSettlementDetailDTO;
import com.yiling.order.order.dto.OrderGiftDTO;
import com.yiling.order.order.dto.OrderPromotionActivityDTO;
import com.yiling.order.order.dto.PresaleOrderDTO;
import com.yiling.order.order.enums.OrderStatusEnum;
import com.yiling.order.order.enums.PaymentMethodEnum;
import com.yiling.order.order.enums.PaymentStatusEnum;
import com.yiling.order.order.enums.PromotionActivityTypeEnum;
import com.yiling.settlement.b2b.bo.OrderGiftBO;
import com.yiling.settlement.b2b.dao.SettlementOrderSyncMapper;
import com.yiling.settlement.b2b.dto.SettlementOrderSyncDTO;
import com.yiling.settlement.b2b.dto.request.QuerySettOrderDataPageListRequest;
import com.yiling.settlement.b2b.dto.request.UpdateOrderSyncSettStatusRequest;
import com.yiling.settlement.b2b.entity.SettlementOrderSyncDO;
import com.yiling.settlement.b2b.enums.OrderSyncDataStatusEnum;
import com.yiling.settlement.b2b.enums.OrderSyncDefaultStatusEnum;
import com.yiling.settlement.b2b.enums.OrderSyncStatusEnum;
import com.yiling.settlement.b2b.service.SettlementOrderSyncService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dexi.yao
 * @date 2022-04-07
 */
@Slf4j
@Service
public class SettlementOrderSyncServiceImpl extends BaseServiceImpl<SettlementOrderSyncMapper, SettlementOrderSyncDO> implements SettlementOrderSyncService {

    @DubboReference
    OrderB2BApi orderB2BApi;
    @DubboReference
    OrderGiftApi orderGiftApi;
    @DubboReference
    PromotionActivityApi promotionActivityApi;
    @DubboReference
    PresaleOrderApi presaleOrderApi;
    @DubboReference
    PresaleActivityApi presaleActivityApi;
    @DubboReference
    OrderPromotionActivityApi orderPromotionActivityApi;

    @Override
    public Boolean createSettOrderSync(String orderCode) {
        if (StrUtil.isBlank(orderCode)) {
            return Boolean.TRUE;
        }
        SettlementOrderSyncDTO settlementOrderSyncDTO = querySettOrderSyncByOrderCode(orderCode);
        //如果订单数据已存在
        if (ObjectUtil.isNotNull(settlementOrderSyncDTO)) {
            return Boolean.TRUE;
        }

        B2BSettlementDTO orderDTO = orderB2BApi.getB2bSettlementOne(orderCode);
        if (ObjectUtil.isNull(orderDTO)) {
            log.error("收货时根据订单号查询的订单的结算单数据为空，orderCode={}", orderCode);
            //存盘-同步状态记为失败
            SettlementOrderSyncDO var = new SettlementOrderSyncDO();
            var.setOrderNo(orderCode);
            var.setStatus(OrderSyncStatusEnum.FAIL.getCode());
            var.setDataStatus(OrderSyncDataStatusEnum.UNAUTHORIZED.getCode());
            return save(var);

        }
        //生成订单数据
        SettlementOrderSyncDO orderDataDO = generateSettOrderData(orderDTO);
        //        //订单结算金额为0的忽略
        //        if (ObjectUtil.isNull(orderDataDO)){
        //            return Boolean.TRUE;
        //        }

        return save(orderDataDO);
    }

    @Override
    public Boolean createSettOrderCancelSync(String orderCode) {
        if (StrUtil.isBlank(orderCode)) {
            return Boolean.TRUE;
        }
        SettlementOrderSyncDTO dbOrder = querySettOrderSyncByOrderCode(orderCode);
        //如果订单数据已存在
        if (ObjectUtil.isNotNull(dbOrder)) {
            return Boolean.TRUE;
        }

        B2BSettlementDTO orderDTO = orderB2BApi.getB2bSettlementOne(orderCode);
        if (ObjectUtil.isNull(orderDTO)) {
            log.error("收货时根据订单号查询的订单的结算单数据为空，orderCode={}", orderCode);
            //存盘-同步状态记为失败
            SettlementOrderSyncDO var = new SettlementOrderSyncDO();
            var.setOrderNo(orderCode);
            var.setStatus(OrderSyncStatusEnum.FAIL.getCode());
            var.setDataStatus(OrderSyncDataStatusEnum.UNAUTHORIZED.getCode());
            return save(var);

        }
        //生成订单数据
        SettlementOrderSyncDO orderDataDO = generateSettOrderCancelData(orderDTO);

        return save(orderDataDO);
    }

    @Override
    public void syncSettOrderSyncByIds(List<Long> ids) {
        log.info("同步结算单的订单数据，参数={}", ids);
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        List<SettlementOrderSyncDO> updateList = ListUtil.toList();
        List<Long> deleteList = ListUtil.toList();

        ids.forEach(id -> {
            SettlementOrderSyncDO dataDO = getById(id);

            if (ObjectUtil.isNull(dataDO)) {
                log.error("手动同步结算单的订单数据时根据订单号查询的b2b_settlement_order_data表数据为空，id={}", id);
                return;
            }
            //如果订单同步成功
            if (ObjectUtil.equal(dataDO.getStatus(), OrderSyncStatusEnum.SUCCESS.getCode())) {
                return;
            }

            B2BSettlementDTO orderDTO = orderB2BApi.getB2bSettlementOne(dataDO.getOrderNo());
            if (ObjectUtil.isNull(orderDTO)) {
                log.error("手动同步结算单的订单数据时根据订单号查询的订单的结算单数据为空，orderCode={}", dataDO.getOrderNo());
                return;
            }
            //生成订单数据
            SettlementOrderSyncDO var = generateSettOrderData(orderDTO);
            //            if (ObjectUtil.isNotNull(var)){
            var.setId(dataDO.getId());
            updateList.add(var);
            //            }else {
            //                deleteList.add(dataDO.getId());
            //            }
        });
        if (CollUtil.isNotEmpty(updateList)) {
            boolean isSuccess = updateBatchById(updateList);
            if (isSuccess) {
                log.info("手动同步结算单的订单数据成功，id={}", updateList.stream().map(SettlementOrderSyncDO::getId).collect(Collectors.toList()));
            } else {
                log.info("手动同步结算单的订单数据失败，id={}", updateList.stream().map(SettlementOrderSyncDO::getId).collect(Collectors.toList()));
            }
        }
        //        //忽略金额为0的订单
        //        if (CollUtil.isNotEmpty(deleteList)){
        //            LambdaQueryWrapper<SettlementOrderSyncDO> wrapper = Wrappers.lambdaQuery();
        //            wrapper.in(SettlementOrderSyncDO::getId,deleteList);
        //            SettlementOrderSyncDO deleteDO=new SettlementOrderSyncDO();
        //            deleteDO.setExplication("该订单结算金额为0，忽略该订单");
        //
        //            int rows = batchDeleteWithFill(deleteDO, wrapper);
        //            if (rows==0){
        //                log.error("删除结算金额为0的订单失败，参数={}",deleteList);
        //            }
        //        }
    }

    @Override
    public SettlementOrderSyncDTO querySettOrderSyncByOrderCode(String orderCode) {
        if (StrUtil.isBlank(orderCode)) {
            return null;
        }
        LambdaQueryWrapper<SettlementOrderSyncDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SettlementOrderSyncDO::getOrderNo, orderCode);
        SettlementOrderSyncDO orderDataDO = getOne(wrapper);

        return PojoUtils.map(orderDataDO, SettlementOrderSyncDTO.class);
    }

    @Override
    public Boolean cashRepaymentNotifySett(String orderCode) {
        log.info("通知结算单还款，orderCode={}", orderCode);
        //查询订单数据
        if (StrUtil.isBlank(orderCode)) {
            return Boolean.TRUE;
        }
        SettlementOrderSyncDTO orderDataDTO = querySettOrderSyncByOrderCode(orderCode);
        //不存在则新增一条记录，同步状态置为失败
        if (ObjectUtil.isNull(orderDataDTO)) {
            log.error("通知结算单还款时根据订单号查询的订单的结算单数据为空，orderCode={}", orderCode);
            //存盘-同步状态记为失败
            SettlementOrderSyncDO var = new SettlementOrderSyncDO();
            var.setOrderNo(orderCode);
            var.setStatus(OrderSyncStatusEnum.FAIL.getCode());
            var.setDataStatus(OrderSyncDataStatusEnum.UNAUTHORIZED.getCode());
            return save(var);
        }
        //如果同步状态为失败，则忽略，等待手动同步
        if (ObjectUtil.equal(orderDataDTO.getStatus(), OrderSyncStatusEnum.FAIL.getCode())) {
            return Boolean.TRUE;
        }
        //如果生成状态为可生成且订单已支付，则忽略
        if (ObjectUtil.equal(orderDataDTO.getGenerateStatus(), OrderSyncDataStatusEnum.AUTHORIZED.getCode()) && ObjectUtil.equal(orderDataDTO.getPaymentStatus(), PaymentStatusEnum.PAID.getCode())) {
            return Boolean.TRUE;
        }
        SettlementOrderSyncDO data = new SettlementOrderSyncDO();
        data.setId(orderDataDTO.getId());
        //更新可生成状态
        data.setPaymentStatus(PaymentStatusEnum.PAID.getCode());
        data.setDataStatus(OrderSyncDataStatusEnum.AUTHORIZED.getCode());
        boolean isSuccess = updateById(data);
        if (!isSuccess) {
            log.error("通知结算单还款时更新订单为可生成结算单失败，请手动同步，orderCode={}，orderData={}", orderCode, orderDataDTO);
        }
        return isSuccess;
    }

    @Override
    public Page<SettlementOrderSyncDTO> querySettOrderSyncPageList(QuerySettOrderDataPageListRequest request) {
        LambdaQueryWrapper<SettlementOrderSyncDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ObjectUtil.isNotNull(request.getDefaultStatus()) && ObjectUtil.notEqual(request.getDefaultStatus(), 0), SettlementOrderSyncDO::getDefaultStatus, request.getDefaultStatus());
        wrapper.in(SettlementOrderSyncDO::getPaymentMethod, PaymentMethodEnum.PAYMENT_DAYS.getCode(), PaymentMethodEnum.ONLINE.getCode(), PaymentMethodEnum.OFFLINE.getCode());
        wrapper.in(CollUtil.isNotEmpty(request.getOrderStatusList()), SettlementOrderSyncDO::getOrderStatus, request.getOrderStatusList());
        if (request.getStartReceiveTime() != null) {
            wrapper.ge(SettlementOrderSyncDO::getReceiveTime, request.getStartReceiveTime());
        }
        if (request.getEndReceiveTime() != null) {
            wrapper.le(SettlementOrderSyncDO::getReceiveTime, request.getEndReceiveTime());
        }
        if (request.getStartCancelTime() != null) {
            wrapper.ge(SettlementOrderSyncDO::getCancelTime, request.getStartCancelTime());
        }
        if (request.getEndCancelTime() != null) {
            wrapper.le(SettlementOrderSyncDO::getCancelTime, request.getEndCancelTime());
        }
        if (request.getGenerateStatus() != null) {
            wrapper.eq(SettlementOrderSyncDO::getGenerateStatus, request.getGenerateStatus());
        }
        if (request.getDataStatus() != null) {
            wrapper.eq(SettlementOrderSyncDO::getDataStatus, request.getDataStatus());
        }
        if (request.getSyncStatus() != null) {
            wrapper.eq(SettlementOrderSyncDO::getStatus, request.getSyncStatus());
        }
        if (request.getSellerEid() != null) {
            wrapper.eq(SettlementOrderSyncDO::getSellerEid, request.getSellerEid());
        }
        Page<SettlementOrderSyncDO> page = page(request.getPage(), wrapper);
        return PojoUtils.map(page, SettlementOrderSyncDTO.class);
    }

    @Override
    public Page<SettlementOrderSyncDTO> pageListBySyncStatus(QuerySettOrderDataPageListRequest request) {
        LambdaQueryWrapper<SettlementOrderSyncDO> wrapper = Wrappers.lambdaQuery();
        if (request.getSyncStatus() != null) {
            wrapper.eq(SettlementOrderSyncDO::getStatus, request.getSyncStatus());
        }
        Page<SettlementOrderSyncDO> page = page(request.getPage(), wrapper);
        return PojoUtils.map(page, SettlementOrderSyncDTO.class);
    }

    @Override
    public Boolean updateOrderSyncSettStatus(UpdateOrderSyncSettStatusRequest request) {
        List<SettlementOrderSyncDO> syncList = PojoUtils.map(request.getSettStatusList(), SettlementOrderSyncDO.class);
        return updateBatchById(syncList);
    }

    /**
     * 生成SettlementOrderDataDO
     *
     * @param order
     * @return
     */
    private SettlementOrderSyncDO generateSettOrderData(B2BSettlementDTO order) {

        SettlementOrderSyncDO orderDataDO = new SettlementOrderSyncDO();
        PojoUtils.map(order, orderDataDO);
        orderDataDO.setOrderId(order.getId());
        //初始化付款金额为0，避免因属性copy导致的字段值不正确
        orderDataDO.setPaymentAmount(BigDecimal.ZERO);
        orderDataDO.setRefundPaymentAmount(BigDecimal.ZERO);
        orderDataDO.setCouponAmount(BigDecimal.ZERO);
        orderDataDO.setRefundCouponAmount(BigDecimal.ZERO);
        orderDataDO.setPromotionAmount(BigDecimal.ZERO);
        orderDataDO.setRefundPromotionAmount(BigDecimal.ZERO);
        orderDataDO.setGiftAmount(BigDecimal.ZERO);
        orderDataDO.setRefundGiftAmount(BigDecimal.ZERO);
        orderDataDO.setDataStatus(OrderSyncDataStatusEnum.AUTHORIZED.getCode());
        orderDataDO.setStatus(OrderSyncStatusEnum.SUCCESS.getCode());
        //如果订单为账期支付且没有还款则设置此订单为不可生成结算单状态
        if (ObjectUtil.equal(order.getPaymentMethod(), PaymentMethodEnum.PAYMENT_DAYS.getCode().intValue()) && ObjectUtil.equal(order.getPaymentStatus(), PaymentStatusEnum.UNPAID.getCode())) {
            orderDataDO.setDataStatus(OrderSyncDataStatusEnum.UNAUTHORIZED.getCode());
        }

        //计算货款金额
        //货款只结算除线下支付以外的且支付状态为已支付订单，线下支付订单只结算促销部分
        if (ObjectUtil.notEqual(order.getPaymentMethod(), PaymentMethodEnum.OFFLINE.getCode().intValue())) {
            orderDataDO.setPaymentAmount(order.getPaymentAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
            BigDecimal paymentRefundAmount;
            //实付退款金额=return_amount - return_coupon_discount_amount - returnPlatformCouponDiscountAmount - returnPresaleDiscountAmount - returnPlatformPaymentDiscountAmount - returnShopPaymentDiscountAmount
            paymentRefundAmount = order.getReturnAmount().subtract(order.getReturnCouponDiscountAmount()).subtract(order.getReturnPlatformCouponDiscountAmount()).subtract(order.getReturnPresaleDiscountAmount()).subtract(order.getReturnPlatformPaymentDiscountAmount()).subtract(order.getReturnShopPaymentDiscountAmount()).setScale(2, BigDecimal.ROUND_HALF_UP);
            orderDataDO.setRefundPaymentAmount(paymentRefundAmount);
        }

        //开计算促销部分
        //查询orderDetailChange计算平台补贴金额和平台补贴退款金额
        List<B2BSettlementDetailDTO> orderDetailChangeList = orderB2BApi.listSettleOrderDetailByOrderId(order.getId());
        //计算优惠券&秒杀&特价促销金额&组合包促销金额&预售促销
        calculateCouponAndPromotionSale(orderDataDO, orderDetailChangeList);
        //计算赠品金额
        //订单下的赠品
        List<OrderGiftBO> giftOrderList = getGiftInfo(order.getId(), order.getFullGiftPromotionActivitys());
        //如果该订单有赠品
        if (CollUtil.isNotEmpty(giftOrderList)) {
            calculateGiftSale(orderDataDO, orderDetailChangeList, giftOrderList);
        } else {
            orderDataDO.setGiftAmount(BigDecimal.ZERO);
            orderDataDO.setRefundGiftAmount(BigDecimal.ZERO);
        }

        return orderDataDO;
    }

    /**
     * 生成SettlementOrderCancelDO
     *
     * @param order
     * @return
     */
    private SettlementOrderSyncDO generateSettOrderCancelData(B2BSettlementDTO order) {


        SettlementOrderSyncDO orderDataDO = new SettlementOrderSyncDO();
        PojoUtils.map(order, orderDataDO);
        orderDataDO.setPresaleDiscountAmount(BigDecimal.ZERO);
        orderDataDO.setRefundPreAmount(BigDecimal.ZERO);
        orderDataDO.setPlatformCouponDiscountAmount(BigDecimal.ZERO);
        orderDataDO.setShopCouponDiscountAmount(BigDecimal.ZERO);
        orderDataDO.setReturnPlatformCouponDiscountAmount(BigDecimal.ZERO);
        orderDataDO.setReturnCouponDiscountAmount(BigDecimal.ZERO);


        orderDataDO.setDefaultStatus(OrderSyncDefaultStatusEnum.ORDER_DEFAULT.getCode());
        orderDataDO.setOrderId(order.getId());
        orderDataDO.setCancelTime(new Date());
        //查询订单定金
        PresaleOrderDTO orderInfo = presaleOrderApi.getOrderInfo(order.getId());
        orderDataDO.setPresaleDepositAmount(orderInfo.getDepositAmount());

        orderDataDO.setOrderCreateTime(order.getCreateTime());

        orderDataDO.setDataStatus(OrderSyncDataStatusEnum.AUTHORIZED.getCode());
        orderDataDO.setStatus(OrderSyncStatusEnum.SUCCESS.getCode());

        return orderDataDO;
    }

    /**
     * 查询赠品信息
     *
     * @param orderId
     * @param request
     * @return
     */
    private List<OrderGiftBO> getGiftInfo(Long orderId, List<OrderPromotionActivityDTO> request) {
        if (CollUtil.isEmpty(request)) {
            return ListUtil.toList();
        }
        List<OrderPromotionActivityDTO> list = request.stream().filter(e -> ObjectUtil.equal(e.getActivityType(), CouponBearTypeEnum.PLATFORM.getCode())).collect(Collectors.toList());
        List<OrderGiftBO> result = PojoUtils.map(list, OrderGiftBO.class);
        //查询赠品价格 key=activityId
        Map<Long, OrderGiftDTO> giftMap = orderGiftApi.listByOrderIds(ListUtil.toList(orderId)).stream().collect(Collectors.groupingBy(OrderGiftDTO::getOrderId)).get(orderId).stream().collect(Collectors.toMap(OrderGiftDTO::getPromotionActivityId, e -> e));
        //补全赠品价格
        result.forEach(orderGiftBO -> {
            OrderGiftDTO giftDTO = giftMap.get(orderGiftBO.getActivityId());
            orderGiftBO.setGoodsGiftId(giftDTO.getGoodsGiftId());
            orderGiftBO.setPrice(giftDTO.getPrice());
            orderGiftBO.setPromotionLimitId(giftDTO.getPromotionLimitId());
        });

        return result;
    }

    /**
     * 计算优惠券&秒杀&特价促销金额&组合包促销金额&预售促销金额
     *
     * @param orderDataDO
     * @param orderDetailChangeList
     */
    private void calculateCouponAndPromotionSale(SettlementOrderSyncDO orderDataDO, List<B2BSettlementDetailDTO> orderDetailChangeList) {
        //优惠券平台抵扣金额
        AtomicReference<BigDecimal> platformSubsidyAmount = new AtomicReference<>();
        platformSubsidyAmount.set(BigDecimal.ZERO);
        //退回优惠券平台应抵扣金额
        AtomicReference<BigDecimal> returnPlatformSubsidyAmount = new AtomicReference<>();
        returnPlatformSubsidyAmount.set(BigDecimal.ZERO);
        //秒杀&特价平台抵扣金额
        AtomicReference<BigDecimal> platformSpecialAmount = new AtomicReference<>();
        platformSpecialAmount.set(BigDecimal.ZERO);
        //退回的秒杀&特价平台抵扣金额
        AtomicReference<BigDecimal> returnPlatformSpecialAmount = new AtomicReference<>();
        returnPlatformSpecialAmount.set(BigDecimal.ZERO);
        //组合包的抵扣金额
        AtomicReference<BigDecimal> comPacAmount = new AtomicReference<>();
        comPacAmount.set(BigDecimal.ZERO);
        //退回的组合包抵扣金额
        AtomicReference<BigDecimal> returnComPacAmount = new AtomicReference<>();
        returnComPacAmount.set(BigDecimal.ZERO);
        //预售优惠金额
        AtomicReference<BigDecimal> preSaleAmount = new AtomicReference<>();
        preSaleAmount.set(BigDecimal.ZERO);
        //退回的预售优惠扣金额
        AtomicReference<BigDecimal> returnPreSaleAmount = new AtomicReference<>();
        returnPreSaleAmount.set(BigDecimal.ZERO);
        //支付促销金额
        AtomicReference<BigDecimal> paySaleAmount = new AtomicReference<>();
        paySaleAmount.set(BigDecimal.ZERO);
        //退回的支付促销金额
        AtomicReference<BigDecimal> returnPaySaleAmount = new AtomicReference<>();
        returnPaySaleAmount.set(BigDecimal.ZERO);

        //查询秒杀&特价活动&预售的平台分摊百分比
        List<Long> promotionActivityIds = orderDetailChangeList.stream().filter(e -> ObjectUtil.equal(e.getPromotionActivityType(), PromotionActivityTypeEnum.FULL_GIFT.getCode()) || ObjectUtil.equal(e.getPromotionActivityType(), PromotionActivityTypeEnum.SPECIAL.getCode()) || ObjectUtil.equal(e.getPromotionActivityType(), PromotionActivityTypeEnum.LIMIT.getCode()) || ObjectUtil.equal(e.getPromotionActivityType(), PromotionActivityTypeEnum.COMBINATION.getCode())).map(B2BSettlementDetailDTO::getPromotionActivityId).distinct().collect(Collectors.toList());
        Map<Long, PromotionActivityDTO> promotionActivityDTOMap;
        if (CollUtil.isNotEmpty(promotionActivityIds)) {
            promotionActivityDTOMap = promotionActivityApi.batchQueryByIdList(promotionActivityIds).stream().collect(Collectors.toMap(PromotionActivityDTO::getId, e -> e));
        } else {
            promotionActivityDTOMap = MapUtil.newHashMap();
        }
        //查询订单的活动信息
        Map<Long, List<OrderPromotionActivityDTO>> orderActivityInfoMap;
        List<Long> orderIds = orderDetailChangeList.stream().map(B2BSettlementDetailDTO::getOrderId).distinct().collect(Collectors.toList());
        if (CollUtil.isNotEmpty(orderIds)) {
            orderActivityInfoMap = orderPromotionActivityApi.listByOrderIds(orderIds).stream().map(e->{e.setActivityPlatformPercent(e.getActivityPlatformPercent().divide(BigDecimal.TEN).divide(BigDecimal.TEN)); return e;}).collect(Collectors.groupingBy(OrderPromotionActivityDTO::getOrderId));
        } else {
            orderActivityInfoMap = MapUtil.newHashMap();
        }
        List<Long> preSaleActivityIds = orderDetailChangeList.stream().filter(e -> ObjectUtil.equal(e.getPromotionActivityType(), PromotionActivityTypeEnum.PRESALE.getCode())).map(B2BSettlementDetailDTO::getPromotionActivityId).distinct().collect(Collectors.toList());
        Map<Long, PresaleActivityDTO> preSaleActivityDTOMap;
        if (CollUtil.isNotEmpty(preSaleActivityIds)) {
            preSaleActivityDTOMap = presaleActivityApi.batchQueryByIdList(preSaleActivityIds);
        } else {
            preSaleActivityDTOMap = MapUtil.newHashMap();
        }

        orderDetailChangeList.forEach(orderDetail -> {
            BigDecimal currentPlatformAmount = platformSubsidyAmount.get();
            //计算该条订单明细的平台券+商家券的平台补贴金额
            //平台券补贴金额
            BigDecimal subPlatformAmount = orderDetail.getPlatformCouponDiscountAmount().multiply(orderDetail.getPlatformRatio() == null ? BigDecimal.ZERO : orderDetail.getPlatformRatio().divide(BigDecimal.TEN).divide(BigDecimal.TEN));
            //商家券补贴金额
            BigDecimal subBusinessAmount = orderDetail.getCouponDiscountAmount().multiply(orderDetail.getShopPlatformRatio() == null ? BigDecimal.ZERO : orderDetail.getShopPlatformRatio().divide(BigDecimal.TEN).divide(BigDecimal.TEN));
            //平台补贴金额=平台券补贴金额+商家券补贴金额
            BigDecimal totalPlatformAmount = subPlatformAmount.add(subBusinessAmount);
            //当前平台补贴金额=currentPlatformAmount+totalPlatformAmount
            currentPlatformAmount = currentPlatformAmount.add(totalPlatformAmount).setScale(2, BigDecimal.ROUND_HALF_UP);
            platformSubsidyAmount.set(currentPlatformAmount);

            //计算该条订单明细的平台券+商家券的平台补贴退款金额
            BigDecimal currentReturnPlatformAmount = returnPlatformSubsidyAmount.get();
            //商家券的退款金额
            BigDecimal subReturnPlatformAmount = orderDetail.getReturnCouponDiscountAmount().multiply(orderDetail.getShopPlatformRatio() == null ? BigDecimal.ZERO : orderDetail.getShopPlatformRatio().divide(BigDecimal.TEN).divide(BigDecimal.TEN));
            //平台券的退款金额
            BigDecimal subReturnBusinessAmount = orderDetail.getReturnPlatformCouponDiscountAmount().multiply(orderDetail.getPlatformRatio() == null ? BigDecimal.ZERO : orderDetail.getPlatformRatio().divide(BigDecimal.TEN).divide(BigDecimal.TEN));
            //该条明细的平台补贴退款金额=商家券的退款金额+平台券的退款金额
            BigDecimal totalReturnPlatformAmount = subReturnPlatformAmount.add(subReturnBusinessAmount);
            //当前退款的平台补贴金额=currentReturnPlatformAmount+该条明细的平台补贴退款金额
            currentReturnPlatformAmount = currentReturnPlatformAmount.add(totalReturnPlatformAmount).setScale(2, BigDecimal.ROUND_HALF_UP);
            returnPlatformSubsidyAmount.set(currentReturnPlatformAmount);

            //计算秒杀&特价金额
            //下单时秒杀&特价优惠金额
            if (ObjectUtil.equal(orderDetail.getPromotionActivityType(), PromotionActivityTypeEnum.LIMIT.getCode()) || ObjectUtil.equal(orderDetail.getPromotionActivityType(), PromotionActivityTypeEnum.SPECIAL.getCode())) {
                //如果商品的结算金额小于等于0 则忽略该商品
                if (BigDecimal.ZERO.compareTo(orderDetail.getPromotionSaleSubTotal()) == -1) {
                    PromotionActivityDTO activityDTO = promotionActivityDTOMap.get(orderDetail.getPromotionActivityId());
                    BigDecimal platformPercentage = activityDTO.getPlatformPercent().divide(BigDecimal.TEN).divide(BigDecimal.TEN);

                    BigDecimal specialAmount = platformSpecialAmount.get();
                    BigDecimal subSpecialAmount = orderDetail.getPromotionSaleSubTotal().multiply(platformPercentage).setScale(2, BigDecimal.ROUND_HALF_UP);
                    specialAmount = specialAmount.add(subSpecialAmount);
                    platformSpecialAmount.set(specialAmount);
                    //退款的秒杀&特价优惠金额
                    BigDecimal returnSpecialAmount = returnPlatformSpecialAmount.get();
                    BigDecimal subReturnSpecialAmount = orderDetail.getReturnPromotionSaleSubTotal().multiply(platformPercentage).setScale(2, BigDecimal.ROUND_HALF_UP);
                    returnSpecialAmount = returnSpecialAmount.add(subReturnSpecialAmount);
                    returnPlatformSpecialAmount.set(returnSpecialAmount);
                }
            }
            //计算组合包促销金额
            if (ObjectUtil.equal(orderDetail.getPromotionActivityType(), PromotionActivityTypeEnum.COMBINATION.getCode())) {
                //如果商品的结算金额小于等于0 则忽略该商品
                if (BigDecimal.ZERO.compareTo(orderDetail.getComPacAmount()) == -1) {
                    PromotionActivityDTO activityDTO = promotionActivityDTOMap.get(orderDetail.getPromotionActivityId());
                    BigDecimal platformPercentage = activityDTO.getPlatformPercent().divide(BigDecimal.TEN).divide(BigDecimal.TEN);

                    BigDecimal oldAmount = comPacAmount.get();
                    BigDecimal amount = orderDetail.getComPacAmount().multiply(platformPercentage).setScale(2, BigDecimal.ROUND_HALF_UP);
                    oldAmount = oldAmount.add(amount);
                    comPacAmount.set(oldAmount);
                    //退款的组合包促销金额
                    BigDecimal oldReturnAmount = returnComPacAmount.get();
                    BigDecimal returnAmount = orderDetail.getReturnComPacAmount().multiply(platformPercentage).setScale(2, BigDecimal.ROUND_HALF_UP);
                    oldReturnAmount = oldReturnAmount.add(returnAmount);
                    returnComPacAmount.set(oldReturnAmount);
                }
            }
            //计算预售促销金额
            if (ObjectUtil.equal(orderDetail.getPromotionActivityType(), PromotionActivityTypeEnum.PRESALE.getCode())) {
                //如果商品的结算金额小于等于0 则忽略该商品
                if (BigDecimal.ZERO.compareTo(orderDetail.getPresaleDiscountAmount()) == -1) {
                    PresaleActivityDTO activityDTO = preSaleActivityDTOMap.get(orderDetail.getPromotionActivityId());
                    BigDecimal platformPercentage;
                    if (ObjectUtil.equal(activityDTO.getBear(), CouponBearTypeEnum.BUSINESS.getCode())) {
                        platformPercentage = BigDecimal.ZERO;
                    } else {
                        platformPercentage = activityDTO.getPlatformRatio().divide(BigDecimal.TEN).divide(BigDecimal.TEN);
                    }

                    BigDecimal oldAmount = preSaleAmount.get();
                    BigDecimal amount = orderDetail.getPresaleDiscountAmount().multiply(platformPercentage).setScale(2, BigDecimal.ROUND_HALF_UP);
                    oldAmount = oldAmount.add(amount);
                    preSaleAmount.set(oldAmount);
                    //退款的组合包促销金额
                    BigDecimal oldReturnAmount = returnPreSaleAmount.get();
                    BigDecimal returnAmount = orderDetail.getReturnPresaleDiscountAmount().multiply(platformPercentage).setScale(2, BigDecimal.ROUND_HALF_UP);
                    oldReturnAmount = oldReturnAmount.add(returnAmount);
                    returnPreSaleAmount.set(oldReturnAmount);
                }
            }
            //计算支付促销金额
            //order_detail的promotion_activity_type字段，支付促销不会打标记的
            BigDecimal paymentDiscountAmount = orderDetail.getPaymentPlatformDiscountAmount().add(orderDetail.getPaymentShopDiscountAmount());
            //平台承担活动百分比
            BigDecimal payPlatformPercent = orderActivityInfoMap.getOrDefault(orderDetail.getOrderId(),ListUtil.toList()).stream().filter(e -> ObjectUtil.equal(e.getActivityType(), PromotionActivityTypeEnum.PAYMENT.getCode())).map(OrderPromotionActivityDTO::getActivityPlatformPercent).reduce(BigDecimal.ZERO, BigDecimal::add);

            //如果商品的支付结算金额大于0 且 平台承担百分比大于0 才计算支付促销金额
            if (BigDecimal.ZERO.compareTo(paymentDiscountAmount) == -1 && BigDecimal.ZERO.compareTo(payPlatformPercent) == -1) {

                Map<Integer, BigDecimal> sponsorPercentMap = orderActivityInfoMap.getOrDefault(orderDetail.getOrderId(),ListUtil.toList()).stream().filter(e -> ObjectUtil.equal(e.getActivityType(), PromotionActivityTypeEnum.PAYMENT.getCode())).collect(Collectors.toMap(OrderPromotionActivityDTO::getSponsorType, OrderPromotionActivityDTO::getActivityPlatformPercent));

                BigDecimal oldAmount = paySaleAmount.get();
                //平台承担支付促销金额=（平台承担金额*平台支付促销平台百分比） +（商家承担金额 * 商家支付促销平台承担百分比）
                BigDecimal amount = orderDetail.getPaymentPlatformDiscountAmount().multiply(sponsorPercentMap.getOrDefault(1,BigDecimal.ZERO)).add(orderDetail.getPaymentShopDiscountAmount().multiply(sponsorPercentMap.getOrDefault(2,BigDecimal.ZERO))).setScale(2, BigDecimal.ROUND_HALF_UP);
                oldAmount = oldAmount.add(amount);
                paySaleAmount.set(oldAmount);
                //退款的支付促销金额
                BigDecimal oldReturnAmount = returnPaySaleAmount.get();
                //平台承担支付促销退款金额=（退货商品的平台支付优惠金额*平台支付促销平台百分比） +（退货商品的商家支付优惠金额 * 商家支付促销平台承担百分比）
                BigDecimal returnAmount = orderDetail.getReturnPlatformPaymentDiscountAmount().multiply(sponsorPercentMap.getOrDefault(1,BigDecimal.ZERO)).add(orderDetail.getReturnShopPaymentDiscountAmount().multiply(sponsorPercentMap.getOrDefault(2,BigDecimal.ZERO))).setScale(2, BigDecimal.ROUND_HALF_UP);
                oldReturnAmount = oldReturnAmount.add(returnAmount);
                returnPaySaleAmount.set(oldReturnAmount);
            }


        });

        orderDataDO.setCouponAmount(platformSubsidyAmount.get());
        orderDataDO.setRefundCouponAmount(returnPlatformSubsidyAmount.get());
        orderDataDO.setPromotionAmount(platformSpecialAmount.get());
        orderDataDO.setRefundPromotionAmount(returnPlatformSpecialAmount.get());
        orderDataDO.setComPacAmount(comPacAmount.get());
        orderDataDO.setRefundComPacAmount(returnComPacAmount.get());
        orderDataDO.setPresaleDiscountAmount(preSaleAmount.get());
        orderDataDO.setRefundPreAmount(returnPreSaleAmount.get());
        orderDataDO.setPayDiscountAmount(paySaleAmount.get());
        orderDataDO.setRefundPayAmount(returnPaySaleAmount.get());
    }

    /**
     * 计算订单的赠品金额
     *
     * @param orderDataDO
     * @param orderDetailChangeList
     * @param orderGiftBOList 订单的平台赠品
     */
    private void calculateGiftSale(SettlementOrderSyncDO orderDataDO, List<B2BSettlementDetailDTO> orderDetailChangeList, List<OrderGiftBO> orderGiftBOList) {
        //赠品金额
        AtomicReference<BigDecimal> giftAmount = new AtomicReference<>();
        giftAmount.set(BigDecimal.ZERO);
        //赠品退款金额
        AtomicReference<BigDecimal> refundGiftAmount = new AtomicReference<>();
        refundGiftAmount.set(BigDecimal.ZERO);
        //计算满赠金额
        orderGiftBOList.forEach(orderGiftBO -> {
            BigDecimal var1 = giftAmount.get();
            var1 = var1.add(orderGiftBO.getPrice());
            giftAmount.set(var1);
        });

        //计算满赠退款金额
        PromotionSettleJudgeRequest settleJudgeRequest = new PromotionSettleJudgeRequest();
        //活动列表
        List<PromotionSettleJudgeRequestItem> requestItemList = orderGiftBOList.stream().map(e -> {
            PromotionSettleJudgeRequestItem requestItem = new PromotionSettleJudgeRequestItem();
            requestItem.setPromotionActivityId(e.getActivityId());
            requestItem.setGoodsGiftLimitId(e.getPromotionLimitId());
            return requestItem;
        }).collect(Collectors.toList());
        //商品收货明细
        List<PromotionJudgeGoodsRequest> goodsRequestList = ListUtil.toList();
        orderDetailChangeList.forEach(orderDetail -> {
            BigDecimal goodsAmount = orderDetail.getGoodsAmount();
            BigDecimal returnAmount = orderDetail.getReturnAmount();
            //实际收货的小计
            BigDecimal practicalSubtotal = goodsAmount.subtract(returnAmount);
            //如果实际收货的小计不等于0
            if (practicalSubtotal.compareTo(BigDecimal.ZERO) != 0) {
                PromotionJudgeGoodsRequest judgeGoodsRequest = new PromotionJudgeGoodsRequest();
                judgeGoodsRequest.setGoodsId(orderDetail.getDistributorGoodsId());
                judgeGoodsRequest.setAmount(practicalSubtotal);
                judgeGoodsRequest.setCount(orderDetail.getReceiveQuantity());
                goodsRequestList.add(judgeGoodsRequest);
            }
        });
        settleJudgeRequest.setRequestItemList(requestItemList);
        settleJudgeRequest.setGoodsRequestList(goodsRequestList);
        //查询退货后的实际赠品idList
        Result<List<Long>> settleJudgePromotion = promotionActivityApi.settleJudgePromotion(settleJudgeRequest);
        List<Long> giftIdList = ObjectUtil.equal(settleJudgePromotion.getCode(), ResultCode.SUCCESS.getCode()) == Boolean.TRUE ? settleJudgePromotion.getData() : ListUtil.toList();

        orderGiftBOList.forEach(orderGiftBO -> {
            //如果实际收货 计算的赠品List中没有下单时的 giftId了 视为满赠退货
            if (!giftIdList.contains(orderGiftBO.getActivityId())) {
                BigDecimal var1 = refundGiftAmount.get();
                var1 = var1.add(orderGiftBO.getPrice());
                refundGiftAmount.set(var1);
            }
        });

        orderDataDO.setGiftAmount(giftAmount.get());
        orderDataDO.setRefundGiftAmount(refundGiftAmount.get());
    }
}
