package com.yiling.user.integral;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.yiling.user.BaseTest;
import com.yiling.user.integral.dto.GenerateMultipleConfigDTO;
import com.yiling.user.integral.dto.request.QueryIntegralGiveMatchRuleRequest;
import com.yiling.user.integral.service.IntegralOrderGiveConfigService;

/**
 * 积分 Test
 *
 * @author: lun.yu
 * @date: 2023-03-16
 */
public class IntegralServiceTest extends BaseTest {

    @Autowired
    private IntegralOrderGiveConfigService integralOrderGiveConfigService;

    /**
     * 自动匹配订单送积分倍数
     */
    @Test
    public void autoMatchRule() {
        QueryIntegralGiveMatchRuleRequest matchRuleRequest = new QueryIntegralGiveMatchRuleRequest();
        matchRuleRequest.setEid(212L);
        matchRuleRequest.setUid(220L);
        matchRuleRequest.setGoodsId(2802L);
        matchRuleRequest.setStandardId(171075L);
        matchRuleRequest.setSpecificationId(41556L);
        matchRuleRequest.setPaymentMethod(1);
        matchRuleRequest.setOrderNO("D20230316172818169402");
        GenerateMultipleConfigDTO multipleConfigDTO = integralOrderGiveConfigService.autoMatchRule(matchRuleRequest);
        System.out.println(JSONObject.toJSONString(multipleConfigDTO));
    }


}
