package com.yiling.user.integral.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.integral.bo.IntegralRuleItemBO;
import com.yiling.user.integral.dto.request.QueryIntegralRulePageRequest;
import com.yiling.user.integral.entity.IntegralGiveRuleDO;
import com.yiling.framework.common.base.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 积分发放规则表 Dao 接口
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-29
 */
@Repository
public interface IntegralGiveRuleMapper extends BaseMapper<IntegralGiveRuleDO> {

    /**
     * 积分发放规则分页列表
     *
     * @param page
     * @param request
     * @return
     */
    Page<IntegralRuleItemBO> queryListPage(Page page, @Param("request") QueryIntegralRulePageRequest request);

}
