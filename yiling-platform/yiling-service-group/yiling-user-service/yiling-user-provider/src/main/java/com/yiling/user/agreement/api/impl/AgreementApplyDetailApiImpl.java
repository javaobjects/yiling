package com.yiling.user.agreement.api.impl;

import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreement.api.AgreementApplyDetailApi;
import com.yiling.user.agreement.dto.AgreementApplyDetailDTO;
import com.yiling.user.agreement.dto.request.AddRebateApplyDetailRequest;
import com.yiling.user.agreement.dto.request.EditApplyDetailRequest;
import com.yiling.user.agreement.dto.request.QueryRebateApplyDetailPageListRequest;
import com.yiling.user.agreement.entity.AgreementApplyDetailDO;
import com.yiling.user.agreement.service.AgreementApplyDetailService;

/**
 * @author dexi.yao
 * @date 2021-07-28
 */
@DubboService
public class AgreementApplyDetailApiImpl implements AgreementApplyDetailApi {
	@Autowired
	AgreementApplyDetailService rebateApplyDetailService;

	@Override
	public Boolean batchSave(List<AddRebateApplyDetailRequest> list) {
		List<AgreementApplyDetailDO> dataList = PojoUtils.map(list, AgreementApplyDetailDO.class);
		return rebateApplyDetailService.saveBatch(dataList);
	}

	@Override
	public Boolean batchUpdate(List<AddRebateApplyDetailRequest> list) {
		List<AgreementApplyDetailDO> updateList=PojoUtils.map(list, AgreementApplyDetailDO.class);
		return rebateApplyDetailService.updateBatchById(updateList);
	}

	@Override
	public Boolean updateById(AddRebateApplyDetailRequest request) {
		return rebateApplyDetailService.updateById(request);
	}

	@Override
	public Page<AgreementApplyDetailDTO> queryRebateApplyDetailPageList(QueryRebateApplyDetailPageListRequest request) {
		return rebateApplyDetailService.queryRebateApplyDetailPageList(request);
	}

	@Override
	public Map<Long, List<AgreementApplyDetailDTO>> queryRebateApplyDetailList(List<Long> ids) {
		return rebateApplyDetailService.queryRebateApplyDetailList(ids);
	}

	@Override
	public List<AgreementApplyDetailDTO> queryRebateApplyDetail(Long id) {
		return rebateApplyDetailService.queryRebateApplyDetail(id);
	}

	@Override
	public List<AgreementApplyDetailDTO> queryRebateApplyDetailList(String code, List<Long> agreementId) {
		return rebateApplyDetailService.queryRebateApplyDetailList(code, agreementId);
	}

	@Override
	public AgreementApplyDetailDTO queryRebateApplyDetailById(Long detailId) {
		return rebateApplyDetailService.queryRebateApplyDetailById(detailId);
	}

	@Override
	public AgreementApplyDetailDTO queryById(Long id) {
		return rebateApplyDetailService.queryById(id);
	}

	@Override
	public Boolean editApplyDetail(EditApplyDetailRequest request) {
		return rebateApplyDetailService.editApplyDetail(request);
	}


}
