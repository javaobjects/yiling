package com.yiling.dataflow.gb.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.dataflow.gb.bo.GbAppealFormFlowCountBO;
import com.yiling.dataflow.gb.dao.GbAppealFlowStatisticMapper;
import com.yiling.dataflow.gb.dto.request.SaveOrUpdateGbAppealFlowStatisticRequest;
import com.yiling.dataflow.gb.dto.request.SubstractGbAppealFlowStatisticRequest;
import com.yiling.dataflow.gb.entity.GbAppealFlowStatisticDO;
import com.yiling.dataflow.gb.service.GbAppealFlowStatisticService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;

/**
 * <p>
 * 流向团购统计表 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-05-12
 */
@Service
public class GbAppealFlowStatisticServiceImpl extends BaseServiceImpl<GbAppealFlowStatisticMapper, GbAppealFlowStatisticDO> implements GbAppealFlowStatisticService {

    @Override
    public boolean saveOrUpdateByFlowWashId(SaveOrUpdateGbAppealFlowStatisticRequest saveOrUpdateGbAppealFlowStatisticRequest) {
        LambdaQueryWrapper<GbAppealFlowStatisticDO> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(GbAppealFlowStatisticDO::getFlowWashId, saveOrUpdateGbAppealFlowStatisticRequest.getFlowWashId());
        GbAppealFlowStatisticDO gbAppealFlowStatisticDO = this.getOne(lambdaQueryWrapper);

        if (gbAppealFlowStatisticDO == null) {
            gbAppealFlowStatisticDO = PojoUtils.map(saveOrUpdateGbAppealFlowStatisticRequest, GbAppealFlowStatisticDO.class);
            this.save(gbAppealFlowStatisticDO);
        }
        return true;
    }

    @Override
    public List<GbAppealFlowStatisticDO> getListByFlowWashIds(List<Long> flowWashIds) {
        LambdaQueryWrapper<GbAppealFlowStatisticDO> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.in(GbAppealFlowStatisticDO::getFlowWashId, flowWashIds);
        lambdaQueryWrapper.orderByDesc(GbAppealFlowStatisticDO::getUnMatchQuantity);
        return this.list(lambdaQueryWrapper);
    }

    @Override
    public int substract(SubstractGbAppealFlowStatisticRequest request) {
        return this.baseMapper.substract(request);
    }

    @Override
    public GbAppealFormFlowCountBO getFlowCountByGbAppealFormId(Long gbAppealFormId) {
        Assert.notNull(gbAppealFormId, "参数 gbAppealFormId 不能为空");
        return this.baseMapper.getFlowCountByGbAppealFormId(gbAppealFormId);
    }

    @Override
    public GbAppealFlowStatisticDO getByFlowWashId(Long flowWashId) {
        Assert.notNull(flowWashId, "参数 flowWashId 不能为空");
        LambdaQueryWrapper<GbAppealFlowStatisticDO> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(GbAppealFlowStatisticDO::getFlowWashId, flowWashId);
        return this.getOne(lambdaQueryWrapper);
    }

    @Override
    public List<GbAppealFlowStatisticDO> getByFlowWashIdList(List<Long> flowWashIdList) {
        Assert.notEmpty(flowWashIdList, "参数 flowWashIdList 不能为空");
        LambdaQueryWrapper<GbAppealFlowStatisticDO> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.in(GbAppealFlowStatisticDO::getFlowWashId, flowWashIdList);
        List<GbAppealFlowStatisticDO> list = this.list(lambdaQueryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return list;
    }

}
