package com.yiling.user.agreement.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreement.dao.ApplyOrderMapper;
import com.yiling.user.agreement.dto.ApplyOrderDTO;
import com.yiling.user.agreement.dto.request.AddApplyOrderRequest;
import com.yiling.user.agreement.dto.request.QueryApplyOrderPageListRequest;
import com.yiling.user.agreement.entity.ApplyOrderDO;
import com.yiling.user.agreement.service.ApplyOrderService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;

/**
 * <p>
 * 协议申请订单关联表 服务实现类
 * </p>
 *
 * @author dexi.yao
 * @date 2021-07-30
 */
@Service
public class ApplyOrderServiceImpl extends BaseServiceImpl<ApplyOrderMapper, ApplyOrderDO> implements ApplyOrderService {

	@Override
	public List<ApplyOrderDTO> queryApplyOrderList(List<Long> applyIdList) {
		if (CollUtil.isEmpty(applyIdList)) {
			return ListUtil.toList();
		}
		LambdaQueryWrapper<ApplyOrderDO> wrapper = Wrappers.lambdaQuery();
		wrapper.in(ApplyOrderDO::getApplyId, applyIdList);
		List<ApplyOrderDO> list = list(wrapper);
		return PojoUtils.map(list, ApplyOrderDTO.class);
	}

	@Override
	public Page<ApplyOrderDTO> queryApplyOrderPageList(QueryApplyOrderPageListRequest request) {
		Assert.notNull(request.getApplyId(), "申请单id不能为空");
		LambdaQueryWrapper<ApplyOrderDO> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(ApplyOrderDO::getApplyId, request.getApplyId());
		wrapper.eq(ObjectUtil.isNotNull(request.getAgreementId()),ApplyOrderDO::getAgreementId, request.getAgreementId());
		Page<ApplyOrderDO> page = page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
		return PojoUtils.map(page, ApplyOrderDTO.class);
	}

	@Override
	public List<ApplyOrderDTO> batchSaveOrUpdate(List<AddApplyOrderRequest> requests) {
		if (CollUtil.isEmpty(requests)) {
			return ListUtil.toList();
		}
		List<ApplyOrderDO> list = PojoUtils.map(requests, ApplyOrderDO.class);
		boolean isSuccess = saveOrUpdateBatch(list);
		if (!isSuccess) {
			throw new BusinessException();
		}

		return PojoUtils.map(list, ApplyOrderDTO.class);
	}
}
