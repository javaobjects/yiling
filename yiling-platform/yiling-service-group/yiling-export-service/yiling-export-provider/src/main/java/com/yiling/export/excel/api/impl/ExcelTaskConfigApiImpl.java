package com.yiling.export.excel.api.impl;

import com.yiling.export.excel.api.ExcelTaskConfigApi;
import com.yiling.export.excel.dto.ExcelTaskConfigDTO;
import com.yiling.export.excel.service.ExcelTaskConfigService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: shuang.zhang
 * @date: 2021/5/17
 */
@DubboService
public class ExcelTaskConfigApiImpl implements ExcelTaskConfigApi {

    @Autowired
    private ExcelTaskConfigService excelTaskConfigService;

    @Override
    public ExcelTaskConfigDTO findExcelTaskConfigByCode(String className) {
        return excelTaskConfigService.findExcelTaskConfigByCode(className);
    }

    @Override
    public ExcelTaskConfigDTO findExcelTaskConfigById(Long id) {
        return excelTaskConfigService.findExcelTaskConfigById(id);
    }
}
