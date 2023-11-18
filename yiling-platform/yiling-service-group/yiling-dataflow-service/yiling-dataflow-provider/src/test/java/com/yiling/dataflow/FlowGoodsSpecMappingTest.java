package com.yiling.dataflow;

import com.yiling.dataflow.order.api.FlowGoodsSpecMappingApi;
import com.yiling.dataflow.order.dto.FlowGoodsSpecMappingDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author fucheng.bai
 * @date 2022/7/18
 */
@Slf4j
public class FlowGoodsSpecMappingTest extends BaseTest {

    @Autowired
    private FlowGoodsSpecMappingApi flowGoodsSpecMappingApi;

    @Test
    public void findByGoodsNameAndSpecTest() {
        FlowGoodsSpecMappingDTO flowGoodsSpecMappingDTO = flowGoodsSpecMappingApi.findByGoodsNameAndSpec("!健胃消食片", "0.5g*40片","");
        System.out.println(flowGoodsSpecMappingDTO.getId());
        System.out.println(flowGoodsSpecMappingDTO);
    }

    @Test
    public void updateRecommendInfoByGoodsNameAndSpecTest() {
        flowGoodsSpecMappingApi.updateRecommendInfoByGoodsNameAndSpec();
    }
}
