package com.yiling.user.agreement.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreement.api.UseDetailApi;
import com.yiling.user.agreement.dto.UseDetailDTO;
import com.yiling.user.agreement.dto.request.AddUseDetailRequest;
import com.yiling.user.agreement.dto.request.QueryUseDetailListPageRequest;
import com.yiling.user.agreement.entity.UseDetailDO;
import com.yiling.user.agreement.enums.AgreementErrorCode;
import com.yiling.user.agreement.service.UseDetailService;

/**
 * @author dexi.yao
 * @date 2021-08-03
 */
@DubboService
public class UseDetailApiImpl implements UseDetailApi {
	@Autowired
	UseDetailService useDetailService;


	@Override
	public List<UseDetailDTO> batchSave(List<AddUseDetailRequest> requests) {
		List<UseDetailDO> useDetailList = PojoUtils.map(requests, UseDetailDO.class);
		boolean isAdd = useDetailService.saveBatch(useDetailList);
		if (!isAdd) {
			throw new BusinessException(AgreementErrorCode.AGREEMENT_USE_DETAIL_ADD);
		}
		return PojoUtils.map(useDetailList, UseDetailDTO.class);
	}

	@Override
	public Page<UseDetailDTO> queryUseDetailListPageList(QueryUseDetailListPageRequest request) {
		return useDetailService.queryUseDetailListPageList(request);
	}
}
