package com.yiling.settlement.report.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.settlement.report.entity.ReportTaskDO;

/**
 * <p>
 * 返利报表生成任务表 服务类
 * </p>
 *
 * @author dexi.yao
 * @date 2022-10-09
 */
public interface ReportTaskService extends BaseService<ReportTaskDO> {


    /**
     * 根据eid查询正在生成中的报表任务
     *
     * @param eid
     * @return
     */
    List<ReportTaskDO> queryInProductionTask(Long eid);
}
