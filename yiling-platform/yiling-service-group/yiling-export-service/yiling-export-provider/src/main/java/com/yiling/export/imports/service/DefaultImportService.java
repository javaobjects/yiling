package com.yiling.export.imports.service;

import java.io.InputStream;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yiling.export.excel.service.BaseExcelImportService;
import com.yiling.export.imports.util.ImportConstants;
import com.yiling.framework.common.util.SpringUtils;
import com.yiling.export.excel.model.ImportResultModel;
import com.yiling.export.excel.handler.ImportDataHandler;
import com.yiling.export.excel.util.ExeclImportUtils;

import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;

/**
 * 默认导入方式 Service
 *
 * @author: xuan.zhou
 * @date: 2022/12/2
 */
@Service("defaultImportService")
public class DefaultImportService implements BaseExcelImportService {

    @Override
    public ImportResultModel importExcel(InputStream inputstream, Integer readRows, Map<String, Object> param) throws Exception {
        ImportParams params = new ImportParams();
        params.setNeedVerify(true);
        String verifyHandlerBeanName = Convert.toStr(param.get(ImportConstants.FIELD_VERIFY_HANDLER_BEAN_NAME));
        if (StrUtil.isNotEmpty(verifyHandlerBeanName)) {
            params.setVerifyHandler((IExcelVerifyHandler) SpringUtils.getBean(verifyHandlerBeanName));
        }
        params.setKeyIndex(0);
        params.setReadRows(readRows);

        String modelClass = Convert.toStr(param.get(ImportConstants.FIELD_MODEL_CLASS));
        String importDataHandlerBeanName = Convert.toStr(param.get(ImportConstants.FIELD_IMPORT_DATA_HANDLER_BEAN_NAME));
        ImportDataHandler importDataHandler = (ImportDataHandler) SpringUtils.getBean(importDataHandlerBeanName);
        return ExeclImportUtils.importExcelMore(inputstream, Class.forName(modelClass), params, importDataHandler, param);
    }
}
