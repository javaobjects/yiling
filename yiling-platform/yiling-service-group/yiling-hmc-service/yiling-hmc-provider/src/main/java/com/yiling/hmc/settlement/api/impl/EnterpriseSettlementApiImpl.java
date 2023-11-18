package com.yiling.hmc.settlement.api.impl;

import java.util.Date;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.hmc.settlement.api.EnterpriseSettlementApi;
import com.yiling.hmc.settlement.bo.EnterpriseSettlementBO;
import com.yiling.hmc.settlement.bo.EnterpriseSettlementPageBO;
import com.yiling.hmc.settlement.bo.EnterpriseSettlementPageResultBO;
import com.yiling.hmc.settlement.bo.SettlementEnterprisePageBO;
import com.yiling.hmc.settlement.dto.request.EnterpriseSettlementPageRequest;
import com.yiling.hmc.settlement.dto.request.EnterpriseSettlementRequest;
import com.yiling.hmc.settlement.dto.request.SettlementEnterprisePageRequest;
import com.yiling.hmc.settlement.service.EnterpriseSettlementService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: yong.zhang
 * @date: 2022/3/31
 */
@Slf4j
@DubboService
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EnterpriseSettlementApiImpl implements EnterpriseSettlementApi {

    private final EnterpriseSettlementService enterpriseSettlementService;

    @Override
    public List<SettlementEnterprisePageBO> pageSettlement(SettlementEnterprisePageRequest request) {
        return enterpriseSettlementService.pageSettlement(request);
    }

    @Override
    public List<SettlementEnterprisePageBO> countUnSettlementOrder(SettlementEnterprisePageRequest request) {
        return enterpriseSettlementService.countUnSettlementOrder(request);
    }

    @Override
    public List<SettlementEnterprisePageBO> countEnSettlementOrder(SettlementEnterprisePageRequest request) {
        return enterpriseSettlementService.countEnSettlementOrder(request);
    }

    @Override
    public Page<EnterpriseSettlementPageBO> pageList(EnterpriseSettlementPageRequest request) {
        return enterpriseSettlementService.pageList(request);
    }

    @Override
    public EnterpriseSettlementPageResultBO pageResult(EnterpriseSettlementPageRequest request) {
        return enterpriseSettlementService.pageResult(request);
    }

    @Override
    public List<EnterpriseSettlementBO> settlement(List<EnterpriseSettlementRequest> requestList, Long opUserId, Date opTime) {
        return enterpriseSettlementService.settlement(requestList, opUserId, opTime);
    }

    @Override
    public boolean importSettlementData(EnterpriseSettlementRequest request) {
        return enterpriseSettlementService.importSettlementData(request);
    }
}
