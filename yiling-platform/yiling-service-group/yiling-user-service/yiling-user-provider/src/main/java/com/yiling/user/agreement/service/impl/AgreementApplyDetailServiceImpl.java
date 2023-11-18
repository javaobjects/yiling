package com.yiling.user.agreement.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreement.dao.AgreementRebateApplyDetailMapper;
import com.yiling.user.agreement.dto.AgreementApplyDetailDTO;
import com.yiling.user.agreement.dto.request.AddRebateApplyDetailRequest;
import com.yiling.user.agreement.dto.request.AddRebateLogRequest;
import com.yiling.user.agreement.dto.request.EditApplyDetailRequest;
import com.yiling.user.agreement.dto.request.QueryRebateApplyDetailPageListRequest;
import com.yiling.user.agreement.entity.AgreementApplyDO;
import com.yiling.user.agreement.entity.AgreementApplyDetailDO;
import com.yiling.user.agreement.enums.AgreementApplyStatusEnum;
import com.yiling.user.agreement.enums.AgreementErrorCode;
import com.yiling.user.agreement.enums.ApplyDetailTypeEnum;
import com.yiling.user.agreement.service.AgreementApplyDetailService;
import com.yiling.user.agreement.service.AgreementApplyService;
import com.yiling.user.agreement.service.AgreementRebateLogService;
import com.yiling.user.enterprise.service.EnterpriseCustomerEasService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 返利申请明细表 服务实现类
 * </p>
 *
 * @author dexi.yao
 * @date 2021-07-26
 */
@Service
@Slf4j
public class AgreementApplyDetailServiceImpl extends BaseServiceImpl<AgreementRebateApplyDetailMapper, AgreementApplyDetailDO> implements AgreementApplyDetailService {

	@Autowired
	AgreementApplyService        agreementApplyService;
	@Autowired
	EnterpriseCustomerEasService enterpriseCustomerEasService;
	@Autowired
	AgreementRebateLogService    rebateLogService;

	@Override
	public Boolean updateById(AddRebateApplyDetailRequest request) {
		AgreementApplyDetailDO detailDO=PojoUtils.map(request,AgreementApplyDetailDO.class);
		return updateById(detailDO);
	}

	@Override
	public Page<AgreementApplyDetailDTO> queryRebateApplyDetailPageList(QueryRebateApplyDetailPageListRequest request) {
		QueryWrapper<AgreementApplyDetailDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(AgreementApplyDetailDO::getApplyId, request.getApplyId());
		Page<AgreementApplyDetailDO> page = page(new Page<>(request.getCurrent(), request.getSize()), queryWrapper);
		return PojoUtils.map(page, AgreementApplyDetailDTO.class);
	}

	@Override
	public Map<Long, List<AgreementApplyDetailDTO>> queryRebateApplyDetailList(List<Long> ids) {
		if (CollUtil.isEmpty(ids)) {
			return MapUtil.newHashMap();
		}
		QueryWrapper<AgreementApplyDetailDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().in(AgreementApplyDetailDO::getApplyId, ids);
		List<AgreementApplyDetailDO> list = list(queryWrapper);
		List<AgreementApplyDetailDTO> detailDTOS = PojoUtils.map(list, AgreementApplyDetailDTO.class);
		return detailDTOS.stream().collect(Collectors.groupingBy(AgreementApplyDetailDTO::getApplyId));
	}

	@Override
	public List<AgreementApplyDetailDTO> queryRebateApplyDetail(Long id) {
		Map<Long, List<AgreementApplyDetailDTO>> listMap = queryRebateApplyDetailList(ListUtil.toList(id));
		return listMap.getOrDefault(id,ListUtil.toList());
	}

	@Override
	public List<AgreementApplyDetailDTO> queryRebateApplyDetailList(String code, List<Long> agreementId) {
		if (CollUtil.isEmpty(agreementId)){
			return ListUtil.toList();
		}
		LambdaQueryWrapper<AgreementApplyDetailDO> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(AgreementApplyDetailDO::getApplyCode, code).in(AgreementApplyDetailDO::getAgreementId,agreementId);
		List<AgreementApplyDetailDO> list = list(wrapper);
		return PojoUtils.map(list, AgreementApplyDetailDTO.class);
	}

	@Override
	public AgreementApplyDetailDTO queryRebateApplyDetailById(Long detailId) {
		AgreementApplyDetailDO applyDetailDO = getById(detailId);
		return PojoUtils.map(applyDetailDO, AgreementApplyDetailDTO.class);
	}

	@Override
	public AgreementApplyDetailDTO queryById(Long id) {
		Assert.notNull(id, "明细id不能为空");
		AgreementApplyDetailDO detailDO = getById(id);
		return PojoUtils.map(detailDO, AgreementApplyDetailDTO.class);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean editApplyDetail(EditApplyDetailRequest request) {
		AgreementApplyDetailDTO detail = queryById(request.getId());
		if (ObjectUtil.isNull(detail)) {
			log.error("申请单明细不存在");
			return Boolean.FALSE;
		}
		if (!ApplyDetailTypeEnum.OTHER.getCode().equals(detail.getDetailType())) {
			throw new BusinessException(AgreementErrorCode.AGREEMENT_REBATE_UPDATE_DETAIL);
		}
		//操作金额
		BigDecimal amount = request.getAmount().subtract(detail.getAmount());
		//更新eas表金额
		enterpriseCustomerEasService.updateAppliedAmount(request.getEid(), request.getEasCode(), amount);
		//更新申请单金额
		agreementApplyService.updateTotalAmountById(request.getApplyId(), amount);
		//更新申请单明细
		AddRebateApplyDetailRequest updateDetailRequest = new AddRebateApplyDetailRequest();
		updateDetailRequest.setId(request.getId());
		updateDetailRequest.setAmount(request.getAmount());
		updateDetailRequest.setEntryDescribe(request.getEntryDescribe());
		updateDetailRequest.setOpUserId(request.getOpUserId());
		AgreementApplyDetailDO detailDO = PojoUtils.map(request, AgreementApplyDetailDO.class);
		//更新申请单状态为待审核
		AgreementApplyDO applyDO = new AgreementApplyDO();
		applyDO.setId(request.getApplyId());
		applyDO.setStatus(AgreementApplyStatusEnum.CHECK.getCode());
		applyDO.setOpUserId(request.getOpUserId());
		Boolean isSuccess = agreementApplyService.updateById(applyDO);
		if (!isSuccess) {
			throw new BusinessException(AgreementErrorCode.AGREEMENT_REBATE_UPDATE_APPLY);
		}
		//更细申请单
		isSuccess = updateById(detailDO);
		if (!isSuccess){
			throw new BusinessException(AgreementErrorCode.AGREEMENT_REBATE_UPDATE_APPLY);
		}
		//插入日志
		AddRebateLogRequest logRequest = new AddRebateLogRequest();
		logRequest.setDiscountAmount(amount);
		logRequest.setEasCode(request.getEasCode());
		//其他类型
		logRequest.setCashType(ApplyDetailTypeEnum.OTHER.getCode());
		logRequest.setLogName(request.getEntryDescribe());
		isSuccess =rebateLogService.batchSave(ListUtil.toList(logRequest));
		return isSuccess;
	}
}
