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
import com.yiling.dataflow.flow.entity.FlowMonthBiTaskDO;
import com.yiling.dataflow.flow.service.FlowMonthBiTaskService;
import com.yiling.dataflow.flow.service.FlowSaleSummaryService;
import com.yiling.dataflow.flow.util.FlowJudgeTypeUtil;
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
public class FlowSaleSummaryTest extends BaseTest {

    @Autowired
    private FlowMonthBiTaskService           flowMonthBiTaskService;
    @Autowired
    private CrmEnterpriseService             crmEnterpriseService;
    @Autowired
    private FlowSaleService                  flowSaleService;
    @Autowired
    private CrmEnterpriseRelationShipService crmEnterpriseRelationShipService;
    @Autowired
    private CrmGoodsGroupService             crmGoodsGroupService;
    @Autowired
    private CrmGoodsInfoService              crmGoodsInfoService;
    @Autowired
    private FlowSaleSummaryService           flowSaleSummaryService;
    @Autowired
    private FlowJudgeTypeUtil                flowJudgeTypeUtil;

    @Test
    public void Test3(){
        String type=flowJudgeTypeUtil.flowJudgeType("上海交通大学医学院附属新华医院{A061148}新华西");
        System.out.println(type);
    }
    @Test
    public void Test2(){

    }
}
