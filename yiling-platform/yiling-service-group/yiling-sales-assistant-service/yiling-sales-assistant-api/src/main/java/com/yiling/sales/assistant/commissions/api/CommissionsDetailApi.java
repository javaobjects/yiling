package com.yiling.sales.assistant.commissions.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.sales.assistant.commissions.dto.CommissionsDetailDTO;
import com.yiling.sales.assistant.commissions.dto.request.BatchQueryCommissionsDetailPageListRequest;
import com.yiling.sales.assistant.commissions.dto.request.QueryCommissionsDetailPageListRequest;

/**
 * @author dexi.yao
 * @date 2021-09-22
 */
public interface CommissionsDetailApi {

	/**
	 * 根据佣金明细id查询佣金明细
	 *
	 * @param id
	 * @return
	 */
	CommissionsDetailDTO queryById(Long id);

	/**
	 * 根据佣金明细id批量查询佣金明细
	 *
	 * @param idList
	 * @return
	 */
	List<CommissionsDetailDTO> batchQueryById(List<Long> idList);

	/**
	 * 查询佣金记录的明细
	 *
	 * @param request
	 * @return
	 */
	Page<CommissionsDetailDTO> queryCommissionsDetailPageList(QueryCommissionsDetailPageListRequest request);

	/**
	 * 批量查询佣金记录的明细
	 *
	 * @param request
	 * @return
	 */
	Page<CommissionsDetailDTO> batchQueryCommissionsDetailPageList(BatchQueryCommissionsDetailPageListRequest request);


}
