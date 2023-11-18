package com.yiling.user.enterprise.api.impl;

import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.enterprise.api.EnterprisePurchaseApplyApi;
import com.yiling.user.enterprise.bo.EnterprisePurchaseApplyBO;
import com.yiling.user.enterprise.dto.request.AddPurchaseApplyRequest;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePurchaseApplyPageRequest;
import com.yiling.user.enterprise.dto.request.UpdatePurchaseApplyStatusRequest;
import com.yiling.user.enterprise.service.EnterprisePurchaseApplyService;

/**
 * 企业采购关系申请 API 实现
 *
 * @author: lun.yu
 * @date: 2022/01/17
 */
@DubboService
public class EnterprisePurchaseApplyApiImpl implements EnterprisePurchaseApplyApi {

    @Autowired
    private EnterprisePurchaseApplyService enterprisePurchaseApplyService;


    @Override
    public Page<EnterprisePurchaseApplyBO> pageList(QueryEnterprisePurchaseApplyPageRequest request) {
        return enterprisePurchaseApplyService.pageList(request);
    }

    @Override
    public boolean addPurchaseApply(AddPurchaseApplyRequest request) {
        return enterprisePurchaseApplyService.addPurchaseApply(request);
    }

    @Override
    public EnterprisePurchaseApplyBO getByCustomerEid(Long customerEid, Long eid) {
        return enterprisePurchaseApplyService.getByCustomerEid(customerEid,eid);
    }

    @Override
    public EnterprisePurchaseApplyBO getByEid(Long customerEid, Long eid) {
        return enterprisePurchaseApplyService.getByEid(customerEid,eid);
    }

    @Override
    public boolean updateAuthStatus(UpdatePurchaseApplyStatusRequest request) {
        return enterprisePurchaseApplyService.updateAuthStatus(request);
    }

    @Override
    public Map<Long,Integer> getPurchaseApplyStatus(List<Long> eidList, Long customerEid) {
        Assert.notNull(eidList, "参数eidList不能为空");
        Assert.notNull(customerEid, "参数customerEid不能为空");
        return enterprisePurchaseApplyService.getPurchaseApplyStatus(eidList,customerEid);
    }
}
