package com.yiling.dataflow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsRelationPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsRelationYlGoodsIdRequest;
import com.yiling.dataflow.order.dto.request.SaveOrUpdateFlowGoodsRelationRequest;
import com.yiling.dataflow.order.dto.request.UpdateFlowGoodsRelationRequest;
import com.yiling.dataflow.relation.entity.FlowGoodsRelationDO;
import com.yiling.dataflow.relation.handler.FlowGoodsRelationHandler;
import com.yiling.dataflow.relation.service.FlowGoodsRelationService;

import cn.hutool.core.date.DateUtil;

/**
 * @author: houjie.sun
 * @date: 2022/5/24
 */
public class FlowGoodsRelationServiceTest extends BaseTest  {

    @Autowired
    private FlowGoodsRelationService flowGoodsRelationService;
    @Autowired
    private FlowGoodsRelationHandler flowGoodsRelationHandler;

    @Test
    public void pageTest() {
        System.out.println(">>>>> pageTest start");
        QueryFlowGoodsRelationPageRequest request = new QueryFlowGoodsRelationPageRequest();
        request.setEid(4333L);
        request.setCreateTimeStart(DateUtil.parse("2022-06-01", "yyyy-MM-dd"));
        request.setCreateTimeEnd(DateUtil.parse("2022-06-21", "yyyy-MM-dd"));
        request.setRelationFlag(0);
        List<Long> opUserList = new ArrayList<>();
        opUserList.add(1L);
        opUserList.add(2L);
        opUserList.add(3L);
        request.setOpUserIdList(opUserList);

        Page<FlowGoodsRelationDO> page = flowGoodsRelationService.page(request);
        System.out.println(">>>>> pageTest end");
    }

    @Test
    public void editTest() {
        System.out.println(">>>>> edit start");
        UpdateFlowGoodsRelationRequest request = new UpdateFlowGoodsRelationRequest();
        request.setId(193L);
        request.setGoodsRelationLabel(2);
        request.setYlGoodsId(3189L);
        request.setYlGoodsName("（测试）连花清瘟片");
        request.setYlGoodsSpecifications("0.35g*12片*1板");
        request.setOpUserId(1L);
        flowGoodsRelationService.edit(request);
        System.out.println(">>>>> edit end");
    }

    @Test
    public void saveTest() {
        SaveOrUpdateFlowGoodsRelationRequest request = new SaveOrUpdateFlowGoodsRelationRequest();
        request.setEid(4333L);
        request.setEname("太极集团重庆桐君阁医药批发有限公司");
        request.setGoodsName("通心络胶囊11");
        request.setGoodsSpecifications("0.26gx40粒");
        request.setYlGoodsId(1L);
        request.setYlGoodsName("通心络胶囊");
        request.setYlGoodsSpecifications("0.26gx40粒");
        request.setGoodsInSnList(Arrays.asList("10185", "10186"));
        flowGoodsRelationService.saveOrUpdateByGoodsAuditApproved(request);
    }

    @Test
    public void saveOrUpdateByFlowSyncTest() {
        QueryFlowGoodsRelationYlGoodsIdRequest ylGoodsIdRequest = new QueryFlowGoodsRelationYlGoodsIdRequest();
        ylGoodsIdRequest.setEid(4333L);
        ylGoodsIdRequest.setEname("太极集团重庆桐君阁医药批发有限公司");
        ylGoodsIdRequest.setGoodsInSn("123409");
        ylGoodsIdRequest.setGoodsName("参松养心胶囊");
        ylGoodsIdRequest.setGoodsSpecifications("0.4gx36粒");
        flowGoodsRelationService.saveOrUpdateByFlowSync(ylGoodsIdRequest);

    }

    @Test
    public void getListByEidAndGoodsNameTest() {
        List<FlowGoodsRelationDO> list = flowGoodsRelationService.getListByEidAndGoodsName(72L, "连花");
    }

    @Test
    public void handleFlowGoodsRelationMqSyncTest() {
        Long flowGoodsRelationId = 7463L;
        Long oldYlGoodsId = 2202L;
        Long opUserId = 1L;
        int result = flowGoodsRelationHandler.handleFlowGoodsRelationMqSync(flowGoodsRelationId, opUserId);
    }
}
