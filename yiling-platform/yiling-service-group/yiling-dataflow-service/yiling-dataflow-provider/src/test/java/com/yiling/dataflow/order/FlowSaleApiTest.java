package com.yiling.dataflow.order;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.BaseTest;
import com.yiling.dataflow.order.api.FlowSaleApi;
import com.yiling.dataflow.order.dto.FlowSaleDTO;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseListPageRequest;

import cn.hutool.core.date.DateUtil;

/**
 * @author fucheng.bai
 * @date 2023/2/7
 */
public class FlowSaleApiTest extends BaseTest {

    @Autowired
    private FlowSaleApi flowSaleApi;

    @Test
    public void pageTest() {
        QueryFlowPurchaseListPageRequest request = new QueryFlowPurchaseListPageRequest();
        request.setEid(1236L);
        request.setStartTime(DateUtil.beginOfDay(DateUtil.parse("2022-12-01", "yyyy-MM-dd")));
        request.setEndTime(DateUtil.endOfDay(DateUtil.parse("2023-01-01", "yyyy-MM-dd")));
        request.setTimeType(0);
//        request.setEnterpriseName(form.getEnterpriseName());
//        request.setGoodsName(form.getGoodsName());
//        request.setSpecifications(form.getGoodsSpec());

        Page<FlowSaleDTO> page = flowSaleApi.page(request);
        System.out.println(page.getRecords().size());
    }
}
