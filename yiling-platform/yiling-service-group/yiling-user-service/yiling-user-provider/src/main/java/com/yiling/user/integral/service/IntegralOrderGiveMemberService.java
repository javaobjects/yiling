package com.yiling.user.integral.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.integral.dto.IntegralOrderGiveMemberDTO;
import com.yiling.user.integral.dto.request.AddIntegralGiveMemberRequest;
import com.yiling.user.integral.dto.request.DeleteIntegralGiveMemberRequest;
import com.yiling.user.integral.dto.request.QueryIntegralGiveMemberPageRequest;
import com.yiling.user.integral.entity.IntegralGiveRuleDO;
import com.yiling.user.integral.entity.IntegralOrderGiveMemberDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 订单送积分-用户类型指定会员方案表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-29
 */
public interface IntegralOrderGiveMemberService extends BaseService<IntegralOrderGiveMemberDO> {

    /**
     * 根据规则ID获取会员方案
     *
     * @param giveRuleId
     * @return
     */
    List<Long> getByGiveRuleId(Long giveRuleId);

    /**
     * 订单送积分-添加会员方案
     *
     * @param request 添加会员方案
     * @return 成功/失败
     */
    boolean add(AddIntegralGiveMemberRequest request);

    /**
     * 订单送积分-删除会员方案
     *
     * @param request 删除会员方案
     * @return 成功/失败
     */
    boolean delete(DeleteIntegralGiveMemberRequest request);

    /**
     * 订单送积分-查询指定方案会员分页列表
     *
     * @param request 查询条件
     * @return 已添加会员方案列表
     */
    Page<IntegralOrderGiveMemberDO> pageList(QueryIntegralGiveMemberPageRequest request);

    /**
     * 订单送积分-根据发放规则id查询指定方案会员数量
     *
     * @param giveRuleId 发放规则id
     * @return 指定方案会员数量
     */
    Integer countMemberByRuleId(Long giveRuleId);

    /**
     * 订单送积分-复制会员方案
     *
     * @param giveRuleDO
     * @param oldId
     * @param opUserId
     */
    void copy(IntegralGiveRuleDO giveRuleDO, Long oldId, Long opUserId);

    /**
     * 订单送积分-根据发放规则id查询会员方案
     *
     * @param giveRuleId
     * @return
     */
    List<IntegralOrderGiveMemberDO> listMemberByRuleId(Long giveRuleId);

    /**
     * 订单送积分-根据发放规则id和指定会员方案查询
     *
     * @param giveRuleId
     * @param memberId
     * @return
     */
    IntegralOrderGiveMemberDO queryByRuleIdAndMemberId(Long giveRuleId, Long memberId);
}
