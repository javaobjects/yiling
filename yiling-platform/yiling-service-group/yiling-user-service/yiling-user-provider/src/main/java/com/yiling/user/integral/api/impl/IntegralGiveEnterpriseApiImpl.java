package com.yiling.user.integral.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.integral.api.IntegralGiveEnterpriseApi;
import com.yiling.user.integral.dto.IntegralOrderGiveEnterpriseDTO;
import com.yiling.user.integral.dto.request.AddIntegralGiveEnterpriseRequest;
import com.yiling.user.integral.dto.request.DeleteIntegralGiveEnterpriseRequest;
import com.yiling.user.integral.dto.request.QueryIntegralGiveEnterprisePageRequest;
import com.yiling.user.integral.entity.IntegralOrderGiveEnterpriseDO;
import com.yiling.user.integral.service.IntegralOrderGiveEnterpriseService;

/**
 * 订单送积分-指定客户 Api实现
 *
 * @author: lun.yu
 * @date: 2023-01-04
 */
@DubboService
public class IntegralGiveEnterpriseApiImpl implements IntegralGiveEnterpriseApi {

    @Autowired
    IntegralOrderGiveEnterpriseService integralOrderGiveEnterpriseService;

    @Override
    public boolean add(AddIntegralGiveEnterpriseRequest request) {
        return integralOrderGiveEnterpriseService.add(request);
    }

    @Override
    public boolean delete(DeleteIntegralGiveEnterpriseRequest request) {
        return integralOrderGiveEnterpriseService.delete(request);
    }

    @Override
    public Page<IntegralOrderGiveEnterpriseDTO> pageList(QueryIntegralGiveEnterprisePageRequest request) {
        Page<IntegralOrderGiveEnterpriseDO> doPage = integralOrderGiveEnterpriseService.pageList(request);
        return PojoUtils.map(doPage, IntegralOrderGiveEnterpriseDTO.class);
    }

    @Override
    public Integer countGiveEnterpriseByRuleId(Long giveRuleId) {
        return integralOrderGiveEnterpriseService.countGiveEnterpriseByRuleId(giveRuleId);
    }

}
