package com.yiling.sales.assistant.commissions.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sales.assistant.commissions.api.CommissionsDetailApi;
import com.yiling.sales.assistant.commissions.dto.CommissionsDetailDTO;
import com.yiling.sales.assistant.commissions.dto.request.BatchQueryCommissionsDetailPageListRequest;
import com.yiling.sales.assistant.commissions.dto.request.QueryCommissionsDetailPageListRequest;
import com.yiling.sales.assistant.commissions.entity.CommissionsDetailDO;
import com.yiling.sales.assistant.commissions.service.CommissionsDetailService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;

/**
 * @author dexi.yao
 * @date 2021-09-22
 */
@DubboService
public class CommissionsDetailApiImpl implements CommissionsDetailApi {

	@Autowired
	CommissionsDetailService commissionsDetailService;

	@Override
	public CommissionsDetailDTO queryById(Long id) {
		CommissionsDetailDO commissionsDetailDO = commissionsDetailService.getById(id);

		return PojoUtils.map(commissionsDetailDO, CommissionsDetailDTO.class);
	}

	@Override
	public List<CommissionsDetailDTO> batchQueryById(List<Long> idList) {
		if (CollUtil.isEmpty(idList)) {
			return ListUtil.toList();
		}
		List<CommissionsDetailDTO> detailDTOS = batchQueryById(idList);
		return PojoUtils.map(detailDTOS, CommissionsDetailDTO.class);
	}

	@Override
	public Page<CommissionsDetailDTO> queryCommissionsDetailPageList(QueryCommissionsDetailPageListRequest request) {
		return commissionsDetailService.queryCommissionsDetailPageList(request);
	}

	@Override
	public Page<CommissionsDetailDTO> batchQueryCommissionsDetailPageList(BatchQueryCommissionsDetailPageListRequest request) {
		return commissionsDetailService.batchQueryCommissionsDetailPageList(request);
	}
}
