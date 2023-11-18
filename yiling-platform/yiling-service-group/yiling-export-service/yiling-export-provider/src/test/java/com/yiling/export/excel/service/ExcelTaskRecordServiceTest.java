package com.yiling.export.excel.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.export.BaseTest;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: xuan.zhou
 * @date: 2023/3/31
 */
@Slf4j
public class ExcelTaskRecordServiceTest extends BaseTest {

    @Autowired
    ExcelTaskRecordService excelTaskRecordService;

    @Test
    public void syncImportExcelTask() {
        Long excelTaskRecordId = 2269L;
        excelTaskRecordService.syncImportExcelTask(excelTaskRecordId);
    }
}
