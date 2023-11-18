package com.yiling.export.imports.handler;

import java.math.BigDecimal;
import java.util.Map;

import com.yiling.export.excel.handler.EasyExcelVerifyHandlerResult;
import com.yiling.export.excel.listener.AbstractImportReaderListener;
import com.yiling.export.imports.model.ImportFlowMonthInventoryModel;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 补传月流向库存数据验证处理器
 *
 * @author: gxl
 * @date: 2023-06-29
 */
@Slf4j
public class ImportFixFlowMonthInventoryHandler extends AbstractImportReaderListener<ImportFlowMonthInventoryModel> {

    @Override
    public void saveData(Map<String, Object> paramMap) {

    }

    @Override
    public EasyExcelVerifyHandlerResult verify(ImportFlowMonthInventoryModel model) {
        {
            // 库存提取日期格式校验
            String soTimeStr = model.getGbExtractTimeStr();
            if (StrUtil.isNotEmpty(soTimeStr)) {
                try {
                    DateTime dateTime = DateUtil.parse(soTimeStr, "yyyy-MM-dd","yyyy/MM/dd");
                } catch (Exception e) {
                    return this.error("库存提取日期格式不正确","GbExtractTimeStr",soTimeStr);
                }

            }

            // 数量
            String gbNumberStr = model.getGbNumberStr();
            if (StrUtil.isNotEmpty(gbNumberStr)) {
                try {
                    BigDecimal gbNumber = new BigDecimal(gbNumberStr);
                } catch (Exception e) {
                    return this.error("数量（盒）格式不正确","GbNumberStr",gbNumberStr);
                }
            }

            // 入库日期格式校验
            String gbTimeStr = model.getGbTimeStr();
            if (StrUtil.isNotEmpty(gbTimeStr)) {
                try {
                    DateTime dateTime = DateUtil.parse(gbTimeStr, "yyyy-MM-dd");
                } catch (Exception e) {
                    return this.error("入库日期格式不正确","GbTimeStr",gbTimeStr);
                }

            }

            // 生产日期格式校验
            String gbProduceTime = model.getGbProduceTime();
            if (StrUtil.isNotEmpty(gbProduceTime)) {
                try {
                    DateTime dateTime = DateUtil.parse(gbProduceTime, "yyyy-MM-dd");
                } catch (Exception e) {
                    return this.error("生产日期格式不正确","GbProduceTime",gbProduceTime);
                }

            }

            // 有效期格式校验
            String gbEndTime = model.getGbEndTime();
            if (StrUtil.isNotEmpty(gbEndTime)) {
                try {
                    DateTime dateTime = DateUtil.parse(gbEndTime, "yyyy-MM-dd");
                } catch (Exception e) {
                    return this.error("有效期格式不正确","GbEndTime",gbEndTime);
                }

            }

            // 单价
            String gbPrice = model.getGbPrice();
            if (StrUtil.isNotEmpty(gbPrice)) {
                try {
                    BigDecimal price = new BigDecimal(gbPrice);
                } catch (Exception e) {
                    return this.error("单价格式不正确","GbPrice",gbPrice);
                }
            }
            // 金额
            String gbTotalAmount = model.getGbTotalAmount();
            if (StrUtil.isNotEmpty(gbTotalAmount)) {
                try {
                    BigDecimal totalAmount = new BigDecimal(gbTotalAmount);
                } catch (Exception e) {
                    return this.error("金额格式不正确","GbTotalAmount",gbTotalAmount);
                }
            }
        }
        return null;
    }

}
