package com.yiling.cms.evaluate.service;

import com.yiling.cms.evaluate.dto.HealthEvaluateQuestionDTO;
import com.yiling.cms.evaluate.dto.request.AddHealthEvaluateQuestionRequest;
import com.yiling.cms.evaluate.dto.request.EditHealthEvaluateQuestionRequest;
import com.yiling.cms.evaluate.entity.HealthEvaluateQuestionDO;
import com.yiling.framework.common.base.BaseService;

import java.util.List;

/**
 * <p>
 * 健康测评题目表 服务类
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-06
 */
public interface HealthEvaluateQuestionService extends BaseService<HealthEvaluateQuestionDO> {

    /**
     * 添加题目
     * @param request
     * @return
     */
    Boolean addHealthEvaluateQuestion(AddHealthEvaluateQuestionRequest request);

    /**
     * 获取测评题目信息
     * @param evaluateId
     * @return
     */
    List<HealthEvaluateQuestionDTO> getHealthEvaluateQuestionByEvaluateId(Long evaluateId);

    /**
     * 获取测评题目信息
     * @param evaluateIdList
     * @return
     */
    List<HealthEvaluateQuestionDTO> getHealthEvaluateQuestionByEvaluateIdList(List<Long> evaluateIdList);

    /**
     * 获取测评全部题目信息
     * @param id
     * @return
     */
    List<HealthEvaluateQuestionDTO> getFullQuestionsByHealthEvaluateId(Long id);

    /**
     * 删除题目
     * @param id
     * @return
     */
    Boolean delQuestionsById(Long id);

    /**
     * 编辑题目
     * @param request
     * @return
     */
    Boolean editQuestions(EditHealthEvaluateQuestionRequest request);

    /**
     * 获取题目信息
     * @param id
     * @return
     */
    HealthEvaluateQuestionDTO getQuestionsByQuestionId(Long id);
}
