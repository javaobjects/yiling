package com.yiling.cms.evaluate.api;

import com.yiling.cms.evaluate.dto.HealthEvaluateResultDTO;
import com.yiling.cms.evaluate.dto.request.*;

import java.util.List;

/**
 * 健康测评结果API
 *
 * @author: fan.shen
 * @date: 2022/12/8
 */
public interface HealthEvaluateResultApi {

    /**
     * 添加健康测评结果
     *
     * @param addHealthEvaluateResultRequest
     * @return
     */
    Boolean addHealthEvaluateResult(AddHealthEvaluateResultRequest addHealthEvaluateResultRequest);

    /**
     * 更新测评结果
     *
     * @param request
     * @return
     */
    Boolean updateHealthEvaluateResult(UpdateHealthEvaluateResultRequest request);

    /**
     * 删除测评结果
     * @param id
     * @return
     */
    Boolean delHealthEvaluateResultById(Long id);

    /**
     * 获取测评结果
     * @param evaluateId
     * @return
     */
    List<HealthEvaluateResultDTO> getResultListByEvaluateId(Long evaluateId);
}
