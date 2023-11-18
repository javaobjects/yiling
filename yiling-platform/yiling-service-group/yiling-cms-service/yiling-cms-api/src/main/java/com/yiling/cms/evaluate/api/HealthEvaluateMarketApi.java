package com.yiling.cms.evaluate.api;

import com.yiling.cms.evaluate.dto.HealthEvaluateMarketDTO;
import com.yiling.cms.evaluate.dto.request.*;

import java.util.List;

/**
 * 健康测评营销API
 *
 * @author: fan.shen
 * @date: 2022/12/9
 */
public interface HealthEvaluateMarketApi {

    /**
     * 营销设置
     * @param request
     * @return
     */
    Boolean marketSet(HealthEvaluateMarketRequest request);

    /**
     * 删除
     * @param request
     * @return
     */
    Boolean delMarket(DelHealthEvaluateMarketRequest request);

    /**
     *
     * @param evaluateId
     * @return
     */
    HealthEvaluateMarketDTO getMarketListByEvaluateId(Long evaluateId);

    //
    // /**
    //  * 更新测评结果
    //  *
    //  * @param request
    //  * @return
    //  */
    // Boolean updateHealthEvaluateResult(UpdateHealthEvaluateResultRequest request);
    //
    // /**
    //  * 获取测评结果
    //  * @param evaluateId
    //  * @return
    //  */
    // List<HealthEvaluateResultDTO> getResultListByEvaluateId(Long evaluateId);
}
