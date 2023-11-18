package com.yiling.export.export.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.export.export.api.ExportApi;
import com.yiling.export.export.dto.ExportTaskRecordDTO;
import com.yiling.export.export.dto.request.QueryExportPageListRequest;
import com.yiling.export.export.dto.request.SaveExportTaskRequest;
import com.yiling.export.export.entity.ExportTaskRecordDO;
import com.yiling.export.export.service.ExportTaskRecordService;
import com.yiling.framework.common.util.PojoUtils;

/**
 * @author: shuang.zhang
 * @date: 2021/5/17
 */
@DubboService
public class ExportApiImpl implements ExportApi {

    @Autowired
    private ExportTaskRecordService exportTaskRecordService;

    @Override
    public Boolean saveExportTaskRecord(SaveExportTaskRequest request) {
        return exportTaskRecordService.saveExportTaskRecord(request);
    }

    @Override
    public Page<ExportTaskRecordDTO> pageList(QueryExportPageListRequest request) {
        Page<ExportTaskRecordDO> page = exportTaskRecordService.pageList(request);
        return PojoUtils.map(page, ExportTaskRecordDTO.class);
    }

    @Override
    public ExportTaskRecordDTO findExportTaskRecord(Long userId, Long id) {
        return PojoUtils.map(exportTaskRecordService.findExportTaskRecord(userId, id), ExportTaskRecordDTO.class);
    }
}
