package com.yiling.dataflow.order.api;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.order.bo.FlowStatisticsBO;
import com.yiling.dataflow.order.dto.FlowGoodsBatchDetailDTO;
import com.yiling.dataflow.order.dto.request.DeleteFlowGoodsBatchDetailRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsBatchDetailExistRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsBatchDetailListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowStatisticesRequest;
import com.yiling.dataflow.order.dto.request.SaveOrUpdateFlowGoodsBatchDetailRequest;

/**
 * erp库存流向接口
 *
 * @author: houjie.sun
 * @date: 2022/2/14
 */
public interface FlowGoodsBatchDetailApi {

    /**
     * 查询列表分页
     * @param request
     * @return
     */
    Page<FlowGoodsBatchDetailDTO> page(QueryFlowGoodsBatchDetailListPageRequest request);

    /**
     * 新增流向
     * @param requestList
     * @return
     */
    Boolean updateFlowGoodsBatchDetailByIds(List<SaveOrUpdateFlowGoodsBatchDetailRequest> requestList);

    /**
     * 删除流向
     * @param request
     * @return
     */
    Integer deleteFlowGoodsBatchDetailByEidAndDate(DeleteFlowGoodsBatchDetailRequest request);

    Integer addFlowGoodsBatchDetailList(List<SaveOrUpdateFlowGoodsBatchDetailRequest> requestList);

    void statisticsFlowGoodsBatch(List<Long> suIdList);

    /**
     * 根据企业id、当前库存时间查询是否有流向数据
     *
     * @param request
     * @return true / false
     */
    boolean isHaveDataByEidAndGbDetailTime(QueryFlowGoodsBatchDetailExistRequest request);

    List<FlowStatisticsBO> getFlowGoodsBatchStatistics(QueryFlowStatisticesRequest request);
}
