package com.yiling.payment.pay.ai.impl;

import java.util.Date;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.pojo.Result;
import com.yiling.payment.enums.OrderPlatformEnum;
import com.yiling.payment.enums.RefundStateEnum;
import com.yiling.payment.pay.api.RefundApi;
import com.yiling.payment.pay.api.RepeatOrderApi;
import com.yiling.payment.pay.dto.RepeatPayOrderDTO;
import com.yiling.payment.pay.dto.request.RefundParamRequest;
import com.yiling.payment.pay.dto.request.RepeatOrderPageRequest;
import com.yiling.payment.pay.dto.request.UpdateRepeatOrderRequest;
import com.yiling.payment.pay.entity.PaymentMergeOrderDO;
import com.yiling.payment.pay.entity.PaymentRepeatOrderDO;
import com.yiling.payment.pay.service.PaymentMergeOrderService;
import com.yiling.payment.pay.service.PaymentRepeatOrderService;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;

/**
 * 重复退款
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.payment.pay.ai.impl
 * @date: 2021/11/1
 */
@DubboService
@Slf4j
public class RepeatOrderApiImpl implements RepeatOrderApi {
    @Autowired
    private PaymentRepeatOrderService repeatOrderService;
    @Autowired
    private RefundApi refundApi;
    @Autowired
    private PaymentMergeOrderService paymentMergeOrderService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> processRepeatOrder(UpdateRepeatOrderRequest updateRepeatOrderRequest) {
        PaymentRepeatOrderDO orderDO = repeatOrderService.getById(updateRepeatOrderRequest.getRepeatOrderId());
        if (orderDO == null) {
          return  Result.failed("未查询到支付重复记录");
        }
        if (RefundStateEnum.WAIT_REFUND !=  RefundStateEnum.getByCode(orderDO.getRefundState()) && RefundStateEnum.FALIUE !=  RefundStateEnum.getByCode(orderDO.getRefundState())) {
            return  Result.failed("请选择待处理或者退款失败的退款单!");
        }
        List<PaymentMergeOrderDO> mergeOrderList = paymentMergeOrderService.selectMergerOrderByPayId(orderDO.getPayId());
        if (CollectionUtil.isEmpty(mergeOrderList)) {
            return  Result.failed("未查询到支付记录");
        }
        PaymentMergeOrderDO currentMergeOrder = mergeOrderList.stream().filter(e -> orderDO.getPayNo().equals(e.getPayNo()) && orderDO.getAppOrderId().equals(e.getAppOrderId())).findFirst().get();
        // 直接标记退款为退款完成,并且修改，订单的退款金额
        if (2 == updateRepeatOrderRequest.getMethodType()) {
            paymentMergeOrderService.updateMergeOrderFundAmount(currentMergeOrder.getId(),orderDO.getRefundAmount());
            // 修改重复退款的退款状态
            PaymentRepeatOrderDO repeatOrderDo = new PaymentRepeatOrderDO();
            repeatOrderDo.setId(orderDO.getId());
            repeatOrderDo.setRefundState(RefundStateEnum.SUCCESS.getCode());
            repeatOrderDo.setThirdFundNo("");
            repeatOrderDo.setRefundDate(new Date());
            repeatOrderDo.setErrorMessage("");
            repeatOrderDo.setOpUserId(updateRepeatOrderRequest.getOpUserId());
            repeatOrderDo.setUpdateUser(updateRepeatOrderRequest.getOpUserId());
            repeatOrderDo.setOperateUser(updateRepeatOrderRequest.getOpUserId());
            repeatOrderDo.setOperateTime(new Date());
            repeatOrderDo.setMethodType(updateRepeatOrderRequest.getMethodType());
            repeatOrderService.updateById(repeatOrderDo);
            return Result.success();
        }

        // 健康管理中心订单目前未对接,只能线下退款
        if (OrderPlatformEnum.HMC == OrderPlatformEnum.getByCode(currentMergeOrder.getOrderPlatform())) {

            return  Result.failed("健康管理中心订单未对接接口,请采用线下退款形式!");
        }

        RefundParamRequest refundParamRequest =
                RefundParamRequest.builder()
                        .refundId(orderDO.getId())
                        .refundType(2)
                        .reason("支付重复退款")
                        .refundAmount(orderDO.getRefundAmount())
                        .appOrderId(orderDO.getAppOrderId())
                        .appOrderNo(orderDO.getAppOrderNo())
                        .payNo(orderDO.getPayNo())
                        .build();
        Result<Void> result =   refundApi.refundPayOrder(refundParamRequest);

        if (HttpStatus.HTTP_OK == result.getCode()) {
            PaymentRepeatOrderDO repeatOrderDo = new PaymentRepeatOrderDO();
            repeatOrderDo.setId(orderDO.getId());
            repeatOrderDo.setRefundState(RefundStateEnum.REFUND_ING.getCode());
            repeatOrderDo.setThirdFundNo("");
            repeatOrderDo.setRefundDate(new Date());
            repeatOrderDo.setErrorMessage("");
            repeatOrderDo.setOpUserId(updateRepeatOrderRequest.getOpUserId());
            repeatOrderDo.setUpdateUser(updateRepeatOrderRequest.getOpUserId());
            repeatOrderDo.setOperateUser(updateRepeatOrderRequest.getOpUserId());
            repeatOrderDo.setOperateTime(new Date());
            repeatOrderDo.setMethodType(updateRepeatOrderRequest.getMethodType());
            repeatOrderService.updateById(repeatOrderDo);
        }
        return result;
    }

    @Override
    public Page<RepeatPayOrderDTO> selectPageRepeatOrderList(RepeatOrderPageRequest request) {

        return repeatOrderService.selectPageRepeatOrderList(request);
    }
}
