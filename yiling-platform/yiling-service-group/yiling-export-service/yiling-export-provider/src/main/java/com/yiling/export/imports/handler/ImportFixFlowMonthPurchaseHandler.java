package com.yiling.export.imports.handler;

import java.math.BigDecimal;
import java.util.Map;

import com.yiling.export.excel.handler.EasyExcelVerifyHandlerResult;
import com.yiling.export.excel.listener.AbstractImportReaderListener;
import com.yiling.export.imports.model.ImportFlowMonthPurchaseModel;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 补传月流向采购数据验证处理器
 *
 * @author: gxl
 * @date: 2023-06-29
 */
@Slf4j
public class ImportFixFlowMonthPurchaseHandler extends AbstractImportReaderListener<ImportFlowMonthPurchaseModel> {



    @Override
    public void saveData(Map<String, Object> paramMap) {
    }

    @Override
    public EasyExcelVerifyHandlerResult verify(ImportFlowMonthPurchaseModel model) {
        {
            // 购进日期格式校验
            String poTimeStr = model.getPoTimeStr();
            if (StrUtil.isNotEmpty(poTimeStr)) {
                try {
                    DateTime dateTime = DateUtil.parse(poTimeStr, "yyyy-MM-dd","yyyy/MM/dd");
                } catch (Exception e) {
                    return this.error("购进日期格式不正确","PoTimeStr",poTimeStr);
                }

            }
            // 数量
            String poQuantityStr = model.getPoQuantityStr();
            if (StrUtil.isNotEmpty(poQuantityStr)) {
                try {
                    BigDecimal poQuantity = new BigDecimal(poQuantityStr);
                } catch (Exception e) {
                    return this.error("数量（盒）格式不正确","PoQuantityStr",poQuantityStr);
                }
            }
            // 单价
            String poPrice = model.getPoPrice();
            if (StrUtil.isNotEmpty(poPrice)) {
                try {
                    BigDecimal price = new BigDecimal(poPrice);
                } catch (Exception e) {
                    return this.error("单价格式不正确","PoPrice",poPrice);
                }
            }
            // 金额
            String poTotalAmount = model.getPoTotalAmount();
            if (StrUtil.isNotEmpty(poTotalAmount)) {
                try {
                    BigDecimal totalAmount = new BigDecimal(poTotalAmount);
                } catch (Exception e) {
                    return this.error("金额格式不正确","PoTotalAmount",poTotalAmount);
                }
            }

        }
        return null;
    }

}
