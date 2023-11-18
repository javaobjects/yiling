package com.yiling.dataflow.wash.api.impl;

import java.util.List;

import com.yiling.dataflow.wash.api.ErpClientWashPlanApi;
import com.yiling.dataflow.wash.dto.ErpClientWashPlanDTO;
import com.yiling.dataflow.wash.dto.request.SaveOrUpdateErpClientWashPlanRequest;
import com.yiling.dataflow.wash.service.ErpClientWashPlanService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: shuang.zhang
 * @date: 2023/5/30
 */
@DubboService
public class ErpClientWashPlanApiImpl implements ErpClientWashPlanApi {

    @Autowired
    private ErpClientWashPlanService erpClientWashPlanService;

    @Override
    public boolean generate(List<SaveOrUpdateErpClientWashPlanRequest> erpClientWashPlanList) {
        return erpClientWashPlanService.generate(erpClientWashPlanList);
    }

    @Override
    public boolean updateByFmwcId(List<SaveOrUpdateErpClientWashPlanRequest> erpClientWashPlanList) {
        return erpClientWashPlanService.updateByFmwcId(erpClientWashPlanList);
    }

    @Override
    public List<ErpClientWashPlanDTO> findListByFmwcId(Long fmwcId) {
        return erpClientWashPlanService.findListByFmwcId(fmwcId);
    }

    @Override
    public boolean updateById(SaveOrUpdateErpClientWashPlanRequest request) {
        return erpClientWashPlanService.updateById(request);
    }
}
