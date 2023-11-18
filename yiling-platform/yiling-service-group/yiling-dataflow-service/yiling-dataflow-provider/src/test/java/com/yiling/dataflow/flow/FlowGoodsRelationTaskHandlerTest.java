package com.yiling.dataflow.flow;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.dataflow.BaseTest;
import com.yiling.dataflow.flow.handler.FlowGoodsRelationTaskHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: houjie.sun
 * @date: 2022/12/26
 */
@Slf4j
public class FlowGoodsRelationTaskHandlerTest extends BaseTest {

    @Autowired
    private FlowGoodsRelationTaskHandler flowGoodsRelationTaskHandler;

    @Test
    public void flowSaleYlGoodsIdSendMsgTest() throws Exception {
        List<Long> flowSaleIds = new ArrayList<>();
        // eid = 64, goodsInSn = 12004211|500
        flowSaleIds.add(2611364L);
        // eid = 40, goodsInSn = 36019265
        flowSaleIds.add(1320808L);

        flowGoodsRelationTaskHandler.flowSaleYlGoodsIdSendMsg(flowSaleIds);
    }
}
