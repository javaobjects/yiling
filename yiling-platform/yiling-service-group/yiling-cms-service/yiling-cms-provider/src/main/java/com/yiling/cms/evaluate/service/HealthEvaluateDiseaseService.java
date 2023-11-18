package com.yiling.cms.evaluate.service;

import com.yiling.cms.evaluate.entity.HealthEvaluateDiseaseDO;
import com.yiling.framework.common.base.BaseService;

import java.util.List;

/**
 * <p>
 * 健康测评关联疾病 服务类
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-06
 */
public interface HealthEvaluateDiseaseService extends BaseService<HealthEvaluateDiseaseDO> {

    /**
     * 获取疾病
     * @param id
     * @return
     */
    List<Long> getByEvaluateId(Long id);
}
