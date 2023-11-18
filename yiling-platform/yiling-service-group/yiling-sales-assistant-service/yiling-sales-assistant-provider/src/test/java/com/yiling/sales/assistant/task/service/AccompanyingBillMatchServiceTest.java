package com.yiling.sales.assistant.task.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.sales.assistant.BaseTest;

/**
 * @author: gxl
 * @date: 2023/2/2
 */
public class AccompanyingBillMatchServiceTest extends BaseTest {
    @Autowired
    AccompanyingBillMatchService accompanyingBillMatchService;
    @Autowired
    TaskAccompanyingBillService taskAccompanyingBillService;
    @Test
    public void test1(){
        accompanyingBillMatchService.billFlowMatchTimer();
    }

    @Test
    public void test2(){
        taskAccompanyingBillService.handleTaskAccompanyingBill(12L);
    }
}