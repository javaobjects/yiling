package com.yiling.sales.assistant.app.rebate.controller;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.BoolObject;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.api.PopGoodsApi;
import com.yiling.goods.medicine.dto.GoodsInfoDTO;
import com.yiling.mall.agreement.api.AgreementBusinessApi;
import com.yiling.mall.agreement.dto.CalculateRebateApplyDTO;
import com.yiling.mall.agreement.dto.request.CalculateRebateApplyRequest;
import com.yiling.mall.agreement.dto.request.SaveRebateApplyRequest;
import com.yiling.sales.assistant.app.rebate.form.CalculateRebateApplyForm;
import com.yiling.sales.assistant.app.rebate.form.QueryApplyEntPageListForm;
import com.yiling.sales.assistant.app.rebate.form.QueryApplyPageListForm;
import com.yiling.sales.assistant.app.rebate.form.QueryRebateApplyDetailPageListForm;
import com.yiling.sales.assistant.app.rebate.form.QueryRebateGoodsPageListForm;
import com.yiling.sales.assistant.app.rebate.form.QueryRebateOrderPageListForm;
import com.yiling.sales.assistant.app.rebate.form.SaveRebateApplyForm;
import com.yiling.sales.assistant.app.rebate.vo.ApplyEntPageListItemVO;
import com.yiling.sales.assistant.app.rebate.vo.ApplyPageListItemVO;
import com.yiling.sales.assistant.app.rebate.vo.ApplyPageVO;
import com.yiling.sales.assistant.app.rebate.vo.CalculateRebateApplyVO;
import com.yiling.sales.assistant.app.rebate.vo.RebateApplyDetailPageListItemVO;
import com.yiling.sales.assistant.app.rebate.vo.RebateApplyPageVO;
import com.yiling.sales.assistant.app.rebate.vo.RebateGoodsPageListItemVO;
import com.yiling.sales.assistant.app.rebate.vo.RebateGoodsPageVO;
import com.yiling.sales.assistant.app.rebate.vo.RebateOrderPageListItemVO;
import com.yiling.sales.assistant.app.rebate.vo.RebateOrderPageVO;
import com.yiling.user.agreement.api.AgreementApplyApi;
import com.yiling.user.agreement.api.AgreementApplyDetailApi;
import com.yiling.user.agreement.api.AgreementRebateOrderDetailApi;
import com.yiling.user.agreement.api.ApplyOrderApi;
import com.yiling.user.agreement.dto.AgreementApplyDetailDTO;
import com.yiling.user.agreement.dto.AgreementRebateApplyDTO;
import com.yiling.user.agreement.dto.AgreementRebateOrderDetailDTO;
import com.yiling.user.agreement.dto.ApplyEntDTO;
import com.yiling.user.agreement.dto.ApplyOrderDTO;
import com.yiling.user.agreement.dto.request.PageListByIdRequest;
import com.yiling.user.agreement.dto.request.QueryApplyEntPageListRequest;
import com.yiling.user.agreement.dto.request.QueryApplyOrderPageListRequest;
import com.yiling.user.agreement.dto.request.QueryApplyPageListRequest;
import com.yiling.user.agreement.dto.request.QueryRebateApplyDetailPageListRequest;
import com.yiling.user.agreement.dto.request.RebateApplyPageListItemDTO;
import com.yiling.user.agreement.enums.AgreementRebateApplyRangeTypeEnum;
import com.yiling.user.enterprise.api.EmployeeApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.EnterpriseEmployeeDTO;
import com.yiling.user.system.api.DataPermissionsApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.enums.PermissionAppEnum;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: dexi.yao
 * @date: 2021/9/9
 */
@Api(tags = "返利模块")
@RestController
@RequestMapping("/rebate")
@Slf4j
public class RebateController extends BaseController {

	@DubboReference
	AgreementApplyApi             applyApi;
	@DubboReference
	EnterpriseApi                 enterpriseApi;
	@DubboReference
	AgreementApplyDetailApi       applyDetailApi;
	@DubboReference
	ApplyOrderApi                 applyOrderApi;
	@DubboReference
	AgreementRebateOrderDetailApi rebateOrderDetailApi;
	@DubboReference
	GoodsApi                      goodsApi;
    @DubboReference
    PopGoodsApi                   popGoodsApi;
	@DubboReference
	AgreementBusinessApi          agreementBusinessApi;
	@DubboReference
	EmployeeApi                   employeeApi;
	@DubboReference
	DataPermissionsApi dataPermissionsApi;


	@ApiOperation("查询已申请的返利入账客户")
	@PostMapping("/queryApplyEntPageList")
	public Result<Page<ApplyEntPageListItemVO>> queryApplyEntPageList(@Valid @RequestBody QueryApplyEntPageListForm form,
																	  @CurrentUser CurrentStaffInfo user) {
		QueryApplyEntPageListRequest request = PojoUtils.map(form, QueryApplyEntPageListRequest.class);
		request.setOpUserId(user.getCurrentUserId());
		Page<ApplyEntDTO> page = applyApi.queryApplyEntPageList(request);

		return Result.success(PojoUtils.map(page, ApplyEntPageListItemVO.class));
	}

	@ApiOperation("返利申请计算")
	@PostMapping("/calculateRebateApply")
	public Result<CalculateRebateApplyVO> calculateRebateApply(@RequestBody @Valid CalculateRebateApplyForm form, @CurrentUser CurrentStaffInfo staffInfo) {
		//计算申请的时间起止
		Map<String, Date> dateMap = getDate(form.getYear(), form.getMonth());
		Date startDate = dateMap.get("startDate");
		Date endDate = dateMap.get("endDate");

		CalculateRebateApplyRequest request = PojoUtils.map(form, CalculateRebateApplyRequest.class);
		request.setStartDate(startDate);
		request.setEndDate(endDate);
		request.setOpUserId(staffInfo.getCurrentUserId());
		//传一级商则easCode置为空
		if (StrUtil.isNotEmpty(form.getEntryCode()) && ObjectUtil.isNotNull(form.getEntryEid())) {
			request.setInputEntry(1);
		} else {
			request.setInputEntry(0);
		}
		CalculateRebateApplyDTO applyDTO = agreementBusinessApi.calculateRebateApply(request);
		return Result.success(PojoUtils.map(applyDTO, CalculateRebateApplyVO.class));
	}

	private Map<String, Date> getDate(Integer year, Integer month) {
		Map<String, Date> result = MapUtil.newHashMap();
		//申请的开始时间
		Date startDate = null;
		//申请的结束时间
		Date endDate = null;
		String startDateStr = year + "-";
		//月度
		if (month >= 1 && month <= CalculateRebateApplyForm.monthType12) {
			startDateStr = startDateStr + month + "-01";
			startDate = DateUtil.parse(startDateStr, "yyyy-MM-dd");
			endDate = DateUtil.endOfMonth(startDate);
		} else if (month >= CalculateRebateApplyForm.monthType13 && month <= CalculateRebateApplyForm.monthType16) {
			//季度
			if (CalculateRebateApplyForm.monthType13.equals(month)) {
				startDateStr = startDateStr + "01-01";
			} else if (CalculateRebateApplyForm.monthType14.equals(month)) {
				startDateStr = startDateStr + "04-01";
			} else if (CalculateRebateApplyForm.monthType15.equals(month)) {
				startDateStr = startDateStr + "07-01";
			} else if (CalculateRebateApplyForm.monthType16.equals(month)) {
				startDateStr = startDateStr + "10-01";
			}
			startDate = DateUtil.parse(startDateStr, "yyyy-MM-dd");
			endDate = DateUtil.endOfQuarter(startDate);
		} else if (month >= CalculateRebateApplyForm.monthType17 && month <= CalculateRebateApplyForm.monthType18) {
			String endDateStr = null;
			//年度
			if (CalculateRebateApplyForm.monthType17.equals(month)) {
				startDateStr = startDateStr + "01-01";
				endDateStr = startDateStr + "06-30";
			} else if (CalculateRebateApplyForm.monthType18.equals(month)) {
				startDateStr = startDateStr + "07-01";
				endDateStr = startDateStr + "12-31";
			}
			startDate = DateUtil.parse(startDateStr, "yyyy-MM-dd");
			endDate = DateUtil.parse(endDateStr, "yyyy-MM-dd");
		} else if (month.equals(CalculateRebateApplyForm.monthType19)) {
			//全年
			startDateStr = startDateStr + "01-01";
			startDate = DateUtil.parse(startDateStr, "yyyy-MM-dd");
			endDate = DateUtil.endOfYear(startDate);
		}
		result.put("startDate", startDate);
		result.put("endDate", endDate);
		return result;
	}

	@ApiOperation("返利申请保存")
	@PostMapping("/saveRebateApply")
	public Result<BoolObject> saveRebateApply(@RequestBody @Valid SaveRebateApplyForm form, @CurrentUser CurrentStaffInfo staffInfo) {
		//查询企业
		Map<Long, String> enterpriseNames = enterpriseApi.listByIds(ListUtil.toList(form.getEid(), form.getEntryEid()))
				.stream().collect(Collectors.toMap(EnterpriseDTO::getId, EnterpriseDTO::getName));
		Assert.notBlank(enterpriseNames.get(form.getEid()), "企业不存在");

		//计算申请的时间起止
		Map<String, Date> dateMap = getDate(form.getYear(), form.getMonth());
		Date startDate = dateMap.get("startDate");
		Date endDate = dateMap.get("endDate");

		SaveRebateApplyRequest request = PojoUtils.map(form, SaveRebateApplyRequest.class);
		request.setStartDate(startDate);
		request.setEndDate(endDate);
		EnterpriseEmployeeDTO employee = employeeApi.listByEidUserIds(staffInfo.getCurrentEid(), ListUtil.toList(staffInfo.getCurrentEid())).get(0);
		if (ObjectUtil.isNull(employee)) {
			return Result.failed("操作人不存在");
		}
		request.setName(enterpriseNames.get(form.getEid()));
		request.setEntryName(enterpriseNames.getOrDefault(form.getEntryEid(), ""));
		request.setCreateUserCode(employee.getCode());

		Integer month = form.getMonth();
		//月度
		if (month >= 1 && month <= CalculateRebateApplyForm.monthType12) {
			request.setRangeType(AgreementRebateApplyRangeTypeEnum.MONTH.getCode());
		} else if (month >= CalculateRebateApplyForm.monthType13 && month <= CalculateRebateApplyForm.monthType16) {
			if (CalculateRebateApplyForm.monthType13.equals(month)) {
				request.setMonth(1);
			}
			if (CalculateRebateApplyForm.monthType14.equals(month)) {
				request.setMonth(2);
			}
			if (CalculateRebateApplyForm.monthType15.equals(month)) {
				request.setMonth(3);
			}
			if (CalculateRebateApplyForm.monthType16.equals(month)) {
				request.setMonth(4);
			}
			//季度
			request.setRangeType(AgreementRebateApplyRangeTypeEnum.QUARTER.getCode());
		} else if (CalculateRebateApplyForm.monthType17.equals(month)) {
			//上半年
			request.setRangeType(AgreementRebateApplyRangeTypeEnum.FIRST_HALF_YEAR.getCode());
			request.setMonth(0);
		} else if (CalculateRebateApplyForm.monthType18.equals(month)) {
			//下半年
			request.setRangeType(AgreementRebateApplyRangeTypeEnum.SECOND_HALF_YEAR.getCode());
			request.setMonth(0);
		} else if (month.equals(CalculateRebateApplyForm.monthType19)) {
			//全年
			request.setRangeType(AgreementRebateApplyRangeTypeEnum.ALL_YEAR.getCode());
			request.setMonth(0);
		}
		//没有传一级商时默认企业为本企业
		if (StrUtil.isEmpty(form.getEntryCode()) && ObjectUtil.isNull(form.getEntryEid())) {
			request.setEntryCode(form.getEasCode());
			request.setEntryEid(form.getEid());
			request.setEntryName(request.getName());
			request.setInputEntry(0);
		} else {
			request.setInputEntry(1);
		}
		request.setOpUserId(staffInfo.getCurrentUserId());
		Boolean isSave = agreementBusinessApi.saveRebateApply(request);
		return Result.success(new BoolObject(isSave));
	}

	@ApiOperation("查询企业下的返利订单")
	@PostMapping("/queryApplyPageList")
	public Result<ApplyPageVO<ApplyPageListItemVO>> queryApplyPageList(@Valid @RequestBody QueryApplyPageListForm form,
																	   @CurrentUser CurrentStaffInfo user) {
		ApplyPageVO result;
		EnterpriseDTO enterprise = enterpriseApi.getById(form.getEid());
		if (ObjectUtil.isNull(enterprise)) {
			return Result.failed("企业不存在");
		}
		result = PojoUtils.map(enterprise, ApplyPageVO.class);
		QueryApplyPageListRequest request = PojoUtils.map(form, QueryApplyPageListRequest.class);
//		由于b2b_v1.0新增了数据权限此参数作废
//		request.setCreateUser(user.getCurrentUserId());
		//查询数据权限
		List<Long> authorizedUserIds = dataPermissionsApi.listAuthorizedUserIds(PermissionAppEnum.MALL_ADMIN_SALES_ASSISTANT, user.getCurrentEid(), user.getCurrentUserId());
		//设置数据权限
		request.setCreateUserList(authorizedUserIds);

		Integer month = form.getMonth();
		if (ObjectUtil.isNotNull(month)) {
			if (ObjectUtil.isNull(form.getYear())) {
				return Result.failed("按月度查询必须输入年度");
			}
			//月度
			if (month >= 1 && month <= QueryApplyPageListForm.monthType12) {
				request.setRangeType(AgreementRebateApplyRangeTypeEnum.MONTH.getCode());
			} else if (month >= QueryApplyPageListForm.monthType13 && month <= QueryApplyPageListForm.monthType16) {
				if (QueryApplyPageListForm.monthType13.equals(month)) {
					request.setMonth(1);
				}
				if (QueryApplyPageListForm.monthType14.equals(month)) {
					request.setMonth(2);
				}
				if (QueryApplyPageListForm.monthType15.equals(month)) {
					request.setMonth(3);
				}
				if (QueryApplyPageListForm.monthType16.equals(month)) {
					request.setMonth(4);
				}
				//季度
				request.setRangeType(AgreementRebateApplyRangeTypeEnum.QUARTER.getCode());
			} else if (QueryApplyPageListForm.monthType17.equals(month)) {
				//上半年
				request.setRangeType(AgreementRebateApplyRangeTypeEnum.FIRST_HALF_YEAR.getCode());
				request.setMonth(0);
			} else if (QueryApplyPageListForm.monthType18.equals(month)) {
				//下半年
				request.setRangeType(AgreementRebateApplyRangeTypeEnum.SECOND_HALF_YEAR.getCode());
				request.setMonth(0);
			} else if (month.equals(QueryApplyPageListForm.monthType19)) {
				//全年
				request.setRangeType(AgreementRebateApplyRangeTypeEnum.ALL_YEAR.getCode());
				request.setMonth(0);
			}
		}
		Page<RebateApplyPageListItemDTO> page = applyApi.queryRebateApplyPageList(request);
		PojoUtils.map(page, result);
		if (CollUtil.isNotEmpty(page.getRecords())) {
			List<ApplyPageListItemVO> list = PojoUtils.map(page.getRecords(), ApplyPageListItemVO.class);
			result.setRecords(list);
		}
		return Result.success(result);
	}

	@ApiOperation("查询返利申请---协议明细")
	@PostMapping("/queryRebateApplyPageList")
	public Result<RebateApplyPageVO<RebateApplyDetailPageListItemVO>> queryRebateApplyPageList(@RequestBody @Valid QueryRebateApplyDetailPageListForm form) {
		RebateApplyPageVO<RebateApplyDetailPageListItemVO> result;
		//分页列表
		List<RebateApplyDetailPageListItemVO> applyDetailList = ListUtil.toList();
		//查询返利申请
		List<AgreementRebateApplyDTO> rebateApplyList = applyApi.queryRebateApplyList(ListUtil.toList(form.getApplyId()));
		if (CollUtil.isEmpty(rebateApplyList)) {
			return Result.failed("申请单不存在");
		}
		AgreementRebateApplyDTO rebateApplyDTO = rebateApplyList.get(0);
		//查询企业信息
		EnterpriseDTO enterpriseDTO = enterpriseApi.getById(rebateApplyDTO.getEid());
		result = PojoUtils.map(enterpriseDTO, RebateApplyPageVO.class);
		PojoUtils.map(rebateApplyDTO, result);

		//分页查询申请明细或
		QueryRebateApplyDetailPageListRequest pageRequest = PojoUtils.map(form, QueryRebateApplyDetailPageListRequest.class);
		Page<AgreementApplyDetailDTO> page = applyDetailApi.queryRebateApplyDetailPageList(pageRequest);
		PojoUtils.map(page, result);
		if (CollUtil.isEmpty(page.getRecords())) {
			return Result.success(result);
		}
		result.setRecords(PojoUtils.map(page.getRecords(), RebateApplyDetailPageListItemVO.class));
		result.setTotalAmount(rebateApplyDTO.getTotalAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
		return Result.success(result);
	}

	@ApiOperation("查询返利申请---订单明细")
	@PostMapping("/queryRebateOrderPageList")
	public Result<RebateOrderPageVO<RebateOrderPageListItemVO>> queryRebateOrderPageList(@RequestBody @Valid QueryRebateOrderPageListForm form) {
		RebateOrderPageVO<RebateOrderPageListItemVO> result;
		//查询返利申请明细
		AgreementApplyDetailDTO detailDTO = applyDetailApi.queryRebateApplyDetailById(form.getApplyDetailId());
		if (ObjectUtil.isNull(detailDTO)) {
			return Result.failed("申请单明细不存在");
		}
		result = PojoUtils.map(detailDTO, RebateOrderPageVO.class);

		//分页查询订单明细
		QueryApplyOrderPageListRequest pageRequest = PojoUtils.map(detailDTO, QueryApplyOrderPageListRequest.class);
		Page<ApplyOrderDTO> page = applyOrderApi.queryApplyOrderPageList(pageRequest);
		PojoUtils.map(page, result);
		if (CollUtil.isEmpty(page.getRecords())) {
			return Result.success(result);
		}
		result.setRecords(PojoUtils.map(page.getRecords(), RebateOrderPageListItemVO.class));
		return Result.success(result);
	}

	@ApiOperation("查询返利申请---商品明细")
	@PostMapping("/queryRebateGoodsPageList")
	public Result<RebateGoodsPageVO<RebateGoodsPageListItemVO>> queryRebateGoodsPageList(@RequestBody @Valid QueryRebateGoodsPageListForm form) {
		RebateGoodsPageVO<RebateGoodsPageListItemVO> result;
		//查询返利申请明细
		ApplyOrderDTO detailDTO = applyOrderApi.queryById(form.getApplyOrderId());
		if (ObjectUtil.isNull(detailDTO)) {
			return Result.failed("订单不存在");
		}
		result = PojoUtils.map(detailDTO, RebateGoodsPageVO.class);

		//分页查询商品明细
		PageListByIdRequest pageRequest = new PageListByIdRequest();
		List<Long> rebateOrderDetailId = ListUtil.toList();
		if (StrUtil.isNotBlank(detailDTO.getRebateOrderDetailId())) {
			long[] ids = StrUtil.splitToLong(detailDTO.getRebateOrderDetailId(), ",");
			rebateOrderDetailId.addAll(Arrays.stream(ids).boxed().collect(Collectors.toList()));
		}
		pageRequest.setIdList(rebateOrderDetailId);
		Page<AgreementRebateOrderDetailDTO> page = rebateOrderDetailApi.pageListById(pageRequest);
		PojoUtils.map(page, result);
		if (CollUtil.isEmpty(page.getRecords())) {
			return Result.success(result);
		}
		//商品信息列表
		List<AgreementRebateOrderDetailDTO> records = page.getRecords();
		List<RebateGoodsPageListItemVO> listItemVOS = PojoUtils.map(records, RebateGoodsPageListItemVO.class);

		Map<Long, String> goodsMap = popGoodsApi.batchQueryInfo(records.stream().map(AgreementRebateOrderDetailDTO::getGoodsId)
				.collect(Collectors.toList())).stream().collect(Collectors.toMap(GoodsInfoDTO::getId, GoodsInfoDTO::getCommonName));
		listItemVOS.forEach(e -> e.setGoodsName(goodsMap.getOrDefault(e.getGoodsId(), "")));
		result.setRecords(listItemVOS);
		return Result.success(result);
	}


}
