package com.yiling.user.integral.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.integral.bo.IntegralRuleItemBO;
import com.yiling.user.integral.dto.request.QueryIntegralRulePageRequest;
import com.yiling.user.integral.entity.IntegralUseRuleDO;
import com.yiling.framework.common.base.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 积分消耗规则表 Dao 接口
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-06
 */
@Repository
public interface IntegralUseRuleMapper extends BaseMapper<IntegralUseRuleDO> {

    /**
     * 分页查询积分消耗规则
     *
     * @param page
     * @param request
     * @return
     */
    Page<IntegralRuleItemBO> queryListPage(Page page, @Param("request") QueryIntegralRulePageRequest request);
}
