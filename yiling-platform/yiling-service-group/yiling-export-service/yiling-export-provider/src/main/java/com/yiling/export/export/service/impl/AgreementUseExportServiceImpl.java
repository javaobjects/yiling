package com.yiling.export.export.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.export.export.bo.ExportAgreementUseBO;
import com.yiling.export.export.bo.ExportAgreementUseDetailBO;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreement.api.UseApi;
import com.yiling.user.agreement.api.UseDetailApi;
import com.yiling.user.agreement.dto.UseDTO;
import com.yiling.user.agreement.dto.UseDetailDTO;
import com.yiling.user.agreement.dto.request.ExportUseListRequest;
import com.yiling.user.agreement.dto.request.QueryUseDetailListPageRequest;
import com.yiling.user.agreement.dto.request.QueryUseListPageRequest;
import com.yiling.user.agreement.enums.AgreementRestitutionTypeEnum;
import com.yiling.user.agreement.enums.AgreementUseStatusEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.system.api.DataPermissionsApi;
import com.yiling.user.system.enums.PermissionAppEnum;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 批量导出申请单明细
 *
 * @author: dexi.yao
 * @date: 2021/7/30
 */
@Service("agreementUseExportService")
@Slf4j
public class AgreementUseExportServiceImpl implements BaseExportQueryDataService<ExportUseListRequest> {

	@DubboReference
	UseApi       useApi;
	@DubboReference
	UseDetailApi useDetailApi;
	@DubboReference
	EnterpriseApi      enterpriseApi;
	@DubboReference
	DataPermissionsApi dataPermissionsApi;

	private static final LinkedHashMap<String, String> FIELD_SHEET  = new LinkedHashMap<String, String>() {

		{
			put("applicantCode", "申请单号");
			put("name", "企业名称");
			put("eid", "企业名称ID");
			put("provinceName", "所属省份");
			put("totalAmount", "使用返利金额");
			put("executeMeans", "申请类型");
			put("createTime", "申请时间");
			put("status", "审核状态");
			put("updateTime", "审核时间");
			put("sellerName", "发货组织");
			put("updateUserName", "申请人");
		}
	};
	private static final LinkedHashMap<String, String> FIELD_SHEET2 = new LinkedHashMap<String, String>() {

		{
			put("applicantCode", "申请单号");
			put("year", "归属年度");
			put("month", "归属月度");
			put("amount", "金额");
			put("rebateCategory", "返利种类");
			put("costSubject", "费用科目");
			put("costDept", "费用归属部门");
			put("executeDept", "执行部门");
			put("replyCode", "批复代码");
		}
	};

	@Override
	public QueryExportDataDTO queryData(ExportUseListRequest request) {

		//需要返回的对象
		QueryExportDataDTO result = new QueryExportDataDTO();

		//不同sheet数据
		List<Map<String, Object>> useData = new ArrayList<>();
		List<Map<String, Object>> useDetailData = new ArrayList<>();

		QueryUseListPageRequest queryUseRequest = PojoUtils.map(request, QueryUseListPageRequest.class);
		//查询数据权限
		List<Long> authorizedUserIds = dataPermissionsApi.listAuthorizedUserIds(PermissionAppEnum.MALL_ADMIN_POP,
				request.getEnterpriseId(), request.getCurrentUserId());

		queryUseRequest.setCreateUserIdList(authorizedUserIds);

//        // 商务---由于b2b_v1.0新增了数据权限此参数作废
//        if (!ObjectUtil.equal(AgreementUserTypeEnum.FINANCE.getCode(), request.getQueryType())) {
//            //查询当前用户负责的企业
//            Map<Long, List<EnterpriseDTO>> contactUserMap = enterpriseApi.listByContactUserIds(ErpConstants.YILING_EID,
//                ListUtil.toList(request.getCurrentUserId()));
//            queryUseRequest
//                .setEidList(contactUserMap.get(request.getCurrentUserId()).stream().map(EnterpriseDTO::getId).collect(Collectors.toList()));
//        }
        
		//如果传了企业id
		if (ObjectUtil.isNotNull(request.getEnterpriseId())){
			queryUseRequest.setEidList(ListUtil.toList(request.getEnterpriseId()));
		}else {
			queryUseRequest.setEidList(ListUtil.toList());
		}
		int mainCurrent = 1;
		Page<UseDTO> mainPage;
		//分页查询申请单列表
		do {

			queryUseRequest.setCurrent(mainCurrent);
			queryUseRequest.setSize(50);
			mainPage = useApi.queryUseListPageList(queryUseRequest);
			if (CollUtil.isEmpty(mainPage.getRecords())) {
				break;
			}
			mainPage.getRecords().forEach(useDTO -> {
				ExportAgreementUseBO useBO = PojoUtils.map(useDTO, ExportAgreementUseBO.class);
				useBO.setExecuteMeans(AgreementRestitutionTypeEnum.getByCode(useDTO.getExecuteMeans()).getName());
				useBO.setStatus(AgreementUseStatusEnum.getByCode(useDTO.getStatus()).getName());
				useBO.setCreateTime(DateUtil.format(useDTO.getCreateTime(),"yyyy-MM-dd"));
				useBO.setUpdateTime(DateUtil.format(useDTO.getUpdateTime(),"yyyy-MM-dd"));
				Map<String, Object> applyDataExportPojo = BeanUtil.beanToMap(useBO);
				useData.add(applyDataExportPojo);
			});

			List<Long> applyUseIdList = mainPage.getRecords().stream().map(UseDTO::getId).collect(Collectors.toList());
			QueryUseDetailListPageRequest detailListPageRequest = new QueryUseDetailListPageRequest();
			detailListPageRequest.setUseIdList(applyUseIdList);
			Page<UseDetailDTO> useDetailDTOPage;
			int detailCurrent = 1;
			do {
				detailListPageRequest.setCurrent(detailCurrent);
				detailListPageRequest.setSize(50);
				useDetailDTOPage = useDetailApi.queryUseDetailListPageList(detailListPageRequest);
				if (CollUtil.isEmpty(useDetailDTOPage.getRecords())) {
					break;
				}
				for (UseDetailDTO item : useDetailDTOPage.getRecords()) {
					ExportAgreementUseDetailBO useDetailBO = PojoUtils.map(item, ExportAgreementUseDetailBO.class);
					Map<String, Object> applyDataExportPojo = BeanUtil.beanToMap(useDetailBO);
					useDetailData.add(applyDataExportPojo);
				}

				detailCurrent = detailCurrent + 1;
			} while (useDetailDTOPage != null && CollectionUtils.isNotEmpty(useDetailDTOPage.getRecords()));

			mainCurrent = mainCurrent + 1;
		} while (mainPage != null && CollectionUtils.isNotEmpty(mainPage.getRecords()));


		//返利申请单
		ExportDataDTO exportApplyDTO = new ExportDataDTO();
		exportApplyDTO.setSheetName("sheet1");
		// 页签字段
		exportApplyDTO.setFieldMap(FIELD_SHEET);
		// 页签数据
		exportApplyDTO.setData(useData);


		//返利申请明细
		ExportDataDTO exportApplyDetailDTO = new ExportDataDTO();
		exportApplyDetailDTO.setSheetName("sheet2");
		// 页签字段
		exportApplyDetailDTO.setFieldMap(FIELD_SHEET2);
		// 页签数据
		exportApplyDetailDTO.setData(useDetailData);


		//封装excel
		List<ExportDataDTO> sheets = new ArrayList<>();
		sheets.add(exportApplyDTO);
		sheets.add(exportApplyDetailDTO);
		result.setSheets(sheets);

		return result;
	}

	@Override
	public ExportUseListRequest getParam(Map<String, Object> map) {
		return PojoUtils.map(map, ExportUseListRequest.class);
	}


}
