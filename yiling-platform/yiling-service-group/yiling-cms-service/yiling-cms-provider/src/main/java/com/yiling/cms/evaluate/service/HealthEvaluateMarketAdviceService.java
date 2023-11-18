package com.yiling.cms.evaluate.service;

import com.yiling.cms.evaluate.dto.HealthEvaluateMarketAdviceDTO;
import com.yiling.cms.evaluate.dto.request.HealthEvaluateMarketAdviceRequest;
import com.yiling.cms.evaluate.entity.HealthEvaluateMarketAdviceDO;
import com.yiling.framework.common.base.BaseService;

import java.util.List;

/**
 * <p>
 * 健康测评关联改善建议表 服务类
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-06
 */
public interface HealthEvaluateMarketAdviceService extends BaseService<HealthEvaluateMarketAdviceDO> {

    /**
     * 添加
     * @param request
     * @return
     */
    Long healthEvaluateMarketAdvice(HealthEvaluateMarketAdviceRequest request);

    /**
     * 删除
     * @param id
     * @return
     */
    Boolean delHealthEvaluateMarketAdviceById(Long id);

    List<HealthEvaluateMarketAdviceDTO> getMarketAdviceByEvaluateId(Long evaluateId);
}
