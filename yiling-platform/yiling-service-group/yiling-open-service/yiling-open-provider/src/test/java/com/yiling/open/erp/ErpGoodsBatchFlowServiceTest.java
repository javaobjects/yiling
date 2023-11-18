package com.yiling.open.erp;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.yiling.open.BaseTest;
import com.yiling.open.erp.dto.ErpGoodsBatchFlowDTO;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.handler.ErpGoodsBatchFlowHandler;
import com.yiling.open.erp.service.ErpGoodsBatchFlowService;

/**
 * @author: houjie.sun
 * @date: 2021/9/2
 */
public class ErpGoodsBatchFlowServiceTest extends BaseTest {

    @Autowired
    ErpGoodsBatchFlowService erpGoodsBatchFlowService;
    @Autowired
    private ErpGoodsBatchFlowHandler erpGoodsBatchFlowHandler;

    @Test
    public void Test1() {
        erpGoodsBatchFlowService.syncGoodsBatchFlow();
    }

    @Test
    public void Test2() {
        // dev
        Long tag = 1246L;
        String msg = "[{\"cnt\":1,\"dataMd5\":\"EF50493382BA938EBBB45C4E0A24D81D\",\"failedCount\":0,\"flowCacheFileMd5\":\"927703F88FD5F6A707DFFAC0ED82F6B2\",\"gb_id_no\":\"36fc2d7270e3460faf6e68277be0916d\",\"gb_end_time\":\"2023-02-28\",\"gb_batch_no\":\"B2009083\",\"gb_produce_time\":\"2020-09-09\",\"gb_license\":\"国药准字Z20040063\",\"gb_manufacturer\":\"石家庄以岭药业股份有限公司\",\"gb_name\":\"(兴)连花清瘟胶囊_以岭_0.35g*12粒*3板\",\"gb_number\":12.0000,\"gb_produce_address\":\"\",\"gb_specifications\":\"0.35G*12粒*3板\",\"gb_unit\":\"盒\",\"in_sn\":\"\",\"opTime\":1657274179000,\"oper_type\":1,\"su_dept_no\":\"002\",\"su_id\":1246,\"taskNo\":\"40000008\"}]";
        // test in_sn:000000000000133498
//        Long tag = 73L;
//        String msg = "[{\"cnt\":1,\"dataMd5\":\"927703F88FD5F6A707DFFAC0ED82F6B2\",\"failedCount\":0,\"flowCacheFileMd5\":\"927703F88FD5F6A707DFFAC0ED82F6B2\",\"gb_id_no\":\"12994e41238641b78b33a9f4cc4db3011\",\"gb_end_time\":\"2022-06-23 10:00:00\",\"gb_batch_no\":\"2010040\",\"gb_end_time\":\"2023-03-31 00:00:00\",\"gb_license\":\"国药准字Z20040063\",\"gb_manufacturer\":\"石家庄以岭药业股份有限公司\",\"gb_name\":\"连花清瘟胶囊\",\"gb_number\":10.0,\"gb_produce_address\":\"石家庄11\",\"gb_produce_time\":\"2020-10-25T00:00:00\",\"gb_specifications\":\"0.35gX24s\",\"gb_unit\":\"盒\",\"in_sn\":\"12914\",\"opTime\":1657274179000,\"oper_type\":1,\"su_dept_no\":\"101\",\"su_id\":73,\"taskNo\":\"40000008\"}]";
        erpGoodsBatchFlowHandler.handleGoodsBatchFlowMqSync(tag, ErpTopicName.ErpGoodsBatchFlow.getMethod(), JSONArray.parseArray(msg, ErpGoodsBatchFlowDTO.class));
    }
}
