package com.yiling.user.agreement.api.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreement.api.AgreementApplyApi;
import com.yiling.user.agreement.dto.AgreementApplyOpenDTO;
import com.yiling.user.agreement.dto.AgreementRebateApplyDTO;
import com.yiling.user.agreement.dto.ApplyEntDTO;
import com.yiling.user.agreement.dto.request.AddRebateApplyRequest;
import com.yiling.user.agreement.dto.request.QueryApplyEntPageListRequest;
import com.yiling.user.agreement.dto.request.QueryApplyPageListRequest;
import com.yiling.user.agreement.dto.request.RebateApplyPageListItemDTO;
import com.yiling.user.agreement.entity.AgreementApplyDO;
import com.yiling.user.agreement.enums.AgreementApplyStatusEnum;
import com.yiling.user.agreement.enums.AgreementErrorCode;
import com.yiling.user.agreement.service.AgreementApplyService;

/**
 * @author dexi.yao
 * @date 2021-07-27
 */
@DubboService
public class AgreementApplyApiImpl implements AgreementApplyApi {

	@Autowired
	AgreementApplyService agreementApplyService;


	@Override
	public Page<RebateApplyPageListItemDTO> queryRebateApplyPageList(QueryApplyPageListRequest request) {

		return agreementApplyService.queryRebateApplyPageList(request);
	}

	@Override
	public List<AgreementRebateApplyDTO> queryRebateApplyList(List<Long> idList) {
		List<AgreementApplyDO> applyDOS = agreementApplyService.listByIds(idList);

		return PojoUtils.map(applyDOS, AgreementRebateApplyDTO.class);
	}

	@Override
	public AgreementRebateApplyDTO queryRebateApplyByCode(String code) {
		return agreementApplyService.queryRebateApplyByCode(code);
	}

	@Override
	public AgreementRebateApplyDTO save(AddRebateApplyRequest request) {
		AgreementRebateApplyDTO result = null;
		AgreementApplyDO applyDO = PojoUtils.map(request, AgreementApplyDO.class);
		boolean isSave = agreementApplyService.save(applyDO);
		if (isSave) {
			result = PojoUtils.map(applyDO, AgreementRebateApplyDTO.class);
		} else {
			throw new BusinessException(AgreementErrorCode.AGREEMENT_USE_ADD);
		}
		return result;
	}

	@Override
	public Boolean updateById(AddRebateApplyRequest request) {
		AgreementApplyDO applyDO = PojoUtils.map(request, AgreementApplyDO.class);
		boolean isUpdate = agreementApplyService.updateById(applyDO);
		return isUpdate;
	}

	@Override
	public List<AgreementApplyOpenDTO> queryAgreementApplyOpenList(Date startTime, Date endTime) {
		return agreementApplyService.queryAgreementApplyOpenList(startTime, endTime);
	}

	@Override
	public Boolean applyCompletePush(List<Long> applyIds) {
		return agreementApplyService.applyCompletePush(applyIds);
	}

	@Override
	public Map<Long, List<AgreementRebateApplyDTO>> queryRebateApplyListByEid(List<Long> eidList, AgreementApplyStatusEnum statusEnum) {
		return agreementApplyService.queryRebateApplyListByEid(eidList,statusEnum);
	}

	@Override
	public Page<ApplyEntDTO> queryApplyEntPageList(QueryApplyEntPageListRequest request) {
		return agreementApplyService.queryApplyEntPageList(request);
	}

}
