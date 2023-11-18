package com.yiling.dataflow.wash.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.dto.request.QueryCrmGoodsTagPageRequest;
import com.yiling.dataflow.wash.bo.UnlockSaleGoodsTagBO;
import com.yiling.dataflow.wash.dto.request.DeleteUnlockSaleGoodsTagRequest;
import com.yiling.dataflow.wash.dto.request.SaveUnlockSaleGoodsTagRequest;
import com.yiling.dataflow.wash.entity.UnlockSaleGoodsDO;
import com.yiling.dataflow.wash.entity.UnlockSaleGoodsTagDO;
import com.yiling.dataflow.wash.dao.UnlockSaleGoodsTagMapper;
import com.yiling.dataflow.wash.service.UnlockSaleGoodsTagService;
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
public class UnlockSaleGoodsTagServiceImpl extends BaseServiceImpl<UnlockSaleGoodsTagMapper, UnlockSaleGoodsTagDO> implements UnlockSaleGoodsTagService {

    @Override
    public List<Long> getCrmGoodsCodeByRuleId(Long ruleId) {
        LambdaQueryWrapper<UnlockSaleGoodsTagDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UnlockSaleGoodsTagDO::getRuleId, ruleId);
        List<UnlockSaleGoodsTagDO> list = baseMapper.selectList(wrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return list.stream().map(e->e.getGoodsTagId()).collect(Collectors.toList());
    }

    @Override
    public boolean save(SaveUnlockSaleGoodsTagRequest request) {
        List<UnlockSaleGoodsTagDO> list = new ArrayList<>();
        for (Long goodsTagId:request.getGoodsTagIds()){
            UnlockSaleGoodsTagDO unlockSaleGoodsTagDO=new UnlockSaleGoodsTagDO();
            unlockSaleGoodsTagDO.setRuleId(request.getRuleId());
            unlockSaleGoodsTagDO.setGoodsTagId(goodsTagId);
            unlockSaleGoodsTagDO.setOpUserId(request.getOpUserId());
            list.add(unlockSaleGoodsTagDO);
        }
        return this.saveOrUpdateBatch(list);
    }

    @Override
    public boolean delete(DeleteUnlockSaleGoodsTagRequest request) {
        UnlockSaleGoodsTagDO unlockSaleGoodsTagDO=new UnlockSaleGoodsTagDO();
        unlockSaleGoodsTagDO.setOpUserId(request.getOpUserId());

        LambdaQueryWrapper<UnlockSaleGoodsTagDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(UnlockSaleGoodsTagDO::getId, request.getIds());
        return this.batchDeleteWithFill(unlockSaleGoodsTagDO,wrapper)>0;
    }

    @Override
    public Page<UnlockSaleGoodsTagBO> page(QueryCrmGoodsTagPageRequest request) {
        return this.baseMapper.listPage(request,new Page<>(request.getCurrent(),request.getSize()));
    }
}
