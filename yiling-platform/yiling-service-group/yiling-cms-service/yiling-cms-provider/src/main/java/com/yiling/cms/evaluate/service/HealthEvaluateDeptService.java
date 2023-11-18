package com.yiling.cms.evaluate.service;

import com.yiling.cms.evaluate.entity.HealthEvaluateDeptDO;
import com.yiling.framework.common.base.BaseService;

import java.util.List;

/**
 * <p>
 * 健康测评关联科室表 服务类
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-12
 */
public interface HealthEvaluateDeptService extends BaseService<HealthEvaluateDeptDO> {

    /**
     * 获取部门
     * @param id
     * @return
     */
    List<Long> getByEvaluateId(Long id);
}
