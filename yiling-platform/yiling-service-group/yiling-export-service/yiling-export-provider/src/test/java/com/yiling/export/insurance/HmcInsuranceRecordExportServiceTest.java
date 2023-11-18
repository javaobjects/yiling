package com.yiling.export.insurance;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.export.BaseTest;
import com.yiling.export.excel.service.ExcelTaskRecordService;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.impl.HmcInsurancePayRecordExportServiceImpl;
import com.yiling.export.export.service.impl.HmcInsuranceRecordExportServiceImpl;
import com.yiling.hmc.insurance.dto.request.QueryBackInsuranceRecordPageRequest;
import com.yiling.hmc.insurance.dto.request.QueryInsuranceRecordPayPageRequest;

/**
 * @author: gxl
 * @date: 2022/4/21
 */
public class HmcInsuranceRecordExportServiceTest extends BaseTest {
    @Autowired
    HmcInsuranceRecordExportServiceImpl insuranceRecordExportService;
    @Autowired
    HmcInsurancePayRecordExportServiceImpl insurancePayRecordExportService;

    @Autowired
    ExcelTaskRecordService excelTaskRecordService;

    @Test
    public void test1(){
        QueryBackInsuranceRecordPageRequest request = new QueryBackInsuranceRecordPageRequest();
        QueryExportDataDTO queryExportDataDTO = insuranceRecordExportService.queryData(request);
        System.out.println(queryExportDataDTO.getSheets().get(0).getData().toString());
    }

    @Test
    public void test2(){
        QueryInsuranceRecordPayPageRequest request = new QueryInsuranceRecordPayPageRequest();
        request.setEid(6L);
        QueryExportDataDTO queryExportDataDTO = insurancePayRecordExportService.queryData(request);
        System.out.println(queryExportDataDTO.getSheets().get(0).getData().toString());
    }

    @Test
    public void test3(){
        excelTaskRecordService.syncImportExcelTask(238L);
    }
}