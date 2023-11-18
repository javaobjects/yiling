package com.yiling.settlement.report.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.yiling.settlement.report.dao.ReportDetailB2bMapper;
import com.yiling.settlement.report.dto.ReportDetailB2bDTO;
import com.yiling.settlement.report.dto.request.QueryReportDetailB2bPageByReportIdRequest;
import com.yiling.settlement.report.dto.request.QueryReportDetailB2bPageRequest;
import com.yiling.settlement.report.dto.request.QueryStockOccupyPageRequest;
import com.yiling.settlement.report.entity.ReportDetailB2bDO;
import com.yiling.settlement.report.enums.ReportPurchaseChannelEnum;
import com.yiling.settlement.report.enums.ReportRebateStatusEnum;
import com.yiling.settlement.report.service.ReportDetailB2bService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 返利明细表-b2b订单 服务实现类
 * </p>
 *
 * @author dexi.yao
 * @date 2022-05-20
 */
@Slf4j
@Service
public class ReportDetailB2bServiceImpl extends BaseServiceImpl<ReportDetailB2bMapper, ReportDetailB2bDO> implements ReportDetailB2bService {

    @DubboReference(timeout = 10 * 1000)
    FlowPurchaseInventoryApi flowPurchaseInventoryApi;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, FlowPurchaseInventoryBO> rejectB2bDetail(Long reportId, Long eid) {
        LambdaQueryWrapper<ReportDetailB2bDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ReportDetailB2bDO::getReportId, reportId);

        List<ReportDetailB2bDO> details = ListUtil.toList();
        List<QueryFlowPurchaseInventoryBO> queryInventoryRequest = ListUtil.toList();

        //待查询库存列表
        List<FlowPurchaseInventoryBO> inventoryList = ListUtil.toList();

        //分页查询符合返利条件的订单
        int current = 1;
        Page<ReportDetailB2bDO> orderPage;
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
            List<ReportDetailB2bDO> records = orderPage.getRecords();
            if (CollUtil.isNotEmpty(records)) {
                for (ReportDetailB2bDO detail : records) {

                    QueryFlowPurchaseInventoryBO var = new QueryFlowPurchaseInventoryBO();
                    var.setEid(eid);
                    var.setYlGoodsId(detail.getGoodsId());
                    var.setGoodsInSn(detail.getGoodsErpCode());
                    //如果待查询集合中没有此商品且内码不为空
                    if (!queryInventoryRequest.contains(var) && StrUtil.isNotBlank(detail.getGoodsErpCode())) {
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
            String key = FlowPurchaseInventoryBO.getKey(eid, e.getGoodsId(), e.getGoodsErpCode());
            FlowPurchaseInventoryBO inventoryBO = inventoryMap.get(key);
            //如果没有库存则忽略
            if (ObjectUtil.isNull(inventoryBO)) {
                return;
            }
            //大运河
            if (ObjectUtil.equal(ReportPurchaseChannelEnum.DYH.getCode(), e.getPurchaseChannel())) {
                inventoryBO.setDyhOpQuantity(inventoryBO.getDyhOpQuantity() + e.getReceiveQuantity());
            }
            //京东
            if (ObjectUtil.equal(ReportPurchaseChannelEnum.JD.getCode(), e.getPurchaseChannel())) {
                inventoryBO.setJdOpQuantity(inventoryBO.getJdOpQuantity() + e.getReceiveQuantity());
            }
        });

        return inventoryMap;
    }

    @Override
    public Page<ReportDetailB2bDTO> queryReportDetailB2bPage(QueryReportDetailB2bPageRequest request) {
        LambdaQueryWrapper<ReportDetailB2bDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ReportDetailB2bDO::getReportId, request.getReportId());
        wrapper.eq(ObjectUtil.isNotNull(request.getOrderId()) && ObjectUtil.notEqual(request.getOrderId(), 0L), ReportDetailB2bDO::getOrderId, request.getOrderId());
        wrapper.in(CollUtil.isNotEmpty(request.getBuyerEidList()), ReportDetailB2bDO::getBuyerEid, request.getBuyerEidList());
        wrapper.eq(ObjectUtil.isNotNull(request.getIdentificationStatus()) && ObjectUtil.notEqual(request.getIdentificationStatus(), 0), ReportDetailB2bDO::getIdentificationStatus, request.getIdentificationStatus());
        wrapper.eq(ObjectUtil.isNotNull(request.getRebateStatus()) && ObjectUtil.notEqual(request.getRebateStatus(), 0), ReportDetailB2bDO::getRebateStatus, request.getRebateStatus());
        if (ObjectUtil.isNotNull(request.getStartCreateOrderTime())) {
            wrapper.ge(ReportDetailB2bDO::getOrderCreateTime, request.getStartCreateOrderTime());
        }
        if (ObjectUtil.isNotNull(request.getEndCreateOrderTime())) {
            wrapper.le(ReportDetailB2bDO::getOrderCreateTime, DateUtil.endOfDay(request.getEndCreateOrderTime()));
        }
        Page<ReportDetailB2bDO> page = page(request.getPage(), wrapper);
        return PojoUtils.map(page, ReportDetailB2bDTO.class);
    }

    @Override
    public Page<ReportDetailB2bDTO> queryReportDetailPageByReportIdList(QueryReportDetailB2bPageByReportIdRequest request) {
        LambdaQueryWrapper<ReportDetailB2bDO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(ReportDetailB2bDO::getReportId, request.getReportIdList());
        Page<ReportDetailB2bDO> page = page(request.getPage(), wrapper);
        return PojoUtils.map(page, ReportDetailB2bDTO.class);
    }

    @Override
    public Long queryB2bRebateOrderCount(QueryReportDetailB2bPageRequest request) {
        return baseMapper.queryReportB2bOrderPageGroupByOrder(request.getPage(), request).getTotal();
    }

    @Override
    public List<ReportDetailB2bDTO> queryB2bDetailByReportId(Long reportId, ReportRebateStatusEnum rebateStatusEnum) {
        LambdaQueryWrapper<ReportDetailB2bDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ReportDetailB2bDO::getReportId, reportId);
        wrapper.eq(ObjectUtil.isNotNull(rebateStatusEnum), ReportDetailB2bDO::getRebateStatus, rebateStatusEnum.getCode());
        List<ReportDetailB2bDO> list = list(wrapper);
        return PojoUtils.map(list, ReportDetailB2bDTO.class);
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
