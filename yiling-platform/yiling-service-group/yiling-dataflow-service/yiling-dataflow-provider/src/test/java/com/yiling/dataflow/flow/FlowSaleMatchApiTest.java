package com.yiling.dataflow.flow;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.dataflow.BaseTest;
import com.yiling.dataflow.flow.api.FlowSaleMatchApi;
import com.yiling.dataflow.flow.dto.FlowSaleMatchResultDTO;
import com.yiling.dataflow.flow.dto.request.FlowSaleMatchRequest;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2023/2/3
 */
@Slf4j
public class FlowSaleMatchApiTest extends BaseTest {

    @Autowired
    public FlowSaleMatchApi flowSaleMatchApi;

    @Test
    public void matchTest() {
        FlowSaleMatchRequest request = new FlowSaleMatchRequest();
        request.setEid(1236L);
        request.setSoNo("1010224022610");
        FlowSaleMatchResultDTO result = flowSaleMatchApi.match(request);

        System.out.println(JSONUtil.toJsonStr(result));
    }
}
