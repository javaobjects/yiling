package com.yiling.dataflow.gb.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.dataflow.gb.dao.GbAppealFlowRelatedMapper;
import com.yiling.dataflow.gb.dto.request.DeleteGbAppealFlowRelatedRequest;
import com.yiling.dataflow.gb.dto.request.SubstractGbAppealFlowRelatedRequest;
import com.yiling.dataflow.gb.entity.GbAppealFlowRelatedDO;
import com.yiling.dataflow.gb.service.GbAppealFlowRelatedService;
import com.yiling.framework.common.base.BaseServiceImpl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;

/**
 * <p>
 * 流向团购申诉申请关联流向表 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-05-12
 */
@Service
public class GbAppealFlowRelatedServiceImpl extends BaseServiceImpl<GbAppealFlowRelatedMapper, GbAppealFlowRelatedDO> implements GbAppealFlowRelatedService {

    @Override
    public List<GbAppealFlowRelatedDO> getListByAppealFormId(Long appealFormId) {
        Assert.notNull(appealFormId, "参数 appealFormId 不能为空");
        LambdaQueryWrapper<GbAppealFlowRelatedDO> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(GbAppealFlowRelatedDO::getAppealFormId, appealFormId);
        List<GbAppealFlowRelatedDO> list = this.list(lambdaQueryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return list;
    }

    @Override
    public List<GbAppealFlowRelatedDO> getByAppealFormIdList(List<Long> appealFormIdList) {
        Assert.notEmpty(appealFormIdList, "参数 appealFormIdList 不能为空");
        LambdaQueryWrapper<GbAppealFlowRelatedDO> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.in(GbAppealFlowRelatedDO::getAppealFormId, appealFormIdList);
        List<GbAppealFlowRelatedDO> list = this.list(lambdaQueryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return list;
    }

    @Override
    public GbAppealFlowRelatedDO getByAppealFormIdAndFlowWashId(Long appealFormId, Long flowWashId) {
        Assert.notNull(appealFormId, "参数 appealFormId 不能为空");
        Assert.notNull(flowWashId, "参数 flowWashId 不能为空");
        LambdaQueryWrapper<GbAppealFlowRelatedDO> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(GbAppealFlowRelatedDO::getAppealFormId, appealFormId);
        lambdaQueryWrapper.eq(GbAppealFlowRelatedDO::getFlowWashId, flowWashId);
        return this.getOne(lambdaQueryWrapper);
    }

    @Override
    public boolean updateById(SubstractGbAppealFlowRelatedRequest request) {
        GbAppealFlowRelatedDO entity = new GbAppealFlowRelatedDO();
        entity.setId(request.getId());
        entity.setMatchQuantity(request.getSubstractQuantity());
        entity.setOpUserId(request.getOpUserId());
        entity.setOpTime(request.getOpTime());
        return this.updateById(entity);
    }

    @Override
    public int deleteById(DeleteGbAppealFlowRelatedRequest request) {
        Assert.notNull(request.getId(), "参数 id 不能为空");
        GbAppealFlowRelatedDO entity = new GbAppealFlowRelatedDO();
        entity.setId(request.getId());
        entity.setOpUserId(request.getOpUserId());
        entity.setOpTime(request.getOpTime());
        return this.deleteByIdWithFill(entity);
    }

    @Override
    public int countByAppealFormId(Long appealFormId) {
        Assert.notNull(appealFormId, "参数 appealFormId 不能为空");
        LambdaQueryWrapper<GbAppealFlowRelatedDO> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(GbAppealFlowRelatedDO::getAppealFormId, appealFormId);
        return this.count(lambdaQueryWrapper);
    }
}
