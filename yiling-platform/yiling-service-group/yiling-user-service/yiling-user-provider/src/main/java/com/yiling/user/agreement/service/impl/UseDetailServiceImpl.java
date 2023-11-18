package com.yiling.user.agreement.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreement.dao.UseDetailMapper;
import com.yiling.user.agreement.dto.UseDetailDTO;
import com.yiling.user.agreement.dto.request.QueryUseDetailListPageRequest;
import com.yiling.user.agreement.entity.UseDetailDO;
import com.yiling.user.agreement.service.UseDetailService;

import cn.hutool.core.collection.CollUtil;

/**
 * <p>
 * 协议返利申请使用明细表 服务实现类
 * </p>
 *
 * @author dexi.yao
 * @date 2021-08-03
 */
@Service
public class UseDetailServiceImpl extends BaseServiceImpl<UseDetailMapper, UseDetailDO> implements UseDetailService {

	@Override
	public Page<UseDetailDTO> queryUseDetailListPageList(QueryUseDetailListPageRequest request) {
		if (CollUtil.isEmpty(request.getUseIdList())){
			return new Page<>();
		}
		LambdaQueryWrapper<UseDetailDO> wrapper = Wrappers.lambdaQuery();
		wrapper.in(UseDetailDO::getUseId,request.getUseIdList());
		Page<UseDetailDO> page = page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
		return PojoUtils.map(page, UseDetailDTO.class);
	}
}
