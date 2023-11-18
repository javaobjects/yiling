package com.yiling.ih.patient.api.impl;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.yiling.ih.common.ApiResult;
import com.yiling.ih.patient.api.HmcDiagnosisApi;
import com.yiling.ih.patient.api.HmcPrescriptionApi;
import com.yiling.ih.patient.dto.*;
import com.yiling.ih.patient.dto.request.*;
import com.yiling.ih.patient.feign.HmcDiagnosisFeignClient;
import com.yiling.ih.patient.feign.HmcPrescriptionFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * HMC问诊API实现类
 *
 * @author: fan.shen
 * @date: 2022/6/7
 */
@Slf4j
@DubboService
public class HmcPrescriptionApiImpl implements HmcPrescriptionApi {

    @Autowired
    private HmcPrescriptionFeignClient prescriptionFeignClient;

    @Override
    public HmcMyPrescriptionResultDTO myPreList(HmcMyPrescriptionPageRequest request) {
        log.info("myPreList,入参：{}", JSONUtil.toJsonStr(request));
        ApiResult<HmcMyPrescriptionResultDTO> apiResult = prescriptionFeignClient.myPreList(request);
        log.info("myPreList,返参：{}", JSONUtil.toJsonStr(apiResult));
        if (apiResult.success()) {
            return apiResult.getData();
        }
        return null;
    }

    @Override
    public HmcPrescriptionDetailDTO getDetailById(Integer id) {
        GetPrescriptionDetailRequest request = new GetPrescriptionDetailRequest();
        request.setId(id);
        ApiResult<HmcPrescriptionDetailDTO> apiResult = prescriptionFeignClient.getDetailById(request);
        if (apiResult.success()) {
            return apiResult.getData();
        }
        return null;
    }

    @Override
    public HmcPrescriptionGoodsInfoDTO getPrescriptionGoodsById(Integer id) {
        log.info("getPrescriptionGoodsById 处方药品信息接口查询:{}", id);
        ApiResult<HmcPrescriptionGoodsInfoDTO> apiResult = prescriptionFeignClient.getPrescriptionGoodsById(id);
        log.info("getPrescriptionGoodsById 处方药品信息接口返回:{}", JSONUtil.toJsonStr(apiResult));
        if (apiResult.success()) {
            return apiResult.getData();
        }
        return null;
    }

    @Override
    public HmcMarketPrescriptionOrderDTO createPrescriptionOrder(HmcCreateMarketPrescriptionOrderRequest request) {
        log.info("createPrescriptionOrder 创建处方订单参数:{}", JSONUtil.toJsonStr(request));
        ApiResult<HmcMarketPrescriptionOrderDTO> apiResult = prescriptionFeignClient.createPrescriptionOrder(request);
        if (apiResult.success()) {
            return apiResult.getData();
        }
        return null;
    }

    @Override
    public void cancelPrescriptionOrder(HmcCancelPrescriptionOrderRequest request) {
        log.info("createPrescriptionOrder 取消处方订单参数:{}", JSONUtil.toJsonStr(request));
        ApiResult apiResult = prescriptionFeignClient.cancelPrescriptionOrder(request);
        if (apiResult.success()) {
            log.info("调用IH接口取消处方订单成功");
        } else {
            log.error("调用IH接口取消处方订单失败，入参:{}", JSONUtil.toJsonStr(request));
        }
    }

    @Override
    public void prescriptionOrderPaySuccess(HmcPrescriptionOrderPaySuccessNotifyRequest request) {
        log.info("prescriptionOrderPaySuccess 处方订单支付成功参数:{}", JSONUtil.toJsonStr(request));
        ApiResult apiResult = prescriptionFeignClient.prescriptionOrderPaySuccess(request);
        if (apiResult.success()) {
            log.info("调用IH接口通知处方订单支付成功");
        } else {
            log.error("调用IH接口通知处方订单支付失败，入参:{}", JSONUtil.toJsonStr(request));
        }
    }

    @Override
    public void prescriptionOrderDeliver(HmcPrescriptionOrderDeliverRequest request) {
        log.info("prescriptionOrderDeliver 处方订单发货参数:{}", JSONUtil.toJsonStr(request));
        ApiResult apiResult = prescriptionFeignClient.prescriptionOrderDeliver(request);
        if (apiResult.success()) {
            log.info("调用IH接口通知处方订单发货成功");
        } else {
            log.error("调用IH接口通知处方订单发货失败，入参:{}", JSONUtil.toJsonStr(request));
        }
    }

    @Override
    public void prescriptionOrderReceive(HmcPrescriptionOrderReceiveRequest request) {
        log.info("prescriptionOrderReceive 处方订单参收货数:{}", JSONUtil.toJsonStr(request));
        ApiResult apiResult = prescriptionFeignClient.prescriptionOrderReceive(request);
        if (apiResult.success()) {
            log.info("调用IH接口通知处方订单发货成功");
        } else {
            log.error("调用IH接口通知处方订单发货失败，入参:{}", JSONUtil.toJsonStr(request));
        }
    }

    @Override
    public boolean prescriptionOrderRefundNotify(HmcPrescriptionOrderRefundNotifyRequest request) {
        log.info("prescriptionOrderRefundNotify 处方审方失败-退款回调参数:{}", JSONUtil.toJsonStr(request));
        ApiResult apiResult = prescriptionFeignClient.prescriptionOrderRefundNotify(request);
        if (apiResult.success()) {
            log.info("调用IH接口通知处方订单发货成功");
            return true;
        } else {
            log.error("调用IH接口通知处方订单发货失败，入参:{}", JSONUtil.toJsonStr(request));
            return false;
        }
    }
}