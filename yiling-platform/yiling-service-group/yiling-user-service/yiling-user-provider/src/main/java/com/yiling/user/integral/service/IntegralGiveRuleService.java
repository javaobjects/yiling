package com.yiling.user.integral.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.integral.bo.IntegralGiveRuleDetailBO;
import com.yiling.user.integral.bo.IntegralRuleItemBO;
import com.yiling.user.integral.dto.IntegralGiveRuleDTO;
import com.yiling.user.integral.dto.request.QueryIntegralRulePageRequest;
import com.yiling.user.integral.dto.request.SaveIntegralRuleBasicRequest;
import com.yiling.user.integral.dto.request.UpdateRuleStatusRequest;
import com.yiling.user.integral.entity.IntegralGiveRuleDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 积分发放规则表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-29
 */
public interface IntegralGiveRuleService extends BaseService<IntegralGiveRuleDO> {

    /**
     * 积分发放规则分页列表
     *
     * @param request
     * @return
     */
    Page<IntegralRuleItemBO> queryListPage(QueryIntegralRulePageRequest request);

    /**
     * 停用规则
     *
     * @param request
     * @return
     */
    boolean updateStatus(UpdateRuleStatusRequest request);

    /**
     * 保存积分发放规则基本信息
     *
     * @param request
     * @return
     */
    IntegralGiveRuleDTO saveBasic(SaveIntegralRuleBasicRequest request);

    /**
     * 获取所有有效的发放规则（未结束且状态为启用）
     *
     * @return
     */
    List<IntegralGiveRuleDTO> getValidGiveRuleList();

    /**
     * 获取所有进行中的发放规则
     *
     * @return
     */
    List<IntegralGiveRuleDTO> getDoingGiveRuleList();

    /**
     * 查看
     *
     * @param id
     * @return
     */
    IntegralGiveRuleDetailBO get(Long id);

    /**
     * 复制发放规则
     *
     * @param id
     * @param opUserId
     * @return
     */
    Long copy(Long id, Long opUserId);

    /**
     * 获取当前正在生效的积分发放规则
     *
     * @param platform
     * @return
     */
    List<IntegralGiveRuleDTO> getCurrentValidRule(Integer platform);

}
