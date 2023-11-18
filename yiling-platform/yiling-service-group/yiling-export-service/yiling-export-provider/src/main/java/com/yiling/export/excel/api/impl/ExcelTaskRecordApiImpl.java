package com.yiling.export.excel.api.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.export.excel.api.ExcelTaskRecordApi;
import com.yiling.export.excel.dto.ExcelTaskRecordDTO;
import com.yiling.export.excel.dto.request.QueryExcelTaskRecordPageListRequest;
import com.yiling.export.excel.dto.request.SaveExcelTaskRecordRequest;
import com.yiling.export.excel.service.ExcelTaskRecordService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: shuang.zhang
 * @date: 2022/11/17
 */
@DubboService
public class ExcelTaskRecordApiImpl implements ExcelTaskRecordApi {

    @Autowired
    private ExcelTaskRecordService excelTaskRecordService;

    @Override
    public Long saveExcelTaskRecord(SaveExcelTaskRecordRequest request) {
        return excelTaskRecordService.saveExcelTaskRecord(request);
    }

    @Override
    public Page<ExcelTaskRecordDTO> pageList(QueryExcelTaskRecordPageListRequest request) {
        return excelTaskRecordService.pageList(request);
    }

    @Override
    public ExcelTaskRecordDTO findExcelTaskRecordById(Long id) {
        return excelTaskRecordService.findExcelTaskRecordById(id);
    }
}
