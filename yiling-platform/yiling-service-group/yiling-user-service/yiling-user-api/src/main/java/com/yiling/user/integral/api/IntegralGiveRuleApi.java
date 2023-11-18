package com.yiling.user.integral.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.integral.bo.IntegralGiveRuleDetailBO;
import com.yiling.user.integral.bo.IntegralRuleItemBO;
import com.yiling.user.integral.dto.GenerateMultipleConfigDTO;
import com.yiling.user.integral.dto.IntegralGiveRuleDTO;
import com.yiling.user.integral.dto.IntegralOrderGiveConfigDTO;
import com.yiling.user.integral.dto.IntegralOrderGiveMultipleConfigDTO;
import com.yiling.user.integral.dto.request.QueryIntegralGiveMatchRuleRequest;
import com.yiling.user.integral.dto.request.QueryIntegralRulePageRequest;
import com.yiling.user.integral.dto.request.SaveIntegralRuleBasicRequest;
import com.yiling.user.integral.dto.request.SaveIntegralMultipleConfigRequest;
import com.yiling.user.integral.dto.request.SaveOrderGiveIntegralRequest;
import com.yiling.user.integral.dto.request.SaveSignPeriodRequest;
import com.yiling.user.integral.dto.request.UpdateRuleStatusRequest;

/**
 * 积分发放规则 API
 *
 * @author: lun.yu
 * @date: 2022-12-29
 */
public interface IntegralGiveRuleApi {

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
     * 保存签到周期
     *
     * @param request
     * @return
     */
    boolean saveSignPeriod(SaveSignPeriodRequest request);

    /**
     * 保存订单送积分配置
     *
     * @param request
     * @return
     */
    boolean saveOrderGiveIntegral(SaveOrderGiveIntegralRequest request);

    /**
     * 生成订单送积分倍数配置信息
     *
     * @param giveRuleId
     * @return
     */
    List<GenerateMultipleConfigDTO> generateMultipleConfig(Long giveRuleId);

    /**
     * 保存订单积分倍数配置
     *
     * @param request
     * @return
     */
    boolean saveMultipleConfig(SaveIntegralMultipleConfigRequest request);

    /**
     * 查看
     *
     * @param id
     * @return
     */
    IntegralGiveRuleDetailBO get(Long id);

    /**
     * 根据ID获取发放规则
     *
     * @param id
     * @return
     */
    IntegralGiveRuleDTO getById(Long id);

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

    /**
     * 自动匹配订单送积分的倍数规则
     *
     * @param request
     * @return
     */
    GenerateMultipleConfigDTO autoMatchRule(QueryIntegralGiveMatchRuleRequest request);
}
