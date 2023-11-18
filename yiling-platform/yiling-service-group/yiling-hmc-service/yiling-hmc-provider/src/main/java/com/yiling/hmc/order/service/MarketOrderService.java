package com.yiling.hmc.order.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.hmc.order.dto.AdminMarketOrderDTO;
import com.yiling.hmc.order.dto.MarketOrderDTO;
import com.yiling.hmc.order.dto.request.*;
import com.yiling.hmc.order.entity.MarketOrderDO;
import com.yiling.hmc.wechat.dto.request.*;

/**
 * <p>
 * HMC订单表 服务类
 * </p>
 *
 * @author fan.shen
 * @date 2023-02-16
 */
public interface MarketOrderService extends BaseService<MarketOrderDO> {

    /**
     * 创建订单
     * @param request
     * @return
     */
    MarketOrderDTO createOrder(MarketOrderSubmitRequest request);

    /**
     * 创建处方订单
     * @param request
     * @return
     */
    MarketOrderDTO createPrescriptionOrder(PrescriptionOrderSubmitRequest request);

    /**
     * 获取订单信息
     * @param orderId
     * @return
     */
    MarketOrderDTO queryById(Long orderId);

    /**
     * 批量获取订单信息
     * @param orderIdList
     * @return
     */
    List<MarketOrderDTO> queryByIdList(List<Long> orderIdList);

    /**
     * 支付回调（市场订单、处方订单）
     * @param payNotifyRequest
     * @return
     */
    Boolean payNotify(MarketOrderPayNotifyRequest payNotifyRequest);

    /**
     * 问诊订单支付成功回调
     * @param payNotifyRequest
     * @return
     */
    Boolean diagnosisOrderPayNotify(HmcDiagnosisOrderPaySuccessNotifyRequest payNotifyRequest);

    /**
     * 处方订单审方失败-退款回调
     * @param refundNotifyRequest
     * @return
     */
    Boolean prescriptionOrderRefundNotify(MarketOrderRefundNotifyRequest refundNotifyRequest);

    /**
     * 查询订单
     *
     * @param body
     * @return
     */
    MarketOrderDTO queryByOrderNo(String body);

    /**
     * 小程序查询订单列表
     *
     * @param request
     * @return
     */
    Page<MarketOrderDTO> queryMarketOrderPage(QueryMarketOrderPageRequest request);

    /**
     * 修改订单
     *
     * @param request
     * @return
     */
    Boolean updateMarketOrder(UpdateMarketOrderRequest request);


    /**
     * 运营后台分页查询列表
     *
     * @param request
     * @return
     */
    Page<AdminMarketOrderDTO> queryAdminMarketOrderPage(QueryAdminMarkerOrderPageRequest request);

    /**
     * 订单发货发送小程序通知
     *
     * @param orderId
     */
    void sendMsg(Long orderId);

    /**
     * 保存平台运营备注或商家备注
     * @param request
     */
    void saveRemark(MarketOrderSaveRequest request);

    /**
     * 订单发货
     * @param request
     */
    void orderDelivery(MarketOrderDeliveryRequest request);

    /**
     * 处方订单发货
     * @param request
     */
    void prescriptionOrderDelivery(MarketPrescriptionOrderDeliveryRequest request);

    /**
     * 自动收货
     *
     * @param day
     */
    void marketOrderAutoReceive(Integer day);

    /**
     * 处方订单收货
     * @param request
     */
    void prescriptionOrderReceive(MarketPrescriptionOrderReceiveRequest request);

    /**
     * 根据IH订单id获取处方订单
     * @param ihOrderId
     * @return
     */
    MarketOrderDTO queryPrescriptionOrderByIhOrderId(Integer ihOrderId);

    /**
     * 根据处方id获得处方订单信息
     *
     * @param prescriptionId    处方id
     * @return  处方订单信息
     */
    MarketOrderDTO queryPrescriptionOrderByPrescriptionId(Long prescriptionId);

    /**
     * 获取订单
     * @param prescriptionId
     * @return
     */
    MarketOrderDO getByPrescriptionId(Integer prescriptionId);
}
