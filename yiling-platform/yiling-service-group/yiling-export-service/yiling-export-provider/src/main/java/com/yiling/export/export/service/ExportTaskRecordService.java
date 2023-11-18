package com.yiling.export.export.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.export.export.dto.request.QueryExportPageListRequest;
import com.yiling.export.export.dto.request.SaveExportTaskRequest;
import com.yiling.export.export.entity.ExportTaskRecordDO;
import com.yiling.framework.common.base.BaseService;

/**
 * @author: shuang.zhang
 * @date: 2021/5/17
 */
public interface ExportTaskRecordService extends BaseService<ExportTaskRecordDO> {
    /**
     * 创建导出任务
     *
     * @param request
     * @return
     */
    Boolean saveExportTaskRecord(SaveExportTaskRequest request);

    /**
     * mq执行导出任务
     *
     * @param exportTaskRecordId
     */
    void syncUploadExportTask(Long exportTaskRecordId);
    /**
     * 导出任务分页
     *
     * @param request
     * @return
     */
    Page<ExportTaskRecordDO> pageList(QueryExportPageListRequest request);

    /**
     * 查询导出任务
     *
     * @param userId
     * @param id
     * @return
     */
    ExportTaskRecordDO findExportTaskRecord(Long userId, Long id);
}
