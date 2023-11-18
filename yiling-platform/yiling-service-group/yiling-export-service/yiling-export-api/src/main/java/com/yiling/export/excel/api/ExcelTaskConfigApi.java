package com.yiling.export.excel.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.export.excel.dto.ExcelTaskConfigDTO;
import com.yiling.export.export.dto.ExportTaskRecordDTO;
import com.yiling.export.export.dto.request.QueryExportPageListRequest;
import com.yiling.export.export.dto.request.SaveExportTaskRequest;

/**
 * 导入中心服务
 *
 * @author: shuang.zhang
 * @date: 2021/5/17
 */
public interface ExcelTaskConfigApi {

    /**
     * 查询导入模板
     *
     * @param code
     * @return
     */
    ExcelTaskConfigDTO findExcelTaskConfigByCode(String code);

    /**
     * 通过主键获取导入配置信息
     * @param id
     * @return
     */
    ExcelTaskConfigDTO findExcelTaskConfigById(Long id);
}
