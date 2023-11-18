package com.yiling.export.excel.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.export.excel.dto.ExcelTaskRecordDTO;
import com.yiling.export.excel.dto.request.QueryExcelTaskRecordPageListRequest;
import com.yiling.export.excel.dto.request.SaveExcelTaskRecordRequest;
import com.yiling.export.excel.entity.ExcelTaskRecordDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 导入日志表 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-11-17
 */
public interface ExcelTaskRecordService extends BaseService<ExcelTaskRecordDO> {

    /**
     * mq执行导出任务
     *
     * @param excelTaskRecordId
     */
    void syncImportExcelTask(Long excelTaskRecordId);

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
