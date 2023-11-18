package com.yiling.export.imports.service;

import java.io.InputStream;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yiling.export.excel.service.BaseExcelImportService;
import com.yiling.export.imports.handler.ImportStaffHandler;
import com.yiling.export.imports.model.ImportStaffModel;
import com.yiling.export.excel.model.ImportResultModel;
import com.yiling.export.excel.util.ExeclImportUtils;

import cn.afterturn.easypoi.excel.entity.ImportParams;

/**
 * 导入员工账号数据
 *
 * @author: xuan.zhou
 * @date: 2022/12/1
 */
@Service
public class ImportStaffService implements BaseExcelImportService {

    @Autowired
    ImportStaffHandler importStaffHandler;

    @Override
    public ImportResultModel importExcel(InputStream inputstream, Integer readRows, Map<String, Object> param) throws Exception {
        ImportParams params = new ImportParams();
        params.setNeedVerify(true);
        params.setVerifyHandler(importStaffHandler);
        params.setKeyIndex(0);
        params.setReadRows(readRows);

        return ExeclImportUtils.importExcelMore(inputstream, ImportStaffModel.class, params, importStaffHandler, param);
    }
}
