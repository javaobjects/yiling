package com.yiling.dataflow.check.api;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.check.bo.FlowPurchaseSpecificationIdTotalQuantityBO;
import com.yiling.dataflow.check.dto.FlowPurchaseCheckTaskDTO;
import com.yiling.dataflow.check.dto.request.FlowPurchaseSpecificationIdTotalQuantityRequest;
import com.yiling.dataflow.check.dto.request.QueryFlowPurchaseCheckTaskPageRequest;

/**
 * 采购流向是否有对应的销售数据核查
 *
 * @author: houjie.sun
 * @date: 2022/9/5
 */
public interface FlowPurchaseCheckApi {

    List<FlowPurchaseSpecificationIdTotalQuantityBO> getSpecificationIdTotalQuantityByPoTime(FlowPurchaseSpecificationIdTotalQuantityRequest request);

    Boolean saveBatch(List<FlowPurchaseCheckTaskDTO> list);

    void flowPurchaseCheck();

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
    Page<FlowPurchaseCheckTaskDTO> getPurchaseExceptionListPage(QueryFlowPurchaseCheckTaskPageRequest request);
}
