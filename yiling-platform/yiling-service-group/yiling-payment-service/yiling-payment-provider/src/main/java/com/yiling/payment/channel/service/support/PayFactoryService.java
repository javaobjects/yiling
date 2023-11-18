package com.yiling.payment.channel.service.support;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yiling.framework.common.exception.BusinessException;
import com.yiling.payment.channel.service.PayFactory;
import com.yiling.payment.channel.service.PayService;
import com.yiling.payment.channel.service.TransferService;
import com.yiling.payment.enums.PaymentErrorCode;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.payment.channel.service.support
 * @date: 2021/10/15
 */
@Slf4j
@Component
public class PayFactoryService implements PayFactory {

    @Autowired
    private List<PayService> payServiceList;

    @Autowired
    private List<TransferService> transferServiceList;

    @Override
    public PayService getPayInstance(String payWay,String paySource) {
        String method = "[createPayService]";
        boolean checkExistPayWay = payServiceList.stream().anyMatch(e -> e.matchPayService(payWay,paySource));
        if (!checkExistPayWay) {
            log.error(method + "根据[" + payWay + "],未能创建支付对象!");
            throw new BusinessException(PaymentErrorCode.PAYWAY_NOT_EXISTS);
        }

        return payServiceList.stream().filter(e -> e.matchPayService(payWay,paySource)).findFirst().get();
    }

    @Override
    public TransferService getTransferInstance(String payWay, String paySource) {

        String method = "[getSettleInstance]";
        boolean checkExistPayWay = transferServiceList.stream().anyMatch(e -> e.matchPayService(payWay,paySource));
        if (!checkExistPayWay) {
            log.error(method + "根据[" + payWay + "],未能创建支付对象!");
            throw new BusinessException(PaymentErrorCode.PAYWAY_NOT_EXISTS);
        }
        return transferServiceList.stream().filter(e -> e.matchPayService(payWay,paySource)).findFirst().get();
    }
}
