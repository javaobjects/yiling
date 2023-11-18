package com.yiling.export.imports.service;

import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.yiling.export.excel.model.ImportResultModel;
import com.yiling.export.excel.service.BaseExcelImportService;
import com.yiling.export.excel.util.ExeclImportUtils;
import com.yiling.export.imports.handler.ImportActivityDocPatientDoctorHandler;
import com.yiling.export.imports.model.ImportActivityDocPatientDoctorExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Map;

/**
 * @author fan.shen
 * @类名 ImportActivityDocPatientDoctorExcel
 * @描述 导入C端活动医生
 * @创建时间 2023-02-03
 * @修改人 fan.shen
 * @修改时间 2023-02-03
 **/
@Service
public class ImportActivityDocPatientDoctorService implements BaseExcelImportService {

    @Autowired
    ImportActivityDocPatientDoctorHandler importActivityDocPatientDoctorHandler;

    @Override
    public ImportResultModel importExcel(InputStream inputstream, Integer readRows, Map<String, Object> param) throws Exception {
        ImportParams params = new ImportParams();
        params.setNeedVerify(true);
        params.setVerifyHandler(importActivityDocPatientDoctorHandler);
        params.setKeyIndex(0);
        params.setReadRows(readRows);

        return ExeclImportUtils.importExcelMore(inputstream, ImportActivityDocPatientDoctorExcel.class, params, importActivityDocPatientDoctorHandler, param);
    }
}
