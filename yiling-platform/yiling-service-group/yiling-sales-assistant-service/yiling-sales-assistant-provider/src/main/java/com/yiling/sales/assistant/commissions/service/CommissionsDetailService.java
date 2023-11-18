package com.yiling.sales.assistant.commissions.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.sales.assistant.commissions.dto.CommissionsDetailDTO;
import com.yiling.sales.assistant.commissions.dto.request.BatchQueryCommissionsDetailPageListRequest;
import com.yiling.sales.assistant.commissions.dto.request.QueryCommissionsDetailPageListRequest;
import com.yiling.sales.assistant.commissions.entity.CommissionsDetailDO;

/**
 * <p>
 * 销售助手佣金明细表 服务类
 * </p>
 *
 * @author dexi.yao
 * @date 2021-09-17
 */
public interface CommissionsDetailService extends BaseService<CommissionsDetailDO> {


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

    /**
     * 根据佣金id和订单号查询佣金明细
     * @param commId
     * @param orderCode
     * @return
     */
    CommissionsDetailDTO queryCommDetail(Long commId,String orderCode);
}
