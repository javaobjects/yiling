package com.yiling.settlement.report.service.impl;

import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.order.api.FlowSaleApi;
import com.yiling.dataflow.order.dto.FlowSaleDTO;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseListPageRequest;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.ServiceException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.settlement.report.dao.FlowSaleOrderSyncMapper;
import com.yiling.settlement.report.dto.ReportFlowSaleOrderSyncDTO;
import com.yiling.settlement.report.dto.request.QueryFlowOrderPageListRequest;
import com.yiling.settlement.report.dto.request.UpdateFlowOrderIdenRequest;
import com.yiling.settlement.report.dto.request.UpdateYlGoodsIdRequest;
import com.yiling.settlement.report.entity.FlowSaleOrderSyncDO;
import com.yiling.settlement.report.entity.LogDO;
import com.yiling.settlement.report.enums.ReportErrorCode;
import com.yiling.settlement.report.enums.ReportLogTypeEnum;
import com.yiling.settlement.report.enums.ReportOrderIdenEnum;
import com.yiling.settlement.report.enums.ReportOrderaAnormalReasonEnum;
import com.yiling.settlement.report.enums.ReportSettStatusEnum;
import com.yiling.settlement.report.enums.ReportStatusEnum;
import com.yiling.settlement.report.service.FlowSaleOrderSyncService;
import com.yiling.settlement.report.service.LogService;
import com.yiling.settlement.report.service.ParamSubGoodsService;
import com.yiling.user.enterprise.api.EnterpriseTagApi;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 流向销售明细信息同步表 服务实现类
 * </p>
 *
 * @author dexi.yao
 * @date 2022-05-25
 */
@Slf4j
@Service
public class FlowSaleOrderSyncServiceImpl extends BaseServiceImpl<FlowSaleOrderSyncMapper, FlowSaleOrderSyncDO> implements FlowSaleOrderSyncService {

    @Autowired
    ParamSubGoodsService paramSubGoodsService;
    @Autowired
    LogService logService;

    @DubboReference
    FlowSaleApi flowSaleApi;
    @DubboReference
    EnterpriseTagApi enterpriseTagApi;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean updateYlGoodsId(UpdateYlGoodsIdRequest request) {
        //更新参数表的以岭商品id
        Boolean isSuccess = paramSubGoodsService.updateYlGoodsId(request);
        if (!isSuccess) {
            log.error("更新ylGoodsId时,更新参数表的数据失败，参数={}", request);
            throw new ServiceException(ReportErrorCode.UPDATE_GOODS_ID_FAIL.getMessage());
        }
        LambdaQueryWrapper<FlowSaleOrderSyncDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(FlowSaleOrderSyncDO::getEid, request.getEid());
        wrapper.eq(FlowSaleOrderSyncDO::getYlGoodsId, request.getOldId());
        List<FlowSaleOrderSyncDO> list = list(wrapper);
        if (CollUtil.isEmpty(list)) {
            return Boolean.TRUE;
        }
        FlowSaleOrderSyncDO syncDO = new FlowSaleOrderSyncDO();
        syncDO.setYlGoodsId(request.getNewId());
        syncDO.setYlGoodsIdUpdateTime(new Date());
        syncDO.setYlGoodsIdOld(request.getOldId());
        isSuccess = update(syncDO, wrapper);
        if (!isSuccess) {
            log.error("更新ylGoodsId时,更新流向订单表的数据失败，参数={}", request);
            throw new ServiceException(ReportErrorCode.UPDATE_GOODS_ID_FAIL.getMessage());
        }
        return isSuccess;
    }

    @Override
    public List<ReportFlowSaleOrderSyncDTO> queryOrderListByIdList(List<Long> idList) {
        if (CollUtil.isEmpty(idList)) {
            return ListUtil.toList();
        }
        List<FlowSaleOrderSyncDO> list = listByIds(idList);
        return PojoUtils.map(list, ReportFlowSaleOrderSyncDTO.class);
    }

    @Override
    public Page<ReportFlowSaleOrderSyncDTO> queryFlowOrderPageList(QueryFlowOrderPageListRequest request) {
        if (CollUtil.isEmpty(request.getEidList())) {
            return request.getPage();
        }
        if (ObjectUtil.isNotNull(request.getEndSoTime())) {
            request.setEndSoTime(DateUtil.endOfDay(request.getEndSoTime()));
        }

        Page<ReportFlowSaleOrderSyncDTO> page = baseMapper.queryFlowOrderPageList(request.getPage(), request);
        List<ReportFlowSaleOrderSyncDTO> records = page.getRecords();
        if (CollUtil.isEmpty(records)) {
            return page;
        }
        List<ReportFlowSaleOrderSyncDTO> list = queryOrderListByIdList(records.stream().map(ReportFlowSaleOrderSyncDTO::getId).collect(Collectors.toList()));
        page.setRecords(list);
        return page;
    }

    @Override
    public Boolean syncFlowOrder(List<Long> flowIdList) {

        //分页查询符合返利条件的订单
        int current = 1;
        Page<ReportFlowSaleOrderSyncDTO> orderPage;
        QueryPageListRequest queryRequest = new QueryPageListRequest();
        //当前存在的订单
        List<ReportFlowSaleOrderSyncDTO> dbFlowList = ListUtil.toList();
        //待查询的的订单
        List<Long> queryFlowIdList = ListUtil.toList();

        //分页查询订单列表
        do {
            queryRequest.setCurrent(current);
            queryRequest.setSize(50);
            //分页查询符合结算条件的订单
            orderPage = baseMapper.queryFlowOrderList(queryRequest.getPage(), flowIdList);
            if (CollUtil.isEmpty(orderPage.getRecords())) {
                break;
            }
            List<ReportFlowSaleOrderSyncDTO> records = orderPage.getRecords();
            if (CollUtil.isNotEmpty(records)) {
                for (ReportFlowSaleOrderSyncDTO orderSync : records) {
                    dbFlowList.add(orderSync);
                    //需要更新的流向订单---待生成返利的才进行更新
                    if (ObjectUtil.equal(orderSync.getReportStatus(), 0) || ObjectUtil.equal(orderSync.getReportStatus(), ReportStatusEnum.OPERATOR_REJECT.getCode()) || ObjectUtil.equal(orderSync.getReportStatus(), ReportStatusEnum.FINANCE_REJECT.getCode())) {
                        queryFlowIdList.add(orderSync.getFlowId());
                    }
                }
            }
            current = current + 1;
        } while (orderPage != null && CollUtil.isNotEmpty(orderPage.getRecords()));
        Map<Long, ReportFlowSaleOrderSyncDTO> dbFlowMap = dbFlowList.stream().collect(Collectors.toMap(ReportFlowSaleOrderSyncDTO::getFlowId, e -> e));

        flowIdList.forEach(flowId -> {
            //新增的流向订单
            if (!dbFlowMap.containsKey(flowId)) {
                queryFlowIdList.add(flowId);
            }
        });

        //查询流向订单
        List<FlowSaleDTO> list = ListUtil.toList();
        List<Long> tempIdList;
        int queryFlowCurrent = 0;
        do {
            tempIdList = CollUtil.page(queryFlowCurrent, 5000, queryFlowIdList);

            List<FlowSaleDTO> tempPageFlow = queryFlowOrder(tempIdList);
            if (CollUtil.isNotEmpty(tempPageFlow)) {
                list.addAll(tempPageFlow);
            }

            queryFlowCurrent = queryFlowCurrent + 1;
        } while (CollUtil.isNotEmpty(tempIdList));

        //删除了的流向订单数据
        List<Long> deleteFlowList = ListUtil.toList();

        Map<Long, FlowSaleDTO> newFlowMap = list.stream().collect(Collectors.toMap(FlowSaleDTO::getId, e -> e));
        dbFlowList.forEach(e -> {
            FlowSaleDTO newFlowOrder = newFlowMap.get(e.getFlowId());
            //如果已经同步的流向订单没有生成返利且再次查询没查到视为已删除
            if (ObjectUtil.equal(e.getReportStatus(), 0) && ObjectUtil.isNull(newFlowMap)) {
                deleteFlowList.add(e.getId());
            }
        });
        //如果存在需要删除的订单
        if (CollUtil.isNotEmpty(deleteFlowList)) {
            FlowSaleOrderSyncDO deleteDO = new FlowSaleOrderSyncDO();
            deleteDO.setOpTime(new Date());
            LambdaQueryWrapper<FlowSaleOrderSyncDO> deleteWrapper = Wrappers.lambdaQuery();
            deleteWrapper.in(FlowSaleOrderSyncDO::getId, deleteFlowList);
            int rows = batchDeleteWithFill(deleteDO, deleteWrapper);
            if (rows == 0) {
                log.error("更新流向订单时,删除已同步的流向订单数据失败，参数={}", deleteFlowList);
                throw new ServiceException(ReportErrorCode.UPDATE_PURCHASE_ORDER_FAIL.getMessage());
            }
        }

        //保存或更新数据
        List<FlowSaleOrderSyncDO> saveListTemp = ListUtil.toList();

        list.forEach(e -> {
            FlowSaleOrderSyncDO var = PojoUtils.map(e, FlowSaleOrderSyncDO.class);

            var.setFlowId(e.getId());
            var.setSoTotalAmount(NumberUtil.round(var.getSoPrice().multiply(var.getSoQuantity()), 2, RoundingMode.HALF_UP));
            //如果数据库中存在
            if (dbFlowMap.containsKey(e.getId())) {
                ReportFlowSaleOrderSyncDTO syncDTO = dbFlowMap.get(e.getId());
                var.setId(syncDTO.getId());
                //如果更换了以岭商品id则记录老的以岭商品id
                if (ObjectUtil.notEqual(var.getYlGoodsId(), syncDTO.getYlGoodsId())) {
                    FlowSaleOrderSyncDO syncDO = getById(syncDTO.getId());
                    var.setYlGoodsIdOld(syncDO.getYlGoodsId());
                    var.setYlGoodsIdUpdateTime(new Date());
                }
            } else {
                var.setId(null);
            }
            var.setOpTime(new Date());
            var.setOpUserId(0L);
            if (!saveListTemp.contains(var)) {
                saveListTemp.add(var);
            }
        });
        //数据去重
        List<FlowSaleOrderSyncDO> saveList = saveListTemp.stream().distinct().collect(Collectors.toList());
        if (CollUtil.isNotEmpty(saveList)) {
            Boolean isSucceed = saveOrUpdateBatch(saveList);
            if (!isSucceed) {
                log.error("更新流向订单时,新增或更新订单数据失败，参数={}", saveList);
                throw new ServiceException(ReportErrorCode.UPDATE_PURCHASE_ORDER_FAIL.getMessage());
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public void rejectOrderSync(Long reportId, ReportStatusEnum statusEnum) {
        LambdaQueryWrapper<FlowSaleOrderSyncDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(FlowSaleOrderSyncDO::getReportId, reportId);
        List<FlowSaleOrderSyncDO> list = list(wrapper);
        if (CollUtil.isEmpty(list)) {
            return;
        }
        FlowSaleOrderSyncDO syncDO = new FlowSaleOrderSyncDO();
        syncDO.setReportSettStatus(ReportSettStatusEnum.UN_CALCULATE.getCode());
        syncDO.setReportStatus(statusEnum.getCode());
        syncDO.setReportId(0L);
        syncDO.setOpUserId(0L);
        syncDO.setOpTime(new Date());
        boolean isSuccess = update(syncDO, wrapper);
        if (!isSuccess) {
            log.error("驳回流向报表时，更新流向订单同步表失败，参数={}，报表id={}", syncDO, reportId);
            throw new ServiceException(ReportErrorCode.REPORT_REJECT_FAIL.getMessage());
        }
        List<Long> flowIdList = list.stream().map(FlowSaleOrderSyncDO::getFlowId).collect(Collectors.toList());
        //重新同步订单信息
        isSuccess = syncFlowOrder(flowIdList);
        if (!isSuccess) {
            log.error("驳回流向报表时，更新流向订单失败，参数={}", flowIdList);
            throw new ServiceException(ReportErrorCode.REPORT_REJECT_FAIL.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean updateFlowOrderIdentification(UpdateFlowOrderIdenRequest request) {
        LambdaQueryWrapper<FlowSaleOrderSyncDO> wrapper = Wrappers.lambdaQuery();

        List<Long> updateIdList = ListUtil.toList();
        //更新的数据
        FlowSaleOrderSyncDO var = new FlowSaleOrderSyncDO();
        var.setIdentificationStatus(request.getUpdateIdenStatus());
        var.setAbnormalReason(request.getAbnormalReason());
        var.setAbnormalDescribed(request.getAbnormalDescribed());
        var.setOpUserId(request.getOpUserId());
        var.setOpTime(new Date());

        Boolean result;
        //按id更新
        if (CollUtil.isNotEmpty(request.getIdList())) {
            updateIdList.addAll(request.getIdList());
            wrapper.in(FlowSaleOrderSyncDO::getId, request.getIdList());
            result = update(var, wrapper);
        } else {
            //按查询条件更新
            QueryFlowOrderPageListRequest queryRequest = PojoUtils.map(request, QueryFlowOrderPageListRequest.class);
            if (ObjectUtil.isNotNull(request.getEndSoTime())) {
                queryRequest.setEndSoTime(DateUtil.endOfDay(request.getEndSoTime()));
            }

            //分页查询订单列表
            int current = 1;
            Page<ReportFlowSaleOrderSyncDTO> page;
            do {
                queryRequest.setCurrent(current);
                queryRequest.setSize(100);
                //分页查询符合结算条件的订单
                page = baseMapper.queryFlowOrderPageList(queryRequest.getPage(), queryRequest);
                if (CollUtil.isEmpty(page.getRecords())) {
                    break;
                }
                List<Long> idList = page.getRecords().stream().map(ReportFlowSaleOrderSyncDTO::getId).collect(Collectors.toList());
                updateIdList.addAll(idList);
                //更新订单状态
                wrapper.in(FlowSaleOrderSyncDO::getId, idList);
                boolean isSuccess = update(var, wrapper);
                if (!isSuccess) {
                    log.error("更新流向订单标识时影响的条数为0，参数={},id={}", var, idList);
                    throw new ServiceException(ResultCode.FAILED);
                }
                current = current + 1;
            } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
            result = Boolean.TRUE;
        }

        if (CollUtil.isNotEmpty(updateIdList)) {
            //插入日志
            LogDO logDO = new LogDO();
            logDO.setDataId(JSON.toJSONString(updateIdList));
            logDO.setType(ReportLogTypeEnum.UPDATE_FLOW_ORDER_IDENT.getCode());
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(ReportOrderIdenEnum.getByCode(request.getUpdateIdenStatus()).getName());
            if (ObjectUtil.isNotNull(request.getAbnormalReason()) && ObjectUtil.notEqual(request.getAbnormalReason(), 0)) {
                stringBuilder.append(StrUtil.DASHED);
                stringBuilder.append(ReportOrderaAnormalReasonEnum.getByCode(request.getAbnormalReason()).getName());
            }
            if (StrUtil.isNotBlank(request.getAbnormalDescribed())) {
                stringBuilder.append(StrUtil.DASHED);
                stringBuilder.append(request.getAbnormalDescribed());
            }
            logDO.setOpValue(stringBuilder.toString());
            if (ObjectUtil.isNull(request.getOpUserId()) || ObjectUtil.equal(request.getOpUserId(), 0L)) {
                logDO.setOpRemark("生成报表时系统判定库存不足");
            }
            logDO.setOpUserId(request.getOpUserId());
            logService.createLog(logDO);
        }
        return result;
    }


    @Override
    public Long queryOrderCount(QueryFlowOrderPageListRequest request) {
        if (ObjectUtil.isNotNull(request.getEndSoTime())) {
            request.setEndSoTime(DateUtil.endOfDay(request.getEndSoTime()));
        }
        return baseMapper.queryOrderCount(request);
    }


    private List<Long> querySyncByFlowIdList(List<Long> flowIdList) {
        return baseMapper.querySyncByFlowIdList(flowIdList);
    }

    @Override
    public Boolean initFlowOrderData() {
        //分页查询需要同步订单列表
        int current = 1;
        Page<FlowSaleDTO> page;
        QueryFlowPurchaseListPageRequest request = new QueryFlowPurchaseListPageRequest();
        //分页查询需要同步订单列表
        do {
            List<FlowSaleOrderSyncDO> data = ListUtil.toList();
            request.setCurrent(current);
            request.setSize(100);
            //分页查询同步失败的订单列表
            page = flowSaleApi.flowSaleYunCangPage(request);
            if (CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            //查询是否已经同步
            List<FlowSaleDTO> records = page.getRecords();
            List<Long> existenceOrder = querySyncByFlowIdList(records.stream().map(FlowSaleDTO::getId).collect(Collectors.toList()));

            records.forEach(e -> {
                //已经同步的订单进行忽略
                if (existenceOrder.contains(e.getId())) {
                    return;
                }
                FlowSaleOrderSyncDO var = PojoUtils.map(e, FlowSaleOrderSyncDO.class);
                var.setFlowId(e.getId());
                var.setId(null);
                var.setOpTime(new Date());
                var.setOpUserId(0L);
                data.add(var);
            });

            current = current + 1;
            boolean saveBatch = saveBatch(data);
            if (!saveBatch) {
                log.error("初始流向订单数据 据失败，参数={}", data);
            }
        } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
        return Boolean.TRUE;
    }

    private List<FlowSaleDTO> queryFlowOrder(List<Long> flowIds) {
        if (CollUtil.isEmpty(flowIds)) {
            return ListUtil.toList();
        }
        List<FlowSaleDTO> result = ListUtil.toList();

        //分页查询需要同步订单列表
        int current = 1;
        Page<FlowSaleDTO> page;
        QueryFlowPurchaseListPageRequest request = new QueryFlowPurchaseListPageRequest();
        request.setIdList(flowIds);

        //分页查询需要同步订单列表
        do {
            request.setCurrent(current);
            request.setSize(100);
            //分页查询订单列表
            page = flowSaleApi.flowSaleYunCangPage(request);
            if (CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            //查询是否已经同步
            List<FlowSaleDTO> records = page.getRecords();

            records.forEach(e -> {
                result.add(e);
            });
            current = current + 1;
        } while (page != null && CollUtil.isNotEmpty(page.getRecords()));

        return result;
    }

}
