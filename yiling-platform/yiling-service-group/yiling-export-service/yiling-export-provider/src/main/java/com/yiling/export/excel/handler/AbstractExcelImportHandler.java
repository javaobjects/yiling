package com.yiling.export.excel.handler;

import java.util.List;
import java.util.Map;

import com.yiling.export.excel.model.BaseImportModel;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 导入处理器基类
 *
 * @author: xuan.zhou
 * @date: 2022/12/1
 */
@Slf4j
public abstract class AbstractExcelImportHandler<T extends BaseImportModel> implements IExcelVerifyHandler<T>, ImportDataHandler<T> {

    protected ExcelVerifyHandlerResult error(String errorMessage) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(false);
        result.setMsg(errorMessage);
        return result;
    }

    private ExcelVerifyHandlerResult fastError() {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(false);
        result.setMsg("");
        return result;
    }

    @Override
    public ExcelVerifyHandlerResult verifyHandler(T model) {
        // 如果验证引擎已校验出问题，则不做后续的业务逻辑校验
        String errorMsg = model.getErrorMsg();
        if (StrUtil.isNotEmpty(errorMsg)) {
            return this.fastError();
        }

        try {
            return this.verify(model);
        } catch (Exception e) {
            log.error("数据逻辑校验出错 -> {}", e.getMessage(), e);
            return this.error("数据逻辑校验出错：" + e.getMessage());
        }
    }

    @Override
    public List<T> execute(List<T> object, Map<String, Object> paramMap) {
        return this.importData(object, paramMap);
    }

    /**
     * 验证方法（单行数据）
     *
     * @param model
     * @return cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult
     * @author xuan.zhou
     * @date 2023/4/3
     **/
    protected abstract ExcelVerifyHandlerResult verify(T model);

    /**
     * 导入数据
     *
     * @param object
     * @param paramMap
     * @return java.util.List<T>
     * @author xuan.zhou
     * @date 2023/4/3
     **/
    protected abstract List<T> importData(List<T> object, Map<String, Object> paramMap);
}
