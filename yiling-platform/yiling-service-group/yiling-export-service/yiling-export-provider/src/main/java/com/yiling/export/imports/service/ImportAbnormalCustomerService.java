package com.yiling.export.imports.service;

import java.io.InputStream;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yiling.export.excel.model.ImportResultModel;
import com.yiling.export.excel.service.BaseExcelImportService;
import com.yiling.export.excel.util.ExeclImportUtils;
import com.yiling.export.imports.handler.ImportAbnormalCustomerHandler;
import com.yiling.export.imports.model.ImportAbnormalCustomerExcel;

import cn.afterturn.easypoi.excel.entity.ImportParams;

/**
 * @author shichen
 * @类名 ImportAbnormalCustomerService
 * @描述
 * @创建时间 2022/12/22
 * @修改人 shichen
 * @修改时间 2022/12/22
 **/
@Service("importAbnormalCustomerService")
public class ImportAbnormalCustomerService implements BaseExcelImportService {
    @Autowired
    ImportAbnormalCustomerHandler importAbnormalCustomerHandler;

    @Override
    public ImportResultModel importExcel(InputStream inputstream, Integer readRows, Map<String, Object> param) throws Exception {
        ImportParams params = new ImportParams();
        params.setNeedVerify(true);
        params.setVerifyHandler(importAbnormalCustomerHandler);
        params.setKeyIndex(0);
        params.setReadRows(readRows);
        return ExeclImportUtils.importExcelMore(inputstream, ImportAbnormalCustomerExcel.class, params, importAbnormalCustomerHandler, param);
    }
}
