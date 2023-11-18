package com.yiling.open.erp;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.yiling.open.BaseTest;
import com.yiling.open.erp.dto.ErpOrderSendDTO;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.handler.ErpOrderSendHandler;
import com.yiling.open.erp.service.ErpOrderSendService;

public class ErpOrderSendHandlerTest extends BaseTest {

    @Autowired
    private ErpOrderSendService erpOrderSendService;

    @Autowired
    private ErpOrderSendHandler erpOrderSendHandler;

    @Test
    public void ErpOrderSendTest() {
        erpOrderSendService.syncOrderSend();
    }

    @Test
    public void ErpOrderSendTest1() {
        String json = "[{\"dataMd5\":\"EE9BA30BCDEE8C64521AFC77594EE0F9\",\"delivery_number\":\"XSCK-01-202112-0045\",\"effective_time\":\"2022-12-01 00:00:00\",\"erpPrimaryKey\":\"896$$$01\",\"failedCount\":0,\"opTime\":1640164288509,\"oper_type\":1,\"order_detail_id\":2895,\"order_id\":2266,\"osi_id\":\"896\",\"product_time\":\"2021-01-01 00:00:00\",\"send_batch_no\":\"C20210927\",\"send_num\":10.0000000000,\"send_time\":1640102400000,\"send_type\":3,\"su_dept_no\":\"01\",\"su_id\":1,\"taskNo\":\"10000004\"},{\"dataMd5\":\"DFAE619F7A751E594FFC9E2886AA70AE\",\"delivery_number\":\"XSCK-01-202112-0045\",\"effective_time\":\"2022-12-01 00:00:00\",\"erpPrimaryKey\":\"897$$$01\",\"failedCount\":0,\"opTime\":1640164288512,\"oper_type\":1,\"order_detail_id\":2895,\"order_id\":2266,\"osi_id\":\"897\",\"product_time\":\"2021-01-01 00:00:00\",\"send_batch_no\":\"cs001\",\"send_num\":20.0000000000,\"send_time\":1640102400000,\"send_type\":3,\"su_dept_no\":\"01\",\"su_id\":1,\"taskNo\":\"10000004\"},{\"dataMd5\":\"3CC4B53A94F74BB62CB41FAAE821614A\",\"delivery_number\":\"XSCK-01-202112-0045\",\"effective_time\":\"2022-12-01 00:00:00\",\"erpPrimaryKey\":\"899$$$01\",\"failedCount\":0,\"opTime\":1640164288516,\"oper_type\":1,\"order_detail_id\":2895,\"order_id\":2266,\"osi_id\":\"899\",\"product_time\":\"2021-01-01 00:00:00\",\"send_batch_no\":\"cs001\",\"send_num\":20.0000000000,\"send_time\":1640102400000,\"send_type\":1,\"su_dept_no\":\"01\",\"su_id\":1,\"taskNo\":\"10000004\"},{\"dataMd5\":\"5731E53F2A473C285157E5520CCB6A5A\",\"delivery_number\":\"XSCK-01-202112-0045\",\"effective_time\":\"2022-12-01 00:00:00\",\"erpPrimaryKey\":\"898$$$01\",\"failedCount\":0,\"opTime\":1640164288514,\"oper_type\":1,\"order_detail_id\":2895,\"order_id\":2266,\"osi_id\":\"898\",\"product_time\":\"2021-01-01 00:00:00\",\"send_batch_no\":\"C20210927\",\"send_num\":15.0000000000,\"send_time\":1640102400000,\"send_type\":1,\"su_dept_no\":\"01\",\"su_id\":1,\"taskNo\":\"10000004\"},{\"dataMd5\":\"D13B229970123364C6E349FABE3BC4A8\",\"delivery_number\":\"\",\"erpPrimaryKey\":\"主键2209$$$01\",\"failedCount\":0,\"opTime\":1640164288524,\"oper_type\":2,\"order_detail_id\":0,\"order_id\":2209,\"osi_id\":\"主键2209\",\"send_batch_no\":\"\",\"send_num\":0E-10,\"send_time\":1640164286000,\"send_type\":2,\"su_dept_no\":\"01\",\"su_id\":1,\"taskNo\":\"10000004\"},{\"dataMd5\":\"E099F4C62099BA50E46093F8CB1DAA32\",\"delivery_number\":\"\",\"erpPrimaryKey\":\"主键2187$$$01\",\"failedCount\":0,\"opTime\":1640164288521,\"oper_type\":2,\"order_detail_id\":0,\"order_id\":2187,\"osi_id\":\"主键2187\",\"send_batch_no\":\"\",\"send_num\":0E-10,\"send_time\":1640164286000,\"send_type\":2,\"su_dept_no\":\"01\",\"su_id\":1,\"taskNo\":\"10000004\"},{\"dataMd5\":\"19E0519CB60E062F92DD92798D83EA09\",\"delivery_number\":\"\",\"erpPrimaryKey\":\"主键2196$$$01\",\"failedCount\":0,\"opTime\":1640164288522,\"oper_type\":2,\"order_detail_id\":0,\"order_id\":2196,\"osi_id\":\"主键2196\",\"send_batch_no\":\"\",\"send_num\":0E-10,\"send_time\":1640164286000,\"send_type\":2,\"su_dept_no\":\"01\",\"su_id\":1,\"taskNo\":\"10000004\"},{\"dataMd5\":\"CD7EEC7CF9ED333D04669A10BE7585F7\",\"delivery_number\":\"\",\"erpPrimaryKey\":\"主键2168$$$01\",\"failedCount\":0,\"opTime\":1640164288519,\"oper_type\":2,\"order_detail_id\":0,\"order_id\":2168,\"osi_id\":\"主键2168\",\"send_batch_no\":\"\",\"send_num\":0E-10,\"send_time\":1640164286000,\"send_type\":2,\"su_dept_no\":\"01\",\"su_id\":1,\"taskNo\":\"10000004\"},{\"dataMd5\":\"89E0F58A524598F225B5835C501FA830\",\"delivery_number\":\"\",\"erpPrimaryKey\":\"主键2161$$$01\",\"failedCount\":0,\"opTime\":1640164288518,\"oper_type\":2,\"order_detail_id\":0,\"order_id\":2161,\"osi_id\":\"主键2161\",\"send_batch_no\":\"\",\"send_num\":0E-10,\"send_time\":1640164286000,\"send_type\":2,\"su_dept_no\":\"01\",\"su_id\":1,\"taskNo\":\"10000004\"},{\"dataMd5\":\"9251645E83A2D8F84BC69F78C931A53A\",\"delivery_number\":\"\",\"erpPrimaryKey\":\"主键2208$$$01\",\"failedCount\":0,\"opTime\":1640164288523,\"oper_type\":2,\"order_detail_id\":0,\"order_id\":2208,\"osi_id\":\"主键2208\",\"send_batch_no\":\"\",\"send_num\":0E-10,\"send_time\":1640164286000,\"send_type\":2,\"su_dept_no\":\"01\",\"su_id\":1,\"taskNo\":\"10000004\"}]";
        erpOrderSendHandler.handleOrderSendMqSync(1L, ErpTopicName.ErpOrderSend.getMethod(), JSONArray.parseArray(json, ErpOrderSendDTO.class));
    }
}
