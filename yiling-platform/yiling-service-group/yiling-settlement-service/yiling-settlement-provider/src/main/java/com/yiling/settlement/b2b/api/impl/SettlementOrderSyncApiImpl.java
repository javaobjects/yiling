package com.yiling.settlement.b2b.api.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.settlement.b2b.api.SettlementOrderSyncApi;
import com.yiling.settlement.b2b.dto.SettlementOrderSyncDTO;
import com.yiling.settlement.b2b.dto.request.QuerySettOrderDataPageListRequest;
import com.yiling.settlement.b2b.dto.request.UpdateOrderSyncSettStatusRequest;
import com.yiling.settlement.b2b.enums.OrderSyncDataStatusEnum;
import com.yiling.settlement.b2b.enums.OrderSyncDefaultStatusEnum;
import com.yiling.settlement.b2b.enums.OrderSyncStatusEnum;
import com.yiling.settlement.b2b.enums.SettGenerateStatusEnum;
import com.yiling.settlement.b2b.service.SettlementOrderSyncService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;

/**
 * @author: dexi.yao
 * @date: 2022-04-11
 */
@DubboService
public class SettlementOrderSyncApiImpl implements SettlementOrderSyncApi {

    @Autowired
    SettlementOrderSyncService settlementOrderSyncService;

    @Override
    public Boolean cashRepaymentNotifySett(String orderCode) {
        return settlementOrderSyncService.cashRepaymentNotifySett(orderCode);
    }

    @Override
    public void settOrderSyncFailData() {
        //分页查询同步失败的订单列表
        int current = 1;
        Page<SettlementOrderSyncDTO> page;
        QuerySettOrderDataPageListRequest request = new QuerySettOrderDataPageListRequest();
        //分页查询同步失败的订单列表
        do {
            request.setDefaultStatus(OrderSyncDefaultStatusEnum.ORDER_PERFORMANCE.getCode());
            request.setOrderStatusList(ListUtil.toList(0));
            request.setGenerateStatus(SettGenerateStatusEnum.UN_GENERATE.getCode());

            request.setSyncStatus(OrderSyncStatusEnum.FAIL.getCode());
            request.setCurrent(current);
            request.setSize(50);
            //分页查询同步失败的订单列表
            page = settlementOrderSyncService.pageListBySyncStatus(request);
            if (CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            List<SettlementOrderSyncDTO> records = page.getRecords();
            //重新同步订单数据
            settlementOrderSyncService.syncSettOrderSyncByIds(records.stream().map(SettlementOrderSyncDTO::getId).collect(Collectors.toList()));
            current = current + 1;
        } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
    }

    @Override
    public SettlementOrderSyncDTO querySettOrderSyncByOrderCode(String orderCode) {
        return settlementOrderSyncService.querySettOrderSyncByOrderCode(orderCode);
    }

}
