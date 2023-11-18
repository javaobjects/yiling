package com.yiling.export.imports.service;

import java.io.InputStream;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yiling.export.excel.model.ImportResultModel;
import com.yiling.export.excel.service.BaseExcelImportService;
import com.yiling.export.excel.util.ExeclImportUtils;
import com.yiling.export.imports.handler.ImportOpenMemberHandler;
import com.yiling.export.imports.model.ImportOpenMemberModel;

import cn.afterturn.easypoi.excel.entity.ImportParams;

/**
 * 导入开通会员
 *
 * @author: lun.yu
 * @date: 2022-12-09
 */
@Service
public class ImportOpenMemberService implements BaseExcelImportService {

    @Autowired
    ImportOpenMemberHandler importOpenMemberHandler;

    @Override
    public ImportResultModel importExcel(InputStream inputstream, Integer readRows, Map<String, Object> param) throws Exception {
        ImportParams params = new ImportParams();
        params.setNeedVerify(true);
        params.setVerifyHandler(importOpenMemberHandler);
        params.setKeyIndex(0);
        params.setReadRows(readRows);

        return ExeclImportUtils.importExcelMore(inputstream, ImportOpenMemberModel.class, params, importOpenMemberHandler, param);
    }
}
