package com.yiling.dataflow.wash.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.dto.request.QueryCrmGoodsCategoryRequest;
import com.yiling.dataflow.wash.bo.UnlockSaleGoodsCategoryBO;
import com.yiling.dataflow.wash.dto.request.DeleteUnlockSaleGoodsCategoryRequest;
import com.yiling.dataflow.wash.dto.request.QueryUnlockSaleGoodsCategoryPageRequest;
import com.yiling.dataflow.wash.dto.request.SaveUnlockSaleGoodsCategoryRequest;
import com.yiling.dataflow.wash.entity.UnlockSaleGoodsCategoryDO;
import com.yiling.dataflow.wash.dao.UnlockSaleGoodsCategoryMapper;
import com.yiling.dataflow.wash.service.UnlockSaleGoodsCategoryService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-04-25
 */
@Service
public class UnlockSaleGoodsCategoryServiceImpl extends BaseServiceImpl<UnlockSaleGoodsCategoryMapper, UnlockSaleGoodsCategoryDO> implements UnlockSaleGoodsCategoryService {

    @Override
    public List<Long> getCrmGoodsCodeByRuleId(Long ruleId) {
        LambdaQueryWrapper<UnlockSaleGoodsCategoryDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UnlockSaleGoodsCategoryDO::getRuleId, ruleId);
        List<UnlockSaleGoodsCategoryDO> list = baseMapper.selectList(wrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return list.stream().map(e->e.getGoodsCategoryId()).collect(Collectors.toList());
    }

    @Override
    public boolean save(SaveUnlockSaleGoodsCategoryRequest request) {
        List<UnlockSaleGoodsCategoryDO> list = new ArrayList<>();
        for (Long crmCode:request.getGoodsCategoryIds()){
            UnlockSaleGoodsCategoryDO UnlockSaleBusinessDO=new UnlockSaleGoodsCategoryDO();
            UnlockSaleBusinessDO.setRuleId(request.getRuleId());
            UnlockSaleBusinessDO.setGoodsCategoryId(crmCode);
            UnlockSaleBusinessDO.setOpUserId(request.getOpUserId());
            list.add(UnlockSaleBusinessDO);
        }
        return this.saveOrUpdateBatch(list);
    }

    @Override
    public boolean delete(DeleteUnlockSaleGoodsCategoryRequest request) {
        UnlockSaleGoodsCategoryDO unlockSaleGoodsDO=new UnlockSaleGoodsCategoryDO();
        unlockSaleGoodsDO.setOpUserId(request.getOpUserId());

        LambdaQueryWrapper<UnlockSaleGoodsCategoryDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(UnlockSaleGoodsCategoryDO::getId, request.getIds());
        return this.batchDeleteWithFill(unlockSaleGoodsDO,wrapper)>0;
    }

    @Override
    public Page<UnlockSaleGoodsCategoryBO> page(QueryUnlockSaleGoodsCategoryPageRequest request) {
        return this.baseMapper.listPage(request,new Page<>(request.getCurrent(),request.getSize()));
    }
}
