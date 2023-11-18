package com.yiling.open.erp;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.yiling.open.BaseTest;
import com.yiling.open.erp.dto.ErpGoodsCustomerPriceDTO;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.handler.ErpGoodsCustomerPriceHandler;
import com.yiling.open.erp.service.ErpGoodsCustomerPriceService;

public class ErpGoodsPriceTest extends BaseTest {

    @Autowired
    private ErpGoodsCustomerPriceService erpGoodsCustomerPriceService;

    @Autowired
    private ErpGoodsCustomerPriceHandler erpGoodsCustomerPriceHandler;
//    @Autowired
//    private ErpGoodsDao erpGoodsDao;

    @Test
    public void ErpGoodsHandlerTest() {
        erpGoodsCustomerPriceService.syncEasGoodsCustomerPrice();
    }
    @Test
    public void ErpGoodsHandlerTest3() {
        erpGoodsCustomerPriceService.syncDayunheGoodsCustomerPrice();
    }

    @Test
    public void ErpGoodsHandlerTest2() {
        erpGoodsCustomerPriceService.syncGoodsCustomerPrice();
    }

    //[{"dataMd5":"07D7C28D410A6CE492AE698670AD74F4","erpPrimaryKey":"010531.01LSB.KNSS02.01.004.001$$$01","failedCount":0,"gcp_id_no":"010531.01LSB.KNSS02.01.004.001","in_sn":"02.01.004.001","inner_code":"0531.01LSB.KNSS","oper_type":1,"price":10.65,"su_dept_no":"01","su_id":1,"taskNo":"10000009"}]
    @Test
    public void ErpGoodsHandlerTest1() {
        String json = "[{\"dataMd5\":\"26118033F44E5F76D2101299DC5F7AC6\",\"erpPrimaryKey\":\"027.XL02.01.004.005\",\"failedCount\":0,\"gcp_id_no\":\"027.XL02.01.004.005\",\"in_sn\":\"02.01.004.005\",\"inner_code\":\"027.XL\",\"oper_type\":2,\"price\":15.77777777884400,\"su_dept_no\":\"\",\"su_id\":4,\"taskNo\":\"10000009\"}]";
        erpGoodsCustomerPriceHandler.handleGoodsCustomerPriceMqSync(4L, ErpTopicName.ErpGoodsPrice.getMethod(), JSONArray.parseArray(json, ErpGoodsCustomerPriceDTO.class));
    }

}
