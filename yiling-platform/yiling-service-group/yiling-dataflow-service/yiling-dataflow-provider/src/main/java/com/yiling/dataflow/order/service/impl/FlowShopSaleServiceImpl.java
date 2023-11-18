package com.yiling.dataflow.order.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.order.dao.FlowShopSaleMapper;
import com.yiling.dataflow.order.dto.request.DeleteFlowShopSaleByUnlockRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowShopSaleListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowShopSaleRequest;
import com.yiling.dataflow.order.dto.request.SaveOrUpdateFlowShopSaleRequest;
import com.yiling.dataflow.order.entity.FlowShopSaleDO;
import com.yiling.dataflow.order.service.FlowShopSaleService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;

/**
 * <p>
 * 流向销售明细信息表 服务实现类
 * </p>
 *
 * @author houjie.sun
 * @date 2023-04-06
 */
@Service
public class FlowShopSaleServiceImpl extends BaseServiceImpl<FlowShopSaleMapper, FlowShopSaleDO> implements FlowShopSaleService {

    @Override
    public Page<FlowShopSaleDO> page(QueryFlowShopSaleListPageRequest request) {
        if (ObjectUtil.isNotNull(request.getStartTime())) {
            request.setStartTime(DateUtil.beginOfDay(request.getStartTime()));
        }
        if (ObjectUtil.isNotNull(request.getEndTime())) {
            request.setEndTime(DateUtil.endOfDay(request.getEndTime()));
        }
        return this.baseMapper.page(request.getPage(), request);
    }

    @Override
    public List<FlowShopSaleDO> getFlowSaleDTOBySoIdAndEid(QueryFlowShopSaleRequest request) {
        QueryWrapper<FlowShopSaleDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FlowShopSaleDO::getSoId, request.getSoId())
                .eq(FlowShopSaleDO::getEid, request.getEid());

        return this.list(queryWrapper);
    }

    @Override
    public Integer updateDataTagByIdList(List<Long> idList, Integer dataTag) {
        LambdaQueryWrapper<FlowShopSaleDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(FlowShopSaleDO::getId, idList);

        FlowShopSaleDO flowShopSaleDO = new FlowShopSaleDO();
        flowShopSaleDO.setDataTag(dataTag);
        flowShopSaleDO.setUpdateUser(0L);
        flowShopSaleDO.setUpdateTime(new Date());
        return baseMapper.update(flowShopSaleDO, queryWrapper);
    }

    @Override
    public Integer deleteByIdList(List<Long> idList) {
        Assert.notEmpty(idList, "参数 idList 不能为空");
        LambdaQueryWrapper<FlowShopSaleDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(FlowShopSaleDO::getId, idList);

        FlowShopSaleDO flowShopSaleDO = new FlowShopSaleDO();
        flowShopSaleDO.setUpdateUser(0L);
        flowShopSaleDO.setUpdateTime(new Date());

        return this.batchDeleteWithFill(flowShopSaleDO, queryWrapper);
    }

    @Override
    public FlowShopSaleDO insertFlowSale(SaveOrUpdateFlowShopSaleRequest request) {
        FlowShopSaleDO flowShopSaleDO = PojoUtils.map(request, FlowShopSaleDO.class);
        this.save(flowShopSaleDO);
        return flowShopSaleDO;
    }

    @Override
    public Boolean updateFlowSaleById(SaveOrUpdateFlowShopSaleRequest request) {
        FlowShopSaleDO entity = PojoUtils.map(request, FlowShopSaleDO.class);
        return this.updateById(entity);
    }

    @Override
    public Integer deleteFlowShopSaleBydEidAndSoTime(DeleteFlowShopSaleByUnlockRequest request) {
        return this.baseMapper.deleteFlowShopSaleBydEidAndSoTime(request);
    }
}
