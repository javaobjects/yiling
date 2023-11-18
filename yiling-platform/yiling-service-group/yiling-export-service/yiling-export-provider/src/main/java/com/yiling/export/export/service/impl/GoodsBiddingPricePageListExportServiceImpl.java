package com.yiling.export.export.service.impl;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.export.export.bo.ExportGoodsBiddingPriceBO;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.api.GoodsBiddingPriceApi;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.dto.GoodsBiddingPriceDTO;
import com.yiling.goods.medicine.dto.request.QueryGoodsPageListRequest;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: dexi.yao
 * @date: 2021/7/8
 */
@Service("goodsBiddingPricePageListExportService")
@Slf4j
public class GoodsBiddingPricePageListExportServiceImpl implements BaseExportQueryDataService<QueryGoodsPageListRequest> {

	@DubboReference
	GoodsApi             goodsApi;
	@DubboReference
	EnterpriseApi        enterpriseApi;
	@DubboReference
	GoodsBiddingPriceApi goodsBiddingPriceApi;

	private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<String, String>() {


		private static final long serialVersionUID = 3004511565690425578L;

		{
			put("goodsId", "商品ID");
			put("goodsName", "商品名称");
			put("licenseNo", "批准文号");
			put("manufacturer", "生产厂家");
			put("location_110000", "北京市");
			put("location_120000", "天津市");
			put("location_130000", "河北省");
			put("location_140000", "山西省");
			put("location_150000", "内蒙古自治区");
			put("location_210000", "辽宁省");
			put("location_220000", "吉林省");
			put("location_230000", "黑龙江省");
			put("location_310000", "上海市");
			put("location_320000", "江苏省");
			put("location_330000", "浙江省");
			put("location_340000", "安徽省");
			put("location_350000", "福建省");
			put("location_360000", "江西省");
			put("location_370000", "山东省");
			put("location_410000", "河南省");
			put("location_420000", "湖北省");
			put("location_430000", "湖南省");
			put("location_440000", "广东省");
			put("location_450000", "广西壮族自治区");
			put("location_460000", "海南省");
			put("location_500000", "重庆市");
			put("location_510000", "四川省");
			put("location_520000", "贵州省");
			put("location_530000", "云南省");
			put("location_540000", "西藏自治区");
			put("location_610000", "陕西省");
			put("location_620000", "甘肃省");
			put("location_630000", "青海省");
			put("location_640000", "宁夏回族自治区");
			put("location_650000", "新疆维吾尔自治区");
			put("location_710000", "台湾省");
			put("location_810000", "香港特别行政区");
			put("location_820000", "澳门特别行政区");

		}
	};

	@Override
	public QueryExportDataDTO queryData(QueryGoodsPageListRequest request) {
		//需要返回的对象
		QueryExportDataDTO result = new QueryExportDataDTO();

		//需要循环调用
		List<Map<String, Object>> data = new ArrayList<>();
		Page<GoodsListItemBO> page;
		int current = 1;
		do {
			//商品id列表
			List<Long> goodsIds;
			//设置过价格的省份和价格
			List<GoodsBiddingPriceDTO> priceList;
			//设置过价格的省份和价格map(key=goodsId)
			Map<Long, List<GoodsBiddingPriceDTO>> priceListMap;

			request.setCurrent(current);
			request.setSize(50);
			//查询分公司
			List<Long> eidList = enterpriseApi.listSubEids(Constants.YILING_EID);
			request.setEidList(eidList);
			request.setAuditStatus(GoodsStatusEnum.AUDIT_PASS.getCode());
			//根据条件搜索以岭商品
			page = goodsApi.queryPageListGoods(request);
			if (CollUtil.isEmpty(page.getRecords())) {
				break;
			}
			//根据商品查出各省份价格
			//根据商品id查询设置过价格的省份
			goodsIds = page.getRecords().stream().map(GoodsListItemBO::getId).collect(Collectors.toList());
			priceList = goodsBiddingPriceApi.queryGoodsBidingPriceList(goodsIds);
			priceListMap = priceList.stream().collect(Collectors.groupingBy(GoodsBiddingPriceDTO::getGoodsId));
			//组装数据
			page.getRecords().forEach(e -> {
				ExportGoodsBiddingPriceBO priceBo = PojoUtils.map(e, ExportGoodsBiddingPriceBO.class);
				priceBo.setGoodsId(e.getId());
				priceBo.setGoodsName(e.getName());
				List<GoodsBiddingPriceDTO> locationPriceList = priceListMap.get(priceBo.getGoodsId());
				//没有设置过价格的品不导出
				if (CollUtil.isNotEmpty(locationPriceList)) {
					for (GoodsBiddingPriceDTO obj : locationPriceList) {
						//省份编码
						String locationCode = obj.getLocationCode();
						//省份对应的价格
						BigDecimal price = obj.getPrice();
						//对应省份方法名
						String methodName = "setLocation_" + locationCode;
						try {
							Method method = priceBo.getClass().getMethod(methodName, BigDecimal.class);
							method.invoke(priceBo, price);
						} catch (Exception exception) {
							log.error(exception.getMessage(), exception);
						}
					}
				}
				Map<String, Object> dataPojo = BeanUtil.beanToMap(priceBo);
				data.add(dataPojo);
			});
			current = current + 1;
		} while (page != null && CollectionUtils.isNotEmpty(page.getRecords()));


		ExportDataDTO exportDataDTO = new ExportDataDTO();
		exportDataDTO.setSheetName("招标挂网价导出导出");
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
	public QueryGoodsPageListRequest getParam(Map<String, Object> map) {
		return PojoUtils.map(map, QueryGoodsPageListRequest.class);
	}

}
