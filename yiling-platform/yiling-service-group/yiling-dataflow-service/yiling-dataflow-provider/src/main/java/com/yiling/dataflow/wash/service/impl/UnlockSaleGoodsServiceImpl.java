package com.yiling.dataflow.wash.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.dto.request.QueryCrmGoodsInfoPageRequest;
import com.yiling.dataflow.wash.bo.UnlockSaleGoodsBO;
import com.yiling.dataflow.wash.dto.request.DeleteUnlockSaleGoodsRequest;
import com.yiling.dataflow.wash.dto.request.SaveUnlockSaleGoodsRequest;
import com.yiling.dataflow.wash.entity.UnlockSaleGoodsDO;
import com.yiling.dataflow.wash.dao.UnlockSaleGoodsMapper;
import com.yiling.dataflow.wash.service.UnlockSaleGoodsService;
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
public class UnlockSaleGoodsServiceImpl extends BaseServiceImpl<UnlockSaleGoodsMapper, UnlockSaleGoodsDO> implements UnlockSaleGoodsService {

    @Override
    public List<Long> getCrmGoodsCodeByRuleId(Long ruleId) {
        LambdaQueryWrapper<UnlockSaleGoodsDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UnlockSaleGoodsDO::getRuleId, ruleId);
        List<UnlockSaleGoodsDO> list = baseMapper.selectList(wrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return list.stream().map(e->e.getCrmGoodsCode()).collect(Collectors.toList());
    }

    @Override
    public boolean save(SaveUnlockSaleGoodsRequest request) {
        List<UnlockSaleGoodsDO> list = new ArrayList<>();
        for (Long crmCode:request.getGoodsCodes()){
            UnlockSaleGoodsDO UnlockSaleBusinessDO=new UnlockSaleGoodsDO();
            UnlockSaleBusinessDO.setRuleId(request.getRuleId());
            UnlockSaleBusinessDO.setCrmGoodsCode(crmCode);
            UnlockSaleBusinessDO.setOpUserId(request.getOpUserId());
            list.add(UnlockSaleBusinessDO);
        }
        return this.saveOrUpdateBatch(list);
    }

    @Override
    public boolean delete(DeleteUnlockSaleGoodsRequest request) {
        UnlockSaleGoodsDO unlockSaleGoodsDO=new UnlockSaleGoodsDO();
        unlockSaleGoodsDO.setOpUserId(request.getOpUserId());

        LambdaQueryWrapper<UnlockSaleGoodsDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(UnlockSaleGoodsDO::getId, request.getIds());
        return this.batchDeleteWithFill(unlockSaleGoodsDO,wrapper)>0;
    }

    @Override
    public Page<UnlockSaleGoodsBO> page(QueryCrmGoodsInfoPageRequest request) {
        return this.baseMapper.listPage(request,new Page<>(request.getCurrent(),request.getSize()));
    }


}
