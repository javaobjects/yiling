package com.yiling.export.imports.handler;

import java.math.BigDecimal;
import java.util.Map;

import com.yiling.dataflow.flowcollect.api.FlowMonthUploadRecordApi;
import com.yiling.export.excel.handler.EasyExcelVerifyHandlerResult;
import com.yiling.export.excel.listener.AbstractImportReaderListener;
import com.yiling.export.excel.service.ExcelTaskRecordService;
import com.yiling.export.imports.model.ImportFlowMonthSalesModel;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 补传月流向销售数据验证处理器
 *
 * @author: lun.yu
 * @date: 2023-03-04
 */
@Slf4j
@Data
public class ImportFixFlowMonthSalesHandler extends AbstractImportReaderListener<ImportFlowMonthSalesModel> {




    @Override
    public EasyExcelVerifyHandlerResult verify(ImportFlowMonthSalesModel model) {
        {
            // 销售日期格式校验
            String soTimeStr = model.getSoTimeStr();
            if (StrUtil.isNotEmpty(soTimeStr)) {
                try {
                    DateTime dateTime = DateUtil.parse(soTimeStr, "yyyy-MM-dd",
                            "yyyyMMdd","yyyy/MM/dd","yyyy-M-dd","yyyyMdd","yyyy/M/dd");
                } catch (Exception e) {
                    return this.error("销售日期格式不正确", "soTimeStr", soTimeStr);
                }

            }
            // 数量
            String soQuantityStr = model.getSoQuantityStr();
            if (StrUtil.isNotEmpty(soQuantityStr)) {
                try {
                    BigDecimal soQuantity = new BigDecimal(soQuantityStr);
                } catch (Exception e) {
                    return this.error("数量（盒）格式不正确", "soQuantityStr", soQuantityStr);
                }
            }
            // 单价
            String soPrice = model.getSoPrice();
            if (StrUtil.isNotEmpty(soPrice)) {
                try {
                    BigDecimal price = new BigDecimal(soPrice);
                } catch (Exception e) {
                    return this.error("单价格式不正确", "soPrice", soPrice);
                }
            }
            // 金额
            String soTotalAmount = model.getSoTotalAmount();
            if (StrUtil.isNotEmpty(soTotalAmount)) {
                try {
                    BigDecimal totalAmount = new BigDecimal(soTotalAmount);
                } catch (Exception e) {
                    return this.error("金额格式不正确", "soTotalAmount", soTotalAmount);
                }
            }
        }
        return null;
    }


    @Override
    public void saveData(Map<String, Object> paramMap) {

    }

    @Override
    public boolean isAfterSave() {
        return true;
    }
}
