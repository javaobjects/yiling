package com.yiling.user.integral.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.integral.dto.request.AddIntegralGiveEnterpriseRequest;
import com.yiling.user.integral.dto.request.DeleteIntegralGiveEnterpriseRequest;
import com.yiling.user.integral.dto.request.QueryIntegralGiveEnterprisePageRequest;
import com.yiling.user.integral.entity.IntegralGiveRuleDO;
import com.yiling.user.integral.entity.IntegralOrderGiveEnterpriseDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 订单送积分-指定客户表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-29
 */
public interface IntegralOrderGiveEnterpriseService extends BaseService<IntegralOrderGiveEnterpriseDO> {

    /**
     * 添加客户
     *
     * @param request 添加客户内容
     * @return 成功/失败
     */
    boolean add(AddIntegralGiveEnterpriseRequest request);

    /**
     * 复制客户
     *
     * @param giveRuleDO 发放规则主信息
     * @param oldId 被拷贝的发放规则id
     * @param opUserId 操作人
     */
    void copy(IntegralGiveRuleDO giveRuleDO, Long oldId, Long opUserId);

    /**
     * 删除客户
     *
     * @param request 删除客户内容
     * @return 成功/失败
     */
    boolean delete(DeleteIntegralGiveEnterpriseRequest request);

    /**
     * 订单送积分-已添加客户列表查询
     *
     * @param request 查询条件
     * @return 已添加客户列表
     */
    Page<IntegralOrderGiveEnterpriseDO> pageList(QueryIntegralGiveEnterprisePageRequest request);

    /**
     * 根据发放规则id查询客户范围数量
     *
     * @param giveRuleId 发放规则id
     * @return 客户范围数量
     */
    Integer countGiveEnterpriseByRuleId(Long giveRuleId);

    /**
     * 根据发放规则id和企业id查询指定客户信息
     *
     * @param giveRuleId 发放规则id
     * @param eid 企业id
     * @return 指定客户
     */
    IntegralOrderGiveEnterpriseDO queryByRuleIdAndEid(Long giveRuleId, Long eid);

    /**
     * 根据发放规则id和企业id集合查询指定商户信息
     *
     * @param giveRuleId 发放规则id
     * @param eidList 企业id集合
     * @return 指定客户
     */
    List<IntegralOrderGiveEnterpriseDO> listByRuleIdAndEidList(Long giveRuleId, List<Long> eidList);

}
