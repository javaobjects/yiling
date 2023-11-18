package com.yiling.export.imports.service;

import java.io.InputStream;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yiling.export.excel.service.BaseExcelImportService;
import com.yiling.export.imports.handler.ImportEnterpriseHandler;
import com.yiling.export.imports.model.ImportEnterpriseModel;
import com.yiling.export.excel.model.ImportResultModel;
import com.yiling.export.excel.util.ExeclImportUtils;

import cn.afterturn.easypoi.excel.entity.ImportParams;

/**
 * 导入企业数据
 *
 * @author: xuan.zhou
 * @date: 2022/12/1
 */
@Service
public class ImportEnterpriseService implements BaseExcelImportService {

    @Autowired
    ImportEnterpriseHandler importEnterpriseHandler;

    @Override
    public ImportResultModel importExcel(InputStream inputstream, Integer readRows, Map<String, Object> param) throws Exception {
        ImportParams params = new ImportParams();
        params.setNeedVerify(true);
        params.setVerifyHandler(importEnterpriseHandler);
        params.setKeyIndex(0);
        params.setReadRows(readRows);

        return ExeclImportUtils.importExcelMore(inputstream, ImportEnterpriseModel.class, params, importEnterpriseHandler, param);
    }
}
