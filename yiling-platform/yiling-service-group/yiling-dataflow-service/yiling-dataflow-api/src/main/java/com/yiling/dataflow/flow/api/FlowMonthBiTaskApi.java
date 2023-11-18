package com.yiling.dataflow.flow.api;

import java.util.HashMap;
import java.util.List;

import com.yiling.dataflow.flow.dto.FlowCrmEnterpriseDTO;
import com.yiling.dataflow.flow.dto.FlowMonthBiTaskDTO;
import com.yiling.dataflow.flow.dto.request.FlowMonthBiTaskRequest;

/**
 * @author shichen
 * @类名 FlowMonthBiTaskApi
 * @描述
 * @创建时间 2022/7/11
 * @修改人 shichen
 * @修改时间 2022/7/11
 **/
public interface FlowMonthBiTaskApi {
    /**
     * 获取所有类型流向bi excel数据
     * @param request
     * @return
     */
    HashMap<String, List> getAllFlowMonthBiData(FlowMonthBiTaskRequest request);

    /**
     * 获取企业crm信息
     * @param request
     * @return
     */
    FlowCrmEnterpriseDTO getCrmEnterpriseInfo(FlowMonthBiTaskRequest request);

    void saveBatch(List<FlowMonthBiTaskDTO> flowMonthBiTaskDTOList);


}
