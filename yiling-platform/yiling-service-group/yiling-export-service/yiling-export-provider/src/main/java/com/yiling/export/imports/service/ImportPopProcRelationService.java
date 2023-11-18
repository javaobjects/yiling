package com.yiling.export.imports.service;

import java.io.InputStream;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yiling.export.excel.model.ImportResultModel;
import com.yiling.export.excel.service.BaseExcelImportService;
import com.yiling.export.excel.util.ExeclImportUtils;
import com.yiling.export.imports.handler.ImportPopProcRelationHandler;
import com.yiling.export.imports.model.ImportPopProcRelationModel;

import cn.afterturn.easypoi.excel.entity.ImportParams;

/**
 * 导入pop采购关系
 *
 * @author: dexi.yao
 * @date: 2023-06-20
 */
@Service
public class ImportPopProcRelationService implements BaseExcelImportService {

    @Autowired
    ImportPopProcRelationHandler importPopProcRelationHandler;

    @Override
    public ImportResultModel importExcel(InputStream inputstream, Integer readRows, Map<String, Object> param) throws Exception {
        ImportParams params = new ImportParams();
        params.setNeedVerify(true);
        params.setVerifyHandler(importPopProcRelationHandler);
        params.setKeyIndex(0);
        params.setReadRows(readRows);

        return ExeclImportUtils.importExcelMore(inputstream, ImportPopProcRelationModel.class, params, importPopProcRelationHandler, param);
    }
}
