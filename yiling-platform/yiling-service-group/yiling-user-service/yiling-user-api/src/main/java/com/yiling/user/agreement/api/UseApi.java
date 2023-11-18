package com.yiling.user.agreement.api;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.agreement.dto.UseDTO;
import com.yiling.user.agreement.dto.request.AddUseRequest;
import com.yiling.user.agreement.dto.request.QueryUseListPageRequest;

/**
 * @author dexi.yao
 * @date 2021-08-03
 */
public interface UseApi {

	/**
	 * 保存或更新返利申请使用记录
	 *
	 * @param request
	 * @return
	 */
	UseDTO saveOrUpdate(AddUseRequest request);

	/**
	 * 根据id查询申请单
	 *
	 * @param id
	 * @return
	 */
	UseDTO queryById(Long id);

//	/**
//	 * 撤销或驳回返利申请使用
//	 *
//	 * @param request
//	 * @return
//	 */
//	Boolean withdrawAndReject(AddUseRequest request);


	/**
	 * 根据申请单id查询返利申请单
	 *
	 * @param applyIds
	 * @return
	 */
	List<UseDTO> queryUseList(List<String> applyIds);

	/**
	 * 分页查询返利申请使用记录
	 *
	 * @param request
	 * @return
	 */
	Page<UseDTO> queryUseListPageList(QueryUseListPageRequest request);

	/**
	 * 根据申请单id查询返利申请单
	 *
	 * @param eidList
	 * @return
	 */
	Map<Long, List<UseDTO>> queryUseListByEid(List<Long> eidList);

}
