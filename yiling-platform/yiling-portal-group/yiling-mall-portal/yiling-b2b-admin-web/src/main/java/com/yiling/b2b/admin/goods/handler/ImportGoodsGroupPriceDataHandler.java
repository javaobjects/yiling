package com.yiling.b2b.admin.goods.handler;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.yiling.export.excel.handler.ImportDataHandler;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.yiling.b2b.admin.goods.form.ImportGoodsGroupPriceForm;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.enums.GoodsLineEnum;
import com.yiling.pricing.goods.api.GoodsPriceCustomerApi;
import com.yiling.pricing.goods.api.GoodsPriceCustomerGroupApi;
import com.yiling.pricing.goods.dto.request.SaveOrUpdateGoodsPriceCustomerGroupRequest;
import com.yiling.pricing.goods.enums.GoodsPriceRuleEnum;

import cn.hutool.core.convert.Convert;
import lombok.extern.slf4j.Slf4j;

/**
 * 客户定价信息导入 处理类
 *
 * @author: yuecheng.chen
 * @date: 2021/6/22
 */
@Component
@Slf4j
public class ImportGoodsGroupPriceDataHandler implements ImportDataHandler<ImportGoodsGroupPriceForm> {

    @DubboReference
    GoodsPriceCustomerApi goodsPriceCustomerApi;

    @DubboReference
    GoodsPriceCustomerGroupApi goodsPriceCustomerGroupApi;

    @Override
    public List<ImportGoodsGroupPriceForm> execute(List<ImportGoodsGroupPriceForm> object, Map<String, Object> paramMap) {
        for (ImportGoodsGroupPriceForm form : object) {
            try {
                form.setEid((Long) paramMap.get("eid"));
                form = this.resolvePriceValue(form);
                if (form.getCustomerGroupId() != null) {
                    SaveOrUpdateGoodsPriceCustomerGroupRequest saveOrUpdateGoodsPriceCustomerGroup = PojoUtils.map(form, SaveOrUpdateGoodsPriceCustomerGroupRequest.class);
                    saveOrUpdateGoodsPriceCustomerGroup.setGoodsLine(GoodsLineEnum.B2B.getCode());
                    goodsPriceCustomerGroupApi.saveOrUpdate(saveOrUpdateGoodsPriceCustomerGroup);
                }
            } catch (BusinessException e) {
                form.setErrorMsg(e.getMessage());
                log.error("商品定价导入数据库报错", e);
            } catch (Exception e) {
                form.setErrorMsg(ResultCode.EXCEL_DATA_SAVING_FAILED.getMessage());
                log.error("数据保存出错：{}", e.getMessage(), e);
            }
        }
        return object;
    }

    /**
     * 解析商品定价规则，商品价格
     *
     * @param form
     * @return
     */
    private ImportGoodsGroupPriceForm resolvePriceValue(ImportGoodsGroupPriceForm form) {
        if (form.getSpecificPriceValue() != null) {
            form.setPriceRule(GoodsPriceRuleEnum.SPECIFIC_PRICE.getCode());
            form.setPriceValue(Convert.toBigDecimal(form.getSpecificPriceValue(), new BigDecimal(0)));
        } else if (form.getFloatPriceValue() != null) {
            form.setPriceRule(GoodsPriceRuleEnum.FLOAT_PRICE.getCode());
            form.setPriceValue(Convert.toBigDecimal(form.getFloatPriceValue(), new BigDecimal(0)));
        }
        return form;
    }
}
