package com.yiling.export.imports.service;

import java.io.InputStream;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yiling.export.excel.service.BaseExcelImportService;
import com.yiling.export.imports.handler.ImportUpdateManagerAccountHandler;
import com.yiling.export.imports.model.ImportUpdateManagerAccountModel;
import com.yiling.export.excel.model.ImportResultModel;
import com.yiling.export.excel.util.ExeclImportUtils;

import cn.afterturn.easypoi.excel.entity.ImportParams;

/**
 * 导入更改企业管理员账号数据
 *
 * @author: xuan.zhou
 * @date: 2022/12/1
 */
@Service
public class ImportUpdateManagerAccountService implements BaseExcelImportService {

    @Autowired
    ImportUpdateManagerAccountHandler importUpdateManagerAccountHandler;

    @Override
    public ImportResultModel importExcel(InputStream inputstream, Integer readRows, Map<String, Object> param) throws Exception {
        ImportParams params = new ImportParams();
        params.setNeedVerify(true);
        params.setVerifyHandler(importUpdateManagerAccountHandler);
        params.setKeyIndex(0);
        params.setReadRows(readRows);

        return ExeclImportUtils.importExcelMore(inputstream, ImportUpdateManagerAccountModel.class, params, importUpdateManagerAccountHandler, param);
    }
}
