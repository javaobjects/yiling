package com.yiling.settlement.b2b.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.settlement.b2b.dao.SettlementDetailMapper;
import com.yiling.settlement.b2b.dto.SettlementDetailDTO;
import com.yiling.settlement.b2b.dto.request.QuerySettlementDetailPageListRequest;
import com.yiling.settlement.b2b.entity.SettlementDetailDO;
import com.yiling.settlement.b2b.service.SettlementDetailService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;

/**
 * <p>
 * b2b商家结算单明细表 服务实现类
 * </p>
 *
 * @author dexi.yao
 * @date 2021-10-19
 */
@Service
public class SettlementDetailServiceImpl extends BaseServiceImpl<SettlementDetailMapper, SettlementDetailDO> implements SettlementDetailService {

	@Override
	public Page<SettlementDetailDTO> querySettlementDetailPageList(QuerySettlementDetailPageListRequest request) {
		LambdaQueryWrapper<SettlementDetailDO> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(SettlementDetailDO::getSettlementId, request.getSettlementId());
		wrapper.orderByDesc(SettlementDetailDO::getOrderId);
		Page<SettlementDetailDO> page = page(request.getPage(), wrapper);

		return PojoUtils.map(page, SettlementDetailDTO.class);
	}

	@Override
	public List<SettlementDetailDTO> querySettlementDetailByOrderId(Long orderId) {

		LambdaQueryWrapper<SettlementDetailDO> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(SettlementDetailDO::getOrderId, orderId);
		List<SettlementDetailDO> list = list(wrapper);
		return PojoUtils.map(list, SettlementDetailDTO.class);
	}

	@Override
	public List<SettlementDetailDTO> querySettlementDetailBySettlementId(List<Long> settlementIdList) {
		if (CollUtil.isEmpty(settlementIdList)){
			return ListUtil.toList();
		}
		LambdaQueryWrapper<SettlementDetailDO> wrapper = Wrappers.lambdaQuery();
		wrapper.in(SettlementDetailDO::getSettlementId, settlementIdList);
		wrapper.orderByDesc(SettlementDetailDO::getSettlementId);
		List<SettlementDetailDO> list = list(wrapper);
		return PojoUtils.map(list, SettlementDetailDTO.class);
	}
}
