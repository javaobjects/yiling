package com.yiling.sales.assistant.commissions.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sales.assistant.commissions.dao.CommissionsUserMapper;
import com.yiling.sales.assistant.commissions.dto.CommissionsUserDTO;
import com.yiling.sales.assistant.commissions.dto.StatisticsCommissionsUserDTO;
import com.yiling.sales.assistant.commissions.dto.request.QueryCommissionsUserPageListRequest;
import com.yiling.sales.assistant.commissions.entity.CommissionsDO;
import com.yiling.sales.assistant.commissions.entity.CommissionsUserDO;
import com.yiling.sales.assistant.commissions.service.CommissionsUserService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;

/**
 * <p>
 * 销售助手用户佣金表 服务实现类
 * </p>
 *
 * @author dexi.yao
 * @date 2021-09-17
 */
@Service
public class CommissionsUserServiceImpl extends BaseServiceImpl<CommissionsUserMapper, CommissionsUserDO> implements CommissionsUserService {

	@Override
	public CommissionsUserDTO queryCommissionsUserByUserId(Long userId) {
		if (ObjectUtil.isNull(userId)) {
			return null;
		}
		LambdaQueryWrapper<CommissionsUserDO> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(CommissionsUserDO::getUserId, userId);
		CommissionsUserDO commissionsUser = getOne(wrapper);
		//如果用户还没有余额
		if (ObjectUtil.isNull(commissionsUser)) {
			CommissionsUserDTO commissionsUserDTO = new CommissionsUserDTO();
			commissionsUserDTO.setSurplusAmount(BigDecimal.ZERO);
			commissionsUserDTO.setUserId(userId);
			return commissionsUserDTO;
		}
		return PojoUtils.map(commissionsUser, CommissionsUserDTO.class);
	}

	@Override
	public List<CommissionsUserDTO> batchQueryCommissionsUserByUserId(List<Long> userIdList) {
		if (CollUtil.isEmpty(userIdList)) {
			return ListUtil.toList();
		}
		LambdaQueryWrapper<CommissionsUserDO> wrapper = Wrappers.lambdaQuery();
		wrapper.in(CommissionsUserDO::getUserId, userIdList);
		List<CommissionsUserDO> list = list(wrapper);
		return PojoUtils.map(list, CommissionsUserDTO.class);
	}

	@Override
	public Boolean addUserCommissions(CommissionsDO commissions) {
		CommissionsUserDO commissionsUserDO;
		CommissionsUserDTO commissionsUserDTO = queryCommissionsUserByUserId(commissions.getUserId());
		if (ObjectUtil.isNull(commissionsUserDTO.getTotalAmount())) {
			commissionsUserDO = new CommissionsUserDO();
			commissionsUserDO.setUserId(commissions.getUserId());
			commissionsUserDO.setSurplusAmount(commissions.getAmount());
			commissionsUserDO.setTotalAmount(commissions.getAmount());
		} else {
			commissionsUserDO = PojoUtils.map(commissionsUserDTO, CommissionsUserDO.class);
			commissionsUserDO.setSurplusAmount(commissionsUserDO.getSurplusAmount().add(commissions.getAmount()));
			commissionsUserDO.setTotalAmount(commissionsUserDO.getTotalAmount().add(commissions.getAmount()));
		}
		commissionsUserDO.setOpUserId(commissions.getOpUserId());
		return saveOrUpdate(commissionsUserDO);
	}

	@Override
	public Page<CommissionsUserDTO> queryCommissionsUserPageList(QueryCommissionsUserPageListRequest request) {
        LambdaQueryWrapper<CommissionsUserDO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(CollUtil.isNotEmpty(request.getUserIdList()),CommissionsUserDO::getUserId,request.getUserIdList());
        Page<CommissionsUserDO> page = page(request.getPage(), wrapper);
        return PojoUtils.map(page,CommissionsUserDTO.class);
	}

	@Override
	public StatisticsCommissionsUserDTO statisticsCommissionsUser() {
		return this.baseMapper.statisticsCommissionsUser();
	}
}
