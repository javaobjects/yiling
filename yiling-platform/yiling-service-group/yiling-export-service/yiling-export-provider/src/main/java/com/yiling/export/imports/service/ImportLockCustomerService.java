package com.yiling.export.imports.service;

import java.io.InputStream;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yiling.export.excel.model.ImportResultModel;
import com.yiling.export.excel.service.BaseExcelImportService;
import com.yiling.export.excel.util.ExeclImportUtils;
import com.yiling.export.imports.handler.ImportLockCustomerHandler;
import com.yiling.export.imports.model.ImportLockCustomerModel;

import cn.afterturn.easypoi.excel.entity.ImportParams;

/**
 * 导入锁定客户
 *
 * @author: lun.yu
 * @date: 2022-12-12
 */
@Service
public class ImportLockCustomerService implements BaseExcelImportService {

    @Autowired
    ImportLockCustomerHandler importLockCustomerHandler;

    @Override
    public ImportResultModel importExcel(InputStream inputstream, Integer readRows, Map<String, Object> param) throws Exception {
        ImportParams params = new ImportParams();
        params.setNeedVerify(true);
        params.setVerifyHandler(importLockCustomerHandler);
        params.setKeyIndex(0);
        params.setReadRows(readRows);

        return ExeclImportUtils.importExcelMore(inputstream, ImportLockCustomerModel.class, params, importLockCustomerHandler, param);
    }
}
