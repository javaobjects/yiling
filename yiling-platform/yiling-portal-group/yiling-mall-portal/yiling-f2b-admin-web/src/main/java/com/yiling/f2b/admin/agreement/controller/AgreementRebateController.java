package com.yiling.f2b.admin.agreement.controller;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.yiling.export.excel.model.ImportResultModel;
import com.yiling.export.excel.util.ExeclImportUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.location.api.LocationApi;
import com.yiling.f2b.admin.agreement.form.AuditApplyForm;
import com.yiling.f2b.admin.agreement.form.CalculateRebateApplyForm;
import com.yiling.f2b.admin.agreement.form.EditApplyDetailForm;
import com.yiling.f2b.admin.agreement.form.ImportRebateApplyOrderDetailForm;
import com.yiling.f2b.admin.agreement.form.QueryAgreementRebateLogPageListForm;
import com.yiling.f2b.admin.agreement.form.QueryApplyPageListForm;
import com.yiling.f2b.admin.agreement.form.QueryEasAccountPageListForm;
import com.yiling.f2b.admin.agreement.form.QueryFinanceUseListPageListForm;
import com.yiling.f2b.admin.agreement.form.QueryFinancialRebateEntPageListForm;
import com.yiling.f2b.admin.agreement.form.QueryRebateApplyDetailPageListForm;
import com.yiling.f2b.admin.agreement.form.QueryRebateOrderDetailPageListForm;
import com.yiling.f2b.admin.agreement.form.QueryUseDetailListPageForm;
import com.yiling.f2b.admin.agreement.form.QueryUseListPageForm;
import com.yiling.f2b.admin.agreement.form.SaveRebateApplyForm;
import com.yiling.f2b.admin.agreement.handler.ImportRebateApplyOrderDetailHandler;
import com.yiling.f2b.admin.agreement.vo.AgreementRebateCommonPageVO;
import com.yiling.f2b.admin.agreement.vo.AgreementRebateLogPageListItemVo;
import com.yiling.f2b.admin.agreement.vo.ApplyGoodsVO;
import com.yiling.f2b.admin.agreement.vo.ApplyLocationVO;
import com.yiling.f2b.admin.agreement.vo.CalculateRebateApplyVO;
import com.yiling.f2b.admin.agreement.vo.CashingEntListItemVO;
import com.yiling.f2b.admin.agreement.vo.EasAccountPageListItemVO;
import com.yiling.f2b.admin.agreement.vo.FinancialRebateEntPageListItemVO;
import com.yiling.f2b.admin.agreement.vo.RebateApplyDetailPageListItemVO;
import com.yiling.f2b.admin.agreement.vo.RebateApplyOrderDetailPageListItemVO;
import com.yiling.f2b.admin.agreement.vo.RebateApplyPageListItemVO;
import com.yiling.f2b.admin.agreement.vo.RebateApplyPageVO;
import com.yiling.f2b.admin.agreement.vo.RebateOrderDetailPageListItemVO;
import com.yiling.f2b.admin.agreement.vo.RebateOrderDetailPageListVO;
import com.yiling.f2b.admin.agreement.vo.RebateSystemUrlVO;
import com.yiling.f2b.admin.agreement.vo.UseDetailPageListItemVO;
import com.yiling.f2b.admin.agreement.vo.UseDetailPageVO;
import com.yiling.f2b.admin.agreement.vo.UsePageListItemVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.BoolObject;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.framework.oss.vo.ImportResultVO;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.api.PopGoodsApi;
import com.yiling.goods.medicine.dto.GoodsInfoDTO;
import com.yiling.mall.agreement.api.AgreementBusinessApi;
import com.yiling.mall.agreement.dto.CalculateRebateApplyDTO;
import com.yiling.mall.agreement.dto.request.CalculateRebateApplyRequest;
import com.yiling.mall.agreement.dto.request.SaveRebateApplyRequest;
import com.yiling.open.erp.api.CallThirdSystemApi;
import com.yiling.open.erp.dto.AgreementApplyGoodsDTO;
import com.yiling.open.erp.dto.AgreementApplyLocationDTO;
import com.yiling.order.order.api.NoApi;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderDetailApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.user.agreement.api.AgreementApi;
import com.yiling.user.agreement.api.AgreementApplyApi;
import com.yiling.user.agreement.api.AgreementApplyDetailApi;
import com.yiling.user.agreement.api.AgreementRebateDictionariesApi;
import com.yiling.user.agreement.api.AgreementRebateLogApi;
import com.yiling.user.agreement.api.AgreementRebateOrderApi;
import com.yiling.user.agreement.api.AgreementRebateOrderDetailApi;
import com.yiling.user.agreement.api.ApplyOrderApi;
import com.yiling.user.agreement.api.UseApi;
import com.yiling.user.agreement.api.UseDetailApi;
import com.yiling.user.agreement.dto.AgreementApplyDetailDTO;
import com.yiling.user.agreement.dto.AgreementDTO;
import com.yiling.user.agreement.dto.AgreementOrderStatisticalDTO;
import com.yiling.user.agreement.dto.AgreementRebateApplyDTO;
import com.yiling.user.agreement.dto.AgreementRebateLogDTO;
import com.yiling.user.agreement.dto.AgreementRebateOrderDetailDTO;
import com.yiling.user.agreement.dto.ApplyOrderDTO;
import com.yiling.user.agreement.dto.SupplementAgreementDetailDTO;
import com.yiling.user.agreement.dto.UseDTO;
import com.yiling.user.agreement.dto.UseDetailDTO;
import com.yiling.user.agreement.dto.request.AddRebateApplyRequest;
import com.yiling.user.agreement.dto.request.EditApplyDetailRequest;
import com.yiling.user.agreement.dto.request.PageListByIdRequest;
import com.yiling.user.agreement.dto.request.QueryAgreementRebateLogPageListRequest;
import com.yiling.user.agreement.dto.request.QueryApplyOrderPageListRequest;
import com.yiling.user.agreement.dto.request.QueryApplyPageListRequest;
import com.yiling.user.agreement.dto.request.QueryRebateApplyDetailPageListRequest;
import com.yiling.user.agreement.dto.request.QueryRebateOrderPageListRequest;
import com.yiling.user.agreement.dto.request.QueryUseDetailListPageRequest;
import com.yiling.user.agreement.dto.request.QueryUseListPageRequest;
import com.yiling.user.agreement.dto.request.RebateApplyPageListItemDTO;
import com.yiling.user.agreement.enums.AgreementApplyStatusEnum;
import com.yiling.user.agreement.enums.AgreementModeEnum;
import com.yiling.user.agreement.enums.AgreementRebateApplyRangeTypeEnum;
import com.yiling.user.agreement.enums.AgreementRebateOrderCashStatusEnum;
import com.yiling.user.agreement.enums.AgreementRebateOrderConditionStatusEnum;
import com.yiling.user.agreement.enums.ApplyDetailTypeEnum;
import com.yiling.user.agreement.enums.RebateOrderTypeEnum;
import com.yiling.user.agreement.util.AgreementUtils;
import com.yiling.user.enterprise.api.CustomerApi;
import com.yiling.user.enterprise.api.EmployeeApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseCustomerEasDTO;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.EnterpriseEmployeeDTO;
import com.yiling.user.enterprise.dto.request.QueryCustomerEasInfoPageListByCurrentRequest;
import com.yiling.user.enterprise.dto.request.QueryCustomerEasInfoPageListRequest;
import com.yiling.user.system.api.DataPermissionsApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.dto.UserDTO;
import com.yiling.user.system.enums.PermissionAppEnum;

import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2021/7/8
 */
@Api(tags = "返利模块")
@RestController
@RequestMapping("/rebate")
@Slf4j
public class AgreementRebateController extends BaseController {

	@Value("${agreement.redFlush.applyUrl}")
	String applyUrl;
	@Value("${agreement.redFlush.privateKey}")
	String privateKey;

	@DubboReference
	AgreementBusinessApi           agreementBusinessApi;
	@DubboReference
	EnterpriseApi                  enterpriseApi;
	@DubboReference
	AgreementApi                   agreementApi;
	@DubboReference
	AgreementRebateLogApi          agreementRebateLogApi;
	@DubboReference
	CustomerApi                    customerApi;
	@DubboReference
	AgreementRebateOrderApi        agreementRebateOrderApi;
	@DubboReference
	AgreementRebateOrderDetailApi  agreementRebateOrderDetailApi;
	@DubboReference
	OrderApi                       orderApi;
	@DubboReference
	UserApi                        userApi;
	@DubboReference
    GoodsApi                       goodsApi;
    @DubboReference
    PopGoodsApi                    popGoodsApi;
	@DubboReference
	AgreementApplyApi              rebateApplyApi;
	@DubboReference
	AgreementApplyDetailApi        rebateApplyDetailApi;
	@DubboReference
	AgreementRebateDictionariesApi rebateDictionariesApi;
	@DubboReference
	ApplyOrderApi                  applyOrderApi;
	@DubboReference
	EmployeeApi                    employeeApi;
	@DubboReference
	UseApi                         useApi;
	@DubboReference
	UseDetailApi                   useDetailApi;
	@DubboReference
	NoApi                          noApi;
	@DubboReference
	CallThirdSystemApi             callThirdSystemApi;
	@DubboReference
	LocationApi                    locationApi;
	@DubboReference
	OrderDetailApi 					orderDetailApi;
	@DubboReference
	DataPermissionsApi              dataPermissionsApi;

	@Autowired
	ImportRebateApplyOrderDetailHandler importRebateApplyOrderDetailHandler;


	@ApiOperation(value = "财务-企业返利对账")
	@PostMapping("/queryFinancialRebateEntPageList")
	public Result<Page<FinancialRebateEntPageListItemVO>> queryFinancialRebateEntPageList(@CurrentUser CurrentStaffInfo staffInfo,
																						  @RequestBody @Valid QueryFinancialRebateEntPageListForm form) {
		//返回结果
		Page<FinancialRebateEntPageListItemVO> result;

		//分页查询企业账号
		QueryCustomerEasInfoPageListRequest request = PojoUtils.map(form, QueryCustomerEasInfoPageListRequest.class);
		request.setEid(Constants.YILING_EID);
		Page<EnterpriseCustomerEasDTO> page = customerApi.queryCustomerEasInfoPageList(request);
		result = PojoUtils.map(page, FinancialRebateEntPageListItemVO.class);
		if (CollUtil.isEmpty(page.getRecords())) {
			return Result.success(result);
		}
		List<Long> eidList = page.getRecords().stream().map(EnterpriseCustomerEasDTO::getCustomerEid).distinct().collect(Collectors.toList());
		//查询企业信息
		Map<Long, EnterpriseDTO> enterpriseMap = enterpriseApi.listByIds(eidList).stream().collect(Collectors.toMap(EnterpriseDTO::getId, e -> e));
		//查询企业返利申请
		Map<Long, List<AgreementRebateApplyDTO>> applyMap = rebateApplyApi.queryRebateApplyListByEid(eidList,AgreementApplyStatusEnum.SUCCESS);
		//查询申请返利使用
		Map<Long, List<UseDTO>> useMap = useApi.queryUseListByEid(eidList);
		//组装数据
		result.getRecords().forEach(e -> {
			PojoUtils.map(enterpriseMap.get(e.getCustomerEid()), e);
			BigDecimal totalApply = BigDecimal.ZERO;
			BigDecimal totalUse = BigDecimal.ZERO;
			//返利申请
			List<AgreementRebateApplyDTO> applyList = applyMap.get(e.getCustomerEid());
			if (CollUtil.isNotEmpty(applyList)) {
				applyList = applyMap.get(e.getCustomerEid()).stream().filter(apply -> apply.getEasCode().equals(e.getEasCode())).collect(Collectors.toList());
				for (AgreementRebateApplyDTO item : applyList) {
					totalApply = totalApply.add(item.getTotalAmount());
				}
				e.setDiscountCount(applyList.size());
			}
			//返利使用申请
			List<UseDTO> useList = useMap.get(e.getCustomerEid());
			if (CollUtil.isNotEmpty(useList)) {
				useList = useList.stream()
						.filter(use -> use.getEasCode().equals(e.getEasCode())).collect(Collectors.toList());
				for (UseDTO item : useList) {
					totalUse = totalUse.add(item.getTotalAmount());
				}
				e.setUsedCount(useList.size());
			}
			e.setDiscountAmount(totalApply);
			e.setUsedAmount(totalUse);
		});
		return Result.success(result);
	}

	@ApiOperation(value = "财务-查询使用申请")
	@PostMapping("/queryFinanceUseListPageList")
	public Result<RebateApplyPageVO<UsePageListItemVO>> queryFinanceUseListPageList(@RequestBody @Valid QueryFinanceUseListPageListForm form, @CurrentUser CurrentStaffInfo staffInfo) {
		RebateApplyPageVO<UsePageListItemVO> result;
		EnterpriseDTO enterprise = enterpriseApi.getById(form.getEid());
		result = PojoUtils.map(enterprise, RebateApplyPageVO.class);

		QueryUseListPageRequest request = PojoUtils.map(form, QueryUseListPageRequest.class);
		request.setEidList(ListUtil.toList(form.getEid()));
		request.setEasCode(form.getEasCode());
		//查询使用情况
		Page<UseDTO> page = useApi.queryUseListPageList(request);
		List<UsePageListItemVO> list = PojoUtils.map(page.getRecords(), UsePageListItemVO.class);
		PojoUtils.map(page, result);
		result.setRecords(list);
		return Result.success(result);
	}

	@ApiOperation(value = "财务-查询以兑付申请")
	@PostMapping("/queryFinanceApplyListPageList")
	public Result<RebateApplyPageVO<RebateApplyPageListItemVO>> queryFinanceApplyListPageList(@RequestBody @Valid QueryFinanceUseListPageListForm form, @CurrentUser CurrentStaffInfo staffInfo) {
		RebateApplyPageVO<RebateApplyPageListItemVO> result;

		//查询企业信息
		EnterpriseDTO enterprise = enterpriseApi.getById(form.getEid());
		result = PojoUtils.map(enterprise, RebateApplyPageVO.class);
		//查询返利申请
		QueryApplyPageListRequest request = PojoUtils.map(form, QueryApplyPageListRequest.class);
		Page<RebateApplyPageListItemDTO> page = rebateApplyApi.queryRebateApplyPageList(request);
		List<AgreementRebateApplyDTO> applyList = rebateApplyApi.queryRebateApplyListByEid(ListUtil.toList(form.getEid()),AgreementApplyStatusEnum.SUCCESS)
				.get(form.getEid());
		//计算返利总额
		if (CollUtil.isNotEmpty(applyList)) {
			BigDecimal totalAmount = BigDecimal.ZERO;
			for (AgreementRebateApplyDTO item : applyList) {
				totalAmount = totalAmount.add(item.getTotalAmount());
			}
			result.setTotalAmount(totalAmount);
		}

		if (CollUtil.isEmpty(page.getRecords())) {
			return Result.success(result);
		}
		List<RebateApplyPageListItemVO> list = PojoUtils.map(page.getRecords(), RebateApplyPageListItemVO.class);
		result.setRecords(list);

		//创建人id
		List<Long> userId = result.getRecords().stream().map(RebateApplyPageListItemVO::getCreateUser)
				.collect(Collectors.toList());
		List<Long> eidList = result.getRecords().stream().map(RebateApplyPageListItemVO::getEid)
				.collect(Collectors.toList());
		Map<Long, UserDTO> userDTOMap = userApi.listByIds(userId).stream().collect(Collectors.toMap(UserDTO::getId, e -> e));
		Map<Long, EnterpriseDTO> entMap = enterpriseApi.listByIds(eidList)
				.stream().collect(Collectors.toMap(EnterpriseDTO::getId, e -> e));
		//补全操作人
		result.getRecords().forEach(e -> {
			e.setCreateUserName(userDTOMap.get(e.getCreateUser()).getName());
			EnterpriseDTO enterpriseDTO = entMap.get(e.getEid());
			e.setName(enterpriseDTO.getName());
			e.setChannelId(enterpriseDTO.getChannelId());
			e.setEntStatus(enterpriseDTO.getStatus());
		});
		return Result.success(result);
	}


	@ApiOperation(value = "协议待兑付或以兑付订单明细")
	@PostMapping("/queryRebateOrderDetailPageList")
	public Result<RebateOrderDetailPageListVO<RebateOrderDetailPageListItemVO>> queryRebateOrderDetailPageList(@CurrentUser CurrentStaffInfo staffInfo,
																											   @RequestBody @Valid QueryRebateOrderDetailPageListForm pageListForm) {

		//查询统计数
		AgreementOrderStatisticalDTO statisticalInfo = agreementRebateOrderApi.statisticsOrderCount(pageListForm.getAgreementId());
		RebateOrderDetailPageListVO<RebateOrderDetailPageListItemVO> result = PojoUtils.map(statisticalInfo, RebateOrderDetailPageListVO.class);
		//查询账号下的总兑付金额
		Map<String, BigDecimal> discountAmountMap = agreementRebateLogApi.queryEntAccountDiscountAmount(ListUtil.toList(pageListForm.getAccount()), null);
		result.setDiscountAmount(discountAmountMap.getOrDefault(pageListForm.getAccount(), BigDecimal.ZERO));
		//查询协议名称
		SupplementAgreementDetailDTO agreementDetailInfo = agreementApi.querySupplementAgreementsDetail(pageListForm.getAgreementId());
		result.setName(agreementDetailInfo.getName());
		result.setCategory(agreementDetailInfo.getCategory());

		//查询订单记录
		QueryRebateOrderPageListRequest pageListRequest = PojoUtils.map(pageListForm, QueryRebateOrderPageListRequest.class);
		Page<AgreementRebateOrderDetailDTO> page = agreementRebateOrderDetailApi.queryRebateOrderPageList(pageListRequest,
				AgreementRebateOrderConditionStatusEnum.getByCode(pageListForm.getConditionStatus()),
				AgreementRebateOrderCashStatusEnum.getByCode(pageListForm.getCashStatus()));

		PojoUtils.map(page, result);
		List<RebateOrderDetailPageListItemVO> records = PojoUtils.map(page.getRecords(), RebateOrderDetailPageListItemVO.class);
		result.setRecords(records);
		if (CollUtil.isEmpty(page.getRecords())) {
			return Result.success(result);
		}
		//订单id列表
		List<Long> orderIdList = page.getRecords().stream().map(AgreementRebateOrderDetailDTO::getOrderId)
				.distinct().collect(Collectors.toList());
		Map<Long, OrderDTO> orderMap = orderApi.listByIds(orderIdList).stream().collect(Collectors.toMap(OrderDTO::getId, e -> e));
		//商品id列表
		List<Long> goodsIdList = page.getRecords().stream().map(AgreementRebateOrderDetailDTO::getGoodsId)
				.distinct().collect(Collectors.toList());
		//查询商品信息
		Map<Long, GoodsInfoDTO> goodsInfoDTOMap = popGoodsApi.batchQueryInfo(goodsIdList).stream().collect(Collectors.toMap(GoodsInfoDTO::getId, e -> e));

		result.getRecords().forEach(e -> {
			//补全列表订单信息
			PojoUtils.map(orderMap.get(e.getOrderId()), e);
			//补全订单列表商品信息
			GoodsInfoDTO goodsInfoDTO = goodsInfoDTOMap.get(e.getGoodsId());
			e.setGoodsName(goodsInfoDTO.getName());
			e.setGoodsLicenseNo(goodsInfoDTO.getLicenseNo());
			e.setGoodsSpecification(goodsInfoDTO.getSellSpecifications());
		});
		return Result.success(result);
	}

	@ApiOperation("查询企业兑付日志")
	@PostMapping("/queryAgreementRebateLogPageList")
	public Result<AgreementRebateCommonPageVO<AgreementRebateLogPageListItemVo>> queryAgreementRebateLogPageList(@RequestBody @Valid QueryAgreementRebateLogPageListForm listForm) {
		AgreementRebateCommonPageVO result;
		//返回值分页列表
		List<AgreementRebateLogPageListItemVo> records;
		//企业信息
		EnterpriseDTO enterpriseInfo;
		//用户map
		Map<Long, UserDTO> userMap;

		//查询企业信息
		enterpriseInfo = enterpriseApi.getById(listForm.getEid());
		if (enterpriseInfo == null) {
			return Result.failed("该企业id不存在");
		}
		result = PojoUtils.map(enterpriseInfo, AgreementRebateCommonPageVO.class);

		QueryAgreementRebateLogPageListRequest request = PojoUtils.map(listForm, QueryAgreementRebateLogPageListRequest.class);
		request.setAccounts(ListUtil.toList(listForm.getEasAccount()));
		Page<AgreementRebateLogDTO> page = agreementRebateLogApi.queryAgreementRebateLogPageList(request);
		PojoUtils.map(page, result);
		if (CollUtil.isEmpty(page.getRecords())) {
			return Result.success(result);
		}
		records = PojoUtils.map(page.getRecords(), AgreementRebateLogPageListItemVo.class);
		result.setRecords(records);
		userMap = userApi.listByIds(records.stream().map(AgreementRebateLogPageListItemVo::getCreateUser)
				.collect(Collectors.toList()))
				.stream().collect(Collectors.toMap(UserDTO::getId, e -> e));
		//补全操作人信息
		records.forEach(e -> {
			UserDTO userDTO = userMap.get(e.getCreateUser());
			if (userDTO != null) {
				e.setLogName(userDTO.getName());
			}
		});
		return Result.success(result);
	}

	@ApiOperation("查询返利申请单列表")
	@PostMapping("/queryApplyPageList")
	public Result<Page<RebateApplyPageListItemVO>> queryApplyPageList(@RequestBody @Valid QueryApplyPageListForm form, @CurrentUser CurrentStaffInfo staffInfo) {
		Page<RebateApplyPageListItemVO> result;

		QueryApplyPageListRequest request = PojoUtils.map(form, QueryApplyPageListRequest.class);

		//查询数据权限
		List<Long> authorizedUserIds = dataPermissionsApi.listAuthorizedUserIds(PermissionAppEnum.MALL_ADMIN_POP, staffInfo.getCurrentEid(), staffInfo.getCurrentUserId());


		//如果是商务查询---由于b2b_v1.0新增了数据权限此参数作废
//		Integer queryType = 1;
//		if (queryType.equals(form.getQueryType())) {
//			request.setCreateUser(staffInfo.getCurrentUserId());
//		}
		//设置数据权限
		request.setCreateUserList(authorizedUserIds);
		Page<RebateApplyPageListItemDTO> page = rebateApplyApi.queryRebateApplyPageList(request);
		result = PojoUtils.map(page, RebateApplyPageListItemVO.class);

		if (CollUtil.isEmpty(result.getRecords())) {
			return Result.success(result);
		}
		//创建人id
		List<Long> userId = result.getRecords().stream().map(RebateApplyPageListItemVO::getCreateUser)
				.collect(Collectors.toList());
		List<Long> eidList = result.getRecords().stream().map(RebateApplyPageListItemVO::getEid)
				.collect(Collectors.toList());
		Map<Long, UserDTO> userDTOMap = userApi.listByIds(userId).stream().collect(Collectors.toMap(UserDTO::getId, e -> e));
		Map<Long, EnterpriseDTO> entMap = enterpriseApi.listByIds(eidList)
				.stream().collect(Collectors.toMap(EnterpriseDTO::getId, e -> e));
		//补全操作人
		result.getRecords().forEach(e -> {
			e.setTotalAmount(e.getTotalAmount().setScale(2,BigDecimal.ROUND_HALF_UP));
			e.setCreateUserName(userDTOMap.get(e.getCreateUser()).getName());
			EnterpriseDTO enterpriseDTO = entMap.get(e.getEid());
			if (enterpriseDTO != null) {
				EnterpriseDTO enterprise = entMap.get(e.getEid());
				e.setName(enterprise.getName());
				e.setEid(enterprise.getId());
				e.setChannelId(enterprise.getChannelId());
				e.setEntStatus(enterpriseDTO.getStatus());
			}
		});
		return Result.success(result);
	}

	@ApiOperation("查询企业账号列表")
	@PostMapping("/queryEasAccountPageList")
	public Result<Page<EasAccountPageListItemVO>> queryEasAccountPageList(@RequestBody QueryEasAccountPageListForm form,@CurrentUser CurrentStaffInfo staffInfo ) {
		Page<EasAccountPageListItemVO> result;

		//分页查询当前登录人负责的企业账号
		QueryCustomerEasInfoPageListByCurrentRequest request = PojoUtils.map(form, QueryCustomerEasInfoPageListByCurrentRequest.class);
		request.setOpUserId(staffInfo.getCurrentUserId());
		Page<EnterpriseCustomerEasDTO> enterpriseAccountPage = customerApi.queryCustomerEasInfoPageListByCurrent(request);
		result = PojoUtils.map(enterpriseAccountPage, EasAccountPageListItemVO.class);
		if (CollUtil.isEmpty(result.getRecords())) {
			return Result.success(result);
		}
		List<Long> eidList = result.getRecords().stream().map(EasAccountPageListItemVO::getCustomerEid).collect(Collectors.toList());
		//查询是否有兑付一级商----查询企业列表下的三方协议
		List<AgreementDTO> agreementDTOList = agreementApi.queryAgreementList(eidList,
				AgreementModeEnum.THIRD_AGREEMENTS, null);
		//过滤掉当前企业为乙方的协议
		agreementDTOList = agreementDTOList.stream().filter(agreement -> eidList.contains(agreement.getThirdEid())).collect(Collectors.toList());
		Map<Long, List<AgreementDTO>> firstLevelEntMap = agreementDTOList.stream().collect(Collectors.groupingBy(AgreementDTO::getThirdEid));
		//补全企业信息
		if (CollUtil.isNotEmpty(result.getRecords())) {
			List<Long> sellerEids = result.getRecords().stream().map(EasAccountPageListItemVO::getCustomerEid).distinct()
					.collect(Collectors.toList());
			Map<Long, EnterpriseDTO> erpCodeMap = enterpriseApi.listByIds(sellerEids).stream()
					.collect(Collectors.toMap(EnterpriseDTO::getId, e -> e));
			result.getRecords().forEach(e -> {
				EnterpriseDTO enterpriseDTO = erpCodeMap.get(e.getCustomerEid());
				PojoUtils.map(enterpriseDTO, e);
				e.setEasName(enterpriseDTO.getName());
				String address = enterpriseDTO.getProvinceName() + enterpriseDTO.getCityName() +
						enterpriseDTO.getRegionName() + enterpriseDTO.getAddress();
				e.setAddress(address);
				//设置是否有兑付一级商
				List<AgreementDTO> agreements = firstLevelEntMap.get(e.getCustomerEid());
				e.setIsFirst(CollUtil.isNotEmpty(agreements));
			});
		}
		return Result.success(result);
	}

	@ApiOperation("查询兑付一级商")
	@GetMapping("/queryCashingEntList")
	public Result<CollectionObject<CashingEntListItemVO>> queryCashingEntList(@RequestParam Long applyEid) {
		List<CashingEntListItemVO> result = ListUtil.toList();
		//查询applyEid下的三方协议
		List<AgreementDTO> agreementDTOList = agreementApi.queryAgreementList(ListUtil.toList(applyEid),
				AgreementModeEnum.THIRD_AGREEMENTS, null);
		//过滤掉当前企业为乙方的协议
		agreementDTOList = agreementDTOList.stream().filter(agreement -> agreement.getThirdEid().equals(applyEid)).collect(Collectors.toList());

		if (CollUtil.isEmpty(agreementDTOList)) {
			return Result.success(new CollectionObject<>(ListUtil.toList()));
		}
		Map<Long, List<AgreementDTO>> firstLevelEntMap = agreementDTOList.stream().collect(Collectors.groupingBy(AgreementDTO::getSecondEid));
		//根据企业id查询账号
		Map<Long, List<EnterpriseCustomerEasDTO>> easCodeMap = customerApi.listCustomerEasInfos(Constants.YILING_EID,
				new ArrayList<>(firstLevelEntMap.keySet()));
		Map<Long, EnterpriseDTO> entMap = enterpriseApi.listByIds(new ArrayList<>(firstLevelEntMap.keySet()))
				.stream().collect(Collectors.toMap(EnterpriseDTO::getId, e -> e));
		easCodeMap.forEach((id, codeList) -> {
			CashingEntListItemVO vo = new CashingEntListItemVO();
			vo.setEid(id);
			vo.setName(entMap.get(id).getName());
			List<CashingEntListItemVO.EasCode> easCodeList = PojoUtils.map(codeList, CashingEntListItemVO.EasCode.class);
			vo.setEasAccountList(easCodeList);
			result.add(vo);
		});
		return Result.success(new CollectionObject<>(result));
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
		}else {
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
			} else if (CalculateRebateApplyForm.monthType14.equals(month) ) {
				startDateStr = startDateStr + "04-01";
			} else if (CalculateRebateApplyForm.monthType15.equals(month)) {
				startDateStr = startDateStr + "07-01";
			} else if (CalculateRebateApplyForm.monthType16.equals(month) ) {
				startDateStr = startDateStr + "10-01";
			}
			startDate = DateUtil.parse(startDateStr, "yyyy-MM-dd");
			endDate = DateUtil.endOfQuarter(startDate);
		} else if (month >= CalculateRebateApplyForm.monthType17 && month <= CalculateRebateApplyForm.monthType18) {
			String endDateStr = null;
			//年度
			if (CalculateRebateApplyForm.monthType17.equals(month) ) {
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
		List<EnterpriseEmployeeDTO> enterpriseEmployeeDTOS = employeeApi.listByEidUserIds(staffInfo.getCurrentEid(), ListUtil.toList(staffInfo.getCurrentUserId()));
		if (CollUtil.isEmpty(enterpriseEmployeeDTOS)) {
			return Result.failed("操作人不存在");
		}
		EnterpriseEmployeeDTO employee = enterpriseEmployeeDTOS.get(0);
		request.setName(enterpriseNames.get(form.getEid()));
		request.setEntryName(enterpriseNames.getOrDefault(form.getEntryEid(), ""));
		request.setCreateUserCode(employee.getCode());

		Integer month = form.getMonth();
		//月度
		if (month >= 1 && month <= CalculateRebateApplyForm.monthType12) {
			request.setRangeType(AgreementRebateApplyRangeTypeEnum.MONTH.getCode());
		} else if (month >= CalculateRebateApplyForm.monthType13 && month <= CalculateRebateApplyForm.monthType16) {
			if (CalculateRebateApplyForm.monthType13.equals(month)){
				request.setMonth(1);
			}
			if (CalculateRebateApplyForm.monthType14.equals(month)){
				request.setMonth(2);
			}
			if (CalculateRebateApplyForm.monthType15.equals(month)){
				request.setMonth(3);
			}
			if (CalculateRebateApplyForm.monthType16.equals(month)){
				request.setMonth(4);
			}
			//季度
			request.setRangeType(AgreementRebateApplyRangeTypeEnum.QUARTER.getCode());
		} else if ( CalculateRebateApplyForm.monthType17.equals(month)) {
			//上半年
			request.setRangeType(AgreementRebateApplyRangeTypeEnum.FIRST_HALF_YEAR.getCode());
			request.setMonth(0);
		} else if (CalculateRebateApplyForm.monthType18.equals(month) ) {
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
		}else {
			request.setInputEntry(1);
		}
		request.setOpUserId(staffInfo.getCurrentUserId());
		Boolean isSave = agreementBusinessApi.saveRebateApply(request);
		return Result.success(new BoolObject(isSave));
	}


	@ApiOperation("查询返利申请明细")
	@PostMapping("/queryRebateApplyPageList")
	public Result<RebateApplyPageVO<RebateApplyDetailPageListItemVO>> queryRebateApplyPageList(@RequestBody @Valid QueryRebateApplyDetailPageListForm form) {
		RebateApplyPageVO<RebateApplyDetailPageListItemVO> result = new RebateApplyPageVO<>();
		//分页列表
		List<RebateApplyDetailPageListItemVO> applyDetailList = ListUtil.toList();
		//查询返利申请
		List<AgreementRebateApplyDTO> rebateApplyList = rebateApplyApi.queryRebateApplyList(ListUtil.toList(form.getApplyId()));
		if (CollUtil.isEmpty(rebateApplyList)) {
			return Result.success(result);
		}
		AgreementRebateApplyDTO rebateApplyDTO = rebateApplyList.get(0);
		//查询企业信息
		EnterpriseDTO enterpriseDTO = enterpriseApi.getById(rebateApplyDTO.getEid());
		result = PojoUtils.map(enterpriseDTO, RebateApplyPageVO.class);

		//分页查询申请明细或
		QueryRebateApplyDetailPageListRequest pageRequest = PojoUtils.map(form, QueryRebateApplyDetailPageListRequest.class);
		Page<AgreementApplyDetailDTO> page = rebateApplyDetailApi.queryRebateApplyDetailPageList(pageRequest);
		PojoUtils.map(page, result);
		if (CollUtil.isEmpty(page.getRecords())) {
			return Result.success(result);
		}
		result.setApplyStatus(rebateApplyDTO.getStatus());
		result.setCode(rebateApplyDTO.getCode());
		result.setEasCode(rebateApplyDTO.getEasCode());
		result.setRecords(PojoUtils.map(page.getRecords(), RebateApplyDetailPageListItemVO.class));
		result.setTotalAmount(rebateApplyDTO.getTotalAmount().setScale(2,BigDecimal.ROUND_HALF_UP));
		result.setCreateTime(rebateApplyDTO.getCreateTime());
		//补全数据
		//查询协议
		List<Long> agreementIdList = page.getRecords().stream()
				.filter(e -> ApplyDetailTypeEnum.AGREEMENT.getCode().equals(e.getDetailType()))
				.map(AgreementApplyDetailDTO::getAgreementId).collect(Collectors.toList());
		Map<Long, SupplementAgreementDetailDTO> agreementMap = agreementApi.querySupplementAgreementsDetailList(agreementIdList)
				.stream().collect(Collectors.toMap(SupplementAgreementDetailDTO::getId, e -> e));
		result.getRecords().forEach(e -> {
			if (ApplyDetailTypeEnum.AGREEMENT.getCode().equals(e.getDetailType())) {
				SupplementAgreementDetailDTO agreementDTO = agreementMap.get(e.getAgreementId());
				e.setContent(AgreementUtils.getAgreementText(agreementDTO));
				e.setName(agreementDTO.getName());
			}

		});
		return Result.success(result);
	}

	@ApiOperation("查询返利申请订单明细")
	@PostMapping("/queryRebateApplyOrderDetailPageList")
	public Result<RebateApplyPageVO<RebateApplyOrderDetailPageListItemVO>> queryRebateApplyOrderDetailPageList(@RequestBody @Valid QueryRebateApplyDetailPageListForm form) {
		RebateApplyPageVO<RebateApplyOrderDetailPageListItemVO> result;
		//分页列表
		List<RebateApplyOrderDetailPageListItemVO> applyOrderDetailList = ListUtil.toList();
		//查询返利申请
		List<AgreementRebateApplyDTO> rebateApplyList = rebateApplyApi.queryRebateApplyList(ListUtil.toList(form.getApplyId()));
		if (CollUtil.isEmpty(rebateApplyList)) {
			return Result.failed("申请单不存在");
		}
		AgreementRebateApplyDTO rebateApplyDTO = rebateApplyList.get(0);
		//查询企业信息
		EnterpriseDTO enterpriseDTO = enterpriseApi.getById(rebateApplyDTO.getEid());
		result = PojoUtils.map(enterpriseDTO, RebateApplyPageVO.class);


		QueryApplyOrderPageListRequest pageListRequest = PojoUtils.map(form, QueryApplyOrderPageListRequest.class);

		List<ApplyOrderDTO> applyDetailDTOS = applyOrderApi.queryApplyOrderList(ListUtil.toList(form.getApplyId()));
		result.setApplyStatus(rebateApplyDTO.getStatus());
		result.setCode(rebateApplyDTO.getCode());
		result.setEasCode(rebateApplyDTO.getEasCode());
		result.setTotalAmount(rebateApplyDTO.getTotalAmount().setScale(2,BigDecimal.ROUND_HALF_UP));
		if(CollUtil.isEmpty(applyDetailDTOS)){
			return Result.success(result);
		}
		List<Long> rebateOrderDetailId=ListUtil.toList();
		applyDetailDTOS.forEach(e -> {
			long[] ids = StrUtil.splitToLong(e.getRebateOrderDetailId(), ",");
			rebateOrderDetailId.addAll(Arrays.stream(ids).boxed().collect(Collectors.toList()));
		});
		PageListByIdRequest request=PojoUtils.map(form,PageListByIdRequest.class);
		request.setIdList(rebateOrderDetailId);
		Page<AgreementRebateOrderDetailDTO> page = agreementRebateOrderDetailApi.pageListById(request);
		PojoUtils.map(page,result);
		result.setCreateTime(rebateApplyDTO.getCreateTime());
		if (CollUtil.isEmpty(page.getRecords())){
			return Result.success(result);
		}
		//补全数据
		List<Long> agreementIdList = page.getRecords().stream().map(AgreementRebateOrderDetailDTO::getAgreementId).collect(Collectors.toList());
		//查询协议
		Map<Long, SupplementAgreementDetailDTO> agreementMap = agreementApi.querySupplementAgreementsDetailList(agreementIdList)
				.stream().collect(Collectors.toMap(SupplementAgreementDetailDTO::getId, e -> e));

		page.getRecords().forEach(e -> {
			SupplementAgreementDetailDTO agreementDTO = agreementMap.get(e.getAgreementId());
			OrderDetailDTO orderDetail = orderDetailApi.getOrderDetailById(e.getOrderDetailId());
			RebateApplyOrderDetailPageListItemVO vo= PojoUtils.map(agreementDTO,RebateApplyOrderDetailPageListItemVO.class);
			vo.setContent(AgreementUtils.getAgreementText(agreementDTO));
			vo.setOrderCode(orderDetail.getOrderNo());
			vo.setGoodsId(e.getGoodsId());
			vo.setGoodsName(orderDetail.getGoodsName());
			vo.setErpCode(orderDetail.getGoodsErpCode());
			vo.setOrderType(e.getType());
			vo.setGoodsQuantity(e.getGoodsQuantity());
			vo.setPrice(e.getGoodsAmount());
			BigDecimal amount = e.getDiscountAmount().setScale(2,BigDecimal.ROUND_HALF_UP);
			//如果是退款单转为负数
			if (RebateOrderTypeEnum.REFUND.getCode().equals(e.getType())){
				vo.setDiscountAmount(amount.negate());
			}else {
				vo.setDiscountAmount(amount);
			}
			applyOrderDetailList.add(vo);
		});
		result.setRecords(applyOrderDetailList);
		return Result.success(result);
	}

	@ApiOperation(value = "导入返利申请订单更新信息")
	@PostMapping("/importRebateApplyOrderDetail")
	public Result<ImportResultVO> importRebateApplyOrderDetail(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam("file") MultipartFile file) {
		ImportParams params = new ImportParams();
		params.setNeedSave(false);
		params.setNeedVerify(true);
		params.setVerifyHandler(importRebateApplyOrderDetailHandler);

		InputStream in;
		try {
			in = file.getInputStream();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			return Result.failed(ResultCode.UPLOAD_FILE_FAILED);
		}

		ImportResultModel importResultModel;
		try {
            Map<String,Object> paramMap=new HashMap<>();
            paramMap.put(MyMetaHandler.FIELD_OP_USER_ID,staffInfo.getCurrentUserId());
			importResultModel = ExeclImportUtils.importExcelMore(in, ImportRebateApplyOrderDetailForm.class, params, importRebateApplyOrderDetailHandler, paramMap);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Result.failed(ResultCode.EXCEL_PARSING_ERROR);
		}

		return Result.success(PojoUtils.map(importResultModel, ImportResultVO.class));
	}


	@ApiOperation(value = "获取跳转冲红系统url")
	@PostMapping("/getRebateSystemUrl")
	public Result<RebateSystemUrlVO> getRebateSystemUrl(@CurrentUser CurrentStaffInfo staffInfo) {
		RebateSystemUrlVO result = new RebateSystemUrlVO();

		EnterpriseEmployeeDTO employee = employeeApi.getByEidUserId(staffInfo.getCurrentEid(), staffInfo.getCurrentUserId());
		String url = applyUrl;
		url += "&code=" + employee.getCode();
		String dateStr = DateUtil.formatDateTime(new Date());
		String md5 = SecureUtil.md5(dateStr + privateKey);
		url += "&key=" + md5;

		result.setUrl(url);
		return Result.success(result);
	}

	@ApiOperation(value = "信用科财务---入账审核接口")
	@PostMapping("/auditApply")
	public Result<BoolObject> auditApply(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid AuditApplyForm form) {
		List<AgreementRebateApplyDTO> applyList = rebateApplyApi.queryRebateApplyList(ListUtil.toList(form.getId()));
		if (CollUtil.isEmpty(applyList)) {
			return Result.failed("申请不存在");
		}
		AddRebateApplyRequest request = PojoUtils.map(form, AddRebateApplyRequest.class);
		request.setOpUserId(staffInfo.getCurrentUserId());
		if (AgreementApplyStatusEnum.SUCCESS.getCode().equals(form.getStatus())){
			request.setPushStatus(1);
		}
		request.setAuditTime(new Date());
		Boolean isUpdate = rebateApplyApi.updateById(request);
		return Result.success(new BoolObject(isUpdate));
	}

	@ApiOperation(value = "返利申请---查询商品")
	@GetMapping("/queryGoods")
	public Result<CollectionObject<ApplyGoodsVO>> queryGoods() {
		List<AgreementApplyGoodsDTO> goods = callThirdSystemApi.queryAgreementApplyGoods();
		List<ApplyGoodsVO> result = PojoUtils.map(goods, ApplyGoodsVO.class);
		return Result.success(new CollectionObject<>(result));
	}
	@ApiOperation(value = "返利申请---查询省份")
	@GetMapping("/queryLocation")
	public Result<CollectionObject<ApplyLocationVO>> queryLocation() {
		List<AgreementApplyLocationDTO> goods = callThirdSystemApi.queryAgreementApplyLocation();
		List<ApplyLocationVO> result = PojoUtils.map(goods, ApplyLocationVO.class);
		return Result.success(new CollectionObject<>(result));
	}

	@ApiOperation(value = "返利使用---查询使用申请列表")
	@PostMapping("/queryUseListPageList")
	public Result<Page<UsePageListItemVO>> queryUseListPageList(@RequestBody @Valid QueryUseListPageForm form, @CurrentUser CurrentStaffInfo staffInfo) {
		QueryUseListPageRequest request = PojoUtils.map(form, QueryUseListPageRequest.class);
		//查询数据权限
		List<Long> authorizedUserIds = dataPermissionsApi.listAuthorizedUserIds(PermissionAppEnum.MALL_ADMIN_POP, staffInfo.getCurrentEid(), staffInfo.getCurrentUserId());

		request.setCreateUserIdList(authorizedUserIds);
//		//查询当前用户负责的企业---由于b2b_v1.0新增了数据权限此参数作废
//		Map<Long, List<EnterpriseDTO>> contactUserMap = enterpriseApi.listByContactUserIds(ErpConstants.YILING_EID,
//				ListUtil.toList(staffInfo.getCurrentUserId()));
//		//如果用户没有负责企业
//		if(MapUtil.isEmpty(contactUserMap)){
//			return Result.success(new Page<>());
//		}
//		request.setEidList(contactUserMap
//				.get(staffInfo.getCurrentUserId()).stream().map(EnterpriseDTO::getId).collect(Collectors.toList()));
		//查询使用情况
		Page<UseDTO> page = useApi.queryUseListPageList(request);
		return Result.success(PojoUtils.map(page, UsePageListItemVO.class));
	}

	@ApiOperation(value = "返利使用---查询使用申请明细")
	@PostMapping("/queryUseDetailListPageList")
	public Result<UseDetailPageVO<UseDetailPageListItemVO>> queryUseDetailListPageList(@RequestBody @Valid QueryUseDetailListPageForm form) {
		UseDetailPageVO<UseDetailPageListItemVO> result;
		//查询使用申请单
		UseDTO useDTO = useApi.queryById(form.getUseId());
		if (ObjectUtil.isNull(useDTO)) {
			return Result.failed("申请单不存在");
		}
		//查询企业信息
		List<EnterpriseDTO> enterpriseDTOS = enterpriseApi.listByIds(ListUtil.toList(useDTO.getEid()));
		EnterpriseDTO enterpriseDTO = enterpriseDTOS.get(0);
		result = PojoUtils.map(enterpriseDTO, UseDetailPageVO.class);
		result.setEntStatus(enterpriseDTO.getStatus());
		PojoUtils.map(useDTO, result);
		result.setName(enterpriseDTO.getName());
		QueryUseDetailListPageRequest request = new QueryUseDetailListPageRequest();
		request.setUseIdList(ListUtil.toList(form.getUseId()));
		Page<UseDetailDTO> page = useDetailApi.queryUseDetailListPageList(request);
		PojoUtils.map(page, result);
		if (CollUtil.isNotEmpty(page.getRecords())) {
			List<UseDetailPageListItemVO> records = PojoUtils.map(page.getRecords(), UseDetailPageListItemVO.class);
			result.setRecords(records);
		}
		return Result.success(result);
	}


	@PostMapping("/editApplyDetail")
	@ApiOperation("返利申请---修改入账明细")
	public Result<BoolObject> editApplyDetail(@Valid @RequestBody EditApplyDetailForm form,@CurrentUser CurrentStaffInfo staffInfo){
		List<AgreementRebateApplyDTO> applyList = rebateApplyApi.queryRebateApplyList(ListUtil.toList(form.getApplyId()));
		if (CollUtil.isEmpty(applyList)){
			return Result.failed("返利申请单不存在");
		}
		AgreementRebateApplyDTO applyDTO=applyList.get(0);
		EditApplyDetailRequest request=PojoUtils.map(form,EditApplyDetailRequest.class);
		request.setEid(applyDTO.getEid());
		request.setEasCode(applyDTO.getEasCode());
		request.setApplyId(applyDTO.getId());
		Boolean isEdit = rebateApplyDetailApi.editApplyDetail(request);
		return Result.success(new BoolObject(isEdit));
	}

}
