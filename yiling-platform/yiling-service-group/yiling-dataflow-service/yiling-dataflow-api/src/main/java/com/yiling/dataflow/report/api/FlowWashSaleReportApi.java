package com.yiling.dataflow.report.api;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.dataflow.report.dto.request.CreateFlowWashSaleReportRequest;
import com.yiling.dataflow.report.dto.request.QueryFlowWashSaleReportRequest;
import org.springframework.lang.Nullable;

import com.yiling.dataflow.report.dto.FlowWashSaleReportDTO;
import com.yiling.dataflow.report.dto.request.SumFlowWashReportRequest;
import com.yiling.dataflow.report.dto.request.UpdateFlowWashSaleReportRequest;
import com.yiling.dataflow.wash.enums.FlowClassifyEnum;

/**
 * 流向清洗销售合并报表Api
 *
 * @author zhigang.guo
 * @date: 2023/5/5
 */
public interface FlowWashSaleReportApi {


    /**
     * 更新流向报表信息  批量更细明细长度建议不超过500
     *
     * @param reportRequest
     * @return
     */
    Boolean updateDepartmentInfo(UpdateFlowWashSaleReportRequest reportRequest);

    /**
     * 查询流向数据
     *
     * @param flowSaleWashIds 流向Id
     * @return
     */
    @Deprecated
    List<FlowWashSaleReportDTO> listByFlowSaleWashIds(List<Long> flowSaleWashIds, @Nullable List<FlowClassifyEnum> flowClassifyEnumList);


    /**
     * 根据流向key获取流向数据
     * @param flowKeys
     * @return
     */
    List<FlowWashSaleReportDTO> listByFlowKey(List<String> flowKeys);


    /**
     * 根据年月查询经销商crmId
     * @param reportRequest 查询条件
     * @return
     */
    List<Long> listCrmIdByCondition(QueryFlowWashSaleReportRequest reportRequest);


    /**
     * 查询销售合并报表金额
     * @return 金额
     */
    BigDecimal sumTotalMoney(SumFlowWashReportRequest reportRequest);

    /**
     * 根据源流向创建新的流向
     * @param createFlowWashSaleReportRequest
     * @return
     */
    boolean createByFlowWashRecord(List<CreateFlowWashSaleReportRequest> createFlowWashSaleReportRequests);
}
