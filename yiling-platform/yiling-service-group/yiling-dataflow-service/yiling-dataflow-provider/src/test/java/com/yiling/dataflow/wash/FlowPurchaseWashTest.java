package com.yiling.dataflow.wash;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.BaseTest;
import com.yiling.dataflow.wash.dto.request.QueryFlowPurchaseWashPageRequest;
import com.yiling.dataflow.wash.entity.FlowPurchaseWashDO;
import com.yiling.dataflow.wash.handler.FlowPurchaseWashHandler;
import com.yiling.dataflow.wash.service.FlowPurchaseWashService;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2023/3/10
 */
@Slf4j
public class FlowPurchaseWashTest extends BaseTest {

    @Autowired
    private FlowPurchaseWashService flowPurchaseWashService;

    @Autowired
    private FlowPurchaseWashHandler flowPurchaseWashHandler;

    @Test
    public void washTest() {
        flowPurchaseWashHandler.wash(9L);
    }

    @Test
    public void ListPageTest() {
        QueryFlowPurchaseWashPageRequest request = new QueryFlowPurchaseWashPageRequest();
        request.setCurrent(1);
        request.setSize(10);
        request.setFmwtId(9L);
        Page<FlowPurchaseWashDO> page = flowPurchaseWashService.listPage(request);
        System.out.println(JSONUtil.toJsonStr(page));
    }
}
