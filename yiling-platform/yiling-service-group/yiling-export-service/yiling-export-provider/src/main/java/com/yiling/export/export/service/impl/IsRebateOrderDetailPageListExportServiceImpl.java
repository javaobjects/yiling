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
import com.yiling.export.export.bo.ExportAgreementRebateOrderBO;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.api.GoodsBiddingPriceApi;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.enums.OrderStatusEnum;
import com.yiling.order.order.enums.PaymentMethodEnum;
import com.yiling.order.order.enums.PaymentStatusEnum;
import com.yiling.user.agreement.api.AgreementRebateOrderDetailApi;
import com.yiling.user.agreement.dto.AgreementRebateOrderDetailDTO;
import com.yiling.user.agreement.dto.request.ExportRebateOrderPageListRequest;
import com.yiling.user.agreement.dto.request.QueryRebateOrderPageListRequest;
import com.yiling.user.enterprise.api.EnterpriseApi;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 导出以是否满足返利订单明细
 * @author: dexi.yao
 * @date: 2021/7/15
 */
@Service("isRebateOrderDetailPageListExportService")
@Slf4j
public class IsRebateOrderDetailPageListExportServiceImpl implements BaseExportQueryDataService<ExportRebateOrderPageListRequest> {

	@DubboReference
	GoodsApi                      goodsApi;
	@DubboReference
	EnterpriseApi                 enterpriseApi;
	@DubboReference
	GoodsBiddingPriceApi          goodsBiddingPriceApi;
	@DubboReference
	AgreementRebateOrderDetailApi agreementRebateOrderDetailApi;
	@DubboReference
	OrderApi                      orderApi;

	private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<String, String>() {


		private static final long serialVersionUID = 3004511565690425578L;

		{
			put("orderNo", "订单号");
			put("orderStatusName", "订单状态");
			put("paymentMethodName", "支付方式");
			put("paymentStatusName", "支付状态");
			put("goodsName", "商品名称");
			put("goodsLicenseNo", "商品批准文号");
			put("goodsSpecification", "商品规格");
			put("goodsAmount", "商品购买总额");
			put("goodsQuantity", "购买数量");
			put("discountAmount", "协议返还金额");
			put("policyValue", "返利比例");

		}
	};

	@Override
	public QueryExportDataDTO queryData(ExportRebateOrderPageListRequest request) {
		//需要返回的对象
		QueryExportDataDTO result = new QueryExportDataDTO();

		QueryRebateOrderPageListRequest apiRequest = PojoUtils.map(request, QueryRebateOrderPageListRequest.class);

		//需要循环调用
		List<Map<String, Object>> data = new ArrayList<>();
		Page<AgreementRebateOrderDetailDTO> page;
		int current = 1;
		do {

			apiRequest.setCurrent(current);
			apiRequest.setSize(50);
			//分页查询数据
			page = agreementRebateOrderDetailApi.queryRebateOrderPageList(apiRequest, request.getConditionStatusEnum(), null);
			if (CollUtil.isEmpty(page.getRecords())) {
				break;
			}

			List<Long> orderIdList = page.getRecords().stream().map(AgreementRebateOrderDetailDTO::getOrderId)
					.distinct().collect(Collectors.toList());
			Map<Long, OrderDTO> orderMap = orderApi.listByIds(orderIdList).stream().collect(Collectors.toMap(OrderDTO::getId, e -> e));
			//商品id列表
			List<Long> goodsIdList = page.getRecords().stream().map(AgreementRebateOrderDetailDTO::getGoodsId)
					.distinct().collect(Collectors.toList());
			//查询商品信息
			Map<Long, GoodsDTO> goodsInfoDTOMap = goodsApi.batchQueryInfo(goodsIdList).stream().collect(Collectors.toMap(GoodsDTO::getId, e -> e));
			//组装数据
			page.getRecords().forEach(e -> {
				OrderDTO orderDTO = orderMap.get(e.getOrderId());
				ExportAgreementRebateOrderBO exportBO = PojoUtils.map(e, ExportAgreementRebateOrderBO.class);
				PojoUtils.map(orderDTO, exportBO);
				//设置订单状态
				exportBO.setOrderStatusName(OrderStatusEnum.getByCode(exportBO.getOrderStatus()).getName());
				exportBO.setPaymentMethodName(PaymentMethodEnum.getByCode(Long.valueOf(exportBO.getPaymentMethod())).getName());
				exportBO.setPaymentStatusName(PaymentStatusEnum.getByCode(exportBO.getPaymentStatus()).getName());
				//补全商品信息
				GoodsDTO goodsInfoDTO = goodsInfoDTOMap.get(exportBO.getGoodsId());
				exportBO.setGoodsName(goodsInfoDTO.getName());
				exportBO.setGoodsLicenseNo(goodsInfoDTO.getLicenseNo());
				exportBO.setGoodsSpecification(goodsInfoDTO.getSellSpecifications());
				Map<String, Object> dataPojo = BeanUtil.beanToMap(exportBO);
				data.add(dataPojo);
			});
			current = current + 1;
		} while (page != null && CollectionUtils.isNotEmpty(page.getRecords()));


		ExportDataDTO exportDataDTO = new ExportDataDTO();
		exportDataDTO.setSheetName("协议"+request.getConditionStatusEnum().getName()+"订单明细");
		// 页签字段
		exportDataDTO.setFieldMap(FIELD);
		// 页签数据
		exportDataDTO.setData(data);

		List<ExportDataDTO> sheets = new ArrayList<>();
		sheets.add(exportDataDTO);
		result.setSheets(sheets);
		return result;
	}

	@Override
	public ExportRebateOrderPageListRequest getParam(Map<String, Object> map) {
		return PojoUtils.map(map, ExportRebateOrderPageListRequest.class);
	}

}
