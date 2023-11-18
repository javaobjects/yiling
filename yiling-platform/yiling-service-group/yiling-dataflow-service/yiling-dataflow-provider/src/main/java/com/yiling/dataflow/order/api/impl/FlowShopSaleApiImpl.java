package com.yiling.dataflow.order.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.order.api.FlowShopSaleApi;
import com.yiling.dataflow.order.dto.FlowShopSaleDTO;
import com.yiling.dataflow.order.dto.request.DeleteFlowShopSaleByUnlockRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowShopSaleListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowShopSaleRequest;
import com.yiling.dataflow.order.dto.request.SaveOrUpdateFlowShopSaleRequest;
import com.yiling.dataflow.order.service.FlowShopSaleService;
import com.yiling.dataflow.order.util.FlowCommonUtil;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;

/**
 * @author: houjie.sun
 * @date: 2023/4/10
 */
@DubboService
public class FlowShopSaleApiImpl implements FlowShopSaleApi {

    @Autowired
    private FlowShopSaleService flowShopSaleService;

    @Override
    public Page<FlowShopSaleDTO> page(QueryFlowShopSaleListPageRequest request) {
        Page<FlowShopSaleDTO> page = PojoUtils.map(flowShopSaleService.page(request), FlowShopSaleDTO.class);
        if (ObjectUtil.isNotNull(page) && CollUtil.isNotEmpty(page.getRecords())) {
            page.getRecords().forEach(p -> {
                p.setSoTime(FlowCommonUtil.parseFlowDefaultTime(p.getSoTime()));
                p.setSoProductTime(FlowCommonUtil.parseFlowDefaultTime(p.getSoProductTime()));
                p.setSoEffectiveTime(FlowCommonUtil.parseFlowDefaultTime(p.getSoEffectiveTime()));
            });
        }
        return page;
    }

    @Override
    public List<FlowShopSaleDTO> getFlowSaleDTOBySoIdAndEid(QueryFlowShopSaleRequest request) {
        return PojoUtils.map(flowShopSaleService.getFlowSaleDTOBySoIdAndEid(request), FlowShopSaleDTO.class);
    }

    @Override
    public Integer updateDataTagByIdList(List<Long> idList, Integer dataTag) {
        return flowShopSaleService.updateDataTagByIdList(idList, dataTag);
    }

    @Override
    public Integer deleteByIdList(List<Long> idList) {
        return flowShopSaleService.deleteByIdList(idList);
    }

    @Override
    public FlowShopSaleDTO insertFlowSale(SaveOrUpdateFlowShopSaleRequest request) {
        return PojoUtils.map(flowShopSaleService.insertFlowSale(request), FlowShopSaleDTO.class);
    }

    @Override
    public Boolean updateFlowSaleById(SaveOrUpdateFlowShopSaleRequest request) {
        Assert.notNull(request, "参数 request 不能为空");
        Assert.notNull(request.getId(), "参数 id 不能为空");
        return flowShopSaleService.updateFlowSaleById(request);
    }

    @Override
    public Integer deleteFlowShopSaleBydEidAndSoTime(DeleteFlowShopSaleByUnlockRequest request) {
        return flowShopSaleService.deleteFlowShopSaleBydEidAndSoTime(request);
    }
}
