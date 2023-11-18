package com.yiling.dataflow.crm;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.BaseTest;
import com.yiling.dataflow.backup.dto.request.AgencyBackRequest;
import com.yiling.dataflow.crm.api.impl.CrmEnterpriseRelationPinchRunnerApiImpl;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseSimpleDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmEnterprisePageRequest;
import com.yiling.dataflow.crm.service.CrmEnterpriseService;

import cn.hutool.core.collection.ListUtil;

/**
 * @author fucheng.bai
 * @date 2022/11/2
 */

public class CrmEnterpriseTest extends BaseTest {

    @Autowired
    private CrmEnterpriseService crmEnterpriseService;
    @Autowired
    private CrmEnterpriseRelationPinchRunnerApiImpl crmEnterpriseRelationPinchRunnerService;

    @Test
    public void getCrmEnterpriseCodeByNameTest() {
        CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseService.getCrmEnterpriseCodeByName("上海金高中西医结合医院",false);
        System.out.println("crmEnterpriseDO = " + crmEnterpriseDTO);
    }

    @Test
    public void getCrmEnterpriseByLicenseNumberTest() {
        CrmEnterpriseDTO crmEnterpriseDO = crmEnterpriseService.getCrmEnterpriseByLicenseNumber("91350200MA3287U59C",false);
        System.out.println("crmEnterpriseDO = " + crmEnterpriseDO);
    }

    @Test
    public void test1(){
        QueryCrmEnterprisePageRequest request =new QueryCrmEnterprisePageRequest();
        request.setBusinessCode(1);
        Page<CrmEnterpriseSimpleDTO> crmEnterpriseSimplePage = crmEnterpriseService.getCrmEnterpriseSimplePage(request);
        System.out.println(JSON.toJSONString(crmEnterpriseSimplePage));
        //        CrmEnterpriseDO crmEnterpriseDO=new CrmEnterpriseDO();
//        crmEnterpriseDO.setName("测试");
//        crmEnterpriseDO.setDelFlag(1);
//        crmEnterpriseService.save(crmEnterpriseDO);
    }

    @Test
    public void relationShipPinchRunnerBackupTest(){
        List<Long> orgIds = ListUtil.toList(12294L,
                12296L,
                10515L,
                12297L,
                11026L,
                9792L,
                12930L,
                13591L,
                10328L,
                13798L,
                13799L,
                12298L,
                10410L);
        AgencyBackRequest agencyRequest=new AgencyBackRequest();
        agencyRequest.setOffsetMonth(-1);
        crmEnterpriseRelationPinchRunnerService.relationShipPinchRunnerBackup(agencyRequest, orgIds);
    }

}
