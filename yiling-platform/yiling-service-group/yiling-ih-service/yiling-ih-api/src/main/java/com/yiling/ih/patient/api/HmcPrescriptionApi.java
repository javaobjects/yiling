package com.yiling.ih.patient.api;

import com.yiling.ih.patient.dto.*;
import com.yiling.ih.patient.dto.request.*;

import java.util.List;

/**
 * HMC处方API
 *
 * @author: fan.shen
 * @date: 2022/8/23
 */
public interface HmcPrescriptionApi {

    /**
     * 处方详情
     * @param id
     * @return
     */
    HmcPrescriptionDetailDTO getDetailById(Integer id);

    /**
     * 我的处方列表
     * @param request
     * @return
     */
    HmcMyPrescriptionResultDTO myPreList(HmcMyPrescriptionPageRequest request);

    /**
     * 处方药品信息
     * @param id
     * @return
     */
    HmcPrescriptionGoodsInfoDTO getPrescriptionGoodsById(Integer id);

    /**
     * 创建处方订单
     * @param request
     * @return
     */
    HmcMarketPrescriptionOrderDTO createPrescriptionOrder(HmcCreateMarketPrescriptionOrderRequest request);

    /**
     * 取消处方订单
     * @param request
     * @return
     */
    void cancelPrescriptionOrder(HmcCancelPrescriptionOrderRequest request);

    /**
     * 处方订单支付成功通知
     * @param request
     * @return
     */
    void prescriptionOrderPaySuccess(HmcPrescriptionOrderPaySuccessNotifyRequest request);

    /**
     * 处方订单发货
     * @param request
     * @return
     */
    void prescriptionOrderDeliver(HmcPrescriptionOrderDeliverRequest request);

    /**
     * 处方订单签收
     * @param request
     * @return
     */
    void prescriptionOrderReceive(HmcPrescriptionOrderReceiveRequest request);

    /**
     * 处方审方失败-退款回调
     * @param request
     * @return
     */
    boolean prescriptionOrderRefundNotify(HmcPrescriptionOrderRefundNotifyRequest request);
}
