package com.yiling.user.agreement.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreement.dao.AgreementRebateLogMapper;
import com.yiling.user.agreement.dto.AgreementRebateLogDTO;
import com.yiling.user.agreement.dto.request.AddRebateLogRequest;
import com.yiling.user.agreement.dto.request.QueryAgreementRebateLogPageListRequest;
import com.yiling.user.agreement.entity.AgreementRebateLogDO;
import com.yiling.user.agreement.enums.AgreementCashTypeEnum;
import com.yiling.user.agreement.service.AgreementRebateLogService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;

/**
 * <p>
 * 协议兑付日志表 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-07-12
 */
@Service
public class AgreementRebateLogServiceImpl extends BaseServiceImpl<AgreementRebateLogMapper, AgreementRebateLogDO> implements AgreementRebateLogService {

	@Override
	public Map<String, BigDecimal> queryEntAccountDiscountAmount(List<String> accounts, AgreementCashTypeEnum cashTypeEnum) {
		Map<String, BigDecimal> result=MapUtil.newHashMap();
		//批量查询企业账号下的以兑付记录
		List<AgreementRebateLogDO> logDOS = queryEntAccountDiscountAmountList(accounts,cashTypeEnum);
		Map<String, List<AgreementRebateLogDO>> logMap = logDOS.stream().collect(Collectors.groupingBy(AgreementRebateLogDO::getEasCode));
		logMap.forEach((s, logList) -> {
			BigDecimal totalAmount=BigDecimal.ZERO;
			for(AgreementRebateLogDO item:logList){
				totalAmount=totalAmount.add(item.getDiscountAmount());
			}
			result.put(s,totalAmount);
		});
		return result;
	}

	@Override
	public List<AgreementRebateLogDO> queryEntAccountDiscountAmountList(List<String> accounts, AgreementCashTypeEnum cashTypeEnum) {
		if (CollUtil.isEmpty(accounts)){
			return ListUtil.toList();
		}
		QueryWrapper<AgreementRebateLogDO> queryWrapper=new QueryWrapper<>();
		queryWrapper.lambda().in(AgreementRebateLogDO::getEasCode,accounts);
		if (cashTypeEnum!=null){
			queryWrapper.lambda().eq(AgreementRebateLogDO::getCashType,cashTypeEnum.getCode());
		}
		//查询兑付日志
		List<AgreementRebateLogDO> logDOS = list(queryWrapper);
		return logDOS;
	}

	@Override
	public Page<AgreementRebateLogDTO> queryAgreementRebateLogPageList(QueryAgreementRebateLogPageListRequest request) {
		QueryWrapper<AgreementRebateLogDO> queryWrapper=new QueryWrapper<>();
		queryWrapper.lambda().in(AgreementRebateLogDO::getEasCode,request.getAccounts());
		if (request.getCashTypeEnum()!=null){
			queryWrapper.lambda().eq(AgreementRebateLogDO::getCashType,request.getCashTypeEnum().getCode());
		}
		Page<AgreementRebateLogDO> page = page(new Page<>(request.getCurrent(), request.getSize()), queryWrapper);
		return PojoUtils.map(page,AgreementRebateLogDTO.class);
	}

	@Override
	public Boolean batchSave(List<AddRebateLogRequest> list) {
		List<AgreementRebateLogDO> logDOS = PojoUtils.map(list, AgreementRebateLogDO.class);
		return saveBatch(logDOS);
	}
}
