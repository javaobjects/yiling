package com.yiling.sales.assistant.commissions.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.sales.assistant.commissions.dto.CommissionsDTO;
import com.yiling.sales.assistant.commissions.dto.CommissionsUserStatisticsDTO;
import com.yiling.sales.assistant.commissions.dto.request.AddCommissionsToUserRequest;
import com.yiling.sales.assistant.commissions.dto.request.CommissionsPayRequest;
import com.yiling.sales.assistant.commissions.dto.request.QueryCommissionsPageListRequest;
import com.yiling.sales.assistant.commissions.dto.request.RemoveCommissionsToUserRequest;
import com.yiling.sales.assistant.commissions.dto.request.UpdateCommissionsEffectiveRequest;

/**
 * @author dexi.yao
 * @date 2021-09-17
 */
public interface CommissionsApi {


	/**
	 * 根据佣金记录id查询
	 *
	 * @param id
	 * @return
	 */
	CommissionsDTO queryById(Long id);

	/**
	 * 为用户新增佣金记录
	 *
	 * @param request
	 * @return
	 */
	Boolean addCommissionsToUser(AddCommissionsToUserRequest request);

	/**
	 * 使用户佣金生效
	 *
	 * @param request
	 * @return
	 */
	Boolean updateCommissionsEffective(UpdateCommissionsEffectiveRequest request);

	/**
	 * 统计用户收益
	 *
	 * @param userId 用户id
	 * @return
	 */
	CommissionsUserStatisticsDTO commissionsUserStatistics(Long userId);

	/**
	 * 分页查询收支明细
	 *
	 * @param request
	 * @return
	 */
	Page<CommissionsDTO> queryCommissionsPageList(QueryCommissionsPageListRequest request);

	/**
	 * 佣金兑付
	 *
	 * @param request
	 * @return
	 */
	Boolean commissionsPay(List<CommissionsPayRequest> request);

}
