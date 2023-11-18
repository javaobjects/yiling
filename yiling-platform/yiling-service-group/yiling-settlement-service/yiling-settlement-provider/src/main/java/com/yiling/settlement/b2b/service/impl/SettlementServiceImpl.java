package com.yiling.settlement.b2b.service.impl;

import java.math.BigDecimal;
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
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.api.NoApi;
import com.yiling.order.order.enums.NoEnum;
import com.yiling.order.order.enums.OrderStatusEnum;
import com.yiling.order.order.enums.PaymentMethodEnum;
import com.yiling.settlement.b2b.bo.B2BSettlementMetadataBO;
import com.yiling.settlement.b2b.dao.SettlementMapper;
import com.yiling.settlement.b2b.dto.SettlementAmountInfoDTO;
import com.yiling.settlement.b2b.dto.SettlementDTO;
import com.yiling.settlement.b2b.dto.SettlementDetailDTO;
import com.yiling.settlement.b2b.dto.SettlementOrderSyncDTO;
import com.yiling.settlement.b2b.dto.request.QuerySettOrderDataPageListRequest;
import com.yiling.settlement.b2b.dto.request.QuerySettlementPageListRequest;
import com.yiling.settlement.b2b.dto.request.SubmitSalePaymentRequest;
import com.yiling.settlement.b2b.dto.request.UpdateOrderSyncSettStatusRequest;
import com.yiling.settlement.b2b.dto.request.UpdatePaymentStatusRequest;
import com.yiling.settlement.b2b.dto.request.UpdateSettlementStatusRequest;
import com.yiling.settlement.b2b.entity.SettlementDO;
import com.yiling.settlement.b2b.entity.SettlementDetailDO;
import com.yiling.settlement.b2b.entity.SettlementOrderDO;
import com.yiling.settlement.b2b.entity.SettlementOrderSyncDO;
import com.yiling.settlement.b2b.enums.B2BOrderSettStatusEnum;
import com.yiling.settlement.b2b.enums.OrderSyncDataStatusEnum;
import com.yiling.settlement.b2b.enums.OrderSyncDefaultStatusEnum;
import com.yiling.settlement.b2b.enums.OrderSyncStatusEnum;
import com.yiling.settlement.b2b.enums.SettGenerateStatusEnum;
import com.yiling.settlement.b2b.enums.SettOrderTypenum;
import com.yiling.settlement.b2b.enums.SettlementErrorCode;
import com.yiling.settlement.b2b.enums.SettlementOperationTypeEnum;
import com.yiling.settlement.b2b.enums.SettlementStatusEnum;
import com.yiling.settlement.b2b.enums.SettlementTradeStatusEnum;
import com.yiling.settlement.b2b.enums.SettlementTypeEnum;
import com.yiling.settlement.b2b.service.ReceiptAccountService;
import com.yiling.settlement.b2b.service.SettlementDetailService;
import com.yiling.settlement.b2b.service.SettlementOrderCancelService;
import com.yiling.settlement.b2b.service.SettlementOrderService;
import com.yiling.settlement.b2b.service.SettlementOrderSyncService;
import com.yiling.settlement.b2b.service.SettlementService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * b2b商家结算单表 服务实现类
 * </p>
 *
 * @author dexi.yao
 * @date 2021-10-19
 */
@Service
@Slf4j
public class SettlementServiceImpl extends BaseServiceImpl<SettlementMapper, SettlementDO> implements SettlementService {

    @DubboReference
    NoApi noApi;

    @Autowired
    SettlementDetailService settlementDetailService;
    @Autowired
    SettlementOrderService settlementOrderService;
    @Autowired
    ReceiptAccountService receiptAccountService;
    @Autowired
    SettlementOrderSyncService settlementOrderSyncService;
    @Autowired
    SettlementOrderCancelService settlementOrderCancelService;


    //    @Override
    //    @GlobalTransactional
    //    public void generateSettlement(Long sellerEid) {
    //        //待生成结算单的订单map key=卖家id value=订单列表
    //        Map<Long, List<SettlementOrderSyncDO>> generateSettMap = MapUtil.newHashMap();
    //        //待更新结算单状态的订单request列表
    //        List<SettlementOrderSyncDO> updateOrderList = ListUtil.toList();
    //
    //        //分页查询符合结算条件的订单
    //        int current = 1;
    //        Page<SettlementOrderSyncDTO> orderPage;
    //        QuerySettOrderDataPageListRequest queryRequest = new QuerySettOrderDataPageListRequest();
    //        //分页查询订单列表
    //        do {
    //            DateTime currentDate = DateUtil.date();
    //            //查询今天0点之前签收的订单
    //            queryRequest.setEndReceiveTime(currentDate);
    //            queryRequest.setSellerEid(sellerEid);
    //            //如果传了eid，则证明是手动生成结算单
    //            if (ObjectUtil.isNotNull(sellerEid)) {
    //                queryRequest.setEndReceiveTime(currentDate);
    //            } else {
    //                queryRequest.setEndReceiveTime(DateUtil.beginOfDay(currentDate));
    //            }
    //            queryRequest.setCurrent(current);
    //            queryRequest.setSize(50);
    //            queryRequest.setGenerateStatus(SettGenerateStatusEnum.UN_GENERATE.getCode());
    //            queryRequest.setDataStatus(OrderSyncDataStatusEnum.AUTHORIZED.getCode());
    //            queryRequest.setSyncStatus(OrderSyncStatusEnum.SUCCESS.getCode());
    //            //分页查询符合结算条件的订单
    //            orderPage = settlementOrderSyncService.querySettOrderSyncPageList(queryRequest);
    //            if (CollUtil.isEmpty(orderPage.getRecords())) {
    //                break;
    //            }
    //            List<SettlementOrderSyncDTO> records = orderPage.getRecords();
    //            if (CollUtil.isNotEmpty(records)){
    //                    for (SettlementOrderSyncDTO request:records){
    //                        //将订单request插入待更新结算状态的列表
    //                        updateOrderList.add(PojoUtils.map(request,SettlementOrderSyncDO.class));
    //                        //加入generateSettMap
    //                        List<SettlementOrderSyncDO> orderList = generateSettMap.get(request.getSellerEid());
    //                        if (CollUtil.isEmpty(orderList)) {
    //                            orderList = ListUtil.toList(PojoUtils.map(request, SettlementOrderSyncDO.class));
    //                            generateSettMap.put(request.getSellerEid(), orderList);
    //                        } else {
    //                            orderList.add(PojoUtils.map(request, SettlementOrderSyncDO.class));
    //                        }
    //                    }
    //            }
    //            current = current + 1;
    //        } while (orderPage != null && CollUtil.isNotEmpty(orderPage.getRecords()));
    //        //生成结算单
    //        //如果订单为空则就此结束
    //        if (MapUtil.isEmpty(generateSettMap)) {
    //            return;
    //        }
    //        //生成结算单
    //        B2BSettlementMetadataBO metadataBO = generateSettlementByOrder(generateSettMap);
    //        //回写结算单订单数据同步表结算单相关状态
    //        if (ObjectUtil.isNotNull(metadataBO)) {
    //            updateOrderSyncGenerateStatus(metadataBO, updateOrderList);
    //        }
    //    }

    @Override
    @GlobalTransactional
    public void generateSettlement(Long sellerEid) {
        //待生成结算单的订单map key=卖家id value=订单列表
        Map<Long, List<SettlementOrderSyncDO>> generateSettMap = MapUtil.newHashMap();
        //待更新结算单状态的订单request列表
        List<SettlementOrderSyncDO> updateOrderList = ListUtil.toList();

        //查询用于生成货款&促销&预售违约金结算单的订单
        querySaleOrGoodsOrder(sellerEid, generateSettMap, updateOrderList);
        //查询用于生成预售违约金结算单的订单
        queryPresaleOrder(sellerEid, generateSettMap, updateOrderList);

        //生成结算单
        //如果订单为空则就此结束
        if (MapUtil.isEmpty(generateSettMap)) {
            return;
        }
        //生成结算单
        B2BSettlementMetadataBO metadataBO = generateSettlementByOrder(generateSettMap);
        //回写结算单订单数据同步表结算单相关状态
        if (ObjectUtil.isNotNull(metadataBO)) {
            updateOrderSyncGenerateStatus(metadataBO, updateOrderList);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public B2BSettlementMetadataBO generateSettlementByOrder(Map<Long, List<SettlementOrderSyncDO>> request) {

        if (CollUtil.isEmpty(request)) {
            return null;
        }
        //生成结算单
        List<SettlementDO> settlementList = getSettlement(request);
        if (CollUtil.isEmpty(settlementList)) {
            return null;
        }

        Boolean isSuccess = saveBatch(settlementList);
        if (!isSuccess) {
            log.error("保存结算单失败，数据：", JSON.toJSON(settlementList));
            throw new BusinessException(SettlementErrorCode.SAVE_SETTLEMENT_FAIL);
        }
        //要保存的结算单明细
        List<SettlementDetailDO> detailList = ListUtil.toList();
        //保存结算单明细
        for (SettlementDO settlement : settlementList) {
            Long settlementId = settlement.getId();
            List<SettlementDetailDO> details = settlement.getSettlementDetailDOS();
            List<SettlementDetailDO> detailDOS = PojoUtils.map(details, SettlementDetailDO.class);
            detailDOS.forEach(e -> e.setSettlementId(settlementId));
            detailList.addAll(detailDOS);
        }
        isSuccess = settlementDetailService.saveBatch(detailList);
        if (!isSuccess) {
            log.error("保存结算单明细失败，数据：", JSON.toJSON(detailList));
            throw new BusinessException(SettlementErrorCode.SAVE_SETTLEMENT_DETAIL_FAIL);
        }
        //保存订单对账单
        B2BSettlementMetadataBO b2BSettlementMetadataBO = generateSettlementOrder(request, settlementList);
        return b2BSettlementMetadataBO;
    }

    @Override
    public SettlementAmountInfoDTO querySettlementAmountInfo(Long eid) {
        Date today = DateUtil.date();
        Date yesterday = DateUtil.yesterday();
        return this.baseMapper.querySettlementAmountInfo(DateUtil.beginOfDay(today), DateUtil.beginOfDay(yesterday), eid);
    }

    @Override
    public Page<SettlementDTO> querySettlementPageList(QuerySettlementPageListRequest request) {
        LambdaQueryWrapper<SettlementDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ObjectUtil.isNotNull(request.getEid()), SettlementDO::getEid, request.getEid());
        wrapper.eq(StrUtil.isNotBlank(request.getCode()), SettlementDO::getCode, request.getCode());
        if (ObjectUtil.isNotNull(request.getMinTime())) {
            wrapper.ge(SettlementDO::getCreateTime, DateUtil.beginOfDay(request.getMinTime()));
        }
        if (ObjectUtil.isNotNull(request.getMaxTime())) {
            wrapper.le(SettlementDO::getCreateTime, DateUtil.endOfDay(request.getMaxTime()));
        }
        wrapper.in(CollUtil.isNotEmpty(request.getEidList()), SettlementDO::getEid, request.getEidList());
        wrapper.eq(ObjectUtil.isNotNull(request.getType()) && !ObjectUtil.equal(request.getType(), 0), SettlementDO::getType, request.getType());
        wrapper.eq(ObjectUtil.isNotNull(request.getStatus()) && !ObjectUtil.equal(request.getStatus(), 0), SettlementDO::getStatus, request.getStatus());
        wrapper.orderByDesc(SettlementDO::getId, SettlementDO::getCreateTime);
        Page<SettlementDO> page = page(request.getPage(), wrapper);
        return PojoUtils.map(page, SettlementDTO.class);
    }

    @Override
    @GlobalTransactional
    public Boolean updatePaymentStatus(List<UpdatePaymentStatusRequest> list) {
//        List<UpdatePaymentStatusRequest> updatePaymentStatusRequestList = list.stream().filter(e -> ObjectUtil.notEqual(e.getPaymentStatus(), SettlementTradeStatusEnum.WAIT_PAY.getCode()) && ObjectUtil.notEqual(e.getPaymentStatus(), SettlementTradeStatusEnum.BANK_ING.getCode())).collect(Collectors.toList());
        log.info("settlement...updatePaymentStatus参数：{}", list);
//        //企业打款状态map 排除待付款和银行处理中的状态
//        Map<String, UpdatePaymentStatusRequest> payMap = updatePaymentStatusRequestList.stream().collect(Collectors.toMap(UpdatePaymentStatusRequest::getPayNo, e -> e));
//
//        //查询结算单并过滤状态为银行处理的结算单
//        List<SettlementDTO> settlementList = querySettlementListByPayNo(updatePaymentStatusRequestList.stream().map(UpdatePaymentStatusRequest::getPayNo).collect(Collectors.toList())).stream().filter(e -> ObjectUtil.equal(SettlementStatusEnum.BANK_COPE.getCode(), e.getStatus())).collect(Collectors.toList());
        //企业打款状态map 排除待付款和银行处理中的状态
        Map<String, UpdatePaymentStatusRequest> payMap = list.stream().collect(Collectors.toMap(UpdatePaymentStatusRequest::getPayNo, e -> e));

        //查询结算单并过滤状态为银行处理的结算单
        List<SettlementDTO> settlementList = querySettlementListByPayNo(list.stream().map(UpdatePaymentStatusRequest::getPayNo).collect(Collectors.toList())).stream().collect(Collectors.toList());
        if (CollUtil.isEmpty(settlementList)) {
            return Boolean.TRUE;
        }
        //打款成功的结算单
        List<SettlementDTO> successList = ListUtil.toList();
        //打款失败的结算单
        List<SettlementDTO> failList = ListUtil.toList();

        settlementList.forEach(settlementDTO -> {
            UpdatePaymentStatusRequest paymentStatus = payMap.get(settlementDTO.getPayNo());
            if (ObjectUtil.equal(SettlementTradeStatusEnum.BANK_ING.getCode(), paymentStatus.getPaymentStatus())) {
                //易宝下单成功补全三方单号
                settlementDTO.setThirdPayNo(paymentStatus.getThirdPayNo());
            } else if (ObjectUtil.equal(SettlementTradeStatusEnum.CLOSE.getCode(), paymentStatus.getPaymentStatus()) && StrUtil.isEmpty(paymentStatus.getThirdPayNo())) {
                //易宝下单失败
                settlementDTO.setStatus(SettlementStatusEnum.FAIL.getCode());
                settlementDTO.setErrMsg(paymentStatus.getErrMsg());
                failList.add(settlementDTO);
            } else if (ObjectUtil.equal(SettlementTradeStatusEnum.SUCCESS.getCode(), paymentStatus.getPaymentStatus())) {
                //打款成功
                settlementDTO.setStatus(SettlementStatusEnum.SETTLE.getCode());
                successList.add(settlementDTO);
            } else if (ObjectUtil.equal(SettlementTradeStatusEnum.FALIUE.getCode(), paymentStatus.getPaymentStatus())) {
                //打款失败
                settlementDTO.setStatus(SettlementStatusEnum.FAIL.getCode());
                settlementDTO.setErrMsg(paymentStatus.getErrMsg());
                failList.add(settlementDTO);
            } else if (ObjectUtil.equal(SettlementTradeStatusEnum.CLOSE.getCode(), paymentStatus.getPaymentStatus()) && StrUtil.isNotEmpty(paymentStatus.getThirdPayNo())) {
                //打款取消
                settlementDTO.setStatus(SettlementStatusEnum.FAIL.getCode());
                settlementDTO.setErrMsg(SettlementTradeStatusEnum.CLOSE.getName());
                failList.add(settlementDTO);
            }
        });
        //更新结算单状态
        Boolean isSuccess = updateSettlementById(PojoUtils.map(settlementList, SettlementDO.class));
        if (!isSuccess) {
            log.error("更新结算单打款状态失败");
            throw new BusinessException(SettlementErrorCode.UPDATE_SETTLEMENT_STATUS_FAIL);
        }
        //更新结算订单状态
        isSuccess = settlementOrderService.updateSettlementStatus(PojoUtils.map(successList, UpdateSettlementStatusRequest.class), PojoUtils.map(failList, UpdateSettlementStatusRequest.class), SettlementOperationTypeEnum.CALL);
        if (!isSuccess) {
            log.error("更新结算单明细的订单的结算状态失败");
            throw new BusinessException(SettlementErrorCode.UPDATE_ORDER_SETTLEMENT_DETAIL_STATUS_FAIL);
        }
        if (CollUtil.isNotEmpty(successList)) {
            //更新订单状态
            return updateOrderSyncSettStatus(successList);
        }

        return Boolean.TRUE;
    }

    @Override
    public List<SettlementDTO> querySettlementListByPayNo(List<String> payNo) {
        if (CollUtil.isEmpty(payNo)) {
            return ListUtil.toList();
        }
        LambdaQueryWrapper<SettlementDO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(SettlementDO::getPayNo, payNo);
        List<SettlementDO> list = list(wrapper);

        return PojoUtils.map(list, SettlementDTO.class);
    }

    @Override
    public Boolean updateSettlementById(List<SettlementDO> settlementDOList) {
        if (CollUtil.isEmpty(settlementDOList)) {
            return Boolean.TRUE;
        }
        return updateBatchById(settlementDOList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean submitSalePayment(SubmitSalePaymentRequest request) {
        List<SettlementDO> settlementDOS = listByIds(request.getSettlementIds());
        List<UpdateSettlementStatusRequest> successSettOrderList = ListUtil.toList();

        settlementDOS.forEach(settlementDO -> {
            settlementDO.setStatus(SettlementStatusEnum.SETTLE.getCode());
            settlementDO.setSettlementRemark(request.getSettlementRemark());
            settlementDO.setOpUserId(request.getOpUserId());
            settlementDO.setSettlementTime(new Date());
            UpdateSettlementStatusRequest successSett = PojoUtils.map(settlementDO, UpdateSettlementStatusRequest.class);
            successSettOrderList.add(successSett);
        });
        //更新结算单
        Boolean isSuccess = updateBatchById(settlementDOS);
        if (!isSuccess) {
            log.error("更新促销结算单打款失败，参数={}", settlementDOS);
            throw new BusinessException(SettlementErrorCode.UPDATE_SETTLEMENT_STATUS_FAIL);
        }
        return settlementOrderService.updateSettlementStatus(successSettOrderList, CollUtil.toList(), SettlementOperationTypeEnum.CALL);
    }


    /**
     * 生成结算单
     *
     * @param request
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public List<SettlementDO> getSettlement(Map<Long, List<SettlementOrderSyncDO>> request) {
        //生成的结算单列表
        List<SettlementDO> settlementList = ListUtil.toList();

        request.forEach((sellerEid, orderList) -> {
            //货款结算单
            SettlementDO goodsSettlement = new SettlementDO();
            goodsSettlement.setEid(sellerEid);
            goodsSettlement.setCode(noApi.gen(NoEnum.B2B_SETTLEMENT_GOODS_NO));
            goodsSettlement.setAmount(BigDecimal.ZERO);
            goodsSettlement.setGoodsAmount(BigDecimal.ZERO);
            goodsSettlement.setRefundGoodsAmount(BigDecimal.ZERO);
            goodsSettlement.setOrderCount(0);
            goodsSettlement.setType(SettlementTypeEnum.GOODS.getCode());
            goodsSettlement.setStatus(SettlementStatusEnum.UN_SETTLE.getCode());
            goodsSettlement.setSettlementDetailDOS(ListUtil.toList());
            //促销结算单
            SettlementDO saleSettlement = new SettlementDO();
            PojoUtils.map(goodsSettlement, saleSettlement);
            saleSettlement.setCode(noApi.gen(NoEnum.B2B_SETTLEMENT_SALE_NO));
            saleSettlement.setType(SettlementTypeEnum.SALE.getCode());
            saleSettlement.setDiscountAmount(BigDecimal.ZERO);
            saleSettlement.setRefundDiscountAmount(BigDecimal.ZERO);
            saleSettlement.setCouponAmount(BigDecimal.ZERO);
            saleSettlement.setRefundCouponAmount(BigDecimal.ZERO);
            saleSettlement.setPromotionAmount(BigDecimal.ZERO);
            saleSettlement.setRefundPromotionAmount(BigDecimal.ZERO);
            saleSettlement.setGiftAmount(BigDecimal.ZERO);
            saleSettlement.setRefundGiftAmount(BigDecimal.ZERO);
            saleSettlement.setComPacAmount(BigDecimal.ZERO);
            saleSettlement.setRefundComPacAmount(BigDecimal.ZERO);
            saleSettlement.setPresaleDiscountAmount(BigDecimal.ZERO);
            saleSettlement.setRefundPreAmount(BigDecimal.ZERO);
            saleSettlement.setPayDiscountAmount(BigDecimal.ZERO);
            saleSettlement.setRefundPayAmount(BigDecimal.ZERO);
            saleSettlement.setSettlementDetailDOS(ListUtil.toList());

            //预售违约金结算单
            SettlementDO dpSettlement = new SettlementDO();
            dpSettlement.setEid(sellerEid);
            dpSettlement.setCode(noApi.gen(NoEnum.B2B_SETTLEMENT_PRESALE_DEFAULT_NO));
            dpSettlement.setAmount(BigDecimal.ZERO);
            dpSettlement.setPresaleDefaultAmount(BigDecimal.ZERO);
            dpSettlement.setOrderCount(0);
            dpSettlement.setType(SettlementTypeEnum.PRESALE_DEFAULT.getCode());
            dpSettlement.setStatus(SettlementStatusEnum.UN_SETTLE.getCode());
            dpSettlement.setSettlementDetailDOS(ListUtil.toList());

            orderList.forEach(order -> {
                //如果订单用途是货款&促销结算单
                if (ObjectUtil.equal(order.getDefaultStatus(), OrderSyncDefaultStatusEnum.ORDER_PERFORMANCE.getCode())) {
                    //计算该笔订单的货款应结算金额
                    setGoodsSett(order, goodsSettlement);
                    goodsSettlement.setOrderCount(goodsSettlement.getSettlementDetailDOS().size());
                    //计算该笔订单的优惠券应结算金额
                    setCouponSett(order, saleSettlement);
                    //计算该笔订单的秒杀&特价应结算金额
                    setPromotionSett(order, saleSettlement);
                    //计算满赠应结算金额
                    setGiftSett(order, saleSettlement);
                    //计算组合包应结算金额
                    setComPacSett(order, saleSettlement);
                    //计算预售应结算金额
                    setPreSaleSett(order, saleSettlement);
                    //计算支付促销应结算金额
                    setPaySaleSett(order, saleSettlement);
                    saleSettlement.setOrderCount(saleSettlement.getSettlementDetailDOS().size());
                }else {
                    //计算该笔订单的预售违约金
                    setDPSett(order, dpSettlement);
                    dpSettlement.setOrderCount(dpSettlement.getSettlementDetailDOS().size());
                }

            });
            //            //如果结算金额为0则状态置为已结算
            //            if (goodsSettlement.getAmount().compareTo(BigDecimal.ZERO) == 0) {
            //                goodsSettlement.setStatus(SettlementStatusEnum.SETTLE.getCode());
            //                goodsSettlement.setSettlementTime(new Date());
            //            }
            //            //如果结算金额为0则状态置为已结算
            //            if (saleSettlement.getAmount().compareTo(BigDecimal.ZERO) == 0) {
            //                saleSettlement.setStatus(SettlementStatusEnum.SETTLE.getCode());
            //                saleSettlement.setSettlementTime(new Date());
            //            }

            if (CollUtil.isNotEmpty(goodsSettlement.getSettlementDetailDOS())) {
                settlementList.add(goodsSettlement);
            }
            if (CollUtil.isNotEmpty(saleSettlement.getSettlementDetailDOS())) {
                settlementList.add(saleSettlement);
            }
            if (CollUtil.isNotEmpty(dpSettlement.getSettlementDetailDOS())) {
                settlementList.add(dpSettlement);
            }
        });
        return settlementList;
    }

    /**
     * 生成订单对账单
     *
     * @param request
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public B2BSettlementMetadataBO generateSettlementOrder(Map<Long, List<SettlementOrderSyncDO>> request, List<SettlementDO> settlementList) {
        List<SettlementOrderDO> settlementOrderList = ListUtil.toList();
        //货款结算单明细，ps:因为是根据结算单类型分组的  所以同一笔订单在此列表中只会有一条记录
        List<SettlementDetailDO> goodsSettDetailDOS = ListUtil.toList();
        //促销结算单明细，ps:因为是根据结算单类型分组的  所以同一笔订单在此列表中只会有一条记录
        List<SettlementDetailDO> saleSettDetailDOS = ListUtil.toList();
        //预售违约金结算单明细，ps:因为是根据结算单类型分组的  所以同一笔订单在此列表中只会有一条记录
        List<SettlementDetailDO> pdSettDetailDOS = ListUtil.toList();
        Map<Long, SettlementDO> settMap = settlementList.stream().collect(Collectors.toMap(SettlementDO::getId, e -> e));

        //根据结算单类型分组
        Map<Integer, List<SettlementDO>> settlementMap = settlementList.stream().collect(Collectors.groupingBy(SettlementDO::getType));
        List<SettlementDO> goodsSett = settlementMap.get(SettlementTypeEnum.GOODS.getCode());
        if (CollUtil.isNotEmpty(goodsSett)) {
            goodsSett.forEach(e -> {
                goodsSettDetailDOS.addAll(e.getSettlementDetailDOS());
            });
        }
        List<SettlementDO> saleSett = settlementMap.get(SettlementTypeEnum.SALE.getCode());
        if (CollUtil.isNotEmpty(saleSett)) {
            saleSett.forEach(e -> {
                saleSettDetailDOS.addAll(e.getSettlementDetailDOS());
            });
        }
        List<SettlementDO> dpSett = settlementMap.get(SettlementTypeEnum.PRESALE_DEFAULT.getCode());
        if (CollUtil.isNotEmpty(dpSett)) {
            dpSett.forEach(e -> {
                pdSettDetailDOS.addAll(e.getSettlementDetailDOS());
            });
        }
        //订单-结算单明细map
        Map<Long, SettlementDetailDO> goodsSettMap = goodsSettDetailDOS.stream().collect(Collectors.toMap(SettlementDetailDO::getOrderId, e -> e));
        Map<Long, SettlementDetailDO> saleSettMap = saleSettDetailDOS.stream().collect(Collectors.toMap(SettlementDetailDO::getOrderId, e -> e));
        Map<Long, SettlementDetailDO> pdSettMap = pdSettDetailDOS.stream().collect(Collectors.toMap(SettlementDetailDO::getOrderId, e -> e));

        request.forEach((sellerEid, orderList) -> {
            //设置相应属性
            orderList.forEach(order -> {

                //如果订单履约
                if (ObjectUtil.equal(order.getDefaultStatus(), OrderSyncDefaultStatusEnum.ORDER_PERFORMANCE.getCode())) {
                    SettlementOrderDO settlementOrderDO = setGoodsOrSaleSettOrder(order, settMap, goodsSettMap, saleSettMap);
                    if (ObjectUtil.isNotNull(settlementOrderDO)) {
                        settlementOrderList.add(settlementOrderDO);
                    }
                }
                //如果订单违约
                if (ObjectUtil.equal(order.getDefaultStatus(), OrderSyncDefaultStatusEnum.ORDER_DEFAULT.getCode())) {
                    SettlementOrderDO settlementOrderDO = setPresaleDefaultSettOrder(order, settMap, pdSettMap);
                    if (ObjectUtil.isNotNull(settlementOrderDO)) {
                        settlementOrderList.add(settlementOrderDO);
                    }
                }

            });
        });

        boolean isSuccess = settlementOrderService.saveBatch(settlementOrderList);
        if (!isSuccess) {
            log.error("保存结算单对账单失败，数据：", JSON.toJSON(request));
            throw new BusinessException(SettlementErrorCode.SAVE_ORDER_SETTLEMENT_FAIL);
        }
        B2BSettlementMetadataBO result = new B2BSettlementMetadataBO();
        result.setSettlementList(settlementList);
        result.setSaleSettDetailList(saleSettDetailDOS);
        result.setGoodsSettDetailList(goodsSettDetailDOS);
        result.setPdSettDetailList(pdSettDetailDOS);
        return result;
    }

    /**
     * 回写结算单订单数据同步表结算单状态
     *
     * @param metadataBO
     * @param orderSynList
     */
    public void updateOrderSyncGenerateStatus(B2BSettlementMetadataBO metadataBO, List<SettlementOrderSyncDO> orderSynList) {
        //结算单map
        Map<Long, SettlementDO> settMap = metadataBO.getSettlementList().stream().collect(Collectors.toMap(SettlementDO::getId, e -> e));
        //订单-结算单明细map
        Map<Long, SettlementDetailDO> goodsSettMap = metadataBO.getGoodsSettDetailList().stream().collect(Collectors.toMap(SettlementDetailDO::getOrderId, e -> e));
        Map<Long, SettlementDetailDO> saleSettMap = metadataBO.getSaleSettDetailList().stream().collect(Collectors.toMap(SettlementDetailDO::getOrderId, e -> e));
        Map<Long, SettlementDetailDO> pdSettMap = metadataBO.getPdSettDetailList().stream().collect(Collectors.toMap(SettlementDetailDO::getOrderId, e -> e));

        //更新订单状态为已生成结算单
        List<UpdateOrderSyncSettStatusRequest.OrderSettlementStatusRequest> orderList = ListUtil.toList();
        orderSynList.forEach(orderSyncDO -> {
            UpdateOrderSyncSettStatusRequest.OrderSettlementStatusRequest var = new UpdateOrderSyncSettStatusRequest.OrderSettlementStatusRequest();
            var.setId(orderSyncDO.getId());
            var.setGenerateStatus(SettGenerateStatusEnum.GENERATED.getCode());
            if (ObjectUtil.equal(orderSyncDO.getDefaultStatus(), OrderSyncDefaultStatusEnum.ORDER_PERFORMANCE.getCode())) {
                SettlementDetailDO saleSettDetailDO = saleSettMap.get(orderSyncDO.getOrderId());
                if (ObjectUtil.isNotNull(saleSettDetailDO)) {
                    SettlementDO settlementDO = settMap.get(saleSettDetailDO.getSettlementId());
                    if (ObjectUtil.equal(settlementDO.getStatus(), SettlementStatusEnum.SETTLE.getCode())) {
                        var.setSaleSettlementStatus(B2BOrderSettStatusEnum.SETTLEMENT.getCode());
                    } else if (ObjectUtil.equal(settlementDO.getStatus(), SettlementStatusEnum.UN_SETTLE.getCode())) {
                        var.setSaleSettlementStatus(B2BOrderSettStatusEnum.UN_SETTLEMENT.getCode());
                    }
                }
                SettlementDetailDO goodsSettDetailDO = goodsSettMap.get(orderSyncDO.getOrderId());
                if (ObjectUtil.isNotNull(goodsSettDetailDO)) {
                    SettlementDO settlementDO = settMap.get(goodsSettDetailDO.getSettlementId());
                    if (ObjectUtil.equal(settlementDO.getStatus(), SettlementStatusEnum.SETTLE.getCode())) {
                        var.setGoodsSettlementStatus(B2BOrderSettStatusEnum.SETTLEMENT.getCode());
                    } else if (ObjectUtil.equal(settlementDO.getStatus(), SettlementStatusEnum.UN_SETTLE.getCode())) {
                        var.setGoodsSettlementStatus(B2BOrderSettStatusEnum.UN_SETTLEMENT.getCode());
                    }
                }
                orderList.add(var);
            }
            if (ObjectUtil.equal(orderSyncDO.getDefaultStatus(), OrderSyncDefaultStatusEnum.ORDER_DEFAULT.getCode())) {
                SettlementDetailDO pdSettDetailDO = pdSettMap.get(orderSyncDO.getOrderId());
                if (ObjectUtil.isNotNull(pdSettDetailDO)) {
                    SettlementDO settlementDO = settMap.get(pdSettDetailDO.getSettlementId());
                    if (ObjectUtil.equal(settlementDO.getStatus(), SettlementStatusEnum.SETTLE.getCode())) {
                        var.setPresaleSettlementStatus(B2BOrderSettStatusEnum.SETTLEMENT.getCode());
                    } else if (ObjectUtil.equal(settlementDO.getStatus(), SettlementStatusEnum.UN_SETTLE.getCode())) {
                        var.setPresaleSettlementStatus(B2BOrderSettStatusEnum.UN_SETTLEMENT.getCode());
                    }
                }
                orderList.add(var);
            }
        });
        UpdateOrderSyncSettStatusRequest updateOrderSettlementStatusRequest = new UpdateOrderSyncSettStatusRequest();
        updateOrderSettlementStatusRequest.setSettStatusList(orderList);
        Boolean isUpdate = settlementOrderSyncService.updateOrderSyncSettStatus(updateOrderSettlementStatusRequest);
        if (!isUpdate) {
            log.error("更新结算单的订单同步表状态为已生成结算单时失败,{}", JSON.toJSON(updateOrderSettlementStatusRequest));
            throw new BusinessException(SettlementErrorCode.UPDATE_ORDER_SETTLEMENT_GENERATE_FAIL);
        }
    }

    /**
     * 更新结算单下的订单数据同步表的结算状态
     *
     * @param settlementList
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateOrderSyncSettStatus(List<SettlementDTO> settlementList) {
        //结算单map
        Map<Long, SettlementDTO> settlementDTOMap = settlementList.stream().collect(Collectors.toMap(SettlementDTO::getId, e -> e));

        //查询结算单下的订单
        List<SettlementDetailDTO> settlementDetailDTOS = settlementDetailService.querySettlementDetailBySettlementId(settlementList.stream().map(SettlementDTO::getId).collect(Collectors.toList()));

        //更新订单的结算状态
        List<UpdateOrderSyncSettStatusRequest.OrderSettlementStatusRequest> orderList = ListUtil.toList();
        settlementDetailDTOS.forEach(settlementDetailDTO -> {
            UpdateOrderSyncSettStatusRequest.OrderSettlementStatusRequest var = new UpdateOrderSyncSettStatusRequest.OrderSettlementStatusRequest();
            var.setId(settlementDetailDTO.getOrderSyncId());
            //根据结算单id查询结算单
            SettlementDTO settlementDTO = settlementDTOMap.get(settlementDetailDTO.getSettlementId());
            if (ObjectUtil.equal(SettlementTypeEnum.GOODS.getCode(), settlementDTO.getType())) {
                var.setGoodsSettlementStatus(B2BOrderSettStatusEnum.SETTLEMENT.getCode());
            } else {
                var.setSaleSettlementStatus(B2BOrderSettStatusEnum.SETTLEMENT.getCode());
            }
            orderList.add(var);
        });
        UpdateOrderSyncSettStatusRequest updateOrderSettlementStatusRequest = new UpdateOrderSyncSettStatusRequest();
        updateOrderSettlementStatusRequest.setSettStatusList(orderList);
        Boolean isUpdate = settlementOrderSyncService.updateOrderSyncSettStatus(updateOrderSettlementStatusRequest);
        if (!isUpdate) {
            log.error("更新结算单的订单同步数据表的结算状态失败,{}", JSON.toJSON(updateOrderSettlementStatusRequest));
            throw new BusinessException(SettlementErrorCode.UPDATE_ORDER_SETTLEMENT_STATUS_FAIL);
        }
        return Boolean.TRUE;
    }

    /**
     * 查询用于生成货款&促销&违约结算单的订单
     *
     * @param sellerEid
     * @param orderSyncMap
     * @param updateOrderList
     */
    public void querySaleOrGoodsOrder(Long sellerEid, Map<Long, List<SettlementOrderSyncDO>> orderSyncMap, List<SettlementOrderSyncDO> updateOrderList) {
        //分页查询符合结算条件的订单
        int current = 1;
        Page<SettlementOrderSyncDTO> orderPage;
        QuerySettOrderDataPageListRequest queryRequest = new QuerySettOrderDataPageListRequest();
        //分页查询订单列表
        do {
            DateTime currentDate = DateUtil.date();
            //查询今天0点之前签收的订单
            //            queryRequest.setEndReceiveTime(currentDate);
            queryRequest.setSellerEid(sellerEid);
            //如果传了eid，则证明是手动生成结算单
            if (ObjectUtil.isNotNull(sellerEid)) {
                queryRequest.setEndReceiveTime(currentDate);
            } else {
                queryRequest.setEndReceiveTime(DateUtil.beginOfDay(currentDate));
            }
            queryRequest.setOrderStatusList(ListUtil.toList(OrderStatusEnum.RECEIVED.getCode(), OrderStatusEnum.FINISHED.getCode()));
            queryRequest.setDefaultStatus(OrderSyncDefaultStatusEnum.ORDER_PERFORMANCE.getCode());
            queryRequest.setCurrent(current);
            queryRequest.setSize(50);
            queryRequest.setGenerateStatus(SettGenerateStatusEnum.UN_GENERATE.getCode());
            queryRequest.setDataStatus(OrderSyncDataStatusEnum.AUTHORIZED.getCode());
            queryRequest.setSyncStatus(OrderSyncStatusEnum.SUCCESS.getCode());
            //分页查询符合结算条件的订单
            orderPage = settlementOrderSyncService.querySettOrderSyncPageList(queryRequest);
            if (CollUtil.isEmpty(orderPage.getRecords())) {
                break;
            }
            List<SettlementOrderSyncDTO> records = orderPage.getRecords();
            if (CollUtil.isNotEmpty(records)) {
                for (SettlementOrderSyncDTO request : records) {
                    //将订单request插入待更新结算状态的列表
                    updateOrderList.add(PojoUtils.map(request, SettlementOrderSyncDO.class));
                    //加入generateSettMap
                    List<SettlementOrderSyncDO> orderList = orderSyncMap.get(request.getSellerEid());
                    if (CollUtil.isEmpty(orderList)) {
                        orderList = ListUtil.toList(PojoUtils.map(request, SettlementOrderSyncDO.class));
                        orderSyncMap.put(request.getSellerEid(), orderList);
                    } else {
                        orderList.add(PojoUtils.map(request, SettlementOrderSyncDO.class));
                    }
                }
            }
            current = current + 1;
        } while (orderPage != null && CollUtil.isNotEmpty(orderPage.getRecords()));
    }

    /**
     * 查询用于生预售违约结算单的订单
     *
     * @param sellerEid
     * @param orderSyncMap
     * @param updateOrderList
     */
    public void queryPresaleOrder(Long sellerEid, Map<Long, List<SettlementOrderSyncDO>> orderSyncMap, List<SettlementOrderSyncDO> updateOrderList) {
        //分页查询符合结算条件的订单
        int current = 1;
        Page<SettlementOrderSyncDTO> orderPage;
        QuerySettOrderDataPageListRequest queryRequest = new QuerySettOrderDataPageListRequest();
        //分页查询订单列表
        do {
            DateTime currentDate = DateUtil.date();
            //查询今天0点之前签收的订单
            queryRequest.setSellerEid(sellerEid);
            //如果传了eid，则证明是手动生成结算单
            if (ObjectUtil.isNotNull(sellerEid)) {
                queryRequest.setEndCancelTime(currentDate);
            } else {
                queryRequest.setEndCancelTime(DateUtil.offsetDay(DateUtil.beginOfDay(currentDate), -2));
            }
            queryRequest.setCurrent(current);
            queryRequest.setSize(50);
            queryRequest.setOrderStatusList(ListUtil.toList(OrderStatusEnum.CANCELED.getCode()));
            queryRequest.setDefaultStatus(OrderSyncDefaultStatusEnum.ORDER_DEFAULT.getCode());
            queryRequest.setGenerateStatus(SettGenerateStatusEnum.UN_GENERATE.getCode());
            queryRequest.setDataStatus(OrderSyncDataStatusEnum.AUTHORIZED.getCode());
            queryRequest.setSyncStatus(OrderSyncStatusEnum.SUCCESS.getCode());
            //分页查询符合结算条件的订单
            orderPage = settlementOrderSyncService.querySettOrderSyncPageList(queryRequest);
            if (CollUtil.isEmpty(orderPage.getRecords())) {
                break;
            }
            List<SettlementOrderSyncDTO> records = orderPage.getRecords();
            if (CollUtil.isNotEmpty(records)) {
                for (SettlementOrderSyncDTO request : records) {
                    //将订单request插入待更新结算状态的列表
                    updateOrderList.add(PojoUtils.map(request, SettlementOrderSyncDO.class));
                    //加入generateSettMap
                    List<SettlementOrderSyncDO> orderList = orderSyncMap.get(request.getSellerEid());
                    if (CollUtil.isEmpty(orderList)) {
                        orderList = ListUtil.toList(PojoUtils.map(request, SettlementOrderSyncDO.class));
                        orderSyncMap.put(request.getSellerEid(), orderList);
                    } else {
                        orderList.add(PojoUtils.map(request, SettlementOrderSyncDO.class));
                    }
                }
            }
            current = current + 1;
        } while (orderPage != null && CollUtil.isNotEmpty(orderPage.getRecords()));
    }

    /**
     * 生成货款或促销订单对账
     *
     * @param order
     * @param settMap
     * @param goodsSettMap
     * @param saleSettMap
     * @return
     */
    public SettlementOrderDO setGoodsOrSaleSettOrder(SettlementOrderSyncDO order, Map<Long, SettlementDO> settMap, Map<Long, SettlementDetailDO> goodsSettMap, Map<Long, SettlementDetailDO> saleSettMap) {
        SettlementOrderDO settlementOrderDO = new SettlementOrderDO();
        settlementOrderDO.setSettType(SettOrderTypenum.GOODS_OR_SALE.getCode());
        //设置结算单号
        SettlementDetailDO goodsSellDetail = goodsSettMap.get(order.getOrderId());
        SettlementDetailDO saleSellDetail = saleSettMap.get(order.getOrderId());
        //如果订单既没有促销结算单也没有货款结算单
        if (ObjectUtil.isNull(goodsSellDetail) && ObjectUtil.isNull(saleSellDetail)) {
            return null;
        }
        if (ObjectUtil.isNotNull(goodsSellDetail)) {
            settlementOrderDO.setGoodsSettlementId(goodsSellDetail.getSettlementId());
            SettlementDO settlementDO = settMap.get(goodsSellDetail.getSettlementId());
            settlementOrderDO.setGoodsSettlementNo(settlementDO.getCode());
            settlementOrderDO.setGoodsStatus(settlementDO.getStatus());
        }
        if (ObjectUtil.isNotNull(saleSellDetail)) {
            settlementOrderDO.setSaleSettlementId(saleSellDetail.getSettlementId());
            SettlementDO settlementDO = settMap.get(saleSellDetail.getSettlementId());
            settlementOrderDO.setSaleSettlementNo(settlementDO.getCode());
            settlementOrderDO.setSaleStatus(settlementDO.getStatus());
        }

        PojoUtils.map(order, settlementOrderDO);
        BigDecimal goodsAmount = BigDecimal.ZERO;
        //如果不是线下付款则统计货款金额
        if (!ObjectUtil.equal(order.getPaymentMethod(), PaymentMethodEnum.OFFLINE.getCode())) {
            //应结算货款金额=支付金额-支付退款金额
            goodsAmount = order.getPaymentAmount().subtract(order.getRefundPaymentAmount());
            settlementOrderDO.setGoodsAmount(goodsAmount);
            settlementOrderDO.setTotalGoodsAmount(order.getPaymentAmount());
            settlementOrderDO.setRefundGoodsAmount(order.getRefundPaymentAmount());
        }

        //本单秒杀&特价促销金额
        BigDecimal orderPromotionAmount = order.getPromotionAmount();
        //本单单秒杀&特价退款金额
        BigDecimal refundPromotionAmount = order.getRefundPromotionAmount();

        //如果本单应结算的秒杀&特价金额小于等于0，则结算金额记为0
        if (orderPromotionAmount.compareTo(BigDecimal.ZERO) < 1) {
            orderPromotionAmount = BigDecimal.ZERO;
            refundPromotionAmount = BigDecimal.ZERO;
            settlementOrderDO.setPromotionAmount(orderPromotionAmount);
            settlementOrderDO.setRefundPromotionAmount(refundPromotionAmount);
        }
        //本单组合包促销金额
        BigDecimal orderComPacAmount = order.getComPacAmount();
        //本单组合包退款金额
        BigDecimal refundComPacAmount = order.getRefundComPacAmount();
        //如果本单应结算的组合包金额小于等于0，则结算金额记为0
        if (orderComPacAmount.compareTo(BigDecimal.ZERO) < 1) {
            orderComPacAmount = BigDecimal.ZERO;
            refundComPacAmount = BigDecimal.ZERO;
            settlementOrderDO.setComPacAmount(orderComPacAmount);
            settlementOrderDO.setRefundComPacAmount(refundComPacAmount);
        }
        //本单预售优惠金额
        BigDecimal orderPresaleDiscountAmount = order.getPresaleDiscountAmount();
        //本单预售优惠退款金额
        BigDecimal refundPreAmount = order.getRefundPreAmount();
        //如果本单应结算的组合包金额小于等于0，则结算金额记为0
        if (orderPresaleDiscountAmount.compareTo(BigDecimal.ZERO) < 1) {
            orderPresaleDiscountAmount = BigDecimal.ZERO;
            refundPreAmount = BigDecimal.ZERO;
            settlementOrderDO.setPresaleDiscountAmount(orderPresaleDiscountAmount);
            settlementOrderDO.setRefundPreAmount(refundPreAmount);
        }
        //本单支付促销优惠金额
        BigDecimal payDiscountAmount = order.getPayDiscountAmount();
        //本单支付促销退款金额
        BigDecimal refundPayAmount = order.getRefundPayAmount();
        //如果本单应结算的支付金额小于等于0，则结算金额记为0
        if (payDiscountAmount.compareTo(BigDecimal.ZERO) < 1) {
            payDiscountAmount = BigDecimal.ZERO;
            refundPayAmount = BigDecimal.ZERO;
            settlementOrderDO.setPayDiscountAmount(payDiscountAmount);
            settlementOrderDO.setRefundPayAmount(refundPayAmount);
        }
        //应结算促销金额=（优惠券金额-优惠券退款金额）+（秒杀特价金额-秒杀特价退款金额）+（满赠金额-满赠退款金额）+（组合包金额-组合包退款金额）+（预售优惠金额-预售优惠退款金额）+（支付促销优惠金额-支付促销退款金额）
        BigDecimal saleAmount = order.getCouponAmount().subtract(order.getRefundCouponAmount()).add(orderPromotionAmount).subtract(refundPromotionAmount).add(order.getGiftAmount().subtract(order.getRefundGiftAmount())).add(orderComPacAmount.subtract(refundComPacAmount)).add(orderPresaleDiscountAmount.subtract(refundPreAmount)).add(payDiscountAmount.subtract(refundPayAmount));
        settlementOrderDO.setSalesAmount(saleAmount);
        //结算总金额=应结算货款金额+应结算促销金额
        BigDecimal totalAmount = goodsAmount.add(saleAmount);
        settlementOrderDO.setTotalAmount(totalAmount);
        return settlementOrderDO;
    }

    /**
     * 生成预售违约金订单对账
     *
     * @param order
     * @param settMap
     * @param pdSettMap
     * @return
     */
    public SettlementOrderDO setPresaleDefaultSettOrder(SettlementOrderSyncDO order, Map<Long, SettlementDO> settMap, Map<Long, SettlementDetailDO> pdSettMap) {
        SettlementOrderDO settlementOrderDO = new SettlementOrderDO();
        settlementOrderDO.setSettType(SettOrderTypenum.PRESALE_DEFAULT.getCode());
        //设置结算单号
        SettlementDetailDO pdSellDetail = pdSettMap.get(order.getOrderId());
        //如果订单没有预售违约金结算单
        if (ObjectUtil.isNull(pdSellDetail)) {
            return null;
        }
        PojoUtils.map(order, settlementOrderDO);
        settlementOrderDO.setPdSettlementId(pdSellDetail.getSettlementId());
        SettlementDO settlementDO = settMap.get(pdSellDetail.getSettlementId());
        settlementOrderDO.setPdSettlementNo(settlementDO.getCode());
        settlementOrderDO.setPresaleDefaultStatus(settlementDO.getStatus());
        settlementOrderDO.setPresaleDefaultAmount(pdSellDetail.getPresaleDefaultAmount());
        settlementOrderDO.setPdAmount(pdSellDetail.getPresaleDefaultAmount());
        //结算总金额
        settlementOrderDO.setTotalAmount(pdSellDetail.getPresaleDefaultAmount());
        return settlementOrderDO;
    }


    /**
     * 计算该笔订单货款应结算金额
     *
     * @param order
     * @param goodsSettlement
     */
    private void setGoodsSett(SettlementOrderSyncDO order, SettlementDO goodsSettlement) {
        //如果不是线下付款则加入货款结算单
        if (ObjectUtil.notEqual(order.getPaymentMethod(), PaymentMethodEnum.OFFLINE.getCode())) {
            //如果货款金额为0 则终止
            if (order.getPaymentAmount().compareTo(BigDecimal.ZERO) == 0) {
                return;
            }
            //货款单应结算的金额
            BigDecimal amount = goodsSettlement.getAmount();
            //货款单的总货款金额
            BigDecimal goodsAmount = goodsSettlement.getGoodsAmount();
            //货款单的总货款退款金额
            BigDecimal refundAmount = goodsSettlement.getRefundGoodsAmount();

            //本单应结算的货款金额
            BigDecimal orderAmount = order.getPaymentAmount().subtract(order.getRefundPaymentAmount());
            //结算金额小于等于0时忽略该订单
            if (BigDecimal.ZERO.compareTo(orderAmount) > -1) {
                return;
            }
            amount = amount.add(orderAmount);
            goodsAmount = goodsAmount.add(order.getPaymentAmount());
            refundAmount = refundAmount.add(order.getRefundPaymentAmount());
            goodsSettlement.setAmount(amount);
            goodsSettlement.setGoodsAmount(goodsAmount);
            goodsSettlement.setRefundGoodsAmount(refundAmount);
            goodsSettlement.setOrderCount(goodsSettlement.getOrderCount() + 1);

            SettlementDetailDO detail = buildSettlementDetailDO(order);
            detail.setAmount(orderAmount);
            detail.setGoodsAmount(order.getPaymentAmount());
            detail.setRefundGoodsAmount(order.getRefundPaymentAmount());
            goodsSettlement.getSettlementDetailDOS().add(detail);
        }
    }

    /**
     * 统计该笔订单优惠券应结算金额
     *
     * @param order
     * @param saleSettlement
     */
    private void setCouponSett(SettlementOrderSyncDO order, SettlementDO saleSettlement) {

        //如果该订单涉及了优惠券平台促销结算
        if (order.getCouponAmount().compareTo(BigDecimal.ZERO) == 1) {
            //促销单应结算的金额
            BigDecimal amount = saleSettlement.getAmount();
            //结算单的总促销金额
            BigDecimal discountAmount = saleSettlement.getDiscountAmount();
            //结算单的促销退款金额
            BigDecimal refundDiscountAmount = saleSettlement.getRefundDiscountAmount();
            //优惠券促销金额
            BigDecimal saleAmount = saleSettlement.getCouponAmount();
            //优惠券退款金额
            BigDecimal refundAmount = saleSettlement.getRefundCouponAmount();

            //本单应结算的优惠券促销金额
            BigDecimal orderAmount = order.getCouponAmount().subtract(order.getRefundCouponAmount());
            //如果本单优惠券促销金额小于等于0 则终止
            if (BigDecimal.ZERO.compareTo(orderAmount) > -1) {
                return;
            }
            amount = amount.add(orderAmount);
            saleAmount = saleAmount.add(order.getCouponAmount());
            refundAmount = refundAmount.add(order.getRefundCouponAmount());

            saleSettlement.setAmount(amount);
            saleSettlement.setDiscountAmount(discountAmount.add(order.getCouponAmount()));
            saleSettlement.setRefundDiscountAmount(refundDiscountAmount.add(order.getRefundCouponAmount()));
            saleSettlement.setCouponAmount(saleAmount);
            saleSettlement.setRefundCouponAmount(refundAmount);
            SettlementDetailDO detail = buildSettlementDetailDO(order);
            detail.setAmount(orderAmount);
            detail.setCouponAmount(order.getCouponAmount());
            detail.setRefundCouponAmount(order.getRefundCouponAmount());

            detail.setShopCouponId(order.getShopCouponId());
            detail.setPlatformCouponId(order.getPlatformCouponId());
            detail.setShopCouponPercent(order.getShopCouponPercent());
            detail.setPlatformCouponPercent(order.getPlatformCouponPercent());
            detail.setPlatformCouponDiscountAmount(order.getPlatformCouponDiscountAmount());
            detail.setCouponDiscountAmount(order.getShopCouponDiscountAmount());
            detail.setReturnPlatformCouponDiscountAmount(order.getReturnPlatformCouponDiscountAmount());
            detail.setReturnCouponDiscountAmount(order.getReturnCouponDiscountAmount());
            saleSettlement.getSettlementDetailDOS().add(detail);
        }
    }

    /**
     * 统计该笔订单秒杀&特价应结算金额
     *
     * @param order
     * @param saleSettlement
     */
    private void setPromotionSett(SettlementOrderSyncDO order, SettlementDO saleSettlement) {

        //本单秒杀&特价促销金额
        BigDecimal orderSaleAmount = order.getPromotionAmount();
        //本单单秒杀&特价退款金额
        BigDecimal orderSaleRefundAmount = order.getRefundPromotionAmount();
        //如果本单的秒杀&特价结算金额为0，则不将该笔订单归入到结算单明细
        if (orderSaleAmount.compareTo(BigDecimal.ZERO) == 0) {
            return;
        }
        //如果本单应结算的秒杀&特价金额小于等于0，则归入到结算单明细且结算金额记为0
        if (orderSaleAmount.compareTo(BigDecimal.ZERO) < 1) {
            orderSaleAmount = BigDecimal.ZERO;
            orderSaleRefundAmount = BigDecimal.ZERO;
        }
        //促销单应结算的金额
        BigDecimal amount = saleSettlement.getAmount();
        //结算单的总促销金额
        BigDecimal discountAmount = saleSettlement.getDiscountAmount();
        //结算单的促销退款金额
        BigDecimal refundDiscountAmount = saleSettlement.getRefundDiscountAmount();
        //结算单秒杀&特价促销金额
        BigDecimal saleAmount = saleSettlement.getPromotionAmount();
        //结算单秒杀&特价退款金额
        BigDecimal refundAmount = saleSettlement.getRefundPromotionAmount();

        //本单应结算的秒杀&特价促销金额
        BigDecimal orderAmount = orderSaleAmount.subtract(orderSaleRefundAmount);
        //如果本单结算的秒杀&特价促销金额小于等于0 则终止
        if (BigDecimal.ZERO.compareTo(orderAmount) > -1) {
            return;
        }
        amount = amount.add(orderAmount);
        saleAmount = saleAmount.add(orderSaleAmount);
        refundAmount = refundAmount.add(orderSaleRefundAmount);

        saleSettlement.setAmount(amount);
        saleSettlement.setDiscountAmount(discountAmount.add(orderSaleAmount));
        saleSettlement.setRefundDiscountAmount(refundDiscountAmount.add(orderSaleRefundAmount));
        saleSettlement.setPromotionAmount(saleAmount);
        saleSettlement.setRefundPromotionAmount(refundAmount);
        Map<Long, SettlementDetailDO> detailDOMap = saleSettlement.getSettlementDetailDOS().stream().collect(Collectors.toMap(SettlementDetailDO::getOrderId, e -> e));
        SettlementDetailDO detailDO = detailDOMap.get(order.getOrderId());
        if (ObjectUtil.isNull(detailDO)) {
            detailDO = buildSettlementDetailDO(order);
            detailDO.setAmount(orderAmount);
            saleSettlement.getSettlementDetailDOS().add(detailDO);
        } else {
            detailDO.setAmount(detailDO.getAmount().add(orderAmount));
        }
        detailDO.setPromotionAmount(orderSaleAmount);
        detailDO.setRefundPromotionAmount(orderSaleRefundAmount);
        //如果本单应结算的秒杀&特价金额小于等于0
        if (orderSaleAmount.compareTo(BigDecimal.ZERO) < 1) {
            detailDO.setRemark("秒杀&特价结算金额为负数，系统自动置为0，置为0前的加个为{" + order.getPromotionAmount().toString() + "}");
        }
    }

    /**
     * 统计该笔订赠品应结算金额
     *
     * @param order
     * @param saleSettlement
     */
    private void setGiftSett(SettlementOrderSyncDO order, SettlementDO saleSettlement) {

        //如果该订单涉及了赠品促销结算
        if (order.getGiftAmount().compareTo(BigDecimal.ZERO) == 1) {
            //促销单应结算的金额
            BigDecimal amount = saleSettlement.getAmount();
            //结算单的总促销金额
            BigDecimal discountAmount = saleSettlement.getDiscountAmount();
            //结算单的促销退款金额
            BigDecimal refundDiscountAmount = saleSettlement.getRefundDiscountAmount();
            //赠品促销金额
            BigDecimal saleAmount = saleSettlement.getGiftAmount();
            //赠品退款金额
            BigDecimal refundAmount = saleSettlement.getRefundGiftAmount();
            //            //如果促销金额为0 则终止
            //            if (order.getPromotionAmount().compareTo(BigDecimal.ZERO) == 0) {
            //                return;
            //            }
            //本单应结算的赠品促销金额
            BigDecimal orderAmount = order.getGiftAmount().subtract(order.getRefundGiftAmount());
            //如果本单结算的赠品促销金额小于等于0 则终止
            if (BigDecimal.ZERO.compareTo(orderAmount) > -1) {
                return;
            }
            amount = amount.add(orderAmount);
            saleAmount = saleAmount.add(order.getGiftAmount());
            refundAmount = refundAmount.add(order.getRefundGiftAmount());

            saleSettlement.setAmount(amount);
            saleSettlement.setDiscountAmount(discountAmount.add(order.getGiftAmount()));
            saleSettlement.setRefundDiscountAmount(refundDiscountAmount.add(order.getRefundGiftAmount()));
            saleSettlement.setGiftAmount(saleAmount);
            saleSettlement.setRefundGiftAmount(refundAmount);
            Map<Long, SettlementDetailDO> detailDOMap = saleSettlement.getSettlementDetailDOS().stream().collect(Collectors.toMap(SettlementDetailDO::getOrderId, e -> e));
            SettlementDetailDO detailDO = detailDOMap.get(order.getOrderId());
            if (ObjectUtil.isNull(detailDO)) {
                detailDO = buildSettlementDetailDO(order);
                detailDO.setAmount(orderAmount);
                saleSettlement.getSettlementDetailDOS().add(detailDO);
            } else {
                detailDO.setAmount(detailDO.getAmount().add(orderAmount));
            }
            detailDO.setGiftAmount(order.getGiftAmount());
            detailDO.setRefundGiftAmount(order.getRefundGiftAmount());
        }
    }

    /**
     * 统计该笔订组合包应结算金额
     *
     * @param order
     * @param saleSettlement
     */
    private void setComPacSett(SettlementOrderSyncDO order, SettlementDO saleSettlement) {

        //如果该订单涉及了组合包促销结算
        if (order.getComPacAmount().compareTo(BigDecimal.ZERO) == 1) {
            //促销单应结算的金额
            BigDecimal amount = saleSettlement.getAmount();
            //结算单的总促销金额
            BigDecimal discountAmount = saleSettlement.getDiscountAmount();
            //结算单的促销退款金额
            BigDecimal refundDiscountAmount = saleSettlement.getRefundDiscountAmount();
            //赠品促销金额
            BigDecimal saleAmount = saleSettlement.getComPacAmount();
            //赠品退款金额
            BigDecimal refundAmount = saleSettlement.getRefundComPacAmount();
            //            //如果促销金额为0 则终止
            //            if (order.getPromotionAmount().compareTo(BigDecimal.ZERO) == 0) {
            //                return;
            //            }
            //本单应结算的组合包促销金额
            BigDecimal orderAmount = order.getComPacAmount().subtract(order.getRefundComPacAmount());
            //如果本单结算的组合包促销金额小于等于0 则终止
            if (BigDecimal.ZERO.compareTo(orderAmount) > -1) {
                return;
            }
            amount = amount.add(orderAmount);
            saleAmount = saleAmount.add(order.getComPacAmount());
            refundAmount = refundAmount.add(order.getRefundComPacAmount());

            saleSettlement.setAmount(amount);
            saleSettlement.setDiscountAmount(discountAmount.add(order.getComPacAmount()));
            saleSettlement.setRefundDiscountAmount(refundDiscountAmount.add(order.getRefundComPacAmount()));
            saleSettlement.setComPacAmount(saleAmount);
            saleSettlement.setRefundComPacAmount(refundAmount);
            Map<Long, SettlementDetailDO> detailDOMap = saleSettlement.getSettlementDetailDOS().stream().collect(Collectors.toMap(SettlementDetailDO::getOrderId, e -> e));
            SettlementDetailDO detailDO = detailDOMap.get(order.getOrderId());
            if (ObjectUtil.isNull(detailDO)) {
                detailDO = buildSettlementDetailDO(order);
                detailDO.setAmount(orderAmount);
                saleSettlement.getSettlementDetailDOS().add(detailDO);
            } else {
                detailDO.setAmount(detailDO.getAmount().add(orderAmount));
            }
            detailDO.setComPacAmount(order.getComPacAmount());
            detailDO.setRefundComPacAmount(order.getRefundComPacAmount());
        }
    }

    /**
     * 统计该笔订预售应结算金额
     *
     * @param order
     * @param saleSettlement
     */
    private void setPreSaleSett(SettlementOrderSyncDO order, SettlementDO saleSettlement) {

        //如果该订单涉及了预售促销结算
        if (order.getPresaleDiscountAmount().compareTo(BigDecimal.ZERO) == 1) {
            //促销单应结算的金额
            BigDecimal amount = saleSettlement.getAmount();
            //结算单的总促销金额
            BigDecimal discountAmount = saleSettlement.getDiscountAmount();
            //结算单的促销退款金额
            BigDecimal refundDiscountAmount = saleSettlement.getRefundDiscountAmount();
            //预售促销金额
            BigDecimal saleAmount = saleSettlement.getPresaleDiscountAmount();
            //预售退款金额
            BigDecimal refundAmount = saleSettlement.getRefundPreAmount();
            //            //如果促销金额为0 则终止
            //            if (order.getPromotionAmount().compareTo(BigDecimal.ZERO) == 0) {
            //                return;
            //            }
            //本单应结算的预售促销金额
            BigDecimal orderAmount = order.getPresaleDiscountAmount().subtract(order.getRefundPreAmount());
            //如果本单结算的预售促销金额小于等于0 则终止
            if (BigDecimal.ZERO.compareTo(orderAmount) > -1) {
                return;
            }
            amount = amount.add(orderAmount);
            saleAmount = saleAmount.add(order.getPresaleDiscountAmount());
            refundAmount = refundAmount.add(order.getRefundPreAmount());

            saleSettlement.setAmount(amount);
            saleSettlement.setDiscountAmount(discountAmount.add(order.getPresaleDiscountAmount()));
            saleSettlement.setRefundDiscountAmount(refundDiscountAmount.add(order.getRefundPreAmount()));
            saleSettlement.setPresaleDiscountAmount(saleAmount);
            saleSettlement.setRefundPreAmount(refundAmount);
            Map<Long, SettlementDetailDO> detailDOMap = saleSettlement.getSettlementDetailDOS().stream().collect(Collectors.toMap(SettlementDetailDO::getOrderId, e -> e));
            SettlementDetailDO detailDO = detailDOMap.get(order.getOrderId());
            if (ObjectUtil.isNull(detailDO)) {
                detailDO = buildSettlementDetailDO(order);
                detailDO.setAmount(orderAmount);
                saleSettlement.getSettlementDetailDOS().add(detailDO);
            } else {
                detailDO.setAmount(detailDO.getAmount().add(orderAmount));
            }
            detailDO.setPresaleDiscountAmount(order.getPresaleDiscountAmount());
            detailDO.setRefundPreAmount(order.getRefundPreAmount());
        }
    }

    /**
     * 统计该笔订支付促销应结算金额
     *
     * @param order
     * @param saleSettlement
     */
    private void setPaySaleSett(SettlementOrderSyncDO order, SettlementDO saleSettlement) {

        //如果该订单涉及了预售促销结算
        if (order.getPayDiscountAmount().compareTo(BigDecimal.ZERO) == 1) {
            //促销单应结算的金额
            BigDecimal amount = saleSettlement.getAmount();
            //结算单的总促销金额
            BigDecimal discountAmount = saleSettlement.getDiscountAmount();
            //结算单的促销退款金额
            BigDecimal refundDiscountAmount = saleSettlement.getRefundDiscountAmount();
            //支付促销金额
            BigDecimal saleAmount = saleSettlement.getPayDiscountAmount();
            //支付促销退款金额
            BigDecimal refundAmount = saleSettlement.getRefundPayAmount();
            //本单应结算的支付促销金额
            BigDecimal orderAmount = order.getPayDiscountAmount().subtract(order.getRefundPayAmount());
            //todo 如果本单结算的支付促销金额小于等于0 则终止
            if (BigDecimal.ZERO.compareTo(orderAmount) > -1) {
                return;
            }
            amount = amount.add(orderAmount);
            saleAmount = saleAmount.add(order.getPayDiscountAmount());
            refundAmount = refundAmount.add(order.getRefundPayAmount());

            saleSettlement.setAmount(amount);
            saleSettlement.setDiscountAmount(discountAmount.add(order.getPayDiscountAmount()));
            saleSettlement.setRefundDiscountAmount(refundDiscountAmount.add(order.getRefundPayAmount()));
            saleSettlement.setPayDiscountAmount(saleAmount);
            saleSettlement.setRefundPayAmount(refundAmount);
            Map<Long, SettlementDetailDO> detailDOMap = saleSettlement.getSettlementDetailDOS().stream().collect(Collectors.toMap(SettlementDetailDO::getOrderId, e -> e));
            SettlementDetailDO detailDO = detailDOMap.get(order.getOrderId());
            if (ObjectUtil.isNull(detailDO)) {
                detailDO = buildSettlementDetailDO(order);
                detailDO.setAmount(orderAmount);
                saleSettlement.getSettlementDetailDOS().add(detailDO);
            } else {
                detailDO.setAmount(detailDO.getAmount().add(orderAmount));
            }
            detailDO.setPayDiscountAmount(order.getPayDiscountAmount());
            detailDO.setRefundPayAmount(order.getRefundPayAmount());
        }
    }

    /**
     * 计算该笔订单预售违约金应结算金额
     *
     * @param order
     * @param pdSettlement
     */
    private void setDPSett(SettlementOrderSyncDO order, SettlementDO pdSettlement) {
        //如果预售付款金额为0 则终止
        if (order.getPresaleDepositAmount().compareTo(BigDecimal.ZERO) == 0) {
            return;
        }
        //违约金结算单应结算的金额
        BigDecimal amount = pdSettlement.getAmount();
        //预售违约结算单的总货款金额
        BigDecimal pdAmount = pdSettlement.getPresaleDefaultAmount();

        //本单应结算的违约金额
        BigDecimal orderAmount = order.getPresaleDepositAmount();

        amount = amount.add(orderAmount);
        pdAmount = pdAmount.add(orderAmount);

        pdSettlement.setAmount(amount);
        pdSettlement.setPresaleDefaultAmount(pdAmount);
        pdSettlement.setOrderCount(pdSettlement.getOrderCount() + 1);

        SettlementDetailDO detail = buildSettlementDetailDO(order);
        detail.setAmount(orderAmount);
        detail.setPresaleDefaultAmount(orderAmount);
        pdSettlement.getSettlementDetailDOS().add(detail);
    }

    private SettlementDetailDO buildSettlementDetailDO(SettlementOrderSyncDO order) {
        SettlementDetailDO detailDO = new SettlementDetailDO();
        detailDO.setOrderId(order.getOrderId());
        detailDO.setOrderNo(order.getOrderNo());
        detailDO.setBuyerEid(order.getBuyerEid());
        detailDO.setSellerEid(order.getSellerEid());
        detailDO.setOrderSyncId(order.getId());

        return detailDO;
    }

}
