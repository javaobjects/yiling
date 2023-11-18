package com.yiling.f2b.admin.goods.handler;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.yiling.f2b.admin.goods.form.ImportGoodsPriceForm;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.export.excel.handler.ImportDataHandler;
import com.yiling.pricing.goods.api.GoodsPriceCustomerApi;
import com.yiling.pricing.goods.api.GoodsPriceCustomerGroupApi;
import com.yiling.pricing.goods.dto.request.SaveOrUpdateGoodsPriceCustomerGroupRequest;
import com.yiling.pricing.goods.dto.request.SaveOrUpdateGoodsPriceCustomerRequest;
import com.yiling.pricing.goods.enums.GoodsPriceRuleEnum;

import cn.hutool.core.convert.Convert;
import lombok.extern.slf4j.Slf4j;

/**
 * 客户定价信息导入 处理类
 * @author: yuecheng.chen
 * @date: 2021/6/22
 */
@Component
@Slf4j
public class ImportGoodsPriceDataHandler implements ImportDataHandler<ImportGoodsPriceForm> {

    @DubboReference
    GoodsPriceCustomerApi goodsPriceCustomerApi;

    @DubboReference
    GoodsPriceCustomerGroupApi goodsPriceCustomerGroupApi;

    @Override
    public List<ImportGoodsPriceForm> execute(List<ImportGoodsPriceForm> object, Map<String,Object> paramMap) {
        for (ImportGoodsPriceForm form : object) {
            try {
                form.setEid((Long) paramMap.get("eid"));
                form = this.resolvePriceValue(form);
                if (form.getCustomerEid() != null) {
                    SaveOrUpdateGoodsPriceCustomerRequest saveOrUpdateGoodsPriceCustomer = PojoUtils.map(form, SaveOrUpdateGoodsPriceCustomerRequest.class);
                    goodsPriceCustomerApi.saveOrUpdate(saveOrUpdateGoodsPriceCustomer);
                }else if (form.getCustomerGroupId() != null) {
                    SaveOrUpdateGoodsPriceCustomerGroupRequest saveOrUpdateGoodsPriceCustomerGroup = PojoUtils.map(form, SaveOrUpdateGoodsPriceCustomerGroupRequest.class);
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
     * @param form
     * @return
     */
    private ImportGoodsPriceForm resolvePriceValue(ImportGoodsPriceForm form) {
        if (form.getSpecificPriceValue() != null) {
            form.setPriceRule(GoodsPriceRuleEnum.SPECIFIC_PRICE.getCode());
            form.setPriceValue(Convert.toBigDecimal(form.getSpecificPriceValue(), new BigDecimal(0)));
        }else if (form.getFloatPriceValue() != null) {
            form.setPriceRule(GoodsPriceRuleEnum.FLOAT_PRICE.getCode());
            form.setPriceValue(Convert.toBigDecimal(form.getFloatPriceValue(), new BigDecimal(0)));
        }
        return form;
    }
}
