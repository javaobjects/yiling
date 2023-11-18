package com.yiling.cms.evaluate.service;

import com.yiling.cms.evaluate.dto.HealthEvaluateResultDTO;
import com.yiling.cms.evaluate.dto.request.AddHealthEvaluateResultRequest;
import com.yiling.cms.evaluate.dto.request.UpdateHealthEvaluateResultRequest;
import com.yiling.cms.evaluate.entity.HealthEvaluateResultDO;
import com.yiling.framework.common.base.BaseService;

import java.util.List;

/**
 * <p>
 * 健康测评结果表 服务类
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-06
 */
public interface HealthEvaluateResultService extends BaseService<HealthEvaluateResultDO> {

    /**
     * 添加测评结果
     * @param request
     * @return
     */
    Boolean addHealthEvaluateResult(AddHealthEvaluateResultRequest request);

    /**
     * 更新测评结果
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
