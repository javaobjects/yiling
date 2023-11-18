package com.yiling.payment.basic.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.payment.basic.dao.BankMapper;
import com.yiling.payment.basic.dto.BankDTO;
import com.yiling.payment.basic.dto.request.QueryBankPageListRequest;
import com.yiling.payment.basic.entity.BankDO;
import com.yiling.payment.basic.service.BankService;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * 银行表 服务实现类
 * </p>
 *
 * @author dexi.yao
 * @date 2021-11-10
 */
@Service
public class BankServiceImpl extends BaseServiceImpl<BankMapper, BankDO> implements BankService {

	@Override
	public Page<BankDTO> queryBankPageList(QueryBankPageListRequest request) {
		LambdaQueryWrapper<BankDO> wrapper = Wrappers.lambdaQuery();
		if (StrUtil.isNotBlank(request.getName())){
			wrapper.and(e->e.like( BankDO::getHeadName, request.getName())
					.or().like(BankDO::getBranchName, request.getName())
					.or().like( BankDO::getHeadSimpleName, request.getName()));
		}

		wrapper.and(e->e.eq(ObjectUtil.isNotNull(request.getType()) && ObjectUtil.notEqual(request.getType(), 0), BankDO::getType, request.getType()));
		Page<BankDO> page = page(request.getPage(), wrapper);
		return PojoUtils.map(page, BankDTO.class);
	}
}
