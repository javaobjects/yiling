package com.yiling.user.agreement.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreement.dao.AgreementRebateApplyMapper;
import com.yiling.user.agreement.dto.AgreementApplyDetailOpenDTO;
import com.yiling.user.agreement.dto.AgreementApplyOpenDTO;
import com.yiling.user.agreement.dto.AgreementDTO;
import com.yiling.user.agreement.dto.AgreementRebateApplyDTO;
import com.yiling.user.agreement.dto.ApplyEntDTO;
import com.yiling.user.agreement.dto.request.QueryApplyEntPageListRequest;
import com.yiling.user.agreement.dto.request.QueryApplyPageListRequest;
import com.yiling.user.agreement.dto.request.RebateApplyPageListItemDTO;
import com.yiling.user.agreement.entity.AgreementApplyDO;
import com.yiling.user.agreement.entity.AgreementApplyDetailDO;
import com.yiling.user.agreement.enums.AgreementApplyPushStatusEnum;
import com.yiling.user.agreement.enums.AgreementApplyStatusEnum;
import com.yiling.user.agreement.enums.AgreementErrorCode;
import com.yiling.user.agreement.enums.AgreementRebateApplyRangeTypeEnum;
import com.yiling.user.agreement.enums.ApplyDetailTypeEnum;
import com.yiling.user.agreement.service.AgreementApplyDetailService;
import com.yiling.user.agreement.service.AgreementApplyService;
import com.yiling.user.agreement.service.AgreementService;
import com.yiling.user.enterprise.api.EmployeeApi;
import com.yiling.user.enterprise.dto.EnterpriseEmployeeDTO;
import com.yiling.user.enterprise.entity.EnterpriseEmployeeDO;
import com.yiling.user.enterprise.service.EnterpriseEmployeeService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 返利申请表 服务实现类
 * </p>
 *
 * @author dexi.yao
 * @date 2021-07-26
 */
@Slf4j
@Service
public class AgreementApplyServiceImpl extends BaseServiceImpl<AgreementRebateApplyMapper, AgreementApplyDO> implements AgreementApplyService {

	@Autowired
	AgreementApplyDetailService applyDetailService;
	@Autowired
	AgreementService            agreementService;
	@Autowired
    EnterpriseEmployeeService enterpriseEmployeeService;

	@Override
	public Page<RebateApplyPageListItemDTO> queryRebateApplyPageList(QueryApplyPageListRequest request) {
		QueryWrapper<AgreementApplyDO> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda()
				.eq(AgreementApplyStatusEnum.SUCCESS
						.getCode().equals(request.getStatus()) || AgreementApplyStatusEnum.CHECK
						.getCode().equals(request.getStatus()) || AgreementApplyStatusEnum.REJECT
						.getCode().equals(request.getStatus()), AgreementApplyDO::getStatus, request.getStatus())
				.eq(StrUtil.isNotBlank(request.getEasCode()), AgreementApplyDO::getEasCode, request.getEasCode())
				.eq(ObjectUtil.isNotNull(request.getEid()), AgreementApplyDO::getEid, request.getEid())
				.eq(ObjectUtil.isNotNull(request.getYear()), AgreementApplyDO::getYear, request.getYear())
				.eq(ObjectUtil.isNotNull(request.getMonth()), AgreementApplyDO::getMonth, request.getMonth())
				.eq(ObjectUtil.isNotNull(request.getRangeType()), AgreementApplyDO::getRangeType, request.getRangeType())
				.ge(ObjectUtil.isNotNull(request.getStartDate()), AgreementApplyDO::getCreateTime, request.getStartDate())
				.le(ObjectUtil.isNotNull(request.getEndDate()), AgreementApplyDO::getCreateTime, request.getEndDate())
				.like(StrUtil.isNotBlank(request.getName()), AgreementApplyDO::getName, request.getName())
				.in(CollUtil.isNotEmpty(request.getCreateUserList()), AgreementApplyDO::getCreateUser, request.getCreateUserList())
				.like(StrUtil.isNotBlank(request.getCode()), AgreementApplyDO::getCode, request.getCode());
		if (request.getSequence().equals(1)) {
			queryWrapper.lambda().orderByAsc(AgreementApplyDO::getCreateTime);
		} else {
			queryWrapper.lambda().orderByDesc(AgreementApplyDO::getCreateTime);
		}
		Page<AgreementApplyDO> page = page(new Page<>(request.getCurrent(), request.getSize()), queryWrapper);
		return PojoUtils.map(page, RebateApplyPageListItemDTO.class);
	}

	@Override
	public AgreementRebateApplyDTO queryRebateApplyByCode(String code) {
		LambdaQueryWrapper<AgreementApplyDO> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(AgreementApplyDO::getCode, code);
		AgreementApplyDO applyDO = getOne(wrapper);
		return PojoUtils.map(applyDO, AgreementRebateApplyDTO.class);
	}

	@Override
	public List<AgreementApplyOpenDTO> queryAgreementApplyOpenList(Date startTime, Date endTime) {
		List<AgreementApplyOpenDTO> result = ListUtil.toList();

		LambdaQueryWrapper<AgreementApplyDO> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(AgreementApplyDO::getStatus, AgreementApplyStatusEnum.SUCCESS.getCode())
				.eq(AgreementApplyDO::getPushStatus, AgreementApplyPushStatusEnum.NOT_PUSHED.getCode());
		if (ObjectUtil.isNotNull(startTime)) {
			wrapper.ge(AgreementApplyDO::getCreateTime, startTime);
		}
		if (ObjectUtil.isNotNull(endTime)) {
			wrapper.le(AgreementApplyDO::getCreateTime, endTime);
		}
		//查询申请单
		List<AgreementApplyDO> applyList = list(wrapper);
		if (CollUtil.isEmpty(applyList)) {
			return result;
		}
		//查询申请明细
		Map<Long, AgreementApplyDO> applyMap = applyList.stream().collect(Collectors.toMap(AgreementApplyDO::getId, e -> e));
		LambdaQueryWrapper<AgreementApplyDetailDO> detailWrapper = Wrappers.lambdaQuery();
		detailWrapper.in(AgreementApplyDetailDO::getApplyId, applyMap.keySet());
		List<AgreementApplyDetailDO> detailList = applyDetailService.list(detailWrapper);
		Map<Long, List<AgreementApplyDetailDO>> applyDetailMap = detailList.stream().collect(Collectors.groupingBy(AgreementApplyDetailDO::getApplyId));

		//校验明细与申请单长度是否一致
		if (applyList.size() != applyDetailMap.size()) {
			applyList.forEach(e -> {
				List<AgreementApplyDetailDO> details = applyDetailMap.get(e.getId());
				if (CollUtil.isEmpty(details)) {
					log.error("查询待推送至冲红系统的申请单数据异常：申请单{" + e.getCode() + "}没有申请明细");
				}
			});
		}
		List<Long> agreementIdList = detailList.stream().map(AgreementApplyDetailDO::getAgreementId).distinct().collect(Collectors.toList());

		//组装数据
		//查询协议
		Map<Long, AgreementDTO> agreementMap = agreementService.getAgreementList(agreementIdList)
				.stream().collect(Collectors.toMap(AgreementDTO::getId, e -> e));
		//查询员工工号
		Map<Long, String> employeeCodeMap = enterpriseEmployeeService.listByEidUserIds(Constants.YILING_EID, applyList.stream()
				.map(AgreementApplyDO::getCreateUser).distinct().collect(Collectors.toList()))
				.stream().collect(Collectors.toMap(EnterpriseEmployeeDO::getId, EnterpriseEmployeeDO::getCode));
		for (AgreementApplyDO apply : applyList) {
			AgreementApplyOpenDTO applyOpen = PojoUtils.map(apply, AgreementApplyOpenDTO.class);
			applyOpen.setOwnerName(apply.getName());
			applyOpen.setOwnerCode(apply.getEasCode());
			applyOpen.setApplyId(apply.getId());
			applyOpen.setApplyCode(apply.getCode());
			applyOpen.setEntryTime(apply.getAuditTime());
			applyOpen.setYear(apply.getYear() + "年");
			applyOpen.setCreateUserCode(employeeCodeMap.getOrDefault(apply.getCreateUser(),""));
			StringBuffer stringBuffer = new StringBuffer();
			if (apply.getMonth() != 0) {
				stringBuffer.append(apply.getMonth());
			}
			stringBuffer.append(AgreementRebateApplyRangeTypeEnum.getByCode(apply.getRangeType()).getName());
			applyOpen.setMonth(stringBuffer.toString());
			List<AgreementApplyDetailDO> applyDetailList = applyDetailMap.get(apply.getId());
			List<AgreementApplyDetailOpenDTO> detailOpenList = PojoUtils.map(applyDetailList, AgreementApplyDetailOpenDTO.class);
			detailOpenList.forEach(e -> {
				if (ApplyDetailTypeEnum.AGREEMENT.getCode().equals(e.getDetailType())) {
					//补全协议信息
					AgreementDTO agreement = agreementMap.get(e.getAgreementId());
					e.setAgreementName(agreement.getName());
					e.setAgreementContent(agreement.getContent());
				}
			});
			applyOpen.setDetails(detailOpenList);
			result.add(applyOpen);
		}
		return result;
	}

	@Override
	public Boolean applyCompletePush(List<Long> applyIds) {
		AgreementApplyDO applyDO = new AgreementApplyDO();
		applyDO.setPushStatus(AgreementApplyPushStatusEnum.PUSHED.getCode());
		LambdaQueryWrapper<AgreementApplyDO> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(AgreementApplyDO::getPushStatus, AgreementApplyPushStatusEnum.NOT_PUSHED.getCode())
				.in(AgreementApplyDO::getId, applyIds);
		return update(applyDO, wrapper);
	}

	@Override
	public Map<Long, List<AgreementRebateApplyDTO>> queryRebateApplyListByEid(List<Long> eidList, AgreementApplyStatusEnum statusEnum) {
		if (CollUtil.isEmpty(eidList)) {
			return MapUtil.newHashMap();
		}
		LambdaQueryWrapper<AgreementApplyDO> wrapper = Wrappers.lambdaQuery();
		wrapper.in(AgreementApplyDO::getEid, eidList);
		if (statusEnum != null) {
			wrapper.eq(AgreementApplyDO::getStatus, statusEnum.getCode());
		}
		List<AgreementApplyDO> list = list(wrapper);
		List<AgreementRebateApplyDTO> applyDTOList = PojoUtils.map(list, AgreementRebateApplyDTO.class);

		return applyDTOList.stream().collect(Collectors.groupingBy(AgreementRebateApplyDTO::getEid));
	}

	@Override
	public Boolean updateTotalAmountById(Long id, BigDecimal amount) {
		Assert.notNull(id, "申请单id不能为空");
		Assert.notNull(amount, "操作金额不能为空");
		int row = baseMapper.updateTotalAmountById(id, amount);
		if (row > 0) {
			return Boolean.TRUE;
		} else {
			throw new BusinessException(AgreementErrorCode.AGREEMENT_REBATE_UPDATE_APPLY);
		}
	}

	@Override
	public Page<ApplyEntDTO> queryApplyEntPageList(QueryApplyEntPageListRequest request) {
		return this.baseMapper.queryApplyEntPageList(request.getPage(), request);
	}
}
