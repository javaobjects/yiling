package com.yiling.export.export.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.export.export.dto.ExportTaskRecordDTO;
import com.yiling.export.export.dto.request.QueryExportPageListRequest;
import com.yiling.export.export.dto.request.SaveExportTaskRequest;

/**
 * 导出中心服务
 *
 * @author: shuang.zhang
 * @date: 2021/5/17
 */
public interface ExportApi {
    /**
     * 创建导出任务
     *
     * @param request
     * @return
     */
    Boolean saveExportTaskRecord(SaveExportTaskRequest request);

    /**
     * 导出任务分页
     *
     * @param request
     * @return
     */
    Page<ExportTaskRecordDTO> pageList(QueryExportPageListRequest request);

    /**
     * 查询导出任务
     *
     * @param userId
     * @param id
     * @return
     */
    ExportTaskRecordDTO findExportTaskRecord(Long userId, Long id);
}
