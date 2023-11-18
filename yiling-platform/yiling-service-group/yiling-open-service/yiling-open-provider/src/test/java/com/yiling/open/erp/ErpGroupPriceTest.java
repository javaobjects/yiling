package com.yiling.open.erp;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.yiling.open.BaseTest;
import com.yiling.open.erp.dto.ErpGoodsGroupPriceDTO;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.handler.ErpGoodsGroupPriceHandler;

public class ErpGroupPriceTest extends BaseTest {

    @Autowired
    private ErpGoodsGroupPriceHandler erpGoodsGroupPriceHandler;

    @Test
    public void ErpGoodsHandlerTest1() {
        String json = "[{\"dataMd5\":\"574B1ED8565B98996AC461223A658063\",\"erpPrimaryKey\":\"1\",\"failedCount\":0,\"ggp_id_no\":\"1\",\"group_name\":\"分组1\",\"in_sn\":\"24833\",\"opTime\":1639560095416,\"oper_type\":1,\"price\":11.89898988080800001,\"su_dept_no\":\"\",\"su_id\":43,\"taskNo\":\"10000010\"}]";
        erpGoodsGroupPriceHandler.handleGoodsGroupPriceMqSync(43L, ErpTopicName.ErpGroupPrice.getMethod(), JSONArray.parseArray(json, ErpGoodsGroupPriceDTO.class));
    }

}
