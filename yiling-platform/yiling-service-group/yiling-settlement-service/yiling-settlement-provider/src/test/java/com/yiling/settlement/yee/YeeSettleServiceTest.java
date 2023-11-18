package com.yiling.settlement.yee;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.settlement.BaseTest;
import com.yiling.settlement.yee.service.YeeSettleSyncRecordService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class YeeSettleServiceTest extends BaseTest {


    @Autowired
    YeeSettleSyncRecordService yeeSettleSyncRecordService;




    @Test
    public void test(){
        yeeSettleSyncRecordService.queryYeeSettleRecord("10088793568",null,null);
        System.err.println();
    }

    @Test
    public void test2(){
        yeeSettleSyncRecordService.initData();
        System.err.println();
    }

    @Test
    public void test3(){
        yeeSettleSyncRecordService.syncMemberSettleForToday();
        System.err.println();
    }






}
