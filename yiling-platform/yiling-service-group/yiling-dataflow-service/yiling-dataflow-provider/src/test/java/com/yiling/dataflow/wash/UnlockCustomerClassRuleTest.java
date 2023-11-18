package com.yiling.dataflow.wash;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.BaseTest;
import com.yiling.dataflow.wash.api.UnlockCustomerClassRuleApi;
import com.yiling.dataflow.wash.dto.UnlockCustomerClassRuleDTO;
import com.yiling.dataflow.wash.dto.request.QueryUnlockCustomerClassRulePageRequest;
import com.yiling.dataflow.wash.dto.request.SaveOrUpdateUnlockCustomerClassRuleRequest;

import cn.hutool.json.JSONUtil;

/**
 * 非锁客户分类规则Test
 * @author fucheng.bai
 * @date 2023/5/17
 */
public class UnlockCustomerClassRuleTest extends BaseTest {

    @Autowired
    private UnlockCustomerClassRuleApi unlockCustomerClassRuleApi;

    @Test
    public void listPageTest() {
        QueryUnlockCustomerClassRulePageRequest request = new QueryUnlockCustomerClassRulePageRequest();
        request.setRuleId(null);
        request.setRemark("");
        request.setCustomerClassification(null);
        request.setStartOpTime(null);
        request.setEndOpTime(null);

        Page<UnlockCustomerClassRuleDTO> result = unlockCustomerClassRuleApi.listPage(request);
        System.out.println(JSONUtil.toJsonStr(result));
    }

    @Test
    public void addTest() {
        SaveOrUpdateUnlockCustomerClassRuleRequest request = new SaveOrUpdateUnlockCustomerClassRuleRequest();
        request.setOrderNo(10L);
        request.setCondition1(1);   // 条件1 1-客户名称 2-客户名称结尾
        request.setOperator1(1);     // 运算符1 1-包含 2-不包含 3-等于
        request.setConditionValue1("医药物流");

        request.setCondition2(2);   // 条件1 1-客户名称 2-客户名称结尾
        request.setOperator2(3);     // 运算符1 1-包含 2-不包含 3-等于
        request.setConditionValue2("有限公司");
        request.setConditionRelation(1);

        request.setCustomerClassification(1);
        request.setStatus(1);
        request.setRemark("");

        unlockCustomerClassRuleApi.add(request);
    }

    @Test
    public void updateTest() {
        SaveOrUpdateUnlockCustomerClassRuleRequest request = new SaveOrUpdateUnlockCustomerClassRuleRequest();
        request.setId(2L);
        request.setOrderNo(20L);
        request.setCondition1(1);   // 条件1 1-客户名称 2-客户名称结尾
        request.setOperator1(1);     // 运算符1 1-包含 2-不包含 3-等于
        request.setConditionValue1("医药物流");

        request.setCondition2(2);   // 条件1 1-客户名称 2-客户名称结尾
        request.setOperator2(3);     // 运算符1 1-包含 2-不包含 3-等于
        request.setConditionValue2("分公司");
        request.setConditionRelation(1);

        request.setCustomerClassification(2);
        request.setStatus(1);
        request.setRemark("");
        unlockCustomerClassRuleApi.update(request);
    }

    @Test
    public void detail() {
        Long id = 1L;
        UnlockCustomerClassRuleDTO result = unlockCustomerClassRuleApi.getById(id);
        System.out.println(JSONUtil.toJsonStr(result));
    }
}
