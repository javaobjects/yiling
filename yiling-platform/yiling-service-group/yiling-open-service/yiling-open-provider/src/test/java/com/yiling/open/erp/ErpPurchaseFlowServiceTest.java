package com.yiling.open.erp;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.JSONArray;
import com.yiling.open.BaseTest;
import com.yiling.open.config.ErpFlowOrderSourceConfig;
import com.yiling.open.erp.dto.ErpPurchaseFlowDTO;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.handler.ErpPurchaseFlowHandler;
import com.yiling.open.erp.service.ErpPurchaseFlowService;

/**
 * @author: houjie.sun
 * @date: 2021/9/2
 */
public class ErpPurchaseFlowServiceTest extends BaseTest {

    @Autowired
    ErpPurchaseFlowService erpPurchaseFlowService;
    @Autowired
    private ErpPurchaseFlowHandler erpPurchaseFlowHandler;
    @Autowired
    private ErpFlowOrderSourceConfig erpFlowOrderSourceConfig;

    @Test
    public void Test1() {
        erpPurchaseFlowService.synPurchaseFlow();
    }

    @Test
    public void Test2() {
        Long tag = 1246L;
        // dev
        String msg = "[{\"cnt\":1,\"dataMd5\":\"C86ACB431E5964C2A66753C7FFFB64FE\",\"enterprise_inner_code\":\"123\",\"enterprise_name\":\"山东康诺盛世医药有限公司\",\"erpPrimaryKey\":\"2f874fb617097b18e18da00018c81325\",\"failedCount\":0,\"goods_in_sn\":\"01.02.03\",\"goods_name\":\"连花清瘟胶囊\",\"opTime\":1675319400,\"order_time\":\"2022-07-01 00:00:00\",\"po_batch_no\":\"A001\",\"po_effective_time\":\"1970-01-01 00:00:00\",\"po_license\":\"\",\"po_manufacturer\":\"石家庄以岭药业股份有限公司\",\"po_no\":\"004\",\"po_price\":12.00,\"po_product_time\":\"2022-05-30 10:30:00\",\"po_quantity\":7.00,\"po_source\":\"其它渠道采购\",\"po_specifications\":\"0.35g*12*2\",\"po_time\":\"2022-07-01 11:30:00\",\"po_unit\":\"盒\",\"su_dept_no\":\"002\",\"su_id\":1246,\"taskNo\":\"40000006\",\"oper_type\":1,\"po_id\":\"2f874fb617097b18e18da00018c81325\", \"control_id\":0}]";
        // test
//        Long tag = 4333L;
//        String msg = "[{\"cnt\":1,\"control_id\": 1,\"dataMd5\":\"F7D6D873E978B0CA5E4B6EAE6CBAD332\",\"enterprise_inner_code\":\"kh0013\",\"enterprise_name\":\"河北大运河医药物流有限公司\",\"erpPrimaryKey\":\"fdd169dac5d17b0027e2e0e9cd971377\",\"failedCount\":0,\"goods_in_sn\":\"80808\",\"goods_name\":\"连花清瘟胶囊1\",\"opTime\":1664441049074,\"oper_type\":3,\"order_time\":\"2022-09-25\",\"po_batch_no\":\"ph-0013\",\"po_effective_time\":\"2024-02-24 00:00:00\",\"po_id\":\"fdd169dac5d17b0027e2e0e9cd971377\",\"po_license\":\"国药准字Z20040063\",\"po_manufacturer\":\"石家庄以岭3\",\"po_no\":\"RK003\",\"po_price\":10.22,\"po_product_time\":\"2022-02-24 00:00:00\",\"po_quantity\":-10,\"po_source\":\"POP采购\",\"po_specifications\":\"0.35g*24S\",\"po_time\":\"2022-09-11 00:00:00\",\"po_unit\":\"盒10\",\"su_dept_no\":\"\",\"su_id\":4333,\"taskNo\":\"40000006\"}]";
        erpPurchaseFlowHandler.handlePurchaseFlowMqSync(tag, ErpTopicName.ErpPurchaseFlow.getMethod(), JSONArray.parseArray(msg, ErpPurchaseFlowDTO.class));
    }
}
