
package com.yiling.user.agreement.api.impl;

import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreement.api.UseApi;
import com.yiling.user.agreement.dto.UseDTO;
import com.yiling.user.agreement.dto.request.AddUseRequest;
import com.yiling.user.agreement.dto.request.QueryUseListPageRequest;
import com.yiling.user.agreement.entity.UseDO;
import com.yiling.user.agreement.service.UseService;

/**
 * @author dexi.yao
 * @date 2021-08-03
 */
@DubboService
public class UseApiImpl implements UseApi {
	@Autowired
	UseService useService;

	@Override
	public UseDTO saveOrUpdate(AddUseRequest request) {
		return useService.saveOrUpdate(request);
	}

	@Override
	public UseDTO queryById(Long id) {
		UseDO useDO = useService.getById(id);
		return PojoUtils.map(useDO,UseDTO.class);
	}

//	@Override
//	public Boolean withdrawAndReject(AddUseRequest request) {
//		return useService.withdrawAndReject(request);
//	}

	@Override
	public List<UseDTO> queryUseList(List<String> applyIds) {
		return useService.queryUseList(applyIds);
	}

	@Override
	public Page<UseDTO> queryUseListPageList(QueryUseListPageRequest request) {
		return useService.queryUseListPageList(request);
	}

	@Override
	public Map<Long, List<UseDTO>> queryUseListByEid(List<Long> eidList) {
		return useService.queryUseListByEid(eidList);
	}

}
