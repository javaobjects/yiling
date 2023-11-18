package com.yiling.mall.order.service.impl;

import java.util.Date;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.pojo.Result;
import com.yiling.goods.inventory.api.InventoryApi;
import com.yiling.goods.inventory.dto.request.AddOrSubtractQtyRequest;
import com.yiling.mall.order.service.OrderModifyAuditService;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.bo.OrderModifyAuditChangeBO;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.request.ModifyOrderNotAuditRequest;
import com.yiling.order.order.dto.request.UpdateOrderNotAuditRequest;
import com.yiling.order.order.enums.PaymentMethodEnum;
import com.yiling.order.order.enums.PaymentStatusEnum;
import com.yiling.user.payment.api.PaymentDaysAccountApi;
import com.yiling.user.payment.dto.request.RefundPaymentDaysAmountRequest;

import cn.hutool.core.map.MapUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

/**
 *  订单反审核
 * @author zhigang.guo
 * @date: 2021/8/9
 */
@Service
@Slf4j
public class OrderModifyAuditServiceImpl implements OrderModifyAuditService {
    @DubboReference
    OrderApi                    orderApi;
    @DubboReference
    PaymentDaysAccountApi       paymentDaysAccountApi;
    @DubboReference
    InventoryApi                inventoryApi;

    @Override
    @GlobalTransactional
    public Result<Boolean> modifyOrderNotAudit(UpdateOrderNotAuditRequest updateOrderNotAuditRequest) {

        // 调用order接口操作反审核
        Result<OrderModifyAuditChangeBO> result = orderApi.modifyOrderNotAudit(updateOrderNotAuditRequest);

        if (!ResultCode.SUCCESS.getCode().equals(result.getCode())) {

            return Result.failed(result.getMessage());
        }

        OrderModifyAuditChangeBO changeBO = result.getData();

        // 订单信息
        OrderDTO orderDTO =  orderApi.getOrderInfo(updateOrderNotAuditRequest.getOrderId());
        // 扣减库存 todo

        // 是否需要生成退货单
        if (changeBO.getIsReducePaymentAccount()) {

            // 归还账期支付
            // 如果订单是账期支付,需要归还账期支付金额
            if (PaymentMethodEnum.PAYMENT_DAYS.getCode().equals(orderDTO.getPaymentMethod().longValue())&& PaymentStatusEnum.getByCode(orderDTO.getPaymentStatus()) == PaymentStatusEnum.UNPAID) {

                RefundPaymentDaysAmountRequest refundPaymentDaysAmountRequest = new RefundPaymentDaysAmountRequest();
                refundPaymentDaysAmountRequest.setOrderId(updateOrderNotAuditRequest.getOrderId());
                refundPaymentDaysAmountRequest.setRefundAmount(changeBO.getReturnTotalAmount());

                paymentDaysAccountApi.modifyAuditRefund(refundPaymentDaysAmountRequest);

            }
        }

        // 是否需要产生退货信息
        if (changeBO.getIsProductReturnOrder()) {

            // todo 如果反审成功，推送生成退货单消息,已删除
        }

        return Result.success(true);
    }


    @Override
    @GlobalTransactional
    public Boolean modifyOrderNotAudit_v2(ModifyOrderNotAuditRequest modifyOrderNotAuditRequest) {
        // 调用order接口操作反审核
        Result<OrderModifyAuditChangeBO> result = orderApi.modifyOrderNotAudit_v2(modifyOrderNotAuditRequest);
        if (!ResultCode.SUCCESS.getCode().equals(result.getCode())) {
            return false;
        }
        OrderModifyAuditChangeBO changeBO = result.getData();
        // 是否需要退还库存
        this.backInventory(modifyOrderNotAuditRequest.getOrderId(),changeBO.getInventoryChangeMap(),changeBO.getFrozenChangeMap());
        // 是否需要退还账期
        if (changeBO.getIsReducePaymentAccount()) {
            // 归还账期支付
            RefundPaymentDaysAmountRequest refundPaymentDaysAmountRequest = new RefundPaymentDaysAmountRequest();
            refundPaymentDaysAmountRequest.setOrderId(modifyOrderNotAuditRequest.getOrderId());
            refundPaymentDaysAmountRequest.setRefundAmount(changeBO.getReturnTotalAmount());
            paymentDaysAccountApi.modifyAuditRefund(refundPaymentDaysAmountRequest);
        }
        return true;
    }

    /**
     * 退还库存
     * @param orderId
     * @param inventoryChangeMap
     */
    private void backInventory(Long orderId,Map<Long,Integer> inventoryChangeMap,Map<Long,Integer> frozenMap) {
        OrderDTO orderDTO = orderApi.getOrderInfo(orderId);
        if (MapUtil.isNotEmpty(inventoryChangeMap)) {
            for (Long skuGoodId : inventoryChangeMap.keySet()) {
                AddOrSubtractQtyRequest request = new AddOrSubtractQtyRequest();
                request.setFrozenQty(0l);
                request.setSkuId(skuGoodId);
                request.setOpUserId(0l);
                request.setOpTime(new Date());
                request.setOrderNo(orderDTO.getOrderNo());
                request.setQty(Long.valueOf(inventoryChangeMap.get(skuGoodId)));
                inventoryApi.backFrozenQtyAndQty(request);
            }
        }
        if (MapUtil.isNotEmpty(frozenMap)) {
            for (Long skuGoodId : frozenMap.keySet()) {
                AddOrSubtractQtyRequest request = new AddOrSubtractQtyRequest();
                request.setFrozenQty(Long.valueOf(frozenMap.get(skuGoodId)));
                request.setSkuId(skuGoodId);
                request.setOpUserId(0l);
                request.setOpTime(new Date());
                request.setOrderNo(orderDTO.getOrderNo());
                request.setQty(0l);
                inventoryApi.backFrozenQtyAndQty(request);
            }
        }
    }
}
