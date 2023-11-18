package com.yiling.cms.evaluate.api.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.cms.common.enums.BusinessLineEnum;
import com.yiling.cms.evaluate.api.HealthEvaluateApi;
import com.yiling.cms.evaluate.dto.HealthEvaluateDTO;
import com.yiling.cms.evaluate.dto.HealthEvaluateUserDTO;
import com.yiling.cms.evaluate.dto.SubmitHealthEvaluateResultDTO;
import com.yiling.cms.evaluate.dto.request.*;
import com.yiling.cms.evaluate.entity.HealthEvaluateDO;
import com.yiling.cms.evaluate.service.*;
import com.yiling.framework.common.util.PojoUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 健康测评API
 *
 * @author: fan.shen
 * @date: 2022-12-06
 */
@Slf4j
@DubboService
public class HealthEvaluateApiImpl implements HealthEvaluateApi {

    @Autowired
    HealthEvaluateService healthEvaluateService;

    @Autowired
    HealthEvaluateLineService healthEvaluateLineService;

    @Autowired
    HealthEvaluateDeptService deptService;

    @Autowired
    HealthEvaluateDiseaseService diseaseService;

    @Autowired
    HealthEvaluateUserService userService;

    @Override
    public Long addHealthEvaluate(AddHealthEvaluateRequest addHealthEvaluateRequest) {
        return healthEvaluateService.addHealthEvaluate(addHealthEvaluateRequest);
    }

    @Override
    public Boolean updateHealthEvaluate(UpdateHealthEvaluateRequest updateHealthEvaluateRequest) {
        return healthEvaluateService.updateHealthEvaluate(updateHealthEvaluateRequest);
    }

    @Override
    public Page<HealthEvaluateDTO> listPage(QueryHealthEvaluatePageRequest request) {
        return healthEvaluateService.listPage(request);
    }

    @Override
    public HealthEvaluateDTO getHealthEvaluateById(Long id) {
        HealthEvaluateDO evaluateDO = healthEvaluateService.getById(id);
        HealthEvaluateDTO evaluateDTO = PojoUtils.map(evaluateDO, HealthEvaluateDTO.class);
        List<Long> deptIdList = deptService.getByEvaluateId(id);
        List<Long> diseaseIdList = diseaseService.getByEvaluateId(id);
        evaluateDTO.setDeptIdList(deptIdList);
        evaluateDTO.setDiseaseIdList(diseaseIdList);
        return evaluateDTO;
    }

    @Override
    public List<Long> getLineIdList(Long id) {
        return healthEvaluateLineService.getByHealthEvaluateId(id);
    }

    @Override
    public void publishHealthEvaluate(PublishHealthEvaluateRequest request) {
        healthEvaluateService.publishHealthEvaluate(request);
    }

    @Override
    public List<HealthEvaluateDTO> getIndexPage() {
        List<Long> healthEvaluateIdList = healthEvaluateLineService.getByLineId(BusinessLineEnum.TOC.getCode());
        if (CollUtil.isEmpty(healthEvaluateIdList)) {
            log.info("[getIndexPage]根据业务线未获取到测评数据");
            return Lists.newArrayList();
        }

        return healthEvaluateService.getByIdList(healthEvaluateIdList);
    }

    @Override
    public Map<Long, Long> getTotalQuestionByEvaluateIdList(List<Long> healthEvaluateIdList) {
        return healthEvaluateService.getTotalSubjectByEvaluateIdList(healthEvaluateIdList);
    }

    @Override
    public Map<Long, Long> getTotalUserByEvaluateIdList(List<Long> healthEvaluateIdList) {
        return userService.getTotalUserByEvaluateIdList(healthEvaluateIdList);
    }

    @Override
    public List<HealthEvaluateUserDTO> getUserByEvaluateIdList(List<Long> healthEvaluateIdList) {
        return userService.getUserByEvaluateIdList(healthEvaluateIdList);
    }

    @Override
    public Long startEvaluate(UserStartEvaluateRequest request) {
        return userService.startEvaluate(request);
    }

    @Override
    public SubmitHealthEvaluateResultDTO submitEvaluateResult(SubmitEvaluateResultRequest request) {
        return healthEvaluateService.submitEvaluateResult(request);
    }
}