package com.yiling.dataflow.order.api.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.yiling.dataflow.order.bo.FlowStatisticsBO;
import com.yiling.dataflow.order.dto.request.QueryFlowStatisticesRequest;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.index.enums.IndexErrorEnum;
import com.yiling.dataflow.order.api.FlowSaleApi;
import com.yiling.dataflow.order.bo.FlowCrmGoodsBO;
import com.yiling.dataflow.order.bo.FlowSaleCurrentMonthCountBO;
import com.yiling.dataflow.order.dto.FlowOrderExportReportDetailDTO;
import com.yiling.dataflow.order.dto.FlowSaleDTO;
import com.yiling.dataflow.order.dto.request.DeleteFlowSaleByUnlockRequest;
import com.yiling.dataflow.order.dto.request.DeleteFlowSaleRequest;
import com.yiling.dataflow.order.dto.request.FlowSaleMonthCountRequest;
import com.yiling.dataflow.order.dto.request.QueryCrmGoodsRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowOrderExportReportPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowSaleExistRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowSaleRequest;
import com.yiling.dataflow.order.dto.request.SaveOrUpdateFlowSaleRequest;
import com.yiling.dataflow.order.dto.request.UpdateFlowIndexRequest;
import com.yiling.dataflow.order.service.FlowSaleService;
import com.yiling.dataflow.order.util.FlowCommonUtil;
import com.yiling.dataflow.relation.entity.FlowGoodsRelationDO;
import com.yiling.dataflow.relation.service.FlowGoodsRelationService;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

/**
 * @author: shuang.zhang
 * @date: 2022/2/14
 */
@DubboService
public class FlowSaleApiImpl implements FlowSaleApi {

    @Autowired
    private FlowSaleService             flowSaleService;
    @Autowired
    private FlowGoodsRelationService    flowGoodsRelationService;

    @Override
    public Integer deleteFlowSaleBySoIdAndEid(DeleteFlowSaleRequest request) {
        return flowSaleService.deleteFlowSaleBySoIdAndEid(request);
    }

    @Override
    public Integer updateDataTagByIdList(List<Long> idList, Integer dataTag) {
        return flowSaleService.updateDataTagByIdList(idList, dataTag);
    }

    @Override
    public Integer deleteByIdList(List<Long> idList) {
        Assert.notEmpty(idList, "参数 idList 不能为空");
        return flowSaleService.deleteByIdList(idList);
    }

    @Override
    public List<FlowSaleDTO> getFlowSaleDTOBySoIdAndEid(QueryFlowSaleRequest request) {
        return PojoUtils.map(flowSaleService.getFlowSaleDTOBySoIdAndEid(request), FlowSaleDTO.class);
    }

    @Override
    public Integer updateFlowSaleBySoIdAndEid(SaveOrUpdateFlowSaleRequest request) {
        return flowSaleService.updateFlowSaleBySoIdAndEid(request);
    }

    @Override
    public FlowSaleDTO insertFlowSale(SaveOrUpdateFlowSaleRequest request) {
        return PojoUtils.map(flowSaleService.insertFlowSale(request), FlowSaleDTO.class);
    }

    @Override
    public Integer updateFlowSaleByIds(List<SaveOrUpdateFlowSaleRequest> requestList) {
        return flowSaleService.updateFlowSaleByIds(requestList);
    }

    @Override
    public Page<FlowSaleDTO> page(QueryFlowPurchaseListPageRequest request) {
        Page<FlowSaleDTO> page = PojoUtils.map(flowSaleService.page(request), FlowSaleDTO.class);
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
    public List<FlowOrderExportReportDetailDTO> getOrderFlowReport(QueryFlowOrderExportReportPageRequest request) {
        return flowSaleService.getOrderFlowReport(request);
    }

    @Override
    public Integer deleteFlowSaleBydEidAndSoTime(DeleteFlowSaleByUnlockRequest request) {
        return flowSaleService.deleteFlowSaleBydEidAndSoTime(request);
    }

    @Override
    public Boolean updateFlowSaleById(SaveOrUpdateFlowSaleRequest request) {
        Assert.notNull(request, "参数 request 不能为空");
        Assert.notNull(request.getId(), "参数 id 不能为空");
        return flowSaleService.updateFlowSaleById(request);
    }

    @Override
    public Page<FlowSaleDTO> flowSaleYunCangPage(QueryFlowPurchaseListPageRequest request) {
        Assert.notNull(request, "参数 request 不能为空");
        Assert.notNull(request.getCurrent(), "参数 current 不能为空");
        Assert.notNull(request.getSize(), "参数 size 不能为空");
        Assert.notNull(request.getIdList(), "参数 idList 不能为空");
        request.getIdList().forEach(o -> Assert.notNull(o, "参数 idList 不能为空"));
        Page<FlowSaleDTO> page = PojoUtils.map(flowSaleService.flowSaleYunCangPage(request), FlowSaleDTO.class);
        if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
            return page;
        }
        List<Long> eidList = page.getRecords().stream().filter(o -> StrUtil.isNotBlank(o.getGoodsInSn())).map(FlowSaleDTO::getEid).distinct().collect(Collectors.toList());
        List<String> goodsInSnList = page.getRecords().stream().filter(o -> StrUtil.isNotBlank(o.getGoodsInSn())).map(FlowSaleDTO::getGoodsInSn).distinct().collect(Collectors.toList());
        if (CollUtil.isNotEmpty(eidList) && CollUtil.isNotEmpty(goodsInSnList)) {
            List<FlowGoodsRelationDO> flowGoodsRelationList = flowGoodsRelationService.getByEidAndGoodsInSn(eidList, goodsInSnList);
            if (CollUtil.isNotEmpty(flowGoodsRelationList)) {
                Map<String, FlowGoodsRelationDO> flowGoodsRelationMap = flowGoodsRelationList.stream().collect(Collectors.toMap(o -> o.getEid() + "_" + o.getGoodsInSn(), o -> o, (k1, k2) -> k1));
                for (FlowSaleDTO flowSale : page.getRecords()) {
                    String key = flowSale.getEid() + "_" + flowSale.getGoodsInSn();
                    FlowGoodsRelationDO flowGoodsRelationDO = flowGoodsRelationMap.get(key);
                    if (ObjectUtil.isNotNull(flowGoodsRelationDO)) {
                        flowSale.setYlGoodsId(flowGoodsRelationDO.getYlGoodsId());
                    }
                }
            }
        }
        return page;
    }

    @Override
    public void flowSaleReportSyncByEnterpriseTag() {
        flowSaleService.flowSaleReportSyncByEnterpriseTag();
    }

    @Override
    public Boolean updateReportSyncStatusByIdList(List<Long> idList) {
        return flowSaleService.updateReportSyncStatusByIdList(idList);
    }

    @Override
    public void syncFlowSaleSpec() {
        flowSaleService.syncFlowSaleSpec();
    }

    @Override
    public List<FlowSaleCurrentMonthCountBO> getMonthCount(FlowSaleMonthCountRequest request) {
        return flowSaleService.getMonthCount(request);
    }

    @Override
    public void updateFlowSaleCrmInnerSign(List<Long> eids) {
        flowSaleService.updateFlowSaleCrmInnerSign(eids);
    }

    @Override
    public void updateFlowSaleCrmSign(List<Long> crmIds) {
        flowSaleService.updateFlowSaleCrmSign(crmIds);
    }

    @Override
    public void updateFlowSaleCrmGoodsSign(List<Long> eids) {
        flowSaleService.updateFlowSaleCrmGoodsSign(eids);
    }

    @Override
    public Date getMaxSoTimeByEid(Long eid) {
        return flowSaleService.getMaxSoTimeByEid(eid);
    }

    @Override
    public int refreshFlowSale(UpdateFlowIndexRequest request) {
        try {
            return flowSaleService.refreshFlowSale(request);
        }catch (Exception e){
            throw new BusinessException(IndexErrorEnum.FLOW_SALE_ERROR);
        }

    }

    @Override
    public List<FlowCrmGoodsBO> getUnmappedCrmGoods(QueryCrmGoodsRequest request) {
        return flowSaleService.getUnmappedCrmGoods(request);
    }

    @Override
    public boolean isHaveDataByEidAndSoTime(QueryFlowSaleExistRequest request) {
        return flowSaleService.isHaveDataByEidAndSoTime(request);
    }

    @Override
    public List<FlowStatisticsBO> getFlowSaleStatistics(QueryFlowStatisticesRequest request) {
        return flowSaleService.getFlowSaleStatistics(request);
    }
}
