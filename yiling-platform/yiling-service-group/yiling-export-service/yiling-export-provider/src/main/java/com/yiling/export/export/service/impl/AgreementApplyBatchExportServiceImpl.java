package com.yiling.export.export.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.export.export.bo.ExportAgreementApplyBO;
import com.yiling.export.export.bo.ExportAgreementApplyDetailBO;
import com.yiling.export.export.bo.ExportAgreementApplyOrderBO;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.api.OrderDetailApi;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.user.agreement.api.AgreementApi;
import com.yiling.user.agreement.api.AgreementApplyApi;
import com.yiling.user.agreement.api.AgreementApplyDetailApi;
import com.yiling.user.agreement.api.AgreementRebateOrderDetailApi;
import com.yiling.user.agreement.api.ApplyOrderApi;
import com.yiling.user.agreement.dto.AgreementApplyDetailDTO;
import com.yiling.user.agreement.dto.AgreementDTO;
import com.yiling.user.agreement.dto.AgreementRebateApplyDTO;
import com.yiling.user.agreement.dto.AgreementRebateOrderDetailDTO;
import com.yiling.user.agreement.dto.ApplyOrderDTO;
import com.yiling.user.agreement.dto.SupplementAgreementDetailDTO;
import com.yiling.user.agreement.dto.request.ExportAgreementBatchApplyRequest;
import com.yiling.user.agreement.dto.request.PageListByIdRequest;
import com.yiling.user.agreement.dto.request.QueryApplyPageListRequest;
import com.yiling.user.agreement.dto.request.QueryRebateApplyDetailPageListRequest;
import com.yiling.user.agreement.dto.request.RebateApplyPageListItemDTO;
import com.yiling.user.agreement.enums.AgreementRebateApplyRangeTypeEnum;
import com.yiling.user.agreement.enums.ApplyDetailTypeEnum;
import com.yiling.user.agreement.enums.RebateOrderTypeEnum;
import com.yiling.user.agreement.util.AgreementUtils;
import com.yiling.user.system.api.DataPermissionsApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.dto.UserDTO;
import com.yiling.user.system.enums.PermissionAppEnum;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 批量导出申请单明细
 *
 * @author: dexi.yao
 * @date: 2021/7/30
 */
@Service("agreementApplyBatchExportService")
@Slf4j
public class AgreementApplyBatchExportServiceImpl implements BaseExportQueryDataService<ExportAgreementBatchApplyRequest> {

	@DubboReference
	AgreementApplyApi             rebateApplyApi;
	@DubboReference
	AgreementApplyDetailApi       rebateApplyDetailApi;
	@DubboReference
	ApplyOrderApi                 applyOrderApi;
	@DubboReference
	UserApi                       userApi;
	@DubboReference
	AgreementApi                  agreementApi;
	@DubboReference
	AgreementRebateOrderDetailApi rebateOrderDetailApi;
	@DubboReference
	OrderDetailApi                orderDetailApi;
	@DubboReference
	DataPermissionsApi dataPermissionsApi;

	public static String[] str=new String[]{"零","一","二","三","四"};

	private static final LinkedHashMap<String, String> FIELD_SHEET  = new LinkedHashMap<String, String>() {

		{
			put("code", "申请单号");
			put("entryEid", "入账企业ID");
			put("entryName", "入账企业名称");
			put("provinceName", "所属省份");
			put("totalAmount", "返利总金额");
			put("year", "归属年度");
			put("month", "归属月度");
			put("goodsName", "品种");
			put("eid", "返利所属企业ID");
			put("name", "返利所属企业名称");
			put("user", "操作人");
		}
	};
	private static final LinkedHashMap<String, String> FIELD_SHEET2 = new LinkedHashMap<String, String>() {

		{
			put("code", "申请单号");
			put("entryEid", "入账企业ID");
			put("entryName", "入账企业名称");
			put("provinceName", "所属省份");
			put("detailId", "申请明细Id");
			put("detailType", "入账类型");
			put("entryDescribe", "入账原因");
			put("year", "归属年度");
			put("month", "归属月度");
			put("applyTime", "申请日期");
			put("goodsName", "品种");
			put("amount", "返利金额");
			put("orderCount", "订单数量");
			put("rebateCategory", "返利种类");
			put("sellerName", "发货组织");
			put("costSubject", "费用科目");
			put("costDept", "费用归属部门");
			put("executeDept", "执行部门");
			put("replyCode", "批复代码");
		}
	};
	private static final LinkedHashMap<String, String> FIELD_SHEET3 = new LinkedHashMap<String, String>() {

		{
			put("code", "申请单号");
			put("agreementId", "协议ID");
			put("name", "协议名称");
			put("orderCode", "订单号");
			put("goodsName", "商品名称");
			put("goodsId", "商品编码");
			put("erpCode", "商品erp内码");
			put("orderType", "类型");
			put("goodsQuantity", "成交数量");
			put("price", "销售金额");
			put("discountAmount", "返利金额");
		}
	};

	@Override
	public QueryExportDataDTO queryData(ExportAgreementBatchApplyRequest request) {

		log.debug("agreementApplyBatchExportService参数：{}", JSON.toJSON(request));
		//需要返回的对象
		QueryExportDataDTO result = new QueryExportDataDTO();

		//不同sheet数据
		List<Map<String, Object>> applyData = new ArrayList<>();
		List<Map<String, Object>> applyDetailData = new ArrayList<>();
		List<Map<String, Object>> applyOrderData = new ArrayList<>();

		QueryApplyPageListRequest queryRequest = PojoUtils.map(request, QueryApplyPageListRequest.class);
//		//如果传了企业id
//		if (ObjectUtil.isNotNull(request.getEnterpriseId())){
//			queryRequest.setEid(request.getEnterpriseId());
//		}else {
//			queryRequest.setEid(null);
//		}
//		//如果是商务查询---由于b2b_v1.0新增了数据权限此参数作废
//		if (ObjectUtil.equal(AgreementUserTypeEnum.BUSINESS.getCode(), request.getQueryType())) {
//			queryRequest.setCreateUser(request.getOpUserId());
//		}
		//查询数据权限
		List<Long> authorizedUserIds = dataPermissionsApi.listAuthorizedUserIds(PermissionAppEnum.MALL_ADMIN_POP, request.getEid(), request.getOpUserId());
		//设置数据权限
		queryRequest.setCreateUserList(authorizedUserIds);
		queryRequest.setEid(null);

		int mainCurrent = 1;
		Page<RebateApplyPageListItemDTO> mainPage;
		//分页查询申请单列表
		do {

			queryRequest.setCurrent(mainCurrent);
			queryRequest.setSize(50);
			mainPage = rebateApplyApi.queryRebateApplyPageList(queryRequest);
			if (CollUtil.isEmpty(mainPage.getRecords())) {
				break;
			}
			List<Long> applyIdList = mainPage.getRecords().stream().map(RebateApplyPageListItemDTO::getId).collect(Collectors.toList());

			//查询返利申请
			List<AgreementRebateApplyDTO> applyList = rebateApplyApi.queryRebateApplyList(applyIdList);
			if (CollUtil.isEmpty(applyList)) {
				return result;
			}
			//返利申请
			applyList.forEach(apply -> {
				ExportAgreementApplyBO applyDataExport = PojoUtils.map(apply, ExportAgreementApplyBO.class);
				//设置月度
				StringBuffer monthStr = new StringBuffer();
				if (!AgreementRebateApplyRangeTypeEnum.QUARTER.getCode().equals(apply.getRangeType())) {
					if (!apply.getMonth().equals(0)) {
						monthStr.append(apply.getMonth());
					}
					monthStr.append(AgreementRebateApplyRangeTypeEnum.getByCode(apply.getRangeType()).getName());
				}else {
					monthStr.append(str[apply.getMonth()]);
					monthStr.append(AgreementRebateApplyRangeTypeEnum.QUARTER.getName());
				}
				applyDataExport.setMonth(monthStr.toString());
				//补全操作人及工号
				UserDTO user = userApi.getById(apply.getCreateUser());
				applyDataExport.setUser(user.getName());
				Map<String, Object> applyDataExportPojo = BeanUtil.beanToMap(applyDataExport);
				applyData.add(applyDataExportPojo);



				//查询返利明细
				QueryRebateApplyDetailPageListRequest applyDetailRequest = new QueryRebateApplyDetailPageListRequest();
				applyDetailRequest.setApplyId(apply.getId());
				Page<AgreementApplyDetailDTO> applyDetailPage;
				int current = 1;
				do {

					applyDetailRequest.setCurrent(current);
					applyDetailRequest.setSize(50);
					//分页查询申请明细
					applyDetailPage = rebateApplyDetailApi.queryRebateApplyDetailPageList(applyDetailRequest);
					if (CollUtil.isEmpty(applyDetailPage.getRecords())) {
						break;
					}
					//补全数据
					//过滤掉非协议类型的明细
					List<Long> agreementIdList = applyDetailPage.getRecords().stream()
							.filter(e -> ApplyDetailTypeEnum.AGREEMENT.getCode().equals(e.getDetailType()))
							.map(AgreementApplyDetailDTO::getAgreementId).collect(Collectors.toList());
					//查询协议
					Map<Long, SupplementAgreementDetailDTO> agreementMap = agreementApi.querySupplementAgreementsDetailList(agreementIdList)
							.stream().collect(Collectors.toMap(SupplementAgreementDetailDTO::getId, e -> e));
					//组装数据
					applyDetailPage.getRecords().forEach(e -> {
						ExportAgreementApplyDetailBO applyDetail = PojoUtils.map(e, ExportAgreementApplyDetailBO.class);
						applyDetail.setCode(apply.getCode());
						applyDetail.setDetailId(e.getId());
						applyDetail.setEntryEid(apply.getEntryEid());
						applyDetail.setEntryName(apply.getEntryName());
						applyDetail.setProvinceName(apply.getProvinceName());
						if (ApplyDetailTypeEnum.AGREEMENT.getCode().equals(e.getDetailType())){
							SupplementAgreementDetailDTO agreementDTO = agreementMap.get(e.getAgreementId());
							applyDetail.setEntryDescribe(AgreementUtils.getAgreementText(agreementDTO));
						}
						applyDetail.setYear(apply.getYear());
						applyDetail.setDetailType(ApplyDetailTypeEnum.getByCode(e.getDetailType()).getName());
						applyDetail.setMonth(monthStr.toString());
						applyDetail.setGoodsName(apply.getGoodsName());
						applyDetail.setApplyTime(e.getApplyTime());
						Map<String, Object> dataPojo = BeanUtil.beanToMap(applyDetail);
						applyDetailData.add(dataPojo);
					});
					current = current + 1;
				} while (applyDetailPage != null && CollectionUtils.isNotEmpty(applyDetailPage.getRecords()));

				//查询返利订单
				//订单列表
				List<ApplyOrderDTO> applyOrderList = applyOrderApi.queryApplyOrderList(ListUtil.toList(apply.getId()));

				PageListByIdRequest applyOrderRequest = new PageListByIdRequest();
				List<Long> rebateOrderDetailId = ListUtil.toList();
				applyOrderList.forEach(e -> {
					long[] ids = StrUtil.splitToLong(e.getRebateOrderDetailId(), ",");
					rebateOrderDetailId.addAll(Arrays.stream(ids).boxed().collect(Collectors.toList()));
				});
				applyOrderRequest.setIdList(rebateOrderDetailId);

				//查询订单明细
				Page<AgreementRebateOrderDetailDTO> page;
				current = 1;
				do {

					applyOrderRequest.setCurrent(current);
					applyOrderRequest.setSize(50);
					page = rebateOrderDetailApi.pageListById(applyOrderRequest);
					if (CollUtil.isEmpty(page.getRecords())) {
						break;
					}
					//查询协议
					List<Long> agreementIdList = page.getRecords().stream().map(AgreementRebateOrderDetailDTO::getAgreementId).collect(Collectors.toList());
					Map<Long, AgreementDTO> agreementMap = agreementApi.getAgreementDetailsInfoByIds(agreementIdList)
							.stream().collect(Collectors.toMap(AgreementDTO::getId, e -> e));
					//组装数据
					page.getRecords().forEach(e -> {
						AgreementDTO agreementDTO = agreementMap.get(e.getAgreementId());
						ExportAgreementApplyOrderBO applyOrder = new ExportAgreementApplyOrderBO();
						applyOrder.setCode(apply.getCode());
						applyOrder.setAgreementId(agreementDTO.getId());
						applyOrder.setName(agreementDTO.getName());
						OrderDetailDTO orderDetail = orderDetailApi.getOrderDetailById(e.getOrderDetailId());
						applyOrder.setOrderCode(orderDetail.getOrderNo());
						applyOrder.setGoodsId(e.getGoodsId());
						applyOrder.setOrderType(RebateOrderTypeEnum.getByCode(e.getType()).getName());
						applyOrder.setGoodsName(orderDetail.getGoodsName());
						applyOrder.setErpCode(orderDetail.getGoodsErpCode());
						applyOrder.setGoodsQuantity(e.getGoodsQuantity());
						applyOrder.setPrice(e.getGoodsAmount());
						BigDecimal amount = e.getDiscountAmount().setScale(2,BigDecimal.ROUND_HALF_UP);
						//如果是退款单转为负数
						if (RebateOrderTypeEnum.REFUND.getCode().equals(e.getType())){
							applyOrder.setDiscountAmount(amount.negate());
						}else {
							applyOrder.setDiscountAmount(amount);
						}
						Map<String, Object> dataPojo = BeanUtil.beanToMap(applyOrder);
						applyOrderData.add(dataPojo);
					});
					current = current + 1;
				} while (page != null && CollectionUtils.isNotEmpty(page.getRecords()));
			});
			mainCurrent = mainCurrent + 1;
		} while (mainPage != null && CollectionUtils.isNotEmpty(mainPage.getRecords()));


		//返利申请单
		ExportDataDTO exportApplyDTO = new ExportDataDTO();
		exportApplyDTO.setSheetName("sheet1");
		// 页签字段
		exportApplyDTO.setFieldMap(FIELD_SHEET);
		// 页签数据
		exportApplyDTO.setData(applyData);


		//返利申请明细
		ExportDataDTO exportApplyDetailDTO = new ExportDataDTO();
		exportApplyDetailDTO.setSheetName("sheet2");
		// 页签字段
		exportApplyDetailDTO.setFieldMap(FIELD_SHEET2);
		// 页签数据
		exportApplyDetailDTO.setData(applyDetailData);


		//商品明细
		ExportDataDTO exportApplyOrderDTO = new ExportDataDTO();
		exportApplyOrderDTO.setSheetName("sheet3");
		// 页签字段
		exportApplyOrderDTO.setFieldMap(FIELD_SHEET3);
		// 页签数据
		exportApplyOrderDTO.setData(applyOrderData);


		//封装excel
		List<ExportDataDTO> sheets = new ArrayList<>();
		sheets.add(exportApplyDTO);
		sheets.add(exportApplyDetailDTO);
		sheets.add(exportApplyOrderDTO);
		result.setSheets(sheets);
		log.debug("返回数据：{}",JSON.toJSON(result));
		return result;
	}

	@Override
	public ExportAgreementBatchApplyRequest getParam(Map<String, Object> map) {
		return PojoUtils.map(map, ExportAgreementBatchApplyRequest.class);
	}


}
