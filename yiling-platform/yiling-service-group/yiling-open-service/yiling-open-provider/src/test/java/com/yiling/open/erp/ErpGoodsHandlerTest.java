package com.yiling.open.erp;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.yiling.open.BaseTest;
import com.yiling.open.erp.dto.ErpGoodsDTO;
import com.yiling.open.erp.handler.ErpGoodsHandler;
import com.yiling.open.erp.service.ErpGoodsService;

public class ErpGoodsHandlerTest extends BaseTest {

    @Autowired
    private ErpGoodsHandler erpGoodsHandler;

    @Autowired
    private ErpGoodsService erpGoodsService;

//    @Autowired
//    private ErpGoodsDao erpGoodsDao;

    @Test
    public void ErpGoodsHandlerTest() {
        String body="[\n" +
                "\t{\n" +
                "\t\t\"goods_status\": 1,\n" +
                "\t\t\"oper_type\": 1,\n" +
                "\t\t\"alias_name\": \"\",\n" +
                "\t\t\"big_package\": 400,\n" +
                "\t\t\"can_split\": 1,\n" +
                "\t\t\"common_name\": \"通心络胶囊\",\n" +
                "\t\t\"dataMd5\": \"C5AEBD8EBA862FFE51FFC88960639F9F\",\n" +
                "\t\t\"erpPrimaryKey\": \"30174\",\n" +
                "\t\t\"failedCount\": 0,\n" +
                "\t\t\"in_sn\": \"30174\",\n" +
                "\t\t\"license_no\": \"国药准字Z19980015\",\n" +
                "\t\t\"manufacturer\": \"石家庄以岭药业股份有限公司\",\n" +
                "\t\t\"manufacturer_code\": \"\",\n" +
                "\t\t\"middle_package\": 1,\n" +
                "\t\t\"name\": \"通心络胶囊\",\n" +
                "\t\t\"price\": 10.6577,\n" +
                "\t\t\"sn\": \"30174\",\n" +
                "\t\t\"specifications\": \"0.26g*30粒(超微粉化)\",\n" +
                "\t\t\"su_dept_no\": \"\",\n" +
                "\t\t\"su_id\": 43,\n" +
                "\t\t\"taskNo\": \"10000002\",\n" +
                "\t\t\"unit\": \"盒\"\n" +
                "\t}\n" +
                "]";
        erpGoodsHandler.handleGoodsMqSync(43L,"erp_goods", JSONArray.parseArray(body, ErpGoodsDTO.class));
    }
    @Test
    public void ErpGoodsHandlerTest1() {
        erpGoodsService.syncGoods();
    }

}
