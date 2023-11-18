package com.yiling.dataflow.statistics;

import java.util.ArrayList;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.dataflow.BaseTest;
import com.yiling.dataflow.statistics.service.FlowDistributionEnterpriseService;

import cn.hutool.core.collection.ListUtil;

/**
 * @author: houjie.sun
 * @date: 2023/2/7
 */
public class FlowDistributionEnterpriseServiceTest extends BaseTest {

    @Autowired
    private FlowDistributionEnterpriseService flowDistributionEnterpriseService;

    @Test
    public void flowDistributionEnterpriseServiceTest(){
        flowDistributionEnterpriseService.getListByCodeList(ListUtil.toList("123"));
    }

    @Test
    public void getCountByEidListTest(){
        Integer count1 = flowDistributionEnterpriseService.getCountByEidList(new ArrayList<>());
        System.out.println("count1:" + count1);
        Integer count2 = flowDistributionEnterpriseService.getCountByEidList(ListUtil.toList(213L, 383L, 386L));
        System.out.println("count2:" + count2);
    }
}
