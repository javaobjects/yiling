package com.yiling.dataflow.flow.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.yiling.dataflow.flow.dto.FlowCrmEnterpriseDTO;
import com.yiling.dataflow.flow.dto.request.FlowMonthBiTaskRequest;
import com.yiling.dataflow.flow.entity.FlowMonthBiTaskDO;
import com.yiling.dataflow.flow.excel.GoodsBatchFlowExcel;
import com.yiling.framework.common.base.BaseService;


/**
 * <p>
 * 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-04-06
 */
public interface FlowMonthBiTaskService extends BaseService<FlowMonthBiTaskDO> {
    /**
     * 执行流向导入到bi系统任务
     */
    void excelMonthFlowBiTask();

    /**
     * 获取所有类型流向bi excel数据
     * @param request
     * @return
     */
    HashMap<String, List> getAllFlowMonthBiData(FlowMonthBiTaskRequest request);

    /**
     * 获取所有月流向的企业
     * @param request
     * @return
     */
    List<FlowMonthBiTaskDO> getAllFlowEidList(FlowMonthBiTaskRequest request);

    /**
     * 获取企业crm信息
     * @param request
     * @return
     */
    FlowCrmEnterpriseDTO getCrmEnterpriseInfo(FlowMonthBiTaskRequest request);

     List<GoodsBatchFlowExcel> getGoodsBatchFlowExcelList(Long eid, Date time);


}
