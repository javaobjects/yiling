package com.yiling.ih.patient.feign;

import com.yiling.ih.common.ApiResult;
import com.yiling.ih.config.FeignConfig;
import com.yiling.ih.patient.dto.*;
import com.yiling.ih.patient.dto.request.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 处方服务
 *
 * @author: fan.shen
 * @date: 2023/5/8
 */
@FeignClient(name = "prescriptionFeignClient", url = "${ih.service.baseUrl}", configuration = FeignConfig.class)
public interface HmcPrescriptionFeignClient {

    /**
     * 我的处方
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/prescription/prescriptionList")
    ApiResult<HmcMyPrescriptionResultDTO> myPreList(HmcMyPrescriptionPageRequest request);

    /**
     * 处方详情
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/prescription/selectPrescription")
    ApiResult<HmcPrescriptionDetailDTO> getDetailById(GetPrescriptionDetailRequest request);

    /**
     * 处方药品信息接口
     *
     * @param prescriptionId
     * @return
     */
    @GetMapping("/hmc/prescription/getPrescriptionPayInfo")
    ApiResult<HmcPrescriptionGoodsInfoDTO> getPrescriptionGoodsById(@RequestParam("prescriptionId") Integer prescriptionId);

    /**
     * 处方下单接口
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/prescriptionOrder/createOrder")
    ApiResult<HmcMarketPrescriptionOrderDTO> createPrescriptionOrder(@RequestBody HmcCreateMarketPrescriptionOrderRequest request);

    /**
     * 取消处方订单接口
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/prescriptionOrder/cancelOrder")
    ApiResult cancelPrescriptionOrder(@RequestBody HmcCancelPrescriptionOrderRequest request);

    /**
     * 处方订单支付成功通知接口
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/prescriptionOrder/orderPaySuccess")
    ApiResult prescriptionOrderPaySuccess(@RequestBody HmcPrescriptionOrderPaySuccessNotifyRequest request);

    /**
     * 处方订单发货通知接口
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/prescriptionOrder/deliverOrder")
    ApiResult prescriptionOrderDeliver(@RequestBody HmcPrescriptionOrderDeliverRequest request);

    /**
     * 处方订单收货通知
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/prescriptionOrder/receiveOrder")
    ApiResult prescriptionOrderReceive(@RequestBody HmcPrescriptionOrderReceiveRequest request);

    /**
     * 处方审方失败-退款回调参数
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/prescriptionOrder/refundOrderNotify")
    ApiResult<Boolean> prescriptionOrderRefundNotify(HmcPrescriptionOrderRefundNotifyRequest request);
}
