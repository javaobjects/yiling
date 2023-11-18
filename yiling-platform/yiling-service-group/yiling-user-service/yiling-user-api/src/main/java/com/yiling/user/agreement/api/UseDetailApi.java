package com.yiling.user.agreement.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.agreement.dto.UseDetailDTO;
import com.yiling.user.agreement.dto.request.AddUseDetailRequest;
import com.yiling.user.agreement.dto.request.QueryUseDetailListPageRequest;

/**
 * @author dexi.yao
 * @date 2021-08-03
 */
public interface UseDetailApi {

	/**
	 * 批量新增
	 *
	 * @param requests
	 * @return
	 */
	List<UseDetailDTO> batchSave(List<AddUseDetailRequest> requests);

	/**
	 * 分页查询返利申请使用明细记录
	 *
	 * @param request
	 * @return
	 */
	Page<UseDetailDTO> queryUseDetailListPageList(QueryUseDetailListPageRequest request);
}
