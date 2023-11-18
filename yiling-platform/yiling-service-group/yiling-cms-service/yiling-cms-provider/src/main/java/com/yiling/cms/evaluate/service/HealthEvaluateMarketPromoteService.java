package com.yiling.cms.evaluate.service;

import com.yiling.cms.evaluate.dto.HealthEvaluateMarketGoodsDTO;
import com.yiling.cms.evaluate.dto.HealthEvaluateMarketPromoteDTO;
import com.yiling.cms.evaluate.dto.request.HealthEvaluateMarketPromoteRequest;
import com.yiling.cms.evaluate.entity.HealthEvaluateMarketPromoteDO;
import com.yiling.framework.common.base.BaseService;

import java.util.List;

/**
 * <p>
 * 健康测评关联推广服务表 服务类
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-06
 */
public interface HealthEvaluateMarketPromoteService extends BaseService<HealthEvaluateMarketPromoteDO> {

    /**
     * 添加
     * @param request
     * @return
     */
    Long healthEvaluateMarketPromote(HealthEvaluateMarketPromoteRequest request);

    /**
     * 删除
     * @param id
     * @return
     */
    Boolean delHealthEvaluateMarketPromoteById(Long id);

    /**
     *
     * @param evaluateId
     * @return
     */
    List<HealthEvaluateMarketPromoteDTO> getMarketPromoteByEvaluateId(Long evaluateId);
}
