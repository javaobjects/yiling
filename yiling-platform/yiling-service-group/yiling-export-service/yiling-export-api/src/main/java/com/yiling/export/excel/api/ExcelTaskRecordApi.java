package com.yiling.export.excel.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.export.excel.dto.ExcelTaskRecordDTO;
import com.yiling.export.excel.dto.request.QueryExcelTaskRecordPageListRequest;
import com.yiling.export.excel.dto.request.SaveExcelTaskRecordRequest;

/**
 * 导出中心服务
 *
 * @author: shuang.zhang
 * @date: 2021/5/17
 */
public interface ExcelTaskRecordApi {
    /**
     * 创建导出任务
     *
     * @param request
     * @return
     */
    Long saveExcelTaskRecord(SaveExcelTaskRecordRequest request);

    /**
     * 导出任务分页
     *
     * @param request
     * @return
     */
    Page<ExcelTaskRecordDTO> pageList(QueryExcelTaskRecordPageListRequest request);

    /**
     * 查询导出任务
     *
     * @param id
     * @return
     */
    ExcelTaskRecordDTO findExcelTaskRecordById(Long id);
}
