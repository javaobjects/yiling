package com.yiling.settlement.report.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.settlement.report.dto.request.QuerySyncTaskPageListRequest;
import com.yiling.settlement.report.dto.request.ReportSyncTaskDTO;
import com.yiling.settlement.report.entity.ReportSyncTaskDO;
import com.yiling.framework.common.base.BaseService;
import com.yiling.settlement.report.enums.ReportSyncTaskEnum;

/**
 * <p>
 * 返利报表同步任务表 服务类
 * </p>
 *
 * @author dexi.yao
 * @date 2022-10-13
 */
public interface ReportSyncTaskService extends BaseService<ReportSyncTaskDO> {

    /**
     * 新增同步任务
     *
     * @param syncData
     * @param syncTaskEnum
     */
    void addSyncTask(String syncData, ReportSyncTaskEnum syncTaskEnum);

    /**
     * 分页查询同步任务
     *
     * @param request
     * @return
     */
    Page<ReportSyncTaskDTO> querySyncPageList(QuerySyncTaskPageListRequest request);

    /**
     * 同步流向订单
     */
    void syncFlowOrder();
}
