package com.yiling.marketing.integral.service;

import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.integral.entity.IntegralUseRuleDO;
import com.yiling.user.integral.bo.IntegralUseRuleDetailBO;

/**
 * <p>
 * 积分消耗规则表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-06
 */
public interface IntegralUseRuleService extends BaseService<IntegralUseRuleDO> {

    /**
     * 查看
     *
     * @param id
     * @return
     */
    IntegralUseRuleDetailBO get(Long id);

}
