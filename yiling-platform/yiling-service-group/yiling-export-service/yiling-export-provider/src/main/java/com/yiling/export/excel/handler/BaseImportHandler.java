package com.yiling.export.excel.handler;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;

/**
 * 导入处理器基类（已过时，由 AbstractExcelImportHandler 类代替）
 *
 * @author: xuan.zhou
 * @date: 2022/12/1
 */
@Deprecated
public abstract class BaseImportHandler<T> implements IExcelVerifyHandler<T>, ImportDataHandler<T> {

    protected ExcelVerifyHandlerResult error(String errorMessage) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(false);
        result.setMsg(errorMessage);
        return result;
    }
}
