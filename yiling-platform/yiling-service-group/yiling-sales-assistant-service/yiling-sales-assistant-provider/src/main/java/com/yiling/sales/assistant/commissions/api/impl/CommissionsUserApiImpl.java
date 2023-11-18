package com.yiling.sales.assistant.commissions.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.sales.assistant.commissions.api.CommissionsUserApi;
import com.yiling.sales.assistant.commissions.dto.CommissionsUserDTO;
import com.yiling.sales.assistant.commissions.dto.StatisticsCommissionsUserDTO;
import com.yiling.sales.assistant.commissions.dto.request.QueryCommissionsUserPageListRequest;
import com.yiling.sales.assistant.commissions.service.CommissionsUserService;

/**
 * @author dexi.yao
 * @date 2021-09-17
 */
@DubboService
public class CommissionsUserApiImpl implements CommissionsUserApi {

	@Autowired
	CommissionsUserService commissionsUserService;

	@Override
	public Page<CommissionsUserDTO> queryCommissionsUserPageList(QueryCommissionsUserPageListRequest request) {
		return commissionsUserService.queryCommissionsUserPageList(request);
	}

	@Override
	public List<CommissionsUserDTO> batchQueryCommissionsUserByUserId(List<Long> userIdList) {
		return commissionsUserService.batchQueryCommissionsUserByUserId(userIdList);
	}

	@Override
	public StatisticsCommissionsUserDTO statisticsCommissionsUser() {
		return commissionsUserService.statisticsCommissionsUser();
	}
}
