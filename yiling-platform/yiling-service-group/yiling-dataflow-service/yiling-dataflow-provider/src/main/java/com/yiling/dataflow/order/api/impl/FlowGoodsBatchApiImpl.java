package com.yiling.dataflow.order.api.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.order.api.FlowGoodsBatchApi;
import com.yiling.dataflow.order.dto.FlowGoodsBatchDTO;
import com.yiling.dataflow.order.dto.request.DeleteFlowGoodsBatchRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsBatchListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsBatchRequest;
import com.yiling.dataflow.order.dto.request.SaveOrUpdateFlowGoodsBatchRequest;
import com.yiling.dataflow.order.service.FlowGoodsBatchService;
import com.yiling.dataflow.order.util.FlowCommonUtil;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;

/**
 * @author: shuang.zhang
 * @date: 2022/2/14
 */
@DubboService
public class FlowGoodsBatchApiImpl implements FlowGoodsBatchApi {

    @Autowired
    private FlowGoodsBatchService flowGoodsBatchService;

    @Override
    public Integer deleteFlowGoodsBatchByGbIdNoAndEid(DeleteFlowGoodsBatchRequest request) {
        return flowGoodsBatchService.deleteFlowGoodsBatchByGbIdNoAndEid(request);
    }

    @Override
    public Integer deleteByIdList(List<Long> idList) {
        Assert.notEmpty(idList, "参数 idList 不能为空");
        return flowGoodsBatchService.deleteByIdList(idList);
    }

    @Override
    public List<FlowGoodsBatchDTO> getFlowGoodsBatchDTOByGbIdNoAndEid(QueryFlowGoodsBatchRequest request) {
        return PojoUtils.map(flowGoodsBatchService.getFlowGoodsBatchDTOByGbIdNoAndEid(request), FlowGoodsBatchDTO.class);
    }

    @Override
    public List<FlowGoodsBatchDTO> getFlowGoodsBatchDTOByInSnAndEid(QueryFlowGoodsBatchRequest request) {
        return PojoUtils.map(flowGoodsBatchService.getFlowGoodsBatchDTOByInSnAndEid(request), FlowGoodsBatchDTO.class);
    }

    @Override
    public Integer updateFlowGoodsBatchByGbIdNoAndEid(SaveOrUpdateFlowGoodsBatchRequest request) {
        return flowGoodsBatchService.updateFlowGoodsBatchByGbIdNoAndEid(request);
    }

    @Override
    public Integer updateFlowGoodsBatchByInSnAndEid(SaveOrUpdateFlowGoodsBatchRequest request) {
        return flowGoodsBatchService.updateFlowGoodsBatchByInSnAndEid(request);
    }

    @Override
    public Integer insertFlowGoodsBatch(SaveOrUpdateFlowGoodsBatchRequest request) {
        return flowGoodsBatchService.insertFlowGoodsBatch(request);
    }

    @Override
    public Page<FlowGoodsBatchDTO> page(QueryFlowGoodsBatchListPageRequest request) {
        Page<FlowGoodsBatchDTO> page = PojoUtils.map(flowGoodsBatchService.page(request), FlowGoodsBatchDTO.class);
        if (ObjectUtil.isNotNull(page) && CollUtil.isNotEmpty(page.getRecords())) {
            page.getRecords().forEach(p -> {
                p.setGbTime(FlowCommonUtil.parseFlowDefaultTime(p.getGbTime()));
                p.setGbProduceTime(p.getGbProduceTime());
                p.setGbEndTime(p.getGbEndTime());
            });
        }
        return page;
    }

    @Override
    public Integer deleteFlowGoodsBatchByEids(List<Long> eids, Date createTime) {
        return flowGoodsBatchService.deleteFlowGoodsBatchByEids(eids,createTime);
    }

    @Override
    public Boolean updateFlowGoodsBatchTotalNumberByIdList(List<Long> idList, BigDecimal totalNumber) {
        Assert.notNull(idList, "参数 idList 不能为空");
        Assert.notNull(totalNumber, "参数 totalNumber 不能为空");
        return flowGoodsBatchService.updateFlowGoodsBatchTotalNumberByIdList(idList, totalNumber);
    }

    @Override
    public Boolean updateFlowGoodsBatchById(SaveOrUpdateFlowGoodsBatchRequest request) {
        Assert.notNull(request, "参数 request 不能为空");
        Assert.notNull(request.getId(), "参数 id 不能为空");
        return flowGoodsBatchService.updateFlowGoodsBatchById(request);
    }

    @Override
    public void statisticsFlowGoodsBatchTotalNumber() {
        flowGoodsBatchService.statisticsFlowGoodsBatchTotalNumber();
    }

    @Override
    public void syncFlowGoodsBatchSpec() {
        flowGoodsBatchService.syncFlowGoodsBatchSpec();
    }

    @Override
    public void updateFlowSaleCrmGoodsSign(List<Long> eids) {
        flowGoodsBatchService.updateFlowGoodsBatchCrmGoodsSign(eids);
    }

    @Override
    public Date getMaxGbTimeByEid(Long eid) {
        return flowGoodsBatchService.getMaxGbTimeByEid(eid);
    }
}
