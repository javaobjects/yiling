package com.yiling.dataflow.flow;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.BaseTest;
import com.yiling.dataflow.order.api.FlowPurchaseApi;
import com.yiling.dataflow.order.dto.FlowPurchaseDTO;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseListPageRequest;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2022/12/12
 */
@Slf4j
public class FlowPurchaseApiTest extends BaseTest {

    @Autowired
    private FlowPurchaseApi flowPurchaseApi;

    @Test
    public void page() {
        QueryFlowPurchaseListPageRequest request = new QueryFlowPurchaseListPageRequest();
//        request.setTimeType(0);
        request.setStartTime(DateUtil.parse("2022-08-01", "yyyy-MM-dd"));
        request.setStartTime(DateUtil.parse("2022-08-31", "yyyy-MM-dd"));
//        request.setSelectYear(2022);

        Page<FlowPurchaseDTO> page = flowPurchaseApi.page(request);
        System.out.println(page);
    }
}
