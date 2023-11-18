package com.yiling.dataflow.check.service;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.check.bo.FlowPurchaseSpecificationIdTotalQuantityBO;
import com.yiling.dataflow.check.dto.request.FlowPurchaseSpecificationIdTotalQuantityRequest;
import com.yiling.dataflow.check.dto.request.QueryFlowPurchaseCheckTaskPageRequest;
import com.yiling.dataflow.check.entity.FlowPurchaseCheckTaskDO;

/**
 * @author: houjie.sun
 * @date: 2022/9/5
 */
public interface FlowPurchaseCheckService {

    List<FlowPurchaseSpecificationIdTotalQuantityBO> getSpecificationIdTotalQuantityByPoTime(FlowPurchaseSpecificationIdTotalQuantityRequest request);

    Boolean saveBatchCheck(List<FlowPurchaseCheckTaskDO> list);

    void flowPurchaseCheck();

    void getFlowPurchaseCheckTaskDO(List<FlowPurchaseCheckTaskDO> list, List<Long> eidList, Date start, Date end);

    /**
     * 根据开始、结束时间获取采购异常数据数量
     *
     * @param startTime
     * @param endTime
     * @return
     */
    Long getPurchaseExceptionCount(Date startTime, Date endTime);

    /**
     * 采购异常数据列表分页
     *
     * @param request
     * @return
     */
    Page<FlowPurchaseCheckTaskDO> page(QueryFlowPurchaseCheckTaskPageRequest request);
}
