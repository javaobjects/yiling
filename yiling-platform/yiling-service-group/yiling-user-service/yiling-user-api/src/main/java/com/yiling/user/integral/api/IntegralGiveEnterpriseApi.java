package com.yiling.user.integral.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.integral.dto.IntegralOrderGiveEnterpriseDTO;
import com.yiling.user.integral.dto.request.AddIntegralGiveEnterpriseRequest;
import com.yiling.user.integral.dto.request.DeleteIntegralGiveEnterpriseRequest;
import com.yiling.user.integral.dto.request.QueryIntegralGiveEnterprisePageRequest;

/**
 * 订单送积分-指定客户 Api
 *
 * @author: lun.yu
 * @date: 2023-01-04
 */
public interface IntegralGiveEnterpriseApi {

    /**
     * 添加客户
     *
     * @param request 添加客户内容
     * @return 成功/失败
     */
    boolean add(AddIntegralGiveEnterpriseRequest request);

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
    Page<IntegralOrderGiveEnterpriseDTO> pageList(QueryIntegralGiveEnterprisePageRequest request);

    /**
     * 根据发放规则id查询客户数量
     *
     * @param giveRuleId 发放规则id
     * @return 客户数量
     */
    Integer countGiveEnterpriseByRuleId(Long giveRuleId);

}
