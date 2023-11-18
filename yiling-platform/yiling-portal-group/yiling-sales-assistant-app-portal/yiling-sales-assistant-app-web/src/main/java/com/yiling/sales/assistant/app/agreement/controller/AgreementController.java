package com.yiling.sales.assistant.app.agreement.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.pojo.vo.SimpleEnterpriseVO;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.api.PopGoodsApi;
import com.yiling.goods.medicine.dto.GoodsInfoDTO;
import com.yiling.sales.assistant.app.agreement.form.QueryAgreementGoodsPageListForm;
import com.yiling.sales.assistant.app.agreement.form.QueryEntPageListForm;
import com.yiling.sales.assistant.app.agreement.form.QuerySupplementAgreementPageListForm;
import com.yiling.sales.assistant.app.agreement.form.QueryYearAgreementPageListForm;
import com.yiling.sales.assistant.app.agreement.vo.AgreementConditionVO;
import com.yiling.sales.assistant.app.agreement.vo.AgreementGoodsVO;
import com.yiling.sales.assistant.app.agreement.vo.EntPageListItemVO;
import com.yiling.sales.assistant.app.agreement.vo.SupplementAgreementDetailVO;
import com.yiling.sales.assistant.app.agreement.vo.SupplementAgreementPageListItemVO;
import com.yiling.sales.assistant.app.agreement.vo.YearAgreementGoodsPageVO;
import com.yiling.sales.assistant.app.agreement.vo.YearAgreementPageListItemVO;
import com.yiling.sales.assistant.app.agreement.vo.YearAgreementPageVO;
import com.yiling.sales.assistant.app.agreement.vo.YearSupplementAgreementPageVO;
import com.yiling.user.agreement.api.AgreementApi;
import com.yiling.user.agreement.api.AgreementGoodsApi;
import com.yiling.user.agreement.dto.AgreementDTO;
import com.yiling.user.agreement.dto.AgreementGoodsDTO;
import com.yiling.user.agreement.dto.AgreementPageListItemDTO;
import com.yiling.user.agreement.dto.EntPageListItemDTO;
import com.yiling.user.agreement.dto.SupplementAgreementDetailDTO;
import com.yiling.user.agreement.dto.YearAgreementDetailDTO;
import com.yiling.user.agreement.dto.request.AgreementGoodsDetailsPageRequest;
import com.yiling.user.agreement.dto.request.QueryAgreementPageListRequest;
import com.yiling.user.agreement.dto.request.QueryEntPageListRequest;
import com.yiling.user.agreement.enums.AgreementCategoryEnum;
import com.yiling.user.agreement.enums.AgreementModeEnum;
import com.yiling.user.agreement.enums.AgreementStatusEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dexi.yao
 * @date 2021-09-14
 */
@Api(tags = "协议模块")
@RestController
@RequestMapping("/agreement")
@Slf4j
public class AgreementController {
	@DubboReference
	AgreementApi      agreementApi;
	@DubboReference
	AgreementGoodsApi agreementGoodsApi;
	@DubboReference
	GoodsApi          goodsApi;
    @DubboReference
    PopGoodsApi       popGoodsApi;
	@DubboReference
    EnterpriseApi     enterpriseApi;


	@ApiOperation("协议模块查询企业列表")
	@PostMapping("queryEntPageList")
	public Result<Page<EntPageListItemVO>> queryEntPageList(@Valid @RequestBody QueryEntPageListForm form) {
		QueryEntPageListRequest request = PojoUtils.map(form, QueryEntPageListRequest.class);
		Page<EntPageListItemDTO> page = agreementApi.queryEntPageList(request);
		return Result.success(PojoUtils.map(page, EntPageListItemVO.class));
	}


	/**
	 * 根据状态查询年度协议列表
	 *
	 * @param
	 * @return
	 */
	@ApiOperation(value = "根据状态查询年度协议列表")
	@PostMapping("/getYearAgreementPageList")
	public Result<YearAgreementPageVO<YearAgreementPageListItemVO>> getYearAgreementPageList(@Valid @RequestBody QueryYearAgreementPageListForm form) {
		YearAgreementPageVO<YearAgreementPageListItemVO> result = new YearAgreementPageVO<>(0,0,0,0);
		//查询各状态协议个数
		List<AgreementDTO> agreementList = agreementApi.queryAgreementList(ListUtil.toList(form.getQueryEid()),
				AgreementModeEnum.SECOND_AGREEMENTS, AgreementCategoryEnum.YEAR_AGREEMENT);

		if (CollUtil.isEmpty(agreementList)) {
			return Result.success(result);
		}
		agreementList.forEach(e -> {

			if (AgreementStatusEnum.CLOSE.getCode().equals(e.getStatus())) {
				result.setStop(result.getStop() + 1);
			} else {
				Date curentDate = new Date();
				//未开始
				if (curentDate.compareTo(e.getStartTime()) == -1) {
					result.setUnStart(result.getUnStart() + 1);
				}
				//进行中
				if (curentDate.compareTo(e.getStartTime()) == 1 && curentDate.compareTo(e.getEndTime()) == -1) {
					result.setStart(result.getStart() + 1);
				}
				//已过期
				if (curentDate.compareTo(e.getEndTime()) == 1) {
					result.setExpire(result.getExpire() + 1);
				}
			}
		});

		//主协议id
		Set<Long> agreementIds = new HashSet<>();
		//临时协议map
		Map<Long, List<AgreementDTO>> tempCountMap;
		//协议下的商品
		Map<Long, List<AgreementGoodsDTO>> agreementGoodsMap;

		QueryAgreementPageListRequest request = PojoUtils.map(form, QueryAgreementPageListRequest.class);
		Page<AgreementPageListItemDTO> page = agreementApi.queryAgreementsPageList(request);
		List<AgreementPageListItemDTO> records = page.getRecords();
		PojoUtils.map(page, result);
		if (CollUtil.isEmpty(records)) {
			return Result.success(result);
		}

		//返回参数设置状态
		records.forEach(e -> {
			agreementIds.add(e.getId());
		});
		result.setRecords(PojoUtils.map(records,YearAgreementPageListItemVO.class));
		//如果查询年度协议则查出补充协议个数
		//批量查询子协议数
		tempCountMap = agreementApi.queryTempAgreement(new ArrayList<>(agreementIds), AgreementModeEnum.SECOND_AGREEMENTS);
		//批量查询协议下商品数
		agreementGoodsMap = agreementGoodsApi.getAgreementGoodsListByAgreementIds(new ArrayList<>(agreementIds));
		//补全数据
		result.getRecords().forEach(e -> {
			e.setTempCount(tempCountMap.getOrDefault(e.getId(), ListUtil.toList()).size());
			e.setGoodsCount(agreementGoodsMap.getOrDefault(e.getId(), ListUtil.toList()).size());
		});
		return Result.success(result);
	}

	/**
	 * 根据年度协议id查询年度协议商品
	 *
	 * @param form
	 * @return
	 */
	@ApiOperation(value = "根据协议id查询协议商品")
	@PostMapping("/queryYearGoodsPageList")
	public Result<YearAgreementGoodsPageVO<AgreementGoodsVO>> queryYearGoodsPageList(@RequestBody @Valid QueryAgreementGoodsPageListForm form) {
		YearAgreementGoodsPageVO result = new YearAgreementGoodsPageVO();
		//查询年度协议
		AgreementDTO agreementDTO = agreementApi.getAgreementDetailsInfo(form.getAgreementId());
		if (ObjectUtil.isNull(agreementDTO)) {
			return Result.failed("协议不存在");
		}
		PojoUtils.map(agreementDTO, result);

		AgreementGoodsDetailsPageRequest request = PojoUtils.map(form, AgreementGoodsDetailsPageRequest.class);
		Page<AgreementGoodsDTO> page = agreementGoodsApi.getAgreementGoodsByAgreementIdPage(request);
		List<AgreementGoodsDTO> records = page.getRecords();
		List<Long> goodsIds = records.stream().map(AgreementGoodsDTO::getGoodsId).collect(Collectors.toList());
		PojoUtils.map(page, result);
		if (CollUtil.isEmpty(goodsIds)) {
			return Result.success(result);
		}
		List<AgreementGoodsVO> goodsVO = PojoUtils.map(records, AgreementGoodsVO.class);
		//查询商品信息
		List<GoodsInfoDTO> list = popGoodsApi.batchQueryInfo(goodsIds);
		if (CollectionUtil.isNotEmpty(list)) {
			Map<Long, GoodsInfoDTO> map = list.stream().collect(Collectors.toMap(GoodsInfoDTO::getId, e -> e));
			goodsVO.forEach(e -> {
				e.setPrice(map.get(e.getGoodsId()).getPrice());
			});
		}
		result.setRecords(goodsVO);
		return Result.success(result);
	}

	/**
	 * 根据年度协议id查询补充协议
	 *
	 * @param form
	 * @return
	 */
	@ApiOperation(value = "根据年度协议id查询补充协议")
	@PostMapping("/querySupplementAgreementPageList")
	public Result<YearSupplementAgreementPageVO<SupplementAgreementPageListItemVO>> querySupplementAgreementPageList(@Valid @RequestBody QuerySupplementAgreementPageListForm form) {
		YearSupplementAgreementPageVO<SupplementAgreementPageListItemVO> result = new YearSupplementAgreementPageVO<>();
		result.setStart(0);
		result.setUnStart(0);
		result.setExpire(0);
		result.setStop(0);

		//查询年度协议
		AgreementDTO agreementDTO = agreementApi.getAgreementDetailsInfo(form.getAgreementId());
		if (ObjectUtil.isNull(agreementDTO)) {
			return Result.failed("年度协议不存在");
		}
		//设置年度协议信息
		PojoUtils.map(agreementDTO, result);
		//查询各状态数量
		List<AgreementDTO> agreementList = agreementApi.queryTempAgreement(ListUtil.toList(form.getAgreementId()), AgreementModeEnum.SECOND_AGREEMENTS)
				.getOrDefault(form.getAgreementId(), ListUtil.toList());
		agreementList.forEach(e -> {

			if (AgreementStatusEnum.CLOSE.getCode().equals(e.getStatus())) {
				result.setStop(result.getStop() + 1);
			} else {
				Date currentDate = new Date();
				//未开始
				if (currentDate.compareTo(e.getStartTime()) == -1) {
					result.setUnStart(result.getUnStart() + 1);
				}
				//进行中
				if (currentDate.compareTo(e.getStartTime()) == 1 && currentDate.compareTo(e.getEndTime()) == -1) {
					result.setStart(result.getStart() + 1);
				}
				//已过期
				if (currentDate.compareTo(e.getEndTime()) == 1) {
					result.setExpire(result.getExpire() + 1);
				}
			}
		});

		//查询该年度协议下的补充协议
		QueryAgreementPageListRequest request = PojoUtils.map(form, QueryAgreementPageListRequest.class);
		request.setParentAgreementIds(ListUtil.toList(form.getAgreementId()));
		Page<AgreementPageListItemDTO> page = agreementApi.queryAgreementsPageList(request);
		List<AgreementPageListItemDTO> records = page.getRecords();
		PojoUtils.map(page, result);
		if (CollUtil.isEmpty(records)) {
			return Result.success(result);
		}
		List<SupplementAgreementPageListItemVO> recordsVO = PojoUtils.map(records, SupplementAgreementPageListItemVO.class);
		result.setRecords(recordsVO);
		return Result.success(result);
	}

	@ApiOperation(value = "根据补充协议id查询协议详情")
	@GetMapping("/getSupplementAgreementsDetail")
	public Result<SupplementAgreementDetailVO> getSupplementAgreementsDetail(@RequestParam("agreementId") Long agreementId) {
		SupplementAgreementDetailVO result;

		SupplementAgreementDetailDTO detailDTO = agreementApi.querySupplementAgreementsDetail(agreementId);
		if (detailDTO == null) {
			return Result.success();
		}

		result = PojoUtils.map(detailDTO, SupplementAgreementDetailVO.class);
		return buildSupplementAgreementDetailResult(agreementId, result, detailDTO);
	}

	@ApiOperation(value = "获取以岭主体企业列表")
	@GetMapping("/mainPart/list")
	public Result<CollectionObject<SimpleEnterpriseVO>> getMainPartList() {
		List<EnterpriseDTO> list = enterpriseApi.listMainPart();
		return Result.success(new CollectionObject<>(PojoUtils.map(list, SimpleEnterpriseVO.class)));
	}

	private Result<SupplementAgreementDetailVO> buildSupplementAgreementDetailResult(Long agreementId,
																					 SupplementAgreementDetailVO result,
																					 SupplementAgreementDetailDTO detailDTO) {
		//客户ID列表
		ArrayList<Long> customerEids;
		//设置年度协议名称
		YearAgreementDetailDTO yearAgreementDetailDTO = agreementApi.queryYearAgreementsDetail(detailDTO.getParentId());
		if (yearAgreementDetailDTO == null) {
			return Result.failed("主协议不存在");
		}
		result.setParentName(yearAgreementDetailDTO.getName());
		result.setParentId(yearAgreementDetailDTO.getId());

		if (CollUtil.isEmpty(detailDTO.getAgreementsConditionList())) {
			log.error("该协议没有条件{" + agreementId + "}");
			return Result.failed("数据不完整");
		}
		//封装协议条件listVO
		AgreementConditionVO agreementConditionVO = PojoUtils.map(detailDTO.getAgreementsConditionList().get(0), AgreementConditionVO.class);
		agreementConditionVO.setIsPatent(detailDTO.getIsPatent());
		agreementConditionVO.setConditionRule(detailDTO.getConditionRule());
		List<AgreementConditionVO.ConditionDetailVO> conditionDetailVOS = PojoUtils.map(detailDTO.getAgreementsConditionList(),
				AgreementConditionVO.ConditionDetailVO.class);
		agreementConditionVO.setConditions(conditionDetailVOS);
		result.setAgreementsCondition(agreementConditionVO);
		return Result.success(result);
	}


}
