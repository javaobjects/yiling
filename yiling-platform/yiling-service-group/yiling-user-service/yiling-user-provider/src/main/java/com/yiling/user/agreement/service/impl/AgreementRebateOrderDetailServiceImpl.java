package com.yiling.user.agreement.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreement.dao.AgreementRebateOrderDetailMapper;
import com.yiling.user.agreement.dto.AgreementRebateOrderDetailDTO;
import com.yiling.user.agreement.dto.request.AgreementConditionCalculateRequest;
import com.yiling.user.agreement.dto.request.CashAgreementRequest;
import com.yiling.user.agreement.dto.request.ClearAgreementConditionCalculateRequest;
import com.yiling.user.agreement.dto.request.PageListByIdRequest;
import com.yiling.user.agreement.dto.request.QueryRebateOrderDetailPageListRequest;
import com.yiling.user.agreement.dto.request.QueryRebateOrderDetailRequest;
import com.yiling.user.agreement.dto.request.QueryRebateOrderPageListRequest;
import com.yiling.user.agreement.entity.AgreementRebateOrderDetailDO;
import com.yiling.user.agreement.enums.AgreementRebateOrderCashStatusEnum;
import com.yiling.user.agreement.enums.AgreementRebateOrderConditionStatusEnum;
import com.yiling.user.agreement.service.AgreementRebateOrderDetailService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;

/**
 * <p>
 * 协议兑付订单明细表 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-07-07
 */
@Service
public class AgreementRebateOrderDetailServiceImpl extends BaseServiceImpl<AgreementRebateOrderDetailMapper, AgreementRebateOrderDetailDO> implements AgreementRebateOrderDetailService {

	@Override
	public Boolean updateBatchDiscountAmount(AgreementConditionCalculateRequest request) {
		this.baseMapper.updateBatchDiscountAmount(request);
		return true;
	}

	@Override
	public Boolean clearDiscountAmountByOrderIdsAndAgreementIds(ClearAgreementConditionCalculateRequest request) {
		this.baseMapper.clearDiscountAmountByOrderIdsAndAgreementIds(request);
		return true;
	}

    @Override
	public Boolean cashAgreementRebateOrderDetail(CashAgreementRequest request) {
		QueryWrapper<AgreementRebateOrderDetailDO> wrapper = new QueryWrapper<>();
		wrapper.lambda().eq(AgreementRebateOrderDetailDO::getAgreementId, request.getAgreementId());
		wrapper.lambda().eq(AgreementRebateOrderDetailDO::getCashStatus, 1);
        if(CollUtil.isNotEmpty(request.getTimeRange())){
            Date startTime= DateUtil.beginOfMonth(DateUtil.parse(request.getTimeRange().get(0),"yyyy-MM"));
            Date endTime= DateUtil.endOfMonth(DateUtil.parse(request.getTimeRange().get(request.getTimeRange().size() - 1),"yyyy-MM"));
            wrapper.lambda().between(AgreementRebateOrderDetailDO::getComparisonTime, startTime, endTime);
        }
		AgreementRebateOrderDetailDO agreementRebateOrderDO = new AgreementRebateOrderDetailDO();
		agreementRebateOrderDO.setCashStatus(2);
		agreementRebateOrderDO.setOpUserId(request.getOpUserId());
		return this.update(agreementRebateOrderDO, wrapper);
	}

	@Override
	public List<AgreementRebateOrderDetailDTO> queryAgreementRebateOrderDetailList(QueryRebateOrderDetailRequest request) {
		List<AgreementRebateOrderDetailDO> detailList = baseMapper.queryAgreementRebateOrderDetailList(request);
		return PojoUtils.map(detailList, AgreementRebateOrderDetailDTO.class);
	}

	@Override
	public Page<AgreementRebateOrderDetailDTO> queryRebateOrderPageList(QueryRebateOrderPageListRequest request,
																		AgreementRebateOrderConditionStatusEnum conditionStatusEnum,
																		AgreementRebateOrderCashStatusEnum agreementRebateOrderCashStatusEnum) {
		QueryWrapper<AgreementRebateOrderDetailDO> queryWrapper = new QueryWrapper<>();
		if (conditionStatusEnum != null) {
			queryWrapper.lambda().eq(AgreementRebateOrderDetailDO::getConditionStatus, conditionStatusEnum.getCode());
		}
		if (agreementRebateOrderCashStatusEnum != null) {
			queryWrapper.lambda().eq(AgreementRebateOrderDetailDO::getCashStatus, agreementRebateOrderCashStatusEnum.getCode());
		}
		queryWrapper.lambda().eq(AgreementRebateOrderDetailDO::getAgreementId, request.getAgreementId())
				.eq(AgreementRebateOrderDetailDO::getDelFlag, 0);
		Page<AgreementRebateOrderDetailDO> page = page(new Page<>(request.getCurrent(), request.getSize()), queryWrapper);
		return PojoUtils.map(page, AgreementRebateOrderDetailDTO.class);
	}

	@Override
	public Page<AgreementRebateOrderDetailDTO> pageListById(PageListByIdRequest request) {
		if (CollUtil.isEmpty(request.getIdList())){
			return new Page<>();
		}
		LambdaQueryWrapper<AgreementRebateOrderDetailDO> wrapper = Wrappers.lambdaQuery();
		wrapper.in(AgreementRebateOrderDetailDO::getId,request.getIdList());
		Page<AgreementRebateOrderDetailDO> page = page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
		return PojoUtils.map(page, AgreementRebateOrderDetailDTO.class);
	}

	@Override
	public Page<AgreementRebateOrderDetailDTO> queryAgreementRebateOrderDetailPageList(QueryRebateOrderDetailPageListRequest request) {
		LambdaQueryWrapper<AgreementRebateOrderDetailDO> wrapper = Wrappers.lambdaQuery();
		if (ObjectUtil.isNotNull(request.getSecondEid())) {
			wrapper.eq(AgreementRebateOrderDetailDO::getSecondEid, request.getSecondEid());
		}
		if (ObjectUtil.isNotNull(request.getEasCode())) {
			wrapper.eq(AgreementRebateOrderDetailDO::getEasAccount, request.getEasCode());
		}
		if (CollUtil.isNotEmpty(request.getOrderIdList())) {
			wrapper.in(AgreementRebateOrderDetailDO::getOrderId, request.getOrderIdList());
		}
		if (CollUtil.isNotEmpty(request.getReturnIdList())) {
			if (CollUtil.isNotEmpty(request.getOrderIdList())) {
				wrapper.or();
			}
			wrapper.in(AgreementRebateOrderDetailDO::getReturnId, request.getReturnIdList());
		}
		Page<AgreementRebateOrderDetailDO> page = page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
		return PojoUtils.map(page, AgreementRebateOrderDetailDTO.class);
	}

}
