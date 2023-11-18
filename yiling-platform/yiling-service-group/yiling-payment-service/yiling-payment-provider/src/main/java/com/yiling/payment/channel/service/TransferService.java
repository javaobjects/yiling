package com.yiling.payment.channel.service;

import java.util.List;

import com.yiling.payment.channel.service.dto.PaymentSettlementDTO;
import com.yiling.payment.channel.service.dto.request.CreatePaymentRequest;
import com.yiling.payment.channel.yee.dto.QueryTransferOrderDTO;

/**
 * 企业打款API
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.payment.channel.service
 * @date: 2021/11/29
 */
public interface TransferService {

    /**
     *  获取支付Service
     * @param payWay 支付方式
     * @param paySource 支付来源
     * @return
     */
    boolean matchPayService(String payWay,String paySource);

    /**
     * 创建企业付款单
     *
     * @param request
     * @return
     */
    List<PaymentSettlementDTO> createPaymentTransfer(List<CreatePaymentRequest> request);

    /**
     * 企业打款查询
     * @param payNo
     * @return
     */
    QueryTransferOrderDTO transferQuery(String payNo);



}
