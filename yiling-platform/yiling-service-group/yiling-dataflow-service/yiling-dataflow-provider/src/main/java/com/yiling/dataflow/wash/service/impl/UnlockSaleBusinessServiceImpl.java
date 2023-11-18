package com.yiling.dataflow.wash.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.agency.dto.request.QueryCrmAgencyPageListRequest;
import com.yiling.dataflow.agency.dto.request.QueryCrmEnterpriseByNamePageListRequest;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.wash.bo.CrmEnterpriseBusinessRuleBO;
import com.yiling.dataflow.wash.dao.UnlockSaleBusinessMapper;
import com.yiling.dataflow.wash.dto.request.DeleteUnlockSaleBusinessRequest;
import com.yiling.dataflow.wash.dto.request.QueryCrmEnterpriseUnlockSalePageListRequest;
import com.yiling.dataflow.wash.dto.request.SaveUnlockSaleBusinessRequest;
import com.yiling.dataflow.wash.entity.UnlockSaleBusinessDO;
import com.yiling.dataflow.wash.service.UnlockSaleBusinessService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-04-25
 */
@Service
public class UnlockSaleBusinessServiceImpl extends BaseServiceImpl<UnlockSaleBusinessMapper, UnlockSaleBusinessDO> implements UnlockSaleBusinessService {

    @Override
    public List<Long> getCrmEnterpriseIdByRuleId(Long ruleId) {
        LambdaQueryWrapper<UnlockSaleBusinessDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UnlockSaleBusinessDO::getRuleId, ruleId);
        List<UnlockSaleBusinessDO> list = baseMapper.selectList(wrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return list.stream().map(e -> e.getCrmEnterpriseId()).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(SaveUnlockSaleBusinessRequest requests) {
        List<UnlockSaleBusinessDO> list = new ArrayList<>();
        for (Long crmId:requests.getCrmEnterpriseIds()){
            UnlockSaleBusinessDO UnlockSaleBusinessDO=new UnlockSaleBusinessDO();
            UnlockSaleBusinessDO.setRuleId(requests.getRuleId());
            UnlockSaleBusinessDO.setCrmEnterpriseId(crmId);
            UnlockSaleBusinessDO.setOpUserId(requests.getOpUserId());
            list.add(UnlockSaleBusinessDO);
        }
        return this.saveOrUpdateBatch(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(DeleteUnlockSaleBusinessRequest request) {
        UnlockSaleBusinessDO unlockSaleBusinessDO=new UnlockSaleBusinessDO();
        unlockSaleBusinessDO.setOpUserId(request.getOpUserId());

        LambdaQueryWrapper<UnlockSaleBusinessDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(UnlockSaleBusinessDO::getId, request.getIds());
        return this.batchDeleteWithFill(unlockSaleBusinessDO,wrapper)>0;
    }

    @Override
    public Page<CrmEnterpriseBusinessRuleBO> page(QueryCrmEnterpriseUnlockSalePageListRequest request) {
        Page<CrmEnterpriseBusinessRuleBO> crmEnterpriseBusinessRuleBOPage=this.baseMapper.listPage(request,new Page<>(request.getCurrent(),request.getSize()));
        return crmEnterpriseBusinessRuleBOPage;
    }
}
