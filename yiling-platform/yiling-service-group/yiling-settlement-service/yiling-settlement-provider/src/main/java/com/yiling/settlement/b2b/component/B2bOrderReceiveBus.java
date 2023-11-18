package com.yiling.settlement.b2b.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yiling.settlement.b2b.service.SettlementOrderSyncService;
import com.yiling.settlement.report.service.B2bOrderSyncService;

/**
 * b2b 收货成功消息通知
 *
 * @author: dexi.yao
 * @date: 2022-05-20
 */
@Component
public class B2bOrderReceiveBus {

    @Autowired
    SettlementOrderSyncService settlementOrderSyncService;
    @Autowired
    B2bOrderSyncService b2bOrderSyncService;

    public Boolean orderReceiveMsg(String orderNo){
        //通知结算单
        Boolean isSuccess = settlementOrderSyncService.createSettOrderSync(orderNo);
        if (!isSuccess){
            return isSuccess;
        }
        //通知返利
        isSuccess= b2bOrderSyncService.createOrderSync(orderNo);
        if (!isSuccess){
            return isSuccess;
        }
        return Boolean.TRUE;
    }
}
