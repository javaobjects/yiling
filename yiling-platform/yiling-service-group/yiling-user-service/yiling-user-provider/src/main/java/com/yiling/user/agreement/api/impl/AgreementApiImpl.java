package com.yiling.user.agreement.api.impl;

import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreement.api.AgreementApi;
import com.yiling.user.agreement.bo.AgreementPageListItemBO;
import com.yiling.user.agreement.bo.EntThirdAgreementInfoBO;
import com.yiling.user.agreement.bo.StatisticsAgreementDTO;
import com.yiling.user.agreement.dto.AgreementDTO;
import com.yiling.user.agreement.dto.AgreementPageListItemDTO;
import com.yiling.user.agreement.dto.EntPageListItemDTO;
import com.yiling.user.agreement.dto.SupplementAgreementDetailDTO;
import com.yiling.user.agreement.dto.ThirdAgreementEntPageListItemDTO;
import com.yiling.user.agreement.dto.YearAgreementDetailDTO;
import com.yiling.user.agreement.dto.request.CloseAgreementRequest;
import com.yiling.user.agreement.dto.request.EditAgreementRequest;
import com.yiling.user.agreement.dto.request.QueryAgreementCountRequest;
import com.yiling.user.agreement.dto.request.QueryAgreementPageListRequest;
import com.yiling.user.agreement.dto.request.QueryEntPageListRequest;
import com.yiling.user.agreement.dto.request.QueryThirdAgreementEntListRequest;
import com.yiling.user.agreement.dto.request.QueryThirdAgreementPageListRequest;
import com.yiling.user.agreement.dto.request.SaveAgreementRequest;
import com.yiling.user.agreement.dto.request.UpdateAgreementRequest;
import com.yiling.user.agreement.enums.AgreementCategoryEnum;
import com.yiling.user.agreement.enums.AgreementModeEnum;
import com.yiling.user.agreement.service.AgreementService;

/**
 * @author: shuang.zhang
 * @date: 2021/6/4
 */
@DubboService
public class AgreementApiImpl implements AgreementApi {

	@Autowired
	private AgreementService agreementService;

//	@Autowired
//	private AgreementAccountService agreementAccountService;

	@Override
	public Boolean save(SaveAgreementRequest request) {
		return agreementService.save(request);
	}

    @Override
    public Long saveForImport(SaveAgreementRequest request) {
        return agreementService.saveForImport(request);
    }

    @Override
	public Boolean update(EditAgreementRequest request) {
		return agreementService.update(request);
	}

	@Override
	public AgreementDTO getAgreementDetailsInfo(Long id) {
		return agreementService.getAgreementDetailsInfo(id);
	}

//	@Override
//	public AgreementConditionDTO getAgreementConditionById(Long conditionId) {
//		return agreementService.getAgreementConditionById(conditionId);
//	}

	@Override
	public List<AgreementDTO> getAgreementDetailsInfoByIds(List<Long> ids) {
		return agreementService.getAgreementList(ids);
	}

	@Override
	public Integer countAgreementByTimeAndOther(QueryAgreementCountRequest request) {
		return agreementService.countAgreementByTimeAndOther(request);
	}

	@Override
	public EntThirdAgreementInfoBO statisticsAgreement() {
		return agreementService.statisticsAgreement();
	}

	@Override
	public List<StatisticsAgreementDTO> statisticsAgreementByEid(List<Long> eidList, Long queryEid) {
		return agreementService.statisticsAgreementByEid(eidList, queryEid);
	}

	@Override
	public Page<AgreementPageListItemDTO> queryAgreementsPageList(QueryAgreementPageListRequest request) {
		Page<AgreementPageListItemBO> agreementsPageListItemBOPage = agreementService.queryAgreementsPageList(request);
		return PojoUtils.map(agreementsPageListItemBOPage, AgreementPageListItemDTO.class);
	}

	@Override
	public Page<AgreementPageListItemDTO> querySupplementAgreementsPageListByEids(QueryThirdAgreementPageListRequest request) {
		Page<AgreementPageListItemBO> agreementsPageListItemBOPage = agreementService.querySupplementAgreementsPageListByEids(request);
		return PojoUtils.map(agreementsPageListItemBOPage, AgreementPageListItemDTO.class);
	}

	@Override
	public List<AgreementPageListItemDTO> querySupplementAgreementsByEids(QueryThirdAgreementPageListRequest request) {
		List<AgreementPageListItemBO> agreementsPageListItemBO = agreementService.querySupplementAgreementsByEids(request);
		return PojoUtils.map(agreementsPageListItemBO, AgreementPageListItemDTO.class);
	}

//	@Override
//	public Map<Long, Long> queryTempAgreementCount(List<Long> agreementIdList, AgreementModeEnum modeEnum) {
//		return agreementService.queryTempAgreementCount(agreementIdList, modeEnum);
//	}

	@Override
	public Map<Long, List<AgreementDTO>> queryTempAgreement(List<Long> agreementIdList, AgreementModeEnum modeEnum) {
		return agreementService.queryTempAgreement(agreementIdList, modeEnum);
	}

	@Override
	public Page<ThirdAgreementEntPageListItemDTO> queryThirdAgreementsEntPageList(QueryThirdAgreementEntListRequest request) {
		return PojoUtils.map(agreementService.queryThirdAgreementsEntPageList(request), ThirdAgreementEntPageListItemDTO.class);
	}

	@Override
	public Boolean agreementClose(CloseAgreementRequest request) {
		return agreementService.agreementClose(request);
	}

	@Override
	public YearAgreementDetailDTO queryYearAgreementsDetail(Long id) {
		return agreementService.queryYearAgreementsDetail(id);
	}

	@Override
	public SupplementAgreementDetailDTO querySupplementAgreementsDetail(Long id) {
		return agreementService.querySupplementAgreementsDetail(id);
	}

	@Override
	public List<SupplementAgreementDetailDTO> querySupplementAgreementsDetailList(List<Long> ids) {
		return agreementService.querySupplementAgreementsDetailList(ids);
	}

	@Override
	public List<SupplementAgreementDetailDTO> queryStartSupplementAgreementsDetail() {
		return agreementService.queryStartSupplementAgreementsDetail();
	}

	@Override
	public Boolean updateAgreement(UpdateAgreementRequest request) {
		return agreementService.updateAgreement(request);
	}

    @Override
    public Boolean syncAgreementRelation() {
        return agreementService.syncAgreementRelation();
    }

	@Override
	public Page<EntPageListItemDTO> queryEntPageList(QueryEntPageListRequest request) {
		return agreementService.queryEntPageList(request);
	}

	@Override
	public List<AgreementDTO> queryAgreementList(List<Long> eidList, AgreementModeEnum mode, AgreementCategoryEnum agreementCategoryEnum) {
		List<AgreementDTO> agreementDTOS = agreementService.queryAgreementList(eidList, mode, agreementCategoryEnum);
		return agreementDTOS;
	}

    @Override
    public List<AgreementDTO> queryAgreementListByBuyerEid(Long buyerEid) {
        return agreementService.queryAgreementListByBuyerEid(buyerEid);
    }

}
