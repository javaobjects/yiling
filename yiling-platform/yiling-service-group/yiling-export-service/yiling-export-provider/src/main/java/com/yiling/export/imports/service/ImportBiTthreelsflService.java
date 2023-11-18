package com.yiling.export.imports.service;

import java.io.InputStream;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yiling.export.excel.model.ImportResultModel;
import com.yiling.export.excel.service.BaseExcelImportService;
import com.yiling.export.excel.util.ExeclImportUtils;
import com.yiling.export.imports.handler.ImportBiTthreelsflHandler;
import com.yiling.export.imports.model.ImportBiTthreelsflModel;

import cn.afterturn.easypoi.excel.entity.ImportParams;

/**
 * @author fucheng.bai
 * @date 2023/1/4
 */
@Service
public class ImportBiTthreelsflService implements BaseExcelImportService {

    @Autowired
    private ImportBiTthreelsflHandler importBiTthreelsflHandler;


    @Override
    public ImportResultModel importExcel(InputStream inputstream, Integer readRows, Map<String, Object> param) throws Exception {
        ImportParams params = new ImportParams();
        params.setNeedVerify(true);
        params.setVerifyHandler(importBiTthreelsflHandler);
        params.setKeyIndex(1);
        params.setReadRows(readRows);

        return ExeclImportUtils.importExcelMore(inputstream, ImportBiTthreelsflModel.class, params, importBiTthreelsflHandler, param);
    }
}
