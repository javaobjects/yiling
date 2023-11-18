package com.yiling.sales.assistant.commissions.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.sales.assistant.commissions.dto.CommissionsUserDTO;
import com.yiling.sales.assistant.commissions.dto.StatisticsCommissionsUserDTO;
import com.yiling.sales.assistant.commissions.dto.request.QueryCommissionsUserPageListRequest;

/**
 * @author dexi.yao
 * @date 2021-09-17
 */
public interface CommissionsUserApi {


	/**
	 * 查询用户佣金列表
	 *
	 * @param request
	 * @return
	 */
	Page<CommissionsUserDTO> queryCommissionsUserPageList(QueryCommissionsUserPageListRequest request);

	/**
	 * 根据用户id批量查询佣金余额
	 *
	 * @param userIdList
	 * @return
	 */
	List<CommissionsUserDTO> batchQueryCommissionsUserByUserId(List<Long> userIdList);

	/**
	 * 统计平台佣金相关
	 * @return
	 */
	StatisticsCommissionsUserDTO statisticsCommissionsUser();
}
