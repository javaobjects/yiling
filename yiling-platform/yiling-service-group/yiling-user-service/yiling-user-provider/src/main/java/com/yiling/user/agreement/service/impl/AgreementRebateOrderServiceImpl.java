package com.yiling.user.agreement.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreement.dao.AgreementRebateOrderMapper;
import com.yiling.user.agreement.dto.AgreementOrderStatisticalDTO;
import com.yiling.user.agreement.dto.AgreementRebateOrderDTO;
import com.yiling.user.agreement.dto.RebateOrderPageListDTO;
import com.yiling.user.agreement.dto.request.AgreementConditionCalculateRequest;
import com.yiling.user.agreement.dto.request.AgreementRebateOrderRequest;
import com.yiling.user.agreement.dto.request.CashAgreementRequest;
import com.yiling.user.agreement.dto.request.ClearAgreementConditionCalculateRequest;
import com.yiling.user.agreement.dto.request.QueryAgreementRebateRequest;
import com.yiling.user.agreement.dto.request.QueryRebateOrderPageListRequest;
import com.yiling.user.agreement.entity.AgreementRebateOrderDO;
import com.yiling.user.agreement.entity.AgreementRebateOrderDetailDO;
import com.yiling.user.agreement.enums.AgreementRebateOrderCashStatusEnum;
import com.yiling.user.agreement.enums.AgreementRebateOrderConditionStatusEnum;
import com.yiling.user.agreement.service.AgreementConditionService;
import com.yiling.user.agreement.service.AgreementRebateLogService;
import com.yiling.user.agreement.service.AgreementRebateOrderDetailService;
import com.yiling.user.agreement.service.AgreementRebateOrderService;
import com.yiling.user.agreement.service.AgreementService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;

/**
 * <p>
 * 协议兑付订单表 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-07-07
 */
@Service
public class AgreementRebateOrderServiceImpl extends BaseServiceImpl<AgreementRebateOrderMapper, AgreementRebateOrderDO> implements AgreementRebateOrderService {

    @Autowired
    AgreementRebateOrderDetailService agreementRebateOrderDetailService;

    @Autowired
    AgreementConditionService agreementConditionService;

    @Autowired
    AgreementService agreementService;

    @Autowired
    AgreementRebateLogService agreementRebateLogService;

//	@Autowired
//    AgreementAccountService agreementAccountService;

    @Override
//    @Transactional(rollbackFor = Exception.class)
    public Boolean saveBatch(List<AgreementRebateOrderRequest> agreementOrderList) {
        agreementOrderList.forEach(e -> {
            AgreementRebateOrderDO agreementRebateOrderDO = PojoUtils.map(e, AgreementRebateOrderDO.class);
            this.save(agreementRebateOrderDO);
            List<AgreementRebateOrderRequest.AgreementRebateOrderDetailRequest> orderDetailList = e.getAgreementRebateOrderDetailList();
            List<AgreementRebateOrderDetailDO> agreementRebateOrderDetailList = PojoUtils.map(orderDetailList, AgreementRebateOrderDetailDO.class);
            agreementRebateOrderDetailList.forEach(r -> {
                r.setRebateOrderId(agreementRebateOrderDO.getId());
            });
            agreementRebateOrderDetailService.saveBatch(agreementRebateOrderDetailList);
        });
        return true;
    }

    @Override
    public List<AgreementRebateOrderDTO> getAgreementRebateOrderListByAgreementIds(QueryAgreementRebateRequest request) {
        return PojoUtils.map(this.baseMapper.getAgreementRebateOrderListByAgreementIds(request), AgreementRebateOrderDTO.class);
    }

    /**
     * @param request
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateBatchDiscountAmount(AgreementConditionCalculateRequest request) {
        //订单的计算
        if (CollUtil.isNotEmpty(request.getOrderIds())) {
            this.baseMapper.updateBatchDiscountAmount(request);
            agreementRebateOrderDetailService.updateBatchDiscountAmount(request);
        }
        //退货单的计算
        if (CollUtil.isNotEmpty(request.getReturnOrderIds())) {
            //先查出已经退货的订单信息
            List<Long> returnOrderList = request.getReturnOrderIds();
            for (Long returnOrder : returnOrderList) {
                QueryWrapper<AgreementRebateOrderDO> wrapper = new QueryWrapper<>();
                wrapper.lambda().eq(AgreementRebateOrderDO::getAgreementId, request.getAgreementId());
                wrapper.lambda().eq(AgreementRebateOrderDO::getType, 1);
                wrapper.lambda().eq(AgreementRebateOrderDO::getOrderId, returnOrder);
                wrapper.lambda().eq(AgreementRebateOrderDO::getEasAccount, request.getEasAccount());

                AgreementRebateOrderDO agreementRebateOrderDO = this.getOne(wrapper);
                request.setPolicyValue(agreementRebateOrderDO.getPolicyValue());
                request.setAgreementConditionId(agreementRebateOrderDO.getAgreementConditionId());
                request.setOrderIds(Arrays.asList(returnOrder));
                request.setType(2);
                this.baseMapper.updateBatchDiscountAmount(request);
                agreementRebateOrderDetailService.updateBatchDiscountAmount(request);
            }
        }
        return true;

        //统计协议计算金额
//		QueryWrapper<AgreementRebateOrderDO> wrapper = new QueryWrapper<>();
//		wrapper.lambda().eq(AgreementRebateOrderDO::getAgreementId, request.getAgreementId());
//		wrapper.lambda().eq(AgreementRebateOrderDO::getCashStatus, 1);
//		List<AgreementRebateOrderDO> orderList = this.list(wrapper);
//
//		//订单信息
//		List<AgreementRebateOrderDO> agreementRebateOrderList = orderList.stream().filter(e -> e.getReturnId() <= 0 && e.getConditionStatus() == 2).collect(Collectors.toList());
//		//退货单信息
//		List<AgreementRebateOrderDO> agreementRebateReturnList = orderList.stream().filter(e -> e.getReturnId() > 0 && e.getConditionStatus() == 2).collect(Collectors.toList());
//
//		BigDecimal discountAmount = BigDecimal.ZERO;
//		for (AgreementRebateOrderDO agreementRebateOrderDO : agreementRebateOrderList) {
//			discountAmount = discountAmount.add(agreementRebateOrderDO.getDiscountAmount());
//		}
//
//		BigDecimal returnDiscountAmount = BigDecimal.ZERO;
//		for (AgreementRebateOrderDO agreementRebateOrderDO : agreementRebateReturnList) {
//			returnDiscountAmount = returnDiscountAmount.add(agreementRebateOrderDO.getDiscountAmount());
//		}
//
//		//订单信息
//		List<AgreementRebateOrderDO> unAgreementRebateOrderList = orderList.stream().filter(e -> e.getReturnId() <= 0 && e.getConditionStatus() == 1).collect(Collectors.toList());
//		//退货单信息
//		List<AgreementRebateOrderDO> unAgreementRebateReturnList = orderList.stream().filter(e -> e.getReturnId() > 0 && e.getConditionStatus() == 1).collect(Collectors.toList());
//
//		//已完成金额
//		BigDecimal unDiscountAmount = BigDecimal.ZERO;
//		for (AgreementRebateOrderDO agreementRebateOrderDO : unAgreementRebateOrderList) {
//			unDiscountAmount = unDiscountAmount.add(agreementRebateOrderDO.getDiscountAmount());
//		}
//
//		BigDecimal unReturnDiscountAmount = BigDecimal.ZERO;
//		for (AgreementRebateOrderDO agreementRebateOrderDO : unAgreementRebateReturnList) {
//			unReturnDiscountAmount = unReturnDiscountAmount.add(agreementRebateOrderDO.getDiscountAmount());
//		}
//
//        AgreementAccountDO agreementAccountDO=new AgreementAccountDO();
//        agreementAccountDO.setEid(request.getEid());
//        agreementAccountDO.setEasAccount(request.getEasAccount());
//        agreementAccountDO.setAgreementId(request.getAgreementId());
//        agreementAccountDO.setAgreementConditionId(request.getAgreementConditionId());
//        agreementAccountDO.setCalculateTime(request.getCalculateTime());
//        agreementAccountDO.setRebateAmount(discountAmount.subtract(returnDiscountAmount));
//        agreementAccountDO.setCompletedConditionValue(unDiscountAmount.subtract(unReturnDiscountAmount));
//        agreementAccountDO.setOpUserId(request.getOpUserId());
//
//        QueryWrapper<AgreementAccountDO> agreementAccountWrapper = new QueryWrapper<>();
//        agreementAccountWrapper.lambda().eq(AgreementAccountDO::getAgreementId, request.getAgreementId());
//        agreementAccountWrapper.lambda().eq(AgreementAccountDO::getEasAccount,request.getEasAccount());
//        return agreementAccountService.saveOrUpdate(agreementAccountDO,agreementAccountWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean clearDiscountAmountByOrderIdsAndAgreementIds(ClearAgreementConditionCalculateRequest request) {
        this.baseMapper.clearDiscountAmountByOrderIdsAndAgreementIds(request);
        return agreementRebateOrderDetailService.clearDiscountAmountByOrderIdsAndAgreementIds(request);
    }

    /**
     * agreement_account 删除
     * 修改兑付次数,修改状态，兑付时间,待反金额清零,计算协议条件ID清零,添加兑付日志
     *
     * @param request
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean cashAgreementByAgreementId(CashAgreementRequest request) {
        QueryWrapper<AgreementRebateOrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(AgreementRebateOrderDO::getAgreementId, request.getAgreementId());
        wrapper.lambda().eq(AgreementRebateOrderDO::getCashStatus, 1);
        if (CollUtil.isNotEmpty(request.getTimeRange())) {
            Date startTime = DateUtil.beginOfMonth(DateUtil.parse(request.getTimeRange().get(0), "yyyy-MM"));
            Date endTime = DateUtil.endOfMonth(DateUtil.parse(request.getTimeRange().get(request.getTimeRange().size() - 1), "yyyy-MM"));
            wrapper.lambda().between(AgreementRebateOrderDO::getComparisonTime, startTime, endTime);
        }
        AgreementRebateOrderDO agreementRebateOrderDO = new AgreementRebateOrderDO();
        agreementRebateOrderDO.setCashStatus(2);
        agreementRebateOrderDO.setCashTime(new Date());
        agreementRebateOrderDO.setOpUserId(request.getOpUserId());

        agreementRebateOrderDetailService.cashAgreementRebateOrderDetail(request);
        return this.update(agreementRebateOrderDO, wrapper);

//		AgreementRebateLogDO agreementRebateLogDO = new AgreementRebateLogDO();
//		agreementRebateLogDO.setCashType(AgreementCashTypeEnum.AGREEMENT.getCode());
//		agreementRebateLogDO.setLogName(request.getAgreementName());
//		agreementRebateLogDO.setAgreementId(request.getAgreementId());
//		agreementRebateLogDO.setVersion(request.getVersion());
//		agreementRebateLogDO.setAccount(request.getEasAccount());
//		agreementRebateLogDO.setDiscountAmount(request.getCashAmount());
//		return agreementRebateLogService.save(agreementRebateLogDO);
    }

    @Override
    public AgreementOrderStatisticalDTO statisticsOrderCount(Long agreementId) {
        //未满足订单数
        int unPassOrderCount = count(Wrappers.<AgreementRebateOrderDO>query().lambda()
                .eq(AgreementRebateOrderDO::getAgreementId, agreementId)
                .eq(AgreementRebateOrderDO::getConditionStatus, AgreementRebateOrderConditionStatusEnum.UN_PASS.getCode())
                .eq(AgreementRebateOrderDO::getDelFlag, 0));
        //满足订单数
        int passOrderCount = count(Wrappers.<AgreementRebateOrderDO>query().lambda()
                .eq(AgreementRebateOrderDO::getAgreementId, agreementId)
                .eq(AgreementRebateOrderDO::getConditionStatus, AgreementRebateOrderConditionStatusEnum.PASS.getCode())
                .eq(AgreementRebateOrderDO::getDelFlag, 0));
        //可对账订单退货数量
        int passOrderRefundCount = count(Wrappers.<AgreementRebateOrderDO>query().lambda()
                .eq(AgreementRebateOrderDO::getAgreementId, agreementId)
                .eq(AgreementRebateOrderDO::getConditionStatus, AgreementRebateOrderConditionStatusEnum.PASS.getCode())
                .gt(AgreementRebateOrderDO::getReturnId, 0)
                .eq(AgreementRebateOrderDO::getDelFlag, 0));
        //不可对账订单退货数量
        int unPassOrderRefundCount = count(Wrappers.<AgreementRebateOrderDO>query().lambda()
                .eq(AgreementRebateOrderDO::getAgreementId, agreementId)
                .eq(AgreementRebateOrderDO::getConditionStatus, AgreementRebateOrderConditionStatusEnum.UN_PASS.getCode())
                .gt(AgreementRebateOrderDO::getReturnId, 0)
                .eq(AgreementRebateOrderDO::getDelFlag, 0));
        return new AgreementOrderStatisticalDTO(unPassOrderCount, passOrderCount, passOrderRefundCount, unPassOrderRefundCount);
    }

    @Override
    public Page<RebateOrderPageListDTO> queryRebateOrderPageList(QueryRebateOrderPageListRequest request,
                                                                 AgreementRebateOrderConditionStatusEnum conditionStatusEnum,
                                                                 AgreementRebateOrderCashStatusEnum agreementRebateOrderCashStatusEnum) {
        QueryWrapper<AgreementRebateOrderDO> queryWrapper = new QueryWrapper<>();
        if (conditionStatusEnum != null) {
            queryWrapper.lambda().eq(AgreementRebateOrderDO::getConditionStatus, conditionStatusEnum.getCode());
        }
        if (agreementRebateOrderCashStatusEnum != null) {
            queryWrapper.lambda().eq(AgreementRebateOrderDO::getCashStatus, agreementRebateOrderCashStatusEnum.getCode());
        }
        queryWrapper.lambda().eq(AgreementRebateOrderDO::getAgreementId, request.getAgreementId())
                .eq(AgreementRebateOrderDO::getDelFlag, 0);
        Page<AgreementRebateOrderDO> page = page(new Page<>(request.getCurrent(), request.getSize()), queryWrapper);
        return PojoUtils.map(page, RebateOrderPageListDTO.class);
    }

}
