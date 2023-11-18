package com.yiling.dataflow.order.api.impl;

import java.util.List;

import com.yiling.dataflow.order.bo.FlowStatisticsBO;
import com.yiling.dataflow.order.dto.request.QueryFlowStatisticesRequest;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.order.api.FlowGoodsBatchDetailApi;
import com.yiling.dataflow.order.dto.FlowGoodsBatchDetailDTO;
import com.yiling.dataflow.order.dto.request.DeleteFlowGoodsBatchDetailRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsBatchDetailExistRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsBatchDetailListPageRequest;
import com.yiling.dataflow.order.dto.request.SaveOrUpdateFlowGoodsBatchDetailRequest;
import com.yiling.dataflow.order.service.FlowGoodsBatchDetailService;
import com.yiling.framework.common.util.PojoUtils;

/**
 * @author: shuang.zhang
 * @date: 2022/2/14
 */
@DubboService
public class FlowGoodsBatchDetailApiImpl implements FlowGoodsBatchDetailApi {

    @Autowired
    private FlowGoodsBatchDetailService flowGoodsBatchDetailService;

    @Override
    public Page<FlowGoodsBatchDetailDTO> page(QueryFlowGoodsBatchDetailListPageRequest request) {
        return PojoUtils.map(flowGoodsBatchDetailService.page(request),FlowGoodsBatchDetailDTO.class);
    }

    @Override
    public Boolean updateFlowGoodsBatchDetailByIds(List<SaveOrUpdateFlowGoodsBatchDetailRequest> requestList) {
        return flowGoodsBatchDetailService.updateFlowGoodsBatchDetailByIds(requestList);
    }

    @Override
    public Integer deleteFlowGoodsBatchDetailByEidAndDate(DeleteFlowGoodsBatchDetailRequest request) {
        return flowGoodsBatchDetailService.deleteFlowGoodsBatchDetailByEidAndDate(request);
    }

    @Override
    public Integer addFlowGoodsBatchDetailList(List<SaveOrUpdateFlowGoodsBatchDetailRequest> requestList) {
        return flowGoodsBatchDetailService.addFlowGoodsBatchDetailList(requestList);
    }

    @Override
    public void statisticsFlowGoodsBatch(List<Long> suIdList) {
        flowGoodsBatchDetailService.statisticsFlowGoodsBatch(suIdList);
    }

//    @Override
//    public void updateFlowGoodsBatchDetailCrmGoodsSign(List<Long> eids) {
//        flowGoodsBatchDetailService.updateFlowGoodsBatchDetailCrmGoodsSign(eids);
//    }

    /*@Override
    public Date getMaxUpdateTime(QueryMaxUpdateTimeRequest request) {
        return flowGoodsBatchDetailService.getMaxUpdateTime(request);
    }*/

    @Override
    public boolean isHaveDataByEidAndGbDetailTime(QueryFlowGoodsBatchDetailExistRequest request) {
        return flowGoodsBatchDetailService.isHaveDataByEidAndGbDetailTime(request);
    }

    @Override
    public List<FlowStatisticsBO> getFlowGoodsBatchStatistics(QueryFlowStatisticesRequest request) {
        return flowGoodsBatchDetailService.getFlowGoodsBatchStatistics(request);
    }
}
