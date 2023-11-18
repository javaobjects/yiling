package com.yiling.user.integral.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.integral.api.IntegralGiveSellerApi;
import com.yiling.user.integral.dto.IntegralOrderSellerDTO;
import com.yiling.user.integral.dto.request.AddIntegralOrderSellerRequest;
import com.yiling.user.integral.dto.request.DeleteIntegralGiveSellerRequest;
import com.yiling.user.integral.dto.request.QueryGiveIntegralSellerPageRequest;
import com.yiling.user.integral.entity.IntegralOrderSellerDO;
import com.yiling.user.integral.service.IntegralOrderSellerService;

import lombok.RequiredArgsConstructor;

/**
 * 订单送积分-商家范围Api
 *
 * @author: lun.yu
 * @date: 2023-01-03
 */
@DubboService
public class IntegralGiveSellerApiImpl implements IntegralGiveSellerApi {

    @Autowired
    private IntegralOrderSellerService integralOrderSellerService;

    @Override
    public boolean add(AddIntegralOrderSellerRequest request) {
        return integralOrderSellerService.add(request);
    }

    @Override
    public boolean delete(DeleteIntegralGiveSellerRequest request) {
        return integralOrderSellerService.delete(request);
    }

    @Override
    public Page<IntegralOrderSellerDTO> pageList(QueryGiveIntegralSellerPageRequest request) {
        return integralOrderSellerService.pageList(request);
    }

    @Override
    public Integer countSellerByGiveRuleId(Long giveRuleId) {
        return integralOrderSellerService.countSellerByGiveRuleId(giveRuleId);
    }

    @Override
    public List<IntegralOrderSellerDTO> listSellerByGiveRuleId(Long giveRuleId) {
        List<IntegralOrderSellerDO> doList = integralOrderSellerService.listSellerByGiveRuleId(giveRuleId);
        return PojoUtils.map(doList, IntegralOrderSellerDTO.class);
    }

    @Override
    public List<IntegralOrderSellerDTO> listByRuleIdAndEidList(Long giveRuleId, List<Long> eidList) {
        List<IntegralOrderSellerDO> sellerDOList = integralOrderSellerService.listByRuleIdAndEidList(giveRuleId, eidList);
        return PojoUtils.map(sellerDOList, IntegralOrderSellerDTO.class);
    }
}
