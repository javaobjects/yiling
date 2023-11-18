package com.yiling.settlement.b2b.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.settlement.b2b.dao.ReceiptAccountMapper;
import com.yiling.settlement.b2b.dto.ReceiptAccountDTO;
import com.yiling.settlement.b2b.dto.request.QueryReceiptAccountPageListRequest;
import com.yiling.settlement.b2b.dto.request.SaveOrUpdateReceiptAccountRequest;
import com.yiling.settlement.b2b.entity.ReceiptAccountDO;
import com.yiling.settlement.b2b.enums.ReceiptAccountStatusEnum;
import com.yiling.settlement.b2b.enums.ReceiptAccountValidEnum;
import com.yiling.settlement.b2b.enums.SettlementErrorCode;
import com.yiling.settlement.b2b.service.ReceiptAccountService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * b2b企业收款账户表 服务实现类
 * </p>
 *
 * @author dexi.yao
 * @date 2021-10-27
 */
@Service
@Slf4j
public class ReceiptAccountServiceImpl extends BaseServiceImpl<ReceiptAccountMapper, ReceiptAccountDO> implements ReceiptAccountService {

	@Override
	public ReceiptAccountDTO queryValidReceiptAccountByEid(Long eid) {
		LambdaQueryWrapper<ReceiptAccountDO> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(ReceiptAccountDO::getEid, eid);
		wrapper.eq(ReceiptAccountDO::getInvalid, ReceiptAccountValidEnum.VALID.getCode());
		ReceiptAccountDO accountDO = getOne(wrapper);
		return PojoUtils.map(accountDO, ReceiptAccountDTO.class);
	}

	@Override
	public List<ReceiptAccountDTO> queryValidReceiptAccountByEidList(List<Long> eidList) {
		LambdaQueryWrapper<ReceiptAccountDO> wrapper = Wrappers.lambdaQuery();
		wrapper.in(ReceiptAccountDO::getEid, eidList);
		wrapper.eq(ReceiptAccountDO::getInvalid, ReceiptAccountValidEnum.VALID.getCode());
		List<ReceiptAccountDO> list = list(wrapper);
		return PojoUtils.map(list, ReceiptAccountDTO.class);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean submitAuditReceiptAccount(SaveOrUpdateReceiptAccountRequest request) {
		//如果是更新
		if (ObjectUtil.isNotNull(request.getReceiptAccountId())) {
			ReceiptAccountDO accountDO = getById(request.getReceiptAccountId());
			if (ObjectUtil.isNull(accountDO)){
				log.error("企业收款账户不存在，id={}", request.getReceiptAccountId());
				throw new BusinessException(SettlementErrorCode.ACCOUNT_NOT_FOUND);
			}
			if (!ObjectUtil.equal(accountDO.getEid(), request.getEid())) {
				log.error("收款账户的eid与当前更者的eid不匹配，id={},operatorEid={}", request.getReceiptAccountId(), request.getEid());
				throw new BusinessException(SettlementErrorCode.ACCOUNT_EID_INVALID);
			}
			if (ObjectUtil.equal(accountDO.getInvalid(), ReceiptAccountValidEnum.INVALID.getCode())) {
				log.error("状态为失效的企业收款账户不允许修改，id={}", request.getReceiptAccountId());
				throw new BusinessException(SettlementErrorCode.ACCOUNT_INVALID);
			}
			if (ObjectUtil.equal(accountDO.getStatus(), ReceiptAccountStatusEnum.WAIT.getCode())) {
				log.error("状态为审核中的企业收款账户不允许修改，id={}", request.getReceiptAccountId());
				throw new BusinessException(SettlementErrorCode.ACCOUNT_STATUS_INVALID);
			}
			ReceiptAccountDO accountInfo = PojoUtils.map(request, ReceiptAccountDO.class);
			accountInfo.setStatus(ReceiptAccountStatusEnum.WAIT.getCode());
            if (ObjectUtil.isNull(request.getBranchBankId())||ObjectUtil.equal(request.getBranchBankId(),0L)){
                accountInfo.setBranchBankId(0L);
                accountInfo.setBranchNum("");
                accountInfo.setSubBankName("");
            }
			//如果在审核成功的基础上修改
			if (ObjectUtil.equal(accountDO.getStatus(), ReceiptAccountStatusEnum.SUCCESS.getCode())) {
				accountDO.setInvalid(ReceiptAccountValidEnum.INVALID.getCode());
				//当前收款账户置为失效
				boolean isSuccess = updateById(accountDO);
				if (!isSuccess) {
					log.error("更新企业收款账户为失效状态操作失败，id={}", request.getReceiptAccountId());
					throw new BusinessException(SettlementErrorCode.ACCOUNT_UPDATE_FAIL);
				}
				accountInfo.setId(null);
				accountInfo.setInvalid(ReceiptAccountValidEnum.VALID.getCode());
			} else {
				//如果在驳回的状态下修改
				accountInfo.setId(request.getReceiptAccountId());
			}
			accountInfo.setSubmitTime(new Date());
			return saveOrUpdate(accountInfo);
		} else {
			//如果是新增
			ReceiptAccountDTO accountDTO = queryValidReceiptAccountByEid(request.getEid());
			if (ObjectUtil.isNotNull(accountDTO)) {
				log.error("当前企业存在企业收款账户，不能再新增，id={}", accountDTO.getId());
				throw new BusinessException(SettlementErrorCode.ACCOUNT_TOO_MANY);
			}
			ReceiptAccountDO accountInfo = PojoUtils.map(request, ReceiptAccountDO.class);
			accountInfo.setStatus(ReceiptAccountStatusEnum.WAIT.getCode());
			accountInfo.setInvalid(ReceiptAccountValidEnum.VALID.getCode());
			accountInfo.setSubmitTime(new Date());
			return save(accountInfo);
		}
	}

	@Override
	public Page<ReceiptAccountDTO> queryReceiptAccountPageList(QueryReceiptAccountPageListRequest request) {
		LambdaQueryWrapper<ReceiptAccountDO> wrapper=Wrappers.lambdaQuery();
		wrapper.in(CollUtil.isNotEmpty(request.getEidList()), ReceiptAccountDO::getEid, request.getEidList());
		wrapper.eq(ObjectUtil.isNotNull(request.getStatus())&&!ObjectUtil.equal(request.getStatus(), 0),
				ReceiptAccountDO::getStatus, request.getStatus());
		wrapper.eq(ReceiptAccountDO::getInvalid, ReceiptAccountValidEnum.VALID.getCode());
		if (ObjectUtil.isNotNull(request.getMinDate())){
			wrapper.ge( ReceiptAccountDO::getCreateTime, DateUtil.beginOfDay(request.getMinDate()));
		}
		if (ObjectUtil.isNotNull(request.getMaxDate())){
			wrapper.le( ReceiptAccountDO::getCreateTime, DateUtil.endOfDay(request.getMaxDate()));
		}
		wrapper.orderByDesc(ReceiptAccountDO::getCreateTime);
		Page<ReceiptAccountDO> page = page(request.getPage(), wrapper);
		return PojoUtils.map(page,ReceiptAccountDTO.class);
	}
}
