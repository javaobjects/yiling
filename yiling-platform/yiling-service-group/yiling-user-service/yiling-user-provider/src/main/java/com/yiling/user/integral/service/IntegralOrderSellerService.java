package com.yiling.user.integral.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.user.integral.dto.IntegralOrderSellerDTO;
import com.yiling.user.integral.dto.request.AddIntegralOrderSellerRequest;
import com.yiling.user.integral.dto.request.DeleteIntegralGiveSellerRequest;
import com.yiling.user.integral.dto.request.QueryGiveIntegralSellerPageRequest;
import com.yiling.user.integral.entity.IntegralGiveRuleDO;
import com.yiling.user.integral.entity.IntegralOrderSellerDO;

/**
 * <p>
 * 订单送积分指定商家 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-03
 */
public interface IntegralOrderSellerService extends BaseService<IntegralOrderSellerDO> {

    /**
     * 订单送积分-已添加商家列表查询
     *
     * @param request
     * @return
     */
    Page<IntegralOrderSellerDTO> pageList(QueryGiveIntegralSellerPageRequest request);

    /**
     * 添加商家
     *
     * @param request
     * @return
     */
    boolean add(AddIntegralOrderSellerRequest request);

    /**
     * 根据企业ID查询订单送积分指定商家
     *
     * @param giveRuleId
     * @param eidList
     * @return
     */
    List<IntegralOrderSellerDO> listByRuleIdAndEidList(Long giveRuleId, List<Long> eidList);

    /**
     * 删除商家
     *
     * @param request
     * @return
     */
    boolean delete(DeleteIntegralGiveSellerRequest request);

    /**
     * 复制商家
     *
     * @param giveRuleDO 发放规则主信息
     * @param oldId 被拷贝的发放规则id
     * @param opUserId 操作人
     */
    void copy(IntegralGiveRuleDO giveRuleDO, Long oldId, Long opUserId);

    /**
     * 根据发放规则ID统计商家数量
     *
     * @param giveRuleId
     * @return
     */
    Integer countSellerByGiveRuleId(Long giveRuleId);

    /**
     * 根据发放规则id查询商家范围
     *
     * @param giveRuleId
     * @return
     */
    List<IntegralOrderSellerDO> listSellerByGiveRuleId(Long giveRuleId);

    /**
     * 根据发放规则id查询商家范围
     *
     * @param giveRuleIdList
     * @return
     */
    List<IntegralOrderSellerDO> listSellerByGiveRuleIdList(List<Long> giveRuleIdList);
}
