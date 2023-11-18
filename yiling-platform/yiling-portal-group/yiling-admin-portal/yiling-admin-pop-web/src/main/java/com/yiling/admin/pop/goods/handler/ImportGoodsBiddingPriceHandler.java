package com.yiling.admin.pop.goods.handler;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.yiling.export.excel.handler.ImportDataHandler;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.yiling.admin.pop.goods.form.ImportGoodsBiddingPriceForm;
import com.yiling.basic.location.api.LocationApi;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.api.GoodsBiddingPriceApi;
import com.yiling.goods.medicine.api.PopGoodsApi;
import com.yiling.goods.medicine.dto.GoodsInfoDTO;
import com.yiling.goods.medicine.dto.request.GoodsBiddingPriceRequest;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 导入招标挂网价处理器
 *
 * @author: dexi.yao
 * @date: 2021/07/08
 */
@Slf4j
@Component
public class ImportGoodsBiddingPriceHandler implements IExcelVerifyHandler<ImportGoodsBiddingPriceForm>, ImportDataHandler<ImportGoodsBiddingPriceForm> {

	@DubboReference
	LocationApi          locationApi;
	@DubboReference
	GoodsApi             goodsApi;
    @DubboReference
    PopGoodsApi          popGoodsApi;
	@DubboReference
	GoodsBiddingPriceApi goodsBiddingPriceApi;

	@Override
	public ExcelVerifyHandlerResult verifyHandler(ImportGoodsBiddingPriceForm form) {
		ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);

		//校验商品是否存在
		{
			Long goodsId = form.getGoodsId();
			if (goodsId != null && goodsId != 0L) {
				List<GoodsInfoDTO> goodsInfoList = popGoodsApi.batchQueryInfo(ListUtil.toList(goodsId));
				if (CollUtil.isEmpty(goodsInfoList)) {
					result.setSuccess(false);
					result.setMsg("商品ID错误");
					return result;
				}
			}
		}

		return result;
	}

	@Override
	public List<ImportGoodsBiddingPriceForm> execute(List<ImportGoodsBiddingPriceForm> object, Map<String,Object> paramMap) {
		for (ImportGoodsBiddingPriceForm form : object) {
			try {
				List<GoodsBiddingPriceRequest> requestList = resolverObj(form, (Long) paramMap.getOrDefault(MyMetaHandler.FIELD_OP_USER_ID,0));
				if (CollUtil.isNotEmpty(requestList)){
					boolean result = goodsBiddingPriceApi.batchSaveGoodsBiddingPrice(requestList);
					if (!result) {
						form.setErrorMsg(ResultCode.EXCEL_DATA_SAVING_FAILED.getMessage());
					}
				}

			} catch (BusinessException be) {
				form.setErrorMsg(be.getMessage());
			} catch (Exception e) {
				form.setErrorMsg(ResultCode.EXCEL_DATA_SAVING_FAILED.getMessage());
				log.error("数据保存出错：{}", e.getMessage(), e);
			}
		}
		return object;
	}

	/**
	 * 将form解析成GoodsBiddingPriceRequest类型
	 *
	 * @param form
	 * @return
	 */
	private List<GoodsBiddingPriceRequest> resolverObj(ImportGoodsBiddingPriceForm form, Long opUserId) {
		List<GoodsBiddingPriceRequest> result = ListUtil.toList();
		Field[] fields = form.getClass().getDeclaredFields();

		for (Field field : fields) {
			String name = field.getName();
			if (name.contains("location_")) {
				//构造方法名
				name = name.substring(0, 1).toUpperCase() + name.substring(1);
				try {
					GoodsBiddingPriceRequest request = new GoodsBiddingPriceRequest();
					Method m = form.getClass().getMethod("get" + name);
					//价格
					BigDecimal locationPrice = (BigDecimal) m.invoke(form);
					//忽略价格为0的数据
//					if (locationPrice.compareTo(BigDecimal.ZERO)==0){
//						continue;
//					}
					if (locationPrice==null){
						continue;
					}
					//地区编码
					String locationCode = name.split("_")[1];
					//地区名称
					String locationName = field.getAnnotation(Excel.class).name();
					request.setPrice(locationPrice);
					request.setLocationCode(locationCode);
					request.setLocationName(locationName);
					request.setGoodsId(form.getGoodsId());
					request.setOpUserId(opUserId);

					result.add(request);

				} catch (Exception e) {
					log.error(e.getMessage(), e);
					throw new BusinessException();
				}
			}
		}

		return result;
	}
}
