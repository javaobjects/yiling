package com.yiling.open.erp;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.yiling.open.BaseTest;
import com.yiling.open.erp.dto.ErpGoodsBatchDTO;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.enums.OperTypeEnum;
import com.yiling.open.erp.handler.ErpGoodsBatchHandler;
import com.yiling.open.erp.service.ErpGoodsBatchService;
import com.yiling.open.erp.validation.ValidationUtils;
import com.yiling.open.erp.validation.entity.ValidationResult;

public class ErpGoodsBatchTest extends BaseTest {

    @Autowired
    private ErpGoodsBatchService erpGoodsBatchService;

    @Autowired
    private ErpGoodsBatchHandler erpGoodsBatchHandler;

//    @Autowired
//    private ErpGoodsDao erpGoodsDao;

    @Test
    public void ErpGoodsHandlerTest() {
        erpGoodsBatchService.syncEasGoodsBatch();
    }

    @Test
    public void ErpGoodsHandlerTest2() {
        erpGoodsBatchService.syncGoodsBatch();
    }


    @Test
    public void ErpGoodsHandlerTest1() {
        String json = "[{\"operType\":2,\"dataMd5\":\"FE01DA776E7398F69855E9FFA339DE61\",\"erpPrimaryKey\":\"01-02.01.004.001\",\"failedCount\":0,\"gb_batch_no\":\"\",\"gb_id_no\":\"01-02.01.004.001\",\"gb_number\":64347886.0000,\"in_sn\":\"02.01.004.001\",\"su_dept_no\":\"01\",\"su_id\":1,\"taskNo\":\"10000003\"}]";
        erpGoodsBatchHandler.handleGoodsBatchMqSync(1L, ErpTopicName.ErpGoodsBatch.getMethod(), JSONArray.parseArray(json, ErpGoodsBatchDTO.class));
    }

    @Test
    public void test() {
        ErpGoodsBatchDTO erpGoodsBatch = new ErpGoodsBatchDTO();
        erpGoodsBatch.setGbIdNo("123");
        erpGoodsBatch.setOperType(OperTypeEnum.DELETE.getCode());
        // 数据校验
        ValidationResult validationResult = ValidationUtils.validate(erpGoodsBatch);
        if (validationResult.hasError()) {
            System.out.println(">>>>> gbIdNo 为空");
        }
    }

}
