package com.yiling.cms.evaluate.service;

import com.yiling.cms.evaluate.dto.HealthEvaluateLineDTO;
import com.yiling.cms.evaluate.entity.HealthEvaluateLineDO;
import com.yiling.framework.common.base.BaseService;

import java.util.List;

/**
 * <p>
 * 健康测评引用业务线表 服务类
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-06
 */
public interface HealthEvaluateLineService extends BaseService<HealthEvaluateLineDO> {

    /**
     * 获取业务线
     * @param id
     * @return
     */
    List<Long> getByHealthEvaluateId(Long id);

    /**
     * 根据业务线id获取测评idlist
     * @param code
     * @return
     */
    List<Long> getByLineId(Integer code);

    /**
     * 获取业务线
     * @param evaluateIdList
     * @return
     */
    List<HealthEvaluateLineDTO> getByHealthEvaluateIdList(List<Long> evaluateIdList);
}
