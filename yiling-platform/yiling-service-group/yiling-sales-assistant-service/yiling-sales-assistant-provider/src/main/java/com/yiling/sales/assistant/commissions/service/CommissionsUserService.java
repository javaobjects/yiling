package com.yiling.sales.assistant.commissions.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.sales.assistant.commissions.dto.CommissionsUserDTO;
import com.yiling.sales.assistant.commissions.dto.StatisticsCommissionsUserDTO;
import com.yiling.sales.assistant.commissions.dto.request.QueryCommissionsUserPageListRequest;
import com.yiling.sales.assistant.commissions.entity.CommissionsDO;
import com.yiling.sales.assistant.commissions.entity.CommissionsUserDO;

/**
 * <p>
 * 销售助手用户佣金表 服务类
 * </p>
 *
 * @author dexi.yao
 * @date 2021-09-17
 */
public interface CommissionsUserService extends BaseService<CommissionsUserDO> {

	/**
	 * 根据用户id查询佣金余额
	 *
	 * @param userId
	 * @return
	 */
	CommissionsUserDTO queryCommissionsUserByUserId(Long userId);


	/**
	 * 根据用户id批量查询佣金余额
	 *
	 * @param userIdList
	 * @return
	 */
	List<CommissionsUserDTO> batchQueryCommissionsUserByUserId(List<Long> userIdList);

	/**
	 * 佣金生效后更新用户佣金表相应金额
	 *
	 * @param commissions 佣金记录
	 * @return
	 */
	Boolean addUserCommissions(CommissionsDO commissions);

	/**
	 * 查询用户佣金列表
	 *
	 * @param request
	 * @return
	 */
	Page<CommissionsUserDTO> queryCommissionsUserPageList(QueryCommissionsUserPageListRequest request);

	/**
	 * 统计平台佣金相关
	 * @return
	 */
	StatisticsCommissionsUserDTO statisticsCommissionsUser();
}
