package com.yiling.dataflow;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.dto.request.UpdateCrmEnterpriseRelationShipRequest;
import com.yiling.dataflow.crm.entity.CrmEnterpriseRelationShipDO;
import com.yiling.dataflow.crm.service.CrmEnterpriseRelationShipService;
import com.yiling.dataflow.statistics.api.FlowBalanceStatisticsApi;
import com.yiling.dataflow.statistics.dto.FlowBalanceStatisticsDTO;
import com.yiling.dataflow.statistics.dto.FlowBalanceStatisticsMonthDTO;
import com.yiling.dataflow.statistics.dto.GoodsSpecInfoDTO;
import com.yiling.dataflow.statistics.dto.GoodsSpecNoMatchedDTO;
import com.yiling.dataflow.statistics.dto.GoodsSpecStatisticsDTO;
import com.yiling.dataflow.statistics.dto.request.FlushGoodsSpecIdRequest;
import com.yiling.dataflow.statistics.dto.request.GoodsSpecStatisticsRequest;
import com.yiling.dataflow.statistics.dto.request.QueryBalanceStatisticsMonthRequest;
import com.yiling.dataflow.statistics.dto.request.QueryBalanceStatisticsRequest;
import com.yiling.dataflow.statistics.dto.request.RecommendScoreRequest;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2022/7/27
 */
@Slf4j
public class FlowStatisticsApiTest extends BaseTest {


    @Autowired
    CrmEnterpriseRelationShipService crmEnterpriseRelationShipService;
    @Autowired
    private FlowBalanceStatisticsApi flowBalanceStatisticsApi;

    @Test
    public void crmEnterpriseRelationShipService() {
        CrmEnterpriseRelationShipDO byId = crmEnterpriseRelationShipService.getById(598731L);
        UpdateCrmEnterpriseRelationShipRequest request = PojoUtils.map(byId, UpdateCrmEnterpriseRelationShipRequest.class);
        crmEnterpriseRelationShipService.updateBackUpBatchByPostCode(request,"crm_enterprise_relation_ship_wash_202302");

    }

    @Test
    public void getFlowBalanceStatisticsEnterpriseListTest() {
        List<FlowBalanceStatisticsDTO> list = flowBalanceStatisticsApi.getFlowBalanceStatisticsEnterpriseList();
        System.out.println("list = " + list);
    }

    @Test
    public void getMonthPageListTest() {
        QueryBalanceStatisticsMonthRequest request = new QueryBalanceStatisticsMonthRequest();
        request.setCurrent(1);
        request.setSize(10);

        request.setTime("2022-08");
//        request.setQuantityType(3);
//        request.setMinQuantity(0);
//        request.setMaxQuantity(10000);

        Page<FlowBalanceStatisticsMonthDTO> page = flowBalanceStatisticsApi.getMonthPageList(request);
        System.out.println("page.total = " + page.getTotal());
        System.out.println("page.records = " + page.getRecords());
    }

    @Test
    public void getFlowBalanceStatisticsListTest() {
        QueryBalanceStatisticsRequest request = new QueryBalanceStatisticsRequest();
        request.setEid(304931L);
        request.setMonthTime("2022-07");

        List<FlowBalanceStatisticsDTO> flowBalanceStatisticsDTOList = flowBalanceStatisticsApi.getFlowBalanceStatisticsList(request);
        System.out.println("flowBalanceStatisticsDTOList.size = " + flowBalanceStatisticsDTOList.size());
    }


    public void getGoodsSpecInfoListTest() {
        Long eid = 304931L;
        String monthTime = "2022-07";
        List<GoodsSpecInfoDTO> goodsSpecInfoDTOList = flowBalanceStatisticsApi.getGoodsSpecInfoList(eid, monthTime);
        System.out.println("goodsSpecInfoDTOList.size = " + goodsSpecInfoDTOList.size());
    }

    @Test
    public void getGoodsSpecStatisticsListPageTest() {
        GoodsSpecStatisticsRequest request = new GoodsSpecStatisticsRequest();
        request.setCurrent(1);
        request.setSize(10);
        request.setMonthTime("2022-09");
        request.setEid(306054L);
//        request.setSpecificationId(1104L);
        Page<GoodsSpecStatisticsDTO> page = flowBalanceStatisticsApi.getGoodsSpecStatisticsListPage(request);
        System.out.println("page.total = " + page.getTotal());

    }

    @Test
    public void goodsSpecNoMatchedListTest() {
        GoodsSpecStatisticsRequest request = new GoodsSpecStatisticsRequest();
        request.setEid(64L);
        request.setMonthTime("2022-08");

        List<GoodsSpecNoMatchedDTO> list = flowBalanceStatisticsApi.goodsSpecNoMatchedList(request);
        System.out.println(list);
    }

    @Test
    public void getRecommendScoreTest() {
        RecommendScoreRequest request = new RecommendScoreRequest();
        request.setGoodsName("△芪苈强心胶囊（以岭）");
        request.setSpec("0.3g*36s");
        request.setTargetGoodsName("芪苈强心胶囊");
        request.setTargetSpec("0.3g*12粒*3板");

        Integer per = flowBalanceStatisticsApi.getRecommendScore(request);
        System.out.println(per);
    }

    @Test
    public void flushGoodsSpecificationIdTest() {
        String json = "{\"eid\":306054,\"flushDataList\":[{\"id\":0,\"goodsName\":\"连花清瘟胶囊\",\"spec\":\"0.35g*12粒*2板\",\"recommendSpecificationId\":\"494\",\"recommendGoods\":\"\",\"recommendSpec\":\"0.35g*12粒*2板\",\"recommendScore\":9375,\"goodsSpecification\":\"连花清瘟胶囊-0.35g*12粒*2板\",\"recommendGoodsName\":\"连花清瘟胶囊\"},{\"id\":0,\"goodsName\":\"参松养心胶囊\",\"spec\":\"0.4g*36粒\",\"recommendSpecificationId\":\"1104\",\"recommendGoods\":\"\",\"recommendSpec\":\"0.4g*36粒\",\"recommendScore\":9286,\"goodsSpecification\":\"参松养心胶囊-0.4g*36粒\",\"recommendGoodsName\":\"参松养心胶囊\"},{\"id\":0,\"goodsName\":\"通心络胶囊\",\"spec\":\"0.26g*10粒*3板\",\"recommendSpecificationId\":\"5513\",\"recommendGoods\":\"\",\"recommendSpec\":\"0.26g*30粒\",\"recommendScore\":9231,\"goodsSpecification\":\"通心络胶囊-0.26g*30粒\",\"recommendGoodsName\":\"通心络胶囊\"},{\"id\":0,\"goodsName\":\"通心络胶囊\",\"spec\":\"0.26g*10粒*3板\",\"recommendSpecificationId\":\"5513\",\"recommendGoods\":\"\",\"recommendSpec\":\"0.26g*30粒\",\"recommendScore\":9231,\"goodsSpecification\":\"通心络胶囊-0.26g*30粒\",\"recommendGoodsName\":\"通心络胶囊\"}]}";

        FlushGoodsSpecIdRequest request = JSONUtil.toBean(json, FlushGoodsSpecIdRequest.class);
//        FlushGoodsSpecIdRequest request = new FlushGoodsSpecIdRequest();
//        request.setEid(64L);
//
//        FlushGoodsSpecIdRequest.FlushDataRequest flushData = new FlushGoodsSpecIdRequest.FlushDataRequest();
//        flushData.setGoodsName("津力达颗粒_以岭_9g*15袋");
//        flushData.setSpec("9G*15袋");
//        flushData.setRecommendGoodsName("津力达颗粒");
//        flushData.setRecommendSpec("9g*9袋");
//        flushData.setRecommendSpecificationId(19279L);
//        flushData.setRecommendScore(8913);
//        List<FlushGoodsSpecIdRequest.FlushDataRequest> flushDataList = new ArrayList<>();
//        flushDataList.add(flushData);
//        request.setFlushDataList(flushDataList);

        flowBalanceStatisticsApi.flushGoodsSpecificationId(request);

    }

    @Test
    public void isUsedSpecificationIdTest() {

        boolean result = flowBalanceStatisticsApi.isUsedSpecificationId(123456L);
        System.out.println(result);
    }


}
