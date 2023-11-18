package com.yiling.user.agreement.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreement.dao.UseMapper;
import com.yiling.user.agreement.dto.UseDTO;
import com.yiling.user.agreement.dto.request.AddUseRequest;
import com.yiling.user.agreement.dto.request.QueryUseListPageRequest;
import com.yiling.user.agreement.entity.UseDO;
import com.yiling.user.agreement.enums.AgreementErrorCode;
import com.yiling.user.agreement.service.UseDetailService;
import com.yiling.user.agreement.service.UseService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * 协议返利申请使用表 服务实现类
 * </p>
 *
 * @author dexi.yao
 * @date 2021-08-03
 */
@Service
public class UseServiceImpl extends BaseServiceImpl<UseMapper, UseDO> implements UseService {

	@Autowired
	UseDetailService useDetailService;

	@Override
	public UseDTO saveOrUpdate(AddUseRequest request) {
		UseDO useDO = PojoUtils.map(request, UseDO.class);
		boolean isAdd = saveOrUpdate(useDO);
		if (!isAdd) {
			throw new BusinessException(AgreementErrorCode.AGREEMENT_USE_ADD);
		}
		return PojoUtils.map(useDO, UseDTO.class);
	}

//	@Override
//	public Boolean withdrawAndReject(AddUseRequest request) {
//		UseDO useDO = PojoUtils.map(request, UseDO.class);
//		boolean update = saveOrUpdate(useDO);
//		//删除返利申请
//		Integer isDelete = deleteByIdWithFill(useDO);
//		if (isDelete.equals(0)) {
//			throw new BusinessException(AgreementErrorCode.AGREEMENT_USE_ADD);
//		}
//		//删除返利申请明细
//		LambdaQueryWrapper<UseDetailDO> wrapper = Wrappers.lambdaQuery();
//		wrapper.eq(UseDetailDO::getUseId, request.getId());
//		UseDetailDO useDetailDO = new UseDetailDO();
//		useDetailDO.setOpUserId(request.getOpUserId());
//		useDetailDO.setDelFlag(1);
//		isDelete = useDetailService.batchDeleteWithFill(useDetailDO, wrapper);
//		if (isDelete.equals(0)) {
//			throw new BusinessException(AgreementErrorCode.AGREEMENT_USE_ADD);
//		}
//		return Boolean.TRUE;
//	}

	@Override
	public List<UseDTO> queryUseList(List<String> applyIds) {
		if (CollUtil.isEmpty(applyIds)) {
			return ListUtil.toList();
		}
		LambdaQueryWrapper<UseDO> wrapper = Wrappers.lambdaQuery();
		wrapper.in(UseDO::getApplicantId, applyIds);
		List<UseDO> useDOList = list(wrapper);
		return PojoUtils.map(useDOList, UseDTO.class);
	}

	@Override
	public Page<UseDTO> queryUseListPageList(QueryUseListPageRequest request) {
		LambdaQueryWrapper<UseDO> wrapper = Wrappers.lambdaQuery();
		wrapper.in(CollUtil.isNotEmpty(request.getEidList()), UseDO::getEid, request.getEidList())
				.in(CollUtil.isNotEmpty(request.getCreateUserIdList()), UseDO::getCreateUser, request.getCreateUserIdList())
				.eq(StrUtil.isNotBlank(request.getEasCode()), UseDO::getEasCode, request.getEasCode())
				.eq(StrUtil.isNotBlank(request.getApplicantCode()), UseDO::getApplicantCode, request.getApplicantCode())
				.like(StrUtil.isNotBlank(request.getName()), UseDO::getName, request.getName())
				.orderByDesc(UseDO:: getCreateTime);
		Page<UseDO> page = page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
		return PojoUtils.map(page, UseDTO.class);
	}

	@Override
	public Map<Long, List<UseDTO>> queryUseListByEid(List<Long> eidList) {
		if (CollUtil.isEmpty(eidList)) {
			return MapUtil.newHashMap();
		}
		LambdaQueryWrapper<UseDO> wrapper = Wrappers.lambdaQuery();
		wrapper.in(UseDO::getEid, eidList);
		List<UseDO> list = list(wrapper);
		List<UseDTO> dtoList = PojoUtils.map(list, UseDTO.class);

		return dtoList.stream().collect(Collectors.groupingBy(UseDTO::getEid));
	}
}
