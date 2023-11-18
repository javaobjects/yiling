package com.yiling.dataflow.wash;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.BaseTest;
import com.yiling.dataflow.wash.dto.request.QueryFlowGoodsBatchDetailWashPageRequest;
import com.yiling.dataflow.wash.entity.FlowGoodsBatchDetailWashDO;
import com.yiling.dataflow.wash.handler.FlowGoodsBatchDetailWashHandler;
import com.yiling.dataflow.wash.service.FlowGoodsBatchDetailWashService;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2023/3/10
 */
@Slf4j
public class FlowGoodsBatchDetailWashTest extends BaseTest {

    @Autowired
    private FlowGoodsBatchDetailWashService flowGoodsBatchDetailWashService;

    @Autowired
    private FlowGoodsBatchDetailWashHandler flowGoodsBatchDetailWashHandler;

    @Test
    public void washTest() {
        flowGoodsBatchDetailWashHandler.wash(10L);
    }

    @Test
    public void listPageTest() {
        QueryFlowGoodsBatchDetailWashPageRequest request = new QueryFlowGoodsBatchDetailWashPageRequest();
        request.setFmwtId(10L);
        request.setCurrent(1);
        request.setSize(10);
        Page<FlowGoodsBatchDetailWashDO> page = flowGoodsBatchDetailWashService.listPage(request);
        System.out.println(JSONUtil.toJsonStr(page));
    }
}
