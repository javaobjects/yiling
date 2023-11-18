package com.yiling.hmc.settlement.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.settlement.api.InsuranceSettlementApi;
import com.yiling.hmc.settlement.bo.InsuranceSettlementPageResultBO;
import com.yiling.hmc.settlement.dto.InsuranceSettlementDTO;
import com.yiling.hmc.settlement.dto.InsuranceSettlementDetailDTO;
import com.yiling.hmc.settlement.dto.request.InsuranceSettlementCallbackRequest;
import com.yiling.hmc.settlement.dto.request.InsuranceSettlementPageRequest;
import com.yiling.hmc.settlement.dto.request.InsuranceSettlementRequest;
import com.yiling.hmc.settlement.dto.request.SyncOrderRequest;
import com.yiling.hmc.settlement.entity.InsuranceSettlementDO;
import com.yiling.hmc.settlement.entity.InsuranceSettlementDetailDO;
import com.yiling.hmc.settlement.service.InsuranceSettlementDetailService;
import com.yiling.hmc.settlement.service.InsuranceSettlementService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 保司结算Api
 *
 * @author: yong.zhang
 * @date: 2022/3/31
 */
@Slf4j
@DubboService
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InsuranceSettlementApiImpl implements InsuranceSettlementApi {

    private final InsuranceSettlementService insuranceSettlementService;

    private final InsuranceSettlementDetailService insuranceSettlementDetailService;

    @Override
    public InsuranceSettlementDTO queryById(Long id) {
        InsuranceSettlementDO insuranceSettlementDO = insuranceSettlementService.getById(id);
        return PojoUtils.map(insuranceSettlementDO, InsuranceSettlementDTO.class);
    }

    @Override
    public InsuranceSettlementPageResultBO pageList(InsuranceSettlementPageRequest request) {
        return insuranceSettlementService.pageList(request);
    }

    @Override
    public boolean importSettlementData(InsuranceSettlementRequest request) {
        return insuranceSettlementService.importSettlementData(request);
    }

    @Override
    public boolean callback(InsuranceSettlementCallbackRequest request) {
        return insuranceSettlementService.callback(request);
    }

    @Override
    public List<InsuranceSettlementDetailDTO> listByInsuranceSettlementId(Long insuranceSettlementId) {
        List<InsuranceSettlementDetailDO> doList = insuranceSettlementDetailService.listByInsuranceSettlementId(insuranceSettlementId);
        return PojoUtils.map(doList, InsuranceSettlementDetailDTO.class);
    }

    @Override
    public boolean syncOrder(SyncOrderRequest request) {
        return insuranceSettlementService.syncOrder(request);
    }

    @Override
    public Result<Object> getSyncOrderResult(SyncOrderRequest request) {
        return insuranceSettlementService.getSyncOrderResult(request);
    }
}
