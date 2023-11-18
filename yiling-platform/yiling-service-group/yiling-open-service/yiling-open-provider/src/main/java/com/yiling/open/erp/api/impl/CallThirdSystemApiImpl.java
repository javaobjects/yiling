package com.yiling.open.erp.api.impl;

import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.open.erp.api.CallThirdSystemApi;
import com.yiling.open.erp.bo.QueryRedFlushBO;
import com.yiling.open.erp.dto.AgreementApplyGoodsDTO;
import com.yiling.open.erp.dto.AgreementApplyLocationDTO;
import com.yiling.open.erp.dto.ValidAgreementApplyImportDTO;
import com.yiling.open.erp.dto.request.ValidExcelRequest;
import com.yiling.open.erp.enums.OpenErrorCode;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpRequest;

/**
 * @author dexi.yao
 * @date 2021-08-09
 */
@DubboService
public class CallThirdSystemApiImpl implements CallThirdSystemApi {

	@Value("${erp.redFlush.validExcel}")
	private String validExcel;

	@Value("${erp.redFlush.queryGoods}")
	private String queryGoods;

	@Value("${erp.redFlush.queryLocation}")
	private String queryLocation;

	@Override
	public ValidAgreementApplyImportDTO validAgreementApplyImport(List<ValidExcelRequest> request) {
		Map<Object, Object> map = MapUtil.newHashMap();
		map.put("list", request);
		Map<String, Object> parMap = BeanUtil.beanToMap(map);
		String result = HttpRequest.post(validExcel)
				.header("Content-Type", "application/json")
				.body(JSON.toJSONString(map))
				.execute().body();
		return JSON.parseObject(result, ValidAgreementApplyImportDTO.class);
	}

	@Override
	public List<AgreementApplyGoodsDTO> queryAgreementApplyGoods() {

		HttpRequest request = HttpRequest.get(queryGoods);
		String body = request.execute().body();
		JSONObject jsonObject = JSONObject.parseObject(body);
		String code = jsonObject.getString("code");
		if (!"200".equals(code)){
			new BusinessException(OpenErrorCode.QUERY_AGREEMENT_APPLY_GOODS);
		}
		List<AgreementApplyGoodsDTO> result=ListUtil.toList();
		List<QueryRedFlushBO> applyGoodsBOS = jsonObject.getJSONObject("result").getJSONArray("ficoRebateParameter").toJavaList(QueryRedFlushBO.class);

		applyGoodsBOS.forEach(o -> {
			AgreementApplyGoodsDTO   goods=new AgreementApplyGoodsDTO();
			goods.setName(o.getKeyValue());
			result.add(goods);
		});
		return result;
	}

	@Override
	public List<AgreementApplyLocationDTO> queryAgreementApplyLocation() {
		HttpRequest request = HttpRequest.get(queryLocation);
		String body = request.execute().body();
		JSONObject jsonObject = JSONObject.parseObject(body);
		String code = jsonObject.getString("code");
		if (!"200".equals(code)){
			new BusinessException(OpenErrorCode.QUERY_AGREEMENT_APPLY_GOODS);
		}
		List<AgreementApplyLocationDTO> result=ListUtil.toList();
		List<QueryRedFlushBO> applyGoodsBOS = jsonObject.getJSONObject("result").getJSONArray("ficoRebateParameter").toJavaList(QueryRedFlushBO.class);

		applyGoodsBOS.forEach(o -> {
			AgreementApplyLocationDTO   location=new AgreementApplyLocationDTO();
			location.setName(o.getKeyValue());
			result.add(location);
		});
		return result;
	}

}
