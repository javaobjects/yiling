package com.yiling.marketing.integral.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.integral.bo.IntegralRuleItemBO;
import com.yiling.user.integral.bo.IntegralUseRuleDetailBO;
import com.yiling.user.integral.dto.IntegralUseRuleDTO;
import com.yiling.user.integral.dto.request.QueryIntegralRulePageRequest;
import com.yiling.user.integral.dto.request.SaveIntegralRuleBasicRequest;
import com.yiling.user.integral.dto.request.UpdateRuleStatusRequest;

/**
 * 积分消耗规则 API
 *
 * @author: lun.yu
 * @date: 2023-01-06
 */
public interface IntegralUseRuleApi {

    /**
     * 查看
     *
     * @param id
     * @return
     */
    IntegralUseRuleDetailBO get(Long id);

}
