package com.yiling.user.agreement.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.agreement.dto.ApplyOrderDTO;
import com.yiling.user.agreement.dto.request.AddApplyOrderRequest;
import com.yiling.user.agreement.dto.request.QueryApplyOrderPageListRequest;

/**
 * @author dexi.yao
 * @date 2021-07-30
 */
public interface ApplyOrderApi {

	/**
	 * 根据返利申请id查询申请单内的订单
	 *
	 * @param applyIdList
	 * @return
	 */
	List<ApplyOrderDTO> queryApplyOrderList(List<Long> applyIdList);

	/**
	 * 根据返利申请id分页查询申请单内的订单
	 *
	 * @param request
	 * @return
	 */
	Page<ApplyOrderDTO> queryApplyOrderPageList(QueryApplyOrderPageListRequest request);

	/**
	 * 批量新增或更新
	 *
	 * @param requests
	 * @return
	 */
	List<ApplyOrderDTO> batchSaveOrUpdate(List<AddApplyOrderRequest> requests);

	/**
	 * 主键查询申请单订单关联表
	 *
	 * @param id
	 * @return
	 */
	ApplyOrderDTO queryById(Long id);
}
