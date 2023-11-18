package com.yiling.settlement.report.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.order.api.FlowPurchaseInventoryApi;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseInventorySettlementDetailRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseInventorySettlementRequest;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.framework.common.exception.ServiceException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.settlement.report.bo.FlowPurchaseInventoryBO;
import com.yiling.settlement.report.bo.QueryFlowPurchaseInventoryBO;
import com.yiling.settlement.report.bo.RebatedGoodsCountBO;
import com.yiling.settlement.report.dao.ReportDetailFlowMapper;
import com.yiling.settlement.report.dto.ReportDetailFlowDTO;
import com.yiling.settlement.report.dto.request.QueryReportDetailFlowPageRequest;
import com.yiling.settlement.report.dto.request.QueryStockOccupyPageRequest;
import com.yiling.settlement.report.entity.ReportDetailFlowDO;
import com.yiling.settlement.report.enums.ReportPurchaseChannelEnum;
import com.yiling.settlement.report.enums.ReportRebateStatusEnum;
import com.yiling.settlement.report.service.ReportDetailFlowService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dexi.yao
 * @date 2022-05-20
 */
@Slf4j
@Service
public class ReportDetailFlowServiceImpl extends BaseServiceImpl<ReportDetailFlowMapper, ReportDetailFlowDO> implements ReportDetailFlowService {

    @DubboReference(timeout = 60 * 1000)
    FlowPurchaseInventoryApi flowPurchaseInventoryApi;

    @Override
    public Map<String, FlowPurchaseInventoryBO> rejectB2bDetail(Long reportId, Long eid) {
        LambdaQueryWrapper<ReportDetailFlowDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ReportDetailFlowDO::getReportId, reportId);

        List<ReportDetailFlowDO> details = ListUtil.toList();
        List<QueryFlowPurchaseInventoryBO> queryInventoryRequest = ListUtil.toList();

        //待查询库存列表
        List<FlowPurchaseInventoryBO> inventoryList = ListUtil.toList();

        //分页查询符合返利条件的订单
        int current = 1;
        Page<ReportDetailFlowDO> orderPage;
        QueryPageListRequest queryRequest = new QueryPageListRequest();
        //分页查询订单列表
        do {
            queryRequest.setCurrent(current);
            queryRequest.setSize(100);
            //分页查询符合结算条件的订单
            orderPage = page(queryRequest.getPage(), wrapper);
            if (CollUtil.isEmpty(orderPage.getRecords())) {
                break;
            }
            List<ReportDetailFlowDO> records = orderPage.getRecords();
            if (CollUtil.isNotEmpty(records)) {
                for (ReportDetailFlowDO detail : records) {

                    QueryFlowPurchaseInventoryBO var = new QueryFlowPurchaseInventoryBO();
                    var.setEid(eid);
                    var.setYlGoodsId(detail.getYlGoodsId());
                    var.setGoodsInSn(detail.getGoodsInSn());
                    //如果待查询集合中没有此商品
                    if (!queryInventoryRequest.contains(var)) {
                        queryInventoryRequest.add(var);
                    }
                    details.add(detail);
                }
            }
            current = current + 1;
        } while (orderPage != null && CollUtil.isNotEmpty(orderPage.getRecords()));

        //查询库存
        if (CollUtil.isNotEmpty(queryInventoryRequest)) {
            //调用接口查询
            List<QueryFlowPurchaseInventorySettlementDetailRequest> list = PojoUtils.map(queryInventoryRequest, QueryFlowPurchaseInventorySettlementDetailRequest.class);
            QueryFlowPurchaseInventorySettlementRequest request = new QueryFlowPurchaseInventorySettlementRequest();
            request.setList(list);
            List<com.yiling.dataflow.order.bo.FlowPurchaseInventoryBO> inventory = flowPurchaseInventoryApi.getListByEidAndYlGoodsIdAndGoodsInSn(request);
            List<FlowPurchaseInventoryBO> queryResult = PojoUtils.map(inventory, FlowPurchaseInventoryBO.class);
            //放入库存列表
            inventoryList.addAll(queryResult);
        }
        Map<String, FlowPurchaseInventoryBO> inventoryMap = resolveInventory(inventoryList);
        if (CollUtil.isEmpty(details)) {
            return MapUtil.newHashMap();
        }

        //退回库存
        details.forEach(e -> {
            String key = FlowPurchaseInventoryBO.getKey(eid, e.getYlGoodsId(), e.getGoodsInSn());
            FlowPurchaseInventoryBO inventoryBO = inventoryMap.get(key);
            //如果没有采购库存信息则忽略
            if (ObjectUtil.isNull(inventoryBO)) {
                return;
            }
            //大运河
            if (ObjectUtil.equal(ReportPurchaseChannelEnum.DYH.getCode(), e.getPurchaseChannel())) {
                inventoryBO.setDyhOpQuantity(inventoryBO.getDyhOpQuantity() + e.getSoQuantity().longValue());
            }
            //京东
            if (ObjectUtil.equal(ReportPurchaseChannelEnum.JD.getCode(), e.getPurchaseChannel())) {
                inventoryBO.setJdOpQuantity(inventoryBO.getJdOpQuantity() + e.getSoQuantity().longValue());
            }
        });
        return inventoryMap;
    }

    @Override
    public Page<ReportDetailFlowDTO> queryReportDetailFlowPage(QueryReportDetailFlowPageRequest request) {
        LambdaQueryWrapper<ReportDetailFlowDO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(ReportDetailFlowDO::getReportId, request.getReportIdList());
        wrapper.like(StrUtil.isNotBlank(request.getEnterpriseName()), ReportDetailFlowDO::getEnterpriseName, request.getEnterpriseName());
        wrapper.eq(ObjectUtil.isNotNull(request.getIdentificationStatus()) && ObjectUtil.notEqual(request.getIdentificationStatus(), 0), ReportDetailFlowDO::getIdentificationStatus, request.getIdentificationStatus());
        wrapper.eq(ObjectUtil.isNotNull(request.getRebateStatus()) && ObjectUtil.notEqual(request.getRebateStatus(), 0), ReportDetailFlowDO::getRebateStatus, request.getRebateStatus());
        if (ObjectUtil.isNotNull(request.getStartSoTime())) {
            wrapper.ge(ReportDetailFlowDO::getSoTime, request.getStartSoTime());
        }
        if (ObjectUtil.isNotNull(request.getEndSoTime())) {
            wrapper.le(ReportDetailFlowDO::getSoTime, DateUtil.endOfDay(request.getEndSoTime()));
        }
        Page<ReportDetailFlowDO> page = page(request.getPage(), wrapper);
        return PojoUtils.map(page, ReportDetailFlowDTO.class);
    }

    @Override
    public Long queryFlowRebateOrderCount(QueryReportDetailFlowPageRequest request) {
        return baseMapper.queryReportFlowOrderPageGroupByOrder(request.getPage(), request).getTotal();
    }

    @Override
    public List<ReportDetailFlowDTO> queryFlowDetailByReportId(Long reportId, ReportRebateStatusEnum rebateStatusEnum) {
        LambdaQueryWrapper<ReportDetailFlowDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ReportDetailFlowDO::getReportId, reportId);
        wrapper.eq(ObjectUtil.isNotNull(rebateStatusEnum), ReportDetailFlowDO::getRebateStatus, rebateStatusEnum.getCode());
        List<ReportDetailFlowDO> list = list(wrapper);

        return PojoUtils.map(list, ReportDetailFlowDTO.class);
    }

    @Override
    public List<RebatedGoodsCountBO> queryRebateGoods(List<QueryStockOccupyPageRequest> request) {
        return baseMapper.queryRebateGoods(request);
    }

    /**
     * 解析库存
     *
     * @param inventoryList
     * @return
     */
    private Map<String, FlowPurchaseInventoryBO> resolveInventory(List<FlowPurchaseInventoryBO> inventoryList) {
        HashMap<String, FlowPurchaseInventoryBO> map = MapUtil.newHashMap();
        inventoryList.forEach(e -> {
            String key = FlowPurchaseInventoryBO.getKey(e.getEid(), e.getYlGoodsId(), e.getGoodsInSn());
            if (map.containsKey(key)) {
                log.error("查询采购库存时数据key存在重复，key={}", key);
                throw new ServiceException("返利报表保存失败");
            }
            map.put(key, e);
        });
        return map;
    }
}
