package com.yiling.dataflow.statistics;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import cn.afterturn.easypoi.csv.CsvExportUtil;
import cn.afterturn.easypoi.csv.entity.CsvExportParams;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.BaseTest;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseRelationShipDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsGroupDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmEnterprisePageRequest;
import com.yiling.dataflow.crm.dto.request.QueryCrmEnterpriseRelationShipRequest;
import com.yiling.dataflow.crm.entity.CrmGoodsInfoDO;
import com.yiling.dataflow.crm.service.CrmEnterpriseRelationShipService;
import com.yiling.dataflow.crm.service.CrmEnterpriseService;
import com.yiling.dataflow.crm.service.CrmGoodsGroupService;
import com.yiling.dataflow.crm.service.CrmGoodsInfoService;
import com.yiling.dataflow.flow.dto.request.FlowMonthBiTaskRequest;
import com.yiling.dataflow.flow.dto.request.QueryFlowSaleSummaryRequest;
import com.yiling.dataflow.flow.dto.request.UpdateFlowSaleSummaryRequest;
import com.yiling.dataflow.flow.entity.FlowMonthBiTaskDO;
import com.yiling.dataflow.flow.service.FlowMonthBiTaskService;
import com.yiling.dataflow.flow.service.FlowSaleSummaryDayService;
import com.yiling.dataflow.flow.service.FlowSaleSummaryService;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseListPageRequest;
import com.yiling.dataflow.order.entity.FlowSaleDO;
import com.yiling.dataflow.order.service.FlowSaleService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: shuang.zhang
 * @date: 2022/11/2
 */
@Slf4j
public class FlowSaleSummaryDayTest extends BaseTest {

  @Autowired
  private FlowSaleSummaryDayService flowSaleSummaryDayService;

    @Autowired
    private FlowSaleSummaryService flowSaleSummaryService;

    @Test
    public void Test2(){
        QueryFlowSaleSummaryRequest request=new QueryFlowSaleSummaryRequest();
        request.setEid(306054L);
        request.setStartTime(DateUtil.parse("2022-07-01","yyyy-MM-dd"));
        request.setEndTime(DateUtil.parse("2022-07-31","yyyy-MM-dd"));
        flowSaleSummaryDayService.updateFlowSaleSummaryDayByDateTimeAndEid(request);
        flowSaleSummaryDayService.updateFlowSaleSummaryDayLingShouByTerminalCustomerType(request);
        flowSaleSummaryDayService.updateFlowSaleSummaryDayPifaByTerminalCustomerType(request);
    }

    @Test
    public void test1(){
        UpdateFlowSaleSummaryRequest updateFlowSaleSummaryRequest=new UpdateFlowSaleSummaryRequest();
        updateFlowSaleSummaryRequest.setEid(7L);
        updateFlowSaleSummaryRequest.setCrmId(222905L);
        updateFlowSaleSummaryRequest.setStartTime(DateUtil.parse("2023-03-01","yyyy-MM-dd"));
        updateFlowSaleSummaryRequest.setEndTime(DateUtil.parse("2023-03-31","yyyy-MM-dd"));
        flowSaleSummaryService.updateFlowSaleSummaryByDateTimeAndEid(updateFlowSaleSummaryRequest);
    }


}
