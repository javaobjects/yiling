package com.yiling.cms.evaluate.api;

import com.yiling.cms.evaluate.dto.HealthEvaluateQuestionDTO;
import com.yiling.cms.evaluate.dto.request.AddHealthEvaluateQuestionRequest;
import com.yiling.cms.evaluate.dto.request.EditHealthEvaluateQuestionRequest;

import java.util.List;

/**
 * 健康测评API
 * @author: fan.shen
 * @date: 2022/12/6
 */
public interface HealthEvaluateQuestionApi {

    /**
     * 获取测评下的题目
     * @param evaluateId
     * @return
     */
    List<HealthEvaluateQuestionDTO> getHealthEvaluateQuestionByEvaluateId(Long evaluateId);

    /**
     * 添加题目
     * @param request
     * @return
     */
    Boolean addHealthEvaluateQuestion(AddHealthEvaluateQuestionRequest request);

    /**
     * 获取题目全部信息接口
     * @param id
     * @return
     */
    List<HealthEvaluateQuestionDTO> getFullQuestionsByHealthEvaluateId(Long id);

    /**
     * 获取题目信息接口
     * @param id
     * @return
     */
    HealthEvaluateQuestionDTO getQuestionsByQuestionId(Long id);

    /**
     * 删除测评题目
     * @param id
     * @return
     */
    Boolean delQuestionsById(Long id);

    /**
     * 编辑测评题目
     * @param request
     * @return
     */
    Boolean editQuestions(EditHealthEvaluateQuestionRequest request);


}
