package com.yiling.settlement.report.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.settlement.report.dto.ReportLogDTO;
import com.yiling.settlement.report.entity.LogDO;
import com.yiling.settlement.report.enums.ReportLogTypeEnum;

/**
 * <p>
 * 报表操作日志表 服务类
 * </p>
 *
 * @author dexi.yao
 * @date 2022-05-17
 */
public interface LogService extends BaseService<LogDO> {

    /**
     * 创建报表日志
     *
     * @param reportId
     * @param typeEnum
     * @param opUser
     * @return
     */
    void createReportTypeLog(Long reportId, ReportLogTypeEnum typeEnum, Long opUser);

    /**
     * 根据报表id查询日志
     *
     * @param reportId
     * @return
     */
    List<ReportLogDTO> queryLogList(Long reportId);

    /**
     * 创建报表日志
     *
     * @param logDO
     * @return
     */
    void createLog(LogDO logDO);
}
