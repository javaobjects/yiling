package com.yiling.cms.evaluate.service;

import com.yiling.cms.evaluate.dto.HealthEvaluateMarketGoodsDTO;
import com.yiling.cms.evaluate.dto.request.HealthEvaluateMarketGoodsRequest;
import com.yiling.cms.evaluate.entity.HealthEvaluateMarketGoodsDO;
import com.yiling.framework.common.base.BaseService;

import java.util.List;

/**
 * <p>
 * 健康测评关联营销商品表 服务类
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-06
 */
public interface HealthEvaluateMarketGoodsService extends BaseService<HealthEvaluateMarketGoodsDO> {

    /**
     * 营销商品
     * @param request
     * @return
     */
    Long healthEvaluateMarketGoods(HealthEvaluateMarketGoodsRequest request);

    /**
     * 删除
     * @param id
     * @return
     */
    Boolean delHealthEvaluateMarketGoodsById(Long id);

    /**
     *
     * @param evaluateId
     * @return
     */
    List<HealthEvaluateMarketGoodsDTO> getMarketGoodsByEvaluateId(Long evaluateId);
}
