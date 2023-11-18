package com.yiling.settlement.report.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.yiling.settlement.report.enums.*;
import com.yiling.user.common.util.bean.In;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.order.api.FlowPurchaseInventoryApi;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseInventorySettlementDetailRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseInventorySettlementRequest;
import com.yiling.dataflow.order.dto.request.UpdateFlowPurchaseInventoryQuantityRequest;
import com.yiling.dataflow.order.dto.request.UpdateFlowPurchaseInventoryQuantityRequestDetail;
import com.yiling.dataflow.order.enums.FlowPurchaseInventoryBusinessTypeEnum;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.exception.ServiceException;
import com.yiling.framework.common.redis.RedisDistributedLock;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.constant.GoodsConstant;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.goods.ylprice.api.GoodsYilingPriceApi;
import com.yiling.marketing.couponactivity.api.CouponActivityApi;
import com.yiling.marketing.couponactivity.bo.CouponActivityRulesBO;
import com.yiling.marketing.promotion.dto.PromotionActivityDTO;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderDetailChangeApi;
import com.yiling.order.order.dto.OrderCouponUseDTO;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDetailChangeDTO;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.dto.ReturnDetailBathDTO;
import com.yiling.order.order.enums.PromotionActivityTypeEnum;
import com.yiling.settlement.report.api.ReportParamApi;
import com.yiling.settlement.report.bo.B2bOrderSyncBO;
import com.yiling.settlement.report.bo.FlowPurchaseInventoryBO;
import com.yiling.settlement.report.bo.MemberOrderSyncBO;
import com.yiling.settlement.report.bo.QueryFlowPurchaseInventoryBO;
import com.yiling.settlement.report.bo.RebatedGoodsCountBO;
import com.yiling.settlement.report.bo.ReportLadderGoodsBO;
import com.yiling.settlement.report.dao.ReportMapper;
import com.yiling.settlement.report.dto.RebatedGoodsCountDTO;
import com.yiling.settlement.report.dto.ReportDTO;
import com.yiling.settlement.report.dto.ReportDetailB2bDTO;
import com.yiling.settlement.report.dto.ReportDetailFlowDTO;
import com.yiling.settlement.report.dto.ReportFlowSaleOrderSyncDTO;
import com.yiling.settlement.report.dto.ReportLadderGoodsInfoDTO;
import com.yiling.settlement.report.dto.ReportParamSubDTO;
import com.yiling.settlement.report.dto.ReportParamSubGoodsDTO;
import com.yiling.settlement.report.dto.ReportPriceParamNameDTO;
import com.yiling.settlement.report.dto.ReportPurchaseStockOccupyDTO;
import com.yiling.settlement.report.dto.ReportYlGoodsCategoryDTO;
import com.yiling.settlement.report.dto.request.AdjustReportRequest;
import com.yiling.settlement.report.dto.request.ConfirmReportRequest;
import com.yiling.settlement.report.dto.request.CreateReportB2bRequest;
import com.yiling.settlement.report.dto.request.CreateReportFlowRequest;
import com.yiling.settlement.report.dto.request.QueryFlowOrderPageListRequest;
import com.yiling.settlement.report.dto.request.QueryGoodsCategoryRequest;
import com.yiling.settlement.report.dto.request.QueryGoodsInfoRequest;
import com.yiling.settlement.report.dto.request.QueryOrderSyncPageListRequest;
import com.yiling.settlement.report.dto.request.QueryReportGoodsRequest;
import com.yiling.settlement.report.dto.request.QueryReportPageRequest;
import com.yiling.settlement.report.dto.request.QueryStockOccupyPageRequest;
import com.yiling.settlement.report.dto.request.RebateByReportRequest;
import com.yiling.settlement.report.dto.request.RejectReportRequest;
import com.yiling.settlement.report.dto.request.UpdateB2bOrderIdenRequest;
import com.yiling.settlement.report.dto.request.UpdateFlowOrderIdenRequest;
import com.yiling.settlement.report.entity.B2bOrderSyncDO;
import com.yiling.settlement.report.entity.FlowSaleOrderSyncDO;
import com.yiling.settlement.report.entity.LogDO;
import com.yiling.settlement.report.entity.MemberSyncDO;
import com.yiling.settlement.report.entity.ReportDO;
import com.yiling.settlement.report.entity.ReportDetailB2bDO;
import com.yiling.settlement.report.entity.ReportDetailFlowDO;
import com.yiling.settlement.report.service.B2bOrderSyncService;
import com.yiling.settlement.report.service.FlowSaleOrderSyncService;
import com.yiling.settlement.report.service.LogService;
import com.yiling.settlement.report.service.MemberSyncService;
import com.yiling.settlement.report.service.ParamSubService;
import com.yiling.settlement.report.service.ReportDetailB2bService;
import com.yiling.settlement.report.service.ReportDetailFlowService;
import com.yiling.settlement.report.service.ReportService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 报表表 服务实现类
 * </p>
 *
 * @author dexi.yao
 * @date 2022-05-17
 */
@Slf4j
@Service
public class ReportServiceImpl extends BaseServiceImpl<ReportMapper, ReportDO> implements ReportService {

    @Autowired
    B2bOrderSyncService b2bOrderSyncService;
    @Autowired
    MemberSyncService memberSyncService;
    @Autowired
    ParamSubService paramSubService;
    @Autowired
    ReportDetailB2bService detailB2bService;
    @Autowired
    ReportDetailFlowService detailFlowService;
    @Autowired
    FlowSaleOrderSyncService flowSaleOrderSyncService;
    @Autowired
    LogService logService;
    @Autowired
    RedisDistributedLock redisDistributedLock;

    @DubboReference
    OrderDetailChangeApi orderDetailChangeApi;
    @DubboReference
    OrderApi orderApi;
    @DubboReference
    ReportParamApi reportParamApi;
    @DubboReference
    CouponActivityApi couponActivityApi;
    @DubboReference
    GoodsApi goodsApi;
    @DubboReference(timeout = 60 * 1000)
    FlowPurchaseInventoryApi flowPurchaseInventoryApi;
    @DubboReference
    GoodsYilingPriceApi goodsYilingPriceApi;

    public static Integer[] otherOrderSource=new Integer[]{4,5,6,7,8,9,10,11,12,13,14,15,16,17};


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long createB2bReport(CreateReportB2bRequest request) {
        //校验起止时间
        checkTimeSlot(request.getStartReceiveTime(), request.getEndReceiveTime());
        //待计算返利的订单编号列表
        List<String> orderNoList = ListUtil.toList();
        //待更新库存的商品列表
        List<FlowPurchaseInventoryBO> inventoryList = ListUtil.toList();
        //待更新为库存不足的同步订单id
        List<Long> b2bOrderSyncIdenUpdateList = ListUtil.toList();

        //分页查询符合返利条件的订单
        int current = 1;
        Page<String> orderPage;
        QueryOrderSyncPageListRequest queryRequest = new QueryOrderSyncPageListRequest();
        queryRequest.setSellerEidList(ListUtil.toList(request.getSellerEid()));
        queryRequest.setStartReceiveTime(request.getStartReceiveTime());
        queryRequest.setEndReceiveTime(request.getEndReceiveTime());
        queryRequest.setReportStatusList(request.getReportStatusList());
        queryRequest.setIdentificationStatus(request.getIdentificationStatus());
        //过滤失效订单
        queryRequest.setFilterInvalidOrder(1);
        //分页查询订单列表
        do {
            queryRequest.setCurrent(current);
            queryRequest.setSize(50);
            //分页查询符合结算条件的订单
            orderPage = b2bOrderSyncService.queryB2bOrderNoPageList(queryRequest);
            if (CollUtil.isEmpty(orderPage.getRecords())) {
                break;
            }
            List<String> records = orderPage.getRecords();
            if (CollUtil.isNotEmpty(records)) {
                for (String orderNo : records) {
                    //将订单orderSync插入待更新结算状态的列表
                    if (!orderNoList.contains(orderNo)) {
                        orderNoList.add(orderNo);
                    }
                }
            }
            current = current + 1;
        } while (orderPage != null && CollUtil.isNotEmpty(orderPage.getRecords()));
        //生成返利报表
        //查询该企业下会员订单
        List<MemberOrderSyncBO> memberOrderSyncs = memberSyncService.queryOrderByEid(request.getSellerEid(), request.getStartReceiveTime(), request.getEndReceiveTime());
        //如果订单为空则就此结束
        if (CollUtil.isEmpty(orderNoList) && CollUtil.isEmpty(memberOrderSyncs)) {
            return 0L;
        }

        //生成返利报表
        ReportDO reportDO = saveB2bReport(orderNoList, inventoryList, request.getSellerEid(), memberOrderSyncs, request.getStartReceiveTime(), request.getEndReceiveTime(), request.getOpUserId(), b2bOrderSyncIdenUpdateList);
        //更新订单同步表返利状态及会员订单
        if (ObjectUtil.isNotNull(reportDO)) {
            //更新订单状态（包括会员订单）以及库存不足的订单
            updateB2bOrderSyncRewardStatus(reportDO, orderNoList, PojoUtils.map(memberOrderSyncs, MemberSyncDO.class), b2bOrderSyncIdenUpdateList);
        }
        //插入报表日志
        logService.createReportTypeLog(reportDO.getId(), ReportLogTypeEnum.CREATE, request.getOpUserId());

        //更新采购库存
        updatePurchaseQuantity(inventoryList, request.getOpUserId());
        return reportDO.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long createFlowReport(CreateReportFlowRequest request) {
        //校验起止时间
        checkTimeSlot(request.getStartSoTime(), request.getEndSoTime());
        //待更新返利生成状态的订单列表
        List<FlowSaleOrderSyncDO> updateOrderList = ListUtil.toList();
        //待计算返利的订单编号列表
        List<String> orderNoList = ListUtil.toList();
        //待更新库存的商品列表
        List<FlowPurchaseInventoryBO> inventoryList = ListUtil.toList();
        //待更新为库存不足的同步订单id
        List<Long> flowOrderSyncIdenUpdateList = ListUtil.toList();

        //分页查询符合返利条件的订单
        int current = 1;
        Page<ReportFlowSaleOrderSyncDTO> orderPage;
        QueryFlowOrderPageListRequest queryRequest = new QueryFlowOrderPageListRequest();
        queryRequest.setEidList(ListUtil.toList(request.getEid()));
        queryRequest.setStartSoTime(request.getStartSoTime());
        queryRequest.setEndSoTime(request.getEndSoTime());
        queryRequest.setReportStatusList(request.getReportStatusList());
        queryRequest.setSoSourceList(request.getSoSourceList());
        queryRequest.setIdentificationStatus(request.getIdentificationStatus());
        //过滤失效订单
        queryRequest.setFilterInvalidOrder(1);
        //分页查询订单列表
        do {
            queryRequest.setCurrent(current);
            queryRequest.setSize(50);
            //分页查询符合结算条件的订单
            orderPage = flowSaleOrderSyncService.queryFlowOrderPageList(queryRequest);
            if (CollUtil.isEmpty(orderPage.getRecords())) {
                break;
            }
            List<ReportFlowSaleOrderSyncDTO> records = orderPage.getRecords();
            if (CollUtil.isNotEmpty(records)) {
                for (ReportFlowSaleOrderSyncDTO orderSync : records) {
                    //将订单orderSync插入待更新结算状态的列表
                    updateOrderList.add(PojoUtils.map(orderSync, FlowSaleOrderSyncDO.class));
                    orderNoList.add(orderSync.getSoNo());
                }
            }
            current = current + 1;
        } while (orderPage != null && CollUtil.isNotEmpty(orderPage.getRecords()));
        //生成返利报表
        //如果订单为空则就此结束
        if (CollUtil.isEmpty(orderNoList)) {
            return 0L;
        }

        //生成返利报表
        ReportDO reportDO = saveFlowReport(updateOrderList, inventoryList, request.getEid(), request.getStartSoTime(), request.getEndSoTime(), request.getOpUserId(), flowOrderSyncIdenUpdateList);
        //更新订单同步表返利状态
        if (ObjectUtil.isNotNull(reportDO)) {
            //更新订单状态以及库存不足的订单标识
            updateFlowOrderSyncRewardStatus(reportDO, updateOrderList, flowOrderSyncIdenUpdateList);
        }
        //插入报表日志
        logService.createReportTypeLog(reportDO.getId(), ReportLogTypeEnum.CREATE, request.getOpUserId());
        //更新采购库存
        updatePurchaseQuantity(inventoryList, request.getOpUserId());
        return reportDO.getId();
    }

    @Override
    public Page<ReportDTO> queryReportPage(QueryReportPageRequest request) {
        LambdaQueryWrapper<ReportDO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(CollUtil.isNotEmpty(request.getReportIdList()), ReportDO::getId, request.getReportIdList());
        wrapper.in(CollUtil.isNotEmpty(request.getEidList()), ReportDO::getEid, request.getEidList());
        wrapper.eq(ObjectUtil.isNotNull(request.getType()) && ObjectUtil.notEqual(request.getType(), 0), ReportDO::getType, request.getType());
        wrapper.eq(ObjectUtil.isNotNull(request.getRebateStatus()) && ObjectUtil.notEqual(request.getRebateStatus(), 0), ReportDO::getRebateStatus, request.getRebateStatus());
        wrapper.in(CollUtil.isNotEmpty(request.getStatus()), ReportDO::getStatus, request.getStatus());
        wrapper.orderByDesc(ReportDO::getUpdateTime);
        if (request.getStartCreateTime() != null) {
            wrapper.ge(ReportDO::getCreateTime, request.getStartCreateTime());
        }
        if (request.getEndCreateTime() != null) {
            wrapper.le(ReportDO::getCreateTime, DateUtil.endOfDay(request.getEndCreateTime()));
        }
        Page<ReportDO> page = page(request.getPage(), wrapper);
        return PojoUtils.map(page, ReportDTO.class);
    }

    @Override
    public Boolean queryMemberRefundIsAlert(Long reportId, Long eid) {
        //查询该企业下会员订单
        List<MemberOrderSyncBO> memberOrderSyncs = memberSyncService.queryOrderByReportId(reportId);
        AtomicReference<Boolean> result = new AtomicReference<>(Boolean.FALSE);
        memberOrderSyncs.forEach(e -> {
            if (e.getRefundAmount().compareTo(BigDecimal.ZERO) != 0) {
                result.set(Boolean.TRUE);
                return;
            }
        });
        return result.get();
    }

    @Override
    public Boolean rejectReport(RejectReportRequest request) {
        ReportDO reportDO = getById(request.getReportId());
        if (ObjectUtil.isNull(reportDO)) {
            throw new BusinessException(ReportErrorCode.REPORT_NOT_FOUND);
        }
        if (ObjectUtil.equal(request.getStatusEnum().getCode(), ReportStatusEnum.OPERATOR_REJECT.getCode()) && ObjectUtil.notEqual(ReportStatusEnum.UN_OPERATOR_CONFIRM.getCode(), reportDO.getStatus())) {
            throw new BusinessException(ReportErrorCode.REPORT_STATUS_EXCEPTION, "当前报表状态为" + ReportStatusEnum.getByCode(reportDO.getStatus()).getName() + "不能进行运营驳回");
        }
        if (ObjectUtil.equal(request.getStatusEnum().getCode(), ReportStatusEnum.FINANCE_REJECT.getCode()) && ObjectUtil.notEqual(ReportStatusEnum.UN_FINANCE_CONFIRM.getCode(), reportDO.getStatus())) {
            throw new BusinessException(ReportErrorCode.REPORT_STATUS_EXCEPTION, "当前报表状态为" + ReportStatusEnum.getByCode(reportDO.getStatus()).getName() + "不能进行财务驳回");
        }
        Map<String, FlowPurchaseInventoryBO> inventoryBOMap;
        //如果是b2b报表
        if (ObjectUtil.equal(reportDO.getType(), ReportTypeEnum.B2B.getCode())) {
            //更新采购库存
            inventoryBOMap = detailB2bService.rejectB2bDetail(request.getReportId(), reportDO.getEid());
            //如果有订单类的报表明细则驳回b2b订单同步表
            b2bOrderSyncService.rejectOrderSync(request.getReportId(), request.getStatusEnum());
            //如果有会员返利则驳回会员订单
            memberSyncService.rejectMemberOrder(request.getReportId(), request.getStatusEnum());
        } else {
            //如果是流向订单报表
            //更新采购库存
            inventoryBOMap = detailFlowService.rejectB2bDetail(request.getReportId(), reportDO.getEid());
            //如果有订单类的报表明细则更新流向订单同步表
            flowSaleOrderSyncService.rejectOrderSync(request.getReportId(), request.getStatusEnum());
        }
        //更新报表状态
        reportDO.setStatus(request.getStatusEnum().getCode());
        reportDO.setRejectReason(request.getRejectReason());
        reportDO.setOpUserId(request.getOpUserId());
        reportDO.setOpTime(request.getOpTime());
        Boolean isSuccess = updateById(reportDO);
        if (!isSuccess) {
            log.error("驳回报表时更新报表失败，参数={}", reportDO);
            throw new ServiceException(ReportErrorCode.REPORT_REJECT_FAIL.getMessage());
        }
        //插入日志
        if (ObjectUtil.equal(request.getStatusEnum().getCode(), ReportStatusEnum.OPERATOR_REJECT.getCode())) {
            logService.createReportTypeLog(request.getReportId(), ReportLogTypeEnum.OPERATOR_REJECT, request.getOpUserId());
        } else if (ObjectUtil.equal(request.getStatusEnum().getCode(), ReportStatusEnum.FINANCE_REJECT.getCode())) {
            logService.createReportTypeLog(request.getReportId(), ReportLogTypeEnum.FINANCE_REJECT, request.getOpUserId());
        } else if (ObjectUtil.equal(request.getStatusEnum().getCode(), ReportStatusEnum.ADMIN_REJECT.getCode())) {
            logService.createReportTypeLog(request.getReportId(), ReportLogTypeEnum.ADMIN_REJECT, request.getOpUserId());
        }
        //更新采购库存
        List<FlowPurchaseInventoryBO> inventoryList = inventoryBOMap.entrySet().stream().map(e -> e.getValue()).collect(Collectors.toList());
        updatePurchaseQuantity(inventoryList, request.getOpUserId());
        return Boolean.TRUE;
    }

    @Override
    public Boolean confirm(ConfirmReportRequest request) {
        ReportDO reportDO = getById(request.getReportId());
        if (ObjectUtil.isNull(reportDO)) {
            throw new BusinessException(ReportErrorCode.REPORT_NOT_FOUND);
        }
        if (ObjectUtil.equal(request.getStatusEnum().getCode(), ReportStatusEnum.UN_FINANCE_CONFIRM.getCode()) && ObjectUtil.notEqual(ReportStatusEnum.UN_OPERATOR_CONFIRM.getCode(), reportDO.getStatus())) {
            throw new BusinessException(ReportErrorCode.REPORT_STATUS_EXCEPTION, "当前报表状态为" + ReportStatusEnum.getByCode(reportDO.getStatus()).getName() + "不能进行运营确认");
        }
        if (ObjectUtil.equal(request.getStatusEnum().getCode(), ReportStatusEnum.FINANCE_CONFIRMED.getCode()) && ObjectUtil.notEqual(ReportStatusEnum.UN_FINANCE_CONFIRM.getCode(), reportDO.getStatus())) {
            throw new BusinessException(ReportErrorCode.REPORT_STATUS_EXCEPTION, "当前报表状态为" + ReportStatusEnum.getByCode(reportDO.getStatus()).getName() + "不能进行财务确认");
        }
        reportDO.setStatus(request.getStatusEnum().getCode());
        reportDO.setOpUserId(request.getOpUserId());
        reportDO.setOpTime(request.getOpTime());
        boolean isSuccess = updateById(reportDO);
        if (!isSuccess) {
            log.error("确认报表时更新报表失败，参数={}", reportDO);
            throw new ServiceException(ReportErrorCode.REPORT_CONFIRM_FAIL.getMessage());
        }
        //插入日志
        if (ObjectUtil.equal(request.getStatusEnum().getCode(), ReportStatusEnum.UN_FINANCE_CONFIRM.getCode())) {
            logService.createReportTypeLog(request.getReportId(), ReportLogTypeEnum.OPERATOR_CONFIRMED, request.getOpUserId());
        } else if (ObjectUtil.equal(request.getStatusEnum().getCode(), ReportStatusEnum.FINANCE_CONFIRMED.getCode())) {
            logService.createReportTypeLog(request.getReportId(), ReportLogTypeEnum.FINANCE_CONFIRMED, request.getOpUserId());
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean adjust(AdjustReportRequest request) {
        ReportDO reportDO = getById(request.getReportId());
        if (ObjectUtil.isNull(reportDO)) {
            throw new BusinessException(ReportErrorCode.REPORT_NOT_FOUND);
        }
        if (ObjectUtil.notEqual(reportDO.getStatus(), ReportStatusEnum.UN_OPERATOR_CONFIRM.getCode()) && ObjectUtil.notEqual(ReportStatusEnum.UN_FINANCE_CONFIRM.getCode(), reportDO.getStatus())) {
            throw new BusinessException(ReportErrorCode.REPORT_STATUS_EXCEPTION, "当前报表状态为" + ReportStatusEnum.getByCode(reportDO.getStatus()).getName() + "不能进行金额调整");
        }
        ReportDO aDo = PojoUtils.map(request, ReportDO.class);
        aDo.setId(reportDO.getId());
        BigDecimal totalAmount = reportDO.getTotalAmount();
        BigDecimal newAmount = totalAmount.subtract(reportDO.getAdjustAmount()).add(request.getAdjustAmount());
        reportDO.setAdjustAmount(request.getAdjustAmount());
        aDo.setTotalAmount(newAmount);
        boolean isSuccess = updateById(aDo);
        if (!isSuccess) {
            log.error("调整报表金额时更新报表失败，参数={}", aDo);
            throw new ServiceException(ReportErrorCode.ADJUST_FAIL.getMessage());
        }
        //插入日志
        LogDO logDO = PojoUtils.map(request, LogDO.class);
        logDO.setReportId(reportDO.getId());
        logDO.setType(ReportLogTypeEnum.ADJUST.getCode());
        logDO.setOpValue(request.getAdjustAmount().toString());
        logDO.setOpRemark(request.getAdjustReason());
        logDO.setOpUserId(request.getOpUserId());
        logService.createLog(logDO);
        return Boolean.TRUE;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean rebateByReport(RebateByReportRequest request) {
        ReportDO reportDO = getById(request.getReportId());
        if (ObjectUtil.isNull(reportDO)) {
            log.warn("勾选返利时，报表未查询到，报表id={}", request.getReportId());
            throw new BusinessException(ReportErrorCode.REPORT_NOT_FOUND);
        }
        if (ObjectUtil.notEqual(reportDO.getStatus(), ReportStatusEnum.FINANCE_CONFIRMED.getCode())) {
            log.warn("勾选返利时，报表状态为={}，不能勾选返利，报表id={}", ReportStatusEnum.getByCode(reportDO.getStatus()).getName(), request.getReportId());
            throw new BusinessException(ReportErrorCode.REPORT_REBATE_INVALID);
        }

        boolean isSuccess;
        String lockName = "report_rebate" + Constants.SEPARATOR_MIDDLELINE + "callback" + Constants.SEPARATOR_MIDDLELINE + reportDO.getId();
        String lockId = redisDistributedLock.lock2(lockName, 60, 120, TimeUnit.SECONDS);
        try {
            //计算勾选总额
            AtomicReference<BigDecimal> rebateSubAmount = new AtomicReference<>();
            rebateSubAmount.set(BigDecimal.ZERO);
            List<Long> updateDetailId = null;
            ReportRebateStatusEnum reportRebateStatus = null;

            //查询明细
            if (ObjectUtil.equal(reportDO.getType(), ReportTypeEnum.B2B.getCode())) {
                List<ReportDetailB2bDO> detailB2bDOS;
                List<ReportDetailB2bDTO> allUnRebateDetail = detailB2bService.queryB2bDetailByReportId(request.getReportId(), ReportRebateStatusEnum.UN_REBATE);
                if (CollUtil.isNotEmpty(request.getDetailIdList())) {
                    List<ReportDetailB2bDO> parDetailList = detailB2bService.listByIds(request.getDetailIdList());
                    List<ReportDetailB2bDO> rebateList = parDetailList.stream().filter(e -> ObjectUtil.equal(e.getRebateStatus(), ReportRebateStatusEnum.REBATED.getCode())).collect(Collectors.toList());
                    if (CollUtil.isNotEmpty(rebateList)) {
                        log.warn("标识返利的订单中包含已返利的报表明细,参数={}", rebateList);
                        throw new BusinessException(ReportErrorCode.REPORT_REBATE_ILLEGAL);
                    }
                    detailB2bDOS = parDetailList.stream().filter(e -> ObjectUtil.equal(e.getRebateStatus(), ReportRebateStatusEnum.UN_REBATE.getCode())).collect(Collectors.toList());
                    List<Long> reportDetailIdList = detailB2bDOS.stream().map(ReportDetailB2bDO::getId).collect(Collectors.toList());
                    reportRebateStatus = ReportRebateStatusEnum.REBATED;
                    //判断总表返利状态
                    for (ReportDetailB2bDTO e : allUnRebateDetail) {
                        //如果本次勾选的返利列表不包含 所有待返利明细的任意一项 则总表记为部分返利
                        if (!reportDetailIdList.contains(e.getId())) {
                            reportRebateStatus = ReportRebateStatusEnum.PROPORTION_REBATE;
                            break;
                        }
                    }
                } else {
                    reportRebateStatus = ReportRebateStatusEnum.REBATED;
                    detailB2bDOS = PojoUtils.map(allUnRebateDetail, ReportDetailB2bDO.class);
                }

                if (CollUtil.isEmpty(detailB2bDOS)) {
                    log.warn("当前筛选条件下没有可标记为已返利的报表明细,参数={}", request);
                    throw new BusinessException(ReportErrorCode.REPORT_REBATE_DETAIL_NOT_FOUND);
                }
                detailB2bDOS.forEach(e -> {
                    BigDecimal totalAmount = rebateSubAmount.get();
                    totalAmount = totalAmount.add(e.getSubRebate());
                    rebateSubAmount.set(totalAmount);
                });
                updateDetailId = detailB2bDOS.stream().map(ReportDetailB2bDO::getId).collect(Collectors.toList());
            }
            if (ObjectUtil.equal(reportDO.getType(), ReportTypeEnum.FLOW.getCode())) {
                List<ReportDetailFlowDO> detailB2bDOS;
                List<ReportDetailFlowDTO> allUnRebateDetail = detailFlowService.queryFlowDetailByReportId(request.getReportId(), ReportRebateStatusEnum.UN_REBATE);
                if (CollUtil.isNotEmpty(request.getDetailIdList())) {
                    List<ReportDetailFlowDO> parDetailList = detailFlowService.listByIds(request.getDetailIdList());
                    List<ReportDetailFlowDO> rebateList = parDetailList.stream().filter(e -> ObjectUtil.equal(e.getRebateStatus(), ReportRebateStatusEnum.REBATED.getCode())).collect(Collectors.toList());
                    if (CollUtil.isNotEmpty(rebateList)) {
                        log.warn("标识返利的订单中包含已返利的报表明细,参数={}", rebateList);
                        throw new BusinessException(ReportErrorCode.REPORT_REBATE_ILLEGAL);
                    }
                    detailB2bDOS = parDetailList.stream().filter(e -> ObjectUtil.equal(e.getRebateStatus(), ReportRebateStatusEnum.UN_REBATE.getCode())).collect(Collectors.toList());
                    List<Long> reportDetailIdList = detailB2bDOS.stream().map(ReportDetailFlowDO::getId).collect(Collectors.toList());
                    reportRebateStatus = ReportRebateStatusEnum.REBATED;
                    //判断总表返利状态
                    for (ReportDetailFlowDTO e : allUnRebateDetail) {
                        //如果本次勾选的返利列表不包含 所有待返利明细的任意一项 则总表记为部分返利
                        if (!reportDetailIdList.contains(e.getId())) {
                            reportRebateStatus = ReportRebateStatusEnum.PROPORTION_REBATE;
                            break;
                        }
                    }
                } else {
                    reportRebateStatus = ReportRebateStatusEnum.REBATED;
                    detailB2bDOS = PojoUtils.map(allUnRebateDetail, ReportDetailFlowDO.class);
                }

                if (CollUtil.isEmpty(detailB2bDOS)) {
                    log.warn("当前筛选条件下没有可标记为已返利的报表明细,参数={}", request);
                    throw new BusinessException(ReportErrorCode.REPORT_REBATE_DETAIL_NOT_FOUND);
                }
                detailB2bDOS.forEach(e -> {
                    BigDecimal totalAmount = rebateSubAmount.get();
                    totalAmount = totalAmount.add(e.getSubRebate());
                    rebateSubAmount.set(totalAmount);
                });
                updateDetailId = detailB2bDOS.stream().map(ReportDetailFlowDO::getId).collect(Collectors.toList());
            }
            if (CollUtil.isEmpty(updateDetailId)) {
                return Boolean.FALSE;
            }
            //计算总返利金额
            BigDecimal rebateAmount = reportDO.getRebateAmount().add(rebateSubAmount.get());
            ReportDO data = new ReportDO();
            data.setId(reportDO.getId());
            data.setRebateAmount(rebateAmount);
            data.setOpUserId(request.getOpUserId());
            data.setOpTime(new Date());
            if (ObjectUtil.isNotNull(reportRebateStatus)) {
                data.setRebateStatus(reportRebateStatus.getCode());
            }
            isSuccess = false;
            //更新明细
            if (ObjectUtil.equal(reportDO.getType(), ReportTypeEnum.B2B.getCode())) {
                LambdaQueryWrapper<ReportDetailB2bDO> wrapper = Wrappers.lambdaQuery();
                wrapper.in(ReportDetailB2bDO::getId, updateDetailId);
                ReportDetailB2bDO var = new ReportDetailB2bDO();
                var.setRebateStatus(ReportRebateStatusEnum.REBATED.getCode());
                var.setOpUserId(request.getOpUserId());
                var.setOpTime(new Date());
                isSuccess = detailB2bService.update(var, wrapper);
            }
            if (ObjectUtil.equal(reportDO.getType(), ReportTypeEnum.FLOW.getCode())) {
                LambdaQueryWrapper<ReportDetailFlowDO> wrapper = Wrappers.lambdaQuery();
                wrapper.in(ReportDetailFlowDO::getId, updateDetailId);
                ReportDetailFlowDO var = new ReportDetailFlowDO();
                var.setRebateStatus(ReportRebateStatusEnum.REBATED.getCode());
                var.setOpUserId(request.getOpUserId());
                var.setOpTime(new Date());
                isSuccess = detailFlowService.update(var, wrapper);
            }
            if (!isSuccess) {
                log.error("报表返利时更新报表详情数据失败，报表类型={}，参数={}", ReportTypeEnum.getByCode(reportDO.getType()).getName(), updateDetailId);
                throw new ServiceException(ResultCode.FAILED);
            }
            isSuccess = updateById(data);
            if (!isSuccess) {
                log.error("报表返利时更新报表数据失败，参数={}", data);
                throw new ServiceException(ResultCode.FAILED);
            }
            //记录日志
            LogDO logDO = PojoUtils.map(request, LogDO.class);
            logDO.setReportId(reportDO.getId());
            logDO.setDataId(JSON.toJSONString(updateDetailId));
            logDO.setType(ReportLogTypeEnum.REPORT_REBATE.getCode());
            logDO.setOpUserId(request.getOpUserId());
            logService.createLog(logDO);
        } catch (Exception e) {
            throw e;
        } finally {
            redisDistributedLock.releaseLock(lockName, lockId);
        }
        return isSuccess;
    }

    @Override
    public List<RebatedGoodsCountDTO> queryRebateCount(List<QueryStockOccupyPageRequest> request) {
        if (CollUtil.isEmpty(request)) {
            return ListUtil.toList();
        }
        List<RebatedGoodsCountDTO> result = ListUtil.toList();
        //查询B2B返利报表占用
        Map<RebatedGoodsCountBO, Integer> b2bRebateCountMap = detailB2bService.queryRebateGoods(request).stream().collect(Collectors.toMap(e -> e, RebatedGoodsCountBO::getQuantity));
        //查询流向返利报表占用
        Map<RebatedGoodsCountBO, Integer> flowRebateCountMap = detailFlowService.queryRebateGoods(request).stream().collect(Collectors.toMap(e -> e, RebatedGoodsCountBO::getQuantity));
        request.forEach(e -> {
            RebatedGoodsCountDTO var = PojoUtils.map(e, RebatedGoodsCountDTO.class);
            var.setQuantity(0);
            RebatedGoodsCountBO var2 = PojoUtils.map(e, RebatedGoodsCountBO.class);
            if (b2bRebateCountMap.containsKey(var2)) {
                var.setQuantity(b2bRebateCountMap.getOrDefault(var2, 0));
            } else {
                var.setQuantity(0);
            }
            if (flowRebateCountMap.containsKey(var2)) {
                var.setQuantity(var.getQuantity() + flowRebateCountMap.getOrDefault(var2, 0));
            }
            result.add(var);
        });
        return result;
    }

    @Override
    public Page<ReportPurchaseStockOccupyDTO> queryStockOccupyPage(QueryStockOccupyPageRequest request) {
        return baseMapper.queryStockOccupyPage(request.getPage(), request);
    }

    @Override
    public Boolean isExitReportByGoods(QueryReportGoodsRequest request) {
        if (ObjectUtil.equal(0L, request.getEid()) || ObjectUtil.isNull(request.getEid())) {
            throw new BusinessException(ResultCode.PARAM_MISS, "eid不能为空");
        }
        if (ObjectUtil.equal(0L, request.getYlGoodsId()) || ObjectUtil.isNull(request.getYlGoodsId())) {
            throw new BusinessException(ResultCode.PARAM_MISS, "以岭商品id不能为空");
        }
        if (StrUtil.isBlank(request.getGoodsInSn())) {
            throw new BusinessException(ResultCode.PARAM_MISS, "商品内码不能为空");
        }
        Integer count = baseMapper.queryExitReportByGoodsCount(request);
        return ObjectUtil.equal(0, count);
    }


    /**
     * 更新采购流向库存
     *
     * @param inventoryBOS
     */
    private void updatePurchaseQuantity(List<FlowPurchaseInventoryBO> inventoryBOS, Long opUser) {
        if (CollUtil.isEmpty(inventoryBOS)) {
            return;
        }
        List<UpdateFlowPurchaseInventoryQuantityRequestDetail> updateList = ListUtil.toList();
        inventoryBOS.forEach(e -> {
            if (e.getDyhOpQuantity() != 0) {
                UpdateFlowPurchaseInventoryQuantityRequestDetail var1 = new UpdateFlowPurchaseInventoryQuantityRequestDetail();
                var1.setId(e.getDyhId());
                var1.setQuantity(new BigDecimal(e.getDyhOpQuantity().toString()));
                updateList.add(var1);
            }
            if (e.getJdOpQuantity() != 0) {
                UpdateFlowPurchaseInventoryQuantityRequestDetail var2 = new UpdateFlowPurchaseInventoryQuantityRequestDetail();
                var2.setId(e.getJdId());
                var2.setQuantity(new BigDecimal(e.getJdOpQuantity().toString()));
                updateList.add(var2);
            }
        });
        if (CollUtil.isEmpty(updateList)) {
            return;
        }
        UpdateFlowPurchaseInventoryQuantityRequest request = new UpdateFlowPurchaseInventoryQuantityRequest();
        request.setBusinessType(FlowPurchaseInventoryBusinessTypeEnum.SETTLEMENT.getCode());
        request.setList(updateList);
        request.setOpUserId(opUser);
        request.setOpTime(new Date());
        int rows = flowPurchaseInventoryApi.updateQuantityById(request);
        if (rows == 0) {
            log.error("更新采购流向库存失败，参数={}", request);
            throw new ServiceException(ReportErrorCode.UPDATE_PURCHASE_FAIL.getMessage());
        }
    }

    /**
     * 更新订单状态
     *
     * @param reportDO
     * @param updateOrderList
     * @param updateOrderList
     * @param flowOrderSyncIdenUpdateList
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateFlowOrderSyncRewardStatus(ReportDO reportDO, List<FlowSaleOrderSyncDO> updateOrderList, List<Long> flowOrderSyncIdenUpdateList) {
        Boolean isSuccess;
        if (CollUtil.isNotEmpty(updateOrderList)) {
            //更新订单
            updateOrderList.forEach(e -> {
                e.setReportId(reportDO.getId());
                e.setReportSettStatus(ReportSettStatusEnum.CALCULATED.getCode());
                e.setReportStatus(ReportStatusEnum.UN_OPERATOR_CONFIRM.getCode());
            });
            isSuccess = flowSaleOrderSyncService.updateBatchById(updateOrderList);
            if (!isSuccess) {
                log.error("流向返利生成时更新订单同步表失败,参数={}", updateOrderList);
                throw new ServiceException("返利报表保存失败");
            }
        }
        //库存不足的修改订单标识
        if (CollUtil.isNotEmpty(flowOrderSyncIdenUpdateList)) {
            UpdateFlowOrderIdenRequest updateFlowOrderIdenRequest = new UpdateFlowOrderIdenRequest();
            updateFlowOrderIdenRequest.setIdList(flowOrderSyncIdenUpdateList);
            updateFlowOrderIdenRequest.setUpdateIdenStatus(ReportOrderIdenEnum.ABNORMAL.getCode());
            updateFlowOrderIdenRequest.setAbnormalReason(ReportOrderaAnormalReasonEnum.STOCK_SHORTAGE.getCode());
            isSuccess = flowSaleOrderSyncService.updateFlowOrderIdentification(updateFlowOrderIdenRequest);
            if (!isSuccess) {
                log.error("流向返利生成时更新订单同步信息的标识状态失败,参数={}", flowOrderSyncIdenUpdateList);
                throw new ServiceException("返利报表保存失败");
            }
        }
    }

    /**
     * 计算并生成流向返利报表
     *
     * @param orderSyncList
     * @param inventoryList
     * @param eid
     * @param startTime
     * @param endTime
     * @param opUser
     * @param flowOrderSyncIdenUpdateList
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ReportDO saveFlowReport(List<FlowSaleOrderSyncDO> orderSyncList, List<FlowPurchaseInventoryBO> inventoryList, Long eid, Date startTime, Date endTime, Long opUser, List<Long> flowOrderSyncIdenUpdateList) {
        //计算返利明细
        ReportDO reportDO = initReport(ReportTypeEnum.FLOW, eid, startTime, endTime);
        reportDO.setOpUserId(opUser);
        Map<String, List<FlowSaleOrderSyncDO>> orderMap = orderSyncList.stream().filter(e -> ObjectUtil.notEqual(e.getIdentificationStatus(), ReportOrderIdenEnum.INVALID.getCode())).collect(Collectors.groupingBy(FlowSaleOrderSyncDO::getSoNo));
        //按订单计算返利明细
        orderMap.forEach((soNo, orderDetailList) -> {
            calculateFlowOrderReward(reportDO, orderDetailList, inventoryList, orderDetailList.stream().findAny().get().getSoTime(), flowOrderSyncIdenUpdateList);
        });

        //保存总表
        boolean isSuccess = save(reportDO);
        if (!isSuccess) {
            log.error("b2b返利报表保存失败,参数={}", reportDO);
            throw new ServiceException("返利报表保存失败");
        }
        //保存明细
        reportDO.getDetailFlowList().forEach(e -> {
            e.setReportId(reportDO.getId());
        });
        if (CollUtil.isNotEmpty(reportDO.getDetailFlowList())) {
            isSuccess = detailFlowService.saveBatch(reportDO.getDetailFlowList());
            if (!isSuccess) {
                log.error("b2b返利报表明细保存失败,参数={}", reportDO.getDetailB2bList());
                throw new ServiceException("返利报表保存失败");
            }
        }
        return reportDO;
    }

    /**
     * 根据订单的配送单计算订单返利
     *
     * @param reportDO
     * @param orderDetail
     * @param inventoryList
     * @param createOrderTime
     * @param flowOrderSyncIdenUpdateList
     */
    private void calculateFlowOrderReward(ReportDO reportDO, List<FlowSaleOrderSyncDO> orderDetail, List<FlowPurchaseInventoryBO> inventoryList, Date createOrderTime, List<Long> flowOrderSyncIdenUpdateList) {
        //商品id集合
        List<Long> goodsIdList = orderDetail.stream().map(FlowSaleOrderSyncDO::getYlGoodsId).distinct().collect(Collectors.toList());
//        //商品内码集合
//        List<String> goodsInSnList = orderDetail.stream().map(FlowSaleOrderSyncDO::getGoodsInSn).distinct().collect(Collectors.toList());
        //查询以岭商品名称
        Map<Long, GoodsDTO> goodsMap = goodsApi.batchQueryInfo(goodsIdList).stream().collect(Collectors.toMap(GoodsDTO::getId, e -> e));
        //查询参数
        //查询价格信息
        List<ReportPriceParamNameDTO> priceParamNameList = PojoUtils.map(goodsYilingPriceApi.getPriceParamNameList(goodsIdList, createOrderTime), ReportPriceParamNameDTO.class);

        //查询采购库存
        Map<String, FlowPurchaseInventoryBO> inventoryBOMap = queryGoodsInventoryFlow(inventoryList, orderDetail, reportDO.getEid());

        orderDetail.forEach(detail -> {
            ReportDetailFlowDO detailFlow = PojoUtils.map(detail, ReportDetailFlowDO.class);
            detailFlow.setSellerEid(detail.getEid());
            initReportDetailFlow(detailFlow, goodsMap);
            //设置参数信息如出货价
            setReportDetailFlowPrice(detailFlow, priceParamNameList);

            //销售额返利
            String key = FlowPurchaseInventoryBO.getKey(reportDO.getEid(), detail.getYlGoodsId(), detail.getGoodsInSn());
            ReportPurchaseChannelEnum channelEnum = calculatePurchaseChannel(inventoryBOMap, key, detail.getSoQuantity().longValue());
            detailFlow.setPurchaseChannel(channelEnum.getCode());
            //库存不足的更新订单同步信息的标识状态
            if (ObjectUtil.equal(ReportPurchaseChannelEnum.SHORTAGE.getCode(), channelEnum.getCode()) && ObjectUtil.equal(ReportOrderIdenEnum.NORMAL.getCode(), detailFlow.getIdentificationStatus())) {
                flowOrderSyncIdenUpdateList.add(detail.getId());
                detailFlow.setIdentificationStatus(ReportOrderIdenEnum.ABNORMAL.getCode());
                detailFlow.setAbnormalReason(ReportOrderaAnormalReasonEnum.STOCK_SHORTAGE.getCode());
            }
            //购进渠道为大运河时才计算
            if (ObjectUtil.equal(ReportPurchaseChannelEnum.DYH.getCode(), channelEnum.getCode())) {
                BigDecimal rebateMoney = BigDecimal.ZERO;
                if (detailFlow.getSupplyPrice().compareTo(BigDecimal.ZERO) != 0) {
                    rebateMoney = NumberUtil.round(detailFlow.getSupplyPrice().multiply(detail.getSoQuantity()).multiply(new BigDecimal(GoodsConstant.REBATE_RATE)), 2, RoundingMode.HALF_UP);
                }
                detailFlow.setSalesAmount(rebateMoney);
            }

            //计算奖励
            calculateRewardFlow(reportDO.getEid(), detailFlow, detailFlow.getOutPrice(), detailFlow.getGoodsInSn(),detailFlow.getSoSource());

            //加入总表
            joinReportFlow(reportDO, detailFlow);
        });
    }

    /**
     * 计算总表金额
     *
     * @param reportDO
     * @param detailFlow
     */
    private void joinReportFlow(ReportDO reportDO, ReportDetailFlowDO detailFlow) {

        BigDecimal totalAmount = BigDecimal.ZERO;

        //销售额返利
        BigDecimal salesAmount = reportDO.getSalesAmount().add(detailFlow.getSalesAmount());
        reportDO.setSalesAmount(salesAmount);
        //阶梯金额
        BigDecimal ladderAmount = reportDO.getLadderAmount().add(detailFlow.getLadderAmount());
        reportDO.setLadderAmount(ladderAmount);
        //小三员基础奖励
        BigDecimal xsyAmount = reportDO.getXsyAmount().add(detailFlow.getXsyAmount());
        reportDO.setXsyAmount(xsyAmount);
        //活动一金额
        BigDecimal firstAmount = reportDO.getActFirstAmount().add(detailFlow.getActFirstAmount());
        reportDO.setActFirstAmount(firstAmount);
        //活动二金额
        BigDecimal secondAmount = reportDO.getActSecondAmount().add(detailFlow.getActSecondAmount());
        reportDO.setActSecondAmount(secondAmount);
        //活动三金额
        BigDecimal thirdAmount = reportDO.getActThirdAmount().add(detailFlow.getActThirdAmount());
        reportDO.setActThirdAmount(thirdAmount);
        //活动四金额
        BigDecimal fourthAmount = reportDO.getActFourthAmount().add(detailFlow.getActFourthAmount());
        reportDO.setActFourthAmount(fourthAmount);
        //活动五金额
        BigDecimal fifthAmount = reportDO.getActFifthAmount().add(detailFlow.getActFifthAmount());
        reportDO.setActFifthAmount(fifthAmount);
        totalAmount = totalAmount.add(salesAmount).add(ladderAmount).add(xsyAmount).add(firstAmount).add(secondAmount).add(thirdAmount).add(fourthAmount).add(fifthAmount);
        reportDO.setTotalAmount(totalAmount);
        reportDO.setOrderAmount(totalAmount);

        //放入明细
        List<ReportDetailFlowDO> flowList = reportDO.getDetailFlowList();
        if (CollUtil.isEmpty(flowList)) {
            reportDO.setDetailFlowList(ListUtil.toList(detailFlow));
        } else {
            flowList.add(detailFlow);
        }
    }

    /**
     * 计算返利信息
     *
     * @param eid
     * @param detailFlow
     * @param outPrice
     * @param goodsInSn
     * @param soResource
     */
    private void calculateRewardFlow(Long eid, ReportDetailFlowDO detailFlow, BigDecimal outPrice,String goodsInSn,String soResource) {
        //查询阶梯参数
        QueryGoodsInfoRequest request = new QueryGoodsInfoRequest();
        request.setEid(eid);
        request.setGoodsInSns(ListUtil.toList(goodsInSn));
        List<ReportLadderGoodsInfoDTO> ladderParList = reportParamApi.queryLadderInfo(request);
        //查询商品参数
        List<ReportParamSubGoodsDTO> activityParList = reportParamApi.queryActivityGoodsInfo(request);
        BigDecimal subRebate = BigDecimal.ZERO;

        //计算阶梯奖励
        ReportLadderGoodsInfoDTO ladderRewardInfo = queryLadderRewardInfo(detailFlow.getYlGoodsId(), detailFlow.getSoQuantity().intValue(), detailFlow.getSoTime(), ladderParList,getFlowSourceByFlowOrderSource(soResource));
        if (ObjectUtil.isNotNull(ladderRewardInfo)) {
            detailFlow.setThresholdCount(ladderRewardInfo.getThresholdCount());
            BigDecimal amount = ladderRewardInfo.getRewardAmount();
            BigDecimal reward;
            //如果是金额
            if (ObjectUtil.equal(ladderRewardInfo.getRewardType(), ReportRewardTypeEnum.CASH.getCode())) {
                reward = amount.multiply(detailFlow.getSoQuantity());
            } else {
                //如果是百分比
                reward = outPrice.multiply(detailFlow.getSoQuantity()).multiply(ladderRewardInfo.getRewardPercentage().divide(BigDecimal.TEN).divide(BigDecimal.TEN));
            }
            detailFlow.setLadderName(ladderRewardInfo.getName());
            detailFlow.setLadderAmount(reward);
            detailFlow.setLadderStartTime(ladderRewardInfo.getStartTime());
            detailFlow.setLadderEndTime(ladderRewardInfo.getEndTime());
            subRebate = subRebate.add(reward);
        }
        Map<Long, ReportParamSubGoodsDTO> activityMap = queryActivityRewardInfo(detailFlow.getYlGoodsId(), detailFlow.getSoTime(), activityParList,getFlowSourceByFlowOrderSource(soResource));
        ReportParamSubGoodsDTO xsy = activityMap.get(ReportActivityIdEnum.XSY.getId());
        ReportParamSubGoodsDTO first = activityMap.get(ReportActivityIdEnum.FIRST.getId());
        ReportParamSubGoodsDTO second = activityMap.get(ReportActivityIdEnum.SECOND.getId());
        ReportParamSubGoodsDTO third = activityMap.get(ReportActivityIdEnum.THIRD.getId());
        ReportParamSubGoodsDTO fourth = activityMap.get(ReportActivityIdEnum.FOURTH.getId());
        ReportParamSubGoodsDTO fifth = activityMap.get(ReportActivityIdEnum.FIFTH.getId());
        //计算小三员奖励
        if (ObjectUtil.isNotNull(xsy)) {
            BigDecimal reward;
            //如果是金额
            if (ObjectUtil.equal(xsy.getRewardType(), ReportRewardTypeEnum.CASH.getCode())) {
                BigDecimal amount = xsy.getRewardAmount();
                reward = amount.multiply(detailFlow.getSoQuantity());
                detailFlow.setXsyPrice(amount);
            } else {
                //如果是百分比
                reward = outPrice.multiply(detailFlow.getSoQuantity()).multiply(xsy.getRewardPercentage().divide(BigDecimal.TEN).divide(BigDecimal.TEN));
                detailFlow.setXsyPrice(xsy.getRewardPercentage());
            }
            detailFlow.setXsyRewardType(xsy.getRewardType());
            detailFlow.setXsyName(xsy.getActivityName());
            detailFlow.setXsyAmount(reward);
            detailFlow.setXsyStartTime(xsy.getStartTime());
            detailFlow.setXsyEndTime(xsy.getEndTime());
            subRebate = subRebate.add(reward);
        }
        //计算活动一奖励
        if (ObjectUtil.isNotNull(first)) {
            detailFlow.setActFirstName(first.getActivityName());
            BigDecimal reward;
            //如果是金额
            if (ObjectUtil.equal(first.getRewardType(), ReportRewardTypeEnum.CASH.getCode())) {
                BigDecimal amount = first.getRewardAmount();
                reward = amount.multiply(detailFlow.getSoQuantity());
            } else {
                //如果是百分比
                reward = outPrice.multiply(detailFlow.getSoQuantity()).multiply(first.getRewardPercentage().divide(BigDecimal.TEN).divide(BigDecimal.TEN));
            }
            detailFlow.setActFirstAmount(reward);
            detailFlow.setActFirstStartTime(first.getStartTime());
            detailFlow.setActFirstEndTime(first.getEndTime());
            subRebate = subRebate.add(reward);
        }
        //计算活动二奖励
        if (ObjectUtil.isNotNull(second)) {
            detailFlow.setActSecondName(second.getActivityName());
            BigDecimal reward;
            //如果是金额
            if (ObjectUtil.equal(second.getRewardType(), ReportRewardTypeEnum.CASH.getCode())) {
                BigDecimal amount = second.getRewardAmount();
                reward = amount.multiply(detailFlow.getSoQuantity());
            } else {
                //如果是百分比
                reward = outPrice.multiply(detailFlow.getSoQuantity()).multiply(second.getRewardPercentage().divide(BigDecimal.TEN).divide(BigDecimal.TEN));
            }
            detailFlow.setActSecondAmount(reward);
            detailFlow.setActSecondStartTime(second.getStartTime());
            detailFlow.setActSecondEndTime(second.getEndTime());
            subRebate = subRebate.add(reward);
        }
        //计算活动三奖励
        if (ObjectUtil.isNotNull(third)) {
            detailFlow.setActThirdName(third.getActivityName());
            BigDecimal reward;
            //如果是金额
            if (ObjectUtil.equal(third.getRewardType(), ReportRewardTypeEnum.CASH.getCode())) {
                BigDecimal amount = third.getRewardAmount();
                reward = amount.multiply(detailFlow.getSoQuantity());
            } else {
                //如果是百分比
                reward = outPrice.multiply(detailFlow.getSoQuantity()).multiply(third.getRewardPercentage().divide(BigDecimal.TEN).divide(BigDecimal.TEN));
            }
            detailFlow.setActThirdAmount(reward);
            detailFlow.setActThirdStartTime(third.getStartTime());
            detailFlow.setActThirdEndTime(third.getEndTime());
            subRebate = subRebate.add(reward);
        }
        //计算活动四奖励
        if (ObjectUtil.isNotNull(fourth)) {
            detailFlow.setActFourthName(fourth.getActivityName());
            BigDecimal reward;
            //如果是金额
            if (ObjectUtil.equal(fourth.getRewardType(), ReportRewardTypeEnum.CASH.getCode())) {
                BigDecimal amount = fourth.getRewardAmount();
                reward = amount.multiply(detailFlow.getSoQuantity());
            } else {
                //如果是百分比
                reward = outPrice.multiply(detailFlow.getSoQuantity()).multiply(fourth.getRewardPercentage().divide(BigDecimal.TEN).divide(BigDecimal.TEN));
            }
            detailFlow.setActFourthAmount(reward);
            detailFlow.setActFourthStartTime(fourth.getStartTime());
            detailFlow.setActFourthEndTime(fourth.getEndTime());
            subRebate = subRebate.add(reward);
        }
        //计算活动五奖励
        if (ObjectUtil.isNotNull(fifth)) {
            detailFlow.setActFifthName(fifth.getActivityName());
            BigDecimal reward;
            //如果是金额
            if (ObjectUtil.equal(fifth.getRewardType(), ReportRewardTypeEnum.CASH.getCode())) {
                BigDecimal amount = fifth.getRewardAmount();
                reward = amount.multiply(detailFlow.getSoQuantity());
            } else {
                //如果是百分比
                reward = outPrice.multiply(detailFlow.getSoQuantity()).multiply(fifth.getRewardPercentage().divide(BigDecimal.TEN).divide(BigDecimal.TEN));
            }
            detailFlow.setActFifthAmount(reward);
            detailFlow.setActFifthStartTime(fifth.getStartTime());
            detailFlow.setActFifthEndTime(fifth.getEndTime());
            subRebate = subRebate.add(reward);
        }
        subRebate = subRebate.add(detailFlow.getSalesAmount());
        detailFlow.setSubRebate(subRebate);
    }

    /**
     * 初始化返利报表
     *
     * @param detailFlow
     * @return
     */
    private void initReportDetailFlow(ReportDetailFlowDO detailFlow, Map<Long, GoodsDTO> goodsMap) {
        GoodsDTO goodsInfo = goodsMap.get(detailFlow.getYlGoodsId());
        if (ObjectUtil.isNotNull(goodsInfo)) {
            detailFlow.setYlGoodsName(goodsInfo.getName());
            detailFlow.setYlGoodsSpecification(goodsInfo.getSellSpecifications());
        }
        detailFlow.setSalesAmount(BigDecimal.ZERO);
        detailFlow.setLadderAmount(BigDecimal.ZERO);
        detailFlow.setXsyAmount(BigDecimal.ZERO);
        detailFlow.setActFirstAmount(BigDecimal.ZERO);
        detailFlow.setActSecondAmount(BigDecimal.ZERO);
        detailFlow.setActThirdAmount(BigDecimal.ZERO);
        detailFlow.setActFourthAmount(BigDecimal.ZERO);
        detailFlow.setActFifthAmount(BigDecimal.ZERO);
        detailFlow.setOpUserId(null);
    }

    /**
     * 设置出货价相关
     *
     * @param detailFlow
     * @param priceParamNameList
     */
    private void setReportDetailFlowPrice(ReportDetailFlowDO detailFlow, List<ReportPriceParamNameDTO> priceParamNameList) {
        //供货价
        BigDecimal supplyPrice = BigDecimal.ZERO;
        //出货价
        BigDecimal outPrice = BigDecimal.ZERO;

        ReportPriceParamNameDTO s = this.getPriceByParam(2L, detailFlow.getYlGoodsId(), priceParamNameList);
        if (null != s) {
            supplyPrice = NumberUtil.round(s.getPrice(), 2, RoundingMode.HALF_UP);
        }
        ReportPriceParamNameDTO o = this.getPriceByParam(1L, detailFlow.getYlGoodsId(), priceParamNameList);
        if (null != o) {
            outPrice = NumberUtil.round(o.getPrice(), 2, RoundingMode.HALF_UP);
        }
        detailFlow.setSupplyPrice(supplyPrice);
        detailFlow.setOutPrice(outPrice);
    }

    /**
     * 计算并生成b2b返利报表
     *
     * @param orderNoList
     * @param inventoryList
     * @param sellerEid
     * @param memberOrderSyncs
     * @param startTime
     * @param endTime
     * @param opUser
     * @param b2bOrderSyncIdenUpdateList 待更新为库存不足的同步订单id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ReportDO saveB2bReport(List<String> orderNoList, List<FlowPurchaseInventoryBO> inventoryList, Long sellerEid, List<MemberOrderSyncBO> memberOrderSyncs, Date startTime, Date endTime, Long opUser, List<Long> b2bOrderSyncIdenUpdateList) {
        //计算返利明细
        ReportDO reportDO = initReport(ReportTypeEnum.B2B, sellerEid, startTime, endTime);
        reportDO.setOpUserId(opUser);
        //计算返利明细
        orderNoList.forEach(orderNo -> {
            calculateB2bOrderRewardByDeliver(reportDO, orderNo, inventoryList, b2bOrderSyncIdenUpdateList);
        });

        //计算会员返利
        calculateMemberReward(reportDO, memberOrderSyncs);
        //保存总表
        boolean isSuccess = save(reportDO);
        if (!isSuccess) {
            log.error("b2b返利报表保存失败,参数={}", reportDO);
            throw new ServiceException("返利报表保存失败");
        }
        //保存明细
        reportDO.getDetailB2bList().forEach(e -> {
            e.setReportId(reportDO.getId());
        });
        if (CollUtil.isNotEmpty(reportDO.getDetailB2bList())) {
            isSuccess = detailB2bService.saveBatch(reportDO.getDetailB2bList());
            if (!isSuccess) {
                log.error("b2b返利报表明细保存失败,参数={}", reportDO.getDetailB2bList());
                throw new ServiceException("返利报表保存失败");
            }
        }
        return reportDO;
    }

    /**
     * 计算会员返利
     *
     * @param reportDO
     * @param memberOrderSyncs
     */
    private void calculateMemberReward(ReportDO reportDO, List<MemberOrderSyncBO> memberOrderSyncs) {
        //查询报表参数
        List<ReportParamSubDTO> paramSubDTOS = paramSubService.queryMemberParInfoByEid(reportDO.getEid());
        if (CollUtil.isEmpty(paramSubDTOS)) {
            return;
        }

        Map<BigDecimal, List<ReportParamSubDTO>> map = paramSubDTOS.stream().collect(Collectors.groupingBy(ReportParamSubDTO::getThresholdAmount));

        Iterator<MemberOrderSyncBO> iterator = memberOrderSyncs.iterator();
        while (iterator.hasNext()) {
            MemberOrderSyncBO e = iterator.next();
            List<ReportParamSubDTO> paramList = map.get(e.getOriginalPrice());
            if (CollUtil.isEmpty(paramList)){
                iterator.remove();
                continue;
            }
            //过滤时间范围、会员来源、会员id
            ReportParamSubDTO paramSubDTO = paramList.stream().filter(par -> DateUtil.compare(par.getStartTime(), e.getOrderCreateTime()) <= 0 && DateUtil.compare(par.getEndTime(), e.getOrderCreateTime()) >= 0 && ObjectUtil.equal(e.getSource(), par.getMemberSource()) &&(ObjectUtil.equal(e.getMemberId(), par.getMemberId())||ObjectUtil.equal(par.getMemberId(),0L))).collect(Collectors.toList()).stream().findAny().orElse(null);
            //该订单价格没有设置返利或来源不匹配
            if (ObjectUtil.isNull(paramSubDTO)) {
                iterator.remove();
                continue;
            }

            //下单时间在活动时间段内
            BigDecimal oldAmount = reportDO.getMemberAmount();
            BigDecimal newAmount;
            BigDecimal subAmount;
            //如果是金额
            if (ObjectUtil.equal(ReportRewardTypeEnum.CASH.getCode(), paramSubDTO.getRewardType())) {
                subAmount = paramSubDTO.getRewardAmount();
                newAmount = oldAmount.add(subAmount);
            } else {
                subAmount = NumberUtil.round(paramSubDTO.getThresholdAmount().multiply(paramSubDTO.getRewardPercentage().divide(BigDecimal.TEN).divide(BigDecimal.TEN)), 2, RoundingMode.HALF_UP);
                newAmount = oldAmount.add(subAmount);
            }
            reportDO.setMemberAmount(newAmount);
            BigDecimal totalAmount = reportDO.getTotalAmount();
            totalAmount = totalAmount.add(subAmount);
            reportDO.setTotalAmount(totalAmount);
        }
    }

    /**
     * 更新订单状态
     *
     * @param reportDO
     * @param updateOrderNoList
     * @param memberOrderSyncs
     * @param b2bOrderSyncIdenUpdateList 待更新为库存不足的同步订单id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateB2bOrderSyncRewardStatus(ReportDO reportDO, List<String> updateOrderNoList, List<MemberSyncDO> memberOrderSyncs, List<Long> b2bOrderSyncIdenUpdateList) {
        Boolean isSuccess;
        if (CollUtil.isNotEmpty(updateOrderNoList)) {
            //更新订单同步表
            B2bOrderSyncDO updateDO = new B2bOrderSyncDO();
            updateDO.setReportId(reportDO.getId());
            updateDO.setReportSettStatus(ReportSettStatusEnum.CALCULATED.getCode());
            updateDO.setReportStatus(ReportStatusEnum.UN_OPERATOR_CONFIRM.getCode());
            LambdaQueryWrapper<B2bOrderSyncDO> updateWrapper = Wrappers.lambdaQuery();
            updateWrapper.in(B2bOrderSyncDO::getOrderNo, updateOrderNoList);
            updateWrapper.ne(B2bOrderSyncDO::getIdentificationStatus, ReportOrderIdenEnum.INVALID.getCode());

            isSuccess = b2bOrderSyncService.update(updateDO, updateWrapper);

            if (!isSuccess) {
                log.error("b2b返利生成时更新订单同步表失败,参数={}", updateOrderNoList);
                throw new ServiceException("返利报表保存失败");
            }
        }
        if (CollUtil.isNotEmpty(memberOrderSyncs)) {

            //更新会员订单
            memberOrderSyncs.forEach(e -> {
                e.setReportId(reportDO.getId());
                e.setReportSettStatus(ReportSettStatusEnum.CALCULATED.getCode());
                e.setReportStatus(ReportStatusEnum.UN_OPERATOR_CONFIRM.getCode());
            });
            isSuccess = memberSyncService.updateBatchById(memberOrderSyncs);
            if (!isSuccess) {
                log.error("b2b返利生成时更新会员订单同步表失败,参数={}", memberOrderSyncs);
                throw new ServiceException("返利报表保存失败");
            }
        }
        //库存不足的修改订单标识
        if (CollUtil.isNotEmpty(b2bOrderSyncIdenUpdateList)) {
            UpdateB2bOrderIdenRequest updateB2bOrderIdenRequest = new UpdateB2bOrderIdenRequest();
            updateB2bOrderIdenRequest.setIdList(b2bOrderSyncIdenUpdateList);
            updateB2bOrderIdenRequest.setUpdateIdenStatus(ReportOrderIdenEnum.ABNORMAL.getCode());
            updateB2bOrderIdenRequest.setAbnormalReason(ReportOrderaAnormalReasonEnum.STOCK_SHORTAGE.getCode());
            isSuccess = b2bOrderSyncService.updateB2bOrderIdentification(updateB2bOrderIdenRequest);
            if (!isSuccess) {
                log.error("b2b返利生成时更新订单同步信息的标识状态失败,参数={}", b2bOrderSyncIdenUpdateList);
                throw new ServiceException("返利报表保存失败");
            }
        }
    }

    /**
     * 初始化返利报表
     *
     * @param reportTypeEnum
     * @param eid
     * @param startTime
     * @param endTime
     * @return
     */
    private ReportDO initReport(ReportTypeEnum reportTypeEnum, Long eid, Date startTime, Date endTime) {
        ReportDO reportDO = new ReportDO();
        reportDO.setEid(eid);
        reportDO.setType(reportTypeEnum.getCode());
        reportDO.setMemberAmount(BigDecimal.ZERO);
        reportDO.setSalesAmount(BigDecimal.ZERO);
        reportDO.setTerminalSalesAmount(BigDecimal.ZERO);
        reportDO.setLadderAmount(BigDecimal.ZERO);
        reportDO.setXsyAmount(BigDecimal.ZERO);
        reportDO.setActFirstAmount(BigDecimal.ZERO);
        reportDO.setActSecondAmount(BigDecimal.ZERO);
        reportDO.setActThirdAmount(BigDecimal.ZERO);
        reportDO.setActFourthAmount(BigDecimal.ZERO);
        reportDO.setActFifthAmount(BigDecimal.ZERO);
        reportDO.setStartTime(startTime);
        reportDO.setEndTime(endTime);
        reportDO.setDetailB2bList(ListUtil.toList());
        reportDO.setDetailFlowList(ListUtil.toList());
        reportDO.setTotalAmount(BigDecimal.ZERO);
        return reportDO;
    }

    /**
     * 初始化返利报表
     *
     * @param detailB2b
     * @return
     */
    private void initReportDetailB2b(ReportDetailB2bDO detailB2b) {
        detailB2b.setSalesAmount(BigDecimal.ZERO);
        detailB2b.setLadderAmount(BigDecimal.ZERO);
        detailB2b.setXsyAmount(BigDecimal.ZERO);
        detailB2b.setActFirstAmount(BigDecimal.ZERO);
        detailB2b.setActSecondAmount(BigDecimal.ZERO);
        detailB2b.setActThirdAmount(BigDecimal.ZERO);
        detailB2b.setActFourthAmount(BigDecimal.ZERO);
        detailB2b.setActFifthAmount(BigDecimal.ZERO);
        detailB2b.setOpUserId(null);
    }

    /**
     * 根据订单的配送单计算订单返利
     *
     * @param reportDO
     * @param orderNo
     * @param inventoryList
     * @param b2bOrderSyncIdenUpdateList 待更新为库存不足的同步订单id
     */
    private void calculateB2bOrderRewardByDeliver(ReportDO reportDO, String orderNo, List<FlowPurchaseInventoryBO> inventoryList, List<Long> b2bOrderSyncIdenUpdateList) {
        //查询订单
        OrderDTO orderDTO = orderApi.selectByOrderNo(orderNo);
        List<B2bOrderSyncBO> orderGoodsList = b2bOrderSyncService.queryOrderSync(orderDTO.getOrderNo()).stream().filter(e -> ObjectUtil.notEqual(e.getIdentificationStatus(), ReportOrderIdenEnum.INVALID.getCode()) && ObjectUtil.equal(e.getReportSettStatus(), ReportSettStatusEnum.UN_CALCULATE.getCode())).collect(Collectors.toList());
        if (CollUtil.isEmpty(orderGoodsList)) {
            return;
        }

        //商品id集合
        List<Long> goodsIdList = orderGoodsList.stream().map(B2bOrderSyncBO::getGoodsId).distinct().collect(Collectors.toList());
        //商品erp内码集合
        List<String> goodsErpCodeList = orderGoodsList.stream().map(B2bOrderSyncBO::getGoodsErpCode).distinct().collect(Collectors.toList());
        //查询orderChange
        List<OrderDetailChangeDTO> changeList = orderDetailChangeApi.listByOrderId(orderDTO.getId());
        Map<Long, OrderDetailChangeDTO> orderDetailChangeMap = changeList.stream().collect(Collectors.toMap(OrderDetailChangeDTO::getDetailId, e -> e));

        //查询参数
        //查询价格信息
        List<ReportPriceParamNameDTO> priceParamNameList = PojoUtils.map(goodsYilingPriceApi.getPriceParamNameList(goodsIdList, orderDTO.getCreateTime()), ReportPriceParamNameDTO.class);
        //查询商品分类
        QueryGoodsCategoryRequest queryGoodsCategoryRequest = new QueryGoodsCategoryRequest();
        queryGoodsCategoryRequest.setDate(orderDTO.getCreateTime());
        queryGoodsCategoryRequest.setGoodsIds(goodsIdList);
        List<ReportYlGoodsCategoryDTO> reportParamSubGoodsDTOS = reportParamApi.queryCategoryByYlGoodsIds(queryGoodsCategoryRequest);
        Map<Long, ReportYlGoodsCategoryDTO> categoryDTOMap = reportParamSubGoodsDTOS.stream().collect(Collectors.toMap(ReportYlGoodsCategoryDTO::getYlGoodsId, Function.identity()));

        //查询采购库存
        Map<String, FlowPurchaseInventoryBO> inventoryBOMap = queryGoodsInventoryB2b(inventoryList, orderGoodsList, reportDO.getEid());

        Map<Long, OrderDetailChangeDTO> finalOrderDetailChangeMap = orderDetailChangeMap;
        orderGoodsList.forEach(orderGoodsReceiveInfo -> {
            ReportDetailB2bDO detailB2b = PojoUtils.map(orderGoodsReceiveInfo, ReportDetailB2bDO.class);
            detailB2b.setSellerEid(orderDTO.getSellerEid());
            detailB2b.setBuyerEid(orderDTO.getBuyerEid());
            detailB2b.setOrderNo(orderDTO.getOrderNo());
            detailB2b.setOrderSource(orderDTO.getOrderSource());
            detailB2b.setPaymentMethod(orderDTO.getPaymentMethod());
            detailB2b.setOrderCreateTime(orderDTO.getCreateTime());

            initReportDetailB2b(detailB2b);

            //设置参数信息如出货价
            setReportDetailB2bPrice(detailB2b, priceParamNameList, categoryDTOMap);

            //销售额返利
            String key = FlowPurchaseInventoryBO.getKey(orderDTO.getSellerEid(), orderGoodsReceiveInfo.getGoodsId(), orderGoodsReceiveInfo.getGoodsErpCode());
            ReportPurchaseChannelEnum channelEnum = calculatePurchaseChannel(inventoryBOMap, key, Long.valueOf(detailB2b.getReceiveQuantity()));
            //库存不足的更新订单同步信息的标识状态
            if (ObjectUtil.equal(ReportPurchaseChannelEnum.SHORTAGE.getCode(), channelEnum.getCode()) && ObjectUtil.equal(ReportOrderIdenEnum.NORMAL.getCode(), orderGoodsReceiveInfo.getIdentificationStatus())) {
                b2bOrderSyncIdenUpdateList.add(orderGoodsReceiveInfo.getId());
                detailB2b.setIdentificationStatus(ReportOrderIdenEnum.ABNORMAL.getCode());
                detailB2b.setAbnormalReason(ReportOrderaAnormalReasonEnum.STOCK_SHORTAGE.getCode());
            }
            detailB2b.setPurchaseChannel(channelEnum.getCode());
            //购进渠道为大运河时才计算
            if (ObjectUtil.equal(ReportPurchaseChannelEnum.DYH.getCode(), channelEnum.getCode())) {
                BigDecimal rebateMoney = BigDecimal.ZERO;
                if (detailB2b.getSupplyPrice().compareTo(BigDecimal.ZERO) != 0) {
                    rebateMoney = NumberUtil.round(detailB2b.getSupplyPrice().multiply(BigDecimal.valueOf(detailB2b.getReceiveQuantity())).multiply(new BigDecimal(GoodsConstant.REBATE_RATE)), 2, RoundingMode.HALF_UP);
                }
                detailB2b.setSalesAmount(rebateMoney);
            }

            //判断如果是非以岭品则就此结束
            if (ObjectUtil.notEqual(orderGoodsReceiveInfo.getOrderGoodsType(), 0)) {
                //计算奖励
                calculateRewardB2b(detailB2b, detailB2b.getOutPrice(), finalOrderDetailChangeMap.get(orderGoodsReceiveInfo.getDetailId()), goodsErpCodeList);
            }
            //加入总表
            joinReportB2b(reportDO, detailB2b);
        });
    }

    /**
     * 计算商品的购进渠道
     *
     * @param inventoryBOMap
     * @param key
     * @param count
     * @return
     */
    ReportPurchaseChannelEnum calculatePurchaseChannel(Map<String, FlowPurchaseInventoryBO> inventoryBOMap, String key, Long count) {
        FlowPurchaseInventoryBO inventoryBO = inventoryBOMap.get(key);
        if (ObjectUtil.isNull(inventoryBO)) {
            return ReportPurchaseChannelEnum.SHORTAGE;
        }
        Long dyhQuantity = inventoryBO.getDyhQuantity();
        if (dyhQuantity >= count) {
            if (ObjectUtil.isNull(inventoryBO.getDyhId()) || ObjectUtil.equal(inventoryBO.getDyhId(), 0L)) {
                return ReportPurchaseChannelEnum.SHORTAGE;
            }
            dyhQuantity = dyhQuantity - count;
            inventoryBO.setDyhQuantity(dyhQuantity);
            Long dyhOpQuantity = inventoryBO.getDyhOpQuantity();
            dyhOpQuantity = dyhOpQuantity - count;
            inventoryBO.setDyhOpQuantity(dyhOpQuantity);
            return ReportPurchaseChannelEnum.DYH;
        }
        Long jdQuantity = inventoryBO.getJdQuantity();
        if (jdQuantity >= count) {
            if (ObjectUtil.isNull(inventoryBO.getJdId()) || ObjectUtil.equal(inventoryBO.getJdId(), 0L)) {
                return ReportPurchaseChannelEnum.SHORTAGE;
            }
            jdQuantity = jdQuantity - count;
            inventoryBO.setJdQuantity(jdQuantity);
            Long jdOpQuantity = inventoryBO.getJdOpQuantity();
            jdOpQuantity = jdOpQuantity - count;
            inventoryBO.setJdOpQuantity(jdOpQuantity);
            return ReportPurchaseChannelEnum.JD;
        }
        return ReportPurchaseChannelEnum.SHORTAGE;
    }

    /**
     * 根据配送单查询内码库存
     *
     * @param inventoryList
     * @param orderGoodsList
     * @param eid
     * @return
     */
    private Map<String, FlowPurchaseInventoryBO> queryGoodsInventoryB2b(List<FlowPurchaseInventoryBO> inventoryList, List<B2bOrderSyncBO> orderGoodsList, Long eid) {
        HashMap<String, FlowPurchaseInventoryBO> map = MapUtil.newHashMap();
        //需要查询库存的商品列表
        List<QueryFlowPurchaseInventoryBO> queryBoList = ListUtil.toList();

        orderGoodsList.forEach(e -> {
            FlowPurchaseInventoryBO var = new FlowPurchaseInventoryBO();
            var.setEid(eid);
            var.setYlGoodsId(e.getGoodsId());
            var.setGoodsInSn(e.getGoodsErpCode());
            //如果还没有库存信息则查询且商品存在内码
            if (!inventoryList.contains(var) && StrUtil.isNotBlank(e.getGoodsErpCode())) {
                QueryFlowPurchaseInventoryBO queryBo = new QueryFlowPurchaseInventoryBO();
                queryBo.setEid(eid);
                queryBo.setYlGoodsId(e.getGoodsId());
                queryBo.setGoodsInSn(e.getGoodsErpCode());
                queryBoList.add(queryBo);
            }
        });
        //查询库存
        if (CollUtil.isNotEmpty(queryBoList)) {
            //调用接口查询
            List<QueryFlowPurchaseInventorySettlementDetailRequest> list = PojoUtils.map(queryBoList, QueryFlowPurchaseInventorySettlementDetailRequest.class);
            QueryFlowPurchaseInventorySettlementRequest request = new QueryFlowPurchaseInventorySettlementRequest();
            request.setList(list);
            List<com.yiling.dataflow.order.bo.FlowPurchaseInventoryBO> inventory = flowPurchaseInventoryApi.getListByEidAndYlGoodsIdAndGoodsInSn(request);
            List<FlowPurchaseInventoryBO> queryResult = PojoUtils.map(inventory, FlowPurchaseInventoryBO.class);
            queryResult.forEach(e -> {
                if (ObjectUtil.isNull(e.getDyhQuantity())) {
                    e.setDyhQuantity(0L);
                }
                if (ObjectUtil.isNull(e.getJdQuantity())) {
                    e.setJdQuantity(0L);
                }
            });
            //放入库存列表
            inventoryList.addAll(queryResult);
        }
        if (CollUtil.isEmpty(inventoryList)) {
            return map;
        }
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

    /**
     * 根据流向订单查询内码库存
     *
     * @param inventoryList
     * @param inventoryList
     * @return
     */
    private Map<String, FlowPurchaseInventoryBO> queryGoodsInventoryFlow(List<FlowPurchaseInventoryBO> inventoryList, List<FlowSaleOrderSyncDO> orderDetail, Long eid) {
        HashMap<String, FlowPurchaseInventoryBO> map = MapUtil.newHashMap();
        //需要查询库存的商品列表
        List<QueryFlowPurchaseInventoryBO> queryBoList = ListUtil.toList();

        orderDetail.forEach(e -> {
            FlowPurchaseInventoryBO var = new FlowPurchaseInventoryBO();
            var.setEid(eid);
            var.setYlGoodsId(e.getYlGoodsId());
            var.setGoodsInSn(e.getGoodsInSn());
            //如果还没有库存信息则查询且商品内码不为空
            if (!inventoryList.contains(var) && StrUtil.isNotBlank(e.getGoodsInSn())) {
                QueryFlowPurchaseInventoryBO queryRequest = new QueryFlowPurchaseInventoryBO();
                queryRequest.setEid(eid);
                queryRequest.setYlGoodsId(e.getYlGoodsId());
                queryRequest.setGoodsInSn(e.getGoodsInSn());
                queryBoList.add(queryRequest);
            }
        });
        //查询库存
        if (CollUtil.isNotEmpty(queryBoList)) {
            List<QueryFlowPurchaseInventorySettlementDetailRequest> list = PojoUtils.map(queryBoList, QueryFlowPurchaseInventorySettlementDetailRequest.class);
            QueryFlowPurchaseInventorySettlementRequest request = new QueryFlowPurchaseInventorySettlementRequest();
            request.setList(list);
            List<com.yiling.dataflow.order.bo.FlowPurchaseInventoryBO> inventory = flowPurchaseInventoryApi.getListByEidAndYlGoodsIdAndGoodsInSn(request);
            List<FlowPurchaseInventoryBO> queryResult = PojoUtils.map(inventory, FlowPurchaseInventoryBO.class);
            queryResult.forEach(e -> {
                if (ObjectUtil.isNull(e.getDyhQuantity())) {
                    e.setDyhQuantity(0L);
                }
                if (ObjectUtil.isNull(e.getJdQuantity())) {
                    e.setJdQuantity(0L);
                }
            });
            //放入库存列表
            inventoryList.addAll(queryResult);
        }
        if (CollUtil.isEmpty(inventoryList)) {
            return map;
        }
        inventoryList.forEach(e -> {
            String key = FlowPurchaseInventoryBO.getKey(e.getEid(), e.getYlGoodsId(), e.getGoodsInSn());
            if (map.containsKey(key)) {
                log.error("查询采购库存时数据key存在重复，key={}", key);
                throw new ServiceException("返利报表保存失败");
            }
            if (ObjectUtil.isNull(e.getDyhQuantity())) {
                e.setDyhQuantity(0L);
            }
            if (ObjectUtil.isNull(e.getJdQuantity())) {
                e.setJdQuantity(0L);
            }
            map.put(key, e);
        });
        return map;
    }

    /**
     * 计算总表金额
     *
     * @param reportDO
     * @param detailB2b
     */
    private void joinReportB2b(ReportDO reportDO, ReportDetailB2bDO detailB2b) {

        BigDecimal totalAmount = BigDecimal.ZERO;

        //销售额返利
        BigDecimal salesAmount = reportDO.getSalesAmount().add(detailB2b.getSalesAmount());
        reportDO.setSalesAmount(salesAmount);
        //终端促销返利
        BigDecimal terminal = reportDO.getTerminalSalesAmount().add(detailB2b.getPlatformAmount());
        reportDO.setTerminalSalesAmount(terminal);
        //阶梯金额
        BigDecimal ladderAmount = reportDO.getLadderAmount().add(detailB2b.getLadderAmount());
        reportDO.setLadderAmount(ladderAmount);
        //小三员基础奖励
        BigDecimal xsyAmount = reportDO.getXsyAmount().add(detailB2b.getXsyAmount());
        reportDO.setXsyAmount(xsyAmount);
        //活动一金额
        BigDecimal firstAmount = reportDO.getActFirstAmount().add(detailB2b.getActFirstAmount());
        reportDO.setActFirstAmount(firstAmount);
        //活动二金额
        BigDecimal secondAmount = reportDO.getActSecondAmount().add(detailB2b.getActSecondAmount());
        reportDO.setActSecondAmount(secondAmount);
        //活动三金额
        BigDecimal thirdAmount = reportDO.getActThirdAmount().add(detailB2b.getActThirdAmount());
        reportDO.setActThirdAmount(thirdAmount);
        //活动四金额
        BigDecimal fourthAmount = reportDO.getActFourthAmount().add(detailB2b.getActFourthAmount());
        reportDO.setActFourthAmount(fourthAmount);
        //活动五金额
        BigDecimal fifthAmount = reportDO.getActFifthAmount().add(detailB2b.getActFifthAmount());
        reportDO.setActFifthAmount(fifthAmount);
        totalAmount = totalAmount.add(salesAmount).add(terminal).add(ladderAmount).add(xsyAmount).add(firstAmount).add(secondAmount).add(thirdAmount).add(fourthAmount).add(fifthAmount);
        reportDO.setTotalAmount(totalAmount);
        reportDO.setOrderAmount(totalAmount);
        //放入明细
        List<ReportDetailB2bDO> detailB2bList = reportDO.getDetailB2bList();
        if (CollUtil.isEmpty(detailB2bList)) {
            reportDO.setDetailB2bList(ListUtil.toList(detailB2b));
        } else {
            detailB2bList.add(detailB2b);
        }
    }

    /**
     * 设置出货价相关
     *
     * @param detailB2b
     * @param priceParamNameList
     * @param categoryDTOMap
     */
    private void setReportDetailB2bPrice(ReportDetailB2bDO detailB2b, List<ReportPriceParamNameDTO> priceParamNameList, Map<Long, ReportYlGoodsCategoryDTO> categoryDTOMap) {
        ReportYlGoodsCategoryDTO categoryDTO = categoryDTOMap.get(detailB2b.getGoodsId());
        if (Objects.nonNull(categoryDTO)) {
            detailB2b.setGoodsType(categoryDTO.getName());
        }
        //供货价
        BigDecimal supplyPrice = BigDecimal.ZERO;
        //出货价
        BigDecimal outPrice = BigDecimal.ZERO;
        //商销价
        BigDecimal goodsSellPrice = BigDecimal.ZERO;

        ReportPriceParamNameDTO s = this.getPriceByParam(2L, detailB2b.getGoodsId(), priceParamNameList);
        if (null != s) {
            supplyPrice = NumberUtil.round(s.getPrice(), 2, RoundingMode.HALF_UP);
        }
        ReportPriceParamNameDTO o = this.getPriceByParam(1L, detailB2b.getGoodsId(), priceParamNameList);
        if (null != o) {
            outPrice = NumberUtil.round(o.getPrice(), 2, RoundingMode.HALF_UP);
        }
        ReportPriceParamNameDTO b = this.getPriceByParam(3L, detailB2b.getGoodsId(), priceParamNameList);
        if (ObjectUtil.isNotNull(b)) {
            goodsSellPrice = NumberUtil.round(b.getPrice(), 2, RoundingMode.HALF_UP);
        }
        detailB2b.setSupplyPrice(supplyPrice);
        detailB2b.setOutPrice(outPrice);
        detailB2b.setMerchantSalePrice(goodsSellPrice);
    }

    /**
     * 设置活动相关
     *
     * @param detailB2b
     * @param detailDTO
     */
    private void calculateActivityInfo(ReportDetailB2bDO detailB2b, OrderDetailDTO detailDTO, Map<Long, PromotionActivityDTO> activityMap) {

        //秒杀特价商品小计=原价*数量
        //秒杀特价优惠金额=原价*数量-goodsAmount
        if (ObjectUtil.equal(detailDTO.getPromotionActivityType(), PromotionActivityTypeEnum.SPECIAL.getCode()) || ObjectUtil.equal(detailDTO.getPromotionActivityType(), PromotionActivityTypeEnum.LIMIT.getCode()) || ObjectUtil.equal(detailDTO.getPromotionActivityType(), PromotionActivityTypeEnum.COMBINATION.getCode())) {
            BigDecimal sellAmount = detailB2b.getOriginalPrice().subtract(detailDTO.getGoodsPrice()).multiply(new BigDecimal(detailB2b.getReceiveQuantity()));
            //如果秒杀特价金额为负数
            if (BigDecimal.ZERO.compareTo(sellAmount) == 1) {
                sellAmount = BigDecimal.ZERO;
            }

            detailB2b.setGoodsAmount(NumberUtil.round(detailB2b.getGoodsPrice().multiply(new BigDecimal(detailB2b.getGoodsQuantity())), 2, RoundingMode.HALF_UP));

            //            detailB2b.setPaymentAmount(NumberUtil.round(detailDTO.getGoodsAmount(), 2, RoundingMode.HALF_UP));
            detailB2b.setPaymentAmount(NumberUtil.round(detailDTO.getGoodsPrice().multiply(new BigDecimal(detailDTO.getReceiveQuantity().toString())), 2, RoundingMode.HALF_UP));

            detailB2b.setDiscountAmount(NumberUtil.round(sellAmount, 2, RoundingMode.HALF_UP));
            BigDecimal percentage = BigDecimal.ONE.subtract(detailDTO.getGoodsPrice().divide(detailDTO.getOriginalPrice(), 5, RoundingMode.HALF_UP));
            //如果秒杀特价金额为负数百分之置为0
            if (BigDecimal.ZERO.compareTo(percentage) == 1) {
                percentage = BigDecimal.ZERO;
            }
            detailB2b.setDiscountPercentage(percentage);

            //设置活动的平台承担百分比
            PromotionActivityDTO activityDTO = activityMap.get(detailDTO.getPromotionActivityId());
            detailB2b.setActivityType(PromotionActivityTypeEnum.getByCode(detailDTO.getPromotionActivityType()).getName());
            detailB2b.setActivityDescribe(activityDTO.getName());
            BigDecimal platformPercentage = activityDTO.getPlatformPercent().divide(BigDecimal.TEN).divide(BigDecimal.TEN);
            //平台承担折扣金额=折扣金额*平台承担比例
            detailB2b.setPlatformAmount(NumberUtil.round(detailB2b.getDiscountAmount().multiply(platformPercentage), 2, RoundingMode.HALF_UP));
            //平台承担折扣比例=折扣比率*平台承担比例
            detailB2b.setPlatformPercentage(NumberUtil.round(detailB2b.getDiscountPercentage().multiply(platformPercentage), 5, RoundingMode.HALF_UP));
            BigDecimal shopPercentage = activityDTO.getMerchantPercent().divide(BigDecimal.TEN).divide(BigDecimal.TEN);
            //商家承担折扣金额=折扣金额*商家承担比例
            detailB2b.setShopAmount(NumberUtil.round(detailB2b.getDiscountAmount().multiply(shopPercentage), 2, RoundingMode.HALF_UP));
            //商家承担折扣比例=折扣比率*商家承担比例
            detailB2b.setShopPercentage(NumberUtil.round(detailB2b.getDiscountPercentage().multiply(shopPercentage), 5, RoundingMode.HALF_UP));

        } else {
            //折扣金额
            //优惠券金额=商家+平台
            BigDecimal sellAmount = detailDTO.getReceivePlatformCouponDiscountAmount().add(detailDTO.getReceiveCouponDiscountAmount());
            //实付金额=商品单价*签收数量-促销金额
            BigDecimal price = detailDTO.getGoodsPrice();
            BigDecimal goodsAmount = price.multiply(new BigDecimal(detailB2b.getReceiveQuantity().toString()));
            detailB2b.setPaymentAmount(NumberUtil.round(goodsAmount.subtract(sellAmount), 2, RoundingMode.HALF_UP));

            detailB2b.setDiscountAmount(NumberUtil.round(sellAmount, 2, RoundingMode.HALF_UP));
            if (BigDecimal.ZERO.compareTo(sellAmount) == 0) {
                detailB2b.setDiscountPercentage(BigDecimal.ZERO);
            } else {
                detailB2b.setDiscountPercentage(NumberUtil.round(sellAmount.divide(detailDTO.getGoodsAmount(), 5, RoundingMode.HALF_UP), 2, RoundingMode.HALF_UP));
            }
        }
    }

    /**
     * 计算优惠券相关
     *
     * @param detailB2b
     * @param detailDTO
     * @param orderDetailChangeMap
     * @param couponUseDTOMap
     */
    private void calculateCouponInfo(ReportDetailB2bDO detailB2b, OrderDetailDTO detailDTO, Map<Long, OrderDetailChangeDTO> orderDetailChangeMap, Map<Integer, OrderCouponUseDTO> couponUseDTOMap) {
        OrderDetailChangeDTO changeDTO = orderDetailChangeMap.get(detailB2b.getDetailId());
        if (ObjectUtil.isNull(changeDTO)) {
            return;
        }
        //如果用了优惠券
        if (BigDecimal.ZERO.compareTo(changeDTO.getReceivePlatformCouponDiscountAmount()) == -1 || BigDecimal.ZERO.compareTo(changeDTO.getReceiveCouponDiscountAmount()) == -1) {
            detailB2b.setActivityType("优惠券");
            BigDecimal platformAmount = BigDecimal.ZERO;
            BigDecimal shopAmount = BigDecimal.ZERO;
            //如果平台券金额不为0
            if (BigDecimal.ZERO.compareTo(changeDTO.getReceivePlatformCouponDiscountAmount()) != 0) {
                OrderCouponUseDTO couponUseDTO = couponUseDTOMap.get(1);
                BigDecimal amount = changeDTO.getReceivePlatformCouponDiscountAmount();
                //平台承担金额
                BigDecimal pa = amount.multiply(couponUseDTO.getPlatformRatio().divide(BigDecimal.TEN).divide(BigDecimal.TEN));
                //商家承担金额
                BigDecimal sa = amount.subtract(pa);
                platformAmount = platformAmount.add(pa);
                shopAmount = shopAmount.add(sa);
                CouponActivityRulesBO activityRulesBO = couponActivityApi.getCouponActivityRulesById(couponUseDTO.getCouponActivityId());
                if (ObjectUtil.isNotNull(activityRulesBO)) {
                    detailB2b.setActivityDescribe(activityRulesBO.getCouponRules());
                }
            }
            //如果商家券金额不为0
            if (BigDecimal.ZERO.compareTo(changeDTO.getReceiveCouponDiscountAmount()) != 0) {
                OrderCouponUseDTO couponUseDTO = couponUseDTOMap.get(2);
                BigDecimal amount = changeDTO.getReceiveCouponDiscountAmount();
                //平台承担金额
                BigDecimal pa = amount.multiply(couponUseDTO.getPlatformRatio().divide(BigDecimal.TEN).divide(BigDecimal.TEN));
                //商家承担金额
                BigDecimal sa = amount.subtract(pa);
                platformAmount = platformAmount.add(pa);
                shopAmount = shopAmount.add(sa);
                CouponActivityRulesBO activityRulesBO = couponActivityApi.getCouponActivityRulesById(couponUseDTO.getCouponActivityId());
                if (ObjectUtil.isNotNull(activityRulesBO)) {
                    String content = detailB2b.getActivityDescribe();
                    if (StrUtil.isBlank(content)) {
                        detailB2b.setActivityDescribe(activityRulesBO.getCouponRules());
                    } else {
                        detailB2b.setActivityDescribe(content + StrUtil.CR + activityRulesBO.getCouponRules());
                    }
                }
            }
            //收货小计=收货数量*商品单价
            BigDecimal receiveAmount = detailB2b.getGoodsPrice().multiply(new BigDecimal(detailB2b.getReceiveQuantity().toString()));

            detailB2b.setPlatformAmount(NumberUtil.round(platformAmount, 2, RoundingMode.HALF_UP));
            detailB2b.setShopAmount(NumberUtil.round(shopAmount, 2, RoundingMode.HALF_UP));
            //平台承担比例=平台承担金额/收货小计
            detailB2b.setPlatformPercentage(platformAmount.divide(receiveAmount, 5, RoundingMode.HALF_UP));
            //商家承担比例=商家承担金额/收货小计
            detailB2b.setShopPercentage(shopAmount.divide(receiveAmount, 5, RoundingMode.HALF_UP));
        }
    }

    /**
     * 计算返利信息
     *
     * @param detailB2b
     * @param outPrice
     * @param changeDTO
     * @param goodsInSnList
     */
    private void calculateRewardB2b(ReportDetailB2bDO detailB2b, BigDecimal outPrice, OrderDetailChangeDTO changeDTO, List<String> goodsInSnList) {
        //查询阶梯参数
        QueryGoodsInfoRequest request = new QueryGoodsInfoRequest();
        request.setEid(detailB2b.getSellerEid());
        request.setGoodsInSns(goodsInSnList);
        List<ReportLadderGoodsInfoDTO> ladderParList = reportParamApi.queryLadderInfo(request);
        //查询商品参数
        List<ReportParamSubGoodsDTO> activityParList = reportParamApi.queryActivityGoodsInfo(request);
        BigDecimal subRebate = BigDecimal.ZERO.add(detailB2b.getPlatformAmount());

        //本发货单收货数量
        Integer receiveQuantity = detailB2b.getReceiveQuantity();
        //计算阶梯奖励
        ReportLadderGoodsInfoDTO ladderRewardInfo = queryLadderRewardInfo(detailB2b.getGoodsId(), changeDTO.getReceiveQuantity(), detailB2b.getOrderCreateTime(), ladderParList,ReportParSubGoodsOrderSourceEnum.B2B);
        if (ObjectUtil.isNotNull(ladderRewardInfo)) {
            detailB2b.setThresholdCount(ladderRewardInfo.getThresholdCount());

            BigDecimal amount = ladderRewardInfo.getRewardAmount();
            BigDecimal reward;
            //如果是金额
            if (ObjectUtil.equal(ladderRewardInfo.getRewardType(), ReportRewardTypeEnum.CASH.getCode())) {
                reward = amount.multiply(new BigDecimal(receiveQuantity));
            } else {
                //如果是百分比
                reward = outPrice.multiply(new BigDecimal(receiveQuantity)).multiply(ladderRewardInfo.getRewardPercentage().divide(BigDecimal.TEN).divide(BigDecimal.TEN));
            }

            detailB2b.setLadderName(ladderRewardInfo.getName());
            detailB2b.setLadderAmount(reward);
            detailB2b.setLadderStartTime(ladderRewardInfo.getStartTime());
            detailB2b.setLadderEndTime(ladderRewardInfo.getEndTime());
            subRebate = subRebate.add(reward);
        }
        Map<Long, ReportParamSubGoodsDTO> activityMap = queryActivityRewardInfo(detailB2b.getGoodsId(), detailB2b.getOrderCreateTime(), activityParList,ReportParSubGoodsOrderSourceEnum.B2B);
        ReportParamSubGoodsDTO xsy = activityMap.get(ReportActivityIdEnum.XSY.getId());
        ReportParamSubGoodsDTO first = activityMap.get(ReportActivityIdEnum.FIRST.getId());
        ReportParamSubGoodsDTO second = activityMap.get(ReportActivityIdEnum.SECOND.getId());
        ReportParamSubGoodsDTO third = activityMap.get(ReportActivityIdEnum.THIRD.getId());
        ReportParamSubGoodsDTO fourth = activityMap.get(ReportActivityIdEnum.FOURTH.getId());
        ReportParamSubGoodsDTO fifth = activityMap.get(ReportActivityIdEnum.FIFTH.getId());
        //计算小三员奖励
        if (ObjectUtil.isNotNull(xsy)) {
            BigDecimal reward;
            //如果是金额
            if (ObjectUtil.equal(xsy.getRewardType(), ReportRewardTypeEnum.CASH.getCode())) {
                BigDecimal amount = xsy.getRewardAmount();
                reward = amount.multiply(new BigDecimal(receiveQuantity));
                detailB2b.setXsyPrice(amount);
            } else {
                //如果是百分比
                reward = outPrice.multiply(new BigDecimal(receiveQuantity)).multiply(xsy.getRewardPercentage().divide(BigDecimal.TEN).divide(BigDecimal.TEN));
                detailB2b.setXsyPrice(xsy.getRewardPercentage());
            }
            detailB2b.setXsyRewardType(xsy.getRewardType());
            detailB2b.setXsyName(xsy.getActivityName());
            detailB2b.setXsyAmount(reward);
            detailB2b.setXsyStartTime(xsy.getStartTime());
            detailB2b.setXsyEndTime(xsy.getEndTime());
            subRebate = subRebate.add(reward);
        }
        //计算活动一奖励
        if (ObjectUtil.isNotNull(first)) {
            detailB2b.setActFirstName(first.getActivityName());
            BigDecimal reward;
            //如果是金额
            if (ObjectUtil.equal(first.getRewardType(), ReportRewardTypeEnum.CASH.getCode())) {
                BigDecimal amount = first.getRewardAmount();
                reward = amount.multiply(new BigDecimal(receiveQuantity));
            } else {
                //如果是百分比
                reward = outPrice.multiply(new BigDecimal(receiveQuantity)).multiply(first.getRewardPercentage().divide(BigDecimal.TEN).divide(BigDecimal.TEN));
            }
            detailB2b.setActFirstAmount(reward);
            detailB2b.setActFirstStartTime(first.getStartTime());
            detailB2b.setActFirstEndTime(first.getEndTime());
            subRebate = subRebate.add(reward);
        }
        //计算活动二奖励
        if (ObjectUtil.isNotNull(second)) {
            detailB2b.setActSecondName(second.getActivityName());
            BigDecimal reward;
            //如果是金额
            if (ObjectUtil.equal(second.getRewardType(), ReportRewardTypeEnum.CASH.getCode())) {
                BigDecimal amount = second.getRewardAmount();
                reward = amount.multiply(new BigDecimal(receiveQuantity));
            } else {
                //如果是百分比
                reward = outPrice.multiply(new BigDecimal(receiveQuantity)).multiply(second.getRewardPercentage().divide(BigDecimal.TEN).divide(BigDecimal.TEN));
            }
            detailB2b.setActSecondAmount(reward);
            detailB2b.setActSecondStartTime(second.getStartTime());
            detailB2b.setActSecondEndTime(second.getEndTime());
            subRebate = subRebate.add(reward);
        }
        //计算活动三奖励
        if (ObjectUtil.isNotNull(third)) {
            detailB2b.setActThirdName(third.getActivityName());
            BigDecimal reward;
            //如果是金额
            if (ObjectUtil.equal(third.getRewardType(), ReportRewardTypeEnum.CASH.getCode())) {
                BigDecimal amount = third.getRewardAmount();
                reward = amount.multiply(new BigDecimal(receiveQuantity));
            } else {
                //如果是百分比
                reward = outPrice.multiply(new BigDecimal(receiveQuantity)).multiply(third.getRewardPercentage().divide(BigDecimal.TEN).divide(BigDecimal.TEN));
            }
            detailB2b.setActThirdAmount(reward);
            detailB2b.setActThirdStartTime(third.getStartTime());
            detailB2b.setActThirdEndTime(third.getEndTime());
            subRebate = subRebate.add(reward);
        }
        //计算活动四奖励
        if (ObjectUtil.isNotNull(fourth)) {
            detailB2b.setActFourthName(fourth.getActivityName());
            BigDecimal reward;
            //如果是金额
            if (ObjectUtil.equal(fourth.getRewardType(), ReportRewardTypeEnum.CASH.getCode())) {
                BigDecimal amount = fourth.getRewardAmount();
                reward = amount.multiply(new BigDecimal(receiveQuantity));
            } else {
                //如果是百分比
                reward = outPrice.multiply(new BigDecimal(receiveQuantity)).multiply(fourth.getRewardPercentage().divide(BigDecimal.TEN).divide(BigDecimal.TEN));
            }
            detailB2b.setActFourthAmount(reward);
            detailB2b.setActFourthStartTime(fourth.getStartTime());
            detailB2b.setActFourthEndTime(fourth.getEndTime());
            subRebate = subRebate.add(reward);
        }
        //计算活动五奖励
        if (ObjectUtil.isNotNull(fifth)) {
            detailB2b.setActFifthName(fifth.getActivityName());
            BigDecimal reward;
            //如果是金额
            if (ObjectUtil.equal(fifth.getRewardType(), ReportRewardTypeEnum.CASH.getCode())) {
                BigDecimal amount = fifth.getRewardAmount();
                reward = amount.multiply(new BigDecimal(receiveQuantity));
            } else {
                //如果是百分比
                reward = outPrice.multiply(new BigDecimal(receiveQuantity)).multiply(fifth.getRewardPercentage().divide(BigDecimal.TEN).divide(BigDecimal.TEN));
            }
            detailB2b.setActFifthAmount(reward);
            detailB2b.setActFifthStartTime(fifth.getStartTime());
            detailB2b.setActFifthEndTime(fifth.getEndTime());
            subRebate = subRebate.add(reward);
        }
        subRebate = subRebate.add(detailB2b.getSalesAmount());
        detailB2b.setSubRebate(subRebate);
    }

    /**
     * 查询参数价格
     *
     * @param paramId
     * @param goodsId
     * @param priceParamNameList
     * @return
     */
    private ReportPriceParamNameDTO getPriceByParam(Long paramId, Long goodsId, List<ReportPriceParamNameDTO> priceParamNameList) {
        ReportPriceParamNameDTO supplyParam = priceParamNameList.stream().filter(param -> {
            if (param.getGoodsId().equals(goodsId) && param.getParamId().equals(paramId)) {
                return true;
            }
            return false;
        }).findAny().orElse(null);
        return supplyParam;
    }

    /**
     * 获取退货数量
     *
     * @param returnDetailBathDTOList
     * @param goodsDetailId
     * @param batchNo
     * @return
     */
    private Integer getReturnQuantity(List<ReturnDetailBathDTO> returnDetailBathDTOList, Long goodsDetailId, String batchNo) {
        if (CollUtil.isEmpty(returnDetailBathDTOList)) {
            return 0;
        }
        List<ReturnDetailBathDTO> returnDetailBathDTOS = returnDetailBathDTOList.stream().filter(r -> {
            if (r.getDetailId().equals(goodsDetailId) && batchNo.equals(r.getBatchNo())) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        if (CollUtil.isEmpty(returnDetailBathDTOS)) {
            return 0;
        }
        return returnDetailBathDTOS.stream().mapToInt(ReturnDetailBathDTO::getReturnQuantity).sum();
    }

    /**
     * 查询阶梯活动信息
     *
     * @param goodsId 商品id
     * @param receiveCount 收货数量
     * @param receiveTime 收货日期
     * @param ladderParList
     * @param ladderParList 订单来源枚举
     * @return
     */
    private ReportLadderGoodsInfoDTO queryLadderRewardInfo(Long goodsId, Integer receiveCount, Date receiveTime, List<ReportLadderGoodsInfoDTO> ladderParList, ReportParSubGoodsOrderSourceEnum sourceEnum) {
        if (ObjectUtil.isNull(sourceEnum)){
            return null;
        }
        if (receiveCount < 0) {
            receiveCount = receiveCount * -1;
        }
        List<ReportLadderGoodsInfoDTO> infoDTOList = ladderParList.stream().filter(e -> ObjectUtil.equal(goodsId, e.getYlGoodsId()) && DateUtil.compare(e.getStartTime(), receiveTime) <= 0 && DateUtil.compare(e.getEndTime(), receiveTime) >= 0).collect(Collectors.toList());
        //过滤订单来源
        infoDTOList=infoDTOList.stream().filter(e->ObjectUtil.equal(e.getOrderSource(),ReportParSubGoodsOrderSourceEnum.ALL.getCode())||ObjectUtil.equal(e.getOrderSource(),sourceEnum.getCode())).collect(Collectors.toList());
        if (CollUtil.isEmpty(infoDTOList)) {
            return null;
        }
        List<ReportLadderGoodsBO> tempRange = ListUtil.toList();

        Integer finalReceiveCount = receiveCount;
        infoDTOList.forEach(e -> {
            //收货数小于阶梯起步数忽略
            if (finalReceiveCount < e.getThresholdCount()) {
                return;
            }
            ReportLadderGoodsBO var = new ReportLadderGoodsBO();
            var.setId(e.getId());
            var.setDifference(finalReceiveCount - e.getThresholdCount());
            tempRange.add(var);
        });
        if (CollUtil.isEmpty(tempRange)) {
            return null;
        }
        ReportLadderGoodsBO goodsBO = tempRange.stream().sorted(Comparator.comparing(ReportLadderGoodsBO::getDifference)).collect(Collectors.toList()).stream().findFirst().get();
        Map<Long, ReportLadderGoodsInfoDTO> map = infoDTOList.stream().collect(Collectors.toMap(ReportLadderGoodsInfoDTO::getId, e -> e));
        return map.get(goodsBO.getId());
    }

    /**
     * 查询活动信息
     *
     * @param goodsId 商品id
     * @param receiveTime 收货日期
     * @param activityParList
     * @param sourceEnum
     * @return
     */
    private Map<Long, ReportParamSubGoodsDTO> queryActivityRewardInfo(Long goodsId, Date receiveTime, List<ReportParamSubGoodsDTO> activityParList,ReportParSubGoodsOrderSourceEnum sourceEnum) {
        Map<Long, ReportParamSubGoodsDTO> result = MapUtil.newHashMap();

        if (ObjectUtil.isNull(sourceEnum)){
            return result;
        }
        List<ReportParamSubGoodsDTO> infoDTOList = activityParList.stream().filter(e -> ObjectUtil.equal(goodsId, e.getYlGoodsId()) && DateUtil.compare(e.getStartTime(), receiveTime) <= 0 && DateUtil.compare(e.getEndTime(), receiveTime) >= 0).collect(Collectors.toList());
        infoDTOList=infoDTOList.stream().filter(e->ObjectUtil.equal(e.getOrderSource(),sourceEnum.getCode())||ObjectUtil.equal(e.getOrderSource(),ReportParSubGoodsOrderSourceEnum.ALL.getCode())).collect(Collectors.toList());
        if (CollUtil.isEmpty(infoDTOList)) {
            return result;
        }
        result = infoDTOList.stream().collect(Collectors.toMap(ReportParamSubGoodsDTO::getParamSubId, e -> e));
        return result;
    }

    /**
     * 校验报表计算周期
     *
     * @param date1
     * @param date2
     */
    public static void checkTimeSlot(Date date1, Date date2) {
        long between = DateUtil.between(date1, date2, DateUnit.DAY);
        if (between > 90) {
            throw new BusinessException(ReportErrorCode.REPORT_TIME_SLOT_INVALID);
        }
    }

    /**
     * 获取订单来源
     *
     * @param soSource
     * @return
     */
    public ReportParSubGoodsOrderSourceEnum  getFlowSourceByFlowOrderSource(String soSource){
        if (ObjectUtil.equal(soSource,"3")){
            return ReportParSubGoodsOrderSourceEnum.OWN_SELF;
        }

        try {
            ArrayList<Integer> list = ListUtil.toList(otherOrderSource);
            if (list.contains(Integer.valueOf(soSource))){
                return ReportParSubGoodsOrderSourceEnum.OTHER;
            }
        } catch (NumberFormatException e) {
            log.error("流向订单来源不正确，来源={}",soSource);
            return null;
        }
        return null;
    }


}
