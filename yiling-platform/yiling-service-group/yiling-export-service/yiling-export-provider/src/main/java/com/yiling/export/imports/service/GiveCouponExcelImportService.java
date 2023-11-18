package com.yiling.export.imports.service;

import java.io.InputStream;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yiling.export.imports.model.ImportCouponInfoExcel;
import com.yiling.export.imports.handler.ImportCouponActivityDataHandler;
import com.yiling.export.excel.service.BaseExcelImportService;
import com.yiling.export.excel.model.ImportResultModel;
import com.yiling.export.excel.util.ExeclImportUtils;

import cn.afterturn.easypoi.excel.entity.ImportParams;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2022/11/17
 */
@Slf4j
@Service("giveCouponExcelImportService")
public class GiveCouponExcelImportService implements BaseExcelImportService {
    @Autowired
    ImportCouponActivityDataHandler importCouponActivityDataHandler;

    @Override
    public ImportResultModel importExcel(InputStream inputstream, Integer readRows, Map<String, Object> param) throws Exception {
        log.info("导入importExcel param{}", param);
        ImportParams params = new ImportParams();
        params.setNeedVerify(true);
        params.setVerifyHandler(null);
        params.setKeyIndex(0);
        params.setReadRows(readRows);
        return ExeclImportUtils.importExcelMore(inputstream, ImportCouponInfoExcel.class, params, importCouponActivityDataHandler, param);
    }
}
