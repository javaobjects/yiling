package com.yiling.dataflow.wash;

import com.alibaba.fastjson.JSON;
import com.yiling.dataflow.BaseTest;
import com.yiling.dataflow.wash.dto.request.SaveOrUpdateUnlockSaleRuleRequest;
import com.yiling.dataflow.wash.dto.request.SaveUnlockSaleDepartmentRequest;
import com.yiling.dataflow.wash.entity.UnlockSaleRuleDO;
import com.yiling.dataflow.wash.service.UnlockSaleRuleService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author fucheng.bai
 * @date 2023/3/9
 */
@Slf4j
public class UnlockSaleRuleTest extends BaseTest {

    @Autowired
    private UnlockSaleRuleService UnlockSaleRuleService;

    @Test
    public void listPageTest() {
        UnlockSaleRuleDO unlockSaleRuleDO = UnlockSaleRuleService.getById(12L);
        System.out.println(JSON.toJSONString(unlockSaleRuleDO));
    }

    @Test
    public void listPageTest1() {
        SaveOrUpdateUnlockSaleRuleRequest request = new SaveOrUpdateUnlockSaleRuleRequest();
        request.setId(14L);
        SaveUnlockSaleDepartmentRequest saveUnlockSaleDepartmentRequest=new SaveUnlockSaleDepartmentRequest();
        saveUnlockSaleDepartmentRequest.setDepartmentName("111");
        saveUnlockSaleDepartmentRequest.setBusinessDepartmentName("222");
        request.setSaveUnlockSaleDepartmentRequest(saveUnlockSaleDepartmentRequest);
        UnlockSaleRuleService.saveOrUpdate(request);
    }
}
