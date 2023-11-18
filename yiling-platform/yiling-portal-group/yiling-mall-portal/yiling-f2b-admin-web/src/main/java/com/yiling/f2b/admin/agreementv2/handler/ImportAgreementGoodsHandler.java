package com.yiling.f2b.admin.agreementv2.handler;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.yiling.f2b.admin.agreementv2.form.ImportAgreementGoodsForm;
import com.yiling.export.excel.handler.ImportDataHandler;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 导入协议商品数据验证处理器
 *
 * @author: lun.yu
 * @date: 2022-03-16
 */
@Slf4j
@Component
public class ImportAgreementGoodsHandler implements IExcelVerifyHandler<ImportAgreementGoodsForm>, ImportDataHandler<ImportAgreementGoodsForm> {

    @Override
    public ExcelVerifyHandlerResult verifyHandler(ImportAgreementGoodsForm form) {
        return new ExcelVerifyHandlerResult(true);
    }

    @Override
    public List<ImportAgreementGoodsForm> execute(List<ImportAgreementGoodsForm> object, Map<String,Object> paramMap) {
        return object;
    }
}
