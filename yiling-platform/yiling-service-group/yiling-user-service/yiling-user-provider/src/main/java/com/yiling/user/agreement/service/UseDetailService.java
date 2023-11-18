package com.yiling.user.agreement.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.user.agreement.dto.UseDetailDTO;
import com.yiling.user.agreement.dto.request.QueryUseDetailListPageRequest;
import com.yiling.user.agreement.entity.UseDetailDO;

/**
 * <p>
 * 协议返利申请使用明细表 服务类
 * </p>
 *
 * @author dexi.yao
 * @date 2021-08-03
 */
public interface UseDetailService extends BaseService<UseDetailDO> {

	/**
	 * 分页查询返利申请使用明细记录
	 *
	 * @param request
	 * @return
	 */
	Page<UseDetailDTO> queryUseDetailListPageList(QueryUseDetailListPageRequest request);
}
