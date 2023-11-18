package com.yiling.dataflow.report.api.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.yiling.dataflow.report.dto.request.CreateFlowWashSaleReportRequest;
import com.yiling.dataflow.report.dto.request.QueryFlowWashSaleReportRequest;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.http.util.Asserts;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Preconditions;
import com.yiling.dataflow.report.api.FlowWashSaleReportApi;
import com.yiling.dataflow.report.dto.FlowWashSaleReportDTO;
import com.yiling.dataflow.report.dto.request.SumFlowWashReportRequest;
import com.yiling.dataflow.report.dto.request.UpdateFlowWashSaleReportRequest;
import com.yiling.dataflow.report.entity.FlowWashSaleReportDO;
import com.yiling.dataflow.report.service.FlowWashSaleReportService;
import com.yiling.dataflow.wash.enums.FlowClassifyEnum;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollectionUtil;

/**
 * 流向销售合并报表api
 *
 * @author zhigang.guo
 * @date: 2023/5/5
 */
@DubboService
public class FlowWashSaleReportApiImpl implements FlowWashSaleReportApi {

    @Autowired
    private FlowWashSaleReportService flowWashSaleReportService;


    @Override
    public Boolean updateDepartmentInfo(UpdateFlowWashSaleReportRequest reportRequest) {
        Asserts.check(CollectionUtil.isNotEmpty(reportRequest.getDetailRequests()), "更新数据不能为空!");
        Preconditions.checkArgument(reportRequest.getDetailRequests().size() <= 500, "更新长度超过500,请分批处理!");
        List<FlowWashSaleReportDO> updateReportList = reportRequest.getDetailRequests().stream().map(t -> PojoUtils.map(t, FlowWashSaleReportDO.class)).collect(Collectors.toList());

        return flowWashSaleReportService.updateBatchById(updateReportList);
    }


    @Override
    public List<FlowWashSaleReportDTO> listByFlowSaleWashIds(List<Long> flowSaleWashIds, List<FlowClassifyEnum> flowClassifyEnumList) {

        return flowWashSaleReportService.listByFlowSaleWashIds(flowSaleWashIds, flowClassifyEnumList);
    }

    @Override
    public List<Long> listCrmIdByCondition(QueryFlowWashSaleReportRequest reportRequest) {
        return flowWashSaleReportService.listCrmIdByCondition(reportRequest);
    }


    @Override
    public BigDecimal sumTotalMoney(SumFlowWashReportRequest reportRequest) {


        return flowWashSaleReportService.sumTotalMoney(reportRequest);
    }


    @Override
    public List<FlowWashSaleReportDTO> listByFlowKey(List<String> flowKeys) {

        return PojoUtils.map(flowWashSaleReportService.listByFlowKey(flowKeys),FlowWashSaleReportDTO.class);
    }

    @Override
    public boolean createByFlowWashRecord(List<CreateFlowWashSaleReportRequest> createFlowWashSaleReportRequests) {

        return flowWashSaleReportService.createByFlowWashRecord(createFlowWashSaleReportRequests);
    }
}
