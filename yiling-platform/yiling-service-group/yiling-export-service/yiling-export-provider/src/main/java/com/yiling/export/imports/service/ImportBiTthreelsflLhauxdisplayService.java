package com.yiling.export.imports.service;

import java.io.InputStream;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yiling.export.excel.model.ImportResultModel;
import com.yiling.export.excel.service.BaseExcelImportService;
import com.yiling.export.excel.util.ExeclImportUtils;
import com.yiling.export.imports.handler.ImportBiTthreelsflLhauxdisplayHandler;
import com.yiling.export.imports.model.ImportBiTthreelsflLhauxdisplayModel;

import cn.afterturn.easypoi.excel.entity.ImportParams;

/**
 * @author fucheng.bai
 * @date 2023/1/4
 */
@Service
public class ImportBiTthreelsflLhauxdisplayService implements BaseExcelImportService {

    @Autowired
    private ImportBiTthreelsflLhauxdisplayHandler importBiTthreelsflLhauxdisplayHandler;

    @Override
    public ImportResultModel importExcel(InputStream inputstream, Integer readRows, Map<String, Object> param) throws Exception {
        ImportParams params = new ImportParams();
        params.setNeedVerify(true);
        params.setVerifyHandler(importBiTthreelsflLhauxdisplayHandler);
        params.setKeyIndex(0);
        params.setReadRows(readRows);

        return ExeclImportUtils.importExcelMore(inputstream, ImportBiTthreelsflLhauxdisplayModel.class, params, importBiTthreelsflLhauxdisplayHandler, param);
    }
}
