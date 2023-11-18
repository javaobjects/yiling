package com.yiling.dataflow.wash;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.BaseTest;
import com.yiling.dataflow.crm.bo.CrmEnterpriseIdAndNameBO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.service.CrmEnterpriseService;
import com.yiling.dataflow.wash.service.UnlockCustomerMatchingInfoService;

import cn.hutool.json.JSONUtil;

/**
 * @author fucheng.bai
 * @date 2023/5/25
 */
public class UnlockCustomerMatchingInfoTest extends BaseTest {

    @Autowired
    UnlockCustomerMatchingInfoService unlockCustomerMatchingInfoService;

    @Autowired
    CrmEnterpriseService crmEnterpriseService;


    @Test
    public void matchingRateExecuteTest() {
        String name = "武汉市蔡甸区蔡甸街华利村卫生室";
        unlockCustomerMatchingInfoService.matchingRateExecute(name);
    }

    @Test
    public void matchingRateBatchExecuteTest() {
        //  批量执行匹配度任务
        unlockCustomerMatchingInfoService.matchingRateBatchExecute();
    }

    @Test
    public void getIdAndNameListPageTest() {
        Page<CrmEnterpriseIdAndNameBO> result1 =  crmEnterpriseService.getIdAndNameListPage(1, 2000);
        Page<CrmEnterpriseIdAndNameBO> result2 =  crmEnterpriseService.getIdAndNameListPage(2, 2000);
        Page<CrmEnterpriseIdAndNameBO> result3 =  crmEnterpriseService.getIdAndNameListPage(3, 2000);
        Page<CrmEnterpriseIdAndNameBO> result4 =  crmEnterpriseService.getIdAndNameListPage(4, 2000);
        Page<CrmEnterpriseIdAndNameBO> result5 =  crmEnterpriseService.getIdAndNameListPage(5, 2000);
        Page<CrmEnterpriseIdAndNameBO> result6 =  crmEnterpriseService.getIdAndNameListPage(6, 2000);
        Page<CrmEnterpriseIdAndNameBO> result7 =  crmEnterpriseService.getIdAndNameListPage(7, 2000);

        Page<CrmEnterpriseIdAndNameBO> result11 =  crmEnterpriseService.getIdAndNameListPage(1, 2000);
        System.out.println(JSONUtil.toJsonStr(result11.getRecords()));
    }
}
