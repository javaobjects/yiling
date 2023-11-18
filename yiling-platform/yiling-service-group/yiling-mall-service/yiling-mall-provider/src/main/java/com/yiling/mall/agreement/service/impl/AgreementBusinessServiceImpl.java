package com.yiling.mall.agreement.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.B2bGoodsApi;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.api.GoodsLimitPriceApi;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.goods.medicine.dto.GoodsInfoDTO;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.mall.agreement.dto.AgreementCalculateResultDTO;
import com.yiling.mall.agreement.dto.CalculateRebateApplyDTO;
import com.yiling.mall.agreement.dto.request.AgreementCalculateRequest;
import com.yiling.mall.agreement.dto.request.CalculateRebateApplyRequest;
import com.yiling.mall.agreement.dto.request.CashMallAgreementRequest;
import com.yiling.mall.agreement.dto.request.SaveRebateApplyRequest;
import com.yiling.mall.agreement.enums.GoodsLimitStatusEnum;
import com.yiling.mall.agreement.service.AgreementBusinessService;
import com.yiling.order.order.api.NoApi;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderDetailApi;
import com.yiling.order.order.api.OrderReturnApi;
import com.yiling.order.order.dto.AgreementOrderReturnDetailDTO;
import com.yiling.order.order.dto.OrderAndDetailedAllInfoDTO;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDetailByAgreementDTO;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.dto.request.QueryOrderUseAgreementRequest;
import com.yiling.order.order.enums.NoEnum;
import com.yiling.order.settlement.api.SettlementApi;
import com.yiling.order.settlement.dto.SettlementDetailDTO;
import com.yiling.order.settlement.dto.SettlementFullDTO;
import com.yiling.user.agreement.api.AgreementApi;
import com.yiling.user.agreement.api.AgreementApplyApi;
import com.yiling.user.agreement.api.AgreementApplyDetailApi;
import com.yiling.user.agreement.api.AgreementGoodsApi;
import com.yiling.user.agreement.api.AgreementRebateLogApi;
import com.yiling.user.agreement.api.AgreementRebateOrderApi;
import com.yiling.user.agreement.api.AgreementRebateOrderDetailApi;
import com.yiling.user.agreement.api.ApplyOrderApi;
import com.yiling.user.agreement.dto.AgreementConditionDTO;
import com.yiling.user.agreement.dto.AgreementDTO;
import com.yiling.user.agreement.dto.AgreementGoodsDTO;
import com.yiling.user.agreement.dto.AgreementRebateApplyDTO;
import com.yiling.user.agreement.dto.AgreementRebateOrderDTO;
import com.yiling.user.agreement.dto.AgreementRebateOrderDetailDTO;
import com.yiling.user.agreement.dto.SupplementAgreementDetailDTO;
import com.yiling.user.agreement.dto.request.AddApplyOrderRequest;
import com.yiling.user.agreement.dto.request.AddRebateApplyDetailRequest;
import com.yiling.user.agreement.dto.request.AddRebateApplyRequest;
import com.yiling.user.agreement.dto.request.AddRebateLogRequest;
import com.yiling.user.agreement.dto.request.AgreementConditionCalculateRequest;
import com.yiling.user.agreement.dto.request.AgreementPurchaseGoodsRequest;
import com.yiling.user.agreement.dto.request.AgreementRebateOrderRequest;
import com.yiling.user.agreement.dto.request.CashAgreementRequest;
import com.yiling.user.agreement.dto.request.ClearAgreementConditionCalculateRequest;
import com.yiling.user.agreement.dto.request.QueryAgreementRebateRequest;
import com.yiling.user.agreement.dto.request.QueryRebateOrderDetailRequest;
import com.yiling.user.agreement.enums.AgreementApplyStatusEnum;
import com.yiling.user.agreement.enums.AgreementChildTypeEnum;
import com.yiling.user.agreement.enums.AgreementConditionRuleEnum;
import com.yiling.user.agreement.enums.AgreementErrorCode;
import com.yiling.user.agreement.enums.AgreementModeEnum;
import com.yiling.user.agreement.enums.AgreementRebateCycleEnum;
import com.yiling.user.agreement.enums.AgreementRebateOrderCashStatusEnum;
import com.yiling.user.agreement.enums.AgreementRebateOrderConditionStatusEnum;
import com.yiling.user.agreement.enums.AgreementStatusEnum;
import com.yiling.user.agreement.enums.ApplyDetailTypeEnum;
import com.yiling.user.agreement.enums.RebateOrderTypeEnum;
import com.yiling.user.agreement.util.AgreementUtils;
import com.yiling.user.control.api.ControlApi;
import com.yiling.user.enterprise.api.CustomerApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.api.EnterpriseCustomerLineApi;
import com.yiling.user.enterprise.api.EnterprisePurchaseApplyApi;
import com.yiling.user.enterprise.api.EnterprisePurchaseRelationApi;
import com.yiling.user.enterprise.dto.EnterpriseCustomerEasDTO;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.procrelation.api.ProcurementRelationGoodsApi;
import com.yiling.user.shop.api.ShopApi;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2021/6/17
 */
@Service
@Slf4j
public class AgreementBusinessServiceImpl implements AgreementBusinessService {

    @DubboReference
    AgreementGoodsApi             agreementGoodsApi;
    @DubboReference
    AgreementApi                  agreementApi;
    @DubboReference
    OrderApi                      orderApi;
    @DubboReference
    SettlementApi                 settlementApi;
    @DubboReference
    EnterprisePurchaseRelationApi enterprisePurchaseRelationApi;
    @DubboReference
    AgreementRebateOrderApi       agreementRebateOrderApi;
    @DubboReference
    OrderReturnApi                orderReturnApi;
    @DubboReference
    CustomerApi                   customerApi;
    @DubboReference
    AgreementRebateLogApi         agreementRebateLogApi;
    @DubboReference
    AgreementRebateOrderDetailApi agreementRebateOrderDetailApi;
    @DubboReference
    NoApi                         noApi;
    @DubboReference
    AgreementApplyApi             applyApi;
    @DubboReference
    AgreementApplyDetailApi       applyDetailApi;
    @DubboReference
    EnterpriseApi                 enterpriseApi;
    @DubboReference
    ApplyOrderApi                 applyOrderApi;
    @DubboReference
    OrderDetailApi                orderDetailApi;
    @DubboReference
    ControlApi                    controlApi;
    @DubboReference
    GoodsLimitPriceApi            goodsLimitPriceApi;
    @DubboReference
    ShopApi                       shopApi;
    @DubboReference
    B2bGoodsApi                   b2bGoodsApi;
    @DubboReference
    EnterpriseCustomerLineApi     enterpriseCustomerLineApi;
    @DubboReference
    EnterprisePurchaseApplyApi    enterprisePurchaseApplyApi;
    @DubboReference
    GoodsApi                      goodsApi;
    @DubboReference
    ProcurementRelationGoodsApi procurementRelationGoodsApi;

    @Override
    public Map<Long, Integer> getGoodsLimitByGids(List<Long> gidList, Long buyerEid) {
        if (CollUtil.isEmpty(gidList)) {
            return Collections.emptyMap();
        }

        Map<Long, Integer> limitMap = new HashMap<>(gidList.size());

        // 未登录
        if (buyerEid == null || buyerEid.intValue() == 0) {
            for (Long gid : gidList) {
                limitMap.put(gid, GoodsLimitStatusEnum.NOT_LOGIN.getCode());
            }
            return limitMap;
        }

        // 查询是否有销售商信息
        List<Long> sellerIds = enterprisePurchaseRelationApi.listSellerEidsByBuyerEid(buyerEid);
        if (CollUtil.isEmpty(sellerIds)) {
            for (Long gid : gidList) {
                limitMap.put(gid, GoodsLimitStatusEnum.OUT_OF_RELATION.getCode());
            }
            return limitMap;
        }

        List<GoodsDTO> goodsDTOList = goodsApi.batchQueryInfo(gidList);
        Map<Long, GoodsDTO> goodsDTOMap = goodsDTOList.stream().collect(Collectors.toMap(GoodsDTO::getId, Function.identity()));

        List<Long> procurementGoodsIds = procurementRelationGoodsApi.getYlGoodsIdByYlGoodsIds(gidList, buyerEid);
        // 默认正常
        for (Long gid : gidList) {
            limitMap.put(gid, GoodsLimitStatusEnum.NORMAL.getCode());
            //查询是否在协议内
//            Long agreementId = agreementGoodsApi.getAgreementIdByPurchaseGoodsList(buyerEid, gid);
            if (!procurementGoodsIds.contains(gid)) {
                limitMap.put(gid, GoodsLimitStatusEnum.NOT_BUY.getCode());
                continue;
            }
            GoodsDTO goods = goodsDTOMap.get(gid);
            if(goods == null || !GoodsStatusEnum.AUDIT_PASS.getCode().equals(goods.getAuditStatus())){
                limitMap.put(gid, GoodsLimitStatusEnum.INVALID_GOODS.getCode());
                continue;
            }
        }

        return limitMap;
    }

    @Override
    public Map<Long, Integer> getB2bGoodsLimitByGids(List<Long> gidList, Long buyerEid) {
        if (CollUtil.isEmpty(gidList)) {
            return Collections.emptyMap();
        }

        Map<Long, Integer> limitMap = new HashMap<>(gidList.size());

        // 未登录
        if (buyerEid == null || buyerEid.intValue() == 0) {
            for (Long gid : gidList) {
                limitMap.put(gid, GoodsLimitStatusEnum.NOT_LOGIN.getCode());
            }
            return limitMap;
        }

        // 默认正常
        for (Long gid : gidList) {
            limitMap.put(gid, GoodsLimitStatusEnum.NORMAL.getCode());
        }

        List<GoodsInfoDTO> goodsList = b2bGoodsApi.batchQueryInfo(gidList);
        List<Long> eids = goodsList.stream().map(e -> e.getEid()).distinct().collect(Collectors.toList());
        Map<Long, GoodsInfoDTO> goodsDTOMap = goodsList.stream().collect(Collectors.toMap(GoodsInfoDTO::getId, Function.identity()));
        for (Long gid : gidList) {
            GoodsInfoDTO b2bGoodsDTO = goodsDTOMap.get(gid);
            if (b2bGoodsDTO == null || b2bGoodsDTO.getGoodsStatus().equals(GoodsStatusEnum.UN_SHELF.getCode()) || b2bGoodsDTO.getGoodsStatus().equals(GoodsStatusEnum.WAIT_SET.getCode())) {
                limitMap.put(gid, GoodsLimitStatusEnum.UN_SHELF.getCode());
            }
            if(!GoodsStatusEnum.AUDIT_PASS.getCode().equals(b2bGoodsDTO.getAuditStatus())){
                limitMap.put(gid, GoodsLimitStatusEnum.INVALID_GOODS.getCode());
            }
        }

        // 查询是否区域控销
        Map<Long, Boolean> checkResultMap = shopApi.checkSaleAreaByCustomerEid(buyerEid, eids);
        for (GoodsInfoDTO goodsDTO : goodsList) {
            if (!checkResultMap.get(goodsDTO.getEid())) {
                limitMap.put(goodsDTO.getId(), GoodsLimitStatusEnum.SHOP_CONTROL.getCode());
            }
        }

        // 查询是否品种控销
        Map<Long, Integer> controlMap = new HashMap<>();
        Map<Long, List<GoodsInfoDTO>> goodsInfoMap = goodsList.stream().collect(Collectors.groupingBy(GoodsInfoDTO::getEid));
        for (Map.Entry<Long, List<GoodsInfoDTO>> entry : goodsInfoMap.entrySet()) {
            List<Long> gids = entry.getValue().stream().map(e -> e.getId()).collect(Collectors.toList());
            controlMap.putAll(controlApi.getGoodsControlByBuyerEidAndGid(gids, entry.getKey(), buyerEid));
        }
        for (Long gid : gidList) {
            if (controlMap.get(gid) == 1) {
                limitMap.put(gid, GoodsLimitStatusEnum.CONTROL_GOODS.getCode());
            }
        }

        // 查询是否采购关系
        Map<Long, Integer> enterpriseCustomerLineApiCustomerLineListFlagMap = enterprisePurchaseApplyApi.getPurchaseApplyStatus(eids, buyerEid);
        for (GoodsInfoDTO goodsDTO : goodsList) {
            if (enterpriseCustomerLineApiCustomerLineListFlagMap.get(goodsDTO.getEid()) == 1) {
                limitMap.put(goodsDTO.getId(), GoodsLimitStatusEnum.NOT_RELATION_SHIP.getCode());
            }
            if (enterpriseCustomerLineApiCustomerLineListFlagMap.get(goodsDTO.getEid()) == 2) {
                limitMap.put(goodsDTO.getId(), GoodsLimitStatusEnum.AUDIT_RELATION_SHIP.getCode());
            }
        }

        return limitMap;
    }

    @Override
    public Map<Long, Integer> getB2bGoodsLimitBySkuIds(List<Long> skuIdList, Long buyerEid) {
        if (CollUtil.isEmpty(skuIdList)) {
            return Collections.emptyMap();
        }
        List<GoodsSkuDTO> skuList = goodsApi.getGoodsSkuByIds(skuIdList);
        if (CollUtil.isEmpty(skuList)) {
            return Collections.emptyMap();
        }
        List<Long> gidList = skuList.stream().map(GoodsSkuDTO::getGoodsId).distinct().collect(Collectors.toList());
        Map<Long, Integer> map = this.getB2bGoodsLimitByGids(gidList, buyerEid);
        return map;
    }

    @Override
    public Boolean getGoodsLimitByGidsAndBuyerEid(List<Long> gidList, Long buyerEid) {
        if (CollUtil.isEmpty(gidList)) {
            return false;
        }
        // 未登录
        if (buyerEid == null || buyerEid.intValue() == 0) {
            return false;
        }

        // 默认正常
        for (Long gid : gidList) {
            //查询是否在协议内
            AgreementPurchaseGoodsRequest request = new AgreementPurchaseGoodsRequest();
            request.setPurchaseEid(buyerEid);
            request.setGoodsId(gid);
            List<AgreementGoodsDTO> list = agreementGoodsApi.getTempPurchaseGoodsList(request);
            if (CollUtil.isEmpty(list)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<AgreementCalculateResultDTO> calculateCashAgreement(AgreementCalculateRequest request) {
        if (request.getBuyerEid() == null || request.getBuyerEid() == 0) {
            throw new BusinessException(ResultCode.PARAM_MISS);
        }

        List<AgreementCalculateRequest.AgreementCalculateDetailRequest> goodsList = request.getAgreementCalculateDetailList();
        if (CollUtil.isEmpty(goodsList)) {
            throw new BusinessException(ResultCode.PARAM_MISS);
        }

        //1判断采购商是不是在协议里面
        AgreementPurchaseGoodsRequest agreementPurchaseGoodsRequest = new AgreementPurchaseGoodsRequest();
        agreementPurchaseGoodsRequest.setPurchaseEid(request.getBuyerEid());
        agreementPurchaseGoodsRequest.setDistributionEid(request.getDistributorEid());
        agreementPurchaseGoodsRequest.setRebateCycle(AgreementRebateCycleEnum.IMMEDIATELY_REBATE.getCode());
        List<AgreementGoodsDTO> agreementGoodsDTOList = agreementGoodsApi.getTempPurchaseGoodsByDistributionList(agreementPurchaseGoodsRequest);
        if (CollUtil.isEmpty(agreementGoodsDTOList)) {
            return ListUtil.empty();
        }
        //转化成商品Id对应协议list
        Map<Long, List<AgreementGoodsDTO>> agreementMap = new HashMap<>();
        for (AgreementGoodsDTO agreementGoodsDTO : agreementGoodsDTOList) {
            if (agreementMap.containsKey(agreementGoodsDTO.getGoodsId())) {
                agreementMap.get(agreementGoodsDTO.getGoodsId()).add(agreementGoodsDTO);
            } else {
                List<AgreementGoodsDTO> agreementGoodsList = new ArrayList<>();
                agreementGoodsList.add(agreementGoodsDTO);
                agreementMap.put(agreementGoodsDTO.getGoodsId(), agreementGoodsList);
            }
        }

        //转化成协议对应协议list
        Map<Long, List<AgreementGoodsDTO>> agreementIdMap = new HashMap<>();
        for (AgreementGoodsDTO agreementGoodsDTO : agreementGoodsDTOList) {
            if (agreementIdMap.containsKey(agreementGoodsDTO.getAgreementId())) {
                agreementIdMap.get(agreementGoodsDTO.getAgreementId()).add(agreementGoodsDTO);
            } else {
                List<AgreementGoodsDTO> agreementGoodsList = new ArrayList<>();
                agreementGoodsList.add(agreementGoodsDTO);
                agreementIdMap.put(agreementGoodsDTO.getAgreementId(), agreementGoodsList);
            }
        }

        //2取出所有的采购商对应的协议商品集合
        List<AgreementGoodsDTO> agreementGoodsList = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        Long goodsQuantity = 0L;
        Map<Long, BigDecimal> goodsAmountMap = new HashMap<>();
        for (AgreementCalculateRequest.AgreementCalculateDetailRequest agreementCalculateDetailRequest : goodsList) {
            //有协议商品存在
            if (agreementMap.containsKey(agreementCalculateDetailRequest.getGoodsId())) {
                agreementGoodsList.addAll(agreementMap.get(agreementCalculateDetailRequest.getGoodsId()));
                totalAmount = totalAmount.add(agreementCalculateDetailRequest.getGoodsAmount());
                goodsQuantity = goodsQuantity + agreementCalculateDetailRequest.getGoodsQuantity();
                goodsAmountMap.put(agreementCalculateDetailRequest.getGoodsId(), agreementCalculateDetailRequest.getGoodsAmount());
            }
        }
        if (CollUtil.isEmpty(agreementGoodsList)) {
            return ListUtil.empty();
        }

        //3取出协议的条件
        List<Long> agreementIds = agreementGoodsList.stream().map(e -> e.getAgreementId()).distinct().collect(Collectors.toList());
        List<SupplementAgreementDetailDTO> agreementDetailDTOList = agreementApi.querySupplementAgreementsDetailList(agreementIds);
        if (CollUtil.isEmpty(agreementDetailDTOList)) {
            return ListUtil.empty();
        }

        //4判断金额是否满足条件
        List<AgreementCalculateResultDTO> resultAgreementCalculateResultDTOList = new ArrayList<>();
        //循环协议
        for (SupplementAgreementDetailDTO supplementAgreementDetailDTO : agreementDetailDTOList) {
            //返利周期
            if (supplementAgreementDetailDTO.getRebateCycle().equals(AgreementRebateCycleEnum.IMMEDIATELY_REBATE.getCode())) {
                List<AgreementConditionDTO> agreementConditionDTOList = supplementAgreementDetailDTO.getAgreementsConditionList();
                if (CollUtil.isEmpty(agreementConditionDTOList)) {
                    continue;
                }
                AgreementConditionDTO agreementConditionDTO = agreementConditionDTOList.get(0);
                //直接返利
                if (Arrays.asList(AgreementChildTypeEnum.DATA_LINK.getCode(), AgreementChildTypeEnum.DAMAGED_REBATE.getCode()).contains(supplementAgreementDetailDTO.getChildType()) && AgreementConditionRuleEnum.ORDER_AMOUNT.getCode().equals(supplementAgreementDetailDTO.getConditionRule())) {
                    for (Map.Entry<Long, BigDecimal> entry : goodsAmountMap.entrySet()) {
                        AgreementCalculateResultDTO agreementCalculateResultDTO = new AgreementCalculateResultDTO();
                        agreementCalculateResultDTO.setAgreementId(supplementAgreementDetailDTO.getId());
                        agreementCalculateResultDTO.setVersion(supplementAgreementDetailDTO.getVersion());
                        agreementCalculateResultDTO.setDiscountAmount(agreementConditionDTO.getPolicyValue().divide(new BigDecimal(100)).multiply(entry.getValue()).setScale(2, BigDecimal.ROUND_HALF_UP));
                        agreementCalculateResultDTO.setGoodsAmount(entry.getValue());
                        agreementCalculateResultDTO.setGoodsId(entry.getKey());
                        agreementCalculateResultDTO.setPolicyValue(agreementConditionDTO.getPolicyValue().setScale(2, BigDecimal.ROUND_HALF_UP));
                        resultAgreementCalculateResultDTOList.add(agreementCalculateResultDTO);
                    }
                } else if (Arrays.asList(AgreementChildTypeEnum.PURCHASE_AMOUNT.getCode(), AgreementChildTypeEnum.PURCHASE_CAPACITY.getCode()).contains(supplementAgreementDetailDTO.getChildType()) && AgreementConditionRuleEnum.TOTAL_AMOUNT.getCode().equals(supplementAgreementDetailDTO.getConditionRule())) {
                    BigDecimal compareValue = BigDecimal.ZERO;
                    if (AgreementChildTypeEnum.PURCHASE_AMOUNT.getCode().equals(supplementAgreementDetailDTO.getChildType())) {
                        compareValue = totalAmount;
                    } else {
                        compareValue = new BigDecimal(goodsQuantity);
                    }
                    //判断金额条件
                    if (agreementConditionDTO.getAmount().compareTo(compareValue) > 0) {
                        continue;
                    }
                    //判断支付方式
                    if (agreementConditionDTO.getPayType().equals(1) && !agreementConditionDTO.getPayTypeValues().contains(request.getPaymentMethod())) {
                        continue;
                    }
                    //回款形式
                    if (agreementConditionDTO.getBackAmountType().equals(1)) {
                        continue;
                    }
                    for (Map.Entry<Long, BigDecimal> entry : goodsAmountMap.entrySet()) {
                        //需要判断商品是否在这个协议下面
                        List<AgreementGoodsDTO> goodsDTOS = agreementIdMap.get(supplementAgreementDetailDTO.getId());
                        List<Long> goodsIdList = goodsDTOS.stream().map(e -> e.getGoodsId()).collect(Collectors.toList());
                        if (!goodsIdList.contains(entry.getKey())) {
                            continue;
                        }
                        AgreementCalculateResultDTO agreementCalculateResultDTO = new AgreementCalculateResultDTO();
                        agreementCalculateResultDTO.setAgreementId(supplementAgreementDetailDTO.getId());
                        agreementCalculateResultDTO.setVersion(supplementAgreementDetailDTO.getVersion());
                        agreementCalculateResultDTO.setDiscountAmount(agreementConditionDTO.getPolicyValue().multiply(entry.getValue()).divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP));
                        agreementCalculateResultDTO.setGoodsId(entry.getKey());
                        agreementCalculateResultDTO.setGoodsAmount(entry.getValue());
                        agreementCalculateResultDTO.setPolicyValue(agreementConditionDTO.getPolicyValue().setScale(2, BigDecimal.ROUND_HALF_UP));
                        resultAgreementCalculateResultDTOList.add(agreementCalculateResultDTO);
                    }
                }
            }
        }
        //5返回返利金额
        return resultAgreementCalculateResultDTOList;
    }

    @Override
    public Boolean calculateRebateAgreementByDay() {
        //找出现在已经开始的协议集合和协议商品集合
        List<SupplementAgreementDetailDTO> supplementAgreementDetailDTOList = agreementApi.queryStartSupplementAgreementsDetail();
        List<Long> agreementIds = supplementAgreementDetailDTOList.stream().filter(e -> e.getRebateCycle().equals(2)).map(e -> e.getId()).collect(Collectors.toList());
        Map<Long, List<AgreementRebateOrderDTO>> agreementOrderMap = getAgreementRebateOrderListMap(agreementIds, null);

        //3把协议商品订单明细和订单协议插入到协议兑付表里面
        {
            for (SupplementAgreementDetailDTO supplementAgreementDetailDTO : supplementAgreementDetailDTOList) {
                Long buyEid = supplementAgreementDetailDTO.getThirdEid();

                //获取企业的eas账号,如果协议是三方协议不需要统计传编码
                Map<Long, List<EnterpriseCustomerEasDTO>> accountDTOList = customerApi.listCustomerEasInfos(Constants.YILING_EID, Arrays.asList(buyEid));
                if (CollUtil.isEmpty(accountDTOList)) {
                    continue;
                }
                for (List<EnterpriseCustomerEasDTO> list : accountDTOList.values()) {
                    for (EnterpriseCustomerEasDTO enterpriseCustomerEasDTO : list) {
                        Date calculateTime = new Date();
                        if (supplementAgreementDetailDTO.getStatus().equals(AgreementStatusEnum.CLOSE.getCode())) {
                            supplementAgreementDetailDTO.setEndTime(supplementAgreementDetailDTO.getStopTime());
                        }
                        if (supplementAgreementDetailDTO.getEndTime().getTime() < calculateTime.getTime()) {
                            calculateTime = supplementAgreementDetailDTO.getEndTime();
                        }
                        List<AgreementGoodsDTO> agreementGoodsDTOList = agreementGoodsApi.getAgreementGoodsAgreementIdByList(supplementAgreementDetailDTO.getId());
//                        List<AgreementGoodsDTO> agreementGoodsDTOList = agreementGoodsDTOMap.get(supplementAgreementDetailDTO.getId());
                        List<Long> goodsIds = agreementGoodsDTOList.stream().map(e -> e.getGoodsId()).collect(Collectors.toList());
                        List<AgreementRebateOrderDTO> agreementOrderList = agreementOrderMap.getOrDefault(supplementAgreementDetailDTO.getId(), ListUtil.empty());
                        agreementOrderList = agreementOrderList.stream().filter(e -> e.getEasAccount().equals(enterpriseCustomerEasDTO.getEasCode())).collect(Collectors.toList());

                        String easCode = "";
                        if (supplementAgreementDetailDTO.getMode().equals(AgreementModeEnum.SECOND_AGREEMENTS.getCode())) {
                            easCode = enterpriseCustomerEasDTO.getEasCode();
                        }

                        prepareCalculateAgreement(supplementAgreementDetailDTO, goodsIds, agreementOrderList, calculateTime, easCode, 0L);
                        calculateAgreement(supplementAgreementDetailDTO, calculateTime, Arrays.asList(DateUtil.format(new Date(), "yyyy-MM")), easCode, buyEid, 0L);
                    }
                }
            }
        }
        return true;
    }

    /**
     * 查询已经插入的订单信息
     *
     * @param agreementIds
     * @return
     */
    private Map<Long, List<AgreementRebateOrderDTO>> getAgreementRebateOrderListMap(List<Long> agreementIds, String easAccount) {
        //找出已经加入到计算表里面的订单信息
        QueryAgreementRebateRequest request = new QueryAgreementRebateRequest();
        request.setAgreementIds(agreementIds);
//        request.setCashStatus(1);
        request.setEasAccount(easAccount);
        List<AgreementRebateOrderDTO> agreementRebateOrderDTOList = agreementRebateOrderApi.getAgreementRebateOrderListByAgreementIds(request);
        Map<Long, List<AgreementRebateOrderDTO>> agreementOrderMap = new HashMap<>();
        for (AgreementRebateOrderDTO agreementRebateOrderDTO : agreementRebateOrderDTOList) {
            if (agreementOrderMap.containsKey(agreementRebateOrderDTO.getAgreementId())) {
                agreementOrderMap.get(agreementRebateOrderDTO.getAgreementId()).add(agreementRebateOrderDTO);
            } else {
                List<AgreementRebateOrderDTO> dtoList = new ArrayList<>();
                dtoList.add(agreementRebateOrderDTO);
                agreementOrderMap.put(agreementRebateOrderDTO.getAgreementId(), dtoList);
            }
        }
        return agreementOrderMap;
    }

    /**
     * 通过页面触发计算协议返回金额
     *
     * @param request
     * @return
     */
    @Override
    public Boolean calculateRebateAgreementByAgreementId(CashMallAgreementRequest request) {
        Long agreementId = request.getAgreementId();
        //获取协议信息
        SupplementAgreementDetailDTO supplementAgreementDetailDTO = agreementApi.querySupplementAgreementsDetail(agreementId);
        if (supplementAgreementDetailDTO.getRebateCycle().equals(AgreementRebateCycleEnum.IMMEDIATELY_REBATE.getCode())) {
            return false;
        }

        Long buyEid = supplementAgreementDetailDTO.getThirdEid();

        //计算的时间点
        Date calculateTime = new Date();
        if (supplementAgreementDetailDTO.getStatus().equals(AgreementStatusEnum.CLOSE.getCode())) {
            supplementAgreementDetailDTO.setEndTime(supplementAgreementDetailDTO.getStopTime());
        }
        if (supplementAgreementDetailDTO.getEndTime().getTime() < calculateTime.getTime()) {
            calculateTime = supplementAgreementDetailDTO.getEndTime();
        }
        //协议商品信息
        Map<Long, List<AgreementGoodsDTO>> agreementGoodsDTOMap = agreementGoodsApi.getAgreementGoodsListByAgreementIds(Arrays.asList(agreementId));
        List<AgreementGoodsDTO> agreementGoodsDTOList = agreementGoodsDTOMap.get(supplementAgreementDetailDTO.getId());
        List<Long> goodsIds = agreementGoodsDTOList.stream().map(e -> e.getGoodsId()).collect(Collectors.toList());
        //获取已经插入数据库的协议订单和协议退货单
        Map<Long, List<AgreementRebateOrderDTO>> agreementOrderMap = getAgreementRebateOrderListMap(Arrays.asList(agreementId), request.getEasAccount());
        List<AgreementRebateOrderDTO> agreementOrderList = agreementOrderMap.get(supplementAgreementDetailDTO.getId());
        //计算之前的数据准备
        prepareCalculateAgreement(supplementAgreementDetailDTO, goodsIds, agreementOrderList, calculateTime, request.getEasAccount(), request.getOpUserId());
        //计算返利值
        return calculateAgreement(supplementAgreementDetailDTO, calculateTime, request.getTimeRange(), request.getEasAccount(), buyEid, request.getOpUserId());
    }

    @Override
    public Boolean cashAgreementByAgreementId(CashMallAgreementRequest request) {
        AgreementDTO agreementDTO = agreementApi.getAgreementDetailsInfo(request.getAgreementId());
        //2修改兑付次数,修改状态，兑付时间,待反金额清零,计算协议条件ID清零,添加兑付日志
        CashAgreementRequest cashAgreementRequest = new CashAgreementRequest();
        cashAgreementRequest.setTimeRange(request.getTimeRange());
        cashAgreementRequest.setAgreementId(request.getAgreementId());
        cashAgreementRequest.setAgreementName(agreementDTO.getName());
        cashAgreementRequest.setVersion(agreementDTO.getVersion());
        cashAgreementRequest.setOpUserId(request.getOpUserId());
        cashAgreementRequest.setEasAccount(request.getEasAccount());
        agreementRebateOrderApi.cashAgreementByAgreementId(cashAgreementRequest);
        return true;
    }

    @Override
    @GlobalTransactional
    public CalculateRebateApplyDTO calculateRebateApply(CalculateRebateApplyRequest rebateApplyRequest) {
        //协议列表
        List<CalculateRebateApplyDTO.AgreementDetail> agreementDetailList = ListUtil.toList();
        //订单明细列表
        List<CalculateRebateApplyDTO.OrderDetail> applyOrderDetailList = ListUtil.toList();
        //订单列表
        List<CalculateRebateApplyDTO.Order> applyOrderList = ListUtil.toList();
        BigDecimal totalAmount = BigDecimal.ZERO;
        CalculateRebateApplyDTO result = new CalculateRebateApplyDTO();
        result.setAgreementDetailList(agreementDetailList);
        result.setOrderList(applyOrderList);
        result.setOrderDetailList(applyOrderDetailList);

        //计算申请的时间起止
        Date startDate = rebateApplyRequest.getStartDate();
        Date endDate = rebateApplyRequest.getEndDate();

        //设置时间范围
        List<String> timeRange = ListUtil.toList();
        Integer startDateStr = Integer.valueOf(DateUtil.format(startDate, "MM"));
        Integer endDateStr = Integer.valueOf(DateUtil.format(endDate, "MM"));
        for (int i = 0; i <= endDateStr - startDateStr; i++) {
            DateTime dateTime = DateUtil.offsetMonth(startDate, i);
            timeRange.add(DateUtil.format(dateTime, "yyyy-MM"));
        }

        //协议列表
        List<AgreementDTO> agreementList;
        //协议map
        Map<Long, SupplementAgreementDetailDTO> agreementDTOMap;
        //根据开始时间查询协议
        agreementList = agreementApi.queryAgreementListByBuyerEid(rebateApplyRequest.getEid());
        //查询开始时间小于等于时间段的
        agreementList = agreementList.stream()
                .filter(agreementDTO -> DateUtil.compare(agreementDTO.getStartTime(), endDate) == -1 || DateUtil.compare(agreementDTO.getStartTime(), endDate) == 0)
                .collect(Collectors.toList());
        //过滤掉直接返利
        agreementList = agreementList.stream().filter(agreementDTO -> agreementDTO.getRebateCycle().equals(2)).collect(Collectors.toList());
        agreementDTOMap = agreementApi.querySupplementAgreementsDetailList(agreementList.stream().map(AgreementDTO::getId).collect(Collectors.toList()))
                .stream().collect(Collectors.toMap(SupplementAgreementDetailDTO::getId, e -> e));
        if (CollUtil.isEmpty(agreementList)) {
            return result;
        }
        //调用计算接口
        for (AgreementDTO agreementDTO : agreementList) {
            CashMallAgreementRequest request = new CashMallAgreementRequest();
            //判断是否传入一级商
            if (rebateApplyRequest.getInputEntry().equals(1)) {
                request.setEasAccount("");
            } else {
                request.setEasAccount(rebateApplyRequest.getEasCode());
            }
            request.setAgreementId(agreementDTO.getId());
            request.setTimeRange(timeRange);
            request.setOpUserId(rebateApplyRequest.getOpUserId());
            calculateRebateAgreementByAgreementId(request);
        }

        //查询返利订单表
        QueryAgreementRebateRequest queryAgreementRebateRequest = new QueryAgreementRebateRequest();
        queryAgreementRebateRequest.setAgreementIds(agreementList.stream().map(AgreementDTO::getId).collect(Collectors.toList()));
        queryAgreementRebateRequest.setConditionStatus(AgreementRebateOrderConditionStatusEnum.PASS.getCode());
        queryAgreementRebateRequest.setCashStatus(AgreementRebateOrderCashStatusEnum.CALCULATE.getCode());
        //判断是否传入一级商
        if (rebateApplyRequest.getInputEntry().equals(1)) {
            queryAgreementRebateRequest.setEasAccount("");
        } else {
            queryAgreementRebateRequest.setEasAccount(rebateApplyRequest.getEasCode());
        }

        queryAgreementRebateRequest.setComparisonTimes(timeRange);
        List<AgreementRebateOrderDTO> rebateOrderList = agreementRebateOrderApi.getAgreementRebateOrderListByAgreementIds(queryAgreementRebateRequest);
        //如果没有返利订单
        if (CollUtil.isEmpty(rebateOrderList)) {
            return result;
        }
        //协议下的订单map，key=协议id
        Map<Long, List<AgreementRebateOrderDTO>> rebateOrderMap = rebateOrderList.stream().collect(Collectors.groupingBy(AgreementRebateOrderDTO::getAgreementId));

        //查询订单编号
        Map<Long, String> orderMap = orderApi.listByIds(rebateOrderList.stream().map(AgreementRebateOrderDTO::getOrderId)
                .collect(Collectors.toList()).stream().distinct().collect(Collectors.toList()))
                .stream().collect(Collectors.toMap(OrderDTO::getId, OrderDTO::getOrderNo));
        //查询返利订单明细
        List<AgreementRebateOrderDetailDTO> rebateOrderDetailDTOS = ListUtil.toList();

        //查询协议下订单明细
        rebateOrderMap.forEach((agreementId, value) -> {
            QueryRebateOrderDetailRequest detailRequest = new QueryRebateOrderDetailRequest();
            detailRequest.setConditionStatus(AgreementRebateOrderConditionStatusEnum.PASS.getCode());
            detailRequest.setCashStatus(AgreementRebateOrderCashStatusEnum.CALCULATE.getCode());
            detailRequest.setComparisonTimes(timeRange);
            //判断是否传入一级商
            if (rebateApplyRequest.getInputEntry().equals(1)) {
                detailRequest.setEasCode("");
            } else {
                detailRequest.setEasCode(rebateApplyRequest.getEasCode());
            }
            detailRequest.setSecondEid(rebateApplyRequest.getEid());
            detailRequest.setOrderIdList(value.stream().map(AgreementRebateOrderDTO::getOrderId).collect(Collectors.toList()));
            detailRequest.setAgreementId(agreementId);
            List<AgreementRebateOrderDetailDTO> details = agreementRebateOrderDetailApi.queryAgreementRebateOrderDetailList(detailRequest);
            rebateOrderDetailDTOS.addAll(details);
            Map<Long, List<AgreementRebateOrderDetailDTO>> orderDetail = details.stream().collect(Collectors.groupingBy(AgreementRebateOrderDetailDTO::getRebateOrderId));
            //生成返利订单
            orderDetail.forEach((rebateOrderId, rebateOrderDetails) -> {
                List<CalculateRebateApplyDTO.Order> detailList = PojoUtils.map(rebateOrderDetails, CalculateRebateApplyDTO.Order.class);
                for (CalculateRebateApplyDTO.Order item : detailList) {
                    item.setRebateOrderId(rebateOrderId);
                    item.setRebateOrderDetailId(CollUtil.join(rebateOrderDetails.stream().map(AgreementRebateOrderDetailDTO::getId).collect(Collectors.toList()), ","));
                    //补全订单号
                    item.setOrderCode(orderMap.getOrDefault(item.getOrderId(), ""));
                    applyOrderList.add(item);
                }
            });
        });

        //查询销售组织easCode
        List<Long> eids = agreementList.stream().map(AgreementDTO::getEid).distinct().collect(Collectors.toList());
        Map<Long, String> easCodeMap = enterpriseApi.listByIds(eids).stream().collect(Collectors.toMap(EnterpriseDTO::getId, EnterpriseDTO::getErpCode));
        //组装数据
        //设置申请单明细
        for (AgreementDTO e : agreementList) {
            List<AgreementRebateOrderDTO> orderList = rebateOrderMap.get(e.getId());
            if (CollUtil.isNotEmpty(orderList)) {
                CalculateRebateApplyDTO.AgreementDetail agreementDetail = new CalculateRebateApplyDTO.AgreementDetail();
                //订单数量
                agreementDetail.setOrderCount(orderList.stream().collect(Collectors.groupingBy(AgreementRebateOrderDTO::getOrderId)).size());
                PojoUtils.map(e, agreementDetail);
                agreementDetail.setId(e.getId());
                BigDecimal amount = BigDecimal.ZERO;
                for (AgreementRebateOrderDTO order : orderList) {
                    BigDecimal discountAmount = order.getDiscountAmount().setScale(2, BigDecimal.ROUND_HALF_UP);
                    if (RebateOrderTypeEnum.REFUND.getCode().equals(order.getType())) {
                        discountAmount = discountAmount.negate();
                    }
                    amount = amount.add(discountAmount);
                }
                //设置返利金额
                agreementDetail.setAmount(amount);
                //设置销售组织
                agreementDetail.setSellerName(e.getEname());
                agreementDetail.setSellerEid(e.getEid());
                agreementDetail.setSellerCode(easCodeMap.getOrDefault(e.getEid(), ""));
                SupplementAgreementDetailDTO agreementDTO = agreementDTOMap.get(e.getId());
                agreementDetail.setContent(AgreementUtils.getAgreementText(agreementDTO));
                agreementDetailList.add(agreementDetail);
                totalAmount = totalAmount.add(amount);
            }
        }
        //设置商品明细
        rebateOrderDetailDTOS.forEach(e -> {
            OrderDetailDTO orderDetailDTO = orderDetailApi.getOrderDetailById(e.getOrderDetailId());
            SupplementAgreementDetailDTO agreementDTO = agreementDTOMap.get(e.getAgreementId());
            CalculateRebateApplyDTO.OrderDetail detail = PojoUtils.map(e, CalculateRebateApplyDTO.OrderDetail.class);
            detail.setId(agreementDTO.getId());
            detail.setName(agreementDTO.getName());
            detail.setContent(AgreementUtils.getAgreementText(agreementDTO));
            detail.setEasCode(e.getEasAccount());
            detail.setGoodsId(e.getGoodsId());
            detail.setGoodsName(orderDetailDTO.getGoodsName());
            detail.setErpCode(orderDetailDTO.getGoodsErpCode());
            detail.setPrice(e.getGoodsAmount());
            BigDecimal discountAmount = e.getDiscountAmount().setScale(2, BigDecimal.ROUND_HALF_UP);
            //如果是退款单转为负数
            if (RebateOrderTypeEnum.REFUND.getCode().equals(e.getType())) {
                detail.setDiscountAmount(discountAmount.negate());
            } else {
                detail.setDiscountAmount(discountAmount);
            }
            //补全订单号
            detail.setOrderCode(orderMap.getOrDefault(e.getOrderId(), ""));
            applyOrderDetailList.add(detail);
        });
        result.setTotalAmount(totalAmount);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveRebateApply(SaveRebateApplyRequest request) {
        CalculateRebateApplyRequest calculateRebateApplyForm = PojoUtils.map(request, CalculateRebateApplyRequest.class);

        //新增返利申请request
        AddRebateApplyRequest applyRequest = PojoUtils.map(request, AddRebateApplyRequest.class);
        //新增申请明细request
        List<AddRebateApplyDetailRequest> detailList = ListUtil.toList();
        //新增申请订单
        List<AddApplyOrderRequest> orderList = ListUtil.toList();
        //申请总额
        BigDecimal totalAmount = BigDecimal.ZERO;
        applyRequest.setStatus(AgreementApplyStatusEnum.CHECK.getCode());
        applyRequest.setOpUserId(request.getOpUserId());
        applyRequest.setCode(noApi.gen(NoEnum.AGREEMENT_APPLY_NO));

        //点击了计算按钮
        if (request.getCalculateStatus().equals(1)) {
            //计算返利订单
            CalculateRebateApplyDTO apply = calculateRebateApply(calculateRebateApplyForm);
            totalAmount = apply.getTotalAmount();
            //设置申请单下的协议
            apply.getAgreementDetailList().forEach(e -> {
                AddRebateApplyDetailRequest detailRequest = PojoUtils.map(e, AddRebateApplyDetailRequest.class);
                detailRequest.setDetailType(ApplyDetailTypeEnum.AGREEMENT.getCode());
                detailRequest.setAgreementId(e.getId());
                detailList.add(detailRequest);
            });
            //如果协议下没有订单
            if (CollUtil.isEmpty(apply.getOrderDetailList())) {
                throw new BusinessException(AgreementErrorCode.AGREEMENT_REBATE_NOT_ORDER);
            }
            //设置协议下订单及明细关系
            apply.getOrderList().forEach(e -> {
                AddApplyOrderRequest orderRequest = PojoUtils.map(e, AddApplyOrderRequest.class);
                orderRequest.setEasCode(e.getEasAccount());
                orderList.add(orderRequest);
            });
        }
        //如果有其他返利
        if (CollUtil.isNotEmpty(request.getApplyDetails())) {
            List<SaveRebateApplyRequest.RebateApplyDetail> applyDetails = request.getApplyDetails();
            for (SaveRebateApplyRequest.RebateApplyDetail item : applyDetails) {
                totalAmount = totalAmount.add(item.getAmount());
                AddRebateApplyDetailRequest detailRequest = PojoUtils.map(item, AddRebateApplyDetailRequest.class);
                detailRequest.setDetailType(ApplyDetailTypeEnum.OTHER.getCode());
                detailList.add(detailRequest);
            }
        }
        //设置申请总额
        applyRequest.setTotalAmount(totalAmount);
        if (BigDecimal.ZERO.compareTo(totalAmount) > -1) {
            throw new BusinessException(AgreementErrorCode.AGREEMENT_APPLY_SAVE);
        }
        //对比申请单金额与当前计算金额
        if (request.getTotalAmount().compareTo(applyRequest.getTotalAmount()) != 0) {
            throw new BusinessException(AgreementErrorCode.AGREEMENT_APPLY_SAVE_AMOUNT);
        }
        //保存申请单
        AgreementRebateApplyDTO applyDTO = applyApi.save(applyRequest);

        //计算申请的时间起止
        Date startDate = request.getStartDate();
        Date endDate = request.getEndDate();

        //设置时间范围
        List<String> timeRange = ListUtil.toList();
        Integer startDateStr = Integer.valueOf(DateUtil.format(startDate, "MM"));
        Integer endDateStr = Integer.valueOf(DateUtil.format(endDate, "MM"));
        for (int i = 0; i <= endDateStr - startDateStr; i++) {
            DateTime dateTime = DateUtil.offsetMonth(startDate, i);
            timeRange.add(DateUtil.format(dateTime, "yyyy-MM"));
        }

        //新增返利申请明细
        //查询销售组织信息
        List<Long> sellerEids = detailList.stream().map(AddRebateApplyDetailRequest::getSellerEid).collect(Collectors.toList());
        Map<Long, String> erpCodeMap = enterpriseApi.listByIds(sellerEids).stream().collect(Collectors.toMap(EnterpriseDTO::getId, EnterpriseDTO::getErpCode));
        detailList.forEach(e -> {
            e.setApplyId(applyDTO.getId());
            e.setApplyCode(applyDTO.getCode());
            e.setSellerCode(erpCodeMap.get(e.getSellerEid()));
            e.setOpUserId(request.getOpUserId());
            if (ApplyDetailTypeEnum.AGREEMENT.getCode().equals(e.getDetailType())) {
                CashMallAgreementRequest agreementRequest = new CashMallAgreementRequest();
                agreementRequest.setAgreementId(e.getAgreementId());
                agreementRequest.setEasAccount(request.getEasCode());
                agreementRequest.setTimeRange(timeRange);
                cashAgreementByAgreementId(agreementRequest);
            }
        });
        //新增申请订单
        orderList.forEach(e -> {
            e.setApplyId(applyDTO.getId());
            e.setApplyCode(applyDTO.getCode());
        });
        applyOrderApi.batchSaveOrUpdate(orderList);
        Boolean isSuccess = applyDetailApi.batchSave(detailList);
        if (!isSuccess) {
            throw new BusinessException(AgreementErrorCode.AGREEMENT_REBATE_DETAIL);
        }
        //更新eas表兑付额度
        customerApi.updateAppliedAmount(request.getEid(), request.getEasCode(), applyDTO.getTotalAmount());
        //插入日志
        List<AddRebateLogRequest> logRequestList = ListUtil.toList();
        detailList.forEach(e -> {
            AddRebateLogRequest logRequest = new AddRebateLogRequest();
            logRequest.setDiscountAmount(e.getAmount());
            logRequest.setEasCode(request.getEasCode());
            //协议类型
            if (ApplyDetailTypeEnum.AGREEMENT.getCode().equals(e.getDetailType())) {
                logRequest.setAgreementId(e.getAgreementId());
                logRequest.setVersion(e.getVersion());
                logRequest.setCashType(ApplyDetailTypeEnum.AGREEMENT.getCode());
            } else {
                //其他类型
                logRequest.setCashType(ApplyDetailTypeEnum.OTHER.getCode());
                logRequest.setLogName(e.getEntryDescribe());
            }
            logRequest.setOpUserId(request.getOpUserId());
            logRequestList.add(logRequest);
        });
        isSuccess = agreementRebateLogApi.batchSave(logRequestList);
        return isSuccess;
    }

    //只插入有协议商品的订单信息
    public boolean prepareCalculateAgreement(SupplementAgreementDetailDTO supplementAgreementDetailDTO, List<Long> goodsIds, List<AgreementRebateOrderDTO> agreementOrderList, Date calculateTime, String easAccount, Long opUserId) {
        if (CollUtil.isEmpty(agreementOrderList)) {
            agreementOrderList = ListUtil.empty();
        }

        List<AgreementRebateOrderRequest> agreementRebateOrderRequestList = new ArrayList<>();
        Set<Long> goodsIdList = new HashSet<>();
        Long distributorEid = supplementAgreementDetailDTO.getSecondEid();
        Long buyEid = supplementAgreementDetailDTO.getThirdEid();


        List<Long> orderIds = agreementOrderList.stream().filter(e -> e.getOrderId() > 0).map(e -> e.getOrderId()).collect(Collectors.toList());
        List<Long> returnIds = agreementOrderList.stream().filter(e -> e.getReturnId() > 0).map(e -> e.getReturnId()).collect(Collectors.toList());
        List<Long> settlementIds = agreementOrderList.stream().filter(e -> e.getSettlementId() > 0).map(e -> e.getSettlementId()).collect(Collectors.toList());

        QueryOrderUseAgreementRequest request = new QueryOrderUseAgreementRequest();
        request.setBuyerEids(Arrays.asList(buyEid));
        request.setEndCreateTime(calculateTime);
        request.setDistributorEid(distributorEid);
        request.setStartCreateTime(supplementAgreementDetailDTO.getStartTime());
        request.setEasAccount(easAccount);

        if (supplementAgreementDetailDTO.getChildType().equals(1) || supplementAgreementDetailDTO.getChildType().equals(2)) {
            List<OrderAndDetailedAllInfoDTO> orderList = orderApi.getOrderAllInfoByBuyerEidAndCreateTime(request);

            for (OrderAndDetailedAllInfoDTO orderAndDetailedAllInfoDTO : orderList) {
                if (orderIds.contains(orderAndDetailedAllInfoDTO.getId())) {
                    continue;
                }
                AgreementRebateOrderRequest orderRequest = new AgreementRebateOrderRequest();
                List<AgreementRebateOrderRequest.AgreementRebateOrderDetailRequest> agreementRebateOrderDetailRequestList = new ArrayList<>();
                BigDecimal totalAmount = BigDecimal.ZERO;
                Long quantity = 0L;
                for (OrderDetailByAgreementDTO orderDetailDTO : orderAndDetailedAllInfoDTO.getDetailLists()) {
                    //判断订单明细是否在协议商品里面
                    if (goodsIds.contains(orderDetailDTO.getGoodsId())) {
                        AgreementRebateOrderRequest.AgreementRebateOrderDetailRequest goodsRequest = new AgreementRebateOrderRequest.AgreementRebateOrderDetailRequest();
                        goodsRequest.setAgreementId(supplementAgreementDetailDTO.getId());
                        goodsRequest.setEid(supplementAgreementDetailDTO.getEid());
                        goodsRequest.setOrderDetailId(orderDetailDTO.getId());
                        goodsRequest.setGoodsId(orderDetailDTO.getGoodsId());
                        goodsRequest.setComparisonTime(orderAndDetailedAllInfoDTO.getCreateTime());
                        goodsRequest.setVersion(supplementAgreementDetailDTO.getVersion());
                        goodsRequest.setEasAccount(orderAndDetailedAllInfoDTO.getCustomerErpCode());
                        goodsRequest.setType(1);
                        goodsRequest.setSecondEid(buyEid);
                        goodsRequest.setOrderId(orderDetailDTO.getOrderId());
                        goodsRequest.setGoodsAmount(orderDetailDTO.getDeliveryAmount());
                        goodsRequest.setGoodsQuantity(orderDetailDTO.getDeliveryQuantity().longValue());
                        orderRequest.setOpUserId(opUserId);
                        agreementRebateOrderDetailRequestList.add(goodsRequest);
                        totalAmount = totalAmount.add(orderDetailDTO.getDeliveryAmount());
                        quantity = quantity + orderDetailDTO.getDeliveryQuantity();
                    }
                }
                //如果明细一个商品都没有不插入订单信息
                if (CollUtil.isNotEmpty(agreementRebateOrderDetailRequestList)) {
                    orderRequest.setAgreementId(supplementAgreementDetailDTO.getId());
                    orderRequest.setVersion(supplementAgreementDetailDTO.getVersion());
                    orderRequest.setEid(supplementAgreementDetailDTO.getEid());
                    orderRequest.setSecondEid(buyEid);
                    orderRequest.setOrderId(orderAndDetailedAllInfoDTO.getId());
                    orderRequest.setType(1);
                    orderRequest.setPaymentMethod(orderAndDetailedAllInfoDTO.getPaymentMethod());
                    orderRequest.setEasAccount(orderAndDetailedAllInfoDTO.getCustomerErpCode());
                    orderRequest.setGoodsAmount(totalAmount);
                    orderRequest.setGoodsQuantity(quantity);
                    orderRequest.setComparisonTime(orderAndDetailedAllInfoDTO.getCreateTime());
                    orderRequest.setOpUserId(opUserId);
                    orderRequest.setAgreementRebateOrderDetailList(agreementRebateOrderDetailRequestList);
                    agreementRebateOrderRequestList.add(orderRequest);
                }
            }
        } else if (supplementAgreementDetailDTO.getChildType().equals(3)) {
            //回款金额
            List<SettlementFullDTO> settlementFullDTOList = settlementApi.getSettlementDetailByEidAndTime(request);
            for (SettlementFullDTO settlementFullDTO : settlementFullDTOList) {
                if (settlementIds.contains(settlementFullDTO.getId())) {
                    continue;
                }
                AgreementRebateOrderRequest orderRequest = new AgreementRebateOrderRequest();
                List<AgreementRebateOrderRequest.AgreementRebateOrderDetailRequest> agreementRebateOrderDetailRequestList = new ArrayList<>();
                Long quantity = 0L;
                for (SettlementDetailDTO settlementDetailDTO : settlementFullDTO.getSettlementDetailDTOList()) {
                    if (goodsIds.contains(settlementDetailDTO.getGoodsId())) {
                        AgreementRebateOrderRequest.AgreementRebateOrderDetailRequest goodsRequest = new AgreementRebateOrderRequest.AgreementRebateOrderDetailRequest();
                        goodsRequest.setAgreementId(supplementAgreementDetailDTO.getId());
                        goodsRequest.setEid(supplementAgreementDetailDTO.getEid());
                        goodsRequest.setOrderDetailId(settlementDetailDTO.getOrderDetailId());
                        goodsRequest.setGoodsId(settlementDetailDTO.getGoodsId());
                        goodsRequest.setVersion(supplementAgreementDetailDTO.getVersion());
                        goodsRequest.setEasAccount(easAccount);
                        goodsRequest.setType(3);
                        goodsRequest.setDeliveryTime(settlementFullDTO.getDeliveryTime());
                        goodsRequest.setSettlementId(settlementFullDTO.getId());
                        goodsRequest.setSecondEid(buyEid);
                        goodsRequest.setOrderId(settlementDetailDTO.getOrderId());
                        goodsRequest.setComparisonTime(settlementFullDTO.getBackTime());
                        goodsRequest.setGoodsAmount(settlementDetailDTO.getBackAmount());
                        goodsRequest.setOpUserId(opUserId);
                        agreementRebateOrderDetailRequestList.add(goodsRequest);
                    }
                }
                //如果明细一个商品都没有不插入订单信息
                if (CollUtil.isNotEmpty(agreementRebateOrderDetailRequestList)) {
                    orderRequest.setAgreementId(supplementAgreementDetailDTO.getId());
                    orderRequest.setVersion(supplementAgreementDetailDTO.getVersion());
                    orderRequest.setEid(supplementAgreementDetailDTO.getEid());
                    orderRequest.setSecondEid(buyEid);
                    orderRequest.setOrderId(settlementFullDTO.getOrderId());
                    orderRequest.setType(3);
                    orderRequest.setDeliveryTime(settlementFullDTO.getDeliveryTime());
                    orderRequest.setSettlementId(settlementFullDTO.getId());
                    orderRequest.setPaymentMethod(settlementFullDTO.getBackAmountType());
                    orderRequest.setGoodsAmount(settlementFullDTO.getBackAmount());
                    orderRequest.setGoodsQuantity(quantity);
                    orderRequest.setComparisonTime(settlementFullDTO.getBackTime());
                    orderRequest.setOpUserId(opUserId);
                    orderRequest.setEasAccount(easAccount);
                    orderRequest.setAgreementRebateOrderDetailList(agreementRebateOrderDetailRequestList);
                    agreementRebateOrderRequestList.add(orderRequest);
                }
            }
        }

        //4查询所有订单的退货单信息审核通过的状态
        List<AgreementOrderReturnDetailDTO> agreementOrderReturnDetailDTOList = orderReturnApi.getOrderReturnDetailByEidAndTime(request);
        Map<Long, List<AgreementOrderReturnDetailDTO>> listMap = agreementOrderReturnDetailDTOList.stream().collect(Collectors.groupingBy(AgreementOrderReturnDetailDTO::getId));
        for (Map.Entry<Long, List<AgreementOrderReturnDetailDTO>> entry : listMap.entrySet()) {
            if (returnIds.contains(entry.getKey())) {
                continue;
            }
            AgreementRebateOrderRequest orderRequest = new AgreementRebateOrderRequest();
            List<AgreementRebateOrderRequest.AgreementRebateOrderDetailRequest> agreementRebateOrderDetailRequestList = new ArrayList<>();
            BigDecimal totalAmount = BigDecimal.ZERO;
            Long quantity = 0L;
            List<AgreementOrderReturnDetailDTO> entryValue = entry.getValue();
            for (AgreementOrderReturnDetailDTO agreementOrderReturnDetailDTO : entryValue) {
                //有新增的协议商品退货
                if (goodsIds.contains(agreementOrderReturnDetailDTO.getGoodsId())) {
                    goodsIdList.add(agreementOrderReturnDetailDTO.getGoodsId());
                    AgreementRebateOrderRequest.AgreementRebateOrderDetailRequest goodsRequest = new AgreementRebateOrderRequest.AgreementRebateOrderDetailRequest();
                    goodsRequest.setAgreementId(supplementAgreementDetailDTO.getId());
                    goodsRequest.setEid(supplementAgreementDetailDTO.getEid());
                    goodsRequest.setOrderDetailId(agreementOrderReturnDetailDTO.getDetailId());
                    goodsRequest.setGoodsId(agreementOrderReturnDetailDTO.getGoodsId());
                    goodsRequest.setVersion(supplementAgreementDetailDTO.getVersion());
                    goodsRequest.setSecondEid(buyEid);
                    goodsRequest.setType(2);
                    goodsRequest.setReturnId(agreementOrderReturnDetailDTO.getId());
                    goodsRequest.setEasAccount(easAccount);
                    goodsRequest.setOrderId(agreementOrderReturnDetailDTO.getOrderId());
                    goodsRequest.setComparisonTime(agreementOrderReturnDetailDTO.getReturnAuditTime());
                    goodsRequest.setGoodsAmount(agreementOrderReturnDetailDTO.getReturnAmount());
                    goodsRequest.setGoodsQuantity(agreementOrderReturnDetailDTO.getReturnQuantity().longValue());
                    orderRequest.setOpUserId(opUserId);
                    agreementRebateOrderDetailRequestList.add(goodsRequest);
                    totalAmount = totalAmount.add(agreementOrderReturnDetailDTO.getReturnAmount());
                    quantity = quantity + agreementOrderReturnDetailDTO.getReturnQuantity().longValue();
                }
            }
            //如果明细一个商品都没有不插入订单信息
            if (CollUtil.isNotEmpty(agreementRebateOrderDetailRequestList)) {
                AgreementOrderReturnDetailDTO agreementOrderReturnDetailDTO = entryValue.get(0);
                orderRequest.setAgreementId(supplementAgreementDetailDTO.getId());
                orderRequest.setVersion(supplementAgreementDetailDTO.getVersion());
                orderRequest.setEid(supplementAgreementDetailDTO.getEid());
                orderRequest.setSecondEid(buyEid);
                orderRequest.setReturnId(agreementOrderReturnDetailDTO.getId());
                orderRequest.setOrderId(agreementOrderReturnDetailDTO.getOrderId());
                orderRequest.setEasAccount(easAccount);
                orderRequest.setType(2);
                orderRequest.setGoodsAmount(totalAmount);
                orderRequest.setComparisonTime(agreementOrderReturnDetailDTO.getReturnAuditTime());
                orderRequest.setGoodsQuantity(quantity);
                orderRequest.setOpUserId(opUserId);
                orderRequest.setAgreementRebateOrderDetailList(agreementRebateOrderDetailRequestList);
                agreementRebateOrderRequestList.add(orderRequest);
            }
        }

        //6如果有退货的商品信息，需要把对应的协议商品协议政策清空
        {
            if (CollUtil.isNotEmpty(goodsIdList)) {
//                List<Long> agreementIdList = agreementGoodsApi.getAgreementIdsByPurchaseGoodsList(buyEid, new ArrayList<>(goodsIdList));
                ClearAgreementConditionCalculateRequest calculateRequest = new ClearAgreementConditionCalculateRequest();
                calculateRequest.setAgreementIds(Arrays.asList(supplementAgreementDetailDTO.getId()));
                calculateRequest.setOpUserId(opUserId);
                agreementRebateOrderApi.clearDiscountAmountByOrderIdsAndAgreementIds(calculateRequest);
            }
        }
        //5把订单和退货单信息插入到兑付表里面
        return agreementRebateOrderApi.saveBatch(agreementRebateOrderRequestList);
    }

    /**
     * 计算订单和退货单返利金额
     *
     * @param supplementAgreementDetailDTO 协议
     * @param calculateTime                计算时间点
     * @param easAccount                   账号
     * @return
     */
    public boolean calculateAgreement(SupplementAgreementDetailDTO supplementAgreementDetailDTO, Date calculateTime, List<String> timeRange, String easAccount, Long buyEid, Long opUserId) {
        List<AgreementConditionDTO> agreementConditionDTOList = supplementAgreementDetailDTO.getAgreementsConditionList();
        if (CollUtil.isEmpty(agreementConditionDTOList)) {
            log.error("协议条件信息为空,id={}", supplementAgreementDetailDTO.getId());
            return false;
        }

        //获取当前的协议条件信息
        List<String> comparisonTimes = null;
        AgreementConditionDTO agreementConditionDTO = agreementConditionDTOList.get(0);
        if (supplementAgreementDetailDTO.getConditionRule().equals(AgreementConditionRuleEnum.MONTHLY.getCode()) || supplementAgreementDetailDTO.getConditionRule().equals(AgreementConditionRuleEnum.SEASONS.getCode())) {
            for (String time : timeRange) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(DateUtil.parse(time, "yyyy-MM"));
                int month = cal.get(Calendar.MONTH) + 1;
                int quarter = month % 3 == 0 ? month / 3 : month / 3 + 1;
                if (supplementAgreementDetailDTO.getConditionRule().equals(AgreementConditionRuleEnum.MONTHLY.getCode())) {
                    for (AgreementConditionDTO agreementCondition : agreementConditionDTOList) {
                        if (agreementCondition.getRangeNo().equals(month)) {
                            agreementConditionDTO = agreementCondition;
                        }
                    }
                } else if (supplementAgreementDetailDTO.getConditionRule().equals(AgreementConditionRuleEnum.SEASONS.getCode())) {
                    for (AgreementConditionDTO agreementCondition : agreementConditionDTOList) {
                        if (agreementCondition.getRangeNo().equals(quarter)) {
                            agreementConditionDTO = agreementCondition;
                        }
                    }
                }
                comparisonTimes = Arrays.asList(time);
                calculateAgreementCondition(supplementAgreementDetailDTO, agreementConditionDTO, calculateTime, comparisonTimes, easAccount, buyEid, opUserId);
            }
        } else {
            calculateAgreementCondition(supplementAgreementDetailDTO, agreementConditionDTO, calculateTime, timeRange, easAccount, buyEid, opUserId);
        }
        return true;
    }


    public boolean calculateAgreementCondition(SupplementAgreementDetailDTO supplementAgreementDetailDTO, AgreementConditionDTO agreementConditionDTO, Date calculateTime, List<String> timeRange, String easAccount, Long buyEid, Long opUserId) {
        List<AgreementConditionDTO> agreementConditionDTOList = supplementAgreementDetailDTO.getAgreementsConditionList();
        if (CollUtil.isEmpty(agreementConditionDTOList)) {
            log.error("协议条件信息为空,id={}", supplementAgreementDetailDTO.getId());
            return false;
        }

        //通过协议条件获取所有的数据信息
        QueryAgreementRebateRequest queryAgreementRebateRequest = new QueryAgreementRebateRequest();
        queryAgreementRebateRequest.setAgreementIds(Arrays.asList(supplementAgreementDetailDTO.getId()));
        queryAgreementRebateRequest.setComparisonTimes(timeRange);
        queryAgreementRebateRequest.setEasAccount(easAccount);
        List<AgreementRebateOrderDTO> agreementRebateOrderDTOList = agreementRebateOrderApi.getAgreementRebateOrderListByAgreementIds(queryAgreementRebateRequest);
        if (CollUtil.isEmpty(agreementRebateOrderDTOList)) {
            return false;
        }

        //协议订单信息
        List<AgreementRebateOrderDTO> agreementRebateOrderList = agreementRebateOrderDTOList.stream().filter(e -> e.getType() == 1).collect(Collectors.toList());
        //协议退货单信息
        List<AgreementRebateOrderDTO> agreementRebateReturnList = agreementRebateOrderDTOList.stream().filter(e -> e.getType() == 2).collect(Collectors.toList());
        //协议回款单信息
        List<AgreementRebateOrderDTO> agreementRebateBackList = agreementRebateOrderDTOList.stream().filter(e -> e.getType() == 3).collect(Collectors.toList());


        Map<Long, List<AgreementRebateOrderDTO>> agreementOrderMap = new HashMap<>();
        for (AgreementRebateOrderDTO agreementRebateOrderDTO : agreementRebateReturnList) {
            if (agreementOrderMap.containsKey(agreementRebateOrderDTO.getOrderId())) {
                agreementOrderMap.get(agreementRebateOrderDTO.getOrderId()).add(agreementRebateOrderDTO);
            } else {
                List<AgreementRebateOrderDTO> dtoList = new ArrayList<>();
                dtoList.add(agreementRebateOrderDTO);
                agreementOrderMap.put(agreementRebateOrderDTO.getOrderId(), dtoList);
            }
        }

        //待处理的订单ID和统计所有的采购额和采购量
        List<Long> orderIds = new ArrayList<>();
        List<Long> returnOrderIds = new ArrayList<>();
        //已经兑付的订单ID集合
        List<Long> cashOrderIds = new ArrayList<>();
        //已经计算过的未兑付的条件Id结合
        Set<Long> agreementConditionIds = new HashSet<>();

        BigDecimal compareValue = BigDecimal.ZERO;
        BigDecimal totalAmount = BigDecimal.ZERO;
        Long totalQuantity = 0L;
        //判断协议类型
        Integer type = 1;
        if (supplementAgreementDetailDTO.getChildType().equals(3)) {
            type = 3;
            agreementRebateOrderList = agreementRebateBackList;
        }

        //协议统计协议订单的值
        {
            for (AgreementRebateOrderDTO agreementRebateOrderDTO : agreementRebateOrderList) {
                //判断支付方式
                if (agreementConditionDTO.getPayType().equals(1) && !agreementConditionDTO.getPayTypeValues().contains(agreementRebateOrderDTO.getPaymentMethod())) {
                    continue;
                }
                //回款形式
                if (agreementConditionDTO.getBackAmountType().equals(1) && !agreementConditionDTO.getBackAmountTypeValues().contains(agreementRebateOrderDTO.getBackAmountType())) {
                    continue;
                }

                //如果是回款时间点，需要判断回款时间小于设置时间点
                if (type == 3 && AgreementConditionRuleEnum.CONFIRM_DATA.getCode().equals(supplementAgreementDetailDTO.getConditionRule())) {
                    String timeNode = DateUtil.format(calculateTime, "dd");
                    if (Integer.parseInt(timeNode) > agreementConditionDTO.getTimeNode()) {
                        continue;
                    }
                }

                //如果是回款类型并且是阶梯类型需要一条一条的计算
                if (type == 3 && AgreementConditionRuleEnum.GRADIENT.getCode().equals(supplementAgreementDetailDTO.getConditionRule())) {
                    long day = DateUtil.between(agreementRebateOrderDTO.getDeliveryTime(), agreementRebateOrderDTO.getComparisonTime(), DateUnit.DAY);
                    for (AgreementConditionDTO agreementCondition : agreementConditionDTOList) {
                        if (agreementCondition.getMixValue().compareTo(new BigDecimal(day)) < 0 && agreementCondition.getMaxValue().compareTo(new BigDecimal(day)) >= 0) {
                            AgreementConditionCalculateRequest requestBackGradient = new AgreementConditionCalculateRequest();
                            requestBackGradient.setType(3);
                            requestBackGradient.setEid(buyEid);
                            requestBackGradient.setOrderIds(Arrays.asList(agreementRebateOrderDTO.getOrderId()));
                            requestBackGradient.setCalculateTime(calculateTime);
                            requestBackGradient.setEasAccount(easAccount);
                            requestBackGradient.setAgreementId(supplementAgreementDetailDTO.getId());
                            requestBackGradient.setPolicyValue(agreementCondition.getPolicyValue().setScale(2, BigDecimal.ROUND_HALF_UP));
                            requestBackGradient.setAgreementConditionId(agreementCondition.getId());
                            agreementRebateOrderApi.updateBatchDiscountAmount(requestBackGradient);
                            continue;
                        }
                    }
                }

                //如果是回款类型并且是阶梯类型需要一条一条的计算,这里直接返回
                if (type == 3 && AgreementConditionRuleEnum.GRADIENT.getCode().equals(supplementAgreementDetailDTO.getConditionRule())) {
                    return true;
                }

                //已经计算的订单不需要再次计算
                if (agreementRebateOrderDTO.getConditionStatus() != 2) {
                    orderIds.add(agreementRebateOrderDTO.getOrderId());
                }
                //统计已经兑付的订单ID
                if (agreementRebateOrderDTO.getCashStatus() == 2) {
                    cashOrderIds.add(agreementRebateOrderDTO.getOrderId());
                }
                //累计总额（包含已经兑付的情况）
                totalQuantity = totalQuantity + agreementRebateOrderDTO.getGoodsQuantity();
                totalAmount = totalAmount.add(agreementRebateOrderDTO.getGoodsAmount());
                //统计已经计算好的条件信息（如果阶梯已经调级就需要重新计算）
                if (agreementRebateOrderDTO.getCashStatus() != 2 && agreementRebateOrderDTO.getAgreementConditionId() != 0) {
                    agreementConditionIds.add(agreementRebateOrderDTO.getAgreementConditionId());
                }
                //获取订单已经退货的信息
                if (type != 3) {
                    List<AgreementRebateOrderDTO> returnOrderDTOList = agreementOrderMap.get(agreementRebateOrderDTO.getOrderId());
                    if (CollUtil.isNotEmpty(returnOrderDTOList)) {
                        for (AgreementRebateOrderDTO returnOrder : returnOrderDTOList) {
                            totalQuantity = totalQuantity - returnOrder.getGoodsQuantity();
                            totalAmount = totalAmount.subtract(returnOrder.getGoodsAmount());
                            if (returnOrder.getConditionStatus() != 2) {
                                returnOrderIds.add(agreementRebateOrderDTO.getOrderId());
                            }
                        }
                    }
                }
            }

            //判断类型 采购额和采购量
            if (supplementAgreementDetailDTO.getChildType().equals(AgreementChildTypeEnum.PURCHASE_AMOUNT.getCode())) {
                compareValue = totalAmount;
            } else if (supplementAgreementDetailDTO.getChildType().equals(AgreementChildTypeEnum.PURCHASE_CAPACITY.getCode())) {
                compareValue = new BigDecimal(totalQuantity);
            } else if (supplementAgreementDetailDTO.getChildType().equals(AgreementChildTypeEnum.PAY_BACK_AMOUNT.getCode())) {
                compareValue = totalAmount;
            }
        }

        //返回true没有满足条件，需要进一步处理退货单对应的主单已经兑付的情况
        boolean bool = compareAgreementCondition(supplementAgreementDetailDTO, agreementConditionDTO, compareValue);
        if (bool) {
            if (type != 3) {
                //不满足条件
                returnOrderIds = ListUtil.toList();
                orderIds = ListUtil.empty();
                for (AgreementRebateOrderDTO agreementRebateOrderDTO : agreementRebateReturnList) {
                    //已经兑付的订单，发生退货的情况，直接计算兑付退货单信息
                    if (cashOrderIds.contains(agreementRebateOrderDTO.getOrderId())) {
                        //满足条件
                        returnOrderIds.add(agreementRebateOrderDTO.getOrderId());
                    }
                }
            }
        } else {
            //满足条件
            if (supplementAgreementDetailDTO.getConditionRule().equals(AgreementConditionRuleEnum.GRADIENT.getCode())) {
                for (AgreementConditionDTO agreementCondition : agreementConditionDTOList) {
                    if (agreementCondition.getMixValue().compareTo(compareValue) < 0 && agreementCondition.getMaxValue().compareTo(compareValue) >= 0) {
                        agreementConditionDTO = agreementCondition;
                    }
                }

                agreementConditionIds.remove(agreementConditionDTO.getId());
                if (CollUtil.isNotEmpty(agreementConditionIds)) {
                    ClearAgreementConditionCalculateRequest request = new ClearAgreementConditionCalculateRequest();
                    request.setOrderIds(orderIds);
                    request.setAgreementConditionIds(new ArrayList<>(agreementConditionIds));
                    request.setOpUserId(opUserId);
                    agreementRebateOrderApi.clearDiscountAmountByOrderIdsAndAgreementIds(request);
                }
            }
        }
        //需要处理的订单信息和退货单信息
        if (CollUtil.isEmpty(orderIds) && CollUtil.isEmpty(returnOrderIds)) {
            return false;
        }

        //修改协议条件id、政策返利金额、计算时间、退货单、统计金额
        AgreementConditionCalculateRequest request = new AgreementConditionCalculateRequest();
        request.setEid(buyEid);
        request.setAgreementConditionId(agreementConditionDTO.getId());
        request.setAgreementId(supplementAgreementDetailDTO.getId());
        request.setEasAccount(easAccount);
        request.setOrderIds(orderIds);
        request.setType(type);
        request.setReturnOrderIds(returnOrderIds);
        request.setCalculateTime(calculateTime);
        request.setPolicyValue(agreementConditionDTO.getPolicyValue().setScale(2, BigDecimal.ROUND_HALF_UP));
        request.setOpUserId(opUserId);
        return agreementRebateOrderApi.updateBatchDiscountAmount(request);
    }

    /**
     * 比较条件和实际值
     *
     * @param supplementAgreementDetailDTO
     * @param agreementConditionDTO
     * @param compareValue
     * @return true 没有满足条件 false 满足条件
     */
    private boolean compareAgreementCondition(SupplementAgreementDetailDTO supplementAgreementDetailDTO, AgreementConditionDTO agreementConditionDTO, BigDecimal compareValue) {
        if (supplementAgreementDetailDTO.getChildType().equals(3) && supplementAgreementDetailDTO.getConditionRule().equals(AgreementConditionRuleEnum.CONFIRM_DATA)) {
            return false;
        }

        if (supplementAgreementDetailDTO.getConditionRule().equals(AgreementConditionRuleEnum.TOTAL_AMOUNT.getCode())) {
            //不满足条件
            return agreementConditionDTO.getAmount().compareTo(compareValue) > 0;
        } else if (supplementAgreementDetailDTO.getConditionRule().equals(AgreementConditionRuleEnum.MONTHLY.getCode())) {
            //不满足条件
            return agreementConditionDTO.getAmount().compareTo(compareValue) > 0;
        } else if (supplementAgreementDetailDTO.getConditionRule().equals(AgreementConditionRuleEnum.SEASONS.getCode())) {
            //不满足条件
            return agreementConditionDTO.getAmount().compareTo(compareValue) > 0;
        } else if (supplementAgreementDetailDTO.getConditionRule().equals(AgreementConditionRuleEnum.GRADIENT.getCode())) {
            //最小的梯度都没有满足
            return agreementConditionDTO.getMixValue().compareTo(compareValue) > 0;
        }
        return false;
    }

    private List<String> monthByQuarter(Integer year, Integer quarter) {
        List<String> comparisonTimes = new ArrayList<>();
        if (quarter.equals(1)) {
            comparisonTimes.add(year + "-01");
            comparisonTimes.add(year + "-02");
            comparisonTimes.add(year + "-03");
        } else if (quarter.equals(2)) {
            comparisonTimes.add(year + "-04");
            comparisonTimes.add(year + "-05");
            comparisonTimes.add(year + "-06");
        } else if (quarter.equals(3)) {
            comparisonTimes.add(year + "-07");
            comparisonTimes.add(year + "-08");
            comparisonTimes.add(year + "-09");
        } else {
            comparisonTimes.add(year + "-10");
            comparisonTimes.add(year + "-11");
            comparisonTimes.add(year + "-12");
        }
        return comparisonTimes;
    }
}
