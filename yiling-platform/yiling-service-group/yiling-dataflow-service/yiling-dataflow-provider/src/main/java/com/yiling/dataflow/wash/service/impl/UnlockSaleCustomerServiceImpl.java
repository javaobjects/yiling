package com.yiling.dataflow.wash.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.agency.dto.request.QueryCrmEnterpriseByNamePageListRequest;
import com.yiling.dataflow.wash.bo.CrmEnterpriseBusinessRuleBO;
import com.yiling.dataflow.wash.dto.request.DeleteUnlockSaleCustomerRequest;
import com.yiling.dataflow.wash.dto.request.QueryCrmEnterpriseUnlockSalePageListRequest;
import com.yiling.dataflow.wash.dto.request.SaveUnlockSaleCustomerRequest;
import com.yiling.dataflow.wash.entity.UnlockSaleCustomerDO;
import com.yiling.dataflow.wash.dao.UnlockSaleCustomerMapper;
import com.yiling.dataflow.wash.service.UnlockSaleCustomerService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-04-25
 */
@Service
public class UnlockSaleCustomerServiceImpl extends BaseServiceImpl<UnlockSaleCustomerMapper, UnlockSaleCustomerDO> implements UnlockSaleCustomerService {

    @Override
    public List<Long> getCrmEnterpriseIdByRuleId(Long ruleId) {
        LambdaQueryWrapper<UnlockSaleCustomerDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UnlockSaleCustomerDO::getRuleId, ruleId);
        List<UnlockSaleCustomerDO> list = baseMapper.selectList(wrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return list.stream().map(e->e.getCrmEnterpriseId()).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(SaveUnlockSaleCustomerRequest requests) {
        List<UnlockSaleCustomerDO> list = new ArrayList<>();
        for (Long crmId:requests.getCrmEnterpriseIds()){
            UnlockSaleCustomerDO UnlockSaleBusinessDO=new UnlockSaleCustomerDO();
            UnlockSaleBusinessDO.setRuleId(requests.getRuleId());
            UnlockSaleBusinessDO.setCrmEnterpriseId(crmId);
            UnlockSaleBusinessDO.setOpUserId(requests.getOpUserId());
            list.add(UnlockSaleBusinessDO);
        }
        return this.saveOrUpdateBatch(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(DeleteUnlockSaleCustomerRequest request) {
        UnlockSaleCustomerDO unlockSaleBusinessDO=new UnlockSaleCustomerDO();
        unlockSaleBusinessDO.setOpUserId(request.getOpUserId());

        LambdaQueryWrapper<UnlockSaleCustomerDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(UnlockSaleCustomerDO::getId, request.getIds());
        return this.batchDeleteWithFill(unlockSaleBusinessDO,wrapper)>0;
    }

    @Override
    public Page<CrmEnterpriseBusinessRuleBO> page(QueryCrmEnterpriseUnlockSalePageListRequest request) {
        Page<CrmEnterpriseBusinessRuleBO> crmEnterpriseBusinessRuleBOPage=this.baseMapper.listPage(request,new Page<>(request.getCurrent(),request.getSize()));
        return crmEnterpriseBusinessRuleBOPage;
    }

}
