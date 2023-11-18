package com.yiling.cms.evaluate.api.impl;

import com.yiling.cms.evaluate.api.HealthEvaluateQuestionApi;
import com.yiling.cms.evaluate.dto.HealthEvaluateQuestionDTO;
import com.yiling.cms.evaluate.dto.request.AddHealthEvaluateQuestionRequest;
import com.yiling.cms.evaluate.dto.request.EditHealthEvaluateQuestionRequest;
import com.yiling.cms.evaluate.service.HealthEvaluateQuestionService;
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
public class HealthEvaluateQuestionApiImpl implements HealthEvaluateQuestionApi {

    @Autowired
    HealthEvaluateQuestionService healthEvaluateQuestionService;

    @Override
    public List<HealthEvaluateQuestionDTO> getHealthEvaluateQuestionByEvaluateId(Long evaluateId) {
        return healthEvaluateQuestionService.getHealthEvaluateQuestionByEvaluateId(evaluateId);
    }

    @Override
    public Boolean addHealthEvaluateQuestion(AddHealthEvaluateQuestionRequest request) {
        return healthEvaluateQuestionService.addHealthEvaluateQuestion(request);
    }

    @Override
    public List<HealthEvaluateQuestionDTO> getFullQuestionsByHealthEvaluateId(Long id) {
        return healthEvaluateQuestionService.getFullQuestionsByHealthEvaluateId(id);
    }

    @Override
    public HealthEvaluateQuestionDTO getQuestionsByQuestionId(Long id) {
        return healthEvaluateQuestionService.getQuestionsByQuestionId(id);
    }

    @Override
    public Boolean delQuestionsById(Long id) {
        return healthEvaluateQuestionService.delQuestionsById(id);
    }

    @Override
    public Boolean editQuestions(EditHealthEvaluateQuestionRequest request) {
        return healthEvaluateQuestionService.editQuestions(request);
    }
}