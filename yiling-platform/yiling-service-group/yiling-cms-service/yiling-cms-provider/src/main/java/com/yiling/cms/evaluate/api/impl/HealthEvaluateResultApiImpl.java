package com.yiling.cms.evaluate.api.impl;

import com.yiling.cms.evaluate.api.HealthEvaluateResultApi;
import com.yiling.cms.evaluate.dto.HealthEvaluateResultDTO;
import com.yiling.cms.evaluate.dto.request.*;
import com.yiling.cms.evaluate.service.HealthEvaluateLineService;
import com.yiling.cms.evaluate.service.HealthEvaluateResultService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 健康测评API
 *
 * @author: fan.shen
 * @date: 2022-12-06
 */
@DubboService
public class HealthEvaluateResultApiImpl implements HealthEvaluateResultApi {

    @Autowired
    HealthEvaluateResultService healthEvaluateResultService;

    @Autowired
    HealthEvaluateLineService healthEvaluateLineService;

    @Override
    public Boolean addHealthEvaluateResult(AddHealthEvaluateResultRequest request) {
        return healthEvaluateResultService.addHealthEvaluateResult(request);
    }

    @Override
    public Boolean updateHealthEvaluateResult(UpdateHealthEvaluateResultRequest request) {
        return healthEvaluateResultService.updateHealthEvaluateResult(request);
    }

    @Override
    public Boolean delHealthEvaluateResultById(Long id) {
        return healthEvaluateResultService.delHealthEvaluateResultById(id);
    }

    @Override
    public List<HealthEvaluateResultDTO> getResultListByEvaluateId(Long evaluateId) {
        return healthEvaluateResultService.getResultListByEvaluateId(evaluateId);
    }
}