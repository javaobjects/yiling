package com.yiling.dataflow.sale;

import com.alibaba.fastjson.JSONObject;
import com.yiling.dataflow.BaseTest;
import com.yiling.dataflow.sale.api.SaleTargetApi;
import com.yiling.dataflow.sale.dto.request.SaveSaleTargetRequest;
import org.junit.Test;

import javax.annotation.Resource;

public class SaleDepartmentSubTest extends BaseTest {
    @Resource
    private SaleTargetApi saleTargetApi;

    @Test
    public void  saleTargetSaveTest(){
        String json="{\"departmentTargets\":[{\"id\":0,\"departId\":1184,\"departName\":\"上海\",\"lastTarget\":\"3423\",\"lastTargetRatio\":36.08,\"currentTarget\":\"360800\",\"currentTargetRatio\":36.08,\"currentIncrease\":357377},{\"id\":0,\"departId\":12749,\"departName\":\"江苏\",\"lastTarget\":\"43\",\"lastTargetRatio\":0.45,\"currentTarget\":\"4500\",\"currentTargetRatio\":0.45,\"currentIncrease\":4457},{\"id\":0,\"departId\":13810,\"departName\":\"湖北\",\"lastTarget\":\"34\",\"lastTargetRatio\":0.36,\"currentTarget\":\"3600\",\"currentTargetRatio\":0.36,\"currentIncrease\":3566},{\"id\":0,\"departId\":13812,\"departName\":\"吉林\",\"lastTarget\":\"34\",\"lastTargetRatio\":0.36,\"currentTarget\":\"3600\",\"currentTargetRatio\":0.36,\"currentIncrease\":3566},{\"id\":0,\"departId\":13817,\"departName\":\"辽宁\",\"lastTarget\":\"463\",\"lastTargetRatio\":4.88,\"currentTarget\":\"48800\",\"currentTargetRatio\":4.88,\"currentIncrease\":48337},{\"id\":0,\"departId\":11761,\"departName\":\"湖北\",\"lastTarget\":\"435\",\"lastTargetRatio\":4.59,\"currentTarget\":\"45900\",\"currentTargetRatio\":4.59,\"currentIncrease\":45465},{\"id\":0,\"departId\":11762,\"departName\":\"江西\",\"lastTarget\":\"45\",\"lastTargetRatio\":0.47,\"currentTarget\":\"4700\",\"currentTargetRatio\":0.47,\"currentIncrease\":4655},{\"id\":0,\"departId\":12945,\"departName\":\"江苏\",\"lastTarget\":\"455\",\"lastTargetRatio\":4.8,\"currentTarget\":\"48000\",\"currentTargetRatio\":4.8,\"currentIncrease\":47545},{\"id\":0,\"departId\":13327,\"departName\":\"东区\",\"lastTarget\":\"4554\",\"lastTargetRatio\":48.01,\"currentTarget\":\"480099\",\"currentTargetRatio\":48.01,\"currentIncrease\":475545}],\"name\":\"2023年销售指标\",\"targetYear\":\"2023\",\"saleAmount\":\"999999\"}";
        SaveSaleTargetRequest request =JSONObject.parseObject(json,SaveSaleTargetRequest.class);
        saleTargetApi.save(request);
    }
}
